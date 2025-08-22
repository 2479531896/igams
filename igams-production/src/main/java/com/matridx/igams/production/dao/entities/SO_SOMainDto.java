package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="SO_SOMainDto")
public class SO_SOMainDto extends SO_SOMainModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private String sqrqstart;
    private String sqrqend;
    private String xsid;
    //时间戳
    private String prefix;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getXsid() {
        return xsid;
    }

    public void setXsid(String xsid) {
        this.xsid = xsid;
    }

    public String getSqrqstart() {
        return sqrqstart;
    }

    public void setSqrqstart(String sqrqstart) {
        this.sqrqstart = sqrqstart;
    }

    public String getSqrqend() {
        return sqrqend;
    }

    public void setSqrqend(String sqrqend) {
        this.sqrqend = sqrqend;
    }
}
