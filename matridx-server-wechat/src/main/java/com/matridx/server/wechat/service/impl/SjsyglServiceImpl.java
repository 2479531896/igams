package com.matridx.server.wechat.service.impl;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.enums.DetectionTypeEnum;
import com.matridx.igams.common.service.BaseBasicServiceImpl;

import com.matridx.server.wechat.dao.entities.*;
import com.matridx.server.wechat.dao.post.ISjsyglDao;
import com.matridx.server.wechat.service.svcinterface.ISjjcxmService;
import com.matridx.server.wechat.service.svcinterface.ISjsyglService;
import com.matridx.server.wechat.service.svcinterface.IXmsyglService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SjsyglServiceImpl extends BaseBasicServiceImpl<SjsyglDto, SjsyglModel, ISjsyglDao> implements ISjsyglService {

    @Autowired
    private ISjjcxmService sjjcxmService;
    @Autowired
    IXmsyglService xmsyglService;
    private Logger log = LoggerFactory.getLogger(SjsyglServiceImpl.class);

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public Boolean insertInfo(SjsyglDto sjsyglDto) {
        List<SjsyglDto> dtos = dao.getInsertInfo(sjsyglDto);
        List<SjsyglDto> insertList =  new ArrayList<>();
        for (SjsyglDto dto : dtos) {
            if(StringUtil.isBlank(dto.getKzcs4())){
                dto.setSyglid(StringUtil.generateUUID());
                dto.setYwlx(sjsyglDto.getYwlx());
                insertList.add(dto);
            }
        }
        return dao.insertList(insertList) != 0;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public Boolean updateList(List<SjsyglDto> list) {
        return dao.updateList(list)!=0;
    }
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public Boolean delInfo(SjsyglDto sjsyglDto) {
        return dao.delInfo(sjsyglDto);
    }
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public Boolean insertList(List<SjsyglDto> list) {
        return dao.insertList(list) != 0;
    }

    @Override
    public Boolean adjustUpdateList(List<SjsyglDto> list) {
        return dao.adjustUpdateList(list);
    }

    /**
     * 获取插入送检实验管理的数据
     */
    @Override
    public List<SjsyglDto> getDetectionInfo(SjxxDto sjxxDto, FjsqDto fjsqDto, String lx){
        SjsyglDto sjsyglDto=new SjsyglDto();
        sjsyglDto.setYwlx(lx);
        String sjqfdm="";
        sjsyglDto.setYblxdm(sjxxDto.getYblxdm());
        sjsyglDto.setJcdwmc(sjxxDto.getJcdwmc());
        sjsyglDto.setSjid(sjxxDto.getSjid());
        if(StringUtil.isNotBlank(sjxxDto.getSjqf())){
            sjqfdm=sjxxDto.getSjqf();
        }
        //Q2.0是一个限制项目，所以需要从现有检测项目中抽取Q2.0的检测项目ID列表
        List<SjsyglDto> xzIds=new ArrayList<>();
        //查询伙伴实验室限制信息
        List<SjsyglDto> sjsyglDtos = getHbDto(sjsyglDto);
        //log.error("送检区分代码为："+sjqfdm);
        if ("RESEARCH".equals(sjqfdm)){//若为科研直接插入2.5
            SjsyglDto sjsyglDto2=new SjsyglDto();
            sjsyglDto2.setJcxmcskz3("IMP_REPORT_ONCO_QINDEX_TEMEPLATE");
            sjsyglDto2.setXzlx("X");//复检的情况，默认限制类型为X
            xzIds.add(sjsyglDto2);
            SjsyglDto sjsyglDto3=new SjsyglDto();
            sjsyglDto3.setJcxmcskz3("IMP_REPORT_SEQ_TNGS_E");
            sjsyglDto3.setXzlx("X");//复检的情况，默认限制类型为X
            xzIds.add(sjsyglDto3);
        }else{
            if(!CollectionUtils.isEmpty(sjsyglDtos)) {
                for (SjsyglDto t_sjsyglDto : sjsyglDtos) {
                    if(t_sjsyglDto!=null && StringUtil.isNotBlank(t_sjsyglDto.getHbid())){
                        //如果有数据则说明有限制，再去判断送检或者复检检测项目是否是2.0项目（即cskz3包含IMP_REPORT_ONCO_QINDEX_TEMEPLATE）
                        if("DETECT_SJ".equals(lx)){
                            xzIds.add(t_sjsyglDto);
                        }
                    }
                }
            }
        }

        //log.error("限制Jcxmids："+xzIds);
        //查询已经存在的送检实验管理的数据，确认是否为限制的Q2.0项目，如果不是，则确认是否为X项目，就是需要特殊处理的3.0项目。
        List<SjsyglDto> insertInfo = dao.getInsertInfo(sjsyglDto);
        List<SjsyglDto> list =  new ArrayList<>();
        if (null != insertInfo && insertInfo.size()>0){
            List<SjsyglDto> insertList =  new ArrayList<>();
            //先将不在限制里的项目放进list
            for(SjsyglDto dto:insertInfo){
                if(!CollectionUtils.isEmpty(xzIds)){
                    boolean isAdd=true;
                    for(SjsyglDto t_sjsyglDto:xzIds){
                        if(StringUtil.isNotBlank(dto.getJcxmcskz3())&&dto.getJcxmcskz3().equals(t_sjsyglDto.getJcxmcskz3())){
                            isAdd=false;
                        }
                    }
                    if(StringUtil.isBlank(dto.getKzcs6()) && isAdd){
                        insertList.add(dto);
                    }
                }else{
                    if(StringUtil.isBlank(dto.getKzcs6())){
                        insertList.add(dto);
                    }
                }
            }
            if(!CollectionUtils.isEmpty(xzIds)){
                for(SjsyglDto t_sjsyglDto:xzIds){
                    boolean isFind=false;
                    for(SjsyglDto dto:insertInfo){
                        //如果项目相同，且项目限制相同，则插入
                        if(StringUtil.isNotBlank(t_sjsyglDto.getJcxmcskz3()) && t_sjsyglDto.getJcxmcskz3().equals(dto.getJcxmcskz3())) {
                            if (StringUtil.isNotBlank(t_sjsyglDto.getXzlx()) && StringUtil.isNotBlank(dto.getKzcs6())) {
                                if (t_sjsyglDto.getXzlx().equals(dto.getKzcs6())){
                                    isFind = true;
                                    insertList.add(dto);
                                }
                            }
                        }
                    }
                    if(!isFind){//若有项目限制信息，但是没有符合的。按不带项目限制的走
                        for(SjsyglDto dto:insertInfo){
                            if(StringUtil.isNotBlank(dto.getJcxmcskz3()) && dto.getJcxmcskz3().equals(t_sjsyglDto.getJcxmcskz3())
                                    && StringUtil.isBlank(dto.getKzcs6())){
                                insertList.add(dto);
                            }
                        }
                    }
                }
            }

            //如果查出来的不为空，则拿取第一条的kzcs4以及kzcs5去查找
            if(insertList!=null&&insertList.size() > 0){
                SjsyglDto sjsyglDto_t=insertList.get(0);
                log.error("送检实验管理数据——0：dyid:"+sjsyglDto_t.getDyid()+ " jclxid:" + sjsyglDto_t.getJclxid()+ " cs4:" + sjsyglDto_t.getKzcs4()
                        + " cs5:" + sjsyglDto_t.getKzcs5()+ " cs6:" + sjsyglDto_t.getKzcs6()+ " wksxbm:" + sjsyglDto_t.getWksxbm()+ " 个数:" + insertList.size());
                for(SjsyglDto dto:insertList){
                    if(StringUtil.isBlank(dto.getKzcs5())){
                        dto.setKzcs5("");
                    }
                    if(StringUtil.isBlank(dto.getKzcs4())){
                        dto.setKzcs4("");
                    }
                    if(StringUtil.isBlank(dto.getJcxmid())){
                        dto.setJcxmid("");
                    }
                    if(StringUtil.isBlank(dto.getJczxmid())){
                        dto.setJczxmid("");
                    }
                    //先判断检测项目以及检测子项目是否相同
                    if(sjsyglDto_t.getJcxmid().equals(dto.getJcxmid())&&sjsyglDto_t.getJczxmid().equals(dto.getJczxmid())){
                        //判断kzcs4与kzcs5是否相同
                        if(sjsyglDto_t.getKzcs5().equals(dto.getKzcs5())&&sjsyglDto_t.getKzcs4().equals(dto.getKzcs4())){
                            list.add(dto);
                        }
                    }else{
                        //出现检测项目以及检测子项目不相同的时候，说明是新的检测项目，把sjsyglDto_t换成新检测项目的的第一个然后再依次遍历判断
                        list.add(dto);
                        sjsyglDto_t=dto;
                    }
                }
            }
        }
        return list;
    }

    /**
     * 直接删除
     * @param sjsyglDto
     * @return
     */
    public Boolean deleteInfo(SjsyglDto sjsyglDto){
        return dao.deleteInfo(sjsyglDto);
    }

    /**
     * 获取伙伴信息
     * @param sjsyglDto
     * @return
     */
    public List<SjsyglDto> getHbDto(SjsyglDto sjsyglDto){
        return dao.getHbDto(sjsyglDto);
    }

    /**
     * 批量修改整张表数据
     * @param
     * @return
     */
    public int modAllList(List<SjsyglDto> list){
        return dao.modAllList(list);
    }

    /**
     * Rabbit同步
     * @param list
     * @return
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public void syncRabbitMsg(List<SjsyglDto> list){
        if(list!=null&&list.size()>0){
            SjsyglDto sjsyglDto=new SjsyglDto();
            sjsyglDto.setSjid(list.get(0).getSjid());
            sjsyglDto.setYwlx(DetectionTypeEnum.DETECT_SJ.getCode());
            List<SjsyglModel> insertInfo = dao.getModelList(sjsyglDto);
            dao.deleteInfo(sjsyglDto);
            sjsyglDto.setScry(StringUtil.isNotBlank(list.get(0).getXgry())?list.get(0).getXgry():list.get(0).getLrry());
            dao.delBySjid(sjsyglDto);
            List<SjsyglDto> insertList =  new ArrayList<>();
            List<SjsyglDto> updateList=new ArrayList<>();
            if(insertInfo!=null && insertInfo.size()>0){
                for (SjsyglDto dto : list) {
                    boolean dbIsExistSyglid = insertInfo.stream ().anyMatch (item->item.getSyglid().equals (dto.getSyglid()));
                    if (dbIsExistSyglid){
                        updateList.add(dto);
                    }else{
                        insertList.add(dto);
                    }
                }
            }else {
                insertList.addAll(list);
            }
            if(updateList!=null&&updateList.size() > 0){
                dao.modAllList(updateList);
            }
            if(insertList!=null&&insertList.size() > 0){
                dao.insertList(insertList);
            }
        }
    }

    /**
     * 获取销售展示数据
     * @param sjid
     * @return
     */
    public List<SjsyglDto> getViewDetectData(String sjid){
        return dao.getViewDetectData(sjid);
    }
    /**
     * 样本实验调整功能
     * @param sjsyglDto
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean adjustSaveSjxx(SjsyglDto sjsyglDto){
        List<SjsyglDto> list=(List<SjsyglDto>) JSON.parseArray(sjsyglDto.getJson(),SjsyglDto.class);
        if(list!=null&&list.size()>0){
            List<SjsyglDto> insertList=new ArrayList<>();
            List<SjsyglDto> insertSjList=new ArrayList<>();
            List<SjsyglDto> updateSjList=new ArrayList<>();
            List<XmsyglDto> updateXmList=new ArrayList<>();
            for(SjsyglDto sjsyglDto_t:list){
                if(StringUtil.isNotBlank(sjsyglDto_t.getSyglid())){
                        sjsyglDto_t.setXgry(sjsyglDto.getXgry());
                        updateSjList.add(sjsyglDto_t);
                }else{
                    insertSjList.add(sjsyglDto_t);
                }
                break;
            }
            if(updateSjList!=null&&updateSjList.size()>0){
                Boolean adjustUpdateList = dao.adjustUpdateList(updateSjList);
                if(!adjustUpdateList){
                    return false;
                }
            }
            if(insertSjList!=null&&insertSjList.size()>0){
                Map<String, List<SjsyglDto>> map = insertSjList.stream().collect(Collectors.groupingBy(SjsyglDto::getJcdw));
                if (!CollectionUtils.isEmpty(map)){
                    Iterator<Map.Entry<String, List<SjsyglDto>>> entries = map.entrySet().iterator();
                    while (entries.hasNext()) {
                        Map.Entry<String,  List<SjsyglDto>> entry = entries.next();
                        List<SjsyglDto> resultModelList = entry.getValue();
                        String jcdw = entry.getKey();
                        if (!jcdw.isBlank() && !CollectionUtils.isEmpty(resultModelList)){
                            //在通过检测类型分组
                            Map<String, List<SjsyglDto>> listMap = resultModelList.stream().collect(Collectors.groupingBy(SjsyglDto::getJclxid));
                            if (!CollectionUtils.isEmpty(listMap)){
                                Iterator<Map.Entry<String, List<SjsyglDto>>> entryIterator = listMap.entrySet().iterator();
                                while (entryIterator.hasNext()) {
                                    Map.Entry<String,  List<SjsyglDto>> stringListEntry = entryIterator.next();
                                    String jclx = stringListEntry.getKey();
                                    List<SjsyglDto> sjsyglDtoList = stringListEntry.getValue();
                                    if (!jclx.isBlank() && !CollectionUtils.isEmpty(sjsyglDtoList)){
                                        String syglid = StringUtil.generateUUID();
                                        for(SjsyglDto dto:sjsyglDtoList){
                                            dto.setSyglid(syglid);
                                            dto.setLrry(sjsyglDto.getXgry());
                                            for(SjsyglDto sjsyglDto_t:list){
                                                if(sjsyglDto_t.getSyglid().equals(dto.getSyglid())){
                                                    sjsyglDto_t.setSyglid(syglid);
                                                    break;
                                                }
                                            }
                                        }
                                        insertList.add(sjsyglDtoList.get(0));
                                    }
                                }
                            }
                        }
                    }
                }
                int i = dao.insertList(insertList);
                if(i==0){
                    return false;
                }
            }
            for(SjsyglDto sjsyglDto_t:list){
                XmsyglDto xmsyglDto=new XmsyglDto();
                xmsyglDto.setXmsyglid(sjsyglDto_t.getXmsyglid());
                xmsyglDto.setSyglid(sjsyglDto_t.getSyglid());
                xmsyglDto.setXgry(sjsyglDto.getXgry());
                updateXmList.add(xmsyglDto);
            }
            sjsyglDto.setSjid(list.get(0).getSjid());
            if(updateXmList!=null&&updateXmList.size()>0){
                boolean updateList = xmsyglService.updateList(updateXmList);
                if(!updateList){
                    return false;
                }
            }
            sjsyglDto.setScry(sjsyglDto.getXgry());
            dao.updateScbj(sjsyglDto);

            boolean updateXmmc = dao.updateXmmc(sjsyglDto);
            if(!updateXmmc){
                return false;
            }
        }
        return true;
    }

    @Override
    public void updateJsInfo(SjsyglDto sjsyglDto) {
        dao.updateJsInfo(sjsyglDto);
    }
}
