package com.matridx.igams.storehouse.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.storehouse.dao.entities.*;
import com.matridx.igams.storehouse.dao.post.ILyrkxxDao;
import com.matridx.igams.storehouse.service.svcinterface.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LyrkxxServiceImpl extends BaseBasicServiceImpl<LyrkxxDto, LyrkxxModel, ILyrkxxDao> implements ILyrkxxService {
	/**
	 * 批量新增
	 * @param list
	 * @return
	 */
	public boolean insertList(List<LyrkxxDto> list){
		return dao.insertList(list);
	}

}
