package com.matridx.igams.warehouse.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.warehouse.dao.entities.GysxxDto;
import com.matridx.igams.warehouse.dao.entities.GysxxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IGysxxDao extends BaseBasicDao<GysxxDto, GysxxModel>{

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
	 * 查询供应商信息名称
	 * @param gysxxDto
	 
	 */
	GysxxDto selectByGysmc(GysxxDto gysxxDto);
	
	/**
	 * 删除供应商信息
	 * @param gysxxDto

	 */
	void deleteByGysids(GysxxDto gysxxDto);
	
	/**
	 * 模糊查询供应商
	 * @param gysxxDto
	 
	 */
	List<GysxxDto> selectBygysmc(GysxxDto gysxxDto);
	
	/**
	 * 查询供应商编码最大值
	 * @param gysxxDto
	 
	 */
	Integer countMax(GysxxDto gysxxDto);
	
	/**
	 * 供应商名称供应商简称去重
	 * @param gysxxDto
	 
	 */
	List<GysxxDto> getDtoByGysmcAndJc(GysxxDto gysxxDto);

	/**
	 * 查找服务器
	 * @param server
	 
	 */
	JcsjDto getServer(String server);
	/**
	 * 获取附件信息
	 */
	List<GysxxDto> selectDownLoadFiles(GysxxDto gysxxDto);
	/**
	 * @description 停用供应商
	 * @param gysxxDto
	 
	 */
	boolean disableSupplier(GysxxDto gysxxDto);
	/**
	 * @description 启用供应商
	 * @param gysxxDto
	 
	 */
    boolean enableSupplier(GysxxDto gysxxDto);

	/**
	 * @Description: 根据供方编号查询
	 * @param gysxxDto
	 * @return java.util.List<com.matridx.igams.warehouse.dao.entities.GysxxDto>
	 * @Author: 郭祥杰
	 * @Date: 2024/11/11 9:58
	 */
	List<GysxxDto> queryByGfbh(GysxxDto gysxxDto);
}
