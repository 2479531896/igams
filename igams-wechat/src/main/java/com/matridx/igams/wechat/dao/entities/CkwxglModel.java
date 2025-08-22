package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="CkwxglModel")
public class CkwxglModel extends BaseModel{
	//文献ID  pmid
	private String wxid;
	//物种id   species_taxid
	private String wzid;
	//标本类型  sample_type
	private String yblx;
	//文章标题  title
	private String wzbt;
	//文章作者  author
	private String wzzz;
	//期刊信息 journal
	private String qkxx;
	//默认选择   default_selected 0:否  1：是
	private String mrxz;
	//文献ID  pmid
	public String getWxid() {
		return wxid;
	}
	public void setWxid(String wxid){
		this.wxid = wxid;
	}
	//物种id   species_taxid
	public String getWzid() {
		return wzid;
	}
	public void setWzid(String wzid){
		this.wzid = wzid;
	}
	//标本类型  sample_type
	public String getYblx() {
		return yblx;
	}
	public void setYblx(String yblx){
		this.yblx = yblx;
	}
	//文章标题  title
	public String getWzbt() {
		return wzbt;
	}
	public void setWzbt(String wzbt){
		this.wzbt = wzbt;
	}
	//文章作者  author
	public String getWzzz() {
		return wzzz;
	}
	public void setWzzz(String wzzz){
		this.wzzz = wzzz;
	}
	//期刊信息 journal
	public String getQkxx() {
		return qkxx;
	}
	public void setQkxx(String qkxx){
		this.qkxx = qkxx;
	}
	//默认选择   default_selected 0:否  1：是
	public String getMrxz() {
		return mrxz;
	}
	public void setMrxz(String mrxz){
		this.mrxz = mrxz;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
