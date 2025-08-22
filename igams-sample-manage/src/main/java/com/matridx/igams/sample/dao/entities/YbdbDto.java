package com.matridx.igams.sample.dao.entities;


import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="YbdbDto")
public class YbdbDto extends YbdbModel{
    //调拨人名称
    private String dbrmc;
    //调出储存单位名称
    private String dcccdwmc;
    //调出冰箱名称
    private String dcbxmc;
    //调出抽屉名称
    private String dcctmc;
    //调入储存单位名称
    private String drccdwmc;
    //调入冰箱名称
    private String drbxmc;
    //调入抽屉名称
    private String drctmc;
    //调出储存单位(多)
    private String[] dcccdws;
    //调出冰箱(多)
    private String[] dcbxs;
    //调出抽屉(多)
    private String[] dccts;
    //调入储存单位(多)
    private String[] drccdws;
    //调入冰箱(多)
    private String[] drbxs;
    //调入抽屉(多)
    private String[] drcts;
    //调拨开始日期
    private String dbrqstart;
    //调拨结束日期
    private String dbrqend;
    //父参数ID(多)
    private String[] fcsids;
    //调拨盒子明细List
    private List<YbdbmxDto> ybdbmxDtos;
    //位置
    private String wz;
    //调出冰箱设备号
    private String dcbxsbh;
    //调入冰箱设备号
    private String drbxsbh;
    //调出抽屉设备号
    private String dcctsbh;
    //调入抽屉设备号
    private String drctsbh;
    //调入盒子编号
    private String drhzbh;
    //盒子json
    private String hzInfo_json;
    //角色id
    private String jsid;
    //检测单位限制
    private List<String> jcdwxz;
    //预定标记
    private String ydbj;
    private String bxpx;
    private String ctpx;
    private String dcctwz;

    public String getDcctwz() {
        return dcctwz;
    }

    public void setDcctwz(String dcctwz) {
        this.dcctwz = dcctwz;
    }

    public String getCtpx() {
        return ctpx;
    }

    public void setCtpx(String ctpx) {
        this.ctpx = ctpx;
    }

    public String getBxpx() {
        return bxpx;
    }

    public void setBxpx(String bxpx) {
        this.bxpx = bxpx;
    }

    public String getYdbj() {
        return ydbj;
    }

    public void setYdbj(String ydbj) {
        this.ydbj = ydbj;
    }

    public String getJsid() {
        return jsid;
    }

    public void setJsid(String jsid) {
        this.jsid = jsid;
    }

    public List<String> getJcdwxz() {
        return jcdwxz;
    }

    public void setJcdwxz(List<String> jcdwxz) {
        this.jcdwxz = jcdwxz;
    }

    public String getHzInfo_json() {
        return hzInfo_json;
    }

    public void setHzInfo_json(String hzInfo_json) {
        this.hzInfo_json = hzInfo_json;
    }

    public String getDrhzbh() {
        return drhzbh;
    }

    public void setDrhzbh(String drhzbh) {
        this.drhzbh = drhzbh;
    }

    public String getDbrmc() {
        return dbrmc;
    }

    public void setDbrmc(String dbrmc) {
        this.dbrmc = dbrmc;
    }

    public String getDcccdwmc() {
        return dcccdwmc;
    }

    public void setDcccdwmc(String dcccdwmc) {
        this.dcccdwmc = dcccdwmc;
    }

    public String getDcbxmc() {
        return dcbxmc;
    }

    public void setDcbxmc(String dcbxmc) {
        this.dcbxmc = dcbxmc;
    }

    public String getDcctmc() {
        return dcctmc;
    }

    public void setDcctmc(String dcctmc) {
        this.dcctmc = dcctmc;
    }

    public String getDrccdwmc() {
        return drccdwmc;
    }

    public void setDrccdwmc(String drccdwmc) {
        this.drccdwmc = drccdwmc;
    }

    public String getDrbxmc() {
        return drbxmc;
    }

    public void setDrbxmc(String drbxmc) {
        this.drbxmc = drbxmc;
    }

    public String getDrctmc() {
        return drctmc;
    }

    public void setDrctmc(String drctmc) {
        this.drctmc = drctmc;
    }

    public String[] getDcccdws() {
        return dcccdws;
    }

    public void setDcccdws(String[] dcccdws) {
        this.dcccdws = dcccdws;
        for (int i = 0; i < dcccdws.length; i++) {
            this.dcccdws[i] = this.dcccdws[i].replace("'", "");
        }
    }

    public String[] getDcbxs() {
        return dcbxs;
    }

    public void setDcbxs(String[] dcbxs) {
        this.dcbxs = dcbxs;
        for (int i = 0; i < dcbxs.length; i++) {
            this.dcbxs[i] = this.dcbxs[i].replace("'", "");
        }
    }

    public String[] getDccts() {
        return dccts;
    }

    public void setDccts(String[] dccts) {
        this.dccts = dccts;
        for (int i = 0; i < dccts.length; i++) {
            this.dccts[i] = this.dccts[i].replace("'", "");
        }
    }

    public String[] getDrccdws() {
        return drccdws;
    }

    public void setDrccdws(String[] drccdws) {
        this.drccdws = drccdws;
        for (int i = 0; i < drccdws.length; i++) {
            this.drccdws[i] = this.drccdws[i].replace("'", "");
        }
    }

    public String[] getDrbxs() {
        return drbxs;
    }

    public void setDrbxs(String[] drbxs) {
        this.drbxs = drbxs;
        for (int i = 0; i < drbxs.length; i++) {
            this.drbxs[i] = this.drbxs[i].replace("'", "");
        }
    }

    public String[] getDrcts() {
        return drcts;
    }

    public void setDrcts(String[] drcts) {
        this.drcts = drcts;
        for (int i = 0; i < drcts.length; i++) {
            this.drcts[i] = this.drcts[i].replace("'", "");
        }
    }

    public String getDbrqstart() {
        return dbrqstart;
    }

    public void setDbrqstart(String dbrqstart) {
        this.dbrqstart = dbrqstart;
    }

    public String getDbrqend() {
        return dbrqend;
    }

    public void setDbrqend(String dbrqend) {
        this.dbrqend = dbrqend;
    }


    public String[] getFcsids() {
        return fcsids;
    }
    public void setFcsids(String[] fcsids) {
        this.fcsids = fcsids;
        for(int i=0;i<this.fcsids.length;i++)
        {
            this.fcsids[i] = this.fcsids[i].replace("'", "");
        }
    }

    public List<YbdbmxDto> getYbdbmxDtos() {
        return ybdbmxDtos;
    }

    public void setYbdbmxDtos(List<YbdbmxDto> ybdbmxDtos) {
        this.ybdbmxDtos = ybdbmxDtos;
    }

    public String getWz() {
        return wz;
    }

    public void setWz(String wz) {
        this.wz = wz;
    }

    public String getDcbxsbh() {
        return dcbxsbh;
    }

    public void setDcbxsbh(String dcbxsbh) {
        this.dcbxsbh = dcbxsbh;
    }

    public String getDrbxsbh() {
        return drbxsbh;
    }

    public void setDrbxsbh(String drbxsbh) {
        this.drbxsbh = drbxsbh;
    }

    public String getDcctsbh() {
        return dcctsbh;
    }

    public void setDcctsbh(String dcctsbh) {
        this.dcctsbh = dcctsbh;
    }

    public String getDrctsbh() {
        return drctsbh;
    }

    public void setDrctsbh(String drctsbh) {
        this.drctsbh = drctsbh;
    }
}
