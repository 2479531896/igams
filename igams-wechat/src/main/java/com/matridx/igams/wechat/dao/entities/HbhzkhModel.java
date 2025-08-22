package com.matridx.igams.wechat.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

/**
 * 伙伴合作客户(IgamsHbhzkh)实体类
 *
 * @author makejava
 * @since 2023-04-13 09:59:38
 */
@Alias(value="HbhzkhModel")
public class HbhzkhModel extends BaseModel {
    private static final long serialVersionUID = 1L;
    /**
     * 序号
     */
    private String xh;
    /**
     * 伙伴ID
     */
    private String hbid;
    /**
     * 客户ID
     */
    private String khid;
    /**
     * 主要客户
     */
    private String zykh;

    public String getZykh() {
        return zykh;
    }

    public void setZykh(String zykh) {
        this.zykh = zykh;
    }

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getHbid() {
        return hbid;
    }

    public void setHbid(String hbid) {
        this.hbid = hbid;
    }

    public String getKhid() {
        return khid;
    }

    public void setKhid(String khid) {
        this.khid = khid;
    }

}

