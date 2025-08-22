package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.SjybztDto;
import com.matridx.igams.wechat.dao.entities.SjybztModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface ISjybztDao extends BaseBasicDao<SjybztDto, SjybztModel>{
	/**
	 * 新增标本状态
	 * @param sjybztDtos
	 * @return
	 */
    boolean insertYbzt(List<SjybztDto> sjybztDtos);
	
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
