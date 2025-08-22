package com.matridx.igams.storehouse.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.storehouse.dao.entities.JccglDto;
import com.matridx.igams.storehouse.dao.entities.JccglModel;
import com.matridx.igams.storehouse.dao.post.IJccglDao;
import com.matridx.igams.storehouse.service.svcinterface.IJccglService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JccglServiceImpl extends BaseBasicServiceImpl<JccglDto, JccglModel, IJccglDao> implements IJccglService {
	
	/**
	 * 借出车列表
	 * @param
	 * @return
	 */
	public List<JccglDto> getJccDtoList(JccglDto jccglDto){
		//获取领料车数据
		return dao.getJccDtoList(jccglDto);
	}
}
