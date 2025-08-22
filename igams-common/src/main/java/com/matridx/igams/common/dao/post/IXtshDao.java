package com.matridx.igams.common.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.XtshDto;
import com.matridx.igams.common.dao.entities.XtshModel;

@Mapper
public interface IXtshDao extends BaseBasicDao<XtshDto, XtshModel>{
	
	/**
	 * 根据ids获取系统审核信息
	 */
	List<XtshDto> getXtshByIds(XtshDto xtshDto);

	/**
	 * 到货审核延期列表
	 */
	List<XtshDto> getPagedDaoHuoSpyq(XtshDto xtshDto);

	/**
	 * 查看某条到货审批延期信息
	 */
	XtshDto getDHspyqByShid(XtshDto xtshDto);

	/**
	 * 获取审批岗位
	 */
	List<XtshDto> getGwDtoList(XtshDto xtshDto);
}
