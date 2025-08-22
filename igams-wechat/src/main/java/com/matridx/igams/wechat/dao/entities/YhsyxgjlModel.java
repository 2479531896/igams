package com.matridx.igams.wechat.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="YhsyxgjlModel")
public class YhsyxgjlModel extends BaseModel{

	//使用习惯记录ID
	private String syxgjlid;
	//用户ID
	private String yhid;
	//业务类型
	private String ywlx;
	//字段名1
	private String zdm1;
	//字段值1
	private String zdz1;
	//字段名2
	private String zdm2;
	//字段值2
	private String zdz2;
	//字段名3
	private String zdm3;
	//字段值3
	private String zdz3;
	//字段名4
	private String zdm4;
	//字段值4
	private String zdz4;
	//字段名5
	private String zdm5;
	//字段值5
	private String zdz5;
	//字段名6
	private String zdm6;
	//字段值6
	private String zdz6;

	public String getSyxgjlid() {
		return syxgjlid;
	}

	public void setSyxgjlid(String syxgjlid) {
		this.syxgjlid = syxgjlid;
	}

	public String getYhid() {
		return yhid;
	}

	public void setYhid(String yhid) {
		this.yhid = yhid;
	}

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public String getZdm1() {
		return zdm1;
	}

	public void setZdm1(String zdm1) {
		this.zdm1 = zdm1;
	}

	public String getZdz1() {
		return zdz1;
	}

	public void setZdz1(String zdz1) {
		this.zdz1 = zdz1;
	}

	public String getZdm2() {
		return zdm2;
	}

	public void setZdm2(String zdm2) {
		this.zdm2 = zdm2;
	}

	public String getZdz2() {
		return zdz2;
	}

	public void setZdz2(String zdz2) {
		this.zdz2 = zdz2;
	}

	public String getZdm3() {
		return zdm3;
	}

	public void setZdm3(String zdm3) {
		this.zdm3 = zdm3;
	}

	public String getZdz3() {
		return zdz3;
	}

	public void setZdz3(String zdz3) {
		this.zdz3 = zdz3;
	}

	public String getZdm4() {
		return zdm4;
	}

	public void setZdm4(String zdm4) {
		this.zdm4 = zdm4;
	}

	public String getZdz4() {
		return zdz4;
	}

	public void setZdz4(String zdz4) {
		this.zdz4 = zdz4;
	}

	public String getZdm5() {
		return zdm5;
	}

	public void setZdm5(String zdm5) {
		this.zdm5 = zdm5;
	}

	public String getZdz5() {
		return zdz5;
	}

	public void setZdz5(String zdz5) {
		this.zdz5 = zdz5;
	}

	public String getZdm6() {
		return zdm6;
	}

	public void setZdm6(String zdm6) {
		this.zdm6 = zdm6;
	}

	public String getZdz6() {
		return zdz6;
	}

	public void setZdz6(String zdz6) {
		this.zdz6 = zdz6;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
