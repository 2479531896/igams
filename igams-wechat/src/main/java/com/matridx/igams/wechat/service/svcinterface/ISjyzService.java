package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.dao.entities.XtshDto;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.SjyzDto;
import com.matridx.igams.wechat.dao.entities.SjyzModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ISjyzService extends BaseBasicService<SjyzDto, SjyzModel>{

	/**
	 * 送检验证列表（查询审核状态）
	 * @param sjyzDto
	 * @return
	 */
    List<SjyzDto> getPagedList(SjyzDto sjyzDto);
	
	/**
	 * 审核列表
	 * @param sjyzDto
	 * @return
	 */
    List<SjyzDto> getPagedAuditSjyz(SjyzDto sjyzDto);
	
	/**
	 * 修改保存送检验证信息
	 * @param sjyzDto
	 * @return
	 */
    boolean updateSjyzxx(SjyzDto sjyzDto);
	
	/**
	 * 查询审批岗位钉钉
	 * @param sjyzDto
	 * @return
	 */
    List<SjyzDto> getSpgwcyList(SjyzDto sjyzDto);
	
	/**
	 * 修改检测单位时发送信息通知相关人员
	 * @param sjyzDto
	 * @return
	 */
    void sendUpdateJcdwMessage(SjyzDto sjyzDto);

	/**
	 * 选择数据为生成pcr对接文档做准备(查找区分为正常的数据)
	 * @param list
	 * @return
	 */
    List<Map<String,String>>  getDtoForPcrReady(List<Map<String, String>> list);

	/**
	 * 选择数据为生成pcr对接文档做准备(查找区分为去人源的数据)
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

	/**
	 * 根据yzid修改yzjg字段
	 * @param sjyzDtoList
	 * @return
	 */
	boolean updateListYzjg(List<SjyzDto> sjyzDtoList);

	/**
	 * 天隆接口解析数据上传文件接口
	 * @param file
	 * @param jsonparam
	 * @return
	 */
	String uploadTlFile(MultipartFile file, String jsonparam) throws IOException;

	/**
	 * 送检验证审核流程步骤
	 * @return
	 */
    List<XtshDto> getSjyzShlc(SjyzDto sjyzDto);
	/**
	 * @description 重新读取PCR验证文件
	 * @param sjyzDto
	 * @return
	 */
	boolean rereadVerify(SjyzDto sjyzDto) throws IOException;
	/*
	 * 获取未完成报告数量
	 */
    Integer getUnFinishedReportCount();

	/**
	 * 获取未完成报告列表
	 */
	List<Map<String, Object>> getUnFinishedReportList();
}
