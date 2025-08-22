package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.SjbgsmDto;
import com.matridx.igams.wechat.dao.entities.SjbgsmModel;
import com.matridx.igams.wechat.dao.post.ISjbgsmDao;
import com.matridx.igams.wechat.service.svcinterface.ISjbgsmService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SjbgsmServiceImpl extends BaseBasicServiceImpl<SjbgsmDto, SjbgsmModel, ISjbgsmDao> implements ISjbgsmService{

	
	/**
	 * 根据送检id查询送检报告说明信息
	 * @param sjbgsmdto
	 * @return
	 */
	public List<SjbgsmDto> selectSjbgBySjid(SjbgsmDto sjbgsmdto) {
		return dao.selectSjbgBySjid(sjbgsmdto);
	}

	@Override
	public List<SjbgsmDto> selectBySjbgsmDto(SjbgsmDto sjbgsmdto) {
		return dao.selectBySjbgsmDto(sjbgsmdto);
	}

	/**
	 * 根据Dto删除信息
	 * @param sjbgsmDto
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean deleteBySjbgsmDto(SjbgsmDto sjbgsmDto) {
		// TODO Auto-generated method stub
		int result = dao.deleteBySjbgsmDto(sjbgsmDto);
        return result != 0;
    }
	/**
	 * 通过检测类型分组
	 * @param sjbgsmdto
	 * @return
	 */
	public List<SjbgsmDto> selectGroupByJclx(SjbgsmDto sjbgsmdto){
		return dao.selectGroupByJclx(sjbgsmdto);
	}
}
