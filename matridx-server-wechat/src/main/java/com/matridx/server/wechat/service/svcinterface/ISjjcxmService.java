package com.matridx.server.wechat.service.svcinterface;

import java.util.List;
import java.util.Map;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.SjjcxmDto;
import com.matridx.server.wechat.dao.entities.SjjcxmModel;
import com.matridx.server.wechat.dao.entities.SjxxDto;

public interface ISjjcxmService extends BaseBasicService<SjjcxmDto, SjjcxmModel>{

	/**
	 * 根据送检信息新增检测项目
	 * @param sjxxDto
	 * @return
	 */
	boolean insertBySjxx(SjxxDto sjxxDto);

	/**
	 * 根据项目管理id修改
	 * @param list
	 * @return
	 */
	Boolean updateListNew(List<SjjcxmDto> list);

	/**
	 * 根据送检信息新增检测项目
	 * @param sjxxDto
	 * @return
	 */
	boolean syncInfo(SjxxDto sjxxDto) throws BusinessException;

	/**
	 * 根据送检信息修改检测项目
	 * @param sjxxDto
	 * @return
	 */
	//boolean updateBySjxx(SjxxDto sjxxDto);

	/**
	 * 获取检测项目的String清单
	 * @param sjjcxmDto
	 * @return
	 */
    List<String> getStringList(SjjcxmDto sjjcxmDto);
	/**
	 * 获取检测子项目的String清单
	 * @param sjjcxmDto
	 * @return
	 */
    List<String> getJczxmString(SjjcxmDto sjjcxmDto);

	/**
	 * 根据送检ID查询检测项目
	 * @param re_sjxxDto
	 * @return
	 */
	List<SjjcxmDto> selectJcxmBySjid(SjxxDto re_sjxxDto);


	/**
	 * 根据sjid获取送检检测项目以及对应项目收费标准
	 * @param sjid
	 * @return
	 */
	List<Map<String,Object>> getDetectionPayInfo(String sjid);

	/**
	 * 根据送检信息更新检测项目
	 * @param sjjcxmDtos
	 * @return
	 */
	int updateSjjcxmDtos(List<SjjcxmDto> sjjcxmDtos);

	/**
	 * 还原数据
	 * @param sjjcxmDtos
	 * @return
	 */
	int revertData(List<SjjcxmDto> sjjcxmDtos);
}
