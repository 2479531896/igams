package com.matridx.igams.common.dao;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.matridx.igams.common.enums.PageListTypeEnum;
import com.matridx.springboot.util.base.StringUtil;

public class QueryModel implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int showCount = 10; //每页显示记录数
	private int totalPage;		//总页数
	private int totalResult;	//总记录数
	private int currentPage;	//当前页
	private int currentResult;	//当前记录起始索引
	private boolean entityOrField;	//true:需要分页的地方，传入的参数就是Page实体；false:需要分页的地方，传入的参数所代表的实体拥有Page属性
	//private String pageStr;		//最终页面显示的底部翻页导航，详细见：getPageStr();
	private String sortName;
	private String sortOrder;
	private String sortNamePro;
	private int startPage;
	
	private int showPageNum = 5;//需要显示的页码个数
	private String pageTime;
	//页面类型，用于客户端判断当前是否从查询按钮进去，还是从返回按钮进去，若返回按钮进去，这session查询条件不更新，否者更新session
	//1：代表 查看，修改页面返回  不更新session  ， 其他(0)：查询 ，更新session
	private String pageType;
	private PageListTypeEnum listType;//当前列表的音色
	
	
	public int getStartPage() {
		return startPage;
	}
	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}
	
	public int getCurrentPage() {
		if(currentPage<=0)
			currentPage = 1;
		if(currentPage>getTotalPage())
			currentPage = getTotalPage();
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	
	
	public int getTotalPage() {
		if(totalResult%showCount==0)
			totalPage = totalResult/showCount;
		else
			totalPage = totalResult/showCount+1;
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getTotalResult() {
		return totalResult;
	}
	public void setTotalResult(int totalResult) {
		this.totalResult = totalResult;
	}
	public int getShowCount() {
		return showCount;
	}
	public void setShowCount(int showCount) {
		this.showCount = showCount;
	}
	public int getCurrentResult() {
		currentResult = (getCurrentPage()-1)*getShowCount();
		if(currentResult<0)
			currentResult = 0;
		return currentResult;
	}
	public void setCurrentResult(int currentResult) {
		this.currentResult = currentResult;
	}
	public boolean isEntityOrField() {
		return entityOrField;
	}
	public void setEntityOrField(boolean entityOrField) {
		this.entityOrField = entityOrField;
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
	public String getSortNamePro() {
		return sortNamePro;
	}
	public String getPageType() {
		return pageType;
	}
	public void setPageType(String pageType) {
		this.pageType = pageType;
	}
	public void setSortNamePro(String sortNamePro) {
		this.sortNamePro = sortNamePro;
	}
	public String getPageTime() {
		if(pageTime == null){
			pageTime = "PAGE"+ System.currentTimeMillis();
		}
		return pageTime;
	}
	public void setPageTime(String pageTime) {
		this.pageTime = pageTime;
	}
	public int getShowPageNum() {
		if(showPageNum<=0) {
			showPageNum = 1;
		}
		return showPageNum;
	}
	public void setShowPageNum(int showPageNum) {
		this.showPageNum = showPageNum;
	}
	
	/**
	 * 获得排序字段属性（不带别名）
	 * @return
	 */
	public Set<String> getOrderByKeys(){
		Set<String> keys = new HashSet<>();
		StringBuilder sb = new StringBuilder();
		if(StringUtil.isNotBlank(this.sortName)){
			sb.append(this.sortName);
		}
		if(StringUtil.isNotBlank(this.sortOrder)){
			sb.append(" ").append(this.sortOrder);
		}
		if(StringUtil.isNotBlank(this.sortNamePro)){
			sb.append(sb.length()>0?",":this.sortNamePro);
		}
		if(sb.length()==0){
			return keys;
		}
		String[] keyArr = sb.toString().split(",");
		for (String keyStr : keyArr) {
			//替换别名.和desc或asc后缀
			String key = keyStr.replaceAll("(?i)^\\s*(\\w+\\.)?|(\\s+(desc|asc)\\s*$)?", "");
			if(StringUtil.isNotBlank(key)){
				keys.add(key);
			}
		}
		return keys;
	}
	
	public PageListTypeEnum getListType() {
		return listType;
	}
	public void setListType(PageListTypeEnum listType) {
		this.listType = listType;
	}
	
}