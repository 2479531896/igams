package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.SjlczzDto;
import com.matridx.igams.wechat.dao.entities.SjlczzModel;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ISjlczzDao extends BaseBasicDao<SjlczzDto, SjlczzModel>{

	/**
	 * 根据送检信息新增临床症状
	 * @param sjlczzDtos
	 * @return
	 */
	int insertSjlczzDtos(List<SjlczzDto> sjlczzDtos);

	/**
	 * 根据送检ID删除临床症状
	 * @param sjxxDto
	 */
	boolean deleteBySjxx(SjxxDto sjxxDto);
	
	/**
	 * 根据送检信息查询临床症状
	 * @param sjid
	 * @return
	 */
    List<String>  getLczz(String sjid);
}
