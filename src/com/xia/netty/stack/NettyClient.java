package com.xia.netty.stack;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.xia.netty.stack.codec.NettyMessageDecoder;
import com.xia.netty.stack.codec.NettyMessageEncoder;
import com.xia.netty.stack.heartbeat.HeartBeatReqHandler;
import com.xia.netty.stack.login.LoginAuthReqHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * 客户端主要用于初始化系统资源，根据配置信息发起连接
 * 
 * 利用Netty的ChannelPipeline和ChannelHandler机制，可以非常方便地实现功能解耦
 * 和业务产品的定制。如本例中的心跳定时器，握手请求和后端的业务处理可以通过不同
 * 的Handler来实现，类似于AOP。通过Handler Chain的机制可以方便地实现切面拦截和
 * 定制，相比于AOP它的性能更高。
 * 
 * @Package: com.xia.netty.stack 
 * @author: xiawq   
 * @date: 2017年8月5日 下午11:31:55
 */
public class NettyClient {

	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	EventLoopGroup group = new NioEventLoopGroup();
	
	public void connect(int port, String host) throws Exception {
		// 配置客户端NIO线程组
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						// 加了NettyMessageDecoder用于Netty消息解码，为了防止由于单条消息过大
						// 导致的内存溢出或者畸形码流导致解码错位引起内存分配失败，我们对
						// 单条消息最大长度进行了上限限制
						ch.pipeline().addLast(new NettyMessageDecoder(1024*1024, 4, 4, -8, 0));
						ch.pipeline().addLast("MessageEncoder", new NettyMessageEncoder());
						//读超时handler
						ch.pipeline().addLast("ReadTimeoutHandler", new ReadTimeoutHandler(50));
						ch.pipeline().addLast("LoginAuthHandler", new LoginAuthReqHandler());
						ch.pipeline().addLast("HeartBeatHandler", new HeartBeatReqHandler());
					}
				});
			
			// 发起异步连接操作
			// 这次我们绑定了本地端口，主要用于服务端重复登录保护，另外
			// 从产品管理角度看，一般情况下不允许系统随便使用随机端口。
			ChannelFuture future = b.connect(
					new InetSocketAddress(host, port),
					new InetSocketAddress(NettyConstant.LOCAL_IP, NettyConstant.LOCAL_PORT)).sync();
			future.channel().closeFuture().sync();
		
		} finally {
			// 所有资源释放完成之后，清空资源，再次发起重连操作
			executor.execute(new Runnable() {

				@Override
				public void run() {
					try {
						TimeUnit.SECONDS.sleep(5);
						try {
							connect(NettyConstant.PORT, NettyConstant.REMOTE_IP);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
				
			});
		}
	}
	
	public static void main(String[] args) throws Exception {
		new NettyClient().connect(NettyConstant.PORT, NettyConstant.REMOTE_IP);
	}
}
