package com.matridx.crf.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.crf.web.dao.entities.NdzxxjlDto;
import com.matridx.crf.web.dao.entities.NdzdmxqDto;
import com.matridx.crf.web.dao.entities.NdzdmxqModel;
import com.matridx.crf.web.dao.post.INdzdmxqDao;
import com.matridx.crf.web.service.svcinterface.INdzdmxqService;

@Service
public class NdzdmxqServiceImpl extends BaseBasicServiceImpl<NdzdmxqDto, NdzdmxqModel, INdzdmxqDao> implements INdzdmxqService{
	@Autowired
	private INdzdmxqDao ndzdmxqdao;
	@Override
	public List<NdzdmxqDto> getSjz(String ndzjlid){
		return ndzdmxqdao.getSjz(ndzjlid);
		
	}
	/*
	 * 保存动脉血气
	 */
	@Override
	public void saveDmxq(List<NdzdmxqDto> dmxqs, NdzxxjlDto ndz) {
		//先删除已存在的动脉血气
		ndzdmxqdao.deleteByNdz(ndz);
		//删除后所有的数据都是新增
		if(dmxqs!=null&&dmxqs.size()>0) {
			 for (int i = dmxqs.size()-1; i >=0; i--) {
	            	if(StringUtil.isBlank(dmxqs.get(i).getSjz()))
	            	{
	            		dmxqs.remove(i);
	            		continue;
	            	}
	            	dmxqs.get(i).setNdzjlid(ndz.getNdzjlid());
				}
	            if(dmxqs.size() > 0) {
				 ndzdmxqdao.insertDmxq(dmxqs);
	            }
		}
	}

}
