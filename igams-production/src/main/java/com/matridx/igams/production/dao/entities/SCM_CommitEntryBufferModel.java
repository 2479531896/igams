package com.matridx.igams.production.dao.entities;

public class SCM_CommitEntryBufferModel {
	//库存表ItemId
	private String ItemId;
	//0201
	private String subject;
	//仓库
	private String cwhcode;
	//物料编码
	private String cinvcode;
	//生产批号
	private String cbatch;
	//""
	private String cVMIVenCode;
	//0
	private String iNum;
	//入库数量
	private String iquantity;
	//0
	private String iBillNum;
	//入库数量
	private String iBillQuantity;
	//spid_266
	private String transactionid;
	//好像取最大自增 ？
	private String sequenceid;
	//入库单类型
	private String DocumentType;
	//入库单ID
	private String DocumentId;
	//入库明细ID
	private String DocumentDId;
	//0
	private String DemandPlanType;
	//生产日期
	private String dMDate;
	//失效日期
	private String dVDate;
	//保质期标记
	private String imassunit;
	//保质期
	private String imassday;
	//""
	private String DemandPlanDId;
	//1
	private String iControlMethod;
	//1
	private String iCheckRuleId;
	//采购类型
	private String cBusType;
	//采购订单
	private String cSource;
	//null
	private String cExpirationdate;
	//null
	private String dExpirationdate;
	//0
	private String iExpiratDateCalcu;
	//0
	private String btrack;
	//1
	private String iControlMethodtrack;
	public String getItemId() {
		return ItemId;
	}
	public void setItemId(String itemId) {
		ItemId = itemId;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getCwhcode() {
		return cwhcode;
	}
	public void setCwhcode(String cwhcode) {
		this.cwhcode = cwhcode;
	}
	public String getCinvcode() {
		return cinvcode;
	}
	public void setCinvcode(String cinvcode) {
		this.cinvcode = cinvcode;
	}
	public String getCbatch() {
		return cbatch;
	}
	public void setCbatch(String cbatch) {
		this.cbatch = cbatch;
	}
	public String getcVMIVenCode() {
		return cVMIVenCode;
	}
	public void setcVMIVenCode(String cVMIVenCode) {
		this.cVMIVenCode = cVMIVenCode;
	}
	public String getiNum() {
		return iNum;
	}
	public void setiNum(String iNum) {
		this.iNum = iNum;
	}
	public String getIquantity() {
		return iquantity;
	}
	public void setIquantity(String iquantity) {
		this.iquantity = iquantity;
	}
	public String getiBillNum() {
		return iBillNum;
	}
	public void setiBillNum(String iBillNum) {
		this.iBillNum = iBillNum;
	}
	public String getiBillQuantity() {
		return iBillQuantity;
	}
	public void setiBillQuantity(String iBillQuantity) {
		this.iBillQuantity = iBillQuantity;
	}
	public String getTransactionid() {
		return transactionid;
	}
	public void setTransactionid(String transactionid) {
		this.transactionid = transactionid;
	}
	public String getSequenceid() {
		return sequenceid;
	}
	public void setSequenceid(String sequenceid) {
		this.sequenceid = sequenceid;
	}

	public String getDocumentType() {
		return DocumentType;
	}
	public void setDocumentType(String documentType) {
		DocumentType = documentType;
	}
	public String getDocumentId() {
		return DocumentId;
	}
	public void setDocumentId(String documentId) {
		DocumentId = documentId;
	}
	public String getDocumentDId() {
		return DocumentDId;
	}
	public void setDocumentDId(String documentDId) {
		DocumentDId = documentDId;
	}
	public String getDemandPlanType() {
		return DemandPlanType;
	}
	public void setDemandPlanType(String demandPlanType) {
		DemandPlanType = demandPlanType;
	}
	public String getdMDate() {
		return dMDate;
	}
	public void setdMDate(String dMDate) {
		this.dMDate = dMDate;
	}
	public String getdVDate() {
		return dVDate;
	}
	public void setdVDate(String dVDate) {
		this.dVDate = dVDate;
	}
	public String getImassunit() {
		return imassunit;
	}
	public void setImassunit(String imassunit) {
		this.imassunit = imassunit;
	}
	public String getImassday() {
		return imassday;
	}
	public void setImassday(String imassday) {
		this.imassday = imassday;
	}
	public String getDemandPlanDId() {
		return DemandPlanDId;
	}
	public void setDemandPlanDId(String demandPlanDId) {
		DemandPlanDId = demandPlanDId;
	}
	public String getiControlMethod() {
		return iControlMethod;
	}
	public void setiControlMethod(String iControlMethod) {
		this.iControlMethod = iControlMethod;
	}
	public String getiCheckRuleId() {
		return iCheckRuleId;
	}
	public void setiCheckRuleId(String iCheckRuleId) {
		this.iCheckRuleId = iCheckRuleId;
	}
	public String getcBusType() {
		return cBusType;
	}
	public void setcBusType(String cBusType) {
		this.cBusType = cBusType;
	}
	public String getcSource() {
		return cSource;
	}
	public void setcSource(String cSource) {
		this.cSource = cSource;
	}
	public String getcExpirationdate() {
		return cExpirationdate;
	}
	public void setcExpirationdate(String cExpirationdate) {
		this.cExpirationdate = cExpirationdate;
	}
	public String getdExpirationdate() {
		return dExpirationdate;
	}
	public void setdExpirationdate(String dExpirationdate) {
		this.dExpirationdate = dExpirationdate;
	}
	public String getiExpiratDateCalcu() {
		return iExpiratDateCalcu;
	}
	public void setiExpiratDateCalcu(String iExpiratDateCalcu) {
		this.iExpiratDateCalcu = iExpiratDateCalcu;
	}
	public String getBtrack() {
		return btrack;
	}
	public void setBtrack(String btrack) {
		this.btrack = btrack;
	}
	public String getiControlMethodtrack() {
		return iControlMethodtrack;
	}
	public void setiControlMethodtrack(String iControlMethodtrack) {
		this.iControlMethodtrack = iControlMethodtrack;
	}
	
}
