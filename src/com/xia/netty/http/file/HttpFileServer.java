package com.xia.netty.http.file;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * 文件目录服务器实现
 * 
 * @Package: com.xia.netty.http.file 
 * @author: xiawq   
 * @date: 2017年8月2日 下午7:57:35
 */
public class HttpFileServer {

	private static final String DEFAULT_URL = "/src/com/xia/netty/";
	
	public void run(final String host, final int port, final String url) throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel arg0) throws Exception {
						arg0.pipeline().addLast("http-decoder", new HttpRequestDecoder());
						//HttpObjectAggregator解码器将多个消息转换为单一的FullHttpRequest或者FullHttpResponse，
						//原因是HTTP解码器在每个HTTP消息中会生成多个消息对象
						arg0.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));
						arg0.pipeline().addLast("http-encoder", new HttpResponseEncoder());
						//ChunkedWriteHandler支持异步发送大的码流，但不占用过多的内存，防止java内存溢出错误
						arg0.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
						//HttpFileServerHandler用于文件服务器的业务逻辑处理
						arg0.pipeline().addLast("fileServerHandler", new HttpFileServerHandler());
					}
				});
			ChannelFuture f = b.bind(host, port).sync();
			System.out.println("HTTP 文件目录服务器启动，网址是："
					+ host + ":" + port + url);
			f.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws Exception {
		int port = 8090;
		new HttpFileServer().run("127.0.0.1", port, DEFAULT_URL);
	}
}
