package com.matridx.igams.common.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.ShtxDto;
import com.matridx.igams.common.dao.entities.ShtxModel;

@Mapper
public interface IShtxDao extends BaseBasicDao<ShtxDto, ShtxModel>{

	/**
	 * 插入一条审核提醒信息
	 * shtxDto
	 * 
	 */
	boolean insertshtx(ShtxDto shtxDto);

	/**
	 * 得到审批延期数据信息（全部审批和个人审批使用同一个sql）
	 * shtxDto
	 * 
	 */
	List<ShtxDto> getPagedSpyq(ShtxDto shtxDto);

	/**
	 * 得到审批延期中选中的一条数据信息
	 * shtxDto
	 * 
	 */
	ShtxDto getShyqxxByShid(ShtxDto shtxDto);

	/**
	   * 得到   物料请购审核   业务名称
	 * lb_list
	 * 
	 */
	List<ShtxDto> WlqgshYwmcList(List<ShtxDto> lb_list);

    /**
               * 得到    取消请购审核    业务名称
     * lb_list
	 * 
     */
	List<ShtxDto> QxqgshYwmcList(List<ShtxDto> lb_list);
	
	/**
	 * 得到   物料申请   业务名称
	 * lb_list
	 * 
	 */
	List<ShtxDto> WlsqYwmcList(List<ShtxDto> lb_list);

	/**
	 * 得到物料修改审核  业务名称
	 * lb_list
	 * 
	 */
	List<ShtxDto> WlxgshYwmcList(List<ShtxDto> lb_list);

	/**
	 * 得到标本申请审核业务名称
	 * lb_list
	 * 
	 */
	List<ShtxDto> YbsqshYwmcList(List<ShtxDto> lb_list);

	/**
	 * 得到文件申请业务名称
	 * lb_list
	 * 
	 */
	List<ShtxDto> WjsqYwmcList(List<ShtxDto> lb_list);

	/**
	 * 合同审核
	 * lb_list
	 * 
	 */
	List<ShtxDto> HtshYwmcList(List<ShtxDto> lb_list);

	/**
	 * 得到设备审核业务名称
	 * lb_list
	 * 
	 */
	List<ShtxDto> SbshYwmcList(List<ShtxDto> lb_list);

	/**
	 * 得到领料申请审核业务名称
	 * lb_list
	 * 
	 */
	List<ShtxDto> LlsqshYwmcList(List<ShtxDto> lb_list);

	/**
	 * 得到仪器和试剂质检审核业务名称（到货检验的检验单号
	 * lb_list
	 * 
	 */
	List<ShtxDto> DhjyYwmcList(List<ShtxDto> lb_list);

	/**
	 * 得到入库审核业务名称
	 * lb_list
	 * 
	 */
	List<ShtxDto> RkshYwmcList(List<ShtxDto> lb_list);

	/**
	 * 得到货物到货审核业务名称
	 * lb_list
	 * 
	 */
	List<ShtxDto> HwdhshYwmcList(List<ShtxDto> lb_list);
	/**
	 * 得到送检验证审核业务名称
	 * lb_list
	 * 
	 */
	List<ShtxDto> SjyzshYwmcList(List<ShtxDto> lb_list);

	/**
	 * 得到复检审核业务名称
	 * lb_list
	 * 
	 */
	List<ShtxDto> FjshYwmcList(List<ShtxDto> lb_list);

	/**
	 * 查找所有需要审核提醒的审核lb
	 * 
	 */
	List<ShtxDto> getShtxlb();

	/**
	 * 得到发送出去的钉钉消息上的内容
	 * shtxdto
	 * 
	 */
	List<ShtxDto> getMgsnrToSend(ShtxDto shtxdto);

	/**
	 * 点击钉钉消息上查看某审核类别的延期审核任务列表
	 * shtxDto
	 * 
	 */
	List<ShtxDto> getSpyqDdview(ShtxDto shtxDto);

	/**
	 * 查找提醒类别
	 * 
	 */
	List<ShtxDto> getTxlbList();

	/**
	 * 个人审批延期列表
	 * shtxDto
	 * 
	 */
	List<ShtxDto> getPagedPersonSpyq(ShtxDto shtxDto);

	/**
	 * 发送钉钉提醒_物料请购审核（主要是为了部门经理提醒时候做限制）
	 * shtxdto
	 * 
	 */
	List<ShtxDto> getWlqgMgsToSend(ShtxDto shtxdto);
	
	/**
	 * 发送钉钉消息_取消请购审核（主要是为了部门经理提醒时候做限制）
	 * shtxdto
	 * 
	 */
	List<ShtxDto> getQxqgMgsnrToSend(ShtxDto shtxdto);

	/**
	 * 发送钉钉消息_合同审核（主要是为了部门经理提醒时候做限制）
	 * shtxdto
	 * 
	 */
	List<ShtxDto> getHtshMgsnrToSend(ShtxDto shtxdto);
	
	/**
	 * 发送钉钉消息_复检审核（主要是为了复检的检测单位提醒时候做限制）
	 * shtxdto
	 * 
	 */
	List<ShtxDto> getFjshMgsnrToSend(ShtxDto shtxdto);
	
	/**
	 * 发送钉钉消息_验证审核（主要是为了验证的检测单位提醒时候做限制）
	 * shtxdto
	 * 
	 */
	List<ShtxDto> getYzshMgsnrToSend(ShtxDto shtxdto);

	/**
	 * 发送钉钉消息_货物到货新增审核
	 * shtxdto
	 * 
	 */
	List<ShtxDto> getHwxzshMgsnrToSend(ShtxDto shtxdto);

	/**
	 * 发送钉钉消息_领料申请审核
	 * shtxdto
	 * 
	 */
    List<ShtxDto> getLlsqshMgsnrToSend(ShtxDto shtxdto);
	/**
	 * 获取所有待审核任务
	 */
	List<ShtxDto> getAllAuditings(ShtxDto shtxDto);

}
