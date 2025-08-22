package com.matridx.igams.experiment.service.impl;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.experiment.dao.entities.ZdhJkcsDto;
import com.matridx.igams.experiment.dao.entities.ZdhYbxxDto;
import com.matridx.igams.experiment.dao.entities.ZdhYbxxModel;
import com.matridx.igams.experiment.dao.post.IZdhYbxxDao;
import com.matridx.igams.experiment.service.svcinterface.IZdhJkcsService;
import com.matridx.igams.experiment.service.svcinterface.IZdhYbxxService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author : 郭祥杰
 * @date :
 */
@Service
public class ZdhYbxxServiceImpl extends BaseBasicServiceImpl<ZdhYbxxDto, ZdhYbxxModel, IZdhYbxxDao> implements IZdhYbxxService {
    private final Logger log = LoggerFactory.getLogger(ZdhYbxxServiceImpl.class);
    @Autowired
    private IZdhJkcsService jkcsService;

    /**
     * @Description: 样本状态修改
     * @param jsonString
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/7 16:08
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean statusReporting(String jsonString) throws BusinessException {
        boolean result = false;
        List<ZdhYbxxDto> experimentList =  JSON.parseArray(jsonString, ZdhYbxxDto.class);
        if(experimentList==null || experimentList.isEmpty()){
            log.error("experimentList为空!");
            return false;
        }
        List<ZdhYbxxDto> addYbxxDtoList = new ArrayList<>();
        List<ZdhYbxxDto> modYbxxDtoList = new ArrayList<>();
        List<ZdhYbxxDto> addYbmxDtoList = new ArrayList<>();
        List<ZdhYbxxDto> modYbmxDtoList = new ArrayList<>();
        List<ZdhYbxxDto> addYbsjDtoList = new ArrayList<>();
        List<ZdhYbxxDto> ybbmxLcList = new ArrayList<>();
        List<ZdhJkcsDto> jkcsDtoList = new ArrayList<>();
        //根据标本编号，状态和试管架编号查询是否存在相同的样本编号
        List<ZdhYbxxDto> ybxxDtos = dao.queryByYbxxDto(experimentList);
        if(ybxxDtos!=null && !ybxxDtos.isEmpty()){
            for (ZdhYbxxDto ybxxDto : experimentList) {
                boolean flg = false;
                for (ZdhYbxxDto dto : ybxxDtos) {
                    //取 experimentList 中存在，ybxxDtos中存在的数据，做修改
                    if (ybxxDto.getBbbm().equals(dto.getBbbm()) && ybxxDto.getYbjbh().equals(dto.getYbjbh())) {
                        ybxxDto.setYbxxid(dto.getYbxxid());
                        //如果存在ybmxid，说明数据库存在这个流程，直接做修改操作
                        if (StringUtil.isNotBlank(dto.getYbmxid())) {
                            ybxxDto.setYbmxid(dto.getYbmxid());
                            modYbmxDtoList.add(ybxxDto);
                            //样本试剂id存在，说明数据库存在样本试剂信息，存入ybsjid做标识。
                            if (StringUtil.isNotBlank(dto.getYbsjid())) {
                                ybxxDto.setYbsjid(dto.getYbsjid());
                            }
                        }else if(StringUtil.isNotBlank(ybxxDto.getLcmc())){
                            flg = true;
                            ybxxDto.setYbmxid(StringUtil.generateUUID());
                        }
                        modYbxxDtoList.add(ybxxDto);
                        insertJkcsDtoList(jkcsDtoList,jsonString,ybxxDto.getYbxxid());
                        break;
                    }
                }
                if (StringUtil.isBlank(ybxxDto.getYbxxid())) {
                    ybxxDto.setYbxxid(StringUtil.generateUUID());
                    addYbxxDtoList.add(ybxxDto);
                    insertJkcsDtoList(jkcsDtoList,jsonString,ybxxDto.getYbxxid());
                }
                if (flg && StringUtil.isNotBlank(ybxxDto.getLcmc())) {
                    addYbmxDtoList.add(ybxxDto);
                    if (StringUtil.isBlank(ybxxDto.getYbsjid()) && StringUtil.isNotBlank(ybxxDto.getSjmc())) {
                        ybxxDto.setYbsjid(StringUtil.generateUUID());
                        addYbsjDtoList.add(ybxxDto);
                    }
                }
                //取zlc或者lc不为null的数据，判断数据库是否存在lc
                if(StringUtil.isNotBlank(ybxxDto.getZlcmc()) || StringUtil.isNotBlank(ybxxDto.getLcmc())){
                    ybbmxLcList.add(ybxxDto);
                }
            }
        }else{
            for (ZdhYbxxDto ybxxDto : experimentList) {
                ybxxDto.setYbxxid(StringUtil.generateUUID());
                if (StringUtil.isNotBlank(ybxxDto.getLcmc())) {
                    ybxxDto.setYbmxid(StringUtil.generateUUID());
                    addYbmxDtoList.add(ybxxDto);
                }
                addYbxxDtoList.add(ybxxDto);
                insertJkcsDtoList(jkcsDtoList,jsonString,ybxxDto.getYbxxid());
                if (StringUtil.isNotBlank(ybxxDto.getSjmc())) {
                    ybxxDto.setYbsjid(StringUtil.generateUUID());
                    addYbsjDtoList.add(ybxxDto);
                }
            }
        }
        List<ZdhYbxxDto> ybxxDtoList = new ArrayList<>();
        if(!ybbmxLcList.isEmpty()){
            ybxxDtoList = dao.queryYbmxDtoList(ybbmxLcList);
        }
        for (ZdhYbxxDto ybxx: experimentList) {
            boolean lcflg = false;
            if(!ybxxDtoList.isEmpty()){
                for (ZdhYbxxDto ybxxDto: ybxxDtoList) {
                    //如果流程已经存在，判断参数流程结束时间是否为null,如果不为null,做更新，如果为null,判断子流程结束时间是否为null,如果不为null,做更新
                    if(ybxx.getYbxxid().equals(ybxxDto.getYbxxid()) && ybxx.getLcmc().equals(ybxxDto.getLc())){
                        if(StringUtil.isNotBlank(ybxx.getLcjssj())){
                            ybxxDto.setLcjssj(ybxx.getLcjssj());
                        }
                        if(StringUtil.isNotBlank(ybxx.getZlcjssj())){
                            ybxxDto.setLcjssj(ybxx.getZlcjssj());
                        }
                        ybxxDto.setLcmc(ybxxDto.getLc());
                        ybxxDto.setZlcmc(ybxxDto.getZlc());
                        modYbmxDtoList.add(ybxxDto);
                        lcflg =true;
                        break;
                    }
                }
            }
            if(!lcflg && StringUtil.isNotBlank(ybxx.getLcmc())){
                ZdhYbxxDto ybxxDto = new ZdhYbxxDto();
                ybxxDto.setYbmxid(StringUtil.generateUUID());
                ybxxDto.setLcmc(ybxx.getLcmc());
                ybxxDto.setYbxxid(ybxx.getYbxxid());
                ybxxDto.setLckssj(StringUtil.isNotBlank(ybxx.getLckssj())?ybxx.getLckssj():ybxx.getZlckssj());
                ybxxDto.setLcjssj(StringUtil.isNotBlank(ybxx.getLcjssj())?ybxx.getLcjssj():ybxx.getZlcjssj());
                addYbmxDtoList.add(ybxxDto);
            }
        }

        if(!jkcsDtoList.isEmpty()){
            result = jkcsService.insertJkcsList(jkcsDtoList);
            if(!result){
                log.error("新增接口参数失败!");
                throw new BusinessException("msg", "新增接口参数失败!");
            }
        }
        if(!addYbxxDtoList.isEmpty()){
            result = dao.insertYbxxDtoList(addYbxxDtoList);
            if(!result){
                log.error("新增样本信息失败!");
                throw new BusinessException("msg", "新增样本信息失败!");
            }
        }
        if(!modYbxxDtoList.isEmpty()){
            result = dao.updateYbxxDtoList(modYbxxDtoList);
            if(!result){
                log.error("修改样本信息失败!");
                throw new BusinessException("msg", "修改样本信息失败!");
            }
        }
        if(!addYbmxDtoList.isEmpty()){
            //根据流程名称，样本信息id和子流程名称去重
            List<ZdhYbxxDto> ybxxList = addYbmxDtoList.stream()
                    .collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                            new TreeSet<>(Comparator.comparing(o ->
                                    (o.getYbxxid()+o.getLcmc()+(StringUtil.isNotBlank(o.getZlcmc())?o.getZlcmc():"ZLC"))))), ArrayList::new));
            result = dao.insertYbmxDtoList(ybxxList);
            if(!result){
                log.error("新增样本明细信息失败!");
                throw new BusinessException("msg", "新增样本明细信息失败!");
            }
        }
        if(!modYbmxDtoList.isEmpty()){
            result = dao.updateYbmxDtoList(modYbmxDtoList);
            if(!result){
                log.error("修改样本明细信息失败!");
                throw new BusinessException("msg", "修改样本明细信息失败!");
            }
        }
        if(!addYbsjDtoList.isEmpty()){
            result = dao.insertYbsjxxDtoList(addYbsjDtoList);
            if(!result){
                log.error("修改样本试剂信息失败!");
                throw new BusinessException("msg", "修改样本试剂信息失败!");
            }
        }
        return result;
    }

    private void insertJkcsDtoList(List<ZdhJkcsDto> jkcsDtoList,String jsonString,String ybxxid){
        ZdhJkcsDto zdhJkcsDto = new ZdhJkcsDto();
        zdhJkcsDto.setCs(jsonString);
        zdhJkcsDto.setYbxxid(ybxxid);
        zdhJkcsDto.setJkcsid(StringUtil.generateUUID());
        jkcsDtoList.add(zdhJkcsDto);
    }
}
