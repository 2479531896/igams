package com.matridx.igams.experiment.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.experiment.dao.entities.TqglDto;
import com.matridx.igams.experiment.dao.entities.TqglModel;
import com.matridx.igams.experiment.dao.entities.TqmxDto;

import java.util.List;
import java.util.Map;

public interface ITqglService extends BaseBasicService<TqglDto, TqglModel>{

	/**
	 * 获取提取管理列表
	 */
	List<TqglDto> getPagedTqglDtoList(TqglDto tqglDto);
	
	/**
	 * 更新提取管理信息
	 */
	boolean updateTqgl(TqglDto tqglDto);
	
	/**
	 * 删除提取信息
	 */
	boolean deleteTqxx(TqglDto tqglDto);
	
	/**
	 * 根据名称获取提取管理信息
	 */
	List<TqglDto> getTqglByMc(TqglDto tqglDto);

	List<Map<String,String>> getJsjcdwByjsid(String jsid);
	
	/**
	 * 删除临时保存的提取管理信息
	 */
	boolean deleteLs(String tqid);

	/**
	 * @Description: 获取操作人和审核人签名
	 * @param tqmxDto
	 * @return com.matridx.igams.experiment.dao.entities.TqglDto
	 * @Author: 郭祥杰
	 * @Date: 2025/5/7 15:01
	 */
	TqglDto getCzrAndShrPic(TqmxDto tqmxDto);
}
