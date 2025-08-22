package com.matridx.igams.common.dao;

import com.matridx.springboot.util.base.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BaseModel extends Model {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2348471938043883394L;
	
	//public QueryModel queryModel = new QueryModel();

	private String extend_1;//打开新页面时，页面间传递参数使用。
	private String extend_2;//打开新页面时，页面间传递参数使用。
	
	//token值
	private String access_token;
	//刷新用token值
	private String ref;
	//区分是新增还是修改
	private String formAction;
	//当前页
	private int pageNumber;
	//每页条数
	private int pageSize;
	//总页数
	private int totalPage;
	//总条数
	private int totalNumber;
	//排序字段
	private String sortName;
	//排序方式
	private String sortOrder;
	//最后排序字段 用于防止排序
	private String sortLastName;
	//最后排序方式
	private String sortLastOrder;
	//复数ID
	private List<String> ids;
	//操作类型
	private String doType;

	private int pageStart;
	//菜单跳转
	private String menu_jump;

	public String getMenu_jump() {
		return menu_jump;
	}

	public void setMenu_jump(String menu_jump) {
		this.menu_jump = menu_jump;
	}

	public String getExtend_1() {
		return extend_1;
	}
	public void setExtend_1(String extend_1) {
		this.extend_1 = extend_1;
	}
	public String getExtend_2() {
		return extend_2;
	}
	public void setExtend_2(String extend_2) {
		this.extend_2 = extend_2;
	}

	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getFormAction() {
		return formAction;
	}
	public void setFormAction(String formAction) {
		this.formAction = formAction;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getTotalNumber() {
		return totalNumber;
	}
	public void setTotalNumber(int totalNumber) {
		this.totalNumber = totalNumber;
	}
	public String getSortName() {
		return sortName;
	}
	public void setSortName(String sortName) {
		this.sortName = sortName;
	}
	public String getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
	public List<String> getIds() {
		return ids;
	}
	public void setIds(String ids) {
		List<String> list = new ArrayList<>();
		if(StringUtil.isNotBlank(ids)) {
			String[] str = ids.split(",");
			list = Arrays.asList(str);
		}
		this.ids = list;
	}
	public void setIds(List<String> ids) {
		if(ids!=null && !ids.isEmpty()){
            ids.replaceAll(s -> s.replace("[", "").replace("]", "").trim());
		}
		this.ids = ids;
	}
	/**
	 * 获取用户登录信息
	 * @return
	 */
	/*	protected User getLoginInfo(){
		//String name = request.getRemoteUser();
		// 获取安全上下文对象，就是那个保存在 ThreadLocal 里面的安全上下文对象
		// 总是不为null(如果不存在，则创建一个authentication属性为null的empty安全上下文对象)
		SecurityContext securityContext = SecurityContextHolder.getContext();

		// 获取当前认证了的 principal(当事人),或者 request token (令牌)
		// 如果没有认证，会是 null
		Authentication authentication = securityContext.getAuthentication();

		if(authentication == null)
			return null;

		// 获取当事人信息对象，返回结果是 Object 类型，但实际上可以是应用程序自定义的带有更多应用相关信息的某个类型。
		// 很多情况下，该对象是 Spring Security 核心接口 UserDetails 的一个实现类，你可以把 UserDetails 想像
		// 成我们数据库中保存的一个用户信息到 SecurityContextHolder 中 Spring Security 需要的用户信息格式的
		// 一个适配器。
		@SuppressWarnings("unchecked")
		List<IgamsGrantedAuthority> authorities = (List<IgamsGrantedAuthority>)authentication.getAuthorities();

		if(authorities.get(0) instanceof IgamsGrantedAuthority) {

			IgamsGrantedAuthority authority = authorities.get(0);

			return authority.getYhxx();
		}else {
			return null;
		}
	}*/
	public String getSortLastName() {
		return sortLastName;
	}
	public void setSortLastName(String sortLastName) {
		this.sortLastName = sortLastName;
	}
	public String getSortLastOrder() {
		return sortLastOrder;
	}
	public void setSortLastOrder(String sortLastOrder) {
		this.sortLastOrder = sortLastOrder;
	}
	public String getDoType() {
		return doType;
	}
	public void setDoType(String doType) {
		this.doType = doType;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}

	public int getPageStart() {
		return (this.pageNumber-1)*this.pageSize;
	}
	
	public int getPageStartOld() {
		return pageStart;
	}

	public void setPageStart(int pageStart) {
		this.pageStart = pageStart;
	}
}
