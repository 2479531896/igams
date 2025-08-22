package com.matridx.igams.common.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="DcszDto")
public class DcszDto extends DcszModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//用户ID
	private String yhid;
	//角色ID
	private String jsid;
	//导出业务ID
	private String exportids;
	//是否保存更新个人配置  Y:是
	private String sfbc;
	//导出文件类型  0：xls  1：csv
	private String fileType;
	//导出文件ID
	private String wjid;
	//导出类型  search：搜索导出  select：选中导出
	private String expType;
	//回调JS方法名
	private String callbackJs;
	private String wkbh;
	private String hzxm;
	private String yblxmc;
	private String selectjson;

	public String getSelectjson() {
		return selectjson;
	}

	public void setSelectjson(String selectjson) {
		this.selectjson = selectjson;
	}

	public String getHzxm() {
		return hzxm;
	}

	public void setHzxm(String hzxm) {
		this.hzxm = hzxm;
	}

	public String getYblxmc() {
		return yblxmc;
	}

	public void setYblxmc(String yblxmc) {
		this.yblxmc = yblxmc;
	}

	public String getWkbh() {
		return wkbh;
	}

	public void setWkbh(String wkbh) {
		this.wkbh = wkbh;
	}

	private List<String> choseList;

	public String getYhid() {
		return yhid;
	}

	public void setYhid(String yhid) {
		this.yhid = yhid;
	}

	public String getExportids() {
		return exportids;
	}

	public void setExportids(String exportids) {
		this.exportids = exportids;
	}

	public String getSfbc() {
		return sfbc;
	}

	public void setSfbc(String sfbc) {
		this.sfbc = sfbc;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getWjid() {
		return wjid;
	}

	public void setWjid(String wjid) {
		this.wjid = wjid;
	}

	public String getExpType() {
		return expType;
	}

	public void setExpType(String expType) {
		this.expType = expType;
	}

	public String getCallbackJs() {
		return callbackJs;
	}

	public void setCallbackJs(String callbackJs) {
		this.callbackJs = callbackJs;
	}

	public List<String> getChoseList() {
		return choseList;
	}

	public void setChoseList(List<String> choseList) {
		this.choseList = choseList;
	}

	public String getJsid() {
		return jsid;
	}

	public void setJsid(String jsid) {
		this.jsid = jsid;
	}

}
