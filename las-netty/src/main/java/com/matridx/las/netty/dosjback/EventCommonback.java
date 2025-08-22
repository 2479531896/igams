package com.matridx.las.netty.dosjback;

import com.alibaba.fastjson.JSONObject;
import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.dao.entities.SjlcDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 事件回调处理
 */
public interface EventCommonback {

	Logger log = LoggerFactory.getLogger(EventCommonback.class);
	 JSONObject handBack(SjlcDto sjlcDto, FrameModel frameModel, Map<String,String> ma) ;

}
