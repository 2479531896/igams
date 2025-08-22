package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="SjxxjgDto")
public class SjxxjgDto extends SjxxjgModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//相应检测类型下的数据总和
	private int num;
	//用于取详细结果最底层的子数据
	private String np;
	//种名称（中文名+英文名）
	private String mc;
	//属名
	private String sm;
	//属的读数
	private String sdds;
	//属丰度
	private String sfd;
	//G_zwm 属的中文名
	private String  G_zwm;
	//G_ywm 属的英文名
	private String  G_ywm;
	//S_zwm 种的中文名
	private String  S_zwm;
	//S_ywm 种的英文名
	private String  S_ywm;
	//病毒名称
	private String bdmc;
	//种读取数
	private String dqs;
	//层数
	private String depth;
	//是否显示(小程序)
	private boolean isshow;
	//树是否打开(小程序)
	private boolean open;
	//用户ID
	private String yhid;
	//关联类型
	private String gllx;
	//显示顺序
	private String t_xssx;
	//检测类型名称
	private String jclxmc;
	//样本类型名称
	private String yblxmc;
	//患者姓名
	private String hzxm;
	//tngs标记
	private String tngs_flg;
	//fdwds
	private String fdwds;
	//内部编码，送检检出结果导出时使用
	private String nbbm;
	//报告日期，送检检出结果导出时使用
	private String bgrq;
	//删除判断标记
	private String scflag;

	private String sfhb;

	public String getSfhb() {
		return sfhb;
	}

	public void setSfhb(String sfhb) {
		this.sfhb = sfhb;
	}

	public String getFdwds() {
		return fdwds;
	}

	public void setFdwds(String fdwds) {
		this.fdwds = fdwds;
	}

	public String getTngs_flg() {
		return tngs_flg;
	}

	public void setTngs_flg(String tngs_flg) {
		this.tngs_flg = tngs_flg;
	}

	public String getHzxm() {
		return hzxm;
	}

	public void setHzxm(String hzxm) {
		this.hzxm = hzxm;
	}

	public String getYblxmc() {
		return yblxmc;
	}

	public void setYblxmc(String yblxmc) {
		this.yblxmc = yblxmc;
	}

	public String getJclxmc() {
		return jclxmc;
	}

	public void setJclxmc(String jclxmc) {
		this.jclxmc = jclxmc;
	}

	public String getT_xssx() {
		return t_xssx;
	}

	public void setT_xssx(String t_xssx) {
		this.t_xssx = t_xssx;
	}

	public String getGllx() {
		return gllx;
	}

	public void setGllx(String gllx) {
		this.gllx = gllx;
	}

	public String getYhid() {
		return yhid;
	}

	public void setYhid(String yhid) {
		this.yhid = yhid;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public boolean getIsshow() {
		return isshow;
	}

	public void setIsshow(boolean isshow) {
		this.isshow = isshow;
	}

	public String getDepth() {
		return depth;
	}

	public void setDepth(String depth) {
		this.depth = depth;
	}

	public String getDqs()
	{
		return dqs;
	}

	public void setDqs(String dqs)
	{
		this.dqs = dqs;
	}

	public String getBdmc()
	{
		return bdmc;
	}

	public void setBdmc(String bdmc)
	{
		this.bdmc = bdmc;
	}

	public String getG_zwm()
	{
		return G_zwm;
	}

	public void setG_zwm(String g_zwm)
	{
		G_zwm = g_zwm;
	}

	public String getG_ywm()
	{
		return G_ywm;
	}

	public void setG_ywm(String g_ywm)
	{
		G_ywm = g_ywm;
	}

	public String getS_zwm()
	{
		return S_zwm;
	}

	public void setS_zwm(String s_zwm)
	{
		S_zwm = s_zwm;
	}

	public String getS_ywm()
	{
		return S_ywm;
	}

	public void setS_ywm(String s_ywm)
	{
		S_ywm = s_ywm;
	}

	public String getSm()
	{
		return sm;
	}

	public void setSm(String sm)
	{
		this.sm = sm;
	}

	public String getSdds()
	{
		return sdds;
	}

	public void setSdds(String sdds)
	{
		this.sdds = sdds;
	}

	public String getSfd()
	{
		return sfd;
	}

	public void setSfd(String sfd)
	{
		this.sfd = sfd;
	}

	public String getMc()
	{
		return mc;
	}

	public void setMc(String mc)
	{
		this.mc = mc;
	}

	public String getNp() {
		return np;
	}

	public void setNp(String np) {
		this.np = np;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getNbbm() {
		return nbbm;
	}

	public void setNbbm(String nbbm) {
		this.nbbm = nbbm;
	}

	public String getBgrq() {
		return bgrq;
	}

	public void setBgrq(String bgrq) {
		this.bgrq = bgrq;
	}

	public String getScflag() {
		return scflag;
	}

	public void setScflag(String scflag) {
		this.scflag = scflag;
	}
}
