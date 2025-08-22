package com.matridx.igams.hrm.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="YhcqtzDto")
public class YhcqtzDto extends YhcqtzModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//用户名称
	private String yhmc;
	//用户名，工号
	private String sjyhm;
	//上级用户名称
	private String sjyhmc;
	//原用户id
	private String yyhid;
	//用户钉钉id
	private String ddid;
	//上级用户钉钉id
	private String sjddid;
	//考勤类型(上班打卡，下班打卡)
	private String kqlx;
	//考勤类型名称
	private String kqlxmc;
	//上下班flag true为上班  false为下班
	private boolean Duty_flag;
	//当前日期(判断是否为当天打卡)
	private String dqrq;
	//打卡地点
	private String dkdd;
	//记录该上级下已打卡的人员数量
	private int num;
	//出勤日期(YYYY-MM-DD)
	private String cqrq;
	//退勤日期(YYYY-MM-DD)
	private String tqrq;
	//日期
	private String rq;
	private String rqstart;
	private String rqend;
	private String cqsjstart;
	private String cqsjend;
	private String tqsjstart;
	private String tqsjend;
	private String dkrq;
	private String sqlParam;
	//日期数组
	private String[] rqs;
	//主键
	private String userid;

	public String getSjyhm() {
		return sjyhm;
	}

	public void setSjyhm(String sjyhm) {
		this.sjyhm = sjyhm;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String[] getRqs() {
		return rqs;
	}

	public void setRqs(String[] rqs) {
		this.rqs = rqs;
	}

	public String getSqlParam() {
		return sqlParam;
	}

	public void setSqlParam(String sqlParam) {
		this.sqlParam = sqlParam;
	}

	public String getDkrq() {
		return dkrq;
	}

	public void setDkrq(String dkrq) {
		this.dkrq = dkrq;
	}

	public String getRqstart() {
		return rqstart;
	}

	public void setRqstart(String rqstart) {
		this.rqstart = rqstart;
	}

	public String getRqend() {
		return rqend;
	}

	public void setRqend(String rqend) {
		this.rqend = rqend;
	}

	public String getCqsjstart() {
		return cqsjstart;
	}

	public void setCqsjstart(String cqsjstart) {
		this.cqsjstart = cqsjstart;
	}

	public String getCqsjend() {
		return cqsjend;
	}

	public void setCqsjend(String cqsjend) {
		this.cqsjend = cqsjend;
	}

	public String getTqsjstart() {
		return tqsjstart;
	}

	public void setTqsjstart(String tqsjstart) {
		this.tqsjstart = tqsjstart;
	}

	public String getTqsjend() {
		return tqsjend;
	}

	public void setTqsjend(String tqsjend) {
		this.tqsjend = tqsjend;
	}

	public String getRq() {
		return rq;
	}

	public void setRq(String rq) {
		this.rq = rq;
	}

	public String getCqrq() {
		return cqrq;
	}

	public void setCqrq(String cqrq) {
		this.cqrq = cqrq;
	}

	public String getTqrq() {
		return tqrq;
	}

	public void setTqrq(String tqrq) {
		this.tqrq = tqrq;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getDkdd() {
		return dkdd;
	}

	public void setDkdd(String dkdd) {
		this.dkdd = dkdd;
	}

	public String getDqrq() {
		return dqrq;
	}

	public void setDqrq(String dqrq) {
		this.dqrq = dqrq;
	}

	public boolean isDuty_flag() {
		return Duty_flag;
	}

	public void setDuty_flag(boolean duty_flag) {
		Duty_flag = duty_flag;
	}

	public String getKqlxmc() {
		return kqlxmc;
	}

	public void setKqlxmc(String kqlxmc) {
		this.kqlxmc = kqlxmc;
	}

	public String getKqlx() {
		return kqlx;
	}

	public void setKqlx(String kqlx) {
		this.kqlx = kqlx;
	}

	public String getSjddid() {
		return sjddid;
	}

	public void setSjddid(String sjddid) {
		this.sjddid = sjddid;
	}

	public String getDdid() {
		return ddid;
	}

	public void setDdid(String ddid) {
		this.ddid = ddid;
	}

	public String getYyhid() {
		return yyhid;
	}

	public void setYyhid(String yyhid) {
		this.yyhid = yyhid;
	}

	public String getSjyhmc() {
		return sjyhmc;
	}

	public void setSjyhmc(String sjyhmc) {
		this.sjyhmc = sjyhmc;
	}

	public String getYhmc() {
		return yhmc;
	}

	public void setYhmc(String yhmc) {
		this.yhmc = yhmc;
	}
	
	
}
