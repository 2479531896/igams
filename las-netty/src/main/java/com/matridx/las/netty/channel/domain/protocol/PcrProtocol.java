package com.matridx.las.netty.channel.domain.protocol;

import com.matridx.las.netty.channel.domain.Command;
import com.matridx.las.netty.channel.domain.Packet;

public class PcrProtocol extends Packet {

	private String info;

	public PcrProtocol(String info) {
		this.setInfo(info);
	}

	@Override
	public Byte getCommand() {
		return Command.PCR;
	}

	@Override
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}
