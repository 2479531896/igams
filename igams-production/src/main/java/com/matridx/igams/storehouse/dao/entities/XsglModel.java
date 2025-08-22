package com.matridx.igams.storehouse.dao.entities;


import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias(value="XsglModel")
public class XsglModel extends BaseBasicModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//销售id
	private String xsid;
	//OA销售单号
	private String oaxsdh;
	//U8销售单号
	private String u8xsdh;
	//关联id
	private String glid;
	//订单日期
	private String ddrq;
	//业务类型
	private String ywlx;
	//销售类型
	private String xslx;
	//销售部门
	private String xsbm;
	//业务员
	private String ywy;
	//客户简称
	private String khjc;
	//收货地址
	private String shdz;
	//备注
	private String bz;
	//状态
	private String zt;
	//负责大区
	private String fzdq;
	//销售总金额
	private String xszje;
	//销售发票号
	private String xsfph;
	//快递信息
	private String kdxx;
	//是否确认
	private String sfqr;
	//收货联系方式
	private String shlxfs;
	//审核人员
	private String shry;
	//折扣
	private String zk;
	//到款金额
	private String dkje;
	//终端区域
	private String zdqy;
	//到款月
	private String dky;
	//收货联系人
	private String shlxr;
		//借出借用id
	private String jcjyid;
	//原销售id
	private String yxsid;
	//单据类型
	private String djlx;
	//是否签收
	private String sfqs;
	//生产状态
	private String sczt;
	//应收款
	private String ysk;
	//负责人
	private String fzr;
	//发货状态
	private String fhzt;
	//运输方式
	private String ysfs;
	//回款周期
	private String hkzq;
	//修改人员
	private String xgry;

	@Override
	public String getXgry() {
		return xgry;
	}

	@Override
	public void setXgry(String xgry) {
		this.xgry = xgry;
	}

	public String getHkzq() {
		return hkzq;
	}

	public void setHkzq(String hkzq) {
		this.hkzq = hkzq;
	}

	public String getYsfs() {
		return ysfs;
	}

	public void setYsfs(String ysfs) {
		this.ysfs = ysfs;
	}

	public String getFhzt() {
		return fhzt;
	}

	public void setFhzt(String fhzt) {
		this.fhzt = fhzt;
	}

	public String getFzr() {
		return fzr;
	}

	public void setFzr(String fzr) {
		this.fzr = fzr;
	}

	public String getYsk() {
		return ysk;
	}

	public void setYsk(String ysk) {
		this.ysk = ysk;
	}

	public String getSczt() {
		return sczt;
	}

	public void setSczt(String sczt) {
		this.sczt = sczt;
	}

	public String getSfqs() {
		return sfqs;
	}

	public void setSfqs(String sfqs) {
		this.sfqs = sfqs;
	}
	public String getDjlx() {
		return djlx;
	}

	public void setDjlx(String djlx) {
		this.djlx = djlx;
	}

	public String getYxsid() {
		return yxsid;
	}

	public void setYxsid(String yxsid) {
		this.yxsid = yxsid;
	}

	public String getJcjyid() {
		return jcjyid;
	}

	public void setJcjyid(String jcjyid) {
		this.jcjyid = jcjyid;
	}

	public String getShlxr() {
		return shlxr;
	}

	public void setShlxr(String shlxr) {
		this.shlxr = shlxr;
	}

	public String getKdxx() {
		return kdxx;
	}

	public void setKdxx(String kdxx) {
		this.kdxx = kdxx;
	}

	public String getSfqr() {
		return sfqr;
	}

	public void setSfqr(String sfqr) {
		this.sfqr = sfqr;
	}

	public String getShlxfs() {
		return shlxfs;
	}

	public void setShlxfs(String shlxfs) {
		this.shlxfs = shlxfs;
	}

	public String getShry() {
		return shry;
	}

	public void setShry(String shry) {
		this.shry = shry;
	}

	public String getZk() {
		return zk;
	}

	public void setZk(String zk) {
		this.zk = zk;
	}

	public String getDkje() {
		return dkje;
	}

	public void setDkje(String dkje) {
		this.dkje = dkje;
	}

	public String getZdqy() {
		return zdqy;
	}

	public void setZdqy(String zdqy) {
		this.zdqy = zdqy;
	}


	public String getDky() {
		return dky;
	}

	public void setDky(String dky) {
		this.dky = dky;
	}

	public String getXsfph() {
		return xsfph;
	}

	public void setXsfph(String xsfph) {
		this.xsfph = xsfph;
	}

	public String getXszje() {
		return xszje;
	}

	public void setXszje(String xszje) {
		this.xszje = xszje;
	}

	public String getFzdq() {
		return fzdq;
	}

	public void setFzdq(String fzdq) {
		this.fzdq = fzdq;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getXsid() {
		return xsid;
	}

	public void setXsid(String xsid) {
		this.xsid = xsid;
	}

	public String getOaxsdh() {
		return oaxsdh;
	}

	public void setOaxsdh(String oaxsdh) {
		this.oaxsdh = oaxsdh;
	}

	public String getU8xsdh() {
		return u8xsdh;
	}

	public void setU8xsdh(String u8xsdh) {
		this.u8xsdh = u8xsdh;
	}

	public String getGlid() {
		return glid;
	}

	public void setGlid(String glid) {
		this.glid = glid;
	}

	public String getDdrq() {
		return ddrq;
	}

	public void setDdrq(String ddrq) {
		this.ddrq = ddrq;
	}

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public String getXslx() {
		return xslx;
	}

	public void setXslx(String xslx) {
		this.xslx = xslx;
	}

	public String getXsbm() {
		return xsbm;
	}

	public void setXsbm(String xsbm) {
		this.xsbm = xsbm;
	}

	public String getYwy() {
		return ywy;
	}

	public void setYwy(String ywy) {
		this.ywy = ywy;
	}

	public String getKhjc() {
		return khjc;
	}

	public void setKhjc(String khjc) {
		this.khjc = khjc;
	}

	public String getShdz() {
		return shdz;
	}

	public void setShdz(String shdz) {
		this.shdz = shdz;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public XsglModel() {
	}

	public XsglModel(String xsid,String ysk,String xgry) {
		this.xsid = xsid;
		this.ysk = ysk;
		this.xgry = xgry;
	}
}
