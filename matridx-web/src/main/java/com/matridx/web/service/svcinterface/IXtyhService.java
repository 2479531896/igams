package com.matridx.web.service.svcinterface;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.ClientDetailsService;

import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.web.dao.entities.XtyhDto;
import com.matridx.web.dao.entities.XtyhModel;

public interface IXtyhService extends BaseBasicService<XtyhDto, XtyhModel>,UserDetailsService,ClientDetailsService{
	/**
	 * 根据用户名查找用户
	 * @param yhm
	 * @return
	 */
    XtyhDto getDtoByName(String yhm);

	/**
	 * 根据yhm去重
	 * @param xtyhDto
	 * @return
	 */
    XtyhDto getDtoByYhm(XtyhDto xtyhDto);

	/**
	 * 个人信息包含部门信息
	 * @param xtyhDto
	 */
    XtyhDto getPersonalData(XtyhDto xtyhDto);
	
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
     * 更新用户错误次数
     * @param xtyhDto
     * @return
     */
    boolean updateLoginInfo(XtyhDto xtyhDto);
    
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
