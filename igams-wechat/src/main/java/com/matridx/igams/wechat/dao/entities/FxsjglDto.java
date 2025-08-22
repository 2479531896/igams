package com.matridx.igams.wechat.dao.entities;

import com.matridx.igams.common.dao.entities.FjcfbDto;
import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="FxsjglDto")
public class FxsjglDto extends FxsjglModel{
	private String fxlbmc;//风险类别名称
	private String ybbh;//样本编号
	private String syglid;//实验管理id
	private String nbbm;//内部编码
	private String tzrqstart;//通知日期开始日期
	private String tzrqend;//通知日期结束日期
	private String qrsjstart;//确认时间开始时间
	private String qrsjend;//确认时间结束时间
	private String yblxmc;//样本类型
	private String jcxmmc;//检测项目名称
	private String hzxm;//患者姓名
	private String entire;//模糊查询
	private String shbj;//审核标记
	private String ddid;//钉钉id
	private String yhm;//用户名
	private String bbclmc;//标本处理名称
	private String[] fxlbids;//风险类别ids
	private String[] bbclids;//标本处理
	private List<String> fjids;//复检
	private List<FjcfbDto> fjcfbDtos;
	private String jcdw;//检测单位
	private String db;
	private String chbj;//撤回标记
	private String wbcxid;

	private String phoneywid;
	private String ywlx;
	private String sjdwmc;//送检单位名称

	public String getSjdwmc() {
		return sjdwmc;
	}

	public void setSjdwmc(String sjdwmc) {
		this.sjdwmc = sjdwmc;
	}

	public String getWbcxid() {
		return wbcxid;
	}

	public void setWbcxid(String wbcxid) {
		this.wbcxid = wbcxid;
	}

	public String getChbj() {
		return chbj;
	}

	public void setChbj(String chbj) {
		this.chbj = chbj;
	}

	public String getDb() {
		return db;
	}

	public void setDb(String db) {
		this.db = db;
	}

	public String getJcdw() {
		return jcdw;
	}

	public void setJcdw(String jcdw) {
		this.jcdw = jcdw;
	}

	public String getYhm() {
		return yhm;
	}

	public void setYhm(String yhm) {
		this.yhm = yhm;
	}

	public String[] getBbclids() {
		return bbclids;
	}

	public void setBbclids(String[] bbclids) {
		this.bbclids = bbclids;
		for(int i=0;i<this.bbclids.length;i++)
		{
			this.bbclids[i] = this.bbclids[i].replace("'", "");
		}

	}
	public String getBbclmc() {
		return bbclmc;
	}

	public void setBbclmc(String bbclmc) {
		this.bbclmc = bbclmc;
	}

	public List<FjcfbDto> getFjcfbDtos() {
		return fjcfbDtos;
	}

	public void setFjcfbDtos(List<FjcfbDto> fjcfbDtos) {
		this.fjcfbDtos = fjcfbDtos;
	}

	public String getSyglid() {
		return syglid;
	}

	public void setSyglid(String syglid) {
		this.syglid = syglid;
	}

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}

	public String getDdid() {
		return ddid;
	}

	public void setDdid(String ddid) {
		this.ddid = ddid;
	}

	public String getShbj() {
		return shbj;
	}

	public void setShbj(String shbj) {
		this.shbj = shbj;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getJcxmmc() {
		return jcxmmc;
	}

	public void setJcxmmc(String jcxmmc) {
		this.jcxmmc = jcxmmc;
	}

	public String getHzxm() {
		return hzxm;
	}

	public void setHzxm(String hzxm) {
		this.hzxm = hzxm;
	}

	public String getYblxmc() {
		return yblxmc;
	}

	public void setYblxmc(String yblxmc) {
		this.yblxmc = yblxmc;
	}

	public String getFxlbmc() {
		return fxlbmc;
	}

	public void setFxlbmc(String fxlbmc) {
		this.fxlbmc = fxlbmc;
	}

	public String getYbbh() {
		return ybbh;
	}

	public void setYbbh(String ybbh) {
		this.ybbh = ybbh;
	}

	public String getNbbm() {
		return nbbm;
	}

	public void setNbbm(String nbbm) {
		this.nbbm = nbbm;
	}

	public String getTzrqstart() {
		return tzrqstart;
	}

	public void setTzrqstart(String tzrqstart) {
		this.tzrqstart = tzrqstart;
	}

	public String getTzrqend() {
		return tzrqend;
	}

	public void setTzrqend(String tzrqend) {
		this.tzrqend = tzrqend;
	}

	public String getQrsjstart() {
		return qrsjstart;
	}

	public void setQrsjstart(String qrsjstart) {
		this.qrsjstart = qrsjstart;
	}

	public String getQrsjend() {
		return qrsjend;
	}

	public void setQrsjend(String qrsjend) {
		this.qrsjend = qrsjend;
	}

	public String[] getFxlbids() {
		return fxlbids;
	}

	public void setFxlbids(String[] fxlbids) {
		this.fxlbids = fxlbids;
		for(int i=0;i<this.fxlbids.length;i++)
		{
			this.fxlbids[i] = this.fxlbids[i].replace("'", "");
		}

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public String getPhoneywid() {
		return phoneywid;
	}

	public void setPhoneywid(String phoneywid) {
		this.phoneywid = phoneywid;
	}
}
