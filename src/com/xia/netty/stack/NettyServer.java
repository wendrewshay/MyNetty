package com.xia.netty.stack;

import com.xia.netty.stack.codec.NettyMessageDecoder;
import com.xia.netty.stack.codec.NettyMessageEncoder;
import com.xia.netty.stack.heartbeat.HeartBeatRespHandler;
import com.xia.netty.stack.login.LoginAuthRespHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * 服务端：主要工作就是握手的接入认证等，不用关心断连重连等事件。
 * 
 * @Package: com.xia.netty.stack 
 * @author: xiawq   
 * @date: 2017年8月6日 上午12:01:23
 */
public class NettyServer {
	
	public void bind() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		ServerBootstrap b = new ServerBootstrap();
		
		try {
			b.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 100) // backlog指定了内核为此套接口排队的最大连接个数
				.handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(new ChannelInitializer<SocketChannel>() {
	
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new NettyMessageDecoder(1024*1024, 4, 4, -8, 0));
						ch.pipeline().addLast("MessageEncoder", new NettyMessageEncoder());
						//读超时handler
						ch.pipeline().addLast("ReadTimeoutHandler", new ReadTimeoutHandler(50));
						ch.pipeline().addLast("LoginAuthHandler", new LoginAuthRespHandler());
						ch.pipeline().addLast("HeartBeatHandler", new HeartBeatRespHandler());
						
					}
				});
				
			// 绑定端口，同步等待成功
			ChannelFuture future = b.bind(NettyConstant.REMOTE_IP, NettyConstant.PORT).sync();
			System.out.println("Netty server start ok : " + (NettyConstant.REMOTE_IP + ":" + NettyConstant.PORT));
			future.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		new NettyServer().bind();
	}
}
