package com.xia.netty.codec.msgpack;

import org.msgpack.MessagePack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**   
 * @ClassName: MsgpackEncoder   
 * @Description: msgpack编码器---序列化
 *    特点：MessagePack是一个高效的二进制序列化框架，多语言支持，性能高，序列化后码流小。
 * @author: XiaWenQiang
 * @date: 2017年8月2日 上午9:18:40   
 *      
 */
public class MsgpackEncoder extends MessageToByteEncoder<Object>{

	/**   
	 * @Title: encode  
	 * @Description:   
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @throws Exception   
	 * @see io.netty.handler.codec.MessageToByteEncoder#encode(io.netty.channel.ChannelHandlerContext, java.lang.Object, io.netty.buffer.ByteBuf)   
	 */
	@Override
	protected void encode(ChannelHandlerContext arg0, Object arg1, ByteBuf arg2) throws Exception {
		MessagePack msgpack = new MessagePack();
		// Serialize
		byte[] raw = msgpack.write(arg1);
		arg2.writeBytes(raw);
	}
	
}
