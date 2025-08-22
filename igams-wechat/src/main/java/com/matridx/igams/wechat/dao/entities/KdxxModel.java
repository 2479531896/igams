package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="KdxxModel")
public class KdxxModel extends BaseModel{
	//快递主键ID
    private String kdid;
	//快递单号
	private String mailno;
	//接收地址
	private String acceptaddress;
	//接收时间
	private String accepttime;
	//备注
	private String remark;
	//路由状态码
	private String opcode;
	//路由节点排序
	private String px;
	//京东快递派件人
	private String courier;
	//京东快递派件人电话号码
	private String couriertel;

	public String getCouriertel() {
		return couriertel;
	}

	public void setCouriertel(String couriertel) {
		this.couriertel = couriertel;
	}

	public String getCourier() {
		return courier;
	}

	public void setCourier(String courier) {
		this.courier = courier;
	}

	public String getKdid() {
		return kdid;
	}

	public void setKdid(String kdid) {
		this.kdid = kdid;
	}

	public String getMailno() {
		return mailno;
	}

	public void setMailno(String mailno) {
		this.mailno = mailno;
	}

	public String getAcceptaddress() {
		return acceptaddress;
	}

	public void setAcceptaddress(String acceptaddress) {
		this.acceptaddress = acceptaddress;
	}

	public String getAccepttime() {
		return accepttime;
	}

	public void setAccepttime(String accepttime) {
		this.accepttime = accepttime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOpcode() {
		return opcode;
	}

	public void setOpcode(String opcode) {
		this.opcode = opcode;
	}

	public String getPx() {
		return px;
	}

	public void setPx(String px) {
		this.px = px;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
