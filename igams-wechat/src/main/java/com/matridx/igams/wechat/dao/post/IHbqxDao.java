package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.HbqxDto;
import com.matridx.igams.wechat.dao.entities.HbqxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

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
	
	/**
	 * 通过用户id查询对应的伙伴Group信息
	 * @param yhid
	 * @return
	 */
    List<HbqxDto> getHbxxGroupByYhid(String yhid);

	/**
	 * 设置上级的伙伴权限
	 * @param map
	 */
    Boolean insertSjhbqx(Map<String, Object> map);
	/**
	 * 获取伙伴名称集合
	 */
    List<String> getHbmcByYhid(String id);
}
