package com.matridx.igams.crm.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="JxhsglDto")
public class JxhsglDto extends JxhsglModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 模糊查询 全部
	private String all_param;
	// 负责人姓名
	private String fzrxm;
	private String fzryhm;
	// 创建日期开始
	private String cjrqStart;
	// 创建日期结束
	private String cjrqEnd;
	// 是否核对s
	private String [] sfhds;
	// 审核状态s
	private String [] zts;
	// 创建人姓名
	private String cjryxm;
	// 核对人员姓名
	private String hdryxm;
	// 创建日期
	private String cjrq;
	// 创建时间
	private String cjsj;

	private List<JxhsmxDto> jxhsmxDtos;

	private String jxksrq_y;//绩效开始日期(年)

	private String yhid;

	private String xsmb;

	public String getFzryhm() {
		return fzryhm;
	}

	public void setFzryhm(String fzryhm) {
		this.fzryhm = fzryhm;
	}

	public String getXsmb() {
		return xsmb;
	}

	public void setXsmb(String xsmb) {
		this.xsmb = xsmb;
	}

	public String getYhid() {
		return yhid;
	}

	public void setYhid(String yhid) {
		this.yhid = yhid;
	}

	public String getJxksrq_y() {
		return jxksrq_y;
	}

	public void setJxksrq_y(String jxksrq_y) {
		this.jxksrq_y = jxksrq_y;
	}

	public String getAll_param() {
		return all_param;
	}

	public void setAll_param(String all_param) {
		this.all_param = all_param;
	}

	public String getFzrxm() {
		return fzrxm;
	}

	public void setFzrxm(String fzrxm) {
		this.fzrxm = fzrxm;
	}

	public String getCjrqStart() {
		return cjrqStart;
	}

	public void setCjrqStart(String cjrqStart) {
		this.cjrqStart = cjrqStart;
	}

	public String getCjrqEnd() {
		return cjrqEnd;
	}

	public void setCjrqEnd(String cjrqEnd) {
		this.cjrqEnd = cjrqEnd;
	}

	public String[] getSfhds() {
		return sfhds;
	}

	public void setSfhds(String[] sfhds) {
		this.sfhds = sfhds;
		for (int i = 0; i < sfhds.length; i++){
			this.sfhds[i]=this.sfhds[i].replace("'","");
		}
	}

	public String[] getZts() {
		return zts;
	}

	public void setZts(String[] zts) {
		this.zts = zts;
		for (int i = 0; i < zts.length; i++){
			this.zts[i]=this.zts[i].replace("'","");
		}
	}

	public String getCjryxm() {
		return cjryxm;
	}

	public void setCjryxm(String cjryxm) {
		this.cjryxm = cjryxm;
	}

	public String getHdryxm() {
		return hdryxm;
	}

	public void setHdryxm(String hdryxm) {
		this.hdryxm = hdryxm;
	}

	public String getCjrq() {
		return cjrq;
	}

	public void setCjrq(String cjrq) {
		this.cjrq = cjrq;
	}

	public String getCjsj() {
		return cjsj;
	}

	public void setCjsj(String cjsj) {
		this.cjsj = cjsj;
	}

	public List<JxhsmxDto> getJxhsmxDtos() {
		return jxhsmxDtos;
	}

	public void setJxhsmxDtos(List<JxhsmxDto> jxhsmxDtos) {
		this.jxhsmxDtos = jxhsmxDtos;
	}
}
