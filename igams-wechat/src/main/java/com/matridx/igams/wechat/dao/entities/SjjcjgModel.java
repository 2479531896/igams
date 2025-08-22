package com.matridx.igams.wechat.dao.entities;

import com.matridx.igams.common.dao.Model;
import org.apache.ibatis.type.Alias;

@Alias(value="SjjcjgModel")
public class SjjcjgModel extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String jcjgid;
	private String xh;
	private String jclx;
	private String jczlx;
	private String ywid;
	private String ncysz;
	private String ncjsz;
	private String ncysz2;
	private String ncjsz2;
	private String lx;
	private String jcjg;
	private String jl;
	private String jgsz;
	private String pdz;
	private String ckbl1;
	private String pjz;

	public String getPjz() {
		return pjz;
	}

	public void setPjz(String pjz) {
		this.pjz = pjz;
	}

	public String getCkbl1() {
		return ckbl1;
	}

	public void setCkbl1(String ckbl1) {
		this.ckbl1 = ckbl1;
	}

	public String getNcysz2() {
		return ncysz2;
	}

	public void setNcysz2(String ncysz2) {
		this.ncysz2 = ncysz2;
	}

	public String getNcjsz2() {
		return ncjsz2;
	}

	public void setNcjsz2(String ncjsz2) {
		this.ncjsz2 = ncjsz2;
	}

	public String getNcysz() {
		return ncysz;
	}

	public void setNcysz(String ncysz) {
		this.ncysz = ncysz;
	}

	public String getNcjsz() {
		return ncjsz;
	}

	public void setNcjsz(String ncjsz) {
		this.ncjsz = ncjsz;
	}

	public String getPdz() {
		return pdz;
	}

	public void setPdz(String pdz) {
		this.pdz = pdz;
	}

	public String getYwid() {
		return ywid;
	}

	public void setYwid(String ywid) {
		this.ywid = ywid;
	}

	public String getLx() {
		return lx;
	}

	public void setLx(String lx) {
		this.lx = lx;
	}

	public String getJcjgid() {
		return jcjgid;
	}

	public void setJcjgid(String jcjgid) {
		this.jcjgid = jcjgid;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getJclx() {
		return jclx;
	}

	public void setJclx(String jclx) {
		this.jclx = jclx;
	}

	public String getJcjg() {
		return jcjg;
	}

	public void setJcjg(String jcjg) {
		this.jcjg = jcjg;
	}

	public String getJl() {
		return jl;
	}

	public void setJl(String jl) {
		this.jl = jl;
	}

	public String getJgsz() {
		return jgsz;
	}

	public void setJgsz(String jgsz) {
		this.jgsz = jgsz;
	}


    public String getJczlx() {
        return jczlx;
    }

    public void setJczlx(String jczlx) {
        this.jczlx = jczlx;
    }
}
