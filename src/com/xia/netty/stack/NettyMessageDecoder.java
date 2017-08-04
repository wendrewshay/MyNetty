package com.xia.netty.stack;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**   
 * @ClassName: NettyMessageDecoder   
 * @Description: 用于消息的解码     
 * @author: XiaWenQiang
 * @date: 2017年8月4日 下午6:05:37   
 *      
 */
public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {

	NettyMarshallingDecoder marshallingDecoder;
	
	/**   
	 * @Title: NettyMessageDecoder   
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 *   
	 * @param maxFrameLength
	 * @param lengthFieldOffset
	 * @param lengthFieldLength  
	 * 
	 */ 
	public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
		this.marshallingDecoder = MarshallingCodecFactory.buildMarshallingDecoder();
	}

}
