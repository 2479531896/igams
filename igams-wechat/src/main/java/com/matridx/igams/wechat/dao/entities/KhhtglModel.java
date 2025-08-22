package com.matridx.igams.wechat.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

/**
 * 客户合同管理(IgamsKhhtgl)实体类
 *
 * @author makejava
 * @since 2023-04-04 13:50:53
 */
@Alias(value="KhhtglModel")
public class KhhtglModel extends BaseModel {
    private static final long serialVersionUID = 1L;
    /**
     * 客户ID
     */
    private String khid;
    /**
     * 序号
     */
    private String xh;
    /**
     * 合同ID
     */
    private String htid;


    public String getKhid() {
        return khid;
    }

    public void setKhid(String khid) {
        this.khid = khid;
    }

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getHtid() {
        return htid;
    }

    public void setHtid(String htid) {
        this.htid = htid;
    }

}

