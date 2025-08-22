package com.matridx.igams.common.dao.entities;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
/**
 * 审核参数（用于审核回调时传递的参数信息）
 */
public class AuditParam implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private boolean batchOpe;	//是否是批量操作
	private boolean commitOpe; //是否是提交操作
	private boolean cancelOpe; //是否是取消审核操作
	private Map<String,String> errMap;//错误提示Map集合
	private Map<String,String> sucMap;//成功提示的Map信息集合

	private Map<String,String> backMap;//审核返回信息
	private boolean passOpe; //是否是通过审核操作

	private ShgcDto shgcDto;//审核过程
	
	public AuditParam() {
	}
	
	public AuditParam(boolean batchOpe, boolean commitOpe, boolean cancelOpe, boolean passOpe) {
		super();
		this.commitOpe = commitOpe;
		this.batchOpe = batchOpe;
		this.cancelOpe = cancelOpe;
		this.passOpe = passOpe;
		this.backMap = new HashMap<>();
		this.errMap = new HashMap<>();
		this.sucMap = new HashMap<>();
	}

	public boolean isPassOpe() {
		return passOpe;
	}

	public void setPassOpe(boolean passOpe) {
		this.passOpe = passOpe;
	}

	
	public boolean isBatchOpe() {
		return batchOpe;
	}

	public void setBatchOpe(boolean batchOpe) {
		this.batchOpe = batchOpe;
	}

	public boolean isCommitOpe() {
		return commitOpe;
	}

	public void setCommitOpe(boolean commitOpe) {
		this.commitOpe = commitOpe;
	}

	public boolean isCancelOpe() {
		return cancelOpe;
	}

	public void setCancelOpe(boolean cancelOpe) {
		this.cancelOpe = cancelOpe;
	}

	public void setErrMap(Map<String,String> errMap) {
		this.errMap = errMap;
	}

	public Map<String,String> getErrMap() {
		return errMap;
	}

	public Map<String,String> getSucMap() {
		return sucMap;
	}

	public void setSucMap(Map<String,String> sucMap) {
		this.sucMap = sucMap;
	}

	public Map<String,String> getBackMap() {
		return backMap;
	}

	public void setBackMap(Map<String,String> backMap) {
		this.backMap = backMap;
	}

	public ShgcDto getShgcDto() {
		return shgcDto;
	}

	public void setShgcDto(ShgcDto shgcDto) {
		this.shgcDto = shgcDto;
	}
	
}
