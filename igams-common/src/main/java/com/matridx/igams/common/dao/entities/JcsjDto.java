package com.matridx.igams.common.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="JcsjDto")
public class JcsjDto extends JcsjModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String jclbmc;	//基础类别名称
	private String kz1mc;	//扩展参数1名称
	private String kz2mc;	//扩展参数2名称
	private String kz3mc;	//扩展参数3名称
	private String kz4mc;	//扩展参数4名称
	private String fjclb;	//父参数类别
	private String[] fcsids;	//父参数类别
	private String fcsdm;	//父参数代码
	private String fcsmc;	//父参数名称
	private String ffcsmc;	//父参数名称
	private String sfmrmc;	//是否默认名称
	private String sfgbmc;	//是否广播名称
	private String checked;	//是否选中(助手小程序)
	private List<String> fjids; //附件id
	private List<String> fjids_D; //附件id（DNA）
	private List<String> fjids_R; //附件id（RNA）
	private List<String> fjids_C; //附件id（DNA+RNA）
	private String ywlx_D;  //业务类型（DNA）
	private String ywlx_R;  //业务类型（RNA）
	private String ywlx_C;  //业务类型（CNA）
	private String[] csdms; //参数代码[多]
	private List<String> jclbs; //基础类别[多]
	private String yzjgjl;
	private String fcskz2;
	private String fcskz1;
	private String yzid;
	private String min_cel;//最小行
	private String max_cel;//最大行
	private String min_row;//最小列
	private String max_row;//最大列
	private String defaultCsid;//默认的参数id（判断是否有除自己外的是否默认数据用）
	private String[] scbjs;//删除标记多
	private List<String> fcsidList; //检测子项目id
	private String newSign;
	private String likecsdm;//模糊查询参数代码

	public String getLikecsdm() {
		return likecsdm;
	}

	public void setLikecsdm(String likecsdm) {
		this.likecsdm = likecsdm;
	}

	public List<String> getFcsidList() {
		return fcsidList;
	}

	public void setFcsidList(List<String> fcsidList) {
		this.fcsidList = fcsidList;
	}

	public String getFcskz2() {
		return fcskz2;
	}

	public void setFcskz2(String fcskz2) {
		this.fcskz2 = fcskz2;
	}

	public String getFcskz1() {
		return fcskz1;
	}

	public void setFcskz1(String fcskz1) {
		this.fcskz1 = fcskz1;
	}

	public String getMin_cel() {
		return min_cel;
	}

	public void setMin_cel(String min_cel) {
		this.min_cel = min_cel;
	}

	public String getMax_cel() {
		return max_cel;
	}

	public void setMax_cel(String max_cel) {
		this.max_cel = max_cel;
	}

	public String getMin_row() {
		return min_row;
	}

	public void setMin_row(String min_row) {
		this.min_row = min_row;
	}

	public String getMax_row() {
		return max_row;
	}

	public void setMax_row(String max_row) {
		this.max_row = max_row;
	}

	public String[] getScbjs() {
		return scbjs;
	}

	public void setScbjs(String[] scbjs) {
		this.scbjs = scbjs;
	}

	public String getYzid() {
		return yzid;
	}

	public void setYzid(String yzid) {
		this.yzid = yzid;
	}

	public String getYzjgjl() {
		return yzjgjl;
	}

	public void setYzjgjl(String yzjgjl) {
		this.yzjgjl = yzjgjl;
	}

	public String getSfgbmc() {
		return sfgbmc;
	}
	public void setSfgbmc(String sfgbmc) {
		this.sfgbmc = sfgbmc;
	}
	public String getChecked() {
		return checked;
	}
	public void setChecked(String checked) {
		this.checked = checked;
	}
	public String getJclbmc() {
		return jclbmc;
	}
	public void setJclbmc(String jclbmc) {
		this.jclbmc = jclbmc;
	}
	public String getKz1mc() {
		return kz1mc;
	}
	public void setKz1mc(String kz1mc) {
		this.kz1mc = kz1mc;
	}
	public String getKz2mc() {
		return kz2mc;
	}
	public void setKz2mc(String kz2mc) {
		this.kz2mc = kz2mc;
	}
	public String getKz3mc() {
		return kz3mc;
	}
	public void setKz3mc(String kz3mc) {
		this.kz3mc = kz3mc;
	}
	public String getKz4mc() {
		return kz4mc;
	}
	public void setKz4mc(String kz4mc) {
		this.kz4mc = kz4mc;
	}
	public String getFcsmc() {
		return fcsmc;
	}
	public void setFcsmc(String fcsmc) {
		this.fcsmc = fcsmc;
	}
	public String getFfcsmc() {
		return ffcsmc;
	}
	public void setFfcsmc(String ffcsmc) {
		this.ffcsmc = ffcsmc;
	}
	public String getSfmrmc() {
		return sfmrmc;
	}
	public void setSfmrmc(String sfmrmc) {
		this.sfmrmc = sfmrmc;
	}
	public String getFjclb() {
		return fjclb;
	}
	public void setFjclb(String fjclb) {
		this.fjclb = fjclb;
	}
	public String getFcsdm() {
		return fcsdm;
	}
	public void setFcsdm(String fcsdm) {
		this.fcsdm = fcsdm;
	}
	public String[] getFcsids() {
		return fcsids;
	}
	public void setFcsids(String[] fcsids) {
		this.fcsids = fcsids;
		for(int i=0;i<this.fcsids.length;i++)
		{
			this.fcsids[i] = this.fcsids[i].replace("'", "");
		}
	}
	
	public String[] getCsdms() {
		return csdms;
	}
	public void setCsdms(String[] csdms) {
		this.csdms = csdms;
		for(int i=0;i<this.csdms.length;i++)
		{
			this.csdms[i] = this.csdms[i].replace("'", "");
		}
	}
	public List<String> getFjids_D() {
		return fjids_D;
	}
	public void setFjids_D(List<String> fjids_D) {
		this.fjids_D = fjids_D;
	}
	public List<String> getFjids_R() {
		return fjids_R;
	}
	public void setFjids_R(List<String> fjids_R) {
		this.fjids_R = fjids_R;
	}
	public List<String> getFjids_C() {
		return fjids_C;
	}
	public void setFjids_C(List<String> fjids_C) {
		this.fjids_C = fjids_C;
	}
	public String getYwlx_D() {
		return ywlx_D;
	}
	public void setYwlx_D(String ywlx_D) {
		this.ywlx_D = ywlx_D;
	}
	public String getYwlx_R() {
		return ywlx_R;
	}
	public void setYwlx_R(String ywlx_R) {
		this.ywlx_R = ywlx_R;
	}
	public String getYwlx_C() {
		return ywlx_C;
	}
	public void setYwlx_C(String ywlx_C) {
		this.ywlx_C = ywlx_C;
	}
	public List<String> getFjids() {
		return fjids;
	}
	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}
	public List<String> getJclbs() {
		return jclbs;
	}
	public void setJclbs(List<String> jclbs) {
		this.jclbs = jclbs;
	}
	public String getDefaultCsid() {
		return defaultCsid;
	}
	public void setDefaultCsid(String defaultCsid) {
		this.defaultCsid = defaultCsid;
	}

    public void setNewSign(String newSign) {
        this.newSign = newSign;
    }

    public String getNewSign() {
        return newSign;
    }
}
