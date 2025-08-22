package com.matridx.igams.production.service.impl;


import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.production.dao.entities.SO_SODetailsDto;
import com.matridx.igams.production.dao.entities.SO_SODetailsModel;
import com.matridx.igams.production.dao.matridxsql.SO_SODetailsDao;
import com.matridx.igams.production.service.svcinterface.ISO_SODetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SO_SODetailsServiceImpl extends BaseBasicServiceImpl<SO_SODetailsDto, SO_SODetailsModel, SO_SODetailsDao> implements ISO_SODetailsService {

	@Override
	public List<SO_SODetailsDto> getSO_SODetailsInfo(SO_SODetailsDto so_soDetailsDto) {
		return dao.getSO_SODetailsInfo(so_soDetailsDto);
	}
}
