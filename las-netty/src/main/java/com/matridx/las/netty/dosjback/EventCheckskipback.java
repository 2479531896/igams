package com.matridx.las.netty.dosjback;

import com.alibaba.fastjson.JSONObject;
import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.channel.server.handler.MatridxProtocolHandler;
import com.matridx.las.netty.dao.entities.SjlcDto;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 事件回调处理
 */
public interface EventCheckskipback {

	Logger log = LoggerFactory.getLogger(EventCheckskipback.class);
	 boolean checkSkip(SjlcDto sjlcDto, FrameModel frameModel, Map<String,String> map) ;


}
