package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="HY_DZ_BorrowOutBacksDto")
public class HY_DZ_BorrowOutBacksDto extends HY_DZ_BorrowOutBacksModel{
    private String UpID;
    private String VoucherType;

    public String getUpID() {
        return UpID;
    }

    public void setUpID(String upID) {
        UpID = upID;
    }

    public String getVoucherType() {
        return VoucherType;
    }

    public void setVoucherType(String voucherType) {
        VoucherType = voucherType;
    }
}
