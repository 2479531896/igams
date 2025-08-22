package com.matridx.igams.experiment.dao.entities;


import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="WkmxDto")
public class WkmxDto extends WkmxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//接头信息集合
	private String[] jtxxs;
	//内部编号集合(有后缀)
	private String[] nbbhs;
	//送检内部编码集合(无后缀)
	private String[] nbbms;
	//序号集合
	private String[] xhs;
	//送检信息list
	private String[] sjids;
	//ids
	private List<String> ids;
	//保存状态
	private String zt;
	//导出关联标记位
	//所选择的字段
	private String SqlParam;
	//接头信息flg
	private String jtxx_flg;
	//内部编号flg
	private String nbbh_flg;
	//标本编号flg
	private String ybbh_flg;
	//标本编号（导出用）
	private String ybbh;
	//library（导出用）
	private String library;
	//接头（index2导出用）
	private String jtsecond;
	//project(导出用)
	private String project;
	//comment(导出用)
	private String comment;
	//concentration(导出用)
	private String concentration;
	//inner_index
	private String inner_index;
	//添加接头按钮ID
	private String lieid;
	//是否为单个接头编辑
	private String djtbj;
	//状态flag   0为保存状态为00，1为确定状态为80
	private String zt_flag;
	//是否继续保存  1代表是，0代表否
	private String sfbc;
	//数量(浓度)
	private String[] quantitys;
		//polling导出文库名称编码
	private String wkmc;
	//检测单位
	private String jcdw;
	//检测单位名称
	private String jcdwmc;
	//核酸浓度
	private String hsnd;
	//检测项目ID
	private String jcxmid;
	//检测项目名称
	private String jcxmmc;
	//cskz1(检测项目的cskz1)
	private String cskz1;
	//患者姓名
	private String hzxm;
	//合作伙伴
	private String db;
	//标本类型名称
	private String yblxmc;
	//PCR选择的仪器ID
	private String machineId;
	//输入路径
	private String srlj;
	//提取码(复数)
	private String[] tqms;
	//实验管理数据Json
	private String sygl_json;
	//实验管理ID
	private String syglid;
	//实验管理ID(复数)
	private String[] syglids;
	//文库上机id
	private String wksjid;
	//kzcs7
	private String kzcs7;
	private String spike;
	private String[] jcdws;
	private String wkmx_json;
	private String kzcs1;
	private String kzcs2;
	private String kzcs3;
	private String nbzbm;
	private String fjid;
	private String dsyrq;
	private String rsyrq;
	private String qtsyrq;
	private String syrq;
	private String jcxmcskz1;
	private String lx;
	//文库生信编码
	private String wksxbm;
	//提取明细ID(复数)
	private String[] tqmxids;
	private List<TqmxDto> tqmxDtos;
	//仪器类型
	private String yqlx;
	//试剂选择
	private String sjxz;

	//定量试剂
	private String sjph3;

	private String update_syrq_flag;

	private String tj;

	private String hbnd;

	private String cxyid;

	private String wksj;
	private String sysjStr;

	public String getSysjStr() {
		return sysjStr;
	}

	public void setSysjStr(String sysjStr) {
		this.sysjStr = sysjStr;
	}

	public String getCxyid() {
		return cxyid;
	}

	public void setCxyid(String cxyid) {
		this.cxyid = cxyid;
	}

	public String getUpdate_syrq_flag() {
		return update_syrq_flag;
	}

	public void setUpdate_syrq_flag(String update_syrq_flag) {
		this.update_syrq_flag = update_syrq_flag;
	}

	public String getSjph3() {
		return sjph3;
	}

	public void setSjph3(String sjph3) {
		this.sjph3 = sjph3;
	}

	public String getSjxz() {
		return sjxz;
	}

	public void setSjxz(String sjxz) {
		this.sjxz = sjxz;
	}

	public String getYqlx() {
		return yqlx;
	}

	public void setYqlx(String yqlx) {
		this.yqlx = yqlx;
	}

	//传递给生信用
	private String project_type;

	public String getProject_type() {
		return project_type;
	}

	public void setProject_type(String project_type) {
		this.project_type = project_type;
	}
	public String getJcdwmc() {
		return jcdwmc;
	}

	public void setJcdwmc(String jcdwmc) {
		this.jcdwmc = jcdwmc;
	}

	private String zdpb;//自动排版个人设置用

	private String zdpbin;//自动排版包含个人设置用

	private String zdpbnotin;//自动排版不包含个人设置用

	public String[] getTqmxids() {
		return tqmxids;
	}

	public void setTqmxids(String[] tqmxids) {
		this.tqmxids = tqmxids;
	}

	public List<TqmxDto> getTqmxDtos() {
		return tqmxDtos;
	}

	public void setTqmxDtos(List<TqmxDto> tqmxDtos) {
		this.tqmxDtos = tqmxDtos;
	}

	public String getWksxbm() {
		return wksxbm;
	}

	public void setWksxbm(String wksxbm) {
		this.wksxbm = wksxbm;
	}

	public String getLx() {
		return lx;
	}

	public void setLx(String lx) {
		this.lx = lx;
	}

	public String getJcxmcskz1() {
		return jcxmcskz1;
	}

	public void setJcxmcskz1(String jcxmcskz1) {
		this.jcxmcskz1 = jcxmcskz1;
	}

	public String getSyrq() {
		return syrq;
	}

	public void setSyrq(String syrq) {
		this.syrq = syrq;
	}

	public String getFjid() {
		return fjid;
	}

	public void setFjid(String fjid) {
		this.fjid = fjid;
	}

	public String getDsyrq() {
		return dsyrq;
	}

	public void setDsyrq(String dsyrq) {
		this.dsyrq = dsyrq;
	}

	public String getRsyrq() {
		return rsyrq;
	}

	public void setRsyrq(String rsyrq) {
		this.rsyrq = rsyrq;
	}

	public String getQtsyrq() {
		return qtsyrq;
	}

	public void setQtsyrq(String qtsyrq) {
		this.qtsyrq = qtsyrq;
	}

	public String getKzcs1() {
		return kzcs1;
	}

	public void setKzcs1(String kzcs1) {
		this.kzcs1 = kzcs1;
	}

	public String getKzcs2() {
		return kzcs2;
	}

	public void setKzcs2(String kzcs2) {
		this.kzcs2 = kzcs2;
	}

	public String getKzcs3() {
		return kzcs3;
	}

	public void setKzcs3(String kzcs3) {
		this.kzcs3 = kzcs3;
	}

	public String getNbzbm() {
		return nbzbm;
	}

	public void setNbzbm(String nbzbm) {
		this.nbzbm = nbzbm;
	}

	public String getWkmx_json() {
		return wkmx_json;
	}

	public void setWkmx_json(String wkmx_json) {
		this.wkmx_json = wkmx_json;
	}

	public String[] getJcdws() {
		return jcdws;
	}

	public void setJcdws(String[] jcdws) {
		this.jcdws = jcdws;
	}
	public String getSpike() {
		return spike;
	}

	public void setSpike(String spike) {
		this.spike = spike;
	}

	public String getKzcs7() {
		return kzcs7;
	}

	public void setKzcs7(String kzcs7) {
		this.kzcs7 = kzcs7;
	}

	public String getWksjid() {
		return wksjid;
	}

	public void setWksjid(String wksjid) {
		this.wksjid = wksjid;
	}

	public String[] getSyglids() {
		return syglids;
	}

	public void setSyglids(String[] syglids) {
		this.syglids = syglids;
	}

	public String getSyglid() {
		return syglid;
	}

	public void setSyglid(String syglid) {
		this.syglid = syglid;
	}

	public String getSygl_json() {
		return sygl_json;
	}

	public void setSygl_json(String sygl_json) {
		this.sygl_json = sygl_json;
	}

	public String[] getTqms() {
		return tqms;
	}

	public void setTqms(String[] tqms) {
		this.tqms = tqms;
	}

	public String getSrlj() {
		return srlj;
	}

	public void setSrlj(String srlj) {
		this.srlj = srlj;
	}

	public String getMachineId() {
		return machineId;
	}
	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}	
	public String getYblxmc() {
		return yblxmc;
	}
	public void setYblxmc(String yblxmc) {
		this.yblxmc = yblxmc;
	}
	public String getJcxmid() {
		return jcxmid;
	}
	public void setJcxmid(String jcxmid) {
		this.jcxmid = jcxmid;
	}
	public String getJcxmmc() {
		return jcxmmc;
	}
	public void setJcxmmc(String jcxmmc) {
		this.jcxmmc = jcxmmc;
	}
	public String getCskz1() {
		return cskz1;
	}
	public void setCskz1(String cskz1) {
		this.cskz1 = cskz1;
	}
	public String getInner_index() {
		return inner_index;
	}
	public void setInner_index(String inner_index) {
		this.inner_index = inner_index;
	}
	public String getHsnd() {
		return hsnd;
	}
	public void setHsnd(String hsnd) {
		this.hsnd = hsnd;
	}
	public String getJcdw() {
		return jcdw;
	}
	public void setJcdw(String jcdw) {
		this.jcdw = jcdw;
	}
		public String getWkmc()
	{
		return wkmc;
	}
	public void setWkmc(String wkmc)
	{
		this.wkmc = wkmc;
	}
	public String[] getQuantitys() {
		return quantitys;
	}
	public void setQuantitys(String[] quantitys) {
		this.quantitys = quantitys;
	}
	public String getSfbc() {
		return sfbc;
	}
	public void setSfbc(String sfbc) {
		this.sfbc = sfbc;
	}	
	public String[] getNbbms() {
		return nbbms;
	}
	public void setNbbms(String[] nbbms) {
		this.nbbms = nbbms;
	}
	public String getZt_flag() {
		return zt_flag;
	}
	public void setZt_flag(String zt_flag) {
		this.zt_flag = zt_flag;
	}
	public String getZt() {
		return zt;
	}
	public void setZt(String zt) {
		this.zt = zt;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getConcentration() {
		return concentration;
	}
	public void setConcentration(String concentration) {
		this.concentration = concentration;
	}
	public String getDjtbj() {
		return djtbj;
	}
	public void setDjtbj(String djtbj) {
		this.djtbj = djtbj;
	}
	public String getLieid() {
		return lieid;
	}
	public void setLieid(String lieid) {
		this.lieid = lieid;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getJtsecond() {
		return jtsecond;
	}
	public void setJtsecond(String jtsecond) {
		this.jtsecond = jtsecond;
	}
	public String getLibrary() {
		return library;
	}
	public void setLibrary(String library) {
		this.library = library;
	}
	public String getYbbh() {
		return ybbh;
	}
	public void setYbbh(String ybbh) {
		this.ybbh = ybbh;
	}
	public String getYbbh_flg() {
		return ybbh_flg;
	}
	public void setYbbh_flg(String ybbh_flg) {
		this.ybbh_flg = ybbh_flg;
	}
	public String getJtxx_flg() {
		return jtxx_flg;
	}
	public void setJtxx_flg(String jtxx_flg) {
		this.jtxx_flg = jtxx_flg;
	}
	public String getNbbh_flg() {
		return nbbh_flg;
	}
	public void setNbbh_flg(String nbbh_flg) {
		this.nbbh_flg = nbbh_flg;
	}
	public String getSqlParam() {
		return SqlParam;
	}
	public void setSqlParam(String sqlParam) {
		SqlParam = sqlParam;
	}
	public List<String> getIds() {
		return ids;
	}
	public void setIds(List<String> ids) {
		this.ids = ids;
	}
	public String[] getSjids() {
		return sjids;
	}
	public void setSjids(String[] sjids) {
		this.sjids = sjids;
	}
	public String[] getJtxxs() {
		return jtxxs;
	}
	public void setJtxxs(String[] jtxxs) {
		this.jtxxs = jtxxs;
	}
	public String[] getNbbhs() {
		return nbbhs;
	}
	public void setNbbhs(String[] nbbhs) {
		this.nbbhs = nbbhs;
	}
	public String[] getXhs() {
		return xhs;
	}
	public void setXhs(String[] xhs) {
		this.xhs = xhs;
	}
	public String getHzxm() {
		return hzxm;
	}
	public void setHzxm(String hzxm) {
		this.hzxm = hzxm;
	}
	public String getDb() {
		return db;
	}
	public void setDb(String db) {
		this.db = db;
	}

	public String getZdpb() {
		return zdpb;
	}

	public void setZdpb(String zdpb) {
		this.zdpb = zdpb;
	}

	public String getZdpbin() {
		return zdpbin;
	}

	public void setZdpbin(String zdpbin) {
		this.zdpbin = zdpbin;
	}

	public String getZdpbnotin() {
		return zdpbnotin;
	}

	public void setZdpbnotin(String zdpbnotin) {
		this.zdpbnotin = zdpbnotin;
	}

	public String getTj() {
		return tj;
	}

	public void setTj(String tj) {
		this.tj = tj;
	}

	public String getHbnd() {
		return hbnd;
	}

	public void setHbnd(String hbnd) {
		this.hbnd = hbnd;
	}

	public String getWksj() {
		return wksj;
	}

	public void setWksj(String wksj) {
		this.wksj = wksj;
	}
}
