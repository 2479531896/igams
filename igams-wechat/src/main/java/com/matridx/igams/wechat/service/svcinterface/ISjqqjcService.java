package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.SjqqjcDto;
import com.matridx.igams.wechat.dao.entities.SjqqjcModel;
import com.matridx.igams.wechat.dao.entities.SjxxDto;

import java.util.List;

public interface ISjqqjcService extends BaseBasicService<SjqqjcDto, SjqqjcModel>
{

	/**
	 * 根据送检信息新增前期检测
	 * 
	 * @param sjxxDto
	 * @return
	 */
    boolean insertBySjxx(SjxxDto sjxxDto);

	/**
	 * 查询前期检测信息
	 * @param sjid
	 * @return
	 */
    List<SjqqjcDto> getJcz(String sjid);
}
