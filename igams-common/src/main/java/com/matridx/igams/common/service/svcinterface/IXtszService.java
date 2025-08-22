package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;

import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.dao.entities.XtszModel;

public interface IXtszService extends BaseBasicService<XtszDto, XtszModel>{
	
	/**
	 * 新增或修改系统设置
	 */
	boolean insertOrUpdateXtsz(XtszDto xtszDto);

	/**
	 * 根据ID查询系统设置信息
	 */
	XtszDto selectById(String szlb);

	/**
	 * 根据设置类别模糊查询系统设置信息 
	 */
	 List<XtszDto> getObscureDto(String szlb);

	/**
	 * 查找系统设置表中的所有数据
	 */
    List<XtszDto> getXtszList();
}
