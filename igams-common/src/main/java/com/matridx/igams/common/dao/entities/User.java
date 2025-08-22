package com.matridx.igams.common.dao.entities;

import com.matridx.springboot.util.base.StringUtil;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Alias(value="User")
public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//用户ID
	private String yhid;
	//角色ID
	private String jsid;
	private String mrfz;
	//用户名
	private String yhm;
	//密码
	private String mm;
	//真实姓名
	private String zsxm;
	//是否锁定
	private String sfsd;
	//登录时间
	private String dlsj;
	//错误次数
	private String cwcs;
	//该用户所有角色
	private List<String> jsids;
	//该用户所有角色名称
	private List<String> jsmcs;
	//当前角色
	private String dqjs;
	//当前角色代码
	private String dqjsdm;
	//当前角色名称
	private String dqjsmc;
	//当前角色单位限定标记
	private String dqjsdwxdbj;
	//用户角色下的操作权限列表
	private List<QxModel> qxModels;
	//用户角色下的系统权限列表
	private List<JsxtqxDto> xtqxDtos;
	//token值
	private String access_token;
	//刷新token值
	private String refresh_token;
	//权限限定标记
	private Map<String, Map<String, Object>> qxxdbjMap = new HashMap<>();
	// 委托人id
	private String wtrid;
	// 委托人用户名
	private String wtryhm;
	// 委托人名称
	private String wtrmc;
	// 钉钉ID
	private String ddid;	
	// 微信ID
	private String wechatid;
	// 岗位名称
	private String gwmc;
	//ID复数
	private List<String> ids;
	//序号
	private String xh;
	//机构id
	private String jgid;
	//外部程序id
	private String wbcxid;
	//登录ip
	private String dlip;
	//密码强度flg
	private String mm_flg;
	//分布式标记
	private String prefix;
	//录入人员
	private String lrry;
	//录入时间
	private String lrsj;
	//修改人员
	private String xgry;
	//修改时间
	private String xgsj;
	//删除人员
	private String scry;
	//删除时间
	private String scsj;
	//删除标记
	private String scbj;
	
	// 系统用户ID
	private String xtyhid;
	//单位代码
	private String jgdm;
	//单位名称
	private String jgmc;
	//锁定时间
	private String sdtime;
	//解锁时间
	private String jstime;
	//审核备用参数
	private  List<ShgcDto> bycsList;
	//仓库权限
	private String ckqx;
	//client_id
	private String client_id;
	//角色名称
	private String jsmc;
	//联合名称
	private String lhmc;
	//密码修改时间
	private String mmxgsj;
	//用户角色下的无按钮列表权限
	private List<QxModel> nobtnlistModels;
	//U8用户id
	private String grouping;
	//非用户名登录标记：微信或者钉钉登录时的标记 0:正常， 1：微信或者钉钉
	private String loginFlg;
	//钉钉头像路径
	private String ddtxlj;
	//钉钉小程序ID
	private String miniappid;
	//外部程序代码
	private String wbcxdm;
	//列表加载的条数
	private String count;
	//从第几条开始
	private String start;
	//全部查询内容
	private String entire;
	//邮箱
	private String email;
	//用户在当前开发者企业账号范围内的唯一标识
	private String unionid;
	private boolean upRedis;//更新redis

	//用户名s
	private List<String> yhms;

	public List<String> getYhms() {
		return yhms;
	}

	public void setYhms(List<String> yhms) {
		this.yhms = yhms;
	}

	public boolean getUpRedis() {
		return upRedis;
	}

	public void setUpRedis(boolean upRedis) {
		this.upRedis = upRedis;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	private String csid;

	private String csmc;

	public String getMrfz() {
		return mrfz;
	}

	public void setMrfz(String mrfz) {
		this.mrfz = mrfz;
	}

	public String getWbcxdm() {
		return wbcxdm;
	}

	public void setWbcxdm(String wbcxdm) {
		this.wbcxdm = wbcxdm;
	}

	public String getWbcxid() {
		return wbcxid;
	}

	public void setWbcxid(String wbcxid) {
		this.wbcxid = wbcxid;
	}

	public String getDdtxlj() {
		return ddtxlj;
	}

	public void setDdtxlj(String ddtxlj) {
		this.ddtxlj = ddtxlj;
	}

	public String getGrouping() {
		return grouping;
	}

	public void setGrouping(String grouping) {
		this.grouping = grouping;
	}

	public List<QxModel> getNobtnlistModels() {
		return nobtnlistModels;
	}

	public void setNobtnlistModels(List<QxModel> nobtnlistModels) {
		this.nobtnlistModels = nobtnlistModels;
	}

	public String getLhmc() {
		return lhmc;
	}

	public void setLhmc(String lhmc) {
		this.lhmc = lhmc;
	}
	public String getJsmc() {
		return jsmc;
	}

	public void setJsmc(String jsmc) {
		this.jsmc = jsmc;
	}

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getCkqx() {
		return ckqx;
	}

	public void setCkqx(String ckqx) {
		this.ckqx = ckqx;
	}

	public List<ShgcDto> getBycsList() {
		return bycsList;
	}

	public void setBycsList(List<ShgcDto> bycsList) {
		this.bycsList = bycsList;
	}

	public String getSdtime() {
		return sdtime;
	}

	public void setSdtime(String sdtime) {
		this.sdtime = sdtime;
	}

	public String getJstime() {
		return jstime;
	}

	public void setJstime(String jstime) {
		this.jstime = jstime;
	}

	public String getJgmc() {
		return jgmc;
	}
	public void setJgmc(String jgmc) {
		this.jgmc = jgmc;
	}
	public String getJgdm() {
		return jgdm;
	}
	public void setJgdm(String jgdm) {
		this.jgdm = jgdm;
	}
	public String getXtyhid() {
		return xtyhid;
	}
	public void setXtyhid(String xtyhid) {
		this.xtyhid = xtyhid;
	}
	public String getJsid() {
		return jsid;
	}
	public void setJsid(String jsid) {
		this.jsid = jsid;
	}
	public String getLrry() {
		return lrry;
	}
	public void setLrry(String lrry) {
		this.lrry = lrry;
	}
	public String getLrsj() {
		return lrsj;
	}
	public void setLrsj(String lrsj) {
		this.lrsj = lrsj;
	}
	public String getXgry() {
		return xgry;
	}
	public void setXgry(String xgry) {
		this.xgry = xgry;
	}
	public String getXgsj() {
		return xgsj;
	}
	public void setXgsj(String xgsj) {
		this.xgsj = xgsj;
	}
	public String getScry() {
		return scry;
	}
	public void setScry(String scry) {
		this.scry = scry;
	}
	public String getScsj() {
		return scsj;
	}
	public void setScsj(String scsj) {
		this.scsj = scsj;
	}
	public String getScbj() {
		return scbj;
	}
	public void setScbj(String scbj) {
		this.scbj = scbj;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getDlip() {
		return dlip;
	}
	public void setDlip(String dlip) {
		this.dlip = dlip;
	}
	public String getMm_flg() {
		return mm_flg;
	}
	public void setMm_flg(String mm_flg) {
		this.mm_flg = mm_flg;
	}
	public String getJgid() {
		return jgid;
	}
	public void setJgid(String jgid) {
		this.jgid = jgid;
	}
	public String getXh() {
		return xh;
	}
	public void setXh(String xh) {
		this.xh = xh;
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
	public String getZsxm() {
		return zsxm;
	}
	public void setZsxm(String zsxm) {
		this.zsxm = zsxm;
	}
	public String getSfsd() {
		return sfsd;
	}
	public void setSfsd(String sfsd) {
		this.sfsd = sfsd;
	}
	public List<String> getJsids() {
		return jsids;
	}
	public void setJsids(List<String> jsids) {
		this.jsids = jsids;
	}
	public List<String> getJsmcs() {
		return jsmcs;
	}
	public void setJsmcs(List<String> jsmcs) {
		this.jsmcs = jsmcs;
	}
	public String getDqjs() {
		return dqjs;
	}
	public void setDqjs(String dqjs) {
		this.dqjs = dqjs;
	}
	public List<QxModel> getQxModels() {
		return qxModels;
	}
	public void setQxModels(List<QxModel> qxModels) {
		this.qxModels = qxModels;
	}
	public String getDqjsdm() {
		return dqjsdm;
	}
	public void setDqjsdm(String dqjsdm) {
		this.dqjsdm = dqjsdm;
	}
	public String getDqjsmc() {
		return dqjsmc;
	}
	public void setDqjsmc(String dqjsmc) {
		this.dqjsmc = dqjsmc;
	}
	public String getMm() {
		return mm;
	}
	public void setMm(String mm) {
		this.mm = mm;
	}
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getRefresh_token() {
		return refresh_token;
	}
	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}
	public String getCwcs() {
		return cwcs;
	}
	public void setCwcs(String cwcs) {
		this.cwcs = cwcs;
	}
	public String getDlsj() {
		return dlsj;
	}
	public void setDlsj(String dlsj) {
		this.dlsj = dlsj;
	}
	public Map<String, Map<String, Object>> getQxxdbjMap() {
		return qxxdbjMap;
	}
	public void setQxxdbjMap(Map<String, Map<String, Object>> qxxdbjMap) {
		this.qxxdbjMap = qxxdbjMap;
	}
	public String getWtrid() {
		return wtrid;
	}
	public void setWtrid(String wtrid) {
		this.wtrid = wtrid;
	}
	public String getWtryhm() {
		return wtryhm;
	}
	public void setWtryhm(String wtryhm) {
		this.wtryhm = wtryhm;
	}
	public String getWtrmc() {
		return wtrmc;
	}
	public void setWtrmc(String wtrmc) {
		this.wtrmc = wtrmc;
	}
	public String getDdid() {
		return ddid;
	}
	public void setDdid(String ddid) {
		this.ddid = ddid;
	}
	public String getWechatid() {
		return wechatid;
	}
	public void setWechatid(String wechatid) {
		this.wechatid = wechatid;
	}
	public String getGwmc() {
		return gwmc;
	}
	public void setGwmc(String gwmc) {
		this.gwmc = gwmc;
	}
	public List<String> getIds() {
		return ids;
	}
	public void setIds(String ids) {
		if(StringUtil.isNotBlank(ids)){
			List<String> list;
			String[] str = ids.split(",");
			list = Arrays.asList(str);
			this.ids = list;
		}else {
			this.ids = null;
		}
	}
	public void setIds(List<String> ids) {
		if(ids!=null && !ids.isEmpty()){
            ids.replaceAll(s -> s.replace("[", "").replace("]", "").trim());
		}
		this.ids = ids;
	}
	public String getDqjsdwxdbj() {
		return dqjsdwxdbj;
	}
	public void setDqjsdwxdbj(String dqjsdwxdbj) {
		this.dqjsdwxdbj = dqjsdwxdbj;
	}
	public List<JsxtqxDto> getXtqxDtos() {
		return xtqxDtos;
	}
	public void setXtqxDtos(List<JsxtqxDto> xtqxDtos) {
		this.xtqxDtos = xtqxDtos;
	}

	public String getMmxgsj() {
		return mmxgsj;
	}

	public void setMmxgsj(String mmxgsj) {
		this.mmxgsj = mmxgsj;
	}

	public String getLoginFlg() {
		return loginFlg;
	}

	public void setLoginFlg(String loginFlg) {
		this.loginFlg = loginFlg;
	}

	public String getMiniappid() {
		return miniappid;
	}

	public void setMiniappid(String miniappid) {
		this.miniappid = miniappid;
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

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getCsid() {
		return csid;
	}

	public void setCsid(String csid) {
		this.csid = csid;
	}

	public String getCsmc() {
		return csmc;
	}

	public void setCsmc(String csmc) {
		this.csmc = csmc;
	}
}
