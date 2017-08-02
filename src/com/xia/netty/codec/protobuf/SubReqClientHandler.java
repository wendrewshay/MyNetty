package com.xia.netty.codec.protobuf;

import java.util.ArrayList;
import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**   
 * @ClassName: SubReqClientHandler   
 * @Description: TODO(这里用一句话描述这个类的作用)   
 * @author: XiaWenQiang
 * @date: 2017年8月2日 下午2:37:29   
 *      
 */
public class SubReqClientHandler extends SimpleChannelInboundHandler<Object>{

	static int counter = 0;
	
	public SubReqClientHandler() {
		
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
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		for (int i = 0; i < 100; i++) {
			ctx.write(subReq(i));
		}
		ctx.flush();
	}
	
	private SubscribeReqProto.SubscribeReq subReq(int i) {
		SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
		builder.setSubReqID(i);
		builder.setUserName("xiawq");
		builder.setProductName("Netty Book For Protobuf");
		List<String> address = new ArrayList<>();
		address.add("Nanjing");
		address.add("Hefei");
		address.add("Beijing");
		address.add("Hangzhou");
		builder.addAllAddress(address);
		return builder.build();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println(++counter + " Time : " + System.currentTimeMillis() +" -Receive server response : [" + msg + "]");
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}
