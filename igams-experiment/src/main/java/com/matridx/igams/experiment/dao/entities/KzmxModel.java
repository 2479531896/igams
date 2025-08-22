package com.matridx.igams.experiment.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="KzmxModel")
public class KzmxModel extends BaseModel{
	//扩增ID
	private String kzid;
	//序号
	private String xh;
	//内部编号
	private String nbbh;
	//送检ID
	private String sjid;
	//检测项目代码
	private String jcxmdm;
	//荧光
	private String referencedye;
	//检测结果
	private String qcresult;
	//孔位
	private String well;
	//提取码
	private String tqm;
	//实验管理ID
	private String syglid;

	public String getKzid() {
		return kzid;
	}

	public void setKzid(String kzid) {
		this.kzid = kzid;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getNbbh() {
		return nbbh;
	}

	public void setNbbh(String nbbh) {
		this.nbbh = nbbh;
	}

	public String getSjid() {
		return sjid;
	}

	public void setSjid(String sjid) {
		this.sjid = sjid;
	}

	public String getJcxmdm() {
		return jcxmdm;
	}

	public void setJcxmdm(String jcxmdm) {
		this.jcxmdm = jcxmdm;
	}

	public String getReferencedye() {
		return referencedye;
	}

	public void setReferencedye(String referencedye) {
		this.referencedye = referencedye;
	}

	public String getQcresult() {
		return qcresult;
	}

	public void setQcresult(String qcresult) {
		this.qcresult = qcresult;
	}

	public String getWell() {
		return well;
	}

	public void setWell(String well) {
		this.well = well;
	}

	public String getTqm() {
		return tqm;
	}

	public void setTqm(String tqm) {
		this.tqm = tqm;
	}

	public String getSyglid() {
		return syglid;
	}

	public void setSyglid(String syglid) {
		this.syglid = syglid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
