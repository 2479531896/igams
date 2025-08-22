package com.matridx.crf.web.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="HbpdrxxjlModel")
public class HbpdrxxjlModel extends BaseModel{
	//表主键，HBP记录ID
	private String hbpjlid;
	//HBP患者ID
	private String hbphzxxid;
	//血浆肝素结合蛋白(HBP)
	private String xjgsjhdb;
	//未查血浆肝素结合蛋白
	private String wcxjgsjhdb;
	//BALF肝素结合蛋白(HBP)
	private String balfgsjhdb;
	//已外送BALF肝素结合蛋白
	private String ywsbalfgsjhdb;
	//降钙素原(PCT)
	private String jgsy;
	//未查降钙素原
	private String wcjgsy;
	//C反应蛋白(CRP)
	private String cfydb;
	//未查C反应蛋白
	private String wccfydb;
	//白细胞计数(WBC)
	private String bxbjs;
	//未查白细胞计数
	private String wcbxbjs;
	//中性粒细胞比率
	private String zxlxbbl;
	//未查中性粒细胞比率
	private String wczxlxbbl;
	//红细胞计数(RBC)
	private String hxbjs;
	//未查红细胞计数
	private String wchxbjs;
	//血红蛋白(Hb)
	private String xhdb;
	//未查血红蛋白
	private String wcxhdb;
	//血小板计数(PLT)
	private String xxbjs;
	//未查血小板计数
	private String wcxxbjs;
	//总胆红素(T-BIL)
	private String zdhs;
	//未查总胆红素
	private String wczdhs;
	//谷丙转氨酶(ALT)
	private String gbzam;
	//未查谷丙转氨酶
	private String wcgbzam;
	//总蛋白
	private String zdb;
	//未查总蛋白
	private String wczdb;
	//白蛋白(Alb)
	private String bdb;
	//未查白蛋白
	private String wcbdb;
	//肌酐(CREA)
	private String jg;
	//未查肌酐
	private String wcjg;
	//氧分压(PO2)
	private String yfy;
	//未查氧分压
	private String wcyfy;
	//吸氧浓度(%)
	private String xynd;
	//未查吸氧浓度
	private String wcxynd;
	//乳酸(Lac)
	private String rs;
	//未查乳酸
	private String wcrs;
	//24小时液体入量
	private String ytrl;
	//24小时液体出量
	private String ytcl;
	//24小时尿量
	private String nl;
	//血管活性药物
	private String xghxyw;
	//其他血管活性药物
	private String qtxghxyw;
	//记录第几天
	private String jldjt;
	//表主键，HBP记录ID
	public String getHbpjlid() {
		return hbpjlid;
	}
	public void setHbpjlid(String hbpjlid){
		this.hbpjlid = hbpjlid;
	}
	//HBP患者ID
	public String getHbphzxxid() { return hbphzxxid; }
	public void setHbphzxxid(String hbphzxxid) { this.hbphzxxid = hbphzxxid; }
	//血浆肝素结合蛋白(HBP)
	public String getXjgsjhdb() {
		return xjgsjhdb;
	}
	public void setXjgsjhdb(String xjgsjhdb){
		this.xjgsjhdb = xjgsjhdb;
	}
	//未查血浆肝素结合蛋白
	public String getWcxjgsjhdb() {
		return wcxjgsjhdb;
	}
	public void setWcxjgsjhdb(String wcxjgsjhdb){
		this.wcxjgsjhdb = wcxjgsjhdb;
	}
	//BALF肝素结合蛋白(HBP)
	public String getBalfgsjhdb() {
		return balfgsjhdb;
	}
	public void setBalfgsjhdb(String balfgsjhdb){
		this.balfgsjhdb = balfgsjhdb;
	}
	//已外送BALF肝素结合蛋白
	public String getYwsbalfgsjhdb() {
		return ywsbalfgsjhdb;
	}
	public void setYwsbalfgsjhdb(String ywsbalfgsjhdb){
		this.ywsbalfgsjhdb = ywsbalfgsjhdb;
	}
	//降钙素原(PCT)
	public String getJgsy() {
		return jgsy;
	}
	public void setJgsy(String jgsy){
		this.jgsy = jgsy;
	}
	//未查降钙素原
	public String getWcjgsy() {
		return wcjgsy;
	}
	public void setWcjgsy(String wcjgsy){
		this.wcjgsy = wcjgsy;
	}
	//C反应蛋白(CRP)
	public String getCfydb() {
		return cfydb;
	}
	public void setCfydb(String cfydb){
		this.cfydb = cfydb;
	}
	//未查C反应蛋白
	public String getWccfydb() {
		return wccfydb;
	}
	public void setWccfydb(String wccfydb){
		this.wccfydb = wccfydb;
	}
	//白细胞计数(WBC)
	public String getBxbjs() {
		return bxbjs;
	}
	public void setBxbjs(String bxbjs){
		this.bxbjs = bxbjs;
	}
	//未查白细胞计数
	public String getWcbxbjs() {
		return wcbxbjs;
	}
	public void setWcbxbjs(String wcbxbjs){
		this.wcbxbjs = wcbxbjs;
	}
	//中性粒细胞比率
	public String getZxlxbbl() {
		return zxlxbbl;
	}
	public void setZxlxbbl(String zxlxbbl){
		this.zxlxbbl = zxlxbbl;
	}
	//未查中性粒细胞比率
	public String getWczxlxbbl() {
		return wczxlxbbl;
	}
	public void setWczxlxbbl(String wczxlxbbl){
		this.wczxlxbbl = wczxlxbbl;
	}
	//红细胞计数(RBC)
	public String getHxbjs() {
		return hxbjs;
	}
	public void setHxbjs(String hxbjs){
		this.hxbjs = hxbjs;
	}
	//未查红细胞计数
	public String getWchxbjs() {
		return wchxbjs;
	}
	public void setWchxbjs(String wchxbjs){
		this.wchxbjs = wchxbjs;
	}
	//血红蛋白(Hb)
	public String getXhdb() {
		return xhdb;
	}
	public void setXhdb(String xhdb){
		this.xhdb = xhdb;
	}
	//未查血红蛋白
	public String getWcxhdb() {
		return wcxhdb;
	}
	public void setWcxhdb(String wcxhdb){
		this.wcxhdb = wcxhdb;
	}
	//血小板计数(PLT)
	public String getXxbjs() {
		return xxbjs;
	}
	public void setXxbjs(String xxbjs){
		this.xxbjs = xxbjs;
	}
	//未查血小板计数
	public String getWcxxbjs() {
		return wcxxbjs;
	}
	public void setWcxxbjs(String wcxxbjs){
		this.wcxxbjs = wcxxbjs;
	}
	//总胆红素(T-BIL)
	public String getZdhs() {
		return zdhs;
	}
	public void setZdhs(String zdhs){
		this.zdhs = zdhs;
	}
	//未查总胆红素
	public String getWczdhs() {
		return wczdhs;
	}
	public void setWczdhs(String wczdhs){
		this.wczdhs = wczdhs;
	}
	//谷丙转氨酶(ALT)
	public String getGbzam() {
		return gbzam;
	}
	public void setGbzam(String gbzam){
		this.gbzam = gbzam;
	}
	//未查谷丙转氨酶
	public String getWcgbzam() {
		return wcgbzam;
	}
	public void setWcgbzam(String wcgbzam){
		this.wcgbzam = wcgbzam;
	}
	//总蛋白
	public String getZdb() {
		return zdb;
	}
	public void setZdb(String zdb){
		this.zdb = zdb;
	}
	//未查总蛋白
	public String getWczdb() {
		return wczdb;
	}
	public void setWczdb(String wczdb){
		this.wczdb = wczdb;
	}
	//白蛋白(Alb)
	public String getBdb() {
		return bdb;
	}
	public void setBdb(String bdb){
		this.bdb = bdb;
	}
	//未查白蛋白
	public String getWcbdb() {
		return wcbdb;
	}
	public void setWcbdb(String wcbdb){
		this.wcbdb = wcbdb;
	}
	//肌酐(CREA)
	public String getJg() {
		return jg;
	}
	public void setJg(String jg){
		this.jg = jg;
	}
	//未查肌酐
	public String getWcjg() {
		return wcjg;
	}
	public void setWcjg(String wcjg){
		this.wcjg = wcjg;
	}
	//氧分压(PO2)
	public String getYfy() {
		return yfy;
	}
	public void setYfy(String yfy){
		this.yfy = yfy;
	}
	//未查氧分压
	public String getWcyfy() {
		return wcyfy;
	}
	public void setWcyfy(String wcyfy){
		this.wcyfy = wcyfy;
	}
	//吸氧浓度(%)
	public String getXynd() {
		return xynd;
	}
	public void setXynd(String xynd){
		this.xynd = xynd;
	}
	//未查吸氧浓度
	public String getWcxynd() {
		return wcxynd;
	}
	public void setWcxynd(String wcxynd){
		this.wcxynd = wcxynd;
	}
	//乳酸(Lac)
	public String getRs() {
		return rs;
	}
	public void setRs(String rs){
		this.rs = rs;
	}
	//未查乳酸
	public String getWcrs() {
		return wcrs;
	}
	public void setWcrs(String wcrs){
		this.wcrs = wcrs;
	}
	//24小时液体入量
	public String getYtrl() {
		return ytrl;
	}
	public void setYtrl(String ytrl){
		this.ytrl = ytrl;
	}
	//24小时液体出量
	public String getYtcl() {
		return ytcl;
	}
	public void setYtcl(String ytcl){
		this.ytcl = ytcl;
	}
	//24小时尿量
	public String getNl() {
		return nl;
	}
	public void setNl(String nl){
		this.nl = nl;
	}
	//血管活性药物
	public String getXghxyw() {
		return xghxyw;
	}
	public void setXghxyw(String xghxyw){
		this.xghxyw = xghxyw;
	}
	//其他血管活性药物
	public String getQtxghxyw() {
		return qtxghxyw;
	}
	public void setQtxghxyw(String qtxghxyw){
		this.qtxghxyw = qtxghxyw;
	}
	//记录第几天
	public String getJldjt() {
		return jldjt;
	}
	public void setJldjt(String jldjt){
		this.jldjt = jldjt;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
