package com.matridx.igams.common.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.SpgwDto;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.dao.entities.SpgwcyModel;
import com.matridx.igams.common.dao.post.ISpgwcyDao;
import com.matridx.igams.common.service.svcinterface.ISpgwService;
import com.matridx.igams.common.service.svcinterface.ISpgwcyService;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class SpgwcyServiceImpl extends BaseBasicServiceImpl<SpgwcyDto, SpgwcyModel, ISpgwcyDao> implements ISpgwcyService{

	@Autowired
	ISpgwService spgwService;
    @Autowired(required=false)
    private AmqpTemplate amqpTempl;
    @Value("${matridx.rabbit.flg:}")
    private String prefixFlg;
	@Value("${matridx.rabbit.preflg:}")
	private String preRabbitFlg;
	@Override
	public List<SpgwcyDto> getPagedOptionalList(SpgwcyDto spgwcyDto) {
		return dao.getPagedOptionalList(spgwcyDto);
	}

	@Override
	public List<SpgwcyDto> getPagedSelectedList(SpgwcyDto spgwcyDto) {
		return dao.getPagedSelectedList(spgwcyDto);
	}

	@Override
	public boolean toSelected(SpgwcyDto spgwcyDto) {
		boolean result;
		//根据是否广播判断是否发送rabbitmq
		SpgwDto spgwDto=spgwService.getDtoById(spgwcyDto.getGwid());
		spgwcyDto.setPrefix(prefixFlg);
		//判断是否插入多条数据
		List<SpgwcyDto> spgwcyList = new ArrayList<>();
		if(!StringUtil.isNull(spgwcyDto.getJsids())){
			String jsids = spgwcyDto.getJsids();
			String yhids = spgwcyDto.getYhids();
			String[] yhidsArr = yhids.split(",");
		    String[] jsidsArr = jsids.split(",");
		    for (int i = 0; i < jsidsArr.length; i++) {
		    	SpgwcyDto t_spgwcyDto = new SpgwcyDto();
		    	t_spgwcyDto.setJsid(jsidsArr[i]);
		    	t_spgwcyDto.setYhid(yhidsArr[i]);
		    	t_spgwcyDto.setGwid(spgwcyDto.getGwid());
		    	t_spgwcyDto.setPrefix(prefixFlg);
		    	spgwcyList.add(t_spgwcyDto);
		    }
		}else{
			spgwcyList.add(spgwcyDto);
		}
		result=dao.toSelected(spgwcyList);
		if("1".equals(spgwDto.getSfgb()) && result) {
			amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.spgwcy.insert",JSONObject.toJSONString(spgwcyList));
		}
		return result;
	}

	@Override
	public boolean toOptional(SpgwcyDto spgwcyDto) {
		boolean result;
		//根据是否广播判断是否发送rabbitmq
		SpgwDto spgwDto=spgwService.getDtoById(spgwcyDto.getGwid());
		//判断是否删除多条数据
		List<SpgwcyDto> spgwcyList = new ArrayList<>();
		if(!StringUtil.isNull(spgwcyDto.getJsids())){
			String jsids = spgwcyDto.getJsids();
			String yhids = spgwcyDto.getYhids();
			String[] yhidsArr = yhids.split(",");
		    String[] jsidsArr = jsids.split(",");
		    for (int i = 0; i < jsidsArr.length; i++) {
		    	SpgwcyDto t_spgwcyDto = new SpgwcyDto();
		    	t_spgwcyDto.setJsid(jsidsArr[i]);
		    	t_spgwcyDto.setYhid(yhidsArr[i]);
		    	t_spgwcyDto.setGwid(spgwcyDto.getGwid());
		    	t_spgwcyDto.setPrefix(prefixFlg);
		    	spgwcyList.add(t_spgwcyDto);
		    }
		}else{
			spgwcyDto.setPrefix(prefixFlg);
			spgwcyList.add(spgwcyDto);
		}
		result=dao.toOptional(spgwcyList);
		
		if("1".equals(spgwDto.getSfgb()) && result) {
			amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.spgwcy.batchdel",JSONObject.toJSONString(spgwcyList));
		}
		return result;
	}

	@Override
	public List<SpgwcyDto> getDtoListWithDW(SpgwcyDto spgwcyDto) {
		return dao.getDtoListWithDW(spgwcyDto);
	}
}
