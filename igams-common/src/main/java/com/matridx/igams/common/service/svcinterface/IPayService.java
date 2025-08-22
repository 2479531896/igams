package com.matridx.igams.common.service.svcinterface;


import java.util.Map;

public interface IPayService {
	/**
	 * 接收rabbit信息，查找数据执行方法
	 */

	 boolean paySuccess(String str, Map<String, String> map);
}
