package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="SjkdxxModel")
public class SjkdxxModel extends BaseModel{
	//送检快递ID
    private String sjkdid;
	//快递单号
	private String mailno;
	//业务ID
	private String ywid;
	//标本编号
	private String ybbh;
	//快递发出时间
	private String starttime;
	//快递签收时间
	private String endtime;
	//快递状态    0运送中     1已签收      2不存在
	private String kdzt;
	//业务类型，ExpressTypeEnum枚举类
	private String ywlx;
	//快递类型，基础数据维护，eg：京东、顺丰等
	private String kdlx;
	//商家快递单号
	private String sjmailno;
	//快递订单是否取消
	private String sfqx;
	//京东类型
	private String jdlx;

	public String getJdlx() {
		return jdlx;
	}

	public void setJdlx(String jdlx) {
		this.jdlx = jdlx;
	}
	public String getSjmailno() {
		return sjmailno;
	}

	public void setSjmailno(String sjmailno) {
		this.sjmailno = sjmailno;
	}
	
	public String getSfqx() {
		return sfqx;
	}

	public void setSfqx(String sfqx) {
		this.sfqx = sfqx;
	}


	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public String getKdlx() {
		return kdlx;
	}

	public void setKdlx(String kdlx) {
		this.kdlx = kdlx;
	}

	public String getSjkdid() {
		return sjkdid;
	}

	public void setSjkdid(String sjkdid) {
		this.sjkdid = sjkdid;
	}

	public String getMailno() {
		return mailno;
	}

	public void setMailno(String mailno) {
		this.mailno = mailno;
	}

	public String getYwid() {
		return ywid;
	}

	public void setYwid(String ywid) {
		this.ywid = ywid;
	}

	public String getYbbh() {
		return ybbh;
	}

	public void setYbbh(String ybbh) {
		this.ybbh = ybbh;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getKdzt() {
		return kdzt;
	}

	public void setKdzt(String kdzt) {
		this.kdzt = kdzt;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
