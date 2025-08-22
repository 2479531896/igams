package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="SjnyxDto")
public class SjnyxDto extends SjnyxModel{
	//基因家族名称
	private String jyjzmc;
	//关联物种id
	private String wzid;
	//注释内容
	private String zsnr;
	//检测项目ID
	private String jcxmid;
	//检测子项目ID
	private String jczxmid;
	//突变基因
	private String tbjy;
	//突变结果
	private String tbjg;
	//耐药性
	private String nyx;
	//突变深度
	private String tbsd;
	//突变频率
	private String tbpl;
	//文库编号
	private String wkbh;
	//分类，区分结核还是非结核
	private String fl;
	//是否合并
	private String sfhb;
	//蛋白突变
	private String dbtb;
	//核苷酸突变
	private String hgstb;
	//相关药品
	private String xgyp;
	//药品等级
	private String ypdj;
	//耐药分类
	private String nyfl;
	//重新排序 只是为了反射不出差
	private String cxpx;

	public String getSfhb() {
		return sfhb;
	}

	public void setSfhb(String sfhb) {
		this.sfhb = sfhb;
	}

	public String getFl() {
		return fl;
	}

	public void setFl(String fl) {
		this.fl = fl;
	}

	private List<String> sjnyxids;
	private List<String> report_taxnames;
	private String jcbl;//检出比率
	private String ybbh;//样本编号

	public String getYbbh() {
		return ybbh;
	}

	public void setYbbh(String ybbh) {
		this.ybbh = ybbh;
	}

	public String getJcbl() {
		return jcbl;
	}

	public void setJcbl(String jcbl) {
		this.jcbl = jcbl;
	}

	public List<String> getSjnyxids() {
		return sjnyxids;
	}

	public void setSjnyxids(List<String> sjnyxids) {
		this.sjnyxids = sjnyxids;
	}

	public String getWkbh() {
		return wkbh;
	}

	public void setWkbh(String wkbh) {
		this.wkbh = wkbh;
	}

	public String getTbjy() {
		return tbjy;
	}

	public void setTbjy(String tbjy) {
		this.tbjy = tbjy;
	}

	public String getTbjg() {
		return tbjg;
	}

	public void setTbjg(String tbjg) {
		this.tbjg = tbjg;
	}

	public String getNyx() {
		return nyx;
	}

	public void setNyx(String nyx) {
		this.nyx = nyx;
	}

	public String getTbsd() {
		return tbsd;
	}

	public void setTbsd(String tbsd) {
		this.tbsd = tbsd;
	}

	public String getTbpl() {
		return tbpl;
	}

	public void setTbpl(String tbpl) {
		this.tbpl = tbpl;
	}

	public String getJcxmid() {
		return jcxmid;
	}


	public void setJcxmid(String jcxmid) {
		this.jcxmid = jcxmid;
	}


	public String getJyjzmc() {
		return jyjzmc;
	}


	public void setJyjzmc(String jyjzmc) {
		this.jyjzmc = jyjzmc;
	}


	public String getZsnr() {
		return zsnr;
	}


	public void setZsnr(String zsnr) {
		this.zsnr = zsnr;
	}

	public List<String> getReport_taxnames() {
		return report_taxnames;
	}

	public void setReport_taxnames(List<String> report_taxnames) {
		this.report_taxnames = report_taxnames;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    public String getJczxmid() {
        return jczxmid;
    }

    public void setJczxmid(String jczxmid) {
        this.jczxmid = jczxmid;
    }

    public String getDbtb() {
        return dbtb;
    }

    public void setDbtb(String dbtb) {
        this.dbtb = dbtb;
    }

    public String getHgstb() {
        return hgstb;
    }

    public void setHgstb(String hgstb) {
        this.hgstb = hgstb;
    }

    public String getXgyp() {
        return xgyp;
    }

    public void setXgyp(String xgyp) {
        this.xgyp = xgyp;
    }

    public String getYpdj() {
        return ypdj;
    }

    public void setYpdj(String ypdj) {
        this.ypdj = ypdj;
    }

    public String getNyfl() {
        return nyfl;
    }

    public void setNyfl(String nyfl) {
        this.nyfl = nyfl;
    }

    public String getCxpx() {
        return cxpx;
    }

    public void setCxpx(String cxpx) {
        this.cxpx = cxpx;
    }

    public String getWzid() {
        return wzid;
    }

    public void setWzid(String wzid) {
        this.wzid = wzid;
    }
}
