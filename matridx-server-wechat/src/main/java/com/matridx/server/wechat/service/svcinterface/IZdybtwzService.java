package com.matridx.server.wechat.service.svcinterface;

import java.util.List;
import java.util.Map;

import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.HzxxDto;
import com.matridx.server.wechat.dao.entities.ZdybtwzDto;
import com.matridx.server.wechat.dao.entities.ZdybtwzModel;

public interface IZdybtwzService extends BaseBasicService<ZdybtwzDto, ZdybtwzModel>{
	
	/**
	 * 点击保存按钮保存附件信息和标题位置信息
	 * @param list
	 * @param fjcfbDto
	 * @param faid
	 * @return
	 */
    boolean SaveImageAndWz(List<Map<String, Map<String, String>>> list, FjcfbDto fjcfbDto, String faid);

	/**
	 * 学习结果处理
	 * @param studyResult
	 * @param hzxxDto
	 * @return
	 */
	boolean dealStudy(Map<String, Map<String, String>> studyResult, HzxxDto hzxxDto);

	/**
	 * 根据方案id查询当前已应用方案的自定义位置信息
	 * @param zdybtwzDto
	 * @return
	 */
    List<ZdybtwzDto> getZdybtwzByFaid(ZdybtwzDto zdybtwzDto);
} 
