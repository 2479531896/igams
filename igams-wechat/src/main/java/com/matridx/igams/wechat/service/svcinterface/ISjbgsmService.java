package com.matridx.igams.wechat.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.SjbgsmDto;
import com.matridx.igams.wechat.dao.entities.SjbgsmModel;

public interface ISjbgsmService extends BaseBasicService<SjbgsmDto, SjbgsmModel>{

	
	/**
	 * 根据送检id查询送检报告说明信息
	 * @param sjbgsmdto
	 * @return
	 */
	List<SjbgsmDto> selectSjbgBySjid(SjbgsmDto sjbgsmdto);

	/**
	 * 获取首次报告日期
	 * @param sjbgsmdto
	 * @return
	 */
	List<SjbgsmDto> selectBySjbgsmDto(SjbgsmDto sjbgsmdto);

	/**
	 * 根据Dto删除信息
	 * @param sjbgsmDto
	 * @return
	 */
	boolean deleteBySjbgsmDto(SjbgsmDto sjbgsmDto);

	/**
	 * 通过检测类型分组
	 * @param sjbgsmdto
	 * @return
	 */
	List<SjbgsmDto> selectGroupByJclx(SjbgsmDto sjbgsmdto);
}
