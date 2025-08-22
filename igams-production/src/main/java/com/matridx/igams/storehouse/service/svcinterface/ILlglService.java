package com.matridx.igams.storehouse.service.svcinterface;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.LlglDto;
import com.matridx.igams.storehouse.dao.entities.LlglModel;
import com.matridx.igams.storehouse.dao.entities.LyrkxxDto;

import javax.servlet.http.HttpServletRequest;

public interface ILlglService extends BaseBasicService<LlglDto, LlglModel>{

	/**
	 * 领料列表
	 * 
	 * @param llglDto
	 * @return
	 */
    List<LlglDto> getPagedDtoReceiveMaterielList(LlglDto llglDto);

	/**
	 * 领料列表(钉钉)
	 *
	 * @param llglDto
	 * @return
	 */
    List<LlglDto> getPagedReceiveMateriel(LlglDto llglDto);
	/**
	 * 查看领料信息 基本信息
	 * @param llid
	 * @return
	 */
    LlglDto getDtoReceiveMaterielByLlid(String llid);
	List<LlglDto> getDtoReceiveMateriel(LlglDto llglDto);
	List<LlglDto> getDtoReceiveMaterielWithPrint(LlglDto llglDto);
	
	/**
	 * 自动生成领料单号
	 * @param llglDto
	 * @return
	 */
    String generateDjh(LlglDto llglDto);
	
	/**
	 * 校验领料单号是否重复
	 * @param llglDto
	 * @return
	 */
	boolean getDtoByLldh(LlglDto llglDto);
	
	/**
	 * 保存领料信息
	 * @param llglDto
	 * @return
	 * @throws BusinessException 
	 */
    boolean addSavePicking(LlglDto llglDto) throws BusinessException;
	
	/**
	 * 修改领料信息
	 * @param llglDto
	 * @return
	 * @throws BusinessException 
	 */
    boolean modSavePicking(LlglDto llglDto) throws BusinessException;
	/**
	 * 修改领料信息
	 * @param llglDto
	 * @return
	 * @throws BusinessException
	 */
    boolean AdvancedmodSavePicking(LlglDto llglDto) throws BusinessException;
//	/**
//	 * 获取领料申请审核列表数据
//	 * @param llglDto
//	 * @return
//	 */
//	public List<LlglDto> getPagedDtoMaterialAudit(LlglDto llglDto);

	/**
	 * 高级修改领料信息
	 * @param llglDto
	 * @return
	 * @throws BusinessException 
	 */
    boolean seniormodSavePicking(LlglDto llglDto) throws BusinessException;
	
	/**
	 * 领料申请审核列表
	 * @param llglDto
	 * @return
	 */
    List<LlglDto> getPagedAuditLlgl(LlglDto llglDto);
//	/**
//	 * 查看领料出库信息
//	 * @param llid
//	 * @return
//	 */
//	public LlglDto getDtoMaterialOutAuditByllid(String llid);
	
	/**
	 * 领料删除
	 * @param llglDto
	 * @return
	 */
    boolean deleteLlgl(LlglDto llglDto, List<LyrkxxDto> list) throws BusinessException;
	

	 /**
	 * 根据搜索条件获取导出条数
	 * @return
	 */
     int getCountForSearchExp(LlglDto llglDto, Map<String, Object> params);
	
	/**
	 * 自动生成出库单号
	 * @param llglDto
	 * @return
	 */
    String generateCkdh(LlglDto llglDto);
	
	/**
	 * 出库修改领料信息
	 * @param llglDto
	 * @return
	 * @throws BusinessException 
	 */
    boolean deliverymodSavePicking(LlglDto llglDto) throws BusinessException;
	
	/**
	 * 自动生成记录编号
	 * @return
	 */
    String generateJlbh();
	
	/**
	 * 自动生成领料记录编号
	 * @param llglDto
	 * @return
	 */
    String generateLljlbh(LlglDto llglDto);
	
	/**
	 * 得到机构的扩展参数
	 * @param llglDto
	 * @return
	 */
    String getlljgdh(LlglDto llglDto);

	/**
	 * 钉钉审批回调
	 * @param obj
	 
	 */
    boolean callbackLlglAduit(JSONObject obj, HttpServletRequest request, Map<String, Object> t_map) throws BusinessException;
	/**
	 * 获取所有的领料单号
	 */
	List<LlglDto> getLldhByIds(List<String> list);

    boolean updateLllx(LlglDto llglDto);
	boolean updateWlxx(LlglDto llglDto);

	List<LlglDto> getLllxByIds(LlglDto llglDto);
	/**
	 * 负责人设置保存
	 */
	void updateFzrByYfzr(LlglDto llglDto);
}
