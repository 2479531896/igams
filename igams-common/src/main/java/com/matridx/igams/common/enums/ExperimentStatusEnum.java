package com.matridx.igams.common.enums;

/**
 * @author : 郭祥杰
 * @date :
 */
public enum ExperimentStatusEnum {
    EXPERIMENT_PREPARE("00","准备中"),//准备中
    EXPERIMENT_START("10","开始"),//开始
    EXPERIMENT_ING("20","实验中"),//实验中
    EXPERIMENT_END("80","结束");//结束

    private final String code;
    private final String value;

    ExperimentStatusEnum(String code, String value) {
        this.value = value;
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public String getCode(){
        return code;
    }

    public static String getValueByCode(String code){
        for (ExperimentStatusEnum enumi : values()) {
            if(enumi.getCode().equals(code)){
                return enumi.getValue();
            }
        }
        return null;
    }
}
