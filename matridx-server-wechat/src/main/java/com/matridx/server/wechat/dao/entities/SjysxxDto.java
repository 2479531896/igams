package com.matridx.server.wechat.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="SjysxxDto")
public class SjysxxDto extends SjysxxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    //扩展参数哪
    private String kzcs;
    //其他科室
    private String qtks;
    //是否收起判断
    private String show_flg;
    //录入人员list
    private List<String> lrrylist;
    //钉钉id
    private String ddid;
    //单位名称
    private String dwmc;
    //送检单位标记
    private String sjdwbj;
    //送检单位简称
    private String sjdwjc;
    
    public String getSjdwjc() {
		return sjdwjc;
	}
	public void setSjdwjc(String sjdwjc) {
		this.sjdwjc = sjdwjc;
	}
	public String getSjdwbj() {
		return sjdwbj;
	}
	public void setSjdwbj(String sjdwbj) {
		this.sjdwbj = sjdwbj;
	}
	public String getDdid() {
		return ddid;
	}
	public void setDdid(String ddid) {
		this.ddid = ddid;
	}
	public List<String> getLrrylist() {
		return lrrylist;
	}
	public void setLrrylist(List<String> lrrylist) {
		this.lrrylist = lrrylist;
	}
	public String getShow_flg()
	{
		return show_flg;
	}
	public void setShow_flg(String show_flg)
	{
		this.show_flg = show_flg;
	}
	public String getQtks()
    {
        return qtks;
    }
    public void setQtks(String qtks)
    {
        this.qtks = qtks;
    }
    public String getKzcs()
    {
        return kzcs;
    }
    public void setKzcs(String kzcs)
    {
        this.kzcs = kzcs;
    }
	public String getDwmc() {
		return dwmc;
	}
	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}

}
