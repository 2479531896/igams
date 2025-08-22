package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="JkdymxModel")
public class JkdymxModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//调用id
	private String dyid;
	//类型区分 发送 send;接收recv
	private String lxqf;
	//调用分类
	private String dyfl;
	//调用子分类
	private String dyzfl;
	//业务ID
	private String ywid;
	//调用时间
	private String dysj;
	//调用地址
	private String dydz;
	//内容
	private String nr;
	//返回时间
	private String fhsj;
	//返回内容
	private String fhnr;
	//其他信息
	private String qtxx;
	//是否成功  0:失败 1:成功 2:未知';
	private String sfcg;
	//子业务ID
	private String zywid;
	//子项目
	private String zxmid;

	public String getZywid() {
		return zywid;
	}

	public void setZywid(String zywid) {
		this.zywid = zywid;
	}

	public String getDyid() {
		return dyid;
	}
	public void setDyid(String dyid) {
		this.dyid = dyid;
	}
	public String getLxqf() {
		return lxqf;
	}
	public void setLxqf(String lxqf) {
		this.lxqf = lxqf;
	}
	public String getDyfl() {
		return dyfl;
	}
	public void setDyfl(String dyfl) {
		this.dyfl = dyfl;
	}
	public String getDyzfl() {
		return dyzfl;
	}
	public void setDyzfl(String dyzfl) {
		this.dyzfl = dyzfl;
	}
	public String getYwid() {
		return ywid;
	}
	public void setYwid(String ywid) {
		this.ywid = ywid;
	}
	public String getDysj() {
		return dysj;
	}
	public void setDysj(String dysj) {
		this.dysj = dysj;
	}
	public String getDydz() {
		return dydz;
	}
	public void setDydz(String dydz) {
		this.dydz = dydz;
	}
	public String getNr() {
		return nr;
	}
	public void setNr(String nr) {
		this.nr = nr;
	}
	public String getFhsj() {
		return fhsj;
	}
	public void setFhsj(String fhsj) {
		this.fhsj = fhsj;
	}
	public String getFhnr() {
		return fhnr;
	}
	public void setFhnr(String fhnr) {
		this.fhnr = fhnr;
	}
	public String getQtxx() {
		return qtxx;
	}
	public void setQtxx(String qtxx) {
		this.qtxx = qtxx;
	}
	public String getSfcg() {
		return sfcg;
	}
	public void setSfcg(String sfcg) {
		this.sfcg = sfcg;
	}

    public String getZxmid() {
        return zxmid;
    }

    public void setZxmid(String zxmid) {
        this.zxmid = zxmid;
    }
}
