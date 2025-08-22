package com.matridx.las.netty.channel.server.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

@Component
public class LasChatServerHandler extends BaseProtocolHandler<String> {

	//private final List<String> exceptList;
	
	Logger log = LoggerFactory.getLogger(LasChatServerHandler.class);

	public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	/*
	public LasChatServerHandler(List<String> exceptList) {
        this.exceptList = exceptList;
    }*/
	
	/**
	 * 客户端连接的时候优先处理处理，然后才会调用 channelActive
	 */
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		Channel inConnect = ctx.channel();
		for (Channel channel : channels) {
            channel.writeAndFlush("[SERVER] - " + inConnect.remoteAddress() + " 加入\n");
        }
		channels.add(inConnect);
		log.info("Channel Added");
	}

	/**
	 * 客户端连接会触发
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception{
		super.channelActive(ctx);
		log.info("Channel Active");
	}

	/**
	 * 客户端发消息会触发
	 */
	@Override
	public void channelRead0(ChannelHandlerContext ctx, String msg) {
		
		log.info("服务器收到消息: {}", msg.toString());
		//业务处理
		Channel incoming = ctx.channel();
		for (Channel channel : channels) {
            if (channel != incoming){
                channel.writeAndFlush("[" + incoming.remoteAddress() + "]" + msg + "\n");
            } else {
                channel.writeAndFlush("[you]" + msg + "\n");
            }
        }
	}
	
	

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		Channel incoming = ctx.channel();
		log.info("SimpleChatClient:" + incoming.remoteAddress() + "掉线");
	}

	/**
	 * 客户端退出是调用
	 */
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception { // (3)
		Channel incoming = ctx.channel();
		channels.remove(incoming);
		log.info("SimpleChatClient:" + incoming.remoteAddress() + "退出");
	}

	/**
	 * 发生异常触发
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
		log.info("发送异常");
	}

}
