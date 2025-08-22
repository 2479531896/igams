package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.DlyzzsDto;
import com.matridx.igams.wechat.dao.entities.DlyzzsModel;
import com.matridx.igams.wechat.dao.post.IDlyzzsDao;
import com.matridx.igams.wechat.service.svcinterface.IDlyzzsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DlyzzsServiceImpl extends BaseBasicServiceImpl<DlyzzsDto, DlyzzsModel, IDlyzzsDao> implements IDlyzzsService{

	/**
	 * 批量查询毒力因子注释
	 * @return
	 */
	@Override
	public List<DlyzzsDto> queryByNames(DlyzzsDto dlyzzsDto) {
		return dao.queryByNames(dlyzzsDto);
	}

}
