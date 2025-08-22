package com.matridx.igams.common.dao.entities.external;

/**
 * @author WYX
 * @version 1.0
 * {@code @className} MkResponseDto
 * {@code @description} TODO
 * {@code @date} 14:05 2023/4/23
 **/
public class MkResponseDto {
    private String code;
    private String message;
    private Object data;
    private boolean success;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
