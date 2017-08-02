/**  
 *  
 * @Title: MsgpackDecoder.java   
 * @Package com.xia.netty.codec.msgpack   
 * @Description: TODO(用一句话描述该文件做什么)   
 * @author: XiaWenQiang  
 * @date: 2017年8月2日 上午9:24:34   
 * @version V1.0     
 */
package com.xia.netty.codec.msgpack;

import java.util.List;

import org.msgpack.MessagePack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

/**   
 * @ClassName: MsgpackDecoder   
 * @Description: MessagePack解码器---反序列化   
 * @author: XiaWenQiang
 * @date: 2017年8月2日 上午9:24:34   
 *      
 */
public class MsgpackDecoder extends MessageToMessageDecoder<ByteBuf> {

	/**   
	 * @Title: decode  
	 * @Description:   
	 * @param ctx
	 * @param msg
	 * @param out
	 * @throws Exception   
	 * @see io.netty.handler.codec.MessageToMessageDecoder#decode(io.netty.channel.ChannelHandlerContext, java.lang.Object, java.util.List)   
	 */
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		final byte[] array;
		final int length = msg.readableBytes();
		
		array = new byte[length];
		msg.getBytes(msg.readerIndex(), array, 0, length);
		
		MessagePack msgpack = new MessagePack();
		out.add(msgpack.read(array));
	}

}
