package com.matridx.las.netty.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.las.netty.dao.entities.YqztxxDto;
import com.matridx.las.netty.dao.entities.YqztxxModel;
import com.matridx.las.netty.dao.post.IYqztxxDao;
import com.matridx.las.netty.service.svcinterface.IYqztxxService;

@Service
public class YqztxxServiceImpl extends BaseBasicServiceImpl<YqztxxDto, YqztxxModel, IYqztxxDao> implements IYqztxxService{
  
	@Autowired
	private IYqztxxDao yqztxxDao;
	public void updateStYqztxx(String sbid,String yqzt) {
 		synchronized(this){
			YqztxxDto yqztxx = new YqztxxDto();
			yqztxx.setSbid(sbid);
			yqztxx.setYqzt(yqzt);
			yqztxxDao.updateStYqztxx(yqztxx);
		}
	}
	@Override
	public List<Map<String, String>> getAllYqztList() {
		
		return yqztxxDao.getAllYqztList();
	}
}
