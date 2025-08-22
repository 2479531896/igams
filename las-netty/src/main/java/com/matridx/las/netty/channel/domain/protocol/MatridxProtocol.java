package com.matridx.las.netty.channel.domain.protocol;

import com.matridx.las.netty.channel.domain.Command;
import com.matridx.las.netty.channel.domain.Packet;

public class MatridxProtocol extends Packet {

	private String channelId;
	private String info;

	public MatridxProtocol(String channelId, String info) {
		this.channelId = channelId;
		this.setInfo(info);
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	@Override
	public Byte getCommand() {
		return Command.AUTO;
	}

	@Override
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}
