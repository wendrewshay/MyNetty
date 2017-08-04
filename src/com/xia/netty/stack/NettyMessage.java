package com.xia.netty.stack;

/**   
 * @ClassName: NettyMessage   
 * @Description: 协议栈使用的数据结构 : 
 * 	心跳消息、握手请求和握手应答消息都可以由此类承载
 * @author: XiaWenQiang
 * @date: 2017年8月4日 下午4:49:10   
 *      
 */
public final class NettyMessage {

	private Header header; // 消息头
	private Object body; // 消息体
	public Header getHeader() {
		return header;
	}
	public void setHeader(Header header) {
		this.header = header;
	}
	public Object getBody() {
		return body;
	}
	public void setBody(Object body) {
		this.body = body;
	}
	@Override
	public String toString() {
		return "NettyMessage [header"+header+"]";
	}
	
}
