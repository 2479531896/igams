package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.service.BaseBasicService;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.matridx.igams.common.dao.entities.CydcszModel;
import com.matridx.igams.common.dao.entities.CydcxxModel;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.DcszModel;
import com.matridx.igams.common.dao.entities.ProcessModel;
import com.matridx.igams.common.exception.BusinessException;

public interface IDcszService extends BaseBasicService<DcszDto, DcszModel>{
	/**
	 * 获取已选字段  
	 */
	 List<DcszDto> getChoseList(DcszDto dcszDto);

	/*
	  获取未选字段   
	  @param dcszModel
	 * @return
	 * @require ywid ryid
	 */
	//List<DcszDto> getWaitList(DcszDto dcszDto);
	
	/**
	 * 获取未选字段   
	 */
	 List<DcszDto> getCateWaitList(DcszDto dcszDto);

	/**
	 * 更新已选字段
	 */
	 void updateChoseList(DcszDto dcszDto, List<DcszDto> choseList) throws BusinessException;

	/*
	  文件导出操作
	  @param dcszModel
	 * @param choseList
	 * @param httpSession 
	 * @throws Exception 
	 */
	//String export(DcszDto dcszDto, List<DcszModel> choseList) throws Exception;
	
	/**
	 * 获取常用导出设置信息
	 */
	 List<CydcszModel> getCydcszxxs(DcszDto dcszDto);
	
	/*
	  获取常用导出信息
	  @param dcszModel
	 * @return
	 */
	List<CydcxxModel> getCydcxxs(CydcszModel cydcszModel);
	

	/**
	 * 文件导出设置
	 */
	 ProcessModel export(DcszDto dcDto, List<String> choseList, User user, HttpServletRequest request) throws Exception;
	
	/**
	 * 取消导出
	 */
	 boolean commCancelExport(DcszDto dcszDto);
	
	/**
	 * 检查文件处理进度
	 */
	 Map<String,Object> commCheckExport(DcszDto dcszDto);
	
	/**
	 * 文件下载处理
	 */
	 void commDownloadExport(DcszDto dcszDto,HttpServletResponse response);
	/**
	 * 根据ywid获取全部信息
	 */
	 List<DcszDto> getListById(String ywid);

	/**
	 * Mngs文件导出设置
	 */
	 ProcessModel mngsExport(DcszDto dcDto,HttpServletRequest request) throws Exception;
}
