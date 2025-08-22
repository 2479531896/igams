package com.matridx.las.netty.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.las.netty.dao.entities.YblcsjDto;
import com.matridx.las.netty.dao.entities.YblcsjModel;
import com.matridx.las.netty.dao.entities.YsybxxDto;

import java.util.List;
import java.util.Map;

public interface IYblcsjService extends BaseBasicService<YblcsjDto, YblcsjModel>{



	boolean updateYblcsjByList(Map<String,Object>map,String index,String startOrEnd);

	public boolean updateYblcsjById(YblcsjDto dto);

	public YblcsjDto getByYsybId(YblcsjDto dto);

	boolean insertList(List<YblcsjDto>list);

	public List<YblcsjDto> getYsybxxDtoList(List<YblcsjDto> list) ;


}
