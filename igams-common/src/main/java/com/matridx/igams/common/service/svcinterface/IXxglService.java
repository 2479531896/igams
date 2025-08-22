package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.dao.entities.XxglDto;
import com.matridx.igams.common.dao.entities.XxglModel;
import com.matridx.igams.common.service.BaseBasicService;
import java.util.Map;

public interface IXxglService extends BaseBasicService<XxglDto, XxglModel> {
	/**
	 * 根据消息ID获取消息，并根据参数进行替换
	 */
	 String getMsg(String msgid, String... params);
	/**
	 * 根据消息ID获取消息，并根据参数进行替换
	 * @param msgid 需要替换的消息id
	 * @param map map中 key为消息内容中«key»，value为替换的内容
	 */
	 String getReplaceMsg(String msgid,  Map<String,Object> map);
	/**
	 * 根据消息ID获取消息，并根据参数进行替换
	 * @param msgid 需要替换的消息id
	 * @param obj obj中 key为消息内容中«key»，value为替换的内容
	 */
	 String getReplaceMsgByObj(String msgid,  Object obj);
}
