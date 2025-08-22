package com.matridx.las.frame.netty.channel.domain.protocol;

import com.matridx.las.frame.connect.channel.domain.Command;
import com.matridx.las.frame.connect.channel.domain.Packet;

public class CubicsProtocol extends Packet {


	public CubicsProtocol(String info) {
		this.setInfo(info);
	}
	
	@Override
	public Byte getCommand() {
		return Command.CUBICS;
	}

}
