package com.matridx.igams.production.dao.entities;

import com.matridx.springboot.util.base.StringUtil;
import org.apache.ibatis.type.Alias;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author:JYK
 */
@Alias("SbtsjlDto")
public class SbtsjlDto extends SbtsjlModel{
    private String sbmc;//设备名称
    private String ggxh;//规格型号
    private String sccj;//生产厂家
    private String sbccbh;//设备出场编号
    private String dzwz;//定制位置
    private String ccrq;//出厂日期
    private String sbbh;//设备编号
    private String jlbh;//记录编号
    private String yjrmc;
    private String ysrmc;
    private String yxrq;
    private String sybmmc;
    private String sydd;
    private String yjsj;

    public String getYjsj() {
        return yjsj;
    }

    public void setYjsj(String yjsj) {
        this.yjsj = yjsj;
    }

    public String getYjrmc() {
        return yjrmc;
    }

    public void setYjrmc(String yjrmc) {
        this.yjrmc = yjrmc;
    }

    public String getYsrmc() {
        return ysrmc;
    }

    public void setYsrmc(String ysrmc) {
        this.ysrmc = ysrmc;
    }


    public String getYxrq() {
        return yxrq;
    }

    public void setYxrq(String yxrq) {
        this.yxrq = yxrq;
    }


    public String getSybmmc() {
        return sybmmc;
    }

    public void setSybmmc(String sybmmc) {
        this.sybmmc = sybmmc;
    }

    public String getSydd() {
        return sydd;
    }

    public void setSydd(String sydd) {
        this.sydd = sydd;
    }

    public String getJlbh() {
        return jlbh;
    }

    public void setJlbh(String jlbh) {
        this.jlbh = jlbh;
    }

    public String getSbbh() {
        return sbbh;
    }

    public void setSbbh(String sbbh) {
        this.sbbh = sbbh;
    }

    public String getSbmc() {
        return sbmc;
    }

    public void setSbmc(String sbmc) {
        this.sbmc = sbmc;
    }

    public String getGgxh() {
        return ggxh;
    }

    public void setGgxh(String ggxh) {
        this.ggxh = ggxh;
    }

    public String getSccj() {
        return sccj;
    }

    public void setSccj(String sccj) {
        this.sccj = sccj;
    }

    public String getSbccbh() {
        return sbccbh;
    }

    public void setSbccbh(String sbccbh) {
        this.sbccbh = sbccbh;
    }

    public String getDzwz() {
        return dzwz;
    }

    public void setDzwz(String dzwz) {
        this.dzwz = dzwz;
    }

    public String getCcrq() {
        return ccrq;
    }

    public void setCcrq(String ccrq) {
        this.ccrq = ccrq;
    }

    //复数ID
    private List<String> llids;
    public List<String> getLlids() {
        return llids;
    }
    public void setLlids(String llids) {
        List<String> list = new ArrayList<>();
        if(StringUtil.isNotBlank(llids)) {
            String[] str = llids.split(",");
            list = Arrays.asList(str);
        }
        this.llids = list;
    }
    public void setLlids(List<String> llids) {
        if(!CollectionUtils.isEmpty(llids)){
            llids.replaceAll(s -> s.replace("[", "").replace("]", "").trim());
        }
        this.llids = llids;
    }
}
