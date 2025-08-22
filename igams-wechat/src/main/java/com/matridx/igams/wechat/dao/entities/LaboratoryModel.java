package com.matridx.igams.wechat.dao.entities;


import org.apache.ibatis.type.Alias;

@Alias(value="LaboratoryModel")
public class LaboratoryModel{
    //实验室姓名
    private String name;
    //实验室代码
    private String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
