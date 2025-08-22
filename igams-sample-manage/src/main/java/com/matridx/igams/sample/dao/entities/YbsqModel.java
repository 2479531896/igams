package com.matridx.igams.sample.dao.entities;

import org.apache.ibatis.type.Alias;
import com.matridx.igams.common.dao.entities.BaseBasicModel;

@Alias(value="YbsqModel")
public class YbsqModel extends BaseBasicModel{
	//申请id
	private String sqid;
	//标本id
	private String ybid;
	//申请数
	private String sqs;
	//用途
	private String yt;
	//申请人员
	private String sqry;
	//申请时间
	private String sqsj;
	//状态   00:未提交 10:审核中  15:审核未通过  80:审核通过
	private String zt;
	//审核时间
	private String shsj;
	//盒子ID
	private String hzid;
	//起始位置
	private String qswz;
	//结束位置
	private String jswz;
	//备注
	private String bz;
	//申请id
	public String getSqid() {
		return sqid;
	}
	public void setSqid(String sqid){
		this.sqid = sqid;
	}
	//标本id
	public String getYbid() {
		return ybid;
	}
	public void setYbid(String ybid){
		this.ybid = ybid;
	}
	//申请数
	public String getSqs() {
		return sqs;
	}
	public void setSqs(String sqs){
		this.sqs = sqs;
	}
	//用途
	public String getYt() {
		return yt;
	}
	public void setYt(String yt){
		this.yt = yt;
	}
	//申请人员
	public String getSqry() {
		return sqry;
	}
	public void setSqry(String sqry){
		this.sqry = sqry;
	}
	//申请时间
	public String getSqsj() {
		return sqsj;
	}
	public void setSqsj(String sqsj){
		this.sqsj = sqsj;
	}
	//状态   00:未提交 10:审核中  15:审核未通过  80:审核通过
	public String getZt() {
		return zt;
	}
	public void setZt(String zt){
		this.zt = zt;
	}
	//审核时间
	public String getShsj() {
		return shsj;
	}
	public void setShsj(String shsj){
		this.shsj = shsj;
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

	//备注
	public String getBz() {
		return bz;
	}
	public void setBz(String bz){
		this.bz = bz;
	}

	public String getHzid() {
		return hzid;
	}
	public void setHzid(String hzid) {
		this.hzid = hzid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
