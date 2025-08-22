package com.matridx.crf.web.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="QmngskgryModel")
public class QmngskgryModel extends BaseModel{
	//抗感染药物使用id
	private String kgrywsyid;
	//药物
	private String yw;
	//剂量
	private String jl;
	//单位
	private String dw;
	//其他
	private String qt;
	//频次
	private String pc;
	//频次其他
	private String pcqt;
	//开始时间
	private String kssj;
	//停止时间
	private String tzsj;
	//用药依据
	private String yyyj;
	//停药原因
	private String tyyy;
	//停药原因其他
	private String tyyyqt;
	//显示排序
	private String xspx;
	//Q-mNGS患者信息
	private String qmngshzid;
	//抗感染药物使用id
	public String getKgrywsyid() {
		return kgrywsyid;
	}
	public void setKgrywsyid(String kgrywsyid){
		this.kgrywsyid = kgrywsyid;
	}
	//药物
	public String getYw() {
		return yw;
	}
	public void setYw(String yw){
		this.yw = yw;
	}
	//剂量
	public String getJl() {
		return jl;
	}
	public void setJl(String jl){
		this.jl = jl;
	}
	//单位
	public String getDw() {
		return dw;
	}
	public void setDw(String dw){
		this.dw = dw;
	}
	//其他
	public String getQt() {
		return qt;
	}
	public void setQt(String qt){
		this.qt = qt;
	}
	//频次
	public String getPc() {
		return pc;
	}
	public void setPc(String pc){
		this.pc = pc;
	}
	//频次其他
	public String getPcqt() {
		return pcqt;
	}
	public void setPcqt(String pcqt){
		this.pcqt = pcqt;
	}
	//开始时间
	public String getKssj() {
		return kssj;
	}
	public void setKssj(String kssj){
		this.kssj = kssj;
	}
	//停止时间
	public String getTzsj() {
		return tzsj;
	}
	public void setTzsj(String tzsj){
		this.tzsj = tzsj;
	}
	//用药依据
	public String getYyyj() {
		return yyyj;
	}
	public void setYyyj(String yyyj){
		this.yyyj = yyyj;
	}
	//停药原因
	public String getTyyy() {
		return tyyy;
	}
	public void setTyyy(String tyyy){
		this.tyyy = tyyy;
	}
	//停药原因其他
	public String getTyyyqt() {
		return tyyyqt;
	}
	public void setTyyyqt(String tyyyqt){
		this.tyyyqt = tyyyqt;
	}
	//显示排序
	public String getXspx() {
		return xspx;
	}
	public void setXspx(String xspx){
		this.xspx = xspx;
	}
	//Q-mNGS患者信息
	public String getQmngshzid() {
		return qmngshzid;
	}
	public void setQmngshzid(String qmngshzid){
		this.qmngshzid = qmngshzid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
