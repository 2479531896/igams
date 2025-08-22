package com.matridx.igams.common.dao.entities;

import com.matridx.springboot.util.base.StringUtil;
import org.apache.ibatis.type.Alias;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Alias(value="SjycDto")
public class SjycDto extends SjycModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//附件ID复数
	private List<String> fjids;
	//附件ID
	private String fjid;
	//标本类型名称
	private String yblxmc;
	//标本编号
	private String ybbh;
	//提问人名称
	private String twrmc;
	//确认人名称
	private String qrrmc;
	//钉钉id
	private String ddid;
	//患者姓名
	private String hzxm;
	//代表
	private String db;
	//类型名称
	private String lxmc;
	//子类型名称
	private String zlxmc;
	//是否结束名称
	private String sfjsmc;
	//评论数
	private String pls;
	//默认提问人
	private String mrtwr;
	//默认提问人名称
	private String mrtwrmc;
	//业务类型
	private String ywlx;
	//导出sql
	private String sqlParam;
	//类型flg
	private String lx_flg;
	//子类型flg
	private String zlx_flg;
	//提问人flg
	private String twr_flg;
	//确认人flg
	private String qrr_flg;
	//标本类型flg
	private String yblxmc_flg;
	//送检信息
	private String sjxx_flg;
	//通知类型
	private String tzlx;
	//通知人员
	private List<String> tzrys;
	//通知人员
	private String str_tzrys;
	//用户id（传递用户信息）
	private String yhid;
	//微信ID
	private String wxid;
	//全部查询内容
	private String entire;
	//列表加载的条数
	private String count;
	//从第几条开始
	private String start;
	//钉钉头像路径
	private String twrtxlj;
	//是否置顶
	private String sfzd;
	//置顶sj
	private String zdsj;
	//置顶人员
	private String zdry;
	//机构ID
	private String jgid;
	//确认类型名称
	private String qrlxmc;
	//检测项目名称
	private String jcxmmc;
	//检测单位名称
	private String jcdwmc;
	//访问标记
	private String fwbj;
	//真实姓名
	private String zsxm;
	//角色id
	private String jsid;
	//用户角色s
	private List<String> yhjss;
	//附件数
	private String fjs;
	//附件保存标记
	private String fjbcbj;
	//转发人员名称
	private String zfrymc;
//人员id
	private String ryid;
	//是否结束多
	private String[] sfjss;
	//创建时间
	private String cjsjstart;
	private String cjsjend;
	//最后回复时间
	private String zhhfsjstart;
	private String zhhfsjend;
	//通知人员
	private String tzry;
	//用户名
	private String yhm;
	//默认提问人角色
	private String mrtwrjs;
	//检测单位s
	private String[] jcdws;
	//异常类型s
	private String[] yclxs;
	//录入人员名称
	private String lrrymc;
	//角色检测单位限制
	private List<String> jcdwxz;
	//未读个数
	private String unreadnum;
	//异常区分代码
	private String ycqfdm;
	private String dwxdbj;
	private String jcdw;
	//异常区分代码
	private String ycqfmc;
	private String sjid;
	private String yczqfdm;
	private String yczqfmc;
	private String wlmc;
	private String wlbm;
	private String gg;
	private String scph;
	private String sl;
	private String shrq;
	private String glid;
	//异常区分s
	private String[] ycqfs;
	//反馈内容
	private  String fknr;
	//提问人类型名称
	private String twrlxmc;
	//是否评价
	private String sfpj;
	//评价星级
	private String pjxj;
	//评价内容
	private String pjnr;
	//是否解决
	private String sfjj;
	//用于个人清单判断lrry [yhid,ddid,wxid]
	private List<String> userids;
	//合作伙伴集合
	private List<String> sjhbs;
	//结束人员名称
	private String jsrmc;
	/**
	 * 异常区分标记 用于不同菜单
	 */
	private String ycqf_flg;

	private List<String> sjycstatisticsids;
	//内部编码
	private String nbbm;
	//年龄
	private String nl;
	//送检是否接收
	private String sjsfjs;
	//接收日期
	private String jsrq;

	//sjids
	private List<String> sjids;

	//异常id
	private String ycid;

	@Override
	public String getYcid() {
		return ycid;
	}

	@Override
	public void setYcid(String ycid) {
		this.ycid = ycid;
	}

	public String getJsrq() {
		return jsrq;
	}

	public void setJsrq(String jsrq) {
		this.jsrq = jsrq;
	}

	public String getSjsfjs() {
		return sjsfjs;
	}

	public void setSjsfjs(String sjsfjs) {
		this.sjsfjs = sjsfjs;
	}

	public String getNbbm() {
		return nbbm;
	}

	public void setNbbm(String nbbm) {
		this.nbbm = nbbm;
	}

	public String getNl() {
		return nl;
	}

	public void setNl(String nl) {
		this.nl = nl;
	}

	public String getSfjj() {
		return sfjj;
	}

	public void setSfjj(String sfjj) {
		this.sfjj = sfjj;
	}

	public String getSfpj() {
		return sfpj;
	}

	public void setSfpj(String sfpj) {
		this.sfpj = sfpj;
	}

	public String getPjxj() {
		return pjxj;
	}

	public void setPjxj(String pjxj) {
		this.pjxj = pjxj;
	}

	public String getPjnr() {
		return pjnr;
	}

	public void setPjnr(String pjnr) {
		this.pjnr = pjnr;
	}

	public List<String> getSjhbs()
	{
		return sjhbs;
	}
	public void setSjhbs(List<String> sjhbs)
	{
		this.sjhbs = sjhbs;
	}
	public List<String> getUserids() {
		return userids;
	}

	public void setUserids(List<String> userids) {
		this.userids = userids;
	}
	public String[] getYcqfs() {
		return ycqfs;
	}

	public void setYcqfs(String[] ycqfs) {
		this.ycqfs = ycqfs;
		for (int i = 0; i <ycqfs.length; i++){
			this.ycqfs[i]=this.ycqfs[i].replace("'","");
		}
	}

	public String getGlid() {
		return glid;
	}

	public void setGlid(String glid) {
		this.glid = glid;
	}

	public String getWlbm() {
		return wlbm;
	}

	public void setWlbm(String wlbm) {
		this.wlbm = wlbm;
	}

	public String getGg() {
		return gg;
	}

	public void setGg(String gg) {
		this.gg = gg;
	}

	public String getScph() {
		return scph;
	}

	public void setScph(String scph) {
		this.scph = scph;
	}

	public String getSl() {
		return sl;
	}

	public void setSl(String sl) {
		this.sl = sl;
	}

	public String getShrq() {
		return shrq;
	}

	public void setShrq(String shrq) {
		this.shrq = shrq;
	}

	public String getYczqfdm() {
		return yczqfdm;
	}

	public void setYczqfdm(String yczqfdm) {
		this.yczqfdm = yczqfdm;
	}

	public String getYczqfmc() {
		return yczqfmc;
	}

	public void setYczqfmc(String yczqfmc) {
		this.yczqfmc = yczqfmc;
	}

	public String getWlmc() {
		return wlmc;
	}

	public void setWlmc(String wlmc) {
		this.wlmc = wlmc;
	}

	public String getSjid() {
		return sjid;
	}

	public void setSjid(String sjid) {
		this.sjid = sjid;
	}

	public String getYcqfmc() {
		return ycqfmc;
	}

	public void setYcqfmc(String ycqfmc) {
		this.ycqfmc = ycqfmc;
	}

	public String getDwxdbj() {
		return dwxdbj;
	}

	public void setDwxdbj(String dwxdbj) {
		this.dwxdbj = dwxdbj;
	}

	public String getJcdw() {
		return jcdw;
	}

	public void setJcdw(String jcdw) {
		this.jcdw = jcdw;
	}

	private String gzpt;

	public String getGzpt() {
		return gzpt;
	}

	public void setGzpt(String gzpt) {
		this.gzpt = gzpt;
	}
	public String getYcqfdm() {
		return ycqfdm;
	}

	public void setYcqfdm(String ycqfdm) {
		this.ycqfdm = ycqfdm;
	}

	public String getUnreadnum() {
		return unreadnum;
	}

	public void setUnreadnum(String unreadnum) {
		this.unreadnum = unreadnum;
	}

	public List<String> getJcdwxz() {
		return jcdwxz;
	}

	public void setJcdwxz(List<String> jcdwxz) {
		this.jcdwxz = jcdwxz;
	}

	public String getLrrymc() {
		return lrrymc;
	}

	public void setLrrymc(String lrrymc) {
		this.lrrymc = lrrymc;
	}

	public String getMrtwrjs() {
		return mrtwrjs;
	}

	public void setMrtwrjs(String mrtwrjs) {
		this.mrtwrjs = mrtwrjs;
	}

	public String getYhm() {
		return yhm;
	}

	public void setYhm(String yhm) {
		this.yhm = yhm;
	}

	public String getTzry() {
		return tzry;
	}

	public void setTzry(String tzry) {
		this.tzry = tzry;
	}

	public String getCjsjstart() {
		return cjsjstart;
	}

	public void setCjsjstart(String cjsjstart) {
		this.cjsjstart = cjsjstart;
	}

	public String getCjsjend() {
		return cjsjend;
	}

	public void setCjsjend(String cjsjend) {
		this.cjsjend = cjsjend;
	}

	public String getZhhfsjstart() {
		return zhhfsjstart;
	}

	public void setZhhfsjstart(String zhhfsjstart) {
		this.zhhfsjstart = zhhfsjstart;
	}

	public String getZhhfsjend() {
		return zhhfsjend;
	}

	public void setZhhfsjend(String zhhfsjend) {
		this.zhhfsjend = zhhfsjend;
	}

	public String[] getSfjss() {
		return sfjss;
	}

	public void setSfjss(String[] sfjss) {
		this.sfjss = sfjss;
	}

	public String getRyid() {
		return ryid;
	}

	public void setRyid(String ryid) {
		this.ryid = ryid;
	}

	public String getZfrymc() {
		return zfrymc;
	}

	public void setZfrymc(String zfrymc) {
		this.zfrymc = zfrymc;
	}
	//判断是否是个人列表
	private String personal_flg;
	//名称
	private String mc;

	public String getMc() {
		return mc;
	}

	public void setMc(String mc) {
		this.mc = mc;
	}
	public String getPersonal_flg() {
		return personal_flg;
	}

	public void setPersonal_flg(String personal_flg) {
		this.personal_flg = personal_flg;
	}


	public String getFjbcbj() {
		return fjbcbj;
	}

	public void setFjbcbj(String fjbcbj) {
		this.fjbcbj = fjbcbj;
	}

	public String getJgid() {
		return jgid;
	}

	public void setJgid(String jgid) {
		this.jgid = jgid;
	}

	public String getZdry() {
		return zdry;
	}

	public void setZdry(String zdry) {
		this.zdry = zdry;
	}

	public String getSfzd() {
		return sfzd;
	}

	public void setSfzd(String sfzd) {
		this.sfzd = sfzd;
	}

	public String getZdsj() {
		return zdsj;
	}

	public void setZdsj(String zdsj) {
		this.zdsj = zdsj;
	}

	public String getTwrtxlj() {
		return twrtxlj;
	}

	public void setTwrtxlj(String twrtxlj) {
		this.twrtxlj = twrtxlj;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getWxid() {
		return wxid;
	}

	public void setWxid(String wxid) {
		this.wxid = wxid;
	}

	public String getYhid() {
		return yhid;
	}

	public void setYhid(String yhid) {
		this.yhid = yhid;
	}

	public String getStr_tzrys() {
		return str_tzrys;
	}

	public void setStr_tzrys(String str_tzrys) {
		this.str_tzrys = str_tzrys;
	}

	public List<String> getTzrys() {
		return tzrys;
	}

	public void setTzrys(List<String> tzrys) {
		this.tzrys = tzrys;
	}

	public String getTzlx() {
		return tzlx;
	}

	public void setTzlx(String tzlx) {
		this.tzlx = tzlx;
	}

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public String getMrtwr() {
		return mrtwr;
	}

	public void setMrtwr(String mrtwr) {
		this.mrtwr = mrtwr;
	}

	public String getMrtwrmc() {
		return mrtwrmc;
	}

	public void setMrtwrmc(String mrtwrmc) {
		this.mrtwrmc = mrtwrmc;
	}

	public String getPls() {
		return pls;
	}

	public void setPls(String pls) {
		this.pls = pls;
	}

	public String getLxmc() {
		return lxmc;
	}

	public void setLxmc(String lxmc) {
		this.lxmc = lxmc;
	}

	public String getZlxmc() {
		return zlxmc;
	}

	public void setZlxmc(String zlxmc) {
		this.zlxmc = zlxmc;
	}

	public String getSfjsmc() {
		return sfjsmc;
	}

	public void setSfjsmc(String sfjsmc) {
		this.sfjsmc = sfjsmc;
	}

	public String getDb() {
		return db;
	}

	public void setDb(String db) {
		this.db = db;
	}

	public String getHzxm() {
		return hzxm;
	}

	public void setHzxm(String hzxm) {
		this.hzxm = hzxm;
	}

	public String getDdid() {
		return ddid;
	}

	public void setDdid(String ddid) {
		this.ddid = ddid;
	}

	public String getQrrmc() {
		return qrrmc;
	}

	public void setQrrmc(String qrrmc) {
		this.qrrmc = qrrmc;
	}

	public String getTwrmc() {
		return twrmc;
	}

	public void setTwrmc(String twrmc) {
		this.twrmc = twrmc;
	}

	public String getYblxmc() {
		return yblxmc;
	}

	public void setYblxmc(String yblxmc) {
		this.yblxmc = yblxmc;
	}

	public String getYbbh() {
		return ybbh;
	}

	public void setYbbh(String ybbh) {
		this.ybbh = ybbh;
	}

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}

	public String getSqlParam() {
		return sqlParam;
	}

	public void setSqlParam(String sqlParam) {
		this.sqlParam = sqlParam;
	}

	public String getLx_flg() {
		return lx_flg;
	}

	public void setLx_flg(String lx_flg) {
		this.lx_flg = lx_flg;
	}

	public String getZlx_flg() {
		return zlx_flg;
	}

	public void setZlx_flg(String zlx_flg) {
		this.zlx_flg = zlx_flg;
	}

	public String getTwr_flg() {
		return twr_flg;
	}

	public void setTwr_flg(String twr_flg) {
		this.twr_flg = twr_flg;
	}

	public String getQrr_flg() {
		return qrr_flg;
	}

	public void setQrr_flg(String qrr_flg) {
		this.qrr_flg = qrr_flg;
	}

	public String getYblxmc_flg() {
		return yblxmc_flg;
	}

	public void setYblxmc_flg(String yblxmc_flg) {
		this.yblxmc_flg = yblxmc_flg;
	}

	public String getSjxx_flg() {
		return sjxx_flg;
	}

	public void setSjxx_flg(String sjxx_flg) {
		this.sjxx_flg = sjxx_flg;
	}

	public String getQrlxmc() {
		return qrlxmc;
	}

	public void setQrlxmc(String qrlxmc) {
		this.qrlxmc = qrlxmc;
	}

	public String getJcxmmc() {
		return jcxmmc;
	}

	public void setJcxmmc(String jcxmmc) {
		this.jcxmmc = jcxmmc;
	}

	public String getJcdwmc() {
		return jcdwmc;
	}

	public void setJcdwmc(String jcdwmc) {
		this.jcdwmc = jcdwmc;
	}

	public String getFjid() {
		return fjid;
	}

	public void setFjid(String fjid) {
		this.fjid = fjid;
	}

	public String getFwbj() {
		return fwbj;
	}

	public void setFwbj(String fwbj) {
		this.fwbj = fwbj;
	}

	public String getZsxm() {
		return zsxm;
	}

	public void setZsxm(String zsxm) {
		this.zsxm = zsxm;
	}

	public String getJsid() {
		return jsid;
	}

	public void setJsid(String jsid) {
		this.jsid = jsid;
	}

	public List<String> getYhjss() {
		return yhjss;
	}

	public void setYhjss(List<String> yhjss) {
		this.yhjss = yhjss;
	}

	public String getFjs() {
		return fjs;
	}

	public void setFjs(String fjs) {
		this.fjs = fjs;
	}


	public String[] getJcdws() {
		return jcdws;
	}

	public void setJcdws(String[] jcdws) {
		this.jcdws = jcdws;
		for (int i = 0; i <jcdws.length; i++){
			this.jcdws[i]=this.jcdws[i].replace("'","");
		}
	}

	public String[] getYclxs() {
		return yclxs;
	}

	public void setYclxs(String[] yclxs) {
		this.yclxs = yclxs;
		for (int i = 0; i <yclxs.length; i++){
			this.yclxs[i]=this.yclxs[i].replace("'","");
		}
	}


	public String getFknr() {
		return fknr;
	}

	public void setFknr(String fknr) {
		this.fknr = fknr;
	}

	public String getTwrlxmc() {
		return twrlxmc;
	}

	public void setTwrlxmc(String twrlxmc) {
		this.twrlxmc = twrlxmc;
	}

	public String getJsrmc() {
		return jsrmc;
	}

	public void setJsrmc(String jsrmc) {
		this.jsrmc = jsrmc;
	}

	public String getYcqf_flg() {
		return ycqf_flg;
	}

	public void setYcqf_flg(String ycqf_flg) {
		this.ycqf_flg = ycqf_flg;
	}

	public List<String> getSjycstatisticsids() {
		return sjycstatisticsids;
	}

	public void setSjycstatisticsids(List<String> sjycstatisticsids) {
		this.sjycstatisticsids = sjycstatisticsids;
	}

	public List<String> getSjids() {
		return sjids;
	}

	public void setSjids(String sjids) {
		List<String> list = new ArrayList<>();
		if(StringUtil.isNotBlank(sjids)) {
			String[] str = sjids.split(",");
			list = Arrays.asList(str);
		}
		this.sjids = list;
	}
	public void setSjids(List<String> sjids) {
		if(sjids!=null && !sjids.isEmpty()){
			sjids.replaceAll(s -> s.replace("[", "").replace("]", "").trim());
		}
		this.sjids = sjids;
	}
}
