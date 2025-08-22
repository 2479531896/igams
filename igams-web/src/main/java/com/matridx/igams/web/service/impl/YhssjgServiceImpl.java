package com.matridx.igams.web.service.impl;

import java.util.List;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.web.dao.entities.YhssjgDto;
import com.matridx.igams.web.dao.entities.YhssjgModel;
import com.matridx.igams.web.dao.post.IYhssjgDao;
import com.matridx.igams.web.service.svcinterface.IYhssjgService;

@Service
public class YhssjgServiceImpl extends BaseBasicServiceImpl<YhssjgDto, YhssjgModel, IYhssjgDao> implements IYhssjgService{

	@Autowired(required=false)
    private AmqpTemplate amqpTempl;
	@Value("${matridx.rabbit.flg:}")
	private String prefixFlg;
	@Value("${matridx.rabbit.preflg:}")
	private String preRabbitFlg;
	@Override
	public List<YhssjgDto> getYhssjgList() {
		return dao.getYhssjgList();
	}

	/**
	 * 根据用户ID删除用户所属机构
	 * @param yhssjgDto
	 * @return
	 */
	@Override
	public boolean deleteYhssjgByYhid(YhssjgDto yhssjgDto) {
		int result=dao.deleteYhssjgByYhid(yhssjgDto);
		yhssjgDto.setPrefix(prefixFlg);
		if(result>0) {
			amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.yhssjg.del",JSONObject.toJSONString(yhssjgDto));
			return true;
		}
		return false;
	}
	
	/**
	 * 根据用户ids查询用户所属机构信息
	 * @param yhssjgDto
	 * @return
	 */
	public List<YhssjgDto> getYhssjgByIds(YhssjgDto yhssjgDto){
		return dao.getYhssjgByIds(yhssjgDto);
	}

	/**
	 * 根据用户ID获取机构信息
	 * @param yhid
	 * @return
	 */
	@Override
	public List<YhssjgDto> getJgxxByYhid(String yhid) {
		// TODO Auto-generated method stub
		return dao.getJgxxByYhid(yhid);
	}
}
