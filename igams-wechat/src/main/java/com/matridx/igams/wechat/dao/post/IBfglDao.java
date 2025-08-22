package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.BfdxlxrglDto;
import com.matridx.igams.wechat.dao.entities.BfglDto;
import com.matridx.igams.wechat.dao.entities.BfglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IBfglDao extends BaseBasicDao<BfglDto, BfglModel>{

	/**
	 * 根据录入人员查询拜访登记信息(根据单位,客户姓名,科室分组)
	 * @param bfglDto
	 * @return
	 */
	List<BfglDto> getDtoByLrry(BfglDto bfglDto);
	
	/**
	 * 钉钉小程序获取个人拜访清单
	 * @param bfglDto
	 * @return
	 */
	List<BfglDto> getPersonalListByLrry(BfglDto bfglDto);
	
	/**
	 * 获取最新的医生数,床位数,分组数,科室合作公司信息
	 * @param bfglDto
	 * @return
	 */
	BfglDto getNewKsxx(BfglDto bfglDto);
	
	/**
	 * 选中导出
	 * @param bfglDto
	 * @return
	 */
	List<BfglDto> getListForSelectExp(BfglDto bfglDto);
	
	/**
	 * 查询可以导出的条数
	 * @param bfglDto
	 * @return
	 */
	int getCountForSearchExp(BfglDto bfglDto);
	
	/**
	 * 从数据库分页获取导出送检异常
	 * @param bfglDto
	 * @return
	 */
	List<BfglDto> getListForSearchExp(BfglDto bfglDto);
	
	/**
	 * 钉钉小程序统计本周的拜访次数，拜访单位个数，拜访时长
	 * @param bfglDto
	 * @return
	 */
	BfglDto getStatictisByWeek(BfglDto bfglDto);
	
	/**
	 * 钉钉小程序统计本月的拜访次数，拜访单位个数，拜访时长
	 * @param bfglDto
	 * @return
	 */
	BfglDto getStatictisByMonth(BfglDto bfglDto);
	
	/**
	 * 钉钉小程序统计全部的拜访次数，拜访单位个数，拜访时长
	 * @param bfglDto
	 * @return
	 */
	BfglDto getStatictisByAll(BfglDto bfglDto);
	
	/**
	 * 钉钉小程序统计本日的拜访次数，拜访单位个数，拜访时长
	 * @param bfglDto
	 * @return
	 */
	BfglDto getStatictisByDAY(BfglDto bfglDto);
	
	/**
	 * 钉钉小程序拜访次数统计图(根据单位区分)
	 * @param bfglDto
	 * @return
	 */
	List<BfglDto> getStatictisCountByDwid(BfglDto bfglDto);
	
	/**
	 * 钉钉小程序查询拜访单位个数统计图(根据拜访时间起始区分)
	 * @param bfglDto
	 * @return
	 */
	List<BfglDto> getStatictisDwgsByBfsjqs(BfglDto bfglDto);
	
	/**
	 * 钉钉小程序查询拜访时长统计图(根据拜访单位区分)
	 * @param bfglDto
	 * @return
	 */
	List<BfglDto> getStatictisBfscByDwid(BfglDto bfglDto);
	
	/**
	 * 获取统计周期内拜访人信息
	 * @param bfglDto
	 * @return
	 */
	List<BfglDto> getBfrByTjrq(BfglDto bfglDto);
	
	/**
	 * 钉钉小程序查询拜访次数统计图(根据人员区分)
	 * @param bfglDto
	 * @return
	 */
	List<BfglDto> getStatictisCountByBfr(BfglDto bfglDto);
	
	/**
	 * 钉钉小程序查询拜访单位个数统计图(根据拜访人区分)
	 * @param bfglDto
	 * @return
	 */
	List<BfglDto> getStatictisDwgsByBfr(BfglDto bfglDto);
	
	/**
	 * 钉钉小程序查询拜访时长统计图(根据拜访人区分) 
	 * @param bfglDto
	 * @return
	 */
	List<BfglDto> getStatictisBfscByBfr(BfglDto bfglDto);
	/**
	 * 合并
	 */
	boolean merge(BfglDto bfglDto);

	/**
	 * 合并修改lxrid
	 * @param bfdxlxrglDtos_sc
	 * @return
	 */
	boolean mergeLxrid(List<BfdxlxrglDto>bfdxlxrglDtos_sc);
	/**
	 * 获取统计数据
	 */
	List<BfglDto> getStatisticsListByBfr(BfglDto bfglDto);
	/**
	 * 获取统计数据
	 */
	List<BfglDto> getStatisticsListByDw(BfglDto bfglDto);
	/**
	 * 查看统计数据详情
	 */
	List<BfglDto> viewStatisticList(BfglDto bfglDto);

}
