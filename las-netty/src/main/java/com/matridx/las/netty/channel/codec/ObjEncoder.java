package com.matridx.las.netty.channel.codec;

import com.matridx.las.netty.channel.domain.Packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ObjEncoder extends MessageToByteEncoder<Packet> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Packet in, ByteBuf out) {
//		byte[] data = SerializationUtil.serialize(in);
		String msg = in.getInfo();
		byte[] data = msg.getBytes();	
		out.writeInt(data.length + 1);
		out.writeByte(in.getCommand()); //添加指令
		out.writeByte(64); //添加分隔符
		out.writeBytes(data);
	}

}
