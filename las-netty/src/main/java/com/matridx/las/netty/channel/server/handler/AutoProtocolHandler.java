package com.matridx.las.netty.channel.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.channel.domain.protocol.AutoProtocol;
import com.matridx.las.netty.receivehandleimpl.AutoReceiveHandleImpl;

import io.netty.channel.ChannelHandler.Sharable;

@Service
@Sharable
public class AutoProtocolHandler extends MatridxProtocolHandler<AutoProtocol> {
	@Autowired 
	private AutoReceiveHandleImpl autoReceiveHandleImpl;
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " Auto:"
				+ incoming.remoteAddress() + "在线");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " Auto:"
				+ incoming.remoteAddress() + "掉线");
	}

	@Override
	protected String firstRecCmd(FrameModel recModel) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	protected String secondRecCmd(FrameModel recModel) {
		System.err.println("第二帧，机器人");
		// 根据命令判断下一步操作

		return "";
	}

	@Override
	protected String excuteCmd(FrameModel recModel) {
		boolean result = autoReceiveHandleImpl.receiveHandle(recModel);
		if(result) {
			return "Success";
		}
		return "Fail";
	}

}
