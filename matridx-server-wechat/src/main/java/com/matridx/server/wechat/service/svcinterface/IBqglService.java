package com.matridx.server.wechat.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.BqglDto;
import com.matridx.server.wechat.dao.entities.BqglModel;

public interface IBqglService extends BaseBasicService<BqglDto, BqglModel>{

	/**
	 * 查询全部标签
	 * @return
	 */
	List<BqglDto> selectAll();

	/**
	 * 新增标签
	 * @param bqglDto
	 * @return
	 */
	boolean addSaveTag(BqglDto bqglDto);

	/**
	 * 修改标签
	 * @param bqglDto
	 * @return
	 */
	boolean modSaveTag(BqglDto bqglDto);

	/**
	 * 删除标签
	 * @param bqglDto
	 * @return
	 */
	boolean delTag(BqglDto bqglDto);

	/**
	 * 获取已创建标签信息
	 * @param bqglDto
	 * @return
	 */
	boolean getTag(BqglDto bqglDto);
	
	/**
	 * 通过微信公众号查询标签
	 * @param bqglDto
	 * @return
	 */
    List<BqglDto> selectTag(BqglDto bqglDto);

}
