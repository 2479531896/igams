package com.matridx.igams.common.service.svcinterface;

import java.util.List;
import java.util.Map;

import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.entities.ImportRecordModel;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;

public interface IFileImport {
	/**
	 * 根据值检查是否存在
	 */
	 boolean existCheck(String fieldName,String value);
	
	/**
	 * 插入数据到数据库
	 */
	 boolean insertImportRec(BaseModel baseModel,User user,int rowindex,StringBuffer errorMessages) throws BusinessException;
	
	/**
	 * 根据转换标识和内容转换成数据库信息
	 */
	 String cellTransform(String tranTrack,String value,ImportRecordModel recModel,StringBuffer errorMessage);
	
	/**
	 * 插入数据到数据库
	 */
	 boolean insertNormalImportRec(Map<String, String> recMap,User user);
	
	/**
	 * 检查标题定义，主要防止模板信息过旧
	 */
	 boolean checkDefined(List<Map<String,String>> defined);
}
