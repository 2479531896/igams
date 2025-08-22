package com.matridx.igams.wechat.dao.entities;


import org.apache.ibatis.type.Alias;

import java.util.List;


/**
 * 商务应收账款(IgamsSwyszk)实体类
 *
 * @author makejava
 * @since 2023-04-12 10:17:46
 */
@Alias(value="SwyszkDto")
public class SwyszkDto extends SwyszkModel {
    //汇款主体名称
    private String hkztmc;
    //对账客户名称
    private String dzkhmc;
    //对接销售名称
    private String djxsmc;
    //客户性质名称
    private String khxzmc;
    private List<String> fjids;
    //全部(查询条件)
    private String entire;
    private String flag;
    private String flmc;
    //客户性质
    private String[] khxzs;
    private String zdzqstart;
    private String zdzqend;
    //导出关联标记为所选择的字段
    private String sqlParam;
    private String dkrqstart;
    private String dkrqend;

    private List<String> hbfls;
    private List<String> yszkids;
    private String kpqk;
    private String fkqk;
    private String fpslbj;
    private String yfpslbj;
    private String fkslbj;
    private String yfkslbj;

    private String khh;

    private String yhzh;

    private String xsmx_json;

    private String khfl;

    private String khflmc;
    private String yszk_json;
    private String fzryx;
    //客户回款周期
    private String khhkzq;
    //账单状态
    private String zdzt;

    private String szz;

    private List<String> htzts;
    //状态区分
    private String ztqf;

    private String hbflmc;
    private String fzrmc;
    private String jxjsje;
    private String lsdkrq;

    private String jxje;

    private String jxhsglid;

    private String jxhsmxid;

    private String sd;

    public String getSd() {
        return sd;
    }

    public void setSd(String sd) {
        this.sd = sd;
    }

    public String getJxhsglid() {
        return jxhsglid;
    }

    public void setJxhsglid(String jxhsglid) {
        this.jxhsglid = jxhsglid;
    }

    public String getJxhsmxid() {
        return jxhsmxid;
    }

    public void setJxhsmxid(String jxhsmxid) {
        this.jxhsmxid = jxhsmxid;
    }

    @Override
    public String getJxje() {
        return jxje;
    }

    @Override
    public void setJxje(String jxje) {
        this.jxje = jxje;
    }

    public String getJxjsje() {
        return jxjsje;
    }

    public void setJxjsje(String jxjsje) {
        this.jxjsje = jxjsje;
    }

    public String getHbflmc() {
        return hbflmc;
    }

    public void setHbflmc(String hbflmc) {
        this.hbflmc = hbflmc;
    }

    public String getFzrmc() {
        return fzrmc;
    }

    public void setFzrmc(String fzrmc) {
        this.fzrmc = fzrmc;
    }

    public String getLsdkrq() {
        return lsdkrq;
    }

    public void setLsdkrq(String lsdkrq) {
        this.lsdkrq = lsdkrq;
    }

    public String getZtqf() {
        return ztqf;
    }

    public void setZtqf(String ztqf) {
        this.ztqf = ztqf;
    }

    public String getZdzt() {
        return zdzt;
    }

    public void setZdzt(String zdzt) {
        this.zdzt = zdzt;
    }

    public String getKhhkzq() {
        return khhkzq;
    }

    public void setKhhkzq(String khhkzq) {
        this.khhkzq = khhkzq;
    }

    public String getFzryx() {
        return fzryx;
    }

    public void setFzryx(String fzryx) {
        this.fzryx = fzryx;
    }

    public String getYszk_json() {
        return yszk_json;
    }

    public void setYszk_json(String yszk_json) {
        this.yszk_json = yszk_json;
    }

    public String getXsmx_json() {
        return xsmx_json;
    }

    public void setXsmx_json(String xsmx_json) {
        this.xsmx_json = xsmx_json;
    }

    public String getFpslbj() {
        return fpslbj;
    }

    public void setFpslbj(String fpslbj) {
        this.fpslbj = fpslbj;
    }

    public String getYfpslbj() {
        return yfpslbj;
    }

    public void setYfpslbj(String yfpslbj) {
        this.yfpslbj = yfpslbj;
    }

    public String getFkslbj() {
        return fkslbj;
    }

    public void setFkslbj(String fkslbj) {
        this.fkslbj = fkslbj;
    }

    public String getYfkslbj() {
        return yfkslbj;
    }

    public void setYfkslbj(String yfkslbj) {
        this.yfkslbj = yfkslbj;
    }

    public String getKpqk() {
        return kpqk;
    }

    public void setKpqk(String kpqk) {
        this.kpqk = kpqk;
    }

    public String getFkqk() {
        return fkqk;
    }

    public void setFkqk(String fkqk) {
        this.fkqk = fkqk;
    }

    public String getDkrqstart() {
        return dkrqstart;
    }

    public void setDkrqstart(String dkrqstart) {
        this.dkrqstart = dkrqstart;
    }

    public String getDkrqend() {
        return dkrqend;
    }

    public void setDkrqend(String dkrqend) {
        this.dkrqend = dkrqend;
    }

    private String _key; //用于vue前端搜索导出 主键id


    public String getFlmc() {
        return flmc;
    }

    public void setFlmc(String flmc) {
        this.flmc = flmc;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String get_key() {
        return _key;
    }

    public void set_key(String _key) {
        this._key = _key;
    }

    public String getSqlParam() {
        return sqlParam;
    }

    public void setSqlParam(String sqlParam) {
        this.sqlParam = sqlParam;
    }

    public String getZdzqstart() {
        return zdzqstart;
    }

    public void setZdzqstart(String zdzqstart) {
        this.zdzqstart = zdzqstart;
    }

    public String getZdzqend() {
        return zdzqend;
    }

    public void setZdzqend(String zdzqend) {
        this.zdzqend = zdzqend;
    }

    public String[] getKhxzs() {
        return khxzs;
    }

    public void setKhxzs(String[] khxzs) {
        this.khxzs = khxzs;
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    public List<String> getFjids() {
        return fjids;
    }

    public void setFjids(List<String> fjids) {
        this.fjids = fjids;
    }

    public String getHkztmc() {
        return hkztmc;
    }

    public void setHkztmc(String hkztmc) {
        this.hkztmc = hkztmc;
    }

    public String getDzkhmc() {
        return dzkhmc;
    }

    public void setDzkhmc(String dzkhmc) {
        this.dzkhmc = dzkhmc;
    }

    public String getDjxsmc() {
        return djxsmc;
    }

    public void setDjxsmc(String djxsmc) {
        this.djxsmc = djxsmc;
    }

    public String getKhxzmc() {
        return khxzmc;
    }

    public void setKhxzmc(String khxzmc) {
        this.khxzmc = khxzmc;
    }

    private static final long serialVersionUID = 1L;


    public List<String> getHbfls() {
        return hbfls;
    }

    public void setHbfls(List<String> hbfls) {
        this.hbfls = hbfls;
    }

    public List<String> getYszkids() {
        return yszkids;
    }

    public void setYszkids(List<String> yszkids) {
        this.yszkids = yszkids;
    }

    public String getYhzh() {
        return yhzh;
    }

    public void setYhzh(String yhzh) {
        this.yhzh = yhzh;
    }

    public String getKhh() {
        return khh;
    }

    public void setKhh(String khh) {
        this.khh = khh;
    }

    public String getKhfl() {
        return khfl;
    }

    public void setKhfl(String khfl) {
        this.khfl = khfl;
    }

    public String getKhflmc() {
        return khflmc;
    }

    public void setKhflmc(String khflmc) {
        this.khflmc = khflmc;
    }



    public List<String> getHtzts() {
        return htzts;
    }

    public void setHtzts(List<String> htzts) {
        this.htzts = htzts;
    }

    public String getSzz() {
        return szz;
    }

    public void setSzz(String szz) {
        this.szz = szz;
    }
}

