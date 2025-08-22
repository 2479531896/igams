package com.matridx.igams.hrm.dao.entities;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.ibatis.type.Alias;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author:JYK
 */
@Alias(value = "YghmcDto")
public class YghmcDto extends YghmcModel{
    //员工编号
    private String ygbm;
    //姓名
    private String xm;
    //部门
    private String bm;
    //入职日期始
    private String rzrqstart;
    //入职日期末
    private String rzrqend;
    //转正日期始
    private String zzrqstart;
    //转正日期末
    private String zzrqend;
    //参加工作时间始
    private String cjgzrqstart;
    //参加工作时间末
    private String cjgzrqend;
    //首次合同起始日始
    private String schtqsrqstart;
    //首次合同起始日末
    private String schtqsrqend;
    //首次合同到期日始
    private String schtdqrqstart;
    //首次合同到期日末
    private String schtdqrqend;
    //离职日期始
    private String lzrqstart;
    //离职日期末
    private String lzrqend;
    //岗位名称
    private String gwmc;
    private String entire;
    private String[] sfqdbmxys;
    private String[] sflzs;
    private String sqlParam;
    private String ddid;
    private String yhm;
    //合同到期日至今天的日期差
    private String rqc;
    //分布式标记
    private String prefix;
    private String jgid;
    private String jgmc;
    private String[] bms;
    //员工合同起始日
    private String htqsr;
    //员工合同到期日
    private String htdqr;
    //员工htid
    private String yghtid;
    //员工离职id
    private String yglzid;
    //员工离职日期
    private String yglzrq;
    //员工入职日期
    private String ygrzrq;
    private String fl;
    private String[] ssgss;
    //用户名或真实姓名
    private String yhmorzsxm;
    //真实姓名
    private String zsxm;
    private String jqxz;//假期限制
    private String gznx;//工作年限
    private List<PxglDto> pxglDtos;//培训档案
    private List<String> gzids;//工作ids

    private String dqy;//当前月

    private String cskz1;
    private String jlbh;

    public String getJlbh() {
        return jlbh;
    }

    public void setJlbh(String jlbh) {
        this.jlbh = jlbh;
    }

    public String getCskz1() {
        return cskz1;
    }

    public void setCskz1(String cskz1) {
        this.cskz1 = cskz1;
    }

    public List<String> getGzids() {
        return gzids;
    }
    public void setGzids(String gzids) {
        List<String> list = new ArrayList<>();
        if(StringUtil.isNotBlank(gzids)) {
            String[] str = gzids.split(",");
            list = Arrays.asList(str);
        }
        this.gzids = list;
    }
    public void setGzids(List<String> gzids) {
        this.gzids = gzids;
    }
    public List<PxglDto> getPxglDtos() {
        return pxglDtos;
    }

    public void setPxglDtos(List<PxglDto> pxglDtos) {
        this.pxglDtos = pxglDtos;
    }

    public String getGznx() {
        return gznx;
    }

    public void setGznx(String gznx) {
        this.gznx = gznx;
    }

    public String getJqxz() {
        return jqxz;
    }

    public void setJqxz(String jqxz) {
        this.jqxz = jqxz;
    }

    public String getZsxm() {
        return zsxm;
    }

    public void setZsxm(String zsxm) {
        this.zsxm = zsxm;
    }

    public String getYhmorzsxm() {
        return yhmorzsxm;
    }

    public void setYhmorzsxm(String yhmorzsxm) {
        this.yhmorzsxm = yhmorzsxm;
    }

    public String[] getSsgss() {
        return ssgss;
    }

    public void setSsgss(String[] ssgss) {
        this.ssgss = ssgss;
    }

    public String getFl() {
        return fl;
    }

    public void setFl(String fl) {
        this.fl = fl;
    }

    public String getYglzrq() {
        return yglzrq;
    }

    public void setYglzrq(String yglzrq) {
        this.yglzrq = yglzrq;
    }

    public String getYgrzrq() {
        return ygrzrq;
    }

    public void setYgrzrq(String ygrzrq) {
        this.ygrzrq = ygrzrq;
    }

    public String getHtqsr() {
        return htqsr;
    }

    public void setHtqsr(String htqsr) {
        this.htqsr = htqsr;
    }

    public String getHtdqr() {
        return htdqr;
    }

    public void setHtdqr(String htdqr) {
        this.htdqr = htdqr;
    }

    public String getYghtid() {
        return yghtid;
    }

    public void setYghtid(String yghtid) {
        this.yghtid = yghtid;
    }

    public String getYglzid() {
        return yglzid;
    }

    public void setYglzid(String yglzid) {
        this.yglzid = yglzid;
    }

    public String[] getBms() {
        return bms;
    }

    public void setBms(String[] bms) {
        this.bms = bms;
    }

    public String getJgid() {
        return jgid;
    }

    public void setJgid(String jgid) {
        this.jgid = jgid;
    }

    public String getJgmc() {
        return jgmc;
    }

    public void setJgmc(String jgmc) {
        this.jgmc = jgmc;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getRqc() {
        return rqc;
    }

    public void setRqc(String rqc) {
        this.rqc = rqc;
    }

    public String getYhm() {
        return yhm;
    }

    public void setYhm(String yhm) {
        this.yhm = yhm;
    }

    public String getDdid() {
        return ddid;
    }

    public void setDdid(String ddid) {
        this.ddid = ddid;
    }

    public String getSqlParam() {
        return sqlParam;
    }

    public void setSqlParam(String sqlParam) {
        this.sqlParam = sqlParam;
    }

    public String[] getSfqdbmxys() {
        return sfqdbmxys;
    }

    public void setSfqdbmxys(String[] sfqdbmxys) {
        this.sfqdbmxys = sfqdbmxys;
    }

    public String[] getSflzs() {
        return sflzs;
    }

    public void setSflzs(String[] sflzs) {
        this.sflzs = sflzs;
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    public String getRzrqstart() {
        return rzrqstart;
    }

    public void setRzrqstart(String rzrqstart) {
        this.rzrqstart = rzrqstart;
    }

    public String getRzrqend() {
        return rzrqend;
    }

    public void setRzrqend(String rzrqend) {
        this.rzrqend = rzrqend;
    }

    public String getZzrqstart() {
        return zzrqstart;
    }

    public void setZzrqstart(String zzrqstart) {
        this.zzrqstart = zzrqstart;
    }

    public String getZzrqend() {
        return zzrqend;
    }

    public void setZzrqend(String zzrqend) {
        this.zzrqend = zzrqend;
    }

    public String getCjgzrqstart() {
        return cjgzrqstart;
    }

    public void setCjgzrqstart(String cjgzrqstart) {
        this.cjgzrqstart = cjgzrqstart;
    }

    public String getCjgzrqend() {
        return cjgzrqend;
    }

    public void setCjgzrqend(String cjgzrqend) {
        this.cjgzrqend = cjgzrqend;
    }

    public String getSchtqsrqstart() {
        return schtqsrqstart;
    }

    public void setSchtqsrqstart(String schtqsrqstart) {
        this.schtqsrqstart = schtqsrqstart;
    }

    public String getSchtqsrqend() {
        return schtqsrqend;
    }

    public void setSchtqsrqend(String schtqsrqend) {
        this.schtqsrqend = schtqsrqend;
    }

    public String getSchtdqrqstart() {
        return schtdqrqstart;
    }

    public void setSchtdqrqstart(String schtdqrqstart) {
        this.schtdqrqstart = schtdqrqstart;
    }

    public String getSchtdqrqend() {
        return schtdqrqend;
    }

    public void setSchtdqrqend(String schtdqrqend) {
        this.schtdqrqend = schtdqrqend;
    }

    public String getLzrqstart() {
        return lzrqstart;
    }

    public void setLzrqstart(String lzrqstart) {
        this.lzrqstart = lzrqstart;
    }

    public String getLzrqend() {
        return lzrqend;
    }

    public void setLzrqend(String lzrqend) {
        this.lzrqend = lzrqend;
    }

    public String getYgbm() {
        return ygbm;
    }

    public void setYgbm(String ygbm) {
        this.ygbm = ygbm;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getBm() {
        return bm;
    }

    public void setBm(String bm) {
        this.bm = bm;
    }

    public String getGwmc() {
        return gwmc;
    }

    public void setGwmc(String gwmc) {
        this.gwmc = gwmc;
    }

    private static final long serialVersionUID = 1L;

    public String getDqy() {
        return dqy;
    }

    public void setDqy(String dqy) {
        this.dqy = dqy;
    }
}
