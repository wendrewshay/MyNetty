package com.xia.netty.codec.base;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * jdk序列化机制和通用二进制编码测试
 * 对比结果：The jdk serializable length is : 120
			-------------------------
			The byte array serializable length is : 24
 * 在同等情况下，编码后的字节数组越大，存储的时候就越占空间，存储的硬件
 * 成本就越高，并且在网络传输时更占带宽，导致系统的吞吐量降低。
 * 所以jdk序列化机制编码并不被推荐。
 * 
 * @Package: com.xia.netty.other.serial 
 * @author: xiawq   
 * @date: 2017年8月1日 下午10:50:20
 */
public class TestUserInfo {

	public static void main(String[] args) throws IOException {
		UserInfo info = new UserInfo();
		info.buildUserID(100).buildUserName("Welcome to netty");
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(bos);
		os.writeObject(info);
		os.flush();
		os.close();
		
		byte[] b = bos.toByteArray();
		System.out.println("The jdk serializable length is : " + b.length);
		bos.close();
		
		System.out.println("-------------------------");
		System.out.println("The byte array serializable length is : " + info.codeC().length);
	}

}
