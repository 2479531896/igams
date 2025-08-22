package com.matridx.igams.common.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.dao.entities.GrlbzdszDto;
import com.matridx.igams.common.dao.entities.GrlbzdszModel;
import com.matridx.igams.common.dao.entities.LbzdszDto;
import com.matridx.igams.common.dao.post.IGrlbzdszDao;
import com.matridx.igams.common.service.svcinterface.IGrlbzdszService;

@Service
public class GrlbzdszServiceImpl extends BaseBasicServiceImpl<GrlbzdszDto, GrlbzdszModel, IGrlbzdszDao> implements IGrlbzdszService{

	/**
	 * 根据用户信息获取用户已选择的字段信息
	 */
	public List<LbzdszDto> getChoseList(GrlbzdszDto grlbzdszDto){
		return dao.getChoseList(grlbzdszDto);
	}
	
	/**
	 * 保存个人选择字段信息
	 */
	public boolean SaveChoseList(GrlbzdszDto grlbzdszDto) {
		dao.delete(grlbzdszDto);
		if(grlbzdszDto.getChoseListVue()!=null&&!grlbzdszDto.getChoseListVue().isEmpty()){
			dao.SaveChoseListVue(grlbzdszDto);
		}else{
			dao.SaveChoseList(grlbzdszDto);
		}

		return true;
	}
}
