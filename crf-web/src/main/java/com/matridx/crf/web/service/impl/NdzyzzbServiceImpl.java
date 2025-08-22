package com.matridx.crf.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.crf.web.dao.entities.NdzxxjlDto;
import com.matridx.crf.web.dao.entities.NdzshDto;
import com.matridx.crf.web.dao.entities.NdzyzzbDto;
import com.matridx.crf.web.dao.entities.NdzyzzbModel;
import com.matridx.crf.web.dao.post.INdzyzzbDao;
import com.matridx.crf.web.service.svcinterface.INdzyzzbService;

@Service
public class NdzyzzbServiceImpl extends BaseBasicServiceImpl<NdzyzzbDto, NdzyzzbModel, INdzyzzbDao> implements INdzyzzbService{
	@Autowired
	private INdzyzzbDao ndzyzzbDao;
	
	@Override
	public List<NdzyzzbDto> getSjz(String ndzjlid){
		return ndzyzzbDao.getSjz(ndzjlid);
		
	}
	/*
	 * 保存炎症指标
	 */
	@Override
	public void saveYzzb(List<NdzyzzbDto> yzzbs, NdzxxjlDto ndz) {
		//先删除已存在的动脉血气
		ndzyzzbDao.deleteByNdz(ndz);
		//删除后所有的数据都是新增
		if(yzzbs!=null&&yzzbs.size()>0) {
			 for (int i = yzzbs.size()-1; i >=0; i--) {
	            	if(StringUtil.isBlank(yzzbs.get(i).getSjz()))
	            	{
	            		yzzbs.remove(i);
	            		continue;
	            	}
	            	yzzbs.get(i).setNdzjlid(ndz.getNdzjlid());
				}
	            if(yzzbs.size() > 0) {
	            	ndzyzzbDao.insertYzzb(yzzbs);
	            }
		}
	}
}
