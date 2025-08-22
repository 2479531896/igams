package com.matridx.igams.wechat.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="BfglDto")
public class BfglDto extends BfglModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//录入人员list
	private List<String> lrrylist;
	//全部查询内容
	private String entire;
	//列表加载的条数
	private String count;
	//从第几条开始
	private String start;
	//钉钉id
	private String ddid;
	//单位名称
	private String dwmc;
	//单位名称
	private String dwjc;
	//微信id
	private String wxid;
	//单位分类名称
	private String dwflmc;
	//单位类别名称
	private String dwlbmc;
	//单位等级名称
	private String dwdjmc;
	//省份名称
	private String sfmc;
	//判断是个人清单还是全部清单,"personal"代表个人，"all"代表全部
	private String list_flg;
	//拜访时长
	private String bfsc;
	//录入人员名称
	private String lrrymc;
    //测序仪器标记名称
    private String cxyqbjmc;
    //拜访人名称
    private String bfrmc;
    //单位分类[多]
  	private String[] dwfls;
  	//单位等级[多]
  	private String[] dwdjs;
  	//单位类别[多]
  	private String[] dwlbs;
  	//统计日期起始
  	private String tjrqstart;
  	//统计日期结束
  	private String tjrqend;
  	//拜访次数
  	private String bfcs;
  	//拜访单位个数
  	private String bfdwgs;
  	//拜访时长
  	private String bfzsc;
  	//统计条件flag(all,day,week,month)
  	private String tjtj_flag;
  	//单位分类代码
  	private String dwfldm;
  	//分类参数代码
  	private String fl_csdm;
	//联系人姓名
	private String lxrxm;
	//联系电话
	private String dh;
	//部门科室
	private String bmks;
	//拜访时间
	private String bfsj;
	//拜访类型名称
	private String bflxmc;
	//区分名称
	private String qfmc;
	private List<String> fjids;
	//开始录入时间
	private String lrsjstart;
	//结束录入时间
	private String lrsjend;
	//拜访类型[多]
	private String[] bflxs;
	//区分[多]
	private String[] qfs;
	//对象编码
	private String dxbm;
	//开始拜访时间
	private String bfsjstart;
	//结束拜访时间
	private String bfsjend;
	//附件保存标记
	private String fjbcbj;
	//机构ID[多]
	private String[] jgids;
	//业务员[多]
	private String[] ywys;
	//单位限制标记
	private String dwxzbj;
	//用户ID
	private String yhid;
	//占比
	private String ratio;
	//颜色
	private String color;
	private String ddanbj;
	private String ycid;

	public String getYcid() {
		return ycid;
	}

	public void setYcid(String ycid) {
		this.ycid = ycid;
	}

	public String getDdanbj() {
		return ddanbj;
	}

	public void setDdanbj(String ddanbj) {
		this.ddanbj = ddanbj;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getDwxzbj() {
		return dwxzbj;
	}

	public void setDwxzbj(String dwxzbj) {
		this.dwxzbj = dwxzbj;
	}

	public String getYhid() {
		return yhid;
	}

	public void setYhid(String yhid) {
		this.yhid = yhid;
	}

	public String getRatio() {
		return ratio;
	}

	public void setRatio(String ratio) {
		this.ratio = ratio;
	}

	public String[] getJgids() {
		return jgids;
	}

	public void setJgids(String[] jgids) {
		this.jgids = jgids;
	}

	public String[] getYwys() {
		return ywys;
	}

	public void setYwys(String[] ywys) {
		this.ywys = ywys;
	}

	public String getFjbcbj() {
		return fjbcbj;
	}

	public void setFjbcbj(String fjbcbj) {
		this.fjbcbj = fjbcbj;
	}

	public String getBfsjstart() {
		return bfsjstart;
	}

	public void setBfsjstart(String bfsjstart) {
		this.bfsjstart = bfsjstart;
	}

	public String getBfsjend() {
		return bfsjend;
	}

	public void setBfsjend(String bfsjend) {
		this.bfsjend = bfsjend;
	}

	public String getDxbm() {
		return dxbm;
	}

	public void setDxbm(String dxbm) {
		this.dxbm = dxbm;
	}

	public String[] getBflxs() {
		return bflxs;
	}

	public void setBflxs(String[] bflxs) {
		this.bflxs = bflxs;
	}

	public String[] getQfs() {
		return qfs;
	}

	public void setQfs(String[] qfs) {
		this.qfs = qfs;
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

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}

	public String getLxrxm() {
		return lxrxm;
	}

	public void setLxrxm(String lxrxm) {
		this.lxrxm = lxrxm;
	}

	public String getDh() {
		return dh;
	}

	public void setDh(String dh) {
		this.dh = dh;
	}

	public String getBmks() {
		return bmks;
	}

	public void setBmks(String bmks) {
		this.bmks = bmks;
	}

	public String getBfsj() {
		return bfsj;
	}

	public void setBfsj(String bfsj) {
		this.bfsj = bfsj;
	}

	public String getBflxmc() {
		return bflxmc;
	}

	public void setBflxmc(String bflxmc) {
		this.bflxmc = bflxmc;
	}

	public String getQfmc() {
		return qfmc;
	}

	public void setQfmc(String qfmc) {
		this.qfmc = qfmc;
	}

	public String getDwfldm() {
		return dwfldm;
	}

	public void setDwfldm(String dwfldm) {
		this.dwfldm = dwfldm;
	}

	public String getTjtj_flag() {
		return tjtj_flag;
	}

	public void setTjtj_flag(String tjtj_flag) {
		this.tjtj_flag = tjtj_flag;
	}

	public String getBfcs() {
		return bfcs;
	}

	public void setBfcs(String bfcs) {
		this.bfcs = bfcs;
	}

	public String getBfdwgs() {
		return bfdwgs;
	}

	public void setBfdwgs(String bfdwgs) {
		this.bfdwgs = bfdwgs;
	}

	public String getBfzsc() {
		return bfzsc;
	}

	public void setBfzsc(String bfzsc) {
		this.bfzsc = bfzsc;
	}

	public String getTjrqstart() {
		return tjrqstart;
	}

	public void setTjrqstart(String tjrqstart) {
		this.tjrqstart = tjrqstart;
	}

	public String getTjrqend() {
		return tjrqend;
	}

	public void setTjrqend(String tjrqend) {
		this.tjrqend = tjrqend;
	}

	public String getBfrmc() {
		return bfrmc;
	}

	public void setBfrmc(String bfrmc) {
		this.bfrmc = bfrmc;
	}
    //导出sql
    private String sqlparam;
    //拜访人导出标记
    private String bfr_flg;
    //单位分类导出标记
    private String dwfl_flg;
    //单位等级导出标记
    private String dwdj_flg;
    //单位类别导出标记
    private String dwlb_flg;
    //省份导出标记
    private String sf_flg;
    //单位id
    private String[] dwids;   
    
	public String getLrrymc() {
		return lrrymc;
	}

	public void setLrrymc(String lrrymc) {
		this.lrrymc = lrrymc;
	}

	public String getBfsc() {
		return bfsc;
	}

	public void setBfsc(String bfsc) {
		this.bfsc = bfsc;
	}

	public String getList_flg() {
		return list_flg;
	}

	public void setList_flg(String list_flg) {
		this.list_flg = list_flg;
	}

	public String getDdid() {
		return ddid;
	}

	public void setDdid(String ddid) {
		this.ddid = ddid;
	}

	public String getWxid() {
		return wxid;
	}

	public void setWxid(String wxid) {
		this.wxid = wxid;
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

	public List<String> getLrrylist() {
		return lrrylist;
	}

	public void setLrrylist(List<String> lrrylist) {
		this.lrrylist = lrrylist;
	}

	public String getDwflmc() {
		return dwflmc;
	}

	public void setDwflmc(String dwflmc) {
		this.dwflmc = dwflmc;
	}

	public String getDwdjmc() {
		return dwdjmc;
	}

	public void setDwdjmc(String dwdjmc) {
		this.dwdjmc = dwdjmc;
	}

	public String getDwlbmc() {
		return dwlbmc;
	}

	public void setDwlbmc(String dwlbmc) {
		this.dwlbmc = dwlbmc;
	}

	public String getSfmc() {
		return sfmc;
	}

	public void setSfmc(String sfmc) {
		this.sfmc = sfmc;
	}

	public String getDwmc() {
		return dwmc;
	}

	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}

	public String getDwjc() {
		return dwjc;
	}

	public void setDwjc(String dwjc) {
		this.dwjc = dwjc;
	}

	public String getCxyqbjmc() {
		return cxyqbjmc;
	}

	public void setCxyqbjmc(String cxyqbjmc) {
		this.cxyqbjmc = cxyqbjmc;
	}

	public String getSqlparam() {
		return sqlparam;
	}

	public void setSqlparam(String sqlparam) {
		this.sqlparam = sqlparam;
	}

	public String getBfr_flg() {
		return bfr_flg;
	}

	public void setBfr_flg(String bfr_flg) {
		this.bfr_flg = bfr_flg;
	}

	public String getDwfl_flg() {
		return dwfl_flg;
	}

	public void setDwfl_flg(String dwfl_flg) {
		this.dwfl_flg = dwfl_flg;
	}

	public String getDwdj_flg() {
		return dwdj_flg;
	}

	public void setDwdj_flg(String dwdj_flg) {
		this.dwdj_flg = dwdj_flg;
	}

	public String getDwlb_flg() {
		return dwlb_flg;
	}

	public void setDwlb_flg(String dwlb_flg) {
		this.dwlb_flg = dwlb_flg;
	}

	public String getSf_flg() {
		return sf_flg;
	}

	public void setSf_flg(String sf_flg) {
		this.sf_flg = sf_flg;
	}

	public String[] getDwids() {
		return dwids;
	}

	public void setDwids(String[] dwids) {
		this.dwids = dwids;
		for (int i = 0; i < dwids.length; i++){
			this.dwids[i]=this.dwids[i].replace("'","");
		}
	}
	public String[] getDwfls() {
		return dwfls;
	}
	public void setDwfls(String[] dwfls) {
		this.dwfls = dwfls;
		for (int i = 0; i < dwfls.length; i++){
			this.dwfls[i]=this.dwfls[i].replace("'","");
		}
	}
	public String[] getDwdjs() {
		return dwdjs;
	}
	public void setDwdjs(String[] dwdjs) {
		this.dwdjs = dwdjs;
		for (int i = 0; i < dwdjs.length; i++){
			this.dwdjs[i]=this.dwdjs[i].replace("'","");
		}
	}
	public String[] getDwlbs() {
		return dwlbs;
	}
	public void setDwlbs(String[] dwlbs) {
		this.dwlbs = dwlbs;
		for (int i = 0; i < dwlbs.length; i++){
			this.dwlbs[i]=this.dwlbs[i].replace("'","");
		}
	}

	public String getFl_csdm() {
		return fl_csdm;
	}

	public void setFl_csdm(String fl_csdm) {
		this.fl_csdm = fl_csdm;
	}
	
}
