package com.matridx.igams.common.dao.entities.external;

/**
 * @author WYX
 * @version 1.0
 * {@code @className} PayeeAccount
 * {@code @description} 	收款账户信息
 * {@code @date} 11:46 2023/4/23
 **/
public class PayeeAccount {
    //账户名
    private String bankAcctName;
    //账号
    private String bankAcctNumber;
    //支付类型;ALIPAY-支付宝,BANK-银行账户,CASH-现金
    private String paymentType;
    //账户性质，个人-PERSONAL,公司-CORP
    private String accountType;

    public String getBankAcctName() {
        return bankAcctName;
    }

    public void setBankAcctName(String bankAcctName) {
        this.bankAcctName = bankAcctName;
    }

    public String getBankAcctNumber() {
        return bankAcctNumber;
    }

    public void setBankAcctNumber(String bankAcctNumber) {
        this.bankAcctNumber = bankAcctNumber;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
