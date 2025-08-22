package com.matridx.igams.detection.molecule.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.enums.RabbitEnum;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.detection.molecule.dao.entities.BkyyrqDto;
import com.matridx.igams.detection.molecule.dao.entities.BkyyrqModel;
import com.matridx.igams.detection.molecule.dao.post.IBkyyrqDao;
import com.matridx.igams.detection.molecule.enums.DetMQTypeEnum;
import com.matridx.igams.detection.molecule.service.svcinterface.IBkyyrqService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BkyyrqServiceImpl extends BaseBasicServiceImpl<BkyyrqDto, BkyyrqModel, IBkyyrqDao> implements IBkyyrqService{

    private final Logger log = LoggerFactory.getLogger(BkyyrqServiceImpl.class);

    @Autowired(required = false)
    private AmqpTemplate amqpTempl;
    /**
     * 新增不可预约日期信息
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public Map<String,Object> insertBkyyrqDtoList(BkyyrqDto bkyyrqDto){
        Map<String,Object> map=new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
        boolean isSuccess = false;
        //查询是否有重复的新增
        List<BkyyrqDto> bkyyrqList = getUnAppDate(bkyyrqDto);
        if (bkyyrqList.size()>0){
            map.put("status", "fail");
            map.put("message", "有重复的日期！");
            return map;
        }
        try {
            Date bkyyrqstart = sdf.parse(bkyyrqDto.getBkyyrqstart());
            Date bkyyrqend = sdf.parse(bkyyrqDto.getBkyyrqend());
            int index = (int) ((bkyyrqend.getTime() - bkyyrqstart.getTime()) / (24 * 60 * 60 * 1000));
            Calendar bkyyrq = Calendar.getInstance();
            bkyyrq.setTime(bkyyrqstart);
            ArrayList<BkyyrqDto> bkyyrqDtos = new ArrayList<>();
            for (int i = 0; i < index+1; i++) {
                //
                bkyyrq.setTime(bkyyrqstart);
                bkyyrq.add(Calendar.DATE,i);
                //遍历新增不可预约日期
                String bkyyrqid = StringUtil.generateUUID();
                BkyyrqDto bkyyDto = new BkyyrqDto();
                bkyyDto.setBkyyrqid(bkyyrqid);
                bkyyDto.setBz(bkyyrqDto.getBz());
                bkyyDto.setBkyyrq(sdf.format(bkyyrq.getTime()));
                bkyyDto.setBkyysjd(bkyyrqDto.getBkyysjd());
                bkyyDto.setLrry(bkyyrqDto.getLrry());
                bkyyDto.setLrsj(bkyyrqDto.getLrsj());
                bkyyrqDtos.add(bkyyDto);
            }
            isSuccess = dao.insertBkyyrqDtoList(bkyyrqDtos);
            if (isSuccess){
                //发送rabbti消息
                Map<String,Object> rabbitmap = new HashMap<>();
                rabbitmap.put("bkyyrqDtos",bkyyrqDtos);
                amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.YYRQ_ADD.getCode() + JSONObject.toJSONString(rabbitmap));
            }
        } catch (ParseException e) {
            log.error(e.toString());
        }
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?"保存成功！":"保存失败！");
        return map;
    }

    /**
     * 修改不可预约日期信息
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public Map<String,Object> updateBkyyrqDto(BkyyrqDto bkyyrqDto){
        Map<String,Object> map=new HashMap<>();
        boolean isSuccess;
        isSuccess = dao.updateBkyyrqDto(bkyyrqDto);
        if (isSuccess){
            //发送rabbti消息
            Map<String,Object> rabbitmap = new HashMap<>();
            rabbitmap.put("bkyyrqDto",bkyyrqDto);
            amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.YYRQ_MOD.getCode() + JSONObject.toJSONString(rabbitmap));
        }
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?"修改成功！":"修改失败！");
        return map;
    }

    /**
     * 根据id查询不可预约日期信息
     */
    public BkyyrqDto getBkyyrqDto(BkyyrqDto bkyyrqDto){
        return dao.getBkyyrqDto(bkyyrqDto);
    }

    /**
     * 删除不可预约日期信息
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean delUnAppDateDetails(BkyyrqDto bkyyrqDto){
        boolean isSuccess;
        isSuccess = dao.delUnAppDateDetails(bkyyrqDto);
        if (isSuccess){
            //发送rabbti消息
            Map<String,Object> rabbitmap = new HashMap<>();
            rabbitmap.put("bkyyrqDto",bkyyrqDto);
            amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.YYRQ_DEL.getCode() + JSONObject.toJSONString(rabbitmap));
        }
        return isSuccess;
    }

    /**
     * 根据日期范围查询不可预约日期信息
     */
    public List<BkyyrqDto> getUnAppDate(BkyyrqDto bkyyrqDto){
        return dao.getUnAppDate(bkyyrqDto);
    }
}
