package com.xia.netty.stack;

import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import io.netty.handler.codec.marshalling.DefaultUnmarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

/**   
 * @ClassName: MarshallingCodecFactory   
 * @Description: 序列化编解码工厂类   
 * @author: XiaWenQiang
 * @date: 2017年8月4日 下午5:08:47   
 *      
 */
public class MarshallingCodecFactory {

	 public static NettyMarshallingDecoder buildMarshallingDecoder() {    
        MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");    
        MarshallingConfiguration configuration = new MarshallingConfiguration();    
        configuration.setVersion(5);    
        UnmarshallerProvider provider = new DefaultUnmarshallerProvider(marshallerFactory, configuration);    
        NettyMarshallingDecoder decoder = new NettyMarshallingDecoder(provider, 10240);    
        return decoder;    
    }    
        
    public static NettyMarshallingEncoder buildMarshallingEncoder() {    
        MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");    
        MarshallingConfiguration configuration = new MarshallingConfiguration();    
        configuration.setVersion(5);    
        MarshallerProvider provider = new DefaultMarshallerProvider(marshallerFactory, configuration);    
        NettyMarshallingEncoder encoder = new NettyMarshallingEncoder(provider);    
        return encoder;    
    }    
}
