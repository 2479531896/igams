package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.FjcfbModel;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.UserDto;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.ExternalMessageModel;
import com.matridx.igams.wechat.dao.entities.FjsqDto;
import com.matridx.igams.wechat.dao.entities.ResFirstDto;
import com.matridx.igams.wechat.dao.entities.SjdwxxDto;
import com.matridx.igams.wechat.dao.entities.SjjcxmDto;
import com.matridx.igams.wechat.dao.entities.SjkzxxDto;
import com.matridx.igams.wechat.dao.entities.SjsyglDto;
import com.matridx.igams.wechat.dao.entities.SjwzxxDto;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.SjxxModel;
import com.matridx.igams.wechat.dao.entities.SjybztDto;
import com.matridx.igams.wechat.dao.entities.WeChatInspectionReportsModel;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

public interface ISjxxService extends BaseBasicService<SjxxDto, SjxxModel>{
	/**
	 * 查询所有送检信息
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getPagedDtoList(SjxxDto sjxxDto);

	/**
	 * 查找未收款记录
	 * @return
	 */
    List<SjxxDto> getDtoBySf(String csid);

	/**
	 * 设置HttpServletRequest
	 * @return
	 */
    void saveHttpServletRequest(HttpServletRequest request);

	/**
	 * 分页查找未收款记录
	 * @param sjxxDto
	 * @return
	 */
	List<SjxxDto> getPagedDtoBySf(SjxxDto sjxxDto);

	/**
	 * 批量更新
	 * @return
	 */
    Boolean updateList(List<SjxxDto> list);
	/**
	 * 查找未收款记录
	 * @return
	 */
    List<SjxxDto> getCountByYxdb(SjxxDto sjxxDto);

	/**
	 * 查找未收款记录发送给医学代表
	 * @return
	 */
    List<SjxxDto> getListByYxdb(SjxxDto sjxxDto);
//	/**
//	 * 通过样本编号获取相关的信息
//	 * @return
//	 */
//	public List<SjxxDto> getInfoByYbbh(SjxxDto sjxxDto);
	/**
	 * 查找未收款记录发送给大区经理
	 * @return
	 */
    List<SjxxDto> getListByDqjl(SjxxDto sjxxDto);
	/**
	 * 查找未收款记录数发送给大区经理
	 * @return
	 */
    List<SjxxDto> getCountByDqjl(SjxxDto sjxxDto);
	
	/**
	 * 通过id查询
	 * @param sjxxDto
	 * @return
	 */
    SjxxDto getDtoById(SjxxDto sjxxDto);
	
	/**
	 * 修改送检信息
	 * @param sjxxDto
	 * @return
	 */
	Map<String, Object> UpdateSjxx(SjxxDto sjxxDto, User user, SjkzxxDto sjkzxxDto) throws BusinessException;
	
	/**
	 * 查询科室信息
	 */
    List<SjdwxxDto> getSjdw();


	/**
	 * 查询送检信息
	 */
    List<SjxxDto> getDtoAllList(SjxxDto sjxxDto);
	
	/**
	 * 保存送检信息
	 * @param sjxxDto
	 */
	void checkApply(SjxxDto sjxxDto);

	/**
	 * 获取检验申请清单
	 * @param sjxxDto
	 * @return
	 */
    Map<String,Object> getApplyList(SjxxDto sjxxDto);

    /**
	 * 检验结果导入保存
	 * @param sjxxDto
	 * @return
     * @throws BusinessException 
	 */
    boolean uploadSaveReport(SjxxDto sjxxDto) throws BusinessException;

	/**
	 * z项目文件保存
	 * @param sjxxDto
	 * @return
	 * @throws BusinessException
	 */
    String uploadSaveFile(SjxxDto sjxxDto) throws BusinessException;

	/**
	 * Z项目文件解析
	 * @param
	 * @return
	 * @throws BusinessException
	 * @throws Exception 
	 */
    boolean disposeFile(Map<String, Object> map, User userDto) throws Exception;

	/**
	 * 根据送检ID查询附件表信息
	 * @param sjid
	 * @return
	 */
    List<FjcfbDto> selectFjByWjid(String sjid);
	
	/**
	 * 添加其他信息
	 * @param sjxxDto
	 * @return
	 */
    boolean insertAll(SjxxDto sjxxDto, String yhid);
	/**
	 * 添加所有
	 * @param sjxxDto
	 * @return
	 */
    boolean AddSjxx(SjxxDto sjxxDto, SjkzxxDto sjkzxxDto);
	
	/**
	 * 修改合作伙伴
	 * @param sjxxDto
	 * @return
	 */
    boolean updateDB(SjxxDto sjxxDto);

	/**
	 * 确认标本信息
	 * @param sjxxDto
	 * @return
	 */
    boolean sampleConfig(SjxxDto sjxxDto);

	/**
	 * 送检收样确认保存
	 * @param sjxxDto
	 * @return
	 * @throws BusinessException 
	 */
    boolean inspectionSaveConfirm(SjxxDto sjxxDto, List<String> stringList, SjkzxxDto sjkzxxDto) throws BusinessException;

	/**
	 * 获取送检结果接口
	 * @return
	 */
    boolean getInspectionResult(SjxxDto sjxxDto);
	
	/**
	 * 获取送检结果接口
	 * @return
	 */
    boolean receiveInspectionResult(String str);
	
	
	/**
	 * 获取修改信息
	 * @param str
	 * @return
	 */
    boolean receiveModSjxx(String str);
	
	/**
	 * 获取送检物种信息
	 * @param sjxxDto
	 * @return
	 */
    List<SjwzxxDto> selectWzxxBySjid(SjxxDto sjxxDto);

	/**
	* @param sjxxDto
	* @return
	*/
    List<FjcfbDto> preview(SjxxDto sjxxDto);
	
	/**
	 * 手机支付结果
	 * @param sjxxDto
	 * @return
	 */
    boolean weChatPayResult(SjxxDto sjxxDto);
	
	/**
	 * 保存手机修改信息
	 * @param sjxxDto
	 * @param oldDto
	 * @throws BusinessException 
	 */
    void checkModApply(SjxxDto sjxxDto,SjxxDto oldDto) throws BusinessException;
	
	/**
	 * 同步修改信息
	 * @param sjxxDto
	 * @return
	 */
    boolean updateforbioinformation(SjxxDto sjxxDto);

	/**
	 * 保存word报告,转换成pdf
	 * @param file
	 * @param sjxxDto
	 * @return
	 * @throws BusinessException 
	 */
    boolean receiveWordReport(MultipartFile file, SjxxDto sjxxDto) throws BusinessException;

	/**
	 * 保存送检报告信息
	 * @param info 
	 * @return
	 */
    boolean saveInspectionResult(WeChatInspectionReportsModel info);
	/**
	 * 给相应人员发送文件消息
	 * @param sjxxDto
	 * @return
	 * @throws BusinessException 
	 */
    boolean sendReportMessage(SjxxDto sjxxDto) throws BusinessException;


	/**
	 * 给相应人员发送邮箱消息
	 * @param sjxxDto
	 * @return
	 * @throws BusinessException
	 */
    boolean sendReportOnlyToYx(SjxxDto sjxxDto) throws BusinessException;
	

		/**
	 * 通过送检id查询检测标记
	 * @param sjxxDto
	 * @return
	 */
        SjxxDto selectjcbjByid(SjxxDto sjxxDto);
	
	/**
	 * 修改检测标记（送检表）
	 * @param sjxxDto
	 * @return
	 * @throws BusinessException 
	 */
    boolean updateJcbjByid(SjxxDto sjxxDto) throws BusinessException;

	/**
	 * 发送文件至微信服务器
	 * @param fjcfbModel
	 * @return
	 */
    boolean sendFileToAli(FjcfbModel fjcfbModel);
	/**
	 * 发送多文件至微信服务器
	 * @param fjcfbModels
	 * @return
	 */
    boolean sendFilesToAli(List<FjcfbModel> fjcfbModels);
	
	/**
	 * 标本统计页面，跟统计接口一样
	 * @return
	 */
    ModelAndView getStatisPage();
	
	/**
	 * 高关注度
	 * @return
	 */
    List<Map<String, String>> getWzxxByGgzd(SjxxDto sjxxDto);
	
	/**
	 * 按照送检单位统计个数。点击后按照送检医生排名
	 * @return
	 */
    List<Map<String, String>> getStatisBysjhb(SjxxDto sjxxDto);

	/**
	 * 按照送检单位统计个数。点击后按照送检医生排名(接收日期)
	 * @return
	 */
    List<Map<String, String>> getStatisBysjhbAndJsrq(SjxxDto sjxxDto);

	List<Map<String, String>> getStatisWzSf(SjxxDto sjxxDto);

	/**
	 * 按照周送检单位统计个数。点击后按照送检医生排名
	 * @return
	 */
    List<Map<String, String>> getStatisWeekBysjhb(SjxxDto sjxxDto);
	/**
	 * 按照周送检单位统计个数。点击后按照送检医生排名(接收日期)
	 * @return
	 */
    List<Map<String, String>> getStatisWeekBysjhbAndJsrq(SjxxDto sjxxDto);
	
	/**
	 * 按照合作伙伴，查找所属的送检单位的送检信息
	 * @return
	 */
    List<Map<String, String>> getSjdwBydb(SjxxDto sjxxDto);
	
	/**
	 * 根据日期查询每天标本数量
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getStatisByDate(SjxxDto sjxxDto);
	/**
	 * 根据日期查询每天标本数量(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getStatisByDateAndJsrq(SjxxDto sjxxDto);
	
	/**
	 * 根据标本类型统计标本信息(按照日期)
	 * @return
	 */
    List<Map<String, String>> getStatisByYblx(SjxxDto sjxxDto);
	/**
	 * 根据标本类型统计标本信息(按照日期)(接收日期)
	 * @return
	 */
    List<Map<String, String>> getStatisByYblxAndJsrq(SjxxDto sjxxDto);
	
	/**
	 * 统计结果类型为高关，疑似以及无的标本数量
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getYbnumByJglx(SjxxDto sjxxDto);
	
	/**
	 * 发送统计连接给送检人员
	 * @return
	 */
    boolean sendStatis();
	
	/**
	 * 发送日报链接给送检人员
	 * @return
	 */
    boolean sendStatisByDay();
	/**
	 * 查询当天标本数量
	 * @param sjxxDto
	 * @return
	 */
    Map<String,Object> getYbslByday(SjxxDto sjxxDto);
	
	/**
	 *统计复检申请
	 * @param fjsqDto
	 * @return
	 */
    List<Map<String,String>> getFjsqByDay(FjsqDto fjsqDto);
	
	/**
	 * 按照日期统计送检报告个数，可按照日，月，年，显示指定前几个,需传递起始时间和时间列表
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getStatisSjbgByBgrq(SjxxDto sjxxDto);
	
	/**
	 * 统计每天标本的阳性率
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getStatisYxlBybgrq(SjxxDto sjxxDto);
	
	/**
	 * 统计每周的标本数变化情况
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getStatisWeekYbsByJsrq(SjxxDto sjxxDto);
	/**
	 * 统计每周的标本数变化情况(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getStatisWeekYbsByJsrqAndJsrq(SjxxDto sjxxDto);

	/**
	 * 查询送检报告完成度
	 * @param sjxxDto
	 * @return
	 */
    Map<String,Object> getBgWcd(SjxxDto sjxxDto);
	
	/**
	 * 获取最近一周标本送检路线
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,Object>> getSjxlxx(SjxxDto sjxxDto);
	
	/**
	 * 根据收费情况进行统计收样
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getSjxxBySy(SjxxDto sjxxDto);

	/**
	 * 根据收费情况进行统计收样(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getSjxxBySyAndJsrq(SjxxDto sjxxDto);

	/**
	 * 根据收费情况以周为单位进行统计收样(按照不收费的标本变化，收费标本的变化)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getSjxxWeekBySy(SjxxDto sjxxDto);
	/**
	 * 根据收费情况以周为单位进行统计收样(按照不收费的标本变化，收费标本的变化)(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getSjxxWeekBySyAndJsrq(SjxxDto sjxxDto);

	/**
	 * 外部发送消息
	 * @param sjxxDto
	 * @param message
	 * @param title
	 * @return
	 * @throws BusinessException 
	 */
    boolean sendOutMessage(SjxxDto sjxxDto, String message, String title) throws BusinessException;
	
	/**
	 *送检列表发送多条信息
	 * @param sjxxDto
	 * @return
	 * @throws BusinessException 
	 */
    boolean sendMessage(SjxxDto sjxxDto) throws BusinessException;

	/**
	 * 保存详细审核结果
	 * @param str
	 * @return
	 */
    boolean receiveDetailedReport(String str);
	
	/**
	 * 日报统计
	 * @return
	 */
    List<Map<String, String>> getYbxxByDay(SjxxDto sjxxDto);



	/**
	 * 日报统计(接收日期)
	 * @return
	 */
    List<Map<String, String>> getYbxxByDayAndJsrq(SjxxDto sjxxDto);

	/**
	 * 按照合作伙伴分类统计
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getYbxxByDb(SjxxDto sjxxDto);
	
	/**
	 * 按照统计名称分类统计
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getYbxxByTjmc(SjxxDto sjxxDto);
	/**
	 * 按照统计名称分类统计(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getYbxxByTjmcAndJsrq(SjxxDto sjxxDto);
	
	/**
	 * 查询扩展参数
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getSjxxCskz(SjxxDto sjxxDto);
	
	/**
	 * 修改参数扩展
	 * @param sjxxDto
	 * @return
	 */
    boolean updateCskz(SjxxDto sjxxDto);
	
	/**
	 * 根据 患者姓名，合作伙伴的信息，去查找非本标本编号的其他同类信息
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getPagedTlxxList(SjxxDto sjxxDto);
	
	/**
	 * 根据送检ids查询送检物种信息
	 * @param sjxxDto
	 * @return
	 */
    List<SjwzxxDto> selectWzxxBySjids(SjxxDto sjxxDto);
	
	/**
	 * 根据ids查询送检信息
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> selectSjxxBySjids(SjxxDto sjxxDto);
	
	/**
	 * 根据 患者姓名，合作伙伴的信息，去查找非本标本编号的其他同类信息(不分页)
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getTlxxList(SjxxDto sjxxDto);
	
	/**
	 * 查询内部编码是不是已经存在
	 * @param sjxxDto
	 * @return
	 */
    Integer getCountBynbbm(SjxxDto sjxxDto);

	/**
	 * 根据伙伴分类，伙伴子分类，代表名查询送检信息
	 * @param sjxxDto
	 * @return
	 */
    Map<String,List<Map<String, String>>> getStatisByFl(SjxxDto sjxxDto);
	
	/**
	 * 根据周的伙伴分类，伙伴子分类，代表名查询送检信息
	 * @param sjxxDto
	 * @return
	 */
    Map<String,List<Map<String, String>>> getStatisByWeekFl(SjxxDto sjxxDto);
	
	/**
	 * 查询标本编号是不是已经存在
	 * @param sjxxDto
	 * @return
	 */
    Integer getCountByybbh(SjxxDto sjxxDto);
	/**
	 * 验证标本编号以及外部编码是不是已经存在
	 * @return
	 */
    Integer getCountByWbbmAndYbbh(SjxxDto sjxxDto);
	
	/**
	 * 生信部访问查询方法
	 * @param sjxxDto
	 * @return
	 */
    SjxxDto getDtoForbiont(SjxxDto sjxxDto);

	/**
	 * 查询分类下伙伴信息
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getStatisByHbfl(SjxxDto sjxxDto);
	
	/**
	 * 查询分类下统计名称伙伴信息
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getStatisByTjHbfl(SjxxDto sjxxDto);

	/**
	 * 查询分类下统计名称伙伴信息(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getStatisByTjHbflAndJsrq(SjxxDto sjxxDto);

	/**
	 * 查询分类下伙伴信息(周)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getStatisByWeekHbfl(SjxxDto sjxxDto);
	
	/**
	 * 查询分类下统计名称伙伴信息(周)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getStatisByWeekTjHbfl(SjxxDto sjxxDto);

	/**
	 * 查询分类下统计名称伙伴信息(周)(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getStatisByWeekTjHbflAndJsrq(SjxxDto sjxxDto);

	/**
	 * 设置周报返回对应ID
	 * @param fllist
	 * @param name
	 * @return
	 */
    Map<String, List<Map<String, String>>> setReceiveId(List<Map<String, String>> fllist, String name);

	/**
	 * 根据合作伙伴查询送检数量
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getStatisByDb(SjxxDto sjxxDto);
	
	/**
	 * 根据统计名称查询送检数量
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getStatisByTj(SjxxDto sjxxDto);
	/**
	 * 根据统计名称查询送检数量(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, String>> getStatisByTjAndJsrq(SjxxDto sjxxDto);
	
	/**
	 * 统计废弃标本
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getFqybByYbzt(SjxxDto sjxxDto);
	
	/**
	 * 废弃标本列表
	 * @param sjybztDto
	 * @return
	 */
    List<SjxxDto> getPageFqybList(SjybztDto sjybztDto);
	
	/**
	 * 统计所有的溶血标本（nbbm为B开头的）
	 * @param sjybztDto
	 * @return
	 */
    Integer getCountAllSjxx(SjybztDto sjybztDto);
	/**
	 * 统计所有的溶血标本（nbbm为B开头的）(接收日期)
	 * @param sjybztDto
	 * @return
	 */
    Integer getCountAllSjxxByJsrq(SjybztDto sjybztDto);
	
	/**
	 * 统计所有状态为溶血的标本数量
	 * @param sjybztDto
	 * @return
	 */
    Integer getCountByzt(SjybztDto sjybztDto);
	
	/**
	 * 统计所有的废弃标本
	 * @param sjybztDto
	 * @return
	 */
    Integer getAllFqyb(SjybztDto sjybztDto);
	
	/**
	 * 外部提交复检审核
	 * @param str
	 * @return
	 */
    boolean submitRecheckAudit(String str, HttpServletRequest request);
	
	/**
	 * 根据送检ids判断所选数据检测项目是否相同
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> checkJcxm(SjxxDto sjxxDto);
	
	/**
	 * 判断当天实验的标本实验报告是否全部完成
	 * @return
	 */
    void checkSybgSfwc();


	/**
	 * 定时发送今日实验报告数据
	 * @param
	 * @return
	 */
    void sendReportsNumber( Map<String,String> csMap);
	
	/**
	 * 数据滚动刷新
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getListSjxx(SjxxDto sjxxDto);
	
	/**
	 * 查询刷新之间新增的送检信息
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getListSjxxBylrsj(SjxxDto sjxxDto);
	
	/**
	 * 报告阳性率列表
	 * @param sjwzxxDto
	 * @return
	 */
    List<SjxxDto> getListPositive(SjwzxxDto sjwzxxDto);
	
	/**
	 * 合作伙伴查看送检列表
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getPageDtoBySjhb(SjxxDto sjxxDto);
	
	/**
	 * 统计送检物种信息
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getWzxxBySjid(SjxxDto sjxxDto);
	
	/**
	 * 保存反馈消息
	 * @param sjxxDto
	 */
    void getFeedBack(SjxxDto sjxxDto);

	/**
	 * 保存附件
	 * @param
	 * @return
	 */
    boolean saveScanFile(SjxxDto sjxxDto) throws BusinessException;
	boolean saveLocalScanFile(SjxxDto sjxxDto) ;

	/**
	 * 自定义统计图
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> customStatis(SjxxDto sjxxDto);
	
	/**
	 * 自定义统计（列表显示）
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getCustomStatisList(SjxxDto sjxxDto);
	
	/**
	 * 添加反馈信息
	 * @param sjxxDto
	 * @return
	 */
    boolean updateFeedBack(SjxxDto sjxxDto);

	/**
	 * 根据送检ID查询伙伴信息
	 * @param sjid
	 * @return
	 */
    List<SjxxDto> selectBgmbBySjid(String sjid);
	
	/**
	 * 日报增加送检清单
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getListBydaily(SjxxDto sjxxDto);

	/**
	 * 日报增加送检清单(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getListBydailyAndJsrq(SjxxDto sjxxDto);

	/**
	 * 获取内部编码（组装）
	 * @param sjxxDto
	 * @return
	 */
    String getNbbm(SjxxDto sjxxDto);

	/**
	 * 根据规则生成内部编码
	 * @param sjxxDto
	 * @return string
	 *
	 */
	String generateNbbm(SjxxDto sjxxDto) throws BusinessException;
	/**
	 * 检测项目个数
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getYbxxDRByDay(SjxxDto sjxxDto);

	/**
	 * 检测项目个数(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getYbxxDRByDayAndJsrq(SjxxDto sjxxDto);

	/**
	 * 检测项目个数（按照检测单位）
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getYbxxDRByDayAndJcdw(SjxxDto sjxxDto);

	/**
	 * 检测项目个数（按照检测单位）(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getYbxxDRByDayAndJcdwAndJsrq(SjxxDto sjxxDto);

	/**
	 * 收费标本里边检测数量
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getYbxxTypeByDay(SjxxDto sjxxDto);

	/**
	 * 收费标本里边检测数量(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getYbxxTypeByDayAndJsrq(SjxxDto sjxxDto);

	/**
	 * 按照日期统计收费标本
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>>  getSjxxDRBySy(SjxxDto sjxxDto);
	/**
	 * 按照日期统计收费标本(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>>  getSjxxDRBySyAndJsrq(SjxxDto sjxxDto);
	
	/**
	 * 按照周统计检测总条数
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>>  getSjxxDRByWeek(SjxxDto sjxxDto);

	/**
	 * 按照周统计检测总条数(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>>  getSjxxDRByWeekAndJsrq(SjxxDto sjxxDto);

    /**
     * 周报统计收费标本下边检测项目的总条数
     * @param sjxxDto
     * @return
     */
    List<Map<String, String>> getYbxxTypeBYWeek(SjxxDto sjxxDto);

    /**
     * 周报统计收费标本下边检测项目的总条数(接收日期)
     * @param sjxxDto
     * @return
     */
    List<Map<String, String>> getYbxxTypeBYWeekAndJsrq(SjxxDto sjxxDto);

    /**
	 * 周报统计收费标本下边的检测项目总条数(通过周)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getWeekYbxxType(SjxxDto sjxxDto);

    /**
	 * 周报统计收费标本下边的检测项目总条数(通过周)(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getWeekYbxxTypeByJsrq(SjxxDto sjxxDto);

	/**
	 * 统计上上周的临床反馈结果
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getLcfkByBefore(SjxxDto sjxxDto);
	
	/**
	 * 根据样类型统计前三的阳性率 
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getJyjgByYblx(SjxxDto sjxxDto);

	/**
	 * 增加科室的圆饼统计图
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getKsByweek(SjxxDto sjxxDto);
	/**
	 * 增加科室的圆饼统计图(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getKsByweekAndJsrq(SjxxDto sjxxDto);
	
	/**
	 * 增加合作医院数，科室数，医生数的统计表
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getSjdwSjysKs(SjxxDto sjxxDto);
	/**
	 * 增加合作医院数，科室数，医生数的统计表(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getSjdwSjysKsByJsrq(SjxxDto sjxxDto);
	/**
	 * 根据样本编号下载报告
	 * @param sjxxDto
	 * @param request
	 * @return
	 */
	Map<String, Object> reportZipDownloadByNbbms(SjxxDto sjxxDto, HttpServletRequest request);
	/**
	 * 根据条件下载报告压缩包
	 * @param sjxxDto
	 * @param request
	 * @return
	 */
    Map<String,Object> reportZipDownload(SjxxDto sjxxDto, HttpServletRequest request);

	/**
	 * 根据条件下载报告压缩包（伙伴权限限制）
	 * @param sjxxDto
	 * @param request
	 * @return
	 */
    Map<String, Object> reportZipDownloadSjhb(SjxxDto sjxxDto, HttpServletRequest request);
	
	/**
	 * 接收外部送检信息
	 * @param request
	 * @param jsonStr
	 * @return
	 * @throws BusinessException 
	 */
    boolean receiveInspectInfo(HttpServletRequest request, String jsonStr) throws BusinessException;


	/**
	 * 接收外部送检信息
	 * @param request
	 * @param jsonStr
	 * @return
	 * @throws BusinessException
	 */
    Map<String,Object> receiveInspectInfoMap(HttpServletRequest request, String jsonStr) throws BusinessException;

	/**
	 * 接收外部送检信息New--提供不同参数的接口，主要是为了防止 JSONObject.parseArray执行两次，提高效率
	 * @param request
	 * @param sjxxDtos
	 * @return
	 * @throws BusinessException
	 */
	Map<String,Object> receiveInspectInfoMap(HttpServletRequest request, List<SjxxDto> sjxxDtos) throws BusinessException;

	/**
	 * 接收外部送检信息Plus
	 * @param request
	 * @param jsonStr
	 * @return
	 * @throws BusinessException
	 */
    Map<String,Object> receiveInspectInfoMapPlus(HttpServletRequest request, String jsonStr) throws BusinessException;

	/**
	 * 通过内部编码获取送检信息
	 * @param sjxxDto
	 * @return
	 */
    SjxxDto getSjxxDto(SjxxDto sjxxDto);
	
	/**
	 * 小程序获取日报(根据伙伴权限进行限制)
	 * @param yhid
	 * @return
	 */
    Map<String, Object> sendStatisDailyToMiniProgram(String yhid, HttpServletRequest request);
	
	/**
	 * 小程序获取周报
	 * @param yhid
	 * @return
	 */
    Map<String, Object> sendStatisWeeklyToMiniProgram(String yhid, HttpServletRequest request);

	/**
	 * 整体送检结果信息
	 * @param str
	 * @return
	 */
    boolean receiveEntiretyResult(String str);

	/**
	 * 根据录入人员获取送检表的合作伙伴
	 * @param sjxxDto
	 * @return
	 */
    List<String> getDbsByLrrys(SjxxDto sjxxDto);
	
	/**
	 * 查询角色检测单位限制
	 * @param jsid
	 * @return
	 */
	List<Map<String,String>> getJsjcdwByjsid(String jsid);

	/**
	 * 送检结果分析列表
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getAnalysisList(SjxxDto sjxxDto);
	
	/**
	 * 查询根据日期查询实验的记录数据
	 * @param sjxxDto
	 * @return
	 */
	List<SjxxDto> selectInspSize(SjxxDto sjxxDto);

	/**
	 * 外部接口获取送检报告信息
	 * @param request
	 * @param sjxxDto
	 * @param code
	 * @return
	 */
    Map<String, Object> externalGetReport(HttpServletRequest request, SjxxDto sjxxDto, String code);

	/**
	 * 根据wbbm获取送检报告
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getListByWbbm(SjxxDto sjxxDto);

    List<SjxxDto> getDtosByWbbm(SjxxDto sjxxDto);

    /**
	 * 根据lastwbbm获取送检报告
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getListByLastWbbm(SjxxDto sjxxDto);

	/**
	 * 根据业务ID和业务类型查询下载文件
	 * @param sjxxDtos
	 * @return
	 */
    List<SjxxDto> selectDownloadReportBySjxxDtos(List<SjxxDto> sjxxDtos);

	/**
	 * 根据外部编码查询伙伴信息
	 * @param wbbm
	 * @return
	 */
    List<String> getSjhbByCode(String wbbm);

	/**
	 * 外部发送消息通知
	 * @param str
	 * @return
	 * @throws BusinessException 
	 */
    boolean sendExternalMessage(String str) throws BusinessException;

	/**
	 * 外部发送消息通知
	 * @param
	 * @return
	 * @throws BusinessException
	 */
    boolean sendExternalMessageNew(ExternalMessageModel externalMessageModel) throws BusinessException;

	/**
	 * 删除送检信息
	 * @param sjxxDto
	 */
    void delCheckApply(SjxxDto sjxxDto);

	/**
	 * 获取测试数信息根据检测单位区分
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getSjxxcssByJcdw(SjxxDto sjxxDto);
	/**
	 * 获取测试数信息根据检测单位区分(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getSjxxcssByJcdwAndJsrq(SjxxDto sjxxDto);
	
	/**
	 * 获取测试数信息根据检测单位区分(日报)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getSjxxcssByDayAndJcdw(SjxxDto sjxxDto);

	/**
	 * 获取测试数信息根据检测单位区分(日报)(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getSjxxcssByDayAndJcdwAndJsrq(SjxxDto sjxxDto);

	/**
	 * 检测项目个数(按照一段周期内检测单位进行)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getSjxxcssBySomeTimeAndJcdw(SjxxDto sjxxDto);

	/**
	 * 检测项目个数(按照一段周期内检测单位进行)(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getSjxxcssBySomeTimeAndJcdwAndJsrq(SjxxDto sjxxDto);

	/**
	 * 查询接收后但未实验的标本信息
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getNotSyList(SjxxDto sjxxDto);

	/**
	 * 查询接收后但未实验的标本信息(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getNotSyListByJsrq(SjxxDto sjxxDto);

	/**
	 * 首页统计:统计当天实验的数据量(根据检测单位区分)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String,String>> getSjlStatisticsByJcdw(SjxxDto sjxxDto);

	/**
	 * 文件下载
	 * @param fjcfbModel
	 * @return
	 */
    boolean downloadFile(FjcfbModel fjcfbModel);
	
	/**
	 * 更新报告日期
	 * @param sjxxDto
	 * @return
	 */
    int updateReport(SjxxDto sjxxDto);
	
	/**
	 * 以map为返回值查询SJxx
	 * @param sjid
	 * @param jclx 检测类型
	 * @param jczlx 检测子类型
	 * @return
	 */
    Map<String,Object> getMapBySjid(String sjid, String jclx,String jczlx);
	
	/**
	 * 根据接收日期的开始日期和结束日期，自动计算每一天的日期，形成一个list
	 * 
	 * @param sjxxDto
	 * @return
	 */
    List<String> getRqsByStartAndEnd(SjxxDto sjxxDto);
	
	/**
	 * 修改实验日期
	 * @param sjxxDto
	 * @return
	 */
    boolean updateSyrq(SjxxDto sjxxDto);
	
	/**
	 * 查询送检待发送报告
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> querySjxx(SjxxDto sjxxDto);
	
	/**
	 * 获取总数
	 * @param sjxxDto
	 * @return
	 */
	Integer querysjxxnum(SjxxDto sjxxDto);
	
	/**
	 * 获取上传word但未发送报告总数
	 * @param sjxxDto
	 * @return
	 */
    int queryWordNum(SjxxDto sjxxDto);
	
	/**
	 * 获取上传word但未发送报告信息
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> queryWord(SjxxDto sjxxDto);

	/**
	 * 获取前一天到今天6点前审核中的复测验证数量
	 * @param sjxxDto
	 * @return
	 */
	int queryFjAndYzBeforeNum(SjxxDto sjxxDto);

	/**
	 * 获取前一天到今天6点前审核中的复测验证信息
	 * @param sjxxDto
	 * @return
	 */
	List<SjxxDto> queryFjAndYzBefore(SjxxDto sjxxDto);

	/**
	 * 获取今天6后前审核中的复测验证数量
	 * @param sjxxDto
	 * @return
	 */
	int queryFjAndYzAfterNum(SjxxDto sjxxDto);

	/**
	 * 获取今天6后前审核中的复测验证信息
	 * @param sjxxDto
	 * @return
	 */
	List<SjxxDto> queryFjAndYzAfter(SjxxDto sjxxDto);

	/**
	 * 获取接收日期不为空，报告未发送的记录
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getAccptNotUpload(SjxxDto sjxxDto);

	/**
	 * 获取实验列表数据
	 * @param
	 * @return
	 */
    List<SjxxDto> getExperimentList(SjxxDto sjxxDto);
	
    /**
	 * 按照区分字段去送检表中查找一条记录
     * @param sjxxDto
     * @return
     */
    SjxxDto getOneFromSJ(SjxxDto sjxxDto);

    /**
	 * 按照区分字段去复检表中查找一条记录
	* @param sjxxDto
	* @return
	*/
	SjxxDto getOneFromFJ(SjxxDto sjxxDto);

	/**
	 * 从送检表中找出检查项目的类别数量
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> jcxmFromSJ(SjxxDto sjxxDto);

	/**
	 * 从复检表中找出检查项目的类别数量
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> jcxmFromFJ(SjxxDto sjxxDto);
    
	/**
	 * 根据ids(实际是fjid)查询送检信息
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> selectSjxxByFjids(SjxxDto sjxxDto);

	/**
	 * 修改检测标记（修改复检表）
	 * @param sjxxDto
	 * @return
	 * @throws BusinessException 
	 */
    boolean updateJcbjByFjid(SjxxDto sjxxDto) throws BusinessException;

	/**
	 * 同步送检支付信息
	 * @param sjxxDto
	 */
    void modInspPayinfo(SjxxDto sjxxDto);
	
	/**
	 * 根据岗位名称查询用户信息
	 * @param userDto
	 * @return
	 */
    List<UserDto> queryYhByGwmc(UserDto userDto);
	/**
	 * 根据角色名称查询用户信息
	 * @param userDto
	 * @return
	 */
    List<UserDto> queryYhByJsmc(UserDto userDto);
	
	/**
     * 检测实验日期是否为空
     * @param sjxxDto
     * @return
     */
    Map<String,Object> checkSyrq(SjxxDto sjxxDto);

	/**
	 * 根据检测项目查询检测子项目
	 * @param jcxmid
	 * @return
	 */
    List<JcsjDto> getSubDetect(String jcxmid);

	/**
	 * 清空检测检测子项目
	 * @param sjxxDto
	 * @return
	 */
    boolean emptySubDetect(SjxxDto sjxxDto);

	/**
	 * 查询所有送检信息(优化)
	 * @param params
	 * @return
	 */
    List<SjxxDto> getDtoListOptimize(Map<String, Object> params);

	/**
	 * 测试方法，待删除
	 * @return
	 */
    boolean kdhRemind();

	/**
	 * 标本数列表查询
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getBbsList(SjxxDto sjxxDto);

	/**
	 * 标本数列表查询(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getBbsListByJsrq(SjxxDto sjxxDto);


	/**
	 * 查询所有伙伴
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getAllSjhb(SjxxDto sjxxDto);
	/**
	 * 时效性统计
	 * @param sjxxDto
	 * @return
	 */
    SjxxDto getSxx(SjxxDto sjxxDto);
	/**
	 * 复测率统计
	 * @param sjxxDto
	 * @return
	 */
    SjxxDto getFcl(SjxxDto sjxxDto);
	/**
	 * 送检单位转义
	 * @param sjxxDto
	 * @return
	 */
    String escapeSjdw(SjxxDto sjxxDto);

	/**
	 * sjxx导入
	 * @param sjxxDto
	 * @return
	 */
    boolean insertSjxx(SjxxDto sjxxDto);

	/**
	 * 其它
	 * @return
	 */
    String getQt();
	/**
	 * 其它
	 * @param
	 * @return
	 */
    String getYyxxQt();
	/**
	 * 科室转义
	 * @param sjxxDto
	 * @return
	 */
    String escapeKs(SjxxDto sjxxDto);

	/**
	 * 查询当日标本编号和复检原因
	 * @return
	 */
    List<SjxxDto> getYbbhAndYyToday();

	/**
	 * 提交送检验证审核
	 * @return
	 */
    Map<String,Object> sendPCRAudit(SjxxDto sjxxDto);

	/**
	 * 根据样本编号模糊查询送检信息
	 * @param sjxxDto
	 * @return
	 */
    SjxxDto getDtoVague(SjxxDto sjxxDto);

	/**
	 * 查找接收日期为空的可废弃数据
	 * @param sjxxDto
	 * @return
	 */
	List<String> getJsrqIsNull(SjxxDto sjxxDto);
	/**
	 * 更新送检信息
	 * @param sjxxDto
	 * @return
	 */
	boolean updateModel(SjxxDto sjxxDto);

	/**
	 * 获取样本编号对应检测项目以及合作伙伴的参数扩展3
	 * @param ybbh
	 * @return
	 */
	SjxxDto getSjxxGlCskz3(String ybbh);
	
	/**
	 * 通过检测单位限制查找数据
	 * @param sjxxDto
	 * @return
	 */
    SjxxDto getInfo(SjxxDto sjxxDto);

	/**
	 * 修改保存时根据确认状态发送消息
	 *
	 * @return
	 */
    boolean sendMessageByConfirmStatus(SjxxDto sjxxDto, User user) throws BusinessException;

	/**
	 * 查询有word报告的送检信息
	 * @param params
	 * @return
	 */
    List<SjxxDto> selectReportInfo(Map<String,Object> params);

	/**
	 * 查询快递信息
	 * @return
	 */
    boolean expressRemind();
	/**
	 * 修改特殊申请
	 * @param sjxxDto
	 * @return
	 */
    boolean updateTssq(SjxxDto sjxxDto);

	/**
	 * 根据样本编号查询送检基本信息
	 * @param sjxxDto
	 * @return
	 */
	SjxxDto getSjxxInfoByYbbh(SjxxDto sjxxDto);
	/**
	 * 从Dto里把数据放到Map里，减少Dto的属性设置，防止JSON出错
	 * @param sjxxDto
	 * @return
	 */
    Map<String,Object> pareMapFromDto(SjxxDto sjxxDto);
	/*
	* 获取实付总金额和退款总金额
	* */
    SjxxDto getSfzjeAndTkzje(SjxxDto sjxxDto);

	/**
	 * 查询检测单位
	 * @param hbmc
	 * @return
	 */
    List<JcsjDto> getDetectionUnit(String hbmc);

	/**
	 * 接口调用方法(新线程)
	 */
	void matchingUtilNewRun(Map<String,Object> map,String method);


	/**
	 * 定时任务调用杏和接口查询数据
	 * @param codeMap
	 * @throws Exception
	 */
	void timedCallXinghe(Map<String,Object> codeMap);

	/**
	 * 定时任务调用杏和接口查询数据 6.0
	 * @param codeMap
	 * @throws Exception
	 */
	void timedCallXingheNew(Map<String,Object> codeMap);

	/**
	 * 获取十天内的芯片信息
	 * @return
	 */
	List<SjxxDto> getPagedDtoListDays(SjxxDto sjxxDto);
	/**
	 * 样本进度列表
	 * @return
	 */
	List<SjxxDto> getPagedSampleProgressList(SjxxDto sjxxDto);
	/**
	 * 审核列表
	 * @param
	 * @return
	 */
    List<SjxxDto> getPagedAuditDevice(SjxxDto sjxxDto);
	
	/**
	 * 获取实验员列表头部数据
	 * @return
	 */
	Map<String, Object> querySjSyxxnums(SjxxDto sjxxDto);

	/**
	 * 实验统计数量
	 * @return
	 */
	Map<String, Object> queryExperimentNums(SjxxDto sjxxDto);

	/**
	 * 查询所有送检信息
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getPagedExperimentDtoList(SjxxDto sjxxDto);

	/**
	 * 已接受未实验数据
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getConfirmDtoList(SjxxDto sjxxDto);


	/**
	 * 获取ResFirst列表数据
	 * @param resFirstDto
	 * @return
	 */
    List<ResFirstDto> getPagedResFirstList(ResFirstDto resFirstDto);
	
	/**
	 * 更新取样信息
	 * @param sjxxDto
	 * @return
	 */
    boolean updateSampleInfo(SjxxDto sjxxDto);
	/**
	 * 测试数统计信息
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> testNumberList(SjxxDto sjxxDto);

	/**
	 * 根据送检ID和检测类型获取送检数据
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getSjxxDtosByYbbh(SjxxDto sjxxDto);

	/**
	 * 付款维护
	 * @param sjjcxmDto
	 * @return
	 */
	Boolean saveProjectAmount(SjjcxmDto sjjcxmDto) throws BusinessException;

	/**
	 * 修改检测单位
	 * @param sjxxDto
	 * @return
	 */
    boolean updateJcdw(SjxxDto sjxxDto);

	/**
	 * 获取分页的ResFirst列表数据
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getPagedSalesResFirstList(SjxxDto sjxxDto);
    
	/**
	 * 删除
	 */
	boolean deleteDto(SjxxDto sjxxDto);
	
	/**
	 * 查询极简送检信息，不关联其他表
	 * @param sjxxDto
	 * @return
	 */
	SjxxDto getSimpleDto(SjxxDto sjxxDto);

	/**
	 * 更改是否接收，接收时间，接收人员为null
	 * @param sjxxDtoList
	 */
    void updateSfjsInfo(List<SjxxDto> sjxxDtoList);

	/**
	 * 根据sjid查询多个检测项目
	 * @param sjid
	 * @return
	 */
	Map<String, String> getAllSjxxOther(String sjid);

	/**
	 * 返回送检信息的Map形式
	 * @param sjxxDto
	 * @return
	 */
	Map<String,String> getDtoMap(SjxxDto sjxxDto);

	/**
	 * 获取分时报告数
	 * @return
	 */
	List<Map<String,String>>getFsbgs(String xjsj);

	/**
	 * 定时任务报告发送情况清单列表里统计图的每一段时间报告的发送个数
	 * @param xjsj
	 * @return
	 */
	List<String> getTimeByTime(String xjsj);
	
	
	/**
	 * 钉钉小程序端根据样本编号获取下机时间
	 * @param sjxxDto
	 * @return
	 */
	List<SjxxDto> getQdListByYbbh(SjxxDto sjxxDto);
	
	/**
	 * 根据页面传递的送检实验数据，结合原有的数据，原本就存在的检测类型数据，则进行更新，没有的数据则进行新增
	 * 涉及 项目实验管理表，送检实验管理表
	 * @param insertInfo 新加的送检实验管理的数据
	 * @param sjxxDto
	 * @param yhid
	 * @return
	 */
	public boolean addOrUpdateSyData(List<SjsyglDto> insertInfo,SjxxDto sjxxDto,String yhid);

	/**
	 *  处理送检信息保存
	 * @param sjxxDto
	 * @param sjsyglDtos
	 * @param yhid
	 * @return
	 */
	boolean dealSaveInfo(SjxxDto sjxxDto,List<SjsyglDto> sjsyglDtos,String yhid) throws BusinessException;

	public Map<String,Object> packageZipAndDownload(HttpServletRequest request, HttpServletResponse httpResponse) throws IOException;

	/**
	 *更新应付金额
	 * @param sjxxDtos
	 * @return
	 */
	public boolean updateFkje(List<SjxxDto> sjxxDtos);

	/**
	 * 解析多层map为单层map
	 * @param map
	 * @param prefix
	 * @return
	 */
	Map<String, Object> flatten(Map<String, Object> map, String prefix);

	/**
	 * 查找类似标本
	 * @param sjxxDto
	 * @return
	 */
	List<SjxxDto> getSimilarList(SjxxDto sjxxDto);
	/**
	 * 查找类似标本项目
	 * @param sjxxDto
	 * @return
	 */
	List<SjxxDto> getSimilarSjxxJcxmList(SjxxDto sjxxDto);

	List<SjwzxxDto> selectWzxxBySjidAndJclx(SjxxDto sjxxDto);

	/**
	 * 根据自动化编码获取标本实验信息
	 * @param map
	 * @return
	 */
	List<Map<String,String>> getSyxxByZdhbm(Map<String,String> map);

	/**
	 * 数据合并
	 * @param copy1
	 * @param copy2
	 * @param user
	 */

	void copySjxx(SjxxDto copy1,SjxxDto copy2,User user);

/**
	 * @Description: 查询未上传的报告
	 * @param searchMap
	 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 * @Author: 郭祥杰
	 * @Date: 2025/2/25 16:21
	 */
	List<Map<String, Object>> getAbnormalReportList(Map<String, Object> searchMap);
	/**
	 * @Description: 根据sjid查询检测项目
	 * @param sjid
	 * @return com.matridx.igams.wechat.dao.entities.SjxxDto
	 * @Author: 郭祥杰
	 * @Date: 2025/2/25 16:22
	 */
	SjxxDto selectReportBySjid(String sjid);
	
	/**
	 * 根据sjid从wkcx查询wkbh
	 * @param sjxxDto
	 * @return
	 */
	List<Map<String,String>> getWkcxBySjid(SjxxDto sjxxDto);

	/**
	 * 根据wkbh从wkcx查询wkbh
	 * @param map
	 * @return
	 */
	List<Map<String,String>>getWkcxByWkbh(Map<String,String> map);

	/**
	 * 根据wkbh从ncpc查询ncpc
	 * @param map
	 * @return
	 */
	List<Map<String,String>>getNcPcByWkbh(Map<String,String> map);

	/**
	 * 处理Blast数据
	 * @param sjwzxxDto
	 * @param fjcfbDto
	 * @return
	 */
	String dealBlast(SjwzxxDto sjwzxxDto,FjcfbDto fjcfbDto);

	/**
	 * 修改送检信息根据sjid
	 * @param sjxxDto
	 * @return
	 */
	boolean updateByid(SjxxDto sjxxDto);

	/**
	 *
	 * 根据送检信息更新文库名
	 * @param sjxxDtos
	 * @return
	 */
	boolean updateWbbmByList(List<SjxxDto> sjxxDtos);
	
	/**
	 *
	 * 根据文件名获取fjcfb
	 * @param map
	 * @return
	 */
	List<Map<String,String>>getFjByWjm(Map<String,String> map);
}

