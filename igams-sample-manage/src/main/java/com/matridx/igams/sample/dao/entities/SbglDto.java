package com.matridx.igams.sample.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="SbglDto")
public class SbglDto extends SbglModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//标本类型代码
	public String yblxdm;
	//标本类型名称
	public String yblxmc;
	//排序用路径
	public String path;
	//深度
	public String depth;

	//标本ID，用于存放修改的标本ID信息
	public String ybid;
	//标本ID，用于存放修改的标本所在位置
	public String cfqswz;
	//标本ID，用于存放修改的标本所在位置
	public String cfjswz;
	//盒子ID
	public String hzid;
	//抽屉ID
	public String ctid;
	//冰箱ID
	public String bxid;
	//是否为企参盘  1代表是，否则为否
	public String sfqcp;
	//录入人员名称
	public String lrrymc;
	//用于设备类型检索
	private String[] sblxs;
	//用于标本类型检索
	private String[] yblxs;
	//录入时间开始
	private String lrsjstart;
	//录入时间结束
	private String lrsjend;

	//父ids
	private String[] fids;
	//冰箱号
	private String bxh;
	// 抽屉号
	private String cth;
	// 盒子号
	private String hh;
	//对应样本库存盒子id
	private String chtid;
	//样本库存id
	private String ybkcid;
	//ycfs加减标记
	private String ycfsbj;
	//存储单位名称
	private String jcdwmc;
	//存储单位ids
	private String[] jcdws;

	//存放数增加
	private String addcfs;
	//存放数减少
	private String subcfs;
	//是否存在调拨
	private String sfczdb;
	//ybdbid
	private String ybdbid;
	private String bxJcdw;

	public String getBxJcdw() {
		return bxJcdw;
	}

	public void setBxJcdw(String bxJcdw) {
		this.bxJcdw = bxJcdw;
	}

	public String getYbdbid() {
		return ybdbid;
	}

	public void setYbdbid(String ybdbid) {
		this.ybdbid = ybdbid;
	}

	public String getSfczdb() {
		return sfczdb;
	}

	public void setSfczdb(String sfczdb) {
		this.sfczdb = sfczdb;
	}

	public String getAddcfs() {
		return addcfs;
	}

	public void setAddcfs(String addcfs) {
		this.addcfs = addcfs;
	}

	public String getSubcfs() {
		return subcfs;
	}

	public void setSubcfs(String subcfs) {
		this.subcfs = subcfs;
	}

	public String getJcdwmc() {
		return jcdwmc;
	}

	public String[] getJcdws() {
		return jcdws;
	}

	public void setJcdws(String[] jcdws) {
		this.jcdws = jcdws;
	}

	public void setJcdwmc(String jcdwmc) {
		this.jcdwmc = jcdwmc;
	}

	public String getYcfsbj() {
		return ycfsbj;
	}

	public void setYcfsbj(String ycfsbj) {
		this.ycfsbj = ycfsbj;
	}

	public String getYbkcid() {
		return ybkcid;
	}

	public void setYbkcid(String ybkcid) {
		this.ybkcid = ybkcid;
	}

	public String getBxh() {
		return bxh;
	}

	public void setBxh(String bxh) {
		this.bxh = bxh;
	}

	public String getCth() {
		return cth;
	}

	public void setCth(String cth) {
		this.cth = cth;
	}

	public String getHh() {
		return hh;
	}

	public void setHh(String hh) {
		this.hh = hh;
	}

	public String getChtid() {
		return chtid;
	}

	public void setChtid(String chtid) {
		this.chtid = chtid;
	}

	public String[] getFids() {
		return fids;
	}

	public void setFids(String[] fids) {
		this.fids = fids;
		for (int i = 0; i < fids.length; i++){
			this.fids[i]=this.fids[i].replace("'","");
		}
	}
	
	public String getLrsjstart() {
		return lrsjstart;
	}
	public void setLrsjstart(String lrsjstart) {
		this.lrsjstart = lrsjstart;
	}
	public String getLrsjend() {
		return lrsjend;
	}
	public void setLrsjend(String lrsjend) {
		this.lrsjend = lrsjend;
	}
	public String[] getSblxs() {
		return sblxs;
	}
	public void setSblxs(String[] sblxs) {
		this.sblxs = sblxs;
		for (int i = 0; i < sblxs.length; i++){
			this.sblxs[i]=this.sblxs[i].replace("'","");
		}
	}
	public String[] getYblxs() {
		return yblxs;
	}
	public void setYblxs(String[] yblxs) {
		this.yblxs = yblxs;
		for (int i = 0; i < yblxs.length; i++){
			this.yblxs[i]=this.yblxs[i].replace("'","");
		}
	}
	public String getLrrymc() {
		return lrrymc;
	}
	public void setLrrymc(String lrrymc) {
		this.lrrymc = lrrymc;
	}
	public String getSfqcp() {
		return sfqcp;
	}
	public void setSfqcp(String sfqcp) {
		this.sfqcp = sfqcp;
	}
	public String getHzid() {
		return hzid;
	}
	public void setHzid(String hzid) {
		this.hzid = hzid;
	}
	public String getCtid() {
		return ctid;
	}
	public void setCtid(String ctid) {
		this.ctid = ctid;
	}
	public String getBxid() {
		return bxid;
	}
	public void setBxid(String bxid) {
		this.bxid = bxid;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getDepth() {
		return depth;
	}
	public void setDepth(String depth) {
		this.depth = depth;
	}
	public String getYblxdm() {
		return yblxdm;
	}
	public void setYblxdm(String yblxdm) {
		this.yblxdm = yblxdm;
	}
	public String getYblxmc() {
		return yblxmc;
	}
	public void setYblxmc(String yblxmc) {
		this.yblxmc = yblxmc;
	}
	public String getYbid() {
		return ybid;
	}
	public void setYbid(String ybid) {
		this.ybid = ybid;
	}
	public String getCfqswz() {
		return cfqswz;
	}
	public void setCfqswz(String cfqswz) {
		this.cfqswz = cfqswz;
	}
	public String getCfjswz() {
		return cfjswz;
	}
	public void setCfjswz(String cfjswz) {
		this.cfjswz = cfjswz;
	}

	public SbglDto() {
	}

	public SbglDto(String sbid, String addcfs, String subcfs) {
		super(sbid);
		this.addcfs = addcfs;
		this.subcfs = subcfs;
	}
}
