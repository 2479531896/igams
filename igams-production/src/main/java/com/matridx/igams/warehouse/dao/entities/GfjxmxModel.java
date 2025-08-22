package com.matridx.igams.warehouse.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

/**
 * @author : 郭祥杰
 * @date :
 */
@Alias(value="GfjxmxModel")
public class GfjxmxModel extends BaseBasicModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String jxmxid;
    private String gfjxid;
    private String biz;
    private String nr;
    private String xm;
    private String bz;
    private String df;
    private String dfbj;

    public String getDfbj() {
        return dfbj;
    }

    public void setDfbj(String dfbj) {
        this.dfbj = dfbj;
    }

    public String getJxmxid() {
        return jxmxid;
    }

    public void setJxmxid(String jxmxid) {
        this.jxmxid = jxmxid;
    }

    public String getGfjxid() {
        return gfjxid;
    }

    public void setGfjxid(String gfjxid) {
        this.gfjxid = gfjxid;
    }

    public String getBiz() {
        return biz;
    }

    public void setBiz(String biz) {
        this.biz = biz;
    }

    public String getNr() {
        return nr;
    }

    public void setNr(String nr) {
        this.nr = nr;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getDf() {
        return df;
    }

    public void setDf(String df) {
        this.df = df;
    }
}
