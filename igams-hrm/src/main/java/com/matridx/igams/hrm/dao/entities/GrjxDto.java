package com.matridx.igams.hrm.dao.entities;

import com.matridx.springboot.util.base.StringUtil;
import org.apache.ibatis.type.Alias;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author:JYK
 */
@Alias("GrjxDto")
public class GrjxDto extends GrjxModel{
    //区分绩效
    private String flag;
    private String jgid;
    private String sqlParam;
    //适用范围json
    private String syfw_json;
    //区分普通导出和详细导出
    private String dcFlag;
    private String ryid;
    private String[] khlxs;//考核类型多
    private String khzqstart;
    private String khzqend;
    private List<String> mbszids;
    private String zsxm;
    //模板名称
    private String mbmc;
    private List<String> zts;
	private String[] nfs;//年份
    private String[] yfs;//月份
    private String ddid;//钉钉id
    private String mbzf;//模板总分
    private String zqsj;//周期时间
    private String xm;//姓名
    private String khzqts;//周期天数
    private String shlb;//审核类别
    private String khzqmc;//考核周期名称
    private String[] bms;//部门
    private String[] mbjbs;//模板级别s
    private String khfsmin;//考核分数小
    private String khfsmax;//考核分数大
    private String entire;//查询
    private String khrymc;//考核人员名称
    private String bmmc;//部门
    private String fs;//分数
    private String khzb;//考核指标
    private String sm;//评分标准和考核细则
    private String qz;//权重
    private String gwmc;//岗位名称
    private String pfr;//评分人
    private String zj;//总结
    private String jxtzsj;//绩效通知上级
    private String yhm;//用户名
    private String syrq;//使用日期
    private String zdrymc;//模板制定人
    private String chbj;//撤回标记
    private String _key;
    private String sxlx;
    private String cj;
    private List<String> ryids;
    //自评分数
    private String zpfs;
    //上级评分
    private String sjpf;

    private List<String> ids;
    private String performanceresult;
    private String feedbackrecord;

    public String getPerformanceresult() {
        return performanceresult;
    }

    public void setPerformanceresult(String performanceresult) {
        this.performanceresult = performanceresult;
    }

    public String getFeedbackrecord() {
        return feedbackrecord;
    }

    public void setFeedbackrecord(String feedbackrecord) {
        this.feedbackrecord = feedbackrecord;
    }

    public String getZpfs() {
        return zpfs;
    }

    public void setZpfs(String zpfs) {
        this.zpfs = zpfs;
    }

    public String getSjpf() {
        return sjpf;
    }

    public void setSjpf(String sjpf) {
        this.sjpf = sjpf;
    }

    public List<String> getRyids() {
        return ryids;
    }

    public void setRyids(List<String> ryids) {
        this.ryids = ryids;
    }

    public String getSxlx() {
        return sxlx;
    }

    public void setSxlx(String sxlx) {
        this.sxlx = sxlx;
    }

    public String getCj() {
        return cj;
    }

    public void setCj(String cj) {
        this.cj = cj;
    }

    public String get_key() {
        return _key;
    }

    public void set_key(String _key) {
        this._key = _key;
    }

    public String getChbj() {
        return chbj;
    }

    public void setChbj(String chbj) {
        this.chbj = chbj;
    }

    public String getSyrq() {
        return syrq;
    }

    public void setSyrq(String syrq) {
        this.syrq = syrq;
    }

    public String getZdrymc() {
        return zdrymc;
    }

    public void setZdrymc(String zdrymc) {
        this.zdrymc = zdrymc;
    }

    public String getYhm() {
        return yhm;
    }

    public void setYhm(String yhm) {
        this.yhm = yhm;
    }

    public String getJxtzsj() {
        return jxtzsj;
    }

    public void setJxtzsj(String jxtzsj) {
        this.jxtzsj = jxtzsj;
    }

    public String getGwmc() {
        return gwmc;
    }

    public void setGwmc(String gwmc) {
        this.gwmc = gwmc;
    }

    public String getPfr() {
        return pfr;
    }

    public void setPfr(String pfr) {
        this.pfr = pfr;
    }

    public String getZj() {
        return zj;
    }

    public void setZj(String zj) {
        this.zj = zj;
    }

    public String getFs() {
        return fs;
    }

    public void setFs(String fs) {
        this.fs = fs;
    }

    public String getKhzb() {
        return khzb;
    }

    public void setKhzb(String khzb) {
        this.khzb = khzb;
    }

    public String getSm() {
        return sm;
    }

    public void setSm(String sm) {
        this.sm = sm;
    }

    @Override
    public String getQz() {
        return qz;
    }

    @Override
    public void setQz(String qz) {
        this.qz = qz;
    }

    public String getBmmc() {
        return bmmc;
    }

    public void setBmmc(String bmmc) {
        this.bmmc = bmmc;
    }

    public void setZts(List<String> zts) {
        this.zts = zts;
    }

    public String getKhrymc() {
        return khrymc;
    }

    public void setKhrymc(String khrymc) {
        this.khrymc = khrymc;
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    public String[] getMbjbs() {
        return mbjbs;
    }

    public void setMbjbs(String[] mbjbs) {
        this.mbjbs = mbjbs;
    }

    public String getKhfsmin() {
        return khfsmin;
    }

    public void setKhfsmin(String khfsmin) {
        this.khfsmin = khfsmin;
    }

    public String getKhfsmax() {
        return khfsmax;
    }

    public void setKhfsmax(String khfsmax) {
        this.khfsmax = khfsmax;
    }

    public String[] getBms() {
        return bms;
    }

    public void setBms(String[] bms) {
        this.bms = bms;
    }

    public String getKhzqmc() {
        return khzqmc;
    }

    public void setKhzqmc(String khzqmc) {
        this.khzqmc = khzqmc;
    }

    public String getShlb() {
        return shlb;
    }

    public void setShlb(String shlb) {
        this.shlb = shlb;
    }

    public String getKhzqts() {
        return khzqts;
    }

    public void setKhzqts(String khzqts) {
        this.khzqts = khzqts;
    }

    public String getZqsj() {
        return zqsj;
    }

    public void setZqsj(String zqsj) {
        this.zqsj = zqsj;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getMbzf() {
        return mbzf;
    }

    public void setMbzf(String mbzf) {
        this.mbzf = mbzf;
    }

    public String getDdid() {
        return ddid;
    }

    public void setDdid(String ddid) {
        this.ddid = ddid;
    }

    public String[] getNfs() {
        return nfs;
    }

    public void setNfs(String[] nfs) {
        this.nfs = nfs;
    }

    public String[] getYfs() {
        return yfs;
    }

    public void setYfs(String[] yfs) {
        this.yfs = yfs;
    }

 //修改人员名称
    private String xgrymc;
    //自评
    private String zp;
    private String pf_json;//评分信息json
    private String jxshid;//绩效审核id
    private String jxmbid;//绩效模板id
    public List<String> getZts() {
        return zts;
    }

    public void setZts(String  zts) {
        List<String> list = new ArrayList<String>();
        if(StringUtil.isNotBlank(zts)) {
            String[] str = zts.split(",");
            list = Arrays.asList(str);
        }
        this.zts = list;
    }

    @Override
    public String getJxmbid() {
        return jxmbid;
    }

    @Override
    public void setJxmbid(String jxmbid) {
        this.jxmbid = jxmbid;
    }

    public String getJxshid() {
        return jxshid;
    }

    public void setJxshid(String jxshid) {
        this.jxshid = jxshid;
    }

    public String getPf_json() {
        return pf_json;
    }

    public void setPf_json(String pf_json) {
        this.pf_json = pf_json;
    }

    public String getZp() {
        return zp;
    }

    public void setZp(String zp) {
        this.zp = zp;
    }

    public String getXgrymc() {
        return xgrymc;
    }

    public void setXgrymc(String xgrymc) {
        this.xgrymc = xgrymc;
    }

    public String getMbmc() {
        return mbmc;
    }

    public void setMbmc(String mbmc) {
        this.mbmc = mbmc;
    }

    public String getZsxm() {
        return zsxm;
    }

    public void setZsxm(String zsxm) {
        this.zsxm = zsxm;
    }

    public List<String> getMbszids() {
        return mbszids;
    }

    public void setMbszids(List<String> mbszids) {
        this.mbszids = mbszids;
    }

    public String getKhzqstart() {
        return khzqstart;
    }

    public void setKhzqstart(String khzqstart) {
        this.khzqstart = khzqstart;
    }

    public String getKhzqend() {
        return khzqend;
    }

    public void setKhzqend(String khzqend) {
        this.khzqend = khzqend;
    }

    public String[] getKhlxs() {
        return khlxs;
    }

    public void setKhlxs(String[] khlxs) {
        this.khlxs = khlxs;
    }

    public String getRyid() {
        return ryid;
    }

    public void setRyid(String ryid) {
        this.ryid = ryid;
    }

    public String getDcFlag() {
        return dcFlag;
    }

    public void setDcFlag(String dcFlag) {
        this.dcFlag = dcFlag;
    }

    public String getSyfw_json() {
        return syfw_json;
    }

    public void setSyfw_json(String syfw_json) {
        this.syfw_json = syfw_json;
    }

    public String getSqlParam() {
        return sqlParam;
    }

    public void setSqlParam(String sqlParam) {
        this.sqlParam = sqlParam;
    }

    public String getJgid() {
        return jgid;
    }

    public void setJgid(String jgid) {
        this.jgid = jgid;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public List<String> getIds() {
        return ids;
    }

    @Override
    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
