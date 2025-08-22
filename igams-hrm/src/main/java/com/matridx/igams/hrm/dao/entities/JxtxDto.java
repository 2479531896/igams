package com.matridx.igams.hrm.dao.entities;

import com.matridx.springboot.util.base.StringUtil;
import org.apache.ibatis.type.Alias;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author WYX
 * @version 1.0
 * @className JxtxDto
 * @description TODO
 * @date 9:09 2023/3/24
 **/
@Alias(value="JxtxDto")
public class JxtxDto extends JxtxModel{

    private String khzqmc;//考核周期名称
    private String lrrymc;//录入人员名称
    private String entire;//查询
    private String bmmc;//部门
    private String yhm;//用户名
    private String zsxm;//真实姓名
    private String gwmc;//岗位名称
    private List<String> khzqs;//考核周期
    private List<String> zjs;//职级
    private String jxtxmx_json;

    public String getJxtxmx_json() {
        return jxtxmx_json;
    }

    public void setJxtxmx_json(String jxtxmx_json) {
        this.jxtxmx_json = jxtxmx_json;
    }

    public void setZjs(String zjs) {
        List<String> list = new ArrayList<String>();
        if(StringUtil.isNotBlank(zjs)) {
            String[] str = zjs.split(",");
            list = Arrays.asList(str);
        }
        this.zjs = list;
    }
    public List<String> getZjs() {
        return zjs;
    }

    public void setZjs(List<String> zjs) {
        this.zjs = zjs;
    }

    public List<String> getKhzqs() {
        return khzqs;
    }

    public void setKhzqs(List<String> khzqs) {
        this.khzqs = khzqs;
    }

    public void setKhzqs(String khzqs) {
        List<String> list = new ArrayList<String>();
        if(StringUtil.isNotBlank(khzqs)) {
            String[] str = khzqs.split(",");
            list = Arrays.asList(str);
        }
        this.khzqs = list;
    }

    public String getGwmc() {
        return gwmc;
    }

    public void setGwmc(String gwmc) {
        this.gwmc = gwmc;
    }

    public String getKhzqmc() {
        return khzqmc;
    }

    public void setKhzqmc(String khzqmc) {
        this.khzqmc = khzqmc;
    }

    public String getLrrymc() {
        return lrrymc;
    }

    public void setLrrymc(String lrrymc) {
        this.lrrymc = lrrymc;
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    public String getBmmc() {
        return bmmc;
    }

    public void setBmmc(String bmmc) {
        this.bmmc = bmmc;
    }

    public String getYhm() {
        return yhm;
    }

    public void setYhm(String yhm) {
        this.yhm = yhm;
    }

    public String getZsxm() {
        return zsxm;
    }

    public void setZsxm(String zsxm) {
        this.zsxm = zsxm;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
