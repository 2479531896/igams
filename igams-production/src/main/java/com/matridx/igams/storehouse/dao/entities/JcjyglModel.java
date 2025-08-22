package com.matridx.igams.storehouse.dao.entities;


import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias(value = "JcjyglModel")
public class JcjyglModel extends BaseBasicModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    //借出借用管理id
    private String jcjyid;
    //借用单号
    private String jydh;
    //单位类型
    private String dwlx;
    //单位id
    private String dwid;
    //部门
    private String bm;
    //借用日期
    private String jyrq;
    //是否支付费用
    private String sfzfyf;
    //联系人
    private String lxr;
    //联系方式
    private String lxfs;
    //关联id
	private String glid;
    //备注
    private String bz;
    //U8借出单
	private String u8jydh;
	//审核人
	private String shry;
	//审核人
	private String shrq;//审核日期
	private String htbh;//合同编号
	private String fzdq;//负责大区
	private String zd;//终端
	private String zdqy;//终端区域
	private String shdz;//收货地址
	private String shlxfs;//收货联系方式
	private String kdxx;//快递信息
	private String sfqr;//是否确认
	private String ywy;//业务员
	private String yjcjyid;//原借出借用id
	//单据类型
	private String djlx;
	//是否签收
	private String sfqs;
	//销售类型
	private String xslx;
	private String fzr;//负责人
	//运输方式
	private String ysfs;

	public String getYsfs() {
		return ysfs;
	}

	public void setYsfs(String ysfs) {
		this.ysfs = ysfs;
	}

	public String getFzr() {
		return fzr;
	}

	public void setFzr(String fzr) {
		this.fzr = fzr;
	}

	public String getXslx() {
		return xslx;
	}

	public void setXslx(String xslx) {
		this.xslx = xslx;
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
	public String getYjcjyid() {
		return yjcjyid;
	}

	public void setYjcjyid(String yjcjyid) {
		this.yjcjyid = yjcjyid;
	}

	public String getYwy() {
		return ywy;
	}

	public void setYwy(String ywy) {
		this.ywy = ywy;
	}


	public String getShry() {
		return shry;
	}

	public void setShry(String shry) {
		this.shry = shry;
	}

	public String getShrq() {
		return shrq;
	}

	public void setShrq(String shrq) {
		this.shrq = shrq;
	}
	public String getHtbh() {
		return htbh;
	}

	public void setHtbh(String htbh) {
		this.htbh = htbh;
	}

	public String getFzdq() {
		return fzdq;
	}

	public void setFzdq(String fzdq) {
		this.fzdq = fzdq;
	}

	public String getZd() {
		return zd;
	}

	public void setZd(String zd) {
		this.zd = zd;
	}

	public String getZdqy() {
		return zdqy;
	}

	public void setZdqy(String zdqy) {
		this.zdqy = zdqy;
	}

	public String getShdz() {
		return shdz;
	}

	public void setShdz(String shdz) {
		this.shdz = shdz;
	}

	public String getShlxfs() {
		return shlxfs;
	}

	public void setShlxfs(String shlxfs) {
		this.shlxfs = shlxfs;
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

	public String getU8jydh() {
		return u8jydh;
	}

	public void setU8jydh(String u8jydh) {
		this.u8jydh = u8jydh;
	}

	public String getGlid() {
		return glid;
	}

	public void setGlid(String glid) {
		this.glid = glid;
	}

	public String getJcjyid() {
		return jcjyid;
	}

	public void setJcjyid(String jcjyid) {
		this.jcjyid = jcjyid;
	}

	public String getJydh() {
		return jydh;
	}

	public void setJydh(String jydh) {
		this.jydh = jydh;
	}

	public String getDwlx() {
		return dwlx;
	}

	public void setDwlx(String dwlx) {
		this.dwlx = dwlx;
	}

	public String getDwid() {
		return dwid;
	}

	public void setDwid(String dwid) {
		this.dwid = dwid;
	}

	public String getBm() {
		return bm;
	}

	public void setBm(String bm) {
		this.bm = bm;
	}

	public String getJyrq() {
		return jyrq;
	}

	public void setJyrq(String jyrq) {
		this.jyrq = jyrq;
	}

	public String getSfzfyf() {
		return sfzfyf;
	}

	public void setSfzfyf(String sfzfyf) {
		this.sfzfyf = sfzfyf;
	}

	public String getLxr() {
		return lxr;
	}

	public void setLxr(String lxr) {
		this.lxr = lxr;
	}

	public String getLxfs() {
		return lxfs;
	}

	public void setLxfs(String lxfs) {
		this.lxfs = lxfs;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}
}
