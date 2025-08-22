package com.matridx.igams.experiment.dao.entities;

import org.apache.ibatis.type.Alias;

/**
 * @author : 郭祥杰
 * @date :
 */
@Alias(value="ZdhYqycxxDto")
public class ZdhYqycxxDto extends ZdhYqycxxModel{
    private static final long serialVersionUID = 1L;
    private String yqbm;

    public String getYqbm() {
        return yqbm;
    }

    public void setYqbm(String yqbm) {
        this.yqbm = yqbm;
    }
}
