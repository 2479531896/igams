package com.matridx.las.netty.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.las.netty.dao.entities.FsgcmlDto;
import com.matridx.las.netty.dao.entities.FsgcmlModel;

@Mapper
public interface IFsgcmlDao extends BaseBasicDao<FsgcmlDto, FsgcmlModel>{
	/**
	 * 根据类型获取发送命令对象集合
	 * @param type
	 * @return
	 */
	public List<FsgcmlDto> getFsgcmlDtosByType(String type);
}
