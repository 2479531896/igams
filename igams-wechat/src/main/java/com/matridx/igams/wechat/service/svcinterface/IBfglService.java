package com.matridx.igams.wechat.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.BfdxlxrglDto;
import com.matridx.igams.wechat.dao.entities.BfglDto;
import com.matridx.igams.wechat.dao.entities.BfglModel;

public interface IBfglService extends BaseBasicService<BfglDto, BfglModel>{

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
	 * 钉钉小程序全部拜访次数统计图
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
	 * 保存拜访记录
	 */
	boolean saveVisitRecord(BfglDto bfglDto);
	/**
	 * 删除拜访记录
	 */
	boolean delVisitRecord(BfglDto bfglDto);
	/**
	 * 批量删除
	 */
	boolean batchDelete(BfglDto bfglDto);
	/**
	 * 合并
	 */
	boolean merge(BfglDto bfglDto);

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
