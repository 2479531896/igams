package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.SjbgljxxDto;
import com.matridx.igams.wechat.dao.entities.SjbgljxxModel;

import java.util.List;

public interface ISjbgljxxService extends BaseBasicService<SjbgljxxDto, SjbgljxxModel>{
	/**
	 * 批量新增
	 */
    boolean insertList(List<SjbgljxxDto> sjbgljxxDtos);
}
