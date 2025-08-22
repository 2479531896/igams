package com.matridx.las.netty.dao.entities;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value = "DesktopMaterialModel")
public class DesktopMaterialModel extends BaseModel {
	// 物料区待运行托盘
	private List<TrayModel> waitTrayList = new ArrayList<TrayModel>();

	public List<TrayModel> getWaitTrayList() {
		return waitTrayList;
	}

	public void setWaitTrayList(List<TrayModel> waitTrayList) {
		this.waitTrayList = waitTrayList;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
