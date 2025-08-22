package com.matridx.crf.web.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="HzxxDto")
public class HzxxDto extends HzxxModel{

	private static final long serialVersionUID = 1L;
	//开始日期(出院时间)
	private String cysjstart;
	//结束日期(出院时间)
	private String cysjend;
	//开始日期(入院时间)
	private String rysjstart;
	//亚组对应的导出模板类型
	private String mbYzLx;
	//结束日期(入院时间)
	private String rysjend;
	private String [] kjywblss;
	private String [] cyzts;
	private String [] brlbs;
	private String [] xbs;
	private String [] jzkss;
	private String[] kjywjjtzls;
	//报告时间
	private String bgsj;
	//业务类型
	private String ywlx;
	//附件ID复数
	private List<String> fjids;

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}

	public String getBgsj() {
		return bgsj;
	}

	public void setBgsj(String bgsj) {
		this.bgsj = bgsj;
	}

	public String getMbYzLx() {
		return mbYzLx;
	}
	public void setMbYzLx(String mbYzLx) {
		this.mbYzLx = mbYzLx;
	}
	//当前角色
	private String dqjs;
	public String[] getKjywjjtzls() {
		return kjywjjtzls;
	}
	public void setKjywjjtzls(String[] kjywjjtzls) {
		this.kjywjjtzls = kjywjjtzls;
	}
	public String[] getKjywblss() {
		return kjywblss;
	}
	public void setKjywblss(String[] kjywblss) {
		this.kjywblss = kjywblss;
	}
	public String[] getCyzts() {
		return cyzts;
	}
	public void setCyzts(String[] cyzts) {
		this.cyzts = cyzts;
	}
	public String[] getBrlbs() {
		return brlbs;
	}
	public void setBrlbs(String[] brlbs) {
		this.brlbs = brlbs;
	}
	public String[] getXbs() {
		return xbs;
	}
	public void setXbs(String[] xbs) {
		this.xbs = xbs;
	}
	public String[] getJzkss() {
		return jzkss;
	}
	public void setJzkss(String[] jzkss) {
		this.jzkss = jzkss;
	}
	public String getCysjstart() {
		return cysjstart;
	}
	public void setCysjstart(String cysjstart) {
		this.cysjstart = cysjstart;
	}
	public String getCysjend() {
		return cysjend;
	}
	public void setCysjend(String cysjend) {
		this.cysjend = cysjend;
	}
	public String getRysjstart() {
		return rysjstart;
	}
	public void setRysjstart(String rysjstart) {
		this.rysjstart = rysjstart;
	}
	public String getRysjend() {
		return rysjend;
	}
	public void setRysjend(String rysjend) {
		this.rysjend = rysjend;
	}
	public String getDqjs() {
		return dqjs;
	}
	public void setDqjs(String dqjs) {
		this.dqjs = dqjs;
	}
	
	
}
