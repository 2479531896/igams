package com.matridx.igams.common.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.ShlcKzcsEnum;

@Alias(value="ShgcDto")
public class ShgcDto extends ShgcModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//审核类别
	private String shlb;
	//审核类别-复数
	private List<String> shlbs;
	//业务清单
	private List<String> ywids;
	//分区名称
	private String fqmc;
	//排序，用于列表页面查询审核状态的
	private int ordernum;
	//审核状态(用于回调业务接口传递的状态)
	private AuditStateEnum auditState; 
	//业务页面地址
	private String business_url;
	//业务页面地址
	private String business_url2;
	//回调类
	private String hdl;

	//当前审核人
	private String dqshr;

	public String getDqshr() {
		return dqshr;
	}

	public void setDqshr(String dqshr) {
		this.dqshr = dqshr;
	}

	public String getHdl() {
		return hdl;
	}

	public void setHdl(String hdl) {
		this.hdl = hdl;
	}

	public String getBusiness_url2() {
		return business_url2;
	}

	public void setBusiness_url2(String business_url2) {
		this.business_url2 = business_url2;
	}

	//用于加载页面时确认唯一值
	private String loadYmCode;
	//业务字段
	private String ywzd;
	//最大流程序号
	private int maxlcxh;
	//审核流程扩展参数（用于回调业务接口时传递当前流程的扩展参数）
	private ShlcKzcsEnum shlcKzcs; 
	//审核信息
	private ShxxDto shxx;
	//审批岗位成员
	private List<SpgwcyDto> spgwcyDtos;
	//取消的审批岗位成员
	private List<SpgwcyDto> no_spgwcyDtos;
	//审核类别名称
	private String shlbmc;
	//申请人的钉钉ID
	private String sqrddid;
	//期望完成时间
	private String qwwcsj;
	//是否是最后一步审核
	private boolean lastStep;
	//是否进入最后一步审核
	private boolean entryLastStep;
	//是否审核退回的任务
	private boolean isBack;
	//角色ID复数
	private List<String> jsids;
	//角色ID复数（查看）
	private List<String> t_jsids;
	//角色ID复数（下载）
	private List<String> d_jsids;
	//文件转换标记
	private String signflg;
	//文件替换标记
	private String replaceflg;
	//文件审核流程(1代表更新审核时间)
	private String wjsh;
	//请购审核(1代表审核走到了调用填写支付信息的界面，即完善请购信息的最后一步，此时对接钉钉审批接口)
	private String qgsh;
	//抄送人(多)
	private String csrs;
	//钉钉审批ID
	private String ddspid;
	//用户id
	private String yhid;
	//角色id
	private String jsid;
//外部岗位
	private String wbgw;
	//用户名
	private String yhm;
	private String lclbcskz2;//流程类别参数扩展2 是否特殊页面
	private String lclbcskz3;//流程类别参数扩展3 钉钉回调类型参数代码
	private String ddhdlxcskz1;//钉钉审批processCode
	private List<DbszDto> dbszDtos;//代办设置
	private String mrsz;//审核默认设置
	private String zylj;//资源路径
	private String shlbbm;//审核类别别名
	//钉钉ID
	private String ddid;

	public String getDdid() {
		return ddid;
	}

	public void setDdid(String ddid) {
		this.ddid = ddid;
	}

	public String getZylj() {
		return zylj;
	}

	public void setZylj(String zylj) {
		this.zylj = zylj;
	}

	public String getShlbbm() {
 		return shlbbm;
	}

	public void setShlbbm(String shlbbm) {
		this.shlbbm = shlbbm;
	}

	public List<DbszDto> getDbszDtos() {
		return dbszDtos;
	}

	public void setDbszDtos(List<DbszDto> dbszDtos) {
		this.dbszDtos = dbszDtos;
	}
	public String getMrsz() {
		return mrsz;
	}

	public void setMrsz(String mrsz) {
		this.mrsz = mrsz;
	}
	public String getDdhdlxcskz1() {
		return ddhdlxcskz1;
	}

	public void setDdhdlxcskz1(String ddhdlxcskz1) {
		this.ddhdlxcskz1 = ddhdlxcskz1;
	}
	public String getLclbcskz2() {
		return lclbcskz2;
	}

	public void setLclbcskz2(String lclbcskz2) {
		this.lclbcskz2 = lclbcskz2;
	}

	public String getLclbcskz3() {
		return lclbcskz3;
	}

	public void setLclbcskz3(String lclbcskz3) {
		this.lclbcskz3 = lclbcskz3;
	}

	public String getYhm() {
		return yhm;
	}

	public void setYhm(String yhm) {
		this.yhm = yhm;
	}

	public String getWbgw() {
		return wbgw;
	}

	public void setWbgw(String wbgw) {
		this.wbgw = wbgw;
	}


	public String getYhid() {
		return yhid;
	}

	public void setYhid(String yhid) {
		this.yhid = yhid;
	}

	public String getJsid() {
		return jsid;
	}

	public void setJsid(String jsid) {
		this.jsid = jsid;
	}

	public String getDdspid() {
		return ddspid;
	}

	public void setDdspid(String ddspid) {
		this.ddspid = ddspid;
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
	public String getShlb() {
		return shlb;
	}
	public void setShlb(String shlb) {
		this.shlb = shlb;
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
	public void setShlbs(List<String> shlbs) {
		this.shlbs = shlbs;
	}
	public String getFqmc() {
		return fqmc;
	}
	public void setFqmc(String fqmc) {
		this.fqmc = fqmc;
	}
	public int getOrdernum() {
		return ordernum;
	}
	public void setOrdernum(int ordernum) {
		this.ordernum = ordernum;
	}
	public AuditStateEnum getAuditState() {
		return auditState;
	}
	public void setAuditState(AuditStateEnum auditState) {
		this.auditState = auditState;
	}
	public String getBusiness_url() {
		return business_url;
	}
	public void setBusiness_url(String business_url) {
		this.business_url = business_url;
	}
	public String getLoadYmCode() {
		return loadYmCode;
	}
	public void setLoadYmCode(String loadYmCode) {
		this.loadYmCode = loadYmCode;
	}
	public String getYwzd() {
		return ywzd;
	}
	public void setYwzd(String ywzd) {
		this.ywzd = ywzd;
	}
	public int getMaxlcxh() {
		return maxlcxh;
	}
	public void setMaxlcxh(int maxlcxh) {
		this.maxlcxh = maxlcxh;
	}
	public ShlcKzcsEnum getShlcKzcs() {
		return shlcKzcs;
	}
	public void setShlcKzcs(ShlcKzcsEnum shlcKzcs) {
		this.shlcKzcs = shlcKzcs;
	}
	public ShxxDto getShxx() {
		return shxx;
	}
	public void setShxx(ShxxDto shxx) {
		this.shxx = shxx;
	}

	public List<SpgwcyDto> getSpgwcyDtos() {
		return spgwcyDtos;
	}

	public void setSpgwcyDtos(List<SpgwcyDto> spgwcyDtos) {
		this.spgwcyDtos = spgwcyDtos;
	}

	public String getShlbmc() {
		return shlbmc;
	}
	public void setShlbmc(String shlbmc) {
		this.shlbmc = shlbmc;
	}
	public String getSqrddid() {
		return sqrddid;
	}
	public void setSqrddid(String sqrddid) {
		this.sqrddid = sqrddid;
	}
	public List<SpgwcyDto> getNo_spgwcyDtos() {
		return no_spgwcyDtos;
	}
	public void setNo_spgwcyDtos(List<SpgwcyDto> no_spgwcyDtos) {
		this.no_spgwcyDtos = no_spgwcyDtos;
	}
	public String getQwwcsj() {
		return qwwcsj;
	}
	public void setQwwcsj(String qwwcsj) {
		this.qwwcsj = qwwcsj;
	}
	public boolean isLastStep() {
		return lastStep;
	}
	public void setLastStep(boolean lastStep) {
		this.lastStep = lastStep;
	}
	public List<String> getJsids() {
		return jsids;
	}
	public void setJsids(List<String> jsids) {
		this.jsids = jsids;
	}
	public List<String> getT_jsids() {
		return t_jsids;
	}
	public void setT_jsids(List<String> t_jsids) {
		this.t_jsids = t_jsids;
	}

	public boolean isEntryLastStep() {
		return entryLastStep;
	}

	public void setEntryLastStep(boolean entryLastStep) {
		this.entryLastStep = entryLastStep;
	}

	public boolean isBack() {
		return isBack;
	}

	public void setBack(boolean isBack) {
		this.isBack = isBack;
	}
	
}
