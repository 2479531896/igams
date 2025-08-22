package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

/**
 * @program: igams
 * @description: 售后功能
 * @create: 2022-08-01 18:42
 **/
@Alias(value="CljlxxDto")
public class CljlxxDto extends CljlxxModel{
    /**
	 * 
	 */
	private static final long serialVersionUID = 5374569358357634139L;
	private String sfjsmc;
    private String lldh;
    public String getLldh() {
        return lldh;
    }

    public void setLldh(String lldh) {
        this.lldh = lldh;
    }

    public String getSfjsmc() {
        return sfjsmc;
    }

    public void setSfjsmc(String sfjsmc) {
        this.sfjsmc = sfjsmc;
    }
}
