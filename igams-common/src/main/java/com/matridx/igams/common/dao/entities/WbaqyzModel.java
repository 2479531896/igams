package com.matridx.igams.common.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="WbaqyzModel")
public class WbaqyzModel extends BaseModel {
	//代号
	private String code;
	//名称
	private String mc;
	//秘钥
	private String key;
	//关键词
	private String word;
	//调用地址
	private String address;
	//是否外部编码必须
	private String iswbbmmust;

	//验证时间
	private String yzsj;

	//验证次数
	private String yzcs;

	public String getIswbbmmust() {
		return iswbbmmust;
	}

	public void setIswbbmmust(String iswbbmmust) {
		this.iswbbmmust = iswbbmmust;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	//代号
	public String getCode() {
		return code;
	}
	public void setCode(String code){
		this.code = code;
	}
	//名称
	public String getMc() {
		return mc;
	}
	public void setMc(String mc){
		this.mc = mc;
	}
	//秘钥
	public String getKey() {
		return key;
	}
	public void setKey(String key){
		this.key = key;
	}
	//关键词
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getYzsj() {
		return yzsj;
	}

	public void setYzsj(String yzsj) {
		this.yzsj = yzsj;
	}

	public String getYzcs() {
		return yzcs;
	}

	public void setYzcs(String yzcs) {
		this.yzcs = yzcs;
	}
}
