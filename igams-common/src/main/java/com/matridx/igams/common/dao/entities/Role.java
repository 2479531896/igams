package com.matridx.igams.common.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Alias(value="Role")
public class Role extends BaseModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 角色id
	private String jsid;
	// 角色代码
	private String jsdm;
	// 角色名称
	private String jsmc;
	// 角色描述
	private String jsms;
	// 父角色id
	private String fjsid;
	// 首页类型
	private String sylx;
	//单位限定标记
	private String dwxdbj;
	//分布式标记
	private String prefix;
	//录入人员
	private String lrry;
	//录入时间
	private String lrsj;
	//修改人员
	private String xgry;
	//仓库分类
	private String ckqx;
	//修改时间
	private String xgsj;
	//删除人员
	private String scry;
	//删除时间
	private String scsj;
	//删除标记
	private String scbj;
	// 机构id
	private String jgid;
	//ID复数
	private List<String> ids;
	// 用户ID
	private String yhid;
	// 区分
	private String qf;

	private String csid;

	private String csmc;

	public String getYhid() {
		return yhid;
	}

	public void setYhid(String yhid) {
		this.yhid = yhid;
	}

	public String getQf() {
		return qf;
	}

	public void setQf(String qf) {
		this.qf = qf;
	}

	public String getCkqx() {
		return ckqx;
	}

	public void setCkqx(String ckqx) {
		this.ckqx = ckqx;
	}

	public String getJgid() {
		return jgid;
	}
	public void setJgid(String jgid) {
		this.jgid = jgid;
	}
	public List<String> getIds() {
		return ids;
	}
	public void setIds(String ids) {
		List<String> list;
		String[] str = ids.split(",");
		list = Arrays.asList(str);
		this.ids = list;
	}
	public void setIds(List<String> ids) {
		if(ids!=null && !ids.isEmpty()){
            ids.replaceAll(s -> s.replace("[", "").replace("]", "").trim());
		}
		this.ids = ids;
	}
	public String getLrry() {
		return lrry;
	}
	public void setLrry(String lrry) {
		this.lrry = lrry;
	}
	public String getLrsj() {
		return lrsj;
	}
	public void setLrsj(String lrsj) {
		this.lrsj = lrsj;
	}
	public String getXgry() {
		return xgry;
	}
	public void setXgry(String xgry) {
		this.xgry = xgry;
	}
	public String getXgsj() {
		return xgsj;
	}
	public void setXgsj(String xgsj) {
		this.xgsj = xgsj;
	}
	public String getScry() {
		return scry;
	}
	public void setScry(String scry) {
		this.scry = scry;
	}
	public String getScsj() {
		return scsj;
	}
	public void setScsj(String scsj) {
		this.scsj = scsj;
	}
	public String getScbj() {
		return scbj;
	}
	public void setScbj(String scbj) {
		this.scbj = scbj;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getDwxdbj() {
		return dwxdbj;
	}
	public void setDwxdbj(String dwxdbj) {
		this.dwxdbj = dwxdbj;
	}
	public String getJsid() {
		return jsid;
	}
	public void setJsid(String jsid) {
		this.jsid = jsid;
	}
	public String getJsdm() {
		return jsdm;
	}
	public void setJsdm(String jsdm) {
		this.jsdm = jsdm;
	}
	public String getJsmc() {
		return jsmc;
	}
	public void setJsmc(String jsmc) {
		this.jsmc = jsmc;
	}
	public String getJsms() {
		return jsms;
	}
	public void setJsms(String jsms) {
		this.jsms = jsms;
	}
	public String getFjsid() {
		return fjsid;
	}
	public void setFjsid(String fjsid) {
		this.fjsid = fjsid;
	}
	public String getSylx() {
		return sylx;
	}
	public void setSylx(String sylx) {
		this.sylx = sylx;
	}


	public String getCsid() {
		return csid;
	}

	public void setCsid(String csid) {
		this.csid = csid;
	}

	public String getCsmc() {
		return csmc;
	}

	public void setCsmc(String csmc) {
		this.csmc = csmc;
	}
}
