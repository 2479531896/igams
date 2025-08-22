package com.matridx.igams.wechat.service.impl;


import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.enums.DetectionTypeEnum;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.FjsqDto;
import com.matridx.igams.wechat.dao.entities.SjjcxmDto;
import com.matridx.igams.wechat.dao.entities.SjsyglDto;
import com.matridx.igams.wechat.dao.entities.SjsyglModel;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.XmsyglDto;
import com.matridx.igams.wechat.dao.post.IFjsqDao;
import com.matridx.igams.wechat.dao.post.ISjsyglDao;
import com.matridx.igams.wechat.dao.post.ISjxxDao;
import com.matridx.igams.wechat.service.svcinterface.ISjjcxmService;
import com.matridx.igams.wechat.service.svcinterface.ISjsyglService;
import com.matridx.igams.wechat.service.svcinterface.IXmsyglService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class SjsyglServiceImpl extends BaseBasicServiceImpl<SjsyglDto, SjsyglModel, ISjsyglDao> implements ISjsyglService {

    @Autowired
    ISjjcxmService sjjcxmService;
    @Autowired
    IFjsqDao fjsqDao;
    @Autowired
    ISjxxDao sjxxDao;
    @Autowired
    IXmsyglService xmsyglService;
    //@Autowired
    //private RedisUtil redisUtil;

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

    /**
     * 直接删除
     * @param sjsyglDto
     * @return
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public Boolean deleteInfo(SjsyglDto sjsyglDto){
        return dao.deleteInfo(sjsyglDto);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public Boolean deleteInfoAjust(SjsyglDto sjsyglDto){
        return dao.deleteInfoAjust(sjsyglDto)!=0;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public Boolean insertList(List<SjsyglDto> list) {
        return dao.insertList(list) != 0;
    }

    @Override
    public List<SjsyglDto> getInsertInfo(SjsyglDto sjsyglDto) {
        return dao.getInsertInfo(sjsyglDto);
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

    @Override
    public Boolean adjustUpdateList(List<SjsyglDto> list) {
        return dao.adjustUpdateList(list);
    }

    /**
     * 获取提取信息
     * @param sjsyglDto
     * @return
     */
    @Override
    public List<SjsyglDto> getTqInfoList(SjsyglDto sjsyglDto){
        return  dao.getTqInfoList(sjsyglDto);
    }

    /**
     * 删除多余数据
     * @param sjsyglDto
     * @return
     */
    @Override
    public Boolean updateQyrq(SjsyglDto sjsyglDto){
        return dao.updateQyrq(sjsyglDto);
    }

    /**
     * 获取文库信息
     * @param sjsyglDto
     * @return
     */
    public List<SjsyglDto> getWkInfoList(SjsyglDto sjsyglDto){
        return dao.getWkInfoList(sjsyglDto);
    }

    /**
     * 根据ids顺序获取数据
     * @param sjsyglDto
     * @return
     */
    @Override
    public List<SjsyglDto> getDtoListByTempSyglids(SjsyglDto sjsyglDto) {
        return dao.getDtoListByTempSyglids(sjsyglDto);
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
     * 获取插入数据
     */
    @Override
    public List<SjsyglDto> getDetectionInfo(SjxxDto sjxxDto, FjsqDto fjsqDto,String lx){
        SjsyglDto sjsyglDto=new SjsyglDto();
        List<SjsyglDto> sjsyglDtos=new ArrayList<>();
        sjsyglDto.setYwlx(lx);
        String sjqfdm="";
        if("DETECT_SJ".equals(lx)){
            if(StringUtil.isNotBlank(sjxxDto.getNbbm())){
                sjsyglDto.setYblxdm(sjxxDto.getNbbm().substring(sjxxDto.getNbbm().length()-1));
            }else{
                sjsyglDto.setYblxdm(sjxxDto.getYblxdm());
            }
            sjsyglDto.setJcdwmc(sjxxDto.getJcdwmc());
            sjsyglDto.setSjid(sjxxDto.getSjid());
            if(StringUtil.isNotBlank(sjxxDto.getSjqf())){
                sjqfdm=sjxxDto.getSjqf();
            }
            sjsyglDto.setIds(sjxxDto.getIds());
            //查询伙伴实验室限制信息
            sjsyglDtos.addAll(getHbDto(sjsyglDto));
            //提交送检后立马提交加测的情况，所以在SJ修改的时候，也需要查询复检信息 2025-06-26
            FjsqDto t_fjsqDto=new FjsqDto();
            t_fjsqDto.setSjid(sjxxDto.getSjid());
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            String nowDate=sdf.format(new Date());
            t_fjsqDto.setDsyrq(nowDate);
            List<FjsqDto> l_fjsqDtos = fjsqDao.getNotSyFjxxBySjxx(t_fjsqDto);
            if(l_fjsqDtos!=null && l_fjsqDtos.size()>0){
                for (FjsqDto fjsqDto1 : l_fjsqDtos) {
                    SjsyglDto sjsyglDto1=new SjsyglDto();
                    sjsyglDto1.setFjid(fjsqDto1.getFjid());
                    sjsyglDto1.setYwlx(DetectionTypeEnum.DETECT_FJ.getCode());
                    sjsyglDtos.addAll(dao.getHbDto(sjsyglDto1));
                }
            }
        }else if("DETECT_FJ".equals(lx)){
            //若复测加测，需要将正常送检的伙伴X限制信息加入
            SjxxDto t_sjxxDto = sjxxDao.getDtoById(fjsqDto.getSjid());
            if(t_sjxxDto!=null && StringUtil.isBlank(t_sjxxDto.getSyrq())&& StringUtil.isBlank(t_sjxxDto.getDsyrq())&& StringUtil.isBlank(t_sjxxDto.getQtsyrq())) {
                SjsyglDto sjsyglDto1 = new SjsyglDto();
                sjsyglDto1.setSjid(fjsqDto.getSjid());
                sjsyglDto1.setYwlx(DetectionTypeEnum.DETECT_SJ.getCode());
                sjsyglDtos.addAll(dao.getHbDto(sjsyglDto1));
            }
            // 用于查询相应检测文库信息的变量还需要设置
            if(StringUtil.isNotBlank(fjsqDto.getNbbm())){
                sjsyglDto.setYblxdm(fjsqDto.getNbbm().substring(fjsqDto.getNbbm().length()-1));
            }else{
                sjsyglDto.setYblxdm(fjsqDto.getYblxdm());
            }
            sjsyglDto.setJcdwmc(fjsqDto.getJcdwmc());
            sjsyglDto.setFjid(fjsqDto.getFjid());
            sjsyglDto.setSjid(fjsqDto.getSjid());
            sjsyglDto.setIds(fjsqDto.getIds());
            if(StringUtil.isNotBlank(fjsqDto.getSjqfdm())) {
                sjqfdm = fjsqDto.getSjqfdm();
            }

            // 查找指定复检以外的复检信息
            FjsqDto t_fjsqDto=new FjsqDto();
            t_fjsqDto.setSjid(fjsqDto.getSjid());
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            String nowDate=sdf.format(new Date());
            t_fjsqDto.setDsyrq(nowDate);
            List<FjsqDto> l_fjsqDtos = fjsqDao.getNotSyFjxxBySjxx(t_fjsqDto);
            if(l_fjsqDtos!=null && l_fjsqDtos.size()>0){
                for (FjsqDto fjsqDto1 : l_fjsqDtos) {
                    SjsyglDto sjsyglDto1=new SjsyglDto();
                    sjsyglDto1.setFjid(fjsqDto1.getFjid());
                    sjsyglDto1.setYwlx(DetectionTypeEnum.DETECT_FJ.getCode());
                    sjsyglDtos.addAll(dao.getHbDto(sjsyglDto1));
                }
            }
        }
        List<SjsyglDto> xzIds=new ArrayList<>();
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
            if(!CollectionUtils.isEmpty(sjsyglDtos)){
                for(SjsyglDto t_sjsyglDto:sjsyglDtos){
                    if(t_sjsyglDto==null)
                        continue;
                    // 有数据，但伙伴ID为空，代表伙伴不在X限制内。如果伙伴不为空，代表有限制
                    if(StringUtil.isNotBlank(t_sjsyglDto.getHbid())){
                        xzIds.add(t_sjsyglDto);
                    }else{
                        //判断是否为加测项目，如果为加测项目，并且是2.0项目，则都不进行2.5，hbxxz是白名单，有数据才做原始2.0。所以直接算有数据
                        //因为存在同时做Q3.0 和 加测Q2.0的情况，这样会出现伙伴不是X限制，但因为是加测2.0需要强制2.0为X，3.0 不是X,所以只需要加测的项目信息限制
                        if("DETECT_FJ".equals(lx) ) {
                            FjsqDto fjsqDto_t = fjsqDao.getDtoById(fjsqDto.getFjid());
                            if(StringUtil.isNotBlank(fjsqDto_t.getFjlxdm()) &&
                                    ("ADDDETECT".equals(fjsqDto_t.getFjlxdm()) || "LAB_ADDDETECT".equals(fjsqDto_t.getFjlxdm()))
                                    && StringUtil.isNotBlank(fjsqDto_t.getJcxmcskz3())
                                    && (fjsqDto_t.getJcxmcskz3().indexOf("IMP_REPORT_ONCO_QINDEX_TEMEPLATE")!=-1 )){
                                SjsyglDto sjsyglDto2=new SjsyglDto();
                                sjsyglDto2.setHbid(t_sjsyglDto.getHbid());
                                sjsyglDto2.setJcxmcskz3(fjsqDto_t.getJcxmcskz3());
                                sjsyglDto2.setXzlx("X");//复检的情况，默认限制类型为X
                                xzIds.add(sjsyglDto2);
                            }
                        }
                    }
                }
            }
        }
        xzIds = xzIds.stream()
                .collect(Collectors.toMap(
                        d -> Arrays.asList(d.getHbid(), d.getJcxmcskz3(), d.getXzlx()), // 组合三个字段作为唯一键
                        Function.identity(),
                        (existing, replacement) -> existing)) // 遇到重复时保留已有条目
                .values()
                .stream()
                .collect(Collectors.toList());

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
		Date date=new Date();
		String dqrq=formatter.format(date);//当前日期
        sjsyglDto.setDsyrq(dqrq);
        List<SjsyglDto> insertInfo = dao.getInsertInfo(sjsyglDto);
        List<SjsyglDto> list =  new ArrayList<>();
        //循环现有的实验数据
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
                        if(StringUtil.isNotBlank(t_sjsyglDto.getJcxmcskz3())&&t_sjsyglDto.getJcxmcskz3().equals(dto.getJcxmcskz3())) {
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
                            if(StringUtil.isNotBlank(dto.getJcxmcskz3())&&dto.getJcxmcskz3().equals(t_sjsyglDto.getJcxmcskz3())
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
                        //判断信息对应表的kzcs4与kzcs5是否相同,kzcs4为标本类型 B等，kzcs5为实验室
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
     * 获取插入数据
     */
    @Override
    public List<SjsyglDto> getDealDetectionInfo(SjxxDto sjxxDto,String lx){
        SjsyglDto sjsyglDto=new SjsyglDto();
        sjsyglDto.setLx(lx);
        String sjqfdm="";
        if("DETECT_SJ".equals(lx)){
            sjsyglDto.setYblxdm(sjxxDto.getYblxdm());
            sjsyglDto.setJcdwmc(sjxxDto.getJcdwmc());
            sjsyglDto.setSjid(sjxxDto.getSjid());
            if(StringUtil.isNotBlank(sjxxDto.getSjqf())){
                sjqfdm=sjxxDto.getSjqf();
            }
        }
        List<SjsyglDto> xzIds=new ArrayList<>();
        //查询伙伴实验室限制信息
        List<SjsyglDto> sjsyglDtos = getHbDto(sjsyglDto);
        List<SjjcxmDto> jcxmlist=(List<SjjcxmDto>) JSON.parseArray(sjxxDto.getJcxm(), SjjcxmDto.class);

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

        sjsyglDto.setSjjcxmDtos(jcxmlist);
        List<SjsyglDto> insertInfo = dao.getDealInsertInfo(sjsyglDto);
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
                        if(StringUtil.isNotBlank(t_sjsyglDto.getJcxmcskz3())&&t_sjsyglDto.getJcxmcskz3().equals(dto.getJcxmcskz3())) {
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
                            if(StringUtil.isNotBlank(dto.getJcxmcskz3())&&dto.getJcxmcskz3().equals(t_sjsyglDto.getJcxmcskz3())
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
                        //判断信息对应表的kzcs4与kzcs5是否相同,kzcs4为标本类型 B等，kzcs5为实验室
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
     * 根据业务IDs删除数据
     * @param sjsyglDto
     * @return
     */
    public int deleteBySyglids(SjsyglDto sjsyglDto){
        return dao.deleteBySyglids(sjsyglDto);
    }

    /**
     * 根据业务IDs删除数据
     * @param sjsyglDto
     * @return
     */
    /*public int deleteByYwids(SjsyglDto sjsyglDto){
         return dao.deleteByYwids(sjsyglDto);
    }*/

    /**
     * 批量修改整张表数据
     * @param
     * @return
     */
    public int modAllList(List<SjsyglDto> list){
        return dao.modAllList(list);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateJcsj(SjsyglDto sjsyglDto) {
       return dao.updateJcsj(sjsyglDto);
    }

    @Override
    public SjsyglDto getRfs(SjsyglDto sjsyglDto) {
        return dao.getRfs(sjsyglDto);
    }

    @Override
    public List<SjsyglDto> getInfoByNbbm(SjsyglDto sjsyglDto) {
        return dao.getInfoByNbbm(sjsyglDto);
    }

    @Override
    public Boolean updateSyList(List<SjsyglDto> list) {
        return dao.updateSyList(list);
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
     * 通过传入的实验管理ID获取对应sjid的所有实验管理数据
     * @param sjsyglDto
     * @return
     */
    @Override
    public List<Map<String,String>> getDtosBySjxxFromSyglid(SjsyglDto sjsyglDto) {
        return dao.getDtosBySjxxFromSyglid( sjsyglDto);
    }

    /**
     * 通过sjids获取送检实验管理数据
     * @param sjsyglDto
     * @return
     */
    @Override
    public List<SjsyglModel> getDtosBySjids(SjsyglDto sjsyglDto) {
        return dao.getDtosBySjids(sjsyglDto);
    }

    /**
     * 更新接收信息
     * @param sjsyglDto
     * @return
     */
    public boolean updateSfjs(SjsyglDto sjsyglDto){
        return dao.updateSfjs(sjsyglDto);
    }
    /**
     * 更新实验日期
     * @param sjsyglDto
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean adjustSaveSjxx(SjsyglDto sjsyglDto){
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

    /**
     * 送检查看页面检测项目TAB获取实验信息
     * @param sjsyglDto
     * @return
     */
    public List<SjsyglDto> getSyxxViewByYwid(SjsyglDto sjsyglDto){
        return dao.getSyxxViewByYwid(sjsyglDto);
    }

    /**
     * 获取提取信息
     */
    public List<Map<String,Object>> getExtractInfo(Map<String, Object> map){
        return dao.getExtractInfo(map);
    }
	
	 /**
     * 收样确认更新
     */
    public Boolean updateConfirmList(List<SjsyglDto> list){
        return dao.updateConfirmList(list);
    }

    @Override
    public void updateJsInfo(SjsyglDto sjsyglDto) {
        dao.updateJsInfo(sjsyglDto);
    }

    public List<SjsyglDto> getOrderSyrqDto(SjsyglDto sjsyglDto){
        return dao.getOrderSyrqDto(sjsyglDto);
    }

    /**
     * 送检的同时增加加测的时候，加测里的是否接收不会进行插入，所以需要在最后进行更新
     * @param sjsyglList
     * @return
     */
    public boolean updateFjJsrq(List<SjsyglDto> sjsyglList){
        return dao.updateFjJsrq(sjsyglList);
    }
}
