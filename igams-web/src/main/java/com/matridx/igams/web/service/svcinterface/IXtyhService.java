package com.matridx.igams.web.service.svcinterface;

import com.matridx.igams.common.dao.entities.InterfaceModel;
import com.matridx.igams.common.dao.entities.InterfaceReturnModel;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.web.dao.entities.XtyhDto;
import com.matridx.igams.web.dao.entities.XtyhModel;
import com.matridx.igams.web.dao.entities.YhjsDto;
import com.matridx.igams.wechat.dao.entities.WxyhDto;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.ClientDetailsService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface IXtyhService extends BaseBasicService<XtyhDto, XtyhModel>,UserDetailsService,ClientDetailsService{
	/**
	 * 根据用户名查找用户
	 * @param yhm
	 * @return
	 */
    XtyhDto getDtoByName(String yhm);
	/**
	 * 查询用户列表用于查询考勤
	 * @return
	 */
    List<XtyhDto> getDtoList();
	/**
	 * 根据ddid查找yhid
	 * @param xtyhDto
	 */
    XtyhDto getYhid(XtyhDto xtyhDto);
	
	/**
	 * 登录用户验证处理
	 * @return
	 */
    XtyhDto authUser(XtyhDto dto);
	
	/**
	 * 初始化登录页面
	 * @return
	 */
    Map<String, Object> initMenu(String tokenid);
	
	/**
	 * 根据页面填写信息更新用户信息，用户角色，还有审批权限信息
	 * @param xtyhDto
	 * @return
	 */
    boolean updateYhxx(XtyhDto xtyhDto);
	
	/**
	 * 切换用户角色
	 * @return
	 */
    Map<String, Object> switchMenu(XtyhDto xtyhDto);
	
	/**
	 * 修改密码
	 * @return
	 * @throws BusinessException 
	 */
    boolean updatePass(XtyhDto xtyhDto) throws BusinessException;

	/**
	 * 初始化密码
	 * @param xtyhDto
	 */
    boolean initPass(XtyhDto xtyhDto);

	/**
	 * 更新钉钉信息
	 * @param xtyhDto
	 * @return
	 */
    boolean initDingDing(XtyhDto xtyhDto);
	
	/**
	 * 获取微信用户列表
	 * @return
	 */
	List<WxyhDto> getPagedDtoListWxyh(WxyhDto wxyhdto);
	
	/**
	 * 将微信用户列表微信ID更新到用户列表
	 * @param wxyhdto
	 * @return
	 */
	boolean updatextyh(WxyhDto wxyhdto);
	
	/**
	 * 对用户进行认证
	 * @param inModel
	 * @return
	 */
    InterfaceReturnModel outsideAuth(InterfaceModel inModel);
	
	/**
	 * 对用户是否为已登录过的进行确认
	 * @param inModel
	 * @return
	 */
    String outsideAuthCheck(InterfaceModel inModel);
	
	/**
	 * 对token进行验证
	 * @param tokenid
	 * @return
	 */
    XtyhDto checkToken(String tokenid);
	
	/**
	 * 检测用户名和密码，如果用户已经登录，则直接返回相应的access_token,没有则重定向到登录接口
	 * @param grant_type
	 * @param username
	 * @param password
	 * @param url
	 * @return
	 */
    Map<String, String> outsideAuthLogin(String grant_type, String username, String password, String url);
	
	/**
	 * 根据用户名模糊查询
	 * @param xtyhDto
	 * @return
	 */
    List<XtyhDto> getListByYhm(XtyhDto xtyhDto);

	/**
	 * 根据用户Ids查询用户信息
	 * @param xtyhDto
	 * @return
	 */
    List<XtyhDto> getListByIds(XtyhDto xtyhDto);

	/**
	 * 查询用户列表
	 * @return
	 */
    List<XtyhDto> getUserList();
	
	/**
	 * 删除系统用户
	 * @param xtyhDto
	 * @return
	 * @throws BusinessException 
	 */
    boolean deleteXtyh(XtyhDto xtyhDto) throws BusinessException;
	
	/**
	 * 根据用户名删除oauth_client_details表中的数据 
	 * @param xtyhDto
	 * @return
	 */
	boolean deleteclientByYhm(XtyhDto xtyhDto);

	/**
	 * 根据token获取用户信息
	 * @param token
	 * @return
	 */
    User getUserByToken(String token);
	
	/**
	 * 小程序通过传递的userid获取系统用户信息
	 * @param xtyhDto
	 * @return
	 */
    XtyhDto getXtyhByUserid(XtyhDto xtyhDto) ;
	/**
	 * 根据钉钉id或微信id获取用户信息(先从redis中获取，如果redis中没有再从数据库中获取)
	 * @param xtyhDto
	 * @return
	 */
    XtyhDto getXtyhRedisAndDbByUserid(XtyhDto xtyhDto) ;

	/**
	 * 上传签名保存
	 * @param xtyhDto
	 * @return
	 */
    boolean uploadSaveSignature(XtyhDto xtyhDto);
	
	/**
	 * 找回密码更新密码
	 * @param xtyhDto
	 */
    boolean modPass(XtyhDto xtyhDto);
	
	/**
	 * 根据yhm去重
	 * @param xtyhDto
	 * @return
	 */
    XtyhDto getDtoByYhm(XtyhDto xtyhDto);
    
    /**
     * 更新用户错误次数
     * @param xtyhDto
     * @return
     */
    boolean updateLoginInfo(XtyhDto xtyhDto);

	/**
	 * 查询锁定时间
	 */
    String selectXtsz(String szlb);

	/**
	 * 根据token获取用户信息
	 * @param tokenid
	 * @return
	 */
    XtyhDto getDtoByToken(String tokenid);

	/**
	 * 獲取用戶信息
	 */
    User getYhInfo(String yhm);

	/**
	 * 更改新用户锁定标记
	 */
	boolean updateSfsd(XtyhDto xtyhDto);

	/**
	 * 个人信息包含部门信息
	 * @param xtyhDto
	 */
    XtyhDto getPersonalData(XtyhDto xtyhDto);

	/**
	 * 机构名称
	 * @return
	 */
    List<XtyhDto> getJgmc();

	/**
	 * 更新用户机构信息
	 * @param xtyhDto
	 */
    boolean updateJg(XtyhDto xtyhDto);

	/**
	 * 根据token_id查询钉钉用户信息
	 * @param token_id
	 * @return
	 */
    XtyhDto getDdByToken(String token_id);

	/**
	 * 用户组列表选择可选择用户
	 * @param xtyhDto
	 * @return
	 */
    List<XtyhDto> getPagedYhzOptionalList(XtyhDto xtyhDto);

	/**
	 * 用户组列表选择已选择用户
	 * @param xtyhDto
	 * @return
	 */
	List<XtyhDto> getPagedYhzSelectedList(XtyhDto xtyhDto);

	/**
     * 查询所有用户信息（yhm,sfsd,sdtime,jstime）
     * @return
     */
    List<User> getUsersList();
    /**
     * 查询oauth_client_details表里钉钉的用户信息
     * @return
     */
	List<User> getUsersByDdORWechatList();
	/**
     * 查询外部安全的用户信息，用于验证外部接口用户
     * @return
     */
	List<User> getWbaqUsersList();
	/**
	 * 根据用户ID删除完成的任务
	 * @param yhid
	 * @return
	 */
	boolean deleteInCompleteTaskById(String yhid);
	/**
	 * 根据多个用户ID删除完成的任务
	 * @param yhid
	 * @return
	 */
	boolean  deleteInCompleteTaskByIds(XtyhDto xtyhDto);
	/**
	 * 获取用户的角色信息（主要是针对钉钉或者微信)
	 * @param user
	 * @param isThrowFlg
	 */
    void getJsInfoByUser(com.matridx.igams.common.dao.entities.User user, boolean isThrowFlg);
	
	/**
	 * 获取用户的所有权限，插入redis（主要是针对钉钉或者微信)
	 * @param user
	 * 
	 */
    com.matridx.igams.common.dao.entities.User getAuthorByUser(com.matridx.igams.common.dao.entities.User user, boolean isThrowFlg, List<String> xtqxList, List<String> zyqxList);
	
	/**
	 * 获取用户的所有权限，插入redis（网页端)
	 * @param user
	 * 
	 */
    void getAuthor(com.matridx.igams.common.dao.entities.User user, List<String> zyqxList);
	
	/**
	 * 获取用户的所有权限，插入redis（网页端)
	 * @param user
	 * 
	 */
    void getWbzyqx(com.matridx.igams.common.dao.entities.User user, String cdcc);
	
	/**
	 * 根据用户信息获取角色信息（项目启动时）
	 * @param user
	 * @param yhqxDtos
	 */
    void getRunJsInfoByUser(com.matridx.igams.common.dao.entities.User user, List<YhjsDto> yhqxDtos);
	/**
	 * 根据用户ID,获取用户的权限信息。（项目启动时）
	 * @param user
	 * @return
	 */
    com.matridx.igams.common.dao.entities.User getRunAuthorByUser(com.matridx.igams.common.dao.entities.User user, List<String> xtqxList, List<String> zyqxList, List<YhjsDto> yhqxDtos);
	/**
	 * @Description 离职审核回调
	 * @Param [processInstanceId, processCode, wbcxid]
	 * @Return boolean
	 */
    boolean aduitDimissionCallback(String processInstanceId, String processCode, String wbcxid)throws BusinessException;
	/**
	 * @description 锁定用户
	 * @param xtyhs
	 * @return
	 */
    boolean lockUser(String xtyhs) throws BusinessException;
	/**
	 * @description 获取系统用户
	 * @param xtyhDto
	 * @return
	 */
    List<XtyhDto> getAllUserList(XtyhDto xtyhDto);
	/**
	 * 查询用户列表(不分页)
	 * @return
	 */
    List<XtyhDto> getNoPagedDtoList(XtyhDto xtyhDto);

	public Map<String,String> insertExternalUser(XtyhDto xtyhDto,HttpServletRequest request);

	public Map<String,String> postToSxSaveUser(XtyhDto xtyhDto, HttpServletRequest request);

	/**
	 * 通过yhm查找用户信息
	 * @param xtyhDto
	 * @return
	 */
	public List<XtyhDto> getListByYhms(XtyhDto xtyhDto);
}
