package com.matridx.igams.bioinformation.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="ChipModel")
public class ChipModel extends BaseModel {
    //芯片ID
    private String chipid;
    //测序仪ID关联基础数据
    private String sequencer;
    //测序表中有送检ID的/文库总数
    private String clinicaltotal;
    //文库中已发送报告的/已经审核的
    private String reportsentreviewed;
    //统计
    private String chipstats;
    private String lastupdate;

    public String getChipid() {
        return chipid;
    }

    public void setChipid(String chipid) {
        this.chipid = chipid;
    }

    public String getSequencer() {
        return sequencer;
    }

    public void setSequencer(String sequencer) {
        this.sequencer = sequencer;
    }

    public String getClinicaltotal() {
        return clinicaltotal;
    }

    public void setClinicaltotal(String clinicaltotal) {
        this.clinicaltotal = clinicaltotal;
    }

    public String getReportsentreviewed() {
        return reportsentreviewed;
    }

    public void setReportsentreviewed(String reportsentreviewed) {
        this.reportsentreviewed = reportsentreviewed;
    }

    public String getChipstats() {
        return chipstats;
    }

    public void setChipstats(String chipstats) {
        this.chipstats = chipstats;
    }

    public String getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(String lastupdate) {
        this.lastupdate = lastupdate;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
