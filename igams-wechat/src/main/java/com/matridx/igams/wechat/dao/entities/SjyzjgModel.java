package com.matridx.igams.wechat.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="SjyzjgModel")
public class SjyzjgModel extends BaseModel{
	//验证结果id
	private String yzjgid;
	//验证id
	private String yzid;
	//验证结果id
	private String yzjg;
	//结论
	private String jl;
	//验证类别
	private String yzlb;
	//孔位1
	private String kw1;
	//孔位2
	private String kw2;
	//孔位3
	private String kw3;
	//p孔位1
	private String pkw1;
	//p孔位2
	private String pkw2;
	//p孔位3
	private String pkw3;
	//n孔位1
	private String nkw1;
	//n孔位2
	private String nkw2;
	//n孔位3
	private String nkw3;
	//状态
	private String zt;
	//检测区分
	private String jcqf;
	//目标基因
	private String mbjy;

	private String ct1	;
	private String ct2	 ;
	private String ct3	 ;
	private String tm11;
	private String tm12;
	private String tm13;
	private String fz11;
	private String fz12;
	private String fz13;

	private String tm21;
	private String tm22;
	private String tm23;
	private String fz21;
	private String fz22;
	private String fz23;

	private String tm31;
	private String tm32;
	private String tm33;
	private String fz31;
	private String fz32;
	private String fz33;
	private String nct1	;//	NC-M阴性对照ct
	private String nct2	;//	NC-M阴性对照ct
	private String nct3	;//	NC-M阴性对照ct
	private String ntm11	;//	ntm11 第一位1代表指定ct1第二位是数字
	private String ntm12	;//	ntm12
	private String ntm13	;//	ntm13
	private String nfz11	;//	n峰值11
	private String nfz12	;//	n峰值12
	private String nfz13	;//	n峰值13
	private String ntm21	;//	ntm21
	private String ntm22	;//	ntm22
	private String ntm23	;//	ntm23
	private String nfz21	;//	n峰值21
	private String nfz22	;//	n峰值22
	private String nfz23	;//	n峰值23
	private String ntm31	;//	ntm21
	private String ntm32	;//	ntm22
	private String ntm33	;//	ntm23
	private String nfz31	;//	n峰值31
	private String nfz32	;//	n峰值32
	private String nfz33	;//	n峰值33
	private String pct1	;//	PC-M阳性对照ct
	private String pct2	;//	PC-M阳性对照ct
	private String pct3	;//	PC-M阳性对照ct
	private String ptm11	;//	ptm11 第一位1代表指定ct1第二位是数字
	private String ptm12	;//	ptm12
	private String ptm13	;//	ptm13
	private String pfz11	;//	p峰值11
	private String pfz12	;//	p峰值12
	private String pfz13	;//	p峰值13
	private String ptm21	;//	ptm21
	private String ptm22	;//	ptm22
	private String ptm23	;//	ptm23
	private String pfz21	;//	p峰值21
	private String pfz22	;//	p峰值22
	private String pfz23	;//	p峰值23
	private String ptm31	;//	ptm21
	private String ptm32	;//	ptm22
	private String ptm33	;//	ptm23
	private String pfz31	;//	p峰值31
	private String pfz32	;//	p峰值32
	private String pfz33	;//	p峰值33
	private String ctbs;
	private String nctbs;
	private String pctbs;

	//备注
	private String bz1;
	private String bz2;

	public String getBz1() {
		return bz1;
	}

	public void setBz1(String bz1) {
		this.bz1 = bz1;
	}

	public String getBz2() {
		return bz2;
	}

	public void setBz2(String bz2) {
		this.bz2 = bz2;
	}

	public String getCtbs() {
		return ctbs;
	}

	public void setCtbs(String ctbs) {
		this.ctbs = ctbs;
	}

	public String getNctbs() {
		return nctbs;
	}

	public void setNctbs(String nctbs) {
		this.nctbs = nctbs;
	}

	public String getPctbs() {
		return pctbs;
	}

	public void setPctbs(String pctbs) {
		this.pctbs = pctbs;
	}

	public String getKw1() {
		return kw1;
	}

	public void setKw1(String kw1) {
		this.kw1 = kw1;
	}

	public String getKw2() {
		return kw2;
	}

	public void setKw2(String kw2) {
		this.kw2 = kw2;
	}

	public String getKw3() {
		return kw3;
	}

	public void setKw3(String kw3) {
		this.kw3 = kw3;
	}

	public String getPkw1() {
		return pkw1;
	}

	public void setPkw1(String pkw1) {
		this.pkw1 = pkw1;
	}

	public String getPkw2() {
		return pkw2;
	}

	public void setPkw2(String pkw2) {
		this.pkw2 = pkw2;
	}

	public String getPkw3() {
		return pkw3;
	}

	public void setPkw3(String pkw3) {
		this.pkw3 = pkw3;
	}

	public String getNkw1() {
		return nkw1;
	}

	public void setNkw1(String nkw1) {
		this.nkw1 = nkw1;
	}

	public String getNkw2() {
		return nkw2;
	}

	public void setNkw2(String nkw2) {
		this.nkw2 = nkw2;
	}

	public String getNkw3() {
		return nkw3;
	}

	public void setNkw3(String nkw3) {
		this.nkw3 = nkw3;
	}

	public String getNct1() {
		return nct1;
	}

	public void setNct1(String nct1) {
		this.nct1 = nct1;
	}

	public String getNct2() {
		return nct2;
	}

	public void setNct2(String nct2) {
		this.nct2 = nct2;
	}

	public String getNct3() {
		return nct3;
	}

	public void setNct3(String nct3) {
		this.nct3 = nct3;
	}

	public String getNtm11() {
		return ntm11;
	}

	public void setNtm11(String ntm11) {
		this.ntm11 = ntm11;
	}

	public String getNtm12() {
		return ntm12;
	}

	public void setNtm12(String ntm12) {
		this.ntm12 = ntm12;
	}

	public String getNtm13() {
		return ntm13;
	}

	public void setNtm13(String ntm13) {
		this.ntm13 = ntm13;
	}

	public String getNfz11() {
		return nfz11;
	}

	public void setNfz11(String nfz11) {
		this.nfz11 = nfz11;
	}

	public String getNfz12() {
		return nfz12;
	}

	public void setNfz12(String nfz12) {
		this.nfz12 = nfz12;
	}

	public String getNfz13() {
		return nfz13;
	}

	public void setNfz13(String nfz13) {
		this.nfz13 = nfz13;
	}

	public String getNtm21() {
		return ntm21;
	}

	public void setNtm21(String ntm21) {
		this.ntm21 = ntm21;
	}

	public String getNtm22() {
		return ntm22;
	}

	public void setNtm22(String ntm22) {
		this.ntm22 = ntm22;
	}

	public String getNtm23() {
		return ntm23;
	}

	public void setNtm23(String ntm23) {
		this.ntm23 = ntm23;
	}

	public String getNfz21() {
		return nfz21;
	}

	public void setNfz21(String nfz21) {
		this.nfz21 = nfz21;
	}

	public String getNfz22() {
		return nfz22;
	}

	public void setNfz22(String nfz22) {
		this.nfz22 = nfz22;
	}

	public String getNfz23() {
		return nfz23;
	}

	public void setNfz23(String nfz23) {
		this.nfz23 = nfz23;
	}

	public String getNtm31() {
		return ntm31;
	}

	public void setNtm31(String ntm31) {
		this.ntm31 = ntm31;
	}

	public String getNtm32() {
		return ntm32;
	}

	public void setNtm32(String ntm32) {
		this.ntm32 = ntm32;
	}

	public String getNtm33() {
		return ntm33;
	}

	public void setNtm33(String ntm33) {
		this.ntm33 = ntm33;
	}

	public String getNfz31() {
		return nfz31;
	}

	public void setNfz31(String nfz31) {
		this.nfz31 = nfz31;
	}

	public String getNfz32() {
		return nfz32;
	}

	public void setNfz32(String nfz32) {
		this.nfz32 = nfz32;
	}

	public String getNfz33() {
		return nfz33;
	}

	public void setNfz33(String nfz33) {
		this.nfz33 = nfz33;
	}

	public String getPct1() {
		return pct1;
	}

	public void setPct1(String pct1) {
		this.pct1 = pct1;
	}

	public String getPct2() {
		return pct2;
	}

	public void setPct2(String pct2) {
		this.pct2 = pct2;
	}

	public String getPct3() {
		return pct3;
	}

	public void setPct3(String pct3) {
		this.pct3 = pct3;
	}

	public String getPtm11() {
		return ptm11;
	}

	public void setPtm11(String ptm11) {
		this.ptm11 = ptm11;
	}

	public String getPtm12() {
		return ptm12;
	}

	public void setPtm12(String ptm12) {
		this.ptm12 = ptm12;
	}

	public String getPtm13() {
		return ptm13;
	}

	public void setPtm13(String ptm13) {
		this.ptm13 = ptm13;
	}

	public String getPfz11() {
		return pfz11;
	}

	public void setPfz11(String pfz11) {
		this.pfz11 = pfz11;
	}

	public String getPfz12() {
		return pfz12;
	}

	public void setPfz12(String pfz12) {
		this.pfz12 = pfz12;
	}

	public String getPfz13() {
		return pfz13;
	}

	public void setPfz13(String pfz13) {
		this.pfz13 = pfz13;
	}

	public String getPtm21() {
		return ptm21;
	}

	public void setPtm21(String ptm21) {
		this.ptm21 = ptm21;
	}

	public String getPtm22() {
		return ptm22;
	}

	public void setPtm22(String ptm22) {
		this.ptm22 = ptm22;
	}

	public String getPtm23() {
		return ptm23;
	}

	public void setPtm23(String ptm23) {
		this.ptm23 = ptm23;
	}

	public String getPfz21() {
		return pfz21;
	}

	public void setPfz21(String pfz21) {
		this.pfz21 = pfz21;
	}

	public String getPfz22() {
		return pfz22;
	}

	public void setPfz22(String pfz22) {
		this.pfz22 = pfz22;
	}

	public String getPfz23() {
		return pfz23;
	}

	public void setPfz23(String pfz23) {
		this.pfz23 = pfz23;
	}

	public String getPtm31() {
		return ptm31;
	}

	public void setPtm31(String ptm31) {
		this.ptm31 = ptm31;
	}

	public String getPtm32() {
		return ptm32;
	}

	public void setPtm32(String ptm32) {
		this.ptm32 = ptm32;
	}

	public String getPtm33() {
		return ptm33;
	}

	public void setPtm33(String ptm33) {
		this.ptm33 = ptm33;
	}

	public String getPfz31() {
		return pfz31;
	}

	public void setPfz31(String pfz31) {
		this.pfz31 = pfz31;
	}

	public String getPfz32() {
		return pfz32;
	}

	public void setPfz32(String pfz32) {
		this.pfz32 = pfz32;
	}

	public String getPfz33() {
		return pfz33;
	}

	public void setPfz33(String pfz33) {
		this.pfz33 = pfz33;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getJcqf() {
		return jcqf;
	}

	public void setJcqf(String jcqf) {
		this.jcqf = jcqf;
	}

	public String getMbjy() {
		return mbjy;
	}

	public void setMbjy(String mbjy) {
		this.mbjy = mbjy;
	}

	public String getCt1() {
		return ct1;
	}

	public void setCt1(String ct1) {
		this.ct1 = ct1;
	}

	public String getCt2() {
		return ct2;
	}

	public void setCt2(String ct2) {
		this.ct2 = ct2;
	}

	public String getCt3() {
		return ct3;
	}

	public void setCt3(String ct3) {
		this.ct3 = ct3;
	}

	public String getTm11() {
		return tm11;
	}

	public void setTm11(String tm11) {
		this.tm11 = tm11;
	}

	public String getTm12() {
		return tm12;
	}

	public void setTm12(String tm12) {
		this.tm12 = tm12;
	}

	public String getTm13() {
		return tm13;
	}

	public void setTm13(String tm13) {
		this.tm13 = tm13;
	}

	public String getFz11() {
		return fz11;
	}

	public void setFz11(String fz11) {
		this.fz11 = fz11;
	}

	public String getFz12() {
		return fz12;
	}

	public void setFz12(String fz12) {
		this.fz12 = fz12;
	}

	public String getFz13() {
		return fz13;
	}

	public void setFz13(String fz13) {
		this.fz13 = fz13;
	}

	public String getTm21() {
		return tm21;
	}

	public void setTm21(String tm21) {
		this.tm21 = tm21;
	}

	public String getTm22() {
		return tm22;
	}

	public void setTm22(String tm22) {
		this.tm22 = tm22;
	}

	public String getTm23() {
		return tm23;
	}

	public void setTm23(String tm23) {
		this.tm23 = tm23;
	}

	public String getFz21() {
		return fz21;
	}

	public void setFz21(String fz21) {
		this.fz21 = fz21;
	}

	public String getFz22() {
		return fz22;
	}

	public void setFz22(String fz22) {
		this.fz22 = fz22;
	}

	public String getFz23() {
		return fz23;
	}

	public void setFz23(String fz23) {
		this.fz23 = fz23;
	}

	public String getTm31() {
		return tm31;
	}

	public void setTm31(String tm31) {
		this.tm31 = tm31;
	}

	public String getTm32() {
		return tm32;
	}

	public void setTm32(String tm32) {
		this.tm32 = tm32;
	}

	public String getTm33() {
		return tm33;
	}

	public void setTm33(String tm33) {
		this.tm33 = tm33;
	}

	public String getFz31() {
		return fz31;
	}

	public void setFz31(String fz31) {
		this.fz31 = fz31;
	}

	public String getFz32() {
		return fz32;
	}

	public void setFz32(String fz32) {
		this.fz32 = fz32;
	}

	public String getFz33() {
		return fz33;
	}

	public void setFz33(String fz33) {
		this.fz33 = fz33;
	}

	public String getYzjgid() {
		return yzjgid;
	}

	public void setYzjgid(String yzjgid) {
		this.yzjgid = yzjgid;
	}

	public String getYzid() {
		return yzid;
	}

	public void setYzid(String yzid) {
		this.yzid = yzid;
	}

	public String getYzjg() {
		return yzjg;
	}

	public void setYzjg(String yzjg) {
		this.yzjg = yzjg;
	}

	public String getJl() {
		return jl;
	}

	public void setJl(String jl) {
		this.jl = jl;
	}

	public String getYzlb() {
		return yzlb;
	}

	public void setYzlb(String yzlb) {
		this.yzlb = yzlb;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
