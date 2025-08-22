package com.matridx.server.wechat.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.server.wechat.dao.entities.SjxxjgDto;
import com.matridx.server.wechat.dao.entities.SjxxjgModel;
import com.matridx.server.wechat.dao.post.ISjxxjgDao;
import com.matridx.server.wechat.service.svcinterface.ISjxxjgService;

@Service
public class SjxxjgServiceImpl extends BaseBasicServiceImpl<SjxxjgDto, SjxxjgModel, ISjxxjgDao> implements ISjxxjgService{

	private Logger log = LoggerFactory.getLogger(SjxxjgServiceImpl.class);
	
	/**
	 * 同步送检详细审核结果
	 * @param sjxxjgDtos
	 * @throws BusinessException 
	 */
	@Override
	public void receiveDetailedInspection(List<SjxxjgDto> sjxxjgDtos) throws BusinessException {
		// TODO Auto-generated method stub
		boolean result=true;
		dao.deleteBySjxxjgDto(sjxxjgDtos.get(0));
		if(StringUtils.isNotBlank(sjxxjgDtos.get(0).getXxjgid()))
			result = dao.insertBySjxxjgDtos(sjxxjgDtos);
		if(!result){
			log.error("送检详细审核结果保存未成功！");
			throw new BusinessException("","送检详细审核结果保存未成功！");
		}
	}
	
	
	/**
	 * 根据送检ID查询fjdid为null的详细信息
	 * @param sjxxjgDto
	 * @return
	 */
	public List<SjxxjgDto> getxxjgByFjdidIsNull(SjxxjgDto sjxxjgDto){
		return dao.getxxjgByFjdidIsNull(sjxxjgDto);
	}
	
	/**
	 * 根据送检ID查询Species下详细结果
	 * @param list
	 * @return
	 */
	public List<SjxxjgDto> getxxInSpecies(List<SjxxjgDto> list){
		return dao.getxxInSpecies(list);
	}

	/**
	 * 根据送检ID查询Genus下详细结果
	 * @param list
	 * @return
	 */
	public List<SjxxjgDto> getxxInGenus(List<SjxxjgDto> list){
		return dao.getxxInGenus(list);
	}
	
	/**
	 * 根据检测类型计算D,R,C下详细结果总和
	 * @param sjxxjgDto
	 * @return
	 */
	public List<SjxxjgDto> getJclxCount(SjxxjgDto sjxxjgDto){
		return dao.getJclxCount(sjxxjgDto);
	}

	
	
}
