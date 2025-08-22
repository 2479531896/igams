package com.matridx.igams.production.dao.entities;

public class RdRecordsModel {
	//入库明细ID
	private int AutoID;
	private String isotype;
	private String bVMIUsed;
	//入库ID
	private int ID;
	//存货编码（物料）
	private String cInvCode;
	//数量
	private String iQuantity;
	//原币单价
	private String iUnitCost;
	//原币金额
	private String iPrice;
	//原币金额
	private String iNum;
	//原币金额
	private String iAPrice;
	//原币含税单价
	private String iOriTaxCost;
	//原币单价
	private String iOriCost;
	//原币金额
	private String iOriMoney;
	//原币税额
	private String iOriTaxPrice;
	//原币价税合计
	private String ioriSum;
	//税率
	private String iTaxRate;
	//税额
	private String iTaxPrice;
	//订单编号
	private String cPOID;
	//数量
	private String iSQuantity;
	//借出单明细ID
	private String idebitchildids;
	//借出单ID
	private String iDebitIDs;
	//原币金额
	private String iMoney;
	//数量
	private String iNQuantity;
	//数量
	private String iSumBillQuantity;
	//项目大类编码
	private String cItem_class;
	//项目编码代码
	private String cItemCode;
	//项目编码名称
	private String cItemCName;
	//物料保质期
	private String cMassUnit;
	//生产批号
	private String cBatch;
	private String bTaxCost;
	//失效日期
	private String dvDate;
	//货位
	private String cPosition;
	//项目编码名称
	private String cName;
	//生产日期
	private String dMadeDate;
	//保质期
	private String iMassDate;

	//当前明细排序
	private String irowno;
	private String dSDate;
	private String iSNum;
	private String dVDate;
	private String iPOsID;
	private String fACost;
	private String chVencode;
	private String iSum;
	private String cbaccounter;
	private String dbKeepDate;
	private String corufts;
	private String cbMemo;
	private String rowufts;
	private String cbsysbarcode;
	private String cDefine23;
	private String cDefine29;
	//生产批号
	private String cDefine22;
	private String iSOutQuantity;
	private String isOutNum;
	private String iTrIds;
	//货位管理标记
	private String iposflag;
	//关联发货单明细
	private String iDLsID;
	//发货单号
	private String cbdlcode;
	//关联销售订单明细
	private String iorderdid;
	//销售订单类型
	private String iordertype;
	//关联销售订单主表
	private String iordercode;
	//关联销售订单明细
	private String ipesodid;
	//销售订单类型
	private String ipesotype;
	//关联销售订单主表
	private String cpesocode;
	//销售订单流水
	private String iorderseq;
	private String ipesoseq;
	private String bcanreplace;
	//原币单价（不含税）
	private String iProcessCost;
	//原币单价×入库数量
	private String iProcessFee;
	//OM_MODetails表 MODetailsID
	private String iOMoDID;
	private String comcode;
	private String iOMoMID;
	private String invcode;
	private String bOutMaterials;
	private String iProductType;
	private String iNNum;
	private String bGsp;
	//
	private String bneedbill;

	public String getBneedbill() {
		return bneedbill;
	}

	public void setBneedbill(String bneedbill) {
		this.bneedbill = bneedbill;
	}
	public String getbGsp() {
		return bGsp;
	}

	public void setbGsp(String bGsp) {
		this.bGsp = bGsp;
	}

	public String getbVMIUsed() {
		return bVMIUsed;
	}

	public void setbVMIUsed(String bVMIUsed) {
		this.bVMIUsed = bVMIUsed;
	}

	public String getiNNum() {
		return iNNum;
	}

	public void setiNNum(String iNNum) {
		this.iNNum = iNNum;
	}

	public String getiProductType() {
		return iProductType;
	}

	public void setiProductType(String iProductType) {
		this.iProductType = iProductType;
	}
	public String getbOutMaterials() {
		return bOutMaterials;
	}

	public void setbOutMaterials(String bOutMaterials) {
		this.bOutMaterials = bOutMaterials;
	}

	public String getInvcode() {
		return invcode;
	}

	public void setInvcode(String invcode) {
		this.invcode = invcode;
	}

	public String getiOMoMID() {
		return iOMoMID;
	}

	public void setiOMoMID(String iOMoMID) {
		this.iOMoMID = iOMoMID;
	}

	public String getComcode() {
		return comcode;
	}

	public void setComcode(String comcode) {
		this.comcode = comcode;
	}

	public String getiProcessCost() {
		return iProcessCost;
	}

	public void setiProcessCost(String iProcessCost) {
		this.iProcessCost = iProcessCost;
	}

	public String getiProcessFee() {
		return iProcessFee;
	}

	public void setiProcessFee(String iProcessFee) {
		this.iProcessFee = iProcessFee;
	}

	public String getiOMoDID() {
		return iOMoDID;
	}

	public void setiOMoDID(String iOMoDID) {
		this.iOMoDID = iOMoDID;
	}

	public String getbTaxCost() {
		return bTaxCost;
	}

	public void setbTaxCost(String bTaxCost) {
		this.bTaxCost = bTaxCost;
	}

	public String getiNum() {
		return iNum;
	}

	public void setiNum(String iNum) {
		this.iNum = iNum;
	}

	public String getBcanreplace() {
		return bcanreplace;
	}

	public void setBcanreplace(String bcanreplace) {
		this.bcanreplace = bcanreplace;
	}

	public String getIsotype() {
		return isotype;
	}

	public void setIsotype(String isotype) {
		this.isotype = isotype;
	}

	public String getIdebitchildids() {
		return idebitchildids;
	}

	public void setIdebitchildids(String idebitchildids) {
		this.idebitchildids = idebitchildids;
	}

	public String getiDebitIDs() {
		return iDebitIDs;
	}

	public void setiDebitIDs(String iDebitIDs) {
		this.iDebitIDs = iDebitIDs;
	}

	public String getIorderseq() {
		return iorderseq;
	}
	public void setIorderseq(String iorderseq) {
		this.iorderseq = iorderseq;
	}
	public String getIpesoseq() {
		return ipesoseq;
	}
	public void setIpesoseq(String ipesoseq) {
		this.ipesoseq = ipesoseq;
	}
	public String getiDLsID() {
		return iDLsID;
	}
	public void setiDLsID(String iDLsID) {
		this.iDLsID = iDLsID;
	}
	public String getCbdlcode() {
		return cbdlcode;
	}
	public void setCbdlcode(String cbdlcode) {
		this.cbdlcode = cbdlcode;
	}
	public String getIorderdid() {
		return iorderdid;
	}
	public void setIorderdid(String iorderdid) {
		this.iorderdid = iorderdid;
	}

	public String getIordertype() {
		return iordertype;
	}
	public void setIordertype(String iordertype) {
		this.iordertype = iordertype;
	}
	public String getIordercode() {
		return iordercode;
	}
	public void setIordercode(String iordercode) {
		this.iordercode = iordercode;
	}
	public String getIpesodid() {
		return ipesodid;
	}
	public void setIpesodid(String ipesodid) {
		this.ipesodid = ipesodid;
	}
	public String getIpesotype() {
		return ipesotype;
	}
	public void setIpesotype(String ipesotype) {
		this.ipesotype = ipesotype;
	}
	public String getCpesocode() {
		return cpesocode;
	}
	public void setCpesocode(String cpesocode) {
		this.cpesocode = cpesocode;
	}
	public String getIposflag() {
		return iposflag;
	}
	public void setIposflag(String iposflag) {
		this.iposflag = iposflag;
	}
	public String getiTrIds() {
		return iTrIds;
	}
	public void setiTrIds(String iTrIds) {
		this.iTrIds = iTrIds;
	}
	public String getiSOutQuantity() {
		return iSOutQuantity;
	}
	public void setiSOutQuantity(String iSOutQuantity) {
		this.iSOutQuantity = iSOutQuantity;
	}
	public String getIsOutNum() {
		return isOutNum;
	}
	public void setIsOutNum(String isOutNum) {
		this.isOutNum = isOutNum;
	}
	public String getcDefine22() {
		return cDefine22;
	}
	public void setcDefine22(String cDefine22) {
		this.cDefine22 = cDefine22;
	}
	public String getiMassDate() {
		return iMassDate;
	}
	public void setiMassDate(String iMassDate) {
		this.iMassDate = iMassDate;
	}
	public String getcDefine23() {
		return cDefine23;
	}
	public void setcDefine23(String cDefine23) {
		this.cDefine23 = cDefine23;
	}
	public String getcDefine29() {
		return cDefine29;
	}
	public void setcDefine29(String cDefine29) {
		this.cDefine29 = cDefine29;
	}
	public String getcMassUnit() {
		return cMassUnit;
	}
	public void setcMassUnit(String cMassUnit) {
		this.cMassUnit = cMassUnit;
	}
	public String getcItem_class() {
		return cItem_class;
	}
	public void setcItem_class(String cItem_class) {
		this.cItem_class = cItem_class;
	}
	public String getCbsysbarcode() {
		return cbsysbarcode;
	}
	public void setCbsysbarcode(String cbsysbarcode) {
		this.cbsysbarcode = cbsysbarcode;
	}
	public String getRowufts() {
		return rowufts;
	}
	public void setRowufts(String rowufts) {
		this.rowufts = rowufts;
	}
	public String getCbMemo() {
		return cbMemo;
	}
	public void setCbMemo(String cbMemo) {
		this.cbMemo = cbMemo;
	}
	public String getCorufts() {
		return corufts;
	}
	public void setCorufts(String corufts) {
		this.corufts = corufts;
	}
	public String getiSumBillQuantity() {
		return iSumBillQuantity;
	}
	public void setiSumBillQuantity(String iSumBillQuantity) {
		this.iSumBillQuantity = iSumBillQuantity;
	}
	public String getDbKeepDate() {
		return dbKeepDate;
	}
	public void setDbKeepDate(String dbKeepDate) {
		this.dbKeepDate = dbKeepDate;
	}
	public String getCbaccounter() {
		return cbaccounter;
	}
	public void setCbaccounter(String cbaccounter) {
		this.cbaccounter = cbaccounter;
	}
	public String getiSum() {
		return iSum;
	}
	public void setiSum(String iSum) {
		this.iSum = iSum;
	}
	public String getChVencode() {
		return chVencode;
	}
	public void setChVencode(String chVencode) {
		this.chVencode = chVencode;
	}
	public String getdMadeDate() {
		return dMadeDate;
	}
	public void setdMadeDate(String dMadeDate) {
		this.dMadeDate = dMadeDate;
	}
	public String getiNQuantity() {
		return iNQuantity;
	}
	public void setiNQuantity(String iNQuantity) {
		this.iNQuantity = iNQuantity;
	}
	public String getcItemCName() {
		return cItemCName;
	}
	public void setcItemCName(String cItemCName) {
		this.cItemCName = cItemCName;
	}
	public String getcName() {
		return cName;
	}
	public void setcName(String cName) {
		this.cName = cName;
	}
	public String getfACost() {
		return fACost;
	}
	public void setfACost(String fACost) {
		this.fACost = fACost;
	}
	public String getiPOsID() {
		return iPOsID;
	}
	public void setiPOsID(String iPOsID) {
		this.iPOsID = iPOsID;
	}
	public String getcItemCode() {
		return cItemCode;
	}
	public void setcItemCode(String cItemCode) {
		this.cItemCode = cItemCode;
	}
	public String getdVDate() {
		return dVDate;
	}
	public void setdVDate(String dVDate) {
		this.dVDate = dVDate;
	}
	public String getiMoney() {
		return iMoney;
	}
	public void setiMoney(String iMoney) {
		this.iMoney = iMoney;
	}
	public String getiSNum() {
		return iSNum;
	}
	public void setiSNum(String iSNum) {
		this.iSNum = iSNum;
	}
	public String getiSQuantity() {
		return iSQuantity;
	}
	public void setiSQuantity(String iSQuantity) {
		this.iSQuantity = iSQuantity;
	}
	public String getdSDate() {
		return dSDate;
	}
	public void setdSDate(String dSDate) {
		this.dSDate = dSDate;
	}
	public String getcBatch() {
		return cBatch;
	}
	public void setcBatch(String cBatch) {
		this.cBatch = cBatch;
	}
	public int getAutoID() {
		return AutoID;
	}
	public void setAutoID(int autoID) {
		AutoID = autoID;
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
	public String getiUnitCost() {
		return iUnitCost;
	}
	public void setiUnitCost(String iUnitCost) {
		this.iUnitCost = iUnitCost;
	}
	public String getiPrice() {
		return iPrice;
	}
	public void setiPrice(String iPrice) {
		this.iPrice = iPrice;
	}
	public String getiAPrice() {
		return iAPrice;
	}
	public void setiAPrice(String iAPrice) {
		this.iAPrice = iAPrice;
	}
	public String getiOriTaxCost() {
		return iOriTaxCost;
	}
	public void setiOriTaxCost(String iOriTaxCost) {
		this.iOriTaxCost = iOriTaxCost;
	}
	public String getiOriCost() {
		return iOriCost;
	}
	public void setiOriCost(String iOriCost) {
		this.iOriCost = iOriCost;
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
	public String getIoriSum() {
		return ioriSum;
	}
	public void setIoriSum(String ioriSum) {
		this.ioriSum = ioriSum;
	}
	public String getiTaxRate() {
		return iTaxRate;
	}
	public void setiTaxRate(String iTaxRate) {
		this.iTaxRate = iTaxRate;
	}
	public String getiTaxPrice() {
		return iTaxPrice;
	}
	public void setiTaxPrice(String iTaxPrice) {
		this.iTaxPrice = iTaxPrice;
	}
	public String getcPOID() {
		return cPOID;
	}
	public void setcPOID(String cPOID) {
		this.cPOID = cPOID;
	}
	public String getDvDate() {
		return dvDate;
	}
	public void setDvDate(String dvDate) {
		this.dvDate = dvDate;
	}
	public String getcPosition() {
		return cPosition;
	}
	public void setcPosition(String cPosition) {
		this.cPosition = cPosition;
	}
	public String getIrowno() {
		return irowno;
	}
	public void setIrowno(String irowno) {
		this.irowno = irowno;
	}

}
