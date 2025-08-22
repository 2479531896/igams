package com.matridx.las.netty.channel.domain.protocol;

import com.matridx.las.netty.channel.domain.Command;
import com.matridx.las.netty.channel.domain.Packet;

public class AvgProtocol extends Packet {

	private String info;

	public AvgProtocol(String info) {
		this.setInfo(info);
	}
	
	@Override
	public Byte getCommand() {
		return Command.AGV;
	}

	@Override
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}
