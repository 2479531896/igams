package com.matridx.igams.web.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.web.dao.entities.XtyhDto;
import com.matridx.igams.web.dao.entities.XtyhModel;
import com.matridx.igams.wechat.dao.entities.WxyhDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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
	 * 根据用户名查找用户
	 * @param yhm
	 * @return
	 */
    List<User> getUserByDdORWechat(String yhm);
	
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
	 * @param xtyhDto
	 */
    void addClientDetails(XtyhDto xtyhDto);
	
	/**
	 * 在修改用户代码时同时修改 oauth_client_details 表里的 ClientID
	 * @param xtyhDto
	 */
    void updateOauthClinetId(XtyhDto xtyhDto);

	/**
	 * 在登录时删除 oauth_access_token 表里的数据
	 * @param yhm
	 */
    void deleteOauthAccessById(String yhm);
	
	/**
	 * 通过用户名获取token信息
	 * @param yhm
	 */
    XtyhDto selectOauthAccessByName(String yhm);

	/**
	 * 根据ddid查找yhid
	 * @param xtyhDto
	 */
    XtyhDto getYhid(XtyhDto xtyhDto);

	/**
	 * 查询用户列表
	 * @return
	 */
    List<XtyhDto> getUserList();
	/**
	 * 查询用户列表用于查询考勤
	 * @return
	 */
    List<XtyhDto> getDtoList();

	/**
	 * 更新钉钉信息
	 * @param xtyhDto
	 */
    void updateDingDing(XtyhDto xtyhDto);
	
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
	 * 根据用户名删除oauth_client_details表中的数据 
	 * @param xtyhDto
	 * @return
	 */
    int deleteclientByYhm(XtyhDto xtyhDto);
	
	/**
	 * 根据用户名查询oauth_client_details表中的数据 
	 * @param xtyhDto
	 * @return
	 */
    List<XtyhDto> getclientByYhm(XtyhDto xtyhDto);

	/**
	 * 根据页面获取的token信息查找用户信息(主要用于)
	 * @param token_id
	 * @return
	 */
    User getUserByToken(String token_id);
	
	/**
	 * 根据微信id获取用户信息
	 * @param xtyhDto
	 * @return
	 */
    XtyhDto getYhxxByWxid(XtyhDto xtyhDto);
	
	/**
	 * 根据ddid获取用户信息
	 * @param xtyhDto
	 * @return
	 */
    XtyhDto getYhxxByDdid(XtyhDto xtyhDto);
	
	/**
	 * 根据yhm去重
	 * @param xtyhDto
	 * @return
	 */
    XtyhDto getDtoByYhm(XtyhDto xtyhDto);

	/**
	 * 拿到数据库中的所有系统用户（包括删除标记为1的）
	 * @return
	 */
    List<XtyhDto> getDBAllUserList();

	/**
	 * 更新用户角色Id
	 * @param js_xtyh
	 */
    void updateJsidByGwmc(XtyhDto js_xtyh);

	/**
	 * 更新登录信息
	 * @param xtyhDto
	 * @return
	 */
    int updateLoginInfo(XtyhDto xtyhDto);

	int updateSfsd(XtyhDto xtyhDto);

	XtszDto selectXtsz(String szlb);

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
	 * @Description 批量更改系统用户信息
	 * @Param [updateYhList]
	 * @Return boolean
	 */
    boolean updateXtyhDtos(List<XtyhDto> updateYhList);
	/**
	 * @Description 获取系统用户信息
	 * @Param []
	 * @Return java.util.List<XtyhDto>
	 */
	List<XtyhDto> getXtyhDtos();
	/**
	 * @Description TODO
	 * @Param [list]
	 * @Return boolean
	 */
	boolean insertXtyhDtos(List<XtyhDto> list);
	boolean deleteInCompleteTaskById(String yhid);
	void deleteInCompleteTaskList(List<XtyhDto> list);
	boolean  deleteInCompleteTaskByIds(XtyhDto xtyhDto);
	/**
	 * @Description 获取删除标记不为1的所有用户
	 * @Param []
	 * @Return java.util.List<XtyhDto>
	 */
	List<XtyhDto> getAllUserList(XtyhDto xtyhDto);

	/**
	 * oauth_client_details表中根据client_id查找数据
	 * @param dd_xtyhDto
	 * @return
	 */
	List<XtyhDto> getByClientId(XtyhDto dd_xtyhDto);

	/**
	 * 根据搜索条件获取导出条数
	 * @param xtyhDto
	 * @return
	 */
    int getCountForSearchExp(XtyhDto xtyhDto);

	/**
	 * 从数据库分页获取导出数据
	 * @param xtyhDto
	 * @return
	 */
    List<XtyhDto> getListForSearchExp(XtyhDto xtyhDto);

	/**
	 * 从数据库分页获取导出数据
	 * @param xtyhDto
	 * @return
	 */
    List<XtyhDto> getListForSelectExp(XtyhDto xtyhDto);
	/**
	 * 查询用户列表(不分页)
	 * @return
	 */
    List<XtyhDto> getNoPagedDtoList(XtyhDto xtyhDto);
	/*
		通过系统用户外部程序和ddid获取工号信息
	 */
	List<String> getYhmByUsers(List<XtyhDto> xtyhDtos);
	/*
        通过用户名锁定用户
     */
	boolean lockUsersByYhms(List<String> yhms);

	/**
	 * 通过yhm查找用户信息
	 * @param xtyhDto
	 * @return
	 */
	List<XtyhDto> getListByYhms(XtyhDto xtyhDto);
}
