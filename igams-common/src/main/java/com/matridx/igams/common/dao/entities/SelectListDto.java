package com.matridx.igams.common.dao.entities;

import com.matridx.igams.common.dao.BaseModel;

/**
 * 下拉框公有MODEL
 * @author lwj
 *
 */
public class SelectListDto extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2530365055927359233L;

	private String list_id;//下拉框值
	private String list_name;//下拉框显示内容
	private String list_extend;	//扩展属性
	
	public String getList_id() {
		return list_id;
	}

	public void setList_id(String listId) {
		list_id = listId;
	}

	public String getList_name() {
		return list_name;
	}

	public void setList_name(String list_name) {
		this.list_name = list_name;
	}

	public void setList_extend(String list_extend) {
		this.list_extend = list_extend;
	}

	public String getList_extend() {
		return list_extend;
	}

}
