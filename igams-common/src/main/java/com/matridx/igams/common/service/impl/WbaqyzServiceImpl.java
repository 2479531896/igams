package com.matridx.igams.common.service.impl;

import com.matridx.igams.common.dao.entities.WbaqyzDto;
import com.matridx.igams.common.dao.entities.WbaqyzModel;
import com.matridx.igams.common.dao.post.IWbaqyzDao;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IWbaqyzService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WbaqyzServiceImpl extends BaseBasicServiceImpl<WbaqyzDto, WbaqyzModel, IWbaqyzDao> implements IWbaqyzService {
    /**
     * 获取列表数据
     */

    @Override
    public List<WbaqyzDto> getPageWbaqyzDtoList(WbaqyzDto wbaqyzDto) {
        return dao.getPageWbaqyzDtoList(wbaqyzDto);
    }

	/**
	 * 根据伙伴id获取伙伴安全验证
	 */
	@Override
	public List<WbaqyzDto> queryByHbid(String hbid) {
		return dao.queryByHbid(hbid);
	}

	@Override
	public boolean updateDtoByIndex(WbaqyzDto wbaqyzDto) {
		return dao.updateDtoByIndex(wbaqyzDto)>=0;
	}

}
