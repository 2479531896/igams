package com.matridx.las.netty.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="YsybxxDto")
public class YsybxxDto extends YsybxxModel{
	//执行时，将托盘号传给后端
	private String trays;
	private String userId;
	//样本状态  0:待放置   1:已放置，待运行
	private String ybzt;
	//样本在载具中的x左边
	private String x;
	//样本在载具中的y左边
	private String y;

	public String getYbzt() {
		return ybzt;
	}

	public void setYbzt(String ybzt) {
		this.ybzt = ybzt;
	}

	public String getTrays() {
		return trays;
	}

	public void setTrays(String trays) {
		this.trays = trays;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
