package com.matridx.igams.experiment.dao.entities;

import org.apache.ibatis.type.Alias;

/**
 * @author : 郭祥杰
 * @date :
 */
@Alias(value="ZdhJkwdjlDto")
public class ZdhJkwdjlDto extends ZdhJkwdjlModel{
    private static final long serialVersionUID = 1L;
    private String bbbm;
    private String ybjbh;

    public String getBbbm() {
        return bbbm;
    }

    public void setBbbm(String bbbm) {
        this.bbbm = bbbm;
    }

    public String getYbjbh() {
        return ybjbh;
    }

    public void setYbjbh(String ybjbh) {
        this.ybjbh = ybjbh;
    }
}
