package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="JcjyxxDto")
public class JcjyxxDto extends JcjyxxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//物料编码
	private String wlbm;
	//物料id
	private String wlid;
	//物料名称
	private String wlmc;
	//总允许数量
	private String zyxsl;
	//规格
	private String gg;
	//计量单位
	private String jldw;
	//库存量
	private String kcl;
	//可借数量
	private String kqls;
	//仓库权限类型名称
	private String ckqxmc;
	//仓库权限
	private String ckqxlx;
	//hwxxList
	private List<HwxxDto> hwxxDtos;
	private String cplxmc;//产品类型名称
//销售明细数量
	private String sl;
	//借用单号
	private String jydh;
	private String zt;//状态
	private String jysl;//借用数量
	private List<JcjymxDto> jcjymxDtos;
	private String ckid;

	public String getCkid() {
		return ckid;
	}

	public void setCkid(String ckid) {
		this.ckid = ckid;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getJysl() {
		return jysl;
	}

	public void setJysl(String jysl) {
		this.jysl = jysl;
	}

	public List<JcjymxDto> getJcjymxDtos() {
		return jcjymxDtos;
	}

	public void setJcjymxDtos(List<JcjymxDto> jcjymxDtos) {
		this.jcjymxDtos = jcjymxDtos;
	}

	public String getJydh() {
		return jydh;
	}

	public void setJydh(String jydh) {
		this.jydh = jydh;
	}

	public String getSl() {
		return sl;
	}

	public void setSl(String sl) {
		this.sl = sl;
	}

	public String getCplxmc() {
		return cplxmc;
	}

	public void setCplxmc(String cplxmc) {
		this.cplxmc = cplxmc;
	}

	public String getZyxsl() {
		return zyxsl;
	}

	public void setZyxsl(String zyxsl) {
		this.zyxsl = zyxsl;
	}

	public List<HwxxDto> getHwxxDtos() {
		return hwxxDtos;
	}

	public void setHwxxDtos(List<HwxxDto> hwxxDtos) {
		this.hwxxDtos = hwxxDtos;
	}

	public String getCkqxlx() {
		return ckqxlx;
	}

	public void setCkqxlx(String ckqxlx) {
		this.ckqxlx = ckqxlx;
	}

	public String getKqls() {
		return kqls;
	}

	public void setKqls(String kqls) {
		this.kqls = kqls;
	}

	public String getCkqxmc() {
		return ckqxmc;
	}

	public void setCkqxmc(String ckqxmc) {
		this.ckqxmc = ckqxmc;
	}

	public String getWlmc() {
		return wlmc;
	}

	public void setWlmc(String wlmc) {
		this.wlmc = wlmc;
	}

	public String getWlbm() {
		return wlbm;
	}

	public void setWlbm(String wlbm) {
		this.wlbm = wlbm;
	}

	public String getWlid() {
		return wlid;
	}

	public void setWlid(String wlid) {
		this.wlid = wlid;
	}

	public String getGg() {
		return gg;
	}

	public void setGg(String gg) {
		this.gg = gg;
	}

	public String getJldw() {
		return jldw;
	}

	public void setJldw(String jldw) {
		this.jldw = jldw;
	}

	public String getKcl() {
		return kcl;
	}

	public void setKcl(String kcl) {
		this.kcl = kcl;
	}

}
