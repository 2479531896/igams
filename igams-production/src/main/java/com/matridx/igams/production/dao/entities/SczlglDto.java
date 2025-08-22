package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.entities.ShxxDto;
import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias("SczlglDto")
public class SczlglDto extends SczlglModel {
    //需求计划明细id
    private String xqjhmxid;
    //明细json
    private String sczlmx_json;
    //附件ids
    private List<String> fjids;
    //物料名称
    private String wlmc;
    //操作标识
    private String czbs;
    //物料编码
    private String wlbm;
    //产品类型代码
    private String cplxdm;
    //查询条件
    private String entire;
    //可引用数量
    private String kyysl;
    //引用数量标记
    private String yyslbj;

    //需求开始时间
    private String zlrqstart;
    //需求开始时间
    private String sczlmxid;
    //需求结束时间
    private String zlrqend;
    //规格
    private String gg;
    //需求单号
    private String xqdh;
    //产品类型名称
    private String cplxmc;
    //序列号
    private String xlh;
    // 类别 数组
    private String[] cplxs;
    //成品需求id
    private String cpxqid;
    //物料id
    private String wlid;
    //生产商
    private String scs;
    //单位
    private String jldw;
    //生产数量
    private String scsl;
    //可用数量
    private String kysl;
    //备用数量
    private String bysl;
    //钉钉标记
    private String ddbj;
    //区分不同的审核页面
    private String xsbj;
    //所属机构
    private String ssjg;
    //ywids
    private String ywids;
    //质量类别
    private String lbcsmc;
    //货号
    private String ychh;
    //预计完成时间
    private String yjwcsj;
    //审核流程信息
    private List<ShxxDto> shxxDtos;
    //状态s
    private String[] zts;
    //预计完成时间开始
    private String yjwcsjstart;
    //预计完成时间结束
    private String yjwcsjend;
    //钉钉查询字段
    private String ddentire;
    private String dhsl;//到货数量
    private String scgs;//生产工时
    private String zjgs;//质检工时
    private String sjwcsj;//实际完成时间
    private String sjwctime;
    private String sfcs;//是否超时
    private String ztmc;
    private String scph;//生产批号
    private String dhjyid;//到货检验id
    private String rkid;//入库id
    private String rkzt;//入库状态
    private String tjbj;//统计标记
    private String dhid;//到货id
    private String qgid;//请购id
    private String flag;
    private String sfsc;//是否生产
    private String yhm;//用户名
	private String sqlParam;
	private String jlbh;//记录编号

    public String getJlbh() {
        return jlbh;
    }

    public void setJlbh(String jlbh) {
        this.jlbh = jlbh;
    }

    public String getSqlParam() {
        return sqlParam;
    }

    public void setSqlParam(String sqlParam) {
        this.sqlParam = sqlParam;
    }
	
    public String getYhm() {
        return yhm;
    }

    public void setYhm(String yhm) {
        this.yhm = yhm;
    }

    public String getSfsc() {
        return sfsc;
    }

    public void setSfsc(String sfsc) {
        this.sfsc = sfsc;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }


    public String getDhid() {
        return dhid;
    }

    public void setDhid(String dhid) {
        this.dhid = dhid;
    }

    public String getQgid() {
        return qgid;
    }

    public void setQgid(String qgid) {
        this.qgid = qgid;
    }

    public String getTjbj() {
        return tjbj;
    }

    public void setTjbj(String tjbj) {
        this.tjbj = tjbj;
    }

    public String getRkzt() {
        return rkzt;
    }

    public void setRkzt(String rkzt) {
        this.rkzt = rkzt;
    }

    public String getDhjyid() {
        return dhjyid;
    }

    public void setDhjyid(String dhjyid) {
        this.dhjyid = dhjyid;
    }

    public String getRkid() {
        return rkid;
    }

    public void setRkid(String rkid) {
        this.rkid = rkid;
    }

    public String getScph() {
        return scph;
    }

    public void setScph(String scph) {
        this.scph = scph;
    }

    public String getZtmc() {
        return ztmc;
    }

    public void setZtmc(String ztmc) {
        this.ztmc = ztmc;
    }

    public String getSfcs() {
        return sfcs;
    }

    public void setSfcs(String sfcs) {
        this.sfcs = sfcs;
    }

    private String kcl;//库存量

    public String getScgs() {
        return scgs;
    }

    public void setScgs(String scgs) {
        this.scgs = scgs;
    }

    public String getZjgs() {
        return zjgs;
    }

    public void setZjgs(String zjgs) {
        this.zjgs = zjgs;
    }

    public String getSjwcsj() {
        return sjwcsj;
    }

    public void setSjwcsj(String sjwcsj) {
        this.sjwcsj = sjwcsj;
    }

    public String getSjwctime() {
        return sjwctime;
    }

    public void setSjwctime(String sjwctime) {
        this.sjwctime = sjwctime;
    }

    public String getKcl() {
        return kcl;
    }

    public void setKcl(String kcl) {
        this.kcl = kcl;
    }

    public String getDhsl() {
        return dhsl;
    }

    public void setDhsl(String dhsl) {
        this.dhsl = dhsl;
    }

    public String getDdentire() {
        return ddentire;
    }

    public void setDdentire(String ddentire) {
        this.ddentire = ddentire;
    }

    public String getYjwcsjstart() {
        return yjwcsjstart;
    }

    public void setYjwcsjstart(String yjwcsjstart) {
        this.yjwcsjstart = yjwcsjstart;
    }

    public String getYjwcsjend() {
        return yjwcsjend;
    }

    public void setYjwcsjend(String yjwcsjend) {
        this.yjwcsjend = yjwcsjend;
    }

    public void setZts(String[] zts) {
        this.zts = zts;
        for (int i = 0; i < zts.length; i++){
            this.zts[i]=this.zts[i].replace("'","");
        }
    }

    public String[] getZts() {
        return zts;
    }

    public List<ShxxDto> getShxxDtos() {
        return shxxDtos;
    }

    public void setShxxDtos(List<ShxxDto> shxxDtos) {
        this.shxxDtos = shxxDtos;
    }

    public String getYjwcsj() {
        return yjwcsj;
    }

    public void setYjwcsj(String yjwcsj) {
        this.yjwcsj = yjwcsj;
    }

    public String getYyslbj() {
        return yyslbj;
    }

    public void setYyslbj(String yyslbj) {
        this.yyslbj = yyslbj;
    }

    public String getKyysl() {
        return kyysl;
    }

    public void setKyysl(String kyysl) {
        this.kyysl = kyysl;
    }

    public String getYchh() {
        return ychh;
    }

    public void setYchh(String ychh) {
        this.ychh = ychh;
    }

    public String getLbcsmc() {
        return lbcsmc;
    }

    public void setLbcsmc(String lbcsmc) {
        this.lbcsmc = lbcsmc;
    }

    public String getYwids() {
        return ywids;
    }

    public void setYwids(String ywids) {
        this.ywids = ywids;
    }

    public String getCzbs() {
        return czbs;
    }

    public void setCzbs(String czbs) {
        this.czbs = czbs;
    }

    public String getSsjg() {
        return ssjg;
    }

    public void setSsjg(String ssjg) {
        this.ssjg = ssjg;
    }

    public String getXsbj() {
        return xsbj;
    }

    public void setXsbj(String xsbj) {
        this.xsbj = xsbj;
    }

    public String getDdbj() {
        return ddbj;
    }

    public void setDdbj(String ddbj) {
        this.ddbj = ddbj;
    }

    public String getBysl() {
        return bysl;
    }

    public void setBysl(String bysl) {
        this.bysl = bysl;
    }

    public String getKysl() {
        return kysl;
    }

    public void setKysl(String kysl) {
        this.kysl = kysl;
    }

    public String getScsl() {
        return scsl;
    }

    public void setScsl(String scsl) {
        this.scsl = scsl;
    }

    public String getJldw() {
        return jldw;
    }

    public void setJldw(String jldw) {
        this.jldw = jldw;
    }

    public String getScs() {
        return scs;
    }

    public void setScs(String scs) {
        this.scs = scs;
    }

    public String getWlid() {
        return wlid;
    }

    public void setWlid(String wlid) {
        this.wlid = wlid;
    }

    public String getCplxdm() {
        return cplxdm;
    }

    public void setCplxdm(String cplxdm) {
        this.cplxdm = cplxdm;
    }

    public String[] getCplxs() {
        return cplxs;
    }
    public void setCplxs(String[] lbs) {
        this.cplxs = lbs;
        for (int i = 0; i < lbs.length; i++){
            this.cplxs[i]=this.cplxs[i].replace("'","");
        }
    }
    public String getCpxqid() {
        return cpxqid;
    }

    public void setCpxqid(String cpxqid) {
        this.cpxqid = cpxqid;
    }

    public String getSczlmxid() {
        return sczlmxid;
    }

    public void setSczlmxid(String sczlmxid) {
        this.sczlmxid = sczlmxid;
    }

    public String getXlh() {
        return xlh;
    }

    public void setXlh(String xlh) {
        this.xlh = xlh;
    }

    public String getWlmc() {
        return wlmc;
    }

    public void setWlmc(String wlmc) {
        this.wlmc = wlmc;
    }

    public String getWlbm() {
        return wlbm;
    }

    public void setWlbm(String wlbm) {
        this.wlbm = wlbm;
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    public String getZlrqstart() {
        return zlrqstart;
    }

    public void setZlrqstart(String zlrqstart) {
        this.zlrqstart = zlrqstart;
    }

    public String getZlrqend() {
        return zlrqend;
    }

    public void setZlrqend(String zlrqend) {
        this.zlrqend = zlrqend;
    }

    public String getGg() {
        return gg;
    }

    public void setGg(String gg) {
        this.gg = gg;
    }

    public String getXqdh() {
        return xqdh;
    }

    public void setXqdh(String xqdh) {
        this.xqdh = xqdh;
    }

    public String getCplxmc() {
        return cplxmc;
    }

    public void setCplxmc(String cplxmc) {
        this.cplxmc = cplxmc;
    }

    public List<String> getFjids() {
        return fjids;
    }

    public void setFjids(List<String> fjids) {
        this.fjids = fjids;
    }

    public String getSczlmx_json() {
        return sczlmx_json;
    }

    public void setSczlmx_json(String sczlmx_json) {
        this.sczlmx_json = sczlmx_json;
    }

    public String getXqjhmxid() {
        return xqjhmxid;
    }

    public void setXqjhmxid(String xqjhmxid) {
        this.xqjhmxid = xqjhmxid;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
