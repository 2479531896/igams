package com.matridx.igams.production.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.QgqxglDto;
import com.matridx.igams.production.dao.entities.QgqxglModel;
import com.matridx.igams.production.dao.entities.QgqxmxDto;

public interface IQgqxglService extends BaseBasicService<QgqxglDto, QgqxglModel>{

	/**
	 * 请购取消管理保存
	 */
	boolean addSavePurchaseCancel(QgqxglDto qgqxglDto);
	
	/**
	 * 请购取消审核列表
	 */
	List<QgqxglDto> getPagedAuditQgqxgl(QgqxglDto qgqxglDto);
	
	/**
	 * 请购取消管理修改保存
	 */
	boolean modSavePurchaseCancel(QgqxglDto qgqxglDto);
	
	/**
	 * 请购取消修改
	 */
	boolean updateQgqxglxx(QgqxglDto qgqxglDto);
	
	/**
	 * 删除请购取消明细
	 */
	boolean deleteQgqx(QgqxmxDto qgqxmxDto) throws BusinessException;
}
