package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.SjlczzDto;
import com.matridx.igams.wechat.dao.entities.SjlczzModel;
import com.matridx.igams.wechat.dao.entities.SjxxDto;

import java.util.List;

public interface ISjlczzService extends BaseBasicService<SjlczzDto, SjlczzModel>{

	/**
	 * 根据送检信息新增临床症状
	 * @param sjxxDto
	 * @return
	 */
	boolean insertBySjxx(SjxxDto sjxxDto);

	/**
	 * 根据送检信息新增临床症状
	 * @param sjxxDto
	 * @return
	 */
    boolean insertSjlczz(SjxxDto sjxxDto);

	/**
	 * 根据送检信息查询临床症状
	 * @param sjid
	 * @return
	 */
    List<String>  getLczz(String sjid);

}
