package com.matridx.las.frame.netty.channel.domain.protocol;

import com.matridx.las.frame.connect.channel.domain.Command;
import com.matridx.las.frame.connect.channel.domain.Packet;

public class MatridxProtocol extends Packet {

	private String channelId;

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
		return Command.DEFAULT;
	}

}
