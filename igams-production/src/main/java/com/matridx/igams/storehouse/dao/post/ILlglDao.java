package com.matridx.igams.storehouse.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.LlglDto;
import com.matridx.igams.storehouse.dao.entities.LlglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ILlglDao extends BaseBasicDao<LlglDto, LlglModel>{

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
	 * 查询已有流水号
	 * @param prefix
	 * @return
	 */
	String getDjhSerial(String prefix);
	/**
	 * 根据钉钉实例ID获取在钉钉进行领料申请的审批信息
	 * @param
	 * @return
	 */
	LlglDto getDtoByDdslid(String ddspid);
	/**
	 * 设置钉钉实例ID为null
	 *
	 * @param
	 */
	void updateDdslidToNull(LlglDto llglDto);
	/**
	 * 获取钉钉审批人信息
	 * @param
	 * @return
	 */
	LlglDto getSprxxByDdid(String ddid);

	/**
	 * 根据审批人用户ID获取角色信息
	 * @param
	 * @return
	 */
	List<LlglDto> getSprjsBySprid(String sprid);
	/**
	 * 校验领料单号是否重复
	 * @param llglDto
	 * @return
	 */
	LlglDto getDtoByLldh(LlglDto llglDto);

//	/**
//	 * 获取领料出库审核列表数据
//	 * @param llglDto
//	 * @return
//	 */
//	List<LlglDto> getPagedDtoMaterialAudit(LlglDto llglDto);

	/**
	 * 领料申请审核
	 * @param llglDto
	 * @return
	 */
	List<LlglDto> getPagedAuditLlgl(LlglDto llglDto);
	List<LlglDto> getAuditListLlgl(List<LlglDto> t_sbyzList);
	
	
	/**
	 * 领料列表选中导出
	 * @param llglDto
	 * @return
	 */
 	List<LlglDto> getListForSelectExp(LlglDto llglDto);

    /**
     * 搜索条件分页获取导出信息
     * @param llglDto
     * @return
     */
	List<LlglDto> getListForSearchExp(LlglDto llglDto);

	/**
	 * 根据搜索条件获取导出条数
	 * @param llglDto
	 * @return
	 */
	int getCountForSearchExp(LlglDto llglDto);
	
	/**
	 * 查询已有流水号
	 * @param prefix
	 * @return
	 */
	String getCkdhSerial(String prefix);
	
	/**
	 * 查询已有流水号
	 * @param prefix
	 * @return
	 */
	String getJlbhSerial(String prefix);
	
	/**
	 * 得到机构的扩展参数
	 * @param llglDto
	 * @return
	 */
	String getlljgdh(LlglDto llglDto);
	
	/**
	 * 领料新增自动生成领料编号
	 * @param prefix
	 * @return
	 */
	String generateLljlbh(String prefix);
	/**
	 * 根据id 获取信息
	 */
	LlglDto getDtoByllId(String llid);
	/**
	 * 获取所有的领料单号
	 */
	List<LlglDto> getLldhByIds(List<String> list);

    boolean updateLllx(LlglDto llglDto);
	boolean updateWlxx(LlglDto llglDto);

	List<LlglDto> getLllxByIds(LlglDto llglDto);
	/**
	 * 根据搜索条件获取导出条数
	 * @param llglDto
	 * @return
	 */
	int getProductionCountForSearchExp(LlglDto llglDto);
	/**
	 * 领料列表生产领料选中导出
	 * @param llglDto
	 * @return
	 */
	List<LlglDto> getListForProductionSelectExp(LlglDto llglDto);
	/**
	 * 搜索条件分页获取导出信息
	 * @param llglDto
	 * @return
	 */
	List<LlglDto> getListForProductionSearchExp(LlglDto llglDto);
	/**
	 * 负责人设置保存
	 */
	void updateFzrByYfzr(LlglDto llglDto);
	/**
	 * @description 校验出库状态修改
	 * @param llglDto
	 * @return
	 */
    boolean updateForCkzt(LlglDto llglDto);

	List<LlglDto> queryByLlid(LlglDto llglDto);
}
