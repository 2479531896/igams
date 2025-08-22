package com.matridx.crf.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.crf.web.dao.entities.NdzxxjlDto;
import com.matridx.crf.web.dao.entities.NdzxcgDto;
import com.matridx.crf.web.dao.entities.NdzxcgModel;
import com.matridx.crf.web.dao.entities.NdzyzzbDto;
import com.matridx.crf.web.dao.post.INdzxcgDao;
import com.matridx.crf.web.dao.post.INdzyzzbDao;
import com.matridx.crf.web.service.svcinterface.INdzxcgService;

@Service
public class NdzxcgServiceImpl extends BaseBasicServiceImpl<NdzxcgDto, NdzxcgModel, INdzxcgDao> implements INdzxcgService{
	@Autowired
	private INdzxcgDao ndzxcgDao;
	
	@Override
	public List<NdzxcgDto> getSjz(String ndzjlid){
		return ndzxcgDao.getSjz(ndzjlid);
		
	}
	/*
	 * 保存炎症指标
	 */
	@Override
	public void saveXcg(List<NdzxcgDto> xcgs, NdzxxjlDto ndz) {
		//先删除已存在的动脉血气
		ndzxcgDao.deleteByNdz(ndz);
		//删除后所有的数据都是新增
		if(xcgs!=null&&xcgs.size()>0) {
			 for (int i = xcgs.size()-1; i >=0; i--) {
	            	if(StringUtil.isBlank(xcgs.get(i).getSjz()))
	            	{
	            		xcgs.remove(i);
	            		continue;
	            	}
	            	xcgs.get(i).setNdzjlid(ndz.getNdzjlid());
				}
	            if(xcgs.size() > 0) {
	            	ndzxcgDao.insertXcg(xcgs);
	            }
		}
	}
}
