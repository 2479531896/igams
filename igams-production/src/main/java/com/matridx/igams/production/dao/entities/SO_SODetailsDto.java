package com.matridx.igams.production.dao.entities;

public class SO_SODetailsDto extends SO_SODetailsModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private String xsid;
    private String xsmxid;
    //汇率
    private String iExchRate;
    //销售表税率
    private String miTaxRate;
    //主表备注
    private String mcMemo;
    //客户代码
    private String khdm;
    //客户名称
    private String cCusName;
    //币种
    private String cexch_name;

    public String getCexch_name() {
        return cexch_name;
    }

    public void setCexch_name(String cexch_name) {
        this.cexch_name = cexch_name;
    }

    public String getcCusName() {
        return cCusName;
    }

    public void setcCusName(String cCusName) {
        this.cCusName = cCusName;
    }

    public String getKhdm() {
        return khdm;
    }

    public void setKhdm(String khdm) {
        this.khdm = khdm;
    }

    public String getMcMemo() {
        return mcMemo;
    }

    public void setMcMemo(String mcMemo) {
        this.mcMemo = mcMemo;
    }

    public String getiExchRate() {
        return iExchRate;
    }

    public void setiExchRate(String iExchRate) {
        this.iExchRate = iExchRate;
    }

    public String getMiTaxRate() {
        return miTaxRate;
    }

    public void setMiTaxRate(String miTaxRate) {
        this.miTaxRate = miTaxRate;
    }

    public String getXsid() {
        return xsid;
    }

    public void setXsid(String xsid) {
        this.xsid = xsid;
    }

    public String getXsmxid() {
        return xsmxid;
    }

    public void setXsmxid(String xsmxid) {
        this.xsmxid = xsmxid;
    }
}
