package com.matridx.server.wechat.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.server.wechat.dao.entities.SjwzxxDto;
import com.matridx.server.wechat.dao.entities.SjwzxxModel;
import com.matridx.server.wechat.dao.entities.SjxxDto;
import com.matridx.server.wechat.dao.post.ISjwzxxDao;
import com.matridx.server.wechat.service.svcinterface.ISjwzxxService;

@Service
public class SjwzxxServiceImpl extends BaseBasicServiceImpl<SjwzxxDto, SjwzxxModel, ISjwzxxDao> implements ISjwzxxService{

	private Logger log = LoggerFactory.getLogger(SjxxServiceImpl.class);
	
	/**
	 * 批量新增送检物种信息
	 * @param sjwzxxDtos
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertBysjwzxxDtos(List<SjwzxxDto> sjwzxxDtos) {
		// TODO Auto-generated method stub
		return dao.insertBysjwzxxDtos(sjwzxxDtos);
	}

	/**
	 * 保存本地送检结果至服务器
	 * @param map
	 * @throws BusinessException 
	 */
	@Override
	public void receiveResultInspection(Map<String,Object> map) throws BusinessException {
		// TODO Auto-generated method stub
		String sjwzxxstr=(String)map.get("sjwzxxDtos");
        String sjid=(String)map.get("sjid");
        String jclx=(String)map.get("jclx");
        SjwzxxDto sjwzxxDto=new SjwzxxDto();
        sjwzxxDto.setSjid(sjid);
        sjwzxxDto.setJclx(jclx);
        List<SjwzxxDto> sjwzxxDtos=JSON.parseArray(sjwzxxstr,SjwzxxDto.class);
		dao.deleteBysjwzxxDto(sjwzxxDto);
		boolean result = insertBysjwzxxDtos(sjwzxxDtos);
		if(!result){
			log.error("送检信息结果保存未成功！");
			throw new BusinessException("","送检信息结果保存未成功！");
		}
	}

	/**
	 * 批量删除送检物种信息
	 * @param sjwzxxDto
	 * @return
	 */
	@Override
	public boolean deleteBysjwzxxDto(SjwzxxDto sjwzxxDto) {
		// TODO Auto-generated method stub
		int result = dao.deleteBysjwzxxDto(sjwzxxDto);
        return result != 0;
    }

	/**
	 * 送检物种类型统计(医生)
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<SjwzxxDto> getSpeciesStatis(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		return dao.getSpeciesStatis(sjxxDto);
	}
}
