package com.matridx.igams.wechat.dao.entities;

import java.util.List;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.ibatis.type.Alias;

@Alias(value="FjsqDto")
public class FjsqDto extends FjsqModel{
	
	//新增检测项目名称
	private String jcxmmc;
	private String jcxmdm;
	//发送报告标记名称
	private String bgbjmc;
	//类型名称
	private String lxmc;
	//类型（多）[]
	private String[] lxs;
	//发送报告（多）[]
	private String[] bgbjs;
	//状态（多）[]
	private String[] zts;
	//录入人员名称
	private String lrrymc;
	//标本编号
	private String ybbh;
	//患者姓名
	private String hzxm;
	//年龄
	private String nl;
	//标本类型名称
	private String yblxmc;
	//合作伙伴
	private String db;
	//内部编码
	private String nbbm;
	//序号
	private String xh;
	//录入时间标记
	private String lrsjFlg;
	//开始录入时间
	private String lrsjstart;
	//结束录入时间
	private String lrsjend;
	//开始录入时间（月）
	private String lrsjMstart;
	//结束录入时间（月）
	private String lrsjMend;
	//开始录入时间（年）
	private String lrsjYstart;
	//结束录入时间（年）
	private String lrsjYend;
	//接收日期
	private String jsrq;
	//开始日期
	private String jsrqstart;
	//结束日期
	private String jsrqend;
	//开始日期(月查询)
	private String jsrqMstart;
	//结束日期(月查询)
	private String jsrqMend;
	//开始日期(年查询)
	private String jsrqYstart;
	//结束日期(年查询)
	private String jsrqYend;
	//RNA检测标记
	private String jcbj;
	//RNA实验日期
	private String syrq;
	//RNA实验日期开始日期
	private String syrqstart;
	//RNA实验日期结束日期
	private String syrqend;
	//DNA实验日期开始日期
	private String dsyrqstart;
	//DNA实验日期结束日期
	private String dsyrqend;
	//报告日期
	private String bgrq;
	//全部字段
	private String entire;
	//外部复检审核
	private String auditTypeOut;
	//钉钉复检审核
	private String auditTypeDing;
	//签名
	private String sign;
	//用戶id
	private String yhid;
	//合作伙伴（多）
	private List<String> sjhbs;
	//标记
	private String flg;
	//检测单位限制
	private List<String> jcdwxz;
	//检测单位
	private String jcdw;
	//检测项目s
	private List<String> jcxms;
	//导出sql
    private String sqlparam;
    //标本类型标记
    private String yblx_flg;
    //加测项目标记
    private String jcxm_flg;
    //录入人员标记
    private String lrry_flg;
    //状态名称
    private String ztmc;
    //基础数据扩展词
    private String ybztcskz1;
    //复检类型代码
    private String fjlxdm;
	//对应代码
	private String dydm;
    //复检类型标记
    private String fjlx_flg;
    //是否上传
    private String sfsc;
    //伙伴权限标记[0 为不限制  1为限制]
    private String auth_flag;
    //伙伴ids[用户销售进行限制伙伴]
    private List<String> hbids;
  //其他实验日期
    private String qtjcbj;
    //其他实验日期
    private String qtsyrq;
    //是否收费名称
    private  String sfffmc;
    //复检类型[多]
    private List<String> fjlxs;
    //检测单位名称
    private String jcdwmc;
    //钉钉id
    private String ddid;
    //是否收费（多）[]
  	private String[] sfffs;
  	//检测单位（多）[]
  	private String[] jcdws;
  	//钉钉加载标记：1 个人清单  2 全部清单
  	private String load_flag;
  	//用户id
  	private List<String> userids;
  	//格式化备注()
  	private String f_bz;
  	//订单编号
    private String ddbh;
	//业务类型(支付)
	private String ywlx;
	//检测项目扩展参数C,D,R
	private String jcxmcskz;
	//是否实验通知
	private String sfsytz;
	//错误ID
    private String cwid;
    //用于检索标本类型
    private String[] yblxs;  
    //用于检索检测项目
    private String[] jcxmss;
    //原因
	private String[] yys;
	//角色ID
	private String jsid;
	//检测项目cskz3
	private String jcxmcskz3;
	//单位名称/科室
	private String dwmc;
	//检测项目参数扩展1
	private String cskz1;
	//送检单位
	private String hospitalname;
	//审批人id
	private String sprid;
	//审批人姓名
	private String sprxm;
	//审批人用户名
	private String spryhm;
	//审批人钉钉id
	private String sprddid;
	//审批人角色id
	private String sprjsid;
	//审批人角色名称
	private String sprjsmc;
	//送检单位名称
	private String sjdwmc;
	//科室名称
	private String ksmc;
	//送检检测项目名称
	private String sjjcxmmc;
	//参数扩展三
	private String cskz3;
	//用户名
	private String yhm;
	//标本类型代码
	private String yblxdm;
	//送检区分代码
	private String sjqfdm;
	//接收人员
	private String jsry;
	//平台归属多
	private String[] ptgss;
	//检测子项目名称
	private String jczxmmc;
	//修改标记
	private String xg_flg;
	private String hbid;
	private List<String> wkbms;

	public List<String> getWkbms() {
		return wkbms;
	}

	public void setWkbms(List<String> wkbms) {
		this.wkbms = wkbms;
	}

	public String getHbid() {
		return hbid;
	}

	public void setHbid(String hbid) {
		this.hbid = hbid;
	}

	public String getXg_flg() {
		return xg_flg;
	}

	public void setXg_flg(String xg_flg) {
		this.xg_flg = xg_flg;
	}

	public String getJcxmdm() {
		return jcxmdm;
	}

	public void setJcxmdm(String jcxmdm) {
		this.jcxmdm = jcxmdm;
	}

	public String getJczxmmc() {
		return jczxmmc;
	}

	public void setJczxmmc(String jczxmmc) {
		this.jczxmmc = jczxmmc;
	}

	public String[] getPtgss() {
		return ptgss;
	}

	public void setPtgss(String[] ptgss) {
		this.ptgss = ptgss;
	}

	public String getJsry() {
		return jsry;
	}

	public void setJsry(String jsry) {
		this.jsry = jsry;
	}

	public String getSjqfdm() {
		return sjqfdm;
	}

	public void setSjqfdm(String sjqfdm) {
		this.sjqfdm = sjqfdm;
	}

	public String getYblxdm() {
		return yblxdm;
	}

	public void setYblxdm(String yblxdm) {
		this.yblxdm = yblxdm;
	}

	public String getYhm() {
		return yhm;
	}

	public void setYhm(String yhm) {
		this.yhm = yhm;
	}

	public String getDydm() {
		return dydm;
	}

	public void setDydm(String dydm) {
		this.dydm = dydm;
	}

	public String getCskz3() {
		return cskz3;
	}

	public void setCskz3(String cskz3) {
		this.cskz3 = cskz3;
	}

	public String getSjjcxmmc() {
		return sjjcxmmc;
	}

	public void setSjjcxmmc(String sjjcxmmc) {
		this.sjjcxmmc = sjjcxmmc;
	}

	public String getKsmc() {
		return ksmc;
	}

	public void setKsmc(String ksmc) {
		this.ksmc = ksmc;
	}

	public String getSjdwmc() {
		return sjdwmc;
	}

	public void setSjdwmc(String sjdwmc) {
		this.sjdwmc = sjdwmc;
	}

	public String getSprid() {
		return sprid;
	}

	public void setSprid(String sprid) {
		this.sprid = sprid;
	}

	public String getSprxm() {
		return sprxm;
	}

	public void setSprxm(String sprxm) {
		this.sprxm = sprxm;
	}

	public String getSpryhm() {
		return spryhm;
	}

	public void setSpryhm(String spryhm) {
		this.spryhm = spryhm;
	}

	public String getSprddid() {
		return sprddid;
	}

	public void setSprddid(String sprddid) {
		this.sprddid = sprddid;
	}

	public String getSprjsid() {
		return sprjsid;
	}

	public void setSprjsid(String sprjsid) {
		this.sprjsid = sprjsid;
	}

	public String getSprjsmc() {
		return sprjsmc;
	}

	public void setSprjsmc(String sprjsmc) {
		this.sprjsmc = sprjsmc;
	}

	public String getCskz1() {
		return cskz1;
	}

	public void setCskz1(String cskz1) {
		this.cskz1 = cskz1;
	}

	public String getDwmc() {
		return dwmc;
	}

	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}

	public String getHospitalname() {
		return hospitalname;
	}

	public void setHospitalname(String hospitalname) {
		this.hospitalname = hospitalname;
	}

	public String getJcxmcskz3() {
		return jcxmcskz3;
	}

	public void setJcxmcskz3(String jcxmcskz3) {
		this.jcxmcskz3 = jcxmcskz3;
	}

	public String getJsid() {
		return jsid;
	}

	public void setJsid(String jsid) {
		this.jsid = jsid;
	}

	public String[] getYys() {
		return yys;
	}

	public void setYys(String[] yys) {
		this.yys = yys;
	}

	public String getSyrqstart() {
		return syrqstart;
	}

	public void setSyrqstart(String syrqstart) {
		this.syrqstart = syrqstart;
	}

	public String getSyrqend() {
		return syrqend;
	}

	public void setSyrqend(String syrqend) {
		this.syrqend = syrqend;
	}

	public String getDsyrqstart() {
		return dsyrqstart;
	}

	public void setDsyrqstart(String dsyrqstart) {
		this.dsyrqstart = dsyrqstart;
	}

	public String getDsyrqend() {
		return dsyrqend;
	}

	public void setDsyrqend(String dsyrqend) {
		this.dsyrqend = dsyrqend;
	}

	public String[] getJcxmss() {
		return jcxmss;
	}

	public void setJcxmss(String[] jcxmss) {
		this.jcxmss = jcxmss;
		for(int i=0; i<jcxmss.length; i++) {
			this.jcxmss[i] = this.jcxmss[i].replace("'", "");
		}
	}

	public String[] getYblxs() {
		return yblxs;
	}

	public void setYblxs(String[] yblxs) {
		this.yblxs = yblxs;
		for(int i=0; i<yblxs.length; i++) {
			this.yblxs[i] = this.yblxs[i].replace("'", "");
		}
	}

	public String getCwid() {
		return cwid;
	}

	public void setCwid(String cwid) {
		this.cwid = cwid;
	}

	public String getSfsytz() {
		return sfsytz;
	}

	public void setSfsytz(String sfsytz) {
		this.sfsytz = sfsytz;
	}

	public String getJcxmcskz() {
		return jcxmcskz;
	}

	public void setJcxmcskz(String jcxmcskz) {
		this.jcxmcskz = jcxmcskz;
	}

	public String getQtjcbj() {
		return qtjcbj;
	}

	public void setQtjcbj(String qtjcbj) {
		this.qtjcbj = qtjcbj;
	}

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}
  	
	public String getDdbh() {
		return ddbh;
	}

	public void setDdbh(String ddbh) {
		this.ddbh = ddbh;
	}

	public String getSfsc() {
		return sfsc;
	}

	public void setSfsc(String sfsc) {
		this.sfsc = sfsc;
	}

	public String getYbztcskz1() {
		return ybztcskz1;
	}

	public void setYbztcskz1(String ybztcskz1) {
		this.ybztcskz1 = ybztcskz1;
	}

	public List<String> getJcxms() {
		return jcxms;
	}

	public void setJcxms(List<String> jcxms) {
		this.jcxms = jcxms;
	}

	public String getJcdw() {
		return jcdw;
	}

	public void setJcdw(String jcdw) {
		this.jcdw = jcdw;
	}

	public List<String> getJcdwxz() {
		return jcdwxz;
	}

	public void setJcdwxz(List<String> jcdwxz) {
		this.jcdwxz = jcdwxz;
	}

	public String getFlg()
	{
		return flg;
	}

	public void setFlg(String flg)
	{
		this.flg = flg;
	}

	public List<String> getSjhbs()
	{
		return sjhbs;
	}

	public void setSjhbs(List<String> sjhbs)
	{
		this.sjhbs = sjhbs;
	}

	public String getYhid()
	{
		return yhid;
	}

	public void setYhid(String yhid)
	{
		this.yhid = yhid;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getAuditTypeDing() {
		return auditTypeDing;
	}

	public void setAuditTypeDing(String auditTypeDing) {
		this.auditTypeDing = auditTypeDing;
	}

	public String getAuditTypeOut() {
		return auditTypeOut;
	}

	public void setAuditTypeOut(String auditTypeOut) {
		this.auditTypeOut = auditTypeOut;
	}

	public String getEntire()
	{
		return entire;
	}

	public void setEntire(String entire)
	{
		this.entire = entire;
	}

	public String getJsrq()
	{
		return jsrq;
	}

	public void setJsrq(String jsrq)
	{
		this.jsrq = jsrq;
	}

	public String getJcbj()
	{
		return jcbj;
	}

	public void setJcbj(String jcbj)
	{
		this.jcbj = jcbj;
	}

	public String getSyrq()
	{
		return syrq;
	}

	public void setSyrq(String syrq)
	{
		this.syrq = syrq;
	}



	public String getLrsjMstart()
	{
		return lrsjMstart;
	}

	public String getJsrqstart()
	{
		return jsrqstart;
	}

	public void setJsrqstart(String jsrqstart)
	{
		this.jsrqstart = jsrqstart;
	}

	public String getJsrqend()
	{
		return jsrqend;
	}

	public void setJsrqend(String jsrqend)
	{
		this.jsrqend = jsrqend;
	}

	public String getJsrqMstart()
	{
		return jsrqMstart;
	}

	public void setJsrqMstart(String jsrqMstart)
	{
		this.jsrqMstart = jsrqMstart;
	}

	public String getJsrqMend()
	{
		return jsrqMend;
	}

	public void setJsrqMend(String jsrqMend)
	{
		this.jsrqMend = jsrqMend;
	}

	public String getJsrqYstart()
	{
		return jsrqYstart;
	}

	public void setJsrqYstart(String jsrqYstart)
	{
		this.jsrqYstart = jsrqYstart;
	}

	public String getJsrqYend()
	{
		return jsrqYend;
	}

	public void setJsrqYend(String jsrqYend)
	{
		this.jsrqYend = jsrqYend;
	}

	public void setLrsjMstart(String lrsjMstart){
		this.lrsjMstart = lrsjMstart;
		if("null".equals(this.lrsjMstart) || this.lrsjMstart=="") {
			this.lrsjMstart="";
		}
		
	}

	public String getLrsjMend()
	{
		return lrsjMend;
	}

	public void setLrsjMend(String lrsjMend){
		this.lrsjMend = lrsjMend;
		if("null".equals(this.lrsjMend) || this.lrsjMend=="") {
			this.lrsjMend="";
		}
	}

	public String getLrsjYstart()
	{
		return lrsjYstart;
	}

	public void setLrsjYstart(String lrsjYstart){
		this.lrsjYstart = lrsjYstart;
		if("null".equals(this.lrsjYstart) ||this.lrsjYstart=="") {
			this.lrsjYstart="";
		}
	}

	public String getLrsjYend()
	{
		return lrsjYend;
	}

	public void setLrsjYend(String lrsjYend){
		this.lrsjYend = lrsjYend;
		if("null".equals(this.lrsjYend) ||this.lrsjYend=="") {
			this.lrsjYend="";
		}
	}

	public String getLrsjFlg()
	{
		return lrsjFlg;
	}

	public void setLrsjFlg(String lrsjFlg)
	{
		this.lrsjFlg = lrsjFlg;
	}

	public String getLrsjstart()
	{
		return lrsjstart;
	}

	public void setLrsjstart(String lrsjstart){
		this.lrsjstart = lrsjstart;
		if("null".equals(this.lrsjstart) || this.lrsjstart=="") {
			this.lrsjstart="";
		}
	}

	public String getLrsjend(){
		return lrsjend;
	}

	public void setLrsjend(String lrsjend){
		this.lrsjend = lrsjend;
		if("null".equals(this.lrsjend) || this.lrsjend=="") {
			this.lrsjend="";
		}
	}

	public String getXh()
	{
		return xh;
	}

	public void setXh(String xh)
	{
		this.xh = xh;
	}

	public String getNbbm()
	{
		return nbbm;
	}

	public void setNbbm(String nbbm)
	{
		this.nbbm = nbbm;
	}

	public String getDb()
	{
		return db;
	}

	public void setDb(String db)
	{
		this.db = db;
	}

	public String getYbbh(){
		return ybbh;
	}

	public void setYbbh(String ybbh){
		this.ybbh = ybbh;
	}

	public String getHzxm(){
		return hzxm;
	}

	public void setHzxm(String hzxm){
		this.hzxm = hzxm;
	}

	public String getNl(){
		return nl;
	}

	public void setNl(String nl){
		this.nl = nl;
	}

	public String getYblxmc(){
		return yblxmc;
	}

	public void setYblxmc(String yblxmc){
		this.yblxmc = yblxmc;
	}

	public String getLrrymc(){
		return lrrymc;
	}

	public void setLrrymc(String lrrymc){
		this.lrrymc = lrrymc;
	}

	public String getLxmc(){
		return lxmc;
	}

	public void setLxmc(String lxmc){
		this.lxmc = lxmc;
	}


	public String getBgbjmc(){
		return bgbjmc;
	}


	public void setBgbjmc(String bgbjmc){
		this.bgbjmc = bgbjmc;
	}


	public String getJcxmmc(){
		return jcxmmc;
	}


	public void setJcxmmc(String jcxmmc){
		this.jcxmmc = jcxmmc;
	}

	public String[] getLxs()
	{
		return lxs;
	}

	public void setLxs(String[] lxs){
		this.lxs = lxs;
		for(int i=0;i<this.lxs.length;i++){
			if(StringUtil.isNotBlank(this.lxs[i]))
				this.lxs[i] = this.lxs[i].replace("'", "");
		}
	}

	public String[] getBgbjs()
	{
		return bgbjs;
	}

	public void setBgbjs(String[] bgbjs)
	{
		this.bgbjs = bgbjs;
		for(int i=0;i<this.bgbjs.length;i++){
			if(StringUtil.isNotBlank(this.bgbjs[i]))
				this.bgbjs[i] = this.bgbjs[i].replace("'", "");
		}
	}

	public String[] getZts()
	{
		return zts;
	}

	public void setZts(String[] zts)
	{
		this.zts = zts;
		for(int i=0;i<this.zts.length;i++){
			if(StringUtil.isNotBlank(this.zts[i]))
			this.zts[i] = this.zts[i].replace("'", "");
		}
	}

	public String getBgrq() {
		return bgrq;
	}

	public void setBgrq(String bgrq) {
		this.bgrq = bgrq;
	}

	public String getSqlparam() {
		return sqlparam;
	}

	public void setSqlparam(String sqlparam) {
		this.sqlparam = sqlparam;
	}

	public String getYblx_flg() {
		return yblx_flg;
	}

	public void setYblx_flg(String yblx_flg) {
		this.yblx_flg = yblx_flg;
	}

	public String getJcxm_flg() {
		return jcxm_flg;
	}

	public void setJcxm_flg(String jcxm_flg) {
		this.jcxm_flg = jcxm_flg;
	}

	public String getZtmc() {
		return ztmc;
	}

	public void setZtmc(String ztmc) {
		this.ztmc = ztmc;
	}

	public String getLrry_flg() {
		return lrry_flg;
	}

	public void setLrry_flg(String lrry_flg) {
		this.lrry_flg = lrry_flg;
	}

	public String getFjlxdm() {
		return fjlxdm;
	}

	public void setFjlxdm(String fjlxdm) {
		this.fjlxdm = fjlxdm;
	}

	public String getFjlx_flg() {
		return fjlx_flg;
	}

	public void setFjlx_flg(String fjlx_flg) {
		this.fjlx_flg = fjlx_flg;
	}

	public String getAuth_flag() {
		return auth_flag;
	}

	public void setAuth_flag(String auth_flag) {
		this.auth_flag = auth_flag;
	}

	public List<String> getHbids() {
		return hbids;
	}

	public void setHbids(List<String> hbids) {
		this.hbids = hbids;
	}

	public String getQtsyrq() {
		return qtsyrq;
	}

	public void setQtsyrq(String qtsyrq) {
		this.qtsyrq = qtsyrq;
	}

	public String getSfffmc() {
		return sfffmc;
	}

	public void setSfffmc(String sfffmc) {
		this.sfffmc = sfffmc;
	}

	public List<String> getFjlxs() {
		return fjlxs;
	}

	public void setFjlxs(List<String> fjlxs) {
		this.fjlxs = fjlxs;
	}

	public String getJcdwmc() {
		return jcdwmc;
	}

	public void setJcdwmc(String jcdwmc) {
		this.jcdwmc = jcdwmc;
	}

	public String getDdid() {
		return ddid;
	}

	public void setDdid(String ddid) {
		this.ddid = ddid;
	}

	public String[] getSfffs() {
		return sfffs;
	}

	public void setSfffs(String[] sfffs) {
		this.sfffs = sfffs;
		for(int i=0;i<this.sfffs.length;i++){
			this.sfffs[i] = this.sfffs[i].replace("'", "");
		}
	}

	public String[] getJcdws() {
		return jcdws;
	}

	public void setJcdws(String[] jcdws) {
		this.jcdws = jcdws;
		for(int i=0;i<this.jcdws.length;i++){
			this.jcdws[i] = this.jcdws[i].replace("'", "");
		}
	}
	public String getLoad_flag() {
		return load_flag;
	}

	public void setLoad_flag(String load_flag) {
		this.load_flag = load_flag;
	}

	public List<String> getUserids() {
		return userids;
	}

	public void setUserids(List<String> userids) {
		this.userids = userids;
	}

	public String getF_bz() {
		return f_bz;
	}

	public void setF_bz(String f_bz) {
		this.f_bz = f_bz;
	}




	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
