package com.matridx.igams.production.service.svcinterface;

import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.GzglDto;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.WjglDto;
import com.matridx.igams.production.dao.entities.WjglModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface IWjglService extends BaseBasicService<WjglDto, WjglModel>{

	/**
	 * 删除文件
	 */
	boolean deleteDto(WjglDto wjglDto);

	/**
	 * 文件申请审核列表
	 */
	List<WjglDto> getPagedAuditList(WjglDto wjglDto);

	/**
	 * 文件修订提交保存
	 */
	WjglDto modSaveDocumentView(WjglDto wjglDto);

	/**
	 * 获取文件新增统计信息
	 */
	List<WjglDto> getInsertStatsInfo(WjglDto wjglDto);

	/**
	 * 获取文件修订统计信息
	 */
	List<WjglDto> getUpdateStatsInfo(WjglDto wjglDto);

	/**
	 * 文件修改时间
	 */
	boolean updateTimeByWjid(WjglDto wjglDto);

	/**
	 * 文件新增保存
	 */
	boolean addSaveDocument(WjglDto wjglDto) throws BusinessException;

	/**
	 * 文件修改保存
	 */
	boolean modSaveDocument(WjglDto wjglDto, HttpServletRequest request);

	/**
	 * 根据文件ID查询附件表信息
	 */
	List<FjcfbDto> selectFjcfbDtoByWjid(String wjid);

	/**
	 * 根据文件ID查询工作管理信息
	 */
	List<GzglDto> selectGzglByWjid(WjglDto wjglDto);

	/**
	 * 根据根文件ID查询wjglDtos
	 */
	List<WjglDto> getDtoListByGwjid(WjglDto wjglDto);

	/**
	 * 根据文件ID通过关联文件ID查询文件数量
	 */
	boolean selectGlwjidCountByWjid(WjglDto wjglDto);

	/**
	 * 获取修改页面信息
	 */
	WjglDto modDocument(WjglDto wjglDto);

	/**
	 * 文件权限批量修改保存
	 */
	boolean batchPermitSaveDocument(WjglDto wjglDto);

	/**
	 * 文件转换
	 */
	boolean transferDocument(WjglDto wjglDto);

	/**
	 * 获取文件列表
	 */
	List<WjglDto> getPagedListDocument(WjglDto wjglDto);

	/**
	 * 修改文件关联关系
	 */
	boolean updateRelated(WjglDto wjglDto);

	/**
	 * 根据ID获取文件信息
	 */
	WjglDto getDtoById(String wjid, HttpServletRequest request);
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
	 * 上传Word文件
	 */
    boolean sendWordFile(String wjljjm);

	/**
	 * @Description: 批量上传
	 * @param wjglDto
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @Author: 郭祥杰
	 * @Date: 2024/12/6 14:20
	 */
	Map<String,Object> bulkimportsSaveDocument(WjglDto wjglDto) throws BusinessException;
}
