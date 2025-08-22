package com.matridx.igams.sample.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.sample.dao.entities.SbkxglDto;
import com.matridx.igams.sample.dao.entities.SbkxglModel;

@Mapper
public interface ISbkxglDao extends BaseBasicDao<SbkxglDto, SbkxglModel>{
	/**
	 * 根据设备ID获取该设备的空闲情况
	 */
    List<SbkxglDto> getSbkxList(SbkxglDto sbkxglDto);
	
	/**
	 * 根据存放数和标本类型获取推荐位置
	 */
    List<SbkxglDto> getFitPos(SbkxglDto sbkxglDto);
	
	/**
	 * 根据空闲位置获取相应的记录
	 */
    List<SbkxglModel> getModelByWz(SbkxglModel sbkxglModel);
	
	/**
	 * 在设备空闲信息最后增加一条空闲信息
	 */
    int insertNew(SbkxglModel sbkxglModel);
}
