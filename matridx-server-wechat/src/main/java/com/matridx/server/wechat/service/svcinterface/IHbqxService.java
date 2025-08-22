package com.matridx.server.wechat.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.HbqxDto;
import com.matridx.server.wechat.dao.entities.HbqxModel;
import com.matridx.server.wechat.dao.entities.YhqtxxDto;

public interface IHbqxService extends BaseBasicService<HbqxDto, HbqxModel>{
	/**
	 * 通过用户id查询对应的伙伴id
	 * @param yhid
	 * @return
	 */
    List<String> getHbidByYhid(String yhid);
	
	/**
	 * 添加伙伴权限
	 * @param hbqxDto
	 * @return
	 */
    boolean insertHbqx(HbqxDto hbqxDto, YhqtxxDto yhqtxxDto)throws BusinessException ;
	
	/**
	 * 查询伙伴权限
	 * @param hbqxDto
	 * @return
	 */
    List<HbqxDto> getHbqxxx(HbqxDto hbqxDto);
	
	/**
	 * 通过用户id 获取伙伴信息
	 * @param yhid
	 * @return
	 */
    List<HbqxDto> getHbxxByYhid(String yhid);
	
}
