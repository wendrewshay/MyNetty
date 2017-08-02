package com.xia.netty.codec.msgpack;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**   
 * @ClassName: EchoServer   
 * @Description: messagegPack编解码测试服务端
 * @author: XiaWenQiang
 * @date: 2017年8月1日 下午4:50:12   
 *      
 */
public class EchoServer {

	public void bind(int port) throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workGroup)
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 100)
				.handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						// MsgpackDecoder解码器之前，LengthFieldBasedFrameDecoder将在bytebuf之前减少两个字节的消息长度，用于处理半包消息
						ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2)); 
						ch.pipeline().addLast("Message Decoder", new MsgpackDecoder());
						// MsgpackEncoder编码器之前，LengthFieldPrepender将在bytebuf之前增加两个字节的消息长度，用于处理粘包消息
						ch.pipeline().addLast(new LengthFieldPrepender(2));                         
						ch.pipeline().addLast("Message Encoder", new MsgpackEncoder());
						ch.pipeline().addLast(new EchoServerHandler());
						
					}
				});
			
			// 绑定端口，同步等待成功
			ChannelFuture f = b.bind(port).sync();
			
			// 等待服务器监听端口关闭
			f.channel().closeFuture().sync();
		} finally {
			// 优雅退出，释放线程池资源
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws Exception {
		int port = 8090;
		new EchoServer().bind(port);
	}
}
