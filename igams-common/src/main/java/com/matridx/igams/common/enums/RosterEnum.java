package com.matridx.igams.common.enums;
/**
 * 钉钉花名册枚举类
 *
 */
public enum RosterEnum {
    EMPLOYEE_NAME("sys00-name","姓名"),
    EMPLOYEE_AGE("sys02-age","年龄"),
    EMAIL("sys00-email","邮箱"),
    DEPT("sys00-dept","部门"),
    DEPTID("sys00-deptId","部门id"),
    DEPTIDS("sys00-deptIds","部门ids"),
    MAIN_DEPT("sys00-mainDept","主部门"),
    MAIN_DEPTID("sys00-mainDeptId","主部门id"),
    POSITION("sys00-position","职位"),
    MOBILE("sys00-mobile","手机号"),
    JOB_NUMBER("sys00-jobNumber","工号"),
    TEL("sys00-tel","分机号"),
    WORK_PLACE("sys00-workPlace","办公地点"),
    REMARK("sys00-remark","备注"),
    CONFIRM_JOIN_TIME("sys00-confirmJoinTime","入职时间"),
    ENTRYAGE("sys00-entryAge","司龄"),
    EMPLOYEE_TYPE("sys01-employeeType","员工类型"),
    EMPLOYEE_STATUS("sys01-employeeStatus","员工状态"),
    PROBATION_PERIOD_TYPE("sys01-probationPeriodType","试用期"),
    REGULAR_TIME("sys01-regularTime","转正日期"),
    POSITION_LEVEL("sys01-positionLevel","岗位职级"),
    REAL_NAME("sys02-realName","身份证姓名"),
    CERT_NO("sys02-certNo","证件号码"),
    BIRTH_TIME("sys02-birthTime","出生日期"),
    SEX_TYPE("sys02-sexType","性别"),
    NATION_TYPE("sys02-nationType","民族"),
    CERT_ADDRESS("sys02-certAddress","身份证地址"),
    CERT_END_TIME("sys02-certEndTime","证件有效期"),
    MARRIAGE("sys02-marriage","婚姻状况"),
    JOIN_WORKING_TIME("sys02-joinWorkingTime","首次参加工作时间"),
    RESIDENCE_TYPE("sys02-residenceType","户籍类型"),
    ADDRESS("sys02-address","住址"),
    POLITICAL_STATUS("sys02-politicalStatus","政治面貌"),
    PERSONAL_SI("sys09-personalSi","个人社保账号"),
    PERSONAL_HF("sys09-personalHf","个人公积金账号"),
    HIGHEST_EDU("sys03-highestEdu","最高学历"),
    GRADUATE_SCHOOL("sys03-graduateSchool","毕业院校"),
    GRADUATION_TIME("sys03-graduationTime","毕业时间"),
    MAJOR("sys03-major","所学专业"),
    BANKACCOUNT_NO("sys04-bankAccountNo","银行卡号"),
    ACCOUNT_BANK("sys04-accountBank","开户行"),
    CONTRACT_COMPANY_NAME("sys05-contractCompanyName","合同公司"),
    CONTRACT_TYPE("sys05-contractType","合同类型"),
    FIRST_CONTRACT_STARTTIME("sys05-firstContractStartTime","首次合同起始日"),
    FIRST_CONTRACT_END_TIME("sys05-firstContractEndTime","首次合同到期日"),
    NOW_CONTRACT_START_TIME("sys05-nowContractStartTime","现合同起始日"),
    NOW_CONTRACT_END_TIME("sys05-nowContractEndTime","现合同到期日"),
    CONTRACT_PERIOD_TYPE("sys05-contractPeriodType","合同期限"),
    CONTRACT_RENEW_COUNT("sys05-contractRenewCount","续签次数"),
    URGENT_CONTACTS_NAME("sys06-urgentContactsName","紧急联系人姓名"),
    URGENT_CONTACTS_RELATION("sys06-urgentContactsRelation","联系人关系"),
    URGENT_CONTACTS_PHONE("sys06-urgentContactsPhone","联系人电话"),
    HAVE_CHILD("sys07-haveChild","有无子女"),
    CHILD_NAME("sys07-childName","子女姓名"),
    CHILD_SEX("sys07-childSex","子女性别"),
    CHILD_BIRTH_DATE("sys07-childBirthDate","子女出生日期"),
    CUSTOM_FIELD("customField","自定义字段");

    public final String name;
    public final String code;
    RosterEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
    public static RosterEnum getByCode(String code) {
        for (RosterEnum rosterEnum : RosterEnum.values()) {
            if (rosterEnum.code.equals(code)) {
                return rosterEnum;
            }
        }
        return CUSTOM_FIELD;
    }
}