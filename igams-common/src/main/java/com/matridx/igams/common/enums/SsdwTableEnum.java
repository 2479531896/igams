package com.matridx.igams.common.enums;
/**
 * 所属单位表枚举类(用于多个所属单位数据权限控制用）
 * @author xinyf
 * @date 2015年7月23日
 */
public enum SsdwTableEnum {
	//xhCol 为空则代表为本数据表，无需关联
	//table,relCol,ssdwCol,xhCol,xhVal,llhCol
	WJGL("igams_wjssdw","wjid","jgid","xh","1",null),//文件所属单位表
	YBSQGL("igams_ybsqssdw","sqid","jgid","xh","1",null),//标本申请所属单位表
	LLSQGL("igams_hwllxx","llid","sqbm",null,null,null),//领料申请所属单位表
	QGGL("igams_qggl","qgid","sqbm",null,null,null),//请购申请所属单位表
	QGQXGL("igams_qgqxgl","qgqxid","sqbm",null,null,null),//请购取消申请所属单位表
	LLGL("igams_llgl","llid","sqbm",null,null,null),//领料申请所属单位表
	JCJYGL("igams_jcjygl","jcjyid","bm",null,null,null),//借出借用所属单位表
	JCGHGL("igams_jcghgl","jcghid","bm",null,null,null),//借出归还所属单位表
	CKGL("igams_ckgl","xkid","bm",null,null,null),//出库所属单位表
	JXMB("igams_jxmb","jxmbid","bm",null,null,null),//绩效目标所属单位表
	GRJX("igams_grjx","grjxid","bm",null,null,null),//个人绩效所属单位表
	SBJL("igams_sbjl","sbjlid","sqbm",null,null,null),//设备计量所属单位表
	SBYZ("igams_sbyz","sbyzid","sqbm",null,null,null),//设备验证所属单位表
	SBWX("igams_sbwx","sbwxid","sqbm",null,null,null),//设备维修所属单位表
	SBTK("igams_sbtk","sbtkid","sqbm",null,null,null),//设备退库所属单位表
	SBBF("igams_sbbf","sbbfid","sqbm",null,null,null),//设备报废所属单位表
	SBYJll("igams_sbyjll","llid","sybm",null,null,null),//设备移交所属单位表
	SBYS("igams_sbys","sbysid","xsybm",null,null,null),//设备维修所属单位表
	SBPD("igams_sbpd","sbpdid","bm",null,null,null),//设备盘点所属单位表
	YSGL("igams_ysgl","ysglid","bm",null,null,null),//预算管理所属单位表
	;
	
	private String table;	//所属单位表
	private String relCol;	//关联字段
	private String ssdwCol;//所属单位字段
	private String xhCol;	//序号字段
	private String xhVal;	//用于审核时，多个所属单位中哪个可以审核
	private String llhCol;	//履历号字段，当所属单位数据存放在履历表时，需通过履历号管理当前履历的单位数据

	SsdwTableEnum(String table, String relCol, String ssdwCol,
                  String xhCol, String xhVal, String llhCol) {
		this.table = table;
		this.relCol = relCol;
		this.ssdwCol = ssdwCol;
		this.xhCol = xhCol;
		this.setXhVal(xhVal);
		this.llhCol = llhCol;
	}

	public String getRelCol() {
		return relCol;
	}

	public String getSsdwCol() {
		return ssdwCol;
	}

	public String getXhCol() {
		return xhCol;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public void setRelCol(String relCol) {
		this.relCol = relCol;
	}

	public void setSsdwCol(String ssdwCol) {
		this.ssdwCol = ssdwCol;
	}

	public void setXhCol(String xhCol) {
		this.xhCol = xhCol;
	}
	
	public String getTable() {
		return table;
	}

	public String getXhVal() {
		return xhVal;
	}

	public void setXhVal(String xhVal) {
		this.xhVal = xhVal;
	}

	public String getLlhCol() {
		return llhCol;
	}

	public void setLlhCol(String llhCol) {
		this.llhCol = llhCol;
	}
}
