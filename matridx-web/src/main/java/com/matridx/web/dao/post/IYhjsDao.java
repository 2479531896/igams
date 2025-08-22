package com.matridx.web.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.web.dao.entities.XtyhDto;
import com.matridx.web.dao.entities.YhjsDto;
import com.matridx.web.dao.entities.YhjsModel;

@Mapper
public interface IYhjsDao extends BaseBasicDao<YhjsDto, YhjsModel>{

	
	/**
	 * 获取可选用户
	 *@param xtyhDto
	 *@return
	 */
    List<XtyhDto> getPagedOptionalList(XtyhDto xtyhDto);
	
	/**
	 * 获取已选用户
	 *@param xtyhDto
	 *@return
	 */
    List<XtyhDto> getPagedSelectedList(XtyhDto xtyhDto);
	
	/**
	 * 添加用户
	 *@param yhjsDto
	 *@return
	 */
    boolean toSelected(YhjsDto yhjsDto);
	
	/**
	 * 去除用户
	 *@param yhjsDto
	 *@return
	 */
    boolean toOptional(YhjsDto yhjsDto);
}
