package com.matridx.igams.warehouse.service.svcinterface;

import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.warehouse.dao.entities.GysxxDto;
import com.matridx.igams.warehouse.dao.entities.GysxxModel;

import java.util.List;
import java.util.Map;

public interface IGysxxService extends BaseBasicService<GysxxDto, GysxxModel>{

	/**
	 * 查询供应商列表
	 * @param gysxxDto
	 
	 */
	List<GysxxDto> getPagedSupplierList(GysxxDto gysxxDto);
	
	/**
	 * 根据供应商id查询供应商信息
	 * @param gysxxDto
	 
	 */
	GysxxDto selectGysxxByGysid(GysxxDto gysxxDto);
	
	/**
	 * 删除供应商信息
	 * @param gysxxDto
	 
	 */
	boolean deleteByGysids(GysxxDto gysxxDto);
	
	/**
	 * 模糊查询供应商
	 * @param gysxxDto
	 
	 */
	List<GysxxDto> selectBygysmc(GysxxDto gysxxDto);
	
	/**
	 * 供应商名称供应商简称去重
	 * @param gysxxDto
	 
	 */
	boolean getDtoByGysmcAndJc(GysxxDto gysxxDto);

	/**
	 * 查找服务器
	 * @param server
	 
	 */
	JcsjDto getServer(String server);
	/**
	 * 查询供应商信息名称
	 * @param gysxxDto
	 
	 */
	GysxxDto selectByGysmc(GysxxDto gysxxDto);
	/**
	 * 供应商附件下载
	 */
	Map<String, Object> downloadSupplier(GysxxDto gysxxDto);
	/**
	 * @description 停用供应商
	 * @param gysxxDto
	 
	 */
    boolean disableSupplier(GysxxDto gysxxDto) throws BusinessException;
	/**
	 * @description 启用供应商
	 * @param gysxxDto
	 
	 */
	boolean enableSupplier(GysxxDto gysxxDto) throws BusinessException;

	/**
	 * @Description: 校验供方编号是否存在
	 * @param gysxxDto
	 * @return boolean
	 * @Author: 郭祥杰
	 * @Date: 2024/11/11 9:55
	 */
	boolean queryByGfbh(GysxxDto gysxxDto);
}
