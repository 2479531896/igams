package com.matridx.igams.hrm.service.impl;

import com.alibaba.fastjson.JSON;
import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.response.OapiAttendanceVacationQuotaListResponse;
import com.google.common.collect.Lists;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.UserDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.DingMessageType;
import com.matridx.igams.common.enums.RedisCommonKeyEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IUserService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.hrm.dao.entities.YhjqDto;
import com.matridx.igams.hrm.dao.entities.YhjqModel;
import com.matridx.igams.hrm.dao.entities.YhqjmxDto;
import com.matridx.igams.hrm.dao.entities.YhqjxxDto;
import com.matridx.igams.hrm.dao.post.IYhjqDao;
import com.matridx.igams.hrm.service.svcinterface.IYhjqService;
import com.matridx.igams.hrm.service.svcinterface.IYhqjmxService;
import com.matridx.igams.hrm.service.svcinterface.IYhqjxxService;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author:JYK
 */
@Service
public class YhjqServiceImpl extends BaseBasicServiceImpl<YhjqDto, YhjqModel, IYhjqDao> implements IYhjqService {
    private final Logger log = LoggerFactory.getLogger(YhjqServiceImpl.class);
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    IUserService userService;
    @Autowired
    IDdxxglService ddxxglService;
    @Autowired
    IXxglService xxglService;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Autowired
    IJcsjService jcsjService;
    @Autowired
    IYhqjxxService yhqjxxService;
    @Autowired
    IYhqjmxService yhqjmxService;

    /**
     * 定时清空过期假期
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void claerAllTimeOutHolidays(){
        try {
            YhjqDto yhjqDto=new YhjqDto();
            yhjqDto.setClearAll("Y");
            List<YhjqDto> yhjqDtos=dao.getDtoList(yhjqDto);
            if (!CollectionUtils.isEmpty(yhjqDtos)){
                List<String> yhjqids=new ArrayList<>();
                for (YhjqDto yhjqDto1:yhjqDtos){
                    yhjqids.add(yhjqDto1.getYhjqid());
                }
                YhjqDto yhjqDto_t=new YhjqDto();
                yhjqDto_t.setIds(yhjqids);
                yhjqDto_t.setSyed("0");
                yhjqDto_t.setDdsyed("0");
                yhjqDto_t.setClearAll("Y");
                update(yhjqDto_t);
            }
        }catch (Exception e){
            log.error(e.toString());
        }

    }
    /**
     * 定时查询假期余额
     */
   public void regularlyQueryHolidayBalance(Map<String,String> csmap){
       try {
           String glyddid =  String.valueOf(redisUtil.get(RedisCommonKeyEnum.VACATION_ADMIN.getKey()));
           if (StringUtil.isBlank(glyddid)){
               throw new BusinessException("msg","请配置管理员钉钉id");
           }
           String wbcxid = csmap.get("wbcxid");
           if (StringUtil.isBlank(wbcxid)){
               throw new BusinessException("msg","请配置外部程序id");
           }
           List<JcsjDto> jqlxlist = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.HOLIDAY_TYPE.getCode());//假期类型
//           List<OapiAttendanceVacationTypeListResponse.Result> resultList = talkUtil.queryHolidayRuleList(talkUtil.getToken(wbcxDto_t.getWbcxid()), "061955116926357964", "all");
           Map<String, String> map = new HashMap<>();
           if (!CollectionUtils.isEmpty(jqlxlist)) {
//               for (int i = 0; i < resultList.size(); i++) {
                   for (JcsjDto jcsjDto : jqlxlist) {
                      if ("1".equals(jcsjDto.getCskz3())) {
                           map.put(jcsjDto.getCskz2(), jcsjDto.getCsid());
                      }
//                   }
               }
           }
           String token = talkUtil.getToken(wbcxid);
           if (!MapUtils.isEmpty(map)) {
               List<OapiAttendanceVacationQuotaListResponse.OapiLeaveQuotaUserListVo> lists = new ArrayList<>();
               String allUserIds = dao.getAllUserIds();
               for (String leave_code : map.keySet()) {
                   if (StringUtil.isNotBlank(leave_code) && StringUtil.isNotBlank(map.get(leave_code))) {
                       List<OapiAttendanceVacationQuotaListResponse.OapiLeaveQuotaUserListVo> leaveQuotaUserListVos =
                               talkUtil.queryHolidayBalance(token, leave_code, glyddid, allUserIds);
                       lists.addAll(leaveQuotaUserListVos);
                   }
               }

               if (!CollectionUtils.isEmpty(lists)) {
                   List<YhjqDto> yhjqDtoList = new ArrayList<>();
                   for (OapiAttendanceVacationQuotaListResponse.OapiLeaveQuotaUserListVo oapiLeaveQuotaUserListVo : lists) {
                       if (!CollectionUtils.isEmpty(oapiLeaveQuotaUserListVo.getLeaveQuotas())) {
                           for (int j = 0; j < oapiLeaveQuotaUserListVo.getLeaveQuotas().size(); j++) {
                               OapiAttendanceVacationQuotaListResponse.Leavequotas leavequotas = oapiLeaveQuotaUserListVo.getLeaveQuotas().get(j);
                               YhjqDto yhjqDto = new YhjqDto();
                               yhjqDto.setJqlx(map.get(leavequotas.getLeaveCode()));
                               yhjqDto.setDdid(leavequotas.getUserid());
                               yhjqDto.setNd(leavequotas.getQuotaCycle());
                               BigDecimal quotaNum = new BigDecimal(0);
                               BigDecimal usedNum = new BigDecimal(0);
                               if (leavequotas.getQuotaNumPerHour() != null) {
                                   quotaNum = new BigDecimal(leavequotas.getQuotaNumPerHour()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                                   usedNum = new BigDecimal(leavequotas.getUsedNumPerHour()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                               } else if (leavequotas.getQuotaNumPerDay() != null) {
                                   quotaNum = new BigDecimal(leavequotas.getQuotaNumPerDay()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                                   usedNum = new BigDecimal(leavequotas.getUsedNumPerDay()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                               }
                               BigDecimal ddsyed = quotaNum.subtract(usedNum);
                               yhjqDto.setDdze(String.valueOf(quotaNum));
                               yhjqDto.setDdyyed(String.valueOf(usedNum));
                               yhjqDto.setDdsyed(String.valueOf(ddsyed));
                               yhjqDtoList.add(yhjqDto);
                           }
                       }
                   }
                   if (!CollectionUtils.isEmpty(yhjqDtoList)) {
                       //查询钉钉接口额度与OA假期额度对比 存在差异发送钉钉消息
                       List<YhjqDto> list = dao.selectDtoListByList(yhjqDtoList);
                       List<DdxxglDto> ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.VACATION_LEFT.getCode());
                       if (!CollectionUtils.isEmpty(list)){
                           List<List<YhjqDto>> partition = Lists.partition(list, 500);
                           for (List<YhjqDto> yhjqDtos : partition) {
                               boolean isSuccess = dao.updateList(yhjqDtos);
                               if (!isSuccess){
                                   throw new BusinessException("msg","更新OA假期额度失败！");
                               }
                           }
                           if (!CollectionUtils.isEmpty(ddxxglDtolist)){
                               String ICOMM_JQ00001 = xxglService.getMsg("ICOMM_JQ00001");
                               String ICOMM_JQ00002 = xxglService.getMsg("ICOMM_JQ00002");
                               for (DdxxglDto ddxxglDto : ddxxglDtolist) {
                                   // 内网访问
                                   String internalbtn  = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/vacation/diffentWithDDAndOA/diffentWithDDAndOA?urlPrefix="+urlPrefix+"&wbfw=1", StandardCharsets.UTF_8);
                                   List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
                                   OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
                                   btnJsonList.setTitle("跳转查看");
                                   btnJsonList.setActionUrl(internalbtn);
                                   btnJsonLists.add(btnJsonList);
                                   talkUtil.sendCardMessage(ddxxglDto.getYhm(),ddxxglDto.getDdid(),ICOMM_JQ00001,ICOMM_JQ00002,
                                           btnJsonLists,"1");
                               }
                           }
                       }
                   }
               }
           }
       }catch (Exception e){
           log.error(e.toString());
       }

   }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateCallBackAskForLeave(GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResult approveInfo,HttpServletRequest request) {
        //管理员人钉钉id
        String glyddid =  String.valueOf(redisUtil.get(RedisCommonKeyEnum.VACATION_ADMIN.getKey()));
        YhqjxxDto yhqjxxDto=new YhqjxxDto();
        YhjqDto yhjqDto=new YhjqDto();
        yhqjxxDto.setDdslid(request.getParameter("processInstanceId"));
        if (yhqjxxService.selectDtoByDdslid(yhqjxxDto)==null){
            yhqjxxDto.setYhqjid(StringUtil.generateUUID());
            UserDto xtyhDto = new UserDto();
            xtyhDto.setDdid(approveInfo.getOriginatorUserId());
            UserDto xtyhDto1 = userService.getYhByDdid(xtyhDto);
            List<GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues> formComponentValues = approveInfo.getFormComponentValues();
            String value = formComponentValues.get(0).getValue();
            List<String> strings = JSON.parseArray(value, String.class);
            yhqjxxDto.setQjkssj(strings.get(0));
            yhqjxxDto.setQjjssj(strings.get(1));
            yhqjxxDto.setQjsc(strings.get(2));
            JcsjDto jcsjDto=new JcsjDto();
            jcsjDto.setCsmc(strings.get(4));
            jcsjDto.setJclb(BasicDataTypeEnum.HOLIDAY_TYPE.getCode());
            jcsjDto=jcsjService.getDtoByCsmcAndJclb(jcsjDto);
            yhqjxxDto.setQjlx(jcsjDto.getCsid());
            yhqjxxDto.setZt("10");
            yhqjxxDto.setYhid(xtyhDto1.getYhid());
            yhjqDto.setQjkssj(strings.get(0));
            yhjqDto.setSc(strings.get(2));
            yhjqDto.setJqlx(jcsjDto.getCsid());
            yhjqDto.setYhid(xtyhDto1.getYhid());
            List<YhjqDto> yhjqDtos=dao.selectDtosByJqlxAndYhid(yhjqDto);
            List<YhjqDto> yhjqDtoList=new ArrayList<>();
            int i=0;
            if (!CollectionUtils.isEmpty(yhjqDtos)){
                yhqjxxService.insert(yhqjxxDto);
                updateRestOfHolidays(yhjqDtos,yhjqDto.getSc(),yhjqDtoList,i,glyddid);
            }
            if (!CollectionUtils.isEmpty(yhjqDtoList)){
                dao.updateListByList(yhjqDtoList);
                List<YhqjmxDto> yhqjmxDtos=new ArrayList<>();
                for (YhjqDto dto : yhjqDtoList) {
                    YhqjmxDto yhqjmxDto = new YhqjmxDto();
                    yhqjmxDto.setQjmxid(StringUtil.generateUUID());
                    yhqjmxDto.setYhqjid(yhqjxxDto.getYhqjid());
                    yhqjmxDto.setYhjqid(dto.getYhjqid());
                    yhqjmxDto.setSc(dto.getQjmxsc());
                    yhqjmxDtos.add(yhqjmxDto);
                }
                yhqjmxService.insertList(yhqjmxDtos);
            }
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean rollBackHolidays(HttpServletRequest request) {
        String glyddid =  String.valueOf(redisUtil.get(RedisCommonKeyEnum.VACATION_ADMIN.getKey()));
        YhqjxxDto yhqjxxDto=new YhqjxxDto();
        yhqjxxDto.setDdslid(request.getParameter("processInstanceId"));
        yhqjxxDto =yhqjxxService.selectDtoByDdslid(yhqjxxDto);
        YhqjmxDto yhqjmxDto=new YhqjmxDto();
        yhqjmxDto.setYhqjid(yhqjxxDto.getYhqjid());
        List<YhqjmxDto> yhqjmxDtos=yhqjmxService.getDtoList(yhqjmxDto);
        YhjqDto yhjqDto_sel = new YhjqDto();
        yhjqDto_sel.setQjkssj(yhqjxxDto.getQjkssj());
        yhjqDto_sel.setJqlx(yhqjxxDto.getQjlx());
        yhjqDto_sel.setYhid(yhqjxxDto.getYhid());
        List<YhjqDto> yhjqDtos_sel = dao.selectDtosByJqlxAndYhid(yhjqDto_sel);
        List<YhjqDto> modYhjqDtos = new ArrayList<>();
        for (YhjqDto yhjqDto : yhjqDtos_sel) {
            YhjqDto yhjqDto_mod = new YhjqDto();
            yhjqDto_mod.setNd(yhjqDto.getNd());
            yhjqDto_mod.setDdid(yhjqDto.getDdid());
            yhjqDto_mod.setYhjqid(yhjqDto.getYhjqid());
            yhjqDto_mod.setJqlxcskz2(yhjqDto.getJqlxcskz2());
            yhjqDto_mod.setYhm(yhjqDto.getYhm());
            this.dealDdedInfo(glyddid, yhjqDto_mod);
            modYhjqDtos.add(yhjqDto_mod);
        }
        dao.updateDdyes(modYhjqDtos);
        List<YhjqDto> yhjqDtos=new ArrayList<>();
        for (YhqjmxDto dto : yhqjmxDtos) {
            YhjqDto yhjqDto = new YhjqDto();
            yhjqDto.setSyed(dto.getSc());
            yhjqDto.setYhjqid(dto.getYhjqid());
            yhjqDto.setYyed(dto.getSc());
            yhjqDtos.add(yhjqDto);
        }
        dao.updateListSyed(yhjqDtos);
        yhqjxxDto.setZt("00");
        yhqjxxService.update(yhqjxxDto);
        return true;
    }

    @Override
    public boolean updateUserHolidaysStatus(HttpServletRequest request) {
       YhqjxxDto yhqjxxDto=new YhqjxxDto();
       yhqjxxDto.setDdslid(request.getParameter("processInstanceId"));
        yhqjxxDto.setZt("80");
       yhqjxxService.update(yhqjxxDto);
        return true;
    }

    @Override
    public boolean reportBackHolidays(String mainProcessInstanceId) {
       String glyddid =  String.valueOf(redisUtil.get(RedisCommonKeyEnum.VACATION_ADMIN.getKey()));
       YhqjxxDto yhqjxxDto=new YhqjxxDto();
       yhqjxxDto.setDdslid(mainProcessInstanceId);
       yhqjxxDto=yhqjxxService.selectDtoByDdslid(yhqjxxDto);
       yhqjxxDto.setZt("90");
       yhqjxxService.update(yhqjxxDto);
        YhqjmxDto yhqjmxDto=new YhqjmxDto();
        yhqjmxDto.setYhqjid(yhqjxxDto.getYhqjid());
        List<YhqjmxDto> yhqjmxDtos=yhqjmxService.getDtoList(yhqjmxDto);
        YhjqDto yhjqDto_sel = new YhjqDto();
        yhjqDto_sel.setQjkssj(yhqjxxDto.getQjkssj());
        yhjqDto_sel.setJqlx(yhqjxxDto.getQjlx());
        yhjqDto_sel.setYhid(yhqjxxDto.getYhid());
        List<YhjqDto> yhjqDtos_sel = dao.selectDtosByJqlxAndYhid(yhjqDto_sel);
        List<YhjqDto> modYhjqDtos = new ArrayList<>();
        for (YhjqDto yhjqDto : yhjqDtos_sel) {
            YhjqDto yhjqDto_mod = new YhjqDto();
            yhjqDto_mod.setNd(yhjqDto.getNd());
            yhjqDto_mod.setDdid(yhjqDto.getDdid());
            yhjqDto_mod.setYhjqid(yhjqDto.getYhjqid());
            yhjqDto_mod.setJqlxcskz2(yhjqDto.getJqlxcskz2());
            yhjqDto_mod.setYhm(yhjqDto.getYhm());
            this.dealDdedInfo(glyddid, yhjqDto_mod);
            modYhjqDtos.add(yhjqDto_mod);
        }
        dao.updateDdyes(modYhjqDtos);
        List<YhjqDto> yhjqDtos=new ArrayList<>();
        for (YhqjmxDto dto : yhqjmxDtos) {
            YhjqDto yhjqDto = new YhjqDto();
            yhjqDto.setSyed(dto.getSc());
            yhjqDto.setYhjqid(dto.getYhjqid());
            yhjqDto.setYyed(dto.getSc());
            yhjqDtos.add(yhjqDto);
        }
        dao.updateListSyed(yhjqDtos);
        return true;
    }

    private void dealDdedInfo(String glyddid, YhjqDto yhjqDto) {
        BigDecimal quotaNum = new BigDecimal(0);
        BigDecimal usedNum = new BigDecimal(0);
        //获取钉钉额度信息
        List<OapiAttendanceVacationQuotaListResponse.OapiLeaveQuotaUserListVo> oapiLeaveQuotaUserListVos = talkUtil.queryHolidayBalance(talkUtil.getDingTokenByUserId(yhjqDto.getYhm()), yhjqDto.getJqlxcskz2(), glyddid, yhjqDto.getDdid());
        for (OapiAttendanceVacationQuotaListResponse.OapiLeaveQuotaUserListVo oapiLeaveQuotaUserListVo : oapiLeaveQuotaUserListVos) {
            if (!CollectionUtils.isEmpty(oapiLeaveQuotaUserListVo.getLeaveQuotas())){
                for (OapiAttendanceVacationQuotaListResponse.Leavequotas leaveQuota : oapiLeaveQuotaUserListVo.getLeaveQuotas()) {
                    if (yhjqDto.getNd().equals(leaveQuota.getQuotaCycle())) {
                        if (leaveQuota.getQuotaNumPerHour() != null) {
                            quotaNum = new BigDecimal(leaveQuota.getQuotaNumPerHour()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                            usedNum = new BigDecimal(leaveQuota.getUsedNumPerHour()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                        } else if (leaveQuota.getQuotaNumPerDay() != null) {
                            quotaNum = new BigDecimal(leaveQuota.getQuotaNumPerDay()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                            usedNum = new BigDecimal(leaveQuota.getUsedNumPerDay()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                        }
                    }
                }
            }
        }
        //由于钉钉回调回来已经扣除 所以不用再减去请假时长直接拿钉钉查询到的额度
        yhjqDto.setDdze(String.valueOf(quotaNum));
        yhjqDto.setDdyyed(String.valueOf(usedNum));
        yhjqDto.setDdsyed(String.valueOf(quotaNum.subtract(usedNum)));
    }

    /**
     * 递归更新额度
     */
    public void updateRestOfHolidays(List<YhjqDto> list,String qjsc,List<YhjqDto> yhjqDtoList,int i,String glyddid){
        if (i<list.size()){
            YhjqDto yhjqDto=list.get(i);
            this.dealDdedInfo(glyddid,yhjqDto);
            BigDecimal syed = new BigDecimal(yhjqDto.getSyed());
            BigDecimal qjscBig = new BigDecimal(qjsc);
            if (qjscBig.compareTo(syed)>0){
                qjsc=String.valueOf(new BigDecimal(qjsc).subtract(syed));
                yhjqDto.setYyed(String.valueOf(new BigDecimal(yhjqDto.getYyed()).add(syed)));
                i++;
                yhjqDto.setQjmxsc(yhjqDto.getSyed());
                yhjqDto.setSyed("0");
                yhjqDtoList.add(yhjqDto);
                updateRestOfHolidays(list,qjsc,yhjqDtoList,i,glyddid);
            }else if (qjscBig.compareTo(syed)<=0){
                yhjqDto.setSyed(String.valueOf(syed.subtract(new BigDecimal(qjsc))));
                yhjqDto.setYyed(String.valueOf(new BigDecimal(yhjqDto.getYyed()).add(new BigDecimal(qjsc))));
                yhjqDto.setQjmxsc(qjsc);
                yhjqDtoList.add(yhjqDto);
            }
        }
    }
    /**
     * 假期审批定时任务
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void holidayApprovalScheduledTask(Map<String,String> map) throws BusinessException {
        String glyddid =  String.valueOf(redisUtil.get(RedisCommonKeyEnum.VACATION_ADMIN.getKey()));
        if (StringUtil.isBlank(glyddid)){
            throw new BusinessException("msg","请配置管理员钉钉id");
        }
        String wbcxid = map.get("wbcxid");
        if (StringUtil.isBlank(wbcxid)){
            throw new BusinessException("msg","请配置外部程序id");
        }
        JcsjDto jcsjDto_dd = new JcsjDto();
        jcsjDto_dd.setCsdm("HOLIDAY");
        jcsjDto_dd.setJclb("DINGTALK_AUDTI_CALLBACK_TYPE");
        jcsjDto_dd = jcsjService.getDtoByCsdmAndJclb(jcsjDto_dd);
        if(StringUtil.isBlank(jcsjDto_dd.getCsmc()) || StringUtil.isBlank(jcsjDto_dd.getCskz1())) {
            throw new BusinessException("msg","请设置假期回调的钉钉审批回调类型基础数据！");
        }
        Date date=new Date();//获取当前时间
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DATE,-3);
        long startTime = calendar.getTime().getTime();
        long endTime=date.getTime();
        List<String> ddsplsids;
            ddsplsids = talkUtil.getApproveList(talkUtil.getToken(wbcxid), jcsjDto_dd.getCskz1(), startTime, endTime, null);
        List<YhqjxxDto> yhqjxxDtos=yhqjxxService.getDtoList(new YhqjxxDto());
        List<String> stringList=new ArrayList<>();
        for (YhqjxxDto yhqjxxDto:yhqjxxDtos){
            stringList.add(yhqjxxDto.getDdslid());
        }
        //销假审批最后处理
        List<GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResult> xjSpList = new ArrayList<>();
        for (String ddspslid:ddsplsids){
            YhqjxxDto yhqjxxDto=new YhqjxxDto();
            YhjqDto yhjqDto=new YhjqDto();
            GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResult approveInfo = talkUtil.getApproveInfo(wbcxid, ddspslid);
            String mainProcessInstanceId = approveInfo.getMainProcessInstanceId();
            String result = approveInfo.getResult();
            String status=approveInfo.getStatus();
            List<GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues> formComponentValues = approveInfo.getFormComponentValues();
            if (StringUtil.isBlank(mainProcessInstanceId)&&"agree".equals(result)){
                yhqjxxDto.setZt("80");
            }else if (StringUtil.isBlank(mainProcessInstanceId)&&"RUNNING".equals(status)){
                yhqjxxDto.setZt("10");
            }else if ((StringUtil.isBlank(mainProcessInstanceId))&&(("".equals(result)&&"TERMINATED".equals(status))||"refuse".equals(result))){
                yhqjxxDto.setZt("00");
            }else if (StringUtil.isNotBlank(mainProcessInstanceId)&&"agree".equals(result)){
                xjSpList.add(approveInfo);
                continue;
            }else if (StringUtil.isNotBlank(mainProcessInstanceId)&&!"agree".equals(result)){
                //销假审批未通过或刚提交的不用处理
                continue;
            }
            if (!stringList.contains(ddspslid)&&!"90".equals(yhqjxxDto.getZt())){
                yhqjxxDto.setYhqjid(StringUtil.generateUUID());
                UserDto xtyhDto = new UserDto();
                xtyhDto.setDdid(approveInfo.getOriginatorUserId());
                UserDto xtyhDto1 = userService.getYhByDdid(xtyhDto);
                String value = formComponentValues.get(0).getValue();
                List<String> strings = JSON.parseArray(value, String.class);
                yhqjxxDto.setQjkssj(strings.get(0));
                yhqjxxDto.setQjjssj(strings.get(1));
                yhqjxxDto.setQjsc(strings.get(2));
                yhqjxxDto.setDdslid(ddspslid);
                yhqjxxDto.setYhid(xtyhDto1.getYhid());
                JcsjDto jcsjDto=new JcsjDto();
                jcsjDto.setCsmc(strings.get(4));
                jcsjDto.setJclb(BasicDataTypeEnum.HOLIDAY_TYPE.getCode());
                jcsjDto=jcsjService.getDtoByCsmcAndJclb(jcsjDto);
                if (null!=jcsjDto){
                    yhqjxxDto.setQjlx(jcsjDto.getCsid());
                    yhjqDto.setJqlx(jcsjDto.getCsid());
                }
                yhjqDto.setYhid(xtyhDto1.getYhid());
                yhjqDto.setQjkssj(strings.get(0));
                yhjqDto.setSc(strings.get(2));
                List<YhjqDto> yhjqDtos=dao.selectDtosByJqlxAndYhid(yhjqDto);
                List<YhjqDto> yhjqDtoList=new ArrayList<>();
                int i=0;
                if (!CollectionUtils.isEmpty(yhjqDtos)){
                    yhqjxxService.insert(yhqjxxDto);
                    updateRestOfHolidays(yhjqDtos,yhjqDto.getSc(),yhjqDtoList,i,glyddid);
                }
                if (!CollectionUtils.isEmpty(yhjqDtoList)){
                    if ("80".equals(yhqjxxDto.getZt())||"10".equals(yhqjxxDto.getZt())){
                        dao.updateListByList(yhjqDtoList);
                    }
                    List<YhqjmxDto> yhqjmxDtos=new ArrayList<>();
                    for (YhjqDto dto : yhjqDtoList) {
                        YhqjmxDto yhqjmxDto = new YhqjmxDto();
                        yhqjmxDto.setQjmxid(StringUtil.generateUUID());
                        yhqjmxDto.setYhqjid(yhqjxxDto.getYhqjid());
                        yhqjmxDto.setYhjqid(dto.getYhjqid());
                        yhqjmxDto.setSc(dto.getQjmxsc());
                        yhqjmxDtos.add(yhqjmxDto);
                    }
                    yhqjmxService.insertList(yhqjmxDtos);
                }
            }else if (stringList.contains(ddspslid)&&!"90".equals(yhqjxxDto.getZt())){
                YhqjxxDto yhqjxxDto1=new YhqjxxDto();
                yhqjxxDto1.setDdslid(ddspslid);
                yhqjxxDto1=yhqjxxService.selectDtoByDdslid(yhqjxxDto1);
                if (null!=yhqjxxDto1) {
                    if (!yhqjxxDto.getZt().equals(yhqjxxDto1.getZt())) {
                        if ("00".equals(yhqjxxDto1.getZt()) && ("10".equals(yhqjxxDto.getZt()) || "80".equals(yhqjxxDto.getZt()))) {
                            yhqjxxDto.setYhqjid(StringUtil.generateUUID());
                            UserDto xtyhDto = new UserDto();
                            xtyhDto.setDdid(approveInfo.getOriginatorUserId());
                            UserDto xtyhDto1 = userService.getYhByDdid(xtyhDto);
                            String value = formComponentValues.get(0).getValue();
                            List<String> strings = JSON.parseArray(value, String.class);
                            yhqjxxDto.setQjkssj(strings.get(0));
                            yhqjxxDto.setQjjssj(strings.get(1));
                            yhqjxxDto.setQjsc(strings.get(2));
                            JcsjDto jcsjDto = new JcsjDto();
                            jcsjDto.setCsmc(strings.get(4));
                            jcsjDto.setJclb(BasicDataTypeEnum.HOLIDAY_TYPE.getCode());
                            jcsjDto = jcsjService.getDtoByCsmcAndJclb(jcsjDto);
                            yhqjxxDto.setYhid(xtyhDto1.getYhid());
                            yhqjxxDto.setQjlx(jcsjDto.getCsid());
                            yhjqDto.setQjkssj(strings.get(0));
                            yhjqDto.setSc(strings.get(2));
                            yhjqDto.setJqlx(jcsjDto.getCsid());
                            yhjqDto.setYhid(xtyhDto1.getYhid());
                            List<YhjqDto> yhjqDtos = dao.selectDtosByJqlxAndYhid(yhjqDto);
                            List<YhjqDto> yhjqDtoList = new ArrayList<>();
                            int i = 0;
                            yhqjxxDto.setDdslid(ddspslid);
                            if (!CollectionUtils.isEmpty(yhjqDtos)) {
                                yhqjxxService.insert(yhqjxxDto);
                                updateRestOfHolidays(yhjqDtos, yhjqDto.getSc(), yhjqDtoList, i,glyddid);
                            }
                            if (!CollectionUtils.isEmpty(yhjqDtoList)) {
                                dao.updateListByList(yhjqDtoList);
                                List<YhqjmxDto> yhqjmxDtos = new ArrayList<>();
                                for (YhjqDto dto : yhjqDtoList) {
                                    YhqjmxDto yhqjmxDto = new YhqjmxDto();
                                    yhqjmxDto.setQjmxid(StringUtil.generateUUID());
                                    yhqjmxDto.setYhqjid(yhqjxxDto.getYhqjid());
                                    yhqjmxDto.setYhjqid(dto.getYhjqid());
                                    yhqjmxDto.setSc(dto.getQjmxsc());
                                    yhqjmxDtos.add(yhqjmxDto);
                                }
                                yhqjmxService.insertList(yhqjmxDtos);
                            }
                        } else if ("10".equals(yhqjxxDto1.getZt()) && "00".equals(yhqjxxDto.getZt())) {
                            yhqjxxDto1.setZt("00");
                            yhqjxxService.update(yhqjxxDto1);
                            YhqjmxDto yhqjmxDto = new YhqjmxDto();
                            yhqjmxDto.setYhqjid(yhqjxxDto1.getYhqjid());
                            List<YhqjmxDto> yhqjmxDtos = yhqjmxService.getDtoList(yhqjmxDto);
                            List<YhjqDto> yhjqDtos = new ArrayList<>();
                            for (YhqjmxDto value : yhqjmxDtos) {
                                YhjqDto dto = new YhjqDto();
                                dto.setSyed(value.getSc());
                                dto.setYhjqid(value.getYhjqid());
                                dto.setYyed(value.getSc());
                                yhjqDtos.add(dto);
                            }
                            dao.updateListSyed(yhjqDtos);
                        } else if ("10".equals(yhqjxxDto1.getZt()) && "80".equals(yhqjxxDto.getZt())) {
                            yhqjxxDto1.setZt("80");
                            yhqjxxService.update(yhqjxxDto1);
                        }
                    }
                }
            }
        }
        //处理销假审核通过的数据
        if (!CollectionUtils.isEmpty(xjSpList)){
            for (GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResult result : xjSpList) {
               YhqjxxDto yhqjxxDto=new YhqjxxDto();
               yhqjxxDto.setDdslid(result.getMainProcessInstanceId());
               yhqjxxDto=yhqjxxService.selectDtoByDdslid(yhqjxxDto);
               if (null!=yhqjxxDto&&!"90".equals(yhqjxxDto.getZt())){
                   yhqjxxDto.setZt("90");
                   yhqjxxService.update(yhqjxxDto);
                   YhqjmxDto yhqjmxDto=new YhqjmxDto();
                   yhqjmxDto.setYhqjid(yhqjxxDto.getYhqjid());
                   List<YhqjmxDto> yhqjmxDtos=yhqjmxService.getDtoList(yhqjmxDto);
                   YhjqDto yhjqDto_sel = new YhjqDto();
                   yhjqDto_sel.setQjkssj(yhqjxxDto.getQjkssj());
                   yhjqDto_sel.setJqlx(yhqjxxDto.getQjlx());
                   yhjqDto_sel.setYhid(yhqjxxDto.getYhid());
                   List<YhjqDto> yhjqDtos_sel = dao.selectDtosByJqlxAndYhid(yhjqDto_sel);
                   List<YhjqDto> modYhjqDtos = new ArrayList<>();
                   for (YhjqDto dto : yhjqDtos_sel) {
                       YhjqDto yhjqDto_mod = new YhjqDto();
                       yhjqDto_mod.setNd(dto.getNd());
                       yhjqDto_mod.setDdid(dto.getDdid());
                       yhjqDto_mod.setYhjqid(dto.getYhjqid());
                       yhjqDto_mod.setJqlxcskz2(dto.getJqlxcskz2());
                       yhjqDto_mod.setYhm(dto.getYhm());
                       this.dealDdedInfo(glyddid, yhjqDto_mod);
                       modYhjqDtos.add(yhjqDto_mod);
                   }
                   dao.updateDdyes(modYhjqDtos);
                   List<YhjqDto> yhjqDtos=new ArrayList<>();
                   for (YhqjmxDto dto : yhqjmxDtos) {
                       YhjqDto yhjqDto = new YhjqDto();
                       yhjqDto.setSyed(dto.getSc());
                       yhjqDto.setYhjqid(dto.getYhjqid());
                       yhjqDto.setYyed(dto.getSc());
                       yhjqDtos.add(yhjqDto);
                   }
                   dao.updateListSyed(yhjqDtos);
               }
            }
        }

    }

    @Override
    public YhjqDto getDtoByDdcs(YhjqDto yhjqDto) {
        return dao.getDtoByDdcs(yhjqDto);
    }

    @Override
    public boolean insertYhjqDtos(List<YhjqDto> yhjqDtos) {
        return dao.insertYhjqDtos(yhjqDtos);
    }

    @Override
    public boolean updateYhjqDtos(List<YhjqDto> yhjqDtos) {
        return dao.updateYhjqDtos(yhjqDtos);
    }
}
