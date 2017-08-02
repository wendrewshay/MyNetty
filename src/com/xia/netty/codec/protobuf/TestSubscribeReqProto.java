package com.xia.netty.codec.protobuf;

import java.util.ArrayList;
import java.util.List;

import com.google.protobuf.InvalidProtocolBufferException;
import com.xia.netty.codec.protobuf.SubscribeReqProto.SubscribeReq;
import com.xia.netty.codec.protobuf.SubscribeReqProto.SubscribeReq.Builder;

/**   
 * @ClassName: TestSubscribeReqProto   
 * @Description: protobuf编解码测试   
 * @author: XiaWenQiang
 * @date: 2017年8月2日 下午1:42:45   
 *      
 */
public class TestSubscribeReqProto {

	//编码
	private static byte[] encode(SubscribeReqProto.SubscribeReq req) {
		return req.toByteArray();
	}
	
	//解码
	private static SubscribeReqProto.SubscribeReq decode(byte[] body)
			throws InvalidProtocolBufferException {
		return SubscribeReqProto.SubscribeReq.parseFrom(body);
	}
	
	private static SubscribeReqProto.SubscribeReq createSubscribeReq() {
		// 创建构建器实例
		Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
		
		// 通过构建器设置属性
		builder.setSubReqID(1);
		builder.setUserName("xiawq");
		builder.setProductName("Netty Book");
		List<String> address = new ArrayList<>();
		address.add("Nanjing");
		address.add("Hefei");
		address.add("Beijing");
		address.add("Hangzhou");
		builder.addAllAddress(address);
		return builder.build();
	}
	
	public static void main(String[] args) throws InvalidProtocolBufferException {
		SubscribeReq req = createSubscribeReq();
		System.out.println("Before encode : " + req.toString());
		
		SubscribeReq req2 = decode(encode(req));
		System.out.println("After decode : " + req2.toString());
		
		System.out.println("Assert equal : --> " + req2.equals(req));
	}
}
