package com.matridx.igams.experiment.dao.entities;

import org.apache.ibatis.type.Alias;

/**
 * @author : 郭祥杰
 * @date :
 */
@Alias(value="ZdhJkcsDto")
public class ZdhJkcsDto extends ZdhJkcsModel{
    private static final long serialVersionUID = 1L;
    private String bbbm;
    private String ybjbh;
    private String yqbm;

    public String getYqbm() {
        return yqbm;
    }

    public void setYqbm(String yqbm) {
        this.yqbm = yqbm;
    }

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
