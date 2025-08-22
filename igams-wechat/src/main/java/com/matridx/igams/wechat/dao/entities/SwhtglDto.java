package com.matridx.igams.wechat.dao.entities;


import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import org.apache.ibatis.type.Alias;

import java.util.List;

/**
 * 商务合同管理(IgamsSwhtgl)实体类
 *
 * @author makejava
 * @since 2023-04-04 13:52:16
 */
@Alias(value="SwhtglDto")
public class SwhtglDto extends SwhtglModel {
    private static final long serialVersionUID = 1L;

    private String htflmc;
    private String hkzq;
    private String htfxmc;
    private String xslbmc;
    private String yzlxmc;
    private String htzflmc;
    private String khmc;
    private String qdztmc;
    private String entire;
    private String sflxmc;
    private String htksrqstart;
    private String htksrqend;
    private String htjsrqstart;
    private String htjsrqend;
    private String json;
    private String xh;
    private String jcxm;
    private String jczxm;
    private String jg;
    private String sfyxmc;
    private String bs;
    //审批人ID
    private String sprid;
    //审批人姓名
    private String sprxm;
    //审批人用户名
    private String spryhm;
    //审批人角色ID
    private String sprjsid;
    //审批人角色名称
    private String sprjsmc;
    //审批人钉钉ID
    private String sprddid;
    //钉钉标记
    private String ddbj;
    //附件IDS
    private List<String> fjids;
    //合同分类代码
    private String htfldm;
    private String _key; //用于vue前端搜索导出 主键id
    private String czbs;//操作标识
    private List<SwhtmxDto> htmxList;
    private List<FjcfbDto> fjcfbDtos;
    private String gsmc;
    private String khid;
    private String htzflcskz1;
    private List<JcsjDto> jcsjDtos;

    private String yhzcjson;


    public String getYhzcjson() {
        return yhzcjson;
    }

    public void setYhzcjson(String yhzcjson) {
        this.yhzcjson = yhzcjson;
    }

    public List<JcsjDto> getJcsjDtos() {
        return jcsjDtos;
    }

    public void setJcsjDtos(List<JcsjDto> jcsjDtos) {
        this.jcsjDtos = jcsjDtos;
    }

    public String getHtzflcskz1() {
        return htzflcskz1;
    }

    public void setHtzflcskz1(String htzflcskz1) {
        this.htzflcskz1 = htzflcskz1;
    }

    public String getGsmc() {
        return gsmc;
    }

    public void setGsmc(String gsmc) {
        this.gsmc = gsmc;
    }

    public String getKhid() {
        return khid;
    }

    public void setKhid(String khid) {
        this.khid = khid;
    }

    public String getHkzq() {
        return hkzq;
    }

    public void setHkzq(String hkzq) {
        this.hkzq = hkzq;
    }

    public String getCzbs() {
        return czbs;
    }

    public void setCzbs(String czbs) {
        this.czbs = czbs;
    }

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getJcxm() {
        return jcxm;
    }

    public void setJcxm(String jcxm) {
        this.jcxm = jcxm;
    }

    public String getJczxm() {
        return jczxm;
    }

    public void setJczxm(String jczxm) {
        this.jczxm = jczxm;
    }

    public String getJg() {
        return jg;
    }

    public void setJg(String jg) {
        this.jg = jg;
    }

    public String getSfyxmc() {
        return sfyxmc;
    }

    public void setSfyxmc(String sfyxmc) {
        this.sfyxmc = sfyxmc;
    }

    public String getBs() {
        return bs;
    }

    public void setBs(String bs) {
        this.bs = bs;
    }

    public String getHtfldm() {
        return htfldm;
    }

    public void setHtfldm(String htfldm) {
        this.htfldm = htfldm;
    }

    private List<String> htzts;
    private List<String> chapterFjids;
    private List<String> khids;
    private List<String> htflids;
    private List<String> htzflids;
    private List<String> qdztids;
    private String SqlParam;

    public String getQdztmc() {
        return qdztmc;
    }

    public void setQdztmc(String qdztmc) {
        this.qdztmc = qdztmc;
    }

    public List<String> getQdztids() {
        return qdztids;
    }

    public void setQdztids(List<String> qdztids) {
        this.qdztids = qdztids;
    }

    public List<FjcfbDto> getFjcfbDtos() {
        return fjcfbDtos;
    }

    public void setFjcfbDtos(List<FjcfbDto> fjcfbDtos) {
        this.fjcfbDtos = fjcfbDtos;
    }

    public List<String> getHtflids() {
        return htflids;
    }

    public void setHtflids(List<String> htflids) {
        this.htflids = htflids;
    }

    public List<String> getHtzflids() {
        return htzflids;
    }

    public void setHtzflids(List<String> htzflids) {
        this.htzflids = htzflids;
    }

    public List<SwhtmxDto> getHtmxList() {
        return htmxList;
    }

    public void setHtmxList(List<SwhtmxDto> htmxList) {
        this.htmxList = htmxList;
    }

    public String get_key() {
        return _key;
    }

    public void set_key(String _key) {
        this._key = _key;
    }
	    public String getSflxmc() {
        return sflxmc;
    }

    public void setSflxmc(String sflxmc) {
        this.sflxmc = sflxmc;
    }

    public String getHtzflmc() {
        return htzflmc;
    }

    public void setHtzflmc(String htzflmc) {
        this.htzflmc = htzflmc;
    }

    public String getSqlParam() {
        return SqlParam;
    }

    public void setSqlParam(String sqlParam) {
        SqlParam = sqlParam;
    }

    public List<String> getKhids() {
        return khids;
    }

    public void setKhids(List<String> khids) {
        this.khids = khids;
    }

    public List<String> getChapterFjids() {
        return chapterFjids;
    }

    public void setChapterFjids(List<String> chapterFjids) {
        this.chapterFjids = chapterFjids;
    }

    public String getDdbj() {
        return ddbj;
    }

    public void setDdbj(String ddbj) {
        this.ddbj = ddbj;
    }

    public String getSprddid() {
        return sprddid;
    }

    public void setSprddid(String sprddid) {
        this.sprddid = sprddid;
    }

    public String getSprid() {
        return sprid;
    }

    public void setSprid(String sprid) {
        this.sprid = sprid;
    }

    public String getSprxm() {
        return sprxm;
    }

    public void setSprxm(String sprxm) {
        this.sprxm = sprxm;
    }

    public String getSpryhm() {
        return spryhm;
    }

    public void setSpryhm(String spryhm) {
        this.spryhm = spryhm;
    }

    public String getSprjsid() {
        return sprjsid;
    }

    public void setSprjsid(String sprjsid) {
        this.sprjsid = sprjsid;
    }

    public String getSprjsmc() {
        return sprjsmc;
    }

    public void setSprjsmc(String sprjsmc) {
        this.sprjsmc = sprjsmc;
    }

    public List<String> getHtzts() {
        return htzts;
    }

    public void setHtzts(List<String> htzts) {
        this.htzts = htzts;
    }
    public List<String> getFjids() {
        return fjids;
    }

    public void setFjids(List<String> fjids) {
        this.fjids = fjids;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getHtksrqstart() {
        return htksrqstart;
    }

    public void setHtksrqstart(String htksrqstart) {
        this.htksrqstart = htksrqstart;
    }

    public String getHtksrqend() {
        return htksrqend;
    }

    public void setHtksrqend(String htksrqend) {
        this.htksrqend = htksrqend;
    }

    public String getHtjsrqstart() {
        return htjsrqstart;
    }

    public void setHtjsrqstart(String htjsrqstart) {
        this.htjsrqstart = htjsrqstart;
    }

    public String getHtjsrqend() {
        return htjsrqend;
    }

    public void setHtjsrqend(String htjsrqend) {
        this.htjsrqend = htjsrqend;
    }

    public String getKhmc() {
        return khmc;
    }

    public void setKhmc(String khmc) {
        this.khmc = khmc;
    }

    public String getHtflmc() {
        return htflmc;
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    public void setHtflmc(String htflmc) {
        this.htflmc = htflmc;
    }

    public String getHtfxmc() {
        return htfxmc;
    }

    public void setHtfxmc(String htfxmc) {
        this.htfxmc = htfxmc;
    }

    public String getXslbmc() {
        return xslbmc;
    }

    public void setXslbmc(String xslbmc) {
        this.xslbmc = xslbmc;
    }

    public String getYzlxmc() {
        return yzlxmc;
    }

    public void setYzlxmc(String yzlxmc) {
        this.yzlxmc = yzlxmc;
    }
}

