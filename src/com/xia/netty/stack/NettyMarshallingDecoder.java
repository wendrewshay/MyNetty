/**  
 *  
 * @Title: NettyMarshallingDecoder.java   
 * @Package com.xia.netty.stack   
 * @Description: TODO(用一句话描述该文件做什么)   
 * @author: XiaWenQiang  
 * @date: 2017年8月4日 下午5:53:55   
 * @version V1.0     
 */
package com.xia.netty.stack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

/**   
 * @ClassName: NettyMarshallingDecoder   
 * @Description: TODO(这里用一句话描述这个类的作用)   
 * @author: XiaWenQiang
 * @date: 2017年8月4日 下午5:53:55   
 *      
 */
public class NettyMarshallingDecoder extends MarshallingDecoder {

	/**   
	 * @Title: NettyMarshallingDecoder   
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 *   
	 * @param provider  
	 * 
	 */ 
	public NettyMarshallingDecoder(UnmarshallerProvider provider) {
		super(provider);
		// TODO Auto-generated constructor stub
	}

	public NettyMarshallingDecoder(UnmarshallerProvider provider, int maxObjectSize) {
		super(provider, maxObjectSize);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		// TODO Auto-generated method stub
		return super.decode(ctx, in);
	}

}
