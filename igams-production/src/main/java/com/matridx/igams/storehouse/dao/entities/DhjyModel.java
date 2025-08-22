package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.entities.BaseBasicModel;

@Alias(value="DhjyModel")
public class DhjyModel extends BaseBasicModel{
	//到货检验ID
	private String dhjyid;
	//检验单号
	private String jydh;
	//检验日期
	private String jyrq;
	//检验负责人
	private String jyfzr;
	//备注
	private String bz;
	//状态 00：未提交  10：审核中
	private String zt;
	//订单完成标记 0:未完成  1：完成 全部已处理
	private String wcbj;
	//检验结果
	private String jyjg;
	//是否废弃领料单
	private String sffqlld;
	//是否留样出库
	private String sflyck;
	//审核时间
	private String shsj;
	//检验工时
	private String jygs;
	//不合格原因
	private String bhgyy;

	public String getBhgyy() {
		return bhgyy;
	}

	public void setBhgyy(String bhgyy) {
		this.bhgyy = bhgyy;
	}

	public String getJygs() {
		return jygs;
	}

	public void setJygs(String jygs) {
		this.jygs = jygs;
	}

	public String getShsj() {
		return shsj;
	}

	public void setShsj(String shsj) {
		this.shsj = shsj;
	}

	public String getSflyck() {
		return sflyck;
	}

	public void setSflyck(String sflyck) {
		this.sflyck = sflyck;
	}

	public String getSffqlld() {
		return sffqlld;
	}
	public void setSffqlld(String sffqlld) {
		this.sffqlld = sffqlld;
	}
	public String getJyjg() {
		return jyjg;
	}
	public void setJyjg(String jyjg) {
		this.jyjg = jyjg;
	}
	//到货检验ID
	public String getDhjyid() {
		return dhjyid;
	}
	public void setDhjyid(String dhjyid){
		this.dhjyid = dhjyid;
	}
	//检验单号
	public String getJydh() {
		return jydh;
	}
	public void setJydh(String jydh){
		this.jydh = jydh;
	}
	//检验日期
	public String getJyrq() {
		return jyrq;
	}
	public void setJyrq(String jyrq){
		this.jyrq = jyrq;
	}
	//检验负责人
	public String getJyfzr() {
		return jyfzr;
	}
	public void setJyfzr(String jyfzr){
		this.jyfzr = jyfzr;
	}
	//备注
	public String getBz() {
		return bz;
	}
	public void setBz(String bz){
		this.bz = bz;
	}
	//状态 00：未提交  10：审核中
	public String getZt() {
		return zt;
	}
	public void setZt(String zt){
		this.zt = zt;
	}
	//订单完成标记 0:未完成  1：完成 全部已处理
	public String getWcbj() {
		return wcbj;
	}
	public void setWcbj(String wcbj){
		this.wcbj = wcbj;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
