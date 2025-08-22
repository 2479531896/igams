package com.matridx.las.frame.connect.channel.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.matridx.las.frame.connect.channel.service.PrintService;
import com.matridx.las.frame.connect.channel.service.SmapService;
import com.matridx.las.frame.netty.channel.domain.protocol.CommonProtocol;
import com.matridx.las.frame.netty.channel.domain.protocol.CubicsProtocol;
import com.matridx.las.frame.netty.channel.domain.protocol.DefaultProtocol;
import com.matridx.las.frame.netty.channel.domain.protocol.PcrProtocol;

public class PacketClazzMap {

	public final static Map<Byte, Class<? extends Object>> packetTypeMap = new ConcurrentHashMap<>();

	static {
		packetTypeMap.put(Command.REGISTER, CommonProtocol.class);
		packetTypeMap.put(Command.DEFAULT, DefaultProtocol.class);
		packetTypeMap.put(Command.CUBICS, CubicsProtocol.class);
		packetTypeMap.put(Command.PCR, PcrProtocol.class);
		packetTypeMap.put(Command.PRINT, PrintService.class);
		packetTypeMap.put(Command.SMAP, SmapService.class);
		packetTypeMap.put(Command.SEQ, SmapService.class);

	}

}
