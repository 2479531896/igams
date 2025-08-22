package com.matridx.igams.experiment.dao.entities;

import org.apache.ibatis.type.Alias;

/**
 * @author : 郭祥杰
 * @date :
 */
@Alias(value="ZdhYbmxDto")
public class ZdhYbmxDto extends ZdhYbmxModel{

    private String lcpxsj;
    private static final long serialVersionUID = 1L;

    public String getLcpxsj() {
        return lcpxsj;
    }

    public void setLcpxsj(String lcpxsj) {
        this.lcpxsj = lcpxsj;
    }
}
