package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="SjwzxxDto")
public class SjwzxxDto extends SjwzxxModel{
	//做统计时查询物种的数量
	private String wznum;
	//类型标记
	private String lx_flg;
	//类型名称
	private String lxmc;
	//接收日期
	private String jsrq;
    //检验结果
    private String jyjg;
    //报告日期
    private String bgrq;
    //开始日期
  	private String bgrqstart;
  	//结束日期
  	private String bgrqend;
  	//开始日期(月查询)
  	private String bgrqMstart;
  	//结束日期(月查询)
  	private String bgrqMend;
  	//开始日期(年查询)
  	private String bgrqYstart;
  	//结束日期(年查询)
  	private String bgrqYend;
    //合作伙伴（多）
  	private List<String> sjhbs;
  	//用户id
  	private String yhid;
  	//名称（中文名+英文名）
  	private String mc;
  	//基因
  	private String jy;
  	//药品
  	private String yp;
  	//机理
  	private String jl;
  	//分类等级
  	private String fldj;
  	//物种名称
  	private String bdmc;
  	//相关物种
  	private String xgwz;
  	//物种类型
  	private String wzlx;
  	//物种注释
  	private String wzzs;
  	//Q_index
  	private String index;
  	//image
  	private String percentile;
  	//图片路径
  	private String percentile_path;
  	//游标路径
  	private String cursor_path;
  	//模板显示顺序
  	private String mbxssx;
  	//结果类型[多]
  	private List<String> wzfls;
  	//检测项目ID
  	private String jcxmid;
	//检测项目ID
	private String jczxmid;
	// 基因分型
	private String jyfx;
	//序列数
	private String xls;
	//起源种
	private String qyz;
	//检测类型名称
	private String jclxmc;
	//物种属分类
	private String wzsfl;
	//物种管理物种分类
	private String wzglwzfl;
	//总长度（作为基因组覆盖度的分母）
	private String zcd;
	//覆盖长度（作为基因组覆盖度的分子）
	private String fgcd;
	//物种图片路径
	private String bjtppath;
	//引用序号
	private String yyxh;
	//旧引用序号
	private String oldyyxh;
	//总序列书
	private String zxls;
	//荧光值
	private String ygz;
	//结果类型名称
	private String jglxmc;
	//新增字段 label
	private String label;
	//是否为空
	private String isnull;
	private String target;//覆盖靶标(90%)
	private String cxpx;//物种注释对应文献下标重新排序

	private List<String> report_taxids;
	private List<String> report_taxnames;

	private String spike;

	private String ntm;

	private String tb;

	private String sfhb;//是否汇报

	private String nyzs;//耐药注释

	private String jcfl;//检测分类，区分是否为TBT的检出物种

	private String sjnyxid;

	private String xm; //项目
	private String jcjg; //检测结果
	private String ctz; //ct值
	private String pdbz; //判定标准

	public String getPdbz() {
		return pdbz;
	}

	public void setPdbz(String pdbz) {
		this.pdbz = pdbz;
	}

	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public String getJcjg() {
		return jcjg;
	}

	public void setJcjg(String jcjg) {
		this.jcjg = jcjg;
	}

	public String getCtz() {
		return ctz;
	}

	public void setCtz(String ctz) {
		this.ctz = ctz;
	}
	
	public String getSjnyxid() {
		return sjnyxid;
	}

	public void setSjnyxid(String sjnyxid) {
		this.sjnyxid = sjnyxid;
	}

	public String getJcfl() {
		return jcfl;
	}

	public void setJcfl(String jcfl) {
		this.jcfl = jcfl;
	}

	public String getNyzs() {
		return nyzs;
	}

	public void setNyzs(String nyzs) {
		this.nyzs = nyzs;
	}

	public String getSfhb() {
		return sfhb;
	}

	public void setSfhb(String sfhb) {
		this.sfhb = sfhb;
	}

	public String getNtm() {
		return ntm;
	}

	public void setNtm(String ntm) {
		this.ntm = ntm;
	}

	public String getTb() {
		return tb;
	}

	public void setTb(String tb) {
		this.tb = tb;
	}

	public String getSpike() {
		return spike;
	}

	public void setSpike(String spike) {
		this.spike = spike;
	}

	public String getCxpx() {
		return cxpx;
	}

	public void setCxpx(String cxpx) {
		this.cxpx = cxpx;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getIsnull() {
		return isnull;
	}

	public void setIsnull(String isnull) {
		this.isnull = isnull;
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getYgz() {
		return ygz;
	}

	public void setYgz(String ygz) {
		this.ygz = ygz;
	}

	public String getJglxmc() {
		return jglxmc;
	}

	public void setJglxmc(String jglxmc) {
		this.jglxmc = jglxmc;
	}

	public String getZxls() {
		return zxls;
	}

	public void setZxls(String zxls) {
		this.zxls = zxls;
	}

	public String getYyxh() {
		return yyxh;
	}

	public void setYyxh(String yyxh) {
		this.yyxh = yyxh;
	}

	public String getOldyyxh() {
		return oldyyxh;
	}

	public void setOldyyxh(String oldyyxh) {
		this.oldyyxh = oldyyxh;
	}

	public String getBjtppath() {
		return bjtppath;
	}

	public void setBjtppath(String bjtppath) {
		this.bjtppath = bjtppath;
	}
	//综合物种中文名
	private String zhwzzwm;

	//综合物种英文名
	private String zhwzywm;

	//综合读取数
	private String zhdqs;

	//综合相对风度
	private String zhxdfd;
	//综合物种分类

	private String zhwzfl;
	//rpm
	private String rpm;
	private String refANI;

	private String reads;//序列数
	private String MappingReads;//总序列数
	private String tbjy;
	private String tbjg;
	private String nyx;
	private String tbsd;
	private String tbpl;
	private String bjtb;
	private String CleanQ30;
	private String RawReads;
	private String CleanReads;

	public String getBjtb() {
		return bjtb;
	}

	public void setBjtb(String bjtb) {
		this.bjtb = bjtb;
	}

	public String getRawReads() {
		return RawReads;
	}

	public void setRawReads(String rawReads) {
		RawReads = rawReads;
	}

	public String getCleanReads() {
		return CleanReads;
	}

	public void setCleanReads(String cleanReads) {
		CleanReads = cleanReads;
	}

	public String getCleanQ30() {
		return CleanQ30;
	}

	public void setCleanQ30(String cleanQ30) {
		CleanQ30 = cleanQ30;
	}

	public String getReads() {
		return reads;
	}

	public void setReads(String reads) {
		this.reads = reads;
	}

	public String getMappingReads() {
		return MappingReads;
	}

	public void setMappingReads(String mappingReads) {
		MappingReads = mappingReads;
	}

	public String getRefANI() {
		return refANI;
	}

	public void setRefANI(String refANI) {
		this.refANI = refANI;
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

	public String getRpm() {
		return rpm;
	}

	public void setRpm(String rpm) {
		this.rpm = rpm;
	}

	public String getZhwzfl() {
		return zhwzfl;
	}

	public void setZhwzfl(String zhwzfl) {
		this.zhwzfl = zhwzfl;
	}

	public String getZhwzzwm() {
		return zhwzzwm;
	}

	public void setZhwzzwm(String zhwzzwm) {
		this.zhwzzwm = zhwzzwm;
	}

	public String getZhwzywm() {
		return zhwzywm;
	}

	public void setZhwzywm(String zhwzywm) {
		this.zhwzywm = zhwzywm;
	}

	public String getZhdqs() {
		return zhdqs;
	}

	public void setZhdqs(String zhdqs) {
		this.zhdqs = zhdqs;
	}

	public String getZhxdfd() {
		return zhxdfd;
	}

	public void setZhxdfd(String zhxdfd) {
		this.zhxdfd = zhxdfd;
	}

	public String getWzsfl() {
		return wzsfl;
	}

	public String getWzglwzfl() {
		return wzglwzfl;
	}

	public String getZcd() {
		return zcd;
	}

	public void setZcd(String zcd) {
		this.zcd = zcd;
	}

	
	public void setWzglwzfl(String wzglwzfl) {
		this.wzglwzfl = wzglwzfl;
	}
	public String getFgcd() {
		return fgcd;
	}
	public void setFgcd(String fgcd) {
		this.fgcd = fgcd;
	}
	
	public void setWzsfl(String wzsfl) {
		this.wzsfl = wzsfl;
	}
	public String getJclxmc() {
		return jclxmc;
	}
	public void setJclxmc(String jclxmc) {
		this.jclxmc = jclxmc;
	}
	public String getJcxmid() {
		return jcxmid;
	}
	public void setJcxmid(String jcxmid) {
		this.jcxmid = jcxmid;
	}
	public String getMbxssx() {
		return mbxssx;
	}
	public void setMbxssx(String mbxssx) {
		this.mbxssx = mbxssx;
	}
	public String getFldj()
	{
		return fldj;
	}
	public void setFldj(String fldj)
	{
		this.fldj = fldj;
	}
	public String getJy()
	{
		return jy;
	}
	public void setJy(String jy)
	{
		this.jy = jy;
	}
	public String getYp()
	{
		return yp;
	}
	public void setYp(String yp)
	{
		this.yp = yp;
	}
	public String getJl()
	{
		return jl;
	}
	public void setJl(String jl)
	{
		this.jl = jl;
	}
	public String getMc()
	{
		return mc;
	}
	public void setMc(String mc)
	{
		this.mc = mc;
	}
	public String getYhid()
	{
		return yhid;
	}
	public void setYhid(String yhid)
	{
		this.yhid = yhid;
	}
	public List<String> getSjhbs()
	{
		return sjhbs;
	}
	public void setSjhbs(List<String> sjhbs)
	{
		this.sjhbs = sjhbs;
	}
	public String getBgrqstart()
	{
		return bgrqstart;
	}
	public void setBgrqstart(String bgrqstart)
	{
		this.bgrqstart = bgrqstart;
		if("null".equals(this.bgrqstart) || this.bgrqstart=="") {
			this.bgrqstart="";
		}
	}
	public String getBgrqend()
	{
		return bgrqend;
	}
	public void setBgrqend(String bgrqend)
	{
		this.bgrqend = bgrqend;
		if("null".equals(this.bgrqend) || this.bgrqend=="") {
			this.bgrqend="";
		}
	}
	public String getBgrqMstart()
	{
		return bgrqMstart;
	}
	public void setBgrqMstart(String bgrqMstart)
	{
		this.bgrqMstart = bgrqMstart;
		if("null".equals(this.bgrqMstart) || this.bgrqMstart=="") {
			this.bgrqMstart="";
		}
	}
	public String getBgrqMend()
	{
		return bgrqMend;
	}
	public void setBgrqMend(String bgrqMend)
	{
		this.bgrqMend = bgrqMend;
		if("null".equals(this.bgrqMend) || this.bgrqMend=="") {
			this.bgrqMend="";
		}
	}
	public String getBgrqYstart()
	{
		return bgrqYstart;
	}
	public void setBgrqYstart(String bgrqYstart)
	{
		this.bgrqYstart = bgrqYstart;
		if("null".equals(this.bgrqYstart) || this.bgrqYstart=="") {
			this.bgrqYstart="";
		}
	}
	public String getBgrqYend()
	{
		return bgrqYend;
	}
	public void setBgrqYend(String bgrqYend)
	{
		this.bgrqYend = bgrqYend;
		if("null".equals(this.bgrqYend) || this.bgrqYend=="") {
			this.bgrqYend="";
		}
	}
	public String getBgrq()
	{
		return bgrq;
	}
	public void setBgrq(String bgrq)
	{
		this.bgrq = bgrq;
	}
	public String getJyjg()
    {
        return jyjg;
    }
    public void setJyjg(String jyjg)
    {
        this.jyjg = jyjg;
    }
	public String getLxmc()
	{
		return lxmc;
	}
	public void setLxmc(String lxmc)
	{
		this.lxmc = lxmc;
	}
	public String getLx_flg()
	{
		return lx_flg;
	}
	public void setLx_flg(String lx_flg)
	{
		this.lx_flg = lx_flg;
	}
	public String getWznum(){
		return wznum;
	}
	public void setWznum(String wznum)
	{
		this.wznum = wznum;
	}
	public String getJsrq() {
		return jsrq;
	}
	public void setJsrq(String jsrq) {
		this.jsrq = jsrq;
	}

	public String getBdmc() {
		return bdmc;
	}
	public void setBdmc(String bdmc) {
		this.bdmc = bdmc;
	}
	public String getXgwz() {
		return xgwz;
	}
	public void setXgwz(String xgwz) {
		this.xgwz = xgwz;
	}

	public String getWzlx() {
		return wzlx;
	}
	public void setWzlx(String wzlx) {
		this.wzlx = wzlx;
	}

	public String getWzzs() {
		return wzzs;
	}
	public void setWzzs(String wzzs) {
		this.wzzs = wzzs;
	}

	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getPercentile() {
		return percentile;
	}
	public void setPercentile(String percentile) {
		this.percentile = percentile;
	}

	public String getPercentile_path() {
		return percentile_path;
	}
	public void setPercentile_path(String percentile_path) {
		this.percentile_path = percentile_path;
	}

	public String getCursor_path() {
		return cursor_path;
	}
	public void setCursor_path(String cursor_path) {
		this.cursor_path = cursor_path;
	}

	public List<String> getWzfls() {
		return wzfls;
	}
	public void setWzfls(List<String> wzfls) {
		this.wzfls = wzfls;
	}

	public String getJyfx() {
		return jyfx;
	}

	public void setJyfx(String jyfx) {
		this.jyfx = jyfx;
	}

	public String getXls() {
		return xls;
	}

	public void setXls(String xls) {
		this.xls = xls;
	}

	public String getQyz() {
		return qyz;
	}

	public void setQyz(String qyz) {
		this.qyz = qyz;
	}

	public List<String> getReport_taxids() {
		return report_taxids;
	}

	public void setReport_taxids(List<String> report_taxids) {
		this.report_taxids = report_taxids;
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
}
