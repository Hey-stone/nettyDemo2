package com.fengyajun.nettyDemo2UI.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @Title: SimpleChatServerHandler.java
 * @Package com.fengyajun.nettyDemo2.server
 * @author 冯亚军
 * @date 2017年12月14日上午10:28:56
 * @Description: TODO(用一句话描述该文件做什么)
 * @version V1.0
 */
public class SimpleChatServerHandler extends SimpleChannelInboundHandler<String> {
	
	private final static Logger logger = LoggerFactory.getLogger(SimpleChatServerHandler.class);

	public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		for (Channel channel : channels) {
			channel.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 加入\n");
		}
		channels.add(ctx.channel());
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		for (Channel channel : channels) {
			channel.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 离开\n");
		}
		channels.remove(ctx.channel());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		super.channelRead(ctx, msg);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		logger.info("SimpleChatClient:{} Online",incoming.remoteAddress());
		System.out.println("SimpleChatClient:" + incoming.remoteAddress() + "Online");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		logger.info("SimpleChatClient:{} offline",incoming.remoteAddress());
		System.out.println("SimpleChatClient:" + incoming.remoteAddress() + "offline");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		Channel incoming = ctx.channel();
		logger.info("SimpleChatClient:{} Exception",incoming.remoteAddress());
		System.out.println("SimpleChatClient:" + incoming.remoteAddress() + "Exception");
		// 当出现异常就关闭连接
		//cause.printStackTrace();
		ctx.close();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		Channel incoming = ctx.channel();
		for (Channel channel : channels) {
			if (channel != incoming) {
				channel.writeAndFlush("[" + incoming.remoteAddress() + "] " + msg + "\n");
			} else {
				channel.writeAndFlush("[you] " + msg + "\n");
			}
		}
	}

}
