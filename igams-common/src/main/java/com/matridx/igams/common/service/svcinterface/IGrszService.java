package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.common.dao.entities.GrszDto;
import com.matridx.igams.common.dao.entities.GrszModel;

import java.util.List;
import java.util.Map;

public interface IGrszService extends BaseBasicService<GrszDto, GrszModel>{

	/**
	 * 主页设置任务确认人
	 */
	boolean modSaveSettingConfirmer(GrszDto grszDto);

	/**
	 * 根据用户ID查询个人设置信息
	 */
	GrszDto selectByYhid(String yhid);
	
	/**
	 * 根据条件查询个人设置信息
	 */
	GrszDto selectGrszDtoByYhidAndSzlb(GrszDto grszDto);

	/**
	 * 根据条件修改个人设置信息
	 */
	boolean updateByYhidAndSzlb(GrszDto grszDto);

	/**
	 * 查询多个type的个人设置
	 * @param grszDto
	 * @return
	 */
    Map<String,GrszDto> selectGrszMapByYhidAndSzlb(GrszDto grszDto);

	/**
	 * @Description: 批量新增
	 * @param grszDto
	 * @return boolean
	 * @Author: 郭祥杰
	 * @Date: 2024/7/24 16:49
	 */
	boolean saveGrsz(GrszDto grszDto);

	/**
	 * @Description: 批量删除
	 * @param grszDto
	 * @return boolean
	 * @Author: 郭祥杰
	 * @Date: 2024/7/25 10:00
	 */
	boolean delGrsz(GrszDto grszDto);

	/**
	 * @Description: 查询默认设置
	 * @param
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @Author: 郭祥杰
	 * @Date: 2024/11/20 10:11
	 */
	Map<String,Object> queryMrsz();

	/**
	 * @Description: 保存默认数据
	 * @param grszDto
	 * @return boolean
	 * @Author: 郭祥杰
	 * @Date: 2024/11/21 10:00
	 */
	boolean setDefaultSave(GrszDto grszDto) throws BusinessException;
	/**
	 * @Description: 批量新增
	 * @param grszDtoList
	 * @return boolean
	 * @Author: 郭祥杰
	 * @Date: 2024/10/15 16:18
	 */
	boolean insertList(List<GrszDto> grszDtoList);

	/**
	 * @Description: 批量修改
	 * @param grszDtoList
	 * @return boolean
	 * @Author: 郭祥杰
	 * @Date: 2024/10/15 16:19
	 */
	boolean updateList(List<GrszDto> grszDtoList);

	/**
	 * @Description: 获取个人订阅消息
	 * @param grszDto
	 * @return java.util.List<com.matridx.igams.common.dao.entities.GrszDto>
	 * @Author: 郭祥杰
	 * @Date: 2024/11/22 13:28
	 */
	List<GrszDto> queryDyxx(GrszDto grszDto);

	/**
	 * @Description: 根据用户id获取伙伴订阅消息
	 * @param yhid
	 * @return java.util.List<com.matridx.igams.common.dao.entities.GrszDto>
	 * @Author: 郭祥杰
	 * @Date: 2024/11/25 14:02
	 */
	List<GrszDto> queryByYhid(String yhid);
	/**
	 * @Description: 获取订阅消息
	 * @param grszDto
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @Author: 郭祥杰
	 * @Date: 2024/11/21 16:58
	 */
	Map<String,Object> queryMessage(GrszDto grszDto);

	/**
	 * @Description: 保存个人订阅消息
	 * @param grszDto
	 * @return boolean
	 * @Author: 郭祥杰
	 * @Date: 2024/11/26 10:23
	 */
	boolean modSaveMessage(GrszDto grszDto) throws BusinessException;

	boolean saveAndSendRabbit(GrszDto grszDto) throws BusinessException;
	List<GrszDto> getPagedDtoList(GrszDto grszDto);

	GrszDto getDto(GrszDto grszDto);

	boolean insertOrUpdate(GrszDto grszDto);

	/**
	 * @Description: 根据用户id和关联信息查询个人设置信息
	 * @param grszDto
	 * @return com.matridx.igams.common.dao.entities.GrszDto
	 * @Author: 郭祥杰
	 * @Date: 2025/7/8 13:27
	 */
	GrszDto getDtoByYhidAndGlxx(GrszDto grszDto);
}
