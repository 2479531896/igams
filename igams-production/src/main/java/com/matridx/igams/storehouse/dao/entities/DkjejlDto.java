package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

/**
 * @author:JYK
 */
@Alias("DkjejlDto")
public class DkjejlDto extends DkjejlModel{
    private String dkjlmx_json;
    private String xsid;//销售id
    private String flag_xz;//限制标记
    private String djrq;//单据日期
    private String dysyje;//对应剩余额度
    private String hkzq;//回款周期
    private List<String> ywids;//业务ids
    private String entire;//全部
    private String ywdh;//业务单号
    private String wlid;//物料id
    private String wlmc;//物料名称
    private String wlbm;//物料编码
    private String fhsl;//发货数量
    private String hkzt;//回款状态
    private String[] hkzts;//回款状态s
    private String djrqstart;//单据日期开始
    private String djrqend;//单据日期结束
    private String dkystart;//到款月开始
    private String dkyend;//到款月结束
    private String sqlParam;//sql导出
    private String dkbj;//到款标记
    private String oaxsdh;//销售单号
    private String fhdh;//发货单号
    private String fhid;//发货id
    private String _key;//导出主键
    private String hsdj;//含税单价
    private String xsmxid;//销售明细id

    public String getXsmxid() {
        return xsmxid;
    }

    public void setXsmxid(String xsmxid) {
        this.xsmxid = xsmxid;
    }

    public String getHsdj() {
        return hsdj;
    }

    public void setHsdj(String hsdj) {
        this.hsdj = hsdj;
    }

    public String get_key() {
        return _key;
    }

    public void set_key(String _key) {
        this._key = _key;
    }

    public String getFhid() {
        return fhid;
    }

    public void setFhid(String fhid) {
        this.fhid = fhid;
    }

    public String getOaxsdh() {
        return oaxsdh;
    }

    public void setOaxsdh(String oaxsdh) {
        this.oaxsdh = oaxsdh;
    }

    public String getFhdh() {
        return fhdh;
    }

    public void setFhdh(String fhdh) {
        this.fhdh = fhdh;
    }

    public String getDkbj() {
        return dkbj;
    }

    public void setDkbj(String dkbj) {
        this.dkbj = dkbj;
    }

    public String getSqlParam() {
        return sqlParam;
    }

    public void setSqlParam(String sqlParam) {
        this.sqlParam = sqlParam;
    }

    public String getYwdh() {
        return ywdh;
    }

    public void setYwdh(String ywdh) {
        this.ywdh = ywdh;
    }

    public String getWlid() {
        return wlid;
    }

    public void setWlid(String wlid) {
        this.wlid = wlid;
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

    public String getFhsl() {
        return fhsl;
    }

    public void setFhsl(String fhsl) {
        this.fhsl = fhsl;
    }

    public String getHkzt() {
        return hkzt;
    }

    public void setHkzt(String hkzt) {
        this.hkzt = hkzt;
    }

    public String[] getHkzts() {
        return hkzts;
    }

    public void setHkzts(String[] hkzts) {
        this.hkzts = hkzts;
    }

    public String getDjrqstart() {
        return djrqstart;
    }

    public void setDjrqstart(String djrqstart) {
        this.djrqstart = djrqstart;
    }

    public String getDjrqend() {
        return djrqend;
    }

    public void setDjrqend(String djrqend) {
        this.djrqend = djrqend;
    }

    public String getDkystart() {
        return dkystart;
    }

    public void setDkystart(String dkystart) {
        this.dkystart = dkystart;
    }

    public String getDkyend() {
        return dkyend;
    }

    public void setDkyend(String dkyend) {
        this.dkyend = dkyend;
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    public List<String> getYwids() {
        return ywids;
    }

    public void setYwids(List<String> ywids) {
        this.ywids = ywids;
    }

    public String getHkzq() {
        return hkzq;
    }

    public void setHkzq(String hkzq) {
        this.hkzq = hkzq;
    }

    public String getDysyje() {
        return dysyje;
    }

    public void setDysyje(String dysyje) {
        this.dysyje = dysyje;
    }

    //销售明细ids
    private List<String> xsmxids;
    //发货明细ids
    private List<String> fhmxids;

    public List<String> getFhmxids() {
        return fhmxids;
    }

    public void setFhmxids(List<String> fhmxids) {
        this.fhmxids = fhmxids;
    }

    public List<String> getXsmxids() {
        return xsmxids;
    }

    public void setXsmxids(List<String> xsmxids) {
        this.xsmxids = xsmxids;
    }

    public String getDjrq() {
        return djrq;
    }

    public void setDjrq(String djrq) {
        this.djrq = djrq;
    }

    public String getFlag_xz() {
        return flag_xz;
    }

    public void setFlag_xz(String flag_xz) {
        this.flag_xz = flag_xz;
    }

    public String getXsid() {
        return xsid;
    }

    public void setXsid(String xsid) {
        this.xsid = xsid;
    }

    public String getDkjlmx_json() {
        return dkjlmx_json;
    }

    public void setDkjlmx_json(String dkjlmx_json) {
        this.dkjlmx_json = dkjlmx_json;
    }
}
