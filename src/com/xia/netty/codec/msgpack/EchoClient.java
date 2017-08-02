package com.xia.netty.codec.msgpack;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**   
 * @ClassName: EchoClient   
 * @Description: messagegPack编解码测试服务端
 * @author: XiaWenQiang
 * @date: 2017年8月2日 上午9:33:16   
 *      
 */
public class EchoClient {

	private final String host;
	private final int port;
	private final int sendNumber;
	
	public EchoClient(String host, int port, int sendNumber) {
		this.host = host;
		this.port = port;
		this.sendNumber = sendNumber;
	}
	
	public void run() throws Exception {
		// Configure the client
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group)
				.channel(NioSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, true)
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
				.handler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						// MsgpackDecoder解码器之前，LengthFieldBasedFrameDecoder将在bytebuf之前减少两个字节的消息长度，用于处理半包消息
						ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2)); 
						ch.pipeline().addLast("Message Decoder", new MsgpackDecoder());
						// MsgpackEncoder编码器之前，LengthFieldPrepender将在bytebuf之前增加两个字节的消息长度，用于处理粘包消息
						ch.pipeline().addLast(new LengthFieldPrepender(2));                         
						ch.pipeline().addLast("Message Encoder", new MsgpackEncoder());
						ch.pipeline().addLast(new EchoClientHandler(sendNumber));
					}
				});
			
			// 异步连接操作
			ChannelFuture f = b.connect(host, port).sync();
			
			// 等待客户端链路关闭
			f.channel().closeFuture().sync();
		} finally {
			// 优雅退出，释放NIO线程组
			group.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws Exception {
		int port = 8090;
		new EchoClient("127.0.0.1", port, 100).run();
	}
}
