package com.matridx.igams.detection.molecule.dao.entities;
/*
    用于普检结果的判断
 */
public class PjjgpdDto {
    private int count;//条数
    private String ct;//ct
    private String pdz;//pdz
    private String pjjgcskz2;//普检结果参数扩展2
    private String pjjgcskz1;//普检结果参数扩展1
    private String pjjgcsid;//普检结果参数id

    public String getPjjgcsid() {
        return pjjgcsid;
    }

    public void setPjjgcsid(String pjjgcsid) {
        this.pjjgcsid = pjjgcsid;
    }

    public String getPjjgcskz2() {
        return pjjgcskz2;
    }

    public void setPjjgcskz2(String pjjgcskz2) {
        this.pjjgcskz2 = pjjgcskz2;
    }

    public String getPjjgcskz1() {
        return pjjgcskz1;
    }

    public void setPjjgcskz1(String pjjgcskz1) {
        this.pjjgcskz1 = pjjgcskz1;
    }

    public String getPdz() {
        return pdz;
    }

    public void setPdz(String pdz) {
        this.pdz = pdz;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public PjjgpdDto() {
    }

    public PjjgpdDto(int count, String ct, String pdz, String pjjgcskz2, String pjjgcskz1, String pjjgcsid) {
        this.count = count;
        this.ct = ct;
        this.pdz = pdz;
        this.pjjgcskz2 = pjjgcskz2;
        this.pjjgcskz1 = pjjgcskz1;
        this.pjjgcsid = pjjgcsid;
    }
}
