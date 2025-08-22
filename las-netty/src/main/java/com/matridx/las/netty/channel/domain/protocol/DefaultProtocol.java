package com.matridx.las.netty.channel.domain.protocol;

import com.matridx.las.netty.channel.domain.Command;
import com.matridx.las.netty.channel.domain.Packet;

public class DefaultProtocol extends Packet {

	private String info;

	public DefaultProtocol(String info) {
		this.setInfo(info);
	}
	
	@Override
	public Byte getCommand() {
		return Command.DEFAULT;
	}

	@Override
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}
