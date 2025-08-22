package com.matridx.igams.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiProcessinstanceGetRequest;
import com.dingtalk.api.request.OapiProcessinstanceListidsRequest;
import com.dingtalk.api.response.OapiProcessinstanceGetResponse;
import com.dingtalk.api.response.OapiProcessinstanceListidsResponse;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.JgxxDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IJgxxService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.web.dao.entities.RslyDto;
import com.matridx.igams.web.dao.entities.RslyModel;
import com.matridx.igams.web.dao.entities.RszpDto;
import com.matridx.igams.web.dao.post.IRslyDao;
import com.matridx.igams.web.service.svcinterface.IRslyService;
import com.matridx.igams.web.service.svcinterface.IRszpService;
import com.matridx.springboot.util.base.StringUtil;
import com.taobao.api.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Service
public class RslyServiceImpl extends BaseBasicServiceImpl<RslyDto, RslyModel, IRslyDao> implements IRslyService {
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    IJgxxService jgxxService;
    @Autowired
    IJcsjService jcsjService;
    @Autowired
    IRszpService rszpService;
    private final Logger log = LoggerFactory.getLogger(RslyServiceImpl.class);

    /**
     * 获取审批id
     * @param rslyDto
     * @return
     */
    public RslyDto getSpid(RslyDto rslyDto){
        return dao.getSpid(rslyDto);
    }

    /**
     * 点击已录用人数展示页面
     * @param rslyDto
     * @return
     */
    public List<RslyDto> viewEmployDetails(RslyDto rslyDto){return dao.viewEmployDetails(rslyDto);}
    /**
     * 定时更新
     */
    public void updateEmployments(Map<String,String> map) {
        //flg为0，更新四天前的招聘信息
        //flg为1，更新上个月的招聘信息
        //flg为2，更新本月的招聘信息
        String flg = map.get("flg");
        getEmployments(flg);
    }
    /**
     * 根据不同参数运行不同方法
     */
    public void getEmployments(String flg){
        switch (flg) {
            case "1": {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MONTH, -1);
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
                long startsj = calendar.getTime().getTime();
                calendar.add(Calendar.DATE, +30);
                long endsj = calendar.getTime().getTime();
                saveEmployments(startsj, endsj);
                break;
            }
            case "2": {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
                long startsj = calendar.getTime().getTime();
                calendar.add(Calendar.DATE, +30);
                long endsj = calendar.getTime().getTime();
                saveEmployments(startsj, endsj);
                break;
            }
            case "0": {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, -3);
                long startsj = calendar.getTime().getTime();
                calendar.add(Calendar.DATE, +1);
                long endsj = calendar.getTime().getTime();
                saveEmployments(startsj, endsj);
                break;
            }
        }
    }



    /**
     * 获取录用信息以及保存
     */
    public void saveEmployments(Long startsj,Long endsj){
        String token = talkUtil.getToken(null);
        String processCode;
        Long nextCursor=0L;
        processCode = "PROC-A5F6E42B-81CB-45FE-9F5E-9EFBD8915E9F";
        while (nextCursor!=null){
            OapiProcessinstanceListidsResponse rsp_lb = getApprovalInstance(processCode, startsj, endsj, token, nextCursor);
            List<String> list= rsp_lb.getResult().getList();
            nextCursor=rsp_lb.getResult().getNextCursor();
            if(!CollectionUtils.isEmpty(list)){
                for(String s:list){
                    DingTalkClient client_t = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/processinstance/get");
                    OapiProcessinstanceGetRequest req_t = new OapiProcessinstanceGetRequest();
                    req_t.setProcessInstanceId(s);
                    OapiProcessinstanceGetResponse rsp_t = null;
                    try {
                        rsp_t = client_t.execute(req_t, token);
                    } catch (ApiException e) {
                        log.error(e.toString());
                    }
                    String body = rsp_t != null ? rsp_t.getBody() : null;
                    Object jsonObject = JSON.parseObject(body);
                    Object obj = ((JSONObject) jsonObject).getJSONObject("process_instance");
                    RslyDto rslyDto=new RslyDto();
                    rslyDto.setSpid(s);
                    JSONArray jsonArray = ((JSONObject) obj).getJSONArray("form_component_values");
                    String value = jsonArray.getJSONObject(0).getString("value");
                    String spxx=jsonArray.getJSONObject(3).getString("ext_value");
                    if(spxx!=null){
                        JSONObject jsonObject_t = JSON.parseObject(spxx);
                        String spid = jsonObject_t.getJSONArray("list").getJSONObject(0).getString("procInstId");
                        RszpDto rszpDto=new RszpDto();
                        rszpDto.setSpid(spid);
                        RszpDto rszpdto = rszpService.getSpid(rszpDto);
                        if(rszpdto!=null){
                            rslyDto.setZpid(rszpdto.getZpid());
                        }
                    }
                    rslyDto.setBz(jsonArray.getJSONObject(1).getString("value"));
                    JSONArray jsonArray_t = JSON.parseArray(value);
                    JgxxDto jgxxDto=new JgxxDto();
                    String bmmc = jsonArray_t.getJSONObject(1).getString("value");
                    String[] split = bmmc.split("-");
                    jgxxDto.setJgmc(split[split.length-1]);
                    JgxxDto jgxx = jgxxService.getJgxxByJgmc(jgxxDto);
                    if(jgxx!=null){
                        rslyDto.setYrbm(jgxx.getJgid());
                    }
                    rslyDto.setRzygxm(jsonArray_t.getJSONObject(0).getString("value"));
                    rslyDto.setZw(jsonArray_t.getJSONObject(2).getString("value"));
                    rslyDto.setSj(jsonArray_t.getJSONObject(3).getString("value"));
                    String lx = jsonArray_t.getJSONObject(4).getString("value");
                    JcsjDto jcsjDto=new JcsjDto();
                    jcsjDto.setCsmc(lx);
                    jcsjDto.setJclb(BasicDataTypeEnum.STAFF_TYPE.getCode());
                    JcsjDto sjdto=jcsjService.getDto(jcsjDto);
                    if(sjdto!=null){
                        rslyDto.setYglx(sjdto.getCsid());
                    }
                    rslyDto.setRzrq(jsonArray_t.getJSONObject(5).getString("value"));
                    RslyDto rslyDto_t = getSpid(rslyDto);
                    if(rslyDto_t!=null){
                        rslyDto.setLyid(rslyDto_t.getLyid());
                        update(rslyDto);
                    }else{
                        rslyDto.setLyid(StringUtil.generateUUID());
                        insert(rslyDto);
                    }
                }
            }
        }
    }
    /**
     * 获取审批实例ID列表
     */
    public  OapiProcessinstanceListidsResponse getApprovalInstance(String processCode,Long startsj,Long endsj,String token,Long nextCursor){
        DingTalkClient client_lb = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/processinstance/listids");
        OapiProcessinstanceListidsRequest req_lb = new OapiProcessinstanceListidsRequest();
        req_lb.setProcessCode(processCode);
        req_lb.setStartTime(startsj);
        req_lb.setEndTime(endsj);
        req_lb.setSize(20L);
        req_lb.setCursor(nextCursor);
        OapiProcessinstanceListidsResponse rsp_lb =null;
        try {
            rsp_lb = client_lb.execute(req_lb, token);
        } catch (ApiException e) {
            log.error(e.toString());
        }
        return rsp_lb;
    }
}
