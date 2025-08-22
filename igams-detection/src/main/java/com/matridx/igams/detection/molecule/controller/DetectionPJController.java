package com.matridx.igams.detection.molecule.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.GrszDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.YyxxDto;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.DataPermissionTypeEnum;
import com.matridx.igams.common.enums.MQTypeEnum;
import com.matridx.igams.common.enums.PersonalSettingEnum;
import com.matridx.igams.common.enums.PrintEnum;
import com.matridx.igams.common.enums.RabbitEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IGrszService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.service.svcinterface.IYyxxService;
import com.matridx.igams.detection.molecule.dao.entities.FzjcjgDto;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxmDto;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxxDto;
import com.matridx.igams.detection.molecule.dao.entities.FzzkjgDto;
import com.matridx.igams.detection.molecule.enums.DetMQTypeEnum;
import com.matridx.igams.detection.molecule.service.svcinterface.IFzbbztService;
import com.matridx.igams.detection.molecule.service.svcinterface.IFzjcjgPJService;
import com.matridx.igams.detection.molecule.service.svcinterface.IFzjcxmPJService;
import com.matridx.igams.detection.molecule.service.svcinterface.IFzjcxxPJService;
import com.matridx.igams.detection.molecule.service.svcinterface.IFzjcxxService;
import com.matridx.igams.detection.molecule.service.svcinterface.IFzzkjgService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/detectionPJ")
public class DetectionPJController extends BaseController {
    private final Logger log = LoggerFactory.getLogger(DetectionPJController.class);

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    IFzjcxxPJService fzjcxxPJService;
    @Autowired
    IXxglService xxglService;
    @Autowired
    IFzjcxmPJService fzjcxmPJService;
    @Autowired
    IFzjcjgPJService fzjcjgPJService;
    @Autowired
    private IGrszService grszService;
    @Autowired
    private IJcsjService jcsjService;
    @Autowired
    private IYyxxService yyxxService;
    @Autowired
    IFzjcxxService fzjcxxService;
    @Autowired
    ICommonService commonService;
    @Autowired
    IFzbbztService fzbbztService;
    @Autowired(required = false)
    private AmqpTemplate amqpTempl;
    @Autowired
    IFzzkjgService fzzkjgService;
    /**
     * 普检列表
     */
    @RequestMapping("/detectionPJ/pageListDetectionPJ")
    public ModelAndView pageListDetectionPJ(FzjcxxDto fzjcxxDto) {
        ModelAndView mav = new ModelAndView("detection/detectPJ/detectPJ_list");
        fzjcxxDto.setAuditType(AuditTypeEnum.AUDIT_GENERAL_INSPECTION.getCode());
        mav.addObject("samplelist",redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.GENERALSAMPLE_TYPE.getCode()));
        return mav;
    }
    /**
     * 分子检测-普检列表新增
     */
    @ResponseBody
    @RequestMapping(value ="/detectionPJ/addDetectionPJ")
    public ModelAndView addDetectionPJ(FzjcxxDto fzjcxxDto){
        FzjcxxDto fzjcxxDto_t = new FzjcxxDto();
        ModelAndView mav = new ModelAndView("detection/detectPJ/detectPJ_list_edit");
        //检测单位默认，因为标本编号生产规则依靠检测项目+检测单位+流水号
        //设置检测单位为默认的检测单位（杭州实验室）
        List<JcsjDto> jcdwList = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT));
        for (JcsjDto jcdw : jcdwList) {
            if ("1".equals(jcdw.getSfmr())){
                fzjcxxDto.setJcdw(jcdw.getCsid());
                fzjcxxDto_t.setJcdw(jcdw.getCsid());
                break;
            }
        }
        List<JcsjDto> jcxmListall = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_ITEM));//所有分子检测项目
        List<JcsjDto> jcxmList=new ArrayList<>();//用来筛选出HPV的检测项目，通过他们的父参数代码判断
        //标本状态
        List<JcsjDto> bbztlistall = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.GENERAL_SAMPLESTATE));//所有的普检标本状态
        List<JcsjDto> bbztlist = new ArrayList<>();
        //标本类型,默认设置为其他
        List<JcsjDto> bblxList = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.GENERALSAMPLE_TYPE));//所有普检标本类型list
        List<JcsjDto> bblxlist = new ArrayList<>();//筛选父参数
        JcsjDto jcsjDto=new JcsjDto();
        if(StringUtil.isNotBlank(fzjcxxDto.getJclx())) {
            fzjcxxDto_t.setJclx(fzjcxxDto.getJclx());
            jcsjDto.setCsdm(fzjcxxDto.getJclx());//根据列表拿到参数代码，然后进行筛查
            jcsjDto.setJclb(BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
            JcsjDto jcsjDto1=jcsjService.getDtoByCsdmAndJclb(jcsjDto);//查找出TYPE_HPV的csid，进行筛查相应的检测项目
            fzjcxxDto.setJclx(jcsjDto1.getCsid());
            for (JcsjDto jcxm : jcxmListall) {
                if(jcsjDto1.getCsid().equals(jcxm.getFcsid()))
                    jcxmList.add(jcxm);
            }
            for (JcsjDto dto : bbztlistall) {//筛选出HPV的标本状态，根据父参数
                if(jcsjDto1.getCsid().equals(dto.getFcsid()))
                    bbztlist.add(dto);
            }
            for (JcsjDto dto : bblxList) {//筛选出HPV的标本类型，根据父参数
                if(jcsjDto1.getCsid().equals(dto.getFcsid()))
                    bblxlist.add(dto);
            }
        }
        for (JcsjDto bbzt : bbztlist) {
            if ("1".equals(bbzt.getSfmr())){
                fzjcxxDto.setBbzt(bbzt.getCsid());//有默认时候设置默认的标本状态
            }
        }
        for (JcsjDto bblx : bblxList) {
            if ("1".equals(bblx.getCskz1())){
                fzjcxxDto.setYblx(bblx.getCsid());
                fzjcxxDto.setYblxcskz1("1");
            }
        }

        //送检单位默认为其他
        YyxxDto yyxxDto = new YyxxDto();
        yyxxDto.setSearchParam("其他");
        List<YyxxDto> yyxxlist = yyxxService.selectHospitalName(yyxxDto);
        if (yyxxlist!=null && yyxxlist.size()>0){
            fzjcxxDto.setSjdw(yyxxlist.get(0).getDwid());
            fzjcxxDto.setYyxxcskz1("1");
            fzjcxxDto.setDwmc(yyxxlist.get(0).getDwmc());
        }
        //获取当前用户，获取实验时间和接收时间以及人员
        User user = getLoginInfo();
        if (user != null) {
            fzjcxxDto.setJsry(user.getYhid());
            fzjcxxDto.setSyry(user.getYhid());
            fzjcxxDto.setJsrymc(user.getZsxm());
            fzjcxxDto.setSyrymc(user.getZsxm());
        }
        List<JcsjDto> fzjczxmlistall = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_SUBITEM));
        List<JcsjDto> fzjczxmlist=new ArrayList<>();//筛选出备选的子项目
        for (JcsjDto dto : fzjczxmlistall) {
            String fcsid=dto.getFcsid();
            for(JcsjDto jcxm:jcxmList){
                if(fcsid.equals(jcxm.getCsid())){
                    dto.setKz4mc(jcxm.getCskz4());
                    fzjczxmlist.add(dto);
                }
            }
        }
        String ybbh=fzjcxxPJService.generateYbbh(fzjcxxDto_t);
        fzjcxxDto.setYbbh(ybbh);
        fzjcxxDto.setWybh(ybbh);
        fzjcxxDto.setBbzbh(ybbh);
        mav.addObject("sjdwxxList",fzjcxxService.getSjdw());//科室
        mav.addObject("bbztlist", bbztlist);
        mav.addObject("fzjcxmlist", jcxmList);//检测项目
        mav.addObject("jcdwxxList", jcdwList);//检测单位
        mav.addObject("bblxList", bblxlist);
        mav.addObject("fzjcxxDto",fzjcxxDto);
        mav.addObject("formAction","addSaveDetectionPJ");
//        mav.addObject("genderlist",redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.GENDER_TYPE)));
        mav.addObject("fzjczxmlist", fzjczxmlist);
        return mav;
    }
    /**
     * 获取分子检测样本编号
     */
    @ResponseBody
    @RequestMapping(value = "/detectionPJ/pagedataGetDetectionCode")
    public Map<String, Object> getDetectionCode(FzjcxxDto fzjcxxDto) {
        Map<String, Object> map = new HashMap<>();
        String ybbh=fzjcxxPJService.generateYbbh(fzjcxxDto);
        map.put("ybbh",ybbh);
        return map;
    }
    /**
     * 	HPV新增保存
     */
    @RequestMapping("/detectionPJ/addSaveDetectionPJ")
    @ResponseBody
    public Map<String,Object> addSaveDetectionPJ(FzjcxxDto fzjcxxDto){
        Map<String,Object> map=new HashMap<>();
        User user = getLoginInfo();
        fzjcxxDto.setLrry(user.getYhid());
        fzjcxxDto.setFzjcid(StringUtil.generateUUID());
        if(StringUtil.isNotBlank(fzjcxxDto.getBbzbh())){
            List<FzjcxxDto> fzjcxxByBbzbh = fzjcxxService.getExistBbzbh(fzjcxxDto);
            if(!CollectionUtils.isEmpty(fzjcxxByBbzbh)){
                map.put("status", "fail");
                map.put("message", "当前标本子编号已存在，请更改再重试");
                return map;
            }
        }
        if(StringUtil.isNotBlank(fzjcxxDto.getSyh())){
            List<FzjcxxDto> fzjcxxDtos = fzjcxxPJService.getFzjcxxBySyh(fzjcxxDto);
            if(!CollectionUtils.isEmpty(fzjcxxDtos)){
                map.put("status","fail");
                map.put("message","当前实验号已存在!");
                return map;
            }
        }
        boolean result=fzjcxxPJService.insertHPVDetection(fzjcxxDto);
        if (result){
            amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.NORMAL_INSPECT_TOWECHAT.getCode(), RabbitEnum.PJXX_ADD.getCode() + JSONObject.toJSONString(fzjcxxDto));
        }
        map.put("status", result ? "success" : "fail");
        map.put("message", result ? xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 分子检测-普检列表修改
     */
    @RequestMapping(value ="/detectionPJ/modDetectionPJ")
    @ResponseBody
    public ModelAndView modDetectionPJ(FzjcxxDto fzjcxx){
        ModelAndView mav = new ModelAndView("detection/detectPJ/detectPJ_list_edit");
        List<JcsjDto> jclxList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
        if(!CollectionUtils.isEmpty(jclxList)){
            for (JcsjDto jclx:jclxList){
                if(fzjcxx.getJclx().equals(jclx.getCsdm()))
                    fzjcxx.setJclx(jclx.getCsid());
            }
        }
        FzjcxxDto fzjcxxDto = fzjcxxPJService.getDto(fzjcxx);
        fzjcxxDto.setBbzt((fzjcxxPJService.getBbzts(fzjcxx)).getBbzt());
        List<FzjcxmDto> xmlist = fzjcxmPJService.getDtoListByFzjcid(fzjcxx.getFzjcid());
        StringBuilder zxmids= new StringBuilder();//子项目ids
        StringBuilder fxmids= new StringBuilder();//父项目ids
        for (FzjcxmDto zxm : xmlist) {
            if(StringUtil.isNoneBlank(zxm.getFzjczxmid())){
                zxmids.append(',').append(zxm.getFzjczxmid());
            }
            if(StringUtil.isNoneBlank(zxm.getFzjcxmid())){
                fxmids.append(',').append(zxm.getFzjcxmid());
            }
        }
        if(StringUtil.isNoneBlank(zxmids.toString())){
            zxmids = new StringBuilder(zxmids.substring(1));
        }
        if(StringUtil.isNoneBlank(fxmids.toString())){
            fxmids = new StringBuilder(fxmids.substring(1));
        }
        //检测单位默认，因为标本编号生产规则依靠检测项目+检测单位+流水号
        //标本类型,默认设置为其他
        List<JcsjDto> bblxList = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.GENERALSAMPLE_TYPE));//所有普检标本类型list
        List<JcsjDto> bblxlist = new ArrayList<>();//筛选父参数的list

        List<JcsjDto> jcxmListall = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_ITEM));
        List<JcsjDto> jcxmList=new ArrayList<>();//用来筛选出HPV的检测项目，通过他们的父参数代码判断
        List<JcsjDto> bbztlistall = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.GENERAL_SAMPLESTATE));//所有的普检标本状态
        List<JcsjDto> bbztlist = new ArrayList<>();
        for (JcsjDto jcxm : jcxmListall) {
            if(fzjcxxDto.getJclx().equals(jcxm.getFcsid())){
                jcxmList.add(jcxm);
            }
        }
        for (JcsjDto dto : bbztlistall) {//筛选出HPV的标本状态，根据父参数筛选
            if(fzjcxxDto.getJclx().equals(dto.getFcsid())){
                bbztlist.add(dto);
            }
        }
        for (JcsjDto dto : bblxList) {//筛选出HPV的标本类型，根据父参数
            if(fzjcxxDto.getJclx().equals(dto.getFcsid())){
                bblxlist.add(dto);
            }
        }
        for (JcsjDto bblx : bblxlist) {
            if (!("1".equals(bblx.getCskz1()))&&fzjcxxDto.getYblx().equals(bblx.getCsid())){
                fzjcxxDto.setYblxmc("");
            }
            else if(("1".equals(bblx.getCskz1()))&&fzjcxxDto.getYblx().equals(bblx.getCsid())){
                fzjcxxDto.setYblxcskz1("1");
            }
        }
        List<JcsjDto> fzjczxmlistall = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_SUBITEM));
        List<JcsjDto> fzjczxmlist=new ArrayList<>();//筛选出备选的子项目
        for (JcsjDto dto : fzjczxmlistall) {
            String fcsid=dto.getFcsid();
            for(JcsjDto jcxm:jcxmList){
                if(fcsid.equals(jcxm.getCsid())){
                    dto.setKz4mc(jcxm.getCskz4());
                    fzjczxmlist.add(dto);
                }
            }
        }
        mav.addObject("bblxList", bblxlist);
        mav.addObject("bbztlist", bbztlist);//标本状态
        mav.addObject("jcdwxxList", redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT)));//检测单位
        mav.addObject("fzjcxmlist", jcxmList);//检测项目
        mav.addObject("fzjcxxDto",fzjcxxDto);
        mav.addObject("formAction","modSaveDetectionPJ");
//        mav.addObject("genderlist",redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.GENDER_TYPE)));
        mav.addObject("fzjczxmlist", fzjczxmlist);
        mav.addObject("sjdwxxList",fzjcxxService.getSjdw());//科室
        mav.addObject("zxmids", zxmids.toString());//选中的子项目
        mav.addObject("fxmids", fxmids.toString());//选中的子项目
        return mav;
    }


    /**
     * 保存修改
     */
    @RequestMapping(value="/detectionPJ/modSaveDetectionPJ")
    @ResponseBody
    public Map<String, String> modSaveDetectionPJ(FzjcxxDto fzjcxxDto) {
        Map<String, String> map = new HashMap<>();
        fzjcxxDto.setXgry(getLoginInfo().getYhid());
        fzjcxxDto.setBbzbh(fzjcxxDto.getYbbh());
        if(StringUtil.isNotBlank(fzjcxxDto.getBbzbh())){
            List<FzjcxxDto> fzjcxxByBbzbh = fzjcxxService.getExistBbzbh(fzjcxxDto);
            if(!CollectionUtils.isEmpty(fzjcxxByBbzbh)){
                map.put("status", "fail");
                map.put("message", "当前标本子编号已存在，请更改再重试");
                return map;
            }
        }
        List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
        if(!CollectionUtils.isEmpty(jclxList)){
            for(JcsjDto jcsjDto : jclxList){
                if(fzjcxxDto.getJclx().equals(jcsjDto.getCsdm()))
                    fzjcxxDto.setJclx(jcsjDto.getCsid());
            }
        }
        if(StringUtil.isNotBlank(fzjcxxDto.getSyh())){
            List<FzjcxxDto> fzjcxxDtos = fzjcxxPJService.getFzjcxxBySyh(fzjcxxDto);
            if(!CollectionUtils.isEmpty(fzjcxxDtos)){
                map.put("status","fail");
                map.put("message","当前实验号已存在!");
                return map;
            }
        }
        boolean isSuccess =fzjcxxPJService.saveEditHPV(fzjcxxDto);
        if (isSuccess){
            amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.NORMAL_INSPECT_TOWECHAT.getCode(), RabbitEnum.PJXX_MOD.getCode() + JSONObject.toJSONString(fzjcxxDto));
        }
        map.put("status", isSuccess?"success":"fail");
        map.put( "message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 删除按钮
     */
    @RequestMapping("/detectionPJ/delHPVDetection")
    @ResponseBody
    public Map<String,Object> delHPVDetection(FzjcxxDto fzjcxxDto) {
        Map<String, Object> map= new HashMap<>();
        User user = getLoginInfo();
        fzjcxxDto.setScry(user.getYhid());
        boolean isSuccess = fzjcxxPJService.delHPVDetection(fzjcxxDto);
        if (isSuccess){
            amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.NORMAL_INSPECT_TOWECHAT.getCode(), RabbitEnum.PJXX_DEL.getCode() + JSONObject.toJSONString(fzjcxxDto));
        }
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00003").getXxnr()
                : xxglService.getModelById("ICOM00004").getXxnr());
        return map;
    }


    /**
     * 条件查找列表信息
     */
    @RequestMapping("/detectionPJ/pageGetListDetectionPJ")
    @ResponseBody
    public Map<String, Object> viewDetectionPJList(FzjcxxDto fzjcxxDto){
        Map<String, Object> map= new HashMap<>();
        //区分普检数据
        List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
        if(jclxList.size()>0){
            for(JcsjDto jcsjDto : jclxList){
                if(fzjcxxDto.getJclx().equals(jcsjDto.getCsdm()))
                    fzjcxxDto.setJclx(jcsjDto.getCsid());
            }
        }
        User user=getLoginInfo();
        List<Map<String,String>> jcdwList=fzjcxxPJService.getJsjcdwByjsid(user.getDqjs());
        if(!CollectionUtils.isEmpty(jcdwList) && "1".equals(jcdwList.get(0).get("dwxdbj"))) {
            List<String> strList=new ArrayList<>();
            for (Map<String, String> stringStringMap : jcdwList) {
                if (stringStringMap.get("jcdw") != null) {
                    strList.add(stringStringMap.get("jcdw"));
                }
            }
            if(strList.size()>0) {
                fzjcxxDto.setJcdwxz(strList);
            }
        }
        List<FzjcxxDto> pagedDtoList = fzjcxxPJService.getPagedDtoList(fzjcxxDto);
        for (FzjcxxDto dto : pagedDtoList) {
            if(StringUtil.isNotBlank(dto.getDwmc())){
                dto.setSjdwmc(dto.getSjdwmc()+"-"+dto.getDwmc());
            }
        }
        if(pagedDtoList.size()>0){
            for(FzjcxxDto dto:pagedDtoList){
                if(StringUtil.isNotBlank(dto.getJcjgdm())){
                    int num=0;//用于记录阳性的数量
                    String[] split = dto.getJcjgdm().split(",");
                    for(String s:split){
                        if(!"NEGATIVE".equals(s)){
                            num+=1;
                        }
                    }
                    if(num>0){
                        dto.setJcjgdm("POSITIVE");
                    }else{
                        dto.setJcjgdm("NEGATIVE");
                    }
                }
            }
        }
        map.put("rows", pagedDtoList);
        map.put("total", fzjcxxDto.getTotalNumber());
        return map;
    }
    /**
     * 跳转报告下载页面
     */

    @RequestMapping(value = "/detectionPJ/reportdownloadZip")
    public ModelAndView reportZipDownload(FzjcxxDto fzjcxxDto){
        ModelAndView mav=new ModelAndView("detection/detectPJ/detecPJ_reportDownload");
        mav.addObject("fzjcxxDto",fzjcxxDto);
        return mav;
    }
    /**
     * 根据条件下载报告压缩包
     */
    @RequestMapping(value = "/detectionPJ/reportdownloadSaveZip")
    @ResponseBody
    public Map<String,Object> reportZipDownloadOut(FzjcxxDto fzjcxxDto, HttpServletRequest request){
        Map<String,Object> map;
        List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
        if(jclxList.size()>0){
            for(JcsjDto jcsjDto : jclxList){
                if(fzjcxxDto.getJclx().equals(jcsjDto.getCsdm()))
                    fzjcxxDto.setJclx(jcsjDto.getCsid());
            }
        }
        map=fzjcxxPJService.reportZipDownload(fzjcxxDto, request);
        return map;
    }
    /**
     * 点击实验跳转页面
     */
    @RequestMapping(value="/detectionPJ/detectionScreening")
    public ModelAndView Detection(FzjcxxDto fzjcxxDto) {
        ModelAndView mav=new ModelAndView("detection/detectPJ/detectPJ_detection");
        List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
        if(jclxList.size()>0){
            for(JcsjDto jcsjDto : jclxList){
                if(fzjcxxDto.getJclx().equals(jcsjDto.getCsdm()))
                    fzjcxxDto.setJclx(jcsjDto.getCsid());
            }
        }
        FzjcxxDto fzjcxxDto_t;
        if (fzjcxxDto.getIds().size()>1){
            List<FzjcxxDto> fzjcxxDtos = fzjcxxPJService.checkJcxm(fzjcxxDto);
            fzjcxxDto_t = fzjcxxDtos.get(0);
        }else {
            FzjcxxDto fzjcxxDto_sel = new FzjcxxDto();
            fzjcxxDto_sel.setFzjcid(fzjcxxDto.getIds().get(0));
            fzjcxxDto_t = fzjcxxPJService.getDto(fzjcxxDto_sel);
        }
        if (StringUtil.isBlank(fzjcxxDto_t.getSysj())){
            fzjcxxDto_t.setSysj(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd HH:mm"));
        }
        fzjcxxDto_t.setIds(fzjcxxDto.getIds());
        mav.addObject("fzjcxxDto",fzjcxxDto_t);
        mav.addObject("jclx",fzjcxxDto.getJclx());
        return mav;
    }

    /**
     * 查看PJ页面
     */
    @RequestMapping(value = "/detectionPJ/viewdetection")
    @ResponseBody
    public ModelAndView viewdetection(FzjcxxDto fzjcxxDto) {
        ModelAndView mav = new ModelAndView("detection/detectPJ/detectPJ_view");
        // 查看基本信息
        FzjcxxDto fzjcxx = fzjcxxPJService.getDto(fzjcxxDto);
        fzjcxx.setBbztmc((fzjcxxPJService.getBbzts(fzjcxxDto)).getBbztmc());
//        List<FzjcxxDto> jcjgs=fzjcxxPJService.getJg(fzjcxx);
        FjcfbDto fjcfbDto=new FjcfbDto();
        fjcfbDto.setYwid(fzjcxxDto.getFzjcid());
        if ("TYPE_FLU".equals(fzjcxx.getJclxdm())){
            fjcfbDto.setYwlx(BusTypeEnum.IMP_REPORT_FLU_WORD.getCode());
        }else if("TYPE_HPV".equals(fzjcxx.getJclxdm())){
            fjcfbDto.setYwlx(BusTypeEnum.IMP_REPORT_HPV_WORD.getCode());
        } else if ("TYPE_JHPCR".equals(fzjcxx.getJclxdm())) {
            fjcfbDto.setYwlx(BusTypeEnum.IMP_REPORT_JHPCR_WORD.getCode());
        }
        List<FjcfbDto> wordList = fzjcxxService.getReport(fjcfbDto);
        if ("TYPE_FLU".equals(fzjcxx.getJclxdm())){
            fjcfbDto.setYwlx(BusTypeEnum.IMP_REPORT_FLU.getCode());
        }else if("TYPE_HPV".equals(fzjcxx.getJclxdm())){
            fjcfbDto.setYwlx(BusTypeEnum.IMP_REPORT_HPV.getCode());
        }else if ("TYPE_JHPCR".equals(fzjcxx.getJclxdm())) {
            fjcfbDto.setYwlx(BusTypeEnum.IMP_REPORT_JHPCR.getCode());
        }
        List<FjcfbDto> pdfList = fzjcxxService.getReport(fjcfbDto);
        if("TYPE_FLU".equals(fzjcxx.getJclxdm())){
            fjcfbDto.setYwlx(BusTypeEnum.IMP_FILE_FLU_TEMEPLATE.getCode());
        }else if ("TYPE_JHPCR".equals(fzjcxx.getJclxdm())) {
            fjcfbDto.setYwlx(BusTypeEnum.IMP_FILE_JHPCR_TEMEPLATE.getCode());
        }

        List<FjcfbDto> jgList = fzjcxxService.getReport(fjcfbDto);
//        if(StringUtil.isNotBlank(fzjcxx.getQtks()))//当是“其他科室”的时候，显示其他科室的具体名字
//            fzjcxx.setKsmc(fzjcxx.getQtks());
        if(StringUtil.isNotBlank(fzjcxx.getQtks())){
            fzjcxx.setKsmc(fzjcxx.getKsmc()+"-"+fzjcxx.getQtks());
        }
        if(StringUtil.isNotBlank(fzjcxx.getSjdwmc())){
            fzjcxx.setSjdwmc(fzjcxx.getDwmc()+"-"+fzjcxx.getSjdwmc());
        }
        FzjcjgDto fzjcjgDto=new FzjcjgDto();
        fzjcjgDto.setFzjcid(fzjcxxDto.getFzjcid());
        List<FzjcjgDto> dtoList = fzjcjgPJService.getDtoList(fzjcjgDto);
        List<String> tabNames=new ArrayList<>();
        if(dtoList!=null&&!dtoList.isEmpty()){
            FzzkjgDto fzzkjgDto=new FzzkjgDto();
            fzzkjgDto.setGlid(dtoList.get(0).getGlid());
            List<FzzkjgDto> fzzkjgDtos = fzzkjgService.getDtoList(fzzkjgDto);
            if(fzzkjgDtos!=null&&!fzzkjgDtos.isEmpty()){
                Map<String, List<FzzkjgDto>> listMap = fzzkjgDtos.stream().collect(Collectors.groupingBy(FzzkjgDto::getZkmc));
                if (null != listMap && listMap.size()>0){
                    Iterator<Map.Entry<String, List<FzzkjgDto>>> entries = listMap.entrySet().iterator();
                    while (entries.hasNext()) {
                        Map.Entry<String,  List<FzzkjgDto>> entry = entries.next();
                        tabNames.add(entry.getKey());
                    }
                }

                for(FzjcjgDto fzjcjgDto_t:dtoList){
                    List<String> tabValues=new ArrayList<>();
                    for(String name:tabNames){
                        for(FzzkjgDto fzzkjgDto_t:fzzkjgDtos){
                            if(name.equals(fzzkjgDto_t.getZkmc())&&fzzkjgDto_t.getJgid().equals(fzjcjgDto_t.getJgid())){
                                tabValues.add(fzzkjgDto_t.getCtz());
                            }
                        }
                    }
                    fzjcjgDto_t.setBzctzs(tabValues);
                }
            }

        }
        List<FzjcxmDto> fzjcxmDtoList=fzjcxmPJService.getJcxmListAndBgByFzjcid(fzjcxx);
        mav.addObject("list",dtoList);
        mav.addObject("tabNames",tabNames);
        mav.addObject("fzjcxxDto", fzjcxx);
        mav.addObject("jcxmlist",fzjcxmDtoList);
        mav.addObject("wordList", wordList);
        mav.addObject("pdfList",pdfList);
        mav.addObject("jgList",jgList);
        return mav;
    }
    /**
     * 点击检测跳转页面前校验所选数据检测项目是否相同
     */
    @RequestMapping(value="/detectionPJ/pagedataCheckJcxm")
    @ResponseBody
    public boolean checkJcxm(FzjcxxDto fzjcxxDto) {
        List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
        if(jclxList.size()>0){
            for(JcsjDto jcsjDto : jclxList){
                if(fzjcxxDto.getJclx().equals(jcsjDto.getCsdm()))
                    fzjcxxDto.setJclx(jcsjDto.getCsid());
            }
        }
        List<FzjcxxDto> jcxmlist=fzjcxxPJService.checkJcxm(fzjcxxDto);
        return jcxmlist == null || jcxmlist.size() <= 1;
    }
    /**
     * 实验保存
     */
    @RequestMapping("/detectionPJ/detectionSaveScreening")
    @ResponseBody
    public Map<String, Object> detectionSaveScreening(FzjcxxDto fzjcxxDto){
        Map<String, Object> map= new HashMap<>();
        User user = getLoginInfo();
        fzjcxxDto.setSyry(user.getYhid());
        if(StringUtil.isBlank(fzjcxxDto.getSfsy())){
            fzjcxxDto.setSfsy("0");
            fzjcxxDto.setSysj(null);
            fzjcxxDto.setSyry(null);
        }
        boolean isSuccess = fzjcxxPJService.updateSyzt(fzjcxxDto);
        if (isSuccess){
            amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.NORMAL_INSPECT_TOWECHAT.getCode(), RabbitEnum.PJXX_DET.getCode() + JSONObject.toJSONString(fzjcxxDto));
        }
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }
    /**
     * 查找样品信息
     */
    @RequestMapping("/detectionPJ/pagedataGetInfoByNbbh")
    @ResponseBody
    public Map<String, Object> pagedataGetInfoByNbbh(FzjcxxDto fzjcxxDto){
        Map<String, Object> map= new HashMap<>();
        FzjcxxDto fzjcxxDtoInfo = null;
        try {
            fzjcxxDtoInfo = fzjcxxPJService.getInfoByNbbh(fzjcxxDto);
        } catch (BusinessException e) {
            map.put("message", e);
        }
        map.put("fzjcxxDtoInfo", fzjcxxDtoInfo);
        return map;
    }
    /**
     * 保存样品信息
     */
    @RequestMapping("/detectionPJ/pagedataUpdateInfoByNbbh")
    @ResponseBody
    public Map<String, Object> pagedataUpdateInfoByNbbh(FzjcxxDto fzjcxxDto, HttpServletRequest request){
        Map<String, Object> map= new HashMap<>();
        //获取用户信息
        User user = getLoginInfo(request);
        if (StringUtil.isNotBlank(user.getYhid())){
            fzjcxxDto.setSyry(user.getYhid());
        }
        Boolean success = fzjcxxPJService.updateInfoByNbbh(fzjcxxDto);
        if (success){
            map.put("success", "实验成功!");
        }else {
            map.put("fail", "实验失败!");
        }
        return map;
    }

    /**
     * 结果修改
     */
    @RequestMapping("/detectionPJ/resultmodDetectionPJ")
    public ModelAndView resultmodDetectionPJ(FzjcxmDto fzjcxmDto) {
        ModelAndView mav = new ModelAndView("detection/detectPJ/detectPJ_resultMod");
        List<FzjcxmDto> fzjcxmxxList = fzjcxmPJService.getXmGroupList(fzjcxmDto);
        mav.addObject("ids",StringUtil.join(fzjcxmDto.getIds(),","));
        mav.addObject("list",fzjcxmxxList);
        mav.addObject("size",fzjcxmxxList==null?"0":fzjcxmxxList.size());
        List<JcsjDto> resultlist = new ArrayList<>();
        List<JcsjDto> detections = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.MOLECULAR_DETECTION_RESULT.getCode());
        for (JcsjDto jcsjDto : detections){
            if (!"SUSPICIOUS".equals(jcsjDto.getCsdm())){
                resultlist.add(jcsjDto);
            }
        }
        mav.addObject("resultlist",resultlist);
        mav.addObject("auditType",AuditTypeEnum.AUDIT_GENERAL_INSPECTION.getCode());
        return mav;
    }

    /**
     * 结果查看
     */
    @RequestMapping("/detectionPJ/pagedataDetectionPJResult")
    public ModelAndView pagedataDetectionPJResult(FzjcjgDto fzjcjgDto) {
        ModelAndView mav = new ModelAndView("detection/detectPJ/detectPJ_viewResult");
        List<FzjcjgDto> dtoList = fzjcjgPJService.getDtoList(fzjcjgDto);
        FzjcxxDto fzjcxxDto=new FzjcxxDto();
        fzjcxxDto.setFzjcid(fzjcjgDto.getFzjcid());
        FzjcxxDto dto = fzjcxxPJService.getDto(fzjcxxDto);
        mav.addObject("fzjcxxDto",dto);
        mav.addObject("list",dtoList);
        return mav;
    }


//    /**
//     * 结果修改保存
//     */
//    @RequestMapping("/detectionPJ/resultmodSaveDetectionPJ")
//    @ResponseBody
//    public Map<String, Object> resultmodDetectionSavePJ(FzjcjgDto fzjcjgDto){
//        Map<String, Object> map= new HashMap<>();
//        boolean isSuccess = fzjcxxPJService.resultModSaveDetectionPJ(fzjcjgDto);
//        if (isSuccess){
//            amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.NORMAL_INSPECT_TOWECHAT.getCode(), RabbitEnum.PJXX_RUM.getCode() + JSONObject.toJSONString(fzjcjgDto));
//        }
//        map.put("status", isSuccess?"success":"fail");
//        map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
//        map.put("auditType",AuditTypeEnum.AUDIT_GENERAL_INSPECTION.getCode());
//        map.put("ywid",fzjcjgDto.getIds());
//        return map;
//    }
    /**
     * 结果修改保存
     */
    @RequestMapping("/detectionPJ/resultmodSaveDetectionPJ")
    @ResponseBody
    public Map<String, Object> resultmodSaveDetectionPJ(FzjcjgDto fzjcjgDto){
        Map<String, Object> map= new HashMap<>();
        boolean isSuccess = fzjcxxPJService.resultModSaveDetectionPJ(fzjcjgDto);
        if (isSuccess){
            amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.NORMAL_INSPECT_TOWECHAT.getCode(), RabbitEnum.PJXX_RUM.getCode() + JSONObject.toJSONString(fzjcjgDto));
        }
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        map.put("auditType",AuditTypeEnum.AUDIT_GENERAL_INSPECTION.getCode());
        map.put("ywid",fzjcjgDto.getIds());
        return map;
    }

    /**
     * 点击实验跳转页面，样本接收
     */
    @RequestMapping(value="/detectionPj/acceptSamplePJ")
    public ModelAndView acceptSamplePJ(FzjcxxDto fzjcxxDto) {
        ModelAndView mav=new ModelAndView("detection/detectPJ/detectPJ_sampleAccept");
        FzjcxxDto fzjcxxDtoInfo = new FzjcxxDto();
        String jclxid="";
        List<JcsjDto> bbztlist = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.GENERAL_SAMPLESTATE.getCode());//普检标本状态
        List<JcsjDto> jcdwList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.DETECTION_UNIT.getCode());
        List<JcsjDto> jclxList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
        if(!CollectionUtils.isEmpty(jclxList)){
            for (JcsjDto jclx:jclxList){
                if(fzjcxxDto.getJclx().equals(jclx.getCsdm()))
                    jclxid=jclx.getCsid();
            }
        }
        if(!CollectionUtils.isEmpty(bbztlist)){
            for(int i=bbztlist.size()-1;i>=0;i--){
                if(!jclxid.equals(bbztlist.get(i).getFcsid()))
                    bbztlist.remove(i);
            }
        }
        User user=getLoginInfo();
        //查个人设置打印机地址
        GrszDto grszDto_t=new GrszDto();
        grszDto_t.setYhid(user.getYhid());
        grszDto_t.setSzlb(PersonalSettingEnum.PRINT_ADDRESS.getCode());
        grszDto_t=grszService.selectGrszDtoByYhidAndSzlb(grszDto_t);
        mav.addObject("grszDto", Objects.requireNonNullElseGet(grszDto_t, GrszDto::new));
        if (StringUtil.isNotBlank(fzjcxxDto.getYbbh())){
            try {
                fzjcxxDtoInfo = fzjcxxPJService.getSampleAcceptInfo(fzjcxxDto);
            } catch (BusinessException e) {
                log.error("acceptSamplePJ方法报错"+e.getMessage());
                mav.addObject("message", e);
            }
        }else{
            if( fzjcxxDtoInfo.getBbzts() == null ){
                for (JcsjDto jcsj: bbztlist ) {
                    if ("1".equals(jcsj.getSfmr())){
                        List<String> mrbbzt = new ArrayList<>();
                        mrbbzt.add(jcsj.getCsid());
                        fzjcxxDtoInfo.setPjbbzts(mrbbzt);
                        break;
                    }
                }
            }
            if (StringUtil.isBlank(fzjcxxDtoInfo.getJcdw())){
                for (JcsjDto jcdw: jcdwList ) {
                    if ("1".equals(jcdw.getSfmr())){
                        fzjcxxDtoInfo.setJcdw(jcdw.getCsid());
                        break;
                    }
                }
            }
        }
        mav.addObject("bbztlist", bbztlist);
        mav.addObject("jcdwList",jcdwList);
        mav.addObject("jcdwjsonlist",JSON.toJSONString(jcdwList));
        mav.addObject("fzjcxxDtoInfo", fzjcxxDtoInfo);
        return mav;
    }

    @RequestMapping("/detection/pagedataGetSampleAcceptInfo")
    @ResponseBody
    public Map<String, Object> pagedataGetSampleAcceptInfo(FzjcxxDto fzjcxxDto, HttpServletRequest request){
        Map<String, Object> map= new HashMap<>();
        //获取用户信息
        User user = getLoginInfo(request);
        if (StringUtil.isNotBlank(user.getYhid())){
            fzjcxxDto.setXgry(user.getYhid());
            fzjcxxDto.setJsry(user.getYhid());
        }
        FzjcxxDto fzjcxxDtoInfo  = null;
        try {
            fzjcxxDtoInfo = fzjcxxPJService.getSampleAcceptInfo(fzjcxxDto);
        } catch (BusinessException e) {
            map.put("message", e);
        }
        map.put("fzjcxxDtoInfo", fzjcxxDtoInfo);
        return map;
    }

    /**
     * 样本接收保存
     */
    @RequestMapping("/detectionPj/acceptSaveSamplePJ")
    @ResponseBody
    public Map<String, Object> printCheckReport(FzjcxxDto fzjcxxDto, GrszDto grszDto) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo();
        if (StringUtil.isNotBlank(fzjcxxDto.getSfsy())){
            if (fzjcxxDto.getSfsy().equals("1")){
                fzjcxxDto.setSyry(user.getYhid());
            }
        }
        if (StringUtil.isNotBlank(fzjcxxDto.getSfjs())){
            if (fzjcxxDto.getSfjs().equals("1")){
                fzjcxxDto.setJsry(user.getYhid());
            }
        }
        try {
            if ("1".equals(grszDto.getGrsz_flg())) {
                // 添加个人设置
                grszDto.setYhid(user.getYhid());
                grszDto.setSzlb(PersonalSettingEnum.PRINT_ADDRESS.getCode());
                grszService.delete(grszDto);
                grszDto.setSzid(StringUtil.generateUUID());
                grszService.insert(grszDto);
            }
            // 获取用户信息
            fzjcxxDto.setXgry(user.getYhid());
            String successMessage = xxglService.getModelById("ICOM00001").getXxnr();
            boolean result = true;
            FzjcxxDto fzjcxxDto1 = fzjcxxPJService.getSampleAcceptInfo(fzjcxxDto);
            if(fzjcxxDto1==null){
                map.put("status","fail");
                map.put("message","标本编号不存在！");
                return map;
            }
            fzjcxxDto.setFzjcid(fzjcxxDto1.getFzjcid());
            if (StringUtil.isNotBlank(fzjcxxDto.getNbbh())) {
                if (StringUtil.isNotBlank(fzjcxxDto1.getNbbh())) {
                    result = !fzjcxxDto1.getNbbh().equals(fzjcxxDto.getNbbh());
                }
                if (result){
                    List<FzjcxxDto> fzjcxxDtoList = fzjcxxPJService.getFzjcxxByNbbh(fzjcxxDto);
                    if (fzjcxxDtoList.size() > 1) {
                        String confirm_nbbm = fzjcxxPJService.getFzjcxxByybbh();
                        if (confirm_nbbm != null) {
                            // 采用最新的流水号
                            fzjcxxDto.setNbbh(confirm_nbbm);
                            // 发送消息通知操作人员
                            successMessage = successMessage + "  当前内部编码已经存在,已为您替换为最新的内部编码 [" + confirm_nbbm + "]";
                        }
                    }
                }
            }
            List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
            if(!CollectionUtils.isEmpty(jclxList)){
                for(JcsjDto jcsjDto : jclxList){
                    if(fzjcxxDto.getJclx().equals(jcsjDto.getCsdm()))
                        fzjcxxDto.setJclx(jcsjDto.getCsid());
                }
            }
            List<FzjcxxDto> fzjcxxDtos = fzjcxxPJService.getFzjcxxBySyh(fzjcxxDto);
            if (!CollectionUtils.isEmpty(fzjcxxDtos)){
                String syh = fzjcxxPJService.getPjSyhSerial(fzjcxxDto);//得到流水序号
                if (syh != null) {
                    fzjcxxDto.setSyh(syh);
                    // 发送消息通知操作人员
                    successMessage = successMessage + "  当前实验号已经存在,已为您替换为最新的实验号 [" + syh + "]";
                }
            }
            //更新分子检测信息
            fzjcxxPJService.saveFzjcxxInfo(fzjcxxDto);
            //更新分子检测标本信息
            fzbbztService.insertFzbbzt(fzjcxxDto);
            amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.NORMAL_INSPECT_TOWECHAT.getCode(), RabbitEnum.PJXX_ACT.getCode() + JSONObject.toJSONString(fzjcxxDto));
            map.put("status","success");
            map.put("message",(successMessage));
            if ("1".equals(fzjcxxDto.getSfjs())) {
                String sign = commonService.getSign(fzjcxxDto.getNbbh());
                String type = PrintEnum.PRINT_XGQY.getCode();
                sign = URLEncoder.encode(sign, StandardCharsets.UTF_8);
                String RequestLocalCode = URLEncoder.encode("新冠取样打印",StandardCharsets.UTF_8);
                String printUrl;
                //本机
                if ("0".equals(grszDto.getSzz())){
                    printUrl = "http://localhost:8081/XGQYPrint";
                }else {
                //外机
                    printUrl = "http://"+grszDto.getGlxx()+":8082/XGQYPrint";
                }
                printUrl+="?RequestLocalCode="+RequestLocalCode+"&ResponseSign=" + sign + "&ResponseNum=3&fontflg=2&type="+
                        type+"&nbbh="+fzjcxxDto.getNbbh()+"&ybbh="+fzjcxxDto.getYbbh()+"&syh="+fzjcxxDto.getSyh()+"&qr=qrcode";
                map.put("printUrl",printUrl);
				log.error("打印地址：" + printUrl);
            }
        }
        catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", e.getMsg());
        }
        catch (Exception e) {
            map.put("status", "fail");
            map.put("message", e.toString());
        }
        return map;
    }
    /**
     * 处理检测报告,生成报告
     */
    @RequestMapping("/detectionPJ/reportgeneratePJ")
    public ModelAndView reportgeneratePJ(FzjcxxDto fzjcxxDto) {
        ModelAndView mav = new ModelAndView("detection/detectPJ/detectPJ_reportgenerate");
        List<JcsjDto> jcxmListall = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_ITEM));//所有分子检测项目
        List<JcsjDto> jcxmList=new ArrayList<>();//用来筛选出HPV的检测项目，通过他们的父参数代码判断
        List<JcsjDto> fzjczxmlist=new ArrayList<>();//筛选出备选的子项目
        if(StringUtil.isNotBlank(fzjcxxDto.getJclx())) {
            JcsjDto jcsjDto = new JcsjDto();
            jcsjDto.setCsdm(fzjcxxDto.getJclx());//根据列表拿到参数代码，然后进行筛查
            jcsjDto.setJclb(BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
            JcsjDto jcsjDto1=jcsjService.getDtoByCsdmAndJclb(jcsjDto);//查找出TYPE_HPV的csid，进行筛查相应的检测项目
            for (JcsjDto jcxm : jcxmListall) {
                if(jcsjDto1.getCsid().equals(jcxm.getFcsid()))
                    jcxmList.add(jcxm);
            }
            List<JcsjDto> fzjczxms = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_SUBITEM));
            for (JcsjDto fzjczxm : fzjczxms) {
                String fcsid = fzjczxm.getFcsid();
                for(JcsjDto jcxm:jcxmList){
                    if(fcsid.equals(jcxm.getCsid())){
                        fzjczxm.setKz4mc(jcxm.getCskz4());
                        fzjczxmlist.add(fzjczxm);
                    }
                }
            }
        }
        mav.addObject("fzjcxmlist", jcxmList);//检测项目
        mav.addObject("fzjczxmlist", fzjczxmlist);
        mav.addObject("fzjcxxDto",fzjcxxDto);
        return mav;
    }
    /**
     * 处理检测报告,生成报告
     */
    @RequestMapping("/detectionPJ/reportgenerateSavePJ")
    @ResponseBody
    public Map<String,Object> reportgenerateSavePJ(FzjcxmDto fzjcxmDto) {
        Map<String,Object> map=new HashMap<>();
        User user = getLoginInfo();
        fzjcxmDto.setSyry(user.getYhid());
        fzjcxmDto.setFlag("1");//判断是审核通过的分子检测项目
        if (ArrayUtils.isEmpty(fzjcxmDto.getFzjczxmids())){
            map.put("status", "fail");
            map.put("message", "未选择检测子项目");
            return map;
        }
        boolean isSuccess;
        try {
            isSuccess = fzjcxmPJService.dealAndGenerateReport(fzjcxmDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
                    : xxglService.getModelById("ICOM00002").getXxnr());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
        }catch (Exception e) {
            map.put("status", "fail");
            map.put("message", e.getMessage());
        }
        return map;
    }
    /**
     * HPV报告审核列表
     */
    @RequestMapping("/detectionPJ/pageListExamineScreening")
    public ModelAndView pageListExamineScreening(FzjcxxDto fzjcxxDto) {
        ModelAndView mav = new ModelAndView("detection/detectPJ/screening_listaudit");
        mav.addObject("samplelist",redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.GENERALSAMPLE_TYPE.getCode()));
        mav.addObject("jclx",fzjcxxDto.getJclx());
        return mav;
    }
    /**
     * HPV报告审核列表待审核
     */
    @RequestMapping("/detectionPJ/pageGetListExamineScreening")
    @ResponseBody
    public Map<String, Object> pageGetListExamineScreening(FzjcxxDto fzjcxxDto){
        //附加委托参数
        DataPermission.addWtParam(fzjcxxDto);
        //附加审核状态过滤
        if(GlobalString.AUDIT_SHZT_YSH.equals(fzjcxxDto.getDqshzt())){
            DataPermission.add(fzjcxxDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "fzjcxm", "fzxmid", AuditTypeEnum.AUDIT_GENERAL_INSPECTION);
        }else{
            DataPermission.add(fzjcxxDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "fzjcxm", "fzxmid", AuditTypeEnum.AUDIT_GENERAL_INSPECTION);
        }
        DataPermission.addCurrentUser(fzjcxxDto, getLoginInfo());
        User user=getLoginInfo();
        List<Map<String,String>> jcdwList=fzjcxxPJService.getJsjcdwByjsid(user.getDqjs());
        if(!CollectionUtils.isEmpty(jcdwList) && "1".equals(jcdwList.get(0).get("dwxdbj"))) {
            List<String> strList=new ArrayList<>();
            for (Map<String, String> stringStringMap : jcdwList) {
                if (stringStringMap.get("jcdw") != null) {
                    strList.add(stringStringMap.get("jcdw"));
                }
            }
            if(strList.size()>0) {
                fzjcxxDto.setJcdwxz(strList);
            }
        }
        List<FzjcxxDto> t_List = fzjcxxPJService.getPagedAuditList(fzjcxxDto);

        //Json格式的要求{total:22,rows:{}}
        //构造成Json的格式传递
        Map<String,Object> result = new HashMap<>();
        result.put("total", fzjcxxDto.getTotalNumber());
        result.put("rows", t_List);
        return result;
    }
    /**
     * HPV报告审核按钮
     */
    @RequestMapping("/detectionPJ/auditScreeningReport")
    public ModelAndView auditScreeningReport(FzjcxmDto fzjcxmDto) {
        ModelAndView mav = new ModelAndView("detection/detectPJ/screening_edit");
        FzjcxmDto fzjcxmDto_t = fzjcxmPJService.getDto(fzjcxmDto);
        if(StringUtil.isNotBlank(fzjcxmDto_t.getSjdwmc())){
            fzjcxmDto_t.setSjdwmc(fzjcxmDto_t.getDwmc()+"-"+fzjcxmDto_t.getSjdwmc());
        }
        FzjcxxDto fzjcxxDto = new FzjcxxDto();
        fzjcxxDto.setFzjcid(fzjcxmDto_t.getFzjcid());
        fzjcxmDto_t.setBbztmc((fzjcxxPJService.getBbzts(fzjcxxDto)).getBbztmc());
        mav.addObject("fzjcxmDto",fzjcxmDto_t);
        return mav;
    }
    /**
     * 普检报告导入
     */
    @RequestMapping("/detectionPJ/pageImportScreening")
    public ModelAndView screeningImport() {
        ModelAndView mav=new ModelAndView("detection/detectPJ/screening_import");
        FjcfbDto fjcfbDto = new FjcfbDto();
        fjcfbDto.setYwlx(BusTypeEnum.IMP_SCREENING.getCode());
        mav.addObject("fjcfbDto", fjcfbDto);
        return mav;
    }

    /**
     * 文件上传
     */
    @RequestMapping(value="/detectionPJ/resultuploadDetectionPJ")
    public ModelAndView resultuploadDetectionPJ(String jclx){
        ModelAndView mav=new ModelAndView("detection/detectPJ/detectPJ_upload");
        List<JcsjDto> jcxmListall = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_ITEM));//所有分子检测项目
        List<JcsjDto> jcxmList=new ArrayList<>();//用来筛选出HPV的检测项目，通过他们的父参数代码判断
        JcsjDto jcsjDto=new JcsjDto();
        jcsjDto.setCsdm(jclx);//根据列表拿到参数代码，然后进行筛查
        jcsjDto.setJclb(BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
        JcsjDto jcsjDto1=jcsjService.getDtoByCsdmAndJclb(jcsjDto);//查找出TYPE_HPV的csid，进行筛查相应的检测项目
        for (JcsjDto jcxm : jcxmListall) {
            if(jcsjDto1.getCsid().equals(jcxm.getFcsid()))
                jcxmList.add(jcxm);
        }
        String ywlx="";
        if("TYPE_FLU".equals(jclx)){
            ywlx=BusTypeEnum.IMP_FILE_FLU_TEMEPLATE.getCode();
        }else if("TYPE_JHPCR".equals(jclx)){
            ywlx=BusTypeEnum.IMP_FILE_JHPCR_TEMEPLATE.getCode();
        }
        mav.addObject("fzjcxmlist", jcxmList);//检测项目
        mav.addObject("ywlx",ywlx);
        mav.addObject("jclx",jclx);
        return mav;
    }

    /**
     * 文件保存
     */
    @RequestMapping(value = "/detectionPJ/resultuploadSaveDetectionPJ")
    @ResponseBody
    public Map<String, Object> resultuploadSaveDetectionPJ(FzjcxxDto fzjcxxDto){
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> newMap = new HashMap<>();
        newMap.put("fjids",fzjcxxDto.getFjids());
        newMap.put("fzjcxmid",fzjcxxDto.getFzjcxmid());
        newMap.put("fzjczxmid",fzjcxxDto.getFzjczxmid());
        newMap.put("jclx",fzjcxxDto.getJclx());
        amqpTempl.convertAndSend("wechat.exchange",MQTypeEnum.FLU_FILE_ANALYSIS.getCode(),JSONObject.toJSONString(newMap));
        map.put("status", "success");
        map.put("fjids", fzjcxxDto.getFjids());
        map.put("jclx", fzjcxxDto.getJclx());
        return map;
    }

    /**
     * 文件上传进度
     */
    @RequestMapping(value="/detectionPJ/pagedataFluUpView")
    public ModelAndView pagedataFluUpView(FzjcxxDto fzjcxxDto){
        ModelAndView mav=new ModelAndView("detection/detectPJ/detectPJ_uploadView");
        mav.addObject("fjids",fzjcxxDto.getFjid());
        mav.addObject("jclx",fzjcxxDto.getJclx());
        mav.addObject("count",StringUtil.isBlank(fzjcxxDto.getFjid())?0:fzjcxxDto.getFjid().split(",").length);
        mav.addObject("jgList",JSON.toJSONString(redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.MOLECULAR_DETECTION_RESULT.getCode())));
        return mav;
    }

    /**
     * 文件上传进度数据
     */
    @RequestMapping(value="/detectionPJ/pagedataFluUpViewList")
    @ResponseBody
    public Map<String, Object> pagedataFluUpViewList(FzjcxxDto fzjcxxDto){
        Map<String, Object> map= new HashMap<>();
        if (!CollectionUtils.isEmpty(fzjcxxDto.getFjids())){
            List<FzjcjgDto> dtos = new ArrayList<>();
            List<FzjcjgDto> allDtos = new ArrayList<>();
            List<FzzkjgDto> fzzkjgDtos = new ArrayList<>();
            int count = 0;
            for (String fjid : fzjcxxDto.getFjids()) {
                Object hget = redisUtil.get("FLU_RESULT_UP:"+fjid);
                if (hget != null){
                    List<FzjcjgDto> list = (List<FzjcjgDto>) JSON.parseArray(String.valueOf(hget),FzjcjgDto.class);
                    allDtos.addAll(list);
                    for(FzjcjgDto dto:list){
                        if(!"CY5".equals(dto.getTd())){
                            dtos.add(dto);
                        }
                    }
                    count ++;
                }
                Object object = redisUtil.get("FLU_QUALITY_CONTROL_RESULT_UP:" + fjid);
                if (object != null){
                    List<FzzkjgDto> list = (List<FzzkjgDto>) JSON.parseArray(String.valueOf(object),FzzkjgDto.class);
                    fzzkjgDtos.addAll(list);
                }
            }
            map.put("count",count);
            map.put("rows",dtos);
            map.put("allDtos",allDtos);
            map.put("fzzkjgDtos",fzzkjgDtos);
        }
        return map;
    }

    /**
     * 结果数据保存
     */
    @RequestMapping(value = "/detectionPJ/pagedataSaveUploadData")
    @ResponseBody
    public Map<String, Object> pagedataSaveUploadData(FzjcjgDto fzjcjgDto){
        Map<String,Object> map = new HashMap<>();
        User user = getLoginInfo();
        fzjcjgDto.setLrry(user.getYhid());
        boolean isSuccess=fzjcjgPJService.uploadSave(fzjcjgDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    @RequestMapping("/detectionPJ/pagedataGetJcxms")
    @ResponseBody
    public Map<String,Object> pagedataGetJcxms(FzjcxmDto fzjcxmDto){
        Map<String, Object> map= new HashMap<>();
        List<FzjcxmDto> jcxmlist = new ArrayList<>();
        if (StringUtil.isNotBlank(fzjcxmDto.getFzjcid())){
            jcxmlist=fzjcxmPJService.getJcxmListByFzjcid(fzjcxmDto.getFzjcid());
            map.put("rows",jcxmlist);
        } else {
            map.put("rows",jcxmlist);
        }
        return map;
    }
}
