package com.matridx.igams.common.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.LbzdszDto;
import com.matridx.igams.common.dao.entities.TyszDto;
import com.matridx.igams.common.dao.entities.TyszModel;
import com.matridx.igams.common.dao.entities.TyszmxDto;
import com.matridx.igams.common.dao.post.ITyszDao;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.ILbzdszService;
import com.matridx.igams.common.service.svcinterface.ITyszService;
import com.matridx.igams.common.service.svcinterface.ITyszmxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TyszServiceImpl extends BaseBasicServiceImpl<TyszDto, TyszModel, ITyszDao> implements ITyszService {
    @Autowired
    private ITyszmxService tyszmxService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ILbzdszService lbzdszService;


    @Override
    public List<TyszDto> getMenuList(TyszDto tyszDto) {
        return dao.getMenuList(tyszDto);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean addSaveGeneralSetting(TyszDto tyszDto) {
        tyszDto.setTyszid(StringUtil.generateUUID());
        int insert = dao.insert(tyszDto);
        if(insert<1){
            return false;
        }
        List<TyszmxDto> list = JSON.parseArray(tyszDto.getSzmx_json(), TyszmxDto.class);
        List<LbzdszDto> lbzdszDtoList =new ArrayList<>();
        TyszDto _tyszDto=dao.getDto(tyszDto);
        if(list!=null && !list.isEmpty()){
            for(TyszmxDto tyszmxDto:list){
                tyszmxDto.setTyszmxid(StringUtil.generateUUID());
                tyszmxDto.setTyszid(tyszDto.getTyszid());
                tyszmxDto.setLrry(tyszDto.getLrry());
                if("2".equals(tyszmxDto.getLx())){
                    LbzdszDto lbzdszDto =new LbzdszDto();
                    lbzdszDto.setYwid(tyszDto.getEjzyid());
                    lbzdszDto.setXszd(tyszmxDto.getNr());
                    lbzdszDto.setXszdmc(tyszmxDto.getBt());
                    lbzdszDto.setSqlzd(tyszmxDto.getNr());
                    lbzdszDto.setZdsm(tyszmxDto.getBt());
                    lbzdszDto.setMrxs(tyszmxDto.getMrxs());
                    lbzdszDto.setSqlzd(tyszmxDto.getSqlzd());
                    lbzdszDto.setPxzd(tyszmxDto.getPxzd());
                    lbzdszDto.setXsxx(tyszmxDto.getSx());
                    lbzdszDto.setJz(tyszmxDto.getJz());
                    lbzdszDto.setYwmc(_tyszDto.getEjzybt());
                    lbzdszDtoList.add(lbzdszDto);
                }
            }
            boolean insertList = tyszmxService.insertList(list);
            lbzdszService.insertByLbcxszlist(lbzdszDtoList);
            if(!insertList){
                return false;
            }
        }
        redisUtil.hset("GeneralSetting", tyszDto.getEjzyid(), JSONObject.toJSONString(list) ,-1);
        return true;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean modSaveGeneralSetting(TyszDto tyszDto) {
        int update = dao.update(tyszDto);
        if(update<1){
            return false;
        }
        TyszmxDto tyszmxDto=new TyszmxDto();
        tyszmxDto.setTyszid(tyszDto.getTyszid());
        tyszmxDto.setScry(tyszDto.getXgry());
        //删除scbj=1的废弃数据
        tyszmxService.delAbandonedData(tyszmxDto);
        //先删除后新增
        tyszmxService.delete(tyszmxDto);
        LbzdszDto lbzdszDto_t=new LbzdszDto();
        lbzdszDto_t.setYwid(tyszDto.getEjzyid());
        lbzdszService.deleteLbzdszbyywid(lbzdszDto_t);
        List<TyszmxDto> list = JSON.parseArray(tyszDto.getSzmx_json(), TyszmxDto.class);
        List<LbzdszDto> lbzdszDtoList =new ArrayList<>();
        tyszDto.setVerifyFlag(null);
        TyszDto _tyszDto=dao.getDto(tyszDto);
        if(list!= null && !list.isEmpty()){
            for(TyszmxDto dto:list){
                dto.setTyszmxid(StringUtil.generateUUID());
                dto.setTyszid(tyszDto.getTyszid());
                dto.setLrry(tyszDto.getXgry());
                if("2".equals(dto.getLx())){
                    LbzdszDto lbzdszDto =new LbzdszDto();
                    lbzdszDto.setYwid(tyszDto.getEjzyid());
                    lbzdszDto.setXszd(dto.getNr());
                    lbzdszDto.setXszdmc(dto.getBt());
                    lbzdszDto.setSqlzd(dto.getNr());
                    lbzdszDto.setZdsm(dto.getBt());
                    lbzdszDto.setXsxx(dto.getSx());
                    lbzdszDto.setJz(dto.getJz());
                    lbzdszDto.setYwmc(_tyszDto.getEjzybt());
                    lbzdszDto.setMrxs(dto.getMrxs());
                    lbzdszDto.setSqlzd(dto.getSqlzd());
                    lbzdszDto.setPxzd(dto.getPxzd());
                    lbzdszDtoList.add(lbzdszDto);
                }
            }
            boolean insertList = tyszmxService.insertList(list);
            lbzdszService.insertByLbcxszlist(lbzdszDtoList);
            if(!insertList){
                return false;
            }
        }
        redisUtil.hdel("GeneralSetting",tyszDto.getEjzyid());
        redisUtil.hset("GeneralSetting", tyszDto.getEjzyid(), JSONObject.toJSONString(list) ,-1);
        return true;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean delGeneralSetting(TyszDto tyszDto) {
        int delete = dao.delete(tyszDto);
        if(delete<1){
            return false;
        }
        TyszmxDto tyszmxDto=new TyszmxDto();
        tyszmxDto.setIds(tyszDto.getIds());
        tyszmxDto.setScry(tyszDto.getScry());
        boolean result = tyszmxService.delete(tyszmxDto);
        if(!result){
            return false;
        }
        List<String> ejzyids = tyszDto.getEjzyids();
        if(ejzyids!= null && !ejzyids.isEmpty()){
            for(String s:ejzyids){
                redisUtil.hdel("GeneralSetting",s);
            }
        }
        return true;
    }

    @Override
    public List<TyszDto> getButtonList(TyszDto tyszDto) {
        return dao.getButtonList(tyszDto);
    }
}
