package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.JkdymxDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.wechat.dao.entities.FjsqDto;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.SjyzDto;
import com.matridx.igams.wechat.dao.entities.SjyzjgDto;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface ISjxxWsService {

	/**
	 * 钉钉小程序获取送检列表
	 * 
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getMiniSjxxList(SjxxDto sjxxDto);

	/**
	 * ddid获取到检测单位
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getJcdwByDdid(SjxxDto sjxxDto);

	/**
	 * 钉钉小程序获取未反馈列表
	 * 
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getMiniFeedbackList(SjxxDto sjxxDto);

	/**
	 * 微信小程序获取送检列表
	 * 
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getWeChatSjxxList(SjxxDto sjxxDto);

	/**
	 * 微信小程序获取未反馈列表
	 * 
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getWeChatFeedbackList(SjxxDto sjxxDto);

	/**
	 * 根据外部编码下载报告
	 * 
	 * @param request
	 * @param organ
	 * @param type
	 * @param code
	 * @param lastcode
	 * @param sign
	 * @param response
	 * @return
	 */
    Map<String, Object> downloadByCode(HttpServletRequest request, String organ, String type, String code,
                                       String lastcode, String sign, HttpServletResponse response);
	/**
	 * 根据外部编码下载报告Plus
	 *
	 * @param request
	 * @param organ
	 * @param type
	 * @param code
	 * @param lastcode
	 * @param sign
	 * @param response
	 * @return
	 */
    Map<String, Object> downloadByCodePlus(HttpServletRequest request, String organ, String type, String code,
                                       String lastcode, String sign, HttpServletResponse response);

	/**
	 * 文件下载(公用)
	 * 
	 * @param filePath
	 * @param wjm
	 * @param response
	 */
    void download(String filePath, String wjm, HttpServletResponse response);

	/**
	 * 校验外部参数信息
	 * 
	 * @param organ
	 * @param type
	 * @param code
	 * @param lastcode
	 * @param sign
	 * @param crypt
	 * @return
	 */
	Map<String, Object> checkSecurity(String organ, String type, String code, String lastcode, String sign ,DBEncrypt crypt);

	/**
	 * 校验外部参数信息
	 * @param organ 机构 wbaqzy.code
	 * @param type
	 * @param code 外部编码
	 * @param lastcode 清单中的最后一个编码
	 * @param sign 哈希值
	 * @param flag 是否code、lastcode必填
	 * @param crypt 加密
	 * @return
	 */
    Map<String, Object> checkSecurity(String organ, String type, String code, String lastcode, String sign, boolean flag, DBEncrypt crypt);
	/**
	 * 校验外部参数信息(送检)
	 * 
	 * @param organ
	 * @param str
	 * @param sign
	 * @param crypt
	 * @return
	 */
	Map<String, Object> checkSecurityReceive(String organ, String str, String sign, DBEncrypt crypt,boolean isSignMustCompare);

	/**
	 * 调用金域接口获取数据
	 * 
	 * @param sjxxDto
	 * @return
	 */
    Map<String, Object> getGoldenData(SjxxDto sjxxDto);

	/**
	 * 接收送检结果信息生成报告
	 * 
	 * @return
	 */
    Map<String,Object> receiveInspectionGenerateReport(HttpServletRequest request, String str, String userid, JkdymxDto jkdymxDto) throws BusinessException;

	/**
	 * 文件上传
	 * 
	 * @param file
	 * @param dir
	 */
    boolean upload(MultipartFile file, String dir,String projectType);
	
	/**
	 * 重新生成word发送报告
	 * @param sjxxDto
	 * @param yhid
	 * @return
	 */
    Map<String,Object> resendNewReport(SjxxDto sjxxDto, String yhid, List<JcsjDto> jcsj_lxs)throws BusinessException;


	/**
	 * 获取文库信息
	 * @return
	 */
    List<Map<String,Object>> getLibraryInfo(Map<String, Object> map);

	/**
	 * 补充数据用需删除
	 * @param sjxxDto
	 * @return
	 */
    List<SjxxDto> getDtoByLrsj(SjxxDto sjxxDto);

	/**
	 * 补充数据用需删除
	 * @param sjxxDto
	 * @return
	 */
    List<FjsqDto> getFjDtoByLrsj(SjxxDto sjxxDto);

	List<Map<String,String>> getYbbhListByWbInfo(Map<String,Object> map);


	List<Map<String,String>> getAndCheckYbxxInfo(HttpServletRequest request, boolean isPlus) throws BusinessException;

	List<Map<String,String>> getAndCheckYbxxInfo(HttpServletRequest request, User user,boolean isPlus) throws BusinessException;
	List<Map<String,Object>> getJcxmFjInfoByYwids(List<String> ywids);
	List<Map<String,Object>> getJcxmFjInfoByYwidsPlus(List<Map<String, String>> ybInfoList,List<String> ywlxlikes,List<String> ywlxnotlikes);
	List<Map<String,String>> getSjxxInfo(List<Map<String, String>> list);

	/**
	 * 结核耐药文件接收处理
	 *
	 * @return
	 */
	Map<String, Object> disposeSaveFile(MultipartFile file);


	/**
	 * 结核耐药文件发送报告
	 *
	 * @return
	 */
	boolean sendReportFile(SjxxDto sjxxDto) throws BusinessException;

	/**
	 * 结核耐药文件发送报告
	 *
	 * @return
	 */
	boolean sendReportFile(SjxxDto sjxxDto,SjxxDto dto) throws BusinessException;
	
	/**
	 * 恢复tBtngs处理
	 * @param path
	 * @return
	 */
	public boolean restoreDeal(String path);
	
	/*
	 * 编辑保存物种信息
	 */
	void editSaveWzxx(SjxxDto sjxxDto, String sjwzxxJson);

	/*
	 * 编辑保存物种信息
	 */
	void editSaveNyx(SjxxDto sjxxDto, String sjnyxJson);
	/**
	 * 重新执行结核耐药结果的流程，重新生信报告
	 * @return
	 */
	public Map<String, Object> disposeSaveFileRetry();


	/**
	 * 检查IP是否在白名单
	 * @param request
	 * @param str
	 * @return
	 */
	public boolean checkIp(HttpServletRequest request,String str);

	public boolean mergeSendReportFile(SjxxDto sjxxDto,SjxxDto dto,User user) throws BusinessException;

	public boolean checkSend(SjxxDto sjxxDto,List<String> jcxmids);

	/**
	 * 先声保存结果文件
	 * @param request
	 * @param code_param
	 * @param organ_param
	 * @return
	 * @throws BusinessException
	 */
	public Map<String,Object>commonJsonResult(HttpServletRequest request,String code_param,String organ_param)throws BusinessException;
	/**
	 * @Description: 发送报告
	 * @param dtoList
	 * @param sjxxDto_t
	 * @param sjyzDto_t
	 * @param yhm
	 * @return boolean
	 * @Author: 郭祥杰
	 * @Date: 2025/6/18 10:59
	 */
	boolean sendVerificationReport(List<SjyzjgDto> dtoList, SjxxDto sjxxDto_t, SjyzDto sjyzDto_t, String yhm) throws BusinessException;

	public boolean mergeSendReportFile(SjxxDto sjxxDto,User user) throws BusinessException;

	/**
	 * @Description: 检查消息是否重复
	 * @param message
	 * @return boolean
	 * @Author: 郭祥杰
	 * @Date: 2025/7/2 15:52
	 */
	boolean checkMessage(String message);
}
