package com.matridx.igams.hrm.service.impl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.enums.RabbitEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.hrm.dao.entities.YglzxxDto;
import com.matridx.igams.hrm.dao.entities.YglzxxModel;
import com.matridx.igams.hrm.dao.post.IYglzxxDao;
import com.matridx.igams.hrm.service.svcinterface.IYglzxxService;
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
public class YglzxxServiceImpl extends BaseBasicServiceImpl<YglzxxDto, YglzxxModel, IYglzxxDao> implements IYglzxxService {
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
    public boolean yglzupholdSaveRoster(YglzxxDto yglzxxDto) throws BusinessException {
        List<YglzxxDto> yglzxxDtos = JSON.parseArray(yglzxxDto.getYglzmx_json(), YglzxxDto.class);
        List<YglzxxDto> addYglzxxs = new ArrayList<>();
        List<YglzxxDto> dtoList = dao.getDtoList(yglzxxDto);
        if (!CollectionUtils.isEmpty(yglzxxDtos)){
            Iterator<YglzxxDto> iterator = dtoList.iterator();
            //迭代循环剩下的就是删除的离职信息
            while (iterator.hasNext()){
                YglzxxDto next = iterator.next();
                for (YglzxxDto dto : yglzxxDtos) {
                    if (StringUtil.isNotBlank(dto.getYglzid())){
                        if (next.getYglzid().equals(dto.getYglzid())){
                            iterator.remove();
                        }
                    }
                }
            }
            //获取新增的员工离职信息
            for (YglzxxDto dto : yglzxxDtos) {
                if (StringUtil.isBlank(dto.getYglzid())){
                    dto.setPrefix(prefixFlg);
                    dto.setYghmcid(yglzxxDto.getYghmcid());
                    dto.setYglzid(StringUtil.generateUUID());
                    dto.setLrry(yglzxxDto.getLrry());
                    addYglzxxs.add(dto);
                }
                if (!CollectionUtils.isEmpty(dto.getFjids())) {
                    for (int j = 0; j < dto.getFjids().size(); j++) {
                        if(StringUtil.isNotBlank(dto.getFjids().get(j))) {
                            fjcfbService.save3RealFile(dto.getFjids().get(j), dto.getYghmcid(), dto.getYglzid());
                        }
                    }
                }
            }
        }
        if (!CollectionUtils.isEmpty(dtoList)){
            List<String> ids = new ArrayList<>();
            for (YglzxxDto dto : dtoList) {
                ids.add(dto.getYglzid());
            }
            yglzxxDto.setPrefix(prefixFlg);
            yglzxxDto.setScry(yglzxxDto.getLrry());
            yglzxxDto.setIds(ids);
            int del = dao.delete(yglzxxDto);
            if (del<1){
                throw new BusinessException("msg","删除员工离职信息失败！");
            }
            if("1".equals(configflg)) {
                amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate", RabbitEnum.YLZ_DEL.getCode() + JSONObject.toJSONString(yglzxxDto));
            }
        }
        if (!CollectionUtils.isEmpty(addYglzxxs)){
            boolean isSuccess = dao.insertYglzxxDtos(addYglzxxs);
            if (!isSuccess){
                throw new BusinessException("msg","新增员工离职信息失败！");
            }
            if("1".equals(configflg)) {
                amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate", RabbitEnum.YLZ_ADD.getCode() + JSONObject.toJSONString(addYglzxxs));
            }
        }
        return true;
    }
}
