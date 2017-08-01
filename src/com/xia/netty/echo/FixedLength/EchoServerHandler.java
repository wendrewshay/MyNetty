package com.xia.netty.echo.FixedLength;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**   
 * @ClassName: EchoServerHandler   
 * @Description: TODO(这里用一句话描述这个类的作用)   
 * @author: XiaWenQiang
 * @date: 2017年8月1日 下午4:51:39   
 *      
 */
@Sharable
public class EchoServerHandler extends SimpleChannelInboundHandler<Object> {

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
		System.out.println("Receive client : [" + msg + "]");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close(); // 发生异常，关闭链路
	}

}
