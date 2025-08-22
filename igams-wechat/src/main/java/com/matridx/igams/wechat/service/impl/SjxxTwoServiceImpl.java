package com.matridx.igams.wechat.service.impl;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.XszbDto;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.dao.entities.XxdyDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXszbService;
import com.matridx.igams.common.service.svcinterface.IXxdyService;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.WbhbyzDto;
import com.matridx.igams.wechat.dao.post.ISjxxTwoDao;
import com.matridx.igams.wechat.service.svcinterface.ISjhbxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxStatisticService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxTwoService;
import com.matridx.igams.wechat.service.svcinterface.IWbhbyzService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author WYX
 * @version 1.0
 * @className SjxxTwoServiceImpl
 * @description TODO
 * @date 17:32 2023/5/10
 **/
@Service
public class SjxxTwoServiceImpl  implements ISjxxTwoService
{
    private static final Logger log = LoggerFactory.getLogger(SjxxTwoServiceImpl.class);
    @Autowired
    private ISjxxTwoDao dao;
    @Autowired
    ISjxxService sjxxService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    IXxdyService xxdyService;
    @Autowired
    ISjxxStatisticService sjxxStatisticService;
    @Autowired
    IJcsjService jcsjService;
    @Autowired
    IXszbService xszbService;
    @Autowired
    ISjhbxxService sjhbxxService;
    @Autowired
    IWbhbyzService wbhbyzService;
    @Autowired
    IFjcfbService fjcfbService;
    @Override
    public List<Map<String, String>> getAllCountryChanges(SjxxDto sjxxDto) {
        List<String> rqs = new ArrayList<String>();
        if ("week".equals(sjxxDto.getTj())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            try {
                calendar.setTime(sdf.parse(sjxxDto.getJsrqend()));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            int zq = Integer.parseInt(sjxxDto.getZq());
            for (int i = 0; i < zq; i++)
            {
                rqs.add(sdf.format(calendar.getTime()));
                calendar.add(Calendar.DATE, -7);
            }
        }else {
            rqs = sjxxService.getRqsByStartAndEnd(sjxxDto);
        }
        sjxxDto.setRqs(rqs);

        List<Map<String, String>> resultMaps = dao.getAllCountryChanges(sjxxDto);
        if (CollectionUtils.isEmpty(resultMaps)){
            return resultMaps;
        }
        return resultMaps;
    }

    @Override
    public List<Map<String, Object>> getProductionChanges(SjxxDto sjxxDto) {
        List<String> rqs = new ArrayList<String>();
        if ("week".equals(sjxxDto.getTj())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            try {
                calendar.setTime(sdf.parse(sjxxDto.getJsrqend()));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            int zq = Integer.parseInt(sjxxDto.getZq());
            for (int i = 0; i < zq; i++)
            {
                rqs.add(sdf.format(calendar.getTime()));
                calendar.add(Calendar.DATE, -7);
            }
        }else {
            rqs = sjxxService.getRqsByStartAndEnd(sjxxDto);
        }
        sjxxDto.setRqs(rqs);
        List<Map<String, Object>> productionChanges = dao.getProductionChanges(sjxxDto);
        if ("week".equals(sjxxDto.getTj())) {
            rqs = rqs.stream().sorted().collect(Collectors.toList());
        }
            List<Map<String,Object>> productionList=new ArrayList<>();
        List<String> names=new ArrayList<>();
        for (int j = 0; j < productionChanges.size(); j++)
        {
            if (!names.contains(String.valueOf(productionChanges.get(j).get("jcxmmc")))&&productionChanges.get(j).get("jcxmmc")!=null){
                names.add(String.valueOf(productionChanges.get(j).get("jcxmmc")));
            }
        }
        List<Map<String, Object>> newproductionChanges=new ArrayList<>();
        if(names!=null&&names.size()>0){
            for (int k=0;k<rqs.size();k++){
            for (int j=0;j<names.size();j++){
                    boolean flag=false;
                    for (int x=0;x<productionChanges.size();x++){
                        if (rqs.get(k).equals(productionChanges.get(x).get("rq"))&&names.get(j).equals(productionChanges.get(x).get("jcxmmc"))){
                            flag=true;
                            break;
                        }
                    }
                    if (flag==false){
                        Map<String, Object> map=new HashMap<>();
                        map.put("jcxmmc",names.get(j));
                        map.put("rq",rqs.get(k));
                        map.put("sl",0);
                        productionChanges.add(map);
                    }
                }
            }
                for (int j=0;j<rqs.size();j++){
                    for (int i=0;i<productionChanges.size();i++){
                        if (rqs.get(j).equals(productionChanges.get(i).get("rq"))&&productionChanges.get(i).get("jcxmmc")!=null){
                            newproductionChanges.add(productionChanges.get(i));
                        }
                    }
                }
            for(int i=0;i<rqs.size();i++){
                for(String name:names){
                    boolean isFind=false;
                    for(Map<String, Object> map_t:newproductionChanges){
                        if(rqs.get(i).equals(map_t.get("rq"))&&name.equals(map_t.get("jcxmmc"))){
                            if(i==0){
                                map_t.put("bl","0");
                            }else{
                                var num="0";
                                for(Map<String, Object> stringMap:productionList){
                                    if(name.equals(stringMap.get("jcxmmc"))){
                                        num=String.valueOf(stringMap.get("sl"));
                                    }
                                }
                                BigDecimal bigDecimal=new BigDecimal(num);
                                BigDecimal bigDecimal1=new BigDecimal(String.valueOf(map_t.get("sl")));
                                map_t.put("bl",String.valueOf((bigDecimal1.subtract(bigDecimal)).multiply(new BigDecimal("100")).divide(bigDecimal.compareTo(BigDecimal.ZERO)!=0?bigDecimal:new BigDecimal("1"),BigDecimal.ROUND_HALF_UP)));
                            }
                            productionList.add(map_t);
                            isFind=true;
                            break;
                        }
                    }
                    if(!isFind){
                        Map<String, Object> newMap=new HashMap<>();
                        newMap.put("rq",rqs.get(i));
                        newMap.put("sl","0");
                        newMap.put("jcxmmc",name);
                        if(i==0){
                            newMap.put("bl","0");
                        }else{
                            var num="0";
                            for(Map<String, Object> stringMap:productionList){
                                if(name.equals(stringMap.get("jcxmmc"))){
                                    num=String.valueOf(stringMap.get("sl"));
                                }
                            }
                            BigDecimal bigDecimal=new BigDecimal(num);
                            BigDecimal bigDecimal1=new BigDecimal("0");
                            newMap.put("bl",String.valueOf((bigDecimal1.subtract(bigDecimal)).multiply(new BigDecimal("100")).divide(bigDecimal.compareTo(BigDecimal.ZERO)!=0?bigDecimal:new BigDecimal("1"),BigDecimal.ROUND_HALF_UP)));
                        }
                        productionList.add(newMap);
                    }
                }
            }
        }
        return newproductionChanges;
    }

    @Override
    public List<Map<String, Object>> getPlatformChanges(SjxxDto sjxxDto) {
        List<String> rqs = new ArrayList<String>();
        if ("week".equals(sjxxDto.getTj())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            try {
                calendar.setTime(sdf.parse(sjxxDto.getJsrqend()));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            int zq = Integer.parseInt(sjxxDto.getZq());
            for (int i = 0; i < zq; i++)
            {
                rqs.add(sdf.format(calendar.getTime()));
                calendar.add(Calendar.DATE, -7);
            }
        }else {
            rqs = sjxxService.getRqsByStartAndEnd(sjxxDto);
        }
        sjxxDto.setRqs(rqs);
        List<Map<String, Object>> platformChanges = dao.getPlatformChanges(sjxxDto);
        if ("week".equals(sjxxDto.getTj())) {
            rqs = rqs.stream().sorted().collect(Collectors.toList());
        }
        List<Map<String,Object>> platformList=new ArrayList<>();
        List<String> names=new ArrayList<>();
        for (int j = 0; j < platformChanges.size(); j++)
        {
            if (!names.contains(String.valueOf(platformChanges.get(j).get("ptmc")))&&platformChanges.get(j).get("ptmc")!=null){
                names.add(String.valueOf(platformChanges.get(j).get("ptmc")));
            }
        }
        List<Map<String,Object>> newPlatformList=new ArrayList<>();
        if(names!=null&&names.size()>0){
                for (int k=0;k<rqs.size();k++){
                    for (int j=0;j<names.size();j++){
                    boolean flag=false;
                    for (int x=0;x<platformChanges.size();x++){
                        if (rqs.get(k).equals(platformChanges.get(x).get("rq"))&&names.get(j).equals(platformChanges.get(x).get("ptmc"))){
                            flag=true;
                            break;
                        }
                    }
                    if (flag==false){
                        Map<String, Object> map=new HashMap<>();
                        map.put("ptmc",names.get(j));
                        map.put("rq",rqs.get(k));
                        map.put("sl",0);
                        platformChanges.add(map);
                    }
                }
            }
            for (int j=0;j<rqs.size();j++){
                for (int i=0;i<platformChanges.size();i++){
                    if (rqs.get(j).equals(platformChanges.get(i).get("rq"))&&platformChanges.get(i).get("ptmc")!=null){
                        newPlatformList.add(platformChanges.get(i));
                    }
                }
            }

            for(int i=0;i<rqs.size();i++){
                for(String name:names){
                    boolean isFind=false;
                    for(Map<String, Object> map_t:newPlatformList){
                        if(rqs.get(i).equals(map_t.get("rq"))&&name.equals(map_t.get("ptmc"))){
                            if(i==0){
                                map_t.put("bl","0");
                            }else{
                                var num="0";
                                for(Map<String, Object> stringMap:platformList){
                                    if(name.equals(stringMap.get("ptmc"))){
                                        num=String.valueOf(stringMap.get("sl"));
                                    }
                                }
                                BigDecimal bigDecimal=new BigDecimal(num);
                                BigDecimal bigDecimal1=new BigDecimal(String.valueOf(map_t.get("sl")));
                                map_t.put("bl",String.valueOf((bigDecimal1.subtract(bigDecimal)).multiply(new BigDecimal("100")).divide(bigDecimal.compareTo(BigDecimal.ZERO)!=0?bigDecimal:new BigDecimal("1"),BigDecimal.ROUND_HALF_UP)));
                            }
                            platformList.add(map_t);
                            isFind=true;
                            break;
                        }
                    }
                    if(!isFind){
                        Map<String, Object> newMap=new HashMap<>();
                        newMap.put("rq",rqs.get(i));
                        newMap.put("sl","0");
                        newMap.put("ptmc",name);
                        if(i==0){
                            newMap.put("bl","0");
                        }else{
                            var num="0";
                            for(Map<String, Object> stringMap:platformList){
                                if(name.equals(stringMap.get("ptmc"))){
                                    num=String.valueOf(stringMap.get("sl"));
                                }
                            }
                            BigDecimal bigDecimal=new BigDecimal(num);
                            BigDecimal bigDecimal1=new BigDecimal("0");
                            newMap.put("bl",String.valueOf((bigDecimal1.subtract(bigDecimal)).multiply(new BigDecimal("100")).divide(bigDecimal.compareTo(BigDecimal.ZERO)!=0?bigDecimal:new BigDecimal("1"),BigDecimal.ROUND_HALF_UP)));
                        }
                        platformList.add(newMap);
                    }
                }
            }
        }
        return newPlatformList;
    }

    /**
     * 模糊查询内部编码
     * @param nbbm
     * @return
     */
    @Override
    public List<String> getSjxxByLikeNbbm(String nbbm) {
        return dao.getSjxxByLikeNbbm(nbbm);
    }

    @Override
    public List<Map<String, Object>> getPlatformProportion(XszbDto xszbDto) {
        return dao.getPlatformProportion(xszbDto);
    }

    @Override
    public List<Map<String, Object>> getProductionProportion(XszbDto xszbDto) {
        return dao.getProductionProportion(xszbDto);
    }

    @Override
    public List<Map<String, String>> getTopDsf20(SjxxDto sjxxDto) {
        return dao.getTopDsf20(sjxxDto);
    }
	    @Override
    public List<Map<String, String>> getTopZx20(SjxxDto sjxxDto) {
        if ("1".equals(sjxxDto.getSingle_flag())&& ArrayUtils.isEmpty(sjxxDto.getPtgss())){
            List<JcsjDto> jcsjDtos = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.PLATFORM_OWNERSHIP.getCode());
            List<String> pts = new ArrayList<>();
            for (JcsjDto jcsjDto : jcsjDtos) {
                //领导查看
                if ("LEAD".equals(jcsjDto.getCsdm())){
                    pts.add(jcsjDto.getCsid());
                }
            }
            sjxxDto.setPtgss(pts.toArray(new String[0]));
            List<Map<String, String>> topZx20 = dao.getTopZx20(sjxxDto);
            sjxxDto.setPtgss(null);
            return topZx20;
        }else {
            return dao.getTopZx20(sjxxDto);
        }
    }

    @Override
    public List<Map<String, String>> getTopCSO20(SjxxDto sjxxDto) {
        return dao.getTopCSO20(sjxxDto);
    }

    @Override
    public List<Map<String, String>> getTopRY20(SjxxDto sjxxDto) {
        return dao.getTopRY20(sjxxDto);
    }

    @Override
    public List<Map<String, Object>> getChargesDivideByKs(SjxxDto sjxxDto) {
        return dao.getChargesDivideByKs(sjxxDto);
    }

    @Override
    public List<Map<String, Object>> getChargesDivideByYblx(SjxxDto sjxxDto) {
        return dao.getChargesDivideByYblx(sjxxDto);
    }

    @Override
    public List<Map<String, Object>> getHxyyTopList(SjxxDto sjxxDto) {
        return dao.getHxyyTopList(sjxxDto);
    }

    @Override
    public List<Map<String, String>> getAllCountryChangesForSale(SjxxDto sjxxDto) {
        List<String> rqs = new ArrayList<String>();
        if ("week".equals(sjxxDto.getTj())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            try {
                calendar.setTime(sdf.parse(sjxxDto.getJsrqend()));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            int zq = Integer.parseInt(sjxxDto.getZq());
            for (int i = 0; i < zq; i++)
            {
                rqs.add(sdf.format(calendar.getTime()));
                calendar.add(Calendar.DATE, -7);
            }
        }else {
            rqs = sjxxService.getRqsByStartAndEnd(sjxxDto);
        }
        sjxxDto.setRqs(rqs);

        List<Map<String, String>> resultMaps = dao.getAllCountryChangesForSale(sjxxDto);
        if (CollectionUtils.isEmpty(resultMaps)){
            return resultMaps;
        }
        return resultMaps;
    }


    public void weekLeadStatisDefault()
    {

        SjxxDto sjxxDto = new SjxxDto();
        // 如果未设定接收起始日期则自动根据规则设定，星期二之前设定为上一周，星期三以后设定为本周
        int dayOfWeek = DateUtils.getWeek(new Date());
        if (dayOfWeek <= 2)
        {
            sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 8), "yyyy-MM-dd"));
            sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-2), "yyyy-MM-dd"));
            sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
            sjxxDto.setBgrqend(sjxxDto.getJsrqend());
        } else
        {
            sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-1), "yyyy-MM-dd"));
            sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), 5 - dayOfWeek), "yyyy-MM-dd"));
            sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
            sjxxDto.setBgrqend(sjxxDto.getJsrqend());
        }
        sjxxDto.setZq("7");
        if (redisUtil.getExpire("weekLeadStatisDefault")==-2){
            redisUtil.hset("weekLeadStatisDefault","ExpirationTime","1440min",86400);
        }
        List<JcsjDto> dyxxs = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.XXDY_TYPE.getCode());
        for (JcsjDto dyxx : dyxxs) {
            if ("XMFL".equals(dyxx.getCsdm())){
                sjxxDto.setYwlx(dyxx.getCsid());
            }
            if ("HBFL".equals(dyxx.getCsdm())){
                sjxxDto.setYwlx_q(dyxx.getCsid());
            }
        }
        List<String> yxxList = new ArrayList<>();
        if (ArrayUtils.isEmpty(sjxxDto.getYxxs())){
            XxdyDto xxdyDto = new XxdyDto();
            xxdyDto.setKzcs6("1");
            xxdyDto.setDylxcsdm("XMFL");
            List<XxdyDto> dtoList = xxdyService.getYxxMsg(xxdyDto);
            if (!org.apache.commons.collections.CollectionUtils.isEmpty(dtoList)){
                List<String> yxxs = new ArrayList<>();
                for (XxdyDto dto : dtoList) {
                    yxxs.add(dto.getYxx());
                }
                yxxList = yxxs;
                sjxxDto.setYxxs(yxxs.toArray(new String[0]));
            }
        }
        sjxxDto.setTjtj("NOW");
        String jsrqStart=sjxxDto.getJsrqstart();
        sjxxDto.setTj("mon");
        setDateByMonth(sjxxDto,-6);
        List<String> rqs = sjxxService.getRqsByStartAndEnd(sjxxDto);
        sjxxDto.setRqs(rqs);
        sjxxDto.setJsrqstart(jsrqStart);

        String key="";

        //循环平台
        List<JcsjDto> ptList = redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.PLATFORM_OWNERSHIP.getCode());
        ptList.add(new JcsjDto());//增加一个普通为空的，及无平台的
        for (JcsjDto pt_jcsj : ptList){
            String[] ptidstr = new String[1];
            ptidstr[0] = pt_jcsj.getCsid();
            if (StringUtil.isNotBlank(ptidstr[0])){
                sjxxDto.setPtgss(ptidstr);
            }
            String yhid = sjxxDto.getYhid();

            //全国趋势
            List<Map<String, String>> qgqsnums = new ArrayList<>();
            if (StringUtil.isNotBlank(ptidstr[0])){
                key = "jsrq_qgqsnums"+"_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
                qgqsnums = getAllCountryChangesForSale(sjxxDto);
            }else{
                key = "jsrq_qgqsnums"+"_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
                qgqsnums = getAllCountryChanges(sjxxDto);
            }
            redisUtil.hset("weekLeadStatisDefault",key, JSON.toJSONString(qgqsnums));

            //产品趋势
            if (StringUtil.isNotBlank(ptidstr[0]))
                key = "jsrq_cpqstjnums" + "_" + sjxxDto.getZq() + "_" + StringUtil.join(sjxxDto.getYxxs(), ",") + "_" + StringUtil.join(sjxxDto.getPtgss(), ",");
            else
                key = "jsrq_cpqstjnums" + "_" + sjxxDto.getZq() + "_" + StringUtil.join(sjxxDto.getYxxs(), ",");
            List<Map<String, Object>> cpqstjnums = getProductionChanges(sjxxDto);
            redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(cpqstjnums));

            //平台趋势(无平台页面)
            if (StringUtil.isBlank(ptidstr[0])){
                key = "jsrq_ptqstjnums"+"_"+sjxxDto.getZq();
                List<Map<String, Object>> ptqstjnums = getPlatformChanges(sjxxDto);
                redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(ptqstjnums));
            }

            //计算季度时间
            String currentYear1 = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
            Calendar calendar = Calendar.getInstance();
            int monthDay = calendar.get(Calendar.DAY_OF_MONTH);
            int currenMonth = 0;
            if (monthDay>7){
                currenMonth = calendar.get(Calendar.MONTH)+1;
            }else {
                currenMonth = calendar.get(Calendar.MONTH);
            }
            String monthEnd = String.valueOf((currenMonth+2)/3*3);
            String monthStart = String.valueOf(((currenMonth-1)/3*3)+1);
            String start1 = currentYear1 + "-" + ( monthStart.length()<2? ("0"+monthStart):monthStart);
            String end1 = currentYear1 + "-" + (monthEnd.length()<2? ("0"+monthEnd):monthEnd);

            //平台销售达成率
            XszbDto xszbDto1 = new XszbDto();
            xszbDto1.setKszq(start1);
            xszbDto1.setJszq(end1);
            xszbDto1.setZblxcsmc("Q");
            yxxList.add("Resfirst");
            xszbDto1.setXms(yxxList);
            xszbDto1.setYwlx(sjxxDto.getYwlx());
            if (StringUtil.isNotBlank(ptidstr[0])) {
                xszbDto1.setPtgss(sjxxDto.getPtgss());
                List<String> ptgss = new ArrayList<>();
                if(StringUtil.isNotBlank(sjxxDto.getYhid())){//当从钉钉消息进入
                    ptgss=sjhbxxService.getPtgsByYhid(sjxxDto.getYhid());
                    xszbDto1.setPtgss(ptgss.toArray(new String[ptgss.size()]));
                }
                key = yhid + "jsrq_ptzbdcl" + "_" + xszbDto1.getKszq() + "~" + xszbDto1.getJszq() + "_" + StringUtil.join(sjxxDto.getYxxs(), ",") + "_" + StringUtil.join(sjxxDto.getPtgss(), ",");
            }else
                key = "jsrq_ptzbdcl"+"_"+xszbDto1.getKszq() + "~" + xszbDto1.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
            List<Map<String, Object>> ptzbdclnums = sjxxStatisticService.getPtzbdcl(xszbDto1);
            redisUtil.hset("weekLeadStatisDefault", key,JSON.toJSONString(ptzbdclnums));


            //平台业务占比（无平台页面）
            if (StringUtil.isBlank(ptidstr[0])){
                xszbDto1.setYwlx(sjxxDto.getYwlx());
                xszbDto1.setYwlx_q(sjxxDto.getYwlx_q());
                xszbDto1.setXms(Arrays.asList(sjxxDto.getYxxs()));
                key = "jsrq_ptywzbtjnums"+"_"+xszbDto1.getKszq() + "~" + xszbDto1.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
                List<Map<String, Object>> ptywzbtjnums = getPlatformProportion(xszbDto1);
                redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(ptywzbtjnums));
            }

            //产品业务占比
            xszbDto1.setYwlx(sjxxDto.getYwlx());
            xszbDto1.setYwlx_q(sjxxDto.getYwlx_q());
            xszbDto1.setXms(Arrays.asList(sjxxDto.getYxxs()));
            if (StringUtil.isNotBlank(ptidstr[0]))
                key = "jsrq_cpywzbtjnums"+"_"+xszbDto1.getKszq() + "~" + xszbDto1.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+StringUtil.join(xszbDto1.getPtgss(),",");
            else
                key = "jsrq_cpywzbtjnums"+"_"+xszbDto1.getKszq() + "~" + xszbDto1.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
            List<Map<String, Object>> cpywzbtjnums = getProductionProportion(xszbDto1);
            redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(cpywzbtjnums));

            //伙伴分类测试数占比
            XxdyDto xxdyDto=new XxdyDto();
            xxdyDto.setCskz1("JCSJ");
            xxdyDto.setCskz2(BasicDataTypeEnum.CLASSIFY.getCode());
            List<XxdyDto> xxdyDtos = xxdyService.getListGroupByYxx(xxdyDto);
            List<Map<String, String>> newList=new ArrayList<>();
            if(xxdyDtos!=null&&xxdyDtos.size()>0){
                SjxxDto sjxxDto_t=new SjxxDto();
                sjxxDto_t.setJsrqstart(start1);
                sjxxDto_t.setJsrqend(end1);
                sjxxDto_t.setYwlx(sjxxDto.getYwlx());
                sjxxDto_t.setYwlx_q(sjxxDto.getYwlx_q());
                sjxxDto_t.setYxxs(sjxxDto.getYxxs());
                if (StringUtil.isNotBlank(ptidstr[0])) {
                    sjxxDto_t.setPtgss(sjxxDto.getPtgss());
                    sjxxDto_t.setYhid(sjxxDto.getYhid());
                    key = "jsrq_getHbflcsszb_" + start1 + "~" + end1 + "_" + StringUtil.join(sjxxDto.getYxxs(), ",") + "_" + StringUtil.join(sjxxDto.getPtgss(), ",") + "_" + sjxxDto.getYhid();
                }else {
                    sjxxDto_t.setXg_flg("1");
                    key = "jsrq_getHbflcsszb_" + start1 + "~" + end1 + "_" + StringUtil.join(sjxxDto.getYxxs(), ",");
                }
                List<Map<String, String>> hbflcsszbMap = sjxxStatisticService.getHbflcsszb(sjxxDto_t);
                for(XxdyDto xxdyDto_t:xxdyDtos){
                    boolean isFind=false;
                    for(Map<String, String> map_t:hbflcsszbMap){
                        if(xxdyDto_t.getYxx().equals(map_t.get("yxx"))){
                            newList.add(map_t);
                            isFind=true;
                            break;
                        }
                    }
                    if(!isFind){
                        Map<String, String> newMap=new HashMap<>();
                        newMap.put("num","0");
                        newMap.put("yxx",xxdyDto_t.getYxx());
                        newList.add(newMap);
                    }
                }
                redisUtil.hset("weekLeadStatisDefault", key,JSON.toJSONString(newList));
            }

            //送检区分测试数占比
            List<JcsjDto> sjqfs = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.INSPECTION_DIVISION.getCode());
            if (StringUtil.isNotBlank(ptidstr[0]))
                key = "jsrq_getSjqfcsszb_"+start1 + "~" + end1+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid();
            else
                key = "jsrq_getSjqfcsszb_"+start1 + "~" + end1+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
            List<Map<String, String>> sjqfList=new ArrayList<>();
            JcsjDto jcsjDto1 = new JcsjDto();
            jcsjDto1.setCsid("第三方实验室");
            jcsjDto1.setCsmc("第三方实验室");
            sjqfs.add(jcsjDto1);
            JcsjDto jcsjDto2 = new JcsjDto();
            jcsjDto2.setCsid("直销");
            jcsjDto2.setCsmc("直销");
            sjqfs.add(jcsjDto2);
            JcsjDto jcsjDto3 = new JcsjDto();
            jcsjDto3.setCsid("CSO");
            jcsjDto3.setCsmc("CSO");
            sjqfs.add(jcsjDto3);
            if(sjqfs!=null&&sjqfs.size()>0){
                SjxxDto sjxxDto_t=new SjxxDto();
                sjxxDto_t.setJsrqstart(start1);
                sjxxDto_t.setJsrqend(end1);
                sjxxDto_t.setYwlx(sjxxDto.getYwlx());
                sjxxDto_t.setYxxs(sjxxDto.getYxxs());
                sjxxDto_t.setNewflg("zb");
                List<Map<String, String>> sjqfcsszb = sjxxStatisticService.getSjqfcsszb(sjxxDto_t);
                for(JcsjDto jcsjDto:sjqfs){
//                    boolean isFind=false;
                    for(Map<String, String> map_t:sjqfcsszb){
                        if(jcsjDto.getCsid().equals(map_t.get("sjqf"))){
                            map_t.put("sjqfmc",jcsjDto.getCsmc());
                            sjqfList.add(map_t);
//                            isFind=true;
                            break;
                        }
                    }
//                    if(!isFind){
//                        Map<String, String> newMap=new HashMap<>();
//                        newMap.put("num","0");
//                        newMap.put("sjqfmc",jcsjDto.getCsmc());
//                        sjqfList.add(newMap);
//                    }
                }
            }
            redisUtil.hset("weekLeadStatisDefault", key,JSON.toJSONString(sjqfList));

            //标本信息检测总条数
            List<Map<String, String>> jcxmnum = new ArrayList<>();
            if (StringUtil.isNotBlank(ptidstr[0])){
                key = yhid + "_jcxmnum_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",") +"_"+ StringUtil.join(sjxxDto.getYxxs(),",");
                if ("week".equals(sjxxDto.getTj())){
                    jcxmnum = sjxxStatisticService.getYbxxDR_weeekByJsrq(sjxxDto);
                }else {
                    jcxmnum = sjxxStatisticService.getYbxxDRByWeeklyAndJsrq(sjxxDto);
                }
            }else{
                key = "jsrq_jcxmnum"+"_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
                if ("week".equals(sjxxDto.getTj())){
                    jcxmnum = sjxxService.getSjxxDRByWeekAndJsrq(sjxxDto);
                }else {
                    jcxmnum = sjxxService.getSjxxDRBySyAndJsrq(sjxxDto);
                }
            }
            redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(jcxmnum));

            //收费标本下边检测项目的总条数
            List<Map<String, String>> ybxxType = new ArrayList<>();
            if (StringUtil.isNotBlank(ptidstr[0])){
                key = yhid + "_ybxxType_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",") +"_"+ StringUtil.join(sjxxDto.getYxxs(),",");
                if ("week".equals(sjxxDto.getTj())){
                    ybxxType = sjxxStatisticService.getYbxxType_weekByJsrq(sjxxDto);
                }else {
                    ybxxType = sjxxStatisticService.getYbxxTypeByWeeklyAndJsrq(sjxxDto);
                }
            }else{
                key = "jsrq_ybxxType"+"_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
                if ("week".equals(sjxxDto.getTj())){
                    ybxxType = sjxxService.getWeekYbxxTypeByJsrq(sjxxDto);
                }else {
                    ybxxType = sjxxService.getYbxxTypeBYWeekAndJsrq(sjxxDto);
                }
            }
            redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(ybxxType));

            //销售性质测试数趋势
            List<Map<String, String>> xsxzcssqsList=new ArrayList<>();
            if (StringUtil.isNotBlank(ptidstr[0])) {
                key = "jsrq_getXsxzcssqszqs_" + sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend() + "_" + StringUtil.join(sjxxDto.getYxxs(), ",") + "_" + StringUtil.join(sjxxDto.getPtgss(), ",") + "_" + sjxxDto.getYhid();
            }else {
                key = "jsrq_getXsxzcssqszqs_" + sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend() + "_" + StringUtil.join(sjxxDto.getYxxs(), ",");
            }
            if(sjqfs!=null&&sjqfs.size()>0){
                sjxxDto.setNewflg("qs");
                List<Map<String, String>> sjqfcsszb = sjxxStatisticService.getSjqfcsszb(sjxxDto);
                for(int i=0;i<rqs.size();i++){
                    for(JcsjDto jcsjDto:sjqfs){
                        boolean isFind=false;
                        for(Map<String, String> map_t:sjqfcsszb){
                            if(rqs.get(i).equals(map_t.get("rq"))&&jcsjDto.getCsid().equals(map_t.get("sjqf"))){
                                map_t.put("sjqfmc",jcsjDto.getCsmc());
                                if(i==0){
                                    map_t.put("bl","0");
                                }else{
                                    var num="0";
                                    for(Map<String, String> stringMap:xsxzcssqsList){
                                        if(jcsjDto.getCsid().equals(stringMap.get("sjqf"))){
                                            num=stringMap.get("num");
                                        }
                                    }
                                    BigDecimal bigDecimal=new BigDecimal(num);
                                    BigDecimal bigDecimal1=new BigDecimal(map_t.get("num"));
                                    map_t.put("bl",String.valueOf((bigDecimal1.subtract(bigDecimal)).multiply(new BigDecimal("100")).divide(bigDecimal.compareTo(BigDecimal.ZERO)!=0?bigDecimal:new BigDecimal("1"),BigDecimal.ROUND_HALF_UP)));
                                }
                                xsxzcssqsList.add(map_t);
                                isFind=true;
                                break;
                            }
                        }
                        if(!isFind){
                            Map<String, String> newMap=new HashMap<>();
                            newMap.put("rq",rqs.get(i));
                            newMap.put("num","0");
                            newMap.put("sjqfmc",jcsjDto.getCsmc());
                            newMap.put("sjqf",jcsjDto.getCsid());
                            if(i==0){
                                newMap.put("bl","0");
                            }else{
                                var num="0";
                                for(Map<String, String> stringMap:xsxzcssqsList){
                                    if(jcsjDto.getCsid().equals(stringMap.get("sjqf"))){
                                        num=stringMap.get("num");
                                    }
                                }
                                BigDecimal bigDecimal=new BigDecimal(num);
                                BigDecimal bigDecimal1=new BigDecimal("0");
                                newMap.put("bl",String.valueOf((bigDecimal1.subtract(bigDecimal)).multiply(new BigDecimal("100")).divide(bigDecimal.compareTo(BigDecimal.ZERO)!=0?bigDecimal:new BigDecimal("1"),BigDecimal.ROUND_HALF_UP)));
                            }
                            xsxzcssqsList.add(newMap);
                        }
                    }
                }
            }
            redisUtil.hset("weekLeadStatisDefault", key,JSON.toJSONString(xsxzcssqsList));

            //业务发展部达成率
            XszbDto xszbDto_t = new XszbDto();
            xszbDto_t.setKszq(start1);
            xszbDto_t.setJszq(end1);
            xszbDto_t.setZblxcsmc("Q");
            xszbDto_t.setXms(yxxList);
            xszbDto_t.setYwlx(sjxxDto.getYwlx());
            if (StringUtil.isNotBlank(ptidstr[0])) {
                xszbDto_t.setPtgss(sjxxDto.getPtgss());
                key = "jsrq_ywzrfz" + "_" + xszbDto1.getKszq() + "~" + xszbDto1.getJszq() + "_" + StringUtil.join(sjxxDto.getYxxs(), ",") + "_" + StringUtil.join(sjxxDto.getPtgss(), ",");
            }else {
                key = "jsrq_ywfzb" + "_" + xszbDto_t.getKszq() + "~" + xszbDto_t.getJszq() + "_" + StringUtil.join(sjxxDto.getYxxs(), ",");
            }
            List<Map<String, Object>> ywfzbnums = sjxxStatisticService.getYwfzbZbdcl(xszbDto_t);
            redisUtil.hset("weekLeadStatisDefault", key,JSON.toJSONString(ywfzbnums));

            sjxxDto.setTj("week");
            //在service里进行加入 特检销售发展部 平台限制（直销，CSO）
            sjxxDto.setSingle_flag("1");
            //Top20第三方
            if (StringUtil.isNotBlank(ptidstr[0]))
                key = yhid + "jsrq_topDsf20"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
            else
                key = "jsrq_topDsf20"+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
            List<Map<String, String>> topDsf20 = getTopDsf20(sjxxDto);
            redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(topDsf20));

            //Top20直销
            if (StringUtil.isNotBlank(ptidstr[0]))
                key = yhid + "jsrq_topZx20"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
            else
                key = "jsrq_topZx20"+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
            List<Map<String, String>> topZx20 = getTopZx20(sjxxDto);
            redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(topZx20));

            //Top20CSO
            if (StringUtil.isNotBlank(ptidstr[0])) {
                key = yhid + "jsrq_topCSO20" + "_" + StringUtil.join(sjxxDto.getYxxs(), ",") + "_" + StringUtil.join(sjxxDto.getPtgss(), ",");
            }else {
                key = "jsrq_topCSO20" + "_" + StringUtil.join(sjxxDto.getYxxs(), ",");
                sjxxDto.setXg_flg("1");//代表领导页面进入进入，用于判断CSOTOP图将其他平台的伙伴进行归类
            }
            List<Map<String, String>> topCSO20 = getTopCSO20(sjxxDto);
            redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(topCSO20));

            //Top20RY
            if (StringUtil.isNotBlank(ptidstr[0]))
                key = yhid +  "jsrq_topRY20"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
            else
                key = "jsrq_topRY20"+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
            List<Map<String, String>> topRY20 = getTopRY20(sjxxDto);
            redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(topRY20));

            //2.5、TOP10核心医院排行
            if (StringUtil.isNotBlank(ptidstr[0]))
                key = yhid + "jsrq_topHxyxList"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
            else
                key = "jsrq_topHxyxList"+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
            List<Map<String, Object>> topHxyxList = getHxyyTopList(sjxxDto);
            redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(topHxyxList));

            //特检销售各区域达成率，初始化按当前季度显示
            XszbDto tjXszb = new XszbDto();
            tjXszb.setZblxcsmc("Q");
            tjXszb.setKszq(xszbDto1.getKszq());
            tjXszb.setJszq(xszbDto1.getJszq());
            tjXszb.setXms(yxxList);
            tjXszb.setYwlx(sjxxDto.getYwlx());
//            Map<String,List<Map<String,String>>> tjxsmap = new HashMap<>();//查平台基础数据
            JcsjDto jcsj_zbfl = new JcsjDto();
            jcsj_zbfl.setJclb(BasicDataTypeEnum.SALE_CLASSIFY.getCode());
            jcsj_zbfl.setCsdm("TJXSTJ");
            jcsj_zbfl = jcsjService.getDto(jcsj_zbfl);
            JcsjDto jcsj_t = new JcsjDto();
            jcsj_t.setFcsid(jcsj_zbfl.getCsid());
            tjXszb.setZbfl(jcsj_zbfl.getCsid());
            List<JcsjDto> tjxs_zfls = jcsjService.getDtoList(jcsj_t);//取特检销售的子分类：区域
            if (tjxs_zfls != null){
                for (JcsjDto jcsj : tjxs_zfls){
                    tjXszb.setQyid(jcsj.getCsid());
                    tjXszb.setCskz3(jcsj.getCskz1());
                    tjXszb.setCskz2(jcsj.getCskz2());
                    tjXszb.setZbzfl(jcsj.getCsid());
                    //由于外部统计数据，需要查找区域下的伙伴去限制外部统计数据范围
                    List<String> hbids = xszbService.getDtosByZfl(tjXszb);
                    tjXszb.setHbids(hbids);
                    key = "jsrq_tjxsdcl"+"_"+jcsj.getCsid()+"_"+ tjXszb.getKszq()+ "~" + tjXszb.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
                    List<Map<String,String>> list = sjxxStatisticService.getTjsxdcl(tjXszb);
                    redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(list));
//                    tjxsmap.put(jcsj.getCsid() , list);
                }
            }

            if (StringUtil.isNotBlank(ptidstr[0]))
                sjxxDto.setTj("week");
            else
                sjxxDto.setTj("mon");
            sjxxDto.setJsrqMstart(start1);
            sjxxDto.setJsrqMend(end1);
            //2.4、收费科室分布（按照收费测试数统计）
            if (StringUtil.isNotBlank(ptidstr[0]))
                key = yhid + "jsrq_ksSfcssList"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
            else
                key = "jsrq_ksSfcssList"+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
            List<Map<String, Object>> ksSfcssList = getChargesDivideByKs(sjxxDto);
            redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(ksSfcssList));

            //2.5、收费标本类型分布（按照收费测试数统计
            if (StringUtil.isNotBlank(ptidstr[0]))
                key = yhid + "jsrq_yblxSfcssList"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
            else
                key = "jsrq_yblxSfcssList"+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
            List<Map<String, Object>> yblxSfcssList  = getChargesDivideByYblx(sjxxDto);
            redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(yblxSfcssList));

        }

        //若过期时间为-1，重新设置过期时间
        if (redisUtil.getExpire("weekLeadStatisDefault")==-1){
            redisUtil.hset("weekLeadStatisDefault","ExpirationTime","1440min",86400);
        }
    }

    /**
     * 设置按月查询的日期
     * @param sjxxDto
     * @param length 长度。要为负数，代表向前推移几月
     */
    private void setDateByMonth(SjxxDto sjxxDto,int length) {
        setDateByMonth(sjxxDto,sjxxDto.getJsrqstart(),length);
    }

    /**
     * 设置按月查询的日期
     * @param sjxxDto
     * @param date 标准日期
     * @param length 长度。要为负数，代表向前推移几月
     */
    private void setDateByMonth(SjxxDto sjxxDto,String date,int length) {
        SimpleDateFormat monthSdf=new SimpleDateFormat("yyyy-MM");

        Calendar calendar=Calendar.getInstance();
        if(StringUtil.isNotBlank(date)) {
            Date jsDate = DateUtils.parseDate("yyyy-MM-dd",date);
            calendar.setTime(jsDate);
        }
        if ("NOW".equals(sjxxDto.getTjtj())){
            Date nowDate = new Date();
            calendar.setTime(nowDate);
            int monthDay=calendar.get(Calendar.DAY_OF_MONTH);
            if (monthDay<7){
                calendar.add(Calendar.MONTH,-1);
            }
        }
        if(length>=0) {
            sjxxDto.setJsrqMstart(monthSdf.format(calendar.getTime()));
            calendar.add(Calendar.MONTH,length);
            sjxxDto.setJsrqMend(monthSdf.format(calendar.getTime()));
        }else {
            sjxxDto.setJsrqMend(monthSdf.format(calendar.getTime()));
            calendar.add(Calendar.MONTH,length);
            sjxxDto.setJsrqMstart(monthSdf.format(calendar.getTime()));
        }
        sjxxDto.setBgrqMstart(sjxxDto.getJsrqMstart());
        sjxxDto.setBgrqMend(sjxxDto.getJsrqMend());

        sjxxDto.setJsrqstart(null);
    }


    /**
     * 获取申请单信息
     * @param request
     * @return
     */
    public Map<String, Object> getApplicationDetail(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            Map<String, Object> searchMap = new HashMap<>();
            // 处理伙伴限制,以及相应伙伴的项目限制
            dealEntrustDbs(searchMap, request);
            // 设置查询方法
            String barcode = request.getParameter("barcode");
            if (StringUtil.isNotBlank(barcode)) {
                // 若传递了barcode，则根据barcode查询申请单信息
                searchMap.put("barcode", barcode);
            } else {
                // 若未传递barcode，则根据时间范围查询申请单信息
                dealEntrustTimes(searchMap, request);
            }
            // 根据searchMap查询送检信息数据
            List<Map<String, Object>> sjxxList = dao.getEntrustDtoList(searchMap);
            log.error("委外接口-getApplicationDetail-获取数据成功:data.size():{}",sjxxList.size());
            map.put("data", sjxxList);
            map.put("errorCode", 0);
            map.put("errorInfo", "操作成功!");
            map.put("status", "success");
        } catch (Exception e) {
            log.error("委外接口-getApplicationDetail-获取数据失败:{}",e.getMessage());
            map.put("status", "fail");
            map.put("errorInfo", e.getMessage());
            map.put("errorCode", -1);
            return map;
        }
        return map;
    }

    /**
     * 委外接口处理伙伴限制,以及相应伙伴的项目限制
     * @param searchMap
     * @param request
     */
    public void dealEntrustDbs(Map<String, Object> searchMap ,HttpServletRequest request ) throws RuntimeException {
        // 设置伙伴权限
        String organ = request.getParameter("organ");
        String db = request.getParameter("db");
        WbhbyzDto wbhbyzDto = new WbhbyzDto();
        wbhbyzDto.setCode(organ);
        List<WbhbyzDto> wbhbyzDtos = wbhbyzService.getListByCode(wbhbyzDto);
        if (CollectionUtils.isEmpty(wbhbyzDtos)) {
            throw new RuntimeException("未设置相关账号的伙伴权限,请联系管理员设置!");
        }
        List<Map<String, Object>> limit = new ArrayList<>();
        for (WbhbyzDto dto : wbhbyzDtos) {
            if (StringUtil.isNotBlank(db) && dto.getHbmc().equals(db)){
                limit.clear();
                Map<String, Object> limitMap = new HashMap<>();
                limitMap.put("hbmc",dto.getHbmc());
                limitMap.put("hbid",dto.getHbid());
                if (StringUtil.isNotBlank(dto.getSz())){
                    try {
                        Map<String, String> settings = JSON.parseObject(dto.getSz(), Map.class);
                        if (settings != null && settings.containsKey("limitJcxm") && settings.get("limitJcxm") != null){
                            String[] jcxm = settings.get("limitJcxm").split(",");
                            limitMap.put("jcxmdms", Arrays.asList(jcxm));
                        }
                        if (settings != null && settings.containsKey("limitJczxm") && settings.get("limitJczxm") != null){
                            String[] jczxm = settings.get("limitJczxm").split(",");
                            limitMap.put("jczxmdms", Arrays.asList(jczxm));
                        }
                    } catch (Exception e) {
                        log.error("设置伙伴权限时，解析权限配置出错，请检查配置是否正确！{},{}",dto.getHbid(),dto.getSz());
                    }
                }
                limit.add(limitMap);
                searchMap.put("limit", limit);
                return;
            } else if (StringUtil.isBlank(db)){
                Map<String, Object> limitMap = new HashMap<>();
                limitMap.put("hbmc",dto.getHbmc());
                limitMap.put("hbid",dto.getHbid());
                if (StringUtil.isNotBlank(dto.getSz())){
                    try {
                        Map<String, String> settings = JSON.parseObject(dto.getSz(), Map.class);
                        if (settings != null && settings.containsKey("limitJcxm") && settings.get("limitJcxm") != null){
                            String[] jcxm = settings.get("limitJcxm").split(",");
                            limitMap.put("jcxmdms", Arrays.asList(jcxm));
                        }
                    } catch (Exception e) {
                        log.error("设置伙伴权限时，解析权限配置出错，请检查配置是否正确！{},{}",dto.getHbid(),dto.getSz());
                    }
                }
                limit.add(limitMap);
            }
        }
        searchMap.put("limit", limit);
    }

    /**
     * 委外接口处理时间限制
     * @param searchMap
     * @param request
     */
    public void dealEntrustTimes(Map<String, Object> searchMap ,HttpServletRequest request ) throws RuntimeException {
        String timeFormatStr = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf_time = new SimpleDateFormat(timeFormatStr);
        String nowTime = sdf_time.format(Calendar.getInstance().getTime());
        String endTime = request.getParameter("endTime");
        if (StringUtil.isBlank(endTime)){
            // 若entdTime为空,则设置当前时间为endTime,格式为yyyy-MM-dd HH:mm:ss
            endTime = nowTime;
        }
        String startTime = request.getParameter("startTime");
        // 若startTime, endTime 均不为空,且符合yyyy-MM-dd HH:mm:ss,则继续,否则抛出异常
        if (StringUtil.isAnyBlank(startTime, endTime)) {
            throw new RuntimeException("未获取到startTime或endTime信息！");
        }
        if (!DateUtils.isValidDate(startTime, timeFormatStr) || !DateUtils.isValidDate(endTime, timeFormatStr)) {
            throw new RuntimeException("startTime或endTime格式不正确,格式为:" + timeFormatStr + "！");
        }
        // 获取startTime 和 endTime 的时间差
        Calendar end = null;
        Calendar start = null;
        try {
            end = Calendar.getInstance();
            end.setTime(sdf_time.parse(endTime));
            start = Calendar.getInstance();
            start.setTime(sdf_time.parse(startTime));
        } catch (ParseException e) {
            throw new RuntimeException("startTime或endTime格式不正确,格式为:" + timeFormatStr + "！");
        }
        long timeDiffSeconds = (end.getTimeInMillis() - start.getTimeInMillis())/(1000);
        if (timeDiffSeconds <= 0 ) {
            // 若 时间差小于等于0，则抛出异常
            throw new RuntimeException("startTime与endTime格式不正确,startTime大于endTime！");
        }
        // 判断时间间隔是否超出设定
        int maxSearchDateTnterval = 60;
        try {
            Object maxSearchDateTntervalObj = redisUtil.hget("matridx_xtsz", "entrust.maxSearchDateTnterval");// 最大搜索时间间隔
            if (maxSearchDateTntervalObj != null) {
                XtszDto xtszDto = JSON.parseObject(maxSearchDateTntervalObj.toString(), XtszDto.class);
                maxSearchDateTnterval = Integer.parseInt(xtszDto.getSzz());
            }
        } catch (NumberFormatException e) {
            log.error("maxSearchDateTnterval设置错误！采用默认maxSearchDateTnterval=60天！");
        }
        if (timeDiffSeconds > maxSearchDateTnterval * 86400) {
            // 若时间间隔超出设定，则抛出异常
            throw new RuntimeException("startTime与endTime的时间差超过限定范围,默认时间间隔最大为:" + maxSearchDateTnterval + "天！");
        }
        int maxSearchDateLength = 1000;
        try {
            Object maxSearchDateLengthObj = redisUtil.hget("matridx_xtsz", "entrust.maxSearchDateLength");// 最大搜索时间范围
            if (maxSearchDateLengthObj != null) {
                XtszDto xtszDto = JSON.parseObject(maxSearchDateLengthObj.toString(), XtszDto.class);
                maxSearchDateLength = Integer.parseInt(xtszDto.getSzz());
            }
        } catch (NumberFormatException e) {
            log.error("maxSearchDateLength设置错误！采用默认maxSearchDateLength=1000天！");
        }
        Calendar now = null;
        try {
            now = Calendar.getInstance();
            now.setTime(sdf_time.parse(nowTime));
        } catch (ParseException e) {
            throw new RuntimeException("startTime或endTime格式不正确,格式为:" + timeFormatStr + "！");
        }
        long beforeDiffSeconds = (now.getTimeInMillis() - start.getTimeInMillis())/(1000);
        if (beforeDiffSeconds <= 0 ) {
            // 若 时间差小于等于0，则抛出异常
            throw new RuntimeException("startTime格式不正确,startTime不可超过当前时间！");
        }
        if (beforeDiffSeconds > maxSearchDateLength * 86400) {
            // 若时间间隔超出设定，则抛出异常
            throw new RuntimeException("startTime超过最早可查询日期,默认最早可查询时间为距现在:" + maxSearchDateLength + "天！");
        }
        searchMap.put("startTime", startTime);
        searchMap.put("endTime", endTime);
    }

    /**
     * 获取样本报告
     * @param request
     * @param barcode
     * @param jcxmcsdm
     * @return
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public Map<String,Object> sendSampleReport(HttpServletRequest request, String barcode, String jcxmcsdm){
        Map<String,Object> map = new HashMap<>();
        try {
            String timeFormatStr = "yyyy-MM-dd HH:mm:ss";
            String organ = request.getParameter("organ");
            //根据样本编号和检测项目代码获取样本信息和实验数据
            Map<String, Object> searchMap = new HashMap<>();
            searchMap.put("barcode", barcode);
            searchMap.put("jcxmcsdm", jcxmcsdm);
            List<Map<String, Object>> sampleInfos = getSampleInfoByBarcode(searchMap, request);
            if (org.apache.commons.collections.CollectionUtils.isEmpty(sampleInfos)) {
                throw new RuntimeException("相关权限下未获取到相关样本数据!");
            }
            //保存文件只临时文件夹
            FjcfbDto fjcfbDto = new FjcfbDto();
            fjcfbDto.setLrry(organ);
            fjcfbDto.setWjm(request.getParameter("filename"));
            fjcfbDto.setYwid((String)sampleInfos.get(0).get("sjid"));
            fjcfbDto.setYwlx((String)sampleInfos.get(0).get("jcxmcskz3") + "_" + (String)sampleInfos.get(0).get("jcxmcskz1"));
            boolean isUploadSuccess = uploadReportFile(request, fjcfbDto);
            log.error("委外接口-sendSampleReport-保存文件至临时文件夹:{}",fjcfbDto.getLsbcdz());
            if (!isUploadSuccess || StringUtil.isBlank(fjcfbDto.getFjid())){
                throw new RuntimeException("文件处理失败,请检查文件或者联系管理员!");
            }
            // 解析data中参数
            String data = request.getParameter("data");
            Map<String, Object> dataMap = JSON.parseObject(data, Map.class);
            if (dataMap != null){
                dataMap.put("barcode", barcode);
                dataMap.put("jcxmcsdm", jcxmcsdm);
            }
            Map<String, Object> reportUpdateMap = new HashMap<>();
            reportUpdateMap.put("sjid",fjcfbDto.getYwid());
            if (dataMap != null && dataMap.containsKey("receiveTime")) {
                if (!DateUtils.isValidDate((String) dataMap.get("receiveTime"), timeFormatStr)){
                    throw new RuntimeException("接收时间格式不正确，请输入yyyy-MM-dd HH:mm:ss格式!");
                }
                reportUpdateMap.put("jsrq",(String) dataMap.get("receiveTime"));
            }
            dao.updateReportInfo(reportUpdateMap);
            // 保存文件数据
            String sjid = (String) sampleInfos.get(0).get("sjid");
            fjcfbDto.setYwid(sjid);
            SjxxDto sjxxDto = new SjxxDto();
            sjxxDto.setLrry(organ);
            sjxxDto.setXgry(organ);
            sjxxDto.setYwlx(fjcfbDto.getYwlx());
            List<String> fjids = new ArrayList<>();
            fjids.add(fjcfbDto.getFjid());
            sjxxDto.setFjids(fjids);
            sjxxDto.setSjid(fjcfbDto.getYwid());
            sjxxDto.setCskz1((String)sampleInfos.get(0).get("jcxmcskz1"));
            sjxxDto.setCskz3((String)sampleInfos.get(0).get("jcxmcskz3"));
            if (dataMap != null && dataMap.containsKey("reportTime")) {
                if (!DateUtils.isValidDate((String) dataMap.get("reportTime"), timeFormatStr)){
                    throw new RuntimeException("接收时间格式不正确，请输入yyyy-MM-dd HH:mm:ss格式!");
                }
                sjxxDto.setBgrq((String) dataMap.get("reportTime"));
            } else {
                // 设置报告日期为当前时间
                // 获取当前时间,timeFormatStr格式
                SimpleDateFormat format = new SimpleDateFormat(timeFormatStr);
                sjxxDto.setBgrq(format.format(new Date()));
            }
            boolean isSuccess = sjxxService.uploadSaveReport(sjxxDto);
            // 将文件保存到正式文件夹内
            map.put("status", "success");
            map.put("errorInfo", "操作成功!");
            map.put("errorCode", 0);
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("errorInfo", e.getMessage());
            map.put("errorCode", -1);
        }
        return map;
    }

    /**
     * @param dateStr
     * @return
     */
    @Override
    public List<Map<String, Object>> getDateSjxxList(String dateStr) {
        return dao.getDateSjxxList(dateStr);
    }

    /**
     * 根据barcode和检测项目代码查询送检信息数据
     * @param searchMap
     * @return
     */
    public List<Map<String,Object>> getSampleInfoByBarcode(Map<String, Object> searchMap ,HttpServletRequest request){
        // 处理伙伴限制,以及相应伙伴的项目限制
        dealEntrustDbs(searchMap, request);
        return dao.getSampleInfoByBarcode(searchMap);
    }
    /**
     * 上传文件并保存至临时文件夹中
     * @param request
     * @return
     */
    public boolean uploadReportFile(HttpServletRequest request,FjcfbDto fjcfbDto) {
        // 设置可上传文件类型
        List<String> fileTypes = new ArrayList<>();
        fileTypes.add(".pdf");
        try{
            String filename = StringUtil.isNotBlank(request.getParameter("filename"))?request.getParameter("filename"):"";
            List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles(request.getParts().iterator().next().getName());
            if (CollectionUtils.isEmpty(files) || files.size() == 0){
                throw new RuntimeException("未检测到有文件信息");
            }
            MultipartFile[] imp_files = new MultipartFile[files.size()];
            files.toArray(imp_files);
            //判断是否增加文件后缀限制
            for (MultipartFile file : files) {
                if (!CollectionUtils.isEmpty(fileTypes)) {
                    boolean resu = false; //标记，判断是否符合限制
                    for (String fileType : fileTypes) {
                        if (file.getOriginalFilename().toLowerCase().endsWith(fileType)) {
                            resu = true;
                            break;
                        }
                    }
                    if (!resu) {
                        throw new RuntimeException("请上传正确格式的文件!");
                    }
                }
            }
            boolean isSuccess = fjcfbService.save2TempFile(imp_files,fjcfbDto,null);
            return isSuccess;
        } catch (Exception e) {
            throw new RuntimeException("文件处理失败!");
        }
    }
}
