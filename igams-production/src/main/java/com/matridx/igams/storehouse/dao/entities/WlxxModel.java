package com.matridx.igams.storehouse.dao.entities;


import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias(value = "WlxxModel")
public class WlxxModel extends BaseBasicModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String wlxxid;  //物流信息id
    private String ywid;    //业务
    private String wldh;    //物流单号
    private String qsrq;    //签收日期
    private String sfqr;    //是否确认
    private String kdgs;    //快递公司
	private String fhrq; //发货日期
	private String yf; //运费

	public String getYf() {
		return yf;
	}

	public void setYf(String yf) {
		this.yf = yf;
	}

	public String getFhrq() {
		return fhrq;
	}

	public void setFhrq(String fhrq) {
		this.fhrq = fhrq;
	}

	public String getWlxxid() {
		return wlxxid;
	}

	public void setWlxxid(String wlxxid) {
		this.wlxxid = wlxxid;
	}

	public String getYwid() {
		return ywid;
	}

	public void setYwid(String ywid) {
		this.ywid = ywid;
	}

	public String getWldh() {
		return wldh;
	}

	public void setWldh(String wldh) {
		this.wldh = wldh;
	}

	public String getQsrq() {
		return qsrq;
	}

	public void setQsrq(String qsrq) {
		this.qsrq = qsrq;
	}

	public String getSfqr() {
		return sfqr;
	}

	public void setSfqr(String sfqr) {
		this.sfqr = sfqr;
	}

	public String getKdgs() {
		return kdgs;
	}

	public void setKdgs(String kdgs) {
		this.kdgs = kdgs;
	}
}
