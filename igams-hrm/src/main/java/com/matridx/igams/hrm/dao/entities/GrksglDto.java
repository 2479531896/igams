package com.matridx.igams.hrm.dao.entities;

import com.matridx.springboot.util.base.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.type.Alias;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Alias(value="GrksglDto")
public class GrksglDto extends GrksglModel {
    //姓名
    private String xm;
    //培训标题
    private String pxbt;
    //测试总分
    private String cszf;
    //测试时长
    private String cssc;
    //测试题数
    private String csts;
    //剩余时长
    private String sysc;
    //超时标记
    private String csbj;
    //钉钉id
    private String ddid;

    //查看答案标记
    private String ckdabj;
    //当场重考标记
    private String dcckbj;
    //回退标记
    private String htbj;
    //跳转方式标记
    private String tzfsbj;
    //通过标记
    private String tgbj;
    //考试次数
    private String kscs;
    //所属机构
    private String jgmc;
    //个人考试明细
    private List<GrksmxDto> grksmxDtos;
    //简答题标记
    private String jdflg;
    //存放请求
    private HttpServletRequest request;
    //考试开始时间
    private String kssjstart;
    //考试结束时间
    private String kssjend;
    //最低分
    private String minscore;
    //最高分
    private String maxscore;
    //确认人
    private String qrry;
    //培训类别
    private String pxlb;
    //培训类别名称
    private String pxlbmc;
    //培训类别多
    private String[] pxlbs;
    //培训子类别多
    private String[] pxzlbs;
    private String entire;
    private String[] ssgss;
    private String sqlParam;
    //所属公司
    private String ssgs;
    //复数IDS
    private List<String> idss;
    private String string_agg;
    private String dqsj;
    //所属公司
    private String ssgsmc;
    //主键id
    private String zjid;
    //是否合格多
    private String[] sfhgs;
    //是否合格
    private String sfhg;
    //是否测试多
    private String[] sfcss;
    //是否测试
    private String sfcsmc;
    //负责人名称
    private String fzrmc;
    //确认人名称
    private String qrrymc;
    //用户名
    private String yhm;
    //金额
    private String je;
    //红包名称
    private String hbmc;
    //是否发送红包
    private String sffshb;
    private String[] sffshbs;
    //是否发送
    private String sfff;
    private String[] sfffs;
    //钉钉头像路径
    private String ddtxlj;
    //排名
    private String pm;
    //通知人员
    private String tzry_json;
    //复数PXID
    private List<String> pxids;
    //视频标记
    private String spbj;

    public String getSpbj() {
        return spbj;
    }

    public void setSpbj(String spbj) {
        this.spbj = spbj;
    }

    public List<String> getPxids() {
        return pxids;
    }
    public void setPxids(String pxids) {
        List<String> list = new ArrayList<String>();
        if(StringUtil.isNotBlank(pxids)) {
            String[] str = pxids.split(",");
            list = Arrays.asList(str);
        }
        this.pxids = list;
    }
    public void setPxids(List<String> pxids) {
        if(!CollectionUtils.isEmpty(pxids)){
            for(int i=0;i<pxids.size();i++){
                pxids.set(i,pxids.get(i).replace("[", "").replace("]", "").trim());
            }
        }
        this.pxids = pxids;
    }

    public String getTzry_json() {
        return tzry_json;
    }

    public void setTzry_json(String tzry_json) {
        this.tzry_json = tzry_json;
    }

    public String getPm() {
        return pm;
    }

    public void setPm(String pm) {
        this.pm = pm;
    }

    public String getDdtxlj() {
        return ddtxlj;
    }

    public void setDdtxlj(String ddtxlj) {
        this.ddtxlj = ddtxlj;
    }

    public String[] getSfffs() {
        return sfffs;
    }

    public void setSfffs(String[] sfffs) {
        this.sfffs = sfffs;
    }

    public String getSfff() {
        return sfff;
    }

    public void setSfff(String sfff) {
        this.sfff = sfff;
    }

    public String[] getSffshbs() {
        return sffshbs;
    }

    public void setSffshbs(String[] sffshbs) {
        this.sffshbs = sffshbs;
    }

    public String getSffshb() {
        return sffshb;
    }

    public void setSffshb(String sffshb) {
        this.sffshb = sffshb;
    }

    public String getYhm() {
        return yhm;
    }

    public void setYhm(String yhm) {
        this.yhm = yhm;
    }

    public String getJe() {
        return je;
    }

    public void setJe(String je) {
        this.je = je;
    }

    public String getHbmc() {
        return hbmc;
    }

    public void setHbmc(String hbmc) {
        this.hbmc = hbmc;
    }

    public String getPxlbmc() {
        return pxlbmc;
    }

    public void setPxlbmc(String pxlbmc) {
        this.pxlbmc = pxlbmc;
    }

    public String getQrrymc() {
        return qrrymc;
    }

    public void setQrrymc(String qrrymc) {
        this.qrrymc = qrrymc;
    }

    public String getFzrmc() {
        return fzrmc;
    }

    public void setFzrmc(String fzrmc) {
        this.fzrmc = fzrmc;
    }

    public String getSfcsmc() {
        return sfcsmc;
    }

    public void setSfcsmc(String sfcsmc) {
        this.sfcsmc = sfcsmc;
    }

    public String[] getSfcss() {
        return sfcss;
    }

    public void setSfcss(String[] sfcss) {
        this.sfcss = sfcss;
    }

    public String getSfhg() {
        return sfhg;
    }

    public void setSfhg(String sfhg) {
        this.sfhg = sfhg;
    }

    public String[] getSfhgs() {
        return sfhgs;
    }

    public void setSfhgs(String[] sfhgs) {
        this.sfhgs = sfhgs;
    }

    public String getZjid() {
        return zjid;
    }

    public void setZjid(String zjid) {
        this.zjid = zjid;
    }

    public String getSsgsmc() {
        return ssgsmc;
    }

    public void setSsgsmc(String ssgsmc) {
        this.ssgsmc = ssgsmc;
    }

    public String getDqsj() {
        return dqsj;
    }

    public void setDqsj(String dqsj) {
        this.dqsj = dqsj;
    }

    public String getString_agg() {
        return string_agg;
    }

    public void setString_agg(String string_agg) {
        this.string_agg = string_agg;
    }

    public List<String> getIdss() {
        return idss;
    }

    public void setIdss(List<String> idss) {
        this.idss = idss;
    }

    public String getSsgs() {
        return ssgs;
    }

    public void setSsgs(String ssgs) {
        this.ssgs = ssgs;
    }

    public String getSqlParam() {
        return sqlParam;
    }

    public void setSqlParam(String sqlParam) {
        this.sqlParam = sqlParam;
    }

    public String[] getSsgss() {
        return ssgss;
    }

    public void setSsgss(String[] ssgss) {
        this.ssgss = ssgss;
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    public String getPxlb() {
        return pxlb;
    }

    public void setPxlb(String pxlb) {
        this.pxlb = pxlb;
    }

    public String[] getPxlbs() {
        return pxlbs;
    }

    public void setPxlbs(String[] pxlbs) {
        this.pxlbs = pxlbs;
    }

    public String[] getPxzlbs() {
        return pxzlbs;
    }

    public void setPxzlbs(String[] pxzlbs) {
        this.pxzlbs = pxzlbs;
    }

    public String getQrry() {
        return qrry;
    }

    public void setQrry(String qrry) {
        this.qrry = qrry;
    }

    public String getMinscore() {
        return minscore;
    }

    public void setMinscore(String minscore) {
        this.minscore = minscore;
    }

    public String getMaxscore() {
        return maxscore;
    }

    public void setMaxscore(String maxscore) {
        this.maxscore = maxscore;
    }

    public String getKssjstart() {
        return kssjstart;
    }

    public void setKssjstart(String kssjstart) {
        this.kssjstart = kssjstart;
    }

    public String getKssjend() {
        return kssjend;
    }

    public void setKssjend(String kssjend) {
        this.kssjend = kssjend;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public String getJdflg() {
        return jdflg;
    }

    public void setJdflg(String jdflg) {
        this.jdflg = jdflg;
    }

    public List<GrksmxDto> getGrksmxDtos() {
        return grksmxDtos;
    }

    public void setGrksmxDtos(List<GrksmxDto> grksmxDtos) {
        this.grksmxDtos = grksmxDtos;
    }

    public String getJgmc() {
        return jgmc;
    }

    public void setJgmc(String jgmc) {
        this.jgmc = jgmc;
    }

    public String getKscs() {
        return kscs;
    }

    public void setKscs(String kscs) {
        this.kscs = kscs;
    }

    public String getTgbj() {
        return tgbj;
    }

    public void setTgbj(String tgbj) {
        this.tgbj = tgbj;
    }

    public String getHtbj() {
        return htbj;
    }

    public void setHtbj(String htbj) {
        this.htbj = htbj;
    }

    public String getTzfsbj() {
        return tzfsbj;
    }

    public void setTzfsbj(String tzfsbj) {
        this.tzfsbj = tzfsbj;
    }

    public String getCkdabj() {
        return ckdabj;
    }

    public void setCkdabj(String ckdabj) {
        this.ckdabj = ckdabj;
    }

    public String getDcckbj() {
        return dcckbj;
    }

    public void setDcckbj(String dcckbj) {
        this.dcckbj = dcckbj;
    }



    public String getDdid() {
        return ddid;
    }

    public void setDdid(String ddid) {
        this.ddid = ddid;
    }

    public String getCsbj() {
        return csbj;
    }

    public void setCsbj(String csbj) {
        this.csbj = csbj;
    }

    public String getSysc() {
        return sysc;
    }

    public void setSysc(String sysc) {
        this.sysc = sysc;
    }

    public String getCszf() {
        return cszf;
    }

    public void setCszf(String cszf) {
        this.cszf = cszf;
    }

    public String getCssc() {
        return cssc;
    }

    public void setCssc(String cssc) {
        this.cssc = cssc;
    }

    public String getCsts() {
        return csts;
    }

    public void setCsts(String csts) {
        this.csts = csts;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getPxbt() {
        return pxbt;
    }

    public void setPxbt(String pxbt) {
        this.pxbt = pxbt;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
