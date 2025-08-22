package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.SjqqjcDto;
import com.matridx.igams.wechat.dao.entities.SjqqjcModel;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ISjqqjcDao extends BaseBasicDao<SjqqjcDto, SjqqjcModel>{

	/**
	 * 根据页面的输入列表登录数据库
	 * 
	 * @param sjqqjcDtos
	 * @return
	 */
	public int insertSjqqjcDtos(List<SjqqjcDto> sjqqjcDtos);

	/**
	 * 根据送检ID删除检测项目
	 * 
	 * @param sjxxDto
	 */
	int deleteBySjxx(SjxxDto sjxxDto);

	/**
	 * 添加送检前期检测
	 * 
	 * @param sjqqjcDtos
	 * @return
	 */
	public boolean insertSjqqjc(List<SjqqjcDto> sjqqjcDtos);
	
	/**
	 * 查询前期检测信息
	 * @param sjid
	 * @return
	 */
	public List<SjqqjcDto> getJcz(String sjid);

	/**
	 * 根据送检ID删除检测项目
	 * 
	 * @param sjxxDto
	 */
	public boolean deleteSjqqjc(SjxxDto sjxxDto);
}
