package com.matridx.igams.common.enums;

/**
 * @author WYX
 * @version 1.0
 * @className AttendanceEnum
 * @description 考勤报表中的列信息。通过获取列信息中的ID值，可以根据列的ID查询考勤智能报表中该列的统计数据
 * @date 14:32 2023/5/26
 **/
public enum AttendanceEnum {
    早退扣款(530121114L,"早退扣款"),
    夜班补贴统计(584832983L,"夜班补贴统计"),
    OFF_DUTY_USER_CHECK_RESULT_1(11725173L,"下班1打卡结果"),
    OFF_DUTY_USER_CHECK_TIME_1(11725172L,"下班1打卡时间"),
    ON_DUTY_USER_CHECK_RESULT_1(11725171L,"上班1打卡结果"),
    ON_DUTY_USER_CHECK_TIME_1(11725170L,"上班1打卡时间"),
    OFF_DUTY_USER_CHECK_RESULT_2(11725177L,"下班2打卡结果"),
    OFF_DUTY_USER_CHECK_TIME_2(11725176L,"下班2打卡时间"),
    ON_DUTY_USER_CHECK_RESULT_2(11725175L,"上班2打卡结果"),
    ON_DUTY_USER_CHECK_TIME_2(11725174L,"上班2打卡时间"),
    OFF_DUTY_USER_CHECK_RESULT_3(11725181L,"下班3打卡结果"),
    OFF_DUTY_USER_CHECK_TIME_3(11725180L,"下班3打卡时间"),
    ON_DUTY_USER_CHECK_RESULT_3(11725179L,"上班3打卡结果"),
    ON_DUTY_USER_CHECK_TIME_3(11725178L,"上班3打卡时间"),
    ABSENTEEISM_DAYS(11725160L,"旷工天数"),
    ABSENTEEISM_LATE_TIMES(11725155L,"旷工迟到天数"),
    ATTENDANCE_APPROVE(11725182L,"关联的审批单"),
    ATTENDANCE_CLASS(11725147L,"出勤班次"),
    ATTENDANCE_DAYS(11725148L,"出勤天数"),
    ATTENDANCE_REST_DAYS(11725149L,"休息天数"),
    ATTENDANCE_WORK_TIME(11725150L,"工作时长"),
    ATTEND_RESULT(11725168L,"考勤结果"),
    BUSINESS_TRIP_TIME(11725161L,"出差时长"),
    FEE_OVERTIME_休息日(63212867L,"休息日（转加班费）"),
    FEE_OVERTIME_工作日(63212866L,"工作日（转加班费）"),
    FEE_OVERTIME_节假日(63212868L,"节假日（转加班费）"),
    LATE_MINUTE(11725152L,"迟到时长"),
    LATE_TIMES(11725151L,"迟到次数"),
    LEAVE_EARLY_MINUTE(11725157L,"早退时长"),
    LEAVE_EARLY_TIMES(11725156L,"早退次数"),
    MAKING_UP_LACK_TIMES(186305464L,"补卡次数"),
    OFF_WORK_LACK_CARD_TIMES(11725159L,"下班缺卡次数"),
    ON_WORK_LACK_CARD_TIMES(11725158L,"上班缺卡次数"),
    OUT_TIME(11725162L,"外出时长"),
    OVERTIME_APPROVE_COUNT(11725164L,"加班-审批单统计"),
    OVERTIME_DURATION(63212865L,"加班总时长"),
    OVERTIME_休息日加班(11725166L,"休息日加班"),
    OVERTIME_工作日加班(11725165L,"工作日加班"),
    OVERTIME_节假日加班(11725167L,"节假日加班"),
    PLAN_DETAIL(11725169L,"班次"),
    REST_OVERTIME_休息日(63212870L,"休息日（转调休）"),
    REST_OVERTIME_工作日(63212869L,"工作日（转调休）"),
    REST_OVERTIME_节假日(63212871L,"节假日（转调休）"),
    SERIOUS_LATE_MINUTE(11725154L,"严重迟到时长"),
    SERIOUS_LATE_TIMES(11725153L,"严重迟到次数"),
    SHOULD_ATTENDANCE_DAYS(11725146L,"应出勤天数"),
    CUSTOM_FIELD(999999999L,"自定义字段"),
        ;

    public Long id;
    public String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    AttendanceEnum(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    public static AttendanceEnum getById(Long id) {
        for (AttendanceEnum attendanceEnum : AttendanceEnum.values()) {
            if (attendanceEnum.getId().equals(id)) {
                return attendanceEnum;
            }
        }
        return CUSTOM_FIELD;
    }
}
