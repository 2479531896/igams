package com.matridx.crf.web.enums;

public enum YYSEnum {
    YYS_NXALKSS("1","β内酰胺类抗生素"),
    YYS_DHNZLKSS("2","大环内酯类抗生素"),
    YYS_AJGLKSS("3","氨基甙类抗生素"),
    YYS_SHSLKSS("4","四环素类抗生素"),
    YYS_LKMSLKSS("5","林可霉素类抗生素"),
    YYS_LMSLKSS("6","氯霉素类抗生素"),
    YYS_WGMSLKSS("7","万古霉素类"),
    YYS_KNTLYW("8","喹诺酮类药物"),
    YYS_ZZBYZJ("9","质子泵抑制剂(抑酸类药物)"),
    YYS_XBDXLYW_SWJL("10","细胞毒性类药物（抗肿瘤）生物碱类"),
    YYS_XBDXLYW_DXL("11","细胞毒性类药物（抗肿瘤）代谢类"),
    YYS_XBDXLYW_KSSL("12","细胞毒性类药物（抗肿瘤）抗生素类"),
    YYS_XBDXLYW_WHJL("13","细胞毒性类药物（抗肿瘤）烷化剂类"),
    YYS_XBDXLYW_BJL("14","细胞毒性类药物（抗肿瘤）铂剂类"),
    ;
    private String code;
    private String value;

    private YYSEnum(String code,String value) {
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
        for (YYSEnum enumi : values()) {
            if(enumi.getCode().equals(code)){
                return enumi.getValue();
            }
        }
        return null;
    }
}
