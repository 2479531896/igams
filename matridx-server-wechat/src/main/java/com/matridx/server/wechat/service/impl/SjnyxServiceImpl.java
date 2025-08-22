package com.matridx.server.wechat.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.server.wechat.dao.entities.SjnyxDto;
import com.matridx.server.wechat.dao.entities.SjnyxModel;
import com.matridx.server.wechat.dao.entities.SjxxDto;
import com.matridx.server.wechat.dao.post.ISjnyxDao;
import com.matridx.server.wechat.service.svcinterface.ISjnyxService;

@Service
public class SjnyxServiceImpl extends BaseBasicServiceImpl<SjnyxDto, SjnyxModel, ISjnyxDao> implements ISjnyxService{

	private Logger log = LoggerFactory.getLogger(SjnyxServiceImpl.class);

	/**
	 * 保存本地送检耐药性至服务器
	 * @param sjnyxDtos
	 * @throws BusinessException 
	 */
	@Override
	public void receiveResistanceInspection(Map<String,Object> map) throws BusinessException {
		// TODO Auto-generated method stub
		String sjnyxstr=(String)map.get("sjnyxDtos");
        String sjid=(String)map.get("sjid");
        String jclx=(String)map.get("jclx");
        SjnyxDto sjnyxDto=new SjnyxDto();
        sjnyxDto.setSjid(sjid);
        sjnyxDto.setJclx(jclx);
        List<SjnyxDto> sjnyxDtos=JSON.parseArray(sjnyxstr,SjnyxDto.class);
		dao.deleteBySjnyxDto(sjnyxDto);
		boolean result = dao.insertBysjnyxDtos(sjnyxDtos);
		if(!result){
			log.error("送检信息结果保存未成功！");
			throw new BusinessException("","送检信息结果保存未成功！");
		}
	}

	/**
	 * 根据送检ID查询耐药信息
	 * @param sjxxDto
	 * @return
	 */
	public List<SjnyxDto> getNyxBySjid(SjxxDto sjxxDto) {
		return dao.getNyxBySjid(sjxxDto);
	}

	/**
	 * 根据Dto删除耐药信息
	 * @param sjnyxDto
	 * @return
	 */
	@Override
	public boolean deleteBySjnyxDto(SjnyxDto sjnyxDto) {
		// TODO Auto-generated method stub
		int result = dao.deleteBySjnyxDto(sjnyxDto);
		if(result == 0)
			return false;
		return true;
	}
}
