package com.xia.netty.stack.heartbeat;

import java.util.concurrent.TimeUnit;

import com.xia.netty.stack.Header;
import com.xia.netty.stack.MessageType;
import com.xia.netty.stack.NettyMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.ScheduledFuture;

/**
 * 客户端心跳检测机制
 * 由于心跳消息的目的是为了检测链路的可用性，因为不需要携带消息体
 * 
 * @Package: com.xia.netty.stack.heartbeat 
 * @author: xiawq   
 * @date: 2017年8月5日 下午11:04:35
 */
public class HeartBeatReqHandler extends ChannelInboundHandlerAdapter{

	private volatile ScheduledFuture<?> heartBeat;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		NettyMessage message = (NettyMessage) msg;
		
		// 握手成功启动无限循环定时器用于定期主动发送心跳消息
		if(message.getHeader() != null 
				&& message.getHeader().getType() == MessageType.LOGIN_RESP.getType()) {
			heartBeat = ctx.executor().scheduleAtFixedRate(new HeartBeatTask(ctx), 0, 5000, TimeUnit.MILLISECONDS);
		
		// 接收服务端发送的心跳应答消息并打印
		}else if(message.getHeader() != null 
				&& message.getHeader().getType() == MessageType.HEARTBEAT_RESP.getType()) {
			System.out.println("Client receive server heart beat message : ---> " + message);
		
		}else{
			ctx.fireChannelRead(msg);
		}
	}
	
	private class HeartBeatTask implements Runnable {

		private final ChannelHandlerContext ctx;
		
		public HeartBeatTask(final ChannelHandlerContext ctx) {
			this.ctx = ctx;
		}
		
		@Override
		public void run() {
			NettyMessage heartBeat = buildHeatBeat();
			System.out.println("Client send heart beat message to server : " + heartBeat);
			ctx.writeAndFlush(heartBeat);
		}
		
	}
	
	private NettyMessage buildHeatBeat() {
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setType(MessageType.HEARTBEAT_REQ.getType());
		message.setHeader(header);
		return message;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if(heartBeat != null) {
			heartBeat.cancel(true);
			heartBeat = null;
		}
		ctx.fireExceptionCaught(cause);
	}
	
}
