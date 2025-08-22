package com.matridx.web.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.web.dao.entities.XtyhDto;
import com.matridx.web.dao.entities.XtyhModel;

@Mapper
public interface IXtyhDao extends BaseBasicDao<XtyhDto, XtyhModel>{
	/**
	 * 根据用户名查找用户
	 * @param yhm
	 * @return
	 */
    XtyhDto getDtoByName(String yhm);
	
	/**
	 * 根据页面获取的token信息查找用户信息
	 * @param token
	 * @return
	 */
    XtyhDto getDtoByToken(String token);
	
	/**
	 * 根据用户名查找用户
	 * @param yhm
	 * @return
	 */
    User getUserByName(String yhm);
	/**
	 * 根据yhm去重
	 * @param xtyhDto
	 * @return
	 */
    XtyhDto getDtoByYhm(XtyhDto xtyhDto);
	/**
	 * 更新系统用户登录时间
	 * @param xtyhModel
	 * @return
	 */
    int updateLastLogin(XtyhModel xtyhModel);
	
	/**
	 * 修改密码
	 * @return
	 */
    int updatePass(XtyhDto xtyhDto);
	
	/**
	 * 更改认证里的密码
	 * @return
	 */
    int updateOauthPass(XtyhDto xtyhDto);
	
	/**
	 * 在新增用户的时候往 oauth_client_details 表里增加数据
	 * @param clientDetails
	 */
    void addClientDetails(XtyhDto xtyhDto);
	
	/**
	 * 在修改用户代码时同时修改 oauth_client_details 表里的 ClientID
	 * @param xtyhDto
	 */
    void updateOauthClinetId(XtyhDto xtyhDto);

	/**
	 * 在登录时删除 oauth_access_token 表里的数据
	 * @param username
	 */
    void deleteOauthAccessById(String yhm);

	/**
	 * 查询用户列表
	 * @return
	 */
    List<XtyhDto> getUserList();

	/**
	 * 更新钉钉信息
	 * @param xtyhDto
	 */
    void updateDingDing(XtyhDto xtyhDto);
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
	 * 更新登录信息
	 * @param xtyhDto
	 * @return
	 */
    int updateLoginInfo(XtyhDto xtyhDto);
    
    /**
     * 查询所有用户信息（yhm,sfsd,sdtime,jstime）
     * @return
     */
    List<User> getUsersList();

	/**
     * 查询外部安全的用户信息，用于验证外部接口用户
     * @return
     */
	List<User> getWbaqUsersList();
}
