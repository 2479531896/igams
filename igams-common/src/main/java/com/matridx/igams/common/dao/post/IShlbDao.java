package com.matridx.igams.common.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.cache.annotation.Cacheable;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.ShlbDto;
import com.matridx.igams.common.dao.entities.ShlbModel;

@Mapper
public interface IShlbDao extends BaseBasicDao<ShlbDto, ShlbModel>{
	/**
	 * 获取所有信息
	 * shlbModel
	 * 
	 */
	@Override
	@Cacheable(value="shlb",key="#p0.getShlb()" ,cacheManager = "ehCacheCacheManager")
	//useGeneratedKeys=true就是定义数据库返回主键ID,KeyProperty是实体类中定义的主键字段名,KeyColumn是表中主键对应的字段
	//@Options(keyProperty="id",keyColumn="id",useGeneratedKeys=true) 
	 List<ShlbModel> getModelList(ShlbModel shlbModel);
	
	/**
	 * 根据审核类别获取审核类别信息
	 * shlbDto
	 * 
	 */
	 ShlbDto getShlbxxByShlb(ShlbDto shlbDto) ;
	
	/**
	 * 根据ids查询审核类别
	 * shlbDto
	 * 
	 */
	 List<ShlbDto> getShlbxxByIds(ShlbDto shlbDto);
	
	/**
	 * 获取审核类别的所有信息 
	 * 
	 */
	 List<ShlbDto> getShlbAllData();
	/*
   		获取首页的所有审核类别
	*/
	List<ShlbDto> getShlbForHomePage();
}
