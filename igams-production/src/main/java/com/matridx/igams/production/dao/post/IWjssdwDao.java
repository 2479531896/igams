package com.matridx.igams.production.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.WjssdwDto;
import com.matridx.igams.production.dao.entities.WjssdwModel;

import java.util.List;

@Mapper
public interface IWjssdwDao extends BaseBasicDao<WjssdwDto, WjssdwModel>{

	/**
	 * 根据文件id查询所属单位信息
	 */
	WjssdwDto getWjssdwByWjid(WjssdwDto wjssdwDto);
	
	/**
	 * 根据文件ids删除所属单位信息
	 */
	boolean deleteWjssdw(WjssdwDto wjssdwDto);
	
	/**
	 * 根据文件ID更新文件所属信息
	 */
	boolean updateWjssdw(WjssdwDto wjssdwDto);
	/**
	 * 分组获取机构信息
	 */
	List<WjssdwDto> getListGroup();

	/**
	 * @Description: 批量新增
	 * @param wjssdwDtoList
	 * @return boolean
	 * @Author: 郭祥杰
	 * @Date: 2024/12/6 13:59
	 */
	boolean insertDtoList(List<WjssdwDto> wjssdwDtoList);
}
