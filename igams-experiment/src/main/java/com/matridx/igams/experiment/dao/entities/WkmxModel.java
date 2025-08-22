package com.matridx.igams.experiment.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="WkmxModel")
public class WkmxModel extends BaseModel{
	//文库ID
	private String wkid;
	//序号
	private String xh;
	//内部编号
	private String nbbh;
	//送检ID
	private String sjid;
	//接头信息
	private String jtxx;
	//数量(浓度)
	private String quantity;
	//文库原液浓度
	private String wkyynd;
	//检测单位
	private String jcdw;
	//文库日期
	private String wkrq;
	//逆转录试剂盒
	private String sjph2;
	//宏基因组DNA建库试剂盒
	private String sjph1;
	//后缀标记
	private String hzbj;
	//检测项目代码.D,R
	private String jcxmdm;
	//结果Ctvaule
	private String ctvaule;
	//结果浓度
	private String concentrationre;
	//结果referencedye
	private String referencedye;
	//结果
	private String qcresult;
	//第几次检查的
	private String djcsy;
	//空位
	private String well;
	//提取码
	private String tqm;
	private String tqmxid;

	public String getTqmxid() {
		return tqmxid;
	}

	public void setTqmxid(String tqmxid) {
		this.tqmxid = tqmxid;
	}

	public String getTqm() {
		return tqm;
	}

	public void setTqm(String tqm) {
		this.tqm = tqm;
	}

	public String getJcxmdm() {
		return jcxmdm;
	}

	public void setJcxmdm(String jcxmdm) {
		this.jcxmdm = jcxmdm;
	}

	public String getHzbj() {
		return hzbj;
	}

	public void setHzbj(String hzbj) {
		this.hzbj = hzbj;
	}

	public String getSjph2() {
		return sjph2;
	}
	public void setSjph2(String sjph2) {
		this.sjph2 = sjph2;
	}
	public String getSjph1() {
		return sjph1;
	}
	public void setSjph1(String sjph1) {
		this.sjph1 = sjph1;
	}
	public String getWkrq() {
		return wkrq;
	}
	public void setWkrq(String wkrq) {
		this.wkrq = wkrq;
	}
	public String getJcdw() {
		return jcdw;
	}
	public void setJcdw(String jcdw) {
		this.jcdw = jcdw;
	}
	public String getWkyynd()
	{
		return wkyynd;
	}
	public void setWkyynd(String wkyynd)
	{
		this.wkyynd = wkyynd;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	//文库ID
	public String getWkid() {
		return wkid;
	}
	public void setWkid(String wkid){
		this.wkid = wkid;
	}
	//序号
	public String getXh() {
		return xh;
	}
	public void setXh(String xh){
		this.xh = xh;
	}
	//内部编号
	public String getNbbh() {
		return nbbh;
	}
	public void setNbbh(String nbbh){
		this.nbbh = nbbh;
	}
	//送检ID
	public String getSjid() {
		return sjid;
	}
	public void setSjid(String sjid){
		this.sjid = sjid;
	}
	//接头信息
	public String getJtxx() {
		return jtxx;
	}
	public void setJtxx(String jtxx){
		this.jtxx = jtxx;
	}

	public String getCtvaule() {
		return ctvaule;
	}

	public void setCtvaule(String ctvaule) {
		this.ctvaule = ctvaule;
	}

	public String getConcentrationre() {
		return concentrationre;
	}

	public void setConcentrationre(String concentrationre) {
		this.concentrationre = concentrationre;
	}

	public String getReferencedye() {
		return referencedye;
	}

	public void setReferencedye(String referencedye) {
		this.referencedye = referencedye;
	}

	public String getQcresult() {
		return qcresult;
	}

	public void setQcresult(String qcresult) {
		this.qcresult = qcresult;
	}

	public String getDjcsy() {
		return djcsy;
	}

	public void setDjcsy(String djcsy) {
		this.djcsy = djcsy;
	}

	public String getWell() {
		return well;
	}

	public void setWell(String well) {
		this.well = well;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
