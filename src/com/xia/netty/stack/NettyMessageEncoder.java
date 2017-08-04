package com.xia.netty.stack;

import java.util.List;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

/**   
 * @ClassName: NettyMessageEncoder   
 * @Description: 用于消息的编码   
 * @author: XiaWenQiang
 * @date: 2017年8月4日 下午4:57:52   
 *      
 */
public class NettyMessageEncoder extends MessageToMessageEncoder<NettyMessage> {

	NettyMarshallingEncoder marshallingEncoder;
	
	public NettyMessageEncoder() {
		this.marshallingEncoder = MarshallingCodecFactory.buildMarshallingEncoder();
	}

	/**   
	 * @Title: encode  
	 * @Description:   
	 * @param ctx
	 * @param msg
	 * @param out
	 * @throws Exception   
	 * @see io.netty.handler.codec.MessageToMessageEncoder#encode(io.netty.channel.ChannelHandlerContext, java.lang.Object, java.util.List)   
	 */
	@Override
	protected void encode(ChannelHandlerContext ctx, NettyMessage msg, List<Object> out) throws Exception {
		if(msg == null || msg.getHeader() == null) {
			throw new Exception("The encode message is null");
		}
		ByteBuf sendBuf = Unpooled.buffer();
		sendBuf.writeInt(msg.getHeader().getCrcCode());
		sendBuf.writeInt(msg.getHeader().getLength());
		sendBuf.writeLong(msg.getHeader().getSessionID());
		sendBuf.writeByte(msg.getHeader().getType());
		sendBuf.writeByte(msg.getHeader().getPriority());
		sendBuf.writeInt(msg.getHeader().getAttachment().size());
		
		String key = null;
		byte[] keyArray = null;
		Object value = null;
		for (Map.Entry<String, Object> param : msg.getHeader().getAttachment().entrySet()) {
			key = param.getKey();
			keyArray = key.getBytes("UTF-8");
			
			sendBuf.writeInt(keyArray.length);
			sendBuf.writeBytes(keyArray);
			
			value = param.getValue();
			marshallingEncoder.encode(ctx, value, sendBuf);
		}
		key = null;
		keyArray = null;
		value = null;
		
		if(msg.getBody() != null) {
			marshallingEncoder.encode(ctx, msg.getBody(), sendBuf);
		}else{
			sendBuf.writeInt(0);
			sendBuf.setInt(4, sendBuf.readableBytes());
		}
	}

}
