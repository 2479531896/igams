package com.matridx.las.netty.channel.domain.protocol;

import com.matridx.las.netty.channel.domain.Command;
import com.matridx.las.netty.channel.domain.Packet;

public class CubicsProtocol extends Packet {

	private String info;

	public CubicsProtocol(String info) {
		this.setInfo(info);
	}
	
	@Override
	public Byte getCommand() {
		return Command.CUBICS;
	}

	@Override
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}
