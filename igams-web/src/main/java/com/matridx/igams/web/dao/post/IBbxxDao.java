package com.matridx.igams.web.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.web.dao.entities.BbxxDto;
import com.matridx.igams.web.dao.entities.BbxxModel;

@Mapper
public interface IBbxxDao extends BaseBasicDao<BbxxDto, BbxxModel> {
	/**
	 * 查询最近3条版本信息
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
