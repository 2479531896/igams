package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="TyszmxDto")
public class TyszmxDto extends TyszmxModel implements Cloneable{

	//明细list
	private Object tyszmxDtos;

	//基础类别ID
	private String jclbid;

	//二级资源ID
	private String ejzyid;
	//父标题
	private String fbt;
	//父内容
	private String fnr;
	//父标题ID
	private String fbtid;

	public String getFbtid() {
		return fbtid;
	}

	public void setFbtid(String fbtid) {
		this.fbtid = fbtid;
	}

	public String getFbt() {
		return fbt;
	}

	public void setFbt(String fbt) {
		this.fbt = fbt;
	}

	public String getFnr() {
		return fnr;
	}

	public void setFnr(String fnr) {
		this.fnr = fnr;
	}

	public String getEjzyid() {
		return ejzyid;
	}

	public void setEjzyid(String ejzyid) {
		this.ejzyid = ejzyid;
	}

	public String getJclbid() {
		return jclbid;
	}

	public void setJclbid(String jclbid) {
		this.jclbid = jclbid;
	}

	@Override
	public TyszmxDto clone() throws CloneNotSupportedException {
		return (TyszmxDto)super.clone();
	}

	public Object getTyszmxDtos() {
		return tyszmxDtos;
	}

	public void setTyszmxDtos(Object tyszmxDtos) {
		this.tyszmxDtos = tyszmxDtos;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
