package com.matridx.igams.common.dao.entities;

import java.util.List;

import com.matridx.igams.common.dao.BaseModel;

public class ImportRecordModel extends BaseModel {
	
	private static final long serialVersionUID = 579080772926079605L;
	
	//文件数
	private int fileNum;
	//行数
	private int rowNum;
	//列数
	private int columnNum;
	//消息类型
	private String messType;
	//检查类型
	private String checkType;
	//字段信息
	private String value;
	//是否重复
	private boolean isRepeat;
	//重复传值
	private String repeatId;
	
	//物料类别
	private String wllb;
	//物料子类别参数扩展1
	private String zlbcskz1;
	//标本类型
	private String yblx;
	
	//错误  --在后面的判断中使用
	private String delayErr;
	private List<String> pm;//排名
	//文件分类
	private String wjfl;
	//物料子类别参数扩展2
	private String zlbcskz2;

	public String getZlbcskz2() {
		return zlbcskz2;
	}

	public void setZlbcskz2(String zlbcskz2) {
		this.zlbcskz2 = zlbcskz2;
	}

	public String getZlbcskz1() {
		return zlbcskz1;
	}
	public void setZlbcskz1(String zlbcskz1) {
		this.zlbcskz1 = zlbcskz1;
	}
	public String getWjfl() {
		return wjfl;
	}
	public void setWjfl(String wjfl) {
		this.wjfl = wjfl;
	}
	public List<String> getPm() {
		return pm;
	}
	public void setPm(List<String> pm) {
		this.pm = pm;
	}
	public String getDelayErr() {
		return delayErr;
	}
	public void setDelayErr(String delayErr) {
		this.delayErr = delayErr;
	}
	public int getRowNum() {
		return rowNum;
	}
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}
	public String getMessType() {
		return messType;
	}
	public void setMessType(String messType) {
		this.messType = messType;
	}
	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}
	public String getCheckType() {
		return checkType;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}
	public void setFileNum(int fileNum) {
		this.fileNum = fileNum;
	}
	public int getFileNum() {
		return fileNum;
	}
	public void setColumnNum(int columnNum) {
		this.columnNum = columnNum;
	}
	public int getColumnNum() {
		return columnNum;
	}
	public boolean isRepeat() {
		return isRepeat;
	}
	public void setRepeat(boolean isRepeat) {
		this.isRepeat = isRepeat;
	}
	public String getRepeatId() {
		return repeatId;
	}
	public void setRepeatId(String repeatId) {
		this.repeatId = repeatId;
	}
	public String getWllb() {
		return wllb;
	}
	public void setWllb(String wllb) {
		this.wllb = wllb;
	}
	public String getYblx() {
		return yblx;
	}
	public void setYblx(String yblx) {
		this.yblx = yblx;
	}
}
