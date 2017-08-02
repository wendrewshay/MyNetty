package com.xia.netty.codec.protobuf;

import com.xia.netty.codec.protobuf.SubscribeReqProto.SubscribeReq;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**   
 * @ClassName: SubReqServerHandler   
 * @Description: TODO(这里用一句话描述这个类的作用)   
 * @author: XiaWenQiang
 * @date: 2017年8月2日 下午2:23:00   
 *      
 */
@Sharable
public class SubReqServerHandler extends SimpleChannelInboundHandler<Object> {

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
		SubscribeReqProto.SubscribeReq req = (SubscribeReq) msg;
		if("xiawq".equalsIgnoreCase(req.getUserName())) {
			System.out.println("Server accept client subscribe req : " + req.toString());
			ctx.writeAndFlush(resp(req.getSubReqID()));
		}
	}
	
	private SubscribeRespProto.SubscribeResp resp(int subReqID) {
		SubscribeRespProto.SubscribeResp.Builder builder = SubscribeRespProto.SubscribeResp.newBuilder();
		builder.setSubReqID(subReqID);
		builder.setRespCode(0);
		builder.setDesc("Netty book order succeed, 3 days later, sent to the designated address");
		return builder.build();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close(); // 发生异常，关闭链路
	}

}
