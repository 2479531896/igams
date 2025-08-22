package com.matridx.igams.common.dao.entities.external;

/**
 * @author WYX
 * @version 1.0
 * {@code @className} ConsumeAmount
 * {@code @description} TODO
 * {@code @date} 11:13 2023/4/23
 **/
public class ConsumeAmount {
    //费用金额
    private Double amount;
    //币种
    private String currency;

    public ConsumeAmount(Double amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
