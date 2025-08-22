package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="NyypxxDto")
public class NyypxxDto extends NyypxxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<String> nyzwms;

    public List<String> getNyzwms() {
        return nyzwms;
    }

    public void setNyzwms(List<String> nyzwms) {
        this.nyzwms = nyzwms;
    }
}
