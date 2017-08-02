package com.xia.netty.codec.protobuf;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**   
 * @ClassName: SubClient   
 * @Description: protobuf编解码客户端演示   
 * @author: XiaWenQiang
 * @date: 2017年8月2日 下午2:34:11   
 *      
 */
public class SubReqClient {

	public void connect(int port, String host) throws Exception {
		// Configure the client
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group)
				.channel(NioSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
						ch.pipeline().addLast(new ProtobufDecoder(SubscribeRespProto.SubscribeResp.getDefaultInstance()));
						ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
						ch.pipeline().addLast(new ProtobufEncoder());
						ch.pipeline().addLast(new SubReqClientHandler());
					}
				});
			
			// 异步连接操作
			ChannelFuture f = b.connect(host, port).sync();
			
			System.out.println("前 " + System.currentTimeMillis());
			f.addListener(new ChannelFutureListener() {
				
				@Override
				public void operationComplete(ChannelFuture arg0) throws Exception {
					System.out.println("已连接 " + System.currentTimeMillis());
				}
			});
			System.out.println("后 " + System.currentTimeMillis());
			
			// 等待客户端链路关闭
			f.channel().closeFuture().sync();
		} finally {
			// 优雅退出，释放NIO线程组
			group.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws Exception {
		int port = 8090;
		new SubReqClient().connect(port, "127.0.0.1");;
	}
}
