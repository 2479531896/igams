package com.matridx.igams.wechat.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="DlyzzsModel")
public class DlyzzsModel extends BaseModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    //毒力因子ID
    private String dljyid;
    //毒力因子名称
    private String name;
    //毒力因子解释ID
    private String vfid;
    //来源
    private String vfcategory;
    //解释
    private String comment;
    //
    private String data;
    //物种ID
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

    public String getVfid() {
        return vfid;
    }

    public void setVfid(String vfid) {
        this.vfid = vfid;
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
}
