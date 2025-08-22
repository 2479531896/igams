package com.matridx.igams.production.service.svcinterface;

import java.util.List;
import java.util.Map;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.QgqxglDto;
import com.matridx.igams.production.dao.entities.QgqxmxDto;
import com.matridx.igams.production.dao.entities.QgqxmxModel;

public interface IQgqxmxService extends BaseBasicService<QgqxmxDto, QgqxmxModel>{

	/**
	 * 新增请购取消明细
	 */
	boolean insertDtoList(List<QgqxmxDto> list);
	
	/**
	 * 删除请购取消明细
	 */
	boolean deleteQgqxmx(QgqxmxDto qgqxmxDto) throws BusinessException;
	
	/**
	 * 请购取消明细列表
	 */
	List<QgqxmxDto> getQgqxmxList(QgqxmxDto qgqxmxDto);
	
	/**
	 * 根据搜索条件获取导出条数
	 */
	int getCountForSearchExp(QgqxmxDto qgqxmxDto, Map<String, Object> params);
	
	/**
	 * 取消请购审核通过更新请购明细sl，sysl字段
	 */
	boolean updateQgmxByList(QgqxglDto qgqxglDto);
	
	/**
	 * 更新请购取消明细数据
	 */
	boolean updateDtoList(List<QgqxmxDto> list);
	
	//获取其他有关该请购明细的取消请购信息
	//List<QgqxmxDto> getOtherQxqgList(QgqxmxDto qgqxmxDto);
	
	/**
	 * 获取请购明细以及可取消数
	 */
	List<QgqxmxDto> getQgqxmxCancelList(QgqxmxDto qgqxmxDto);
}
