package com.matridx.las.netty.dao.post;

import com.matridx.las.netty.dao.entities.YsybxxDto;
import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.las.netty.dao.entities.YblcsjDto;
import com.matridx.las.netty.dao.entities.YblcsjModel;

import java.util.List;


@Mapper
public interface IYblcsjDao extends BaseBasicDao<YblcsjDto, YblcsjModel>{

	//根据样本id修改
	public int updateYblcsjById(YblcsjDto dto);

	public int updateYblcsjByList(List<YblcsjDto> yblcsList);
	//根据样本id查询样本流程事件信息
	public YblcsjDto getByYsybId(YblcsjDto dto);

	int insertList(List<YblcsjDto> list);
	List<YblcsjDto> getYblcsjDtoList(List<YblcsjDto> list);
}
