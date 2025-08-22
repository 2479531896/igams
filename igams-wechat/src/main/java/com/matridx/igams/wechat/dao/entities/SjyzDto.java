package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="SjyzDto")
public class SjyzDto extends SjyzModel {
	//标本编号
	private String ybbh;
	//内部编码
	private String nbbm;
	//验证内部编码
	private String yznbbm;
	//验证默认内部编码
	private String yzmrnbbm;
	//患者姓名
	private String hzxm;
	//年龄
	private String nl;
	//标本类型名称
	private String yblxmc;
	//代表
	private String db;
	//用户id
	private String yhid;
	//验证类别名称
	private String yzlbmc;
	//录入人员名称
	private String lrrymc;
	//验证类别[多]
	private String[] yzlbs;
	//检测项目
	private String jcxm;
	//标本类型[多]
	private String[] yblxs;
	//附件IDS
	private List<String> fjids;
	//验证结果名称
	private String yzjgmc;
	//业务类型
	private String ywlx;
	//检测单位
	private String jcdw;
	//区分代码	
	private String qfdm;
	//区分名称	
	private String qfmc;
	//客户通知名称
	private String khtzmc;
	//录入时间格式化
	private String lrsj_format;
	//是否默认[基础数据]
	private String sfmr;
	//验证代码
	private String yzdm;
	//当前角色
	private String jsid;
	//导出字段[导出]
	private String sqlparam;
	//标本类型[导出]
	private String yblx_flg;
	//验证类别[导出]
	private String yzlb_flg;
	//区分[导出]
	private String qf_flg;
	//验证结果[导出]
	private String yzjg_flg;
	//客户通知[导出]
	private String khtz_flg;
	//录入人员[导出]
	private String lrry_flg;
	//是否通知
	private String sftz;
	//是否上传
	private String sfsc;
	//检测项目[多]
    private String[] jcxms;
	//模糊查询 [全部]
    private String all_param;
    //检测项目[多]
    private String[] yzjgs;
    //检测项目[多]
    private String[] khtzs;
    //检测项目[多]
    private String[] qfs;
    //判断是否是个人清单 single_flag=1 为个人清单  single_flag=2 为全部清单
    private String load_flag;
    //用于个人清单判断lrry [yhid,ddid,wxid]
    private List<String> userids;
    //用户钉钉 判断伙伴权限
    private List<String> sjhbs;
    //检测单位名称
    private String jcdwmc;
    //按钮显示标记
    private String btnFlg;
    //钉钉id
    private String Ddid;
    //检测项目名称
    private String jcxmmc;
    //检测单位限制
	private List<String> jcdwxz;
	//检测项目扩展参数3
	private String jcxmcskz3;
	//内部编码集合
	private String[] nbbms;
	//存储序号,96孔的序号
	private String[] xhs;	
	//序号
	private String xh;
	//机器号（PCR测序仪机器号）
	private String machineId;
	//验证结果具体内容
	private List<List<String>> yzjgnr;
	//验证状态
	private String yzzt;
	//验证状态多选
	private String[] yzzts;
	//结果保存标记
	private String xsbj;
	//检测结果
	private String jcjg;
	//疑似结果
	private String ysjg;
	//高级筛选 检测单位
	private String[] jcdws;
	//开始时间
	private  String start;
	//结束时间
	private String end;
	//输入路径
	private String srlj;
	//是否内部编码
	private String sfnbbm;
	//前端传回来
	private String[] sfnbbms;
	//审批流程序号
	private String[] xlcxhs;
	//审批岗位ID
	private List<String> gwids;
	//用户名
	private String yhm;
	private String sffhmc;
	private String qfcskz2;
	private String hbid;
	private String yzlbcskz2;

	public String getYzlbcskz2() {
		return yzlbcskz2;
	}

	public void setYzlbcskz2(String yzlbcskz2) {
		this.yzlbcskz2 = yzlbcskz2;
	}

	public String getHbid() {
		return hbid;
	}

	public void setHbid(String hbid) {
		this.hbid = hbid;
	}

	public String getQfcskz2() {
		return qfcskz2;
	}

	public void setQfcskz2(String qfcskz2) {
		this.qfcskz2 = qfcskz2;
	}

	public String getSffhmc() {
		return sffhmc;
	}

	public void setSffhmc(String sffhmc) {
		this.sffhmc = sffhmc;
	}

	public String getYhm() {
		return yhm;
	}

	public void setYhm(String yhm) {
		this.yhm = yhm;
	}

	public List<String> getGwids() {
		return gwids;
	}

	public void setGwids(List<String> gwids) {
		this.gwids = gwids;
	}

	public String getYhid() {
		return yhid;
	}

	public void setYhid(String yhid) {
		this.yhid = yhid;
	}

	public String[] getXlcxhs() {
		return xlcxhs;
	}

	public void setXlcxhs(String[] xlcxhs) {
		this.xlcxhs = xlcxhs;
		for (int i = 0; i < xlcxhs.length; i++){
			this.xlcxhs[i] = this.xlcxhs[i].replace("'", "");
		}
	}

	public String[] getYzzts() {
		return yzzts;
	}

	public void setYzzts(String[] yzzts) {
		this.yzzts = yzzts;
		for (int i = 0; i < yzzts.length; i++){
			this.yzzts[i] = this.yzzts[i].replace("'", "");
		}
	}

	public String[] getSfnbbms() {
		return sfnbbms;
	}

	public void setSfnbbms(String[] sfnbbms) {
		this.sfnbbms = sfnbbms;
	}

	public String getSfnbbm() {
		return sfnbbm;
	}

	public void setSfnbbm(String sfnbbm) {
		this.sfnbbm = sfnbbm;
	}

	public String getSrlj() {
		return srlj;
	}

	public void setSrlj(String srlj) {
		this.srlj = srlj;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String[] getJcdws() {
		return jcdws;
	}

	public void setJcdws(String[] jcdws) {
		this.jcdws = jcdws;
	}

	public String getJcjg() {
		return jcjg;
	}

	public void setJcjg(String jcjg) {
		this.jcjg = jcjg;
	}

	public String getYsjg() {
		return ysjg;
	}

	public void setYsjg(String ysjg) {
		this.ysjg = ysjg;
	}

	public String getXsbj() {
		return xsbj;
	}

	public void setXsbj(String jgbj) {
		this.xsbj = jgbj;
	}

	public String getYzzt() {
		return yzzt;
	}

	public void setYzzt(String yzzt) {
		this.yzzt = yzzt;
	}

	public String getMachineId() {
		return machineId;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public List<List<String>> getYzjgnr() {
		return yzjgnr;
	}

	public void setYzjgnr(List<List<String>> yzjgnr) {
		this.yzjgnr = yzjgnr;
	}

	public String[] getNbbms() {
		return nbbms;
	}

	public void setNbbms(String[] nbbms) {
		this.nbbms = nbbms;
	}

	public String[] getXhs() {
		return xhs;
	}

	public void setXhs(String[] xhs) {
		this.xhs = xhs;
	}

	public String getJcxmcskz3() {
		return jcxmcskz3;
	}

	public void setJcxmcskz3(String jcxmcskz3) {
		this.jcxmcskz3 = jcxmcskz3;
	}

	public String getSfsc() {
		return sfsc;
	}

	public void setSfsc(String sfsc) {
		this.sfsc = sfsc;
	}

	public String getSftz() {
		return sftz;
	}

	public void setSftz(String sftz) {
		this.sftz = sftz;
	}

	public String getYzjgmc() {
		return yzjgmc;
	}

	public void setYzjgmc(String yzjgmc) {
		this.yzjgmc = yzjgmc;
	}

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
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

	public String getNl() {
		return nl;
	}

	public void setNl(String nl) {
		this.nl = nl;
	}

	public String getYblxmc() {
		return yblxmc;
	}

	public void setYblxmc(String yblxmc) {
		this.yblxmc = yblxmc;
	}

	public String getDb() {
		return db;
	}

	public void setDb(String db) {
		this.db = db;
	}

	public String getYzlbmc() {
		return yzlbmc;
	}

	public void setYzlbmc(String yzlbmc) {
		this.yzlbmc = yzlbmc;
	}

	public String getLrrymc() {
		return lrrymc;
	}

	public void setLrrymc(String lrrymc) {
		this.lrrymc = lrrymc;
	}

	public String[] getYzlbs() {
		return yzlbs;
	}

	public void setYzlbs(String[] yzlbs) {
		this.yzlbs = yzlbs;
		for (int i = 0; i < yzlbs.length; i++){
			this.yzlbs[i] = this.yzlbs[i].replace("'", "");
		}
	}

	public String getJcxm() {
		return jcxm;
	}

	public void setJcxm(String jcxm) {
		this.jcxm = jcxm;
	}

	public String[] getYblxs() {
		return yblxs;
	}

	public void setYblxs(String[] yblxs) {
		this.yblxs = yblxs;
		for (int i = 0; i < yblxs.length; i++){
			this.yblxs[i] = this.yblxs[i].replace("'", "");
		}
	}
	
	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public String getJcdw() {
		return jcdw;
	}

	public void setJcdw(String jcdw) {
		this.jcdw = jcdw;
	}

	public String getQfmc() {
		return qfmc;
	}

	public void setQfmc(String qfmc) {
		this.qfmc = qfmc;
	}

	public String getKhtzmc() {
		return khtzmc;
	}

	public void setKhtzmc(String khtzmc) {
		this.khtzmc = khtzmc;
	}

	public String getLrsj_format() {
		return lrsj_format;
	}

	public void setLrsj_format(String lrsj_format) {
		this.lrsj_format = lrsj_format;
	}

	public String getSfmr() {
		return sfmr;
	}

	public void setSfmr(String sfmr) {
		this.sfmr = sfmr;
	}


	public String getQfdm() {
		return qfdm;
	}

	public void setQfdm(String qfdm) {
		this.qfdm = qfdm;
	}


	public String getYznbbm() {
		return yznbbm;
	}

	public void setYznbbm(String yznbbm) {
		this.yznbbm = yznbbm;
	}


	public String getYzmrnbbm() {
		return yzmrnbbm;
	}

	public void setYzmrnbbm(String yzmrnbbm) {
		this.yzmrnbbm = yzmrnbbm;
	}

	public String getYzdm() {
		return yzdm;
	}

	public void setYzdm(String yzdm) {
		this.yzdm = yzdm;
	}

	public String getJsid() {
		return jsid;
	}

	public void setJsid(String jsid) {
		this.jsid = jsid;
	}

	public String getSqlparam() {
		return sqlparam;
	}

	public void setSqlparam(String sqlparam) {
		this.sqlparam = sqlparam;
	}

	public String getYblx_flg() {
		return yblx_flg;
	}

	public void setYblx_flg(String yblx_flg) {
		this.yblx_flg = yblx_flg;
	}

	public String getYzlb_flg() {
		return yzlb_flg;
	}

	public void setYzlb_flg(String yzlb_flg) {
		this.yzlb_flg = yzlb_flg;
	}

	public String getQf_flg() {
		return qf_flg;
	}

	public void setQf_flg(String qf_flg) {
		this.qf_flg = qf_flg;
	}

	public String getYzjg_flg() {
		return yzjg_flg;
	}

	public void setYzjg_flg(String yzjg_flg) {
		this.yzjg_flg = yzjg_flg;
	}

	public String getKhtz_flg() {
		return khtz_flg;
	}

	public void setKhtz_flg(String khtz_flg) {
		this.khtz_flg = khtz_flg;
	}

	public String getLrry_flg() {
		return lrry_flg;
	}

	public void setLrry_flg(String lrry_flg) {
		this.lrry_flg = lrry_flg;
	}

	public String[] getJcxms() {
		return jcxms;
		
	}

	public void setJcxms(String[] jcxms) {
		this.jcxms = jcxms;
		for (int i = 0; i < jcxms.length; i++){
			this.jcxms[i]=this.jcxms[i].replace("'","");
		}
	}

	public String getAll_param() {
		return all_param;
	}

	public void setAll_param(String all_param) {
		this.all_param = all_param;
	}

	public String[] getYzjgs() {
		return yzjgs;
	}

	public void setYzjgs(String[] yzjgs) {
		this.yzjgs = yzjgs;
		for (int i = 0; i < yzjgs.length; i++){
			this.yzjgs[i]=this.yzjgs[i].replace("'","");
		}
	}

	public String[] getKhtzs() {
		return khtzs;
	}

	public void setKhtzs(String[] khtzs) {
		this.khtzs = khtzs;
		for (int i = 0; i < khtzs.length; i++){
			this.khtzs[i]=this.khtzs[i].replace("'","");
		}
	}

	public String[] getQfs() {
		return qfs;
	}

	public void setQfs(String[] qfs) {
		this.qfs = qfs;
		for (int i = 0; i < qfs.length; i++){
			this.qfs[i]=this.qfs[i].replace("'","");
		}
	}
	
	public String getLoad_flag() {
		return load_flag;
	}

	public void setLoad_flag(String load_flag) {
		this.load_flag = load_flag;
	}

	public List<String> getUserids() {
		return userids;
	}

	public void setUserids(List<String> userids) {
		this.userids = userids;
	}

	public List<String> getSjhbs() {
		return sjhbs;
	}

	public void setSjhbs(List<String> sjhbs) {
		this.sjhbs = sjhbs;
	}

	public String getJcdwmc() {
		return jcdwmc;
	}

	public void setJcdwmc(String jcdwmc) {
		this.jcdwmc = jcdwmc;
	}

	public String getBtnFlg() {
		return btnFlg;
	}

	public void setBtnFlg(String btnFlg) {
		this.btnFlg = btnFlg;
	}

	public String getDdid() {
		return Ddid;
	}

	public void setDdid(String ddid) {
		Ddid = ddid;
	}


	public String getJcxmmc() {
		return jcxmmc;
	}

	public void setJcxmmc(String jcxmmc) {
		this.jcxmmc = jcxmmc;
	}

	public List<String> getJcdwxz() {
		return jcdwxz;
	}

	public void setJcdwxz(List<String> jcdwxz) {
		this.jcdwxz = jcdwxz;
	}



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
