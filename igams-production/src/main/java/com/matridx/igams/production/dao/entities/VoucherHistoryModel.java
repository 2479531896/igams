package com.matridx.igams.production.dao.entities;

public class VoucherHistoryModel {
	//主键ID
	private String AutoId;
	//单据类型编码
	private String CardNumber;
	//收发标志 
	private String iRdFlagSeed;
	//流水号依据 
	private String cContent;
	//流水号规则
	private String cContentRule;
	//种子编码
	private String cSeed;
	//最大已使用流水号
	private String cNumber;
	//现无用 
	private String bEmpty;
	
	
	public String getAutoId() {
		return AutoId;
	}
	public void setAutoId(String autoId) {
		AutoId = autoId;
	}
	public String getCardNumber() {
		return CardNumber;
	}
	public void setCardNumber(String cardNumber) {
		CardNumber = cardNumber;
	}
	public String getiRdFlagSeed() {
		return iRdFlagSeed;
	}
	public void setiRdFlagSeed(String iRdFlagSeed) {
		this.iRdFlagSeed = iRdFlagSeed;
	}
	public String getcContent() {
		return cContent;
	}
	public void setcContent(String cContent) {
		this.cContent = cContent;
	}
	public String getcContentRule() {
		return cContentRule;
	}
	public void setcContentRule(String cContentRule) {
		this.cContentRule = cContentRule;
	}
	public String getcSeed() {
		return cSeed;
	}
	public void setcSeed(String cSeed) {
		this.cSeed = cSeed;
	}
	public String getcNumber() {
		return cNumber;
	}
	public void setcNumber(String cNumber) {
		this.cNumber = cNumber;
	}
	public String getbEmpty() {
		return bEmpty;
	}
	public void setbEmpty(String bEmpty) {
		this.bEmpty = bEmpty;
	}
	
	
}
