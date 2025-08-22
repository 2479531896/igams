package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.entities.BaseBasicModel;

@Alias(value="SjxxModel")
public class SjxxModel extends BaseBasicModel{
	//送检ID
	private String sjid;
	//患者姓名
	private String hzxm;
	//性别
	private String xb;
	//年龄
	private String nl;
	//电话
	private String dh;
	//送检单位
	private String sjdw;
	//科室
	private String ks;
	//送检医生  暂时不关联
	private String sjys;
	//医生电话
	private String ysdh;
	//代表  关联微信用户
	private String db;
	//标本类型  关联基础数据
	private String yblx;
	//标本类型名称 标本类型为其他的时候填写
	private String yblxmc;
	//标本体积 任意填写
	private String ybtj;
	//住院号
	private String zyh;
	//标本编号
	private String ybbh;
	//采样日期
	private String cyrq;
	//接收日期
	private String jsrq;
	//接收人员
	private String jsry;
	//报告日期
	private String bgrq;
	//临床症状
	private String lczz;
	//前期诊断
	private String qqzd;
	//近期用药
	private String jqyy;
	//前期检测
	private String qqjc;
	//检验结果  0：未检验 1：已检验  2：检验通过 3：检验未通过
	private String jyjg;
	//快递类型
	private String kdlx;
	//检测时间
	private String jcsj;
    //快递单号
    private String kdh;
    //快递费用
    private String kdfy;
	//备注
	private String bz;
	//状态
	private String zt;
	//付款标记
	private String fkbj;
	//付款金额
	private String fkje;
	//实付金额
	private String sfje;
	//内部编号
	private String nbbm;
	//订单号
	private String ddh;
	//年龄单位
	private String nldw;
	//其他科室
	private String qtks;
	//检测标记
	private String jcbj;
	//检测标记名称
	private String jcbjmc;
	//是否收费
	private String sfsf;
	//接头
	private String jt;
	//实验日期
	private String syrq;
	//DNA检测标记
	private String djcbj;
	//DNA检测标记名称
	private String djcbjmc;
	//DNA实验日期
	private String dsyrq;
	//是否上传结果
	private String sfsc;
	//处理标记
	private String clbj;
	//医学部反馈
	private String yxbfk;
	//床位号
	private String cwh;
	//q20
	private String q20;
	//q30
	private String q30;
	//外部编码
	private String wbbm;
	//是否自免检测  0：否  1：是
	private String sfzmjc;
	//自免项目
	private String zmxm;
	//自免目的地
	private String zmmdd;
	//其他检测标记
	private String qtjcbj;
	//其他实验日期
	private String qtsyrq;
	//送检区分
	private String sjqf;
	//是否开票
	private String kpsq;
	//送检单位名称
	private String sjdwmc;
	//检测子项目
	private String jczxm;
	//退款方式
	private String tkfs;
	//退款时间
	private String tksj;
	//退款金额
	private String tkje;
	//顺丰标记（已签收订单为1，订单号查找不到为2）
	private String sfbj;
	//运达时间
	private String ydsj;
	//预计运达时间
	private String yjydsj;
	//检测结果(json存病原体列表的pathpgen的中文名称)
	private String jcjg;
	//疑似结果(json存background的中文名称)
	private String ysjg;
	//镁信id
	private String mxid;
	//是否VIP
	private String sfvip;
	//申请类型
	private String sqlx;
	//科研项目
	private String kyxm;
	//参数扩展6
	private String cskz6;
	//取样人员
	private String qyry;
	//取样时间
	private String qysj;
	//是否完善：0：未完善 1：第一页已完善 2：第二页已完善 3：已完善
	private String sfws;
	//定值结果
	private String dzjg;

	public String getDzjg() {
		return dzjg;
	}

	public void setDzjg(String dzjg) {
		this.dzjg = dzjg;
	}

	public String getSfws() {
		return sfws;
	}

	public void setSfws(String sfws) {
		this.sfws = sfws;
	}
	public String getQyry() {
		return qyry;
	}

	public void setQyry(String qyry) {
		this.qyry = qyry;
	}

	public String getClbj() {
		return clbj;
	}

	public void setClbj(String clbj) {
		this.clbj = clbj;
	}

	public String getQysj() {
		return qysj;
	}

	public void setQysj(String qysj) {
		this.qysj = qysj;
	}

	public String getCskz6() {
		return cskz6;
	}

	public void setCskz6(String cskz6) {
		this.cskz6 = cskz6;
	}

	public String getKyxm() {
		return kyxm;
	}

	public void setKyxm(String kyxm) {
		this.kyxm = kyxm;
	}

	public String getJcsj() {
		return jcsj;
	}

	public void setJcsj(String jcsj) {
		this.jcsj = jcsj;
	}

	public String getSqlx() {
		return sqlx;
	}

	public void setSqlx(String sqlx) {
		this.sqlx = sqlx;
	}

	public String getSfvip() {
		return sfvip;
	}

	public void setSfvip(String sfvip) {
		this.sfvip = sfvip;
	}

	public String getMxid() {
		return mxid;
	}

	public void setMxid(String mxid) {
		this.mxid = mxid;
	}


	public String getJcjg() { return jcjg; }

	public void setJcjg(String jcjg) { this.jcjg = jcjg; }

	public String getYsjg() { return ysjg; }

	public void setYsjg(String ysjg) { this.ysjg = ysjg; }

	public String getYjydsj() {
		return yjydsj;
	}

	public void setYjydsj(String yjydsj) {
		this.yjydsj = yjydsj;
	}

	public String getYdsj() {
		return ydsj;
	}

	public void setYdsj(String ydsj) {
		this.ydsj = ydsj;
	}

	public String getSfbj() {
		return sfbj;
	}
	public void setSfbj(String sfbj) {
		this.sfbj = sfbj;
	}
	public String getTkfs() {
		return tkfs;
	}
	public void setTkfs(String tkfs) {
		this.tkfs = tkfs;
	}
	public String getTksj() {
		return tksj;
	}
	public void setTksj(String tksj) {
		this.tksj = tksj;
	}
	public String getTkje() {
		return tkje;
	}
	public void setTkje(String tkje) {
		this.tkje = tkje;
	}
	public String getJczxm() {
		return jczxm;
	}
	public void setJczxm(String jczxm) {
		this.jczxm = jczxm;
	}
	public String getSjdwmc() {
		return sjdwmc;
	}
	public void setSjdwmc(String sjdwmc) {
		this.sjdwmc = sjdwmc;
	}
	public String getQtsyrq() {
		return qtsyrq;
	}
	public void setQtsyrq(String qtsyrq) {
		this.qtsyrq = qtsyrq;
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
	public String getZmxm() {
		return zmxm;
	}
	public void setZmxm(String zmxm) {
		this.zmxm = zmxm;
	}
	public String getZmmdd() {
		return zmmdd;
	}
	public void setZmmdd(String zmmdd) {
		this.zmmdd = zmmdd;
	}
	public String getWbbm() {
		return wbbm;
	}
	public void setWbbm(String wbbm) {
		this.wbbm = wbbm;
	}
	public String getQ20() {
		return q20;
	}
	public void setQ20(String q20) {
		this.q20 = q20;
	}
	public String getQ30() {
		return q30;
	}
	public void setQ30(String q30) {
		this.q30 = q30;
	}
	public String getCwh() {
		return cwh;
	}
	public void setCwh(String cwh) {
		this.cwh = cwh;
	}
	public String getYxbfk()
	{
		return yxbfk;
	}
	public void setYxbfk(String yxbfk)
	{
		this.yxbfk = yxbfk;
	}
	public String getSfsc() {
		return sfsc;
	}
	public void setSfsc(String sfsc) {
		this.sfsc = sfsc;
	}
	public String getJcbjmc() {
		return jcbjmc;
	}
	public void setJcbjmc(String jcbjmc) {
		this.jcbjmc = jcbjmc;
	}
	public String getDjcbj() {
		return djcbj;
	}
	public void setDjcbj(String djcbj) {
		this.djcbj = djcbj;
	}
	public String getDjcbjmc() {
		return djcbjmc;
	}
	public void setDjcbjmc(String djcbjmc) {
		this.djcbjmc = djcbjmc;
	}
	public String getDsyrq() {
		return dsyrq;
	}
	public void setDsyrq(String dsyrq) {
		this.dsyrq = dsyrq;
	}
	public String getSfje() {
		return sfje;
	}
	public void setSfje(String sfje) {
		this.sfje = sfje;
	}
	public String getJt() {
		return jt;
	}
	public void setJt(String jt) {
		this.jt = jt;
	}
	public String getSyrq() {
		return syrq;
	}
	public void setSyrq(String syrq) {
		this.syrq = syrq;
	}
	public String getSfsf()
	{
		return sfsf;
	}
	public void setSfsf(String sfsf)
	{
		this.sfsf = sfsf;
	}
	public String getJcbj()
	{
		return jcbj;
	}
	public void setJcbj(String jcbj)
	{
		this.jcbj = jcbj;
	}
	public String getQtks(){
		return qtks;
	}
	public void setQtks(String qtks)
	{
		this.qtks = qtks;
	}
	public String getNldw(){
		return nldw;
	}
	public void setNldw(String nldw)
	{
		this.nldw = nldw;
	}
	//送检ID
	public String getSjid() {
		return sjid;
	}
	public void setSjid(String sjid){
		this.sjid = sjid;
	}
	//患者姓名
	public String getHzxm() {
		return hzxm;
	}
	public void setHzxm(String hzxm){
		this.hzxm = hzxm;
	}
	//性别
	public String getXb() {
		return xb;
	}
	public void setXb(String xb) {
		this.xb = xb;
	}
	//年龄
	public String getNl() {
		return nl;
	}
	public void setNl(String nl){
		this.nl = nl;
	}
	//电话
	public String getDh() {
		return dh;
	}
	public void setDh(String dh){
		this.dh = dh;
	}
	//送检单位
	public String getSjdw() {
		return sjdw;
	}
	public void setSjdw(String sjdw){
		this.sjdw = sjdw;
	}
	//科室
	public String getKs() {
		return ks;
	}
	public void setKs(String ks){
		this.ks = ks;
	}
	//送检医生  暂时不关联
	public String getSjys() {
		return sjys;
	}
	public void setSjys(String sjys){
		this.sjys = sjys;
	}
	//医生电话
	public String getYsdh() {
		return ysdh;
	}
	public void setYsdh(String ysdh){
		this.ysdh = ysdh;
	}
	//代表  关联微信用户
	public String getDb() {
		return db;
	}
	public void setDb(String db){
		this.db = db;
	}
	//标本类型  关联基础数据
	public String getYblx() {
		return yblx;
	}
	public void setYblx(String yblx){
		this.yblx = yblx;
	}
	//标本类型名称 标本类型为其他的时候填写
	public String getYblxmc() {
		return yblxmc;
	}
	public void setYblxmc(String yblxmc){
		this.yblxmc = yblxmc;
	}
	//标本体积 任意填写
	public String getYbtj() {
		return ybtj;
	}
	public void setYbtj(String ybtj){
		this.ybtj = ybtj;
	}
	//住院号
	public String getZyh() {
		return zyh;
	}
	public void setZyh(String zyh){
		this.zyh = zyh;
	}
	//标本编号
	public String getYbbh() {
		return ybbh;
	}
	public void setYbbh(String ybbh){
		this.ybbh = ybbh;
	}
	//采样日期
	public String getCyrq() {
		return cyrq;
	}
	public void setCyrq(String cyrq){
		this.cyrq = cyrq;
	}
	//接收日期
	public String getJsrq() {
		return jsrq;
	}
	public void setJsrq(String jsrq){
		this.jsrq = jsrq;
	}
	public String getJsry()
	{
		return jsry;
	}
	public void setJsry(String jsry)
	{
		this.jsry = jsry;
	}
	//报告日期
	public String getBgrq() {
		return bgrq;
	}
	public void setBgrq(String bgrq){
		this.bgrq = bgrq;
	}
	//临床症状
	public String getLczz() {
		return lczz;
	}
	public void setLczz(String lczz){
		this.lczz = lczz;
	}
	//前期诊断
	public String getQqzd() {
		return qqzd;
	}
	public void setQqzd(String qqzd){
		this.qqzd = qqzd;
	}
	//近期用药
	public String getJqyy() {
		return jqyy;
	}
	public void setJqyy(String jqyy){
		this.jqyy = jqyy;
	}
	//前期检测
	public String getQqjc() {
		return qqjc;
	}
	public void setQqjc(String qqjc){
		this.qqjc = qqjc;
	}
	//检验结果  0：未检验 1：已检验  2：检验通过 3：检验未通过
	public String getJyjg() {
		return jyjg;
	}
	public void setJyjg(String jyjg){
		this.jyjg = jyjg;
	}
    //快递类型
	public String getKdlx() {
		return kdlx;
	}
	public void setKdlx(String kdlx) {
		this.kdlx = kdlx;
	}
	//快递号
	public String getKdh() {
		return kdh;
	}
	public void setKdh(String kdh) {
		this.kdh = kdh;
	}
	//快递费用
    public String getKdfy() {
        return kdfy;
    }
    public void setKdfy(String kdfy) {
        this.kdfy = kdfy;
    }
	//备注
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	
	public String getZt() {
		return zt;
	}
	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getFkbj() {
		return fkbj;
	}
	public void setFkbj(String fkbj) {
		this.fkbj = fkbj;
	}

	public String getFkje() {
		return fkje;
	}
	public void setFkje(String fkje) {
		this.fkje = fkje;
	}

	public String getNbbm() {
		return nbbm;
	}
	public void setNbbm(String nbbm) {
		this.nbbm = nbbm;
	}
	public String getDdh() {
		return ddh;
	}
	public void setDdh(String ddh) {
		this.ddh = ddh;
	}

	public String getSjqf() {
		return sjqf;
	}
	public void setSjqf(String sjqf) {
		this.sjqf = sjqf;
	}
	public String getKpsq() {
		return kpsq;
	}
	public void setKpsq(String kpsq) {
		this.kpsq = kpsq;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
