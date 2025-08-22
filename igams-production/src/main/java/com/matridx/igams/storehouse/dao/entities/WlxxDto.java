package com.matridx.igams.storehouse.dao.entities;

import com.matridx.igams.common.dao.entities.FjcfbDto;
import org.apache.ibatis.type.Alias;

import java.util.List;


@Alias(value="WlxxDto")
public class WlxxDto extends WlxxModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//业务类型
	private String ywlx;
	//物流信息json
	private String wlmx_json;
	//业务单号
	private String ywdh;
	//附件ID复数
	private List<String> fjids;
	//附件标记
	private String fjbj;
	List<FjcfbDto> fjcfbDtos;
	//发货时间
	private String fhsj;
	//消息标记 不为空不发送 为空发送
	private String xxbj;

	public String getXxbj() {
		return xxbj;
	}

	public void setXxbj(String xxbj) {
		this.xxbj = xxbj;
	}

	public String getFhsj() {
		return fhsj;
	}

	public void setFhsj(String fhsj) {
		this.fhsj = fhsj;
	}

	public List<FjcfbDto> getFjcfbDtos() {
		return fjcfbDtos;
	}

	public void setFjcfbDtos(List<FjcfbDto> fjcfbDtos) {
		this.fjcfbDtos = fjcfbDtos;
	}

	public String getFjbj() {
		return fjbj;
	}

	public void setFjbj(String fjbj) {
		this.fjbj = fjbj;
	}

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}

	public String getYwdh() {
		return ywdh;
	}

	public void setYwdh(String ywdh) {
		this.ywdh = ywdh;
	}

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public String getWlmx_json() {
		return wlmx_json;
	}

	public void setWlmx_json(String wlmx_json) {
		this.wlmx_json = wlmx_json;
	}
}
