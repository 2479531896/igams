package com.matridx.igams.wechat.control;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.GrszDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XxdyDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.PersonalSettingEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IGrszService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxdyService;
import com.matridx.igams.wechat.dao.entities.SjjcjgDto;
import com.matridx.igams.wechat.dao.entities.WzzkDto;
import com.matridx.igams.wechat.dao.entities.XxdjDto;
import com.matridx.igams.wechat.service.svcinterface.IWzzkService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/wzzk")
public class WzzkController extends BaseController {

    @Autowired
    private IWzzkService wzzkService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IXxdyService xxdyService;

    @Autowired
    private IJcsjService jcsjService;

    @Autowired
    private IGrszService grszService;

    @RequestMapping("/statistics/pagedataGetInfoStatisticsByParam")
    @ResponseBody
    public Map<String,Object>  getInfoByParam(HttpServletRequest request){
        Map<String,Object> map=new HashMap<>();
        User user=getLoginInfo();
        String zkmc=request.getParameter("zkmc");
        String jcdw=request.getParameter("jcdw");
        String wkrqstart=request.getParameter("wkrqstart");
        String wkrqend=request.getParameter("wkrqend");
        String cxy=request.getParameter("cxy");
        Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.XXDY_TYPE});
        List<JcsjDto> xxdyList=jclist.get(BasicDataTypeEnum.XXDY_TYPE.getCode());
        WzzkDto wzzkDto=new WzzkDto();
        List<WzzkDto> wzzkDtoList_nc=new ArrayList<>();
        List<WzzkDto> wzzkDtoList_pc=new ArrayList<>();

        if(StringUtil.isNotBlank(request.getParameter("yxxs"))){
            String yxxs=request.getParameter("yxxs");
            List<String> yxxsList=Arrays.asList(yxxs.split(","));
            wzzkDto.setYxxs(yxxsList);
            GrszDto grszDto = new GrszDto();
            grszDto.setYhid(user.getYhid());
            grszDto.setSzlbs(new String[]{PersonalSettingEnum.SNZK_SET_JCXM.getCode()});

            Map<String, GrszDto> grszDtoMap = grszService.selectGrszMapByYhidAndSzlb(grszDto);
            GrszDto grszxx = grszDtoMap.get(PersonalSettingEnum.SNZK_SET_JCXM.getCode());
            if(grszxx ==null){
                grszxx=new GrszDto();
                grszxx.setYhid(user.getYhid());
                grszxx.setSzz(request.getParameter("yxxs"));
                grszxx.setLrry(user.getYhid());
                grszService.insertDto(grszxx);
            }else{
                grszxx.setSzz(request.getParameter("yxxs"));
                grszService.updateDto(grszxx);
            }
        }
        if(StringUtil.isNotBlank(jcdw)){
            List<String>jcdws=new ArrayList<>();
            jcdws.add(jcdw);
            wzzkDto.setJcdws(jcdws);
        }
        if(StringUtil.isNotBlank(wkrqstart)){
            wzzkDto.setWkrqstart(wkrqstart);
        }
        if(StringUtil.isNotBlank(wkrqend)){
            wzzkDto.setWkrqend(wkrqend);
        }
        if(StringUtil.isNotBlank(cxy)){
            wzzkDto.setCxy(cxy);
        }
        if(StringUtil.isBlank(zkmc)){
            if(!CollectionUtils.isEmpty(xxdyList)){
                Optional<JcsjDto> optional =xxdyList.stream().filter(e->e.getCsdm().equals("ZKXMPC")).findFirst();
                if(optional.isPresent()){
                    wzzkDto.setXmdyStr(optional.get().getCsid());
                }
            }
            //PC图形显示
            wzzkDtoList_pc=wzzkService.getPcStatistics(wzzkDto);
            if(!CollectionUtils.isEmpty(xxdyList)){
                Optional<JcsjDto> optional =xxdyList.stream().filter(e->e.getCsdm().equals("ZKXMNC")).findFirst();
                if(optional.isPresent()){
                    wzzkDto.setXmdyStr(optional.get().getCsid());
                }
            }

            //NC图形显示
            wzzkDtoList_nc=wzzkService.getNcStatistics(wzzkDto);

        }
        List<WzzkDto> ncpcTable=wzzkService.getNcPcTable(wzzkDto);
        Map<String, List<WzzkDto>> groupMap = ncpcTable.stream().collect(Collectors.groupingBy(WzzkDto::getYxx));
        map.put("groupMap",groupMap);
        map.put("wzzkDtoList_nc",wzzkDtoList_nc);
        map.put("wzzkDtoList_pc",wzzkDtoList_pc);


        return map;
    }

    /**
     * 根据检测单位获取测序仪信息
     * @param wzzkDto
     * @return
     */
    @RequestMapping("/statistics/pagedataGetCxyxxBy")
    @ResponseBody
    public Map<String,Object>pagedataGetCxyxxBy(WzzkDto wzzkDto){
        Map<String,Object> returnMap=new HashMap<>();
        if(StringUtil.isNotBlank(wzzkDto.getJcdw())){
            List<JcsjDto> cxyList=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.SEQUENCER_CODE.getCode());
            List<JcsjDto> newcxyList=cxyList.stream().filter(e->wzzkDto.getJcdw().equals(e.getFcsid())).toList();
            returnMap.put("newcxyList",newcxyList);
        }
        return returnMap;
    }
    @RequestMapping("/statistics/pageListWzzkStatisticsInfo")
    @ResponseBody
    public ModelAndView pageListWzzkStatisticsInfo(HttpServletRequest request,WzzkDto wzzkDto){
        User user=getLoginInfo();
        ModelAndView mod=new ModelAndView("/wechat/wzzk/wzzkInfo");
        Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.XXDY_TYPE});
        List<JcsjDto> xxdyList=jclist.get(BasicDataTypeEnum.XXDY_TYPE.getCode());
        if(!CollectionUtils.isEmpty(xxdyList)){
            Optional<JcsjDto> optional =xxdyList.stream().filter(e->e.getCsdm().equals("ZKXMFL")).findFirst();
            if(optional.isPresent()){
                XxdyDto xxdyDto=new XxdyDto();
                xxdyDto.setDylx(optional.get().getCsid());
                List<XxdyDto>xxdyDtoList=xxdyService.getDyxxByYxx(xxdyDto);
                mod.addObject("xxdyxm",xxdyDtoList);
            }
        }
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        //默认结束日期为当天
        String localendtime= LocalDateTime.now().format( DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        // 获取当前日期的前一个月
        LocalDate previousMonth = currentDate.with(currentDate.getMonth().minus(1));
        // 加上一天
        LocalDate resultDate = previousMonth.plusDays(1);
        //默认开始时间  当前日期 -1个月 + 1天
        String localstarttime= resultDate.format( DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        mod.addObject("localstarttime",localstarttime);
        mod.addObject("localendtime",localendtime);
        GrszDto grszDto = new GrszDto();
        grszDto.setYhid(user.getYhid());
        grszDto.setSzlbs(new String[]{PersonalSettingEnum.SNZK_SET_JCXM.getCode()});

        Map<String, GrszDto> grszDtoMap = grszService.selectGrszMapByYhidAndSzlb(grszDto);
        GrszDto grszxx = grszDtoMap.get(PersonalSettingEnum.SNZK_SET_JCXM.getCode());
        if(grszxx != null&&StringUtil.isNotBlank(grszxx.getSzz())){
            mod.addObject("xmmc",grszxx.getSzz());
        }else{
            mod.addObject("xmmc","");
        }

        List<JcsjDto> jcdwList=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
        mod.addObject("jcdwList",jcdwList);
        mod.addObject("jcdw",jcdwList.get(0).getCsid());
        List<JcsjDto> cxyList=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.SEQUENCER_CODE.getCode());
        List<JcsjDto> newcxyList=cxyList.stream().filter(e->jcdwList.get(0).getCsid().equals(e.getFcsid())).toList();
        mod.addObject("cxyList",newcxyList);
        mod.addObject("cxy","");
        return mod;
    }

    @RequestMapping("/statistics/pageListWzzkStatisticsQuality")
    @ResponseBody
    public ModelAndView pageListWzzkStatisticsQuality(HttpServletRequest request,WzzkDto wzzkDto){
        ModelAndView mod=new ModelAndView("/wechat/wzzk/wzzkQuality");

        List<JcsjDto> jcxmList=redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.DETECT_TYPE.getCode());
        mod.addObject("jcxmList",jcxmList);
        return mod;
    }
}
