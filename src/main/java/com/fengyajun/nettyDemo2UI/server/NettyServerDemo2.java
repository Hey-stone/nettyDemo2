package com.fengyajun.nettyDemo2UI.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Title: NettyServerDemo2.java
 * @Package com.fengyajun.nettyDemo2.server
 * @author 冯亚军
 * @date 2017年12月14日上午10:17:10
 * @Description: TODO(用一句话描述该文件做什么)
 * @version V1.0
 */
public class NettyServerDemo2 {
	
	private final static Logger logger = LoggerFactory.getLogger(NettyServerDemo2.class);

	private int port;

	public NettyServerDemo2(int port) {
		this.port = port;
	}

	public void run() throws Exception {

		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {

			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.childHandler(new SimpleChatServerInitializer()).option(ChannelOption.SO_BACKLOG, 1024)
					.childOption(ChannelOption.SO_KEEPALIVE, true);

			logger.info("SimpleChatServer 启动了");
			
			System.out.println("SimpleChatServer 启动了");

			// 绑定端口，开始接收进来的连接
			ChannelFuture f = bootstrap.bind(port).sync();

			// 等待服务器 socket 关闭 。
			// 在这个例子中，这不会发生，但你可以优雅地关闭你的服务器。
			f.channel().closeFuture().sync();

		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
			
			logger.info("SimpleChatServer 关闭了");

			System.out.println("SimpleChatServer 关闭了");
		}

	}
	
	public static void main(String[] args) throws Exception {
	
        int port = 8080; //可以通过args指定
        new NettyServerDemo2(port).run();

    }

}
