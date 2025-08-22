package com.matridx.igams.wechat.service.impl;


import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.WkmxPcrModel;
import com.matridx.igams.common.dao.entities.WkmxPcrResultModel;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IPcrsyjgService;
import com.matridx.igams.wechat.dao.entities.SjyzjgDto;
import com.matridx.igams.wechat.dao.entities.SjyzjgModel;
import com.matridx.igams.wechat.dao.post.ISjyzjgDao;
import com.matridx.igams.wechat.service.svcinterface.ISjyzjgService;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SjyzjgServiceImpl extends BaseBasicServiceImpl<SjyzjgDto, SjyzjgModel, ISjyzjgDao> implements ISjyzjgService {

    private Logger log = LoggerFactory.getLogger(SjyzjgServiceImpl.class);
    @Autowired
    private IPcrsyjgService pcrsyjgService;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean deleteByYzid(SjyzjgDto sjyzjgDto) {
        return dao.deleteByYzid(sjyzjgDto)!= 0;
    }

    /**
     * 除100 保留两位小数
     * @param value
     * @return
     */
    public String exceptOneHundred(String value){
        if (StringUtil.isNotBlank(value)){
            if ("-100".equals(value)){
                return "-";
            }else{
                BigDecimal Hundred = new BigDecimal("100");
                BigDecimal bigDecimal = BigDecimal.valueOf(Double.parseDouble(value.trim())).divide(Hundred,2, RoundingMode.HALF_UP);
                return String.valueOf(bigDecimal);
            }
        }
        return "-";
    }

    /**
     * 接收处理从pcr返回的实验结果
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean getSjyzmxResult(WkmxPcrModel wkmxPcrModel) {
//        if (wkmxPcrModel != null && StringUtil.isNotBlank(wkmxPcrModel.getBackdata())){
//            String[] backdata = wkmxPcrModel.getBackdata().split(",");
//            wkmxPcrModel.setIspcr(backdata[0].split(":")[1]);
//            wkmxPcrModel.setJcdw(backdata[1].split(":")[1]);
//        }
        pcrsyjgService.savePcrsyjgDto(wkmxPcrModel);
        List<WkmxPcrResultModel> resultModels = wkmxPcrModel.getResult();
        List<WkmxPcrResultModel> wkmxPcrResultModelList = new ArrayList<>();
        List<WkmxPcrResultModel> wkmxPcrResultModels = new ArrayList<>();
        log.error("ispcr=0");
        if (resultModels != null && resultModels.size() > 0) {
        	log.error("接收pcr返回的实验结果的条数：" + resultModels.size());
            for (WkmxPcrResultModel pr : resultModels) {
                if (StringUtil.isNotBlank(pr.getSampleName()) && (pr.getSampleName().contains("PC") || pr.getSampleName().contains("NC"))){
                    if(StringUtil.isNotBlank(pr.getSampleName())){
                        pr.setSampleNumber(pr.getSampleName());
                    }
                    wkmxPcrResultModelList.add(pr);
                }else{
                    wkmxPcrResultModels.add(pr);
                }
            }
        }

        Map<String, List<WkmxPcrResultModel>> map = wkmxPcrResultModels.stream().collect(Collectors.groupingBy(WkmxPcrResultModel::getWell));
        List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.VERIFICATION_TYPE.getCode());
        List<String> sjyzjgDtos = new ArrayList<>();
        sjyzjgDtos.add("13213");
        if (null != map && map.size()>0){
            Iterator<Map.Entry<String, List<WkmxPcrResultModel>>> entries = map.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String,  List<WkmxPcrResultModel>> entry = entries.next();
                //String key = entry.getKey();
                List<WkmxPcrResultModel> resultModelList = entry.getValue();
                if (null != resultModelList && resultModelList.size() > 0){
                    if(StringUtil.isBlank(resultModelList.get(0).getSampleNumber())){
                        continue;
                    }
                    SjyzjgDto sjyzjgDto1 = new SjyzjgDto();
                    if (resultModelList.get(0).getSampleNumber().split("-").length>1){
                        sjyzjgDto1.setNbbm(resultModelList.get(0).getSampleNumber().split("-")[0]);
                        sjyzjgDto1.setQfcskz(resultModelList.get(0).getSampleNumber().split("-")[1]);
                    }else{
                        sjyzjgDto1.setNbbm(resultModelList.get(0).getSampleNumber());
                    }
                    int pex = -1;
                    for (WkmxPcrResultModel resultModel:resultModelList) {
                        if (StringUtil.isNotBlank(resultModel.getGeneName())){
                            if (resultModel.getGeneName().contains("结核")||resultModel.getGeneName().contains("MTM")){
                                pex = 1;
                                break;
                            }else if(resultModel.getGeneName().contains("耶氏")||resultModel.getGeneName().contains("ACP")){
                                pex = 0;
                                break;
                            }
                        }
                    }
                    if (pex  == 1){
                        for (JcsjDto jcsjDto:jcsjDtos) {
                            if (StringUtil.isNotBlank(jcsjDto.getCsdm()) && "MTM".equals(jcsjDto.getCsdm())){
                                sjyzjgDto1.setYzlb(jcsjDto.getCsid());
                                break;
                            }
                        }
                    }else if(pex  == 0){
                        for (JcsjDto jcsjDto:jcsjDtos) {
                            if (StringUtil.isNotBlank(jcsjDto.getCsdm()) && "ACP".equals(jcsjDto.getCsdm())){
                                sjyzjgDto1.setYzlb(jcsjDto.getCsid());
                                break;
                            }
                        }
                    }
                    if (StringUtil.isNotBlank(sjyzjgDto1.getYzlb())){
                        List<SjyzjgDto> sjyzjgDtoList = dao.getInfoByYbbh(sjyzjgDto1);
                        for (WkmxPcrResultModel pr:resultModelList) {
                            if (null != sjyzjgDtoList && sjyzjgDtoList.size()>0) {
                                for (SjyzjgDto s : sjyzjgDtoList) {
                                    SjyzjgDto sjyzjgDto = new SjyzjgDto();
                                    sjyzjgDto.setYzjgid(s.getYzjgid());
                                    sjyzjgDto.setJcqf(s.getCskz1());
                                    if (StringUtil.isNotBlank(pr.getGeneName())){
                                        sjyzjgDto.setMbjy(pr.getGeneName());
                                    }
                                    if (StringUtil.isNotBlank(s.getCskz1()) && StringUtil.isNotBlank(pr.getDyeName())){
                                        if (s.getCskz1().equals(pr.getDyeName()) && (StringUtil.isNotBlank(pr.getSampleName()) || StringUtil.isNotBlank(pr.getSampleNumber()))){
                                            switch (Integer.parseInt(s.getCtbs().trim())){
                                                case 1:
                                                    sjyzjgDto.setKw1(pr.getWell());
                                                    sjyzjgDto.setBz1(pr.getSampleName());
                                                    if (StringUtil.isNotBlank(pr.getCtVaule())) {
                                                        if (!NumberUtils.isCreatable(pr.getCtVaule())) {
                                                            sjyzjgDto.setCt1("-");
                                                        } else {
                                                            sjyzjgDto.setCt1(exceptOneHundred(pr.getCtVaule()));
                                                        }
                                                    }
                                                    break;
                                                case 2:
                                                    sjyzjgDto.setKw2(pr.getWell());
                                                    sjyzjgDto.setBz2(pr.getSampleName());
                                                    if (StringUtil.isNotBlank(pr.getCtVaule())) {
                                                        if (!NumberUtils.isCreatable(pr.getCtVaule())) {
                                                            sjyzjgDto.setCt2("-");
                                                        } else {
                                                            sjyzjgDto.setCt2(exceptOneHundred(pr.getCtVaule()));
                                                        }
                                                    }
                                                    break;
                                                case 3:
                                                    sjyzjgDto.setKw3(pr.getWell());
                                                    if (StringUtil.isNotBlank(pr.getCtVaule())) {
                                                        if (!NumberUtils.isCreatable(pr.getCtVaule())) {
                                                            sjyzjgDto.setCt3("-");
                                                        } else {
                                                            sjyzjgDto.setCt3(exceptOneHundred(pr.getCtVaule()));
                                                        }
                                                    }
                                                    break;
                                            }
                                            s.setCtbs(String.valueOf(Integer.parseInt(s.getCtbs().trim())+1));
                                            sjyzjgDto.setCtbs(s.getCtbs());
                                        }
                                    }
                                    if (!sjyzjgDtos.contains(s.getYzjgid())){
                                        if (wkmxPcrResultModelList != null && wkmxPcrResultModelList.size() > 0) {
                                            sjyzjgDtos.add(s.getYzjgid());
                                            for (WkmxPcrResultModel wkmxPcrResultModel : wkmxPcrResultModelList) {
                                                if (StringUtil.isNotBlank(s.getYzlbcs()) && StringUtil.isNotBlank(wkmxPcrResultModel.getDyeName())) {
                                                    if (wkmxPcrResultModel.getSampleName().contains(s.getYzlbcs()) && (wkmxPcrResultModel.getSampleName().contains("PC") || wkmxPcrResultModel.getSampleName().contains("NC"))) {
                                                        if (StringUtil.isNotBlank(s.getCskz1()) && s.getCskz1().equals(wkmxPcrResultModel.getDyeName()) && wkmxPcrResultModel.getSampleName().contains("NC")){
                                                            switch (Integer.parseInt(s.getNctbs().trim())){
                                                                case 1:
                                                                    sjyzjgDto.setNkw1(wkmxPcrResultModel.getWell());
                                                                    if (StringUtil.isNotBlank(wkmxPcrResultModel.getCtVaule())) {
                                                                        if (!NumberUtils.isCreatable(wkmxPcrResultModel.getCtVaule())) {
                                                                            sjyzjgDto.setNct1("-");
                                                                        } else {
                                                                            sjyzjgDto.setNct1(exceptOneHundred(wkmxPcrResultModel.getCtVaule()));
                                                                        }
                                                                    }
                                                                    break;
                                                                case 2:
                                                                    sjyzjgDto.setNkw2(wkmxPcrResultModel.getWell());
                                                                    if (StringUtil.isNotBlank(wkmxPcrResultModel.getCtVaule())) {
                                                                        if (!NumberUtils.isCreatable(wkmxPcrResultModel.getCtVaule())) {
                                                                            sjyzjgDto.setNct2("-");
                                                                        } else {
                                                                            sjyzjgDto.setNct2(exceptOneHundred(wkmxPcrResultModel.getCtVaule()));
                                                                        }
                                                                    }
                                                                    break;
                                                                case 3:
                                                                    sjyzjgDto.setNkw3(wkmxPcrResultModel.getWell());
                                                                    if (StringUtil.isNotBlank(wkmxPcrResultModel.getCtVaule())) {
                                                                        if (!NumberUtils.isCreatable(wkmxPcrResultModel.getCtVaule())) {
                                                                            sjyzjgDto.setNct3("-");
                                                                        } else {
                                                                            sjyzjgDto.setNct3(exceptOneHundred(wkmxPcrResultModel.getCtVaule()));
                                                                        }
                                                                    }
                                                                    break;
                                                            }
                                                            s.setNctbs(String.valueOf(Integer.parseInt(s.getNctbs().trim())+1));
                                                            sjyzjgDto.setNctbs(s.getNctbs());
                                                        }else if ( StringUtil.isNotBlank(s.getCskz1()) && s.getCskz1().equals(wkmxPcrResultModel.getDyeName()) && wkmxPcrResultModel.getSampleName().contains("PC")){
                                                            switch (Integer.parseInt(s.getPctbs().trim())){
                                                                case 1:
                                                                    sjyzjgDto.setPkw1(wkmxPcrResultModel.getWell());
                                                                    if (StringUtil.isNotBlank(wkmxPcrResultModel.getCtVaule())) {
                                                                        if (!NumberUtils.isCreatable(wkmxPcrResultModel.getCtVaule())) {
                                                                            sjyzjgDto.setPct1("-");
                                                                        } else {
                                                                            sjyzjgDto.setPct1(exceptOneHundred(wkmxPcrResultModel.getCtVaule()));
                                                                        }
                                                                    }
                                                                    break;
                                                                case 2:
                                                                    sjyzjgDto.setPkw2(wkmxPcrResultModel.getWell());
                                                                    if (StringUtil.isNotBlank(wkmxPcrResultModel.getCtVaule())) {
                                                                        if (!NumberUtils.isCreatable(wkmxPcrResultModel.getCtVaule())) {
                                                                            sjyzjgDto.setPct2("-");
                                                                        } else {
                                                                            sjyzjgDto.setPct2(exceptOneHundred(wkmxPcrResultModel.getCtVaule()));
                                                                        }
                                                                    }
                                                                    break;
                                                                case 3:
                                                                    sjyzjgDto.setPkw3(wkmxPcrResultModel.getWell());
                                                                    if (StringUtil.isNotBlank(wkmxPcrResultModel.getCtVaule())) {
                                                                        if (!NumberUtils.isCreatable(wkmxPcrResultModel.getCtVaule())) {
                                                                            sjyzjgDto.setPct3("-");
                                                                        } else {
                                                                            sjyzjgDto.setPct3(exceptOneHundred(wkmxPcrResultModel.getCtVaule()));
                                                                        }
                                                                    }
                                                                    break;
                                                            }
                                                            s.setPctbs(String.valueOf(Integer.parseInt(s.getPctbs().trim())+1));
                                                            sjyzjgDto.setPctbs(s.getPctbs());
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    if (sjyzjgDto != null && StringUtil.isNotBlank(sjyzjgDto.getYzjgid())) {
                                        boolean result = dao.updateDto(sjyzjgDto) != 0;
                                        if (!result){
                                            return false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
        dao.updateListCzbs(sjyzjgDtos);
        return true;
    }

    @Override
    public List<SjyzjgDto> getInfoByYbbh(SjyzjgDto sjyzjgDto) {
        return dao.getInfoByYbbh(sjyzjgDto);
    }

    @Override
    public String judgmentConclusion(String ct) {
        List<JcsjDto> bgjgList = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.VERIFICATION_RESULT.getCode());
        for (JcsjDto jcsjDto : bgjgList) {
            if (StringUtil.isNotBlank(jcsjDto.getCskz1())){
                String[] strings = jcsjDto.getCskz1().split(",");
                if (strings.length==4){
                    String[] split = strings[0].split(":");
                    strings[0] = split[1];
                    if ("或".equals(split[0])){
                        if ("小于".equals(strings[0]) && "小于".equals(strings[2])){
                            if (Double.parseDouble(ct) < Double.parseDouble(strings[1]) || Double.parseDouble(ct) < Double.parseDouble(strings[3])){
                                return jcsjDto.getCsid();
                            }
                        }else if ("小于".equals(strings[0])  && "大于".equals(strings[2])){
                            if (Double.parseDouble(ct) < Double.parseDouble(strings[1]) || Double.parseDouble(ct) > Double.parseDouble(strings[3])){
                                return jcsjDto.getCsid();
                            }
                        }else if ("小于".equals(strings[0])  && "小于等于".equals(strings[2])){
                            if (Double.parseDouble(ct) < Double.parseDouble(strings[1]) || Double.parseDouble(ct) <= Double.parseDouble(strings[3])){
                                return jcsjDto.getCsid();
                            }
                        }else if ("小于".equals(strings[0])  && "大于等于".equals(strings[2])){
                            if (Double.parseDouble(ct) < Double.parseDouble(strings[1]) || Double.parseDouble(ct) >= Double.parseDouble(strings[3])){
                                return jcsjDto.getCsid();
                            }
                        }else if ("大于".equals(strings[0])  && "小于".equals(strings[2])){
                            if (Double.parseDouble(ct) > Double.parseDouble(strings[1]) || Double.parseDouble(ct) < Double.parseDouble(strings[3])){
                                return jcsjDto.getCsid();
                            }
                        }else if ("大于".equals(strings[0])  && "小于等于".equals(strings[2])){
                            if (Double.parseDouble(ct) > Double.parseDouble(strings[1]) || Double.parseDouble(ct) <= Double.parseDouble(strings[3])){
                                return jcsjDto.getCsid();
                            }
                        }else if ("大于".equals(strings[0])  && "大于".equals(strings[2])){
                            if (Double.parseDouble(ct) > Double.parseDouble(strings[1]) || Double.parseDouble(ct) > Double.parseDouble(strings[3])){
                                return jcsjDto.getCsid();
                            }
                        }else if ("大于".equals(strings[0])  && "大于等于".equals(strings[2])){
                            if (Double.parseDouble(ct) > Double.parseDouble(strings[1]) || Double.parseDouble(ct) >= Double.parseDouble(strings[3])){
                                return jcsjDto.getCsid();
                            }
                        }else if ("小于等于".equals(strings[0])  && "小于".equals(strings[2])){
                            if (Double.parseDouble(ct) <= Double.parseDouble(strings[1]) || Double.parseDouble(ct) < Double.parseDouble(strings[3])){
                                return jcsjDto.getCsid();
                            }
                        }else if ("小于等于".equals(strings[0])  && "小于等于".equals(strings[2])){
                            if (Double.parseDouble(ct) <= Double.parseDouble(strings[1]) || Double.parseDouble(ct) <= Double.parseDouble(strings[3])){
                                return jcsjDto.getCsid();
                            }
                        }else if ("小于等于".equals(strings[0])  && "大于".equals(strings[2])){
                            if (Double.parseDouble(ct) <= Double.parseDouble(strings[1]) || Double.parseDouble(ct) > Double.parseDouble(strings[3])){
                                return jcsjDto.getCsid();
                            }
                        }else if ("小于等于".equals(strings[0])  && "大于等于".equals(strings[2])){
                            if (Double.parseDouble(ct) <= Double.parseDouble(strings[1]) || Double.parseDouble(ct) >= Double.parseDouble(strings[3])){
                                return jcsjDto.getCsid();
                            }
                        }else if ("大于等于".equals(strings[0])  && "小于".equals(strings[2])){
                            if (Double.parseDouble(ct) >= Double.parseDouble(strings[1]) || Double.parseDouble(ct) < Double.parseDouble(strings[3])){
                                return jcsjDto.getCsid();
                            }
                        }else if ("大于等于".equals(strings[0])  && "小于等于".equals(strings[2])){
                            if (Double.parseDouble(ct) >= Double.parseDouble(strings[1]) || Double.parseDouble(ct) <= Double.parseDouble(strings[3])){
                                return jcsjDto.getCsid();
                            }
                        }else if ("大于等于".equals(strings[0])  && "大于".equals(strings[2])){
                            if (Double.parseDouble(ct) >= Double.parseDouble(strings[1]) || Double.parseDouble(ct) > Double.parseDouble(strings[3])){
                                return jcsjDto.getCsid();
                            }
                        }else if ("大于等于".equals(strings[0])  && "大于等于".equals(strings[2])){
                            if (Double.parseDouble(ct) >= Double.parseDouble(strings[1]) || Double.parseDouble(ct) >= Double.parseDouble(strings[3])){
                                return jcsjDto.getCsid();
                            }
                        }
                    }else{
                        if ("小于".equals(strings[0]) && "小于".equals(strings[2])){
                            if (Double.parseDouble(ct) < Double.parseDouble(strings[1]) && Double.parseDouble(ct) < Double.parseDouble(strings[3])){
                                return jcsjDto.getCsid();
                            }
                        }else if ("小于".equals(strings[0])  && "大于".equals(strings[2])){
                            if (Double.parseDouble(ct) < Double.parseDouble(strings[1]) && Double.parseDouble(ct) > Double.parseDouble(strings[3])){
                                return jcsjDto.getCsid();
                            }
                        }else if ("小于".equals(strings[0])  && "小于等于".equals(strings[2])){
                            if (Double.parseDouble(ct) < Double.parseDouble(strings[1]) && Double.parseDouble(ct) <= Double.parseDouble(strings[3])){
                                return jcsjDto.getCsid();
                            }
                        }else if ("小于".equals(strings[0])  && "大于等于".equals(strings[2])){
                            if (Double.parseDouble(ct) < Double.parseDouble(strings[1]) && Double.parseDouble(ct) >= Double.parseDouble(strings[3])){
                                return jcsjDto.getCsid();
                            }
                        }else if ("大于".equals(strings[0])  && "小于".equals(strings[2])){
                            if (Double.parseDouble(ct) > Double.parseDouble(strings[1]) && Double.parseDouble(ct) < Double.parseDouble(strings[3])){
                                return jcsjDto.getCsid();
                            }
                        }else if ("大于".equals(strings[0])  && "小于等于".equals(strings[2])){
                            if (Double.parseDouble(ct) > Double.parseDouble(strings[1]) && Double.parseDouble(ct) <= Double.parseDouble(strings[3])){
                                return jcsjDto.getCsid();
                            }
                        }else if ("大于".equals(strings[0])  && "大于".equals(strings[2])){
                            if (Double.parseDouble(ct) > Double.parseDouble(strings[1]) && Double.parseDouble(ct) > Double.parseDouble(strings[3])){
                                return jcsjDto.getCsid();
                            }
                        }else if ("大于".equals(strings[0])  && "大于等于".equals(strings[2])){
                            if (Double.parseDouble(ct) > Double.parseDouble(strings[1]) && Double.parseDouble(ct) >= Double.parseDouble(strings[3])){
                                return jcsjDto.getCsid();
                            }
                        }else if ("小于等于".equals(strings[0])  && "小于".equals(strings[2])){
                            if (Double.parseDouble(ct) <= Double.parseDouble(strings[1]) && Double.parseDouble(ct) < Double.parseDouble(strings[3])){
                                return jcsjDto.getCsid();
                            }
                        }else if ("小于等于".equals(strings[0])  && "小于等于".equals(strings[2])){
                            if (Double.parseDouble(ct) <= Double.parseDouble(strings[1]) && Double.parseDouble(ct) <= Double.parseDouble(strings[3])){
                                return jcsjDto.getCsid();
                            }
                        }else if ("小于等于".equals(strings[0])  && "大于".equals(strings[2])){
                            if (Double.parseDouble(ct) <= Double.parseDouble(strings[1]) && Double.parseDouble(ct) > Double.parseDouble(strings[3])){
                                return jcsjDto.getCsid();
                            }
                        }else if ("小于等于".equals(strings[0])  && "大于等于".equals(strings[2])){
                            if (Double.parseDouble(ct) <= Double.parseDouble(strings[1]) && Double.parseDouble(ct) >= Double.parseDouble(strings[3])){
                                return jcsjDto.getCsid();
                            }
                        }else if ("大于等于".equals(strings[0])  && "小于".equals(strings[2])){
                            if (Double.parseDouble(ct) >= Double.parseDouble(strings[1]) && Double.parseDouble(ct) < Double.parseDouble(strings[3])){
                                return jcsjDto.getCsid();
                            }
                        }else if ("大于等于".equals(strings[0])  && "小于等于".equals(strings[2])){
                            if (Double.parseDouble(ct) >= Double.parseDouble(strings[1]) && Double.parseDouble(ct) <= Double.parseDouble(strings[3])){
                                return jcsjDto.getCsid();
                            }
                        }else if ("大于等于".equals(strings[0])  && "大于".equals(strings[2])){
                            if (Double.parseDouble(ct) >= Double.parseDouble(strings[1]) && Double.parseDouble(ct) > Double.parseDouble(strings[3])){
                                return jcsjDto.getCsid();
                            }
                        }else if ("大于等于".equals(strings[0])  && "大于等于".equals(strings[2])){
                            if (Double.parseDouble(ct) >= Double.parseDouble(strings[1]) && Double.parseDouble(ct) >= Double.parseDouble(strings[3])){
                                return jcsjDto.getCsid();
                            }
                        }
                    }
                }else if (strings.length ==2){
                    if ("小于".equals(strings[0])){
                        if (Double.parseDouble(ct) < Double.parseDouble(strings[1])){
                            return jcsjDto.getCsid();
                        }
                    }else if ("大于".equals(strings[0])){
                        if (Double.parseDouble(ct) > Double.parseDouble(strings[1])){
                            return jcsjDto.getCsid();
                        }
                    }else if ("小于等于".equals(strings[0])){
                        if (Double.parseDouble(ct) <= Double.parseDouble(strings[1])){
                            return jcsjDto.getCsid();
                        }
                    }else if ("大于等于".equals(strings[0])){
                        if (Double.parseDouble(ct) >= Double.parseDouble(strings[1])){
                            return jcsjDto.getCsid();
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public String drawAconclusion(String ctVaule) {
        List<JcsjDto> bgjgList = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.VERIFICATION_RESULT.getCode());
        JcsjDto negative = new JcsjDto();
        JcsjDto positive = new JcsjDto();
        JcsjDto greyArea = new JcsjDto();
        for (JcsjDto jcsjDto : bgjgList) {
            if ("001".equals(jcsjDto.getCsdm())){//阳性
                positive = jcsjDto;
            }else if ("002".equals(jcsjDto.getCsdm())){//阴性
                negative = jcsjDto;
            }else if ("003".equals(jcsjDto.getCsdm())){//检测灰区
                greyArea = jcsjDto;
            }
        }
        //阴：<0 或者 >40
        //阳：>=0 并且 <=37
        //灰区：>37 并且 <=40
        if (Double.parseDouble(ctVaule)/100.0 < 0){
            return negative.getCsid();
        }else if (Double.parseDouble(ctVaule)/100.0 >=0 && Double.parseDouble(ctVaule)/100.0 <=37){
            return positive.getCsid();
        }else if (Double.parseDouble(ctVaule)/100.0 >37 && Double.parseDouble(ctVaule)/100.0 <=40){
            return greyArea.getCsid();
        }else {//>40
            return negative.getCsid();
        }
    }

    @Override
    public boolean updateListJl(List<SjyzjgDto> sjyzjgDtoList) {
        int i= dao.updateListJl(sjyzjgDtoList);
        return i > 0;
    }

    /**
     * 通过验证ID、类别、结果获取送检验证结果数据
     * @param sjyzjgDtoList
     * @return
     */
    @Override
    public List<SjyzjgDto> getByYzlbYzidYzjg(List<SjyzjgDto> sjyzjgDtoList) {
        return dao.getByYzlbYzidYzjg(sjyzjgDtoList);
    }

    /**
     * 批量新增送检验证结果数据
     * @param updateSjyzjgList
     */
    @Override
    public void insertList(List<SjyzjgDto> updateSjyzjgList) {
        dao.insertList(updateSjyzjgList);
    }

}
