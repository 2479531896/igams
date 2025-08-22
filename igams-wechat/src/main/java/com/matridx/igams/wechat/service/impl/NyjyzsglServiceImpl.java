package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.NyjyzsglDto;
import com.matridx.igams.wechat.dao.entities.NyjyzsglModel;
import com.matridx.igams.wechat.dao.entities.WechatInspectionResistModel;
import com.matridx.igams.wechat.dao.post.INyjyzsglDao;
import com.matridx.igams.wechat.service.svcinterface.INyjyzsglService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NyjyzsglServiceImpl extends BaseBasicServiceImpl<NyjyzsglDto, NyjyzsglModel, INyjyzsglDao> implements INyjyzsglService{

	/**
	 * 批量新增耐药基因注释时
	 * @param list
	 * @return
	 */
	public boolean insertByListDto(List<NyjyzsglDto> list) {
		return dao.insertByListDto(list);
	}

	/**
	 * 批量查询耐药基因注释时
	 * @param nyjyzsglDto
	 * @return
	 */
	@Override
	public List<NyjyzsglDto> queryByIds(NyjyzsglDto nyjyzsglDto) {
		return dao.queryByIds(nyjyzsglDto);
	}

	/**
	 * 批量新增耐药基因注释时
	 * @param list
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean insertByList(List<WechatInspectionResistModel> list) {
		return dao.insertByList(list);
	}
	/**
	 * 批量更新耐药基因注释时
	 * @param list
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateByList(List<WechatInspectionResistModel> list) {
		return dao.updateByList(list);
	}
}
