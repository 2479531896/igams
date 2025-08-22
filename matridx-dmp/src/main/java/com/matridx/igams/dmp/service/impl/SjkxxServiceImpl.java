package com.matridx.igams.dmp.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.dmp.dao.entities.SjkxxDto;
import com.matridx.igams.dmp.dao.entities.SjkxxModel;
import com.matridx.igams.dmp.dao.entities.ZyxxDto;
import com.matridx.igams.dmp.dao.post.ISjkxxDao;
import com.matridx.igams.dmp.service.svcinterface.ISjkxxService;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class SjkxxServiceImpl extends BaseBasicServiceImpl<SjkxxDto, SjkxxModel, ISjkxxDao> implements ISjkxxService{

	/**
	 * 查询数据库信息
	 * @return
	 */
	@Override
	public List<SjkxxDto> getSjkxxDtoList() {
		// TODO Auto-generated method stub
		return dao.getSjkxxDtoList();
	}

	/**
	 * 根据资源信息Dto新增数据库信息
	 * @param zyxxDto
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertSjkxxDtoByZyxxDto(ZyxxDto zyxxDto) {
		// TODO Auto-generated method stub
		zyxxDto.setSjkid(StringUtil.generateUUID());
		int result = dao.insertSjkxxDtoByZyxxDto(zyxxDto);
        return result != 0;
    }

}
