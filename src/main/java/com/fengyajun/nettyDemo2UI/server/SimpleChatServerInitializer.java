package com.fengyajun.nettyDemo2UI.server;

import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @Title: SimpleChatServerInitializer.java 
 * @Package com.fengyajun.nettyDemo2.server  
 * @author 冯亚军
 * @date 2017年12月14日上午10:25:28
 * @Description: TODO(用一句话描述该文件做什么)
 * @version V1.0   
 */
public class SimpleChatServerInitializer extends ChannelInitializer<SocketChannel>{
	
	private final static Logger logger = LoggerFactory.getLogger(SimpleChatServerInitializer.class);

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		
		ChannelPipeline pipeline = ch.pipeline();
		
		pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast("decoder", new StringDecoder(Charset.forName("UTF-8")));
        pipeline.addLast("encoder", new StringEncoder(Charset.forName("UTF-8")));
        pipeline.addLast("handler", new SimpleChatServerHandler());

        logger.info("SimpleChatClient: {} 连接上",ch.remoteAddress());
        System.out.println("SimpleChatClient:"+ch.remoteAddress() +"连接上");
		
	}

}
