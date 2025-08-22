package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.SjzmjgDto;
import com.matridx.igams.wechat.dao.entities.SjzmjgModel;

import java.util.List;

public interface ISjzmjgService extends BaseBasicService<SjzmjgDto, SjzmjgModel>{

	/**
	 * list新增送检自免结果
	 * @param list
	 * @return
	 */
    boolean insertSjzmjg(List<SjzmjgDto> list);
	
	/**
	 * 根据送检ID删除送检自免结果
	 * @param sjzmjgDto
	 * @return
	 */
    boolean deleteSjzmjg(SjzmjgDto sjzmjgDto);
}
