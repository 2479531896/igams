package com.matridx.server.wechat.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.server.wechat.dao.entities.WbcxDto;
import com.matridx.server.wechat.dao.entities.WbcxModel;
import com.matridx.server.wechat.dao.post.IWbcxDao;
import com.matridx.server.wechat.service.svcinterface.IWbcxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;

@Service
public class WbcxServiceImpl extends BaseBasicServiceImpl<WbcxDto, WbcxModel, IWbcxDao> implements IWbcxService{

	/** 
	 * 插入送检信息到数据库
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(WbcxDto wbcxDto){
		if(StringUtil.isBlank(wbcxDto.getWbcxid())){
			wbcxDto.setWbcxid(StringUtil.generateUUID());
		}
		int result = dao.insert(wbcxDto);
        return result != 0;
    }
	
	/**
	 * 新增外部程序
	 * @param wbcxDto
	 * @return
	 */
	@Override
	public boolean addSave(WbcxDto wbcxDto) {
		// TODO Auto-generated method stub
		DBEncrypt crypt = new DBEncrypt();
		if(StringUtil.isNotBlank(wbcxDto.getToken())){
			wbcxDto.setToken(crypt.eCode(wbcxDto.getToken()));
		}
		wbcxDto.setAppid(crypt.eCode(wbcxDto.getAppid()));
		wbcxDto.setSecret(crypt.eCode(wbcxDto.getSecret()));
		return insertDto(wbcxDto);
	}

	/**
	 * 修改外部程序
	 * @param wbcxDto
	 * @return
	 */
	@Override
	public boolean modSave(WbcxDto wbcxDto) {
		// TODO Auto-generated method stub
		DBEncrypt crypt = new DBEncrypt();
		if(StringUtil.isNotBlank(wbcxDto.getToken())){
			wbcxDto.setToken(crypt.eCode(wbcxDto.getToken()));
		}
		wbcxDto.setAppid(crypt.eCode(wbcxDto.getAppid()));
		wbcxDto.setSecret(crypt.eCode(wbcxDto.getSecret()));
		return update(wbcxDto);
	}
	
	/**
	 * 获取外部程序列表
	 */
	@Override 
	public List<WbcxDto> getPagedDtoList(WbcxDto wbcxDto){
		List<WbcxDto> t_List = dao.getPagedDtoList(wbcxDto);
		if(t_List != null && t_List.size() > 0){
			DBEncrypt crypt = new DBEncrypt();
			for (int i = 0; i < t_List.size(); i++) {
				if(StringUtil.isNotBlank(t_List.get(i).getToken())){
					t_List.get(i).setToken(crypt.dCode(t_List.get(i).getToken()));
				}
				t_List.get(i).setAppid(crypt.dCode(t_List.get(i).getAppid()));
				t_List.get(i).setSecret(crypt.dCode(t_List.get(i).getSecret()));
			}
		}
		return t_List;
	}

}
