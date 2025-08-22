package com.matridx.springboot.util.encrypt;

import com.aliyun.tea.TeaException;

public class Atest {


        /**
         * 使用 Token 初始化账号Client
         * @return Client
         * @throws Exception
         */
        public static com.aliyun.dingtalkattendance_1_0.Client createClient() throws Exception {
            com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
            config.protocol = "https";
            config.regionId = "central";
            return new com.aliyun.dingtalkattendance_1_0.Client(config);
        }

        public static void main(String[] args_) throws Exception {
            java.util.List<String> args = java.util.Arrays.asList(args_);
            com.aliyun.dingtalkattendance_1_0.Client client = Atest.createClient();
            com.aliyun.dingtalkattendance_1_0.models.AddLeaveTypeHeaders addLeaveTypeHeaders = new com.aliyun.dingtalkattendance_1_0.models.AddLeaveTypeHeaders();
            addLeaveTypeHeaders.xAcsDingtalkAccessToken = "6c25e29aeaad389c8976c6e148594e31";
            com.aliyun.dingtalkattendance_1_0.models.AddLeaveTypeRequest.AddLeaveTypeRequestLeaveCertificate leaveCertificate = new com.aliyun.dingtalkattendance_1_0.models.AddLeaveTypeRequest.AddLeaveTypeRequestLeaveCertificate()
                    .setUnit("hour")
                    .setDuration(1D)
                    .setEnable(false)
                    .setPromptInformation("请假文案");
            com.aliyun.dingtalkattendance_1_0.models.AddLeaveTypeRequest.AddLeaveTypeRequestSubmitTimeRule submitTimeRule = new com.aliyun.dingtalkattendance_1_0.models.AddLeaveTypeRequest.AddLeaveTypeRequestSubmitTimeRule()
                    .setTimeValue(2L)
                    .setTimeUnit("day")
                    .setTimeType("before")
                    .setEnableTimeLimit(false);
            com.aliyun.dingtalkattendance_1_0.models.AddLeaveTypeRequest.AddLeaveTypeRequestVisibilityRules visibilityRules0 = new com.aliyun.dingtalkattendance_1_0.models.AddLeaveTypeRequest.AddLeaveTypeRequestVisibilityRules()
                    .setVisible(java.util.Arrays.asList(
                            "052245582240189250",
                            "124056584420327386",
                            "16165307181043789",
                            "163814465329257419",
                            "216400591138884","164035133737613640",
                            "116347186933324206",
                            "49152840351150033",
                            "325810080220408399",
                            "061955116926357964",
                            "104933644122807952",
                            "021525061836643928",
                            "186647501327686010","1537450130695973",
                            "040604610937345850","31236164471282336",
                            "01096110331926605475","231804584238183856"


                    ))
                    .setType("staff");
            com.aliyun.dingtalkattendance_1_0.models.AddLeaveTypeRequest addLeaveTypeRequest = new com.aliyun.dingtalkattendance_1_0.models.AddLeaveTypeRequest()
                    .setOpUserId("16165307181043789")
                    .setLeaveName("测试年假")
                    .setLeaveViewUnit("day")
                    .setBizType("general_leave")
                    .setNaturalDayLeave(false)
                    .setHoursInPerDay(800L)
                    .setExtras("{\"validity_type\":\"absolute_time\",\"validity_value\":\"3-31\"}")
                    .setVisibilityRules(java.util.Arrays.asList(
                            visibilityRules0
                    ))
                    .setSubmitTimeRule(submitTimeRule)
                    .setLeaveCertificate(leaveCertificate);
            try {
                client.addLeaveTypeWithOptions(addLeaveTypeRequest, addLeaveTypeHeaders, new com.aliyun.teautil.models.RuntimeOptions());
                System.out.print("surrces");
            } catch (TeaException err) {
                if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                    // err 中含有 code 和 message 属性，可帮助开发定位问题
                }

            } catch (Exception _err) {
                TeaException err = new TeaException(_err.getMessage(), _err);
                if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                    // err 中含有 code 和 message 属性，可帮助开发定位问题
                }

            }
        }

}
