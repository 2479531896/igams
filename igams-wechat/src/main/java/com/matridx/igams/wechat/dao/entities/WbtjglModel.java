package com.matridx.igams.wechat.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias(value="WbtjglModel")
public class WbtjglModel extends BaseBasicModel {
	private String wbtjid;//外部统计id
	private String wbtjmc;//外部统计名称
	private String hbid;//伙伴id
	private String sjdw;//送检单位
	private String jcxm;//检测项目
	private String jcdw;//检测单位
	private String sjdwmc;//送检单位名称
	private String sfsf;//是否收费
	private String css;//测试数
	private String jsrq;//接收日期
	private String jczxm;//检测子项目
	private String sjqf;//送检区分

	public String getSjqf() {
		return sjqf;
	}

	public void setSjqf(String sjqf) {
		this.sjqf = sjqf;
	}

	public String getWbtjid() {
		return wbtjid;
	}

	public void setWbtjid(String wbtjid) {
		this.wbtjid = wbtjid;
	}

	public String getWbtjmc() {
		return wbtjmc;
	}

	public void setWbtjmc(String wbtjmc) {
		this.wbtjmc = wbtjmc;
	}

	public String getHbid() {
		return hbid;
	}

	public void setHbid(String hbid) {
		this.hbid = hbid;
	}

	public String getSjdw() {
		return sjdw;
	}

	public void setSjdw(String sjdw) {
		this.sjdw = sjdw;
	}

	public String getJcxm() {
		return jcxm;
	}

	public void setJcxm(String jcxm) {
		this.jcxm = jcxm;
	}

	public String getJcdw() {
		return jcdw;
	}

	public void setJcdw(String jcdw) {
		this.jcdw = jcdw;
	}

	public String getSjdwmc() {
		return sjdwmc;
	}

	public void setSjdwmc(String sjdwmc) {
		this.sjdwmc = sjdwmc;
	}

	public String getSfsf() {
		return sfsf;
	}

	public void setSfsf(String sfsf) {
		this.sfsf = sfsf;
	}

	public String getCss() {
		return css;
	}

	public void setCss(String css) {
		this.css = css;
	}

	public String getJsrq() {
		return jsrq;
	}

	public void setJsrq(String jsrq) {
		this.jsrq = jsrq;
	}

	public String getJczxm() {
		return jczxm;
	}

	public void setJczxm(String jczxm) {
		this.jczxm = jczxm;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
