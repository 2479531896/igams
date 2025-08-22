package com.matridx.igams.hrm.dao.entities;

import com.matridx.springboot.util.base.StringUtil;
import org.apache.ibatis.type.Alias;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Alias(value = "BcglDto")
public class BcglDto extends BcglModel {
    private String btmc;//补贴名称
    private String bzsc;//标准时长
    private String bzsj;//标准时间
    private String bzje;//标准金额
    private String dzje;//递增金额
    private String dzjg;//递增间隔
    private String dzkssj;//递增开始时间
    private String fdje;//封顶金额
    private String lrrymc;
    private String xgrymc;
    private String bzjrcr;//标准时间 1：今日 0：次日
    private String dzjrcr;//递增时间 1：今日 0：次日
    //复数ID
    private List<String> bcids;
    public List<String> getBcids() {
        return bcids;
    }
    public void setBcids(String bcids) {
        List<String> list = new ArrayList<String>();
        if(StringUtil.isNotBlank(bcids)) {
            String[] str = bcids.split(",");
            list = Arrays.asList(str);
        }
        this.bcids = list;
    }
    public void setBcids(List<String> bcids) {
        if(!CollectionUtils.isEmpty(bcids)){
            for(int i=0;i<bcids.size();i++){
                bcids.set(i,bcids.get(i).replace("[", "").replace("]", "").trim());
            }
        }
        this.bcids = bcids;
    }
    public String getLrrymc() {
        return lrrymc;
    }

    public void setLrrymc(String lrrymc) {
        this.lrrymc = lrrymc;
    }

    public String getXgrymc() {
        return xgrymc;
    }

    public void setXgrymc(String xgrymc) {
        this.xgrymc = xgrymc;

    }
    public String getBzjrcr() {
        return bzjrcr;

    }
    public void setBzjrcr(String bzjrcr) {
        this.bzjrcr = bzjrcr;
    }
    public String getDzjrcr() {
        return dzjrcr;
    }

    public void setDzjrcr(String dzjrcr) {
        this.dzjrcr = dzjrcr;
    }

    public String getBtmc() {
        return btmc;
    }

    public void setBtmc(String btmc) {
        this.btmc = btmc;
    }

    public String getBzsc() {
        return bzsc;
    }

    public void setBzsc(String bzsc) {
        this.bzsc = bzsc;
    }

    public String getBzsj() {
        return bzsj;
    }

    public void setBzsj(String bzsj) {
        this.bzsj = bzsj;
    }

    public String getBzje() {
        return bzje;
    }

    public void setBzje(String bzje) {
        this.bzje = bzje;
    }

    public String getDzje() {
        return dzje;
    }

    public void setDzje(String dzje) {
        this.dzje = dzje;
    }

    public String getDzjg() {
        return dzjg;
    }

    public void setDzjg(String dzjg) {
        this.dzjg = dzjg;
    }

    public String getDzkssj() {
        return dzkssj;
    }

    public void setDzkssj(String dzkssj) {
        this.dzkssj = dzkssj;
    }

    public String getFdje() {
        return fdje;
    }

    public void setFdje(String fdje) {
        this.fdje = fdje;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

}
