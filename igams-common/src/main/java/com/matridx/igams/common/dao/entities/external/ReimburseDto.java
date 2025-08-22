package com.matridx.igams.common.dao.entities.external;

import java.util.List;
import java.util.Map;

/**
 * @author WYX
 * @version 1.0
 * {@code @className} ReimburseDto
 * {@code @description} 报销单导入类
 * {@code @date} 11:39 2023/4/23
 **/
public class ReimburseDto {
    //单据Code
    private String formCode;
    //系统字段
    private String formSubTypeBizCode;
    //提单人的工号
    private String submittedUserEmployeeId;
    //报销单事由
    private String reimburseName;
    //公司抬头业务编码
    private String legalEntityBizCode;
    //承担人工号
    private String coverUserEmployeeId;
    //承担部门业务编码
    private String coverDepartmentBizCode;
    //提单人部门
    private String requestDepartment;
    //最晚支付日期
    private Long CF49;
    //备注信息
    private String comments;
    //收款账户信息
    private PayeeAccount payeeAccount;
    //多人收款场景表单业务编码
    private String paymentSceneBizCode;
    //费用的每刻内码Code列表
    private List<String> expenseCodes;
    //自定义普通字段
    private Map<String, Object> customObject;
    //往来单位（供应商）业务编码
    private String tradingPartnerBizCode;
    //账户名
    private String bankAcctName;
    //账户类型
    private String accountType;
    //开户行
    private String bankName;
    //分支行（国内银行需要）
    private String bankBranchName;
    //支付类型
    private String paymentType;
    //银行账号
    private String bankAcctNumber;
    //提单时间
    private Long submittedTime;
    //暂存标识 若导入报销单stagingFlag(暂存状态)为true，则只校验费用S级字段必填（对私：消费日期、费用金额、费用类型编码；对公：基于对私，增加到票时间、业务场景、是否对公），其他必填字段由提单人手动添加并提交。
    private boolean stagingFlag;
    //报销单关联的申请单号
    private List<String> preConsumeCodeList;

    public String getFormCode() {
        return formCode;
    }

    public void setFormCode(String formCode) {
        this.formCode = formCode;
    }

    public String getFormSubTypeBizCode() {
        return formSubTypeBizCode;
    }

    public void setFormSubTypeBizCode(String formSubTypeBizCode) {
        this.formSubTypeBizCode = formSubTypeBizCode;
    }

    public String getSubmittedUserEmployeeId() {
        return submittedUserEmployeeId;
    }

    public void setSubmittedUserEmployeeId(String submittedUserEmployeeId) {
        this.submittedUserEmployeeId = submittedUserEmployeeId;
    }

    public String getReimburseName() {
        return reimburseName;
    }

    public void setReimburseName(String reimburseName) {
        this.reimburseName = reimburseName;
    }

    public String getLegalEntityBizCode() {
        return legalEntityBizCode;
    }

    public void setLegalEntityBizCode(String legalEntityBizCode) {
        this.legalEntityBizCode = legalEntityBizCode;
    }

    public String getCoverUserEmployeeId() {
        return coverUserEmployeeId;
    }

    public void setCoverUserEmployeeId(String coverUserEmployeeId) {
        this.coverUserEmployeeId = coverUserEmployeeId;
    }

    public String getCoverDepartmentBizCode() {
        return coverDepartmentBizCode;
    }

    public void setCoverDepartmentBizCode(String coverDepartmentBizCode) {
        this.coverDepartmentBizCode = coverDepartmentBizCode;
    }

    public String getRequestDepartment() {
        return requestDepartment;
    }

    public void setRequestDepartment(String requestDepartment) {
        this.requestDepartment = requestDepartment;
    }

    public Long getCF49() {
        return CF49;
    }

    public void setCF49(Long CF49) {
        this.CF49 = CF49;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public PayeeAccount getPayeeAccount() {
        return payeeAccount;
    }

    public void setPayeeAccount(PayeeAccount payeeAccount) {
        this.payeeAccount = payeeAccount;
    }

    public String getPaymentSceneBizCode() {
        return paymentSceneBizCode;
    }

    public void setPaymentSceneBizCode(String paymentSceneBizCode) {
        this.paymentSceneBizCode = paymentSceneBizCode;
    }

    public List<String> getExpenseCodes() {
        return expenseCodes;
    }

    public void setExpenseCodes(List<String> expenseCodes) {
        this.expenseCodes = expenseCodes;
    }

    public Map<String, Object> getCustomObject() {
        return customObject;
    }

    public void setCustomObject(Map<String, Object> customObject) {
        this.customObject = customObject;
    }

    public String getTradingPartnerBizCode() {
        return tradingPartnerBizCode;
    }

    public void setTradingPartnerBizCode(String tradingPartnerBizCode) {
        this.tradingPartnerBizCode = tradingPartnerBizCode;
    }

    public String getBankAcctName() {
        return bankAcctName;
    }

    public void setBankAcctName(String bankAcctName) {
        this.bankAcctName = bankAcctName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankBranchName() {
        return bankBranchName;
    }

    public void setBankBranchName(String bankBranchName) {
        this.bankBranchName = bankBranchName;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getBankAcctNumber() {
        return bankAcctNumber;
    }

    public void setBankAcctNumber(String bankAcctNumber) {
        this.bankAcctNumber = bankAcctNumber;
    }

    public Long getSubmittedTime() {
        return submittedTime;
    }

    public void setSubmittedTime(Long submittedTime) {
        this.submittedTime = submittedTime;
    }

    public boolean isStagingFlag() {
        return stagingFlag;
    }

    public void setStagingFlag(boolean stagingFlag) {
        this.stagingFlag = stagingFlag;
    }

    public List<String> getPreConsumeCodeList() {
        return preConsumeCodeList;
    }

    public void setPreConsumeCodeList(List<String> preConsumeCodeList) {
        this.preConsumeCodeList = preConsumeCodeList;
    }
}
