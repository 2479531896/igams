package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="DispatchListsModel")
public class DispatchListsModel {
	//主键id
	private String Autoid;
	//关联主表id
	private String DLID;
	//如果存在不同仓库的物料第一个仓库存0，后面的存NULL
	private String iCorID;
	//仓库代码
	private String cWhcode;
	//物料编码
	private String cInvCode;
	//发货数量
	private String iQuantity;
	//报价
	private String iQuotedPrice;
	//无税单价
	private String iUnitPrice;
	//含税单价
	private String iTaxUnitPrice;
	//无税总价
	private String iMoney;
	//税额
	private String iTax;
	//含税总金额
	private String iSum;
	//差额
	private String iDisCount;
	//无税单价
	private String iNatUnitPrice;
	//无税总金额
	private String iNatMoney;
	//税额
	private String iNatTax;
	//含税总金额
	private String iNatSum;
	//差额
	private String iNatDisCount;
	//0/如果存在不同仓库的物料第一个仓库存0，后面的存NULL
	private String isettleNum;
	//0/如果存在不同仓库的物料第一个仓库存0，后面的存NULL
	private String iSettleQuantity;
	//0/如果存在不同仓库的物料第一个仓库存0，后面的存NULL
	private String iBatch;
	//生产批号
	private String cBatch;
	//备注
	private String cMemo;
	//失效日期
	private String dVDate;
	//关联销售订单明细
	private String iSosID;
	//通过UA_Identity 表 DISPATCH 维护
	private String iDLsID;
	//扣率
	private String KL;
	//扣率2
	private String KL2;
	//税率
	private String iTaxRate;
	//发货数量
	private String fOutQuantity;
	//0/如果存在不同仓库的物料第一个仓库存0，后面的存NULL
	private String fEnSettleQuan;
	//0/如果存在不同仓库的物料第一个仓库存0，后面的存NULL
	private String fEnSettleSum;
	//0/如果存在不同仓库的物料第一个仓库存0，后面的存NULL
	private String fsumsignquantity;
	//0/如果存在不同仓库的物料第一个仓库存0，后面的存NULL
	private String fsumsignnum;
	//0/如果存在不同仓库的物料第一个仓库存0，后面的存NULL
	private String bsignover;
	//0/如果存在不同仓库的物料第一个仓库存0，后面的存NULL
	private String bneedloss;
	//0/如果存在不同仓库的物料第一个仓库存0，后面的存NULL
	private String frlossqty;
	//创建日期
	private String dMDate;
	//订单号
	private String cSoCode;
	//保质期
	private String iMassDate;
	//保质期标记
	private String cMassUnit;
	//订单号
	private String cordercode;
	//订单明细号
	private String iorderrowno;
	//发货单明细流水号
	private String irowno;
	//||SA01|单号|+流水
	private String cbSysBarCode;
	//退货数量
	private String iRetQuantity;
	//物料名称
	private String cInvName;
	//退货数
	private String fVeriRetQty;
	//退货数*含税单价
	private String fVeriRetSum;
	//退货数
	private String fretqtywkp;
	private String fretqtyykp;
	private String frettbquantity;
	//退货数*含税单价
	private String fretsum;
	private String bmpforderclosed;
	private String fretailrealamount;
	private String fretailsettleamount;
	private String cCorCode;
	private String fretsumykp;

	public String getFretsumykp() {
		return fretsumykp;
	}

	public void setFretsumykp(String fretsumykp) {
		this.fretsumykp = fretsumykp;
	}

	public String getcCorCode() {
		return cCorCode;
	}

	public void setcCorCode(String cCorCode) {
		this.cCorCode = cCorCode;
	}

	public String getFretailrealamount() {
		return fretailrealamount;
	}

	public void setFretailrealamount(String fretailrealamount) {
		this.fretailrealamount = fretailrealamount;
	}

	public String getFretailsettleamount() {
		return fretailsettleamount;
	}

	public void setFretailsettleamount(String fretailsettleamount) {
		this.fretailsettleamount = fretailsettleamount;
	}

	public String getBmpforderclosed() {
		return bmpforderclosed;
	}

	public void setBmpforderclosed(String bmpforderclosed) {
		this.bmpforderclosed = bmpforderclosed;
	}

	public String getfVeriRetQty() {
		return fVeriRetQty;
	}

	public void setfVeriRetQty(String fVeriRetQty) {
		this.fVeriRetQty = fVeriRetQty;
	}

	public String getfVeriRetSum() {
		return fVeriRetSum;
	}

	public void setfVeriRetSum(String fVeriRetSum) {
		this.fVeriRetSum = fVeriRetSum;
	}

	public String getFretqtywkp() {
		return fretqtywkp;
	}

	public void setFretqtywkp(String fretqtywkp) {
		this.fretqtywkp = fretqtywkp;
	}

	public String getFretqtyykp() {
		return fretqtyykp;
	}

	public void setFretqtyykp(String fretqtyykp) {
		this.fretqtyykp = fretqtyykp;
	}

	public String getFrettbquantity() {
		return frettbquantity;
	}

	public void setFrettbquantity(String frettbquantity) {
		this.frettbquantity = frettbquantity;
	}

	public String getFretsum() {
		return fretsum;
	}

	public void setFretsum(String fretsum) {
		this.fretsum = fretsum;
	}

	public String getcInvName() {
		return cInvName;
	}
	public void setcInvName(String cInvName) {
		this.cInvName = cInvName;
	}
	public String getAutoid() {
		return Autoid;
	}
	public void setAutoid(String autoid) {
		Autoid = autoid;
	}
	public String getDLID() {
		return DLID;
	}
	public void setDLID(String dLID) {
		DLID = dLID;
	}
	public String getiCorID() {
		return iCorID;
	}
	public void setiCorID(String iCorID) {
		this.iCorID = iCorID;
	}
	public String getcWhcode() {
		return cWhcode;
	}
	public void setcWhcode(String cWhcode) {
		this.cWhcode = cWhcode;
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
	public String getiQuotedPrice() {
		return iQuotedPrice;
	}
	public void setiQuotedPrice(String iQuotedPrice) {
		this.iQuotedPrice = iQuotedPrice;
	}
	public String getiUnitPrice() {
		return iUnitPrice;
	}
	public void setiUnitPrice(String iUnitPrice) {
		this.iUnitPrice = iUnitPrice;
	}
	public String getiTaxUnitPrice() {
		return iTaxUnitPrice;
	}
	public void setiTaxUnitPrice(String iTaxUnitPrice) {
		this.iTaxUnitPrice = iTaxUnitPrice;
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
	public String getiDisCount() {
		return iDisCount;
	}
	public void setiDisCount(String iDisCount) {
		this.iDisCount = iDisCount;
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
	public String getiNatDisCount() {
		return iNatDisCount;
	}
	public void setiNatDisCount(String iNatDisCount) {
		this.iNatDisCount = iNatDisCount;
	}
	public String getIsettleNum() {
		return isettleNum;
	}
	public void setIsettleNum(String isettleNum) {
		this.isettleNum = isettleNum;
	}
	public String getiSettleQuantity() {
		return iSettleQuantity;
	}
	public void setiSettleQuantity(String iSettleQuantity) {
		this.iSettleQuantity = iSettleQuantity;
	}
	public String getiBatch() {
		return iBatch;
	}
	public void setiBatch(String iBatch) {
		this.iBatch = iBatch;
	}
	public String getcBatch() {
		return cBatch;
	}
	public void setcBatch(String cBatch) {
		this.cBatch = cBatch;
	}
	public String getcMemo() {
		return cMemo;
	}
	public void setcMemo(String cMemo) {
		this.cMemo = cMemo;
	}
	public String getdVDate() {
		return dVDate;
	}
	public void setdVDate(String dVDate) {
		this.dVDate = dVDate;
	}
	public String getiSosID() {
		return iSosID;
	}
	public void setiSosID(String iSosID) {
		this.iSosID = iSosID;
	}
	public String getiDLsID() {
		return iDLsID;
	}
	public void setiDLsID(String iDLsID) {
		this.iDLsID = iDLsID;
	}
	public String getKL() {
		return KL;
	}
	public void setKL(String kL) {
		KL = kL;
	}
	public String getKL2() {
		return KL2;
	}
	public void setKL2(String kL2) {
		KL2 = kL2;
	}
	public String getiTaxRate() {
		return iTaxRate;
	}
	public void setiTaxRate(String iTaxRate) {
		this.iTaxRate = iTaxRate;
	}
	public String getfOutQuantity() {
		return fOutQuantity;
	}
	public void setfOutQuantity(String fOutQuantity) {
		this.fOutQuantity = fOutQuantity;
	}
	public String getfEnSettleQuan() {
		return fEnSettleQuan;
	}
	public void setfEnSettleQuan(String fEnSettleQuan) {
		this.fEnSettleQuan = fEnSettleQuan;
	}
	public String getfEnSettleSum() {
		return fEnSettleSum;
	}
	public void setfEnSettleSum(String fEnSettleSum) {
		this.fEnSettleSum = fEnSettleSum;
	}
	public String getFsumsignquantity() {
		return fsumsignquantity;
	}
	public void setFsumsignquantity(String fsumsignquantity) {
		this.fsumsignquantity = fsumsignquantity;
	}
	public String getFsumsignnum() {
		return fsumsignnum;
	}
	public void setFsumsignnum(String fsumsignnum) {
		this.fsumsignnum = fsumsignnum;
	}
	public String getBsignover() {
		return bsignover;
	}
	public void setBsignover(String bsignover) {
		this.bsignover = bsignover;
	}
	public String getBneedloss() {
		return bneedloss;
	}
	public void setBneedloss(String bneedloss) {
		this.bneedloss = bneedloss;
	}
	public String getFrlossqty() {
		return frlossqty;
	}
	public void setFrlossqty(String frlossqty) {
		this.frlossqty = frlossqty;
	}
	public String getdMDate() {
		return dMDate;
	}
	public void setdMDate(String dMDate) {
		this.dMDate = dMDate;
	}
	public String getcSoCode() {
		return cSoCode;
	}
	public void setcSoCode(String cSoCode) {
		this.cSoCode = cSoCode;
	}
	public String getiMassDate() {
		return iMassDate;
	}
	public void setiMassDate(String iMassDate) {
		this.iMassDate = iMassDate;
	}
	public String getcMassUnit() {
		return cMassUnit;
	}
	public void setcMassUnit(String cMassUnit) {
		this.cMassUnit = cMassUnit;
	}
	public String getCordercode() {
		return cordercode;
	}
	public void setCordercode(String cordercode) {
		this.cordercode = cordercode;
	}
	public String getIorderrowno() {
		return iorderrowno;
	}
	public void setIorderrowno(String iorderrowno) {
		this.iorderrowno = iorderrowno;
	}
	public String getIrowno() {
		return irowno;
	}
	public void setIrowno(String irowno) {
		this.irowno = irowno;
	}
	public String getCbSysBarCode() {
		return cbSysBarCode;
	}
	public void setCbSysBarCode(String cbSysBarCode) {
		this.cbSysBarCode = cbSysBarCode;
	}
	public String getiRetQuantity() {
		return iRetQuantity;
	}
	public void setiRetQuantity(String iRetQuantity) {
		this.iRetQuantity = iRetQuantity;
	}
	
	
	
	
	
	
	
}
