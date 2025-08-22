package com.matridx.server.wechat.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.server.wechat.dao.entities.YhqtxxDto;
import com.matridx.server.wechat.dao.entities.HbqxDto;
import com.matridx.server.wechat.dao.entities.HbqxModel;
import com.matridx.server.wechat.dao.post.IHbqxDao;
import com.matridx.server.wechat.service.svcinterface.IHbqxService;
import com.matridx.server.wechat.service.svcinterface.IYhqtxxService;

@Service
public class HbqxServiceImpl extends BaseBasicServiceImpl<HbqxDto, HbqxModel, IHbqxDao> implements IHbqxService{
	
	@Autowired
	IYhqtxxService yhqtxxService;
	/**
	 * 通过用户id查询对应的伙伴id
	 * @param yhid
	 * @return
	 */
//	@Override
//	public List<String> getHbidByYhid(String yhid){
//		// TODO Auto-generated method stub
//		return dao.getHbidByYhid(yhid);
//	}

	/**
	 * 添加伙伴权限
	 * @param hbqxDto
	 * @return
	 * @throws BusinessException 
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertHbqx(HbqxDto hbqxDto,YhqtxxDto yhqtxxDto) throws BusinessException {
		boolean isSuccess=yhqtxxService.update(yhqtxxDto);
		if(!isSuccess) {
			throw new BusinessException("msg","用户其他信息添加失败！");
		}
		//先删除已有伙伴权限信息
		if(hbqxDto.getIds()!=null && hbqxDto.getIds().size()>0) {
			dao.delyhqxxx(hbqxDto);
			boolean result=dao.insertHbqx(hbqxDto);
			if(!result) {
				throw new BusinessException("msg","伙伴权限添加失败！");
			}
		}
		return true;
	}
	
	/**
	 * 查询伙伴权限
	 * @param hbqxDto
	 * @return
	 */
	public List<HbqxDto> getHbqxxx(HbqxDto hbqxDto){
		return dao.getHbqxxx(hbqxDto);
	}

	@Override
	public List<String> getHbidByYhid(String yhid) {
		// TODO Auto-generated method stub
		return null;
	}

	
	/**
	 * 通过用户id 获取伙伴信息
	 * @param yhid
	 * @return
	 */
	@Override
	public List<HbqxDto> getHbxxByYhid(String yhid)
	{
		// TODO Auto-generated method stub
		return dao.getHbxxByYhid(yhid);
	}

}
