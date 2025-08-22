package com.matridx.igams.common.dao.post;

import com.matridx.igams.common.dao.BaseDao;
import com.matridx.igams.common.dao.entities.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ICommonRabbitDao extends BaseDao<SelectListDto>{

	/**
	 * 新增或修改用户信息
	 */
	int insertOrUpdateUser(User user);

	/**
	 * 新增或修改角色信息
	 */
	int insertOrUpdateRole(Role role);

	/**
	 * 删除用户信息
	 */
	int deleteUser(User user);

	/**
	 * 删除角色信息
	 */
	int deleteRole(Role role);

	/**
	 * 修改角色仓库分类
	 */
	int updateCkqxByjsid(Role role);

	/**
	 * 根据用户ID删除所属机构信息
	 */
	int deleteYhssjgByYhid(User ssjg_user);

	/**
	 * 根据用户ID新增所属机构信息
	 */
	int insertYhssjg(User ssjg_user);

	/**
	 *  用户机构权限删除
	 */
	int deleteJgqxxxByYhid(User user);

	/**
	 * 用户机构权限新增
	 */
	int insertYhjgqxList(List<User> users);

	/**
	 * 用户角色删除
	 */
	int deleteYhjs(User user);

	/**
	 * 用户角色新增
	 */
	int insertYhjs(User user);

	/**
	 * 机构信息新增或修改
	 */
	int insertOrUpdateJgxx(DepartmentDto departmentDto);

	/**
	 * 机构信息删除
	 */
	int deleteJgxx(DepartmentDto departmentDto);

	/**
	 * 系统审核新增或修改
	 */
	int insertOrUpdateXtsh(XtshDto xtshDto);
	
	/**
	 * 系统审核删除
	 */
	int deleteXtsh(XtshDto xtshDto);
	
		/**
	 * 角色单位权限新增
	 */
	int insertJsdwqx(Role role);

	/**
	 * 角色单位权限删除
	 */
	int deleteJsdwqx(Role role);
	
	/**
	 * 审批岗位新增或修改
	 */
	int insertOrUpdateSpgw(SpgwDto spgwDto);

	/**
	 * 审批岗位删除
	 */
	int deleteSpgw(SpgwDto spgwDto);
	
	/**
	 * 根据审核id修改停用时间
	 */
	int updateTysjByShid(SpgwDto spgwDto);

	/**
	 * 新增审核流程
	 */
	int insertBySpgwList(List<SpgwDto> spgwList);
	
	/**
	 * 新增审核类别
	 */
	int insertShlb(ShlbDto shlbDto);
	
	/**
	 * 更新审核类别
	 */
	int updateShlb(ShlbDto shlbDto);
	
	/**
	 * 删除审核类别
	 */
	int deleteShlb(ShlbDto shlbDto);
	
	/**
	 * 新增或更新基础数据
	 */
	int insertOrUpdateJcsj(JcsjDto jcsjDto);
	
	/**
	 * 删除基础数据
	 */
	int deleteJcsj(JcsjDto jcsjDto);
	/**
	 * 批量修改用户信息
	 */
	boolean updateUsers(List<User> users);
	/**
	 * 批量新增用户
	 */
	boolean insertUsers(List<User> users);
	/**
	 * 批量新增员工花名册
	 */
    boolean insertYghmcDtos(List<Map<String,String>> yghmcs);
	/**
	 * 批量修改员工花名册
	 */
	boolean updateYghmcDtos(List<Map<String,String>> yghmcs);
	/**
	 * 批量修改到期提醒
	 */
	void updateDqtxByIds(Map<String, Object> map);
	/**
	 * 批量新增员工合同信息
	 */
    boolean insertYghtxxDtos(List<Map<String,String>> list);
	/**
	 * 批量新增员工离职信息
	 */
	boolean insertYglzxxDtos(List<Map<String,String>> list);
	/**
	 * 删除员工合同信息
	 */
	boolean deleteYghtxx(Map<String, Object> map);
	/**
	 * 删除员工离职信息
	 */
	boolean deleteYglzxx(Map<String, Object> map);
	/**
	 * 修改机构信息
	 */
    void modJgxxList(List<DepartmentDto> departmentDtos);
	/**
	 * 修改用户所属机构信息
	 */
	void updateYhssjg(User user);
	/**
	 * 新增用户其他信息
	 */
	void insertYhqtxx(User user);
	/**
	 * 删除机构信息
	 */
    void deleteByWbcxid(DepartmentDto departmentDto);
}
