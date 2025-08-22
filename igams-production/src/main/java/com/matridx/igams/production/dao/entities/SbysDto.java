package com.matridx.igams.production.dao.entities;

import com.matridx.springboot.util.base.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.type.Alias;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Alias(value="SbysDto")
public class SbysDto extends SbysModel {

	//物料编码
	private String wlbm;
	//物料名称
	private String wlmc;
	//生产商
	private String scs;
	//合同内部编号
	private String htnbbh;
	//供应商名称
	private String gysmc;
	//电话
	private String dh;
	//申请部门
	private String sqbm;
	//单据号
	private String djh;
	//所属研发项目
	private String ssyfxm;
	//申请人名称
	private String sqrmc;
	//申请部门名称
	private String sqbmmc;
	//所属研发项目名称
	private String ssyfxmmc;
	//到货日期
	private String dhrq;
	private String dhrqstart;
	private String dhrqend;
	private String ysrqstart;
	private String ysrqend;
	private String[] zts;
	private String[] ssyfxms;
	private String[] ysjgs;
	//入库状态
	private String rkzt;
	//规格
	private String gg;
	//是否固定资产
	private String sfgdzc;
	//请购ID
	private String qgid;
	//到货单号
	private String dhdh;
	//生产批号
	private String scph;
	//修改前固定资产编号
	private String xgqgdzcbh;
	//物料id
	private String wlid;
	private String dhsl;
	//现使用人
	private String xsyrmc;
	//设备状态多
	private String[] sbzts;
	//盘库状态
	private String[] pkzts;
	//设备类型多
	private String[] sblxs;
	//现使用部门多
	private String[] xsybms;
	//领用时间开始
	private String lysjstart;
	//领用时间结束
	private String lysjend;
	private String entire;
	private String glrymc;
	private String xsybmmc;
	private String bz;//备注
	private String xsyrddid;//使用人员ddid
	private String sblxmc;
	private String glryddid;//管理人员ddid
	private String lrrymc;
	private String yygdzcbh;
	private String yysbbh;
	private String xsyrbj;
	private String xsyryhm;
	private String glryyhm;
	private String sqlParam;
	private String flag;
	private String lx;
	private String ysrmc;	    //验收人
	private String[] sfjls;//是否计量多
	private String jlrqstart;
	private String jlrqend;
	private String xcjlrqstart;
	private String xcjlrqend;
	private String[] sfyzs;//是否验证多
	private String yzrqstart;//验证日期开始
	private String yzrqend;//验证日期结束
	private String xcyzrqstart;///下次验证日期开始
	private String xcyzrqend;//下次验证日期结束
	private String byrqstart;//保养日期开始
	private String byrqend;//保养日期结束
	private String xcbyrqstart;//下次保养日期开始
	private String xcbyrqend;//下次保养日期结束
	private String qyrqstart;//启用日期开始
	private String qyrqend;//启用日期结束
	private String[] sybms;//使用部门多
	private String[] zxdnrs;//装箱单多
	private String[] sbcss;//设备参数
	private String[] yqgnyzs;//仪器功能运转
	private String[] sjccs;//数据存储
	private String[] sjwzxhbmxs;//数据完整性和保密性
	private String[] sjclhjss;//数据处理和检索
	private String[] sfxyxnyzs;//是否需要性能验证
	private String[] ysjls;//验收结论多
	private String[] sjcss;//数据传输多
	private String jlzqstart;//计量周期开始
	private String jlzqend;//计量周期结束
	private String byzqstart;//保养周期开始
	private String byzqend;//保养周期结束
	private String yzzqstart;//验证周期开始
	private String yzzqend;//验证周期结束
	private String[] xnyss;//性能验证多
	private String xcjlsjstart;
	private String xcjlsjend;
	private String sbbmfzrmc;
	private String xcyzsjstart;
	private String xcyzsjend;
	private String xcbysjstart;
	private String xcbysjend;
	private List<String> fjids;
	private String yjry;//移交人员
	private String sblxdm;//设备类型代码
	private String sfbmxz;//是否部门限制
	private String qwwcsj;//期望完成时间
	private String czflag;//设备闲置时候的标志
	private String _key;
	private String sqsj;//申请时间
	private String sbjlid;//设备计量id
	private String sbyzid;//设备验证id
	private String dsrwFlag;//定时任务标记
	private String cxFlag;//查询标记
	private String yhm;//用户名
	private String dsrwFzr;//定时任务负责人
	private String dsrwGlry;//定时任务管理人员
	private String sfgzmc;
	private String sblxcskz1;//设备类型参数扩展1
	//退库标记
	private String tkbj;
	//盘点状态多
	private String[] pdzts;
	//部门ID
	private String bmid;
	//钉钉ID
	private String ddid;

	public String getDdid() {
		return ddid;
	}

	public void setDdid(String ddid) {
		this.ddid = ddid;
	}

	public String getBmid() {
		return bmid;
	}

	public void setBmid(String bmid) {
		this.bmid = bmid;
	}

	public String[] getPdzts() {
		return pdzts;
	}

	public void setPdzts(String[] pdzts) {
		this.pdzts = pdzts;
	}

	public String getTkbj() {
		return tkbj;
	}

	public void setTkbj(String tkbj) {
		this.tkbj = tkbj;
	}
	public String getSblxcskz1() {
		return sblxcskz1;
	}

	public void setSblxcskz1(String sblxcskz1) {
		this.sblxcskz1 = sblxcskz1;
	}

	public String getSfgzmc() {
		return sfgzmc;
	}

	public void setSfgzmc(String sfgzmc) {
		this.sfgzmc = sfgzmc;
	}


	public String getDsrwFzr() {
		return dsrwFzr;
	}

	public void setDsrwFzr(String dsrwFzr) {
		this.dsrwFzr = dsrwFzr;
	}

	public String getDsrwGlry() {
		return dsrwGlry;
	}

	public void setDsrwGlry(String dsrwGlry) {
		this.dsrwGlry = dsrwGlry;
	}

	public String getYhm() {
		return yhm;
	}

	public void setYhm(String yhm) {
		this.yhm = yhm;
	}

	public String getCxFlag() {
		return cxFlag;
	}

	public void setCxFlag(String cxFlag) {
		this.cxFlag = cxFlag;
	}

	public String getDsrwFlag() {
		return dsrwFlag;
	}

	public void setDsrwFlag(String dsrwFlag) {
		this.dsrwFlag = dsrwFlag;
	}

	public String getSbjlid() {
		return sbjlid;
	}

	public void setSbjlid(String sbjlid) {
		this.sbjlid = sbjlid;
	}

	public String getSbyzid() {
		return sbyzid;
	}

	public void setSbyzid(String sbyzid) {
		this.sbyzid = sbyzid;
	}

	public String getSqsj() {
		return sqsj;
	}

	public void setSqsj(String sqsj) {
		this.sqsj = sqsj;
	}

	public String get_key() {
		return _key;
	}

	public void set_key(String _key) {
		this._key = _key;
	}

	public String getCzflag() {
		return czflag;
	}

	public void setCzflag(String czflag) {
		this.czflag = czflag;
	}

	public String getQwwcsj() {
		return qwwcsj;
	}

	public void setQwwcsj(String qwwcsj) {
		this.qwwcsj = qwwcsj;
	}

	public String getSblxdm() {
		return sblxdm;
	}

	public void setSblxdm(String sblxdm) {
		this.sblxdm = sblxdm;
	}

	public String getSfbmxz() {
		return sfbmxz;
	}

	public void setSfbmxz(String sfbmxz) {
		this.sfbmxz = sfbmxz;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}

	public String getYjry() {
		return yjry;
	}

	public void setYjry(String yjry) {
		this.yjry = yjry;
	}

	public List<String> getFjids() {
        return fjids;
    }

	public String getXcjlsjstart() {
		return xcjlsjstart;
	}

	public void setXcjlsjstart(String xcjlsjstart) {
		this.xcjlsjstart = xcjlsjstart;
	}

	public String getXcjlsjend() {
		return xcjlsjend;
	}

	public void setXcjlsjend(String xcjlsjend) {
		this.xcjlsjend = xcjlsjend;
	}

	public String getSbbmfzrmc() {
		return sbbmfzrmc;
	}

	public void setSbbmfzrmc(String sbbmfzrmc) {
		this.sbbmfzrmc = sbbmfzrmc;
	}

	public String getXcyzsjstart() {
		return xcyzsjstart;
	}

	public void setXcyzsjstart(String xcyzsjstart) {
		this.xcyzsjstart = xcyzsjstart;
	}

	public String getXcyzsjend() {
		return xcyzsjend;
	}

	public void setXcyzsjend(String xcyzsjend) {
		this.xcyzsjend = xcyzsjend;
	}

	public String getXcbysjstart() {
		return xcbysjstart;
	}

	public void setXcbysjstart(String xcbysjstart) {
		this.xcbysjstart = xcbysjstart;
	}

	public String getXcbysjend() {
		return xcbysjend;
	}

	public void setXcbysjend(String xcbysjend) {
		this.xcbysjend = xcbysjend;
	}

	public String[] getZxdnrs() {
		return zxdnrs;
	}

	public void setZxdnrs(String[] zxdnrs) {
		this.zxdnrs = zxdnrs;
	}

	public String[] getXnyss() {
		return xnyss;
	}

	public void setXnyss(String[] xnyss) {
		this.xnyss = xnyss;
	}

	public String getJlzqstart() {
		return jlzqstart;
	}

	public void setJlzqstart(String jlzqstart) {
		this.jlzqstart = jlzqstart;
	}

	public String getJlzqend() {
		return jlzqend;
	}

	public void setJlzqend(String jlzqend) {
		this.jlzqend = jlzqend;
	}

	public String getByzqstart() {
		return byzqstart;
	}

	public void setByzqstart(String byzqstart) {
		this.byzqstart = byzqstart;
	}

	public String getByzqend() {
		return byzqend;
	}

	public void setByzqend(String byzqend) {
		this.byzqend = byzqend;
	}

	public String getYzzqstart() {
		return yzzqstart;
	}

	public void setYzzqstart(String yzzqstart) {
		this.yzzqstart = yzzqstart;
	}

	public String getYzzqend() {
		return yzzqend;
	}

	public void setYzzqend(String yzzqend) {
		this.yzzqend = yzzqend;
	}

	public String[] getSjcss() {
		return sjcss;
	}

	public void setSjcss(String[] sjcss) {
		this.sjcss = sjcss;
	}

	public String[] getSfjls() {
		return sfjls;
	}

	public void setSfjls(String[] sfjls) {
		this.sfjls = sfjls;
	}


	public String getJlrqstart() {
		return jlrqstart;
	}

	public void setJlrqstart(String jlrqstart) {
		this.jlrqstart = jlrqstart;
	}

	public String getJlrqend() {
		return jlrqend;
	}

	public void setJlrqend(String jlrqend) {
		this.jlrqend = jlrqend;
	}

	public String getXcjlrqstart() {
		return xcjlrqstart;
	}

	public void setXcjlrqstart(String xcjlrqstart) {
		this.xcjlrqstart = xcjlrqstart;
	}

	public String getXcjlrqend() {
		return xcjlrqend;
	}

	public void setXcjlrqend(String xcjlrqend) {
		this.xcjlrqend = xcjlrqend;
	}

	public String[] getSfyzs() {
		return sfyzs;
	}

	public void setSfyzs(String[] sfyzs) {
		this.sfyzs = sfyzs;
	}



	public String getYzrqstart() {
		return yzrqstart;
	}

	public void setYzrqstart(String yzrqstart) {
		this.yzrqstart = yzrqstart;
	}

	public String getYzrqend() {
		return yzrqend;
	}

	public void setYzrqend(String yzrqend) {
		this.yzrqend = yzrqend;
	}

	public String getXcyzrqstart() {
		return xcyzrqstart;
	}

	public void setXcyzrqstart(String xcyzrqstart) {
		this.xcyzrqstart = xcyzrqstart;
	}

	public String getXcyzrqend() {
		return xcyzrqend;
	}

	public void setXcyzrqend(String xcyzrqend) {
		this.xcyzrqend = xcyzrqend;
	}

	public String getByrqstart() {
		return byrqstart;
	}

	public void setByrqstart(String byrqstart) {
		this.byrqstart = byrqstart;
	}

	public String getByrqend() {
		return byrqend;
	}

	public void setByrqend(String byrqend) {
		this.byrqend = byrqend;
	}

	public String getXcbyrqstart() {
		return xcbyrqstart;
	}

	public void setXcbyrqstart(String xcbyrqstart) {
		this.xcbyrqstart = xcbyrqstart;
	}

	public String getXcbyrqend() {
		return xcbyrqend;
	}

	public void setXcbyrqend(String xcbyrqend) {
		this.xcbyrqend = xcbyrqend;
	}

	public String getQyrqstart() {
		return qyrqstart;
	}

	public void setQyrqstart(String qyrqstart) {
		this.qyrqstart = qyrqstart;
	}

	public String getQyrqend() {
		return qyrqend;
	}

	public void setQyrqend(String qyrqend) {
		this.qyrqend = qyrqend;
	}

	public String[] getSybms() {
		return sybms;
	}

	public void setSybms(String[] sybms) {
		this.sybms = sybms;
	}

	public String[] getSbcss() {
		return sbcss;
	}

	public void setSbcss(String[] sbcss) {
		this.sbcss = sbcss;
	}

	public String[] getYqgnyzs() {
		return yqgnyzs;
	}

	public void setYqgnyzs(String[] yqgnyzs) {
		this.yqgnyzs = yqgnyzs;
	}
	public String[] getSjccs() {
		return sjccs;
	}

	public void setSjccs(String[] sjccs) {
		this.sjccs = sjccs;
	}

	public String[] getSjwzxhbmxs() {
		return sjwzxhbmxs;
	}

	public void setSjwzxhbmxs(String[] sjwzxhbmxs) {
		this.sjwzxhbmxs = sjwzxhbmxs;
	}

	public String[] getSjclhjss() {
		return sjclhjss;
	}

	public void setSjclhjss(String[] sjclhjss) {
		this.sjclhjss = sjclhjss;
	}

	public String[] getSfxyxnyzs() {
		return sfxyxnyzs;
	}

	public void setSfxyxnyzs(String[] sfxyxnyzs) {
		this.sfxyxnyzs = sfxyxnyzs;
	}

	public String[] getYsjls() {
		return ysjls;
	}

	public void setYsjls(String[] ysjls) {
		this.ysjls = ysjls;
	}

	public String getYsrmc() {
		return ysrmc;
	}

	public void setYsrmc(String ysrmc) {
		this.ysrmc = ysrmc;
	}

	public String getLx() {
		return lx;
	}

	public void setLx(String lx) {
		this.lx = lx;
	}
	//复数ID
	private List<String> sbzts_t;
	public List<String> getSbzts_t() {
		return sbzts_t;
	}
	public void setSbzts_t(String sbzts_t) {
		List<String> list = new ArrayList<>();
		if(StringUtil.isNotBlank(sbzts_t)) {
			String[] str = sbzts_t.split(",");
			list = Arrays.asList(str);
		}
		this.sbzts_t = list;
	}
	public void setSbzts_t(List<String> sbzts_t) {
		if(!CollectionUtils.isEmpty(sbzts_t)) {
            sbzts_t.replaceAll(s -> s.replace("[", "").replace("]", "").trim());
		}
		this.sbzts_t = sbzts_t;
	}
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getSqlParam() {
		return sqlParam;
	}

	public void setSqlParam(String sqlParam) {
		this.sqlParam = sqlParam;
	}

	public String getGlryyhm() {
		return glryyhm;
	}

	public void setGlryyhm(String glryyhm) {
		this.glryyhm = glryyhm;
	}

	public String getXsyryhm() {
		return xsyryhm;
	}

	public void setXsyryhm(String xsyryhm) {
		this.xsyryhm = xsyryhm;
	}

	public String getXsyrbj() {
		return xsyrbj;
	}

	public void setXsyrbj(String xsyrbj) {
		this.xsyrbj = xsyrbj;
	}

	public String getYygdzcbh() {
		return yygdzcbh;
	}

	public void setYygdzcbh(String yygdzcbh) {
		this.yygdzcbh = yygdzcbh;
	}

	public String getYysbbh() {
		return yysbbh;
	}

	public void setYysbbh(String yysbbh) {
		this.yysbbh = yysbbh;
	}

	public String getLrrymc() {
		return lrrymc;
	}

	public void setLrrymc(String lrrymc) {
		this.lrrymc = lrrymc;
	}

	public String getGlryddid() {
		return glryddid;
	}

	public void setGlryddid(String glryddid) {
		this.glryddid = glryddid;
	}

	public String getSblxmc() {
		return sblxmc;
	}

	public void setSblxmc(String sblxmc) {
		this.sblxmc = sblxmc;
	}

	public String getXsyrddid() {
		return xsyrddid;
	}

	public void setXsyrddid(String xsyrddid) {
		this.xsyrddid = xsyrddid;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getXsybmmc() {
		return xsybmmc;
	}

	public void setXsybmmc(String xsybmmc) {
		this.xsybmmc = xsybmmc;
	}

	public String getGlrymc() {
		return glrymc;
	}

	public void setGlrymc(String glrymc) {
		this.glrymc = glrymc;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getLysjstart() {
		return lysjstart;
	}

	public void setLysjstart(String lysjstart) {
		this.lysjstart = lysjstart;
	}

	public String getLysjend() {
		return lysjend;
	}

	public void setLysjend(String lysjend) {
		this.lysjend = lysjend;
	}

	public String[] getSbzts() {
		return sbzts;
	}

	public void setSbzts(String[] sbzts) {
		this.sbzts = sbzts;
	}

	public String[] getPkzts() {
		return pkzts;
	}

	public void setPkzts(String[] pkzts) {
		this.pkzts = pkzts;
	}

	public String[] getSblxs() {
		return sblxs;
	}

	public void setSblxs(String[] sblxs) {
		this.sblxs = sblxs;
	}

	public String[] getXsybms() {
		return xsybms;
	}

	public void setXsybms(String[] xsybms) {
		this.xsybms = xsybms;
	}

	public String getXsyrmc() {
		return xsyrmc;
	}

	public void setXsyrmc(String xsyrmc) {
		this.xsyrmc = xsyrmc;
	}

	public String getDhsl() {
		return dhsl;
	}

	public void setDhsl(String dhsl) {
		this.dhsl = dhsl;
	}

	public String getWlid() {
		return wlid;
	}

	public void setWlid(String wlid) {
		this.wlid = wlid;
	}

	public String getXgqgdzcbh() {
		return xgqgdzcbh;
	}

	public void setXgqgdzcbh(String xgqgdzcbh) {
		this.xgqgdzcbh = xgqgdzcbh;
	}

	public String getSfgdzc() {
		return sfgdzc;
	}

	public void setSfgdzc(String sfgdzc) {
		this.sfgdzc = sfgdzc;
	}

	public String getScph() {
		return scph;
	}

	public void setScph(String scph) {
		this.scph = scph;
	}
	public String getDhdh() {
		return dhdh;
	}
	public void setDhdh(String dhdh) {
		this.dhdh = dhdh;
	}

	public String getQgid() {
		return qgid;
	}

	public void setQgid(String qgid) {
		this.qgid = qgid;
	}


	public String getGg() {
		return gg;
	}

	public void setGg(String gg) {
		this.gg = gg;
	}

	public String getRkzt() {
		return rkzt;
	}

	public void setRkzt(String rkzt) {
		this.rkzt = rkzt;
	}

	public String getWlbm() {
		return wlbm;
	}

	public void setWlbm(String wlbm) {
		this.wlbm = wlbm;
	}

	public String getWlmc() {
		return wlmc;
	}

	public void setWlmc(String wlmc) {
		this.wlmc = wlmc;
	}

	public String getScs() {
		return scs;
	}

	public void setScs(String scs) {
		this.scs = scs;
	}

	public String getHtnbbh() {
		return htnbbh;
	}

	public void setHtnbbh(String htnbbh) {
		this.htnbbh = htnbbh;
	}


	public String getGysmc() {
		return gysmc;
	}

	public void setGysmc(String gysmc) {
		this.gysmc = gysmc;
	}

	public String getDh() {
		return dh;
	}

	public void setDh(String dh) {
		this.dh = dh;
	}
	public String getSqbm() {
		return sqbm;
	}

	public void setSqbm(String sqbm) {
		this.sqbm = sqbm;
	}

	public String getDjh() {
		return djh;
	}

	public void setDjh(String djh) {
		this.djh = djh;
	}

	public String getSsyfxm() {
		return ssyfxm;
	}

	public void setSsyfxm(String ssyfxm) {
		this.ssyfxm = ssyfxm;
	}

	public String getSqrmc() {
		return sqrmc;
	}

	public void setSqrmc(String sqrmc) {
		this.sqrmc = sqrmc;
	}

	public String getSqbmmc() {
		return sqbmmc;
	}

	public void setSqbmmc(String sqbmmc) {
		this.sqbmmc = sqbmmc;
	}

	public String getSsyfxmmc() {
		return ssyfxmmc;
	}

	public void setSsyfxmmc(String ssyfxmmc) {
		this.ssyfxmmc = ssyfxmmc;
	}

	public String getDhrq() {
		return dhrq;
	}

	public void setDhrq(String dhrq) {
		this.dhrq = dhrq;
	}

	public String getDhrqstart() {
		return dhrqstart;
	}

	public void setDhrqstart(String dhrqstart) {
		this.dhrqstart = dhrqstart;
	}

	public String getDhrqend() {
		return dhrqend;
	}

	public void setDhrqend(String dhrqend) {
		this.dhrqend = dhrqend;
	}

	public String getYsrqstart() {
		return ysrqstart;
	}

	public void setYsrqstart(String ysrqstart) {
		this.ysrqstart = ysrqstart;
	}

	public String getYsrqend() {
		return ysrqend;
	}

	public void setYsrqend(String ysrqend) {
		this.ysrqend = ysrqend;
	}

	public String[] getZts() {
		return zts;
	}

	public void setZts(String[] zts) {
		this.zts = zts;
		for(int i=0;i<this.zts.length;i++)
		{
			this.zts[i] = this.zts[i].replace("'", "");
		}
	}

	public String[] getSsyfxms() {
		return ssyfxms;
	}

	public void setSsyfxms(String[] ssyfxms) {
		this.ssyfxms = ssyfxms;
		for(int i=0;i<this.ssyfxms.length;i++)
		{
			this.ssyfxms[i] = this.ssyfxms[i].replace("'", "");
		}
	}

	public String[] getYsjgs() {
		return ysjgs;
	}

	public void setYsjgs(String[] ysjgs) {
		this.ysjgs = ysjgs;
		for(int i=0;i<this.ysjgs.length;i++)
		{
			this.ysjgs[i] = this.ysjgs[i].replace("'", "");
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
