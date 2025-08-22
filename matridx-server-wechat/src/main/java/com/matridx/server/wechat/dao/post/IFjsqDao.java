package com.matridx.server.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.FjsqDto;
import com.matridx.server.wechat.dao.entities.FjsqModel;
import com.matridx.server.wechat.dao.entities.SjxxDto;
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
	public List<FjsqDto> checkFjbgSfwc(FjsqDto fjsqDto);
	
	/**
	 * 钉钉查看当天复检申请列表
	 * @param fjsqDto
	 * @return
	 */
	public List<FjsqDto> getListAuditPhone(FjsqDto fjsqDto);
	
	/**
	 * 送检列表查看复检申请信息
	 * @param fjsqDto
	 * @return
	 */
	public List<FjsqDto> getListBySjid(FjsqDto fjsqDto);

	/**
	 * 根据送检ID和复检类型查询复检Dto
	 * @param sjxxDto
	 * @return
	 */
	public FjsqDto getDtoZt(SjxxDto sjxxDto);

	
	/**
	 * 生信部获取最近的复检信息
	 * @param fjsqDto
	 * @return
	 */
	public List<FjsqDto> getDtoListOrderLrsj(FjsqDto fjsqDto);
	
	/**
	 * 选中导出
	 * @param fjsqDto
	 * @return
	 */
	List<FjsqDto> getListForSelectExp(FjsqDto fjsqDto);
	
	/**
	 * 查询可以导出的条数
	 * @param
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
	public int modRecheckPayinfo(FjsqDto fjsqDto);

	/**
	 * 点击实验按钮时先去查询出dna/rna/其他的实验日期和检测标记
	 * @param fjsqDto
	 * @return
	 */
	public List<FjsqDto> getFjxxByfjids(FjsqDto fjsqDto);

	/**
	 * 点击实验按钮判断检查项目
	 */
	public List<FjsqDto> checkJcxm(FjsqDto fjsqDto);

	/**
	 * 实验按钮修改检测标记
	 * @param fjsqDto
	 * @return
	 */
	public boolean updateJcbjByid(FjsqDto fjsqDto);

	/**
	 * 通过fjid去取到sjid,这里的sjid用于后续的发送实验通知的sendMessage方法
	 * @param
	 * @return
	 */
	public String getSjidFromFjid(String fjid);

	/**
	 * 根据标本编号获取信息
	 */
	public FjsqDto getDtoByYbbh(FjsqDto fjsqDto);
	/**
	 * 根据检测单位和审批岗位对应ddids查询系统用户
	 * @param map
	 * @return
	 */
	List<Map<String,String>> getXtyhByMap(Map<String, Object> map);
}
