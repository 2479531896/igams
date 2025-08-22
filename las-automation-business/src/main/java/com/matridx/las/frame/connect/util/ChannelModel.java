package com.matridx.las.frame.connect.util;

import java.net.Socket;

import com.matridx.las.frame.connect.enums.ChannelStatusEnum;
import com.matridx.springboot.util.base.StringUtil;
import lombok.Data;
@Data
public class ChannelModel {
	
	private String protocol;
	private String address;
	private String deviceId;
	private String commanddeviceid;
	private Socket socket;
	private String status;
	//区域
	private String area;
	//是否桥接
	private boolean bridgingflg;
	//错误次数
	private int errorCount=0;
	
	public String getStatus() {
		if (StringUtil.isBlank(this.status)) {
			this.status = ChannelStatusEnum.FREE.getCode();
		}
		return status;
	}
}
