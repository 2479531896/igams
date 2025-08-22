package com.matridx.igams.common.dao.post;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.JcsjModel;

@Mapper
public interface IJcsjDao extends BaseBasicDao<JcsjDto, JcsjModel>{
	/**
	 * 获取列表
	 * jcsjDto
	 * 
	 */
	@Override
	 List<JcsjDto> getDtoList(JcsjDto jcsjDto);


	@Cacheable(value="jcsj",key="#p0.getJclb()",cacheManager = "ehCacheCacheManager")
	 List<JcsjDto> getDtoListByJclb(JcsjDto jcsjDto);
	
	/**
	 * 获取所有信息
	 * jcsjModel
	 * 
	 */
	@Override
	//@Cacheable(value="jcsj",key="#p0.getJclb()" ,cacheManager = "ehCacheCacheManager")
	//useGeneratedKeys=true就是定义数据库返回主键ID,KeyProperty是实体类中定义的主键字段名,KeyColumn是表中主键对应的字段
	//@Options(keyProperty="id",keyColumn="id",useGeneratedKeys=true) 
	 List<JcsjModel> getModelList(JcsjModel jcsjModel);
	
//	/**
//	 * 获取列表(不保存到缓存中)
//	 * t
//	 * 
//	 */
//	 List<JcsjDto> getJcsjDtoList(JcsjDto jcsjDto);

//	/**
//	 * 获取列表(不保存到缓存中)
//	 * t
//	 * 
//	 */
//	 List<JcsjDto> getJcsjDtoListAndJl(JcsjDto jcsjDto);
	
	/**
	 * 插入基础数据，同时删除缓存
	 * jcsjModel
	 * 
	 */
	@CacheEvict(value="jcsj",key="#p0.getJclb()" ,cacheManager = "ehCacheCacheManager")
	 int insert(JcsjModel jcsjModel);
	
	/**
	 * 更新基础数据，同时删除缓存
	 * jcsjModel
	 * 
	 */
	@CacheEvict(value="jcsj",key="#p0.getJclb()" ,cacheManager = "ehCacheCacheManager")
	 int update(JcsjModel jcsjModel);
	
	/**
	 * 更新基础数据，同时删除缓存
	 * jcsjModel
	 * 
	 */
	@CacheEvict(value="jcsj",key="#p0.getJclb()" ,cacheManager = "ehCacheCacheManager")
	 int delete(JcsjModel jcsjModel);
//	/**
//	 *  从基础数据里边查询单位
//	 * csmc
//	 * 
//	 */
//	  JcsjModel selectJcsj(String csmc);
	 
//	 /**
//	  * 根据省份从基础数据中查询城市
//	  * jcsjDto
//	  * 
//	  */
//	  List<JcsjDto>  jcsjcity(JcsjDto jcsjDto);
	 
//	 /**
//	  * 查询默认的类型
//	  * jcsjDto
//	  * 
//	  */
//	  JcsjDto getdefault(JcsjDto jcsjDto);
	 
	 /**
	  * 通过csdm查询到货验收情况
	  * csdmList
	  * 
	  */
	  List<JcsjDto> getYsqkByCsdm(List<String> csdmList);
	 
//	 /**
//	  * 通过参数名称，查询分组信息
//	  * csmc
//	  * 
//	  */
//	  JcsjDto getFzByCsmc(String csmc);

//	 /**
//	  * 根据基础类别和扩展参数获取检测项目
//	  * jcsjDto
//	  * 
//	  */
//	 List<JcsjDto> getDtoByJclbAndCskz1(JcsjDto jcsjDto);
//	 /**
//	  * 根据基础类别和扩展参数获取检测项目
//	  * jcsjDto
//	  * 
//	  */
//	 List<JcsjDto> getDtoByJclbAndCskz(JcsjDto jcsjDto);
	
	/**
	 * 通过类别查询扩展参数为1基础数据
	 * jcsjDto
	 * 
	 */
	 List<JcsjDto> getCskz1NotNull(JcsjDto jcsjDto);

//	/**
//	 * 根据ids查询基础数据
//	 * jcsjDto
//	 * 
//	 */
//	 List<JcsjDto> getJcsjByids(JcsjDto jcsjDto);
//
//	/**
//	 * 根据基础类别和参数名称查询基础信息
//	 * jcsjDto
//	 * 
//	 */
//	 JcsjDto getDtoByCsmcAndJclb(JcsjDto jcsjDto);
	
	/**
	 * 查询停用数据
	 * jcsjDto
	 * 
	 */
	 List<JcsjDto> getInstopDtoList(JcsjDto jcsjDto);
	
//	/**
//	 * 根据基础类别和cskz3查询基础数据信息
//	 * jcsjDto
//	 * 
//	 */
//	 JcsjDto getDtoByKzcs(JcsjDto jcsjDto);
	
	/**
	 * 查询消息类别排序最大值
	 * jcsjDto
	 * 
	 */
	 Integer getMax(JcsjDto jcsjDto);
	
//	/**
//	 * 修改消息类别
//	 * jcsjDto
//	 * 
//	 */
//	 boolean updateXxlx(JcsjDto jcsjDto);
	
	
//	/**
//	 * 删除消息类别
//	 * jcsjDto
//	 * 
//	 */
//	 boolean deleteXxlx(JcsjDto jcsjDto);

	/**
	 * 伙伴列表页面获取可选择的检测单位
	 * jcsjDto
	 * 
	 */
	 List<JcsjDto> getPagesOptionJcdw(JcsjDto jcsjDto);
	
//	/**
//	 * 查询检测项目
//	 * jcsjDto
//	 * 
//	 */
//	 List<JcsjDto> selectDetectionUnit(JcsjDto jcsjDto);

//	/**
//	 * 根据父ID查询基础数据信息
//	 * jcsjDto
//	 * 
//	 */
//	 List<JcsjDto> getListByFid(JcsjDto jcsjDto);

//	/**
//	 * 根据参数代码和基础类别查找数据
//	 * jcsjDto
//	 * 
//	 */
//	 JcsjDto getDtoByCsdmAndJclb(JcsjDto jcsjDto);

	/**
	 * 基础数据的所有基础类别
	 * 
	 */
	 List<String> getJclbxx();

	/**
	 * 根据类别查找所有基础数据，包含删除标记为0/1/2的
	 * 
	 */
    List<JcsjDto> getAllDtoList();
//
//	/**
//	 * 根据类别查找该类别的所有基础数据
//	 * jclb
//	 * 
//	 */
//	 List<JcsjDto> getDtoListBylb(String jclb);

//	/**
//	 * 更具参数代码和基础类型查找
//	 * jcsjDto
//	 * 
//	 */
//	 JcsjDto getByAndCsdm(JcsjDto jcsjDto);
//	/**
//	 * 证件类型查找
//	 * jcsjDto
//	 * 
//	 */
//	 List<JcsjDto> getZjlx();

	/**
	 * 查询基础数据（所有）
	 * jcsjDto
	 * 
	 */
	List<JcsjDto> getJcsjListByIdsAndJclb(JcsjDto jcsjDto);
	/**
	 * 根据csid查询基础数据（所有）
	 * jcsjDto
	 * 
	 */
	JcsjDto getJcsjByCsid(JcsjDto jcsjDto);
	/**
	 * 删除某一基础类别(慎用)
	 * jcsjDto
	 * 
	 */
	boolean deleteByJclb(JcsjDto jcsjDto);
	/**
	 * 批量新增基础数据
	 * jcsjDtos
	 * 
	 */
	int batchInsertJcsjDtos(List<JcsjDto> jcsjDtos);
	/**
	 * 批量新增
	 * list
	 * 
	 */
	 boolean insertList(List<JcsjDto> list);
	/**
	 * 批量修改删除标记
	 * list
	 * 
	 */
	 boolean updateListScbj(List<JcsjDto> list);

	/**
	 * 查询ResFirst取值范围
	 * 
	 */

	 JcsjDto getResFirstInfo();
	/**
	 * 通过父父id获取list
	 * jcsjDto
	 * 
	 */
	List<JcsjDto> getListByFfid(JcsjDto jcsjDto);

	/**
	 * 通过csid获取检测项目名称
	 * @param map
	 * @return
	 */
	List<JcsjDto> getJcxmCsmcByCsid(Map<String,List<String>>map);

	/**
	 * 获取删除标记不为1的数据
	 * @param jclbDto
	 * @return
	 */
    List<JcsjDto> getNormalList(JcsjDto jclbDto);

	/**
	 * 根据基础数据类别获取基础数据及子级数据
	 * @param jclbDto
	 * @return
	 */
	List<JcsjDto> getDtoAndSubListByJclb(JcsjDto jclbDto);
}
