package com.xia.netty.echo;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**   
 * @ClassName: EchoClientHandler   
 * @Description: TODO(这里用一句话描述这个类的作用)   
 * @author: XiaWenQiang
 * @date: 2017年8月1日 下午5:16:05   
 *      
 */
public class EchoClientHandler extends SimpleChannelInboundHandler<Object> {

	private int counter;
	
	static final String ECHO_REQ = "Hi, XWQ. Welcome to Netty.$_";
	
	public EchoClientHandler() {
		
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
		for (int i = 0; i < 10; i++) {
			ctx.writeAndFlush(Unpooled.copiedBuffer(ECHO_REQ.getBytes()));
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("This is " + ++counter + " times receive server : [" + msg + "]" );
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}
