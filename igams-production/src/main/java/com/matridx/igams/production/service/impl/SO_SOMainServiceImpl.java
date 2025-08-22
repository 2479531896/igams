package com.matridx.igams.production.service.impl;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.production.dao.entities.*;
import com.matridx.igams.production.dao.matridxsql.SO_SOMainDao;
import com.matridx.igams.production.service.svcinterface.ISO_SOMainService;
import org.springframework.stereotype.Service;

@Service
public class SO_SOMainServiceImpl extends BaseBasicServiceImpl<SO_SOMainDto, SO_SOMainModel, SO_SOMainDao> implements ISO_SOMainService {


	@Override
	public SO_SOMainDto getSO_SOMainInfo(SO_SOMainDto so_soMainDto) {
		return dao.getSO_SOMainInfoByID(so_soMainDto);
	}

	@Override
	public Integer getToTalNumber() {
		return dao.getToTalNumber();
	}
}
