package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="FjcfbDto")
public class FjcfbDto extends FjcfbModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//是否导入标记 1：为导入  2为上传，3为普通文件导入，格式固定
	private String impflg;
	//上传文件名
	private String fileName;
	//文件名后缀
	private String wjmhz;
	//查看地址
	private String url;
	//微信ID
	private String wxid;
	//钉钉ID
	private String ddid;
	
	private List<String> ywlxs;
	private List<String> ywids;
	//加密签名
	private String sign;
	//访问路径
	private String applicationurl;
	//判断是不是word转的pdf
	private String pdf_flg;
	//importType用于区分固定表头的导入类别
	private String importType;
	//临时保存地址
	private String lsbcdz;
	//扩展参数1
	private String kzcs1;
	//扩展参数2
	private String kzcs2;
	//扩展参数3
	private String kzcs3;
	//扩展参数4
	private String kzcs4;
	//压缩文件路径
	private String ysblj;
	//短信模板编号
	private String dxmb;
	//短信签名
	private String sms_sign;
	//附件ids
	private List<String> fjids;
	//媒体ID
	private String mediaid;
	//微信localID
	private String localid;
	//分布式标识
	private String Prefix;
	//先上传附件ID(文件批量导入)
	private String prefjid;
	//是否发送(0:不发送, 1:发送)
	private String sffs;
	//使用标记  (1:已使用，0未使用)
	private String sybj;
	//所属医院(脓毒症word导入)
	private String ssyy;
	//亚组(脓毒症word导入)
	private String yz;
	//培训进度
	private String pxjd;
	//临时标识
	private String temporary;
	//录入人员名称
	private String lrrymc;
	//是否过期
	private String sfgq;
	//是否存在附件
	private String sfcz;
	//检查标记
	private String checkFlg;

	public String getCheckFlg() {
		return checkFlg;
	}

	public void setCheckFlg(String checkFlg) {
		this.checkFlg = checkFlg;
	}

	public String getSfcz() {
		return sfcz;
	}

	public void setSfcz(String sfcz) {
		this.sfcz = sfcz;
	}

	public String getSfgq() {
		return sfgq;
	}

	public void setSfgq(String sfgq) {
		this.sfgq = sfgq;
	}

	public String getLrrymc() {
		return lrrymc;
	}

	public void setLrrymc(String lrrymc) {
		this.lrrymc = lrrymc;
	}

	private String ywmc;

	private String bz;

	public String getTemporary() {
		return temporary;
	}

	public void setTemporary(String temporary) {
		this.temporary = temporary;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPxjd() {
		return pxjd;
	}

	public void setPxjd(String pxjd) {
		this.pxjd = pxjd;
	}

	public String getSsyy() {
		return ssyy;
	}

	public void setSsyy(String ssyy) {
		this.ssyy = ssyy;
	}

	public String getYz() {
		return yz;
	}

	public void setYz(String yz) {
		this.yz = yz;
	}

	public String getSybj() {
		return sybj;
	}
	public void setSybj(String sybj) {
		this.sybj = sybj;
	}
	public String getSffs() {
		return sffs;
	}
	public void setSffs(String sffs) {
		this.sffs = sffs;
	}
	public String getPrefjid() {
		return prefjid;
	}
	public void setPrefjid(String prefjid) {
		this.prefjid = prefjid;
	}
	
	public String getPrefix() {
		return Prefix;
	}
	public void setPrefix(String prefix) {
		Prefix = prefix;
	}
	public String getLocalid() {
		return localid;
	}
	public void setLocalid(String localid) {
		this.localid = localid;
	}
	public String getMediaid() {
		return mediaid;
	}
	public void setMediaid(String mediaid) {
		this.mediaid = mediaid;
	}
	public String getKzcs4() {
		return kzcs4;
	}
	public void setKzcs4(String kzcs4) {
		this.kzcs4 = kzcs4;
	}
	public List<String> getFjids() {
		return fjids;
	}
	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}
	public String getDxmb() {
		return dxmb;
	}
	public void setDxmb(String dxmb) {
		this.dxmb = dxmb;
	}
	public String getSms_sign() {
		return sms_sign;
	}
	public void setSms_sign(String sms_sign) {
		this.sms_sign = sms_sign;
	}
	public String getYsblj() {
		return ysblj;
	}
	public void setYsblj(String ysblj) {
		this.ysblj = ysblj;
	}
	public String getLsbcdz() {
		return lsbcdz;
	}
	public void setLsbcdz(String lsbcdz) {
		this.lsbcdz = lsbcdz;
	}
	public String getPdf_flg()
	{
		return pdf_flg;
	}
	public void setPdf_flg(String pdf_flg)
	{
		this.pdf_flg = pdf_flg;
	}
	public String getImpflg() {
		return impflg;
	}
	public void setImpflg(String impflg) {
		this.impflg = impflg;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public List<String> getYwlxs() {
		return ywlxs;
	}
	public void setYwlxs(List<String> ywlxs) {
		this.ywlxs = ywlxs;
	}
	public String getWjmhz() {
		return wjmhz;
	}
	public void setWjmhz(String wjmhz) {
		this.wjmhz = wjmhz;
	}
	public String getWxid() {
		return wxid;
	}
	public void setWxid(String wxid) {
		this.wxid = wxid;
	}
	public String getApplicationurl()
	{
		return applicationurl;
	}
	public void setApplicationurl(String applicationurl)
	{
		this.applicationurl = applicationurl;
	}
	public String getDdid() {
		return ddid;
	}
	public void setDdid(String ddid) {
		this.ddid = ddid;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getImportType() {
		return importType;
	}
	public void setImportType(String importType) {
		this.importType = importType;
	}
	public List<String> getYwids() {
		return ywids;
	}
	public void setYwids(List<String> ywids) {
		this.ywids = ywids;
	}
	public String getKzcs1() {
		return kzcs1;
	}
	public void setKzcs1(String kzcs1) {
		this.kzcs1 = kzcs1;
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

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getYwmc() {
		return ywmc;
	}

	public void setYwmc(String ywmc) {
		this.ywmc = ywmc;
	}
}
