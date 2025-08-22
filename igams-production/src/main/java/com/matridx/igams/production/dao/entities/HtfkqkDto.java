package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="HtfkqkDto")
public class HtfkqkDto extends HtfkqkModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//操作人
	private String czr;
	//合同总金额
	private String htzje;
	//付款金额（修改前）
	private String fkjexgq;
	//合同内部编号
	private String htnbbh;
	//合同状态
	private String zt;
	//合同创建日期
	private String cjrq;
	// 全部(查询条件)
	private String entire;
	// 全部(查询条件)
	// 创建日期结束日期
	private String fksjend;
	// 创建日期开始日期
	private String fksjstart;

	// 创建日期开始日期
	private String yfje;

	//rabbit标记
	private String prefixFlg;
	//废弃标记（判断删除的数据是否为废弃）
	private String fqbj;
	//open_flg,判断是否为提交操作（submit）
	private String open_flg;
	//付款方名称
	private String fkfmc;
	//付款方式名称
	private String fkfsmc;
	//合同内部编号
	private String htwbbh;
	//合同的申请部门名称
	private String sqbmmc;
	//申请人名称
	private String sqrmc;
	//合同ID
	private String htid;
	//用于存放付款明细数据
	private String fkmxJson;
    private String zfdxmc;
	private String [] zts;
	//付款提醒ID
	private String fktxid;
	//用款部门名称
	private String ykbmmc;
	//审核通过开始时间
	private String shtgsjstart;
	//审核通过结束时间
	private String shtgsjend;
	//合同IDs
	public String htids;
	//单据传达方式名称
	private String djcdfsmc;
	//路径标记
	private String ljbj;
	private String sqlParam;
	//供应商代码
	private String gysdm;
	//机构代码
	private String jgdm;
	//往来单位业务编码
	private String ywbm;

	public String getYwbm() {
		return ywbm;
	}

	public void setYwbm(String ywbm) {
		this.ywbm = ywbm;
	}

	public String getJgdm() {
		return jgdm;
	}

	public void setJgdm(String jgdm) {
		this.jgdm = jgdm;
	}

	public String getGysdm() {
		return gysdm;
	}

	public void setGysdm(String gysdm) {
		this.gysdm = gysdm;
	}

	public String getSqlParam() {
		return sqlParam;
	}

	public void setSqlParam(String sqlParam) {
		this.sqlParam = sqlParam;
	}

	public String getLjbj() {
		return ljbj;
	}

	public void setLjbj(String ljbj) {
		this.ljbj = ljbj;
	}


	public String getDjcdfsmc() {
		return djcdfsmc;
	}

	public void setDjcdfsmc(String djcdfsmc) {
		this.djcdfsmc = djcdfsmc;
	}

	public String getHtids() {
		return htids;
	}

	public void setHtids(String htids) {
		this.htids = htids;
	}

	public String getShtgsjstart() {
		return shtgsjstart;
	}

	public void setShtgsjstart(String shtgsjstart) {
		this.shtgsjstart = shtgsjstart;
	}

	public String getShtgsjend() {
		return shtgsjend;
	}

	public void setShtgsjend(String shtgsjend) {
		this.shtgsjend = shtgsjend;
	}

	public String getYkbmmc() {
		return ykbmmc;
	}

	public void setYkbmmc(String ykbmmc) {
		this.ykbmmc = ykbmmc;
	}

	public String getFktxid() {
		return fktxid;
	}

	public void setFktxid(String fktxid) {
		this.fktxid = fktxid;
	}

	public String[] getZts() {
		return zts;
	}

	public void setZts(String[] zts) {
		this.zts = zts;
		for (int i = 0; i < zts.length; i++){
			this.zts[i]=this.zts[i].replace("'","");
		}
	}

	public String getZfdxmc() {
        return zfdxmc;
    }

    public void setZfdxmc(String zfdxmc) {
        this.zfdxmc = zfdxmc;
    }

	public String getFkmxJson() {
		return fkmxJson;
	}

	public void setFkmxJson(String fkmxJson) {
		this.fkmxJson = fkmxJson;
	}

	public String getHtid() {
		return htid;
	}

	public void setHtid(String htid) {
		this.htid = htid;
	}

	public String getSqrmc() {
		return sqrmc;
	}

	public void setSqrmc(String sqrmc) {
		this.sqrmc = sqrmc;
	}

	public String getHtwbbh() {
		return htwbbh;
	}

	public void setHtwbbh(String htwbbh) {
		this.htwbbh = htwbbh;
	}

	public String getSqbmmc() {
		return sqbmmc;
	}

	public void setSqbmmc(String sqbmmc) {
		this.sqbmmc = sqbmmc;
	}

	public String getFkfmc() {
		return fkfmc;
	}

	public void setFkfmc(String fkfmc) {
		this.fkfmc = fkfmc;
	}

	public String getFkfsmc() {
		return fkfsmc;
	}

	public void setFkfsmc(String fkfsmc) {
		this.fkfsmc = fkfsmc;
	}

	public String getOpen_flg() {
		return open_flg;
	}

	public void setOpen_flg(String open_flg) {
		this.open_flg = open_flg;
	}

	public String getYfje() {
		return yfje;
	}

	public void setYfje(String yfje) {
		this.yfje = yfje;
	}

	public String getFqbj() {
		return fqbj;
	}

	public void setFqbj(String fqbj) {
		this.fqbj = fqbj;
	}

	public String getPrefixFlg() {
		return prefixFlg;
	}

	public void setPrefixFlg(String prefixFlg) {
		this.prefixFlg = prefixFlg;
	}

	public String getFksjend() {
		return fksjend;
	}

	public void setFksjend(String fksjend) {
		this.fksjend = fksjend;
	}

	public String getFksjstart() {
		return fksjstart;
	}

	public void setFksjstart(String fksjstart) {
		this.fksjstart = fksjstart;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getCjrq() {
		return cjrq;
	}

	public void setCjrq(String cjrq) {
		this.cjrq = cjrq;
	}

	public String getHtnbbh() {
		return htnbbh;
	}

	public void setHtnbbh(String htnbbh) {
		this.htnbbh = htnbbh;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getFkjexgq() {
		return fkjexgq;
	}
	public void setFkjexgq(String fkjexgq) {
		this.fkjexgq = fkjexgq;
	}
	public String getHtzje() {
		return htzje;
	}
	public void setHtzje(String htzje) {
		this.htzje = htzje;
	}
	public String getCzr() {
		return czr;
	}
	public void setCzr(String czr) {
		this.czr = czr;
	}
}
