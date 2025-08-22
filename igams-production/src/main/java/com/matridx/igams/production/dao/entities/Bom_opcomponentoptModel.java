package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="Bom_opcomponentoptModel")
public class Bom_opcomponentoptModel {
    private String OptionsId;
    private String Offset;
    private String WIPType;
    private String AccuCostFlag;
    private String DrawDeptCode;
    private String Whcode;
    private String OptionalFlag;
    private String MutexRule;
    private String PlanFactor;
    private String Ufts;
    private String CostWIPRel;
    private String FeatureRel;

    public String getOptionsId() {
        return OptionsId;
    }

    public void setOptionsId(String optionsId) {
        OptionsId = optionsId;
    }

    public String getOffset() {
        return Offset;
    }

    public void setOffset(String offset) {
        Offset = offset;
    }

    public String getWIPType() {
        return WIPType;
    }

    public void setWIPType(String WIPType) {
        this.WIPType = WIPType;
    }

    public String getAccuCostFlag() {
        return AccuCostFlag;
    }

    public void setAccuCostFlag(String accuCostFlag) {
        AccuCostFlag = accuCostFlag;
    }

    public String getDrawDeptCode() {
        return DrawDeptCode;
    }

    public void setDrawDeptCode(String drawDeptCode) {
        DrawDeptCode = drawDeptCode;
    }

    public String getWhcode() {
        return Whcode;
    }

    public void setWhcode(String whcode) {
        Whcode = whcode;
    }

    public String getOptionalFlag() {
        return OptionalFlag;
    }

    public void setOptionalFlag(String optionalFlag) {
        OptionalFlag = optionalFlag;
    }

    public String getMutexRule() {
        return MutexRule;
    }

    public void setMutexRule(String mutexRule) {
        MutexRule = mutexRule;
    }

    public String getPlanFactor() {
        return PlanFactor;
    }

    public void setPlanFactor(String planFactor) {
        PlanFactor = planFactor;
    }

    public String getUfts() {
        return Ufts;
    }

    public void setUfts(String ufts) {
        Ufts = ufts;
    }

    public String getCostWIPRel() {
        return CostWIPRel;
    }

    public void setCostWIPRel(String costWIPRel) {
        CostWIPRel = costWIPRel;
    }

    public String getFeatureRel() {
        return FeatureRel;
    }

    public void setFeatureRel(String featureRel) {
        FeatureRel = featureRel;
    }
}
