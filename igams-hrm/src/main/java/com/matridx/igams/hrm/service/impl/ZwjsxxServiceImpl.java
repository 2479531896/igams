package com.matridx.igams.hrm.service.impl;

import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResult;
import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.ApproveStatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.hrm.dao.entities.ZwjsxxDto;
import com.matridx.igams.hrm.dao.entities.ZwjsxxModel;
import com.matridx.igams.hrm.dao.post.IZwjsxxDao;
import com.matridx.igams.hrm.service.svcinterface.IZwjsxxService;
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
public class ZwjsxxServiceImpl extends BaseBasicServiceImpl<ZwjsxxDto, ZwjsxxModel, IZwjsxxDao> implements IZwjsxxService {
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    IJcsjService jcsjService;
    private final Logger log = LoggerFactory.getLogger(ZwjsxxServiceImpl.class);
    /**
     * 职位晋升回调
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean aduitPostPromotionCallback(String processInstanceId,String processCode,String wbcxid) throws BusinessException{
        log.error("aduitPostPromotionCallback processInstanceId: " + processInstanceId);
        log.error("aduitPostPromotionCallback processCode: " + processCode);
        log.error("aduitPostPromotionCallback wbcxid: " + wbcxid);
        String token = talkUtil.getToken(wbcxid);
        GetProcessInstanceResponseBodyResult approveInfo = talkUtil.getApproveInfo(token, processInstanceId);
        if (ApproveStatusEnum.COMPLETED.code.equals(approveInfo.getStatus())&&"agree".equals(approveInfo.getResult())) {
                //获取表单的值
                List<GetProcessInstanceResponseBodyResultFormComponentValues> formComponentValues = approveInfo.getFormComponentValues();
                ZwjsxxDto zwjsxxDto = new ZwjsxxDto();
                zwjsxxDto.setZwjsid(StringUtil.generateUUID());
                zwjsxxDto.setWbcxid(wbcxid);
                zwjsxxDto.setDdslid(processInstanceId);
                zwjsxxDto.setDdid(approveInfo.getOriginatorUserId());
                zwjsxxDto.setBm(approveInfo.getOriginatorDeptId());
                for (GetProcessInstanceResponseBodyResultFormComponentValues formComponentValue : formComponentValues) {
                    if (StringUtil.isNotBlank(formComponentValue.getName()))
                    switch (formComponentValue.getName()){
                        case "入职时间": zwjsxxDto.setRzsj(formComponentValue.getValue());break;
                        case "原职位": zwjsxxDto.setYzw(formComponentValue.getValue());break;
                        case "原职位月薪（税前）": zwjsxxDto.setYzwyx(formComponentValue.getValue());break;
                        case "晋升职位": zwjsxxDto.setJszw(formComponentValue.getValue());break;
                        case "晋升后月薪（税前）": zwjsxxDto.setJshyx(formComponentValue.getValue());break;
                        case "生效时间": zwjsxxDto.setSxsj(formComponentValue.getValue());break;
                    }
                }
                zwjsxxDto.setSptgsj(StringUtil.isNotBlank(approveInfo.getFinishTime())?approveInfo.getFinishTime().substring(0,10):null);
            int insert = dao.insert(zwjsxxDto);
            if (insert<1){
                throw new BusinessException("msg","同步职位晋升的钉钉审批数据失败！");
            }
        }
        return true;
    }

    /**
     * 获取指定时间内的职位晋升审批信息
     * day 天数 最多120天
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void getPostPromotionAuditInfo(Map<String,String> map) throws BusinessException {
        int day = Integer.parseInt(map.get("day"));
        if (day<-120||day>0){
            throw new BusinessException("msg","获取审批信息不能超过120天或大于0");
        }
        String wbcxids = map.get("wbcxids");
        String[] split = wbcxids.split("@");
        for (String wbcxid : split) {
            String token = talkUtil.getToken(wbcxid);
            JcsjDto jcsjDto_dd = new JcsjDto();
            jcsjDto_dd.setCsdm("H");
            jcsjDto_dd.setJclb("DINGTALK_AUDTI_CALLBACK_TYPE");
            jcsjDto_dd = jcsjService.getDtoByCsdmAndJclb(jcsjDto_dd);
            if (StringUtil.isBlank(jcsjDto_dd.getCsmc()) || StringUtil.isBlank(jcsjDto_dd.getCskz1())) {
                throw new BusinessException("msg", "请设置职位晋升的钉钉审批回调类型基础数据！");
            }
            Date date = new Date();
            List<String> approveList = talkUtil.getApproveList(token, jcsjDto_dd.getCskz1(), DateUtils.getDate(date, day).getTime(), date.getTime(), null);
            if (!CollectionUtils.isEmpty(approveList)) {
                ZwjsxxDto zwjsxxDto = new ZwjsxxDto();
                List<ZwjsxxDto> dtoList = dao.getDtoList(zwjsxxDto);
                if (!CollectionUtils.isEmpty(dtoList)) {
                    Iterator<String> iterator = approveList.iterator();
                    while (iterator.hasNext()) {
                        String next = iterator.next();
                        for (ZwjsxxDto dto : dtoList) {
                            if (next.equals(dto.getDdslid())) {
                                //去除数据里有的
                                iterator.remove();
                            }
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(approveList)) {
                    List<ZwjsxxDto> addZwjsDtos = new ArrayList<>();
                    for (String processInstanceId : approveList) {
                        GetProcessInstanceResponseBodyResult approveInfo = talkUtil.getApproveInfo(token, processInstanceId);
                        //审批完成的
                        if (ApproveStatusEnum.COMPLETED.code.equals(approveInfo.getStatus()) && "agree".equals(approveInfo.getResult())) {
                            //获取表单的值
                            List<GetProcessInstanceResponseBodyResultFormComponentValues> formComponentValues = approveInfo.getFormComponentValues();
                            ZwjsxxDto zwjsxxDto_t = new ZwjsxxDto();
                            zwjsxxDto_t.setZwjsid(StringUtil.generateUUID());
                            zwjsxxDto_t.setDdslid(processInstanceId);
                            zwjsxxDto_t.setDdid(approveInfo.getOriginatorUserId());
                            zwjsxxDto_t.setBm(approveInfo.getOriginatorDeptId());
                            for (GetProcessInstanceResponseBodyResultFormComponentValues formComponentValue : formComponentValues) {
                                if (StringUtil.isNotBlank(formComponentValue.getName()))
                                    switch (formComponentValue.getName()) {
                                        case "入职时间":
                                            zwjsxxDto_t.setRzsj(formComponentValue.getValue());
                                            break;
                                        case "原职位":
                                            zwjsxxDto_t.setYzw(formComponentValue.getValue());
                                            break;
                                        case "原职位月薪（税前）":
                                            zwjsxxDto_t.setYzwyx(formComponentValue.getValue());
                                            break;
                                        case "晋升职位":
                                            zwjsxxDto_t.setJszw(formComponentValue.getValue());
                                            break;
                                        case "晋升后月薪（税前）":
                                            zwjsxxDto_t.setJshyx(formComponentValue.getValue());
                                            break;
                                        case "生效时间":
                                            zwjsxxDto_t.setSxsj(formComponentValue.getValue());
                                            break;
                                    }
                            }
                            zwjsxxDto_t.setSptgsj(StringUtil.isNotBlank(approveInfo.getFinishTime()) ? approveInfo.getFinishTime().substring(0, 10) : null);
                            zwjsxxDto_t.setWbcxid(wbcxid);
                            addZwjsDtos.add(zwjsxxDto_t);
                        }
                    }
                    if (!CollectionUtils.isEmpty(addZwjsDtos)) {
                        boolean isSuccess = dao.insertZwjsDtos(addZwjsDtos);
                        if (!isSuccess) {
                            throw new BusinessException("msg", "同步职位晋升的钉钉审批数据失败！");
                        }
                    }
                }
            }
        }
    }
}
