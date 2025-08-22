package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.GzglDto;
import com.matridx.igams.production.dao.entities.WjglDto;
import com.matridx.igams.production.dao.entities.WjglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IWjglDao extends BaseBasicDao<WjglDto, WjglModel>{

	/**
	 * 获取按钮列表
	 */
	List<WjglDto> getButtonList(WjglDto wjglDto);

	/**
	 * 获取文件申请审核的ID列表
	 */
	List<WjglDto> getPagedAuditIdList(WjglDto wjglDto);

	/**
	 * 获取文件新增统计信息
	 */
	List<WjglDto> getInsertStatsInfo(WjglDto wjglDto);

	/**
	 * 获取文件修订统计信息
	 */
	List<WjglDto> getUpdateStatsInfo(WjglDto wjglDto);

	/**
	 * 根据文件申请审核的ID列表获取审核列表详细信息
	 */
	List<WjglDto> getAuditListByIds(List<WjglDto> t_sqList);

	/**
	 * 更新文件申请的审核状态
	 */
	void updateZt(WjglDto wjglDto);

	/**
	 * 更新文件申请的审核批准状态
	 */
	void updatePzsj(WjglDto wjglDto);

	/**
	 * 根据关联文件id查询文件信息
	 */
	WjglDto selectByGlwjid(String wjid);

	/**
	 * 清空关联文件ID
	 */
	int updateGlwjid(WjglDto t_wjglDto);
	
	/**
	 * 根据关联文件ID修改删除标记
	 */
	void updateScbj(WjglDto wjglDto);

	/**
	 * 根据文件ID修改时间
	 */
	int updateTimeByWjid(WjglDto wjglDto);

	/**
	 * 根据文件ID查询附件业务类型
	 */
	List<String> selectYwlxByYwid(String wjid);

	/**
	 * 根据文件ID查询附件表信息
	 */
	List<FjcfbDto> selectFjcfbDtoByWjid(String wjid);

	/**
	 * 根据文件ID查询任务进度
	 */
	List<GzglDto> selectGzglByWjid(WjglDto wjglDto);

	/**
	 * 根据根文件ID查询wjglDtos
	 */
	List<WjglDto> getDtoListByGwjid(WjglDto wjglDto);

	/**
	 * 根据文件ID通过关联文件ID查询文件数量
	 */
	int selectGlwjidCountByWjid(WjglDto wjglDto);

	/**
	 * 根据角色ID查询新增用户信息
	 */
	List<WjglDto> selectAddXtyhByJsid(Map<String, Object> map);

	/**
	 * 根据角色ID查询删除用户信息
	 */
	List<WjglDto> selectDelXtyhByJsid(Map<String, Object> map);

	/**
	 * 根据用户角色删除工作信息
	 */
	void deleteGzglByYhid(WjglDto del_WjglDto);

	/**
	 * 根据角色ID查询人员信息
	 */
	List<WjglDto> selectDtoByJsids(WjglDto wjglDto);

	/**
	 * 查询文件是否重复
	 */
	List<WjglDto> selectDocumentRepeat(WjglDto wjglDto);

	/**
	 * 获取文件列表
	 */
	List<WjglDto> getPagedListDocument(WjglDto wjglDto);

	/**
	 * 修改旧版本文件
	 */
	boolean updatePreviousDto(WjglDto wjglDto);

	/**
	 * 修改新版本文件
	 */
	boolean updateCurrentDto(WjglDto wjglDto);

	/**
	 * 修改新版本根文件ID
	 */
	boolean updateCurGwjid(WjglDto wjglDto);
	
	/**
	 * 从数据库分页获取导出文件信息数据
	 */
	List<WjglDto> getListForSelectExp(WjglDto wjglDto);
	
	/**
	 * 从数据库分页获取导出物料数据
	 */
	List<WjglDto> getListForSearchExp(WjglDto wjglDto);
	
	/**
	 * 根据搜索条件获取导出条数
	 */
	int getCountForSearchExp(WjglDto wjglDto);

	/**
	 * 根据文件ID(复数)查询任务信息
	 */
	List<Map<String, String>> selectGzglByWjids(List<String> ywids);

	/**
	 * 文件分类统计
	 */
	List<WjglDto> getDocumentClassStats();
	/**
	 * 文件月份统计
	 */
	List<WjglDto> getDocumentMonthStats(WjglDto wjglDto);
	/**
	 * 获取统计年份
	 */
	List<WjglDto> getYearGroup();
	/**
	 * @description 修改发放状态
	 */
    boolean updateFfzt(WjglDto wjglDto);
	/**
	 * @description 获取文件信息
	 */
    List<WjglDto> getDtoByIds(WjglDto wjglDto);

	/**
	 * @Description: 批量新增
	 * @param wjglDtoList
	 * @return boolean
	 * @Author: 郭祥杰
	 * @Date: 2024/12/6 10:54
	 */
	boolean insertWjglDtos(List<WjglDto> wjglDtoList);

	/**
	 * @Description: 查询是否重复
	 * @param wjglDto
	 * @return java.util.List<com.matridx.igams.production.dao.entities.WjglDto>
	 * @Author: 郭祥杰
	 * @Date: 2024/12/6 15:42
	 */
	List<WjglDto> queryWjglDto(WjglDto wjglDto);
}
