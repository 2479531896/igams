package com.matridx.igams.common.dao.entities.external;

import java.util.Map;

/**
 * @author WYX
 * @version 1.0
 * {@code @className} Expense
 * {@code @description} TODO
 * {@code @date} 11:10 2023/4/23
 **/
public class Expense {
    //已付金额币种
    private ConsumeAmount consumeAmount;
    //是否是对公费用
    private boolean corpExpense;
    //费用类型的编码
    private String expenseTypeBizCode;
    //枚举值 对公场景，必填 NO_RECEIPT/ALL_RECEIPTS
    private String corpType;
    //未付金额币种
    private ConsumeAmount nonReceiptAmount;
    //到票时间，必填
    private Long receiptDate;
    //往来单位编码
    private String tradingPartnerBizCode;
    //自定义普通字段
    private Map<String, Object> customObject;
    //备注信息
    private String comments;

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public ConsumeAmount getConsumeAmount() {
        return consumeAmount;
    }

    public void setConsumeAmount(ConsumeAmount consumeAmount) {
        this.consumeAmount = consumeAmount;
    }

    public boolean isCorpExpense() {
        return corpExpense;
    }

    public void setCorpExpense(boolean corpExpense) {
        this.corpExpense = corpExpense;
    }

    public String getExpenseTypeBizCode() {
        return expenseTypeBizCode;
    }

    public void setExpenseTypeBizCode(String expenseTypeBizCode) {
        this.expenseTypeBizCode = expenseTypeBizCode;
    }

    public String getCorpType() {
        return corpType;
    }

    public void setCorpType(String corpType) {
        this.corpType = corpType;
    }

    public ConsumeAmount getNonReceiptAmount() {
        return nonReceiptAmount;
    }

    public void setNonReceiptAmount(ConsumeAmount nonReceiptAmount) {
        this.nonReceiptAmount = nonReceiptAmount;
    }

    public Long getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Long receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getTradingPartnerBizCode() {
        return tradingPartnerBizCode;
    }

    public void setTradingPartnerBizCode(String tradingPartnerBizCode) {
        this.tradingPartnerBizCode = tradingPartnerBizCode;
    }

    public Map<String, Object> getCustomObject() {
        return customObject;
    }

    public void setCustomObject(Map<String, Object> customObject) {
        this.customObject = customObject;
    }
}
