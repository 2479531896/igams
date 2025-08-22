package com.matridx.server.wechat.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.SjqqjcDto;
import com.matridx.server.wechat.dao.entities.SjqqjcModel;
import com.matridx.server.wechat.dao.entities.SjxxDto;

public interface ISjqqjcService extends BaseBasicService<SjqqjcDto, SjqqjcModel>{
	/**
	 * 根据送检信息新增前期检测
	 * @param sjxxDto
	 * @return
	 */
    boolean insertBySjxx(SjxxDto sjxxDto);
	
	/**
	 * 根据页面的输入列表更新前期检测
	 * @param sjxxDto
	 * @return
	 */
	//public int updateBySjxx(SjxxDto sjxxDto);
	
	/**
	 * 获取前期检测的清单
	 * @param sjqqjcDto
	 * @return
	 */
    List<SjqqjcDto> getDtoListByJcsj(SjqqjcDto sjqqjcDto);

	/**
	 * 根据送检信息新增前期检测
	 * @param sjxxDto
	 * @return
	 */
    boolean insertBySjxxDto(SjxxDto sjxxDto);
	/**
	 * 查询前期检测信息
	 * @param sjid
	 * @return
	 */
    List<SjqqjcDto> getJcz(String sjid);
}
