package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="SjwzxxModel")
public class SjwzxxModel extends BaseModel{
	//送检物种ID
	private String sjwzid;
	//送检ID
	private String sjid;
	//物种ID
	private String wzid;
	//物种英文名
	private String wzywm;
	//物种中文名
	private String wzzwm;
	//物种分类
	private String wzfl;
	//结果类型  现分为高关注度和疑似
	private String jglx;
	//序号
	private String xh;
	//读取数
	private String dqs;
	//基因组覆盖度
	private String jyzfgd;
	//芯片信息
	private String xpxx;
	//相对丰度
	private String xdfd;
	//属ID
	private String sid;
	//属名
	private String sm;
	//属中文名
	private String szwm;
	//属的读数
	private String sdds;
	//属丰度
	private String sfd;
	//细菌类型
	private String xjlx;
	//病毒类型
	private String bdlx;
	//检测类型
	private String jclx;
	//检测子类型
	private String jczlx;
	//物种分类类型
	private String wzfllx;
	//文献id
	private String wxid;
	//是否高亮
	private String sfgl;
	//校正人源指数
	private String jzryzs;
	//校正人源指数百分比
	private String jzryzsbfb;
	//校正人源指数位置
	private String jzryzswz;
	//核酸类型
	private String hslx;
	//特殊分类
	private String tsfl;
	//新冠分型
	private String xgfx;
	//json
	private String json;
	//技术模型
	private String project_type;
	//是否汇报
	private String sfhb;
	//总扩增子数
	private String zkzz;
	//比对成功扩增子
	private String bdcgkzz;

	public String getSfhb() {
		return sfhb;
	}

	public void setSfhb(String sfhb) {
		this.sfhb = sfhb;
	}

	public String getProject_type() {
		return project_type;
	}

	public void setProject_type(String project_type) {
		this.project_type = project_type;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getXgfx() {
		return xgfx;
	}

	public void setXgfx(String xgfx) {
		this.xgfx = xgfx;
	}

	public String getTsfl() {
		return tsfl;
	}

	public void setTsfl(String tsfl) {
		this.tsfl = tsfl;
	}

	public String getHslx() {
		return hslx;
	}

	public void setHslx(String hslx) {
		this.hslx = hslx;
	}

	public String getJzryzswz() {
		return jzryzswz;
	}
	public void setJzryzswz(String jzryzswz) {
		this.jzryzswz = jzryzswz;
	}
	public String getJzryzs() {
		return jzryzs;
	}
	public void setJzryzs(String jzryzs) {
		this.jzryzs = jzryzs;
	}
	public String getJzryzsbfb() {
		return jzryzsbfb;
	}
	public void setJzryzsbfb(String jzryzsbfb) {
		this.jzryzsbfb = jzryzsbfb;
	}
	public String getWxid() {
		return wxid;
	}
	public void setWxid(String wxid) {
		this.wxid = wxid;
	}
	//物种分类类型
	public String getWzfllx() {
		return wzfllx;
	}
	public void setWzfllx(String wzfllx) {
		this.wzfllx = wzfllx;
	}
	//送检物种ID
	public String getSjwzid() {
		return sjwzid;
	}
	public void setSjwzid(String sjwzid){
		this.sjwzid = sjwzid;
	}
	//送检ID
	public String getSjid() {
		return sjid;
	}
	public void setSjid(String sjid){
		this.sjid = sjid;
	}
	//物种ID
	public String getWzid() {
		return wzid;
	}
	public void setWzid(String wzid){
		this.wzid = wzid;
	}
	//物种英文名
	public String getWzywm() {
		return wzywm;
	}
	public void setWzywm(String wzywm){
		this.wzywm = wzywm;
	}
	//物种中文名
	public String getWzzwm() {
		return wzzwm;
	}
	public void setWzzwm(String wzzwm){
		this.wzzwm = wzzwm;
	}
	//物种分类
	public String getWzfl() {
		return wzfl;
	}
	public void setWzfl(String wzfl){
		this.wzfl = wzfl;
	}
	//结果类型  现分为高关注度和疑似
	public String getJglx() {
		return jglx;
	}
	public void setJglx(String jglx){
		this.jglx = jglx;
	}
	//读取数
	public String getDqs() {
		return dqs;
	}
	public void setDqs(String dqs){
		this.dqs = dqs;
	}

	public int getIntDqs() {
		try {
			if(dqs != null && !"".equals(dqs))
				return Integer.parseInt(dqs);
		}catch(Exception e) {
			return 0;
		}
		return 0;
	}
	//基因组覆盖度
	public String getJyzfgd() {
		return jyzfgd;
	}
	public void setJyzfgd(String jyzfgd){
		this.jyzfgd = jyzfgd;
	}
	//芯片信息
	public String getXpxx() {
		return xpxx;
	}
	public void setXpxx(String xpxx){
		this.xpxx = xpxx;
	}
	//相对丰度
	public String getXdfd() {
		return xdfd;
	}
	public void setXdfd(String xdfd){
		this.xdfd = xdfd;
	}
	//序号
	public String getXh() {
		return xh;
	}
	public void setXh(String xh) {
		this.xh = xh;
	}
	
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getSm() {
		return sm;
	}
	public void setSm(String sm) {
		this.sm = sm;
	}
	public String getSzwm() {
		return szwm;
	}
	public void setSzwm(String szwm) {
		this.szwm = szwm;
	}
	public String getSdds() {
		return sdds;
	}
	public void setSdds(String sdds) {
		this.sdds = sdds;
	}
	public String getSfd() {
		return sfd;
	}
	public void setSfd(String sfd) {
		this.sfd = sfd;
	}
	public String getXjlx() {
		return xjlx;
	}
	public void setXjlx(String xjlx) {
		this.xjlx = xjlx;
	}
	public String getBdlx() {
		return bdlx;
	}
	public void setBdlx(String bdlx) {
		this.bdlx = bdlx;
	}
	public String getJclx() {
		return jclx;
	}
	public void setJclx(String jclx) {
		this.jclx = jclx;
	}
	public String getSfgl() {
		return sfgl;
	}
	public void setSfgl(String sfgl) {
		this.sfgl = sfgl;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    public String getJczlx() {
        return jczlx;
    }

    public void setJczlx(String jczlx) {
        this.jczlx = jczlx;
    }

    public String getZkzz() {
        return zkzz;
    }

    public void setZkzz(String zkzz) {
        this.zkzz = zkzz;
    }

    public String getBdcgkzz() {
        return bdcgkzz;
    }

    public void setBdcgkzz(String bdcgkzz) {
        this.bdcgkzz = bdcgkzz;
    }
}
