package com.matridx.server.wechat.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.server.wechat.dao.entities.SjbgsmDto;
import com.matridx.server.wechat.dao.entities.SjbgsmModel;
import com.matridx.server.wechat.dao.post.ISjbgsmDao;
import com.matridx.server.wechat.service.svcinterface.ISjbgsmService;

@Service
public class SjbgsmServiceImpl extends BaseBasicServiceImpl<SjbgsmDto, SjbgsmModel, ISjbgsmDao> implements ISjbgsmService{

	private Logger log = LoggerFactory.getLogger(SjbgsmServiceImpl.class);
	
	/**
	 * 保存送检报告说明
	 * @param sjbgsmDto
	 * @throws BusinessException 
	 */
	@Override
	public void receiveCommentInspection(SjbgsmDto sjbgsmDto) throws BusinessException {
		// TODO Auto-generated method stub
		dao.deleteBySjbgsmDto(sjbgsmDto);
		int result = dao.insert(sjbgsmDto);
		if(result == 0){
			log.error("送检信息结果保存未成功！");
			throw new BusinessException("","送检信息结果保存未成功！");
		}
	}

	/**
	 * 根据送检id查询送检报告说明信息
	 * @param sjbgsmdto
	 * @return
	 */
	public List<SjbgsmDto> selectSjbgBySjid(SjbgsmDto sjbgsmdto) {
		return dao.selectSjbgBySjid(sjbgsmdto);
	}

	/**
	 * 根据Dto删除信息
	 * @param sjbgsmDto
	 * @return
	 */
	@Override
	public boolean deleteBySjbgsmDto(SjbgsmDto sjbgsmDto) {
		// TODO Auto-generated method stub
		int result = dao.deleteBySjbgsmDto(sjbgsmDto);
		if(result == 0)
			return false;
		return true;
	}
}
