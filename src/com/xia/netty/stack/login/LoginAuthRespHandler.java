package com.xia.netty.stack.login;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.xia.netty.stack.Header;
import com.xia.netty.stack.MessageType;
import com.xia.netty.stack.NettyMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 握手认证服务端
 * 
 * @Package: com.xia.netty.stack 
 * @author: xiawq   
 * @date: 2017年8月5日 下午10:33:29
 */
public class LoginAuthRespHandler extends SimpleChannelInboundHandler<Object>{

	// 重复登录保护
	private Map<String, Boolean> nodeCheck = new ConcurrentHashMap<>();
	// IP认证白名单列表
	private String[] whiteList = {"127.0.0.1"};
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		NettyMessage message = (NettyMessage) msg;
		
		// 如果是握手请求消息，处理，其他消息透传
		if(message.getHeader() != null
				 && message.getHeader().getType() == MessageType.LOGIN_REQ.getType()) {
			String nodeIndex = ctx.channel().remoteAddress().toString();
			NettyMessage loginResp = null;
			
			//重复登录，拒绝
			if(nodeCheck.containsKey(nodeIndex)) {
				loginResp = buildResponse((byte)-1);
			}else{
				// 获取发送方的源地址信息，通过源地址进行白名单校验，
				// 校验通过握手成功，否则握手失败。
				InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
				String ip = address.getAddress().getHostAddress();
				boolean isOK = false;
				for (String WIP : whiteList) {
					if(WIP.equals(ip)) {
						isOK =true;
						break;
					}
				}
				
				loginResp = isOK ? buildResponse((byte)0) : buildResponse((byte)-1);
				if(isOK) {
					nodeCheck.put(nodeIndex, true);
				}
				
				System.out.println("The login response is : " + loginResp 
						+ " body [" + loginResp.getBody() + "]");
				ctx.writeAndFlush(loginResp);
			}
		}else{
			ctx.fireChannelRead(msg);
		}
	}
	
	// 构造握手应答消息返回给客户端
	private NettyMessage buildResponse(byte result) {
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setType(MessageType.LOGIN_RESP.getType());
		message.setHeader(header);
		message.setBody(result);
		return message;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// 发生异常关闭链路的时候，需要将客户端的信息从登录注册表中去掉，
		// 以保证后续客户端可以重连成功 。
		nodeCheck.remove(ctx.channel().remoteAddress().toString()); // 删除缓存
		ctx.close();
		ctx.fireExceptionCaught(cause);
	}

}
