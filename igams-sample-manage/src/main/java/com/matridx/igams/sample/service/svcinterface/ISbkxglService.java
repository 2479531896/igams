package com.matridx.igams.sample.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.sample.dao.entities.SbkxglDto;
import com.matridx.igams.sample.dao.entities.SbkxglModel;

public interface ISbkxglService extends BaseBasicService<SbkxglDto, SbkxglModel>{
	/**
	 * 根据所占用的位置，重新更新空闲表数据
	 */
    boolean updateLessSbkx(SbkxglDto sbkxglDto);
	
	/**
	 * 根据所调整的位置，重新更新空闲表数据
	 */
    boolean recountSbkx(String sbid);
}
