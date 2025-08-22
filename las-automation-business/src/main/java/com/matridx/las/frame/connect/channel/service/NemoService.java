package com.matridx.las.frame.connect.channel.service;

import com.matridx.las.frame.connect.channel.command.FrameModel;
import com.matridx.las.frame.connect.svcinterface.IHttpService;
import com.matridx.las.frame.connect.util.ConnectUtil;
import com.matridx.springboot.util.base.StringUtil;

import java.util.Map;

public class NemoService extends BaseService implements IHttpService {
	@Override
	public boolean sendMessage(FrameModel frameModel,String info) {
		return false;
	}

	@Override
	public Object getChannel() {
		return null;
	}

	@Override
	public boolean init(Map<String, Object> map) {
		String ztqrdz=String.valueOf(map.get("ztqrdz"));
		if(StringUtil.isNotObjectBank(map.get("confirmflg"))&&Boolean.parseBoolean(String.valueOf(map.get("confirmflg"))))
			return ConnectUtil.confirmConnect(ztqrdz);
		return true;
	}
}
