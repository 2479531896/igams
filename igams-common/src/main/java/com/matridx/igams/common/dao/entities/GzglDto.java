package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;
import java.util.Map;

@Alias(value="GzglDto")
public class GzglDto extends GzglModel{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


	private String fqwwcsj;		//期望完成时间格式化
	private String fwcsj;		//完成时间格式化
	private String qwwcsjstart;	//编制开始时间
	private String qwwcsjend;		//编制结束时间
	private String wcsjstart;	//生效开始时间
	private String wcsjend;		//生效结束时间
	private String qrsjstart;	//确认开始时间
	private String qrsjend;		//确认结束时间
	private String[] zts;	//检索工作任务状态（多）
	private String fzrxm;   //负责人姓名
	private String lrryxm; //录入人员姓名
	//处理人员
	private String clry;
	private String jsmc;
	//附件ID复数
	private List<String> fjids;
	//用户ID
	private String yhid;
	private String num;
	//发货id
	private String fhid;
	//机构名称
	private String jgmc;
	//钉钉ID
	private String ddid;
	//用户真实姓名
	private String zsxm;
	//用户名
	private String yhm;
	//岗位名称
	private String gwmc;
	//工作流程ID
	private String lcid;
	//申请人
	private String sqr;
	//是否通过
	private String sftg;
	//审核意见
	private String shyj;
	//申请时间
	private String sqsj;
	//附件业务类型
	private String ywlx;
	//紧急度代码
	private String jjddm;
	//紧急度名称
	private String jjdmc;
	//紧急度排序
	private String jjdpx;
	//推送者
	private String tsz;
	//推送者名称
	private String tszmc;
	//工作逾期时间
	private String yqsj;
	//区分
	private String qf;
	//操作时间
	private String czsj;
	//操作频率
	private String czpl;
	//访问标记(对外接口访问标记)
	private String fwbj;
	//确认人员名称
	private String qrrymc;
	//全部查询内容
	private String entire;
	//列表加载的条数
	private String count;
	//从第几条开始
	private String start;
	//培训类别
	private String pxlb;
	//培训子类别
	private String pxzlb;
	//培训图片路径
	private String tpwjlj;
	//'客户id'
	private String khid;

	//'客户代码'
	private String khdm;

	//'客户名称';
	private String khmc;

	//'客户简称';
	private String khjc;

	//'发展日期';
	private String fzrq;

	//'未完成数量';
	private String wwcsl;

	//'省份';
	private String sf;
	//'省份名称';
	private String sfmc;

	//'联系人';
	private String lxr;

	//'电话';
	private String dh;

	//'地区名称';
	private String dqmc;

	//'专营业务员';
	private String zyywy;

	//'分管部门';
	private String fgbm;

	//'潜在客户';
	private String qzkh;

	//培训id
    private String pxid;
	//培训进度
	private String pxjd;
	private String fjid;

	//视频完成标记
	private String spwcbj;
	//是否测试
	private String sfcs;
	//视频标记
	private String spbj;
	//总分
	private String zf;
	//个人考试id
	private String grksid;
	//url前缀
	private String urlqz;
	//确认人员钉钉
	private String qrrydd;
	private String string_agg;
	private String zs;
	//培训标题
	private String pxbt;
	//培训类别代码
	private String lbdm;
	//过期时间
	private String gqsj;
	private String xmjdid;
	private String jhksrq;
	private String jhjsrq;
	private String sjksrq;
	private String sjjsrq;
	private String jzrq;
	private String old_xmjdid;
	private String lyid;
	private String lyxx;
	private String lylx;
	private String jdmc;
	private String jdxh;
	private String pxlbmc;
	private String kfrqstart;
	private String kfrqend;
	private String qrrqstart;
	private String qrrqend;
	private String csrqstart;
	private String csrqend;
	private String csrmc;
	private String sjflag;
	private String rqid;
	private String dqjd;//当前阶段
	//列表查询标记
	private String listFlag;
	//是否发送红包
	private String sffshb;
	private String gqsjbj;
	private String lrsjstart;
	private String lrsjend;
	private String[] jgs;
	private String[] rwmcs;
	//确认人员用户名
	private String qrryyhm;
	//任务级别参数扩展1
	private String rwjbcskz1;
	//任务级别代码
	private String rwjbdm;
	private List<GzglDto> list;
	//工作耗时
	private String gzhs;

	private String jgid;

	private String jgdm;

	private String wbcxmc;
	private String jdhs;//阶段耗时
	private List<String> yhms;
	private String fs;
	private String jdfs;//阶段分数
	//所属公司名称
	private String ssgsmc;
	//培训方式名称
	private String pxfsmc;
	//培训类别多
	private String[] pxlbs;
	//所属公司多
	private String[] ssgss;
	//培训方式多
	private String[] pxfss;
	//项目ID
	private String xmid;
	//提醒天数
	private String txts;
	//外部程序id
	private String wbcxid;
	//确认人用户名
	private String qrryhm;
	//负责人用户名
	private String fzryhm;
	//是否年度
	private String sfnd;
	//是否公开
	private String sfgk;
	//培训子类别代码
	private String zlbdm;
	private String dwxdbj;
	private String color;
	private String lysj;
	private String lyry;
	private String xh;

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	//留言附件
	private List<FjcfbDto> fj_List;
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getLysj() {
		return lysj;
	}
	public void setLysj(String lysj) {
		this.lysj = lysj;
	}
	public String getLyry() {
		return lyry;
	}
	public void setLyry(String lyry) {
		this.lyry = lyry;
	}
	public List<FjcfbDto> getFj_List()
	{
		return fj_List;
	}
	public void setFj_List(List<FjcfbDto> fj_List)
	{
		this.fj_List = fj_List;
	}
	public String getDwxdbj() {
		return dwxdbj;
	}

	public void setDwxdbj(String dwxdbj) {
		this.dwxdbj = dwxdbj;
	}

	public String getZlbdm() {
		return zlbdm;
	}

	public void setZlbdm(String zlbdm) {
		this.zlbdm = zlbdm;
	}

	public String getSfgk() {
		return sfgk;
	}

	public void setSfgk(String sfgk) {
		this.sfgk = sfgk;
	}

	public String getQrryhm() {
		return qrryhm;
	}
	
	public void setQrryhm(String qrryhm) {
		this.qrryhm = qrryhm;
	}

     public String getSfnd() {
		return sfnd;
	}

	public void setSfnd(String sfnd) {
		this.sfnd = sfnd;
	}


	//MapList
	private List<Map<String,String>> mapList;

	public List<Map<String, String>> getMapList() {
		return mapList;
	}

	public void setMapList(List<Map<String, String>> mapList) {
		this.mapList = mapList;
	}

	public String getWbcxid() {
		return wbcxid;
	}

	public void setWbcxid(String wbcxid) {
		this.wbcxid = wbcxid;
	}

	public String getTxts() {
		return txts;
	}

	public void setTxts(String txts) {
		this.txts = txts;
	}

	public String getRwjbdm() {
		return rwjbdm;
	}

	public void setRwjbdm(String rwjbdm) {
		this.rwjbdm = rwjbdm;
	}

	public String getXmid() {
		return xmid;
	}

	public void setXmid(String xmid) {
		this.xmid = xmid;
	}

	public String[] getPxlbs() {
		return pxlbs;
	}

	public void setPxlbs(String[] pxlbs) {
		this.pxlbs = pxlbs;
	}

	public String[] getSsgss() {
		return ssgss;
	}

	public void setSsgss(String[] ssgss) {
		this.ssgss = ssgss;
	}

	public String[] getPxfss() {
		return pxfss;
	}

	public void setPxfss(String[] pxfss) {
		this.pxfss = pxfss;
	}

	public String getSsgsmc() {
		return ssgsmc;
	}

	public void setSsgsmc(String ssgsmc) {
		this.ssgsmc = ssgsmc;
	}

	public String getPxfsmc() {
		return pxfsmc;
	}

	public void setPxfsmc(String pxfsmc) {
		this.pxfsmc = pxfsmc;
	}

	public String getJdfs() {
		return jdfs;
	}

	public void setJdfs(String jdfs) {
		this.jdfs = jdfs;
	}

	public String getFs() {
		return fs;
	}

	public void setFs(String fs) {
		this.fs = fs;
	}

	public List<String> getYhms() {
		return yhms;
	}

	public void setYhms(List<String> yhms) {
		this.yhms = yhms;
	}

	public String getJdhs() {
		return jdhs;
	}

	public void setJdhs(String jdhs) {
		this.jdhs = jdhs;
	}

	public String getGzhs() {
		return gzhs;
	}

	public void setGzhs(String gzhs) {
		this.gzhs = gzhs;
	}

	public List<GzglDto> getList() {
		return list;
	}

	public void setList(List<GzglDto> list) {
		this.list = list;
	}

	public String getJsmc() {
		return jsmc;
	}

	public void setJsmc(String jsmc) {
		this.jsmc = jsmc;
	}

	public String getRwjbcskz1() {
		return rwjbcskz1;
	}

	public void setRwjbcskz1(String rwjbcskz1) {
		this.rwjbcskz1 = rwjbcskz1;
	}

	public String getSjflag() {
		return sjflag;
	}

	public void setSjflag(String sjflag) {
		this.sjflag = sjflag;
	}

	public String getCsrmc() {
		return csrmc;
	}

	public void setCsrmc(String csrmc) {
		this.csrmc = csrmc;
	}

	public String getKfrqstart() {
		return kfrqstart;
	}

	public void setKfrqstart(String kfrqstart) {
		this.kfrqstart = kfrqstart;
	}

	public String getKfrqend() {
		return kfrqend;
	}

	public void setKfrqend(String kfrqend) {
		this.kfrqend = kfrqend;
	}

	public String getQrrqstart() {
		return qrrqstart;
	}

	public void setQrrqstart(String qrrqstart) {
		this.qrrqstart = qrrqstart;
	}

	public String getQrrqend() {
		return qrrqend;
	}

	public void setQrrqend(String qrrqend) {
		this.qrrqend = qrrqend;
	}

	public String getCsrqstart() {
		return csrqstart;
	}

	public void setCsrqstart(String csrqstart) {
		this.csrqstart = csrqstart;
	}

	public String getCsrqend() {
		return csrqend;
	}

	public void setCsrqend(String csrqend) {
		this.csrqend = csrqend;
	}

	public String getQrryyhm() {
		return qrryyhm;
	}

	public void setQrryyhm(String qrryyhm) {
		this.qrryyhm = qrryyhm;
	}

	public String[] getJgs() {
		return jgs;
	}

	public void setJgs(String[] jgs) {
		this.jgs = jgs;
	}

	public String[] getRwmcs() {
		return rwmcs;
	}

	public void setRwmcs(String[] rwmcs) {
		this.rwmcs = rwmcs;
	}

	public String getLrsjstart() {
		return lrsjstart;
	}

	public void setLrsjstart(String lrsjstart) {
		this.lrsjstart = lrsjstart;
	}

	public String getLrsjend() {
		return lrsjend;
	}

	public void setLrsjend(String lrsjend) {
		this.lrsjend = lrsjend;
	}

	@Override
	public String getDqjd() {
		return dqjd;
	}

	@Override
	public void setDqjd(String dqjd) {
		this.dqjd = dqjd;
	}

	public String getQrsjstart() {
		return qrsjstart;
	}

	public void setQrsjstart(String qrsjstart) {
		this.qrsjstart = qrsjstart;
	}

	public String getQrsjend() {
		return qrsjend;
	}

	public void setQrsjend(String qrsjend) {
		this.qrsjend = qrsjend;
	}

	public String getGqsjbj() {
		return gqsjbj;
	}

	public void setGqsjbj(String gqsjbj) {
		this.gqsjbj = gqsjbj;
	}

	public String getClry() {
		return clry;
	}

	public void setClry(String clry) {
		this.clry = clry;
	}

	public String getSffshb() {
		return sffshb;
	}

	public void setSffshb(String sffshb) {
		this.sffshb = sffshb;
	}

	public String getListFlag() {
		return listFlag;
	}

	public void setListFlag(String listFlag) {
		this.listFlag = listFlag;
	}

	public String getRqid() {
		return rqid;
	}

	public void setRqid(String rqid) {
		this.rqid = rqid;
	}

	public String getPxlbmc() {
		return pxlbmc;
	}

	public void setPxlbmc(String pxlbmc) {
		this.pxlbmc = pxlbmc;
	}

	public String getJdmc() {
		return jdmc;
	}

	public void setJdmc(String jdmc) {
		this.jdmc = jdmc;
	}

	public String getLyid() {
		return lyid;
	}

	public void setLyid(String lyid) {
		this.lyid = lyid;
	}

	public String getLyxx() {
		return lyxx;
	}

	public void setLyxx(String lyxx) {
		this.lyxx = lyxx;
	}

	public String getLylx() {
		return lylx;
	}

	public void setLylx(String lylx) {
		this.lylx = lylx;
	}

	public String getOld_xmjdid() {
		return old_xmjdid;
	}

	public void setOld_xmjdid(String old_xmjdid) {
		this.old_xmjdid = old_xmjdid;
	}

	public String getJhksrq() {
		return jhksrq;
	}

	public void setJhksrq(String jhksrq) {
		this.jhksrq = jhksrq;
	}

	public String getJhjsrq() {
		return jhjsrq;
	}

	public void setJhjsrq(String jhjsrq) {
		this.jhjsrq = jhjsrq;
	}

	public String getSjksrq() {
		return sjksrq;
	}

	public void setSjksrq(String sjksrq) {
		this.sjksrq = sjksrq;
	}

	public String getSjjsrq() {
		return sjjsrq;
	}

	public void setSjjsrq(String sjjsrq) {
		this.sjjsrq = sjjsrq;
	}

	public String getJzrq() {
		return jzrq;
	}

	public void setJzrq(String jzrq) {
		this.jzrq = jzrq;
	}

	public String getXmjdid() {
		return xmjdid;
	}

	public void setXmjdid(String xmjdid) {
		this.xmjdid = xmjdid;
	}

	public String getGqsj() {
		return gqsj;
	}

	public void setGqsj(String gqsj) {
		this.gqsj = gqsj;
	}

	public String getLbdm() {
		return lbdm;
	}

	public void setLbdm(String lbdm) {
		this.lbdm = lbdm;
	}

	public String getPxbt() {
		return pxbt;
	}

	public void setPxbt(String pxbt) {
		this.pxbt = pxbt;
	}

	public String getZs() {
		return zs;
	}

	public void setZs(String zs) {
		this.zs = zs;
	}

	public String getString_agg() {
		return string_agg;
	}


	public void setString_agg(String string_agg) {
		this.string_agg = string_agg;
	}

	public String getWwcsl() {
		return wwcsl;
	}

	public void setWwcsl(String wwcsl) {
		this.wwcsl = wwcsl;
	}

	public String getQrrydd() {
		return qrrydd;
	}

	public void setQrrydd(String qrrydd) {
		this.qrrydd = qrrydd;
	}

	public String getSqr() {
		return sqr;
	}

	public void setSqr(String sqr) {
		this.sqr = sqr;
	}


	public String getUrlqz() {
		return urlqz;
	}

	public void setUrlqz(String urlqz) {
		this.urlqz = urlqz;
	}

	public String getGrksid() {
		return grksid;
	}

	public void setGrksid(String grksid) {
		this.grksid = grksid;
	}

	public String getZf() {
		return zf;
	}

	public void setZf(String zf) {
		this.zf = zf;
	}

	public String getSpbj() {
		return spbj;
	}

	public void setSpbj(String spbj) {
		this.spbj = spbj;
	}

	public String getSfcs() {
		return sfcs;
	}

	public void setSfcs(String sfcs) {
		this.sfcs = sfcs;
	}

	public String getSpwcbj() {
		return spwcbj;
	}

	public void setSpwcbj(String spwcbj) {
		this.spwcbj = spwcbj;
	}



	public String getFjid() {
		return fjid;
	}

	public void setFjid(String fjid) {
		this.fjid = fjid;
	}

	public String getPxjd() {
		return pxjd;
	}

	public void setPxjd(String pxjd) {
		this.pxjd = pxjd;
	}

    public String getPxid() {
		return pxid;
	}

	public void setPxid(String pxid) {
		this.pxid = pxid;
	}
	public String getKhid() {
		return khid;
	}

	public void setKhid(String khid) {
		this.khid = khid;
	}

	public String getKhdm() {
		return khdm;
	}

	public void setKhdm(String khdm) {
		this.khdm = khdm;
	}

	public String getKhmc() {
		return khmc;
	}

	public void setKhmc(String khmc) {
		this.khmc = khmc;
	}

	public String getKhjc() {
		return khjc;
	}

	public void setKhjc(String khjc) {
		this.khjc = khjc;
	}

	public String getFzrq() {
		return fzrq;
	}

	public void setFzrq(String fzrq) {
		this.fzrq = fzrq;
	}

	public String getSf() {
		return sf;
	}

	public void setSf(String sf) {
		this.sf = sf;
	}

	public String getSfmc() {
		return sfmc;
	}

	public void setSfmc(String sfmc) {
		this.sfmc = sfmc;
	}

	public String getLxr() {
		return lxr;
	}

	public void setLxr(String lxr) {
		this.lxr = lxr;
	}

	public String getDh() {
		return dh;
	}

	public void setDh(String dh) {
		this.dh = dh;
	}

	public String getDqmc() {
		return dqmc;
	}

	public void setDqmc(String dqmc) {
		this.dqmc = dqmc;
	}

	public String getZyywy() {
		return zyywy;
	}

	public void setZyywy(String zyywy) {
		this.zyywy = zyywy;
	}

	public String getFgbm() {
		return fgbm;
	}

	public void setFgbm(String fgbm) {
		this.fgbm = fgbm;
	}

	public String getQzkh() {
		return qzkh;
	}

	public void setQzkh(String qzkh) {
		this.qzkh = qzkh;
	}

	public String getTpwjlj() {
		return tpwjlj;
	}

	public void setTpwjlj(String tpwjlj) {
		this.tpwjlj = tpwjlj;
	}

	public String getPxlb() {
		return pxlb;
	}

	public void setPxlb(String pxlb) {
		this.pxlb = pxlb;
	}

	public String getPxzlb() {
		return pxzlb;
	}

	public void setPxzlb(String pxzlb) {
		this.pxzlb = pxzlb;
	}

	public String getJgmc() {
		return jgmc;
	}

	public void setJgmc(String jgmc) {
		this.jgmc = jgmc;
	}

	public String getFhid() {
		return fhid;
	}

	public void setFhid(String fhid) {
		this.fhid = fhid;
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
	public String getQrrymc() {
		return qrrymc;
	}
	public void setQrrymc(String qrrymc) {
		this.qrrymc = qrrymc;
	}
	public String getFwbj() {
		return fwbj;
	}
	public void setFwbj(String fwbj) {
		this.fwbj = fwbj;
	}
	public String getCzsj() {
		return czsj;
	}
	public void setCzsj(String czsj) {
		this.czsj = czsj;
	}
	public String getCzpl() {
		return czpl;
	}
	public void setCzpl(String czpl) {
		this.czpl = czpl;
	}
	public String getQf() {
		return qf;
	}
	public void setQf(String qf) {
		this.qf = qf;
	}
	public String getYqsj()
	{
		return yqsj;
	}
	public void setYqsj(String yqsj)
	{
		this.yqsj = yqsj;
	}
	public String getTszmc()
	{
		return tszmc;
	}
	public void setTszmc(String tszmc)
	{
		this.tszmc = tszmc;
	}
	public String getTsz()
	{
		return tsz;
	}
	public void setTsz(String tsz)
	{
		this.tsz = tsz;
	}
	public String getZsxm() {
		return zsxm;
	}
	public void setZsxm(String zsxm) {
		this.zsxm = zsxm;
	}

	public String getFqwwcsj() {
		return fqwwcsj;
	}

	public void setFqwwcsj(String fqwwcsj) {
		this.fqwwcsj = fqwwcsj;
	}

	public String getFwcsj() {
		return fwcsj;
	}

	public void setFwcsj(String fwcsj) {
		this.fwcsj = fwcsj;
	}

	public String getQwwcsjstart() {
		return qwwcsjstart;
	}

	public void setQwwcsjstart(String qwwcsjstart) {
		this.qwwcsjstart = qwwcsjstart;
	}

	public String getQwwcsjend() {
		return qwwcsjend;
	}

	public void setQwwcsjend(String qwwcsjend) {
		this.qwwcsjend = qwwcsjend;
	}

	public String getWcsjstart() {
		return wcsjstart;
	}

	public void setWcsjstart(String wcsjstart) {
		this.wcsjstart = wcsjstart;
	}

	public String getWcsjend() {
		return wcsjend;
	}

	public void setWcsjend(String wcsjend) {
		this.wcsjend = wcsjend;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
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

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}

	public String getYhid() {
		return yhid;
	}

	public void setYhid(String yhid) {
		this.yhid = yhid;
	}

	public String getYhm() {
		return yhm;
	}

	public void setYhm(String yhm) {
		this.yhm = yhm;
	}

	public String getGwmc() {
		return gwmc;
	}

	public void setGwmc(String gwmc) {
		this.gwmc = gwmc;
	}

	public String getSftg() {
		return sftg;
	}

	public void setSftg(String sftg) {
		this.sftg = sftg;
	}

	public String getShyj() {
		return shyj;
	}

	public void setShyj(String shyj) {
		this.shyj = shyj;
	}

	public String getLcid() {
		return lcid;
	}

	public void setLcid(String lcid) {
		this.lcid = lcid;
	}

	public String getSqsj() {
		return sqsj;
	}

	public void setSqsj(String sqsj) {
		this.sqsj = sqsj;
	}

	public String getDdid() {
		return ddid;
	}

	public void setDdid(String ddid) {
		this.ddid = ddid;
	}

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public String getJjddm() {
		return jjddm;
	}

	public void setJjddm(String jjddm) {
		this.jjddm = jjddm;
	}

	public String getJjdmc() {
		return jjdmc;
	}

	public void setJjdmc(String jjdmc) {
		this.jjdmc = jjdmc;
	}

	public String getJjdpx() {
		return jjdpx;
	}

	public void setJjdpx(String jjdpx) {
		this.jjdpx = jjdpx;
	}
	public String getFzrxm()
	{
		return fzrxm;
	}
	public void setFzrxm(String fzrxm)
	{
		this.fzrxm = fzrxm;
	}

	public String getLrryxm() {
		return lrryxm;
	}

	public void setLrryxm(String lrryxm) {
		this.lrryxm = lrryxm;
	}

	@Override
	public String getJgid() {
		return jgid;
	}

	@Override
	public void setJgid(String jgid) {
		this.jgid = jgid;
	}

	public String getJgdm() {
		return jgdm;
	}

	public void setJgdm(String jgdm) {
		this.jgdm = jgdm;
	}

	public String getWbcxmc() {
		return wbcxmc;
	}

	public void setWbcxmc(String wbcxmc) {
		this.wbcxmc = wbcxmc;
	}

	public String getJdxh() {
		return jdxh;
	}

	public void setJdxh(String jdxh) {
		this.jdxh = jdxh;
	}

	public String getFzryhm() {
		return fzryhm;
	}

	public void setFzryhm(String fzryhm) {
		this.fzryhm = fzryhm;
	}
}
