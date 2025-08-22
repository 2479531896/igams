package com.matridx.igams.web.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.web.dao.entities.BbxxDto;
import com.matridx.igams.web.dao.entities.BbxxModel;

public interface IBbxxService extends BaseBasicService<BbxxDto, BbxxModel> {

	/**
	 * 查询版本信息
	 * 
	 * @param
	 * @return
	 */
    List<BbxxDto> getBbxxDtoList();
	/**
	 * 获取所有版本信息
	 *
	 * @param
	 * @return
	 */
    List<BbxxDto> getAllBbxxDtoList();

	/**
	 * 获取版本信息列表
	 * 
	 * @param bbxxDto
	 * @return
	 */
    List<BbxxDto> getPageDtoListVersionInfo(BbxxDto bbxxDto);
	
	/**
	 * 数据用户查看
	 * @param bbid
	 * @return
	 */
    BbxxDto getDtoVersionInfoByBbid(String bbid);
	
	/**
	 * 版本信息新增
	 * @param bbxxDto
	 * @return
	 */
    boolean insertDtoVersionInfo(BbxxDto bbxxDto);
	
	/**
	 * 版本信息修改
	 * @param bbxxDto
	 * @return
	 */
    boolean updateDtoVersionInfo(BbxxDto bbxxDto);
	
	/**
	 * 版本信息删除（修改删除标记）
	 * @param bbxxDto
	 * @return
	 */
    boolean delDtoListVersionInfo(BbxxDto bbxxDto);
}
