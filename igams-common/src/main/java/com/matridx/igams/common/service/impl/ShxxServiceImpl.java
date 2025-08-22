package com.matridx.igams.common.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.dao.entities.ShxxDto;
import com.matridx.igams.common.dao.entities.ShxxModel;
import com.matridx.igams.common.dao.post.IShxxDao;
import com.matridx.igams.common.service.svcinterface.IShxxService;

@Service
public class ShxxServiceImpl extends BaseBasicServiceImpl<ShxxDto, ShxxModel, IShxxDao> implements IShxxService{
	/**
	 * 获得全部审核信息，按审核时间倒排序
	 */
	public List<ShxxDto> getShxxOrderByShsj(ShxxDto dto) {
		return dao.getShxxOrderByShsj(dto);
	}

	/**
	 * 根据过程ID获取审核信息
	 */
	@Override
	public List<ShxxDto> getShxxOrderByGcid(ShxxDto shxxDto) {
		// TODO Auto-generated method stub
		return dao.getShxxOrderByGcid(shxxDto);
	}

	/**
	 * 查询业务审核过程
	 */
	@Override
	public List<ShxxDto> getShxxOrderByPass(ShxxDto shxxParam) {
		// TODO Auto-generated method stub
		return dao.getShxxOrderByPass(shxxParam);
	}

	/**
	 * 审核信息修改
	 */
	@Override
	public boolean auditmodSaveTime(ShxxDto shxxDto) {
		// TODO Auto-generated method stub
		int result = dao.auditmodSaveTime(shxxDto);
		return result != 0;
	}

	/**
	 * 获取最新的审核通过的信息
	 */
	public List<ShxxDto> getPassShxxBestNew(ShxxDto shxxDto){
		return dao.getPassShxxBestNew(shxxDto);
	}

	/**
	 * 新冠获取提交信息
	 */
	public ShxxDto getPassShxxBestSqr(ShxxDto shxxDto){
		return dao.getPassShxxBestSqr(shxxDto);
	}

	@Override
	public List<ShxxDto> getShxxByYwidsTG(ShxxDto shxxDto) {
		return dao.getShxxByYwidsTG(shxxDto);
	}

	@Override
	public List<ShxxDto> getShxxByYwidsTj(ShxxDto shxxDto) {
		return dao.getShxxByYwidsTj(shxxDto);
	}

	/**
	 * 根据lcxh分组获取审核信息
	 */
	public List<ShxxDto> getShxxGroupByLcxh(ShxxDto shxxDto){
		return dao.getShxxGroupByLcxh(shxxDto);
	}

	@Override
	public ShxxDto getShxxByYwid(ShxxDto shxxDto) {
		return dao.getShxxByYwid(shxxDto);
	}

	@Override
	public List<ShxxDto> getShxxLsit(ShxxDto shxxDto) {
		return dao.getShxxLsit(shxxDto);
	}

	@Override
	public ShxxDto queryShsj(ShxxDto shxxDto) {
		return dao.queryShsj(shxxDto);
	}
}
