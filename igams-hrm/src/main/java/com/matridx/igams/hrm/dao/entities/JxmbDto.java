package com.matridx.igams.hrm.dao.entities;

import com.matridx.springboot.util.base.StringUtil;
import org.apache.ibatis.type.Alias;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author:JYK
 */
@Alias("JxmbDto")
public class JxmbDto extends JxmbModel {
    private static final long serialVersionUID = 1L;
    private String sqlParam;
    private String bmmc;//部门名称
    private String khlxmc;//考核类型名称
    private String flag;
    private String checkflag;
    private String jgid;
    private String yhid;
    private String ryid;
    private String entire;
    private String xm;
    private List<String> khlxs;
    private List<String> bms;
    private List<String> zts;
    private String sxsjstart;
    private String sxsjend;
    private String yxqstart;
    private String yxqend;
    private String jxmbmx_json;
    //考核周期名称
    private String khzqmc;
    //考核周期参数扩展1
    private String khzqcskz1;
    //考核周期参数扩展2
    private String khzqcskz2;
    //生效日期
    private String sxrq;
    //失效日期
    private String yxq;
    //自评
    private String zp;
    //已发放日期
    private String yffrq;
    //发放日期
    // private String ffrq;
    //发放考核月
    private String ffkhy;
    //模板设置id
    private String mbszid;
    //绩效提醒
    // private String jxtx;
    //自动提交
    private String zdtj;
    //截止日期
    // private String jzrq;
	    //资源id
    private String zyid;
	    //角色id
    private String jsid;
    private String czsm;//操作代码
    private String czdm;//操作说明
    private String czmc;//操作名称
	private String dyym;//
    private List<String> splxs;//审核类型
	private String jxshid;
	private String lrsjstart;
	private String lrsjend;
	//模板审核ID
    private String mbshid;
    private String shlb;
    private String signal;
    private String khzb;//考核指标
    private String zbdj;//指标等级
    private String sm;//说明
    private String xh;//序号
    private String fs;//分数
    private String jgmc;//部门名称
    private List<String> khzqs;
    private String lrrymc;//录入人员名称
    private String mbtzsj;//模板通知上级
    private String zqkssj;//周期开始时间
    private String zqjssj;//周期结束时间
    private String txjb;//提醒级别
    private String yhm;//用户名
    private String mbzlxcskz1;//模板类型参数扩展
    //模板类型名称
    private String mblxmc;
    //模板子类型名称
    private String mbzlxmc;
    //发放日期新
    private String xffrq;
    //截止日期新
    private String xjzrq;
    //绩效提醒新
    private String xjxtx;
    private String batchFlag;//批量发放标记
    //适用范围json
    private String syfw_json;
    private String _key;
    private String jzyxq;//截止有效期
    private String sxlx;
    private String cj;
    private List<String> ryids;
//分数限制
    private String fsxz;

    public String getCheckflag() {
        return checkflag;
    }

    public void setCheckflag(String checkflag) {
        this.checkflag = checkflag;
    }

    public String getFsxz() {
        return fsxz;
    }

    public void setFsxz(String fsxz) {
        this.fsxz = fsxz;
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

    public String getJzyxq() {
        return jzyxq;
    }

    public void setJzyxq(String jzyxq) {
        this.jzyxq = jzyxq;
    }

    public String get_key() {
        return _key;
    }

    public void set_key(String _key) {
        this._key = _key;
    }

    public String getSyfw_json() {
        return syfw_json;
    }

    public void setSyfw_json(String syfw_json) {
        this.syfw_json = syfw_json;
    }

    public String getBatchFlag() {
        return batchFlag;
    }

    public void setBatchFlag(String batchFlag) {
        this.batchFlag = batchFlag;
    }
    public String getXffrq() {
        return xffrq;
    }

    public void setXffrq(String xffrq) {
        this.xffrq = xffrq;
    }

    public String getXjzrq() {
        return xjzrq;
    }

    public void setXjzrq(String xjzrq) {
        this.xjzrq = xjzrq;
    }

    public String getXjxtx() {
        return xjxtx;
    }

    public void setXjxtx(String xjxtx) {
        this.xjxtx = xjxtx;
    }

    public String getMbzlxmc() {
        return mbzlxmc;
    }

    public void setMbzlxmc(String mbzlxmc) {
        this.mbzlxmc = mbzlxmc;
    }
    public String getMblxmc() {
        return mblxmc;
    }

    public void setMblxmc(String mblxmc) {
        this.mblxmc = mblxmc;
    }

    public String getMbzlxcskz1() {
        return mbzlxcskz1;
    }

    public void setMbzlxcskz1(String mbzlxcskz1) {
        this.mbzlxcskz1 = mbzlxcskz1;
    }

    public String getYhm() {
        return yhm;
    }

    public void setYhm(String yhm) {
        this.yhm = yhm;
    }

    public String getTxjb() {
        return txjb;
    }

    public void setTxjb(String txjb) {
        this.txjb = txjb;
    }

    public String getZqkssj() {
        return zqkssj;
    }

    public void setZqkssj(String zqkssj) {
        this.zqkssj = zqkssj;
    }

    public String getZqjssj() {
        return zqjssj;
    }

    public void setZqjssj(String zqjssj) {
        this.zqjssj = zqjssj;
    }

    public String getMbtzsj() {
        return mbtzsj;
    }

    public void setMbtzsj(String mbtzsj) {
        this.mbtzsj = mbtzsj;
    }

    public String getLrrymc() {
        return lrrymc;
    }

    public void setLrrymc(String lrrymc) {
        this.lrrymc = lrrymc;
    }

    public String getJgmc() {
        return jgmc;
    }

    public void setJgmc(String jgmc) {
        this.jgmc = jgmc;
    }

    public String getKhzb() {
        return khzb;
    }

    public void setKhzb(String khzb) {
        this.khzb = khzb;
    }

    public String getZbdj() {
        return zbdj;
    }

    public void setZbdj(String zbdj) {
        this.zbdj = zbdj;
    }

    public String getSm() {
        return sm;
    }

    public void setSm(String sm) {
        this.sm = sm;
    }

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getFs() {
        return fs;
    }

    public void setFs(String fs) {
        this.fs = fs;
    }

    public String getSignal() {
        return signal;
    }

    public void setSignal(String signal) {
        this.signal = signal;
    }

    public String getShlb() {
        return shlb;
    }

    public void setShlb(String shlb) {
        this.shlb = shlb;
    }

    public String getMbshid() {
        return mbshid;
    }

    public void setMbshid(String mbshid) {
        this.mbshid = mbshid;
    }
	private List<JxmbmxDto> list;

    public List<JxmbmxDto> getList() {
        return list;
    }

    public void setList(List<JxmbmxDto> list) {
        this.list = list;
    }


    public String getLrsjstart() {
        return lrsjstart;
    }

    public void setLrsjstart(String lrsjstart) {
        this.lrsjstart = lrsjstart;
    }

    public String getLrsjend() {
        return lrsjend;
    }

    public void setLrsjend(String lrsjend) {
        this.lrsjend = lrsjend;
    }

    public String getJxshid() {
        return jxshid;
    }

    public void setJxshid(String jxshid) {
        this.jxshid = jxshid;
    }
    public void setKhlxs(String khlxs) {
        List<String> list = new ArrayList<String>();
        if(StringUtil.isNotBlank(khlxs)) {
            String[] str = khlxs.split(",");
            list = Arrays.asList(str);
        }
        this.khlxs = list;
    }

    public List<String> getBms() {
        return bms;
    }

    public void setBms(String bms) {
        List<String> list = new ArrayList<String>();
        if(StringUtil.isNotBlank(bms)) {
            String[] str = bms.split(",");
            list = Arrays.asList(str);
        }
        this.bms = list;
    }

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

    public List<String> getSplxs() {
        return splxs;
    }

    public void setSplxs(String splxs) {
        List<String> list = new ArrayList<String>();
        if(StringUtil.isNotBlank(splxs)) {
            String[] str = splxs.split(",");
            list = Arrays.asList(str);
        }
        this.splxs = list;
    }
    public List<String> getKhzqs() {
        return khzqs;
    }

    public void setKhzqs(String khzqs) {
        List<String> list = new ArrayList<String>();
        if(StringUtil.isNotBlank(khzqs)) {
            String[] str = khzqs.split(",");
            list = Arrays.asList(str);
        }
        this.khzqs = list;
    }
    public String getDyym() {
        return dyym;
    }

    public void setDyym(String dyym) {
        this.dyym = dyym;
    }

/*    public String getJxtx() {
        return jxtx;
    }

    public void setJxtx(String jxtx) {
        this.jxtx = jxtx;
    }*/

    public String getZdtj() {
        return zdtj;
    }

    public void setZdtj(String zdtj) {
        this.zdtj = zdtj;
    }

/*    public String getJzrq() {
        return jzrq;
    }

    public void setJzrq(String jzrq) {
        this.jzrq = jzrq;
    }*/

    public String getMbszid() {
        return mbszid;
    }

    public void setMbszid(String mbszid) {
        this.mbszid = mbszid;
    }

    public String getFfkhy() {
        return ffkhy;
    }

    public void setFfkhy(String ffkhy) {
        this.ffkhy = ffkhy;
    }

/*    public String getFfrq() {
        return ffrq;
    }

    public void setFfrq(String ffrq) {
        this.ffrq = ffrq;
    }*/

    public String getKhzqmc() {
        return khzqmc;
    }

    public void setKhzqmc(String khzqmc) {
        this.khzqmc = khzqmc;
    }

    public String getKhzqcskz1() {
        return khzqcskz1;
    }

    public void setKhzqcskz1(String khzqcskz1) {
        this.khzqcskz1 = khzqcskz1;
    }

    public String getKhzqcskz2() {
        return khzqcskz2;
    }

    public void setKhzqcskz2(String khzqcskz2) {
        this.khzqcskz2 = khzqcskz2;
    }

    public String getSxrq() {
        return sxrq;
    }

    public void setSxrq(String sxrq) {
        this.sxrq = sxrq;
    }

    public String getYxq() {
        return yxq;
    }

    public void setYxq(String yxq) {
        this.yxq = yxq;
    }

    public String getZp() {
        return zp;
    }

    public void setZp(String zp) {
        this.zp = zp;
    }

    public String getYffrq() {
        return yffrq;
    }

    public void setYffrq(String yffrq) {
        this.yffrq = yffrq;
    }

    public String getJxmbmx_json() {
        return jxmbmx_json;
    }

    public void setJxmbmx_json(String jxmbmx_json) {
        this.jxmbmx_json = jxmbmx_json;
    }

    public String getSxsjstart() {
        return sxsjstart;
    }

    public void setSxsjstart(String sxsjstart) {
        this.sxsjstart = sxsjstart;
    }

    public String getSxsjend() {
        return sxsjend;
    }

    public void setSxsjend(String sxsjend) {
        this.sxsjend = sxsjend;
    }

    public List<String> getKhlxs() {
        return khlxs;
    }

    public void setKhlxs(List<String> khlxs) {
        this.khlxs = khlxs;
    }

    public void setBms(List<String> bms) {
        this.bms = bms;
    }
    public void setKhzqs(List<String> khzqs) {
        this.khzqs = khzqs;
    }

    public void setZts(List<String> zts) {
        this.zts = zts;
    }

    public String getYxqstart() {
        return yxqstart;
    }

    public void setYxqstart(String yxqstart) {
        this.yxqstart = yxqstart;
    }

    public void setSplxs(List<String> splxs) {
        this.splxs = splxs;
    }

    public String getYxqend() {
        return yxqend;
    }

    public void setYxqend(String yxqend) {
        this.yxqend = yxqend;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    public String getRyid() {
        return ryid;
    }

    public void setRyid(String ryid) {
        this.ryid = ryid;
    }

    public String getJgid() {
        return jgid;
    }

    public void setJgid(String jgid) {
        this.jgid = jgid;
    }

    public String getYhid() {
        return yhid;
    }

    public void setYhid(String yhid) {
        this.yhid = yhid;
    }

    public String getBmmc() {
        return bmmc;
    }

    public void setBmmc(String bmmc) {
        this.bmmc = bmmc;
    }

    public String getKhlxmc() {
        return khlxmc;
    }

    public void setKhlxmc(String khlxmc) {
        this.khlxmc = khlxmc;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getSqlParam() {
        return sqlParam;
    }

    public void setSqlParam(String sqlParam) {
        this.sqlParam = sqlParam;
    }
	    public String getZyid() {
        return zyid;
    }

    public void setZyid(String zyid) {
        this.zyid = zyid;
    }

    public String getJsid() {
        return jsid;
    }

    public void setJsid(String jsid) {
        this.jsid = jsid;
    }
	    public String getCzsm() {
        return czsm;
    }

    public void setCzsm(String czsm) {
        this.czsm = czsm;
    }

    public String getCzdm() {
        return czdm;
    }

    public void setCzdm(String czdm) {
        this.czdm = czdm;
    }

    public String getCzmc() {
        return czmc;
    }

    public void setCzmc(String czmc) {
        this.czmc = czmc;
    }

}
