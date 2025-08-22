package com.matridx.igams.wechat.dao.post;


import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.DdyhDto;
import com.matridx.igams.wechat.dao.entities.DdyhModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IDdyhDao extends BaseBasicDao<DdyhDto, DdyhModel>{

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
	 * 根据用户名查询用户ID
	 * @param yhm
	 * @return
	 */
    List<DdyhDto> getUserByYhm(String yhm);
}
