package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.Role;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XxdyDto;
import com.matridx.igams.common.dao.entities.XxdyModel;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;
import java.util.Map;

public interface IXxdyService extends BaseBasicService<XxdyDto, XxdyModel>{

	/**
	 * 取对应表中的岗位名称的角色ID
	 */
	XxdyDto getGwJsid(String gwmc);

	/**
	 * 统计记录数
	 */
	 XxdyDto getInfo(XxdyDto t);
	/**
	 * 查询角色列表
	 */
	List<Role> getRoleList();

	/**
	 * 查询用户列表
	 */
	List<User> getUserList();

	/**
	 * 得到DTO的dylx对应的基础数据cskz
	 */
	XxdyDto getCskz(XxdyDto xxdyDto);

	/**
	 * 通过dyxx拿到不同连接表的dyxxmc(人员)
	 */
	String getRyXxdymc(String dyxx);

	/**
	 * 通过dyxx拿到不同连接表的dyxxmc(角色)
	 */
	String getJsXxdymc(String dyxx);

	/**
	 * 通过dyxx拿到不同连接表的dyxxmc(基础数据)
	 */
	String getJcsjXxdymc(String dyxx);

	/**
	 * 通过用户的岗位名称查找对应表中的角色Id
	 */
	String getJsidByGwmc(String gwmc);

	List<XxdyDto> getPagedInfoDtoList(XxdyDto xxdyDto);

	/**
	 * 更新全部字段
	 */
	 boolean updateAll(XxdyDto xxdyDto);

	/**
	 * 获取原信息
	 */
	 List<XxdyDto> getYxxMsg(XxdyDto xxdyDto);
	/**
	 * 根据原信息分组显示
	 */
	 List<XxdyDto> getListGroupByYxx(XxdyDto xxdyDto);

	/**
	 * 根据对应代码获取对应信息
	 */
	 List<XxdyDto> getDtoByDydm(XxdyDto xxdyDto);

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
	/**
	 * 获取送检物种信息中的物种列表
	 * @return
	 */
	List<Map<String,Object>>getSjwzxxList();

	List<XxdyDto> queryHolidays(XxdyDto xxdyDto);
	
	List<XxdyDto> getListOrder(XxdyDto xxdyDto);

	List<Map<String, Object>> getOriginList(XxdyDto xxdyDto);

	/**
	 * 批量新增
	 * @param list
	 * @return
	 */
	boolean batchInsertDtos(List<Map<String, Object>> list);
	
	/**
	 * 批量修改
	 * @param list
	 * @return
	 */
	boolean batchUpdateDtos(List<Map<String, Object>> list);

	/**
	 * 根据list顺序查找信息对应
	 * @param list
	 * @return
	 */
    List<JcsjDto> getCompareListSortByList(List<Map<String, Object>> list);

	/**
	 * 批量删除
	 * @param list
	 * @return
	 */
	boolean batchDeleteDtos(List<Map<String, Object>> list);

	String getByDylxcsdmAndDyxxAndYxx(XxdyDto xxdyDto);

	/**
	 * 根据原信息获取对应信息
	 * xxdyDto
	 *
	 */
	List<XxdyDto> getDtoMsgByYxx(XxdyDto xxdyDto);
	
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
