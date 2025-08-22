package com.matridx.server.wechat.service.svcinterface;

import java.util.List;
import java.util.Map;

import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.*;

public interface ISjxxService extends BaseBasicService<SjxxDto, SjxxModel>{

	/**
	 * 新增送检信息（患者页面）
	 * @param sjxxDto
	 * @return
	 * @throws BusinessException
	 */
    boolean addSaveReport(SjxxDto sjxxDto) throws BusinessException;

	/**
	 * 新增送检信息（知情同意书）
	 * @param sjxxDto
	 * @return
	 */
    boolean addSaveConsentBack(SjxxDto sjxxDto);

	/**
	 * 新增送检信息（知情同意书）
	 * @param sjxxDto
	 * @return
	 */
    boolean addSaveConsentComp(SjxxDto sjxxDto);

	/**
	 * 标本接收确认
	 * @param sjxxDto
	 * @return
	 */
    boolean sampAcceptConfirm(SjxxDto sjxxDto);

	/**
	 * 根据送检标本编号查询送检信息 0 读取第一页  1 读取第二页
	 * @param sjxxDto
	 * @return
	 */
    SjxxDto getDto(SjxxDto sjxxDto, int flg);

	/**
	 * 保存附件
	 * @param sjxxDto
	 * @return
	 */
    boolean saveFile(SjxxDto sjxxDto);

	/**
	 * 保存附件
	 * @param
	 * @return
	 */
    boolean saveScanFile(SjxxDto sjxxDto);

	/**
	 * 保存本地新增送检信息至微信服务器
	 * @param sjxxDto
	 * @throws BusinessException
	 */
    void receiveAddInspection(SjxxDto sjxxDto) throws BusinessException;

	/**
	 * 保存本地更新送检信息至微信服务器
	 * @param sjxxDto
	 * @throws BusinessException
	 */
    void receiveModInspection(SjxxDto sjxxDto) throws BusinessException;

	/**
	 * 保存本地删除送检信息至微信服务器
	 * @param sjxxDto
	 * @throws BusinessException
	 */
    void receiveDelInspection(SjxxDto sjxxDto) throws BusinessException;

	/**
	 * 付款维护
	 * @param sjjcxmDto
	 * @return
	 */
	Boolean saveProjectAmount(SjjcxmDto sjjcxmDto) throws BusinessException;

	/**
	 * 保存本确认送检收样信息至微信服务器
	 * @param sjxxDto
	 * @throws BusinessException
	 */
    void receiveConfirmInspection(SjxxDto sjxxDto) throws BusinessException;

	/**
	 * 根据状态，患者姓名和时间查询信息
	 * @param sjxxDto
	 * @return
	 * @throws BusinessException
	 */
    SjxxDto extractUserInfo(SjxxDto sjxxDto) throws BusinessException;

	/**
	 * 保存修改金额(微信端)
	 * @param sjxxDto
	 * @return
	 */
    boolean amountSaveEdit(SjxxDto sjxxDto);

	/**
	 * 获取送检物种信息
	 * @param sjxxDto
	 * @return
	 */
    List<SjwzxxDto> selectWzxxBySjid(SjxxDto sjxxDto);

	/**
	 * 根据订单号更新支付结果
	 * @param sjxxDto
	 * @return
	 */
    boolean updateByDdh(SjxxDto sjxxDto);

	/**
	 * 列表查询
	 * @param map
	 * @return
	 */
    List<SjxxDto> getListWithMap(Map<String, Object> map);
	/**
	 * 查询相同用户标本类型是否输入重复
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> isYblxRepeat(SjxxDto sjxxDto);

	/**
	 * 根据订单号查询送检信息
	 * @param sjxxDto
	 * @return
	 */
    SjxxDto getDtoByDdh(SjxxDto sjxxDto);

	/**
	 * 小程序获取送检清单
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getSjxxDtoList(SjxxDto sjxxDto);

	/**
	 * 查询相同的医生电话和患者姓名
	 * @param sjxxDto
	 * @return
	 */
    int getcountByysdh(SjxxDto sjxxDto);

	/**
	 * 根据送检信息查询出送检销售人员
	 * @return
	 */
    List<Map<String,String>> getLrry();

	/**
	 * 根据收费情况进行统计收样
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getSjxxBySy(SjxxDto sjxxDto);

	/**
	 * 根据收费不收费和废弃标本做统计
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getYbxxByWeek(SjxxDto sjxxDto);

	/**
	 * 查询当前销售人员送检单位的送检数量
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getYbxxBySjdw(SjxxDto sjxxDto);

	/**
	 * 获取查看报告列表
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getPagedReportList(SjxxDto sjxxDto);

	/**
	 * 根据收费情况以周为单位进行统计收样(按照不收费的标本变化，收费标本的变化)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getSjxxWeekBySy(SjxxDto sjxxDto);

	/**
	 * 修改临床反馈
	 * @param sjxxDto
	 * @return
	 */
    boolean updateFeedBack(SjxxDto sjxxDto);

	/**
	 * 医生小程序获取送检清单
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getDocMiniInsplist(SjxxDto sjxxDto);

	/**
	 * 送检结果类型统计
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getDetectionResultStatis(SjxxDto sjxxDto);

	/**
	 * 查询没有进行过反馈的送检信息
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getNofeefback(SjxxDto sjxxDto);

	/**
	 * 分页查询送检单位和科室信息
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getPagedUnitList(SjxxDto sjxxDto);

	/**
	 * 修改送检表支付信息(同步)
	 * @param sjxxDto
	 * @return
	 */
    boolean modInspPayinfo(SjxxDto sjxxDto, String zfid);

	/**
	 * 查询角色检测单位限制
	 * @param jsid
	 * @return
	 */
	List<Map<String,String>> getJsjcdwByjsid(String jsid);

	/**
	 * 查询科室信息
	 * @return
	 */
    List<SjdwxxDto> getSjdwxx();

	/**
	 * 病原体列表
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getPagedInspection(SjxxDto sjxxDto);

	/**
	 * 查询检测单位
	 * @param hbmc
	 * @return
	 */
    List<JcsjDto> getDetectionUnit(String hbmc);

	/**
	 * 查询最近一次送检信息
	 * @param sjxxDto
	 * @return
	 */
    SjxxDto getLastInfo(SjxxDto sjxxDto);

	/**
	 * 根据检测项目查询检测子项目
	 * @param jcxmid
	 * @return
	 */
    List<JcsjDto> getSubDetect(String jcxmid);
	/**
	 * 根据检测项目查询检测子项目(不包含禁用)
	 * @param jcxmid
	 * @return
	 */
    List<JcsjDto> getSubDetectWithoutDisabled(String jcxmid);

	/**
	 * 清空检测子项目
	 * @param sjxxDto
	 * @return
	 */
    boolean emptySubDetect(SjxxDto sjxxDto);

	/**
	 * 查询接收人员列表
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getReceiveUserByDb(SjxxDto sjxxDto);


	/*获取未完善的送检信息*/
    List<SjxxDto> getPagedPerfectDtoList(SjxxDto sjxxDto);

	/**
	 * 发送微信消息
	 * @param templateid
	 * @param wxid
	 * @param title
	 * @param keyword1
	 * @param keyword2
	 * @param keyword3
	 * @param keyword4
	 * @param remark
	 * @param reporturl
	 */
    void sendWeChatMessage(String templateid, String wxid, String title, String keyword1, String keyword2, String keyword3, String keyword4, String remark, String reporturl);

	/**
	 * 修改合作伙伴
	 * @param sjxxDto
	 */
    boolean updateDB(SjxxDto sjxxDto);

	/**
	 * 修改参数扩展
	 * @param sjxxDto
	 */
    boolean updateCskz(SjxxDto sjxxDto);
	/**
	 * 修改特殊申请
	 * @param sjxxDto
	 * @return
	 */
    boolean updateTssq(SjxxDto sjxxDto);

	/**
	 * 特检导入同步85端
	 * @param sjxxDto
	 * @throws BusinessException
	 */
    void syncImportInspection(SjxxDto sjxxDto) throws BusinessException;
	/**
	 * 修改检测项目名称
	 * @param sjxxDto
	 * @return
	 */
    boolean updateJcxmmc(SjxxDto sjxxDto);

	/**
	 * 修改检测单位
	 * @param sjxxDto
	 * @return
	 */
    boolean updateJcdw(SjxxDto sjxxDto);

    void updateSfjsInfo(List<SjxxDto> list);

	/**
	 * 根据样本编号更新分析完成时间
	 * @param sjxxDto
	 */
	 void updateFxwcsjByYbbh(SjxxDto sjxxDto);

	boolean updateFkje(List<SjxxDto> list);
}
