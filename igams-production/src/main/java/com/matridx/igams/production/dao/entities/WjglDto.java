package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.Role;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.ibatis.type.Alias;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Alias(value="WjglDto")
public class WjglDto extends WjglModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String auditModType; //审核文件修订类别

	private String[] wjfls;	//检索用文件分类（多）
	private String[] wjlbs;	//检索用文件类别（多）
	private String[] zts;	//检索用审核状态（多）
	private String[] scbjs;	//检索用是否停用（多）
	private String[] ffzts;	//检索发放状态（多）
	private String wjfldm;	//文件分类代码
	private String wjflmc;	//文件分类名称
	private String wjlbdm;	//文件类别代码
	private String wjlbmc;	//文件类别名称
	private String bzsjstart;	//编制开始时间
	private String bzsjend;		//编制结束时间
	private String sxrqstart;	//生效开始时间
	private String sxrqend;		//生效结束时间
	
	//权限ID
	private String qxid;
	//角色ID
	private String jsid;
	//角色名称
	private String jsmc;
	//学习权限角色名称
	private String studyjsmc;
	//查看权限角色名称
	private String viewjsmc;
	//下载权限角色名称
	private String downloadjsmc;
	//角色ID复数
	private List<String> jsids;
	//角色ID复数(查看权限)
	private List<String> t_jsids;
	//角色ID复数(下载权限)
	private List<String> d_jsids;
	private List<String> wjbhs;
	//机构ID
	private String jgid;
	//用户ID
	private String yhid;
	//钉钉ID
	private String ddid;
	//真实姓名
	private String zsxm;
	//附件ID
	private String fjid;
	//附件ID复数
	private List<String> fjids;
	//任务进度
	private String rwjd;
	//附件Dto列表
	private List<FjcfbDto> fjcfbDtos;
	//录入时间格式化
	private String flrsj;
	//文件类型
	private String Ywlx;
	//期望完成时间
	private String wj_qwwcsj;
	//用户ID复数
	private List<String> yhids;
	//文件权限类型
	private String qxlx;
	//角色列表
	private List<Role> roles;
	//学习权限标记
	private int xxflg;
	//下载权限标记
	private int xzflg;
	//查看权限标记
	private int ckflg;
	//签名标记
	private String signflg;
	//机构名称
	private String jgmc;
	//文件替换标记
	private String replaceflg; 
	//压缩文件路径
	private String ysblj; 
	//旧文件ID
	private String prewjid;
	//旧根文件ID
	private String pregwjid;
	//导出关联标记位
	//所选择的字段
	private String SqlParam;
	//文件类别基础数据
	private String wjlb_flg;
	//文件分类基础数据
	private String wjfl_flg;
	//访问标记
	private String fwbj;
	//转换规则
	private String prezhgz;
	//是否替换
	private String presfth;
	//删除标记(高级修改)
	private String scbjFlag;
	//参数扩展1名称
	private String cskz1mc;
	//参数扩展2名称
	private String cskz2mc;
	//参数扩展3名称
	private String cskz3mc;
	//月
	private String lrsjMonth;
	//日
	private String lrsjDay;
	//数量
	private String num;
	//文件分组
	private String lrsjStats;
	//文件分组
	private String xgsjStats;
	//月
	private String xgsjMonth;
	//日
	private String xgsjDay;
	//参数
	private String cs;
	//开始时间
	private String startTimeLr;
	//结束时间
	private String endTimeLr;
	//开始时间
	private String startTimeXg;
	//结束时间
	private String endTimeXg;
	//文件数量
	private String wjsl;
	//年份
	private String year;
	//路径标记
	private String ljbj;
	//用户名
	private String yhm;
	//用于查询关联文件排除自己
	private String nwjid;
	//关联文件
	private String glwj_json;
	//文件类别参数扩展2
	private String wjlbcskz2;
	//设备名称
	private String sbmc;
	//设备关联id
	private String sbglid;
	//设备验收id
	private String sbysid;
	//设备关联文件id
	private String sbglwjid;
	//设备编号
	private String sbbh;
	//序列号
	private String xlh;
	//固定资产编号
	private String gdzcbh;
	//设备出厂编号
	private String sbccbh;
	private String shlb;
	private String wjfls_str;

	private String wjm;

	public String getWjm() {
		return wjm;
	}

	public void setWjm(String wjm) {
		this.wjm = wjm;
	}

	public List<String> getWjbhs() {
		return wjbhs;
	}
	public void setWjbhs(String wjbhs) {
		List<String> list = new ArrayList<>();
		if(StringUtil.isNotBlank(wjbhs)) {
			String[] str = wjbhs.split(",");
			list = Arrays.asList(str);
		}
		this.wjbhs = list;
	}
	public void setWjbhs(List<String> wjbhs) {
		if(wjbhs!=null && !wjbhs.isEmpty()){
			wjbhs.replaceAll(s -> s.replace("[", "").replace("]", "").trim());
		}
		this.wjbhs = wjbhs;
	}
	public String getWjfls_str() {
		return wjfls_str;
	}

	public void setWjfls_str(String wjfls_str) {
		this.wjfls_str = wjfls_str;
	}

	//已选角色ids
	private List<String> njsids;

	public List<String> getNjsids() {
		return njsids;
	}

	public void setNjsids(List<String> njsids) {
		this.njsids = njsids;
	}
	private String zlbcskz2;

	public String getZlbcskz2() {
		return zlbcskz2;
	}

	public void setZlbcskz2(String zlbcskz2) {
		this.zlbcskz2 = zlbcskz2;
	}

	public String getShlb() {
		return shlb;
	}

	public void setShlb(String shlb) {
		this.shlb = shlb;
	}

	public String getSbbh() {
		return sbbh;
	}

	public void setSbbh(String sbbh) {
		this.sbbh = sbbh;
	}

	public String getXlh() {
		return xlh;
	}

	public void setXlh(String xlh) {
		this.xlh = xlh;
	}

	public String getGdzcbh() {
		return gdzcbh;
	}

	public void setGdzcbh(String gdzcbh) {
		this.gdzcbh = gdzcbh;
	}

	public String getSbccbh() {
		return sbccbh;
	}

	public void setSbccbh(String sbccbh) {
		this.sbccbh = sbccbh;
	}

	public String getSbglwjid() {
		return sbglwjid;
	}

	public void setSbglwjid(String sbglwjid) {
		this.sbglwjid = sbglwjid;
	}

	public String getWjlbcskz2() {
		return wjlbcskz2;
	}

	public void setWjlbcskz2(String wjlbcskz2) {
		this.wjlbcskz2 = wjlbcskz2;
	}

	public String getSbmc() {
		return sbmc;
	}

	public void setSbmc(String sbmc) {
		this.sbmc = sbmc;
	}

	public String getSbglid() {
		return sbglid;
	}

	public void setSbglid(String sbglid) {
		this.sbglid = sbglid;
	}

	public String getSbysid() {
		return sbysid;
	}

	public void setSbysid(String sbysid) {
		this.sbysid = sbysid;
	}

	public String getGlwj_json() {
		return glwj_json;
	}

	public void setGlwj_json(String glwj_json) {
		this.glwj_json = glwj_json;
	}

	public String getNwjid() {
		return nwjid;
	}

	public void setNwjid(String nwjid) {
		this.nwjid = nwjid;
	}

	public String getYhm() {
		return yhm;
	}

	public void setYhm(String yhm) {
		this.yhm = yhm;
	}

	public String getLjbj() {
		return ljbj;
	}

	public void setLjbj(String ljbj) {
		this.ljbj = ljbj;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getWjsl() {
		return wjsl;
	}

	public void setWjsl(String wjsl) {
		this.wjsl = wjsl;
	}

	public String getXgsjDay() {
		return xgsjDay;
	}

	public void setXgsjDay(String xgsjDay) {
		this.xgsjDay = xgsjDay;
	}

	public String getStartTimeLr() {
		return startTimeLr;
	}

	public void setStartTimeLr(String startTimeLr) {
		this.startTimeLr = startTimeLr;
	}

	public String getEndTimeLr() {
		return endTimeLr;
	}

	public void setEndTimeLr(String endTimeLr) {
		this.endTimeLr = endTimeLr;
	}

	public String getStartTimeXg() {
		return startTimeXg;
	}

	public void setStartTimeXg(String startTimeXg) {
		this.startTimeXg = startTimeXg;
	}

	public String getEndTimeXg() {
		return endTimeXg;
	}

	public void setEndTimeXg(String endTimeXg) {
		this.endTimeXg = endTimeXg;
	}

	public String getLrsjDay() {
		return lrsjDay;
	}

	public void setLrsjDay(String lrsjDay) {
		this.lrsjDay = lrsjDay;
	}

	public String getCs() {
		return cs;
	}

	public void setCs(String cs) {
		this.cs = cs;
	}

	public String getXgsjMonth() {
		return xgsjMonth;
	}

	public void setXgsjMonth(String xgsjMonth) {
		this.xgsjMonth = xgsjMonth;
	}


	public String getXgsjStats() {
		return xgsjStats;
	}

	public void setXgsjStats(String xgsjStats) {
		this.xgsjStats = xgsjStats;
	}

	public String getLrsjStats() {
		return lrsjStats;
	}

	public void setLrsjStats(String lrsjStats) {
		this.lrsjStats = lrsjStats;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}


	public String getLrsjMonth() {
		return lrsjMonth;
	}

	public void setLrsjMonth(String lrsjMonth) {
		this.lrsjMonth = lrsjMonth;
	}

	public String getCskz1mc() {
		return cskz1mc;
	}

	public void setCskz1mc(String cskz1mc) {
		this.cskz1mc = cskz1mc;
	}

	public String getCskz2mc() {
		return cskz2mc;
	}

	public void setCskz2mc(String cskz2mc) {
		this.cskz2mc = cskz2mc;
	}

	public String getCskz3mc() {
		return cskz3mc;
	}

	public void setCskz3mc(String cskz3mc) {
		this.cskz3mc = cskz3mc;
	}

	public String getScbjFlag() {
		return scbjFlag;
	}

	public void setScbjFlag(String scbjFlag) {
		this.scbjFlag = scbjFlag;
	}

	public String getPrezhgz() {
		return prezhgz;
	}

	public void setPrezhgz(String prezhgz) {
		this.prezhgz = prezhgz;
	}

	public String getPresfth() {
		return presfth;
	}

	public void setPresfth(String presfth) {
		this.presfth = presfth;
	}

	public String getFwbj() {
		return fwbj;
	}

	public void setFwbj(String fwbj) {
		this.fwbj = fwbj;
	}

	public String getWjlb_flg() {
		return wjlb_flg;
	}

	public void setWjlb_flg(String wjlb_flg) {
		this.wjlb_flg = wjlb_flg;
	}

	public String getWjfl_flg() {
		return wjfl_flg;
	}

	public void setWjfl_flg(String wjfl_flg) {
		this.wjfl_flg = wjfl_flg;
	}

	public String getSqlParam() {
		return SqlParam;
	}

	public void setSqlParam(String sqlParam) {
		SqlParam = sqlParam;
	}

	public String getPrewjid() {
		return prewjid;
	}

	public void setPrewjid(String prewjid) {
		this.prewjid = prewjid;
	}

	public String getPregwjid() {
		return pregwjid;
	}

	public void setPregwjid(String pregwjid) {
		this.pregwjid = pregwjid;
	}

	public String getYsblj() {
		return ysblj;
	}

	public void setYsblj(String ysblj) {
		this.ysblj = ysblj;
	}

	public String getReplaceflg() {
		return replaceflg;
	}

	public void setReplaceflg(String replaceflg) {
		this.replaceflg = replaceflg;
	}
	public String getJgmc() {
		return jgmc;
	}

	public void setJgmc(String jgmc) {
		this.jgmc = jgmc;
	}

	public String getSignflg() {
		return signflg;
	}

	public void setSignflg(String signflg) {
		this.signflg = signflg;
	}
	
	public int getXxflg() {
		return xxflg;
	}

	public void setXxflg(int xxflg) {
		this.xxflg = xxflg;
	}

	public int getXzflg() {
		return xzflg;
	}

	public void setXzflg(int xzflg) {
		this.xzflg = xzflg;
	}

	public int getCkflg() {
		return ckflg;
	}

	public void setCkflg(int ckflg) {
		this.ckflg = ckflg;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public String getDownloadjsmc() {
		return downloadjsmc;
	}

	public void setDownloadjsmc(String downloadjsmc) {
		this.downloadjsmc = downloadjsmc;
	}

	public List<String> getD_jsids() {
		return d_jsids;
	}

	public void setD_jsids(List<String> d_jsids) {
		this.d_jsids = d_jsids;
	}

	public String getAuditModType() {
		return auditModType;
	}

	public void setAuditModType(String auditModType) {
		this.auditModType = auditModType;
	}

	public String[] getWjfls() {
		return wjfls;
	}

	public void setWjfls(String[] wjfls) {
		this.wjfls = wjfls;
		for(int i=0;i<this.wjfls.length;i++)
		{
			this.wjfls[i] = this.wjfls[i].replace("'", "");
		}
	}

	public String[] getWjlbs() {
		return wjlbs;
	}

	public void setWjlbs(String[] wjlbs) {
		this.wjlbs = wjlbs;
		for(int i=0;i<this.wjlbs.length;i++)
		{
			this.wjlbs[i] = this.wjlbs[i].replace("'", "");
		}
	}

	public String[] getZts() {
		return zts;
	}

	public void setZts(String[] zts) {
		this.zts = zts;
		for(int i=0;i<this.zts.length;i++)
		{
			this.zts[i] = this.zts[i].replace("'", "");
		}
	}

	public String getStudyjsmc()
	{
		return studyjsmc;
	}

	public void setStudyjsmc(String studyjsmc)
	{
		this.studyjsmc = studyjsmc;
	}

	public String getViewjsmc()
	{
		return viewjsmc;
	}

	public void setViewjsmc(String viewjsmc)
	{
		this.viewjsmc = viewjsmc;
	}

	public String[] getScbjs() {
		return scbjs;
	}

	public void setScbjs(String[] scbjs) {
		this.scbjs = scbjs;
		for(int i=0;i<this.scbjs.length;i++)
		{
			this.scbjs[i] = this.scbjs[i].replace("'", "");
		}
	}
	public String[] getFfzts() {
		return ffzts;
	}

	public void setFfzts(String[] ffzts) {
		this.ffzts = ffzts;
		for(int i=0;i<this.ffzts.length;i++)
		{
			this.ffzts[i] = this.ffzts[i].replace("'", "");
		}
	}
	public String getWjfldm() {
		return wjfldm;
	}

	public void setWjfldm(String wjfldm) {
		this.wjfldm = wjfldm;
	}

	public String getWjflmc() {
		return wjflmc;
	}

	public void setWjflmc(String wjflmc) {
		this.wjflmc = wjflmc;
	}

	public String getWjlbdm() {
		return wjlbdm;
	}

	public void setWjlbdm(String wjlbdm) {
		this.wjlbdm = wjlbdm;
	}

	public String getWjlbmc() {
		return wjlbmc;
	}

	public void setWjlbmc(String wjlbmc) {
		this.wjlbmc = wjlbmc;
	}

	public String getBzsjstart() {
		return bzsjstart;
	}

	public void setBzsjstart(String bzsjstart) {
		this.bzsjstart = bzsjstart;
	}

	public String getBzsjend() {
		return bzsjend;
	}

	public void setBzsjend(String bzsjend) {
		this.bzsjend = bzsjend;
	}

	public String getSxrqstart() {
		return sxrqstart;
	}

	public void setSxrqstart(String sxrqstart) {
		this.sxrqstart = sxrqstart;
	}

	public String getSxrqend() {
		return sxrqend;
	}

	public void setSxrqend(String sxrqend) {
		this.sxrqend = sxrqend;
	}

	public String getQxid() {
		return qxid;
	}

	public void setQxid(String qxid) {
		this.qxid = qxid;
	}

	public String getJsid() {
		return jsid;
	}

	public void setJsid(String jsid) {
		this.jsid = jsid;
	}

	public String getJsmc() {
		return jsmc;
	}

	public void setJsmc(String jsmc) {
		this.jsmc = jsmc;
	}

	public List<String> getJsids() {
		return jsids;
	}

	public void setJsids(List<String> jsids) {
		this.jsids = jsids;
	}

	public String getYhid() {
		return yhid;
	}

	public void setYhid(String yhid) {
		this.yhid = yhid;
	}

	public String getFjid() {
		return fjid;
	}

	public void setFjid(String fjid) {
		this.fjid = fjid;
	}

	public String getRwjd() {
		return rwjd;
	}

	public void setRwjd(String rwjd) {
		this.rwjd = rwjd;
	}

	public List<FjcfbDto> getFjcfbDtos() {
		return fjcfbDtos;
	}

	public void setFjcfbDtos(List<FjcfbDto> fjcfbDtos) {
		this.fjcfbDtos = fjcfbDtos;
	}

	public String getFlrsj() {
		return flrsj;
	}

	public void setFlrsj(String flrsj) {
		this.flrsj = flrsj;
	}

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}

	public String getJgid() {
		return jgid;
	}

	public void setJgid(String jgid) {
		this.jgid = jgid;
	}

	public String getZsxm() {
		return zsxm;
	}

	public void setZsxm(String zsxm) {
		this.zsxm = zsxm;
	}

	public String getDdid() {
		return ddid;
	}

	public void setDdid(String ddid) {
		this.ddid = ddid;
	}

	public String getYwlx() {
		return Ywlx;
	}

	public void setYwlx(String ywlx) {
		Ywlx = ywlx;
	}
	
	public String getWj_qwwcsj() {
		return wj_qwwcsj;
	}

	public void setWj_qwwcsj(String wj_qwwcsj) {
		this.wj_qwwcsj = wj_qwwcsj;
	}

	public List<String> getYhids() {
		return yhids;
	}

	public void setYhids(List<String> yhids) {
		this.yhids = yhids;
	}

	public List<String> getT_jsids() {
		return t_jsids;
	}

	public void setT_jsids(List<String> t_jsids) {
		this.t_jsids = t_jsids;
	}

	public String getQxlx() {
		return qxlx;
	}

	public void setQxlx(String qxlx) {
		this.qxlx = qxlx;
	}

	
}
