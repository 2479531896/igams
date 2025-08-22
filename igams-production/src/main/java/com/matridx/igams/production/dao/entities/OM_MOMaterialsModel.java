package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

/*
 *@date 2022年06月30日9:40
 *
 */
@Alias(value = "OM_MOMaterialsModel")
public class OM_MOMaterialsModel {
    private String MOMaterialsID;
    private String MoDetailsID;
    //关联订单主表id
    private String MOID;
    //物料编码
    private String cInvCode;
    // 数量
    private String iQuantity;
    //计划到货日期
    private String dRequiredDate;
    //基本用量
    private String fBaseQtyN;
    //基础用量
    private String fBaseQtyD;
    //子件损耗率
    private String fCompScrp;
    //仓库代码
    private String cWhCode;
    //使用数量
    private String iUnitQuantity;
    //关联bom_opcomponent表主键id
    private String OpComponentId;
    //备注
    private String cbMemo;
    //||ommo|单号|合同明细流水|bom表流水
    private String csubsysbarcode;
    private String iSendQTY;
    private String iSendNum;


    public String getiSendQTY() {
        return iSendQTY;
    }

    public void setiSendQTY(String iSendQTY) {
        this.iSendQTY = iSendQTY;
    }

    public String getiSendNum() {
        return iSendNum;
    }

    public void setiSendNum(String iSendNum) {
        this.iSendNum = iSendNum;
    }

    public String getMOMaterialsID() {
        return MOMaterialsID;
    }

    public void setMOMaterialsID(String MOMaterialsID) {
        this.MOMaterialsID = MOMaterialsID;
    }

    public String getMoDetailsID() {
        return MoDetailsID;
    }

    public void setMoDetailsID(String moDetailsID) {
        MoDetailsID = moDetailsID;
    }

    public String getMOID() {
        return MOID;
    }

    public void setMOID(String MOID) {
        this.MOID = MOID;
    }

    public String getcInvCode() {
        return cInvCode;
    }

    public void setcInvCode(String cInvCode) {
        this.cInvCode = cInvCode;
    }

    public String getiQuantity() {
        return iQuantity;
    }

    public void setiQuantity(String iQuantity) {
        this.iQuantity = iQuantity;
    }

    public String getdRequiredDate() {
        return dRequiredDate;
    }

    public void setdRequiredDate(String dRequiredDate) {
        this.dRequiredDate = dRequiredDate;
    }

    public String getfBaseQtyN() {
        return fBaseQtyN;
    }

    public void setfBaseQtyN(String fBaseQtyN) {
        this.fBaseQtyN = fBaseQtyN;
    }

    public String getfBaseQtyD() {
        return fBaseQtyD;
    }

    public void setfBaseQtyD(String fBaseQtyD) {
        this.fBaseQtyD = fBaseQtyD;
    }

    public String getfCompScrp() {
        return fCompScrp;
    }

    public void setfCompScrp(String fCompScrp) {
        this.fCompScrp = fCompScrp;
    }

    public String getcWhCode() {
        return cWhCode;
    }

    public void setcWhCode(String cWhCode) {
        this.cWhCode = cWhCode;
    }

    public String getiUnitQuantity() {
        return iUnitQuantity;
    }

    public void setiUnitQuantity(String iUnitQuantity) {
        this.iUnitQuantity = iUnitQuantity;
    }

    public String getOpComponentId() {
        return OpComponentId;
    }

    public void setOpComponentId(String opComponentId) {
        OpComponentId = opComponentId;
    }

    public String getCbMemo() {
        return cbMemo;
    }

    public void setCbMemo(String cbMemo) {
        this.cbMemo = cbMemo;
    }

    public String getCsubsysbarcode() {
        return csubsysbarcode;
    }

    public void setCsubsysbarcode(String csubsysbarcode) {
        this.csubsysbarcode = csubsysbarcode;
    }
}
