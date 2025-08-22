package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.DlyzzsDto;
import com.matridx.igams.wechat.dao.entities.DlyzzsModel;

import java.util.List;

public interface IDlyzzsService extends BaseBasicService<DlyzzsDto, DlyzzsModel>{

	/**
	 * 批量查询毒力因子注释
	 * @return
	 */
    List<DlyzzsDto> queryByNames(DlyzzsDto dlyzzsDto);

}
