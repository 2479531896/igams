package com.matridx.igams.wechat.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;




/**
 * 送检扩展信息(IgamsSjkzxx)实体类
 *
 * @author makejava
 * @since 2023-04-12 10:17:47
 */
@Alias(value="SjkzxxModel")
public class SjkzxxModel extends BaseBasicModel {
    private static final long serialVersionUID = 1L;

    /**
     * 送检ID
     */
    private String sjid;
    /**
     * 应收账款ID
     */
    private String yszkid;
    /**
     * 是否对账
     */
    private String sfdz;
    /**
     * 标本送达温度
     */
    private String bbsdwd;
    /**
     * 其他信息
     */
    private String qtxx;

    public String getQtxx() {
        return qtxx;
    }

    public void setQtxx(String qtxx) {
        this.qtxx = qtxx;
    }

    public String getBbsdwd() {
        return bbsdwd;
    }

    public void setBbsdwd(String bbsdwd) {
        this.bbsdwd = bbsdwd;
    }

    public String getSjid() {
        return sjid;
    }

    public void setSjid(String sjid) {
        this.sjid = sjid;
    }

    public String getYszkid() {
        return yszkid;
    }

    public void setYszkid(String yszkid) {
        this.yszkid = yszkid;
    }

    public String getSfdz() {
        return sfdz;
    }

    public void setSfdz(String sfdz) {
        this.sfdz = sfdz;
    }


}

