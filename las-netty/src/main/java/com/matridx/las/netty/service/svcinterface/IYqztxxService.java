package com.matridx.las.netty.service.svcinterface;

import java.util.List;
import java.util.Map;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.las.netty.dao.entities.YqztxxDto;
import com.matridx.las.netty.dao.entities.YqztxxModel;

public interface IYqztxxService extends BaseBasicService<YqztxxDto, YqztxxModel>{
	//更新yq状态
	public void updateStYqztxx(String sbid,String yqzt) ;
	public List<Map<String, String>> getAllYqztList() ;

}
