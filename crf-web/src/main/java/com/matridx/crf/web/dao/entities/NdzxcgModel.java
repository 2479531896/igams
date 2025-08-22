package com.matridx.crf.web.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="NdzxcgModel")
public class NdzxcgModel extends BaseModel{
	//null
		private String ndzjlid;
		//检验项目关联基础数据id
		private String jyxm;
		//数据值
		private String sjz;
		//null
		public String getNdzjlid() {
			return ndzjlid;
		}
		public void setNdzjlid(String ndzjlid){
			this.ndzjlid = ndzjlid;
		}
		//检验项目关联基础数据id
		public String getJyxm() {
			return jyxm;
		}
		public void setJyxm(String jyxm){
			this.jyxm = jyxm;
		}
		//数据值
		public String getSjz() {
			return sjz;
		}
		public void setSjz(String sjz){
			this.sjz = sjz;
		}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
