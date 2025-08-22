package com.matridx.igams.wechat.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;


/**
 * 商务合同明细(IgamsSwhtmx)实体类
 *
 * @author makejava
 * @since 2023-04-04 13:52:16
 */

@Alias(value="SwhtmxModel")
public class SwhtmxModel  extends BaseModel {
    private static final long serialVersionUID = 1L;
    /**
     * 合同明细ID
     */
    private String htmxid;
    /**
     * 合同ID
     */
    private String htid;
    /**
     * 序号
     */
    private String xh;
    /**
     * 检测项目id
     */
    private String jcxmid;
    /**
     * 检测子项目ID
     */
    private String jczxmid;
    /**
     * 价格
     */
    private String jg;
    /**
     * 是否有效
     */
    private String sfyx;

    public String getHtmxid() {
        return htmxid;
    }

    public void setHtmxid(String htmxid) {
        this.htmxid = htmxid;
    }

    public String getHtid() {
        return htid;
    }

    public void setHtid(String htid) {
        this.htid = htid;
    }

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getJcxmid() {
        return jcxmid;
    }

    public void setJcxmid(String jcxmid) {
        this.jcxmid = jcxmid;
    }

    public String getJczxmid() {
        return jczxmid;
    }

    public void setJczxmid(String jczxmid) {
        this.jczxmid = jczxmid;
    }

    public String getJg() {
        return jg;
    }

    public void setJg(String jg) {
        this.jg = jg;
    }

    public String getSfyx() {
        return sfyx;
    }

    public void setSfyx(String sfyx) {
        this.sfyx = sfyx;
    }



}

