package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.SjdwxxDto;
import com.matridx.igams.wechat.dao.entities.SjdwxxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface ISjdwxxDao extends BaseBasicDao<SjdwxxDto, SjdwxxModel> {
	/**
	* 获取医院名称
	* @return
	*/
    List<SjdwxxDto> getPagedDtoListSjdwxx(SjdwxxDto sjdwxxDto);
	/**
	* 获取所有科室
	* @return
	*/
    List<SjdwxxDto> getAllSjdwxx();
	
	/**
	* 删除菜单及子菜单
	* @param sjdwxxDto 
	* @return
	*/
	
	boolean delHisByDwid(SjdwxxDto sjdwxxDto );

	/**
	 * 根据dwid获取送检单位信息
	 * @param dws
	 * @return
	 */
    List<SjdwxxDto> getListByDwid(List<String> dws);
}
