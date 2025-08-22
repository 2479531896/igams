package com.matridx.igams.crm.dao.entities;

import com.matridx.springboot.util.base.StringUtil;
import org.apache.ibatis.type.Alias;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Alias(value="JxhsmxDto")
public class JxhsmxDto extends JxhsmxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 绩效金额
	private String jxje;
	// 结算金额
	private String jsje;
	// 原绩效核算金额
	private String yjxhsje;

	private List<String> jxglids;
	
	// 账单周期
	private String zdzq;
	// 账单客户名称
	private String dzkhmc;
	// 伙伴分类名称
	private String hbflmc;
	// 销售额
	private String xse;
	// 实付金额
	private String sfje;
	// 回款金额
	private String hkje;
	// 负责人名称
	private String fzrmc;

	private String yszkid;
	private String djxsmc;

	public String getDjxsmc() {
		return djxsmc;
	}

	public void setDjxsmc(String djxsmc) {
		this.djxsmc = djxsmc;
	}

	public String getJxje() {
		return jxje;
	}

	public void setJxje(String jxje) {
		this.jxje = jxje;
	}

	public String getJsje() {
		return jsje;
	}

	public void setJsje(String jsje) {
		this.jsje = jsje;
	}

	public String getYjxhsje() {
		return yjxhsje;
	}

	public void setYjxhsje(String yjxhsje) {
		this.yjxhsje = yjxhsje;
	}

	public List<String> getJxglids() {
		return jxglids;
	}

	public void setJxglids(String jxglids) {
		List<String> list = new ArrayList<>();
		if(StringUtil.isNotBlank(jxglids)) {
			String[] str = jxglids.split(",");
			list = Arrays.asList(str);
		}
		this.jxglids = list;
	}
	public void setJxglids(List<String> jxglids) {
		if(jxglids!=null && !jxglids.isEmpty()){
			jxglids.replaceAll(s -> s.replace("[", "").replace("]", "").trim());
		}
		this.jxglids = jxglids;
	}

	public String getZdzq() {
		return zdzq;
	}

	public void setZdzq(String zdzq) {
		this.zdzq = zdzq;
	}

	public String getDzkhmc() {
		return dzkhmc;
	}

	public void setDzkhmc(String dzkhmc) {
		this.dzkhmc = dzkhmc;
	}

	public String getHbflmc() {
		return hbflmc;
	}

	public void setHbflmc(String hbflmc) {
		this.hbflmc = hbflmc;
	}

	public String getXse() {
		return xse;
	}

	public void setXse(String xse) {
		this.xse = xse;
	}

	public String getSfje() {
		return sfje;
	}

	public void setSfje(String sfje) {
		this.sfje = sfje;
	}

	public String getHkje() {
		return hkje;
	}

	public void setHkje(String hkje) {
		this.hkje = hkje;
	}

	public String getFzrmc() {
		return fzrmc;
	}

	public void setFzrmc(String fzrmc) {
		this.fzrmc = fzrmc;
	}

	@Override
	public String getYszkid() {
		return yszkid;
	}

	@Override
	public void setYszkid(String yszkid) {
		this.yszkid = yszkid;
	}
}
