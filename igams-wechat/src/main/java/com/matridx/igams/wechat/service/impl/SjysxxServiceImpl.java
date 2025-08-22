package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.SjysxxDto;
import com.matridx.igams.wechat.dao.entities.SjysxxModel;
import com.matridx.igams.wechat.dao.post.ISjysxxDao;
import com.matridx.igams.wechat.service.svcinterface.ISjysxxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SjysxxServiceImpl extends BaseBasicServiceImpl<SjysxxDto, SjysxxModel, ISjysxxDao> implements ISjysxxService{

	/**
	 * 根据送检信息新增医生信息
	 * @param sjxxDto
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertBySjxxDto(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		if(StringUtil.isBlank(sjxxDto.getYsid())){
			sjxxDto.setYsid(StringUtil.generateUUID());
		}
		int result = dao.insertBySjxxDto(sjxxDto);
        return result != 0;
    }

	/**
	 * 根据送检医生查询医生信息
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<SjysxxDto> selectSjysxxDtoBySjys(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		return dao.selectSjysxxDtoBySjys(sjxxDto);
	}

}
