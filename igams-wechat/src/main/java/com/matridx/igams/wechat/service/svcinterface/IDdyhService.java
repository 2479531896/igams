package com.matridx.igams.wechat.service.svcinterface;


import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.DdyhDto;
import com.matridx.igams.wechat.dao.entities.DdyhModel;

import java.util.List;

public interface IDdyhService extends BaseBasicService<DdyhDto, DdyhModel>{

	/**
	 * 根据钉钉ID获取用户角色信息
	 * @param ddid
	 * @return
	 */
    DdyhDto getUserByDdid(String ddid);
	
	/**
	 * 根据用户ID获取用户角色信息
	 * @param yhid
	 * @return
	 */
    List<DdyhDto> getUserByYhid(String yhid);
	
	/**
	 * 通过用户id查询单位限定
	 * @param yhid
	 * @return
	 */
    boolean getDwxd(String yhid);

	/**
	 * 根据用户名查询用户ID
	 * @param yhm
	 * @return
	 */
    List<DdyhDto> getUserByYhm(String yhm);
}
