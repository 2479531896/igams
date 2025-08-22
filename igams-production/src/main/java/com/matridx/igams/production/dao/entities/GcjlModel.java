package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.entities.BaseBasicModel;

/**
 * @author:JYK
 */
@Alias(value = "GcjlModel")
public class GcjlModel  extends BaseBasicModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private String gcjlid;//观察记录id
    private String lykcid;//观察记录id
    private String gcyq;//观察记录id
    private String gcjg;//观察记录id
    private String bz;//观察记录id

    public String getGcjlid() {
        return gcjlid;
    }

    public void setGcjlid(String gcjlid) {
        this.gcjlid = gcjlid;
    }

    public String getLykcid() {
        return lykcid;
    }

    public void setLykcid(String lykcid) {
        this.lykcid = lykcid;
    }

    public String getGcyq() {
        return gcyq;
    }

    public void setGcyq(String gcyq) {
        this.gcyq = gcyq;
    }

    public String getGcjg() {
        return gcjg;
    }

    public void setGcjg(String gcjg) {
        this.gcjg = gcjg;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }
}
