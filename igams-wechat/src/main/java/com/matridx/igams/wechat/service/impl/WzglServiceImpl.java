package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.WeChatInspectionSpeciesModel;
import com.matridx.igams.wechat.dao.entities.WzglDto;
import com.matridx.igams.wechat.dao.entities.WzglModel;
import com.matridx.igams.wechat.dao.post.IWzglDao;
import com.matridx.igams.wechat.service.svcinterface.IWzglService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WzglServiceImpl extends BaseBasicServiceImpl<WzglDto, WzglModel, IWzglDao> implements IWzglService{

	/**
	 * 根据物种ids获取物种信息
	 * @param wzglDto
	 * @return
	 */
	public List<WzglDto> getDtoByIds(WzglDto wzglDto){
		return dao.getDtoByIds(wzglDto);
	}
	
	/**
	 * 批量新增物种管理信息
	 * @param list
	 * @return
	 */
	public boolean insertByListDto(List<WzglDto> list) {
		return dao.insertByListDto(list);
	}
	/**
	 * 获取物种级别信息
	 * @param wzglDto
	 * @return
	 */
	public List<WzglDto> getPagedDtoList(WzglDto wzglDto){
		return dao.getPagedDtoList(wzglDto);
	}
	/**
	 * 获取某一个的具体信息
	 * @param wzglDto
	 * @return
	 */
	public WzglDto getDtoByWzid(WzglDto wzglDto){
		return dao.getDtoByWzid(wzglDto);
	}
	/**
	 * 获取物种拦截信息
	 * @param wzglDto
	 * @return
	 */
	public List<WzglDto> getPagedWzljList(WzglDto wzglDto){
		return dao.getPagedWzljList(wzglDto);
	}
	/**
	 * 获取详情信息
	 * @param wzglDto
	 * @return
	 */
	public WzglDto getDtoByLjid(WzglDto wzglDto){
		return dao.getDtoByLjid(wzglDto);
	}
	/**
	 * 根据父id获取详情信息
	 * @param wzglDto
	 * @return
	 */
	public WzglDto getDtoByFid(WzglDto wzglDto){
		return  dao.getDtoByFid(wzglDto);
	}
	/**
	 * 获取拦截信息
	 * @param list
	 * @return
	 */
	public List<WeChatInspectionSpeciesModel> getInspectionSpeciesModelList(List<WeChatInspectionSpeciesModel> list){
		return  dao.getInspectionSpeciesModelList(list);
	}

	/**
	 * 判断 是否需要插入，以及是否需要更新
	 * @param wzglDtos
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean insertOrUpdateByListDto(List<WzglDto> wzglDtos) {
		dao.insertByList(wzglDtos);
		dao.updateByList(wzglDtos);
		return true;
	}

	/**
	 * 根据wzid/中文名称/英文名称 查找物种信息
	 * @param wzglDto
	 * @return
	 */
	@Override
	public WzglDto getWzxxByMc(WzglDto wzglDto) {
		return dao.getWzxxByMc(wzglDto) ;
	}

	/**
	 * 获取物种List
	 * @param wzglDto
	 * @return
	 */
	public List<WzglDto> getWzList(WzglDto wzglDto){
		return dao.getWzList(wzglDto);
	}

	/**
	 * 获取病原体物种List
	 * @param wzglDto
	 * @return
	 */
	public List<WzglDto> getPathogenList(WzglDto wzglDto){
		return dao.getPathogenList(wzglDto);
	}
}
