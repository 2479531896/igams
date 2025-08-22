package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.LczzznglDto;
import com.matridx.igams.wechat.dao.entities.LczzznglModel;
import com.matridx.igams.wechat.dao.entities.WechatClinicalGuideModel;
import com.matridx.igams.wechat.dao.post.ILczzznglDao;
import com.matridx.igams.wechat.service.svcinterface.ILczzznglService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LczzznglServiceImpl extends BaseBasicServiceImpl<LczzznglDto, LczzznglModel, ILczzznglDao> implements ILczzznglService{

	/**
	 * 根据指南ids获取临床症状指南信息
	 * @param lczzznglDto
	 * @return
	 */
	public List<LczzznglDto> getListByIds(LczzznglDto lczzznglDto){
		return dao.getListByIds(lczzznglDto);
	}
	
	/**
	 * 批量新增临床症状指南信息
	 * @param list
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean insertByListDto(List<LczzznglDto> list) {
		return dao.insertByListDto(list);
	}

	/**
	 * 批量新增临床症状指南信息
	 * @param list
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean insertByList(List<WechatClinicalGuideModel> list) {
		return dao.insertByList(list);
	}

	/**
	 * 批量更新临床症状指南信息
	 * @param guidelines
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateByList(List<WechatClinicalGuideModel> guidelines) {
		return dao.updateByList(guidelines);
	}


	/**
	 * 通过指南ids查询List
	 * @param lczzznglDto
	 * @return
	 */
	public List<LczzznglDto> getDtoListByIds(LczzznglDto lczzznglDto){
		return dao.getDtoListByIds(lczzznglDto);
	}
}
