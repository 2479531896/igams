package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.BfdxglDto;
import com.matridx.igams.wechat.dao.entities.BfdxglModel;
import com.matridx.igams.wechat.dao.entities.BfglDto;

import java.util.List;
import java.util.Map;

public interface IBfdxglService extends BaseBasicService<BfdxglDto, BfdxglModel> {
	
	/**
	 * 新增保存
	 */
   boolean addSaveVisitingObject(BfdxglDto bfdxglDto);

	/**
	 * 修改保存
	 */
	boolean modSaveVisitingObject(BfdxglDto bfdxglDto);

	/**
	 * 删除
	 */
	boolean delVisitingObject(BfdxglDto bfdxglDto);

	/**
	 * 选中导出
	 */
	public List<BfdxglDto> getListForSelectExp(Map<String, Object> params);
	/**
	 * 根据搜索条件获取导出条数
	 */
	public int getCountForSearchExp(BfdxglDto bfdxglDto,Map<String,Object> params);
	/**
	 * 根据搜索条件分页获取导出信息
	 */
	public List<BfdxglDto> getListForSearchExp(Map<String,Object> params);
	/**
	 * 合并
	 */
	boolean mergeSaveVisitingObject(BfdxglDto bfdxglDto);

	/**
	 * 生成客户编码
	 */
	public String generateCode(BfdxglDto bfdxglDto);

	/**
	 * 根据dwmc获取拜访对象
	 * @param bfdxglDto
	 * @return
	 */
	List<BfdxglDto> getDtoByDwmc(BfdxglDto bfdxglDto);
}
