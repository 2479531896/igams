package com.matridx.igams.storehouse.service.impl;

import com.matridx.igams.storehouse.dao.entities.KhglDto;
import com.matridx.igams.storehouse.dao.entities.KhglModel;
import com.matridx.igams.storehouse.dao.post.IKhglDao;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.storehouse.service.svcinterface.IKhglService;
import org.springframework.stereotype.Service;


@Service
public class KhglServiceImpl extends BaseBasicServiceImpl<KhglDto, KhglModel, IKhglDao> implements IKhglService {


	@Override
	public KhglDto getKhglInfoByKhdm(KhglDto khglDto) {
		return dao.getKhglInfoByKhdm(khglDto);
	}
}
