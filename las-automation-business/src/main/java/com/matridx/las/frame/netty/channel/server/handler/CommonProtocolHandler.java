package com.matridx.las.frame.netty.channel.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

import com.matridx.las.frame.connect.channel.command.FrameModel;
import com.matridx.las.frame.netty.channel.domain.protocol.CommonProtocol;
import io.netty.channel.ChannelHandler.Sharable;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 处理握手类(仪器上线/下线)
 */
@Service
@Sharable
public class CommonProtocolHandler extends MatridxProtocolHandler<CommonProtocol> {
	
	//private Logger log = LoggerFactory.getLogger(CommonProtocolHandler.class);
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " common:"
				+ incoming.remoteAddress() + "在线");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " common:"
				+ incoming.remoteAddress() + "掉线");
	}
	@Override
	protected String firstRecCmd(FrameModel recModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String secondRecCmd(FrameModel recModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String excuteCmd(FrameModel recModel) {
		// TODO Auto-generated method stub
		return null;
	}
}
