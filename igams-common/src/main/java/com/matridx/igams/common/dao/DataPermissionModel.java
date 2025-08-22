package com.matridx.igams.common.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.matridx.igams.common.dao.entities.User;

public class DataPermissionModel implements Serializable {

	private static final long serialVersionUID = 3715642641357933380L;
	
	private List<User> users = new ArrayList<>();//当前用户
	private boolean activate = false;//是否激活权限控制，true表示启用
	private String wtZyid;	//委托的资源id
	private String wtWtbh;	//委托的委托编号
	private String shxx_gwmc;
	private String sqrxm;		//申请人姓名
	private String sqsjstart;	//审核申请开始时间
	private String sqsjend;		//审核申请开始时间
	private String sftg;		//是否通过

	public String getSftg() {
		return sftg;
	}

	public void setSftg(String sftg) {
		this.sftg = sftg;
	}

	/** 数据权限定义集 */
	private List<DataPermissionSingleModel> permissions = new ArrayList<>();
	//private String funcodeKey;
	private boolean nullflg = false;//是否考虑null， true为null的数据也检索出来
	
	private User currentUser;//当前用户

	public String getShxx_gwmc() {
		return shxx_gwmc;
	}

	public void setShxx_gwmc(String shxx_gwmc) {
		this.shxx_gwmc = shxx_gwmc;
	}

	/*public String getFuncodeKey() {
		return funcodeKey;
	}
	public void setFuncodeKey(String funcodeKey) {
		this.funcodeKey = funcodeKey;
	}*/
	private Class<?> modelClass;
	
	public Class<?> getModelClass() {
		return modelClass;
	}
	public void setModelClass(Class<?> modelClass) {
		if(null == this.modelClass) this.modelClass = modelClass;
	}
	public List<DataPermissionSingleModel> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<DataPermissionSingleModel> permissions) {
		this.permissions = permissions;
	}
	public void addPermission(DataPermissionSingleModel permission) {
		this.activate = true;
		this.permissions.add(permission);
	}
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	public void addUser(User user){
		users.add(user);
	}
	public boolean isActivate() {
		return activate;
	}
	public void setActivate(boolean activate) {
		this.activate = activate;
	}
	/**
	 * 判断是否是委托操作
	 * @return
	 */
	public boolean isWtOpe() {
		return wtZyid!=null&&!wtZyid.trim().isEmpty()
				&&wtWtbh!=null&&!wtWtbh.trim().isEmpty();
	}
	public String getWtZyid() {
		return wtZyid;
	}
	public void setWtZyid(String wtZyid) {
		this.wtZyid = wtZyid;
	}
	public String getWtWtbh() {
		return wtWtbh;
	}
	public void setWtWtbh(String wtWtbh) {
		this.wtWtbh = wtWtbh;
	}

	public void setNullflg(boolean nullflg) {
		this.nullflg = nullflg;
	}

	public boolean getNullflg() {
		return nullflg;
	}

	public String getSqrxm() {
		return sqrxm;
	}

	public void setSqrxm(String sqrxm) {
		this.sqrxm = sqrxm;
	}

	public String getSqsjstart() {
		return sqsjstart;
	}

	public void setSqsjstart(String sqsjstart) {
		this.sqsjstart = sqsjstart;
	}

	public String getSqsjend() {
		return sqsjend;
	}

	public void setSqsjend(String sqsjend) {
		this.sqsjend = sqsjend;
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
	

}
