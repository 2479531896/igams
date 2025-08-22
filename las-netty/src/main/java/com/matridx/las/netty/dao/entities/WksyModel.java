package com.matridx.las.netty.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="WksyModel")
public class WksyModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
		//第几次报告
		private String djcsy;
		//文库id
		private String wkid;
		//类型
		private String ispcr;
		//定量仪实验id
		private String wksyid;
		//接收时间
		private String jssj;
		//上报时间
		private String sbsj;
		//pzy仪器id
		private String pzyqid;
		//实验室id
		private String sysid;
		//变性液
		private String bxy;
		//平衡混合液
		private String phhhy;
		//杂交缓冲液体积
		private String zjhcytj;
		public String getDjcsy() {
			return djcsy;
		}
		public void setDjcsy(String djcsy) {
			this.djcsy = djcsy;
		}
		public String getWkid() {
			return wkid;
		}
		public void setWkid(String wkid) {
			this.wkid = wkid;
		}
		public String getIspcr() {
			return ispcr;
		}
		public void setIspcr(String ispcr) {
			this.ispcr = ispcr;
		}
		
		public String getWksyid() {
			return wksyid;
		}
		public void setWksyid(String wksyid) {
			this.wksyid = wksyid;
		}
		public String getJssj() {
			return jssj;
		}
		public void setJssj(String jssj) {
			this.jssj = jssj;
		}
		public String getSbsj() {
			return sbsj;
		}
		public void setSbsj(String sbsj) {
			this.sbsj = sbsj;
		}
		public String getPzyqid() {
			return pzyqid;
		}
		public void setPzyqid(String pzyqid) {
			this.pzyqid = pzyqid;
		}
		public String getSysid() {
			return sysid;
		}
		public void setSysid(String sysid) {
			this.sysid = sysid;
		}
		public String getBxy() {
			return bxy;
		}
		public void setBxy(String bxy) {
			this.bxy = bxy;
		}
		public String getPhhhy() {
			return phhhy;
		}
		public void setPhhhy(String phhhy) {
			this.phhhy = phhhy;
		}
		public String getZjhcytj() {
			return zjhcytj;
		}
		public void setZjhcytj(String zjhcytj) {
			this.zjhcytj = zjhcytj;
		}
		
	
}
