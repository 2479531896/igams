package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.WeChatInspectionSpeciesModel;
import com.matridx.igams.wechat.dao.entities.WzglDto;
import com.matridx.igams.wechat.dao.entities.WzglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IWzglDao extends BaseBasicDao<WzglDto, WzglModel>{

	/**
	 * 根据物种ids获取物种信息
	 * @param wzglDto
	 * @return
	 */
    List<WzglDto> getDtoByIds(WzglDto wzglDto);
	
	/**
	 * 批量新增物种管理信息
	 * @param list
	 * @return
	 */
    boolean insertByListDto(List<WzglDto> list);
	/**
	 * 获取物种级别信息
	 * @param wzglDto
	 * @return
	 */
    List<WzglDto> getPagedDtoList(WzglDto wzglDto);
	/**
	 * 获取某一个的具体信息
	 * @param wzglDto
	 * @return
	 */
    WzglDto getDtoByWzid(WzglDto wzglDto);
	/**
	 * 获取物种拦截信息
	 * @param wzglDto
	 * @return
	 */
    List<WzglDto> getPagedWzljList(WzglDto wzglDto);
	/**
	 * 获取详情信息
	 * @param wzglDto
	 * @return
	 */
    WzglDto getDtoByLjid(WzglDto wzglDto);
	/**
	 * 根据父id获取详情信息
	 * @param wzglDto
	 * @return
	 */
    WzglDto getDtoByFid(WzglDto wzglDto);
	
	/**
	 * 获取拦截信息
	 * @param list
	 * @return
	 */
    List<WeChatInspectionSpeciesModel> getInspectionSpeciesModelList(List<WeChatInspectionSpeciesModel> list);
	
	/**
	 * 批量新增
	 * @param wzglDtos
	 * @return
	 */
    boolean insertByList(List<WzglDto> wzglDtos);
	
	/**
	 * 批量更新
	 * @param wzglDtos
	 * @return
	 */
    boolean updateByList(List<WzglDto> wzglDtos);
	/**
	 * 根据wzid/中文名称/英文名称 查找物种信息
	 * @param wzglDto
	 * @return
	 */
	WzglDto getWzxxByMc(WzglDto wzglDto);

	/**
	 * 获取物种List
	 * @param wzglDto
	 * @return
	 */
	List<WzglDto> getWzList(WzglDto wzglDto);

	/**
	 * 获取病原体物种List
	 * @param wzglDto
	 * @return
	 */
	List<WzglDto> getPathogenList(WzglDto wzglDto);
}
