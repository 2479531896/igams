package com.matridx.igams.common.service.impl;

import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.WtxxDto;
import com.matridx.igams.common.dao.entities.WtxxModel;
import com.matridx.igams.common.dao.post.IWtxxDao;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IWtxxService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WtxxServiceImpl extends BaseBasicServiceImpl<WtxxDto, WtxxModel, IWtxxDao> implements IWtxxService{
	
	/**
	 * 获取有效的指定的委托人
	 * @param wtbh	委托编号
	 * @param xtzy 系统资源
	 * @param str 受托人
	 */
	public User getWtr(String wtbh, String xtzy, String str) {
		
		Map<String,Object> param = new HashMap<>();
		param.put("wtbh", wtbh);
		param.put("xtzy", xtzy);
		param.put("str", str);
		param.put("is_role_switch", GlobalString.IS_ROLE_SWITCH);
		User wtr = dao.getWtr(param);
		
		List<String> jsids = new ArrayList<>();
		jsids.add(wtr.getDqjs());
		
		//根据给定登录号查询其所有资源对应权限限定标记
		Map<String, Map<String, Object>> qxxdbjMap = new HashMap<>();
		List<Map<String, Object>> xdbjList = dao.listQxxdbjByYh(wtr.getYhid(),jsids);
		if(null != xdbjList){
			for (Map<String, Object> map : xdbjList) {
				if (null != map) {
					String zyid = (String) map.get("ZYID");
					qxxdbjMap.put(zyid, map);
				}
			}
		}
		wtr.setQxxdbjMap(qxxdbjMap);
		
		return wtr;
	}
}
