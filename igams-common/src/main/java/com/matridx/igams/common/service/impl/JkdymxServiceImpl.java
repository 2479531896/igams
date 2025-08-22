package com.matridx.igams.common.service.impl;

import com.matridx.igams.common.dao.entities.JkdymxDto;
import com.matridx.igams.common.dao.entities.JkdymxModel;
import com.matridx.igams.common.dao.post.IJkdymxDao;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IJkdymxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JkdymxServiceImpl extends BaseBasicServiceImpl<JkdymxDto, JkdymxModel, IJkdymxDao> implements IJkdymxService{

	/**
	 * 新增接口调用信息
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertJkdymxDto(JkdymxDto jkdymxDto) {
		// TODO Auto-generated method stub
		if(StringUtil.isBlank(jkdymxDto.getDyid())){
			jkdymxDto.setDyid(StringUtil.generateUUID());
		}
		int insert = 0;
		try {
			insert = dao.insert(jkdymxDto);
		} catch (Exception e) {
			return false;
		}
		return insert != 0;
	}

	/**
	 * 根据业务ID查询调用信息(报告)
	 */
	@Override
	public List<JkdymxDto> getListByYwid(String ywid) {
		// TODO Auto-generated method stub
		return dao.getListByYwid(ywid);
	}

	/**
	 * 根据dyid查询调用信息(报告)
	 */
	@Override
	public List<JkdymxDto> getPageJkdymxDtoList(JkdymxDto jkdymxDto){
		return dao.getPageJkdymxDtoList(jkdymxDto);
	}

	@Override
	public List<String> getSearchItems(String key){
		return dao.getSearchItems(key);
	}

	/**
	 * 查询调用信息(报告)
	 *
	 * @param jkdymxDto
	 * @return
	 */
	@Override
	public List<JkdymxDto> selectReportInfo(JkdymxDto jkdymxDto) {
		return dao.selectReportInfo(jkdymxDto);
	}


}
