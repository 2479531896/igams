package com.matridx.crf.web.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import java.util.List;

import com.matridx.crf.web.dao.entities.JsyyqxDto;
import com.matridx.crf.web.dao.entities.JsyyqxModel;

public interface IJsyyqxService extends BaseBasicService<JsyyqxDto, JsyyqxModel>{
	public List<JsyyqxDto> getPagedYxYyxxList(JsyyqxDto jsyyqxDto);
	public List<JsyyqxDto> getPagedOtherYyxxList(JsyyqxDto jsyyqxDto);
	public boolean toOptionalYy(JsyyqxDto jsyyqxDto);
	public boolean toSelectedYy(JsyyqxDto jsyyqxDto);
}
