package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="Bom_parentModel")
public class Bom_parentModel {
    private String AutoId;
    private String BomId;
    private String ParentId;
    private String ParentScrap;
    private String SharingPartId;
    private String Uftsprivaet;

    public String getAutoId() {
        return AutoId;
    }

    public void setAutoId(String autoId) {
        AutoId = autoId;
    }

    public String getBomId() {
        return BomId;
    }

    public void setBomId(String bomId) {
        BomId = bomId;
    }

    public String getParentId() {
        return ParentId;
    }

    public void setParentId(String parentId) {
        ParentId = parentId;
    }

    public String getParentScrap() {
        return ParentScrap;
    }

    public void setParentScrap(String parentScrap) {
        ParentScrap = parentScrap;
    }

    public String getSharingPartId() {
        return SharingPartId;
    }

    public void setSharingPartId(String sharingPartId) {
        SharingPartId = sharingPartId;
    }

    public String getUftsprivaet() {
        return Uftsprivaet;
    }

    public void setUftsprivaet(String uftsprivaet) {
        Uftsprivaet = uftsprivaet;
    }
}
