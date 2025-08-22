package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="SjpdglDto")
public class SjpdglDto extends SjpdglModel{
	private String ztmc;//状态名称
	private String jdbjmc;//接单标记名称
	public String pdbjmc;//取消派单标记名称
	private String sfsfmc;//是否收费名称
	private String jsfsmc;//寄送方式名称
	private String jcdwmc;//检测单位名称
	private String bblxmc;//标本类型名称
	private String pdrmc;//派单人名称
	private String xnzd;//虚拟字段
	private String pdzt;//派单状态
	private String jdr;//接单人
	private String sdsj;//送达时间
	private String sign;//用权限区分
	private List<String> sjhbs;//伙伴限制
	private String dqjsdwxdbj;//单位限制
	private String jsid;//角色id
	private List<String> sjjcdws;//检测单位显示
	private String sjtdid;//送检团单id
	private String tdbj;//根据团单标记判断发送消息格式
	private String sqlParam;
    private String yjsjkssj;//预计时间开始时间/提前小时数目
	private String yjsjjssj;//预计时间结束时间/延后小时数目
	//附件保存标记
	private String fjbcbj;
	//接单人名称
	private String jdrmc;
	//接单人钉钉ID
	private String jdrddid;
	//派单寄送方式名称
	private String pdjsfsmc;
	//送达人员名称
	private String sdrymc;
	//高级筛选 检测单位
	private String[] jcdws;
	//高级筛选 标本类型
	public String []bblxs;
	//高级筛选 是否收费
	public String [] sfsfs;
	//高级筛查 接单标记
	public String [] jdbjs;
	//高级筛查 寄送方式
	public String [] jsfss;
	//高级筛查 寄送方式
	public String [] zts;
	//通知人员字符串
	private String tzry_str;
	//显示名称
	private String xsmc;
	//删除标记数组
	private String[] scbjs;
	//人员ID
	private String ryid;
	//录入人员名称
	private String lrrymc;
	//关联单号
	private String gldh;
	//entire
	private String entire;
	//取样时间标记
	private String qysjbj;
	//物流ID
	private String wlid;
	//物流状态
	private String wlzt;
	//寄送方式代码
	private String jsfsdm;
	//预估取件时间
	private String ygqjsj;
	//派单人钉钉ID
	private String pdrddid;
	//上级物流ID
	private String sjwlid;
	//到达地点
	private String dddd;
	//班次
	private String bc;
	//上级送达地点
	private String sjqydz;
	//物流状态
	private String[] wlzts;
	//派单号
	private String pdh;
	//患者姓名
	private String hzxm;
	//录入开始时间
	private String lrsjstart;
	//录入结束时间
	private String lrsjend;
	//附件ID复数
	private List<String> fjids;
		//历史记录
	private String lsjl;
	//物流费用
	private String wlfy;
	//录入人员钉钉id
	private String lrryddid;
	//用户名
	private String yhm;

	public String getYhm() {
		return yhm;
	}

	public void setYhm(String yhm) {
		this.yhm = yhm;
	}

	public String getYjsjkssj() {
		return yjsjkssj;
	}

	public void setYjsjkssj(String yjsjkssj) {
		this.yjsjkssj = yjsjkssj;
	}

	public String getYjsjjssj() {
		return yjsjjssj;
	}

	public void setYjsjjssj(String yjsjjssj) {
		this.yjsjjssj = yjsjjssj;
	}
	public String getFjbcbj() {
		return fjbcbj;
	}

	public void setFjbcbj(String fjbcbj) {
		this.fjbcbj = fjbcbj;
	}

	public String getTdbj() {
		return tdbj;
	}

	public void setTdbj(String tdbj) {
		this.tdbj = tdbj;
	}

	public String getSjtdid() {
		return sjtdid;
	}

	public void setSjtdid(String sjtdid) {
		this.sjtdid = sjtdid;
	}
	public String getSqlParam() {
		return sqlParam;
	}

	public void setSqlParam(String sqlParam) {
		this.sqlParam = sqlParam;
	}
	public List<String> getSjjcdws() {
		return sjjcdws;
	}

	public void setSjjcdws(List<String> sjjcdws) {
		this.sjjcdws = sjjcdws;
	}

	public String getJsid() {
		return jsid;
	}

	public void setJsid(String jsid) {
		this.jsid = jsid;
	}

	public String getDqjsdwxdbj() {
		return dqjsdwxdbj;
	}

	public void setDqjsdwxdbj(String dqjsdwxdbj) {
		this.dqjsdwxdbj = dqjsdwxdbj;
	}

	public List<String> getSjhbs() {
		return sjhbs;
	}

	public void setSjhbs(List<String> sjhbs) {
		this.sjhbs = sjhbs;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSdsj() {
		return sdsj;
	}

	public void setSdsj(String sdsj) {
		this.sdsj = sdsj;
	}

	public String getLrryddid() {
		return lrryddid;
	}

	public void setLrryddid(String lrryddid) {
		this.lrryddid = lrryddid;
	}

	public String getWlfy() {
		return wlfy;
	}

	public void setWlfy(String wlfy) {
		this.wlfy = wlfy;
	}

	public String getLsjl() {
		return lsjl;
	}

	public void setLsjl(String lsjl) {
		this.lsjl = lsjl;
	}

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
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

	public String getHzxm() {
		return hzxm;
	}

	public void setHzxm(String hzxm) {
		this.hzxm = hzxm;
	}

	@Override
	public String getPdh() {
		return pdh;
	}

	@Override
	public void setPdh(String pdh) {
		this.pdh = pdh;
	}

	public String[] getWlzts() {
		return wlzts;
	}

	public void setWlzts(String[] wlzts) {
		this.wlzts = wlzts;
	}

	public String getSjqydz() {
		return sjqydz;
	}

	public void setSjqydz(String sjqydz) {
		this.sjqydz = sjqydz;
	}

	//	private List<User> tzryList;//通知人员
	private List<SjwltzDto> tzryList;//通知人员

	public List<SjwltzDto> getTzryList() {
		return tzryList;
	}

	public void setTzryList(List<SjwltzDto> tzryList) {
		this.tzryList = tzryList;
	}

	public String getSdrymc() {
		return sdrymc;
	}

	public void setSdrymc(String sdrymc) {
		this.sdrymc = sdrymc;
	}

	public String getPdjsfsmc() {
		return pdjsfsmc;
	}

	public void setPdjsfsmc(String pdjsfsmc) {
		this.pdjsfsmc = pdjsfsmc;
	}

	public String getJdrmc() {
		return jdrmc;
	}

	public void setJdrmc(String jdrmc) {
		this.jdrmc = jdrmc;
	}

	public String getJdrddid() {
		return jdrddid;
	}

	public void setJdrddid(String jdrddid) {
		this.jdrddid = jdrddid;
	}

	public String getPdzt() {
		return pdzt;
	}

	public void setPdzt(String pdzt) {
		this.pdzt = pdzt;
	}

	public String getJdr() {
		return jdr;
	}

	public void setJdr(String jdr) {
		this.jdr = jdr;
	}

	public String getPdztl() {
		return pdzt;
	}

	public void setPdzl(String pdztl) {
		this.pdzt = pdztl;
	}

	public String getXnzd() {
		return xnzd;
	}

	public void setXnzd(String xnzd) {
		this.xnzd = xnzd;
	}

	public String[] getZts() {
		return zts;
	}

	public void setZts(String[] zts) {
		this.zts = zts;
		for (int i = 0; i < zts.length; i++){
			this.zts[i]=this.zts[i].replace("'","");
		}
	}

	public String getBc() {
		return bc;
	}

	public void setBc(String bc) {
		this.bc = bc;
	}

	public String getSjwlid() {
		return sjwlid;
	}

	public void setSjwlid(String sjwlid) {
		this.sjwlid = sjwlid;
	}

	public String getDddd() {
		return dddd;
	}

	public void setDddd(String dddd) {
		this.dddd = dddd;
	}

	public String getPdrddid() {
		return pdrddid;
	}

	public void setPdrddid(String pdrddid) {
		this.pdrddid = pdrddid;
	}

//	public List<User> getTzryList() {
//		return tzryList;
//	}
//
//	public void setTzryList(List<User> tzryList) {
//		this.tzryList = tzryList;
//	}

	public String getYgqjsj() {
		return ygqjsj;
	}

	public void setYgqjsj(String ygqjsj) {
		this.ygqjsj = ygqjsj;
	}

	public String getPdbjmc() {
		return pdbjmc;
	}

	public void setPdbjmc(String pdbjmc) {
		this.pdbjmc = pdbjmc;
	}

	public String getJsfsdm() {
		return jsfsdm;
	}

	public void setJsfsdm(String jsfsdm) {
		this.jsfsdm = jsfsdm;
	}

	public String getZtmc() {
		return ztmc;
	}

	public void setZtmc(String ztmc) {
		this.ztmc = ztmc;
	}

	public String getWlzt() {
		return wlzt;
	}

	public void setWlzt(String wlzt) {
		this.wlzt = wlzt;
	}

	public String getWlid() {
		return wlid;
	}

	public void setWlid(String wlid) {
		this.wlid = wlid;
	}

	public String getQysjbj() {
		return qysjbj;
	}

	public void setQysjbj(String qysjbj) {
		this.qysjbj = qysjbj;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getLrrymc() {
		return lrrymc;
	}

	public void setLrrymc(String lrrymc) {
		this.lrrymc = lrrymc;
	}

	public String getGldh() {
		return gldh;
	}

	public void setGldh(String gldh) {
		this.gldh = gldh;
	}

	public String getRyid() {
		return ryid;
	}

	public void setRyid(String ryid) {
		this.ryid = ryid;
	}

	public String[] getScbjs() {
		return scbjs;
	}

	public void setScbjs(String[] scbjs) {
			this.scbjs = scbjs;
			for (int i = 0; i < scbjs.length; i++){
				this.scbjs[i]=this.scbjs[i].replace("'","");
			}
	}

	public String getXsmc() {
		return xsmc;
	}

	public void setXsmc(String xsmc) {
		this.xsmc = xsmc;
	}

	public String getTzry_str() {
		return tzry_str;
	}

	public void setTzry_str(String tzry_str) {
		this.tzry_str = tzry_str;
	}


	public String getPdrmc() {
		return pdrmc;
	}

	public void setPdrmc(String pdrmc) {
		this.pdrmc = pdrmc;
	}
	public String getJsfsmc() {
		return jsfsmc;
	}

	public void setJsfsmc(String jsfsmc) {
		this.jsfsmc = jsfsmc;
	}

	public String[] getJsfss() {
		return jsfss;
	}

	public void setJsfss(String[] jsfss) {
		this.jsfss = jsfss;
		for (int i = 0; i < jsfss.length; i++){
			this.jsfss[i]=this.jsfss[i].replace("'","");
		}
	}

	public String[] getJdbjs() {
		return jdbjs;
	}

	public void setJdbjs(String[] jdbjs) {
		this.jdbjs = jdbjs;
		for (int i = 0; i < jdbjs.length; i++){
			this.jdbjs[i]=this.jdbjs[i].replace("'","");
		}
	}

	public String[] getSfsfs() {
		return sfsfs;
	}

	public void setSfsfs(String[] sfsfs) {
		this.sfsfs = sfsfs;
		for (int i = 0; i < sfsfs.length; i++){
			this.sfsfs[i]=this.sfsfs[i].replace("'","");
		}
	}

	public String[] getBblxs() {
		return bblxs;
	}

	public void setBblxs(String[] bblxs) {
		this.bblxs = bblxs;
		for (int i = 0; i < bblxs.length; i++){
			this.bblxs[i]=this.bblxs[i].replace("'","");
		}
	}

	public String getBblxmc() {
		return bblxmc;
	}

	public void setBblxmc(String bblxmc) {
		this.bblxmc = bblxmc;
	}

	public String getSfsfmc() {
		return sfsfmc;
	}

	public void setSfsfmc(String sfsfmc) {
		this.sfsfmc = sfsfmc;
	}

	public String getJcdwmc() {
		return jcdwmc;
	}

	public void setJcdwmc(String jcdwmc) {
		this.jcdwmc = jcdwmc;
	}

	public String[] getJcdws() {
		return jcdws;
	}

	public void setJcdws(String[] jcdws) {
		this.jcdws = jcdws;
		for (int i = 0; i < jcdws.length; i++){
			this.jcdws[i]=this.jcdws[i].replace("'","");
		}
	}

	public String getJdbjmc() {
		return jdbjmc;
	}

	public void setJdbjmc(String jdbjmc) {
		this.jdbjmc = jdbjmc;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
