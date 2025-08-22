package com.matridx.las.netty.channel.server.handler;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.channel.domain.protocol.CommonProtocol;
import com.matridx.las.netty.util.CommonChannelUtil;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler.Sharable;

/**
 * 处理握手类(仪器上线/下线)
 */
@Service
@Sharable
public class CommonProtocolHandler extends MatridxProtocolHandler<CommonProtocol> {
	
	private Logger log = LoggerFactory.getLogger(CommonProtocolHandler.class);
	
	@Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        boolean result =  CommonChannelUtil.addChannel(incoming);
		InetSocketAddress ipSocket = (InetSocketAddress)incoming.remoteAddress();
		String clientIp = ipSocket.getAddress().getHostAddress();
		log.info("客户端ip地址：{}",clientIp);
        if(result) {
        	log.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " CommonProtocolHandler:"+incoming.remoteAddress() +" 加入");
        }else{
        	log.error(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " CommonProtocolHandler:"+incoming.remoteAddress() +" 重复");
        }
    }
	
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        CommonChannelUtil.removeChannel(incoming);
        log.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "CommonProtocolHandler add:"+incoming.remoteAddress() +" 移除");
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
