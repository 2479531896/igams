package com.matridx.las.netty.channel.command;

import com.matridx.las.netty.dao.entities.SjlcDto;

public class ICallBackImpl implements ICallBack{
	/**
     * 回调处理
     * @param times 返回次数：1 第一次  2 第二次
     * @param recModel 返回的帧信息
     * @param sendModel 发送的帧信息
     * @return
     */
    public boolean callfunc(int times,FrameModel recModel, FrameModel sendModel, SjlcDto sjlcDto) {
    	
    	return true;
    }



	
}
