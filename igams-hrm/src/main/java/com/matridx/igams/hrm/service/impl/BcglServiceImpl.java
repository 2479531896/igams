package com.matridx.igams.hrm.service.impl;

import com.dingtalk.api.response.OapiAttendanceShiftQueryResponse;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.hrm.dao.entities.BcglDto;
import com.matridx.igams.hrm.dao.entities.BcglModel;
import com.matridx.igams.hrm.dao.post.IBcglDao;
import com.matridx.igams.hrm.service.svcinterface.IBcglService;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class BcglServiceImpl extends BaseBasicServiceImpl<BcglDto, BcglModel, IBcglDao> implements IBcglService {
    @Autowired
    DingTalkUtil dingTalkUtil;
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean synchronousSavescheduling(User user) {
        String token = dingTalkUtil.getToken(user.getWbcxid());
        List<BcglDto> bcglDtos=new ArrayList<>();
        BcglDto bcglDto_t=new BcglDto();
        List<String> bcids=dingTalkUtil.getSchedulingAbstracts(token,user.getDdid());
        List<BcglDto> bcglDtoList=dao.getBcglDtoList(bcglDto_t);
        for (String bcid : bcids) {
            OapiAttendanceShiftQueryResponse.TopShiftVo schedulingDetails = dingTalkUtil.getSchedulingDetails(token, user.getDdid(), bcid);
            if ("N".equals(schedulingDetails.getShiftSetting().getIsDeleted())) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                BcglDto bcglDto = new BcglDto();
                bcglDto.setBcid(String.valueOf(schedulingDetails.getId()));
                bcglDto.setBcmc(schedulingDetails.getName());
                List<OapiAttendanceShiftQueryResponse.TopSectionVo> sections = schedulingDetails.getSections();
                List<OapiAttendanceShiftQueryResponse.TopPunchVo> punches = sections.get(0).getPunches();
                for (OapiAttendanceShiftQueryResponse.TopPunchVo punch : punches) {
                    if ("OnDuty".equals(punch.getCheckType())) {
                        bcglDto.setSbdksj(simpleDateFormat.format(punch.getCheckTime()));
                    } else if ("OffDuty".equals(punch.getCheckType())) {
                        bcglDto.setXbdksj(simpleDateFormat.format(punch.getCheckTime()));
                    }
                }
                bcglDtos.add(bcglDto);
            }
        }
        List<String> delSchedulingList=new ArrayList<>();
        List<BcglDto> modSchedulingList=new ArrayList<>();
        List<BcglDto> addSchedulingList=new ArrayList<>();
        Iterator<BcglDto> iterator = bcglDtoList.iterator();
        while (iterator.hasNext()){
            BcglDto next = iterator.next();
            Iterator<BcglDto> iterator_t = bcglDtos.iterator();
            while (iterator_t.hasNext()){
                BcglDto next_t = iterator_t.next();
                if (next.getBcid().equals(next_t.getBcid())){
                    if (!next.getSbdksj().equals(next_t.getSbdksj())||!next.getXbdksj().equals(next_t.getXbdksj())){
                        delSchedulingList.add(next.getBcglid());
                        if (StringUtil.isNotBlank(next.getBtglid())){
                            next_t.setBtglid(next.getBtglid());
                        }
                        next_t.setDzje(next.getDzje());
                        next_t.setBzsj(next.getBzsj());
                        next_t.setBzsc(next.getBzsc());
                        next_t.setBzje(next.getBzje());
                        next_t.setDzjg(next.getDzjg());
                        next_t.setDzkssj(next.getDzkssj());
                        next_t.setFdje(next.getFdje());
                        addSchedulingList.add(next_t);
                    }else {
                        if (!next.getBcmc().equals(next_t.getBcmc())){
                            next_t.setBcglid(next.getBcglid());
                            modSchedulingList.add(next_t);
                        }
                    }
                    iterator.remove();
                    iterator_t.remove();
                    break;
                }
            }

        }
        if (!CollectionUtils.isEmpty(bcglDtoList)){
            for (BcglDto bcglDto:bcglDtoList){
                delSchedulingList.add(bcglDto.getBcglid());
            }
        }
        BcglDto bcglDto=new BcglDto();
        if (!CollectionUtils.isEmpty(delSchedulingList)){
            bcglDto.setIds(delSchedulingList);
            bcglDto.setScry(user.getYhid());
            delete(bcglDto);
        }
        if (!CollectionUtils.isEmpty(modSchedulingList)){
            for (BcglDto bcglDto1:modSchedulingList){
                bcglDto1.setXgry(user.getYhid());
            }
            dao.updateList(modSchedulingList);
        }
        if (!CollectionUtils.isEmpty(bcglDtos)){
            addSchedulingList.addAll(bcglDtos);
        }
        if (!CollectionUtils.isEmpty(addSchedulingList)){
            for (BcglDto bcglDto1:addSchedulingList){
                bcglDto1.setBcglid(StringUtil.generateUUID());
                bcglDto1.setLrry(user.getYhid());
            }
            dao.insertList(addSchedulingList);
        }
        return true;
    }

    @Override
    public BcglDto getBcByCqsj(String cqsj) {
        return dao.getBcByCqsj(cqsj);
    }
}
