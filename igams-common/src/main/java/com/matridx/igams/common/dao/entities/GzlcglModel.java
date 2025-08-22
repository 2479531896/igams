package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="GzlcglModel")
public class GzlcglModel extends BaseModel{
	//流程ID
	private String lcid;
	//工作ID
	private String gzid;
	//序号
	private String xh;
	//申请人
	private String sqr;
	//申请时间
	private String sqsj;
	//是否通过
	private String sftg;
	//审核时间
	private String shsj;
	//审核意见
	private String shyj;
	//申诉意见
	private String ssyj;
	//流程ID
	public String getLcid() {
		return lcid;
	}
	public void setLcid(String lcid){
		this.lcid = lcid;
	}
	//工作ID
	public String getGzid() {
		return gzid;
	}
	public void setGzid(String gzid){
		this.gzid = gzid;
	}
	//序号
	public String getXh() {
		return xh;
	}
	public void setXh(String xh){
		this.xh = xh;
	}
	//申请人
	public String getSqr() {
		return sqr;
	}
	public void setSqr(String sqr){
		this.sqr = sqr;
	}
	//申请时间
	public String getSqsj() {
		return sqsj;
	}
	public void setSqsj(String sqsj){
		this.sqsj = sqsj;
	}
	//是否通过
	public String getSftg() {
		return sftg;
	}
	public void setSftg(String sftg){
		this.sftg = sftg;
	}
	//审核时间
	public String getShsj() {
		return shsj;
	}
	public void setShsj(String shsj){
		this.shsj = shsj;
	}
	//审核意见
	public String getShyj() {
		return shyj;
	}
	public void setShyj(String shyj){
		this.shyj = shyj;
	}
	//申诉意见
	public String getSsyj() {
		return ssyj;
	}
	public void setSsyj(String ssyj){
		this.ssyj = ssyj;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
