package com.matridx.las.netty.channel.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.channel.domain.protocol.PcrProtocol;
import com.matridx.las.netty.error.ExceptionHandlingUtil;
import com.matridx.las.netty.receivehandleimpl.PcrReceiveHandleImpl;

import io.netty.channel.ChannelHandler.Sharable;

@Service
@Sharable
public class PcrProtocolHandler extends MatridxProtocolHandler<PcrProtocol> {
	@Autowired
	private PcrReceiveHandleImpl  pcrReceiveHandleImpl;
	@Value("${resultNotification.pcrUrlDate:}")
	public  String pcrUrlDate;
	@Value("${resultNotification.wkUrlDate:}")
	public  String wkUrlDate;
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " Pcr:"
				+ incoming.remoteAddress() + "在线");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " Pcr:"
				+ incoming.remoteAddress() + "掉线");
	}

	@Override
	protected String firstRecCmd(FrameModel recModel) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	protected String secondRecCmd(FrameModel recModel) {
		return "";
	}

	@Override
	protected String excuteCmd(FrameModel recModel)  {
		try {
			boolean result = pcrReceiveHandleImpl.receiveHandle(recModel);
			if(result) {
				return "Success";
			}
		} catch (ExceptionHandlingUtil e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Fail";
	}

}
