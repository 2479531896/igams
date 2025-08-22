package com.matridx.igams.wechat.service.svcinterface;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.FjsqDto;
import com.matridx.igams.wechat.dao.entities.FjsqModel;
import com.matridx.igams.wechat.dao.entities.SjxxDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface IFjsqService extends BaseBasicService<FjsqDto, FjsqModel>{

	/**
	 * 新增复检申请
	 * @param fjsqDto
	 * @param fjsqDto
	 * @return
	 */
    boolean insertFjsq(FjsqDto fjsqDto);

	/**
	 * 修改对账金额
	 * @param list
	 * @return
	 */
    int updateListDzInfo(List<FjsqDto> list);
	/**
	 * 修改检测单位
	 * @param fjsqDto
	 * @param fjsqDto
	 * @return
	 */
    boolean updateJcdw(FjsqDto fjsqDto, String yjcdw);

	/**
	 * 复检申请审核列表
	 * @param fjsqDto
	 * @return
	 */
	List<FjsqDto> getPagedAuditRecheck( FjsqDto  fjsqDto);

	/**
	 * getInfoList
	 * @param
	 * @return
	 */
	List<FjsqDto> getInfoList( FjsqDto  fjsqDto);

	/**
	 * 批量更新
	 * @return
	 */
    Boolean updateList(List<FjsqDto> list);
	/**
	 * 复检列表
	 * @param fjsqDto
	 * @return
	 */
	List<FjsqDto> getPagedDtoList(FjsqDto fjsqDto);
	
	/**
	 * 复检统计时，点击查看附件列表
	 * @param fjsqDto
	 * @return
	 */
	List<FjsqDto> getPagedStatisRecheck(FjsqDto fjsqDto);
	
	/**
	 * 根据复检日期确认是否完成，如果存在复检，或者增加检测项目的，并且要发送报告的，则确认日期是否正确
	 * @param fjsqDto
	 * @return
	 */
    List<FjsqDto> checkFjbgSfwc(FjsqDto fjsqDto);
	
	/**
	 * 删除复检申请
	 * @param fjsqDto
	 * @return
	 */
    boolean deleteFjsq(FjsqDto fjsqDto);
	
	/**
	 * 钉钉查看当天复检申请列表
	 * @param fjsqDto
	 * @return
	 */
    List<FjsqDto> getListAuditPhone(FjsqDto fjsqDto);
	
	/**
	 * 送检列表查看复检申请信息
	 * @param fjsqDto
	 * @return
	 */
    List<FjsqDto> getListBySjid(FjsqDto fjsqDto);

	/**
	 * 根据送检ID和复检类型查询复检Dto
	 * @param sjxxDto
	 * @return
	 */
    FjsqDto getDtoZt(SjxxDto sjxxDto);

	/**
	 * 修改审核状态
	 * @param fjsqDto
	 */
    boolean updateZt(FjsqDto fjsqDto);

	
	/**
	 * 生信部获取最近的复检信息
	 * @param fjsqDto
	 * @return
	 */
    List<FjsqDto> getDtoListOrderLrsj(FjsqDto fjsqDto);
	
	/**
	 * 修改复检申请
	 * @param fjsqDto
	 * @param sjxxDto
	 * @return
	 */
	// public boolean updateFjsq(FjsqDto fjsqDto,SjxxDto sjxxDto);
	
	/**
	 * 获取未发送复检报告
	 * @param fjsqDto
	 * @return
	 */
	List<FjsqDto> queryFjbg(FjsqDto fjsqDto);
	
	/**
	 * 获取总数
	 * @param fjsqDto
	 * @return
	 */
	Integer querynum(FjsqDto fjsqDto);
	
	/**
	 * 修改检测单位时发送信息通知相关人员
	 * @param fjsqDto
	 * @return
	 */
    void sendUpdateJcdwMessage(FjsqDto fjsqDto);
	
	/**
	 * 查询审批岗位钉钉
	 * @param fjsqDto
	 * @return
	 */
	List<FjsqDto> getSpgwcyList(FjsqDto fjsqDto);
	
	/**
	 * 复检通过时发送消息通知客户
	 * @param fjsqDto
	 * @return
	 */
    boolean sendRecheckMessage(FjsqDto fjsqDto);

	/**
	 * 同步复检支付信息
	 * @param fjsqDto
	 */
    void modRecheckPayinfo(FjsqDto fjsqDto);

	/**
	 * 点击实验按钮先去获取dna，rna，其他的实验日期的检测标记
	 * @param fjsqDto
	 * @return
	 */
    List<FjsqDto> getFjxxByfjids(FjsqDto fjsqDto);

	/**
	 * 实验按钮选择列先判断检测项目是否相同
	 * @param fjsqDto
	 * @return
	 */
    List<FjsqDto> checkJcxm(FjsqDto fjsqDto);

	/**
	 * 实验按钮修改检测标记
	 * @param fjsqDto
	 * @return 
	 * @throws BusinessException 
	 */
    boolean updateJcbjByid(FjsqDto fjsqDto) throws BusinessException ;

	/**
	 * 更新复检信息
	 * @param fjsqDto
	 * @return
	 */
    boolean updateFjsq(FjsqDto fjsqDto);
	/**
	 * 根据标本编号获取信息
	 */
    FjsqDto getDtoByYbbh(FjsqDto fjsqDto);

	/**
	 * 医学部审核完成后发送复检通知
	 * @param fjsqDto
	 * @return
	 */
    boolean sendRecheckMessageSec(FjsqDto fjsqDto);

	/**
	 * 根据搜索条件获取审核导出条数
	 * @param fjsqDto
	 * @return
	 */
    int getCountForSearchExpAudit(FjsqDto fjsqDto, Map<String, Object> params);
	
		/**
	 * 复测审批回调
	 * @param obj
	 * @param request
	 * @param t_map
	 * @return
	 * @throws BusinessException
	 */
        boolean callbackRecheckAduit(JSONObject obj, HttpServletRequest request, Map<String, Object> t_map) throws BusinessException;
	/**
	 * 更新取样信息
	 * @param fjsqDto
	 * @return
	 */
    boolean updateSampleInfo(FjsqDto fjsqDto);
	/**
	 * 通过fjid去取到sjid,这里的sjid用于后续的发送实验通知的sendMessage方法
	 * @param fjid
	 * @return
	 */
    String getSjidFromFjid(String fjid);
	/**
	 * 更新导出标记
	 * @param fjsqDto
	 * @return
	 */
	boolean updateDcbj(FjsqDto fjsqDto);
	/**
	 * 根据检测单位和审批岗位对应yhms查询系统用户
	 * @param map
	 * @return
	 */
	List<Map<String,String>> getXtyhByMap(Map<String, Object> map);

	/**
	 * 获取历史数据
	 * @param
	 * @return
	 */
	List<FjsqDto> getHistoryList(FjsqDto fjsqDto);
	
	/**
	 * 根据页面传递的送检实验数据，结合原有的数据，原本就存在的检测类型数据，则进行更新，没有的数据则进行新增
	 * 涉及 项目实验管理表，送检实验管理表
	 * @param fjsqDto
	 * @param sjxxDto
	 * @return
	 */
	public boolean addOrUpdateSyData(FjsqDto fjsqDto,SjxxDto sjxxDto);
	
	/**
	 * 获取实验日期小于今天的送检实验日期清单
	 * @param
	 * @return
	 */
	List<FjsqDto> getSyxxByFj(FjsqDto fjsqDto);
	
	/**
	 * 根据送检ID获取审核中或者还未实验的复检数据
	 * @param
	 * @return
	 */
	List<FjsqDto> getNotSyFjxxBySjxx(FjsqDto fjsqDto);

	/**
	 * 根据复检id更新
	 * @param fjsqDto
	 * @return
	 */
	boolean updateFjsjByFjid(FjsqDto fjsqDto);
	
	/**
	 * 检查加测情况下是否是已有的项目。如今病原体列表复测加测页面只是不允许加测和原项目相同的项目，还需要增加不允许加测已加测的项目。
	 * @param fjsqDto
	 * @param shlx
	 * @param jcsj_jcxm
	 * @param csdm
	 * @param list
	 * @return
	 * @throws BusinessException
	 */
	boolean checkCanFj(FjsqDto fjsqDto, String shlx, JcsjDto jcsj_jcxm,String csdm,List<JcsjDto> list) throws BusinessException;

	/**
	 * 根据文库编码获取复检数据
	 * @param fjsqDto
	 * @return
	 */
	List<FjsqDto> getDtoByWkbms(FjsqDto fjsqDto);

	/**
	 * 根据复检申请ID更新报告日期
	 * @param fjsqDtos
	 * @return
	 */
	boolean updateBgrqByDtos(List<FjsqDto> fjsqDtos);
}

