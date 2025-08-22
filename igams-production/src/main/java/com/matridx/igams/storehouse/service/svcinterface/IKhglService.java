package com.matridx.igams.storehouse.service.svcinterface;

import com.matridx.igams.storehouse.dao.entities.KhglDto;
import com.matridx.igams.storehouse.dao.entities.KhglModel;
import com.matridx.igams.common.service.BaseBasicService;


public interface IKhglService extends BaseBasicService<KhglDto, KhglModel>{
	/**
	 * 通过khdm查找
	 */
	KhglDto getKhglInfoByKhdm(KhglDto khglDto);
}
