package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias("SbtsjlModel")
public class SbtsjlModel extends BaseBasicModel {
    private String tsid;//调试id
    private String llid;//履历id
    private String azdw;//安装单位
    private String yjr;//移交人
    private String yjrq;//移交日期
    private String sydw;//使用单位
    private String ysr;//验收人
    private String ysrq;//验收日期
    private String yxsj;//运行时间
    private String jsjdqbf;//机身及电气部分
    private String czcdjg;//操作、传动机构
    private String fsxt;//附属系统
    private String yspdyj;//验收评定意见

    public String getTsid() {
        return tsid;
    }

    public void setTsid(String tsid) {
        this.tsid = tsid;
    }

    public String getLlid() {
        return llid;
    }

    public void setLlid(String llid) {
        this.llid = llid;
    }

    public String getAzdw() {
        return azdw;
    }

    public void setAzdw(String azdw) {
        this.azdw = azdw;
    }

    public String getYjr() {
        return yjr;
    }

    public void setYjr(String yjr) {
        this.yjr = yjr;
    }

    public String getYjrq() {
        return yjrq;
    }

    public void setYjrq(String yjrq) {
        this.yjrq = yjrq;
    }

    public String getSydw() {
        return sydw;
    }

    public void setSydw(String sydw) {
        this.sydw = sydw;
    }

    public String getYsr() {
        return ysr;
    }

    public void setYsr(String ysr) {
        this.ysr = ysr;
    }

    public String getYsrq() {
        return ysrq;
    }

    public void setYsrq(String ysrq) {
        this.ysrq = ysrq;
    }

    public String getYxsj() {
        return yxsj;
    }

    public void setYxsj(String yxsj) {
        this.yxsj = yxsj;
    }

    public String getJsjdqbf() {
        return jsjdqbf;
    }

    public void setJsjdqbf(String jsjdqbf) {
        this.jsjdqbf = jsjdqbf;
    }

    public String getCzcdjg() {
        return czcdjg;
    }

    public void setCzcdjg(String czcdjg) {
        this.czcdjg = czcdjg;
    }

    public String getFsxt() {
        return fsxt;
    }

    public void setFsxt(String fsxt) {
        this.fsxt = fsxt;
    }

    public String getYspdyj() {
        return yspdyj;
    }

    public void setYspdyj(String yspdyj) {
        this.yspdyj = yspdyj;
    }
    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
