package com.matridx.server.wechat.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.HbdwqxDto;
import com.matridx.server.wechat.dao.entities.HbdwqxModel;

@Mapper
public interface IHbdwqxDao extends BaseBasicDao<HbdwqxDto, HbdwqxModel>{

	/**
	 * 根据基础类别查找检测单位
	 * @param jcdwlb
	 * @return
	 */
	List<HbdwqxDto> getOptionJcdw(String jcdwlb);

	/**
	 * 已经被选择的检测单位从表中查找
	 * @param hbdwqxDto
	 * @return
	 */
	List<HbdwqxDto> getPagedSelectedList(HbdwqxDto hbdwqxDto);

	/**
	 * 一次插入检测单位list信息
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
}
