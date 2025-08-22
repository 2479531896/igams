package com.matridx.igams.hrm.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.Date;

@Alias(value = "JpjgDto")
public class JpjgDto extends JpjgModel {
    //姓名
    private String zsxm;
    //用户名
    private String yhm;
    //日期差
    private long rqc;
    private Date hjsjDate;
    private int pageStart;
    private int pageSize;

    public Date getHjsjDate() {
        return hjsjDate;
    }

    public void setHjsjDate(Date hjsjDate) {
        this.hjsjDate = hjsjDate;
    }

    public long getRqc() {
        return rqc;
    }

    public void setRqc(long rqc) {
        this.rqc = rqc;
    }

    public String getZsxm() {
        return zsxm;
    }

    public void setZsxm(String zsxm) {
        this.zsxm = zsxm;
    }

    public String getYhm() {
        return yhm;
    }

    public void setYhm(String yhm) {
        this.yhm = yhm;
    }

    @Override
    public int getPageStart() {
        return pageStart*this.pageSize;
    }

    @Override
    public void setPageStart(int pageStart) {
        this.pageStart = pageStart;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

}
