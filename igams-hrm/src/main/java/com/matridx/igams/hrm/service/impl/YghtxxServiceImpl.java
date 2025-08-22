package com.matridx.igams.hrm.service.impl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.enums.RabbitEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.hrm.dao.entities.YghtxxDto;
import com.matridx.igams.hrm.dao.entities.YghtxxModel;
import com.matridx.igams.hrm.dao.post.IYghtxxDao;
import com.matridx.igams.hrm.service.svcinterface.IYghtxxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class YghtxxServiceImpl extends BaseBasicServiceImpl<YghtxxDto, YghtxxModel, IYghtxxDao> implements IYghtxxService {
    @Autowired(required=false)
    private AmqpTemplate amqpTempl;
    @Value("${matridx.rabbit.flg:}")
    private String prefixFlg;
    @Value("${matridx.rabbit.preflg:}")
    private String preRabbitFlg;
    //是否发送rabbit标记     1：发送
    @Value("${matridx.rabbit.configflg:1}")
    private String configflg;
    @Autowired
    IFjcfbService fjcfbService;

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean htupholdSaveRoster(YghtxxDto yghtxxDto) throws BusinessException {
        List<YghtxxDto> yghtxxDtos = JSON.parseArray(yghtxxDto.getYghtmx_json(), YghtxxDto.class);
        List<YghtxxDto> addYghtxxs = new ArrayList<>();
        List<YghtxxDto> dtoList = dao.getDtoList(yghtxxDto);
        if (!CollectionUtils.isEmpty(yghtxxDtos)){
            Iterator<YghtxxDto> iterator = dtoList.iterator();
            //迭代循环剩下的就是删除的合同信息
            while (iterator.hasNext()){
                YghtxxDto next = iterator.next();
                for (YghtxxDto dto : yghtxxDtos) {
                    if (StringUtil.isNotBlank(dto.getYghtid())){
                        if (next.getYghtid().equals(dto.getYghtid())){
                            iterator.remove();
                        }
                    }
                }
            }
            //获取新增的员工合同信息
            for (YghtxxDto dto : yghtxxDtos) {
                if (StringUtil.isBlank(dto.getYghtid())){
                    dto.setPrefix(prefixFlg);
                    dto.setYghmcid(yghtxxDto.getYghmcid());
                    dto.setYghtid(StringUtil.generateUUID());
                    dto.setLrry(yghtxxDto.getLrry());
                    addYghtxxs.add(dto);
                }
                if (!CollectionUtils.isEmpty(dto.getFjids())) {
                    for (int j = 0; j < dto.getFjids().size(); j++) {
                        if(StringUtil.isNotBlank(dto.getFjids().get(j))) {
                            fjcfbService.save3RealFile(dto.getFjids().get(j), dto.getYghmcid(), dto.getYghtid());
                        }
                    }
                }
            }
        }
        if (!CollectionUtils.isEmpty(dtoList)){
            List<String> ids = new ArrayList<>();
            for (YghtxxDto dto : dtoList) {
                ids.add(dto.getYghtid());
            }
            yghtxxDto.setPrefix(prefixFlg);
            yghtxxDto.setScry(yghtxxDto.getLrry());
            yghtxxDto.setIds(ids);
            int del = dao.delete(yghtxxDto);
            if (del<1){
                throw new BusinessException("msg","删除员工合同信息失败！");
            }
            if("1".equals(configflg)) {
                amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate", RabbitEnum.YHT_DEL.getCode() + JSONObject.toJSONString(yghtxxDto));
            }
        }
        if (!CollectionUtils.isEmpty(addYghtxxs)){
            boolean isSuccess = dao.insertYghtxxDtos(addYghtxxs);
            if (!isSuccess){
                throw new BusinessException("msg","新增员工合同信息失败！");
            }
            if("1".equals(configflg)) {
                amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate", RabbitEnum.YHT_ADD.getCode() + JSONObject.toJSONString(addYghtxxs));
            }
        }
        return true;
    }
}
