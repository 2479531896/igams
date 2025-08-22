package com.matridx.igams.web.service.impl;


import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.web.dao.entities.JsjcdwDto;
import com.matridx.igams.web.dao.entities.JsjcdwModel;
import com.matridx.igams.web.dao.entities.XtjsDto;
import com.matridx.igams.web.dao.post.IJsjcdwDao;
import com.matridx.igams.web.service.svcinterface.IJsjcdwService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class JsjcdwServiceImpl extends BaseBasicServiceImpl<JsjcdwDto, JsjcdwModel, IJsjcdwDao> implements IJsjcdwService{
	/**
	 * 新增角色检测单位
	 * @param xtjsDto
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertJsjcdw(XtjsDto xtjsDto){
		// TODO Auto-generated method stub
		dao.deleteById(xtjsDto.getJsid());
		if(!CollectionUtils.isEmpty(xtjsDto.getJsjcdwDtos())) {
			List<JsjcdwDto> list= new ArrayList<>();
			for (int i = 0; i < xtjsDto.getJsjcdwDtos().size(); i++){
				if(xtjsDto.getJsjcdwDtos().get(i).getJcdw()!=null) {
					JsjcdwDto jsjcdwDto=new JsjcdwDto();
					jsjcdwDto.setJsid(xtjsDto.getJsid());
					jsjcdwDto.setJcdw(xtjsDto.getJsjcdwDtos().get(i).getJcdw());
					list.add(jsjcdwDto);
				}
			}
            return dao.insertJsjcdw(list);
		}else {
			return true;
		}
	}

}
