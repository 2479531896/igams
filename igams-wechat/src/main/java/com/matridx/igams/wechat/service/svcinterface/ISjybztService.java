package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.SjybztDto;
import com.matridx.igams.wechat.dao.entities.SjybztModel;

import java.util.List;

public interface ISjybztService extends BaseBasicService<SjybztDto, SjybztModel>{
	/**
	 * 新增标本状态
	 * @param sjxxDto
	 * @return
	 */
    boolean insertYbzt(SjxxDto sjxxDto);

	/**
	 * 通过送检id查询标本状态
	 * @param sjid
	 * @return
	 */
    List<String> getZtBysjid(String sjid);

	/**
	 * 标本质量合格率统计SQL
	 * @param sjybztDto
	 * @return
	 */
    SjybztDto getPercentOfPass(SjybztDto sjybztDto);

	/**
	 * 对不合格的标本进行统计，按照标本类型区分开，统计每个类型的不合格率
	 * @param sjybztDto
	 * @return
	 */
	List<SjybztDto> getPercentOfUnPass(SjybztDto sjybztDto);

}
