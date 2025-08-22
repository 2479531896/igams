package com.matridx.server.wechat.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.server.wechat.dao.entities.HbdwqxDto;
import com.matridx.server.wechat.dao.entities.HbdwqxModel;
import com.matridx.server.wechat.dao.post.IHbdwqxDao;
import com.matridx.server.wechat.service.svcinterface.IHbdwqxService;

@Service
public class HbdwqxServiceImpl extends BaseBasicServiceImpl<HbdwqxDto, HbdwqxModel, IHbdwqxDao> implements IHbdwqxService{

	/**
	 * 根据基础类别查找检测单位
	 */
	@Override
	public List<HbdwqxDto> getOptionJcdw(String jcdwlb) {
		// TODO Auto-generated method stub
		return dao.getOptionJcdw(jcdwlb);
	}

	/**
	 * 已经被选择的检测单位从表中查找
	 */
	@Override
	public List<HbdwqxDto> getPagedSelectedList(HbdwqxDto hbdwqxDto) {
		// TODO Auto-generated method stub
		return dao.getPagedSelectedList(hbdwqxDto);
	}

	/**
	 * 一次插入检测单位list信息
	 */
	@Override
	public boolean insertjcdw(List<HbdwqxDto> jcdwlist) {
		// TODO Auto-generated method stub
		return dao.insertjcdw(jcdwlist);
	}
	
	/**
	 * 查询伙伴单位权限
	 * @param hbmc
	 * @return
	 */
	@Override
	public List<String> selectByHbmc(String hbmc) {
		// TODO Auto-generated method stub
		return dao.selectByHbmc(hbmc);
	}

}
