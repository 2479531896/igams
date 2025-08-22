package com.matridx.igams.bioinformation.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.bioinformation.dao.entities.CnvjgDto;
import com.matridx.igams.bioinformation.dao.entities.WkcxDto;
import com.matridx.igams.bioinformation.service.svcinterface.IWkcxService;
import com.matridx.igams.bioinformation.util.JDBCUtils;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.bioinformation.dao.entities.WkcxbbDto;
import com.matridx.igams.bioinformation.dao.entities.WkcxbbModel;
import com.matridx.igams.bioinformation.dao.post.IWkcxbbDao;
import com.matridx.igams.bioinformation.service.svcinterface.IWkcxbbService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WkcxbbServiceImpl extends BaseBasicServiceImpl<WkcxbbDto, WkcxbbModel, IWkcxbbDao> implements IWkcxbbService {
    @Autowired
    IWkcxService wkcxService;


    /**
     * 通过版本号和文库编号获取文库版本信息
     */
    @Override
    public WkcxbbDto getDtoByBbhAndWkbh(WkcxbbDto wkcxbbDto) {
        return dao.getDtoByBbhAndWkbh(wkcxbbDto);
    }
    /**
     * 获取报告导出
     */
    @Override
    public WkcxbbDto getDtoByJgid(WkcxbbDto wkcxbbDto) {
        WkcxbbDto wzqzfw=dao.getWzqzfw(wkcxbbDto);//查找物种的最大最小值
        wkcxbbDto= dao.getDtoByJgid(wkcxbbDto);
        if(wzqzfw!=null&&StringUtil.isNoneBlank(wzqzfw.getZdz())&&StringUtil.isNoneBlank(wzqzfw.getZxz())){
            BigDecimal ryzs=new BigDecimal(wkcxbbDto.getRyzs());
            BigDecimal zxz=new BigDecimal(wzqzfw.getZxz());
            BigDecimal zdz=new BigDecimal(wzqzfw.getZdz());
            ryzs=(ryzs.subtract(zxz)).divide(zdz.subtract(zxz),2, RoundingMode.HALF_UP);
            //得出的结果如果在0到1中间就取本身，如果小于0就取0，大于1就取1
            if(ryzs.compareTo(BigDecimal.ONE) < 0 && ryzs.compareTo(BigDecimal.ZERO) > 0) {
                wkcxbbDto.setHost_position(ryzs.toPlainString());
            }else if(ryzs.compareTo(BigDecimal.ONE) > 0 ||ryzs.compareTo(BigDecimal.ONE)==0){
                wkcxbbDto.setHost_position("1");
            }else{
                wkcxbbDto.setHost_position("0");
            }
        }
        return wkcxbbDto;
    }
    /**
     * 查找最大最小值
     */
    @Override
    public WkcxbbDto getWzqzfw(WkcxbbDto wkcxbbDto) {
        return dao.getWzqzfw(wkcxbbDto);
    }
    /**
     * 查找送检表
     */
    @Override
    public WkcxbbDto getSjxx(WkcxbbDto wkcxbbDto) {
        return dao.getSjxx(wkcxbbDto);
    }

    @Override
    public boolean addWzqf(List<WkcxbbDto> list) {
        dao.addWzqf(list);
        dao.updateYblx();
        return true;
    }

    @Override
    public WkcxbbDto getZxbbDto(WkcxbbDto wkcxbbDto) {
        return dao.getZxbbDto( wkcxbbDto);
    }

    @Override
    public WkcxbbDto getZxbbDtoByXpAndWKbh(WkcxbbDto wkcxbbDto) {
        return dao.getZxbbDtoByXpAndWKbh(wkcxbbDto);
    }

    public  void syncVersionData(){
        try{
            List<WkcxDto> analysisParams = wkcxService.getAnalysisParams();
            for(int i=1;i<=1106;i++){
                WkcxbbDto wkcxbbDto=new WkcxbbDto();
                wkcxbbDto.setPageSize(200);
                wkcxbbDto.setPageNumber(i);
                wkcxbbDto.setPageStart((i-1)*100);
                List<WkcxbbDto> dtoList = dao.syncVersionData(wkcxbbDto);
                List<WkcxbbDto> list = new ArrayList<>();
                for(WkcxbbDto dto:dtoList){
                    if(StringUtil.isNotBlank(dto.getFxbb())){
                        JSONObject jsonObject=JSONObject.parseObject(dto.getFxbb());
                        JSONObject dateJson = JSONObject.parseObject(dto.getVersion_dates());
                        Set<String> strings = dateJson.keySet();
                        for(String value: strings){
                            WkcxbbDto wkcxbbDto_t=new WkcxbbDto();
                            wkcxbbDto_t.setJgid(StringUtil.generateUUID());
                            wkcxbbDto_t.setWkbh(dto.getWkbh());
                            wkcxbbDto_t.setWkcxid(dto.getWkcxid());
                            String str = jsonObject.getString(value);
                            String date = dateJson.getString(value);
                            if(StringUtil.isNotBlank(str)){
                                boolean flag =false;
                                for (WkcxDto analysis : analysisParams) {
                                    if (str.equals(analysis.getParam_md5())) {
                                        wkcxbbDto_t.setBbh("V" + value + ":" + analysis.getVersion() + "-" + date);
                                        flag=true;
                                        break;
                                    }
                                }
                                if(!flag){
                                    wkcxbbDto_t.setBbh("V" + value + ":" + str + "-" + date);
                                }
                            }else{
                                wkcxbbDto_t.setBbh("V" + value + ":" + "-" + date);
                            }
                            wkcxbbDto_t.setSpike(dto.getSpike());
                            wkcxbbDto_t.setRyzs(dto.getRyzs());
                            wkcxbbDto_t.setRypw(dto.getRypw());
                            if(StringUtil.isNotBlank(dto.getSfzwqckj())){
                                if("t".equals(dto.getSfzwqckj())){
                                    wkcxbbDto_t.setSfzwqckj("1");
                                }else  if("f".equals(dto.getSfzwqckj())){
                                    dto.setSfzwqckj("0");
                                }
                            }
                            String[] array = dto.getWkbh().split("-");
                            String lrsj = "";
                            for(String s:array){
                                if(s.length()==8){
                                    if(StringUtil.isNumeric(s)){
                                        lrsj=s;
                                    }
                                }
                            }
                            if(StringUtil.isNotBlank(lrsj)){
                                wkcxbbDto_t.setLrsj(lrsj);
                            }else{
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
                                String format = simpleDateFormat.format(new Date());
                                wkcxbbDto_t.setLrsj(format);
                            }

                            list.add(wkcxbbDto_t);
                        }
                    }
                }
                if(list.size()>0){
                    dao.insertList(list);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public void updateCnvjgxq(){
        JDBCUtils jdbcUtils=new JDBCUtils();
        List<Map<String,String>> lis2_cnvjgxq=jdbcUtils.queryCnvjgxq();
        List<Map<String,String>> lis2_cnvjgxq1=new ArrayList<>();
        if(lis2_cnvjgxq.size()>0){
            if(lis2_cnvjgxq.size()>100){
                for(int a=1;a<=lis2_cnvjgxq.size();a++){
                    lis2_cnvjgxq1.add(lis2_cnvjgxq.get(a-1));
                    if(a!=0&&a%100==0){
                        dao.insertCnvjgxq(lis2_cnvjgxq1);
                        lis2_cnvjgxq1.clear();
                    }
                    if(a>lis2_cnvjgxq.size()-1&&lis2_cnvjgxq1.size()>0){
                        dao.insertCnvjgxq(lis2_cnvjgxq1);
                    }
                }

            }else{
                dao.insertCnvjgxq(lis2_cnvjgxq);
            }
        }
    }
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public void updateCnvjg(){
        JDBCUtils jdbcUtils=new JDBCUtils();
        List<CnvjgDto> lis2_cnvjg=jdbcUtils.queryCnvjg();
        List<CnvjgDto> lis2_cnvjg1=new ArrayList<>();
        if(lis2_cnvjg.size()>0){
            if(lis2_cnvjg.size()>100){
                for(int a=1;a<=lis2_cnvjg.size();a++){
                    lis2_cnvjg1.add(lis2_cnvjg.get(a-1));
                    if(a!=0&&a%100==0){
                        dao.insertCnvjg(lis2_cnvjg1);
                        lis2_cnvjg1.clear();
                    }
                    if(a>lis2_cnvjg.size()-1&&lis2_cnvjg1.size()>0){
                        dao.insertCnvjg(lis2_cnvjg1);
                    }
                }

            }else{
                dao.insertCnvjg(lis2_cnvjg);
            }
        }
    }
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public void updateSample() {
        JDBCUtils jdbcUtils=new JDBCUtils();
        try {
            List<Map<String,String>> lis2_sample=jdbcUtils.querySample();
            List<Map<String,String>> lis2_sample1=new ArrayList<>();
            if(lis2_sample.size()>0){
                if(lis2_sample.size()>100){
                    for(int a=1;a<=lis2_sample.size();a++){
                        lis2_sample1.add(lis2_sample.get(a-1));
                        if(a!=0&&a%100==0){
                            dao.insertSample(lis2_sample1);
                            lis2_sample1.clear();
                        }
                        if(a>lis2_sample.size()-1&&lis2_sample1.size()>0){
                            dao.insertSample(lis2_sample1);
                        }
                    }

                }else{
                    dao.insertSample(lis2_sample);
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public void updatSjbgsm() {
        JDBCUtils jdbcUtils=new JDBCUtils();
        List<Map<String,String>> list=jdbcUtils.queryXpxx();
        List<Map<String,String>> list1=new ArrayList<>();
            if(list.size()>0){
                if(list.size()>100){
                    for(int a=1;a<=list.size();a++){
                        list1.add(list.get(a-1));
                        if(a!=0&&a%100==0){
                            dao.insertXpxx(list1);
                            list1.clear();
                        }
                        if(a>list.size()-1&&list1.size()>0){
                            dao.insertXpxx(list1);
                        }
                    }

                }else{
                    dao.insertXpxx(list);
                }
            }






    }
//    public void updatSjbgsm(){
//        JDBCUtils jdbcUtils=new JDBCUtils();
//        Map<String,List<WkcxbbDto>> map=jdbcUtils.queryWkcxbbDto();
//        List<WkcxbbDto> dna_List=map.get("dna");
//        List<WkcxbbDto> dna_List1=new ArrayList<>();
//        List<WkcxbbDto> other_List=map.get("other");
//        List<WkcxbbDto> other_List1=new ArrayList<>();
//        if(dna_List.size()>0){
//            if(dna_List.size()>100){
//                for(int a=1;a<=dna_List.size();a++){
//                    dna_List1.add(dna_List.get(a-1));
//                    if(a!=0&&a%100==0){
//                        dao.updateSjbgsm("dna",dna_List1);
//                        dna_List1.clear();
//                    }
//
//                    if(a>dna_List.size()-1&&dna_List1.size()>0){
//                        dao.updateSjbgsm("dna",dna_List1);
//                    }
//                }
//
//            }else{
//                dao.updateSjbgsm("dna",dna_List);
//            }
//
//        }
//        if(other_List.size()>0){
//            if(other_List.size()>100){
//                for(int a=1;a<=other_List.size();a++){
//                    other_List1.add(other_List.get(a-1));
//                    if(a!=0&&a%100==0){
//                        dao.updateSjbgsm("other",other_List1);
//                        other_List1.clear();
//                    }
//
//                    if(a>other_List.size()-1&&other_List1.size()>0){
//                        dao.updateSjbgsm("other",other_List1);
//                    }
//                }
//
//            }else{
//                dao.updateSjbgsm("other",other_List);
//            }
//        }
//
//    }



}
