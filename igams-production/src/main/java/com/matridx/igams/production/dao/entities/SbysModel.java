package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias(value="SbysModel")
public class SbysModel extends BaseBasicModel {

    //设备验收ID
    private String sbysid;
    //货物ID
    private String hwid;
    //固定资产编号
    private String gdzcbh;
    //验收数量
    private String yssl;
    //设备出厂编号
    private String sbccbh;
    //使用地点
    private String sydd;
    //验收结果
    private String ysjg;
    //验收日期
    private String ysrq;
    //保修时间
    private String bxsj;
    //状态
    private String zt;
    //设备验收单号
    private String sbysdh;
    //是否为贵重物品
    private String sfgz;
    //设备类型
    private String sblx;
    //设备名称
    private String sbmc;
    //规格型号
    private String ggxh;
    //购置时间
    private String gzsj;
    //供应商
    private String gys;
    //生产厂家
    private String sccj;
    //金额
    private String je;
    //定置位置
    private String dzwz;
    //现使用人
    private String xsyr;
    //领用时间
    private String lysj;
    //序列号
    private String xlh;
    //请购明细id
    private String qgmxid;
    //设备状态
    private String sbzt;
    //原设备编号
    private String ysbbh;
    //原固定资产编号
    private String ygdzcbh;
    //盘库状态
    private String pkzt;
    //管理人员
    private String glry;
    //现使用部门
    private String xsybm;
    private String ysr;	    //验收人
    private String ysdd;	//验收地点
    private String bz;	    //备注
    private String bzqk;	//包装情况
    private String wgqk;	//外观情况
    private String jyqk;	//绝缘情况
    private String zxdnr;	//装箱单内容
    private String sbcs;	//设备参数
    private String yqgnyz;	//仪器功能运转
    private String sjcs;	//数据传输
    private String sjcc;	//数据存储
    private String sjwzxhbmx;	//数据完整性和保密性
    private String sjclhjs;		//数据处理和检索
    private String xnyz;		//性能验证
    private String ysjl;		//验收结论
    private String tzzt;		//台账状态
    private String byzq;		//保养周期
    private String xcbysj;		//下次保养时间
    private String jlzq;		//计量周期
    private String xcjlsj;		//下次计量时间
    private String yzzq;		//验证周期
    private String xcyzsj;		//下次验证时间
    private String sfjl;		//是否计量
    private String sfyz;		//是否验证
    private String bmsbfzr;	    //部门设备负责人
    private String qwfs;	    //去污方式
    private String qyrq;	    //启用日期
    private String lsid;//临时id
    private String shzt;//审核状态
    private String xzllmxid;//行政领料明细id
    private String sccjlxfs;//生产厂家联系方式
	private String sbbh;	    //设备编号
    private String jlrq;//计量日期
    private String yzrq;//验证日期
    private String jlbh;//记录编号
    private String ccrq;//出厂日期
    private String byrq;//保养日期
    private String xzllid;//行政领料id
    private String ysbzt;//原设备状态
    private String pdzt;//盘点状态

    public String getPdzt() {
        return pdzt;
    }

    public void setPdzt(String pdzt) {
        this.pdzt = pdzt;
    }

    public String getYsbzt() {
        return ysbzt;
    }

    public void setYsbzt(String ysbzt) {
        this.ysbzt = ysbzt;
    }

    public String getXzllid() {
        return xzllid;
    }

    public void setXzllid(String xzllid) {
        this.xzllid = xzllid;
    }

    public String getByrq() {
        return byrq;
    }

    public void setByrq(String byrq) {
        this.byrq = byrq;
    }

    public String getCcrq() {
        return ccrq;
    }

    public void setCcrq(String ccrq) {
        this.ccrq = ccrq;
    }

    public String getJlbh() {
        return jlbh;
    }

    public void setJlbh(String jlbh) {
        this.jlbh = jlbh;
    }

    public String getYzrq() {
        return yzrq;
    }

    public void setYzrq(String yzrq) {
        this.yzrq = yzrq;
    }

    public String getJlrq() {
        return jlrq;
    }

    public void setJlrq(String jlrq) {
        this.jlrq = jlrq;
    }

    public String getSbbh() {
        return sbbh;
    }

    public void setSbbh(String sbbh) {
        this.sbbh = sbbh;
    }
    public String getSccjlxfs() {
        return sccjlxfs;
    }

    public void setSccjlxfs(String sccjlxfs) {
        this.sccjlxfs = sccjlxfs;
    }

    public String getLsid() {
        return lsid;
    }

    public void setLsid(String lsid) {
        this.lsid = lsid;
    }

    public String getShzt() {
        return shzt;
    }

    public void setShzt(String shzt) {
        this.shzt = shzt;
    }

    public String getXzllmxid() {
        return xzllmxid;
    }

    public void setXzllmxid(String xzllmxid) {
        this.xzllmxid = xzllmxid;
    }

    public String getYsr() {
        return ysr;
    }

    public void setYsr(String ysr) {
        this.ysr = ysr;
    }

    public String getYsdd() {
        return ysdd;
    }

    public void setYsdd(String ysdd) {
        this.ysdd = ysdd;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getBzqk() {
        return bzqk;
    }

    public void setBzqk(String bzqk) {
        this.bzqk = bzqk;
    }

    public String getWgqk() {
        return wgqk;
    }

    public void setWgqk(String wgqk) {
        this.wgqk = wgqk;
    }

    public String getJyqk() {
        return jyqk;
    }

    public void setJyqk(String jyqk) {
        this.jyqk = jyqk;
    }

    public String getZxdnr() {
        return zxdnr;
    }

    public void setZxdnr(String zxdnr) {
        this.zxdnr = zxdnr;
    }

    public String getSbcs() {
        return sbcs;
    }

    public void setSbcs(String sbcs) {
        this.sbcs = sbcs;
    }

    public String getYqgnyz() {
        return yqgnyz;
    }

    public void setYqgnyz(String yqgnyz) {
        this.yqgnyz = yqgnyz;
    }

    public String getSjcs() {
        return sjcs;
    }

    public void setSjcs(String sjcs) {
        this.sjcs = sjcs;
    }

    public String getSjcc() {
        return sjcc;
    }

    public void setSjcc(String sjcc) {
        this.sjcc = sjcc;
    }

    public String getSjwzxhbmx() {
        return sjwzxhbmx;
    }

    public void setSjwzxhbmx(String sjwzxhbmx) {
        this.sjwzxhbmx = sjwzxhbmx;
    }

    public String getSjclhjs() {
        return sjclhjs;
    }

    public void setSjclhjs(String sjclhjs) {
        this.sjclhjs = sjclhjs;
    }

    public String getXnyz() {
        return xnyz;
    }

    public void setXnyz(String xnyz) {
        this.xnyz = xnyz;
    }

    public String getYsjl() {
        return ysjl;
    }

    public void setYsjl(String ysjl) {
        this.ysjl = ysjl;
    }

    public String getTzzt() {
        return tzzt;
    }

    public void setTzzt(String tzzt) {
        this.tzzt = tzzt;
    }

    public String getByzq() {
        return byzq;
    }

    public void setByzq(String byzq) {
        this.byzq = byzq;
    }

    public String getXcbysj() {
        return xcbysj;
    }

    public void setXcbysj(String xcbysj) {
        this.xcbysj = xcbysj;
    }

    public String getJlzq() {
        return jlzq;
    }

    public void setJlzq(String jlzq) {
        this.jlzq = jlzq;
    }

    public String getXcjlsj() {
        return xcjlsj;
    }

    public void setXcjlsj(String xcjlsj) {
        this.xcjlsj = xcjlsj;
    }

    public String getYzzq() {
        return yzzq;
    }

    public void setYzzq(String yzzq) {
        this.yzzq = yzzq;
    }

    public String getXcyzsj() {
        return xcyzsj;
    }

    public void setXcyzsj(String xcyzsj) {
        this.xcyzsj = xcyzsj;
    }

    public String getSfjl() {
        return sfjl;
    }

    public void setSfjl(String sfjl) {
        this.sfjl = sfjl;
    }

    public String getSfyz() {
        return sfyz;
    }

    public void setSfyz(String sfyz) {
        this.sfyz = sfyz;
    }

    public String getBmsbfzr() {
        return bmsbfzr;
    }

    public void setBmsbfzr(String bmsbfzr) {
        this.bmsbfzr = bmsbfzr;
    }

    public String getQwfs() {
        return qwfs;
    }

    public void setQwfs(String qwfs) {
        this.qwfs = qwfs;
    }

    public String getQyrq() {
        return qyrq;
    }

    public void setQyrq(String qyrq) {
        this.qyrq = qyrq;
    }

    public String getSblx() {
        return sblx;
    }

    public void setSblx(String sblx) {
        this.sblx = sblx;
    }

    public String getSbmc() {
        return sbmc;
    }

    public void setSbmc(String sbmc) {
        this.sbmc = sbmc;
    }

    public String getGgxh() {
        return ggxh;
    }

    public void setGgxh(String ggxh) {
        this.ggxh = ggxh;
    }

    public String getGzsj() {
        return gzsj;
    }

    public void setGzsj(String gzsj) {
        this.gzsj = gzsj;
    }

    public String getGys() {
        return gys;
    }

    public void setGys(String gys) {
        this.gys = gys;
    }

    public String getSccj() {
        return sccj;
    }

    public void setSccj(String sccj) {
        this.sccj = sccj;
    }

    public String getJe() {
        return je;
    }

    public void setJe(String je) {
        this.je = je;
    }

    public String getDzwz() {
        return dzwz;
    }

    public void setDzwz(String dzwz) {
        this.dzwz = dzwz;
    }

    public String getXsyr() {
        return xsyr;
    }

    public void setXsyr(String xsyr) {
        this.xsyr = xsyr;
    }

    public String getLysj() {
        return lysj;
    }

    public void setLysj(String lysj) {
        this.lysj = lysj;
    }

    public String getXlh() {
        return xlh;
    }

    public void setXlh(String xlh) {
        this.xlh = xlh;
    }

    public String getQgmxid() {
        return qgmxid;
    }

    public void setQgmxid(String qgmxid) {
        this.qgmxid = qgmxid;
    }

    public String getSbzt() {
        return sbzt;
    }

    public void setSbzt(String sbzt) {
        this.sbzt = sbzt;
    }

    public String getYsbbh() {
        return ysbbh;
    }

    public void setYsbbh(String ysbbh) {
        this.ysbbh = ysbbh;
    }

    public String getYgdzcbh() {
        return ygdzcbh;
    }

    public void setYgdzcbh(String ygdzcbh) {
        this.ygdzcbh = ygdzcbh;
    }

    public String getPkzt() {
        return pkzt;
    }

    public void setPkzt(String pkzt) {
        this.pkzt = pkzt;
    }

    public String getGlry() {
        return glry;
    }

    public void setGlry(String glry) {
        this.glry = glry;
    }

    public String getXsybm() {
        return xsybm;
    }

    public void setXsybm(String xsybm) {
        this.xsybm = xsybm;
    }
    public String getSfgz() {
        return sfgz;
    }

    public void setSfgz(String sfgz) {
        this.sfgz = sfgz;
    }

    public String getSbysdh() {
        return sbysdh;
    }

    public void setSbysdh(String sbysdh) {
        this.sbysdh = sbysdh;
    }

    public String getSbysid() {
        return sbysid;
    }

    public void setSbysid(String sbysid) {
        this.sbysid = sbysid;
    }

    public String getHwid() {
        return hwid;
    }

    public void setHwid(String hwid) {
        this.hwid = hwid;
    }

    public String getGdzcbh() {
        return gdzcbh;
    }

    public void setGdzcbh(String gdzcbh) {
        this.gdzcbh = gdzcbh;
    }

    public String getYssl() {
        return yssl;
    }

    public void setYssl(String yssl) {
        this.yssl = yssl;
    }

    public String getSbccbh() {
        return sbccbh;
    }

    public void setSbccbh(String sbccbh) {
        this.sbccbh = sbccbh;
    }

    public String getSydd() {
        return sydd;
    }

    public void setSydd(String sydd) {
        this.sydd = sydd;
    }

    public String getYsjg() {
        return ysjg;
    }

    public void setYsjg(String ysjg) {
        this.ysjg = ysjg;
    }

    public String getYsrq() {
        return ysrq;
    }

    public void setYsrq(String ysrq) {
        this.ysrq = ysrq;
    }

    public String getBxsj() {
        return bxsj;
    }

    public void setBxsj(String bxsj) {
        this.bxsj = bxsj;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
