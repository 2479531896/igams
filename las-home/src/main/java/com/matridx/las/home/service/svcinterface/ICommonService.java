package com.matridx.las.home.service.svcinterface;

import java.util.Map;

public interface ICommonService {

	/**
	 * 测试发送消息
	 */
	Map<String, Object> testMessage(String type);
	Map<String, Object> closeNettyMessage(String type);

}
