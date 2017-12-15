package com.fengyajun.nettyDemo2UI.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Title: SimpleChatClientHandler.java 
 * @Package com.fengyajun.nettyDemo2.client  
 * @author 冯亚军
 * @date 2017年12月14日上午10:55:15
 * @Description: TODO(用一句话描述该文件做什么)
 * @version V1.0   
 */
public class SimpleChatClientHandler extends SimpleChannelInboundHandler<String> {
	
	private final static Logger logger = LoggerFactory.getLogger(SimpleChatClientHandler.class);

	private static List<ClientObserver> observerList = new ArrayList<ClientObserver>();
	
	public static final int HISTORY_LIST_SIZE = 30;
	private static List<String> historyMessage = new ArrayList<String>();
	
	public static void register(ClientObserver observer) {
		observerList.add(observer);
	}

	public void notifyObserver() {
		Iterator<ClientObserver> iter = observerList.iterator();
		while (iter.hasNext()) {
			iter.next().update();// 获得各Observer
		}
	}
	
	public static String getMessages() {
		String messages;
		Iterator<String> iter = historyMessage.iterator();
		messages = "";
		while (iter.hasNext()) {
			messages += iter.next();
			messages += "\r\n";
		}
		return messages;
	}
	
	public void updateMsg(String msg) {
		if (historyMessage.size() > HISTORY_LIST_SIZE)
			historyMessage.remove(0);
		historyMessage.add(msg);
		notifyObserver();
	}
	
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		
		logger.info("服务端发送过来的: {}.",msg);
		System.out.println(msg);
		
		updateMsg(msg);
		
	}

}
