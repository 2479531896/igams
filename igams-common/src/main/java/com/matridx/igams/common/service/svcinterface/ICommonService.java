package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.dao.entities.DepartmentDto;
import com.matridx.igams.common.dao.entities.FjcfbModel;
import com.matridx.igams.common.dao.entities.Role;
import com.matridx.igams.common.dao.entities.SelectListDto;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.service.BaseService;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ICommonService extends BaseService<SelectListDto>{
	/**
	 * 根据给定用户信息查询其所有有权限的机构ID集
	 */
	 List<String> listOrgByYhid(Map<String,Object> param);

	/**
	 * 因用户信息保存在web 包里，无法获取，所以在共通里做一个获取委托用户信息的方法
	 */
	 List<Map<String, String>> getJsDtoList(Map<String, String> param);


	/**
	 * 根据用户信息获取用户的权限信息，主要用于各业务模块的委托权限处理，但跟XtyhService 有重复（因各业务无法访问web层），需注意要保持统一
	 */
	 com.matridx.igams.common.dao.entities.User getAuthorByUser(com.matridx.igams.common.dao.entities.User user);


	/**
	 * 根据ddid查找yhid
	 */

	 com.matridx.igams.common.dao.entities.User getYhid(String ddid);

	/**
	 * 设置申请人姓名
	 */
	 void setSqrxm(List<?> list);

	/**
	 * 设置Cloud下获取JSON从而得到申请人姓名
	 */
	 void setSqrxmByJson(List<?> list);

	/**
	 * 检查加密信息
	 */
	boolean checkSign(String sign, String data, HttpServletRequest req);

	/**
	 * 检查加密信息(不可逆)
	 */
	 Map<String, Object> checkSignUnBack(String organ, String sign, HttpServletRequest req);

	/**
	 * 传一个参数默认比较时间
	 */
	boolean checkSign(String sign, HttpServletRequest req);

	/**
	 * 获取加密信息
	 */
	String getSign(String str);

	/**
	 * 不传参默认调用时间加密
	 */
	String getSign();

	/**
	 * 跳转错误页面
	 */
	ModelAndView jumpError();

	/**
	 * 跳转审核错误页面
	 */
	 ModelAndView jumpAuditError();

	/**
	 * 跳转文件预览错误页面
	 */
	 ModelAndView jumpDocumentError();

	/**
	 * 获取用户列表
	 */
	 Map<String, Object> getUserList(String prefix, HttpServletRequest request);

	/**
	 * 获取用户列表
	 */
	 Map<String, Object> getUserList(String prefix, Map<String, String> params);

	/**
	 * 获取机构列表
	 */
	 Map<String, Object> getDepartmentList(String prefix, HttpServletRequest request);

	/**
	 * 获取机构列表
	 */
	 Map<String, Object> getDepartmentList(String prefix, Map<String, String> params);

	/**
	 * 根据用户名获取用户列表
	 */
	 Map<String, Object> getUserListByYhm(String prefix, String yhm, HttpServletRequest request);

	/**
	 * 根据用户名获取用户列表
	 */
	 Map<String, Object> getUserListByYhm(String prefix, String yhm, Map<String, String> params);

    List<User> getListByYhm(User user);

    /**
	 * 根据用户Ids获取用户列表
	 */
	 Map<String, Object> getUserListByIds(String prefix, List<String> ids, HttpServletRequest request);

	/**
	 * 根据用户Ids获取用户列表
	 */
	 Map<String, Object> getUserListByIds(String prefix, List<String> ids, Map<String, String> params);

	/**
	 * 根据用户Dtos获取用户列表
	 */
	 Map<String, Object> getUserListByDtos(String prefix, List<User> users, HttpServletRequest request);

	/**
	 * 根据用户Dtos获取用户列表
	 */
	 Map<String, Object> getUserListByDtos(String prefix, List<User> users, Map<String, String> params);

	/**
	 * 根据用户ID获取用户列表
	 */
	 Map<String, Object> getUserInfo(String prefix, String yhid, HttpServletRequest request);


	/**
	 * 根据用户ID获取用户列表
	 */
	 Map<String, Object> getUserInfo(String prefix, String yhid, Map<String, String> params);

	/**
	 * 根据用户ID获取机构信息
	 */
	 Map<String, Object> getDepartmentByUser(String prefix, String yhid, HttpServletRequest request);

	/**
	 * 根据用户ID获取机构信息
	 */
	 Map<String,Object> getDepartmentByUser(String prefix, String yhid, Map<String, String> params);

	/**
	 * 查询机构列表
	 */
	List<DepartmentDto> getPagedDepartmentList(String prefix, DepartmentDto departmentDto, HttpServletRequest request);

	/**
	 * 根据ID获取机构信息
	 */
	Map<String, Object> getDepartmentById(String prefix, String jgid, HttpServletRequest request);

	/**
	 * 根据ID获取机构信息
	 */
	Map<String, Object> getDepartmentById(String prefix, String jgid, Map<String, String> params);

	/**
	 * 根据机构Ids获取机构列表
	 */
	Map<String, Object> getDepartmentListByIds(String prefix, List<String> ids, HttpServletRequest request);

	/**
	 * 根据机构Ids获取机构列表
	 */
	 Map<String,Object> getDepartmentListByIds(String prefix, List<String> ids, Map<String, String> params);

	/**
	 * 根据departmentDto获取信息
	 */
	 Map<String, Object> getDepartmentListByDtos(String prefix, List<DepartmentDto> departmentDtos, HttpServletRequest request);

	/**
	 * 根据departmentDto获取信息
	 */
	 Map<String, Object> getDepartmentListByDtos(String prefix, List<DepartmentDto> departmentDtos, Map<String, String> params);

	/**
	 * 获取角色列表
	 */
	Map<String, Object> getRoleList(String prefix, HttpServletRequest request);

	/**
	 * 根据角色ID获取用户信息(文件)
	 */
	 Map<String, Object> selectAddXtyhByJsid(String prefix, Map<String, Object> map, HttpServletRequest request);


	/**
	 * 获取机构信息
	 */
	 List<DepartmentDto> getJgxxDto(DepartmentDto departmentDto);

	/**
	 * 筛选机构信息，审批岗位不限制单位的时候，所有审核人员直接加到发送消息的队列中。
	 * 如果审批岗位进行单位限制，则确认角色是否进行单位限制
	 *     如果角色不进行单位限制，则把人员加到发送消息的队列中
	 *     如果角色有单位限制的，则根据机构ID确认该角色有没有这个机构的权限，有则加入队列中
	 */
	 List<SpgwcyDto> siftJgList(List<SpgwcyDto> preList,String jgid);

	/**
	 * 筛选检测单位信息，审批岗位不限制单位的时候，所有审核人员直接加到发送消息的队列中。
	 * 如果审批岗位进行单位限制，则确认角色是否进行单位限制
	 *     如果角色不进行单位限制，则把人员加到发送消息的队列中
	 *     如果角色有单位限制的，则根据检测单位ID确认该角色有没有这个检测单位的权限，有则加入队列中
	 */
	 List<SpgwcyDto> siftJcdwList(List<SpgwcyDto> preList,String jcdwid);

	/**
	 * 根据用户token获取用户信息
	 */
	 Map<String, String> getUserInfoByToken(String tokenId);

	/**
	 * 根据用户ID查询审批成员列表
	 */
	 List<SpgwcyDto> selectSpgwcyByYhid(String yhid);

	/**
	 * 根据机构名称获取机构信息
	 */
	 List<DepartmentDto> getJgxxDtoByJgmc(String value);

	/**
	 * 获取录入人员列表
	 */
	 List<String> getLrryList(String wxid);

	/**
	 * 根据用户名获取用户信息
	 */
	 List<User> getUserListByYhms(List<String> ids);



	/**
	 * 根据钉钉ID或微信id获取录入人员列表
	 */
	 List<String> getLrryListByWxidOrDdid(String ddid,String wxid);

	/**
	 * 获取机构列表(小程序)
	 */
	 List<DepartmentDto> getMiniDepartmentList(DepartmentDto departmentDto);

	/**
	 * 文件下载
	 */
	 boolean downloadFile(FjcfbModel fjcfbModel);

	/**
	 * 根据用户名获取用户信息
	 */
	 User getDtoByYhm(String yhm);

	/**
	 * 根据用户ID获取钉钉ID
	 */
	 List<User> getDdidByYhids(List<String> yhids);

	/**
	 * 根据钉钉ID获取用户信息
	 */
	 List<User> getUserByDdid(User user);

	/**
	 * 根据用户ID查询用户信息
	 */
	 User getUserInfoById(User user);
	/**
	 * 根据微信ID获取外部程序信息
	 */
	 User getWbcxInfoByWxid(String wxid);

	/**
	 * 根据钉钉ID获取用户信息
	 */
	 User getUserId(String zsxm);

	/**
	 * 获取目录下所有的文件并返回所有文件列表，使用递归方式完成
	 */
	List<File> getAllFile(List<File> listFile, File paramFile);

	/**
	 * 根据ddids获取用户信息
	 */
	 List<User> getYhxxsByDdids(User user);

	/**
	 * 根据yhid获取系统用户姓名
	 */
	 List<User> getZsxmByYhid(List<String> yghs);
	/**
	 * 获取机构信息
	 */
	 DepartmentDto getJgxxInfo(String jgid);

	/**
	 * 发送微信消息
	 */
	 void sendWeChatMessage(String templateid, String wxid, String title, String keyword1, String keyword2, String keyword3, String keyword4, String remark, String reporturl);

	/**
	 * 发送微信消息
	 */
	 void sendWeChatMessageMap(String templatedm, String wxid, String wbcxdm, Map<String,String> messageMap);


	Map<String, String> turnToSpringSecurityLogin(String s, String s1);

	/**
	 * 角色资源权限限制
	 */
	 boolean checkCzqxExt(String url, User user, String urlPrefix);

	 boolean checkWbCzqxExt(HttpServletRequest request, User user, String urlPrefix);
	/**
	 * 检测是否为共同权限
	 */
	 boolean checkCommonPower(String url);

	/**
	 * 检查加密信息 未加密信息&时间戳 == 解密（加密信息）
	 */
	boolean checkSignSecretTime(String sign, String secret, String timeFormatString, HttpServletRequest req);
	/**
	 * 检查加密信息 未加密信息&时间戳 == 解密（加密信息）
	 */
	boolean checkSignSecretTime(String sign, String secret, HttpServletRequest req);

	/**
	 * 加密sign(sign&时间戳)
	 */
	String encrypSign(String sign, String timeFormatString);

	/**
	 * 加密sign(sign&时间戳)
	 * @return 加密信息(sign&yyyyMMddHHmm)
	 */
	String encrypSign(String sign);
	/**
	 * 根据Ids查询用户列表
	 */
	 List<User> getListByIds(User user);
	/**
	 * 根据zsxms获取用户信息
	 */
	 List<User> getUsersByYhms(User user);

	/**
	 * 根据ddid和wbcxid获取用户信息
	 */
	 User getByddwbcxid(User user);

	 void addNoClientDdid();
	 void dealClient();

	 void insertXtyh(String code,String word,String mc);

	 void updateXtyh(String code,String key,String befordWord,String mc);

	 void deleteXtyh(List<String> ids);
	

	List<User> getOaXtjsList(User user);


	/**
	 * 审批人员去重
	 */

	void sprDeduplication(List<SpgwcyDto> list);
	
	/**
	  * 根据角色信息查找该角色的相应的单位限制信息
	  * @return
	  */
	 List<Map<String, String>> getJsjcdwDtoList(Map<String, String> map);
	/**
	 * 获取角色列表
	 */
	List<Role> getPagedRoleList(Role role);
	/**
	 * 处理下载文件名
	 */
	void dealDonwloadFileName(HttpServletResponse response, HttpServletRequest request, String fileName);

	/**生信根据测序仪代码获取上机信息
	 *
	 * @param map
	 * @return
	 */

	List<Map<String, Object>> getWksjmxList(Map<String, String> map);

	/**
	 * 获取审核人和检验人List
	 * @param yh
	 * @return
	 */
	List<User> getShryAndJyryList(User yh);
	/**
	 * @Description: 共通方法，查询是否订阅消息
	 * @param ywid 用户id或伙伴id
	 * @param xxid 消息id
	 * @return boolean
	 * @Author: 郭祥杰
	 * @Date: 2024/11/27 14:51
	 */
	boolean queryAuthMessage(String ywid,String xxid);

	/**
	 * @Description: 获取审核时间共用
	 * @param ywid
	 * @param gwid
	 * @param szlb
	 * @return java.lang.String
	 * @Author: 郭祥杰
	 * @Date: 2025/2/14 16:02
	 */
	String getCommonAuditTime(String ywid,String gwid, String szlb);
	/**
	 * @Description: 计算去除节假日时间
	 * @param postStart 开始时间
	 * @param postEnd 结束时间
	 * @param timeLag 时间差，小时
	 * @param officeHours 上班时间
	 * @param closingTime 下班时间
	 * @param beginTime 午休开始时间
	 * @param endTime 午休结束时间
	 * @return java.lang.String
	 * @Author: 郭祥杰
	 * @Date: 2025/1/23 15:36
	 */
	String getAuditTime(String postStart,String postEnd,String timeLag,String officeHours,String closingTime,String beginTime,String endTime);

	BigDecimal getHours(String startString, String endString);
	BigDecimal getHoursByD(String startString,String endString);
	BigDecimal calculateTime(Timestamp timestamp);

	List<LocalDate> getAllDates(String startString, String endString);

	BigDecimal endDayGetHours(String timeString,String officeHours,String closingTime,String beginTime,String endTime);
	BigDecimal beginDayGetHours(String timeString,String officeHours,String closingTime,String beginTime,String endTime);
	BigDecimal oneDayGetHours(String timeString,String endTimeString,String officeHours,String closingTime,String beginTime,String endTime);
	/**
	 * @Description: 日志记录错误数量
	 * @param str
	 * @param errorKey
	 * @param logString
	 * @param state
	 * @return java.lang.String
	 * @Author: 郭祥杰
	 * @Date: 2025/4/28 15:26
	 */
	String logErrorNumMap(String str,String errorKey,String logString,boolean state);
}
