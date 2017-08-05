package com.xia.netty.stack.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingEncoder;

/**   
 * @ClassName: NettyMarshallingEncoder   
 * @Description: 重载JBOSS编码类   
 * @author: XiaWenQiang
 * @date: 2017年8月4日 下午5:52:34   
 *      
 */
public class NettyMarshallingEncoder extends MarshallingEncoder {

	/**   
	 * @Title: NettyMarshallingEncoder   
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 *   
	 * @param provider  
	 * 
	 */ 
	public NettyMarshallingEncoder(MarshallerProvider provider) {
		super(provider);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
		super.encode(ctx, msg, out);
	}

}
