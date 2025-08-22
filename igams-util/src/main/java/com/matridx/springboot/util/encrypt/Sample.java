package com.matridx.springboot.util.encrypt;

import com.aliyun.dingtalktodo_1_0.models.CreateTodoTaskHeaders;
import com.aliyun.dingtalktodo_1_0.models.CreateTodoTaskRequest;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;

public class Sample {

    /**
     * 使用 Token 初始化账号Client
     * @return Client
     * @throws Exception
     */
//    public static com.aliyun.dingtalkworkflow_1_0.Client createClient() throws Exception {
//        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
//        config.protocol = "https";
//        config.regionId = "central";
//        return new com.aliyun.dingtalkworkflow_1_0.Client(config);
//    }
    public static com.aliyun.dingtalkoauth2_1_0.Client createClient2() throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalkoauth2_1_0.Client(config);
    }

    public static com.aliyun.dingtalktodo_1_0.Client createClient3() throws Exception {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalktodo_1_0.Client(config);
    }

    public static void main(String[] args_) throws Exception {
        String token = "";
        java.util.List<String> args2 = java.util.Arrays.asList(args_);
        com.aliyun.dingtalkoauth2_1_0.Client client2 = Sample.createClient2();
        com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest getAccessTokenRequest = new com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest()
                .setAppKey("ding67bwbaypvlgmdjyj")
                .setAppSecret("d6C0UXiJBnN3boPo20aTbsbXALgbjqHx3fBf_iGBnFvh3qvo1glEPCFi8aVmnPqa");
        try {
            token = client2.getAccessToken(getAccessTokenRequest).getBody().getAccessToken();
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

//        java.util.List<String> args = java.util.Arrays.asList(args_);
//        com.aliyun.dingtalkworkflow_1_0.Client client = Sample.createClient();
//        com.aliyun.dingtalkworkflow_1_0.models.QuerySchemaByProcessCodeHeaders querySchemaByProcessCodeHeaders = new com.aliyun.dingtalkworkflow_1_0.models.QuerySchemaByProcessCodeHeaders();
//        querySchemaByProcessCodeHeaders.xAcsDingtalkAccessToken = token;
//        com.aliyun.dingtalkworkflow_1_0.models.QuerySchemaByProcessCodeRequest querySchemaByProcessCodeRequest = new com.aliyun.dingtalkworkflow_1_0.models.QuerySchemaByProcessCodeRequest()
//                .setProcessCode("PROC-EF6Y0XWVO2-B1GBT8NCTNSTKIEC6CJT1-7NE17KPI-SK");
//        try {
//            QuerySchemaByProcessCodeResponse s = client.querySchemaByProcessCodeWithOptions(querySchemaByProcessCodeRequest, querySchemaByProcessCodeHeaders, new RuntimeOptions());
//            String ss = JSONObject.toJSONString(s.getBody().getResult());
//            System.out.print(ss);
//        } catch (TeaException err) {
//            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
//                // err 中含有 code 和 message 属性，可帮助开发定位问题
//            }
//
//        } catch (Exception _err) {
//            TeaException err = new TeaException(_err.getMessage(), _err);
//            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
//                // err 中含有 code 和 message 属性，可帮助开发定位问题
//            }
//
//        }

        //查询审批单内容
//        com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceHeaders getProcessInstanceHeaders = new com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceHeaders();
//        getProcessInstanceHeaders.xAcsDingtalkAccessToken = token;
//        com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceRequest getProcessInstanceRequest = new com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceRequest()
//                .setProcessInstanceId("qDEY0XHOQR61vHG5sSn54A04641734424487");
//        try {
//            GetProcessInstanceResponse s = client.getProcessInstanceWithOptions(getProcessInstanceRequest, getProcessInstanceHeaders, new RuntimeOptions());
//            String ss = JSONObject.toJSONString(s.getBody().getResult());
//            System.out.print("****************");
//            System.out.print(ss);
//        } catch (TeaException err) {
//            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
//                // err 中含有 code 和 message 属性，可帮助开发定位问题
//            }
//
//        } catch (Exception _err) {
//            TeaException err = new TeaException(_err.getMessage(), _err);
//            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
//                // err 中含有 code 和 message 属性，可帮助开发定位问题
//            }
//
//        }
//查询模板
//        com.aliyun.dingtalkworkflow_1_0.models.ListUserVisibleBpmsProcessesHeaders listUserVisibleBpmsProcessesHeaders = new com.aliyun.dingtalkworkflow_1_0.models.ListUserVisibleBpmsProcessesHeaders();
//        listUserVisibleBpmsProcessesHeaders.xAcsDingtalkAccessToken = token;
//        com.aliyun.dingtalkworkflow_1_0.models.ListUserVisibleBpmsProcessesRequest listUserVisibleBpmsProcessesRequest = new com.aliyun.dingtalkworkflow_1_0.models.ListUserVisibleBpmsProcessesRequest()
//                .setUserId("021525061836643928")
//                .setMaxResults(100L)
//                .setNextToken(0L);
//        try {
//            ListUserVisibleBpmsProcessesResponse s = client.listUserVisibleBpmsProcessesWithOptions(listUserVisibleBpmsProcessesRequest, listUserVisibleBpmsProcessesHeaders, new RuntimeOptions());
//            String ss = JSONObject.toJSONString(s.getBody().getResult());
//            System.out.print("****************");
//            System.out.print(ss);
//        } catch (TeaException err) {
//            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
//                // err 中含有 code 和 message 属性，可帮助开发定位问题
//            }
//
//        } catch (Exception _err) {
//            TeaException err = new TeaException(_err.getMessage(), _err);
//            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
//                // err 中含有 code 和 message 属性，可帮助开发定位问题
//            }
//
//        }

        //查询审批单id
//        com.aliyun.dingtalkworkflow_1_0.models.ListProcessInstanceIdsHeaders listProcessInstanceIdsHeaders = new com.aliyun.dingtalkworkflow_1_0.models.ListProcessInstanceIdsHeaders();
//        listProcessInstanceIdsHeaders.xAcsDingtalkAccessToken = token;
//        com.aliyun.dingtalkworkflow_1_0.models.ListProcessInstanceIdsRequest listProcessInstanceIdsRequest = new com.aliyun.dingtalkworkflow_1_0.models.ListProcessInstanceIdsRequest()
//                .setProcessCode("PROC-67C7D5D4-AB6E-4FE0-AF3B-16234A5764F3")
//                .setStartTime(1733893226000L)
//                .setEndTime(1733919146000L)
//                .setNextToken(0L)
//                .setMaxResults(10L)
//                .setUserIds(java.util.Arrays.asList(
//                        "100854601338211208"
//                ));
//        try {
//            String ss = JSONObject.toJSONString(client.listProcessInstanceIdsWithOptions(listProcessInstanceIdsRequest, listProcessInstanceIdsHeaders, new com.aliyun.teautil.models.RuntimeOptions()).getBody().getResult());
//            System.out.print("****************");
//            System.out.print(ss);
//                    } catch (TeaException err) {
//            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
//                // err 中含有 code 和 message 属性，可帮助开发定位问题
//            }
//
//        } catch (Exception _err) {
//            TeaException err = new TeaException(_err.getMessage(), _err);
//            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
//                // err 中含有 code 和 message 属性，可帮助开发定位问题
//            }
//
//        }
//
//        //下载附件
//        com.aliyun.dingtalkworkflow_1_0.models.GrantProcessInstanceForDownloadFileHeaders grantProcessInstanceForDownloadFileHeaders = new com.aliyun.dingtalkworkflow_1_0.models.GrantProcessInstanceForDownloadFileHeaders();
//        grantProcessInstanceForDownloadFileHeaders.xAcsDingtalkAccessToken = token;
//        com.aliyun.dingtalkworkflow_1_0.models.GrantProcessInstanceForDownloadFileRequest grantProcessInstanceForDownloadFileRequest = new com.aliyun.dingtalkworkflow_1_0.models.GrantProcessInstanceForDownloadFileRequest()
//                .setProcessInstanceId("Gn8dVdviR7uDZ_Dx2gSLCw04641733894276")
//                .setFileId("163844741648").setWithCommentAttatchment(true);
//
//        try {
//            String ss = JSONObject.toJSONString(client.grantProcessInstanceForDownloadFileWithOptions(grantProcessInstanceForDownloadFileRequest, grantProcessInstanceForDownloadFileHeaders, new com.aliyun.teautil.models.RuntimeOptions()).getBody().getResult());
//            System.out.print("****************");
//            System.out.print(ss);
//        } catch (TeaException err) {
//            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
//                // err 中含有 code 和 message 属性，可帮助开发定位问题
//            }
//
//        } catch (Exception _err) {
//            TeaException err = new TeaException(_err.getMessage(), _err);
//            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
//                // err 中含有 code 和 message 属性，可帮助开发定位问题
//            }
//
//        }
//        try {
//            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/get");
//            OapiV2UserGetRequest req = new OapiV2UserGetRequest();
//            req.setUserid("021525061836643928");
//            req.setLanguage("zh_CN");
//            OapiV2UserGetResponse rsp = client.execute(req, token);
//            Map<String, Object> map = JSON.parseObject(rsp.getBody(), new TypeReference<Map<String, Object>>(){});
//            String s  = JSONObject.toJSONString(map.get("result"));
//            Map<String, Object> map2 = JSON.parseObject(s, new TypeReference<Map<String, Object>>(){});
//            System.out.println(map2.get("unionid").toString());
//        } catch (ApiException e) {
//            e.printStackTrace();
//        }
        java.util.List<String> args = java.util.Arrays.asList(args_);
        com.aliyun.dingtalktodo_1_0.Client client = Sample.createClient3();
        CreateTodoTaskHeaders createTodoTaskHeaders = new CreateTodoTaskHeaders();
        createTodoTaskHeaders.xAcsDingtalkAccessToken = token;

        CreateTodoTaskRequest.CreateTodoTaskRequestNotifyConfigs notifyConfigs = new CreateTodoTaskRequest.CreateTodoTaskRequestNotifyConfigs()
                .setDingNotify("1");
        CreateTodoTaskRequest.CreateTodoTaskRequestContentFieldList contentFieldList0 = new CreateTodoTaskRequest.CreateTodoTaskRequestContentFieldList();

        CreateTodoTaskRequest.CreateTodoTaskRequestDetailUrl detailUrl = new CreateTodoTaskRequest.CreateTodoTaskRequestDetailUrl();
        CreateTodoTaskRequest createTodoTaskRequest = new CreateTodoTaskRequest()
                .setSubject("您有请购单已超时，请尽快处理！")
                .setDescription("B-2025-0207-01，R1-2025-0207-01，P-2025-0207-02")
                .setExecutorIds(java.util.Arrays.asList(
                        "9t3PRciSjIUOEWe7eXlK3wwiEiE"
                ))
                .setParticipantIds(java.util.Arrays.asList(
                        "9t3PRciSjIUOEWe7eXlK3wwiEiE"
                ))
                .setDetailUrl(detailUrl)
                .setContentFieldList(java.util.Arrays.asList(
                        contentFieldList0
                ))
                .setIsOnlyShowExecutor(true)
                .setPriority(20)
                .setNotifyConfigs(notifyConfigs);

        try {
            client.createTodoTaskWithOptions("9t3PRciSjIUOEWe7eXlK3wwiEiE", createTodoTaskRequest, createTodoTaskHeaders, new RuntimeOptions());
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