package com.matridx.igams.wechat.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.HbdwqxDto;
import com.matridx.igams.wechat.dao.entities.HbdwqxModel;

public interface IHbdwqxService extends BaseBasicService<HbdwqxDto, HbdwqxModel>{

	/**
     * 根据基础类别查找检测单位
     * @param string
     * @return
     */
	List<HbdwqxDto> getOptionJcdw(String string);

	/**
	 * 查找伙伴列表中已经选中的
	 * @param hbdwqxDto
	 * @return
	 */
	List<HbdwqxDto> getPagedSelectedList(HbdwqxDto hbdwqxDto);

	/**
	 * 一次性插入检测单位信息
	 * @param jcdwlist
	 * @return
	 */
	boolean insertjcdw(List<HbdwqxDto> jcdwlist);
	
	/**
	 * 查询伙伴单位权限
	 * @param hbmc
	 * @return
	 */
	List<String> selectByHbmc(String hbmc);

	/**
	 * 查找伙伴列表中已经选中的检测单位(不分页)
	 * @param hbdwqxDto
	 * @return
	 */
	List<HbdwqxDto> getJcdwSelectedList(HbdwqxDto hbdwqxDto);
}
