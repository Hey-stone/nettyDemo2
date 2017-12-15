package com.fengyajun.nettyDemo2UI.client;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @Title: NettyClientDemo2.java
 * @Package com.fengyajun.nettyDemo2.client
 * @author 冯亚军
 * @date 2017年12月14日上午10:44:21
 * @Description: TODO(用一句话描述该文件做什么)
 * @version V1.0
 */
public class NettyClientDemo3 {
	
	private final static Logger logger = LoggerFactory.getLogger(NettyClientDemo3.class);

	private String clientName;
	public static final int BUFFER_SIZE = 1024;
	
	private Bootstrap bootstrap = null;
	private Channel socketChannel = null;

	public NettyClientDemo3(String clientName) {
		this.clientName = clientName;
		
	}

	public void run() throws Exception {

		EventLoopGroup group = new NioEventLoopGroup();

		bootstrap = new Bootstrap();
		bootstrap.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
				.handler(new SimpleChatClientInitializer());

	}

	public int connServer(String IP, Integer port) {
		// Start the client.
		ChannelFuture chanFuture;
		try {
			chanFuture = bootstrap.connect(IP, port).sync();
			socketChannel = chanFuture.channel();
			// Wait until the connection is closed.
			logger.info("==客户端已连接");
			System.out.println("connect");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return 1;
	}

	public void sendMsg(String msg) {
		String newMessage;
		newMessage = clientName + ": " + msg + "\r\n";
		logger.info("Going to Send: {}.",newMessage);
		System.out.println("Going to Send: " + newMessage);
		socketChannel.writeAndFlush(newMessage);
		
	}


	

}
