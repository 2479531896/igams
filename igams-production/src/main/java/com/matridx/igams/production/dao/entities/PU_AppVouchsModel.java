package com.matridx.igams.production.dao.entities;

public class PU_AppVouchsModel {

	//关联请购单表头,采购ID
	private String ID;
	//采购明细ID
	private String AutoID;
	//物料编码
	private String cInvCode;
	//数量
	private String fQuantity;
	//单价税率
	private String iPerTaxRate;
	//要求到货日期
	private String dRequirDate;
	//订单日期
	private String dArriveDate;
	//要求到货数量
	private String iReceivedQTY;
	//项目大类
	private String cItem_class;
	//项目编码
	private String cItemCode;
	//项目名称
	private String CItemName;
	//
	private String bTaxCost;
	//币种
	private String cexch_name;
	//兑率
	private String iExchRate;
	//序号
	private String ivouchrowno;
	//备注
	private String cbMemo;
	//
	private String cbsysbarcode;
	//
	private String dUfts;
	//单价
	private String fUnitPrice;
	//单价*税率+单价
	private String fTaxPrice;
	//(单价*税率+单价)*数量
	private String fMoney;
	//合同未引用null,合同引用0.000000
	private String iReceivedNum;

	//单价
	private String iOriCost;
	//单价*税率+单价
	private String iOriTaxCost;
	//单价*数量
	private String iOriMoney;
	//单价*税率*数量
	private String iOriTaxPrice;
	//(单价*税率+单价)*数量
	private String iOriSum;
	//单价*数量
	private String iMoney;
	//单价*税率*数量
	private String iTaxPrice;


	public String getiOriTaxCost() {
		return iOriTaxCost;
	}

	public void setiOriTaxCost(String iOriTaxCost) {
		this.iOriTaxCost = iOriTaxCost;
	}

	public String getiOriMoney() {
		return iOriMoney;
	}

	public void setiOriMoney(String iOriMoney) {
		this.iOriMoney = iOriMoney;
	}

	public String getiOriTaxPrice() {
		return iOriTaxPrice;
	}

	public void setiOriTaxPrice(String iOriTaxPrice) {
		this.iOriTaxPrice = iOriTaxPrice;
	}

	public String getiOriSum() {
		return iOriSum;
	}

	public void setiOriSum(String iOriSum) {
		this.iOriSum = iOriSum;
	}

	public String getiMoney() {
		return iMoney;
	}

	public void setiMoney(String iMoney) {
		this.iMoney = iMoney;
	}

	public String getiTaxPrice() {
		return iTaxPrice;
	}

	public void setiTaxPrice(String iTaxPrice) {
		this.iTaxPrice = iTaxPrice;
	}

	public String getiOriCost() {
		return iOriCost;
	}

	public void setiOriCost(String iOriCost) {
		this.iOriCost = iOriCost;
	}

	public String getiReceivedNum() {
		return iReceivedNum;
	}

	public void setiReceivedNum(String iReceivedNum) {
		this.iReceivedNum = iReceivedNum;
	}

	public String getfTaxPrice() {
		return fTaxPrice;
	}
	public void setfTaxPrice(String fTaxPrice) {
		this.fTaxPrice = fTaxPrice;
	}
	public String getfMoney() {
		return fMoney;
	}
	public void setfMoney(String fMoney) {
		this.fMoney = fMoney;
	}
	public String getfUnitPrice() {
		return fUnitPrice;
	}
	public void setfUnitPrice(String fUnitPrice) {
		this.fUnitPrice = fUnitPrice;
	}
	public String getdUfts() {
		return dUfts;
	}
	public void setdUfts(String dUfts) {
		this.dUfts = dUfts;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getAutoID() {
		return AutoID;
	}
	public void setAutoID(String autoID) {
		AutoID = autoID;
	}
	public String getcInvCode() {
		return cInvCode;
	}
	public void setcInvCode(String cInvCode) {
		this.cInvCode = cInvCode;
	}
	public String getfQuantity() {
		return fQuantity;
	}
	public void setfQuantity(String fQuantity) {
		this.fQuantity = fQuantity;
	}
	public String getiPerTaxRate() {
		return iPerTaxRate;
	}
	public void setiPerTaxRate(String iPerTaxRate) {
		this.iPerTaxRate = iPerTaxRate;
	}
	public String getdRequirDate() {
		return dRequirDate;
	}
	public void setdRequirDate(String dRequirDate) {
		this.dRequirDate = dRequirDate;
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
	public String getcItem_class() {
		return cItem_class;
	}
	public void setcItem_class(String cItem_class) {
		this.cItem_class = cItem_class;
	}
	public String getcItemCode() {
		return cItemCode;
	}
	public void setcItemCode(String cItemCode) {
		this.cItemCode = cItemCode;
	}
	public String getCItemName() {
		return CItemName;
	}
	public void setCItemName(String cItemName) {
		CItemName = cItemName;
	}
	public String getbTaxCost() {
		return bTaxCost;
	}
	public void setbTaxCost(String bTaxCost) {
		this.bTaxCost = bTaxCost;
	}
	public String getCexch_name() {
		return cexch_name;
	}
	public void setCexch_name(String cexch_name) {
		this.cexch_name = cexch_name;
	}
	public String getiExchRate() {
		return iExchRate;
	}
	public void setiExchRate(String iExchRate) {
		this.iExchRate = iExchRate;
	}
	public String getIvouchrowno() {
		return ivouchrowno;
	}
	public void setIvouchrowno(String ivouchrowno) {
		this.ivouchrowno = ivouchrowno;
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
}
