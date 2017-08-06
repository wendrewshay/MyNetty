package com.xia.netty.stack.login;

import com.xia.netty.stack.Header;
import com.xia.netty.stack.MessageType;
import com.xia.netty.stack.NettyMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 握手认证客户端
 * 
 * @Package: com.xia.netty.stack 
 * @author: xiawq   
 * @date: 2017年8月5日 下午10:31:49
 */
public class LoginAuthReqHandler extends SimpleChannelInboundHandler<Object> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		NettyMessage message = (NettyMessage) msg;
		
		// 如果是握手应答消息，需要判断是否认证成功
		if(message.getHeader() != null 
				&& message.getHeader().getType() == MessageType.LOGIN_RESP.getType()) {
			byte loginResult = (Byte) message.getBody();
			if(loginResult != (byte)0) {
				// 握手失败，关闭连接
				ctx.close();
			}else{
				System.out.println("Login is ok : " + message);
				ctx.fireChannelRead(msg);
			}
		}else{
			ctx.fireChannelRead(msg);
		}
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// 客户端和服务端TCP三次握手成功之后，由客户端构造握手请求消息发送给服务端，
		// 由于采用IP白名单认证机制，因此，不需要携带消息体，消息体为空，消息类型为"3 : 握手请求消息"。
		// 握手请求发送之后，按照协议规范，服务端需要返回握手应答消息。
		ctx.writeAndFlush(buildLoginReq());
	}
	
	private NettyMessage buildLoginReq() {
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setType(MessageType.LOGIN_REQ.getType());
		message.setHeader(header);
		return message;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.fireExceptionCaught(cause);
	}
	
}
