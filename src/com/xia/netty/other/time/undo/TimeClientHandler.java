package com.xia.netty.other.time.undo;

import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**   
 * @ClassName: TimeClientHandler   
 * @Description: TCP粘包/拆包解决方案 客户端演示
 * @author: XiaWenQiang
 * @date: 2017年8月1日 下午2:00:24   
 *      
 */
public class TimeClientHandler extends SimpleChannelInboundHandler<Object>{

	private static final Logger logger = Logger.getLogger(TimeClientHandler.class.getName());
	
	private int counter;
	
	private byte[] req;
	
	public TimeClientHandler() {
		req = ("QUERY TIME ORDER" + System.getProperty("line.separator")).getBytes();
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
		System.out.println(msg);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf = (ByteBuf) msg;
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		
		String body = new String(req, "utf-8");
		
		System.out.println("Now is : " + body 
				+ " ; the counter is : " + ++ counter);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ByteBuf message = null;
		for (int i = 0; i < 100; i++) {
			message = Unpooled.buffer(req.length);
			message.writeBytes(req);
			ctx.writeAndFlush(message);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// 释放资源
		logger.warning("Unexpected exception from downstream : "
				+ cause.getMessage());
		ctx.close();
	}

}
