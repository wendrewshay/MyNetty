package com.xia.netty.other.codec;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

/**
 * jdk序列化和通用二进制编码性能测试
 * 测试结果：The jdk serializable cost time is : 1918 ms
			------------------------------
			The byte array serializable cost time is : 161 ms
 * 可见java的原生序列化性能差强人意。
 * 
 * #业界主流的编码框架推荐：Google的Protobuf，Facebook的Thrift#
 * 
 * @Package: com.xia.netty.other.serial 
 * @author: xiawq   
 * @date: 2017年8月1日 下午11:11:03
 */
public class PerformTestUserInfo {

	public static void main(String[] args) throws IOException {
		UserInfo info = new UserInfo();
		info.buildUserID(100).buildUserName("Welcome to netty");
		
		int loop = 1000000;
		ByteArrayOutputStream bos = null;
		ObjectOutputStream os = null;
		
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < loop; i++) {
			bos = new ByteArrayOutputStream();
			os = new ObjectOutputStream(bos);
			os.writeObject(info);
			os.flush();
			os.close();
			byte[] b = bos.toByteArray();
			bos.close();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("The jdk serializable cost time is : " + (endTime-startTime) + " ms");
		
		System.out.println("------------------------------");
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		startTime = System.currentTimeMillis();
		for (int i = 0; i < loop; i++) {
			byte[] b = info.codeC(buffer);
		}
		endTime = System.currentTimeMillis();
		System.out.println("The byte array serializable cost time is : " + (endTime-startTime) + " ms");
	}
}
