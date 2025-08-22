package com.matridx.igams.common.util;

/**
 * 节假日Vo
 */

public class HolidayVo {
    private String data;//日期

    private String status;//状态：0工作日/1周末/2法定节假日/3节假日调休补班

    private String msg;//描述

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}