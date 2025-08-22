package com.matridx.las.home.service.svcinterface;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.las.home.dao.entities.SysszDto;
import com.matridx.las.home.dao.entities.YqxxDto;
import com.matridx.las.home.dao.entities.YqxxModel;
import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.dao.entities.FrameModelToHtml;
import com.matridx.las.netty.dao.entities.SjlcDto;

import java.util.List;
import java.util.Map;

public interface IGlobalprocessService{
 Map<String ,Object> showGlobalprocessDate();
 Map<String ,Object> saveInstrumentUsedList(List<Map<String,String>> list);
 public Map<String, Object> saveFreeQueue(List<Map<String,String>> list);
 public Map<String, Object> removeCmd(String id, String threadName,String mlid);
 public Map<String, Object> sendCmd(String id, FrameModel frameModel);
 public Map<String, Object> removeEventDic(String id, String threadName,String lcid);

 public Map<String, Object> modEventDic(String id, String threadName, SjlcDto sjlcDto);
 public Map<String, Object> modQueueIndex(String id,String threadName,String type);
 public Map<String, Object> sendMessageBythreadname(String id, String threadName);
 public Map<String, Object> modCmdDic( FrameModelToHtml frameModelToHtml);
 public Map<String, Object> addCmdDic( FrameModelToHtml frameModelToHtml);
 public Map<String, Object> addEventDic(String id, String threadName, SjlcDto sjlcDto);
 public Map<String, Object> sendEventDicBythreadname(String id, String threadName,String command,String deviceid);


}
