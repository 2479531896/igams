package com.matridx.igams.wechat.service.impl;


import com.matridx.igams.common.dao.entities.XxdyDto;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IXxdyService;
import com.matridx.igams.wechat.dao.entities.MrzkDto;
import com.matridx.igams.wechat.dao.entities.MrzkModel;
import com.matridx.igams.wechat.dao.entities.WzzkDto;
import com.matridx.igams.wechat.dao.post.IMrzkDao;
import com.matridx.igams.wechat.service.svcinterface.IMrzkService;
import com.matridx.igams.wechat.service.svcinterface.IWzzkService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Service
public class MrzkServiceImpl extends BaseBasicServiceImpl<MrzkDto, MrzkModel, IMrzkDao> implements IMrzkService {


    @Autowired
    private IXxdyService xxdyService;
    @Autowired
    private IWzzkService wzzkService;

    /**
     * 初始化Wzzk
     */
    public void  initWzzk(){

    }

    /**
     * 更新物种质控
     */
    public void updateWzzk(Map<String, String> map){
        if(map.get("jcdw")!=null){
            String jcdws=map.get("jcdw");
            List<String> kzcs1s= Arrays.asList(jcdws.split(","));
            XxdyDto xxdyDto=new XxdyDto();
            xxdyDto.setKzcs1s(kzcs1s);
            //定时任务根据参数 jcdw，查询信息对应的【质控-项目PC对应】并关联 每日质控，group by得到 项目类型，物种，进行重新计算均值和标准差，并更新物种质控的数据
            //根据参数的jcdw名称，查询信息对应表获取不同单位的各自项目类型，然后再关联物种质控，确认是否需要进行更新，6个月内要每个一个星期进行更新，6个月后不再更新
            List<Map<String,String>> ksrqList=xxdyService.getPcdyrq(xxdyDto);
            if(!CollectionUtils.isEmpty(ksrqList)){
                for(Map<String,String> ksrqMap:ksrqList){
                    if( ksrqMap!= null ){
                        String ksrq=ksrqMap.get("ksrq");
                        boolean falg=false;
                        if(StringUtil.isBlank(ksrq)){
                            falg=true;
                        }else {
                            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            LocalDate dateTime = LocalDate.parse(ksrq, formatter1);

                            LocalDate now = LocalDate.now();
                            Period period = Period.ofMonths(6);
                            now=now.minus(period);
                            if(dateTime.isAfter(now)){
                                falg=true;
                            }
                        }
                        String jcdwmc=ksrqMap.get("jcdwmc");
                        String yxx=ksrqMap.get("yxx");
                        if(falg){
                            XxdyDto xxdyDto1=new XxdyDto();
                            xxdyDto1.setJcdwmc(jcdwmc);
                            xxdyDto1.setYxx(yxx);
                            List<Map<String,String>>numList= xxdyService.getStddevAndVariance(xxdyDto1);
                            List<WzzkDto> wzzkDtoList=new ArrayList<>();
                            for(Map<String,String> map_t:numList){
                                WzzkDto wzzkDto=new WzzkDto();
                                wzzkDto.setZkid(StringUtil.generateUUID());
                                if(StringUtil.isNotBlank(map_t.get("bzc"))){
                                    BigDecimal bigDecimalbzc=new BigDecimal(map_t.get("bzc"));
                                    wzzkDto.setBzc(bigDecimalbzc.setScale(2, RoundingMode.HALF_UP).toString());
                                }else {
                                    wzzkDto.setBzc("0");
                                }

                                wzzkDto.setJcdwid(map_t.get("jcdwid"));
                                wzzkDto.setWzid(map_t.get("wzid"));
                                if(StringUtil.isNotBlank(map_t.get("jz"))){
                                    BigDecimal bigDecimaljz=new BigDecimal(map_t.get("jz"));
                                    wzzkDto.setJz(bigDecimaljz.setScale(2, RoundingMode.HALF_UP).toString());
                                }else {
                                    wzzkDto.setJz("0");
                                }

                                if(StringUtil.isBlank(ksrq)){
                                    LocalDate now = LocalDate.now();
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                    wzzkDto.setKsrq(now.format(formatter));
                                }
                                wzzkDto.setWkbh(map_t.get("wkbh"));
                                wzzkDto.setScbj("0");
                                if(StringUtil.isNotBlank(map_t.get("wzid"))){
                                    wzzkDtoList.add(wzzkDto);
                                }
                            }
                            if(!CollectionUtils.isEmpty(wzzkDtoList)){
                                wzzkService.insertOrUpdateList(wzzkDtoList);
                            }
                        }
                    }
                }

            }
        }
    }
}
