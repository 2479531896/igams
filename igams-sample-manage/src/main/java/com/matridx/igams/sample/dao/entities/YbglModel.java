package com.matridx.igams.sample.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;
import com.matridx.igams.common.dao.entities.BaseBasicModel;

@Alias(value="YbglModel")
public class YbglModel extends BaseBasicModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8945791171098249355L;
	
	private String id;
	//标本ID
	private String ybid;
	//来源ID
	private String lyid;
	//标本类型
	private String yblx;
	//标本来源类型  外部 内部
	private String yblylx;
	//标本编号
	private String ybbh;
	//冰箱ID
	private String bxid;
	//抽屉ID
	private String chtid;
	//盒子ID
	private String hzid;
	//标本获取时间
	private String hqsj;
	//原数量
	private String ysl;
	//数量
	private String sl;
	//预定数
	private String yds;
	//起始位置
	private String qswz;
	//结束位置
	private String jswz;
	//单位
	private String dw;
	//浓度
	private String nd;
	//状态
	private String zt;
	//标本规格
	private String ybgg;
	//产品编号
	private String cpbh;
	//标本批号
	private String ybph;
	//标本编号s
	private List<String> ybbhs;


	public List<String> getYbbhs() {
		return ybbhs;
	}

	public void setYbbhs(List<String> ybbhs) {
		this.ybbhs = ybbhs;
	}

	public String getYbgg() {
		return ybgg;
	}

	public void setYbgg(String ybgg) {
		this.ybgg = ybgg;
	}

	public String getCpbh() {
		return cpbh;
	}

	public void setCpbh(String cpbh) {
		this.cpbh = cpbh;
	}

	public String getYbph() {
		return ybph;
	}

	public void setYbph(String ybph) {
		this.ybph = ybph;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getYbid() {
		return ybid;
	}

	public void setYbid(String ybid) {
		this.ybid = ybid;
	}

	public String getLyid() {
		return lyid;
	}

	public void setLyid(String lyid) {
		this.lyid = lyid;
	}

	public String getYblx() {
		return yblx;
	}

	public void setYblx(String yblx) {
		this.yblx = yblx;
	}

	public String getYblylx() {
		return yblylx;
	}

	public void setYblylx(String yblylx) {
		this.yblylx = yblylx;
	}

	public String getYbbh() {
		return ybbh;
	}

	public void setYbbh(String ybbh) {
		this.ybbh = ybbh;
	}

	public String getBxid() {
		return bxid;
	}

	public void setBxid(String bxid) {
		this.bxid = bxid;
	}

	public String getChtid() {
		return chtid;
	}

	public void setChtid(String chtid) {
		this.chtid = chtid;
	}

	public String getHzid() {
		return hzid;
	}

	public void setHzid(String hzid) {
		this.hzid = hzid;
	}

	public String getYsl() {
		return ysl;
	}

	public void setYsl(String ysl) {
		this.ysl = ysl;
	}

	public String getSl() {
		return sl;
	}

	public void setSl(String sl) {
		this.sl = sl;
	}

	public String getYds() {
		return yds;
	}

	public void setYds(String yds) {
		this.yds = yds;
	}

	public String getQswz() {
		return qswz;
	}

	public void setQswz(String qswz) {
		this.qswz = qswz;
	}

	public String getJswz() {
		return jswz;
	}

	public void setJswz(String jswz) {
		this.jswz = jswz;
	}

	public String getDw() {
		return dw;
	}

	public void setDw(String dw) {
		this.dw = dw;
	}

	public String getNd() {
		return nd;
	}

	public void setNd(String nd) {
		this.nd = nd;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getHqsj() {
		return hqsj;
	}

	public void setHqsj(String hqsj) {
		this.hqsj = hqsj;
	}

}
