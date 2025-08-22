package com.matridx.las.frame.connect.channel.service;

import com.matridx.las.frame.connect.channel.command.FrameModel;
import com.matridx.las.frame.connect.channel.command.IObserver;
import com.matridx.las.frame.connect.svcinterface.IHttpService;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * 用于默认的本地调用的DLL
 */
public class DefaultService extends BaseService implements IHttpService{

	//初始化时获取可以连接的设备
	public DefaultService(){
		A();
	}
	//数据库
	public void A(){
		System.out.println("123");
	}

	@Override
	public boolean sendMessage(FrameModel frameModel,String info) {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public Object getChannel() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean init(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return false;
	}


}
