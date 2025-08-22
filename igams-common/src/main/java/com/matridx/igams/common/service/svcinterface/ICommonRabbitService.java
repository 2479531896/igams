package com.matridx.igams.common.service.svcinterface;

import java.util.List;
import java.util.Map;

import com.matridx.igams.common.dao.entities.DepartmentDto;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.Role;
import com.matridx.igams.common.dao.entities.SelectListDto;
import com.matridx.igams.common.dao.entities.ShlbDto;
import com.matridx.igams.common.dao.entities.SpgwDto;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XtshDto;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseService;

public interface ICommonRabbitService  extends BaseService<SelectListDto>{

	/**
	 * 新增或修改用户信息
	 * user
	 *  
	 */
	 void insertOrUpdateUser(User user) throws BusinessException;

	/**
	 * 删除用户信息
	 * user
	 */
	 void deleteUser(User user);

	/**
	 * 新增或修改角色信息
	 * role
	 *  
	 */
	 void insertOrUpdateRole(Role role) throws BusinessException;

	/**
	 * 删除角色信息
	 * role
	 */
	 void deleteRole(Role role);

	/**
	 * 修改角色仓库分类
	 * 
	 * 
	 */
	 boolean updateCkqxByjsid(Role role);

	/**
	 * 用户机构权限新增
	 * users
	 */
	 void insertYhjgqxs(List<User> users);

	/**
	 * 用户机构权限删除
	 * user
	 */
	 void deleteYhjgqx(User user);

	/**
	 * 用户所属机构新增
	 * user
	 *  
	 */
	 void insertYhssjg(User user) throws BusinessException;

	/**
	 * 用户所属机构删除
	 * user
	 */
	 void deleteYhssjg(User user);

	/**
	 * 审批岗位成员删除
	 * User
	 */
	 void deleteSpgwcy(User User);

	/**
	 * 审批岗位成员新增
	 * spgwcyList
	 */
	 void insertSpgwcys(List<SpgwcyDto> spgwcyList);

	/**
	 * 用户角色新增
	 * user
	 */
	 void insertYhjs(User user);

	/**
	 * 用户角色删除
	 * user
	 */
	 void deleteYhjs(User user);

	/**
	 * 机构信息新增或修改
	 * departmentDto
	 */
	 void insertOrUpdateJgxx(DepartmentDto departmentDto);

	/**
	 * 机构信息删除
	 * departmentDto
	 */
	 void deleteJgxx(DepartmentDto departmentDto);

	/**
	 * 系统审核新增或修改
	 * xtshDto
	 *  
	 */
	 void insertOrUpdateXtsh(XtshDto xtshDto) throws BusinessException;
	
	/**
	 * 系统审核删除
	 * xtshDto
	 */
	 void deleteXtsh(XtshDto xtshDto);
	
	/**
	 * 审批岗位新增或修改
	 * spgwDto
	 *  
	 */
	 void insertOrUpdateSpgw(SpgwDto spgwDto) throws BusinessException;
	
	/**
	 * 审批岗位删除
	 * spgwDto
	 */
	 void deleteSpgw(SpgwDto spgwDto);
	
		/**
	 * 角色单位权限新增
	 * role
	 */
	 void insertJsdwqx(Role role);

	/**
	 * 角色单位权限删除
	 * role
	 */
	 void deleteJsdwqx(Role role);
	
	/**
	 * 根据审核id修改停用时间
	 * spgwDto
	 * 
	 */
	 void updateTysjByShid(SpgwDto spgwDto);

	/**
	 * 新增审核流程
	 * spgwList
	 * 
	 */
	 void insertBySpgwList(List<SpgwDto> spgwList);

	/**
	 * 新增审核类别
	 * shlbDto
	 * 
	 */
	 void insertShlb(ShlbDto shlbDto);
	
	/**
	 * 更新审核类别
	 * shlbDto
	 * 
	 */
	 void updateShlb(ShlbDto shlbDto);
	
	/**
	 * 删除审核类别
	 * shlbDto
	 * 
	 */
	 void deleteShlb(ShlbDto shlbDto);
	
	/**
	 * 新增或更新基础数据
	 * jcsjDto
	 * 
	 *  
	 */
	 void insertOrUpdateJcsj(JcsjDto jcsjDto) throws BusinessException;
	
	/**
	 * 删除基础数据
	 * jcsjDto
	 * 
	 */
	 void deleteJcsj(JcsjDto jcsjDto);

	/**
	 * 审批岗位批量删除
	 * spgwcyList
	 */
	 void batchDelSpgwcy(List<SpgwcyDto> spgwcyList);

	/**
	 * 系统用户签名更新同步
	 * fjcfbDto
	 *  
	 */
	 void userSign(FjcfbDto fjcfbDto) throws BusinessException;

	/**
	 * 用户所属机构删除
	 * user
	 */
	 void deleteYhssjgByYhid(User user);
	/**
	 * 批量修改用户信息
	 * [users]
	 *  void
	 */
	void updateUsers(List<User> users);
	/**
	 * 批量新增用户
	 * [userList]
	 *  void
	 */
	void insertUsers(List<User> userList) throws BusinessException;
	/**
	 * 批量新增员工花名册
	 * [maps]
	 *  void
	 */
	void insertYghmcDtos(List<Map<String,String>> maps);
	/**
	 * 批量修改员工花名册
	 * [maps]
	 *  void
	 */
	void updateYghmcDtos(List<Map<String,String>> maps);
	/**
	 * 批量修改到期提醒
	 * [map]
	 *  void
	 */
	void updateDqtxByIds(Map<String, Object> map);
	/**
	 * 批量新增员工合同信息
	 * [maps]
	 *  void
	 */
	void insertYghtxxDtos(List<Map<String,String>> maps);
	/**
	 * 批量新增员工离职信息
	 * [maps]
	 *  void
	 */
	void insertYglzxxDtos(List<Map<String,String>> maps);
	/**
	 * 删除员工合同信息
	 * [map]
	 *  void
	 */
	void deleteYghtxx(Map<String, Object> map);
	/**
	 * 删除员工离职信息
	 * [map]
	 *  void
	 */
	void deleteYglzxx(Map<String, Object> map);
	/**
	 * 修改机构信息
	 * departmentDtos
	 *  
	 */
	void modJgxxList(List<DepartmentDto> departmentDtos);
	/**
	 * 修改用户所属机构信息
	 * user
	 * 
	 */
	void updateYhssjg(User user);
	/**
	 * 新增用户其他信息
	 * user
	 * 
	 */
	void insertYhqtxx(User user);
	/**
	 * 删除机构信息
	 * departmentDto
	 * 
	 */
    void deleteByWbcxid(DepartmentDto departmentDto);
}
