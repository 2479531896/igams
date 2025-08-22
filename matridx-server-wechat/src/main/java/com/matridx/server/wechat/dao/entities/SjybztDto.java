package com.matridx.server.wechat.dao.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="SjybztDto")
public class SjybztDto extends SjybztModel{
	//基础数据参数名称
	private String csmc;
	
	private String jccsmc;
	//zt标记
	private String ztflg;
	//接收日期
	private String jsrq;
	//标本状态名称
	private String ybztmc;
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
	//接收日期格式化
	
	private String yhid;
	
	private List<String> sjhbs;
	//录入人员（复数）
	private List<String> lrrys;
	
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

	public String getYbztmc()
	{
		return ybztmc;
	}

	public String getJsrqstart()
	{
		return jsrqstart;
	}

	public void setJsrqstart(String jsrqstart){
		this.jsrqstart = jsrqstart;
		if("null".equals(this.jsrqstart) || this.jsrqstart=="") {
			this.jsrqstart="";
		}
	}

	public String getJsrqend()
	{
		return jsrqend;
	}

	public void setJsrqend(String jsrqend){
		this.jsrqend = jsrqend;
		if("null".equals(this.jsrqend) || this.jsrqend=="") {
			this.jsrqend="";
		}
	}

	public String getJsrqMstart()
	{
		return jsrqMstart;
	}

	public void setJsrqMstart(String jsrqMstart){
		this.jsrqMstart = jsrqMstart;
		if("null".equals(this.jsrqMstart) || this.jsrqMstart=="") {
			this.jsrqMstart="";
		}
	}

	public String getJsrqMend()
	{
		return jsrqMend;
	}

	public void setJsrqMend(String jsrqMend){
		this.jsrqMend = jsrqMend;
		if("null".equals(this.jsrqMend) || this.jsrqMend=="") {
			this.jsrqMend="";
		}
	}

	public String getJsrqYstart()
	{
		return jsrqYstart;
	}

	public void setJsrqYstart(String jsrqYstart){
		this.jsrqYstart = jsrqYstart;
		if("null".equals(this.jsrqYstart) || this.jsrqYstart=="") {
			this.jsrqYstart="";
		}
	}

	public String getJsrqYend()
	{
		return jsrqYend;
	}

	public void setJsrqYend(String jsrqYend){
		this.jsrqYend = jsrqYend;
		if("null".equals(this.jsrqYend) || this.jsrqYend=="") {
			this.jsrqYend="";
		}
	}

	public void setYbztmc(String ybztmc)
	{
		this.ybztmc = ybztmc;
	}

	public String getZtflg()
	{
		return ztflg;
	}

	public void setZtflg(String ztflg)
	{
		this.ztflg = ztflg;
	}

	public String getJsrq()
	{
		return jsrq;
	}

	public void setJsrq(String jsrq){
		this.jsrq = jsrq;
		if("null".equals(this.jsrq ) || this.jsrq =="") {
			this.jsrq ="";
		}
	}

	public String getJccsmc()
	{
		return jccsmc;
	}

	public void setJccsmc(String jccsmc)
	{
		this.jccsmc = jccsmc;
	}

	public String getCsmc()
	{
		return csmc;
	}

	public void setCsmc(String csmc)
	{
		this.csmc = csmc;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
