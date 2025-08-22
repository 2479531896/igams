package com.matridx.igams.common.dao.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="ShxxDto")
public class ShxxDto extends ShxxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean isCommit;//是否是提交（用于过滤条件查询 提交的审核信息）
	
	private String shlb;//审核类别
	private List<String> shlbs;//审核类别列表
	private String gwmc; //岗位名称
	private String stshrymc; //受托审核人名称
	private String thlcxh;//退回步骤
	private String shrmc;//审核人名称
	private String sftgmc;	//是否通过名称
	private String sqrmc;//申请人姓名
	private List<String> ywids = new ArrayList<>();//业务ID列表
	private List<String> shxxids = new ArrayList<>();//用于批量取消审核
	private String qwwcsj;//期望完成时间
	//用于加载页面时确认唯一值
	private String loadYmCode;
	//是否是最后一步审核
	private boolean lastStep;
	//角色ID复数
	private List<String> jsids;
	//角色ID复数(查看)
	private List<String> t_jsids;
	//角色ID复数(下载)
	private List<String> d_jsids;
	//用户名
	private String yhm;
	//文件转换标记
	private String signflg;
	//文件替换标记
	private String replaceflg;
	private String wbgw;//外部岗位
	//天数
	private String ts;
	//文件审核流程(1代表更新审核时间)
	private String wjsh;
	//请购审核(1代表审核走到了调用填写支付信息的界面，即完善请购信息的最后一步，此时对接钉钉审批接口)
	private String qgsh;
	//抄送人(多)
	private String csrs;
	//rabbit同步消息队列标记
	private String prefixFlg;
	//钉钉审批ID
	private String ddspid;
	//录入人员钉钉ID
	private String lrryddid;
	//审核名称
	private String shmc;
	//业务关联名称
	private String ywglmc;
	//文件路径(签名)
	private String wjlj;
	//申请人用户名
	private String sqryhm;
	//现流程序号
	private String xlcxh;
	//审核用时
	private String shys;
	//审核人用户名
	private String shryhm;
	//审核开始时间
	private String postStart;
	//审核结束时间
	private String postEnd;
	//相差时间
	private String timeLag;

	public String getPostStart() {
		return postStart;
	}

	public void setPostStart(String postStart) {
		this.postStart = postStart;
	}

	public String getPostEnd() {
		return postEnd;
	}

	public void setPostEnd(String postEnd) {
		this.postEnd = postEnd;
	}

	public String getTimeLag() {
		return timeLag;
	}

	public void setTimeLag(String timeLag) {
		this.timeLag = timeLag;
	}

	public String getWbgw() {
		return wbgw;
	}

	public void setWbgw(String wbgw) {
		this.wbgw = wbgw;
	}

	public String getShryhm() {
		return shryhm;
	}

	public void setShryhm(String shryhm) {
		this.shryhm = shryhm;
	}

	public String getShys() {
		return shys;
	}

	public void setShys(String shys) {
		this.shys = shys;
	}
	public String getXlcxh() {
		return xlcxh;
	}

	public void setXlcxh(String xlcxh) {
		this.xlcxh = xlcxh;
	}

	public String getSqryhm() {
		return sqryhm;
	}

	public void setSqryhm(String sqryhm) {
		this.sqryhm = sqryhm;
	}

	public String getWjlj() {
		return wjlj;
	}

	public void setWjlj(String wjlj) {
		this.wjlj = wjlj;
	}

	public String getYwglmc() {
		return ywglmc;
	}

	public void setYwglmc(String ywglmc) {
		this.ywglmc = ywglmc;
	}

	public String getShmc() {
		return shmc;
	}

	public void setShmc(String shmc) {
		this.shmc = shmc;
	}

	public String getLrryddid() {
		return lrryddid;
	}

	public void setLrryddid(String lrryddid) {
		this.lrryddid = lrryddid;
	}

	public String getDdspid() {
		return ddspid;
	}

	public void setDdspid(String ddspid) {
		this.ddspid = ddspid;
	}

	public String getPrefixFlg() {
		return prefixFlg;
	}

	public void setPrefixFlg(String prefixFlg) {
		this.prefixFlg = prefixFlg;
	}

	public String getCsrs() {
		return csrs;
	}

	public void setCsrs(String csrs) {
		this.csrs = csrs;
	}

	public String getQgsh() {
		return qgsh;
	}

	public void setQgsh(String qgsh) {
		this.qgsh = qgsh;
	}

	public String getWjsh() {
		return wjsh;
	}

	public void setWjsh(String wjsh) {
		this.wjsh = wjsh;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public String getSignflg() {
		return signflg;
	}

	public void setSignflg(String signflg) {
		this.signflg = signflg;
	}

	public String getReplaceflg() {
		return replaceflg;
	}

	public void setReplaceflg(String replaceflg) {
		this.replaceflg = replaceflg;
	}

	public List<String> getD_jsids() {
		return d_jsids;
	}

	public void setD_jsids(List<String> d_jsids) {
		this.d_jsids = d_jsids;
	}

	public String getYhm() {
		return yhm;
	}

	public void setYhm(String yhm) {
		this.yhm = yhm;
	}

	public boolean isCommit() {
		return isCommit;
	}

	public void setCommit(boolean isCommit) {
		this.isCommit = isCommit;
	}

	public String getShlb() {
		return shlb;
	}

	public void setShlb(String shlb) {
		this.shlb = shlb;
	}

	public String getThlcxh() {
		return thlcxh;
	}

	public void setThlcxh(String thlcxh) {
		this.thlcxh = thlcxh;
	}

	public String getGwmc() {
		return gwmc;
	}

	public void setGwmc(String gwmc) {
		this.gwmc = gwmc;
	}

	public String getStshrymc() {
		return stshrymc;
	}

	public void setStshrymc(String stshrymc) {
		this.stshrymc = stshrymc;
	}

	public String getShrmc() {
		return shrmc;
	}

	public void setShrmc(String shrmc) {
		this.shrmc = shrmc;
	}

	public String getSftgmc() {
		return sftgmc;
	}

	public void setSftgmc(String sftgmc) {
		this.sftgmc = sftgmc;
	}

	public String getSqrmc() {
		return sqrmc;
	}

	public void setSqrmc(String sqrmc) {
		this.sqrmc = sqrmc;
	}

	public List<String> getYwids() {
		return ywids;
	}

	public void setYwids(List<String> ywids) {
		this.ywids = ywids;
	}
	
	public List<String> getShlbs() {
		return shlbs;
	}
	
	public void setShlbs(String shlbs) {
		List<String> list;
		String[] str = shlbs.split(",");
		list = Arrays.asList(str);
		this.shlbs = list;
	}
	
	public void setShlbs(List<String> shlbs) {
		this.shlbs = shlbs;
	}

	public List<String> getShxxids() {
		return shxxids;
	}

	public void setShxxids(List<String> shxxids) {
		this.shxxids = shxxids;
	}

	public String getQwwcsj() {
		return qwwcsj;
	}

	public void setQwwcsj(String qwwcsj) {
		this.qwwcsj = qwwcsj;
	}

	public String getLoadYmCode() {
		return loadYmCode;
	}

	public void setLoadYmCode(String loadYmCode) {
		this.loadYmCode = loadYmCode;
	}

	public List<String> getJsids() {
		return jsids;
	}

	public void setJsids(List<String> jsids) {
		this.jsids = jsids;
	}

	public boolean isLastStep() {
		return lastStep;
	}

	public void setLastStep(boolean lastStep) {
		this.lastStep = lastStep;
	}

	public List<String> getT_jsids() {
		return t_jsids;
	}

	public void setT_jsids(List<String> t_jsids) {
		this.t_jsids = t_jsids;
	}
	
}
