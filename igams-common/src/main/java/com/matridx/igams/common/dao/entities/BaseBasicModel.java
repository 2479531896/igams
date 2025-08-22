package com.matridx.igams.common.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.DataFilterModel;
import com.matridx.igams.common.dao.DataPermissionModel;
import com.matridx.igams.common.enums.AuditResultEnum;

public class BaseBasicModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 数据权限范围Model
	 */
	public DataPermissionModel dataPermissionModel = new DataPermissionModel();
	/**
	 * 数据过滤器Model
	 */
	public DataFilterModel dataFilterModel = new DataFilterModel();
	

	//审核类别
	private String auditType;
	//委托资源ID
	private String wtZyid;
	//委托资源编号
	private String wtWtbh;
	
	//审批记录信息属性
	private String dqshzt;	//当前审核状态 用于区分已审核，还是待审核
	private String sqr;			//申请人
	private String sqrxm;		//申请人姓名

	private String sqsjstart;	//审核申请开始时间
	private String sqsjend;		//审核申请开始时间
	private String shxx_shxxid;	//审核信息id

	private String shxx_gcid;	//过程id
	private String shxx_sftg;	//是否通过
	private String shxx_sftgmc;	//是否通过名称
	private String shxx_sqsj;	//申请时间
	private String shxx_shsj;	//审核时间
	private String shxx_shyj;	//审核意见
	private String shxx_ssyj;	//申诉已经
	private String shxx_gwid;	//岗位id
	private String shxx_gwmc;	//岗位名称
	private String shxx_shid;	//审核id
	private String shxx_lcxh;	//流程序号
	private String shxx_xh;		//序号
	private String shxx_lrry;	//录入人
	private String shxx_lrryxm;	//录入人姓名
	private String shxx_shry;	//审核人
	private String shxx_shryxm;	//审核人姓名
	private String shxx_lrsj;	//录入时间
	//业务列表显示审核信息的附加字段
	private String shxx_dqgwmc; //当前审核岗位名称
	private String shxx_dqgwid; //当前审核岗位id
	private String shxx_shjs;	//审核角色
	private String ddspid;
	private String ddslid;

	private String dqshr;

	public String getDqshr() {
		return dqshr;
	}

	public void setDqshr(String dqshr) {
		this.dqshr = dqshr;
	}

	public String getDdspid() {
		return ddspid;
	}

	public void setDdspid(String ddspid) {
		this.ddspid = ddspid;
	}

	public String getDdslid() {
		return ddslid;
	}

	public void setDdslid(String ddslid) {
		this.ddslid = ddslid;
	}

	public String getShxx_shjs() {
		return shxx_shjs;
	}
	public void setShxx_shjs(String shxx_shjs) {
		this.shxx_shjs = shxx_shjs;
	}

	public String getShxx_shxxid() {
		return shxx_shxxid;
	}
	public void setShxx_shxxid(String shxx_shxxid) {
		this.shxx_shxxid = shxx_shxxid;
	}
	public String getShxx_gcid() {
		return shxx_gcid;
	}
	public void setShxx_gcid(String shxx_gcid) {
		this.shxx_gcid = shxx_gcid;
	}
	public String getShxx_sftg() {
		return shxx_sftg;
	}
	public void setShxx_sftg(String shxxSftg) {
		this.shxx_sftg = shxxSftg;
		this.shxx_sftgmc = AuditResultEnum.getValueByCode(shxxSftg);
	}

	public String getShxx_sftgmc() {
		return shxx_sftgmc;
	}
	
	public String getShxx_sqsj() {
		return shxx_sqsj;
	}
	public void setShxx_sqsj(String shxx_sqsj) {
		this.shxx_sqsj = shxx_sqsj;
	}
	public String getShxx_shsj() {
		return shxx_shsj;
	}
	public void setShxx_shsj(String shxx_shsj) {
		this.shxx_shsj = shxx_shsj;
	}
	public String getShxx_shyj() {
		return shxx_shyj;
	}
	public void setShxx_shyj(String shxx_shyj) {
		this.shxx_shyj = shxx_shyj;
	}
	public String getShxx_ssyj() {
		return shxx_ssyj;
	}
	public void setShxx_ssyj(String shxx_ssyj) {
		this.shxx_ssyj = shxx_ssyj;
	}
	public String getShxx_gwid() {
		return shxx_gwid;
	}
	public void setShxx_gwid(String shxx_gwid) {
		this.shxx_gwid = shxx_gwid;
	}
	public String getShxx_gwmc() {
		return shxx_gwmc;
	}
	public void setShxx_gwmc(String shxx_gwmc) {
		this.shxx_gwmc = shxx_gwmc;
	}
	public String getShxx_shid() {
		return shxx_shid;
	}
	public void setShxx_shid(String shxx_shid) {
		this.shxx_shid = shxx_shid;
	}
	public String getShxx_lcxh() {
		return shxx_lcxh;
	}
	public void setShxx_lcxh(String shxx_lcxh) {
		this.shxx_lcxh = shxx_lcxh;
	}
	public String getShxx_xh() {
		return shxx_xh;
	}
	public void setShxx_xh(String shxx_xh) {
		this.shxx_xh = shxx_xh;
	}
	public String getShxx_lrry() {
		return shxx_lrry;
	}
	public void setShxx_lrry(String shxx_lrry) {
		this.shxx_lrry = shxx_lrry;
	}
	public String getShxx_lrryxm() {
		return shxx_lrryxm;
	}
	public void setShxx_lrryxm(String shxx_lrryxm) {
		this.shxx_lrryxm = shxx_lrryxm;
	}
	public String getShxx_shry() {
		return shxx_shry;
	}
	public void setShxx_shry(String shxx_shry) {
		this.shxx_shry = shxx_shry;
	}
	public String getShxx_shryxm() {
		return shxx_shryxm;
	}
	public void setShxx_shryxm(String shxx_shryxm) {
		this.shxx_shryxm = shxx_shryxm;
	}
	public String getShxx_lrsj() {
		return shxx_lrsj;
	}
	public void setShxx_lrsj(String shxx_lrsj) {
		this.shxx_lrsj = shxx_lrsj;
	}
	public String getShxx_dqgwmc() {
		return shxx_dqgwmc;
	}
	public void setShxx_dqgwmc(String shxx_dqgwmc) {
		this.shxx_dqgwmc = shxx_dqgwmc;
	}
	public String getShxx_dqgwid() {
		return shxx_dqgwid;
	}
	public void setShxx_dqgwid(String shxx_dqgwid) {
		this.shxx_dqgwid = shxx_dqgwid;
	}
	public String getAuditType() {
		return auditType;
	}
	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}
	public String getDqshzt() {
		return dqshzt;
	}
	public void setDqshzt(String dqshzt) {
		this.dqshzt = dqshzt;
	}
	
	public DataPermissionModel getDataPermissionModel() {
		dataPermissionModel.setModelClass(this.getClass());
		return dataPermissionModel;
	}

	public void setDataPermissionModel(DataPermissionModel dataPermissionModel) {
		this.dataPermissionModel = dataPermissionModel;
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
	public boolean isWtOpe(){
		return getDataPermissionModel().isWtOpe();
	}
	public String getSqr() {
		return sqr;
	}
	public void setSqr(String sqr) {
		this.sqr = sqr;
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
	
	public DataFilterModel getDataFilterModel() {
		// User user = getLoginInfo();
		// dataFilterModel.setUser(user);
		return dataFilterModel;
	}

	public void setDataFilterModel(DataFilterModel dataFilterModel) {
		this.dataFilterModel = dataFilterModel;
	}
	
}
