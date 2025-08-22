package com.matridx.springboot.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenResponse;
import com.aliyun.dingtalkworkflow_1_0.models.AddApproveDentryAuthResponse;
import com.aliyun.dingtalkworkflow_1_0.models.GetAttachmentSpaceResponse;
import com.aliyun.dingtalkworkflow_1_0.models.GetManageProcessByStaffIdResponse;
import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponse;
import com.aliyun.dingtalkworkflow_1_0.models.GrantProcessInstanceForDownloadFileResponse;
import com.aliyun.tea.TeaException;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author : 郭祥杰
 * @date :
 */
public class Atest {
    public static void main(String[] args_) throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
        config.protocol = "https";
        config.regionId = "central";
        com.aliyun.dingtalkoauth2_1_0.Client client = new com.aliyun.dingtalkoauth2_1_0.Client(config);
        com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest getAccessTokenRequest = new com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest()
                .setAppKey("ding67bwbaypvlgmdjyj")
                .setAppSecret("d6C0UXiJBnN3boPo20aTbsbXALgbjqHx3fBf_iGBnFvh3qvo1glEPCFi8aVmnPqa");
        String accessToken = "";
        try {
            GetAccessTokenResponse res = client.getAccessToken(getAccessTokenRequest);
            accessToken = res.getBody().getAccessToken();
            System.out.println(res.getBody().getAccessToken());
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


        com.aliyun.teaopenapi.models.Config config2 = new com.aliyun.teaopenapi.models.Config();
        config.protocol = "https";
        config.regionId = "central";
        com.aliyun.dingtalkworkflow_1_0.Client client2 = new com.aliyun.dingtalkworkflow_1_0.Client(config2);
        com.aliyun.dingtalkworkflow_1_0.models.GetManageProcessByStaffIdHeaders getManageProcessByStaffIdHeaders = new com.aliyun.dingtalkworkflow_1_0.models.GetManageProcessByStaffIdHeaders();
        getManageProcessByStaffIdHeaders.xAcsDingtalkAccessToken = accessToken;
        com.aliyun.dingtalkworkflow_1_0.models.GetManageProcessByStaffIdRequest getManageProcessByStaffIdRequest = new com.aliyun.dingtalkworkflow_1_0.models.GetManageProcessByStaffIdRequest()
                .setUserId("040604610937345850");
        try {
            GetManageProcessByStaffIdResponse response = client2.getManageProcessByStaffIdWithOptions(getManageProcessByStaffIdRequest, getManageProcessByStaffIdHeaders, new com.aliyun.teautil.models.RuntimeOptions());
//            response.getBody().getResult().forEach(
//                    item->{
//                        System.out.println(item.getFlowTitle());
//                    }
//            );
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


        com.aliyun.teaopenapi.models.Config config3 = new com.aliyun.teaopenapi.models.Config();
        config.protocol = "https";
        config.regionId = "central";
        com.aliyun.dingtalkworkflow_1_0.Client client3 = new com.aliyun.dingtalkworkflow_1_0.Client(config3);
        com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceHeaders getProcessInstanceHeaders = new com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceHeaders();
        getProcessInstanceHeaders.xAcsDingtalkAccessToken = accessToken;
        com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceRequest getProcessInstanceRequest = new com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceRequest()
                .setProcessInstanceId("_t20Wo_iReW3eDzLAY6pAA04641749000590");
        try {
            GetProcessInstanceResponse response3 = client3.getProcessInstanceWithOptions(getProcessInstanceRequest, getProcessInstanceHeaders, new com.aliyun.teautil.models.RuntimeOptions());
            Object object = response3.getBody().getResult();
            String s = JSON.toJSONString(object);
            Map<String, Object> map = JSON.parseObject(s, new TypeReference<Map<String, Object>>(){});
            List<Map<String, Object>> mapList = JSON.parseObject(map.get("operationRecords").toString(), new TypeReference<List<Map<String, Object>>>(){});
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

        Long dpid= 0L;
        com.aliyun.teaopenapi.models.Config config5 = new com.aliyun.teaopenapi.models.Config();
        config5.protocol = "https";
        config5.regionId = "central";
        com.aliyun.dingtalkworkflow_1_0.Client client5 =  new com.aliyun.dingtalkworkflow_1_0.Client(config5);
        com.aliyun.dingtalkworkflow_1_0.models.GetAttachmentSpaceHeaders getAttachmentSpaceHeaders = new com.aliyun.dingtalkworkflow_1_0.models.GetAttachmentSpaceHeaders();
        getAttachmentSpaceHeaders.xAcsDingtalkAccessToken = accessToken;
        com.aliyun.dingtalkworkflow_1_0.models.GetAttachmentSpaceRequest getAttachmentSpaceRequest = new com.aliyun.dingtalkworkflow_1_0.models.GetAttachmentSpaceRequest()
                .setUserId("040604610937345850");
        try {
            GetAttachmentSpaceResponse response = client5.getAttachmentSpaceWithOptions(getAttachmentSpaceRequest, getAttachmentSpaceHeaders, new com.aliyun.teautil.models.RuntimeOptions());
            System.out.println(response.getBody().getResult().getSpaceId());
            dpid = response.getBody().getResult().getSpaceId();
            System.out.println("盯盘id:"+dpid);
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





        com.aliyun.teaopenapi.models.Config configb = new com.aliyun.teaopenapi.models.Config();
        configb.protocol = "https";
        configb.regionId = "central";
        com.aliyun.dingtalkworkflow_1_0.Client clientb = new com.aliyun.dingtalkworkflow_1_0.Client(configb);
        com.aliyun.dingtalkworkflow_1_0.models.AddApproveDentryAuthHeaders addApproveDentryAuthHeaders = new com.aliyun.dingtalkworkflow_1_0.models.AddApproveDentryAuthHeaders();
        addApproveDentryAuthHeaders.xAcsDingtalkAccessToken = accessToken;
        com.aliyun.dingtalkworkflow_1_0.models.AddApproveDentryAuthRequest.AddApproveDentryAuthRequestFileInfos fileInfos0 = new com.aliyun.dingtalkworkflow_1_0.models.AddApproveDentryAuthRequest.AddApproveDentryAuthRequestFileInfos()
                .setFileId("181895095862")
                .setSpaceId(dpid);
        List<com.aliyun.dingtalkworkflow_1_0.models.AddApproveDentryAuthRequest.AddApproveDentryAuthRequestFileInfos> list = new ArrayList<>();
        list.add(fileInfos0);
        com.aliyun.dingtalkworkflow_1_0.models.AddApproveDentryAuthRequest addApproveDentryAuthRequest = new com.aliyun.dingtalkworkflow_1_0.models.AddApproveDentryAuthRequest()
                .setUserId("021525061836643928")
                .setFileInfos(list);
        try {
            AddApproveDentryAuthResponse a = clientb.addApproveDentryAuthWithOptions(addApproveDentryAuthRequest, addApproveDentryAuthHeaders, new com.aliyun.teautil.models.RuntimeOptions());
            System.out.println("授权结果："+JSON.toJSONString(a.getBody().getResult()));
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

        com.aliyun.teaopenapi.models.Config configx = new com.aliyun.teaopenapi.models.Config();
        configx.protocol = "https";
        configx.regionId = "central";

        com.aliyun.dingtalkworkflow_1_0.Client clientx =  new com.aliyun.dingtalkworkflow_1_0.Client(configx);
        com.aliyun.dingtalkworkflow_1_0.models.GrantProcessInstanceForDownloadFileHeaders grantProcessInstanceForDownloadFileHeaders = new com.aliyun.dingtalkworkflow_1_0.models.GrantProcessInstanceForDownloadFileHeaders();
        grantProcessInstanceForDownloadFileHeaders.xAcsDingtalkAccessToken = accessToken;
        com.aliyun.dingtalkworkflow_1_0.models.GrantProcessInstanceForDownloadFileRequest grantProcessInstanceForDownloadFileRequest = new com.aliyun.dingtalkworkflow_1_0.models.GrantProcessInstanceForDownloadFileRequest()
                .setProcessInstanceId("N3evb0VwQDyZ7BKn0eeCfw04641749019850")
                //.setWithCommentAttatchment(true)
                .setFileId("181915252073");
        try {
            GrantProcessInstanceForDownloadFileResponse ss= clientx.grantProcessInstanceForDownloadFileWithOptions(grantProcessInstanceForDownloadFileRequest, grantProcessInstanceForDownloadFileHeaders, new com.aliyun.teautil.models.RuntimeOptions());
            String downloadUrl = ss.getBody().getResult().getDownloadUri();
            System.out.println(JSON.toJSONString(ss.getBody().getResult()));


            Files.createDirectories(Paths.get("/matridx/files"));

            // 拼接完整保存路径
            String filePath = Paths.get("/matridx/files", "lQDPM4nLZIjzonPNBQDNCOSwtZShtzAtLx8IICKL7-IhAA_2276_1280.jpg").toString();

            // 使用try-with-resources确保自动关闭资源
            try (InputStream in = new URL("https://static.dingtalk.com/media/lQDPM4nLZIjzonPNBQDNCOSwtZShtzAtLx8IICKL7-IhAA_2276_1280.jpg").openStream();
                 OutputStream out = new FileOutputStream(filePath)) {

                // 创建8KB缓冲区提高效率
                byte[] buffer = new byte[8192];
                int bytesRead;

                // 循环读写直到文件结束
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }





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

//        com.aliyun.teaopenapi.models.Config configw = new com.aliyun.teaopenapi.models.Config();
//        configw.protocol = "https";
//        configw.regionId = "central";
//
//        com.aliyun.dingtalkworkflow_1_0.Client clienw = new com.aliyun.dingtalkworkflow_1_0.Client(configw);
//        com.aliyun.dingtalkworkflow_1_0.models.ListProcessInstanceIdsHeaders listProcessInstanceIdsHeaders = new com.aliyun.dingtalkworkflow_1_0.models.ListProcessInstanceIdsHeaders();
//        listProcessInstanceIdsHeaders.xAcsDingtalkAccessToken = accessToken;
//        com.aliyun.dingtalkworkflow_1_0.models.ListProcessInstanceIdsRequest listProcessInstanceIdsRequest = new com.aliyun.dingtalkworkflow_1_0.models.ListProcessInstanceIdsRequest()
//                .setProcessCode("PROC-67C7D5D4-AB6E-4FE0-AF3B-16234A5764F3")
//                .setStartTime(1749002948000L)
//                .setEndTime(1749024548000L)
//                .setNextToken(0L)
//                .setMaxResults(10L)
//                .setUserIds(java.util.Arrays.asList(
//                        "021525061836643928"
//                ));
//        try {
//            System.out.println(JSON.toJSONString(clienw.listProcessInstanceIdsWithOptions(listProcessInstanceIdsRequest, listProcessInstanceIdsHeaders, new com.aliyun.teautil.models.RuntimeOptions()).getBody().getResult()));
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






        com.aliyun.teaopenapi.models.Config configaa = new com.aliyun.teaopenapi.models.Config();
        configaa.protocol = "https";
        configaa.regionId = "central";
        java.util.List<String> args = java.util.Arrays.asList(args_);
        com.aliyun.dingtalkworkflow_1_0.Client clientaa = new com.aliyun.dingtalkworkflow_1_0.Client(configaa);
        com.aliyun.dingtalkworkflow_1_0.models.GetManageProcessByStaffIdHeaders getManageProcessByStaffIdHeadersa= new com.aliyun.dingtalkworkflow_1_0.models.GetManageProcessByStaffIdHeaders();
        getManageProcessByStaffIdHeadersa.xAcsDingtalkAccessToken = accessToken;
        com.aliyun.dingtalkworkflow_1_0.models.GetManageProcessByStaffIdRequest getManageProcessByStaffIdRequesta = new com.aliyun.dingtalkworkflow_1_0.models.GetManageProcessByStaffIdRequest()
                .setUserId("040604610937345850");
        try {
            List<Map<String,String>> lists = JSON.parseObject(JSON.toJSONString(clientaa.getManageProcessByStaffIdWithOptions(getManageProcessByStaffIdRequesta, getManageProcessByStaffIdHeadersa, new com.aliyun.teautil.models.RuntimeOptions()).getBody().getResult()), new TypeReference<List<Map<String,String>>>(){});
            for (Map<String,String> map:lists) {
                System.out.println(map.get("flowTitle")+"|||"+map.get("processCode"));
            }
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
