package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.XtshDto;
import com.matridx.igams.wechat.dao.entities.SjyzDto;
import com.matridx.igams.wechat.dao.entities.SjyzModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ISjyzDao extends BaseBasicDao<SjyzDto, SjyzModel>{

	/**
	 * 获取设备计量审核ID列表 
	 * @param sjyzDto
	 * @return
	 */
	List<SjyzDto> getPagedAuditSjyz(SjyzDto sjyzDto);
	
	/**
	 * 审核列表
	 * @param list
	 * @return
	 */
	List<SjyzDto> getAuditListSjyz(List<SjyzDto> list);
	
	/**
	 * 修改保存送检验证信息
	 * @param sjyzDto
	 * @return
	 */
    boolean updateSjyzxx(SjyzDto sjyzDto);
	
	/**
	 * 选中导出
	 * @param sjyzDto
	 * @return
	 */
	List<SjyzDto> getListForSelectExp(SjyzDto sjyzDto);
	
	/**
	 * 查询可以导出的条数
	 * @param sjyzDto
	 * @return
	 */
	int getCountForSearchExp(SjyzDto sjyzDto);
	
	/**
	 * 从数据库分页获取导出送检异常
	 * @param sjyzDto
	 * @return
	 */
	List<SjyzDto> getListForSearchExp(SjyzDto sjyzDto);
	
	/**
	 * 查询审批岗位钉钉
	 * @param sjyzDto
	 * @return
	 */
    List<SjyzDto> getSpgwcyList(SjyzDto sjyzDto);

	/**
	 * 为生成pcr对接文档做准备(查找送检验证中区分为正常的数据)
	 * @param list
	 * @return
	 */
	List<Map<String,String>>  getDtoForPcrReady(List<Map<String,String>> list);

	/**
	 * 为生成pcr对接文档做准备(查找送检验证中区分为去人源的数据)
	 * @param nbbmREMlist
	 * @return
	 */
	List<Map<String, String>> getREMDtoForPcrReady(List<Map<String, String>> nbbmREMlist);

	/**
	 * 根据sjid获取sjyzDto
	 * @param sjid
	 * @return
	 */
    List<SjyzDto> getDtoListBySjid(String sjid);

	/**
	 * 根据验证类别和送检id获取数据
	 * @param sjyzDto
	 * @return
	 */
    List<SjyzDto> getByYzlbAndSjid(SjyzDto sjyzDto);

	/**
	 * 根据验证IDS获取数据
	 * @param sjyzDto
	 * @return
	 */
    List<SjyzDto> getDtoByIds(SjyzDto sjyzDto);

	/**
	 * 获取送检验证数据
	 * @param sjyzDto
	 * @return
	 */
	SjyzDto getSjyzDto(SjyzDto sjyzDto);

	int updateListYzjg(List<SjyzDto> sjyzDtoList);

	/**
	 * 送检验证审核流程步骤
	 * @return
	 */
    List<XtshDto> getSjyzShlc(SjyzDto sjyzDto);

	/*
	 * 获取未完成报告数量
	 */
	Integer getUnFinishedReportCount();

	/**
	 * 获取未完成报告列表
	 */
	List<Map<String, Object>> getUnFinishedReportList();
}
