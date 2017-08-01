package com.xia.netty.echo.FixedLength;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**   
 * @ClassName: EchoServer   
 * @Description: FixedLengthFrameDecoder服务端开发 
 *  使用FixedLengthFrameDecoder按照构造函数中设置的固定长度进行解码。
 *  如果是半包消息，FixedLengthFrameDecoder会缓存半包消息并等待下个包到达后进行拼包。
 *  客户端也是如此。
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
						ch.pipeline().addLast(new FixedLengthFrameDecoder(20));
						ch.pipeline().addLast(new StringDecoder());
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
