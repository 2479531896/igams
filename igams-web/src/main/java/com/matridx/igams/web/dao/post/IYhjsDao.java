package com.matridx.igams.web.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.web.dao.entities.XtyhDto;
import com.matridx.igams.web.dao.entities.YhjsDto;
import com.matridx.igams.web.dao.entities.YhjsModel;

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
	 * 获取已选用户
	 *@param xtyhDto
	 *@return
	 */
    List<XtyhDto> getSelectedList(XtyhDto xtyhDto);
	
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
	
	/**
	 * 根据Ids查询角色里已选用户
	 * @return
	 */
    List<YhjsDto> getYxyhList(XtyhDto xtyhDto);
	
	/**
	 * 通过jsid查询下属用户
	 * @param yhjsDto
	 * @return
	 */
    List<YhjsDto> selectYhidByJsid(YhjsDto yhjsDto);
	
	/**
	 * 根据用户ID查询权限信息
	 * @param yhid
	 * @return
	 */
    List<YhjsDto> getDtoListById(String yhid);

	/**
	 * 根据yhid判断yhjs表中是否已经存在数据
	 * @param yhid
	 * @return
	 */
    List<YhjsModel> isRepeatJsByYhid(String yhid);
}
