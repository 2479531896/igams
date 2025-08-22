package com.matridx.server.wechat.dao.post;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.HbqxDto;
import com.matridx.server.wechat.dao.entities.HbqxModel;

@Mapper
public interface IHbqxDao extends BaseBasicDao<HbqxDto, HbqxModel>{
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
    boolean insertHbqx(HbqxDto hbqxDto);
	
	/**
	 * 根据用户id查询伙伴权限信息
	 * @param hbqxDto
	 * @return
	 */
    List<HbqxDto> getHbqxxx(HbqxDto hbqxDto);
	
	/**
	 * 删除伙伴权限信息
	 * @param hbqxDto
	 * @return
	 */
    boolean delyhqxxx(HbqxDto hbqxDto);
	
	/**
	 * 通过用户id 获取伙伴信息
	 * @param yhid
	 * @return
	 */
    List<HbqxDto> getHbxxByYhid(String yhid);
}
