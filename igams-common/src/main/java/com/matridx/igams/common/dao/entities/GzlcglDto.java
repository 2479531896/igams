package com.matridx.igams.common.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="GzlcglDto")
public class GzlcglDto extends GzlcglModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//用户真实姓名
	private String zsxm;
	//申请时间格式化
	private String fsqsj;
	//审核时间格式化
	private String fshsj;
	//确认时上传的附件
	private List<FjcfbDto> fjcfbList;
	//业务名称
	private String ywmc;
	//任务名称
	private String rwmc;
	//审核人
	private String shry;
	//任务ID
	private String rwid;
	//列表排序审核时间
	private String orderShsj;

	public String getOrderShsj() {
		return orderShsj;
	}

	public void setOrderShsj(String orderShsj) {
		this.orderShsj = orderShsj;
	}

	public String getRwid() {
		return rwid;
	}

	public void setRwid(String rwid) {
		this.rwid = rwid;
	}

	public String getShry() {
		return shry;
	}

	public void setShry(String shry) {
		this.shry = shry;
	}

	public String getRwmc() {
		return rwmc;
	}

	public void setRwmc(String rwmc) {
		this.rwmc = rwmc;
	}

	public String getYwmc() {
		return ywmc;
	}

	public void setYwmc(String ywmc) {
		this.ywmc = ywmc;
	}

	public List<FjcfbDto> getFjcfbList()
	{
		return fjcfbList;
	}

	public void setFjcfbList(List<FjcfbDto> fjcfbList)
	{
		this.fjcfbList = fjcfbList;
	}

	public String getZsxm() {
		return zsxm;
	}

	public void setZsxm(String zsxm) {
		this.zsxm = zsxm;
	}

	public String getFsqsj() {
		return fsqsj;
	}

	public void setFsqsj(String fsqsj) {
		this.fsqsj = fsqsj;
	}

	public String getFshsj() {
		return fshsj;
	}

	public void setFshsj(String fshsj) {
		this.fshsj = fshsj;
	}
	
}
