package com.matridx.igams.web.dao.entities;


import com.matridx.igams.common.dao.entities.QxModel;
import org.apache.ibatis.type.Alias;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Alias(value="XtyhDto")
public class XtyhDto extends XtyhModel implements UserDetails{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//用户角色列表
	private List<YhjsDto> yhjsDtos = null;
	//当前角色名称
	private String dqjsmc;
	//当前角色代码
	private String dqjsdm;
	//角色资源操作表
	private List<QxModel> qxModels;
	//角色ID列表
	private List<String> jsids;
	//原密码
	private String ymm;
	//原用户名
	private String yyhm;
	//角色ID
	private String jsid;
	//机构ID
	private String jgid;
	//机构序号
	private String xh;
	//机构名称
	private String jgmc;
	//token_id
	private String token_id;
	//用户名s
	private List<String> yhms;
	//系统角色
	private List<XtjsDto> xtjsDtos;
	//角色单位权限
	private List<JsdwqxDto> jsdwqxDtos;
	//用户机构权限
	private List<YhjgqxDto> yhjgqxDtos;
	//机构id集合
	private List<String> jgids;
	//签名标记(1为上传)
	private String signflg;
	//业务类型
	private String ywlx;
	//附件ID复数
	private List<String> fjids;
	//单位限定标记
	private String dwxdbj;
	//是否锁定数组
	private String[] sfsds;
	//上次登录日期
	private String dlrq;
	//当前日期
	private String dqrq;
	//起始时间
	private String startdate;
	//结束时间
	private String enddate;
	//用户组ID
	private String yhzid;
	//模糊查询字段
	private String entire;
	//登陆标记，1 钉钉或者微信登陆
	private String loginFlg;
	//外部程序id
	private String wbcxid;
	//外部程序miniappid
	private String miniappid;
	//所属公司
	private String ssgs;
	private String[] ssgss;
	//职级
	private String zj;
	private String sqlParam;
	private String sfsdmc;//是否锁定
	private String sylxdm;//首页类型代码
	private String sylxlj;//首页类型路径

	private String[] permissions;//生信系统内权限
	private List<String> xtqxlist;//系统权限


	public List<String> getXtqxlist() {
		return xtqxlist;
	}

	public void setXtqxlist(List<String> xtqxlist) {
		this.xtqxlist = xtqxlist;
	}

	public String[] getPermissions() {
		return permissions;
	}

	public void setPermissions(String[] permissions) {
		this.permissions = permissions;
	}

	public String getSylxdm() {
		return sylxdm;
	}

	public void setSylxdm(String sylxdm) {
		this.sylxdm = sylxdm;
	}

	public String getSylxlj() {
		return sylxlj;
	}

	public void setSylxlj(String sylxlj) {
		this.sylxlj = sylxlj;
	}

	public String getSfsdmc() {
		return sfsdmc;
	}

	public void setSfsdmc(String sfsdmc) {
		this.sfsdmc = sfsdmc;
	}

	public String getSqlParam() {
		return sqlParam;
	}

	public void setSqlParam(String sqlParam) {
		this.sqlParam = sqlParam;
	}

	public String getZj() {
		return zj;
	}

	public void setZj(String zj) {
		this.zj = zj;
	}

	public String[] getSsgss() {
		return ssgss;
	}

	public void setSsgss(String[] ssgss) {
		this.ssgss = ssgss;
	}

	public String getSsgs() {
		return ssgs;
	}

	public void setSsgs(String ssgs) {
		this.ssgs = ssgs;
	}

	public String getMiniappid() {
		return miniappid;
	}

	public void setMiniappid(String miniappid) {
		this.miniappid = miniappid;
	}

	public String getWbcxid() {
		return wbcxid;
	}

	public void setWbcxid(String wbcxid) {
		this.wbcxid = wbcxid;
	}

	public String getLoginFlg() {
		return loginFlg;
	}

	public void setLoginFlg(String loginFlg) {
		this.loginFlg = loginFlg;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getYhzid() {
		return yhzid;
	}

	public void setYhzid(String yhzid) {
		this.yhzid = yhzid;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}



	public String[] getSfsds() {
		return sfsds;
	}

	public void setSfsds(String[] sfsds) {
		this.sfsds = sfsds;
		for (int i = 0; i < sfsds.length; i++) {
			this.sfsds[i] = this.sfsds[i].replace("'", "");
		}
	}

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	private Collection<? extends GrantedAuthority> authorities;

	
	public String getSignflg() {
		return signflg;
	}

	public void setSignflg(String string) {
		this.signflg = string;
	}

	public String getJgmc() {
		return jgmc;
	}

	public void setJgmc(String jgmc) {
		this.jgmc = jgmc;
	}

	public List<String> getJgids()
	{
		return jgids;
	}

	public void setJgids(List<String> jgids)
	{
		this.jgids = jgids;
	}

	public List<YhjgqxDto> getYhjgqxDtos()
	{
		return yhjgqxDtos;
	}

	public void setYhjgqxDtos(List<YhjgqxDto> yhjgqxDtos)
	{
		this.yhjgqxDtos = yhjgqxDtos;
	}

	public List<JsdwqxDto> getJsdwqxDtos()
	{
		return jsdwqxDtos;
	}

	public void setJsdwqxDtos(List<JsdwqxDto> jsdwqxDtos)
	{
		this.jsdwqxDtos = jsdwqxDtos;
	}

	public List<XtjsDto> getXtjsDtos(){
		return xtjsDtos;
	}

	public void setXtjsDtos(List<XtjsDto> xtjsDtos)
	{
		this.xtjsDtos = xtjsDtos;
	}

	public List<String> getYhms() {
		return yhms;
	}

	public void setYhms(List<String> yhms) {
		this.yhms = yhms;
	}

	public List<QxModel> getQxModels() {
		return qxModels;
	}

	public void setQxModels(List<QxModel> qxModels) {
		this.qxModels = qxModels;
	}

	public List<YhjsDto> getYhjsDtos() {
		return yhjsDtos;
	}

	public void setYhjsDtos(List<YhjsDto> yhjsDtos) {
		this.yhjsDtos = yhjsDtos;
	}
	
	public String getJgid() {
		return jgid;
	}

	public void setJgid(String jgid) {
		this.jgid = jgid;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}
	
	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		// TODO Auto-generated method stub
		this.authorities = authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return mm;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return yhm;
	}

	/**
	 * 用户是否不过期  不过期为true
	 */
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * 账户是否未锁定  未锁定为true
	 */
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return !"1".equals(sfsd);
	}

	/**
	 * 证书是否不过期   不过期为true
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * 是否可用  可用为true
	 */
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public String getDqjsmc() {
		return dqjsmc;
	}

	public void setDqjsmc(String dqjsmc) {
		this.dqjsmc = dqjsmc;
	}

	public String getDqjsdm() {
		return dqjsdm;
	}

	public void setDqjsdm(String dqjsdm) {
		this.dqjsdm = dqjsdm;
	}

	public List<String> getJsids() {
		return jsids;
	}

	public void setJsids(List<String> jsids) {
		this.jsids = jsids;
	}

	public String getYmm() {
		return ymm;
	}

	public void setYmm(String ymm) {
		this.ymm = ymm;
	}

	public String getYyhm() {
		return yyhm;
	}

	public void setYyhm(String yyhm) {
		this.yyhm = yyhm;
	}

	public String getJsid() {
		return jsid;
	}

	public void setJsid(String jsid) {
		this.jsid = jsid;
	}

	public String getToken_id() {
		return token_id;
	}

	public void setToken_id(String token_id) {
		this.token_id = token_id;
	}

	public String getDwxdbj() {
		return dwxdbj;
	}

	public void setDwxdbj(String dwxdbj) {
		this.dwxdbj = dwxdbj;
	}

	public String getDlrq() {
		return dlrq;
	}

	public void setDlrq(String dlrq) {
		this.dlrq = dlrq;
	}

	public String getDqrq() {
		return dqrq;
	}

	public void setDqrq(String dqrq) {
		this.dqrq = dqrq;
	}
}
