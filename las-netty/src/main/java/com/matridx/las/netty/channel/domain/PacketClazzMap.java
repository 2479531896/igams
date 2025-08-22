package com.matridx.las.netty.channel.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.matridx.las.netty.channel.domain.protocol.AutoProtocol;
import com.matridx.las.netty.channel.domain.protocol.AvgProtocol;
import com.matridx.las.netty.channel.domain.protocol.CmhProtocol;
import com.matridx.las.netty.channel.domain.protocol.CommonProtocol;
import com.matridx.las.netty.channel.domain.protocol.CubicsProtocol;
import com.matridx.las.netty.channel.domain.protocol.DefaultProtocol;
import com.matridx.las.netty.channel.domain.protocol.PcrProtocol;
import com.matridx.las.netty.channel.domain.protocol.SeqProtocol;
import com.matridx.las.netty.channel.server.handler.AvgProtocolHandler;

public class PacketClazzMap {

	public final static Map<Byte, Class<? extends Packet>> packetTypeMap = new ConcurrentHashMap<>();

	static {
		packetTypeMap.put(Command.REGISTER, CommonProtocol.class);
		packetTypeMap.put(Command.DEFAULT, DefaultProtocol.class);
		packetTypeMap.put(Command.AUTO, AutoProtocol.class);
		packetTypeMap.put(Command.CUBICS, CubicsProtocol.class);
		packetTypeMap.put(Command.PCR, PcrProtocol.class);
		packetTypeMap.put(Command.SEQ, SeqProtocol.class);
		packetTypeMap.put(Command.AGV, AvgProtocol.class);
		packetTypeMap.put(Command.CMH, CmhProtocol.class);

	}

}
