package com.matridx.igams.bioinformation.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value = "WkcxbbDto")
public class WkcxbbDto extends WkcxbbModel {
	//clean20
	private String q20;
	//clean30
	private String q30;
	//cleangc
	private String gc;
	//报告类型
	private String detection_type;
	//报告类型
	private String host_position;
	//状态
	private String status;
	//样本编号
	private String ybbh;
	//强行合并报告
	private String qxhbbg;
	//文库编号
	private String sample_id;
	private String zdz;//最大值
	private String zxz;//最小值
	private String host_taxid;//人类的taxid
	private String sjid;//送检ID
	private String yblx;//样本类型
	private String taxid;
	private String total_reads;
	private String raw_reads;
	private String homo_percentage;
	private String microbial_reads;
	private String spike_rpm;
	private String fxbb;
	private String version_dates;
	private String xpm;

	public String getFxbb() {
		return fxbb;
	}

	public void setFxbb(String fxbb) {
		this.fxbb = fxbb;
	}

	public String getVersion_dates() {
		return version_dates;
	}

	public void setVersion_dates(String version_dates) {
		this.version_dates = version_dates;
	}

	public String getSpike_rpm() {
		return spike_rpm;
	}

	public void setSpike_rpm(String spike_rpm) {
		this.spike_rpm = spike_rpm;
	}

	public String getTotal_reads() {
		return total_reads;
	}

	public void setTotal_reads(String total_reads) {
		this.total_reads = total_reads;
	}

	public String getRaw_reads() {
		return raw_reads;
	}

	public void setRaw_reads(String raw_reads) {
		this.raw_reads = raw_reads;
	}

	public String getHomo_percentage() {
		return homo_percentage;
	}

	public void setHomo_percentage(String homo_percentage) {
		this.homo_percentage = homo_percentage;
	}

	public String getMicrobial_reads() {
		return microbial_reads;
	}

	public void setMicrobial_reads(String microbial_reads) {
		this.microbial_reads = microbial_reads;
	}

	public String getTaxid() {
		return taxid;
	}

	public void setTaxid(String taxid) {
		this.taxid = taxid;
	}

	public String getYblx() {
		return yblx;
	}

	public void setYblx(String yblx) {
		this.yblx = yblx;
	}

	public String getSjid() {
		return sjid;
	}

	public void setSjid(String sjid) {
		this.sjid = sjid;
	}

	public String getHost_taxid() {
		return host_taxid;
	}

	public void setHost_taxid(String host_taxid) {
		this.host_taxid = host_taxid;
	}

	public String getQ20() {
		return q20;
	}

	public void setQ20(String q20) {
		this.q20 = q20;
	}

	public String getQ30() {
		return q30;
	}

	public void setQ30(String q30) {
		this.q30 = q30;
	}

	public String getGc() {
		return gc;
	}

	public void setGc(String gc) {
		this.gc = gc;
	}

	public String getDetection_type() {
		return detection_type;
	}

	public void setDetection_type(String detection_type) {
		this.detection_type = detection_type;
	}

	public String getHost_position() {
		return host_position;
	}

	public void setHost_position(String host_position) {
		this.host_position = host_position;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getYbbh() {
		return ybbh;
	}

	public void setYbbh(String ybbh) {
		this.ybbh = ybbh;
	}

	public String getQxhbbg() {
		return qxhbbg;
	}

	public void setQxhbbg(String qxhbbg) {
		this.qxhbbg = qxhbbg;
	}

	public String getSample_id() {
		return sample_id;
	}

	public void setSample_id(String sample_id) {
		this.sample_id = sample_id;
	}

	public String getZdz() {
		return zdz;
	}

	public void setZdz(String zdz) {
		this.zdz = zdz;
	}

	public String getZxz() {
		return zxz;
	}

	public void setZxz(String zxz) {
		this.zxz = zxz;
	}

	private static final long serialVersionUID = 1L;

	public String getXpm() {
		return xpm;
	}

	public void setXpm(String xpm) {
		this.xpm = xpm;
	}
}
