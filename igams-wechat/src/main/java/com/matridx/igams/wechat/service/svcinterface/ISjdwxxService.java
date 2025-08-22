package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.SjdwxxDto;
import com.matridx.igams.wechat.dao.entities.SjdwxxModel;

import java.util.List;
public interface ISjdwxxService extends BaseBasicService<SjdwxxDto, SjdwxxModel>{
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
	* 组装医院菜单列表树
	* @param sjdwxxList
	* @param jSONDATA
	* @return
	*/
    String installTree(List<SjdwxxDto> sjdwxxList, String jSONDATA);
	
	/**
	* 添加单位
	* @param sjdwxxDto
	* @return
	*/
    boolean addWechatHis(SjdwxxDto sjdwxxDto);
	
	/**
	* 修改单位
	* @param sjdwxxDto
	* @return
	*/
    boolean modSavewHis(SjdwxxDto sjdwxxDto);

		
	/**
	* 删除单位
	* @param sjdwxxDto
	* @return
	*/
    boolean delHisByDwid(SjdwxxDto sjdwxxDto);

	/**
	 * 根据dwid获取送检单位信息
	 * @param dws
	 * @return
	 */
    List<SjdwxxDto> getListByDwid(List<String> dws);
}
