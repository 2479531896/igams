package com.matridx.igams.hrm.dao.entities;


import org.apache.ibatis.type.Alias;

@Alias(value="KsmxDto")
public class KsmxDto extends KsmxModel {

    //题库id
    private String tkid;

    public String getTkid() {
        return tkid;
    }

    public void setTkid(String tkid) {
        this.tkid = tkid;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
