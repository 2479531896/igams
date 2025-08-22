package com.matridx.igams.storehouse.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.storehouse.dao.entities.LykcxxDto;
import com.matridx.igams.storehouse.dao.entities.LykcxxModel;
import com.matridx.igams.storehouse.dao.post.ILykcxxDao;
import com.matridx.igams.storehouse.service.svcinterface.ILykcxxService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class LykcxxServiceImpl extends BaseBasicServiceImpl<LykcxxDto, LykcxxModel, ILykcxxDao> implements ILykcxxService {
	@Autowired
	private IFjcfbService fjcfbService;
	/**
	 * 批量新增
	 * @param list
	 * @return
	 */
	public boolean insertList(List<LykcxxDto> list){
		return dao.insertList(list);
	}

	/**
	 * 批量修改
	 * @param list
	 * @return
	 */
	public boolean updateList(List<LykcxxDto> list){
		return dao.updateList(list);
	}

	@Override
	public boolean updateKcl(LykcxxDto lykcxxDto) {
		return dao.updateKcl(lykcxxDto);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateDto(LykcxxDto lykcxxDto) {
		int result=dao.updateDto(lykcxxDto);
		if (result==0)
			return false;
		if(!CollectionUtils.isEmpty(lykcxxDto.getFjids())) {
			for (int i = 0; i < lykcxxDto.getFjids().size(); i++) {
				fjcfbService.save2RealFile(lykcxxDto.getFjids().get(i), lykcxxDto.getLykcid());
			}
		}
		return true;
	}

	@Override
	public boolean updateKclList(List<LykcxxDto> list) {
		return dao.updateKclList(list);
	}

	@Override
	public boolean updateKclListByBj(List<LykcxxDto> updateList) {
		return dao.updateKclListByBj(updateList);
	}

	@Override
	public boolean updateScgcrq(LykcxxDto lykcxxDto) {
		return dao.updateScgcrq(lykcxxDto);
	}
}
