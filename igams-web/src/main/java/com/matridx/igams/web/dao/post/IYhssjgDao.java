package com.matridx.igams.web.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.web.dao.entities.YhssjgDto;
import com.matridx.igams.web.dao.entities.YhssjgModel;

@Mapper
public interface IYhssjgDao extends BaseBasicDao<YhssjgDto, YhssjgModel>{

	/**
	 * 查询用户所属机构
	 * @return
	 */
    List<YhssjgDto> getYhssjgList();

	/**
	 * 根据用户ID删除用户所属机构
	 * @param yhssjgDto
	 * @return
	 */
    int deleteYhssjgByYhid(YhssjgDto yhssjgDto);
	
	/**
	 * 根据用户ids查询用户所属机构信息
	 * @param yhssjgDto
	 * @return
	 */
    List<YhssjgDto> getYhssjgByIds(YhssjgDto yhssjgDto);

	/**
	 * 根据用户ID获取机构信息
	 * @param yhid
	 * @return
	 */
    List<YhssjgDto> getJgxxByYhid(String yhid);
}
