package com.matridx.igams.wechat.control;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.XszbDto;
import com.matridx.igams.common.dao.entities.XxdyDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXszbService;
import com.matridx.igams.common.service.svcinterface.IXxdyService;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.service.svcinterface.ISjxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxStatisticService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxTwoService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Controller
@RequestMapping("/ws")
public class TimedTaskController extends BaseController {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    ISjxxStatisticService sjxxStatisticService;
    @Autowired
    IJcsjService jcsjService;
    @Autowired
    private ISjxxService sjxxService;
    @Autowired
    private IXszbService xszbService;
    @Autowired
    IXxdyService xxdyService;
    @Autowired
    ISjxxTwoService sjxxTwoService;


    //测试定时任务
    @RequestMapping(value="/test/pagedataTest")
    @ResponseBody
    public void pagedataTest(HttpServletRequest request, SjxxDto sjxxDto){
        Map<String,Object> params = new HashMap<>();
        params.put("hospitalIds","33A001");
        params.put("timeStartNum","-3");
        sjxxService.timedCallXingheNew(params);
    }
    /**
     * 查询汇报领导的统计(接收日期)
     *
     */
    @RequestMapping("/xxx/weekLeadStatisDefault")
    @ResponseBody
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
            if (!CollectionUtils.isEmpty(dtoList)){
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

        String key;

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
            List<Map<String, String>> qgqsnums;
            if (StringUtil.isNotBlank(ptidstr[0])){
                key = "jsrq_qgqsnums"+"_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
                qgqsnums = sjxxTwoService.getAllCountryChangesForSale(sjxxDto);
            }else{
                key = "jsrq_qgqsnums"+"_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
                qgqsnums = sjxxTwoService.getAllCountryChanges(sjxxDto);
            }
            redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(qgqsnums));

            //产品趋势
            if (StringUtil.isNotBlank(ptidstr[0]))
                key = "jsrq_cpqstjnums" + "_" + sjxxDto.getZq() + "_" + StringUtil.join(sjxxDto.getYxxs(), ",") + "_" + StringUtil.join(sjxxDto.getPtgss(), ",");
            else
                key = "jsrq_cpqstjnums" + "_" + sjxxDto.getZq() + "_" + StringUtil.join(sjxxDto.getYxxs(), ",");
            List<Map<String, Object>> cpqstjnums = sjxxTwoService.getProductionChanges(sjxxDto);
            redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(cpqstjnums));

            //平台趋势(无平台页面)
            if (StringUtil.isBlank(ptidstr[0])){
                key = "jsrq_ptqstjnums"+"_"+sjxxDto.getZq();
                List<Map<String, Object>> ptqstjnums = sjxxTwoService.getPlatformChanges(sjxxDto);
                redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(ptqstjnums));
            }

            //计算季度时间
            String currentYear1 = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
            Calendar calendar = Calendar.getInstance();
            int monthDay = calendar.get(Calendar.DAY_OF_MONTH);
            int currenMonth;
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
            if (StringUtil.isNotBlank(ptidstr[0]))
                key =yhid+ "jsrq_ptzbdcl"+"_"+xszbDto1.getKszq() + "~" + xszbDto1.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
            else
                key = "jsrq_ptzbdcl"+"_"+xszbDto1.getKszq() + "~" + xszbDto1.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
            List<Map<String, Object>> ptzbdclnums = sjxxStatisticService.getPtzbdcl(xszbDto1);
            redisUtil.hset("weekLeadStatisDefault", key,JSON.toJSONString(ptzbdclnums));


            //平台业务占比（无平台页面）
            if (StringUtil.isBlank(ptidstr[0])){
                xszbDto1.setYwlx(sjxxDto.getYwlx());
                xszbDto1.setYwlx_q(sjxxDto.getYwlx_q());
                xszbDto1.setXms(Arrays.asList(sjxxDto.getYxxs()));
                key = "jsrq_ptywzbtjnums"+"_"+xszbDto1.getKszq() + "~" + xszbDto1.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
                List<Map<String, Object>> ptywzbtjnums = sjxxTwoService.getPlatformProportion(xszbDto1);
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
            List<Map<String, Object>> cpywzbtjnums = sjxxTwoService.getProductionProportion(xszbDto1);
            redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(cpywzbtjnums));

            //伙伴分类测试数占比
            XxdyDto xxdyDto=new XxdyDto();
            xxdyDto.setCskz1("JCSJ");
            xxdyDto.setCskz2(BasicDataTypeEnum.CLASSIFY.getCode());
            List<XxdyDto> xxdyDtos = xxdyService.getListGroupByYxx(xxdyDto);
            sjxxDto.getZqs().put("hbflcsszbzqs", start1 + "~" + end1);
            sjxxDto.getZqs().put("hbflcsszbyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
            List<Map<String, String>> newList=new ArrayList<>();
            if(xxdyDtos!=null&&xxdyDtos.size()>0){
                SjxxDto sjxxDto_t=new SjxxDto();
                sjxxDto_t.setJsrqstart(start1);
                sjxxDto_t.setJsrqend(end1);
                sjxxDto_t.setYwlx(sjxxDto.getYwlx());
                sjxxDto_t.setYxxs(sjxxDto.getYxxs());
                if (StringUtil.isNotBlank(ptidstr[0]))
                    key = "jsrq_getHbflcsszb_"+start1 + "~" + end1+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid();
                else
                    key = "jsrq_getHbflcsszb_"+start1 + "~" + end1+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
                sjxxDto_t.setXg_flg("1");
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
            if(sjqfs!=null&&sjqfs.size()>0){
                SjxxDto sjxxDto_t=new SjxxDto();
                sjxxDto_t.setJsrqstart(start1);
                sjxxDto_t.setJsrqend(end1);
                sjxxDto_t.setYwlx(sjxxDto.getYwlx());
                sjxxDto_t.setYxxs(sjxxDto.getYxxs());
                sjxxDto_t.setNewflg("zb");
                List<Map<String, String>> sjqfcsszb = sjxxStatisticService.getSjqfcsszb(sjxxDto_t);
                for(JcsjDto jcsjDto:sjqfs){
                    boolean isFind=false;
                    for(Map<String, String> map_t:sjqfcsszb){
                        if(jcsjDto.getCsid().equals(map_t.get("sjqf"))){
                            map_t.put("sjqfmc",jcsjDto.getCsmc());
                            sjqfList.add(map_t);
                            isFind=true;
                            break;
                        }
                    }
                    if(!isFind){
                        Map<String, String> newMap=new HashMap<>();
                        newMap.put("num","0");
                        newMap.put("sjqfmc",jcsjDto.getCsmc());
                        sjqfList.add(newMap);
                    }
                }
            }
            redisUtil.hset("weekLeadStatisDefault", key,JSON.toJSONString(sjqfList));

            //标本信息检测总条数
            List<Map<String, String>> jcxmnum;
            if (StringUtil.isNotBlank(ptidstr[0])){
                key = yhid + "_jcxmnum_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",") +"_"+ StringUtil.join(sjxxDto.getYxxs(),",");
                jcxmnum = sjxxStatisticService.getYbxxDRByWeeklyAndJsrq(sjxxDto);
            }else{
                key = "jsrq_jcxmnum"+"_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
                jcxmnum =sjxxService.getSjxxDRBySyAndJsrq(sjxxDto);
            }
            redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(jcxmnum));

            //收费标本下边检测项目的总条数
            List<Map<String, String>> ybxxType;
            if (StringUtil.isNotBlank(ptidstr[0])){
                key = yhid + "_ybxxType_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",") +"_"+ StringUtil.join(sjxxDto.getYxxs(),",");
                ybxxType = sjxxStatisticService.getYbxxTypeByWeeklyAndJsrq(sjxxDto);
            }else{
                key = "jsrq_ybxxType"+"_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
                ybxxType =sjxxService.getYbxxTypeBYWeekAndJsrq(sjxxDto);
            }
            redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(ybxxType));

            //销售性质测试数趋势
            List<Map<String, String>> xsxzcssqsList=new ArrayList<>();
            if (StringUtil.isNotBlank(ptidstr[0]))
                key = "jsrq_getXsxzcssqszqs_"+sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend()+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid();
            else
                key = "jsrq_getXsxzcssqszqs_"+sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
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
            if (StringUtil.isNotBlank(ptidstr[0]))
                key = "jsrq_ywzrfz"+"_"+xszbDto1.getKszq() + "~" + xszbDto1.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
            else
                key = "jsrq_ywfzb"+"_"+xszbDto_t.getKszq() + "~" + xszbDto_t.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
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
            List<Map<String, String>> topDsf20 = sjxxTwoService.getTopDsf20(sjxxDto);
            redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(topDsf20));

            //Top20直销
            if (StringUtil.isNotBlank(ptidstr[0]))
                key = yhid + "jsrq_topZx20"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
            else
                key = "jsrq_topZx20"+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
            List<Map<String, String>> topZx20 = sjxxTwoService.getTopZx20(sjxxDto);
            redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(topZx20));

            //Top20CSO
            if (StringUtil.isNotBlank(ptidstr[0]))
                key = yhid + "jsrq_topCSO20"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
            else
                key = "jsrq_topCSO20"+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
            sjxxDto.setXg_flg("1");//代表领导页面进入进入，用于判断CSOTOP图将其他平台的伙伴进行归类
            List<Map<String, String>> topCSO20 = sjxxTwoService.getTopCSO20(sjxxDto);
            redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(topCSO20));

            //Top20RY
            if (StringUtil.isNotBlank(ptidstr[0]))
                key = yhid +  "jsrq_topRY20"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
            else
                key = "jsrq_topRY20"+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
            List<Map<String, String>> topRY20 = sjxxTwoService.getTopRY20(sjxxDto);
            redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(topRY20));

            //2.5、TOP10核心医院排行
            if (StringUtil.isNotBlank(ptidstr[0]))
                key = yhid + "jsrq_topHxyxList"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
            else
                key = "jsrq_topHxyxList"+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
            List<Map<String, Object>> topHxyxList = sjxxTwoService.getHxyyTopList(sjxxDto);
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
                    tjXszb.setZbzfl(jcsj.getCsid());
                    tjXszb.setCskz2(jcsj.getCskz2());
                    //由于外部统计数据，需要查找区域下的伙伴去限制外部统计数据范围
                    List<String> hbids = xszbService.getDtosByZfl(tjXszb);
                    tjXszb.setHbids(hbids);
                    key = "jsrq_tjxsdcl"+"_"+jcsj.getCsid()+"_"+ tjXszb.getKszq()+ "~" + tjXszb.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
                    List<Map<String,String>> list = sjxxStatisticService.getTjsxdcl(tjXszb);
                    redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(list));
//                    tjxsmap.put(jcsj.getCsid() , list);
                }
            }


            sjxxDto.setTj("mon");
            sjxxDto.setJsrqMstart(start1);
            sjxxDto.setJsrqMend(end1);
            //2.4、收费科室分布（按照收费测试数统计）
            if (StringUtil.isNotBlank(ptidstr[0]))
                key = yhid + "jsrq_ksSfcssList"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
            else
                key = "jsrq_ksSfcssList"+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
            List<Map<String, Object>> ksSfcssList = sjxxTwoService.getChargesDivideByKs(sjxxDto);
            redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(ksSfcssList));

            //2.5、收费标本类型分布（按照收费测试数统计
            if (StringUtil.isNotBlank(ptidstr[0]))
                key = yhid + "jsrq_yblxSfcssList"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
            else
                key = "jsrq_yblxSfcssList"+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
            List<Map<String, Object>> yblxSfcssList  = sjxxTwoService.getChargesDivideByYblx(sjxxDto);
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
}
