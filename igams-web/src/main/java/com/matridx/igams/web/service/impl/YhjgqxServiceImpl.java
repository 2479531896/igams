package com.matridx.igams.web.service.impl;

import java.util.List;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.web.dao.entities.YhjgqxDto;
import com.matridx.igams.web.dao.entities.YhjgqxModel;
import com.matridx.igams.web.dao.post.IYhjgqxDao;
import com.matridx.igams.web.service.svcinterface.IYhjgqxService;

@Service
public class YhjgqxServiceImpl extends BaseBasicServiceImpl<YhjgqxDto, YhjgqxModel, IYhjgqxDao> implements IYhjgqxService{

	@Autowired(required=false)
    private AmqpTemplate amqpTempl;
	@Value("${matridx.rabbit.flg:}")
	private String prefixFlg;
	@Value("${matridx.rabbit.preflg:}")
	private String preRabbitFlg;
	
	@Override
	public List<YhjgqxDto> getYhjgqxList() {
		return dao.getYhjgqxList();
	}

	/**
	 * 根据Yhid删除用户机构权限信息
	 * @param yhjgqxDto
	 * @return
	 */
	@Override
	public boolean deleteJgqxxxByYhid(YhjgqxDto yhjgqxDto) {
		int result=dao.deleteJgqxxxByYhid(yhjgqxDto);
		yhjgqxDto.setPrefix(prefixFlg);
		if(result>0) {
			amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.yhjgqx.del",JSONObject.toJSONString(yhjgqxDto));
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 根据ids查询用户机构权限信息
	 * @param yhjgqxDto
	 * @return
	 */
	@Override
	public List<YhjgqxDto> selectYhqxjg(YhjgqxDto yhjgqxDto){
		return dao.selectYhqxjg(yhjgqxDto);
	}

	@Override
	public YhjgqxDto getJgidByYhid(String yhid){
		// TODO Auto-generated method stub
		return dao.getJgidByYhid(yhid);
	}

	/**
	 * 通过yhid和jsid 查询机构
	 * @param yhjgqxDto
	 * @return
	 */
	@Override
	public List<YhjgqxDto> getListByjsid(YhjgqxDto yhjgqxDto){
		// TODO Auto-generated method stub
		return dao.getListByjsid(yhjgqxDto);
	}
	
	
}
