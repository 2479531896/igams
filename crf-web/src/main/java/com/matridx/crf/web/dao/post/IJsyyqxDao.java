package com.matridx.crf.web.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.crf.web.dao.entities.JsyyqxDto;
import com.matridx.crf.web.dao.entities.JsyyqxModel;

@Mapper
public interface IJsyyqxDao extends BaseBasicDao<JsyyqxDto, JsyyqxModel>{
	public List<JsyyqxDto> getPagedYxYyxxList(JsyyqxDto jsyyqxDto);
	public List<JsyyqxDto> getPagedOtherYyxxList(JsyyqxDto jsyyqxDto);
	public boolean toOptionalYy(JsyyqxDto jsyyqxDto);
	public boolean toSelectedYy(JsyyqxDto jsyyqxDto);
}
