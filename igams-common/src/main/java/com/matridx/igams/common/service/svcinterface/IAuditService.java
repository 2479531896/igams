package com.matridx.igams.common.service.svcinterface;

import java.util.List;
import java.util.Map;

import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.ShxxDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;

public interface IAuditService {
	
	/**
	 * 进行前置处理，比如审核时显示编辑页面，则先进行编辑信息的保存操作
	 * @param baseModel	审核过程列表
	 * @param operator  操作人
	 * @param auditParam 审核传递的额外参数信息
	 */
	 boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) throws BusinessException;
	
	/**
	 * 更新审核对象
	 * @param shgcList	审核过程列表
	 * @param operator  操作人
	 * @param auditParam 审核传递的额外参数信息
	 */
	 boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException;
	
	/**
	 * 更新撤回对象
	 * @param shgcList	审核过程列表
	 * @param operator  操作人
	 * @param auditParam 审核传递的额外参数信息
	 */
	 boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException;

	/**
	 * 查看审核历史的页面时，查找机构id信息
	 */
	 Map<String, Object> requirePreAuditMember(ShgcDto shgcDto)throws BusinessException;

	/**
	 * 返回审核相关的业务信息
	 */
	 Map<String, Object> returnAuditServiceInfo(Map<String, Object> param)throws BusinessException;
	
	/**
	 * 审核完成后处理
	 * @param dto	审核信息，主要是业务ID列表，ywids
	 * @param operator  操作人
	 * @param param 审核传递的额外参数信息
	 */
	 boolean updateAuditEnd(ShxxDto dto, User operator, Map<String, String> param) throws BusinessException;
}
