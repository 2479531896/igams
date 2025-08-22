package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.LczzznglDto;
import com.matridx.igams.wechat.dao.entities.LczzznglModel;
import com.matridx.igams.wechat.dao.entities.WechatClinicalGuideModel;

import java.util.List;

public interface ILczzznglService extends BaseBasicService<LczzznglDto, LczzznglModel>{

	/**
	 * 根据指南ids获取临床症状指南信息
	 * @param lczzznglDto
	 * @return
	 */
    List<LczzznglDto> getListByIds(LczzznglDto lczzznglDto);
	
	/**
	 * 批量新增临床症状指南信息
	 * @param list
	 * @return
	 */
    boolean insertByListDto(List<LczzznglDto> list);
	
	/**
	 * 批量新增临床症状指南信息
	 * @param list
	 * @return
	 */
    boolean insertByList(List<WechatClinicalGuideModel> list);

	/**
	 * 批量更新临床症状指南信息
	 * @param list
	 * @return
	 */
    boolean updateByList(List<WechatClinicalGuideModel> guidelines);

	/**
	 * 根据znids查询list
	 * @param lczzznglDto
	 * @return
	 */
    List<LczzznglDto> getDtoListByIds(LczzznglDto lczzznglDto);

}
