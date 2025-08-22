package com.matridx.igams.production.service.svcinterface;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.HtfkqkDto;
import com.matridx.igams.production.dao.entities.HtfkqkModel;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface IHtfkqkService extends BaseBasicService<HtfkqkDto, HtfkqkModel>{

	/**
	 * 新增合同付款情况
	 */
	boolean insertFkqk(HtfkqkDto htfkqkDto);

	/**
	 * 修改合同付款情况
	 */
	boolean updateFkqk(HtfkqkDto htfkqkDto);

	/**
	 * 获取审核列表
	 */
	List<HtfkqkDto> getPagedAuditHtfkqk(HtfkqkDto htfkqkDto);

	/**
	 * 合同付款申请回调方法
	 */
	boolean callbackHtfkqkAduit(JSONObject obj, HttpServletRequest request, Map<String,Object> t_map) throws BusinessException;

	/**
	 * 生成合同付款单号
	 */
	String generatePayDjh(HtfkqkDto HtfkqkDto);

	/**
	 * 校验付款金额是否超过合同未付款金额
	 */
	Map<String,Object> checkMoney(HtfkqkDto htfkqkDto);

	/**
	 * 删除合同付款信息
	 */
	boolean delHtfkqk(HtfkqkDto htfkqkDto) throws BusinessException ;
	/**
	 * 根据搜索条件获取导出条数
	 */
	int getCountForSearchExp(HtfkqkDto htfkqkDto, Map<String, Object> params);
}
