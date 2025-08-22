package com.matridx.igams.wechat.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;
import com.matridx.igams.common.dao.entities.BaseBasicModel;

@Alias(value="SjyzModel")
public class SjyzModel extends BaseBasicModel{
	//验证id
	private String yzid;
	//送检id
	private String sjid;
	//验证类型
	private String yzlb;
	//是否符合
	private String sffh;
	//验证结果
	private String yzjg;
	//备注
	private String bz;
	//状态
	private String zt;
	//状态[多]
	private List<String> zts;
	//客户通知
	private String khtz;
	//区分
	private String qf;
	//试剂批号
	private String sjph;
	//引物编号
	private String ywbh;


	public String getSffh() {
		return sffh;
	}

	public void setSffh(String sffh) {
		this.sffh = sffh;
	}

	public String getSjph() {
		return sjph;
	}
	public void setSjph(String sjph) {
		this.sjph = sjph;
	}
	public String getYwbh() {
		return ywbh;
	}
	public void setYwbh(String ywbh) {
		this.ywbh = ywbh;
	}
	public String getYzid() {
		return yzid;
	}
	public void setYzid(String yzid) {
		this.yzid = yzid;
	}
	public String getSjid() {
		return sjid;
	}
	public void setSjid(String sjid) {
		this.sjid = sjid;
	}
	public String getYzlb() {
		return yzlb;
	}
	public void setYzlb(String yzlb) {
		this.yzlb = yzlb;
	}
	public String getYzjg() {
		return yzjg;
	}
	public void setYzjg(String yzjg) {
		this.yzjg = yzjg;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getZt() {
		return zt;
	}
	public void setZt(String zt) {
		this.zt = zt;
	}
	public List<String> getZts() {
		return zts;
	}
	public void setZts(List<String> zts) {
		this.zts = zts;
	}
	public String getKhtz() {
		return khtz;
	}
	public void setKhtz(String khtz) {
		this.khtz = khtz;
	}
	public String getQf() {
		return qf;
	}
	public void setQf(String qf) {
		this.qf = qf;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
