package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;


@Alias(value="SjsyglDto")
public class SjsyglDto extends SjsyglModel{

	private String yjcdw;
	private String xmsyglid;
	private String json;
	private String lxmc; //类型名称
	//检测项目ID复数
	private List<String> jcxmids;
	//角色检测单位限制
	private List<String> jcdwxz;
	//内部子编码
	private List<String> suffixs;
	private String kzcs1;
	//用于检索送检扩展参数1
	private String[] sjkzcs1;
	//用于检索送检扩展参数2
	private String[] sjkzcs2;
	//用于检索送检扩展参数3
	private String[] sjkzcs3;
	//用于检索送检扩展参数4
	private String[] sjkzcs4;
	//检测项目[多]
	private String[] jcxms;
	//检验结果
	private String[] jyjgs;
	//是否收费
	private String[] sfsfs;
	//是否正确
	private String sfzq;
	//用于检索快递类型
	private String[] kdlxs;
	//用于检索盖章类型
	private String[] gzlxs;
	//检测单位（多）
	private String[] jcdws;
	//科研项目类型（多）
	private String[] kylxs;
	//科研项目（多）
	private String[] kyxms;
	//送检区分[多]
	private String[] sjqfs;
	//医院重点等级（复数）
	private String[] yyzddjs;
	//实验日期开始
	private String syrqstart;
	//实验日期结束
	private String syrqend;
	//是否统计
	private String sftj;
	//是否接收
	private String sfjs;
	//是否自免检测  0：否  1：是
	private String sfzmjc;
	//其他检测标记
	private String qtjcbj;
	//DNA检测标记
	private String djcbj;
	//是否上传结果
	private String sfsc;
	//付款标记
	private String fkbj;
	//代表名
	private String dbm;
	//用于检索标本类型
	private String[] yblxs;
	//合作伙伴分类[多]
	private String[] sjhbfls;
	//合作伙伴子分类[多]
	private String[] sjhbzfls;
	//用于检索科室
	private String[] dwids;
	//付款日期开始时间
	private String fkrqstart;
	//付款日期结束时间
	private String fkrqend;
	//起运时间开始
	private String qyrqstart;
	//起运时间结束
	private String qyrqend;
	//运达时间开始
	private String ydrqstart;
	//运达时间结束
	private String ydrqend;
	//开始日期
	private String bgrqstart;
	//结束日期
	private String bgrqend;
	//开始日期
	private String jsrqstart;
	//结束日期
	private String jsrqend;
	//患者姓名
	private String hzxm;
	//内部编号
	private String nbbm;
	//检测项目名称
	private String jcxmmc;
	private String jcxm;
	//检测子项目名称
	private String jczxmmc;
	//检测类型名称
	private String jclxmc;
	//检测类型代码
	private String jclxdm;
	//检测单位名称
	private String jcdwmc;
	//接收人员名称
	private String jsrymc;
	//录入人员名称
	private String lrrymc;
	//性别
	private String xb;
	//年龄
	private String nl;
	//代表  关联微信用户
	private String db;
	//医院名称(送检单位名称)
	private String hospitalname;
	//参数扩展五
	private String cskz5;
	//住院号
	private String zyh;
	//标本编号
	private String ybbh;
	//销售人员
	private String xsrymc;
	//临床反馈
	private String lcfk;
	//快递单号
	private String kdh;
	//送检医生  暂时不关联
	private String sjys;
	//扩展参数2
	private String kzcs2;
	//扩展参数3
	private String kzcs3;
	//打印参数
	private String print_flg;
	private String print_num;
	//科研项目
	private String kyxm;
	//检测结果(json存病原体列表的pathpgen的中文名称)
	private String jcjg;
	//科室
	private String ks;
	//样本类型名称
	private String yblxmc;
	//伙伴ID
	private String hbid;
	//扩展参数4
	private String kzcs4;
	//复检ID
	private String fjid;
	//样本类型代码
	private String yblxdm;
	//扩展参数5
	private String kzcs5;
	//扩展参数6
	private String kzcs6;
	//原信息
	private String yxx;
	//json
	private String bbsy_json;
	//取样人员
	private String qyrymc;
	//项目限制
	private String xmlimit;
	//检测项目参数扩展1
	private String jcxmcskz1;
	private String jclxcskz1;
	private String wkrq;
	private String rsyrq;
	private String dsyrq;
	private String qtsyrq;
	private String sfqy;
	private String sy_json;
	private String fjzt;
	private String wkid;
	private String dydm;
	private String jth;
	//排序
	private String px;
	//业务类型
	private String ywlx;

	private String qyryyhm;

	private String jsryyhm;

	//精确时间（YYYY-MM-DD HH24:mi:ss）
	private String jqjsrq;
	private String jqsyrq;
	private String jqqysj;
	private String jqjcsj;
	private String jqsjsj;
	private String jqxjsj;
	//文库代码
	private String wkdm;

	//判断是否是个人清单 single_flag=1 为个人清单  single_flag=0 or single_flag=null 为全部清单
	private String single_flag;
	//用于个人清单判断lrry [yhid,ddid,wxid]
	private List<String> userids;
	//合作伙伴集合
	private List<String> sjhbs;

	private String jcxmcskz3;

	private String xzlx;

	public String getJcxmcskz3() {
		return jcxmcskz3;
	}

	public void setJcxmcskz3(String jcxmcskz3) {
		this.jcxmcskz3 = jcxmcskz3;
	}

	public String getXzlx() {
		return xzlx;
	}

	public void setXzlx(String xzlx) {
		this.xzlx = xzlx;
	}

	public String getSingle_flag() {
		return single_flag;
	}

	public void setSingle_flag(String single_flag) {
		this.single_flag = single_flag;
	}

	public List<String> getUserids() {
		return userids;
	}

	public void setUserids(List<String> userids) {
		this.userids = userids;
	}
	public List<String> getSjhbs()
	{
		return sjhbs;
	}
	public void setSjhbs(List<String> sjhbs)
	{
		this.sjhbs = sjhbs;
	}
	public String getJqxjsj() {
		return jqxjsj;
	}

	public void setJqxjsj(String jqxjsj) {
		this.jqxjsj = jqxjsj;
	}

	private List<SjjcxmDto> sjjcxmDtos;

	public List<SjjcxmDto> getSjjcxmDtos() {
		return sjjcxmDtos;
	}

	public void setSjjcxmDtos(List<SjjcxmDto> sjjcxmDtos) {
		this.sjjcxmDtos = sjjcxmDtos;
	}

	public String getJqjsrq() {
		return jqjsrq;
	}

	public void setJqjsrq(String jqjsrq) {
		this.jqjsrq = jqjsrq;
	}

	public String getJqsyrq() {
		return jqsyrq;
	}

	public void setJqsyrq(String jqsyrq) {
		this.jqsyrq = jqsyrq;
	}

	public String getJqqysj() {
		return jqqysj;
	}

	public void setJqqysj(String jqqysj) {
		this.jqqysj = jqqysj;
	}

	public String getJqjcsj() {
		return jqjcsj;
	}

	public void setJqjcsj(String jqjcsj) {
		this.jqjcsj = jqjcsj;
	}

	public String getJqsjsj() {
		return jqsjsj;
	}

	public void setJqsjsj(String jqsjsj) {
		this.jqsjsj = jqsjsj;
	}

	public String getQyryyhm() {
		return qyryyhm;
	}

	public void setQyryyhm(String qyryyhm) {
		this.qyryyhm = qyryyhm;
	}

	public String getJsryyhm() {
		return jsryyhm;
	}

	public void setJsryyhm(String jsryyhm) {
		this.jsryyhm = jsryyhm;
	}

	public String getPx() {
		return px;
	}

	public void setPx(String px) {
		this.px = px;
	}

	public String getWkid() {
		return wkid;
	}

	public void setWkid(String wkid) {
		this.wkid = wkid;
	}

	public String getDydm() {
		return dydm;
	}

	public void setDydm(String dydm) {
		this.dydm = dydm;
	}

	public String getJth() {
		return jth;
	}

	public void setJth(String jth) {
		this.jth = jth;
	}

	public String getFjzt() {
		return fjzt;
	}

	public void setFjzt(String fjzt) {
		this.fjzt = fjzt;
	}

	public String getSy_json() {
		return sy_json;
	}

	public void setSy_json(String sy_json) {
		this.sy_json = sy_json;
	}

	public String getSfqy() {
		return sfqy;
	}

	public void setSfqy(String sfqy) {
		this.sfqy = sfqy;
	}

	public String getJclxcskz1() {
		return jclxcskz1;
	}

	public void setJclxcskz1(String jclxcskz1) {
		this.jclxcskz1 = jclxcskz1;
	}

	public String getRsyrq() {
		return rsyrq;
	}

	public void setRsyrq(String rsyrq) {
		this.rsyrq = rsyrq;
	}

	public String getDsyrq() {
		return dsyrq;
	}

	public void setDsyrq(String dsyrq) {
		this.dsyrq = dsyrq;
	}

	public String getQtsyrq() {
		return qtsyrq;
	}

	public void setQtsyrq(String qtsyrq) {
		this.qtsyrq = qtsyrq;
	}

	public String getJcxmcskz1() {
		return jcxmcskz1;
	}

	public void setJcxmcskz1(String jcxmcskz1) {
		this.jcxmcskz1 = jcxmcskz1;
	}

	public String getXmlimit() {
		return xmlimit;
	}

	public void setXmlimit(String xmlimit) {
		this.xmlimit = xmlimit;
	}

	public String getQyrymc() {
		return qyrymc;
	}

	public void setQyrymc(String qyrymc) {
		this.qyrymc = qyrymc;
	}

	public String getJcxm() {
		return jcxm;
	}

	public void setJcxm(String jcxm) {
		this.jcxm = jcxm;
	}

	public String getLxmc() {
		return lxmc;
	}

	public void setLxmc(String lxmc) {
		this.lxmc = lxmc;
	}

	public String getBbsy_json() {
		return bbsy_json;
	}

	public void setBbsy_json(String bbsy_json) {
		this.bbsy_json = bbsy_json;
	}

	public String getYxx() {
		return yxx;
	}

	public void setYxx(String yxx) {
		this.yxx = yxx;
	}

	public String getKzcs6() {
		return kzcs6;
	}

	public void setKzcs6(String kzcs6) {
		this.kzcs6 = kzcs6;
	}

	public String getKzcs5() {
		return kzcs5;
	}

	public void setKzcs5(String kzcs5) {
		this.kzcs5 = kzcs5;
	}

	public String getYblxdm() {
		return yblxdm;
	}

	public void setYblxdm(String yblxdm) {
		this.yblxdm = yblxdm;
	}

	public String getFjid() {
		return fjid;
	}

	public void setFjid(String fjid) {
		this.fjid = fjid;
	}

	public String getKzcs4() {
		return kzcs4;
	}

	public void setKzcs4(String kzcs4) {
		this.kzcs4 = kzcs4;
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

	public String getPrint_flg() {
		return print_flg;
	}

	public void setPrint_flg(String print_flg) {
		this.print_flg = print_flg;
	}


	public String getPrint_num() {
		return print_num;
	}

	public void setPrint_num(String print_num) {
		this.print_num = print_num;
	}

	public String getHbid() {
		return hbid;
	}

	public void setHbid(String hbid) {
		this.hbid = hbid;
	}

	public String getYblxmc() {
		return yblxmc;
	}

	public void setYblxmc(String yblxmc) {
		this.yblxmc = yblxmc;
	}

	public String getKs() {
		return ks;
	}

	public void setKs(String ks) {
		this.ks = ks;
	}

	public List<String> getSuffixs() {
		return suffixs;
	}

	public void setSuffixs(List<String> suffixs) {
		this.suffixs = suffixs;
	}

	public String getKzcs1() {
		return kzcs1;
	}

	public void setKzcs1(String kzcs1) {
		this.kzcs1 = kzcs1;
	}

	public String getDb() {
		return db;
	}

	public void setDb(String db) {
		this.db = db;
	}

	public String getHospitalname() {
		return hospitalname;
	}

	public void setHospitalname(String hospitalname) {
		this.hospitalname = hospitalname;
	}

	public String getCskz5() {
		return cskz5;
	}

	public void setCskz5(String cskz5) {
		this.cskz5 = cskz5;
	}

	public String getZyh() {
		return zyh;
	}

	public void setZyh(String zyh) {
		this.zyh = zyh;
	}

	public String getYbbh() {
		return ybbh;
	}

	public void setYbbh(String ybbh) {
		this.ybbh = ybbh;
	}

	public String getXsrymc() {
		return xsrymc;
	}

	public void setXsrymc(String xsrymc) {
		this.xsrymc = xsrymc;
	}

	public String getLcfk() {
		return lcfk;
	}

	public void setLcfk(String lcfk) {
		this.lcfk = lcfk;
	}

	public String getKdh() {
		return kdh;
	}

	public void setKdh(String kdh) {
		this.kdh = kdh;
	}

	public String getSjys() {
		return sjys;
	}

	public void setSjys(String sjys) {
		this.sjys = sjys;
	}

	public String getKyxm() {
		return kyxm;
	}

	public void setKyxm(String kyxm) {
		this.kyxm = kyxm;
	}

	public String getJcjg() {
		return jcjg;
	}

	public void setJcjg(String jcjg) {
		this.jcjg = jcjg;
	}

	public String getXb() {
		return xb;
	}

	public void setXb(String xb) {
		this.xb = xb;
	}

	public String getNl() {
		return nl;
	}

	public void setNl(String nl) {
		this.nl = nl;
	}

	public String getHzxm() {
		return hzxm;
	}

	public void setHzxm(String hzxm) {
		this.hzxm = hzxm;
	}

	public String getNbbm() {
		return nbbm;
	}

	public void setNbbm(String nbbm) {
		this.nbbm = nbbm;
	}

	public String getJcxmmc() {
		return jcxmmc;
	}

	public void setJcxmmc(String jcxmmc) {
		this.jcxmmc = jcxmmc;
	}

	public String getJczxmmc() {
		return jczxmmc;
	}

	public void setJczxmmc(String jczxmmc) {
		this.jczxmmc = jczxmmc;
	}

	public String getJclxmc() {
		return jclxmc;
	}

	public void setJclxmc(String jclxmc) {
		this.jclxmc = jclxmc;
	}

	public String getJcdwmc() {
		return jcdwmc;
	}

	public void setJcdwmc(String jcdwmc) {
		this.jcdwmc = jcdwmc;
	}

	public String getJsrymc() {
		return jsrymc;
	}

	public void setJsrymc(String jsrymc) {
		this.jsrymc = jsrymc;
	}

	public String getXmsyglid() {
		return xmsyglid;
	}

	public void setXmsyglid(String xmsyglid) {
		this.xmsyglid = xmsyglid;
	}

	public String getLrrymc() {
		return lrrymc;
	}

	public void setLrrymc(String lrrymc) {
		this.lrrymc = lrrymc;
	}

	public String getJsrqstart() {
		return jsrqstart;
	}

	public void setJsrqstart(String jsrqstart) {
		this.jsrqstart = jsrqstart;
	}

	public String getJsrqend() {
		return jsrqend;
	}

	public void setJsrqend(String jsrqend) {
		this.jsrqend = jsrqend;
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

	public String getQyrqstart() {
		return qyrqstart;
	}

	public void setQyrqstart(String qyrqstart) {
		this.qyrqstart = qyrqstart;
	}

	public String getQyrqend() {
		return qyrqend;
	}

	public void setQyrqend(String qyrqend) {
		this.qyrqend = qyrqend;
	}

	public String getYdrqstart() {
		return ydrqstart;
	}

	public void setYdrqstart(String ydrqstart) {
		this.ydrqstart = ydrqstart;
	}

	public String getYdrqend() {
		return ydrqend;
	}

	public void setYdrqend(String ydrqend) {
		this.ydrqend = ydrqend;
	}

	public String getFkrqstart() {
		return fkrqstart;
	}

	public void setFkrqstart(String fkrqstart) {
		this.fkrqstart = fkrqstart;
	}

	public String getFkrqend() {
		return fkrqend;
	}

	public void setFkrqend(String fkrqend) {
		this.fkrqend = fkrqend;
	}

	public void setDwids(String[] dwids) {
		this.dwids=dwids;
		for (int i = 0; i < dwids.length; i++){
			this.dwids[i]=this.dwids[i].replace("'","");
		}
	}

	public String[] getDwids() {
		return dwids;
	}

	public String[] getSjhbfls() {
		return sjhbfls;
	}

	public void setSjhbfls(String[] sjhbfls) {
		this.sjhbfls = sjhbfls;
		for (int i = 0; i < sjhbfls.length; i++){
			this.sjhbfls[i]=this.sjhbfls[i].replace("'","");
		}
	}

	public String[] getSjhbzfls() {
		return sjhbzfls;
	}

	public void setSjhbzfls(String[] sjhbzfls) {
		this.sjhbzfls = sjhbzfls;
		for (int i = 0; i < sjhbzfls.length; i++){
			this.sjhbzfls[i]=this.sjhbzfls[i].replace("'","");
		}
	}
	public String[] getYblxs() {
		return yblxs;
	}

	public void setYblxs(String[] yblxs){
		this.yblxs = yblxs;
		for (int i = 0; i < yblxs.length; i++){
			this.yblxs[i] = this.yblxs[i].replace("'", "");
		}
	}
	public String getDbm() {
		return dbm;
	}

	public void setDbm(String dbm) {
		this.dbm = dbm;
	}
	public String getFkbj() {
		return fkbj;
	}

	public void setFkbj(String fkbj) {
		this.fkbj = fkbj;
	}

	public String getSfsc() {
		return sfsc;
	}

	public void setSfsc(String sfsc) {
		this.sfsc = sfsc;
	}

	public String getDjcbj() {
		return djcbj;
	}
	public void setDjcbj(String djcbj) {
		this.djcbj = djcbj;
	}
	public String getQtjcbj() {
		return qtjcbj;
	}
	public void setQtjcbj(String qtjcbj) {
		this.qtjcbj = qtjcbj;
	}
	public String getSfzmjc() {
		return sfzmjc;
	}
	public void setSfzmjc(String sfzmjc) {
		this.sfzmjc = sfzmjc;
	}
	public String getSfjs()
	{
		return sfjs;
	}
	public void setSfjs(String sfjs)
	{
		this.sfjs = sfjs;
	}
	public String getSftj()
	{
		return sftj;
	}
	public void setSftj(String sftj)
	{
		this.sftj = sftj;
	}
	public String getSyrqstart() {
		return syrqstart;
	}

	public void setSyrqstart(String syrqstart) {
		this.syrqstart = syrqstart;
	}

	public String getSyrqend() {
		return syrqend;
	}

	public void setSyrqend(String syrqend) {
		this.syrqend = syrqend;
	}
	public String[] getYyzddjs() {
		return yyzddjs;
	}

	public void setYyzddjs(String[] yyzddjs) {
		this.yyzddjs = yyzddjs;
		for (int i = 0; i <yyzddjs.length; i++){
			this.yyzddjs[i]=this.yyzddjs[i].replace("'","");
		}
	}
	public String[] getSjqfs() {
		return sjqfs;
	}

	public void setSjqfs(String[] sjqfs) {
		this.sjqfs = sjqfs;
		for (int i = 0; i < sjqfs.length; i++){
			this.sjqfs[i]=this.sjqfs[i].replace("'","");
		}
	}
	public String[] getKyxms()
	{
		return kyxms;
	}
	public void setKyxms(String[] kyxms)
	{
		this.kyxms = kyxms;
		for (int i = 0; i <kyxms.length; i++){
			this.kyxms[i]=this.kyxms[i].replace("'","");
		}
	}
	public String[] getKylxs()
	{
		return kylxs;
	}
	public void setKylxs(String[] kylxs)
	{
		this.kylxs = kylxs;
		for (int i = 0; i <kylxs.length; i++){
			this.kylxs[i]=this.kylxs[i].replace("'","");
		}
	}
	public String[] getJcdws()
	{
		return jcdws;
	}

	public void setJcdws(String[] jcdws)
	{
		this.jcdws = jcdws;
		for (int i = 0; i <jcdws.length; i++){
			this.jcdws[i]=this.jcdws[i].replace("'","");
		}
	}
	public String[] getKdlxs()
	{
		return kdlxs;
	}

	public void setKdlxs(String[] kdlxs)
	{
		this.kdlxs = kdlxs;
		for (int i = 0; i < kdlxs.length; i++){
			this.kdlxs[i]=this.kdlxs[i].replace("'","");
		}
	}

	public String[] getGzlxs() {
		return gzlxs;
	}

	public void setGzlxs(String[] gzlxs) {
		this.gzlxs = gzlxs;
		for (int i = 0; i < gzlxs.length; i++){
			this.gzlxs[i]=this.gzlxs[i].replace("'","");
		}
	}
	public String getSfzq()
	{
		return sfzq;
	}
	public void setSfzq(String sfzq)
	{
		this.sfzq = sfzq;
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
	public String[] getJyjgs()
	{
		return jyjgs;
	}
	public void setJyjgs(String[] jyjgs)
	{
		this.jyjgs = jyjgs;
		for (int i = 0; i < jyjgs.length; i++){
			this.jyjgs[i]=this.jyjgs[i].replace("'","");
		}
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
	public String[] getSjkzcs1()
	{
		return sjkzcs1;
	}
	public void setSjkzcs1(String[] sjkzcs1)
	{
		this.sjkzcs1 = sjkzcs1;
		for (int i = 0; i < sjkzcs1.length; i++){
			this.sjkzcs1[i]=this.sjkzcs1[i].replace("'","");
		}
	}
	public String[] getSjkzcs2()
	{
		return sjkzcs2;
	}
	public void setSjkzcs2(String[] sjkzcs2)
	{
		this.sjkzcs2 = sjkzcs2;
		for (int i = 0; i < sjkzcs2.length; i++){
			this.sjkzcs2[i]=this.sjkzcs2[i].replace("'","");
		}
	}
	public String[] getSjkzcs3()
	{
		return sjkzcs3;

	}
	public void setSjkzcs3(String[] sjkzcs3)
	{
		this.sjkzcs3 = sjkzcs3;
		for (int i = 0; i < sjkzcs3.length; i++){
			this.sjkzcs3[i]=this.sjkzcs3[i].replace("'","");
		}
	}
	public String[] getSjkzcs4()
	{
		return sjkzcs4;
	}
	public void setSjkzcs4(String[] sjkzcs4)
	{
		this.sjkzcs4 = sjkzcs4;
		for (int i = 0; i < sjkzcs4.length; i++){
			this.sjkzcs4[i]=this.sjkzcs4[i].replace("'","");
		}
	}
	public List<String> getJcdwxz() {
		return jcdwxz;
	}

	public void setJcdwxz(List<String> jcdwxz) {
		this.jcdwxz = jcdwxz;
	}

	public List<String> getJcxmids() {
		return jcxmids;
	}

	public void setJcxmids(List<String> jcxmids) {
		this.jcxmids = jcxmids;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getYjcdw() {
		return yjcdw;
	}

	public void setYjcdw(String yjcdw) {
		this.yjcdw = yjcdw;
	}

	public String getJclxdm() {
		return jclxdm;
	}

	public void setJclxdm(String jclxdm) {
		this.jclxdm = jclxdm;
	}

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public String getWkdm() {
		return wkdm;
	}

	public void setWkdm(String wkdm) {
		this.wkdm = wkdm;
	}

	public String getWkrq() {
		return wkrq;
	}

	public void setWkrq(String wkrq) {
		this.wkrq = wkrq;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

}
