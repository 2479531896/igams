package com.matridx.igams.storehouse.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.storehouse.dao.entities.HwllmxDto;
import com.matridx.igams.storehouse.dao.entities.HwllmxModel;
import com.matridx.igams.storehouse.dao.post.IHwllmxDao;
import com.matridx.igams.storehouse.service.svcinterface.IHwllmxService;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class HwllmxServiceImpl extends BaseBasicServiceImpl<HwllmxDto, HwllmxModel, IHwllmxDao> implements IHwllmxService{

	/**
	 * 批量新增
	 * @param 
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertHwllmxs(List<HwllmxDto> hwllmxDtos) {
		Integer xh = dao.getMaxXh();
		if(xh==null) {
			xh=0;
		}
		for (HwllmxDto hwllmxDto : hwllmxDtos) {
			xh=xh+1;
			hwllmxDto.setLlmxid(StringUtil.generateUUID());
			hwllmxDto.setXh(xh.toString());
		}
		int result = dao.insertHwllmxs(hwllmxDtos);
		return result > 0;
	}

	/**
	 * 批量更新
	 * @param 
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateHwllmxs(List<HwllmxDto> hwllmxDtos) {
		int result = dao.updateHwllmxs(hwllmxDtos);
		return result > 0;
	}
	
	/**
	 * 根据hwllid查询货物领料明细
	 * @param hwllid
	 * @return
	 */
	public List<HwllmxDto> getDtoHwllmxListByHwllid(String hwllid){
		return dao.getDtoHwllmxListByHwllid(hwllid);
	}

	@Override
	public List<HwllmxDto> getDtoHwllmxGroupByHwid(HwllmxDto hwllmxDto) {
		// TODO Auto-generated method stub
		return dao.getDtoHwllmxGroupByHwid(hwllmxDto);
	}

	/**
	 * 根据领料id获取领料明细
	 * @return
	 */
	@Override
	public List<HwllmxDto> getDtoByLlid(HwllmxDto hwllmxDto) {
		// TODO Auto-generated method stub
		return dao.getDtoByLlid(hwllmxDto);
	}

	/**
	 * 批量更新
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateMxList(List<HwllmxDto> hwllmxDtos) {
		int result = dao.updateMxList(hwllmxDtos);
		return result > 0;
	}

	/**
	 * 根据llid分组获取允许领取数量总数
	 * @param 
	 * @return
	 */
	@Override
	public List<HwllmxDto> getDtoGroupByLlid(String llid) {
		// TODO Auto-generated method stub
		return dao.getDtoGroupByLlid(llid);
	}

	/**
     * 更改请领数量
     *
     * @param
     */
	@Override
	public void updateqlsl(HwllmxDto hwllmxDto) {
        dao.updateqlsl(hwllmxDto);
    }

	/**
	 * 根据hwllid获取货物领料明细
	 * @param 
	 * @return
	 */
	@Override
	public List<HwllmxDto> queryByHwllid(HwllmxDto hwllmxDto) {
		// TODO Auto-generated method stub
		return dao.queryByHwllid(hwllmxDto);
	}

	/**
	 * 根据hwllid删除货物领料明细
	 * @param 
	 * @return
	 */
	@Override
	public boolean deleteByHwllid(HwllmxDto hwllmxDto) {
		int result = dao.deleteByHwllid(hwllmxDto);
		return result>0;
	}

	/**
	 * 根据仓库id分组查询
	 * @param 
	 * @return
	 */
	@Override
	public List<HwllmxDto> groupByCkid(String llid) {
		HwllmxDto hwllmxDto = new HwllmxDto();
		hwllmxDto.setLlid(llid);
		return dao.groupByCkid(hwllmxDto);
	}

	@Override
	public List<HwllmxDto> getDtoHwllmxListByLlid(String llid) {
		// TODO Auto-generated method stub
		return dao.getDtoHwllmxListByLlid(llid);
	}
	@Override
	public List<HwllmxDto> getDtoHwllmxListByPrint(String llid) {
		// TODO Auto-generated method stub
		return dao.getDtoHwllmxListByPrint(llid);
	}

	@Override
	public HwllmxDto queryHwllmxs(HwllmxDto hwllmxDto) {
		// TODO Auto-generated method stub
		return dao.queryHwllmxs(hwllmxDto);
	}

	/**
	 * 批量删除
	 * @param 
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean deleteByllid(HwllmxDto hwllmxDto) {
		// TODO Auto-generated method stub
		return dao.deleteByllid(hwllmxDto)>0;
	}

	@Override
	public List<HwllmxDto> getDtoHwllmxByLlid(String llid) {
		return dao.getDtoHwllmxByLlid(llid);
	}
	
	public List<HwllmxDto> getSumGroup(String llid){
		return dao.getSumGroup(llid);
	}

	@Override
	public List<HwllmxDto> getScphAndSlByHwllid(String hwllid) {
		return dao.getScphAndSlByHwllid(hwllid);
	}

	@Override
	public List<HwllmxDto> getDtoGroupByLlxx(String llid) {
		return dao.getDtoGroupByLlxx(llid);
	}
}
