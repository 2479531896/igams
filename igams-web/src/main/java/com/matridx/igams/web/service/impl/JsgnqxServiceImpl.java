package com.matridx.igams.web.service.impl;


import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.web.dao.entities.JsgnqxDto;
import com.matridx.igams.web.dao.entities.JsgnqxModel;
import com.matridx.igams.web.dao.entities.XtjsDto;
import com.matridx.igams.web.dao.post.IJsgnqxDao;
import com.matridx.igams.web.service.svcinterface.IJsgnqxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class JsgnqxServiceImpl extends BaseBasicServiceImpl<JsgnqxDto, JsgnqxModel, IJsgnqxDao> implements IJsgnqxService{

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertJsgnqx(XtjsDto xtjsDto){
		// TODO Auto-generated method stub
		if(!CollectionUtils.isEmpty(xtjsDto.getIds())) {
			JsgnqxDto jsgnqxDto=new JsgnqxDto();
			jsgnqxDto.setIds(xtjsDto.getIds());
			dao.delete(jsgnqxDto);
			List<JsgnqxDto> list= new ArrayList<>();
			for (int i = 0; i <xtjsDto.getIds().size(); i++){
				if(!CollectionUtils.isEmpty(xtjsDto.getJsgrqxDtos())) {
					for (int j = 0; j < xtjsDto.getJsgrqxDtos().size(); j++){
						if(!CollectionUtils.isEmpty(xtjsDto.getJsgrqxDtos().get(j).getJsgnqxs())) {
							for (int b = 0; b< xtjsDto.getJsgrqxDtos().get(j).getJsgnqxs().size(); b++){
								if(StringUtil.isNotBlank(xtjsDto.getJsgrqxDtos().get(j).getJsgnqxs().get(b).getXszd())) {
									JsgnqxDto jsgnqxDto2=new JsgnqxDto();
									jsgnqxDto2.setJsid(xtjsDto.getIds().get(i));
									jsgnqxDto2.setYwid(xtjsDto.getJsgrqxDtos().get(j).getYwid());
									jsgnqxDto2.setXszd(xtjsDto.getJsgrqxDtos().get(j).getJsgnqxs().get(b).getXszd());
									jsgnqxDto2.setXssx(xtjsDto.getJsgrqxDtos().get(j).getJsgnqxs().get(b).getXssx());
									list.add(jsgnqxDto2);
								}
							}
						}
					}
				}
			}
			if(!CollectionUtils.isEmpty(list)) {
                return dao.insertJsgnqx(list);
			}
		}
		return true;
	}

	
	/**
	 * 查询当前角色的权限
	 * @param jsid
	 * @return
	 */
	@Override
	public List<JsgnqxDto> getListById(String jsid){
		// TODO Auto-generated method stub
		return dao.getListById(jsid);
	}
	
}
