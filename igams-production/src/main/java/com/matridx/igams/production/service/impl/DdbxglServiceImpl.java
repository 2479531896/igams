package com.matridx.igams.production.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiProcessinstanceGetRequest;
import com.dingtalk.api.request.OapiProcessinstanceListidsRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiProcessinstanceGetResponse;
import com.dingtalk.api.response.OapiProcessinstanceListidsResponse;
import com.matridx.igams.common.dao.entities.*;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IJgxxService;
import com.matridx.igams.production.dao.entities.DdbxglDto;
import com.matridx.igams.production.dao.entities.DdbxglModel;
import com.matridx.igams.production.dao.entities.DdbxmxDto;
import com.matridx.igams.production.dao.post.IDdbxglDao;
import com.matridx.igams.production.service.svcinterface.IDdbxglService;
import com.matridx.igams.production.service.svcinterface.IDdbxmxService;
import com.matridx.igams.production.service.svcinterface.IRdRecordService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.taobao.api.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author:JYK
 */
@Service
public class DdbxglServiceImpl extends BaseBasicServiceImpl<DdbxglDto, DdbxglModel, IDdbxglDao> implements IDdbxglService {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    ICommonService commonService;
    @Autowired
    IDdbxmxService ddbxmxService;
    @Autowired
    IRdRecordService rdRecordService;
    @Autowired
    IJgxxService jgxxService;
    @Autowired
    DingTalkUtil talkUtil;
    @Value("${matridx.dingtalk.appsecret:}")
    private String appSecret;
    @Value("${matridx.dingtalk.appkey:}")
    private String appKey;

    private final Logger log = LoggerFactory.getLogger(DdbxglServiceImpl.class);


    /**
     * 执行钉钉审批回调
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean reimburseCallback(String data,String processInstanceId,String processCode){
        log.error("钉钉报销processInstanceId: " + processInstanceId);
        log.error("钉钉报销processCode: " + processCode);
        log.error("钉钉报销Data: " + data);
        List<JcsjDto> bxlxs = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.EXPENSE_TYPE.getCode());
        DdbxglDto ddbxglDto=new DdbxglDto();
        List<DdbxmxDto> list=new ArrayList<>();
        ddbxglDto.setSpslid(processInstanceId);
        JSONObject jsonObject = JSONObject.parseObject(data);
        JSONObject process_instance = jsonObject.getJSONObject("process_instance");
        JSONArray form_component_values = process_instance.getJSONArray("form_component_values");
        JSONObject fygs =new JSONObject();
        if("PROC-DFCCB981-37A5-4C25-80A6-B78B7F495930".equals(processCode)){
            fygs=form_component_values.getJSONObject(0);
        }else if("PROC-F933F40F-0524-4767-9349-3DE4F1C5E01E".equals(processCode)){
            fygs=form_component_values.getJSONObject(1);
            ddbxglDto.setHylx(form_component_values.getJSONObject(0).getString("value"));
            ddbxglDto.setSsdq(form_component_values.getJSONObject(2).getString("value"));
        }
        ddbxglDto.setSsgs(fygs.getString("value"));
        if(!CollectionUtils.isEmpty(bxlxs)){
            for(JcsjDto jcsjDto:bxlxs){
                if(processCode.equals(jcsjDto.getCskz3())){
                    ddbxglDto.setBxlx(jcsjDto.getCsid());
                    ddbxglDto.setBxlxdm(jcsjDto.getCsdm());
                    ddbxglDto.setBxlxcskz1(jcsjDto.getCskz1());
                    ddbxglDto.setBxlxcskz2(jcsjDto.getCskz2());
                    ddbxglDto.setBxlxcskz2(jcsjDto.getCskz2());
                    ddbxglDto.setBxlxcskz3(jcsjDto.getCskz3());
                }
            }
        }
        JSONObject zje =new JSONObject();
        if("PROC-DFCCB981-37A5-4C25-80A6-B78B7F495930".equals(processCode)){
            zje = form_component_values.getJSONObject(3);
        }else if("PROC-F933F40F-0524-4767-9349-3DE4F1C5E01E".equals(processCode)){
            zje = form_component_values.getJSONObject(9);
        }
        ddbxglDto.setZje(zje.getString("value"));
        String originator_dept_id = process_instance.getString("originator_dept_id");
        ddbxglDto.setBm(originator_dept_id);
        JgxxDto jgxxDto=new JgxxDto();
        jgxxDto.setJgid(originator_dept_id);
        JgxxDto jgxx=jgxxService.selectJgxxByJgid(jgxxDto);
        if(jgxx!=null){
            ddbxglDto.setBmkzcs1(jgxx.getKzcs1());
        }
        User user = new User();
        user.setDdid( process_instance.getString("originator_userid"));
        List<User> userDto = commonService.getUserByDdid(user);
        if(!CollectionUtils.isEmpty(userDto)) {
            ddbxglDto.setSqr(userDto.get(0).getYhid());
            ddbxglDto.setSqrmc(userDto.get(0).getZsxm());
            ddbxglDto.setU8yhid(userDto.get(0).getGrouping());
        }
        //同步部门获取部门代码
        DepartmentDto jgxx_t = commonService.getJgxxInfo(ddbxglDto.getBm());
        ddbxglDto.setJgdm(jgxx_t.getJgdm());
        JSONArray operation_records = process_instance.getJSONArray("operation_records");
        if(!CollectionUtils.isEmpty(operation_records)) {
            for(int i=operation_records.size()-1;i>=0;i--){
                JSONObject jsonObject_t = operation_records.getJSONObject(i);
                if("EXECUTE_TASK_NORMAL".equals(jsonObject_t.getString("operation_type"))){
                    user.setDdid( jsonObject_t.getString("userid"));
                    userDto = commonService.getUserByDdid(user);
                    if(userDto!=null){
                        ddbxglDto.setSpr(userDto.get(0).getYhid());
                    }
                    break;
                }
            }
        }
        ddbxglDto.setDdbxid(StringUtil.generateUUID());
        int insert = dao.insert(ddbxglDto);
        if(insert==0){
            return false;
        }
        if("PROC-DFCCB981-37A5-4C25-80A6-B78B7F495930".equals(processCode)){
            DdbxmxDto ddbxmxDto=new DdbxmxDto();
            ddbxmxDto.setBxmxid(StringUtil.generateUUID());
            ddbxmxDto.setDdbxid(ddbxglDto.getDdbxid());
            ddbxmxDto.setJe(ddbxglDto.getZje());
            list.add(ddbxmxDto);

        }else if("PROC-F933F40F-0524-4767-9349-3DE4F1C5E01E".equals(processCode)){
            String detail = form_component_values.getJSONObject(8).getString("value");
            JSONArray jsonArray = JSONArray.parseArray(detail);
            for (Object o : jsonArray) {
                JSONObject jsonObject_t = JSONObject.parseObject(JSON.toJSONString(o));
                JSONArray rowValue = JSONArray.parseArray(jsonObject_t.getString("rowValue"));
                DdbxmxDto ddbxmxDto = new DdbxmxDto();
                ddbxmxDto.setBxmxid(StringUtil.generateUUID());
                ddbxmxDto.setDdbxid(ddbxglDto.getDdbxid());
                ddbxmxDto.setJksy(form_component_values.getJSONObject(3).getString("value"));
                ddbxmxDto.setJe(rowValue.getJSONObject(0).getString("value"));
                ddbxmxDto.setSkfmc(rowValue.getJSONObject(1).getString("value"));
                ddbxmxDto.setSkfkhh(rowValue.getJSONObject(2).getString("value"));
                ddbxmxDto.setSkfyhzh(rowValue.getJSONObject(3).getString("value"));
                ddbxmxDto.setZwzfrq(rowValue.getJSONObject(4).getString("value"));
                list.add(ddbxmxDto);
            }
        }
        int insertMx = ddbxmxService.insertList(list);
        if(insertMx==0){
            return false;
        }
        Map<String, Object> map = rdRecordService.addU8Reimburse(ddbxglDto, list);
        DdbxglDto ddbxglDto_t = (DdbxglDto)map.get("ddbxglDto");
        @SuppressWarnings("unchecked")
        List<DdbxmxDto> ddbxmxDtos = (List<DdbxmxDto>) map.get("ddbxmxDtos");
        int update = dao.update(ddbxglDto_t);
        if(update==0){
            return false;
        }
        update=ddbxmxService.updateList(ddbxmxDtos);
        return update != 0;
    }


    /**
     * 定时任务同步
     
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public void timingSynchronization(){
        //会议费同步
        String token = talkUtil.getToken(null);
        String processCode="PROC-F933F40F-0524-4767-9349-3DE4F1C5E01E";
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        long startsj = calendar.getTime().getTime();
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),23,59,59);
        long endsj = calendar.getTime().getTime();
        String approvalInstance = getApprovalInstance(processCode, startsj, endsj, token);
        JSONObject jsonObject= JSONObject.parseObject(approvalInstance);
        JSONArray list = jsonObject.getJSONObject("result").getJSONArray("list");
        List<String> spslids=new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        for (Object o : list) {
            String leaveMessage = "";
            try {
                leaveMessage = getLeaveMessage(o.toString());
            } catch (ApiException e) {
                log.error(e.getMessage());
            }
            JSONObject message = JSONObject.parseObject(leaveMessage);
            JSONObject process_instance = message.getJSONObject("process_instance");
            String result = process_instance.getString("result");
            System.out.println(result);
            String status = process_instance.getString("status");
            System.out.println(status);
            if ("COMPLETED".equals(status) && "agree".equals(result)) {
                spslids.add(o.toString());
                map.put(o.toString(), leaveMessage);
            }
        }
        if(!CollectionUtils.isEmpty(spslids)){
            DdbxglDto ddbxglDto=new DdbxglDto();
            ddbxglDto.setSpslids(spslids);
            List<DdbxglDto> dtoList = dao.getDtoList(ddbxglDto);
            if(!CollectionUtils.isEmpty(dtoList)){
                for(String s:spslids){
                    boolean flag=false;
                    for(DdbxglDto dto:dtoList){
                        if(s.equals(dto.getSpslid())){
                            flag=true;
                            break;
                        }
                    }
                    if(!flag){
                        String data = map.get(s);
                        reimburseCallback(data,s,processCode);
                    }
                }
            }else{
                for(String s:spslids){
                    String data = map.get(s);
                    reimburseCallback(data,s,processCode);
                }
            }
        }
    }

    /**
     * 获取审批实例ID列表
     */
    public String getApprovalInstance(String processCode, Long startsj, Long endsj, String token){
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/processinstance/listids");
        OapiProcessinstanceListidsRequest req = new OapiProcessinstanceListidsRequest();
        req.setProcessCode(processCode);
        req.setStartTime(startsj);
        req.setEndTime(endsj);
        OapiProcessinstanceListidsResponse rsp;
        try {
            rsp = client.execute(req, token);
            return rsp.getBody();
        } catch (ApiException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 获取审批实例详情信息
     */
    public String getLeaveMessage(String processInstanceId) throws ApiException {
        OapiGettokenResponse response = getToken();
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/processinstance/get");
        OapiProcessinstanceGetRequest req = new OapiProcessinstanceGetRequest();
        req.setProcessInstanceId(processInstanceId);
        OapiProcessinstanceGetResponse rsp = client.execute(req, response.getAccessToken());
        return rsp.getBody();
    }

    /**
     * 获取Token
     */
    public OapiGettokenResponse getToken() throws ApiException {
        //获取token
    	DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
        OapiGettokenRequest request = new OapiGettokenRequest();
        DBEncrypt p = new DBEncrypt();
        String dappSecret = p.dCode(appSecret);
        String dappKey = p.dCode(appKey);
        request.setAppkey(dappKey);
        request.setAppsecret(dappSecret);
        request.setHttpMethod("GET");
        return client.execute(request);
    }
}
