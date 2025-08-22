package com.matridx.las.frame.connect.channel.service;

import com.matridx.las.frame.connect.channel.command.FrameModel;
import com.matridx.las.frame.connect.svcinterface.IHttpService;

import com.matridx.las.frame.connect.util.ConnectUtil;
import com.matridx.springboot.util.base.StringUtil;
import io.netty.channel.Channel;

import java.util.Map;

/**
 * 用于Netty的通道类
 */
public class NettyChannelService extends BaseService implements IHttpService{

	//Netty 通道
	private Channel channel;
	
	/**
	 * 发送消息
	 */
	@Override
	public boolean sendMessage(FrameModel frameModel,String info) {
		// TODO Auto-generated method stub
		if(channel != null)
		{
			channel.writeAndFlush(info);
			return true;
		}
		return false;
	}

	public Channel getChannel() {
		return channel;
	}

	@Override
	public boolean init(Map<String, Object> map) {
		String ztqrdz=String.valueOf(map.get("ztqrdz"));
		if(StringUtil.isNotObjectBank(map.get("confirmflg"))&&Boolean.parseBoolean(String.valueOf(map.get("confirmflg"))))
			return ConnectUtil.confirmConnect(ztqrdz);
		return true;
	}


	public void setChannel(Channel channel) {
		this.channel = channel;
	}

}
