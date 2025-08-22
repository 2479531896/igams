package com.matridx.igams.common.service.svcinterface;

import java.util.List;
import java.util.Map;

import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;

public interface IFileInspectionImport extends IFileImport{

	/**
	 * 根据标本编号获取送检信息，用于页面显示
	 */
	List<Map<String, String>> getListByYbbhs(List<String> ybbhs, StringBuffer errorMessage);
	
	/**
	 * 保存上传送检报告
	 */
	boolean saveImportReport(BaseModel baseModel, User user, StringBuffer errorMessage) throws BusinessException;
}
