package com.matridx.server.wechat.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.SjqqjcDto;
import com.matridx.server.wechat.dao.entities.SjqqjcModel;
import com.matridx.server.wechat.dao.entities.SjxxDto;

@Mapper
public interface ISjqqjcDao extends BaseBasicDao<SjqqjcDto, SjqqjcModel>{

	/**
	 * 根据页面的输入列表登录数据库
	 * @param sjqqjcDtos
	 * @return
	 */
    int insertSjqqjcDtos(List<SjqqjcDto> sjqqjcDtos);
	
	/**
	 * 根据送检ID删除检测项目
	 * @param sjxxDto
	 */
	int deleteBySjxx(SjxxDto sjxxDto);
	
	/**
	 * 结合基础数据，获取前期检测项目信息清单
	 * @param sjqqjcDto
	 * @return
	 */
    List<SjqqjcDto> getDtoListByJcsj(SjqqjcDto sjqqjcDto);

    List<SjqqjcDto> getJcz(String sjid);
}
