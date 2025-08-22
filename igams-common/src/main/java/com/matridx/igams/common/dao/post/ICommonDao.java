package com.matridx.igams.common.dao.post;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import com.matridx.igams.common.dao.BaseDao;
import com.matridx.igams.common.dao.entities.DepartmentDto;
import com.matridx.igams.common.dao.entities.QxModel;
import com.matridx.igams.common.dao.entities.Role;
import com.matridx.igams.common.dao.entities.SelectListDto;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.dao.entities.User;

@Mapper
public interface ICommonDao extends BaseDao<SelectListDto>{
	/**
	 * 根据给定用户信息查询其所有机构ID集
	 */
	 List<String> listOrgByYhid(Map<String,Object> param);

	/**
	 * 根据ddid查找yhid
	 */

	com.matridx.igams.common.dao.entities.User getYhid(String ddid);

	/**
	 * 因用户信息保存在web 包里，无法获取，所以在共通里做一个获取委托用户信息的方法
	 */
	 List<Map<String, String>> getJsDtoList(Map<String, String> param);
	
	/**
	 * 根据角色ID获取该角色的各菜单权限
	 */
	 List<QxModel> getQxModelList(QxModel qxModel);
	
	/**
	 * 根据yhid获取系统用户姓名
	 */
	 List<User> getZsxmByYhid(List<String> yhids);

	/**
	 * 获取机构列表
	 */
	 List<DepartmentDto> getJgxxList();

	/**
	 * 获取用户列表
	 */
	 List<User> getUserList();

	/**
	 * 通过用户名模糊查询
	 */
	 List<User> getListByYhm(User user);

	/**
	 * 根据Ids查询用户列表
	 */
	 List<User> getListByIds(User user);

	/**
	 * 根据用户ID查询用户信息
	 */
	 User getUserInfoById(User user);

	/**
	 * 根据用户ID获取机构信息
	 */
	 List<DepartmentDto> getJgxxByYhid(DepartmentDto departmentDto);

	/**
	 * 查询机构列表
	 */
	 List<DepartmentDto> getPagedDepartmentList(DepartmentDto departmentDto);

	/**
	 * 根据ID获取机构信息
	 */
	 List<DepartmentDto> getJgxxById(DepartmentDto departmentDto);

	/**
	 * 根据机构Ids获取机构列表
	 */
	 List<DepartmentDto> getJgListByIds(DepartmentDto departmentDto);

	/**
	 * 根据departmentDtos获取信息
	 */
	 List<Map<String, String>> getDepartmentListByDtos(List<DepartmentDto> departmentDtos);

	/**
	 * 根据用户Dtos获取用户列表
	 */
	 List<Map<String, String>> getUserListByDtos(List<User> users);

	/**
	 * 获取机构信息
	 */
	 List<DepartmentDto> getJgxxDto(DepartmentDto departmentDto);
	
	/**
	 * 根据限制的审批用户信息，查找用户的限制单位信息。最后筛选出允许查看指定单位的审批用户信息
	 * list 审批用户信息  jgid 指定单位
	 */
	 List<SpgwcyDto> getJgidsByLimitJs(Map<String, Object> param);
	
	/**
	 * 根据限制的审批用户信息，查找用户的检测单位限制信息。最后筛选出允许查看指定检测单位的审批用户信息
	 * list 审批用户信息  jgid 指定单位
	 */
	 List<SpgwcyDto> getJcdwsByLimitJs(Map<String, Object> param);
	
	/**
	 * 根据用户token获取用户信息
	 */
	 Map<String, String> getUserInfoByToken(String tokenId);
	
	/**
	 * 获取角色列表
	 */
	 List<Role> getRoleList();

	/**
	 * 根据角色ID获取新增用户信息(文件)
	 */
	 List<User> selectAddXtyhByJsid(Map<String, Object> map);
	
	/**
	 * 根据角色ID查询删除用户信息（文件）
	 */
	 List<User> selectDelXtyhByJsid(Map<String, Object> map);

	/**
	 * 根据用户ID查询审批成员列表
	 */
	 List<SpgwcyDto> selectSpgwcyByYhid(String yhid);
	
	/**
	 * 根据微信ID获取系统用户和钉钉ID
	 */
	 List<User> getListBySameId(User user_wx);

	/**
	 * 根据用户名获取用户信息
	 */
	 List<User> getUserListByYhms(List<String> ids);
	
	/**
	 * 根据钉钉ID获取用户信息
	 */
	 List<User> getUserByDdid(User user);
	/**
	 * 根据微信ID获取外部程序信息
	 */
	 User getWbcxInfoByWxid(String wxid);
	
	/**
	 * 获取机构列表(小程序)
	 */
	 List<DepartmentDto> getMiniDepartmentList(DepartmentDto departmentDto);
	
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
	 User getUserId(String zsxm);
	
	/**
	 * 根据ddids获取用户信息
	 */
	 List<User> getYhxxsByDdids(User user);
	/**
	 * 获取机构信息
	 */
	 DepartmentDto getJgxxInfo(String jgid);
	/**
	 * 获取用户角色列表
	 */
	 List<User> getYhjsDtos(User user);
	/**
	 * 获取系统角色列表
	 */
	 List<User> getXtjsDtos(User user);

	 List<User> getOaXtjsList(User user);
	/**
	 * 获取没有按钮的列表清单权限
	 */
	 List<QxModel> getNoButtonMenu(QxModel qxModel);
	/**
	 * {@code @Description} 获取所有用户
	 * {@code @Param} []
	  java.util.List<com.matridx.igams.common.dao.entities.User>
	 */
    List<User> getAllUserList();
	/**
	 * 根据zsxms获取用户信息
	 */
	 List<User> getUsersByYhms(User user);
	
	/**
	 * 根据ddid和wbcxid获取用户信息
	 */
	 User getByddwbcxid(User user);

	 /**
	  * 根据DDid 获取Client里没有的用户信息
	  * @return
	  */
	 List<User> getNoClientDdid();
	 List<User> getClientinfo(User user);
	 List<User> getWrongClientinfo();
	 
	 /**
	  * 新增用户信息
	  * @param user
	  */
	 void addClientDetails(User user);
	 
	 /**
	  * 获取外部权限信息
	  * @param qxModel
	  * @return
	  */
	 List<QxModel> getWbQxModelList(QxModel qxModel);

	 List<QxModel>getWbQxModelListByCommon();

	List<QxModel>getWbQxModelListByJsid();
	 
	 /**
	  * 更新用户信息
	  * @param user
	  */
	 void updateClientDetails(User user);

	 /**
	  * 删除用户
	  * @param user
	  */
	 void deleteXtyh(User user);
	 
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
	 * 生信根据芯片代码获取上机信息
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getWksjmxList(Map<String, String> map);
}
