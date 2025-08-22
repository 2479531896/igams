package com.matridx.igams.common.service.svcinterface;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.common.dao.entities.DdfbsglDto;
import com.matridx.igams.common.dao.entities.DdfbsglModel;
import com.matridx.igams.common.exception.BusinessException;

import java.util.List;
import java.util.Map;


public interface IDdfbsglService extends BaseBasicService<DdfbsglDto, DdfbsglModel>{
	
	/**
	 * 执行钉钉审批回调
	 */
	boolean excuteAudit(DdfbsglDto ddfbsglDto) throws BusinessException;

	List<DdfbsglDto> getNoEndList(DdfbsglDto ddfbsglDto);

	 void dealCallback(JSONObject json, String plainText, String token, String wbcxid, Map<String,Object>map) throws BusinessException;

	
	/**
     * 将钉钉审批信息保存至钉钉分布式管理表
     */
	 boolean saveDistributedMsg(DdfbsglDto ddfbsglDto);
}
