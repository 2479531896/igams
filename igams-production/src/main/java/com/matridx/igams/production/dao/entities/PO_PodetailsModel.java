package com.matridx.igams.production.dao.entities;

import java.util.List;

public class PO_PodetailsModel {
	//自增ID
	private int ID;
	//物料编码
	private String cInvCode;
	//数量
	private String iQuantity;
	//原币单价
	private String iUnitPrice;
	//原币金额
	private String iMoney;
	//原币税额
	private String iTax;
	//原币价税合计
	private String iSum;
	//null
	private String iDisCount;
	//原币单价？
	private String iNatUnitPrice;
	//原币金额？
	private String iNatMoney;
	//原币税额
	private String iNatTax;
	//原币价税合计
	private String iNatSum;
	//计划到货日期
	private String dArriveDate;
	//数量？
	private String iReceivedQTY;
	//
	private String iReceivedNum;
	//原币金额？
	private String iReceivedMoney;
	//数量？null
	private String iInvQTY;
	//0 null
	private String iInvNum;
	//原币金额？ null
	private String iInvMoney;
	//税率
	private String iPerTaxRate;
	//0
	private String bGsp;
	//关联字段
	private int POID;
	//税额
	private String iTaxPrice;
	//关联请购单详细里的AutoID
	private String iAppIds;
	//1
	private String bTaxCost;
	//app
	private String cSource;
	//0
	private String SoType;
	//记录编号
	private String cupsocode;
	//0
	private String iordertype;
	//序号
	private String ivouchrowno;
	//备注
	private String cbMemo;
	//系统用 ||pupo|2019091005|2
	private String cbsysbarcode;
	//0
	private String bgift;
	//'010002
	private String cItemCode;
	//00
	private String cItem_class;
	//MAR002	
	private String cItemName;
	//??
	private String dUfts;
	private String iBG_Ctrl;
	//行关闭人
	private String cbCloser;
	//行关闭时间，年月日时分秒毫秒
	private String cbCloseTime;
	//行关闭时间，年月日
	private String cbCloseDate;
	private List<Integer> ids;

	public List<Integer> getIds() {
		return ids;
	}

	public void setIds(List<Integer> ids) {
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

	public String getiBG_Ctrl() {
		return iBG_Ctrl;
	}
	public void setiBG_Ctrl(String iBG_Ctrl) {
		this.iBG_Ctrl = iBG_Ctrl;
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
	public String getdUfts() {
		return dUfts;
	}
	public void setdUfts(String dUfts) {
		this.dUfts = dUfts;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
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
	public String getiInvQTY() {
		return iInvQTY;
	}
	public void setiInvQTY(String iInvQTY) {
		this.iInvQTY = iInvQTY;
	}
	public String getiInvNum() {
		return iInvNum;
	}
	public void setiInvNum(String iInvNum) {
		this.iInvNum = iInvNum;
	}
	public String getiInvMoney() {
		return iInvMoney;
	}
	public void setiInvMoney(String iInvMoney) {
		this.iInvMoney = iInvMoney;
	}
	public String getiPerTaxRate() {
		return iPerTaxRate;
	}
	public void setiPerTaxRate(String iPerTaxRate) {
		this.iPerTaxRate = iPerTaxRate;
	}
	public String getbGsp() {
		return bGsp;
	}
	public void setbGsp(String bGsp) {
		this.bGsp = bGsp;
	}

	public int getPOID() {
		return POID;
	}
	public void setPOID(int pOID) {
		POID = pOID;
	}
	public String getiTaxPrice() {
		return iTaxPrice;
	}
	public void setiTaxPrice(String iTaxPrice) {
		this.iTaxPrice = iTaxPrice;
	}
	public String getiAppIds() {
		return iAppIds;
	}
	public void setiAppIds(String iAppIds) {
		this.iAppIds = iAppIds;
	}
	public String getbTaxCost() {
		return bTaxCost;
	}
	public void setbTaxCost(String bTaxCost) {
		this.bTaxCost = bTaxCost;
	}
	public String getcSource() {
		return cSource;
	}
	public void setcSource(String cSource) {
		this.cSource = cSource;
	}
	public String getSoType() {
		return SoType;
	}
	public void setSoType(String soType) {
		SoType = soType;
	}
	public String getCupsocode() {
		return cupsocode;
	}
	public void setCupsocode(String cupsocode) {
		this.cupsocode = cupsocode;
	}
	public String getIordertype() {
		return iordertype;
	}
	public void setIordertype(String iordertype) {
		this.iordertype = iordertype;
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
	public String getBgift() {
		return bgift;
	}
	public void setBgift(String bgift) {
		this.bgift = bgift;
	}
	
	
	
}
