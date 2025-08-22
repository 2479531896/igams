package com.matridx.crf.web.service.svcinterface;

import com.matridx.crf.web.dao.entities.NdzxxjlDto;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;
import java.util.Map;


import com.matridx.crf.web.dao.entities.BeanLdzxxFroms;
import com.matridx.crf.web.dao.entities.HzxxDto;
import com.matridx.crf.web.dao.entities.HzxxModel;

public interface IHzxxService extends BaseBasicService<HzxxDto, HzxxModel>{

	public boolean insertDto(BeanLdzxxFroms beanLdzxxFroms ,HzxxDto hzxxDto);//新增患者信息和报告记录信息
	public boolean updateDto(BeanLdzxxFroms beanLdzxxFroms ,HzxxDto hzxxDto);//修改患者信息和报告记录信息
	public boolean delHzxx(String hzid,String userId);//删除
	public List<Map<String, Object>> getParamForHzxx(HzxxDto hzxxDto) ;
	public List<Map> getHospitailList(HzxxDto hzxxDto);
	/**
	 * 获取上传的文件信息
	 * @param hzxxDto
	 * @return
	 */
	public List<FjcfbDto> getFjcfb(HzxxDto hzxxDto);
	/**
	 *报告上传保存
	 * @param hzxxDto
	 * @return
	 * @throws BusinessException
	 */
	public boolean uploadHzxxSave(HzxxDto hzxxDto) throws BusinessException;
	/**
	 * 获取单日上传的文件信息
	 * @param ndzxxjlDto
	 * @return
	 */
	public FjcfbDto getFjcfbByjlid(NdzxxjlDto ndzxxjlDto);
}
