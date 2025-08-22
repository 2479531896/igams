package com.matridx.igams.hrm.dao.entities;

import com.matridx.igams.common.dao.entities.GzglDto;
import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="PxglDto")
public class PxglDto extends PxglModel {
    //附件id
    private String fjid;
    //培训进度
    private String pxjd;
    //工作ID
    private String gzid;
    //视频完成标记
    private String spwcbj;
    //测试总分
    private String cszf;
    //测试时长
    private String cssc;
    //测试题数
    private String csts;
    //通过分数
    private String tgfs;
    //单选题数
    private String danxts;
    //多选题数
    private String duoxts;
    //简答题数
    private String jdts;
    //填空题数
    private String tkts;
    //判断题数
    private String pdts;
    //题目类型
    private String tmlx;
    //题目序号
    private String xh;
    //题目内容
    private String tmnr;
    //分数
    private String fs;
    //答案
    private String da;
    //选项代码
    private String xxdm;
    //选项内容
    private String xxnr;
    //题目类型名称
    private String tmlxmc;
    //选项A
    private String xxA;
    //选项B
    private String xxB;
    //选项C
    private String xxC;
    //选项D
    private String xxD;
    //选项E
    private String xxE;
    //选项F
    private String xxF;
    //选项G
    private String xxG;
    //选项H
    private String xxH;
    //图片fjid
    private String tpfjid;
    //视频fjid
    private String spfjid;
    //视频标记
    private String[] spbjs;
    //是否测试
    private String[] sfcss;
    //合同标记
    private String[] htbjs;
    //跳转方式标记
    private String[] tzfsbjs;
    //参考答案
    private String[] ckdabjs;
    //当场重考
    private String[] dcckbjs;
    // 全部(查询条件)
    private String entire;
    //真实姓名
    private String zsxm;
    //机构名称
    private String jgmc;
    //培训类别[多]
    private String[] pxlbs;
    //考试次数
    private String kscs;
    //图片文件路径
    private String tpwjlj;
    private String yhm;
    private String pxlbcs;
    private String tmmx_json;
    //确认人员名称
    private String qrrymc;
    //flag(判断是否执行题库的修改操作)
    private String flag;
    //确认人员yhid
    private String qrryid;
    //附件ids
    private List<String> fjids;
    //所属公司(多)
    private String[] ssgss;
	private List<String> yhms;
    private String yhms_str;
    //所属公司
    private String ssgsmc;
    //修改前spxz
    private String xgqspxz;
    //类别个数
    private String lbgs;
    //培训类别名称
    private String pxlbmc;
    //培训个数
    private String pxgs;
    //培训人数
    private String pxrs;
    //机构ID
    private String jgid;
    private String[] pxzlbs;
    //部门名称
    private String bmmc;
	private String sqlParam;
    private String fqwwcsj;
    private String zt;
	private String ddid;
 	private List<String> yhmorzsxms;
    private String yhmorzsxms_str;
	//到期提醒
    private String dqtxmc;
    //到期天数
    private String dqts;
    //剩余时间
    private String syts;
    private String[] dqtxs;
	//是否发送红包
    private String[] sffshbs;
    //原红包设置id
    private String yhbszid;
    //红包封面路径
    private String hbfmlj;
    //答题页面路径
    private String dtymlj;
    //颜色
    private String color;
    //通过标记
    private String tgbj;
    private String lrsjstart;
    private String sftg;//是否通过
    private String zf;//总分
    private String sfks;//是否考试
    private String glwjids;//关联文件
    private String jgkzcs1;//机构扩展参数1
    private String sfhg;//是否合格
    private String pxfsmc;//培训方式名称
    private String qwwcsj;//期望完成时间
    private String jlbh;//记录编号
    private String qdyhids;//签到用户ids
    private List<GzglDto> list;//工作记录
    private String wbcxid;//外部程序id
    private String txts;//提醒天数
    private String pxmc;//培训名称
    private String bz;//备注
    private String flg;
    private String[] zts;

    public String[] getZts() {
        return zts;
    }

    public void setZts(String[] zts) {
        this.zts = zts;
    }

    public String getFlg() {
        return flg;
    }

    public void setFlg(String flg) {
        this.flg = flg;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getPxmc() {
        return pxmc;
    }

    public void setPxmc(String pxmc) {
        this.pxmc = pxmc;
    }

    public String getTxts() {
        return txts;
    }

    public void setTxts(String txts) {
        this.txts = txts;
    }

    public String getWbcxid() {
        return wbcxid;
    }

    public void setWbcxid(String wbcxid) {
        this.wbcxid = wbcxid;
    }

    public List<GzglDto> getList() {
        return list;
    }

    public void setList(List<GzglDto> list) {
        this.list = list;
    }

    public String getQdyhids() {
        return qdyhids;
    }

    public void setQdyhids(String qdyhids) {
        this.qdyhids = qdyhids;
    }

    public String getJlbh() {
        return jlbh;
    }

    public void setJlbh(String jlbh) {
        this.jlbh = jlbh;
    }

    public String getQwwcsj() {
        return qwwcsj;
    }

    public void setQwwcsj(String qwwcsj) {
        this.qwwcsj = qwwcsj;
    }

    public String getPxfsmc() {
        return pxfsmc;
    }

    public void setPxfsmc(String pxfsmc) {
        this.pxfsmc = pxfsmc;
    }

    public String getSfhg() {
        return sfhg;
    }

    public void setSfhg(String sfhg) {
        this.sfhg = sfhg;
    }

    public String getJgkzcs1() {
        return jgkzcs1;
    }

    public void setJgkzcs1(String jgkzcs1) {
        this.jgkzcs1 = jgkzcs1;
    }

    public String getGlwjids() {
        return glwjids;
    }

    public void setGlwjids(String glwjids) {
        this.glwjids = glwjids;
    }

    public String getSfks() {
        return sfks;
    }

    public void setSfks(String sfks) {
        this.sfks = sfks;
    }

    public String getZf() {
        return zf;
    }

    public void setZf(String zf) {
        this.zf = zf;
    }

    public String getSftg() {
        return sftg;
    }

    public void setSftg(String sftg) {
        this.sftg = sftg;
    }

    private String lrsjend;

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

    public String getTgbj() {
        return tgbj;
    }

    public void setTgbj(String tgbj) {
        this.tgbj = tgbj;
    }

    public String getHbfmlj() {
        return hbfmlj;
    }

    public void setHbfmlj(String hbfmlj) {
        this.hbfmlj = hbfmlj;
    }

    public String getDtymlj() {
        return dtymlj;
    }

    public void setDtymlj(String dtymlj) {
        this.dtymlj = dtymlj;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getYhbszid() {
        return yhbszid;
    }

    public void setYhbszid(String yhbszid) {
        this.yhbszid = yhbszid;
    }

    public String[] getSffshbs() {
        return sffshbs;
    }

    public void setSffshbs(String[] sffshbs) {
        this.sffshbs = sffshbs;
    }
    public String[] getDqtxs() {
        return dqtxs;
    }

    public void setDqtxs(String[] dqtxs) {
        this.dqtxs = dqtxs;
    }

    public String getSyts() {
        return syts;
    }

    public void setSyts(String syts) {
        this.syts = syts;
    }

    public String getDqts() {
        return dqts;
    }

    public void setDqts(String dqts) {
        this.dqts = dqts;
    }

    public String getDqtxmc() {
        return dqtxmc;
    }

    public void setDqtxmc(String dqtxmc) {
        this.dqtxmc = dqtxmc;
    }
	public List<String> getYhmorzsxms() {
        return yhmorzsxms;
    }

    public void setYhmorzsxms(List<String> yhmorzsxms) {
        this.yhmorzsxms = yhmorzsxms;
    }

    public String getYhmorzsxms_str() {
        return yhmorzsxms_str;
    }

    public void setYhmorzsxms_str(String yhmorzsxms_str) {
        this.yhmorzsxms_str = yhmorzsxms_str;
    }
	 public String getDdid() {
        return ddid;
    }

    public void setDdid(String ddid) {
        this.ddid = ddid;
    }
    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

    public String getFqwwcsj() {
        return fqwwcsj;
    }

    public void setFqwwcsj(String fqwwcsj) {
        this.fqwwcsj = fqwwcsj;
    }

    public String getSqlParam() {
        return sqlParam;
    }

    public void setSqlParam(String sqlParam) {
        this.sqlParam = sqlParam;
    }

    public String getBmmc() {
        return bmmc;
    }

    public void setBmmc(String bmmc) {
        this.bmmc = bmmc;
    }

    public String[] getPxzlbs() {
        return pxzlbs;
    }

    public void setPxzlbs(String[] pxzlbs) {
        this.pxzlbs = pxzlbs;
    }

    public String getJgid() {
        return jgid;
    }

    public void setJgid(String jgid) {
        this.jgid = jgid;
    }

    public String getPxgs() {
        return pxgs;
    }

    public void setPxgs(String pxgs) {
        this.pxgs = pxgs;
    }

    public String getPxrs() {
        return pxrs;
    }

    public void setPxrs(String pxrs) {
        this.pxrs = pxrs;
    }

    public String getLbgs() {
        return lbgs;
    }

    public void setLbgs(String lbgs) {
        this.lbgs = lbgs;
    }

    public String getPxlbmc() {
        return pxlbmc;
    }

    public void setPxlbmc(String pxlbmc) {
        this.pxlbmc = pxlbmc;
    }

    public String getXgqspxz() {
        return xgqspxz;
    }

    public void setXgqspxz(String xgqspxz) {
        this.xgqspxz = xgqspxz;
    }

   //负责人
    private String fzr;

    public String getFzr() {
        return fzr;
    }

    public void setFzr(String fzr) {
        this.fzr = fzr;
    }
    public String getSsgsmc() {
        return ssgsmc;
    }

    public void setSsgsmc(String ssgsmc) {
        this.ssgsmc = ssgsmc;
    }

    public List<String> getYhms() {
        return yhms;
    }

    public String getYhms_str() {
        return yhms_str;
    }

    public void setYhms_str(String yhms_str) {
        this.yhms_str = yhms_str;
    }

    public void setYhms(List<String> yhms) {
        this.yhms = yhms;
    }

    public String[] getSsgss() {
        return ssgss;
    }

    public void setSsgss(String[] ssgss) {
        this.ssgss = ssgss;
    }

    public List<String> getFjids() {
        return fjids;
    }

    public void setFjids(List<String> fjids) {
        this.fjids = fjids;
    }

    public String getTkts() {
        return tkts;
    }

    public void setTkts(String tkts) {
        this.tkts = tkts;
    }

    public String getPdts() {
        return pdts;
    }

    public void setPdts(String pdts) {
        this.pdts = pdts;
    }

    public String getQrryid() {
        return qrryid;
    }

    public void setQrryid(String qrryid) {
        this.qrryid = qrryid;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getQrrymc() {
        return qrrymc;
    }

    public void setQrrymc(String qrrymc) {
        this.qrrymc = qrrymc;
    }

    public String getTmmx_json() {
        return tmmx_json;
    }

    public void setTmmx_json(String tmmx_json) {
        this.tmmx_json = tmmx_json;
    }

    public String getPxlbcs() {
        return pxlbcs;
    }

    public void setPxlbcs(String pxlbcs) {
        this.pxlbcs = pxlbcs;
    }

    public String getYhm() {
        return yhm;
    }

    public void setYhm(String yhm) {
        this.yhm = yhm;
    }

    public String getTpwjlj() {
        return tpwjlj;
    }

    public void setTpwjlj(String tpwjlj) {
        this.tpwjlj = tpwjlj;
    }

    public String getKscs() {
        return kscs;
    }

    public void setKscs(String kscs) {
        this.kscs = kscs;
    }

    public String[] getPxlbs() {
        return pxlbs;
    }

    public void setPxlbs(String[] pxlbs) {
        this.pxlbs = pxlbs;
    }

    public String getZsxm() {
        return zsxm;
    }

    public void setZsxm(String zsxm) {
        this.zsxm = zsxm;
    }

    public String getJgmc() {
        return jgmc;
    }

    public void setJgmc(String jgmc) {
        this.jgmc = jgmc;
    }

    public String getTpfjid() {
        return tpfjid;
    }

    public void setTpfjid(String tpfjid) {
        this.tpfjid = tpfjid;
    }

    public String getSpfjid() {
        return spfjid;
    }

    public void setSpfjid(String spfjid) {
        this.spfjid = spfjid;
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    public String[] getSpbjs() {
        return spbjs;
    }

    public void setSpbjs(String[] spbjs) {
        this.spbjs = spbjs;
    }

    public String[] getSfcss() {
        return sfcss;
    }

    public void setSfcss(String[] sfcss) {
        this.sfcss = sfcss;
    }

    public String[] getHtbjs() {
        return htbjs;
    }

    public void setHtbjs(String[] htbjs) {
        this.htbjs = htbjs;
    }

    public String[] getTzfsbjs() {
        return tzfsbjs;
    }

    public void setTzfsbjs(String[] tzfsbjs) {
        this.tzfsbjs = tzfsbjs;
    }

    public String[] getCkdabjs() {
        return ckdabjs;
    }

    public void setCkdabjs(String[] ckdabjs) {
        this.ckdabjs = ckdabjs;
    }

    public String[] getDcckbjs() {
        return dcckbjs;
    }

    public void setDcckbjs(String[] dcckbjs) {
        this.dcckbjs = dcckbjs;
    }

    public String getXxA() {
        return xxA;
    }

    public void setXxA(String xxA) {
        this.xxA = xxA;
    }

    public String getXxB() {
        return xxB;
    }

    public void setXxB(String xxB) {
        this.xxB = xxB;
    }

    public String getXxC() {
        return xxC;
    }

    public void setXxC(String xxC) {
        this.xxC = xxC;
    }

    public String getXxD() {
        return xxD;
    }

    public void setXxD(String xxD) {
        this.xxD = xxD;
    }

    public String getXxE() {
        return xxE;
    }

    public void setXxE(String xxE) {
        this.xxE = xxE;
    }

    public String getXxF() {
        return xxF;
    }

    public void setXxF(String xxF) {
        this.xxF = xxF;
    }

    public String getXxG() {
        return xxG;
    }

    public void setXxG(String xxG) {
        this.xxG = xxG;
    }

    public String getXxH() {
        return xxH;
    }

    public void setXxH(String xxH) {
        this.xxH = xxH;
    }

    public String getTmlxmc() {
        return tmlxmc;
    }

    public void setTmlxmc(String tmlxmc) {
        this.tmlxmc = tmlxmc;
    }

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getTmnr() {
        return tmnr;
    }

    public void setTmnr(String tmnr) {
        this.tmnr = tmnr;
    }

    public String getFs() {
        return fs;
    }

    public void setFs(String fs) {
        this.fs = fs;
    }

    public String getDa() {
        return da;
    }

    public void setDa(String da) {
        this.da = da;
    }

    public String getXxdm() {
        return xxdm;
    }

    public void setXxdm(String xxdm) {
        this.xxdm = xxdm;
    }

    public String getXxnr() {
        return xxnr;
    }

    public void setXxnr(String xxnr) {
        this.xxnr = xxnr;
    }

    public String getTmlx() {
        return tmlx;
    }

    public void setTmlx(String tmlx) {
        this.tmlx = tmlx;
    }

    public String getDanxts() {
        return danxts;
    }

    public void setDanxts(String danxts) {
        this.danxts = danxts;
    }

    public String getDuoxts() {
        return duoxts;
    }

    public void setDuoxts(String duoxts) {
        this.duoxts = duoxts;
    }

    public String getJdts() {
        return jdts;
    }

    public void setJdts(String jdts) {
        this.jdts = jdts;
    }

    public String getTgfs() {
        return tgfs;
    }

    public void setTgfs(String tgfs) {
        this.tgfs = tgfs;
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

    public String getCszf() {
        return cszf;
    }

    public void setCszf(String cszf) {
        this.cszf = cszf;
    }



    public String getSpwcbj() {
        return spwcbj;
    }

    public void setSpwcbj(String spwcbj) {
        this.spwcbj = spwcbj;
    }

    public String getGzid() {
        return gzid;
    }

    public void setGzid(String gzid) {
        this.gzid = gzid;
    }

    public String getPxjd() {
        return pxjd;
    }

    public void setPxjd(String pxjd) {
        this.pxjd = pxjd;
    }

    public String getFjid() {
        return fjid;
    }

    public void setFjid(String fjid) {
        this.fjid = fjid;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

}
