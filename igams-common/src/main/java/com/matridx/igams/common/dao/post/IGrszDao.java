package com.matridx.igams.common.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.GrszDto;
import com.matridx.igams.common.dao.entities.GrszModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IGrszDao extends BaseBasicDao<GrszDto, GrszModel>{

	/**
	 * 根据条件查询个人设置信息
	 * grszDto
	 * 
	 */
	GrszDto selectGrszDtoByYhidAndSzlb(GrszDto grszDto);

	/**
	 * 根据条件修改个人设置信息
	 * grszDto
	 * 
	 */
	boolean updateByYhidAndSzlb(GrszDto grszDto);
	
	/**
	 * 通过用户id查询系统信息
	 * yhid
	 * 
	 */
	Map<String,String> getXtyhBYYhid(String yhid);

	/**
	 * 查询多个type的个人设置
	 * @param grszDto
	 * @return
	 */
	List<GrszDto> selectGrszListByYhidAndSzlb(GrszDto grszDto);

	/**
	 * @Description: 批量更新
	 * @param grszDtoList
	 * @return boolean
	 * @Author: 郭祥杰
	 * @Date: 2024/7/24 16:48
	 */
	boolean saveGrsz(List<GrszDto> grszDtoList);

	/**
	 * @Description: 批量删除
	 * @param grszDto
	 * @return boolean
	 * @Author: 郭祥杰
	 * @Date: 2024/7/25 10:01
	 */
	boolean delGrsz(GrszDto grszDto);


	/**
	 * @Description: 查询订阅消息
	 * @param grszDto
	 * @return java.util.List<com.matridx.igams.common.dao.entities.GrszDto>
	 * @Author: 郭祥杰
	 * @Date: 2024/11/21 9:44
	 */
	List<GrszDto> queryDyxx(GrszDto grszDto);


	/**
	 * @Description: 个人设置批量新增
	 * @param list
	 * @return int
	 * @Author: 郭祥杰
	 * @Date: 2024/11/21 10:58
	 */
	int insertList(List<GrszDto> list);

	/**
	 * @Description: 个人设置批量修改
	 * @param list
	 * @return int
	 * @Author: 郭祥杰
	 * @Date: 2024/11/21 10:58
	 */
	int updateList(List<GrszDto> list);

	/**
	 * @Description: 根据用户id查询伙伴list
	 * @param grszDto
	 * @return java.util.List<com.matridx.igams.common.dao.entities.GrszDto>
	 * @Author: 郭祥杰
	 * @Date: 2024/11/25 14:31
	 */
	List<GrszDto> queryHbxxByYhid(GrszDto grszDto);

	/**
	 * @Description: 根据用户id获取伙伴订阅消息
	 * @param yhid
	 * @return java.util.List<com.matridx.igams.common.dao.entities.GrszDto>
	 * @Author: 郭祥杰
	 * @Date: 2024/11/25 14:35
	 */
	List<GrszDto> queryByYhid(String yhid);

	/**
	 * @Description: 根据用户id和关联信息查询个人设置信息
	 * @param grszDto
	 * @return com.matridx.igams.common.dao.entities.GrszDto
	 * @Author: 郭祥杰
	 * @Date: 2025/7/8 13:27
	 */
	GrszDto getDtoByYhidAndGlxx(GrszDto grszDto);
}
