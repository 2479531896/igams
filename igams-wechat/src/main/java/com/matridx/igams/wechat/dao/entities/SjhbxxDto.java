package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="SjhbxxDto")
public class SjhbxxDto extends SjhbxxModel{
	//伙伴收费标准
	private List<HbsfbzDto> hbsfbzs;
	//送检项目
	private List<SjjcxmDto> sjxms;
	//收费标准
	private String sfbz;
	//实验室id
	private String sysid;
	//省份名称
	private String sfmc;
	//城市名称
	private String csmc;
	//用户名
	private String yhm;
	//系统用户id
	private String xtyhid;
	//系统用户名
	private String xtyhm;
	//省份
	private String province;
	//城市
	private String city;
	//钉钉ID
	private String ddid;
	//报告模板名称
	private String bgmbmc;
	//代表
	private String db;
	//参数扩展1名称
	private String cskz1mc;
	//参数扩展2名称
	private String cskz2mc;
	//分类
	private String fl;
	//分类代码
	private String fldm;
	//子分类
	private String zfl;
	//子分类代码
	private String zfldm;
	//分类名称
	private String flmc;
	//子分类名称
	private String zflmc;
	//基础类别
	private String jclb;
	//合作伙伴列表
	private List<Sjhbxx_Dto> sjhblist;
	//子分类列表
	private List<Sjhbxx_Dto> zfllist;
	//盖章类型名称
	private String gzlxmc;
	//参数扩展1
	private String cskz1;
	//盖章扩展1
	private String gzcskz1;
	//报告方式名称
	private String bgfsmc;
	//分文件路径
	private String fwjlj;
	//分文件名
	private String fwjm;
	//业务类型
	private String ywlx;
	//删除标记数组
	private String[] scbjs;
	//伙伴分类参数扩展2(第三方强制收费)
	private String flcskz2;
    //jcdwids,实际取的就是csid
	private String[] jcdwids;
	//分类 多 数组
	private String[] fls;
	//子分类 多  数组
	private String[] zfls;
	//报告模板 多 数组
	private String[] bgmbs;
	//伙伴列表显示选择的检测单位(检测单位可多个，行转列)
	private String jcdwstr;
	//参数扩展3名称
	private String cskz3mc;
	//导出关联标记位
	//所选择的字段
	private String SqlParam;
	//角色检测单位限制
	private List<String> jcdwxz;
	//报告模板Json
	private String bgmb_json;
	//收费标准Json
	private String sfbz_json;
	//送检区分每次
	private String sjqfmc;
	//用于传输伙伴ID，前端使用
	private String t_xzrys;
	//外部安全代号
	private String code;
	//岗位名称
	private String gwmc;
	//大区真实姓名
	private String zsxm;
	//业务部门
	private String ptgsmc;

	private String xsbmmc;
	private List<String> khids;//客户ids
	private String[] ptgss;

	private String[] xsbms;
	private String[] sfs;
	private String hzgs;//合作公司
	private String htbh;//合同编号
	private String xm;//项目
	private String ksrq;
	private String ksrqstart;//开始日期
	private String ksrqend;//开始日期
	private String jsrq;
	private String jsrqstart;//结束日期
	private String jsrqend;//结束日期
	private String sfyx;//是否有效
	private String sfyxmc;//是否有效
	private String[] xms;//项目
	private String zykh;//主要客户
	private String hbjbmc;//伙伴级别名称
	//平台归属名称
	private String yptgsmc;
	//平台归属s
	private String[] yptgss;
	//扩展设置
	private String kzsz;
	//自动发送名称
	private String zdfsmc;
	//自动发送mNGS
	private String zdfs;
	//自动发送tNGS
	private String zdfst;
	//自动发送tNGS名称
	private String zdfstmc;
	//限制类型  X伙伴的限制类型，主要针对，X,Y等
	private String xzlx;

	public String getZdfstmc() {
		return zdfstmc;
	}

	public void setZdfstmc(String zdfstmc) {
		this.zdfstmc = zdfstmc;
	}

	public String getZdfst() {
		return zdfst;
	}

	public void setZdfst(String zdfst) {
		this.zdfst = zdfst;
	}

	public String getKzsz() {
		return kzsz;
	}

	public void setKzsz(String kzsz) {
		this.kzsz = kzsz;
	}

	public String[] getYptgss() {
		return yptgss;
	}

	public void setYptgss(String[] yptgss) {
		this.yptgss = yptgss;
	}

	public String getYptgsmc() {
		return yptgsmc;
	}

	public void setYptgsmc(String yptgsmc) {
		this.yptgsmc = yptgsmc;
	}
	public String getHbjbmc() {
		return hbjbmc;
	}

	public void setHbjbmc(String hbjbmc) {
		this.hbjbmc = hbjbmc;
	}

	public String getZykh() {
		return zykh;
	}

	public void setZykh(String zykh) {
		this.zykh = zykh;
	}

	public String getSfyxmc() {
		return sfyxmc;
	}

	public void setSfyxmc(String sfyxmc) {
		this.sfyxmc = sfyxmc;
	}

	public String getJsrq() {
		return jsrq;
	}

	public void setJsrq(String jsrq) {
		this.jsrq = jsrq;
	}

	public String getKsrq() {
		return ksrq;
	}

	public void setKsrq(String ksrq) {
		this.ksrq = ksrq;
	}

	public String getKsrqstart() {
		return ksrqstart;
	}

	public void setKsrqstart(String ksrqstart) {
		this.ksrqstart = ksrqstart;
	}

	public String getKsrqend() {
		return ksrqend;
	}

	public void setKsrqend(String ksrqend) {
		this.ksrqend = ksrqend;
	}

	public String getJsrqstart() {
		return jsrqstart;
	}

	public void setJsrqstart(String jsrqstart) {
		this.jsrqstart = jsrqstart;
	}

	public String getJsrqend() {
		return jsrqend;
	}

	public void setJsrqend(String jsrqend) {
		this.jsrqend = jsrqend;
	}

	public String getHtbh() {
		return htbh;
	}

	public void setHtbh(String htbh) {
		this.htbh = htbh;
	}

	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}
	public String getSfyx() {
		return sfyx;
	}

	public void setSfyx(String sfyx) {
		this.sfyx = sfyx;
	}

	public String[] getXms() {
		return xms;
	}

	public void setXms(String[] xms) {
		this.xms = xms;
	}

	public String getHzgs() {
		return hzgs;
	}

	public void setHzgs(String hzgs) {
		this.hzgs = hzgs;
	}

	public String getSfbz_json() {
		return sfbz_json;
	}

	public void setSfbz_json(String sfbz_json) {
		this.sfbz_json = sfbz_json;
	}

	public List<String> getKhids() {
		return khids;
	}

	public void setKhids(List<String> khids) {
		this.khids = khids;
	}

	public String[] getPtgss() {
		return ptgss;
	}

	public void setPtgss(String[] ptgss) {
		this.ptgss = ptgss;
	}
	public String[] getSfs() {
		return sfs;
	}

	public void setSfs(String[] sfs) {
		this.sfs = sfs;
	}

	public String getPtgsmc() {
		return ptgsmc;
	}

	public void setPtgsmc(String ptgsmc) {
		this.ptgsmc = ptgsmc;
	}

	public String getSysid() {
		return sysid;
	}

	public void setSysid(String sysid) {
		this.sysid = sysid;
	}

	public String getZsxm() {
		return zsxm;
	}

	public void setZsxm(String zsxm) {
		this.zsxm = zsxm;
	}
	public String getGwmc() {
		return gwmc;
	}

	public void setGwmc(String gwmc) {
		this.gwmc = gwmc;
	}
	public String getT_xzrys() {
		return t_xzrys;
	}

	public void setT_xzrys(String t_xzrys) {
		this.t_xzrys = t_xzrys;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSjqfmc() {
		return sjqfmc;
	}

	public void setSjqfmc(String sjqfmc) {
		this.sjqfmc = sjqfmc;
	}

	public String getBgmb_json() {
		return bgmb_json;
	}

	public void setBgmb_json(String bgmb_json) {
		this.bgmb_json = bgmb_json;
	}

	public List<String> getJcdwxz() {
		return jcdwxz;
	}

	public void setJcdwxz(List<String> jcdwxz) {
		this.jcdwxz = jcdwxz;
	}

	public String getSqlParam() {
		return SqlParam;
	}

	public void setSqlParam(String sqlParam) {
		SqlParam = sqlParam;
	}

	public String getCskz3mc() {
		return cskz3mc;
	}

	public void setCskz3mc(String cskz3mc) {
		this.cskz3mc = cskz3mc;
	}

	public String getJcdwstr() {
		return jcdwstr;
	}
	public void setJcdwstr(String jcdwstr) {
		this.jcdwstr = jcdwstr;
	}
	public String[] getFls() {
		return fls;
	}
	public void setFls(String[] fls) {
		this.fls = fls;
		for (int i = 0; i < fls.length; i++){
			this.fls[i]=this.fls[i].replace("'","");
		}
	}
	public String[] getZfls() {
		return zfls;
	}
	public void setZfls(String[] zfls) {
		this.zfls = zfls;
		for (int i = 0; i < zfls.length; i++){
			this.zfls[i]=this.zfls[i].replace("'","");
		}
	}
	public String[] getBgmbs() {
		return bgmbs;
	}
	public void setBgmbs(String[] bgmbs) {
		this.bgmbs = bgmbs;
		for (int i = 0; i < bgmbs.length; i++){
			this.bgmbs[i]=this.bgmbs[i].replace("'","");
		}
	}
	public String[] getJcdwids() {
		return jcdwids;
	}
	public void setJcdwids(String[] jcdwids) {
		this.jcdwids = jcdwids;
	}	
	public String getFlcskz2() {
		return flcskz2;
	}
	public void setFlcskz2(String flcskz2) {
		this.flcskz2 = flcskz2;
	}
	public String[] getScbjs() {
		return scbjs;
	}
	public void setScbjs(String[] scbjs) {
		this.scbjs = scbjs;
		for (int i = 0; i < scbjs.length; i++) {
			this.scbjs[i] = this.scbjs[i].replace("'", "");
		}
	}
	public String getFwjlj() {
		return fwjlj;
	}
	public void setFwjlj(String fwjlj) {
		this.fwjlj = fwjlj;
	}
	public String getFwjm() {
		return fwjm;
	}
	public void setFwjm(String fwjm) {
		this.fwjm = fwjm;
	}
	public String getGzcskz1() {
		return gzcskz1;
	}
	public void setGzcskz1(String gzcskz1) {
		this.gzcskz1 = gzcskz1;
	}
	public String getCskz1()
	{
		return cskz1;
	}
	public void setCskz1(String cskz1)
	{
		this.cskz1 = cskz1;
	}
	public String getGzlxmc()
	{
		return gzlxmc;
	}
	public void setGzlxmc(String gzlxmc)
	{
		this.gzlxmc = gzlxmc;
	}
	public List<Sjhbxx_Dto> getSjhblist() {
		return sjhblist;
	}
	public void setSjhblist(List<Sjhbxx_Dto> sjhblist) {
		this.sjhblist = sjhblist;
	}
	public List<Sjhbxx_Dto> getZfllist() {
		return zfllist;
	}
	public void setZfllist(List<Sjhbxx_Dto> zfllist) {
		this.zfllist = zfllist;
	}
	public String getJclb() {
		return jclb;
	}
	public void setJclb(String jclb) {
		this.jclb = jclb;
	}
	public String getFlmc()
	{
		return flmc;
	}
	public void setFlmc(String flmc)
	{
		this.flmc = flmc;
	}
	public String getZflmc()
	{
		return zflmc;
	}
	public void setZflmc(String zflmc)
	{
		this.zflmc = zflmc;
	}
	public String getFl()
	{
		return fl;
	}
	public void setFl(String fl)
	{
		this.fl = fl;
	}
	public String getZfl()
	{
		return zfl;
	}
	public void setZfl(String zfl)
	{
		this.zfl = zfl;
	}
	public String getCskz1mc()
	{
		return cskz1mc;
	}
	public void setCskz1mc(String cskz1mc)
	{
		this.cskz1mc = cskz1mc;
	}
	public String getCskz2mc()
	{
		return cskz2mc;
	}
	public void setCskz2mc(String cskz2mc)
	{
		this.cskz2mc = cskz2mc;
	}
	public String getDb() {
		return db;
	}
	public void setDb(String db) {
		this.db = db;
	}
	public String getProvince()
	{
		return province;
	}
	public void setProvince(String province)
	{
		this.province = province;
	}
	public String getCity()
	{
		return city;
	}
	public void setCity(String city)
	{
		this.city = city;
	}
	public List<HbsfbzDto> getHbsfbzs(){
		return hbsfbzs;
	}
	public void setHbsfbzs(List<HbsfbzDto> hbsfbzs){
		this.hbsfbzs = hbsfbzs;
	}

	public String getYhm(){
		return yhm;
	}
	public void setYhm(String yhm){
		this.yhm = yhm;
	}
	public String getXtyhid()
	{
		return xtyhid;
	}
	public void setXtyhid(String xtyhid)
	{
		this.xtyhid = xtyhid;
	}
	public String getXtyhm()
	{
		return xtyhm;
	}
	public void setXtyhm(String xtyhm)
	{
		this.xtyhm = xtyhm;
	}

	public List<SjjcxmDto> getSjxms() {
		return sjxms;
	}
	public void setSjxms(List<SjjcxmDto> sjxms) {
		this.sjxms = sjxms;
	}
	public String getSfbz() {
		return sfbz;
	}
	public void setSfbz(String sfbz) {
		this.sfbz = sfbz;
	}
	public String getSfmc() {
		return sfmc;
	}
	public void setSfmc(String sfmc) {
		this.sfmc = sfmc;
	}
	public String getCsmc() {
		return csmc;
	}
	public void setCsmc(String csmc) {
		this.csmc = csmc;
	}
	public String getDdid() {
		return ddid;
	}
	public void setDdid(String ddid) {
		this.ddid = ddid;
	}
	public String getBgmbmc() {
		return bgmbmc;
	}
	public void setBgmbmc(String bgmbmc) {
		this.bgmbmc = bgmbmc;
	}



	public String getFldm() {
		return fldm;
	}
	public void setFldm(String fldm) {
		this.fldm = fldm;
	}



	public String getZfldm() {
		return zfldm;
	}
	public void setZfldm(String zfldm) {
		this.zfldm = zfldm;
	}

	public String getBgfsmc() {
		return bgfsmc;
	}
	public void setBgfsmc(String bgfsmc) {
		this.bgfsmc = bgfsmc;
	}


	public String getYwlx() {
		return ywlx;
	}
	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getXsbmmc() {
		return xsbmmc;
	}

	public void setXsbmmc(String xsbmmc) {
		this.xsbmmc = xsbmmc;
	}

	public String[] getXsbms() {
		return xsbms;
	}

	public void setXsbms(String[] xsbms) {
		this.xsbms = xsbms;
	}

	public String getZdfsmc() {
		return zdfsmc;
	}

	public void setZdfsmc(String zdfsmc) {
		this.zdfsmc = zdfsmc;
	}

	public String getZdfs() {
		return zdfs;
	}

	public void setZdfs(String zdfs) {
		this.zdfs = zdfs;
	}

    public String getXzlx() {
        return xzlx;
    }

    public void setXzlx(String xzlx) {
        this.xzlx = xzlx;
    }
}
