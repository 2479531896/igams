package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.Arrays;
import java.util.List;

@Alias(value="DlyzzsDto")
public class DlyzzsDto extends DlyzzsModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//名称s
	private List<String> names;

	public List<String> getNames() {
		return names;
	}

	public void setNames(String names) {
		String str[] = names.split(",");
        this.names = Arrays.asList(str);
	}
	public void setNames(List<String> names) {
		if(names!=null && names.size() > 0){
            names.replaceAll(s -> s.replace("[", "").replace("]", "").trim());
		}
		this.names = names;
	}
}
