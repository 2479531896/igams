package com.matridx.igams.storehouse.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value = "JcjyxxModel")
public class JcjyxxModel extends BaseModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    //借用信息id
    private String jyxxid;
    //借出借用id
    private String jcjyid;
    //仓库货物id
    private String ckhwid;
    //明细关联id
    private String mxglid;
    //借出数量
    private String jcsl;
    //预计归还日期
    private String yjghrq;
    //备注
    private String bz;
    //关联id
    private String glid;
	//产品类型
	private String cplx;

	public String getCplx() {
		return cplx;
	}

	public void setCplx(String cplx) {
		this.cplx = cplx;
	}

	public String getJyxxid() {
		return jyxxid;
	}

	public void setJyxxid(String jyxxid) {
		this.jyxxid = jyxxid;
	}

	public String getJcjyid() {
		return jcjyid;
	}

	public void setJcjyid(String jcjyid) {
		this.jcjyid = jcjyid;
	}

	public String getCkhwid() {
		return ckhwid;
	}

	public void setCkhwid(String ckhwid) {
		this.ckhwid = ckhwid;
	}

	public String getMxglid() {
		return mxglid;
	}

	public void setMxglid(String mxglid) {
		this.mxglid = mxglid;
	}

	public String getJcsl() {
		return jcsl;
	}

	public void setJcsl(String jcsl) {
		this.jcsl = jcsl;
	}

	public String getYjghrq() {
		return yjghrq;
	}

	public void setYjghrq(String yjghrq) {
		this.yjghrq = yjghrq;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}


	public String getGlid() {
		return glid;
	}

	public void setGlid(String glid) {
		this.glid = glid;
	}
}
