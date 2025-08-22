package com.matridx.igams.common.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.dao.entities.LbcxszDto;
import com.matridx.igams.common.dao.entities.LbzdszDto;
import com.matridx.igams.common.dao.entities.LbzdszModel;
import com.matridx.igams.common.dao.post.ILbzdszDao;
import com.matridx.igams.common.service.svcinterface.ILbzdszService;

@Service
public class LbzdszServiceImpl extends BaseBasicServiceImpl<LbzdszDto, LbzdszModel, ILbzdszDao> implements ILbzdszService{

	/**
	 * 新增列表字段设置信息
	 */

	@Override
	public boolean insertByLbcxsz(LbcxszDto lbcxszDto) {
		int result = dao.insertByLbcxsz(lbcxszDto);
    	return result > 0;
	}

	@Override
	public int insertByLbcxszlist(List<LbzdszDto> list) {
		return dao.insertByLbcxszlist(list);
	}

	@Override
	public boolean updateByLbcxsz(LbcxszDto lbcxszDto) {
		int result = dao.updateByLbcxsz(lbcxszDto);
		return result > 0;
	}

	@Override
	public boolean deleteLbzdszb(LbcxszDto lbcxszDto) {
		int result = dao.deleteLbzdszb(lbcxszDto);
		return result > 0;
	}

	@Override
	public boolean deleteLbzdszbyywid(LbzdszDto lbzdszDto) {
		int result = dao.deleteLbzdszbyywid(lbzdszDto);
		return result > 0;
	}

	/**
	 * 根据用户信息获取用户未选择的字段信息
	 */
	public List<LbzdszDto> getWaitList(LbzdszDto lbzdszDto){
		return dao.getWaitList(lbzdszDto);
	}

	@Override
	public List<String> getYwmc(){
		// TODO Auto-generated method stub
		return dao.getYwmc();
	}
	
	/**
	 * 通过ywid 查询导出字段
	 */
	@Override
	public List<LbzdszDto> getDczd(String ywid){
		// TODO Auto-generated method stub
		return dao.getDczd(ywid);
	}

	@Override
	public List<LbzdszDto> getXszdQx(Map map) {
		return dao.getXszdQx(map);
	}

}
