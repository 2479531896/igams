package com.matridx.igams.hrm.service.impl;
import com.alibaba.fastjson.JSON;
import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResult;
import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.ApproveStatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.hrm.dao.entities.TgxxDto;
import com.matridx.igams.hrm.dao.entities.TgxxModel;
import com.matridx.igams.hrm.dao.post.ITgxxDao;
import com.matridx.igams.hrm.service.svcinterface.ITgxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.igams.common.util.DingTalkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class TgxxServiceImpl extends BaseBasicServiceImpl<TgxxDto, TgxxModel, ITgxxDao> implements ITgxxService {
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    IJcsjService jcsjService;
    private final Logger log = LoggerFactory.getLogger(TgxxServiceImpl.class);
    /**
     * 调岗审批回调
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean aduitJobAdjustmentCallback(String processInstanceId,String processCode,String wbcxid) throws BusinessException {
        log.error("aduitJobAdjustmentCallback processInstanceId: " + processInstanceId);
        log.error("aduitJobAdjustmentCallback processCode: " + processCode);
        log.error("aduitJobAdjustmentCallback wbcxid: " + wbcxid);
        String token = talkUtil.getToken(wbcxid);
        GetProcessInstanceResponseBodyResult approveInfo = talkUtil.getApproveInfo(token, processInstanceId);
        if (ApproveStatusEnum.COMPLETED.code.equals(approveInfo.getStatus())&&"agree".equals(approveInfo.getResult())) {
            //获取表单的值
            List<GetProcessInstanceResponseBodyResultFormComponentValues> formComponentValues = approveInfo.getFormComponentValues();
            TgxxDto tgxxDto = new TgxxDto();
            tgxxDto.setTgxxid(StringUtil.generateUUID());
            tgxxDto.setWbcxid(wbcxid);
            tgxxDto.setDdslid(processInstanceId);
            tgxxDto.setDdid(approveInfo.getOriginatorUserId());
            for (GetProcessInstanceResponseBodyResultFormComponentValues formComponentValue : formComponentValues) {
                if (StringUtil.isNotBlank(formComponentValue.getName()))
                switch (formComponentValue.getName()){
                    case "入职日期": tgxxDto.setRzrq(formComponentValue.getValue());break;
                    case "原部门": tgxxDto.setYbm(formComponentValue.getValue());break;
                    case "原主部门": tgxxDto.setYzbm(String.valueOf(JSON.parseArray(formComponentValue.getExtValue(),Map.class).get(0).get("id")));break;
                    case "原职位": tgxxDto.setYzw(formComponentValue.getValue());break;
                    case "转入部门": tgxxDto.setZrbm(String.valueOf(JSON.parseArray(formComponentValue.getExtValue(),Map.class).get(0).get("id")));break;
                    case "职位": tgxxDto.setZw(formComponentValue.getValue());break;
                    case "生效日期": tgxxDto.setSxrq(formComponentValue.getValue());break;
                }
            }
            tgxxDto.setSptgsj(StringUtil.isNotBlank(approveInfo.getFinishTime())?approveInfo.getFinishTime().substring(0,10):null);
            int insert = dao.insert(tgxxDto);
            if (insert<1){
                throw new BusinessException("msg","同步调岗的钉钉审批数据失败！");
            }
        }
        return true;
    }

    /**
     * @description 获取指定时间内的调岗审批信息
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void getJobAdjustmentAuditInfo(Map<String,String> map) throws BusinessException {
        int day = Integer.parseInt(map.get("day"));
        if (day<-120||day>0){
            throw new BusinessException("msg","获取审批信息不能超过120天或大于0");
        }
        String wbcxids = map.get("wbcxids");
        String[] split = wbcxids.split("@");
        for (String wbcxid : split) {
            String token = talkUtil.getToken(wbcxid);
            JcsjDto jcsjDto_dd = new JcsjDto();
            jcsjDto_dd.setCsdm("I");
            jcsjDto_dd.setJclb("DINGTALK_AUDTI_CALLBACK_TYPE");
            jcsjDto_dd = jcsjService.getDtoByCsdmAndJclb(jcsjDto_dd);
            if (StringUtil.isBlank(jcsjDto_dd.getCsmc()) || StringUtil.isBlank(jcsjDto_dd.getCskz1())) {
                throw new BusinessException("msg", "请设置调岗的钉钉审批回调类型基础数据！");
            }
            Date date = new Date();
            List<String> approveList = talkUtil.getApproveList(token, jcsjDto_dd.getCskz1(), DateUtils.getDate(date, day).getTime(), date.getTime(), null);
            if (!CollectionUtils.isEmpty(approveList)) {
                TgxxDto tgxxDto = new TgxxDto();
                List<TgxxDto> dtoList = dao.getDtoList(tgxxDto);
                if (!CollectionUtils.isEmpty(dtoList)) {
                    Iterator<String> iterator = approveList.iterator();
                    while (iterator.hasNext()) {
                        String next = iterator.next();
                        for (TgxxDto dto : dtoList) {
                            if (next.equals(dto.getDdslid())) {
                                //去除数据里有的
                                iterator.remove();
                            }
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(approveList)) {
                    List<TgxxDto> addTgxxDtos = new ArrayList<>();
                    for (String processInstanceId : approveList) {
                        GetProcessInstanceResponseBodyResult approveInfo = talkUtil.getApproveInfo(token, processInstanceId);
                        //审批完成的
                        if (ApproveStatusEnum.COMPLETED.code.equals(approveInfo.getStatus()) && "agree".equals(approveInfo.getResult())) {
                            //获取表单的值
                            List<GetProcessInstanceResponseBodyResultFormComponentValues> formComponentValues = approveInfo.getFormComponentValues();
                            TgxxDto tgxxDto_t = new TgxxDto();
                            tgxxDto_t.setTgxxid(StringUtil.generateUUID());
                            tgxxDto_t.setDdslid(processInstanceId);
                            tgxxDto_t.setDdid(approveInfo.getOriginatorUserId());
                            for (GetProcessInstanceResponseBodyResultFormComponentValues formComponentValue : formComponentValues) {
                                if (StringUtil.isNotBlank(formComponentValue.getName()))
                                    switch (formComponentValue.getName()) {
                                        case "入职日期":
                                            tgxxDto_t.setRzrq(formComponentValue.getValue());
                                            break;
                                        case "原部门":
                                            tgxxDto_t.setYbm(formComponentValue.getValue());
                                            break;
                                        case "原主部门":
                                            tgxxDto_t.setYzbm(String.valueOf(JSON.parseArray(formComponentValue.getExtValue(), Map.class).get(0).get("id")));
                                            break;
                                        case "原职位":
                                            tgxxDto_t.setYzw(formComponentValue.getValue());
                                            break;
                                        case "转入部门":
                                            tgxxDto_t.setZrbm(String.valueOf(JSON.parseArray(formComponentValue.getExtValue(), Map.class).get(0).get("id")));
                                            break;
                                        case "职位":
                                            tgxxDto_t.setZw(formComponentValue.getValue());
                                            break;
                                        case "生效日期":
                                            tgxxDto_t.setSxrq(formComponentValue.getValue());
                                            break;
                                    }
                            }
                            tgxxDto_t.setSptgsj(StringUtil.isNotBlank(approveInfo.getFinishTime()) ? approveInfo.getFinishTime().substring(0, 10) : null);
                            tgxxDto_t.setWbcxid(wbcxid);
                            addTgxxDtos.add(tgxxDto_t);
                        }
                    }
                    if (!CollectionUtils.isEmpty(addTgxxDtos)) {
                        boolean isSuccess = dao.insertTgxxDtos(addTgxxDtos);
                        if (!isSuccess) {
                            throw new BusinessException("msg", "同步调岗的钉钉审批数据失败！");
                        }
                    }
                }
            }
        }
    }
}
