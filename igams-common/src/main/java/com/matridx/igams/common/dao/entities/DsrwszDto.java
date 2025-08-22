package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.Arrays;
import java.util.List;

@Alias(value="DsrwszDto")
public class DsrwszDto extends DsrwszModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//复数ID查询ID
	private List<String> rwids;

	//用户ID
	private String yhid;
	private String jsid;
	//豁免ALL类型的标记，如果为1 则 把ALL也检索出来
	private String allflg;

	public List<String> getRwids() {
		return rwids;
	}
	public void setRwids(String rwids) {
		List<String> list;
		String[] str = rwids.split(",");
		list = Arrays.asList(str);
		this.rwids = list;
	}
	public void setRwids(List<String> rwids) {
		if(rwids!=null&&!rwids.isEmpty()){
			rwids.replaceAll(s -> s.replace("[", "").replace("]", "").trim());
		}
		this.rwids = rwids;
	}

	public String getYhid() {
		return yhid;
	}

	public void setYhid(String yhid) {
		this.yhid = yhid;
	}

	public String getJsid() {
		return jsid;
	}

	public void setJsid(String jsid) {
		this.jsid = jsid;
	}

	public String getAllflg() {
		return allflg;
	}

	public void setAllflg(String allflg) {
		this.allflg = allflg;
	}
}
