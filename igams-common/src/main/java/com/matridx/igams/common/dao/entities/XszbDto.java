package com.matridx.igams.common.dao.entities;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.type.Alias;

@Alias(value="XszbDto")
public class XszbDto extends XszbModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//工号
	private String gh;
	//真实姓名
	private String zsxm;
	//指标类型参数名称
	private String zblxcsmc;
	//角色名称
	private String jsmc;
	//区域名称
	private String qymc;
	//完成数量
	private String wczsl;
	//销售完成率
	private String xswcl;
	//日期
	private List<Map<String,String>> rqs;
	//区域id
	private String qyid;
	//当前天数
	private String dqts;
	//总天数
	private String zts;
	//不收费数量
	private String bsfsl;
	//收费数量
	private String sfsl;
	//不收费率
	private String bsfl;
	//指标分类名称
	private String zbflmc;
	//指标子分类名称
	private String zbzflmc;
	private String cskz3;
	//项目名称
	private List<String> xms;
    //平台归属
	private String[] ptgss;
	//指标类型
	private String[] zblxs;
	//指标分类
	private String[] zbfls;
	//指标子分类
	private String[] zbzfls;
	private String ywlx;
	private String ywlx_q;
	//伙伴id
	private List<String> hbids;

	private String cskz2;
	//年份
	private String nf;
	//季度
	private String jd;
	//销售指标json
	private String xszb_json;
	//指标类型代码
	private String zblxdm;
	//月份
	private String yf;
	//付款金额
	private String fkje;
	//指标类型ID
	private String zblxid;
	//单位限定标记
	private String dwxzbj;

	private List<String> yhids;

	public List<String> getYhids() {
		return yhids;
	}

	public void setYhids(List<String> yhids) {
		this.yhids = yhids;
	}

	public String getDwxzbj() {
		return dwxzbj;
	}

	public void setDwxzbj(String dwxzbj) {
		this.dwxzbj = dwxzbj;
	}

	public String getZblxid() {
		return zblxid;
	}

	public void setZblxid(String zblxid) {
		this.zblxid = zblxid;
	}

	public String getFkje() {
		return fkje;
	}

	public void setFkje(String fkje) {
		this.fkje = fkje;
	}

	public String getYf() {
		return yf;
	}

	public void setYf(String yf) {
		this.yf = yf;
	}

	public String getZblxdm() {
		return zblxdm;
	}

	public void setZblxdm(String zblxdm) {
		this.zblxdm = zblxdm;
	}

	public String getXszb_json() {
		return xszb_json;
	}

	public void setXszb_json(String xszb_json) {
		this.xszb_json = xszb_json;
	}

	public String getJd() {
		return jd;
	}

	public void setJd(String jd) {
		this.jd = jd;
	}

	public String getNf() {
		return nf;
	}

	public void setNf(String nf) {
		this.nf = nf;
	}

	public String getCskz2() {
		return cskz2;
	}

	public void setCskz2(String cskz2) {
		this.cskz2 = cskz2;
	}

	public List<String> getHbids() {
		return hbids;
	}

	public void setHbids(List<String> hbids) {
		this.hbids = hbids;
	}

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public String getYwlx_q() {
		return ywlx_q;
	}

	public void setYwlx_q(String ywlx_q) {
		this.ywlx_q = ywlx_q;
	}

	public String[] getZblxs() {
		return zblxs;
	}

	public void setZblxs(String[] zblxs) {
		this.zblxs = zblxs;
		for (int i = 0; i < zblxs.length; i++) {
			this.zblxs[i] = this.zblxs[i].replace("'", "");
		}
	}

	public String[] getZbfls() {
		return zbfls;
	}

	public void setZbfls(String[] zbfls) {
		this.zbfls = zbfls;
		for (int i = 0; i < zbfls.length; i++) {
			this.zbfls[i] = this.zbfls[i].replace("'", "");
		}
	}

	public String[] getZbzfls() {
		return zbzfls;
	}

	public void setZbzfls(String[] zbzfls) {
		this.zbzfls = zbzfls;
		for (int i = 0; i < zbzfls.length; i++) {
			this.zbzfls[i] = this.zbzfls[i].replace("'", "");
		}
	}

	public String[] getPtgss() {
		return ptgss;
	}

	public void setPtgss(String[] ptgss) {
		this.ptgss = ptgss;
	}

	public List<String> getXms() {
		return xms;
	}

	public void setXms(List<String> xms) {
		this.xms = xms;
	}

	public String getCskz3() {
		return cskz3;
	}

	public void setCskz3(String cskz3) {
		this.cskz3 = cskz3;
	}

	public String getZbflmc() {
		return zbflmc;
	}

	public void setZbflmc(String zbflmc) {
		this.zbflmc = zbflmc;
	}

	public String getZbzflmc() {
		return zbzflmc;
	}

	public void setZbzflmc(String zbzflmc) {
		this.zbzflmc = zbzflmc;
	}

	public String getGh() {
		return gh;
	}
	public void setGh(String gh) {
		this.gh = gh;
	}
	public String getZsxm() {
		return zsxm;
	}
	public void setZsxm(String zsxm) {
		this.zsxm = zsxm;
	}

	public String getZblxcsmc() {
		return zblxcsmc;
	}

	public void setZblxcsmc(String zblxmc) {
		this.zblxcsmc = zblxmc;
	}

	public String getJsmc() {
		return jsmc;
	}

	public void setJsmc(String jsmc) {
		this.jsmc = jsmc;
	}

	public String getQymc() {
		return qymc;
	}

	public void setQymc(String qymc) {
		this.qymc = qymc;
	}

	public String getWczsl() {
		return wczsl;
	}

	public void setWczsl(String wczsl) {
		this.wczsl = wczsl;
	}

	public List<Map<String,String>> getRqs() {
		return rqs;
	}

	public void setRqs(List<Map<String,String>> rq) {
		this.rqs = rq;
	}

	public String getQyid() {
		return qyid;
	}

	public void setQyid(String qyid) {
		this.qyid = qyid;
	}

	public String getDqts() {
		return dqts;
	}

	public void setDqts(String dqts) {
		this.dqts = dqts;
	}

	public String getZts() {
		return zts;
	}

	public void setZts(String zts) {
		this.zts = zts;
	}

	public String getXswcl() {
		return xswcl;
	}

	public void setXswcl(String xswcl) {
		this.xswcl = xswcl;
	}
	public String getBsfsl() {
		return bsfsl;
	}
	public void setBsfsl(String bsfsl) {
		this.bsfsl = bsfsl;
	}
	public String getSfsl() {
		return sfsl;
	}
	public void setSfsl(String sfsl) {
		this.sfsl = sfsl;
	}
	public String getBsfl() {
		return bsfl;
	}
	public void setBsfl(String bsfl) {
		this.bsfl = bsfl;
	}
	
	
}
