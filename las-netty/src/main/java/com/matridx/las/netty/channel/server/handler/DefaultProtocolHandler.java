package com.matridx.las.netty.channel.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Service;
import io.netty.channel.ChannelHandler.Sharable;
import com.matridx.las.netty.channel.domain.protocol.DefaultProtocol;
@Service
@Sharable
public class DefaultProtocolHandler extends BaseProtocolHandler<DefaultProtocol> {

	public ChannelGroup socketChannels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DefaultProtocol msg) throws Exception {
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 接收消息的处理器：" + this.getClass().getName());
		System.out.println("消息内容：" + msg.getInfo());
		Channel incoming = ctx.channel();
		//收到消息后，处理消息
		//incoming.writeAndFlush(new DefaultProtocol(msg.getInfo()));
		
        for (Channel channel : socketChannels) {
            if (channel != incoming){
                channel.writeAndFlush(new DefaultProtocol(msg.getInfo()));
            } else {
                channel.writeAndFlush(new DefaultProtocol("[you] "+msg.getInfo()));
            }
        }
	}

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " Default:"+incoming.remoteAddress()+"在线");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " Default:"+incoming.remoteAddress()+"掉线");
    }
	
}
