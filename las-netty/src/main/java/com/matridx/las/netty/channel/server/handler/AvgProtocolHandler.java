package com.matridx.las.netty.channel.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.channel.domain.protocol.AvgProtocol;
import com.matridx.las.netty.receivehandleimpl.AvgReceiveHandleImpl;

import io.netty.channel.ChannelHandler.Sharable;

@Service
@Sharable
public class AvgProtocolHandler extends MatridxProtocolHandler<AvgProtocol> {
	@Autowired
	private AvgReceiveHandleImpl avgReceiveHandleImpl;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " "+incoming.remoteAddress()+"在线");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " AVG:"+incoming.remoteAddress()+"掉线");
    }

	@Override
	protected String firstRecCmd(FrameModel recModel) {
		System.err.println("第一帧，机器人");
		return "";
	}

	@Override
	protected String secondRecCmd(FrameModel recModel) {
		
				return "";
	}

	@Override
	protected String excuteCmd(FrameModel recModel) {
		boolean result =  avgReceiveHandleImpl.receiveHandle(recModel);
		if(result) {
			return "Success";
		}
		return "Fail";
	}
	
}
