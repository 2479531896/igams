package com.matridx.igams.experiment.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="JdmbDto")
public class JdmbDto extends JdmbModel{
	//负责人名称
	private String fzrmc;
	
	public String getFzrmc()
	{
		return fzrmc;
	}

	public void setFzrmc(String fzrmc)
	{
		this.fzrmc = fzrmc;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
