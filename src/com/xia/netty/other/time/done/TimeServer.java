package com.xia.netty.other.time.done;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**   
 * @ClassName: TimeServer   
 * @Description: 时间服务器   
 * @author: XiaWenQiang
 * @date: 2017年8月1日 上午10:57:09   
 *      
 */
public class TimeServer {

	public void bind(int port) throws Exception {
		//配置服务端的NIO线程组
		NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		NioEventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workGroup)
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 1024)
				.childHandler(new ChildChannelHandler());
			
			// 绑定端口，同步等待成功
			ChannelFuture f = b.bind(port).sync();
			
			// 等待服务端监听端口关闭
			f.channel().closeFuture().sync();
		} finally {
			// 优雅退出，释放线程池资源
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
	
	private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

		/**   
		 * @Title: initChannel  
		 * @Description:   
		 * @param arg0
		 * @throws Exception   
		 * @see io.netty.channel.ChannelInitializer#initChannel(io.netty.channel.Channel)   
		 */
		@Override
		protected void initChannel(SocketChannel arg0) throws Exception {
			//此两行半包解码handler加入channelPipeline可解决tcp粘包/拆包问题
			arg0.pipeline().addLast(new LineBasedFrameDecoder(1024));
			arg0.pipeline().addLast(new StringDecoder());
			
			arg0.pipeline().addLast(new TimeServerHandler());	
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		int port = 8080;
		new TimeServer().bind(port);
	}
}
