package com.matridx.las.netty.channel.domain.protocol;

import com.matridx.las.netty.channel.domain.Command;
import com.matridx.las.netty.channel.domain.Packet;

public class CmhProtocol extends Packet {

	private String info;

	public CmhProtocol(String info) {
		this.setInfo(info);
	}

	@Override
	public Byte getCommand() {
		return Command.CMH;
	}

	@Override
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}
