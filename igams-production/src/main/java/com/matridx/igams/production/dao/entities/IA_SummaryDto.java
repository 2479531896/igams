package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="IA_SummaryDto")
public class IA_SummaryDto extends IA_SummaryModel{
    private List<String> cInvCodes;

    public List<String> getcInvCodes() {
        return cInvCodes;
    }

    public void setcInvCodes(List<String> cInvCodes) {
        this.cInvCodes = cInvCodes;
    }
}
