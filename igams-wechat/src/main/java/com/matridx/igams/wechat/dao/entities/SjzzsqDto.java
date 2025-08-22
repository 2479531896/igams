package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="SjzzsqDto")
public class SjzzsqDto extends SjzzsqModel{
	//报告状态
	private String bgzt;
	private String entire;
	// 样本编号
	private String ybbh;
	// 内部编码
	private String nbbm;
	// 患者姓名
	private String hzxm;
	// 样本类型
	private String yblx;
	// 检测项目
	private String jcxm;
	// 报告日期
	private String bgrq;
	// 送检伙伴
	private String sjhb;
	// 申请时间
	private String sqsj;
	// 是否寄出
	private String sfjc;
	//送检快递ID
	private String sjkdid;
	//快递单号
	private String mailno;
	//业务ID
	private String ywid;
	//快递发出时间
	private String starttime;
	//快递签收时间
	private String endtime;
	//快递状态    0运送中     1已签收      2不存在
	private String kdzt;
	//业务类型，ExpressTypeEnum枚举类
	private String ywlx;
	//快递类型，基础数据维护，eg：京东、顺丰等
	private String kdlx;
	//发票随寄名称
	private String fpsjmc;
	//送检单位名称
	private String sjdwmc;
	//性别名称
	private String xbmc;
	//样本类型名称
	private String yblxmc;
	//检测单位名称
	private String jcxmmc;
	//录入人员名称
	private String lrrymc;
	//判断是否是个人清单 single_flag=1 为个人清单  single_flag=0 or single_flag=null 为全部清单
	private String single_flag;
	//用于个人清单判断lrry [yhid,ddid,wxid]
	private List<String> userids;
	//用户钉钉 判断伙伴权限
	private List<String> sjhbs;
	//检测单位限制
	private List<String> jcdwxz;
	//导出关联标记为所选择的字段
	private String sqlParam;
	//关联表标记
	private String sjxx_flg;
	//锁定人员名称
	private String sdrymc;
	//锁定日期开始日期
	private String sdrqstart;
	//锁定日期结束日期
	private String sdrqend;
	//快递类型
	private String[] kdlxs;
	//申请时间开始日期
	private String sqsjend;
	//申请时间开始日期
	private String sqsjstart;
	//检测子项目
	private String jczxm;
	//快递客户订单号
	private String sjmailno;
	//打包操作标记
	private String cz_flg;
	//附件ID
	private String fjid;
	//打印信息
	private String printInfo;
	//文件名
	private String wjm;
	//京东类型
	private String jdlx;
	//业务IDs
	private List<String> ywids;

	private String cskz3s;

	public List<String> getYwids() {
		return ywids;
	}

	public void setYwids(List<String> ywids) {
		this.ywids = ywids;
	}

	public String getJdlx() {
		return jdlx;
	}

	public void setJdlx(String jdlx) {
		this.jdlx = jdlx;
	}

	public String getWjm() {
		return wjm;
	}

	public void setWjm(String wjm) {
		this.wjm = wjm;
	}

	public String getPrintInfo() {
		return printInfo;
	}

	public void setPrintInfo(String printInfo) {
		this.printInfo = printInfo;
	}

	public String getFjid() {
		return fjid;
	}

	public void setFjid(String fjid) {
		this.fjid = fjid;
	}

	public String getJczxm() {
		return jczxm;
	}

	public void setJczxm(String jczxm) {
		this.jczxm = jczxm;
	}
	
	public String getCz_flg() {
		return cz_flg;
	}

	public void setCz_flg(String cz_flg) {
		this.cz_flg = cz_flg;
	}

	public String getSjmailno() {
		return sjmailno;
	}

	public void setSjmailno(String sjmailno) {
		this.sjmailno = sjmailno;
	}


	public String getSqlParam() {
		return sqlParam;
	}

	public void setSqlParam(String sqlParam) {
		this.sqlParam = sqlParam;
	}

	public String getSjxx_flg() {
		return sjxx_flg;
	}

	public void setSjxx_flg(String sjxx_flg) {
		this.sjxx_flg = sjxx_flg;
	}

	public String getSdrymc() {
		return sdrymc;
	}

	public void setSdrymc(String sdrymc) {
		this.sdrymc = sdrymc;
	}

	@Override
	public String getSqsjstart() {
		return sqsjstart;
	}

	@Override
	public String getSqsjend() {
		return sqsjend;
	}
	@Override
	public void setSqsjend(String sqsjend) {
		this.sqsjend = sqsjend;
	}


	@Override
	public void setSqsjstart(String sqsjstart) {
		this.sqsjstart = sqsjstart;
	}

	public String getSdrqstart() {
		return sdrqstart;
	}

	public void setSdrqstart(String sdrqstart) {
		this.sdrqstart = sdrqstart;
	}

	public String getSdrqend() {
		return sdrqend;
	}

	public void setSdrqend(String sdrqend) {
		this.sdrqend = sdrqend;
	}



	public String[] getKdlxs() {
		return kdlxs;
	}

	public void setKdlxs(String[] kdlxs) {
		this.kdlxs = kdlxs;
		for (int i = 0; i < kdlxs.length; i++){
			this.kdlxs[i]=this.kdlxs[i].replace("'","");
		}
	}
	//快递订单是否取消
	private String sfqx;

	public String getSfqx() {
		return sfqx;
	}

	public void setSfqx(String sfqx) {
		this.sfqx = sfqx;
	}






	public List<String> getJcdwxz() {
		return jcdwxz;
	}

	public void setJcdwxz(List<String> jcdwxz) {
		this.jcdwxz = jcdwxz;
	}

	public List<String> getSjhbs() {
		return sjhbs;
	}

	public void setSjhbs(List<String> sjhbs) {
		this.sjhbs = sjhbs;
	}

	public List<String> getUserids() {
		return userids;
	}

	public void setUserids(List<String> userids) {
		this.userids = userids;
	}

	public String getSingle_flag() {
		return single_flag;
	}

	public void setSingle_flag(String single_flag) {
		this.single_flag = single_flag;
	}

	public String getLrrymc() {
		return lrrymc;
	}

	public void setLrrymc(String lrrymc) {
		this.lrrymc = lrrymc;
	}

	public String getFpsjmc() {
		return fpsjmc;
	}

	public void setFpsjmc(String fpsjmc) {
		this.fpsjmc = fpsjmc;
	}

	public String getSjdwmc() {
		return sjdwmc;
	}

	public void setSjdwmc(String sjdwmc) {
		this.sjdwmc = sjdwmc;
	}

	public String getXbmc() {
		return xbmc;
	}

	public void setXbmc(String xbmc) {
		this.xbmc = xbmc;
	}

	public String getYblxmc() {
		return yblxmc;
	}

	public void setYblxmc(String yblxmc) {
		this.yblxmc = yblxmc;
	}

	public String getJcxmmc() {
		return jcxmmc;
	}

	public void setJcxmmc(String jcxmmc) {
		this.jcxmmc = jcxmmc;
	}

	public String getSjkdid() {
		return sjkdid;
	}

	public void setSjkdid(String sjkdid) {
		this.sjkdid = sjkdid;
	}

	public String getMailno() {
		return mailno;
	}

	public void setMailno(String mailno) {
		this.mailno = mailno;
	}

	public String getYwid() {
		return ywid;
	}

	public void setYwid(String ywid) {
		this.ywid = ywid;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getKdzt() {
		return kdzt;
	}

	public void setKdzt(String kdzt) {
		this.kdzt = kdzt;
	}

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public String getKdlx() {
		return kdlx;
	}

	public void setKdlx(String kdlx) {
		this.kdlx = kdlx;
	}

	public String getYbbh() {
		return ybbh;
	}

	public void setYbbh(String ybbh) {
		this.ybbh = ybbh;
	}

	public String getNbbm() {
		return nbbm;
	}

	public void setNbbm(String nbbm) {
		this.nbbm = nbbm;
	}

	public String getHzxm() {
		return hzxm;
	}

	public void setHzxm(String hzxm) {
		this.hzxm = hzxm;
	}

	public String getYblx() {
		return yblx;
	}

	public void setYblx(String yblx) {
		this.yblx = yblx;
	}

	public String getJcxm() {
		return jcxm;
	}

	public void setJcxm(String jcxm) {
		this.jcxm = jcxm;
	}

	public String getBgrq() {
		return bgrq;
	}

	public void setBgrq(String bgrq) {
		this.bgrq = bgrq;
	}

	public String getSjhb() {
		return sjhb;
	}

	public void setSjhb(String sjhb) {
		this.sjhb = sjhb;
	}

	public String getSqsj() {
		return sqsj;
	}

	public void setSqsj(String sqsj) {
		this.sqsj = sqsj;
	}


	public String getSfjc() {
		return sfjc;
	}

	public void setSfjc(String sfjc) {
		this.sfjc = sfjc;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getBgzt() {
		return bgzt;
	}

	public void setBgzt(String bgzt) {
		this.bgzt = bgzt;
	}


	// 申请人
	private String sqr;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public String getSqr() {
		return sqr;
	}

	public void setSqr(String sqr) {
		this.sqr = sqr;
	}


	public String getCskz3s() {
		return cskz3s;
	}

	public void setCskz3s(String cskz3s) {
		this.cskz3s = cskz3s;
	}
}
