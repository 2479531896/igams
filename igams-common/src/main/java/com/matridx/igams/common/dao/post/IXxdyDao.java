package com.matridx.igams.common.dao.post;

import com.matridx.igams.common.dao.entities.JcsjDto;
import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.XxdyDto;
import com.matridx.igams.common.dao.entities.XxdyModel;

import java.util.List;
import java.util.Map;

@Mapper
public interface IXxdyDao extends BaseBasicDao<XxdyDto, XxdyModel>{

	/** 
	 * 取到对应表中岗位名称的角色ID
	 * gwmc
	 * 
	 */
	XxdyDto getGwJsid(String gwmc);

	/**
	 * 统计记录数
	 * t
	 * 
	 */
	XxdyDto getInfo(XxdyDto t);
	/**
	 * 得到DTO的dylx对应的基础数据cskz
	 * xxdyDto
	 * 
	 */
	XxdyDto getCskz(XxdyDto xxdyDto);

	/**
	 * 通过dyxx拿到不同连接表的dyxxmc
	 * dyxx
	 * 
	 */
	String getRyXxdymc(String dyxx);

	/**
	 * 通过dyxx拿到不同连接表的dyxxmc(角色)
	 * dyxx
	 * 
	 */
	String getJsXxdymc(String dyxx);

	/**
	 * 通过dyxx拿到不同连接表的dyxxmc(基础数据)
	 * dyxx
	 * 
	 */
	String getJcsjXxdymc(String dyxx);

	/**
	 * 通过用户的岗位名称查找对应表中的角色Id
	 * gwmc
	 * 
	 */
	String getJsidByGwmc(String gwmc);
	/**
	 * 根据搜索条件获取导出条数
	 * xxdyDto
	 * 
	 */
	 int getCountForSearchExp(XxdyDto xxdyDto);

	/**
	 * 从数据库分页获取导出数据
	 * xxdyDto
	 * 
	 */
	 List<XxdyDto> getListForSearchExp(XxdyDto xxdyDto);

	/**
	 * 从数据库分页获取导出数据
	 * xxdyDto
	 * 
	 */
	 List<XxdyDto> getListForSelectExp(XxdyDto xxdyDto);

	 List<XxdyDto> getPagedInfoDtoList(XxdyDto xxdyDto);
	/**
	 * 更新全部字段
	 * xxdyDto
	 * 
	 */
	 boolean updateAll(XxdyDto xxdyDto);
	
	/**
	 * 根据对应代码获取对应信息
	 * xxdyDto
	 * 
	 */
	 List<XxdyDto> getDtoByDydm(XxdyDto xxdyDto);
	/**
	 * 根据原信息获取对应信息
	 * xxdyDto
	 *
	 */
	 List<XxdyDto> getDtoMsgByYxx(XxdyDto xxdyDto);
	/**
	 * 获取原信息
	 * xxdyDto
	 * 
	 */
	 List<XxdyDto> getYxxMsg(XxdyDto xxdyDto);
	/**
	 * 根据原信息分组显示
	 * xxdyDto
	 * 
	 */
	 List<XxdyDto> getListGroupByYxx(XxdyDto xxdyDto);

	/**
	 * 根据原信息获取基础数据List
	 * @param xxdyDto
	 * @return
	 */
	List<JcsjDto> getJcsjListByXxdy(XxdyDto xxdyDto);
	/**
	 * 根据原信息获取基础数据Lis(模糊匹配)
	 * @param xxdyDto
	 * @return
	 */
	List<JcsjDto> getJcsjListByLikeXxdy(XxdyDto xxdyDto);

	List<Map<String,Object>>getSjwzxxList();
	
	List<XxdyDto> queryHolidays(XxdyDto xxdyDto);
	
	List<Map<String, Object>> getOriginList(XxdyDto xxdyDto);
	
	List<JcsjDto> getCompareListSortByList(List<Map<String, Object>> list);
	 
	/**
	 * 批量新增
	 * @param list
	 * @return
	 */
	int batchInsertDtos(List<Map<String, Object>> list);
	/**
	 * 批量修改
	 * @param list
	 * @return
	 */
	int batchUpdateDtos(List<Map<String, Object>> list);

	/**
	 * 批量删除
	 * @param list
	 * @return
	 */
	int batchDeleteDtos(List<Map<String, Object>> list);
	
	List<XxdyDto> getListOrder(XxdyDto xxdyDto);
	
	String getByDylxcsdmAndDyxxAndYxx(XxdyDto xxdyDto);

	/**
	 * @Description: 根据参数扩展7获取检测项目
	 * @param xxdyDto
	 * @return java.util.List<com.matridx.igams.common.dao.entities.XxdyDto>
	 * @Author: 郭祥杰
	 * @Date: 2025/6/10 16:06
	 */
	List<XxdyDto> getJcxmByKzcs7(XxdyDto xxdyDto);

	List<Map<String,String>>getPcdyrq(XxdyDto xxdyDto);

	List<Map<String,String>>getStddevAndVariance(XxdyDto xxdyDto);

	List<XxdyDto>getDyxxByYxx(XxdyDto xxdyDto);
}
