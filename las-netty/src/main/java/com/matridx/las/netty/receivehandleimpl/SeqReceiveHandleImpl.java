package com.matridx.las.netty.receivehandleimpl;

import org.springframework.stereotype.Service;

import com.matridx.las.netty.channel.command.FrameModel;

@Service
public class SeqReceiveHandleImpl extends BaseReceiveHandleImpl{
	
	public boolean receiveHandle(FrameModel recModel) {
		log.info(String.format("测序仪接收消息，命令为%s,设备id%s",recModel.getCmd(),recModel.getDeviceID()));
		return true;
	}
}
