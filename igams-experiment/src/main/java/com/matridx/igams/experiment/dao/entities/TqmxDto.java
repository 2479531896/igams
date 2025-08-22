package com.matridx.igams.experiment.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="TqmxDto")
public class TqmxDto extends TqmxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//内部编号
	private String[] nbbhs;
	//核酸浓度
	private String[] hsnds;
	//CDNA
	private String[] cdnas;
	//序号
	private String[] xhs;
	//建库用量
	private String[] jkyls;
	//缓冲液用量
	private String[] hcyyls;
	//稀释浓度
	private String[] xsnds;
 	//是否继续保存  1代表是，0代表否
	private String sfbc;
	//送检内部编码集合(无后缀)
	private String[] nbbms;
	//名称
	private String mc;
	//检测单位
	private String jcdw;
	//检测单位名称
	private String jcdwmc;
	//序号
	private String xh;
	//用户名称
	private String yhmc;
	//检测项目ID
	private String jcxmid;
	//检测项目名称
	private String jcxmmc;
	//样本类型名称
	private String yblxmc;
	//参数扩展1
	private String cskz1;
	//核酸提取试剂盒
	private String sjph;
	//D实验日期
	private String dsyrq;
	//实验日期
	private String syrq;
	//其他实验日期
	private String qtsyrq;
	//复检ID
	private String fjid;
	//R实验日期
	private String rsyrq;
	//DNA检测标记
	private String djcbj;
	//DNA检测标记
	private String rjcbj;
	//检测标记
	private String jcbj;
	//其他检测标记
	private String qtjcbj;
	private String[] nbzbms;
	private String[] tqmxids;
	private String nbzbm;
	private String kzcs1;
	private String syglid;
	private String kzcs3;
	private String kzcs2;
	private String jcxmcskz1;
	private String lx;
	//实验管理ids
	private String[] syglids;
	//检测类型参数扩展1
	private String jclxcskz1;
	//业务id
	private String ywid;

	private String zdpb;//自动排版个人设置用

	private String zdpbin;//自动排版包含个人设置用

	private String zdpbnotin;//自动排版不包含个人设置用

	private List<TqmxDto> tqmxDtoList;

	private String sjsyglid;

	private String dydm;

	private String jth;

	private String tj;

	private String yblx;
	private String czr;
	private String hdr;
	private String czrmc;
	private String hdrmc;

	public String getCzr() {
		return czr;
	}

	public void setCzr(String czr) {
		this.czr = czr;
	}

	public String getHdr() {
		return hdr;
	}

	public void setHdr(String hdr) {
		this.hdr = hdr;
	}

	public String getCzrmc() {
		return czrmc;
	}

	public void setCzrmc(String czrmc) {
		this.czrmc = czrmc;
	}

	public String getHdrmc() {
		return hdrmc;
	}

	public void setHdrmc(String hdrmc) {
		this.hdrmc = hdrmc;
	}

	private String tqsjStr;
	private String sysjStr;

	public String getSysjStr() {
		return sysjStr;
	}

	public void setSysjStr(String sysjStr) {
		this.sysjStr = sysjStr;
	}

	public String getJcdwmc() {
		return jcdwmc;
	}

	public void setJcdwmc(String jcdwmc) {
		this.jcdwmc = jcdwmc;
	}
	
	public String getYwid() {
		return ywid;
	}

	public void setYwid(String ywid) {
		this.ywid = ywid;
	}

	public String getJclxcskz1() {
		return jclxcskz1;
	}

	public void setJclxcskz1(String jclxcskz1) {
		this.jclxcskz1 = jclxcskz1;
	}

	public String[] getSyglids() {
		return syglids;
	}

	public void setSyglids(String[] syglids) {
		this.syglids = syglids;
	}

	public String getLx() {
		return lx;
	}

	public void setLx(String lx) {
		this.lx = lx;
	}

	public String getJcxmcskz1() {
		return jcxmcskz1;
	}

	public void setJcxmcskz1(String jcxmcskz1) {
		this.jcxmcskz1 = jcxmcskz1;
	}

	//是否更新
	private String sfgx;
		//提取仪孔位  复数
	private String[] tqykws;
	//提取仪编码  复数
	private String[] tqybms;

	private String[] spikes;

	private String[] xsbss;

	public String[] getXsbss() {
		return xsbss;
	}

	public void setXsbss(String[] xsbss) {
		this.xsbss = xsbss;
	}

	public String[] getSpikes() {
		return spikes;
	}

	public void setSpikes(String[] spikes) {
		this.spikes = spikes;
	}

	public String[] getTqybms() {
		return tqybms;
	}

	public void setTqybms(String[] tqybms) {
		this.tqybms = tqybms;
	}

	public String[] getTqykws() {
		return tqykws;
	}

	public void setTqykws(String[] tqykws) {
		this.tqykws = tqykws;
	}

	public String getSfgx() {
		return sfgx;
	}

	public void setSfgx(String sfgx) {
		this.sfgx = sfgx;
	}
	public String getKzcs2() {
		return kzcs2;
	}

	public void setKzcs2(String kzcs2) {
		this.kzcs2 = kzcs2;
	}

	public String getKzcs3() {
		return kzcs3;
	}

	public void setKzcs3(String kzcs3) {
		this.kzcs3 = kzcs3;
	}

	public String getSyglid() {
		return syglid;
	}

	public void setSyglid(String syglid) {
		this.syglid = syglid;
	}

	public String getKzcs1() {
		return kzcs1;
	}

	public void setKzcs1(String kzcs1) {
		this.kzcs1 = kzcs1;
	}

	public String getNbzbm() {
		return nbzbm;
	}

	public void setNbzbm(String nbzbm) {
		this.nbzbm = nbzbm;
	}

	public String[] getTqmxids() {
		return tqmxids;
	}

	public void setTqmxids(String[] tqmxids) {
		this.tqmxids = tqmxids;
	}

	public String[] getNbzbms() {
		return nbzbms;
	}

	public void setNbzbms(String[] nbzbms) {
		this.nbzbms = nbzbms;
	}

	public String getRjcbj() {
		return rjcbj;
	}

	public void setRjcbj(String rjcbj) {
		this.rjcbj = rjcbj;
	}

	public String getQtjcbj() {
		return qtjcbj;
	}

	public void setQtjcbj(String qtjcbj) {
		this.qtjcbj = qtjcbj;
	}

	public String getDjcbj() {
		return djcbj;
	}

	public void setDjcbj(String djcbj) {
		this.djcbj = djcbj;
	}

	public String getJcbj() {
		return jcbj;
	}

	public void setJcbj(String jcbj) {
		this.jcbj = jcbj;
	}

	public String getRsyrq() {
		return rsyrq;
	}

	public void setRsyrq(String rsyrq) {
		this.rsyrq = rsyrq;
	}

	public String getFjid() {
		return fjid;
	}

	public void setFjid(String fjid) {
		this.fjid = fjid;
	}

	public String getDsyrq() {
		return dsyrq;
	}

	public void setDsyrq(String dsyrq) {
		this.dsyrq = dsyrq;
	}

	public String getSyrq() {
		return syrq;
	}

	public void setSyrq(String syrq) {
		this.syrq = syrq;
	}

	public String getQtsyrq() {
		return qtsyrq;
	}

	public void setQtsyrq(String qtsyrq) {
		this.qtsyrq = qtsyrq;
	}

	public String getSjph() {
		return sjph;
	}
	public void setSjph(String sjph) {
		this.sjph = sjph;
	}
	public String getJcxmid() {
		return jcxmid;
	}
	public void setJcxmid(String jcxmid) {
		this.jcxmid = jcxmid;
	}
	public String getJcxmmc() {
		return jcxmmc;
	}
	public void setJcxmmc(String jcxmmc) {
		this.jcxmmc = jcxmmc;
	}
	public String getCskz1() {
		return cskz1;
	}
	public void setCskz1(String cskz1) {
		this.cskz1 = cskz1;
	}
	public String getYhmc()
	{
		return yhmc;
	}
	public void setYhmc(String yhmc)
	{
		this.yhmc = yhmc;
	}
	public String getJcdw()
	{
		return jcdw;
	}
	public void setJcdw(String jcdw)
	{
		this.jcdw = jcdw;
	}
	public String getXh()
	{
		return xh;
	}
	public void setXh(String xh)
	{
		this.xh = xh;
	}
	public String[] getXsnds() {
		return xsnds;
	}
	public void setXsnds(String[] xsnds) {
		this.xsnds = xsnds;
	}
	public String[] getJkyls() {
		return jkyls;
	}
	public void setJkyls(String[] jkyls) {
		this.jkyls = jkyls;
	}
	public String[] getHcyyls() {
		return hcyyls;
	}
	public void setHcyyls(String[] hcyyls) {
		this.hcyyls = hcyyls;
	}
	public String getMc() {
		return mc;
	}
	public void setMc(String mc) {
		this.mc = mc;
	}
	public String[] getNbbms() {
		return nbbms;
	}
	public void setNbbms(String[] nbbms) {
		this.nbbms = nbbms;
	}
	public String getSfbc() {
		return sfbc;
	}
	public void setSfbc(String sfbc) {
		this.sfbc = sfbc;
	}
	public String[] getXhs() {
		return xhs;
	}
	public void setXhs(String[] xhs) {
		this.xhs = xhs;
	}
	public String[] getNbbhs() {
		return nbbhs;
	}
	public void setNbbhs(String[] nbbhs) {
		this.nbbhs = nbbhs;
	}
	public String[] getHsnds() {
		return hsnds;
	}
	public void setHsnds(String[] hsnds) {
		this.hsnds = hsnds;
	}
	public String[] getCdnas() {
		return cdnas;
	}
	public void setCdnas(String[] cdnas) {
		this.cdnas = cdnas;
	}


	public String getZdpb() {
		return zdpb;
	}

	public void setZdpb(String zdpb) {
		this.zdpb = zdpb;
	}

	public String getZdpbin() {
		return zdpbin;
	}

	public void setZdpbin(String zdpbin) {
		this.zdpbin = zdpbin;
	}

	public String getZdpbnotin() {
		return zdpbnotin;
	}

	public void setZdpbnotin(String zdpbnotin) {
		this.zdpbnotin = zdpbnotin;
	}

	public List<TqmxDto> getTqmxDtoList() {
		return tqmxDtoList;
	}

	public void setTqmxDtoList(List<TqmxDto> tqmxDtoList) {
		this.tqmxDtoList = tqmxDtoList;
	}

	public String getSjsyglid() {
		return sjsyglid;
	}

	public void setSjsyglid(String sjsyglid) {
		this.sjsyglid = sjsyglid;
	}

	public String getDydm() {
		return dydm;
	}

	public void setDydm(String dydm) {
		this.dydm = dydm;
	}

	public String getJth() {
		return jth;
	}

	public void setJth(String jth) {
		this.jth = jth;
	}

	public String getYblxmc() {
		return yblxmc;
	}

	public void setYblxmc(String yblxmc) {
		this.yblxmc = yblxmc;
	}

	public String getTj() {
		return tj;
	}

	public void setTj(String tj) {
		this.tj = tj;
	}

	public String getYblx() {
		return yblx;
	}

	public void setYblx(String yblx) {
		this.yblx = yblx;
	}

	public String getTqsjStr() {
		return tqsjStr;
	}

	public void setTqsjStr(String tqsjStr) {
		this.tqsjStr = tqsjStr;
	}
}
