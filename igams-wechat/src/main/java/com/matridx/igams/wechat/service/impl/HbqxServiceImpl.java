package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.HbqxDto;
import com.matridx.igams.wechat.dao.entities.HbqxModel;
import com.matridx.igams.wechat.dao.entities.YhqtxxDto;
import com.matridx.igams.wechat.dao.post.IHbqxDao;
import com.matridx.igams.wechat.service.svcinterface.IHbqxService;
import com.matridx.igams.wechat.service.svcinterface.IYhqtxxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class HbqxServiceImpl extends BaseBasicServiceImpl<HbqxDto, HbqxModel, IHbqxDao> implements IHbqxService{
	
	@Autowired
	IYhqtxxService yhqtxxService;
	/**
	 * 通过用户id查询对应的伙伴id
	 * @param yhid
	 * @return
	 */
	@Override
	public List<String> getHbidByYhid(String yhid){
		// TODO Auto-generated method stub
		return dao.getHbidByYhid(yhid);
	}

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
			isSuccess=yhqtxxService.insert(yhqtxxDto);
			if (!isSuccess){
				throw new BusinessException("msg","用户其他信息添加失败！");
			}
		}
		//先删除已有伙伴权限信息
		dao.delyhqxxx(hbqxDto);
		if(hbqxDto.getIds()!=null && hbqxDto.getIds().size()>0) {
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
	
	/**
	 * 通过用户id 获取伙伴Group信息
	 * @param yhid
	 * @return
	 */
	@Override
	public List<HbqxDto> getHbxxGroupByYhid(String yhid)
	{
		// TODO Auto-generated method stub
		return dao.getHbxxGroupByYhid(yhid);
	}

	/**
	 * 设置上级的伙伴权限
	 * @param map
	 */
	@Override
	public Boolean insertSjhbqx(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return dao.insertSjhbqx(map);
	}

	@Override
	public List<String> getHbmcByYhid(String id) {
		return dao.getHbmcByYhid(id);
	}


}
