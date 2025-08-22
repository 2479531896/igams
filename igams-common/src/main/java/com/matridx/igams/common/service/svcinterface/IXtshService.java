package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;

import com.matridx.igams.common.dao.entities.XtshDto;
import com.matridx.igams.common.dao.entities.XtshModel;

public interface IXtshService extends BaseBasicService<XtshDto, XtshModel>{

	/**
	 * 根据ids获取系统审核信息
	 */
	List<XtshDto> getXtshByIds(XtshDto xtshDto);

	/**
	 * 到货审核延期
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
	/**
	 * 插入系统审核（传入审核id）
	 */
    boolean insertXtsh(XtshDto xtshDto);
}
