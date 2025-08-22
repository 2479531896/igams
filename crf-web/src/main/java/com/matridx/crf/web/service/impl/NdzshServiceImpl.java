package com.matridx.crf.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.crf.web.dao.entities.NdzxxjlDto;
import com.matridx.crf.web.dao.entities.NdzdmxqDto;
import com.matridx.crf.web.dao.entities.NdzshDto;
import com.matridx.crf.web.dao.entities.NdzshModel;
import com.matridx.crf.web.dao.post.INdzshDao;
import com.matridx.crf.web.service.svcinterface.INdzshService;

@Service
public class NdzshServiceImpl extends BaseBasicServiceImpl<NdzshDto, NdzshModel, INdzshDao> implements INdzshService{
	@Autowired
	private INdzshDao ndzshdao;
	
	@Override
	public List<NdzshDto> getSjz(String ndzjlid){
		return ndzshdao.getSjz(ndzjlid);
		
	}
	/*
	 * 保存生化
	 */
	@Override
	public void saveSh(List<NdzshDto> shs, NdzxxjlDto ndz) {
		//先删除已存在的动脉血气
		ndzshdao.deleteByNdz(ndz);
		//删除后所有的数据都是新增
		if(shs!=null&&shs.size()>0) {
			 for (int i = shs.size()-1; i >=0; i--) {
	            	if(StringUtil.isBlank(shs.get(i).getSjz()))
	            	{
	            		shs.remove(i);
	            		continue;
	            	}
	            	shs.get(i).setNdzjlid(ndz.getNdzjlid());
				}
	            if(shs.size() > 0) {
				 ndzshdao.insertSh(shs);
	            }
		}
	}
}
