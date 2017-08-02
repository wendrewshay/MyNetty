package com.xia.netty.codec.msgpack;

import com.xia.netty.codec.base.UserInfo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**   
 * @ClassName: EchoClientHandler   
 * @Description: TODO(这里用一句话描述这个类的作用)   
 * @author: XiaWenQiang
 * @date: 2017年8月2日 上午9:35:51   
 *      
 */
public class EchoClientHandler extends SimpleChannelInboundHandler<Object>{

	private final int sendNumber;
	public EchoClientHandler(int sendNumber) {
		this.sendNumber = sendNumber;
	}
	
	/**   
	 * @Title: channelRead0  
	 * @Description:   
	 * @param ctx
	 * @param msg
	 * @throws Exception   
	 * @see io.netty.channel.SimpleChannelInboundHandler#channelRead0(io.netty.channel.ChannelHandlerContext, java.lang.Object)   
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("Client receive the msgpack message : " + msg);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		UserInfo[] infos = userInfo();
		for (UserInfo infoE : infos) {
			// UserInfo类上必须有@Message注解
			ctx.writeAndFlush(infoE);
		}
	}
	
	private UserInfo[] userInfo() {
		UserInfo[] userInfos = new UserInfo[sendNumber];
		UserInfo userInfo = null;
		for (int i = 0; i < sendNumber; i++) {
			userInfo = new UserInfo();
			userInfo.setUserID(i);
			userInfo.setUserName("ABCDEFG --->" + i);
			userInfos[i] = userInfo;
		}
		return userInfos;
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

}
