package com.matridx.las.netty.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="MlpzModel")
public class MlpzModel extends BaseModel{
	//命令id
	private String mlid;
	//事件id
	private String sjid;
	//协议类型
	private String xylx;
	//命令类型
	private String mllx;
	//命令代码
	private String mldm;
	//命令参数（配置的是需要传入的参数名称）
	private String mlcs;
	//回调类型，就是回调实现类名
	private String hdlx;
	//是否异步
	private String sfyb;
	//是否最后一部
	private String sfzhyb;
	//第一次返回超时时间
	private String dycfhcssj;
	//第二次返回超时时间
	private String decfhcssj;
	//序号，排序用
	private String xh;
	//是否需要传参
	private String sfcc;
	//备注说明
	private String bz;
	//类名称
	private String classname;
	//方法名称
	private String methodname;
	//参数类型
	private String paramlx;
	//命令id
	public String getMlid() {
		return mlid;
	}
	public void setMlid(String mlid){
		this.mlid = mlid;
	}
	//事件id
	public String getSjid() {
		return sjid;
	}
	public void setSjid(String sjid){
		this.sjid = sjid;
	}
	//协议类型
	public String getXylx() {
		return xylx;
	}
	public void setXylx(String xylx){
		this.xylx = xylx;
	}
	//命令类型
	public String getMllx() {
		return mllx;
	}
	public void setMllx(String mllx){
		this.mllx = mllx;
	}
	//命令代码
	public String getMldm() {
		return mldm;
	}
	public void setMldm(String mldm){
		this.mldm = mldm;
	}
	//命令参数（配置的是需要传入的参数名称）
	public String getMlcs() {
		return mlcs;
	}
	public void setMlcs(String mlcs){
		this.mlcs = mlcs;
	}
	//回调类型，就是回调实现类名
	public String getHdlx() {
		return hdlx;
	}
	public void setHdlx(String hdlx){
		this.hdlx = hdlx;
	}
	//是否异步
	public String getSfyb() {
		return sfyb;
	}
	public void setSfyb(String sfyb){
		this.sfyb = sfyb;
	}
	//是否最后一部
	public String getSfzhyb() {
		return sfzhyb;
	}
	public void setSfzhyb(String sfzhyb){
		this.sfzhyb = sfzhyb;
	}
	//第一次返回超时时间
	public String getDycfhcssj() {
		return dycfhcssj;
	}
	public void setDycfhcssj(String dycfhcssj){
		this.dycfhcssj = dycfhcssj;
	}
	//第二次返回超时时间
	public String getDecfhcssj() {
		return decfhcssj;
	}
	public void setDecfhcssj(String decfhcssj){
		this.decfhcssj = decfhcssj;
	}
	//序号，排序用
	public String getXh() {
		return xh;
	}
	public void setXh(String xh){
		this.xh = xh;
	}
	//是否需要传参
	public String getSfcc() {
		return sfcc;
	}
	public void setSfcc(String sfcc){
		this.sfcc = sfcc;
	}
	//备注说明
	public String getBz() {
		return bz;
	}
	public void setBz(String bz){
		this.bz = bz;
	}
	//区域标识
	public String qybs;

	
	

	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	public String getMethodname() {
		return methodname;
	}
	public void setMethodname(String methodname) {
		this.methodname = methodname;
	}
	public String getParamlx() {
		return paramlx;
	}
	public void setParamlx(String paramlx) {
		this.paramlx = paramlx;
	}

	public String getQybs() {
		return qybs;
	}

	public void setQybs(String qybs) {
		this.qybs = qybs;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
