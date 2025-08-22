package com.matridx.las.netty.channel.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.channel.domain.protocol.CubicsProtocol;
import com.matridx.las.netty.receivehandleimpl.CubicsReceiveHandleImpl;

import io.netty.channel.ChannelHandler.Sharable;

@Service
@Sharable
public class CubicsProtocolHandler extends MatridxProtocolHandler<CubicsProtocol> {
	@Autowired
	private CubicsReceiveHandleImpl cubicsReceiveHandleImpl;
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " Cubics:"
				+ incoming.remoteAddress() + "在线");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " Cubics:"
				+ incoming.remoteAddress() + "掉线");
	}

	@Override
	protected String firstRecCmd(FrameModel recModel) {
		return "";

	}

	@Override
	protected String secondRecCmd(FrameModel recModel) {
		// 根据命令判断下一步操作
		return "";
	}

	@Override
	protected String excuteCmd(FrameModel recModel) {
		boolean result = cubicsReceiveHandleImpl.receiveHandle(recModel);
		if(result) {
			return "Success";
		}
		return "Fail";
	}

}
