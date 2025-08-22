package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

/**
 * @author : 郭祥杰
 * @date :
 */
@Alias(value="DdspjlDto")
public class DdspjlDto extends DdspjlModel{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String splx;
    private String zsxm;
    private String entire;
    private String createStart;
    private String createEnd;
    private String finishStart;
    private String finishEnd;
    private String[] splxs;

    public String getCreateEnd() {
        return createEnd;
    }

    public void setCreateEnd(String createEnd) {
        this.createEnd = createEnd;
    }

    public String getFinishStart() {
        return finishStart;
    }

    public void setFinishStart(String finishStart) {
        this.finishStart = finishStart;
    }

    public String[] getSplxs() {
        return splxs;
    }

    public void setSplxs(String[] splxs) {
        this.splxs = splxs;
        for (int i = 0; i < splxs.length; i++) {
            this.splxs[i] = this.splxs[i].replace("'", "");
        }
    }
    public String getCreateStart() {
        return createStart;
    }

    public void setCreateStart(String createStart) {
        this.createStart = createStart;
    }

    public String getFinishEnd() {
        return finishEnd;
    }

    public void setFinishEnd(String finishEnd) {
        this.finishEnd = finishEnd;
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    public String getSplx() {
        return splx;
    }

    public void setSplx(String splx) {
        this.splx = splx;
    }

    public String getZsxm() {
        return zsxm;
    }

    public void setZsxm(String zsxm) {
        this.zsxm = zsxm;
    }
}
