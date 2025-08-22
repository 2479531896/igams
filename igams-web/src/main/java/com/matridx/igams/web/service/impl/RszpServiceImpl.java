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
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.JgxxDto;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IJgxxService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.web.dao.entities.RszpDto;
import com.matridx.igams.web.dao.entities.RszpModel;
import com.matridx.igams.web.dao.entities.XtyhDto;
import com.matridx.igams.web.dao.post.IRszpDao;
import com.matridx.igams.web.service.svcinterface.IRszpService;
import com.matridx.igams.web.service.svcinterface.IXtyhService;
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
public class RszpServiceImpl extends BaseBasicServiceImpl<RszpDto, RszpModel, IRszpDao> implements IRszpService {
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    IXtyhService xtyhService;
    @Autowired
    IJgxxService jgxxService;

    private final Logger log = LoggerFactory.getLogger(RszpServiceImpl.class);

    /**
     * 获取审批id
     * @param rszpDto
     * @return
     */
    public RszpDto getSpid(RszpDto rszpDto){
        return dao.getSpid(rszpDto);
    }

    /**
     * 获取单条记录
     * @param rszpDto
     * @return
     */
    public RszpDto getDtoByZpid(RszpDto rszpDto){
        return dao.getDtoByZpid(rszpDto);
    }

    /**
     * 删除按钮
     * @param rszpDto
     * @return
     */
    public boolean delRecruitment(RszpDto rszpDto){
        return dao.delRecruitment(rszpDto);
    }

    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String, Object> params, RszpDto rszpDto) {
        List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
        StringBuilder sqlParam = new StringBuilder();
        for (DcszDto dcszDto : choseList) {
            if (dcszDto == null || dcszDto.getDczd() == null)
                continue;

            sqlParam.append(",");
            if (StringUtil.isNotBlank(dcszDto.getSqlzd())) {
                sqlParam.append(dcszDto.getSqlzd());
            }
            sqlParam.append(" ");
            sqlParam.append(dcszDto.getDczd());
        }
        String sqlcs = sqlParam.toString();
        rszpDto.setSqlParam(sqlcs);
    }
    /**
     * 从数据库分页获取导出数据
     * @param params
     * @return
     */
    public List<RszpDto> getListForSelectExp(Map<String, Object> params){
        RszpDto rszpDto = (RszpDto) params.get("entryData");
        queryJoinFlagExport(params,rszpDto);
        return dao.getListForSelectExp(rszpDto);

    }
    /**
     * 根据搜索条件获取导出条数
     * @param params
     * @return
     */
    public int getCountForSearchExp(RszpDto rszpDto,Map<String, Object> params){
        return dao.getCountForSearchExp(rszpDto);

    }
    /**
     * 从数据库分页获取导出数据
     * @param params
     * @return
     */
    public List<RszpDto> getListForSearchExp(Map<String, Object> params){
        RszpDto rszpDto = (RszpDto) params.get("entryData");
        queryJoinFlagExport(params, rszpDto);
        return dao.getListForSearchExp(rszpDto);

    }


    /**
     * 定时更新请假信息
     */
    public void updateRecruitments(Map<String,String> map) {
        //flg为0，更新四天前的招聘信息
        //flg为1，更新上个月的招聘信息
        //flg为2，更新本月的招聘信息
        String flg = map.get("flg");
        getRecruitments(flg);
    }
    /**
     * 根据不同参数运行不同方法
     */
    public void getRecruitments(String flg){
        switch (flg) {
            case "1": {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MONTH, -1);
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
                long startsj = calendar.getTime().getTime();
                calendar.add(Calendar.DATE, +30);
                long endsj = calendar.getTime().getTime();
                saveRecruitments(startsj, endsj);
                break;
            }
            case "2": {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
                long startsj = calendar.getTime().getTime();
                calendar.add(Calendar.DATE, +30);
                long endsj = calendar.getTime().getTime();
                saveRecruitments(startsj, endsj);
                break;
            }
            case "0": {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, -3);
                long startsj = calendar.getTime().getTime();
                calendar.add(Calendar.DATE, +1);
                long endsj = calendar.getTime().getTime();
                saveRecruitments(startsj, endsj);
                break;
            }
        }
    }


    /**
     * 获取招聘信息以及保存
     */
    public void saveRecruitments(Long startsj,Long endsj){
        String token = talkUtil.getToken(null);
        String processCode;
        Long nextCursor=0L;
        List<String> list;
        processCode = "PROC-EF6YLU2SO2-CE0CSA7CUYR0I3K7YQXU3-APARE9QI-57";
        while (nextCursor!=null){
            OapiProcessinstanceListidsResponse rsp_lb = getApprovalInstance(processCode, startsj, endsj, token, nextCursor);
            list= rsp_lb.getResult().getList();
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
                    RszpDto rszpDto=new RszpDto();
                    rszpDto.setSpid(s);
                    XtyhDto xtyhDto=new XtyhDto();
                    xtyhDto.setDdid(((JSONObject) obj).getString("originator_userid"));
                    XtyhDto xtyhDto1 = xtyhService.getYhid(xtyhDto);
                    rszpDto.setFqr(xtyhDto1.getYhid());
                    rszpDto.setFqsj(((JSONObject) obj).getString("create_time"));
                    JgxxDto jgxxDto=new JgxxDto();
                    String bmmc = ((JSONObject) obj).getString("originator_dept_name");
                    rszpDto.setFqrbmmc(bmmc);
                    String[] split = bmmc.split("-");
                    jgxxDto.setJgmc(split[split.length-1]);
                    JgxxDto jgxx = jgxxService.getJgxxByJgmc(jgxxDto);
                    if(jgxx!=null){
                        rszpDto.setFqrbm(jgxx.getJgid());
                    }
                    JSONArray jsonArray = ((JSONObject) obj).getJSONArray("form_component_values");
                    rszpDto.setXqgw(jsonArray.getJSONObject(0).getString("value"));
                    rszpDto.setGwbased(jsonArray.getJSONObject(1).getString("value"));
                    rszpDto.setFzqyjyy(jsonArray.getJSONObject(2).getString("value"));
                    String xqrs=jsonArray.getJSONObject(3).getString("value");
                    if(xqrs.contains("人")){
                        String str = xqrs.replaceAll("人", "");
                        rszpDto.setXqrs(str);
                    }else{
                        rszpDto.setXqrs(xqrs);
                    }
                    rszpDto.setYjnx(jsonArray.getJSONObject(4).getString("value"));
                    rszpDto.setYjyx(jsonArray.getJSONObject(5).getString("value"));
                    String rs = jsonArray.getJSONObject(6).getString("value");
                    if(rs.equals("null")){
                        rszpDto.setGwxyrs("0");
                    }else{
                        rszpDto.setGwxyrs(rs);
                    }
                    rszpDto.setGwyqzz(jsonArray.getJSONObject(7).getString("value"));
                    if(!"null".equals(jsonArray.getJSONObject(8).getString("value"))){
                        rszpDto.setXwdgrq(jsonArray.getJSONObject(8).getString("value"));
                    }
                    RszpDto rszpDto_t = getSpid(rszpDto);
                    if(rszpDto_t!=null){
                        rszpDto.setZpid(rszpDto_t.getZpid());
                        update(rszpDto);
                    }else{
                        rszpDto.setZpid(StringUtil.generateUUID());
                        insert(rszpDto);
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
