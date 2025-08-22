package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.NyjyzsglDto;
import com.matridx.igams.wechat.dao.entities.NyjyzsglModel;
import com.matridx.igams.wechat.dao.entities.WechatInspectionResistModel;

import java.util.List;

public interface INyjyzsglService extends BaseBasicService<NyjyzsglDto, NyjyzsglModel>{

	/**
	 * 批量新增耐药基因注释时
	 * @param list
	 * @return
	 */
    boolean insertByListDto(List<NyjyzsglDto> list);
	
	/**
	 * 批量查询耐药基因注释时
	 * @param nyjyzsglDto
	 * @return
	 */
    List<NyjyzsglDto> queryByIds(NyjyzsglDto nyjyzsglDto);
	
	/**
	 * 批量新增耐药基因注释时
	 * @param list
	 * @return
	 */
    boolean insertByList(List<WechatInspectionResistModel> list);
	
	/**
	 * 批量更新耐药基因注释时
	 * @param list
	 * @return
	 */
    boolean updateByList(List<WechatInspectionResistModel> list);
}
