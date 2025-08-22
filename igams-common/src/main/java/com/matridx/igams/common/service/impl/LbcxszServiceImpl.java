package com.matridx.igams.common.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.dao.entities.LbcxszDto;
import com.matridx.igams.common.dao.entities.LbcxszModel;
import com.matridx.igams.common.dao.post.ILbcxszDao;
import com.matridx.igams.common.service.svcinterface.ILbcxszService;
import com.matridx.igams.common.service.svcinterface.ILbcxtjszService;
import com.matridx.igams.common.service.svcinterface.ILbzdszService;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class LbcxszServiceImpl extends BaseBasicServiceImpl<LbcxszDto, LbcxszModel, ILbcxszDao> implements ILbcxszService{

	@Autowired
	ILbcxtjszService lbcxtjszService;
	@Autowired
	ILbzdszService lbzdszSerice;
	
	/**
	 * 列表查询设置表分页查询
	 */
	@Override
	public List<LbcxszDto> getPagedDtoList(LbcxszDto lbcxszDto){
		List<LbcxszDto> t_list = dao.getPagedDtoList(lbcxszDto);
		for (LbcxszDto dto : t_list) {
			dto.setZylj("/common/list/pageListSetting?ywid=" + dto.getYwid());
			LbcxszDto t_lbcxszDto = dao.getXtzyByZylj(dto.getZylj());
			dto.setZyid(t_lbcxszDto.getZyid());
			dto.setFjd(t_lbcxszDto.getFjd());
			dto.setZybt(t_lbcxszDto.getZybt());
			dto.setXssx(t_lbcxszDto.getXssx());
			dto.setCdcc(t_lbcxszDto.getCdcc());
			dto.setGnm(t_lbcxszDto.getGnm());
			dto.setBtsx(t_lbcxszDto.getBtsx());
			List<LbcxszDto> zyczbList = dao.getZyczbByZyid(t_lbcxszDto.getZyid());
			List<String> czdms = new ArrayList<>();
			for (LbcxszDto value : zyczbList) {
				if (value.getCzdm() != null && !"".equals(value.getCzdm())) {
					czdms.add(value.getCzdm());
				}
				if (StringUtil.isBlank(dto.getCzmc())) {
					dto.setCzmc(value.getCzmc());
				} else {
					dto.setCzmc(dto.getCzmc() + "," + value.getCzmc());
				}
			}
			dto.setCzdms(czdms);
		}
		return t_list;
	}
	
	/**
	 * 列表查询设置表查询
	 */
	@Override
	public LbcxszDto getDtoById(String ywid){
		//查询列表
		LbcxszDto lbcxszDto = dao.getDtoById(ywid);
		//查询系统资源
		lbcxszDto.setZylj("/common/list/pageListSetting?ywid="+ywid);
		LbcxszDto t_lbcxszDto = dao.getXtzyByZylj(lbcxszDto.getZylj());
		lbcxszDto.setZyid(t_lbcxszDto.getZyid());
		lbcxszDto.setFjd(t_lbcxszDto.getFjd());
		lbcxszDto.setZybt(t_lbcxszDto.getZybt());
		lbcxszDto.setXssx(t_lbcxszDto.getXssx());
		lbcxszDto.setCdcc(t_lbcxszDto.getCdcc());
		lbcxszDto.setGnm(t_lbcxszDto.getGnm());
		lbcxszDto.setBtsx(t_lbcxszDto.getBtsx());
		//查询资源操作表
		List<LbcxszDto> zyczbList = dao.getZyczbByZyid(lbcxszDto.getZyid());
		List<String> czdms = new ArrayList<>();
		for (LbcxszDto dto : zyczbList) {
			if (dto.getCzdm() != null && !"".equals(dto.getCzdm())) {
				czdms.add(dto.getCzdm());
			}
			if (StringUtil.isBlank(lbcxszDto.getCzmc())) {
				lbcxszDto.setCzmc(dto.getCzmc());
			} else {
				lbcxszDto.setCzmc(lbcxszDto.getCzmc() + "," + dto.getCzmc());
			}
		}
		lbcxszDto.setCzdms(czdms);
		return lbcxszDto;
	}
	
	/**
	 * 插入列表查询设置
	 */
    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean insertDto(LbcxszDto lbcxszDto){
    	//判断zyid是否重复
    	List<LbcxszDto> selectToZyid = dao.selectToZyid(lbcxszDto);
    	if(selectToZyid!=null && !selectToZyid.isEmpty()){
    		return false;
    	}
    	//判断ywid是否重复
    	List<LbcxszDto> selectToYwid = dao.selectToYwid(lbcxszDto);
    	if(selectToYwid!=null && !selectToYwid.isEmpty()){
    		return false;
    	}
    	//将数据同步至系统资源表
    	lbcxszDto.setZylj("/common/list/pageListSetting?ywid="+lbcxszDto.getYwid());
    	int insertXtzy = dao.insertXtzy(lbcxszDto);
    	if(insertXtzy < 1){
    		return false;
    	}
    	//将数据同步至资源操作表
    	if(lbcxszDto.getCzdms()!=null && !lbcxszDto.getCzdms().isEmpty()){
    		for (int i = 0; i < lbcxszDto.getCzdms().size(); i++) {
    			lbcxszDto.setCzdm(lbcxszDto.getCzdms().get(i));
    			lbcxszDto.setDyym("/common/list/"+lbcxszDto.getCzdms().get(i)+"ListSetting");
    			int insertZyczb = dao.insertZyczb(lbcxszDto);
        		if(insertZyczb < 1){
            		return false;
            	}
			}
    	}
    	//将数据同步至查询条件
    	lbcxszDto.setXh_tj("1");
    	boolean insertByLbcxsz_tj = lbcxtjszService.insertByLbcxsz(lbcxszDto);
    	if(!insertByLbcxsz_tj){
    		return false;
    	}
    	//将数据同步至字段
    	boolean insertByLbcxsz_zd= lbzdszSerice.insertByLbcxsz(lbcxszDto);
    	if(!insertByLbcxsz_zd){
    		return false;
    	}
    	//将数据增加到列表查询设置表
    	lbcxszDto.setXh("1");
    	int insert = dao.insert(lbcxszDto);
		return insert >= 1;
	}
    
    /**
	 * 修改列表查询设置
	 */
    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean updateDto(LbcxszDto lbcxszDto) {
    	//判断zyid是否重复
    	List<LbcxszDto> selectToModZyid = dao.selectToModZyid(lbcxszDto);
    	if(selectToModZyid!=null && !selectToModZyid.isEmpty()){
    		return false;
    	}
    	//判断ywid是否重复
    	List<LbcxszDto> selectToModYwid = dao.selectToModYwid(lbcxszDto);
    	if(selectToModYwid!=null && !selectToModYwid.isEmpty()){
    		return false;
    	}
    	//将数据更新至系统资源表
    	lbcxszDto.setZylj("/common/list/pageListSetting?ywid="+lbcxszDto.getYwid());
    	int updateXtzy = dao.updateXtzy(lbcxszDto);
    	if(updateXtzy < 1){
    		return false;
    	}
    	//将数据更新至资源操作表
    	if( lbcxszDto.getCzdms()!=null && !lbcxszDto.getCzdms().isEmpty()){
    		String tempZyid = lbcxszDto.getZyid();
			lbcxszDto.setZyid(lbcxszDto.getPrezyid());
			int deleteZyczb = dao.deleteZyczb(lbcxszDto);
			if(deleteZyczb < 1){
        		return false;
        	}
			lbcxszDto.setZyid(tempZyid);
    		for (int i = 0; i < lbcxszDto.getCzdms().size(); i++) {
    			lbcxszDto.setCzdm(lbcxszDto.getCzdms().get(i));
    			lbcxszDto.setDyym("/common/list/"+lbcxszDto.getCzdms().get(i)+"ListSetting");
    			int updateZyczb = dao.insertZyczb(lbcxszDto);
            	if(updateZyczb < 1){
            		return false;
            	}
			}
    	}
    	//将数据更新至查询条件
    	boolean updateByLbcxsz_tj = lbcxtjszService.updateByLbcxsz(lbcxszDto);
    	if(!updateByLbcxsz_tj){
    		return false;
    	}
    	//将数据更新至字段
    	boolean updateByLbcxsz_zd = lbzdszSerice.updateByLbcxsz(lbcxszDto);
    	if(!updateByLbcxsz_zd){
    		return false;
    	}
    	//将数据更新至列表查询设置表
    	int update = dao.update(lbcxszDto);
		return update >= 1;
	}
    
    /**
	 * 删除列表查询设置
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean deleteDto(LbcxszDto lbcxszDto) {
		List<String> ids= lbcxszDto.getIds();
		List<String> zyids = new ArrayList<>();
		for (String ywid : ids) {
			lbcxszDto.setZylj("/common/list/pageListSetting?ywid="+ywid);
			LbcxszDto t_lbcxszDto = dao.getXtzyByZylj(lbcxszDto.getZylj());
			zyids.add(t_lbcxszDto.getZyid());
		}
		lbcxszDto.setZyids(zyids);
		//删除系统资源表信息
		int deleteXtzy = dao.deleteXtzy(lbcxszDto);
		if(deleteXtzy < 1){
    		return false;
    	}
		//删除资源操作表信息
		int deleteZyczb = dao.deleteZyczb(lbcxszDto);
		if(deleteZyczb < 1){
    		return false;
    	}
		//删除列表查询条件设置表信息
		boolean upateLbcxtjszScbj = lbcxtjszService.upateLbcxtjszScbj(lbcxszDto);
		if(!upateLbcxtjszScbj){
    		return false;
    	}
		//删除列表字段设置表信息
		boolean deleteLbzdszb = lbzdszSerice.deleteLbzdszb(lbcxszDto);
		if(!deleteLbzdszb){
    		return false;
    	}
		//删除列表查询设置表信息
		int updateLbcxszScbj = dao.updateLbcxszScbj(lbcxszDto);
		return updateLbcxszScbj >= 1;
	}

	/**
	 * 获取操作代码同时根据操作代码名称标识操作代码信息
	 */
	@Override
	public List<LbcxszDto> getAllCzdmAndChecked(LbcxszDto lbcxszDto) {
		if(lbcxszDto.getYwid() != null && !"".equals(lbcxszDto.getYwid())){
			lbcxszDto.setZylj("/common/list/pageListSetting?ywid="+lbcxszDto.getYwid());
			LbcxszDto t_lbcxszDto = dao.getXtzyByZylj(lbcxszDto.getZylj());
			lbcxszDto.setZyid(t_lbcxszDto.getZyid());
		}
		List<String> czdms = new ArrayList<>();
		//返回值czdm表示已选操作，czdm_cz表示全部操作
		List<LbcxszDto> listChecked = dao.getAllCzdmAndChecked(lbcxszDto);
		for (LbcxszDto dto : listChecked) {
			if (dto.getCzdm() != null && !"".equals(dto.getCzdm())) {
				czdms.add(dto.getCzdm_cz());
			}
		}
		for (LbcxszDto dto : listChecked) {
			dto.setCzdms(czdms);
		}
		return listChecked;
	}
}
