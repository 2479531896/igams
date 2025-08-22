package com.matridx.igams.detection.molecule.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.ArrayList;
import java.util.List;

@Alias(value="FzjcxxDto")
public class FzjcxxDto extends FzjcxxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//患者信息
	private String xm;
	private String xb;
	//性别名称
	private String xbmc;
	private String nl;
	private String sj;
	private String sfzh;
	private String llid; //履历id
	private String xjzd;
	private String sfqr;
	private String twjcz;
	private String zjlx;
	private String filePath;
	//采集日期
	private String cjsj;
	private String cjry;
	private String syry;
	private String serial;
	private String cjryxm;
	//检测项目
	private String jcxmmc;
	private String jcjgmc;
	private String jcxm;
	private String jssjstart;
	private String jssjend;
	private String sysjstart;
	private String sysjend;
	private String cjsjstart;
	private String cjsjend;
	private String bgrqstart;
	private String bgrqend;
	private String yyrqstart;
	private String yyrqend;
	private String fkrqstart;
	private String fkrqend;
	private String fzxmid;
	private String fyk;
	private String jymc;
	private String bgjy;
	private String ctz;
	private String[] fzxmids;
	private String print_cs;

	private String sqlParam;
	private String auditType;
	private String sqr;
	private String shry;
	private String shsj;
	private String sftg;
	private String shyj;
	private String shxx_shxxid;
	private List<String> fjids;
	private String sqsj;
	private  String yyxxcskz1;
	private  String dwmc;
	//用于存放多个标本编号
	private List<String> ybbhs;
	private String fzjcids;
	//送检科室名称
	private String sjksmc;
	//标记，用于实验列表的确认，未确认，未发送报告按钮的区分
	private String flag;
	//标本状态名称
	private String bbztmc;
	//送检人员名称
	private String sjrymc;
	//检验人员名称
	private String jyrymc;
	//实验人员名称
	private String syrymc;
    //证件类型名称 eg:身份证/护照
	private String zjlxmc;
//审核人s
	private String shrs;
	//审核时间s
	private String shsjs;
	//审核用户名s
	private String shyhms;
	//盖章类型
	private String gzlx;
	//申请单时间，新冠对接中申请报告单的申请时间，为采集时间提前一个小时
	private String sqdsj;
	// 全部(查询条件)
	private String entire;
	private String[] zts;
	//分子检测类型
	private String fzjczxmid;//基础数据的csid
	//分子检测类型cskz1
	private String fzjczxmcskz1;//fzjczxmcskz1
	//标本编号
	private String ybbh_ym;
	//序号
	private String xh;//用于组装标本子编号
	//预约检测日期开始时间
	private String yyjcrqstart;
	//预约检测日期结束时间
	private String yyjcrqend;
	//支付方式名称
	private String zffsmc;
	//标本编号
	private String ybbh;
	//分子检测项目名称
	private String fzxmmc;
	//检测单位名称
	private String jcdwmc;
	//联系电话
	private String lxdh;
	//证件号
	private String zjh;
	//标本类型名称
	private String yblxmc;
	//检测结果
	private String jcjg;
	//项目名称
	private String xmmc;
	//项目简称
	private String xmjc;
	//文件路径
	private String wjlj;
	//检查项目（多）
	private String[] jcxmmcs;
	//检测项目id(多)
	private String[] jcxmids;
    //检测对象参数代码
	private String jcdxcsdm;
	//检测对象名称
	private String jcdxmc;
	//检测子项目名称
	private String jczxmmc;
    //附件业务类型
	private String ywlx;
	//附件ID
	private String fjid;
	//文件名
	private String wjm;
	//证件类型代码
	private String zjlxdm;
    //报告类型
	private String bglxbj;
	//上传平台
	private String scpt;
	//用于判断是否允许修改检测项目的标记
	private String xmflag;
	//检测对象类型（多）
	private String[] jcdxlxs;
	//样本类型参数扩展1
	private String yblxcskz1;
	//采样点名称
	private String cydmc;
	//采集人员名称
	private String cjrymc;
	//检测子项目（多）
	private String[] jczxms;
	//钉钉查询主条码标记
	private String ddbj;
	//实验号起始值
	private String syhstart;
	//实验号结束值
	private String syhend;
		//样本状态名称
	private String ybztmc;
	//退款完成日期
	private String tkwcrq;
	//扩增板操作
	private String cz;
	//扩增板序号
	private String kzbxh;
	//扩增板名称
	private String kzbmc;
	//扩增板——选择名称
	private String kzbmc_sel;
	//行数
	private String hs;
	//列数
	private String ls;
	//用于检索标本类型
	private String[] yblxs;
	//检测结果代码
	private String jcjgdm;
	//接收人员名称
	private String jsrymc;
	//送检医生
	private String sjys;
	//床位号
	private String cwh;
	//医生电话
	private String ysdh;
	//住院号
	private String zyh;
	//科室名称
	private String ksmc;
	//其他样本类型名称
	private String qtyblxmc;
	//其他送检单位
	private String qtsjdwmc;
	//父检测项目名称
	private String fjcxmmc;
	//子检测项目名称
	private String zjcxmmc;
	//普检结果
	private String pjjg;
	//分子检测类型
	private String[] fzjczxmids;//基础数据的csid
	//扩展参数1
	private String kzcs;
	//单位id
	private String dwid;
	//标本状态
	private List<String> pjbbzts;
	private String [] bbzts;
	//合作伙伴集合
	private List<String> sjhbs;
	//角色检测单位限制
	private List<String> jcdwxz;
	//分子检测子项目规则
	private String fzjczmxgz;
	//分子检测项目
	private List<FzjcxmDto> addFzjcxmDtos;
	//检测类型代码
	private String jclxdm;
	//分子检测项目
	private List<String> delIds;
	//检测项目参数代码
	private String jcxmdm;
	//检测子项目参数代码
	private String jczxmdm;
	//分子检测项目ID
	private String fzjcxmid;
	//报告日期，精确到时分秒
	private String qbgrq;

	private String jcxm_json;

	public String getJcxm_json() {
		return jcxm_json;
	}

	public void setJcxm_json(String jcxm_json) {
		this.jcxm_json = jcxm_json;
	}

	public String getQbgrq() {
		return qbgrq;
	}

	public void setQbgrq(String qbgrq) {
		this.qbgrq = qbgrq;
	}

	public String getFzjcxmid() {
		return fzjcxmid;
	}

	public void setFzjcxmid(String fzjcxmid) {
		this.fzjcxmid = fzjcxmid;
	}

	public String getJcxmdm() {
		return jcxmdm;
	}

	public void setJcxmdm(String jcxmdm) {
		this.jcxmdm = jcxmdm;
	}

	public String getJczxmdm() {
		return jczxmdm;
	}

	public void setJczxmdm(String jczxmdm) {
		this.jczxmdm = jczxmdm;
	}

	public List<String> getDelIds() {
		return delIds;
	}

	public void setDelIds(List<String> delIds) {
		this.delIds = delIds;
	}

	public String getJclxdm() {
		return jclxdm;
	}

	public void setJclxdm(String jclxdm) {
		this.jclxdm = jclxdm;
	}

	public List<FzjcxmDto> getAddFzjcxmDtos() {
		return addFzjcxmDtos;
	}

	public void setAddFzjcxmDtos(List<FzjcxmDto> addFzjcxmDtos) {
		this.addFzjcxmDtos = addFzjcxmDtos;
	}

	public String getFzjczmxgz() {
		return fzjczmxgz;
	}

	public void setFzjczmxgz(String fzjczmxgz) {
		this.fzjczmxgz = fzjczmxgz;
	}

	public List<String> getSjhbs() {
		return sjhbs;
	}

	public void setSjhbs(List<String> sjhbs) {
		this.sjhbs = sjhbs;
	}

	public List<String> getJcdwxz() {
		return jcdwxz;
	}

	public void setJcdwxz(List<String> jcdwxz) {
		this.jcdwxz = jcdwxz;
	}

	public List<String> getPjbbzts() {
		return pjbbzts;
	}

	public void setPjbbzts(List<String> pjbbzts) {
		this.pjbbzts = pjbbzts;
	}

	public String[] getBbzts() {
		return bbzts;
	}

	public void setBbzts(String[] bbzts){
		this.bbzts = bbzts;
		for (int i=0; i<bbzts.length; i++){
			this.bbzts[i] = this.bbzts[i].replace("'","");
		}
	}

	public String getDwid() {
		return dwid;
	}

	public void setDwid(String dwid) {
		this.dwid = dwid;
	}

	public String getKzcs() {
		return kzcs;
	}

	public void setKzcs(String kzcs) {
		this.kzcs = kzcs;
	}

	public String[] getFzjczxmids() {
		return fzjczxmids;
	}

	public void setFzjczxmids(String[] fzjczxmids) {
		this.fzjczxmids = fzjczxmids;
	}

	public String getPjjg() {
		return pjjg;
	}

	public void setPjjg(String pjjg) {
		this.pjjg = pjjg;
	}

	public String getFjcxmmc() {
		return fjcxmmc;
	}

	public void setFjcxmmc(String fjcxmmc) {
		this.fjcxmmc = fjcxmmc;
	}

	public String getZjcxmmc() {
		return zjcxmmc;
	}

	public void setZjcxmmc(String zjcxmmc) {
		this.zjcxmmc = zjcxmmc;
	}

	public String getQtyblxmc() {
		return qtyblxmc;
	}

	public void setQtyblxmc(String qtyblxmc) {
		this.qtyblxmc = qtyblxmc;
	}

	public String getQtsjdwmc() {
		return qtsjdwmc;
	}

	public void setQtsjdwmc(String qtsjdwmc) {
		this.qtsjdwmc = qtsjdwmc;
	}

	public String getKsmc() {
		return ksmc;
	}

	public void setKsmc(String ksmc) {
		this.ksmc = ksmc;
	}

	public String getSjys() {
		return sjys;
	}

	public void setSjys(String sjys) {
		this.sjys = sjys;
	}

	public String getCwh() {
		return cwh;
	}

	public void setCwh(String cwh) {
		this.cwh = cwh;
	}

	public String getYsdh() {
		return ysdh;
	}

	public void setYsdh(String ysdh) {
		this.ysdh = ysdh;
	}

	public String getZyh() {
		return zyh;
	}

	public void setZyh(String zyh) {
		this.zyh = zyh;
	}

	public String getJsrymc() {
		return jsrymc;
	}

	public void setJsrymc(String jsrymc) {
		this.jsrymc = jsrymc;
	}

	public String getJcjgdm() {
		return jcjgdm;
	}

	public void setJcjgdm(String jcjgdm) {
		this.jcjgdm = jcjgdm;
	}

	public String[] getYblxs() {
		return yblxs;
	}

	public void setYblxs(String[] yblxs) {
		this.yblxs = yblxs;
		for(int i=0; i<yblxs.length; i++) {
			this.yblxs[i] = this.yblxs[i].replace("'", "");
		}
	}

	public String getPrint_cs() {
		return print_cs;
	}

	public void setPrint_cs(String print_cs) {
		this.print_cs = print_cs;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getHs() {
		return hs;
	}

	public void setHs(String hs) {
		this.hs = hs;
	}

	public String getLs() {
		return ls;
	}

	public void setLs(String ls) {
		this.ls = ls;
	}

	public String getCz() {
		return cz;
	}

	public void setCz(String cz) {
		this.cz = cz;
	}

	public String getKzbxh() {
		return kzbxh;
	}

	public void setKzbxh(String kzbxh) {
		this.kzbxh = kzbxh;
	}

	public String getKzbmc() {
		return kzbmc;
	}

	public void setKzbmc(String kzbmc) {
		this.kzbmc = kzbmc;
	}

	public String getKzbmc_sel() {
		return kzbmc_sel;
	}

	public void setKzbmc_sel(String kzbmc_sel) {
		this.kzbmc_sel = kzbmc_sel;
	}


	public String getTkwcrq() {
		return tkwcrq;
	}

	public void setTkwcrq(String tkwcrq) {
		this.tkwcrq = tkwcrq;
	}

	public String getYbztmc() {
		return ybztmc;
	}

	public void setYbztmc(String ybztmc) {
		this.ybztmc = ybztmc;
	}


	public String getLlid() {
		return llid;
	}

	public void setLlid(String llid) {
		this.llid = llid;
	}

	public String getSyhstart() {
		return syhstart;
	}

	public void setSyhstart(String syhstart) {
		this.syhstart = syhstart;
	}

	public String getSyhend() {
		return syhend;
	}

	public void setSyhend(String syhend) {
		this.syhend = syhend;
	}

	public String getCjrymc() {
		return cjrymc;
	}

	public void setCjrymc(String cjrymc) {
		this.cjrymc = cjrymc;
	}

	public String[] getJczxms() {
		return jczxms;
	}

	public void setJczxms(String[] jczxms) {
		this.jczxms = jczxms;
		for (int i = 0; i < jczxms.length; i++){
			this.jczxms[i]=this.jczxms[i].replace("'","");
		}
	}

	public String getCydmc() {
		return cydmc;
	}

	public void setCydmc(String cydmc) {
		this.cydmc = cydmc;
	}


	public String getYblxcskz1() {
		return yblxcskz1;
	}

	public void setYblxcskz1(String yblxcskz1) {
		this.yblxcskz1 = yblxcskz1;
	}

	public String getXmflag() {
		return xmflag;
	}

	public void setXmflag(String xmflag) {
		this.xmflag = xmflag;
	}
	
	public String[] getJcdxlxs() {
		return jcdxlxs;
	}

	public void setJcdxlxs(String[] jcdxlxs) {
		this.jcdxlxs = jcdxlxs;
		for (int i = 0; i < jcdxlxs.length; i++){
			this.jcdxlxs[i]=this.jcdxlxs[i].replace("'","");
		}
	}

	public String getScpt() {
		return scpt;
	}

	public void setScpt(String scpt) {
		this.scpt = scpt;
	}

	public String getBglxbj() {
		return bglxbj;
	}

	public void setBglxbj(String bglxbj) {
		this.bglxbj = bglxbj;
	}

	public String getJcdxmc() {
		return jcdxmc;
	}

	public void setJcdxmc(String jcdxmc) {
		this.jcdxmc = jcdxmc;
	}

	public String getJcdxcsdm() {
		return jcdxcsdm;
	}

	public void setJcdxcsdm(String jcdxcsdm) {
		this.jcdxcsdm = jcdxcsdm;
	}
	
	public String getJczxmmc() {
		return jczxmmc;
	}

	public void setJczxmmc(String jczxmmc) {
		this.jczxmmc = jczxmmc;
	}
	
	public String getZjlxdm() {
        return zjlxdm;
    }

    public void setZjlxdm(String zjlxdm) {
        this.zjlxdm = zjlxdm;
    }

    public String getFjid() {
        return fjid;
    }

    public void setFjid(String fjid) {
        this.fjid = fjid;
    }

    public String getWjm() {
        return wjm;
    }

    public void setWjm(String wjm) {
        this.wjm = wjm;
    }

    public String getYwlx() {
        return ywlx;
    }

    public void setYwlx(String ywlx) {
        this.ywlx = ywlx;
    }

    public String getFkrqstart() {
		return fkrqstart;
	}

	public void setFkrqstart(String fkrqstart) {
		this.fkrqstart = fkrqstart;
	}

	public String getFkrqend() {
		return fkrqend;
	}

	public void setFkrqend(String fkrqend) {
		this.fkrqend = fkrqend;
	}

	public String getZffsmc() {
		return zffsmc;
	}

	public void setZffsmc(String zffsmc) {
		this.zffsmc = zffsmc;
	}

	//支付金额
	private String amount;
	//商品名称
	private String subject;

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getYyjcrqstart() {
		return yyjcrqstart;
	}

	public void setYyjcrqstart(String yyjcrqstart) {
		this.yyjcrqstart = yyjcrqstart;
	}

	public String getYyjcrqend() {
		return yyjcrqend;
	}

	public void setYyjcrqend(String yyjcrqend) {
		this.yyjcrqend = yyjcrqend;
	}

	public String getYbbh_ym() {
		return ybbh_ym;
	}

	public void setYbbh_ym(String ybbh_ym) {
		this.ybbh_ym = ybbh_ym;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getFzjczxmid() {
		return fzjczxmid;
	}

	public void setFzjczxmid(String fzjczxmid) {
		this.fzjczxmid = fzjczxmid;
	}

	public String getYyrqstart() {
		return yyrqstart;
	}

	public void setYyrqstart(String yyrqstart) {
		this.yyrqstart = yyrqstart;
	}

	public String getYyrqend() {
		return yyrqend;
	}

	public void setYyrqend(String yyrqend) {
		this.yyrqend = yyrqend;
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

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getSqdsj() {
		return sqdsj;
	}

	public void setSqdsj(String sqdsj) {
		this.sqdsj = sqdsj;
	}
	
	public String getGzlx() {
		return gzlx;
	}

	public void setGzlx(String gzlx) {
		this.gzlx = gzlx;
	}

	public String getShrs() {
		return shrs;
	}

	public void setShrs(String shrs) {
		this.shrs = shrs;
	}

	public String getShsjs() {
		return shsjs;
	}

	public void setShsjs(String shsjs) {
		this.shsjs = shsjs;
	}

	public String getShyhms() {
		return shyhms;
	}

	public void setShyhms(String shyhms) {
		this.shyhms = shyhms;
	}


	public String getZjlxmc() {
		return zjlxmc;
	}

	public void setZjlxmc(String zjlxmc) {
		this.zjlxmc = zjlxmc;
	}

	public String getSyrymc() {
		return syrymc;
	}

	public void setSyrymc(String syrymc) {
		this.syrymc = syrymc;
	}

	public String getJyrymc() {
		return jyrymc;
	}

	public void setJyrymc(String jyrymc) {
		this.jyrymc = jyrymc;
	}

	public String getSjrymc() {
		return sjrymc;
	}

	public void setSjrymc(String sjrymc) {
		this.sjrymc = sjrymc;
	}

	public String getXbmc() {
		return xbmc;
	}

	public void setXbmc(String xbmc) {
		this.xbmc = xbmc;
	}

	public String getBbztmc() {
		return bbztmc;
	}

	public void setBbztmc(String bbztmc) {
		this.bbztmc = bbztmc;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getSjksmc() {
		return sjksmc;
	}

	public void setSjksmc(String sjksmc) {
		this.sjksmc = sjksmc;
	}

	public String getFzjcids() {
		return fzjcids;
	}

	public void setFzjcids(String fzjcids) {
		this.fzjcids = fzjcids;
	}

	public List<String> getYbbhs() {
		return ybbhs;
	}

	public void setYbbhs(List<String> ybbhs) {
		List<String> list=new ArrayList<>();
		if(ybbhs!=null && ybbhs.size()>0){
			for(String ybbh : ybbhs){
				if(!list.toString().contains(ybbh))
					list.add(ybbh);
			}
		}
		this.ybbhs = list;
	}

	public String getDwmc() {
		return dwmc;
	}

	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}

	public String getYyxxcskz1() {
		return yyxxcskz1;
	}

	public void setYyxxcskz1(String yyxxcskz1) {
		this.yyxxcskz1 = yyxxcskz1;
	}
	
	public String getWjlj() {
		return wjlj;
	}

	public void setWjlj(String wjlj) {
		this.wjlj = wjlj;
	}

	public String getJcjg() {
		return jcjg;
	}

	public void setJcjg(String jcjg) {
		this.jcjg = jcjg;
	}

	public String getXmmc() {
		return xmmc;
	}

	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
	}

	public String getXmjc() {
		return xmjc;
	}

	public void setXmjc(String xmjc) {
		this.xmjc = xmjc;
	}

	public String getLxdh() {
		return lxdh;
	}

	public void setLxdh(String lxdh) {
		this.lxdh = lxdh;
	}

	public String getZjh() {
		return zjh;
	}

	public void setZjh(String zjh) {
		this.zjh = zjh;
	}

	public String getYblxmc() {
		return yblxmc;
	}

	public void setYblxmc(String yblxmc) {
		this.yblxmc = yblxmc;
	}

	public String getJcxm() {
		return jcxm;
	}

	public void setJcxm(String jcxm) {
		this.jcxm = jcxm;
	}

	public String[] getJcxmids() {
		return jcxmids;
	}

	public void setJcxmids(String[] jcxmids) {
		this.jcxmids = jcxmids;
		for(int i=0;i<this.jcxmids.length;i++)
		{
			this.jcxmids[i] = this.jcxmids[i].replace("'", "");
		}
	}

	public String[] getJcxmmcs() {
		return jcxmmcs;
	}

	public void setJcxmmcs(String[] jcxms) {
		this.jcxmmcs = jcxms;
		for(int i=0;i<this.jcxmmcs.length;i++)
		{
			this.jcxmmcs[i] = this.jcxmmcs[i].replace("'", "");
		}
	}

	public String getJcdwmc() {
		return jcdwmc;
	}

	public void setJcdwmc(String jcdwmc) {
		this.jcdwmc = jcdwmc;
	}

	public String getZjlx() {
		return zjlx;
	}

	public void setZjlx(String zjlx) {
		this.zjlx = zjlx;
	}

	public String getTwjcz() {
		return twjcz;
	}

	public void setTwjcz(String twjcz) {
		this.twjcz = twjcz;
	}

	public String getSfqr() {
		return sfqr;
	}

	public void setSfqr(String sfqr) {
		this.sfqr = sfqr;
	}

	public String[] getFzxmids() {
		return fzxmids;
	}

	public void setFzxmids(String[] fzxmids) {
		this.fzxmids = fzxmids;
	}

	public String getFzxmmc() {
		return fzxmmc;
	}

	public void setFzxmmc(String fzxmmc) {
		this.fzxmmc = fzxmmc;
	}

	public String getJcjgmc() {
		return jcjgmc;
	}

	public void setJcjgmc(String jcjgmc) {
		this.jcjgmc = jcjgmc;
	}

	public String getJcxmmc() {
		return jcxmmc;
	}

	public void setJcxmmc(String jcxmmc) {
		this.jcxmmc = jcxmmc;
	}

	public String getShxx_shxxid() {
		return shxx_shxxid;
	}

	public void setShxx_shxxid(String shxx_shxxid) {
		this.shxx_shxxid = shxx_shxxid;
	}

	public String getSqr() {
		return sqr;
	}

	public void setSqr(String sqr) {
		this.sqr = sqr;
	}

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}

	public String getSqsj() {
		return sqsj;
	}

	public void setSqsj(String sqsj) {
		this.sqsj = sqsj;
	}

	public String getShry() {
		return shry;
	}

	public void setShry(String shry) {
		this.shry = shry;
	}

	public String getShsj() {
		return shsj;
	}

	public void setShsj(String shsj) {
		this.shsj = shsj;
	}

	public String getSftg() {
		return sftg;
	}

	public void setSftg(String sftg) {
		this.sftg = sftg;
	}

	public String getShyj() {
		return shyj;
	}

	public void setShyj(String shyj) {
		this.shyj = shyj;
	}

	public String getAuditType() {
		return auditType;
	}

	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}

	public String getYbbh() {
		return ybbh;
	}

	public void setYbbh(String ybbh) {
		this.ybbh = ybbh;
	}

	public String getSfzh() {
		return sfzh;
	}

	public void setSfzh(String sfzh) {
		this.sfzh = sfzh;
	}

	public String getXjzd() {
		return xjzd;
	}

	public void setXjzd(String xjzd) {
		this.xjzd = xjzd;
	}

	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public String getSqlParam() {
		return sqlParam;
	}

	public void setSqlParam(String sqlParam) {
		this.sqlParam = sqlParam;
	}

	public String getFzxmid() {
		return fzxmid;
	}

	public void setFzxmid(String fzxmid) {
		this.fzxmid = fzxmid;
	}

	public String getFyk() {
		return fyk;
	}

	public void setFyk(String fyk) {
		this.fyk = fyk;
	}

	public String getJymc() {
		return jymc;
	}

	public void setJymc(String jymc) {
		this.jymc = jymc;
	}

	public String getBgjy() {
		return bgjy;
	}

	public void setBgjy(String bgjy) {
		this.bgjy = bgjy;
	}

	public String getCtz() {
		return ctz;
	}

	public void setCtz(String ctz) {
		this.ctz = ctz;
	}

	public String getCjry() {
		return cjry;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getCjryxm() {
		return cjryxm;
	}

	public void setCjryxm(String cjryxm) {
		this.cjryxm = cjryxm;
	}

	public void setCjry(String cjry) {
		this.cjry = cjry;
	}

	@Override
	public String getSyry() {
		return syry;
	}

	@Override
	public void setSyry(String syry) {
		this.syry = syry;
	}

	public String getSj() {
		return sj;
	}

	public void setSj(String sj) {
		this.sj = sj;
	}

	public String getXb() {
		return xb;
	}

	public void setXb(String xb) {
		this.xb = xb;
	}

	public String getNl() {
		return nl;
	}

	public void setNl(String nl) {
		this.nl = nl;
	}

	public String getJssjstart() {
		return jssjstart;
	}

	public void setJssjstart(String jssjstart) {
		this.jssjstart = jssjstart;
	}

	public String getJssjend() {
		return jssjend;
	}

	public void setJssjend(String jssjend) {
		this.jssjend = jssjend;
	}

	public String getSysjstart() {
		return sysjstart;
	}

	public void setSysjstart(String sysjstart) {
		this.sysjstart = sysjstart;
	}

	public String getSysjend() {
		return sysjend;
	}

	public void setSysjend(String sysjend) {
		this.sysjend = sysjend;
	}

	public String getCjsjstart() {
		return cjsjstart;
	}

	public void setCjsjstart(String cjsjstart) {
		this.cjsjstart = cjsjstart;
	}

	public String getCjsjend() {
		return cjsjend;
	}

	public void setCjsjend(String cjsjend) {
		this.cjsjend = cjsjend;
	}

	public String getBgrqstart() {
		return bgrqstart;
	}

	public void setBgrqstart(String bgrqstart) {
		this.bgrqstart = bgrqstart;
	}

	public String getBgrqend() {
		return bgrqend;
	}

	public void setBgrqend(String bgrqend) {
		this.bgrqend = bgrqend;
	}

	public String getCjsj() {
		return cjsj;
	}

	public void setCjsj(String cjsj) {
		this.cjsj = cjsj;
	}

	public String getDdbj() {
		return ddbj;
	}

	public void setDdbj(String ddbj) {
		this.ddbj = ddbj;
	}

	public String getFzjczxmcskz1() {
		return fzjczxmcskz1;
	}

	public void setFzjczxmcskz1(String fzjczxmcskz1) {
		this.fzjczxmcskz1 = fzjczxmcskz1;
	}
}
