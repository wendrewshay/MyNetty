/**  
 *  
 * @Title: TimeServerHandler.java   
 * @Package com.xia.netty.time   
 * @Description: TODO(用一句话描述该文件做什么)   
 * @author: XiaWenQiang  
 * @date: 2017年8月1日 上午11:34:26   
 * @version V1.0     
 */
package com.xia.netty.time;

import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**   
 * @ClassName: TimeServerHandler   
 * @Description: TODO(这里用一句话描述这个类的作用)   
 * @author: XiaWenQiang
 * @date: 2017年8月1日 上午11:34:26   
 *      
 */
public class TimeServerHandler extends SimpleChannelInboundHandler<Object> {

	private int counter;
	
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
		System.out.println(msg);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf = (ByteBuf) msg;
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		
		String body = new String(req, "utf-8");
		System.out.println("The time server receive order : " + body);
		
		String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
		
		ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
		ctx.write(resp);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}


	
}
