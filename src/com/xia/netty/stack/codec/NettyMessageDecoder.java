package com.xia.netty.stack.codec;

import java.util.HashMap;
import java.util.Map;

import com.xia.netty.stack.Header;
import com.xia.netty.stack.NettyMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**   
 * @ClassName: NettyMessageDecoder   
 * @Description: 用于消息的解码  
 * 这里用到LengthFieldBasedFrameDecoder解码器，支持TCP粘包和半包处理，
 * 只需要给出标识消息长度的字段偏移量和消息长度自身所占的字节数。   
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
	
	public NettyMessageDecoder( int maxFrameLength,
            int lengthFieldOffset, int lengthFieldLength,
            int lengthAdjustment, int initialBytesToStrip) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
		this.marshallingDecoder = MarshallingCodecFactory.buildMarshallingDecoder();
	}
	
	

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		// 这里调用父类LengthFieldBasedFrameDecoder的解码方法后，返回的是整包消息或者为空。
		// 如果为空则说明是个半包消息，直接返回继续由I/O线程读取后续的码流。
		ByteBuf frame = (ByteBuf) super.decode(ctx, in);
		if(frame == null) {
			return null;
		}
		
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setCrcCode(frame.readInt());
		header.setLength(frame.readInt());
		header.setSessionID(frame.readLong());
		header.setType(frame.readByte());
		header.setPriority(frame.readByte());
		
		int size = frame.readInt();
		if(size > 0) {
			Map<String, Object> attach = new HashMap<String, Object>(size);
			int keySize = 0;
			byte[] keyArray = null;
			String key = null;
			for (int i = 0; i < size; i++) {
				keySize = frame.readInt();
				keyArray = new byte[keySize];
				frame.readBytes(keyArray);
				key = new String(keyArray, "UTF-8");
				attach.put(key, marshallingDecoder.decode(ctx, frame));
			}
			keyArray = null;
			key = null;
			header.setAttachment(attach);
		}
		if(frame.readableBytes() > 4) {
			message.setBody(marshallingDecoder.decode(ctx, frame));
		}
		message.setHeader(header);
		return message;
	}
	
}
