package com.matridx.igams.production.dao.entities;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RdRecordDto extends RdRecordModel {
	
	//时间戳
	private String prefix;
	//收发类型标记
	private String sflxbj;
	//之前的时间
	private String beforTime;
	//当前时间
	private String nowTime;
	//总借出数
	private String sumIQuantity;
	//value
	private String value;
	//复数ID
	private List<String> ids;
		
	public List<String> getIds() {
		return ids;
	}
	public void setIds(String ids) {
		List<String> list;
		String[] str = ids.split(",");
		list = Arrays.asList(str);
		this.ids = list;
	}
	public void setIds(List<String> ids) {
		if(!CollectionUtils.isEmpty(ids)){
            ids.replaceAll(s -> s.replace("[", "").replace("]", "").trim());
		}
		this.ids = ids;
	}
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getSumIQuantity() {
		return sumIQuantity;
	}

	public void setSumIQuantity(String sumIQuantity) {
		this.sumIQuantity = sumIQuantity;
	}

	public String getBeforTime() {
		return beforTime;
	}

	public void setBeforTime(String beforTime) {
		this.beforTime = beforTime;
	}

	public String getNowTime() {
		return nowTime;
	}

	public void setNowTime(String nowTime) {
		this.nowTime = nowTime;
	}

	public String getSflxbj() {
		return sflxbj;
	}

	public void setSflxbj(String sflxbj) {
		this.sflxbj = sflxbj;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
}
