package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.wechat.dao.entities.FjsqDto;
import com.matridx.igams.wechat.dao.entities.FjsqModel;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IFjsqDao extends BaseBasicDao<FjsqDto, FjsqModel>{

	/**
	 * 审核时修改状态
	 * @param fjsqDto
	 * @return
	 */
    boolean updateZt(FjsqDto fjsqDto);

	/**
	 * 修改对账金额
	 * @param list
	 * @return
	 */
    int updateListDzInfo(List<FjsqDto> list);

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
	 * 申请复检审核
	 * @param hwxxDto
	 * @return
	 */
	List<FjsqDto> getPagedAuditRecheck(FjsqDto hwxxDto);

	/**
	 * 申请复检审核列表
	 * @param hwxxDtos
	 * @return
	 */
	List<FjsqDto> getAuditListRecheck(List<FjsqDto> hwxxDtos);
	
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
	 * 生信部获取最近的复检信息
	 * @param fjsqDto
	 * @return
	 */
    List<FjsqDto> getDtoListOrderLrsj(FjsqDto fjsqDto);
	
	/**
	 * 选中导出
	 * @param fjsqDto
	 * @return
	 */
	List<FjsqDto> getListForSelectExp(FjsqDto fjsqDto);
	
	/**
	 * 查询可以导出的条数
	 * @param fjsqDto
	 * @return
	 */
	int getCountForSearchExp(FjsqDto fjsqDto);
	
	/**
	 * 从数据库分页获取导出送检异常
	 * @param fjsqDto
	 * @return
	 */
	List<FjsqDto> getListForSearchExp(FjsqDto fjsqDto);
	
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
	 * 查询审批岗位钉钉
	 * @param fjsqDto
	 * @return
	 */
	List<FjsqDto> getSpgwcyList(FjsqDto fjsqDto);

	/**
	 * 复检支付信息修改
	 * @param fjsqDto
	 * @return
	 */
    int modRecheckPayinfo(FjsqDto fjsqDto);

	/**
	 * 点击实验按钮时先去查询出dna/rna/其他的实验日期和检测标记
	 * @param fjsqDto
	 * @return
	 */
    List<FjsqDto> getFjxxByfjids(FjsqDto fjsqDto);

	/**
	 * 点击实验按钮判断检查项目
	 */
    List<FjsqDto> checkJcxm(FjsqDto fjsqDto);

	/**
	 * 实验按钮修改检测标记
	 * @param fjsqDto
	 * @return
	 */
    boolean updateJcbjByid(FjsqDto fjsqDto);

	/**
	 * 通过fjid去取到sjid,这里的sjid用于后续的发送实验通知的sendMessage方法
	 * @param fjid
	 * @return
	 */
    String getSjidFromFjid(String fjid);
	/**
	 * 根据标本编号获取信息
	 */
    FjsqDto getDtoByYbbh(FjsqDto fjsqDto);
	/**
	 *根据ddslid获取信息
	 * @param fjsqDto
	 * @return
	 */
    FjsqDto getDtoByDdslid(FjsqDto fjsqDto);

	/**
	 * 获取钉钉审批人信息
	 * @param
	 * @return
	 */
    FjsqDto getSprxxByDdid(User user);
	/**
	 * 根据审批人用户ID获取角色信息
	 * @param
	 * @return
	 */
    List<FjsqDto> getSprjsBySprid(String sprid);
	/**
	 * 将钉钉实例ID至为空
	 * @param fjsqDto
	 * @return
	 */
	boolean updateDdslidToNull(FjsqDto fjsqDto);


	/**
	 * 审核选中导出
	 * @param fjsqDto
	 * @return
	 */
	List<FjsqDto> getListForSelectExpAudit(FjsqDto fjsqDto);

	/**
	 * 审核查询可以导出的条数
	 * @param fjsqDto
	 * @return
	 */
	int getCountForSearchExpAudit(FjsqDto fjsqDto);

	/**
	 * 审核从数据库分页获取导出送检异常
	 * @param fjsqDto
	 * @return
	 */
	List<FjsqDto> getListForSearchExpAudit(FjsqDto fjsqDto);

	/**
	 * 更新取样信息
	 * @param fjsqDto
	 * @return
	 */
    boolean updateSampleInfo(FjsqDto fjsqDto);
	/**
	 * 根据检测单位和审批岗位对应yhms查询系统用户
	 * @param map
	 * @return
	 */
	List<Map<String,String>> getXtyhByMap(Map<String, Object> map);
	/**
	 * 更新导出标记
	 * @param fjsqDto
	 * @return
	 */
	boolean updateDcbj(FjsqDto fjsqDto);
	/**
	 * 获取历史数据
	 * @param
	 * @return
	 */
	List<FjsqDto> getHistoryList(FjsqDto fjsqDto);

	
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
