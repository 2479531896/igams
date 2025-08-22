package com.matridx.igams.bioinformation.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias("BioDlyzzsModel")
public class BioDlyzzsModel extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String dljyid;
    private String name;
    private String vfid;
    private String vfcategory;
    private String comment;
    private String data;
    private String taxid;

    public String getDljyid() {
        return dljyid;
    }

    public void setDljyid(String dljyid) {
        this.dljyid = dljyid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVfcategory() {
        return vfcategory;
    }

    public void setVfcategory(String vfcategory) {
        this.vfcategory = vfcategory;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTaxid() {
        return taxid;
    }

    public void setTaxid(String taxid) {
        this.taxid = taxid;
    }

    public String getVfid() {
        return vfid;
    }

    public void setVfid(String vfid) {
        this.vfid = vfid;
    }
}
