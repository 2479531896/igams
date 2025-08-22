package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="WxyhDto")
public class WxyhDto extends WxyhModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		//标签名
		private String bqm;
		//标签id
		private String bqid;
		//患者姓名
		private String hzxm;
		
		//判断条件
		private String tj_flag;
		//签名加密验证
		private String sign;
		//关注平台flg（0:杰毅检验，1:杰毅生物）
		private String gzptflg;
		//关注平台名称
		private String gzptmc;
		//外部程序代码
		private String wbcxdm;
		//外部程序ID
		private String wbcxid;
		
		public String getWbcxid() {
			return wbcxid;
		}
		public void setWbcxid(String wbcxid) {
			this.wbcxid = wbcxid;
		}
		public String getWbcxdm() {
			return wbcxdm;
		}
		public void setWbcxdm(String wbcxdm) {
			this.wbcxdm = wbcxdm;
		}
		public String getGzptmc() {
			return gzptmc;
		}
		public void setGzptmc(String gzptmc) {
			this.gzptmc = gzptmc;
		}
		public String getGzptflg() {
			return gzptflg;
		}
		public void setGzptflg(String gzptflg) {
			this.gzptflg = gzptflg;
		}
		public String getTj_flag(){
			return tj_flag;
		}
		public void setTj_flag(String tj_flag)
		{
			this.tj_flag = tj_flag;
		}
		public String getHzxm()
		{
			return hzxm;
		}
		public void setHzxm(String hzxm)
		{
			this.hzxm = hzxm;
		}
		public String getBqm() {
			return bqm;
		}
		public void setBqm(String bqm) {
			this.bqm = bqm;
		}
		public String getBqid() {
			return bqid;
		}
		public void setBqid(String bqid) {
			this.bqid = bqid;
		}
		public String getSign() {
			return sign;
		}
		public void setSign(String sign) {
			this.sign = sign;
		}
		
}
