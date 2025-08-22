package com.matridx.server.wechat.dao.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="SjwzxxDto")
public class SjwzxxDto extends SjwzxxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//统计数量
	private String num;
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
    //录入人员（多）
  	private List<String> lrrys;
	//检测项目ID
	private String jcxmid;
	// 基因分型
	private String jyfx;
	//序列数
	private String xls;

	public String getJcxmid() {
		return jcxmid;
	}
	public void setJcxmid(String jcxmid) {
		this.jcxmid = jcxmid;
	}
	public List<String> getLrrys() {
		return lrrys;
	}

	public void setLrrys(String lrrys) {
		List<String> list = new ArrayList<String>();
		String str[] = lrrys.split(",");
		list = Arrays.asList(str);
		this.lrrys = list;
	}
	public void setLrrys(List<String> lrrys) {
		if(lrrys!=null && lrrys.size() > 0){
			for(int i=0;i<lrrys.size();i++){
				lrrys.set(i,lrrys.get(i).replace("[", "").replace("]", "").trim());
			}
		}
		this.lrrys = lrrys;
	}

	public String getWznum() {
		return wznum;
	}

	public void setWznum(String wznum) {
		this.wznum = wznum;
	}

	public String getLx_flg() {
		return lx_flg;
	}

	public void setLx_flg(String lx_flg) {
		this.lx_flg = lx_flg;
	}

	public String getBgrq() {
		return bgrq;
	}

	public void setBgrq(String bgrq) {
		this.bgrq = bgrq;
	}

	public String getBgrqstart() {
		return bgrqstart;
	}

	public void setBgrqstart(String bgrqstart) {
		this.bgrqstart = bgrqstart;
	}

	public String getBgrqend() {
		return bgrqend;
	}

	public void setBgrqend(String bgrqend) {
		this.bgrqend = bgrqend;
	}

	public String getBgrqMstart() {
		return bgrqMstart;
	}

	public void setBgrqMstart(String bgrqMstart) {
		this.bgrqMstart = bgrqMstart;
	}

	public String getBgrqMend() {
		return bgrqMend;
	}

	public void setBgrqMend(String bgrqMend) {
		this.bgrqMend = bgrqMend;
	}

	public String getBgrqYstart() {
		return bgrqYstart;
	}

	public void setBgrqYstart(String bgrqYstart) {
		this.bgrqYstart = bgrqYstart;
	}

	public String getBgrqYend() {
		return bgrqYend;
	}

	public void setBgrqYend(String bgrqYend) {
		this.bgrqYend = bgrqYend;
	}

	public List<String> getSjhbs() {
		return sjhbs;
	}

	public void setSjhbs(List<String> sjhbs) {
		this.sjhbs = sjhbs;
	}

	public String getYhid() {
		return yhid;
	}

	public void setYhid(String yhid) {
		this.yhid = yhid;
	}

	public String getJsrq() {
		return jsrq;
	}

	public void setJsrq(String jsrq) {
		this.jsrq = jsrq;
	}

	public String getLxmc() {
		return lxmc;
	}

	public void setLxmc(String lxmc) {
		this.lxmc = lxmc;
	}

	public String getJyjg() {
		return jyjg;
	}

	public void setJyjg(String jyjg) {
		this.jyjg = jyjg;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
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
}
