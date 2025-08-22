package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

/*
 *@date 2022年06月30日9:40
 *
 */

@Alias(value = "OM_MODetailsModel")
public class OM_MODetailsModel {
    private String MODetailsID;
    //关联主表id
    private String MOID;
    //物料编码
    private String cInvCode;
    //数量
    private String iQuantity;
    //原币单价
    private String iUnitPrice;
    //原币单价×数量
    private String iMoney;
    //原币单价×税率×数量
    private String iTax;
    //（原币单价×税率+原币单价）×数量
    private String iSum;
    //原币单价
    private String iNatUnitPrice;
    //原币单价×数量
    private String iNatMoney;
    //原币单价×税率×数量
    private String iNatTax;
    //（原币单价×税率+原币单价）×数量
    private String iNatSum;
    //创建日期
    private String dStartDate;
    //审核时间
    private String dArriveDate;
    //到货数量
    private String iReceivedQTY;
    //税率
    private String iPerTaxRate;
    //项目编码代码
    private String cItemCode;
    //项目大类代码
    private String cItem_class;
    //项目编码名称
    private String cItemName;
    //（含税单价×税率+含税单价）
    private String iTaxPrice;
    //关联产品结构主表id
    private String BomID;
    //母件损耗率
    private String fParentScrp;
    //模板号8159
    private String iVTids;
    //请购单号
    private String cupsocode;
    //请购单明细id
    private String cupsoids;
    //明细流水号
    private String iVouchRowNo;
    //备注
    private String cbMemo;
    //||ommo|合同单号|流水号
    private String cbsysbarcode;
    //bom类型
    private String BomType;
    //数量
    private String imrpqty;
    //0.000000 合同引用
    private String iReceivedNum;
    //0.00
    private String iReceivedMoney;
    private String iMaterialSendQty;
    //行关闭人
    private String cbCloser;
    //行关闭时间，年月日时分秒毫秒
    private String cbCloseTime;
    //行关闭时间，年月日
    private String cbCloseDate;
    private List<String> ids;

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public String getCbCloser() {
        return cbCloser;
    }

    public void setCbCloser(String cbCloser) {
        this.cbCloser = cbCloser;
    }

    public String getCbCloseTime() {
        return cbCloseTime;
    }

    public void setCbCloseTime(String cbCloseTime) {
        this.cbCloseTime = cbCloseTime;
    }

    public String getCbCloseDate() {
        return cbCloseDate;
    }

    public void setCbCloseDate(String cbCloseDate) {
        this.cbCloseDate = cbCloseDate;
    }

    public String getiMaterialSendQty() {
        return iMaterialSendQty;
    }

    public void setiMaterialSendQty(String iMaterialSendQty) {
        this.iMaterialSendQty = iMaterialSendQty;
    }

    public String getiReceivedNum() {
        return iReceivedNum;
    }

    public void setiReceivedNum(String iReceivedNum) {
        this.iReceivedNum = iReceivedNum;
    }

    public String getiReceivedMoney() {
        return iReceivedMoney;
    }

    public void setiReceivedMoney(String iReceivedMoney) {
        this.iReceivedMoney = iReceivedMoney;
    }

    public String getMODetailsID() {
        return MODetailsID;
    }

    public void setMODetailsID(String MODetailsID) {
        this.MODetailsID = MODetailsID;
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

    public String getiUnitPrice() {
        return iUnitPrice;
    }

    public void setiUnitPrice(String iUnitPrice) {
        this.iUnitPrice = iUnitPrice;
    }

    public String getiMoney() {
        return iMoney;
    }

    public void setiMoney(String iMoney) {
        this.iMoney = iMoney;
    }

    public String getiTax() {
        return iTax;
    }

    public void setiTax(String iTax) {
        this.iTax = iTax;
    }

    public String getiSum() {
        return iSum;
    }

    public void setiSum(String iSum) {
        this.iSum = iSum;
    }

    public String getiNatUnitPrice() {
        return iNatUnitPrice;
    }

    public void setiNatUnitPrice(String iNatUnitPrice) {
        this.iNatUnitPrice = iNatUnitPrice;
    }

    public String getiNatMoney() {
        return iNatMoney;
    }

    public void setiNatMoney(String iNatMoney) {
        this.iNatMoney = iNatMoney;
    }

    public String getiNatTax() {
        return iNatTax;
    }

    public void setiNatTax(String iNatTax) {
        this.iNatTax = iNatTax;
    }

    public String getiNatSum() {
        return iNatSum;
    }

    public void setiNatSum(String iNatSum) {
        this.iNatSum = iNatSum;
    }

    public String getdStartDate() {
        return dStartDate;
    }

    public void setdStartDate(String dStartDate) {
        this.dStartDate = dStartDate;
    }

    public String getdArriveDate() {
        return dArriveDate;
    }

    public void setdArriveDate(String dArriveDate) {
        this.dArriveDate = dArriveDate;
    }

    public String getiReceivedQTY() {
        return iReceivedQTY;
    }

    public void setiReceivedQTY(String iReceivedQTY) {
        this.iReceivedQTY = iReceivedQTY;
    }

    public String getiPerTaxRate() {
        return iPerTaxRate;
    }

    public void setiPerTaxRate(String iPerTaxRate) {
        this.iPerTaxRate = iPerTaxRate;
    }

    public String getcItemCode() {
        return cItemCode;
    }

    public void setcItemCode(String cItemCode) {
        this.cItemCode = cItemCode;
    }

    public String getcItem_class() {
        return cItem_class;
    }

    public void setcItem_class(String cItem_class) {
        this.cItem_class = cItem_class;
    }

    public String getcItemName() {
        return cItemName;
    }

    public void setcItemName(String cItemName) {
        this.cItemName = cItemName;
    }

    public String getiTaxPrice() {
        return iTaxPrice;
    }

    public void setiTaxPrice(String iTaxPrice) {
        this.iTaxPrice = iTaxPrice;
    }

    public String getBomID() {
        return BomID;
    }

    public void setBomID(String bomID) {
        BomID = bomID;
    }

    public String getfParentScrp() {
        return fParentScrp;
    }

    public void setfParentScrp(String fParentScrp) {
        this.fParentScrp = fParentScrp;
    }

    public String getiVTids() {
        return iVTids;
    }

    public void setiVTids(String iVTids) {
        this.iVTids = iVTids;
    }

    public String getCupsocode() {
        return cupsocode;
    }

    public void setCupsocode(String cupsocode) {
        this.cupsocode = cupsocode;
    }

    public String getCupsoids() {
        return cupsoids;
    }

    public void setCupsoids(String cupsoids) {
        this.cupsoids = cupsoids;
    }

    public String getiVouchRowNo() {
        return iVouchRowNo;
    }

    public void setiVouchRowNo(String iVouchRowNo) {
        this.iVouchRowNo = iVouchRowNo;
    }

    public String getCbMemo() {
        return cbMemo;
    }

    public void setCbMemo(String cbMemo) {
        this.cbMemo = cbMemo;
    }

    public String getCbsysbarcode() {
        return cbsysbarcode;
    }

    public void setCbsysbarcode(String cbsysbarcode) {
        this.cbsysbarcode = cbsysbarcode;
    }

    public String getBomType() {
        return BomType;
    }

    public void setBomType(String bomType) {
        BomType = bomType;
    }

    public String getImrpqty() {
        return imrpqty;
    }

    public void setImrpqty(String imrpqty) {
        this.imrpqty = imrpqty;
    }
}
