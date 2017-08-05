package com.xia.netty.stack.heartbeat;

import com.xia.netty.stack.Header;
import com.xia.netty.stack.MessageType;
import com.xia.netty.stack.NettyMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 服务端的心跳应答
 * 
 * @Package: com.xia.netty.stack.heartbeat 
 * @author: xiawq   
 * @date: 2017年8月5日 下午11:23:10
 */
public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		NettyMessage message = (NettyMessage) msg;
		
		// 返回心跳应答消息
		if(message.getHeader() != null 
				&& message.getHeader().getType() == MessageType.HEARTBEAT_REQ.getType()) {
			System.out.println("Receive client heart beat message : ---> " + message);
			NettyMessage heartBeat = buildHeartBeat();
			System.out.println("Send heart beat response message to client : ---> " + heartBeat);
			ctx.writeAndFlush(heartBeat);
		}else{
			ctx.fireChannelRead(msg);
		}
	}

	private NettyMessage buildHeartBeat() {
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setType(MessageType.HEARTBEAT_RESP.getType());
		message.setHeader(header);
		return message;
	}
}
