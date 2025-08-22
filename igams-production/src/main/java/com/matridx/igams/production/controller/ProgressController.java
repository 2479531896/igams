package com.matridx.igams.production.controller;

import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.GrszDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.ShxxDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.DataPermissionTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IGrszService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IShxxService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.production.dao.entities.BomglDto;
import com.matridx.igams.production.dao.entities.BommxDto;
import com.matridx.igams.production.dao.entities.CpxqjhDto;
import com.matridx.igams.production.dao.entities.SczlglDto;
import com.matridx.igams.production.dao.entities.SczlmxDto;
import com.matridx.igams.production.dao.entities.WlglDto;
import com.matridx.igams.production.dao.entities.XqjhmxDto;
import com.matridx.igams.production.service.svcinterface.ICpxqjhService;
import com.matridx.igams.production.service.svcinterface.IScbomService;
import com.matridx.igams.production.service.svcinterface.IScbommxService;
import com.matridx.igams.production.service.svcinterface.ISczlglService;
import com.matridx.igams.production.service.svcinterface.ISczlmxService;
import com.matridx.igams.production.service.svcinterface.IWlglService;
import com.matridx.igams.production.service.svcinterface.IXqjhmxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

@Controller
@RequestMapping("/progress")
public class ProgressController extends BaseController {
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;

    @Override
    public String getPrefix(){
        return urlPrefix;
    }

    @Autowired
    private ICpxqjhService cpxqjhService;
    @Autowired
    private ISczlglService sczlglService;
    @Autowired
    private ISczlmxService sczlmxService;
    @Autowired
    private IXqjhmxService xqjhmxService;
    @Autowired
    ICommonService commonservice;
    @Autowired
    IWlglService wlglService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    IXxglService xxglService;
    @Autowired
    IFjcfbService fjcfbService;
    @Autowired
    IShxxService shxxService;
    @Autowired
    IScbomService scbomService;
    @Autowired
    IScbommxService scbommxService;
    @Autowired
    IJcsjService jcsjService;
    @Autowired
    IGrszService grszService;
    private final Logger log = LoggerFactory.getLogger(ProgressController.class);
    /**
     * 需求计划列表页面
     */
    @RequestMapping(value = "/progress/pageListProgress")
    public ModelAndView pageListProgress() {
        ModelAndView mav = new ModelAndView("production/progress/progress_list");
        mav.addObject("auditType", AuditTypeEnum.AUDIT_FG_PLAN.getCode());
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 需求计划列表数据
     */
    @RequestMapping(value = "/progress/pageGetListProgress")
    @ResponseBody
    public Map<String, Object> getPagedDtoProgressList(CpxqjhDto cpxqjhDto) {
        return pageGetListWaitProgress(cpxqjhDto);
    }
    /**
     * 待生产进度列表页面
     */
    @RequestMapping(value = "/progress/pageListWaitProgress")
    public ModelAndView pageListWaitProgress() {
        ModelAndView mav = new ModelAndView("production/progress/progressWait_list");
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("czbs", "wait");
        return mav;
    }

    /**待生产列表修改生产状态
     *
     */
    @RequestMapping("/produce/modChoose")
    @ResponseBody
    public Map<String,Object> modChoose(XqjhmxDto xqjhmxDto, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        try {
            User user = getLoginInfo(request);
            xqjhmxDto.setXgry(user.getYhid());
            boolean isSuccess=xqjhmxService.modSaveChoose(xqjhmxDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        }catch (BusinessException e){
            map.put("status","fail");
            map.put("message",e.getMsg());
        }
        return map;
    }
    /**
     * 生产指令列表
     */
    @RequestMapping(value = "/produce/pageListProduce")
    public ModelAndView pageListProduce() {
        ModelAndView mav = new ModelAndView("production/produce/produce_list");
        // mav.addObject("YQ_auditType", AuditTypeEnum.AUDIT_YQ_PRODUCE.getCode());
        // mav.addObject("SJ_auditType", AuditTypeEnum.AUDIT_SJ_PRODUCE.getCode());
        List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.PRODUCTS_TYPE.getCode());
        mav.addObject("lblist", jcsjDtos);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 生产指令列表数据
     */
    @RequestMapping(value = "/produce/pageGetListProduce")
    @ResponseBody
    public Map<String, Object> getPagedDtoProduceList(SczlglDto sczlglDto) {
        Map<String, Object> map = new HashMap<>();
        List<SczlglDto> pagedDtoList = sczlglService.getPagedDtoList(sczlglDto);
        map.put("total", sczlglDto.getTotalNumber());
        map.put("rows",pagedDtoList);
        return map;
    }

    /**
     * 待生产进度列表页面
     */
    @RequestMapping(value = "/progress/produceChoose")
    public ModelAndView produceChoose(SczlglDto sczlglDto) {
        ModelAndView mav = new ModelAndView("production/progress/progressWait_choose");
        List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.PRODUCTS_TYPE.getCode());
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("xqjhmxid", sczlglDto.getXqjhmxid());
        mav.addObject("sfsc", sczlglDto.getSfsc());
        mav.addObject("jcsjDtos", jcsjDtos);
        return mav;
    }
    /**
     * 生产指令新增选择
     */
    @RequestMapping(value = "/produce/addProduceChoose")
    public ModelAndView produceChooseAdd() {
        ModelAndView mav = new ModelAndView("production/progress/progressWait_choose");
        List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.PRODUCTS_TYPE.getCode());
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("jcsjDtos", jcsjDtos);
        return mav;
    }

    /**
     * 生产指令新增
     */
    @RequestMapping("/produce/pagedataProduceAdd")
    public ModelAndView produceAdd(SczlglDto sczlglDto) {
        ModelAndView mav = new  ModelAndView("production/produce/produce_edit");
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        sczlglDto.setZlrq(simpleDateFormat.format(date));
        String zldh = sczlglService.getZldh();
        sczlglDto.setZldh(zldh);
        sczlglDto.setFormAction("/progress/progress/pagedataSaveProduce");
        if ("SJ".equals(sczlglDto.getCplxmc())){
            mav.addObject("shlx",AuditTypeEnum.AUDIT_SJ_PRODUCE.getCode());
        }else if ("YQ".equals(sczlglDto.getCplxmc())){
            mav.addObject("shlx",AuditTypeEnum.AUDIT_YQ_PRODUCE.getCode());
        }
        mav.addObject("url", "/progress/progress/pagedataListProduce");
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("sczlglDto", sczlglDto);
        mav.addObject("ywlx",BusTypeEnum.IMP_PRODUCE.getCode());
        return mav;
    }

    /**
     * 生产指令修改
     */
    @RequestMapping("/produce/modProduce")
    public ModelAndView produceMod(SczlglDto sczlglDto) {
        ModelAndView mav = new  ModelAndView("production/produce/produce_edit");
        SczlglDto dto = sczlglService.getDto(sczlglDto);
        if (StringUtil.isNotBlank(sczlglDto.getCzbs())){
            dto.setCzbs(sczlglDto.getCzbs());
        }
        dto.setFormAction("/progress/produce/modSaveProduce");
        mav.addObject("url", "/progress/progress/pagedataProduceModList");
        FjcfbDto fjcfbDto = new FjcfbDto();
        fjcfbDto.setYwlx(BusTypeEnum.IMP_PRODUCE.getCode());
        fjcfbDto.setYwid(dto.getSczlid());
        dto.setCplxmc(dto.getCplxdm());
        dto.setJhcl(String.valueOf((int)Double.parseDouble(dto.getJhcl())));
        List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        if ("SJ".equals(dto.getCplxmc())){
            mav.addObject("shlx",AuditTypeEnum.AUDIT_SJ_PRODUCE.getCode());
        }else if ("YQ".equals(dto.getCplxmc())){
            mav.addObject("shlx",AuditTypeEnum.AUDIT_YQ_PRODUCE.getCode());
        }
        mav.addObject("fjcfbDtos",fjcfbDtos);
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("sczlglDto", dto);
        mav.addObject("ywlx",BusTypeEnum.IMP_PRODUCE.getCode());
        return mav;
    }

    /**
     * 生产指令审核
     */
    @RequestMapping("/produce/bulksubmitProduce")
    public ModelAndView produceAudit(SczlglDto sczlglDto) {
        ModelAndView mav = new  ModelAndView("production/produce/produce_audit");
        List<FjcfbDto> fjcfbDtos = new ArrayList<>();
        if (StringUtil.isNotBlank(sczlglDto.getCzbs())){
            FjcfbDto fjcfbDto = new FjcfbDto();
            fjcfbDto.setYwlx(BusTypeEnum.IMP_PRODUCE.getCode());
            fjcfbDto.setYwid(sczlglDto.getSczlmxid());
            fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        }
        if (!CollectionUtils.isEmpty(sczlglDto.getIds())){
            StringBuilder ywids = new StringBuilder();
            for (int i = 0; i < sczlglDto.getIds().size(); i++) {
                ywids.append(sczlglDto.getIds().get(i));
                if (i != sczlglDto.getIds().size()-1){
                    ywids.append(",");
                }
            }
            sczlglDto.setYwids(ywids.toString());
        }
        mav.addObject("fjcfbDtos",fjcfbDtos);
        mav.addObject("sczlglDto",sczlglDto);
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("ywlx",BusTypeEnum.IMP_PRODUCE.getCode());
        return mav;
    }
    /**
     * 生产指令审核
     */
    @RequestMapping("/produce/auditProduce")
    public ModelAndView auditProduce(SczlglDto sczlglDto) {
        ModelAndView mav = new  ModelAndView("production/produce/produce_audit");
        List<FjcfbDto> fjcfbDtos = new ArrayList<>();
        if (StringUtil.isNotBlank(sczlglDto.getCzbs())){
            FjcfbDto fjcfbDto = new FjcfbDto();
            fjcfbDto.setYwlx(BusTypeEnum.IMP_PRODUCE.getCode());
            fjcfbDto.setYwid(sczlglDto.getSczlmxid());
            fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        }
        if (!CollectionUtils.isEmpty(sczlglDto.getIds())){
            StringBuilder ywids = new StringBuilder();
            for (int i = 0; i < sczlglDto.getIds().size(); i++) {
                ywids.append(sczlglDto.getIds().get(i));
                if (i != sczlglDto.getIds().size()-1){
                    ywids.append(",");
                }
            }
            sczlglDto.setYwids(ywids.toString());
        }
        mav.addObject("fjcfbDtos",fjcfbDtos);
        mav.addObject("sczlglDto",sczlglDto);
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("ywlx",BusTypeEnum.IMP_PRODUCE.getCode());
        return mav;
    }
    /**
     * 生产指令审核
     */
    @RequestMapping("/produce/batchauditProduce")
    public ModelAndView batchauditProduce(SczlglDto sczlglDto) {
        ModelAndView mav = new  ModelAndView("production/produce/produce_audit");
        List<FjcfbDto> fjcfbDtos = new ArrayList<>();
        if (StringUtil.isNotBlank(sczlglDto.getCzbs())){
            FjcfbDto fjcfbDto = new FjcfbDto();
            fjcfbDto.setYwlx(BusTypeEnum.IMP_PRODUCE.getCode());
            fjcfbDto.setYwid(sczlglDto.getSczlmxid());
            fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        }
        if (!CollectionUtils.isEmpty(sczlglDto.getIds())){
            StringBuilder ywids = new StringBuilder();
            for (int i = 0; i < sczlglDto.getIds().size(); i++) {
                ywids.append(sczlglDto.getIds().get(i));
                if (i != sczlglDto.getIds().size()-1){
                    ywids.append(",");
                }
            }
            sczlglDto.setYwids(ywids.toString());
        }
        mav.addObject("fjcfbDtos",fjcfbDtos);
        mav.addObject("sczlglDto",sczlglDto);
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("ywlx",BusTypeEnum.IMP_PRODUCE.getCode());
        return mav;
    }

    /**
     * 新增保存
     */
    @RequestMapping("/produce/modSaveProduce")
    @ResponseBody
    public Map<String,Object> produceModSave(SczlglDto sczlglDto, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        //判断调拨单是否存在
        User user = getLoginInfo(request);
        sczlglDto.setXgry(user.getYhid());
        try {
            List<String> ywids = sczlglService.produceModSave(sczlglDto);
            map.put("status", "success");
            map.put("ywids", ywids);
            map.put("message", xxglService.getModelById("ICOM00001").getXxnr());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
        }
        return map;
    }

    /**
     * 删除
     */
    @RequestMapping("/produce/discardProduce")
    @ResponseBody
    public Map<String,Object> discardProduce(SczlglDto sczlglDto, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        sczlglDto.setScry(user.getYhid());
        sczlglDto.setScbj("1");
        try {
            boolean isSuccess = sczlglService.produceDel(sczlglDto);
            map.put("status", isSuccess?"success":"fail");
            map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00004").getXxnr());
        }
        return map;
    }
    /**
     * 删除
     */
    @RequestMapping("/produce/delProduce")
    @ResponseBody
    public Map<String,Object> delProduce(SczlglDto sczlglDto, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        sczlglDto.setScry(user.getYhid());
        sczlglDto.setScbj("1");
        try {
            boolean isSuccess = sczlglService.produceDel(sczlglDto);
            map.put("status", isSuccess?"success":"fail");
            map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00004").getXxnr());
        }
        return map;
    }

    /**
     * 生产进度列表
     */
    @RequestMapping(value = "/progress/pagedataProduceModList")
    @ResponseBody
    public Map<String, Object> produceModList(SczlglDto sczlglDto) {
        Map<String, Object> map = new HashMap<>();
        List<SczlglDto> dtoList = new ArrayList<>();
        if (StringUtil.isNotBlank(sczlglDto.getSczlmxid())){
            dtoList = sczlglService.getDtoList(sczlglDto);
        }
        map.put("rows",dtoList);
        return map;
    }

    /**
     * 查看按钮
     */
    @RequestMapping("/produce/viewProduce")
    public ModelAndView viewProduce(SczlglDto sczlglDto) {
        ModelAndView mav = new  ModelAndView("production/produce/produce_view");
        SczlglDto dto = sczlglService.getDto(sczlglDto);
        FjcfbDto fjcfbDto = new FjcfbDto();
        fjcfbDto.setYwlx(BusTypeEnum.IMP_PRODUCE.getCode());
        fjcfbDto.setYwid(dto.getSczlid());
        List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        FjcfbDto fjcfbDto1 = new FjcfbDto();
        fjcfbDto1.setYwlx(BusTypeEnum.IMP_PRODUCE.getCode());
        fjcfbDto1.setYwid(dto.getSczlmxid());
        List<FjcfbDto> fjcfbDtoList = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto1);
        SczlmxDto sczlmxDto = new SczlmxDto();
        sczlmxDto.setSczlid(dto.getSczlid());
        List<SczlmxDto> dtoList = sczlmxService.getDtoList(sczlmxDto);
        mav.addObject("dtoList",dtoList);
        mav.addObject("fjcfbDtos",fjcfbDtos);
        mav.addObject("fjcfbDtoList",fjcfbDtoList);
        mav.addObject("dto",dto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 查看指令信息
     */
    @RequestMapping("/produce/pagedataProduceForll")
    public ModelAndView pagedataProduceForll(SczlglDto sczlglDto) {
        ModelAndView mav = new  ModelAndView("production/produce/produce_viewforll");
        SczlglDto dto = sczlglService.getDtoById(sczlglDto.getSczlid());
        FjcfbDto fjcfbDto = new FjcfbDto();
        fjcfbDto.setYwlx(BusTypeEnum.IMP_PRODUCE.getCode());
        fjcfbDto.setYwid(dto.getSczlid());
        List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        SczlmxDto sczlmxDto = new SczlmxDto();
        sczlmxDto.setSczlid(dto.getSczlid());
        List<SczlmxDto> dtoList = sczlmxService.getDtoList(sczlmxDto);
        mav.addObject("dtoList",dtoList);
        mav.addObject("fjcfbDtos",fjcfbDtos);
        mav.addObject("dto",dto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 生产进度列表
     */
    @RequestMapping(value = "/progress/pageGetListWaitProgress")
    @ResponseBody
    public Map<String, Object> pageGetListWaitProgress(CpxqjhDto cpxqjhDto) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isNotBlank(cpxqjhDto.getCzbs())){
            List<CpxqjhDto> pagedDtoList = cpxqjhService.getPagedDtoList(cpxqjhDto);
            map.put("total", cpxqjhDto.getTotalNumber());
            map.put("rows",pagedDtoList);
        }
       else {
            List<CpxqjhDto> pagedDtoList = cpxqjhService.getPagedXqjhDtoList(cpxqjhDto);
            map.put("total", cpxqjhDto.getTotalNumber());
            map.put("rows",pagedDtoList);
        }
        return map;
    }

    /**
     * 查看按钮
     */
    @RequestMapping("/progress/viewProgress")
    public ModelAndView viewProgress(CpxqjhDto cpxqjhDto) {
        ModelAndView mav = new  ModelAndView("production/progress/progress_view");
        CpxqjhDto dtoById = cpxqjhService.getDtoById(cpxqjhDto.getCpxqid());
        XqjhmxDto xqjhmxDto = new XqjhmxDto();
        xqjhmxDto.setXqjhid(cpxqjhDto.getCpxqid());
        List<XqjhmxDto> dtoList = xqjhmxService.getDtoListById(xqjhmxDto);
        List<SczlmxDto> sczlmxDtos=new ArrayList<>();
        if (!CollectionUtils.isEmpty(dtoList)){
             sczlmxDtos=sczlmxService.getSczlmxDtos(dtoList);
        }
        if (!CollectionUtils.isEmpty(dtoList)&&!CollectionUtils.isEmpty(sczlmxDtos)){
            for (XqjhmxDto xqjhmxDto1:dtoList){
                List<SczlmxDto> list=new ArrayList<>();
                for (SczlmxDto sczlmxDto:sczlmxDtos){
                    if (xqjhmxDto1.getXqjhmxid().equals(sczlmxDto.getXqjhmxid())){
                        list.add(sczlmxDto);
                    }
                }
                xqjhmxDto1.setSczlmxDtos(list);
            }
        }

        FjcfbDto fjcfbDto = new FjcfbDto();
        fjcfbDto.setYwlx(BusTypeEnum.IMP_DEMAND.getCode());
        fjcfbDto.setYwid(cpxqjhDto.getCpxqid());
        List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        fjcfbDto.setYwlx(BusTypeEnum.IMP_PRODUCTION_REVIEW.getCode());
        List<FjcfbDto> fjcfbDtos_t = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        mav.addObject("fjcfbDtos_t",fjcfbDtos_t);
        mav.addObject("fjcfbDtos",fjcfbDtos);
        mav.addObject("cpxqjh",dtoById);
        mav.addObject("mxlist",dtoList);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 查看按钮
     */
    @RequestMapping("/progress/pagedataProgress")
    public ModelAndView pagedataProgress(CpxqjhDto cpxqjhDto) {
        return viewProgress(cpxqjhDto);
    }
    /**
     * 钉钉产品需求查看
     */
    @RequestMapping("/progress/minidataViewProgress")
    @ResponseBody
    public Map<String,Object> minidataViewProgress(CpxqjhDto cpxqjhDto) {
        Map<String,Object> map = new HashMap<>();
        CpxqjhDto dtoById = cpxqjhService.getDtoById(cpxqjhDto.getCpxqid());
        XqjhmxDto xqjhmxDto = new XqjhmxDto();
        xqjhmxDto.setXqjhid(cpxqjhDto.getCpxqid());
        List<XqjhmxDto> dtoList = xqjhmxService.getDtoList(xqjhmxDto);
        map.put("cpxqjh",dtoById);
        map.put("mxlist",dtoList);
        map.put("urlPrefix", urlPrefix);
        return map;
    }
    /**
     * 新增
     */
    @RequestMapping("/progress/addProgress")
    public ModelAndView addProgress (HttpServletRequest request, CpxqjhDto cpxqjhDto) {
        ModelAndView mav = new  ModelAndView("production/progress/progress_edit");
        User user = getLoginInfo(request);
        user = commonservice.getUserInfoById(user);
        if (user != null) {
            cpxqjhDto.setSqr(user.getYhid());
            cpxqjhDto.setSqrmc(user.getZsxm());
            cpxqjhDto.setSqbm(user.getJgid());
            cpxqjhDto.setSqbmmc(user.getJgmc());
        }
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        cpxqjhDto.setXqrq(simpleDateFormat.format(date));
        String xqdh = cpxqjhService.getXqdh();
        cpxqjhDto.setXqdh(xqdh);
        cpxqjhDto.setFormAction("/progress/progress/addSaveProgress");
        mav.addObject("url", "/progress/progress/pagedataProgressMore");
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("cpxqjhDto", cpxqjhDto);
        mav.addObject("ywlx",BusTypeEnum.IMP_DEMAND.getCode());
        mav.addObject("auditType", AuditTypeEnum.AUDIT_FG_PLAN.getCode());
        return mav;
    }

    /**
     * 生产
     */
    @RequestMapping("/progress/pagedataProduce")
    public ModelAndView produce(SczlglDto sczlglDto) {
        ModelAndView mav = new  ModelAndView("production/progress/progress_produce");
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        sczlglDto.setZlrq(simpleDateFormat.format(date));
        String zldh = sczlglService.getZldh();
        sczlglDto.setZldh(zldh);
        List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.AUDIT_TYPE.getCode());
        List<JcsjDto> auditlist = new ArrayList<>();
        for (JcsjDto jcsjDto : jcsjDtos) {
            if (StringUtil.isNotBlank(sczlglDto.getCplx())){
                if (sczlglDto.getCplx().equals(jcsjDto.getFcsid())){
                    auditlist.add(jcsjDto);
                }
            }
        }
        sczlglDto.setFormAction("/progress/progress/pagedataSaveProduce");
        mav.addObject("url", "/progress/progress/pagedataListProduce");
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("sczlglDto", sczlglDto);
        mav.addObject("auditlist", auditlist);
        mav.addObject("ywlx",BusTypeEnum.IMP_PRODUCE.getCode());
        return mav;
    }

    /**
     * 新增保存
     */
    @RequestMapping("/progress/pagedataSaveProduce")
    @ResponseBody
    public Map<String,Object> produceAddSave(SczlglDto sczlglDto, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        boolean isSuccess = true;
        SczlglDto dto = new SczlglDto();
        dto.setZldh(sczlglDto.getZldh());
        List<SczlglDto> dtoList = sczlglService.getDtoList(dto);
        XqjhmxDto xqjhmxDto=new XqjhmxDto();
        if ("0".equals(sczlglDto.getSfsc())){
            xqjhmxDto=xqjhmxService.getDtoById(sczlglDto.getXqjhmxid());
            sczlglDto.setSqr(xqjhmxDto.getSqr());
            sczlglDto.setYhm(xqjhmxDto.getYhm());
        }
        if (!CollectionUtils.isEmpty(dtoList)){
            isSuccess = false;
        }
        if(!isSuccess) {
            map.put("status", "fail");
            map.put("message", "指令单号已存在！");
            return map;
        }
        sczlglDto.setLrry(user.getYhid());
        sczlglDto.setYhm(user.getYhm());
        sczlglDto.setSczlid(StringUtil.generateUUID());
        try {
            List<String> ywids = sczlglService.produceAddSave(sczlglDto, xqjhmxDto);
            map.put("status", "success");
            map.put("ywids", ywids);
            map.put("message", xxglService.getModelById("ICOM00001").getXxnr());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
        }
        return map;
    }
    /**
     * 生产进度列表
     */
    @RequestMapping(value = "/progress/pagedataListProduce")
    @ResponseBody
    public Map<String, Object> produceList(SczlglDto sczlglDto) {
        Map<String, Object> map = new HashMap<>();
        List<XqjhmxDto> dtoList = new ArrayList<>();
        if (StringUtil.isNotBlank(sczlglDto.getXqjhmxid())){
            XqjhmxDto xqjhmxDto = new XqjhmxDto();
            xqjhmxDto.setXqjhmxid(sczlglDto.getXqjhmxid());
            dtoList = xqjhmxService.getDtoListById(xqjhmxDto);
        }
        map.put("rows",dtoList);
        return map;
    }

    /**
     * 生产进度列表
     */
    @RequestMapping(value = "/progress/pagedataProgressMore")
    @ResponseBody
    public Map<String, Object> getProgressMore(XqjhmxDto xqjhmxDto) {
        Map<String, Object> map = new HashMap<>();
        List<XqjhmxDto> dtoList = new ArrayList<>();
        if (StringUtil.isNotBlank(xqjhmxDto.getXqjhid())){
            dtoList = xqjhmxService.getDtoList(xqjhmxDto);
        }
        map.put("rows",dtoList);
        return map;
    }

    /**
     * 刷新需求单号
     */
    @RequestMapping("/progress/pagedataRefreshNumber")
    @ResponseBody
    public Map<String, Object> RefreshNumber(){
        Map<String,Object> map = new HashMap<>();
        map.put("xqdh",cpxqjhService.getXqdh());
        return map;
    }


    /**
     * 刷新指令单号
     */
    @RequestMapping("/progress/pagedataRefreshZlNumber")
    @ResponseBody
    public Map<String, Object> RefreshZlNumber(){
        Map<String,Object> map = new HashMap<>();
        map.put("zldh",sczlglService.getZldh());
        return map;
    }

    /**
     * 新增保存
     */
    @RequestMapping("/progress/addSaveProgress")
    @ResponseBody
    public Map<String,Object> progressAddSave(CpxqjhDto cpxqjhDto, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        boolean isSuccess = true;
        CpxqjhDto dto = new CpxqjhDto();
        dto.setXqdh(cpxqjhDto.getXqdh());
        List<CpxqjhDto> dtoList = cpxqjhService.getDtoList(dto);
        if (!CollectionUtils.isEmpty(dtoList)){
            isSuccess = false;
        }
        if(!isSuccess) {
            map.put("status", "fail");
            map.put("message", "需求单号已存在！");
            return map;
        }
        cpxqjhDto.setLrry(user.getYhid());
        cpxqjhDto.setCpxqid(StringUtil.generateUUID());
        try {
            isSuccess = cpxqjhService.progressAddSave(cpxqjhDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("ywid", StringUtil.isNotBlank(cpxqjhDto.getCpxqid())?cpxqjhDto.getCpxqid():"");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
        }
        return map;
    }

    /**
     * 修改
     */
    @RequestMapping("/progress/modProgress")
    public ModelAndView modProgress(CpxqjhDto cpxqjhDto) {
        ModelAndView mav = new  ModelAndView("production/progress/progress_edit");
        CpxqjhDto dtoById = cpxqjhService.getDtoById(cpxqjhDto.getCpxqid());
        dtoById.setFormAction("/progress/progress/modSaveProgress");
        mav.addObject("url", "/progress/progress/pagedataProgressMore");
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("cpxqjhDto", dtoById);
        //查询附件
        FjcfbDto fjcfbDto = new FjcfbDto();
        fjcfbDto.setYwlx(BusTypeEnum.IMP_DEMAND.getCode());
        fjcfbDto.setYwid(cpxqjhDto.getCpxqid());
        List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        mav.addObject("fjcfbDtos",fjcfbDtos);
        mav.addObject("ywlx",BusTypeEnum.IMP_DEMAND.getCode());
        mav.addObject("auditType", AuditTypeEnum.AUDIT_FG_PLAN.getCode());
        return mav;
    }
    /**
     * 修改
     */
    @RequestMapping("/progress/auditProgress")
    public ModelAndView auditProgress(CpxqjhDto cpxqjhDto) {
        ModelAndView mav = new  ModelAndView("production/progress/progress_edit");
        CpxqjhDto dtoById = cpxqjhService.getDtoById(cpxqjhDto.getCpxqid());
        dtoById.setFormAction("/progress/progress/modSaveProgress");
        mav.addObject("url", "/progress/progress/pagedataProgressMore");
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("cpxqjhDto", dtoById);
        //查询附件
        FjcfbDto fjcfbDto = new FjcfbDto();
        fjcfbDto.setYwlx(BusTypeEnum.IMP_DEMAND.getCode());
        fjcfbDto.setYwid(cpxqjhDto.getCpxqid());
        List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        mav.addObject("fjcfbDtos",fjcfbDtos);
        mav.addObject("ywlx",BusTypeEnum.IMP_DEMAND.getCode());
        mav.addObject("auditType", AuditTypeEnum.AUDIT_FG_PLAN.getCode());
        return mav;
    }

    /**
     * 修改保存
     */
    @RequestMapping("/progress/modSaveProgress")
    @ResponseBody
    public Map<String,Object> progressModSave(CpxqjhDto cpxqjhDto, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        boolean isSuccess = true;
        CpxqjhDto dto = new CpxqjhDto();
        dto.setXqdh(cpxqjhDto.getXqdh());
        List<CpxqjhDto> dtoList = cpxqjhService.getDtoList(dto);
        if (null != dtoList && dtoList.size()>1){
            isSuccess = false;
        }
        if(!isSuccess) {
            map.put("status", "fail");
            map.put("message", "需求单号已存在！");
            return map;
        }
        cpxqjhDto.setXgry(user.getYhid());
        try {
            isSuccess = cpxqjhService.progressModSave(cpxqjhDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("ywid", StringUtil.isNotBlank(cpxqjhDto.getCpxqid())?cpxqjhDto.getCpxqid():"");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
        }
        return map;
    }

    /**
     * 	审核列表
     */
    @RequestMapping("/progress/pageListDeviceAudit")
    public ModelAndView pageListDeviceAudit() {
        ModelAndView mav = new  ModelAndView("production/progress/progress_auditList");
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 	审核列表
     */
    @RequestMapping("/progress/pageGetListDeviceAudit")
    @ResponseBody
    public Map<String, Object> getListProgressAudit(CpxqjhDto cpxqjhDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        // 附加委托参数
        DataPermission.addWtParam(cpxqjhDto);
        // 附加审核状态过滤
        if (GlobalString.AUDIT_SHZT_YSH.equals(cpxqjhDto.getDqshzt())) {
            DataPermission.add(cpxqjhDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "cpxqjh", "cpxqid",
                    AuditTypeEnum.AUDIT_FG_PLAN);
        } else {
            DataPermission.add(cpxqjhDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "cpxqjh", "cpxqid",
                    AuditTypeEnum.AUDIT_FG_PLAN);
        }
        DataPermission.addCurrentUser(cpxqjhDto, getLoginInfo(request));
        List<CpxqjhDto> listMap = cpxqjhService.getPagedAuditDevice(cpxqjhDto);
        map.put("total", cpxqjhDto.getTotalNumber());
        map.put("rows", listMap);
        return map;
    }

    /**
     * 删除
     */
    @RequestMapping("/progress/delProgress")
    @ResponseBody
    public Map<String,Object> delProgress(CpxqjhDto cpxqjhDto, HttpServletRequest request) {
        Map<String,Object> map = new HashMap<>();
        try {
            // 获取用户信息
            User user = getLoginInfo(request);
            cpxqjhDto.setScry(user.getYhid());
            cpxqjhDto.setScbj("1");
            boolean isSuccess = cpxqjhService.deleteByIds(cpxqjhDto);
            map.put("status", isSuccess?"success":"fail");
            map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00004").getXxnr());
        }
        return map;
    }

    /**
     * 	生产指令审核列表
     */
    @RequestMapping("/produce/pageListProduceAudit")
    public ModelAndView pageListProduceAudit() {
        ModelAndView mav = new  ModelAndView("production/produce/produce_auditList");
        List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.PRODUCTS_TYPE.getCode());
        mav.addObject("lblist", jcsjDtos);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 获取生产指令审核列表
     */
    @RequestMapping("/produce/pageGetListProduceAudit")
    @ResponseBody
    public Map<String, Object> getPagedListProduceAudit(SczlglDto sczlglDto, HttpServletRequest request) {
        DataPermission.addWtParam(sczlglDto);
        List<AuditTypeEnum> auditTypes = new ArrayList<>();
        List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.AUDIT_TYPE.getCode());
        for (JcsjDto jcsjDto : jcsjDtos) {
            auditTypes.add(AuditTypeEnum.getByCode(jcsjDto.getCskz1()));
        }
        if(GlobalString.AUDIT_SHZT_YSH.equals(sczlglDto.getDqshzt())){
            DataPermission.add(sczlglDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "sczlmx", "sczlmxid",auditTypes);
        }else{
            DataPermission.add(sczlglDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "sczlmx", "sczlmxid",auditTypes);
        }
        DataPermission.addCurrentUser(sczlglDto, getLoginInfo(request));
        List<SczlglDto> sczlglDtos = sczlglService.getPagedAuditDevice(sczlglDto);
        Map<String, Object> map = new HashMap<>();
        map.put("total", sczlglDto.getTotalNumber());
        map.put("rows", sczlglDtos);
        return map;
    }

    /**
     * 根据输入信息查询物料
     */
    @RequestMapping("/progress/pagedataQueryWlxx")
    @ResponseBody
    public Map<String,Object> queryWlxx(WlglDto wlglDto){
        List<WlglDto> dtoList = wlglService.getDtoList(wlglDto);
        Map<String,Object> map = new HashMap<>();
        map.put("dtoList", dtoList);
        return map;
    }
    /**
     * 根据输入信息查询物料
     */
    @RequestMapping("/progress/pagedataQueryWlxxById")
    @ResponseBody
    public Map<String,Object> pagedataQueryWlxxById(WlglDto wlglDto){
        List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.SUPPLY_TYPE.getCode());
        List<WlglDto> dtoList = wlglService.getDtoListByWlid(wlglDto);
        if (!CollectionUtils.isEmpty(jcsjDtos)&&!CollectionUtils.isEmpty(dtoList)){
            for (JcsjDto jcsjDto : jcsjDtos) {
                if ("1".equals(jcsjDto.getSfmr())){
                    dtoList.get(0).setGylx(jcsjDto.getCsid());
                }
            }
        }
        Map<String,Object> map = new HashMap<>();
        map.put("dtoList", dtoList);
        return map;
    }

    /**
     * 批量提交保存
     */
    @RequestMapping("/produce/pagedataProduceSubmitSave")
    @ResponseBody
    public Map<String,Object> produceSubmitSave(SczlglDto sczlglDto){
        Map<String,Object> map = new HashMap<>();
        try {
            sczlglService.produceModAudit(sczlglDto);
            List<String> ywids = new ArrayList<>(Arrays.asList(sczlglDto.getYwids().split(",")));
            map.put("ywids", ywids);
            map.put("status", "success");
            map.put("message", xxglService.getModelById("ICOM00001").getXxnr());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
        }
        return map;
    }
    /**
     * 生产进度统计
     */
    @RequestMapping(value = "/produce/getProducePlanStatis")
    @ResponseBody
    public List<Map<String, Object>> getProducePlanStatis(SczlmxDto sczlmxDto) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<SczlmxDto> sczlmxDtos = sczlmxService.getProducePlanStatis(sczlmxDto);
        int zs = 0;
        for (SczlmxDto dto : sczlmxDtos) {
            zs = zs + Integer.parseInt(dto.getCount());
        }
        BigDecimal bigDecimal = new BigDecimal(zs);
        for (SczlmxDto dto : sczlmxDtos) {
            Map<String, Object> map = new HashMap<>();
            map.put("lx",dto.getLx());
            if(zs==0) {
            	map.put("count",0);
            }else {
            	map.put("count",new BigDecimal(dto.getCount()).divide(bigDecimal,4,RoundingMode.HALF_UP));
            }
            list.add(map);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("lx","总数");
        map.put("count",zs);
        list.add(map);
        return list;
    }
    /**
     * 钉钉生产进度统计
     */
    @RequestMapping(value = "/produce/minidataGetProducePlanStatis")
    @ResponseBody
    public List<Map<String, Object>> minidataGetProducePlanStatis(SczlmxDto sczlmxDto) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<SczlmxDto> sczlmxDtos = sczlmxService.getProducePlanStatis(sczlmxDto);
        int zs = 0;
        for (SczlmxDto dto : sczlmxDtos) {
            zs = zs + Integer.parseInt(dto.getCount());
        }
        BigDecimal bigDecimal = new BigDecimal(zs);
        for (SczlmxDto dto : sczlmxDtos) {
            Map<String, Object> map = new HashMap<>();
            map.put("lx",dto.getLx());
            if(zs==0) {
                map.put("count",0);
            }else {
                map.put("count",new BigDecimal(dto.getCount()).divide(bigDecimal,4, RoundingMode.HALF_UP));
            }
            list.add(map);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("lx","总数");
        map.put("count",zs);
        list.add(map);
        return list;
    }
    /**
     * 生产进度合格统计
     */
    @RequestMapping(value = "/produce/getProduceEligibleStatis")
    @ResponseBody
    public List<SczlmxDto> getProduceEligibleStatis(SczlmxDto sczlmxDto) {
        List<SczlmxDto> sczlmxDtos;
        String zqStr = sczlmxDto.getZq();
        if (StringUtil.isNotBlank(zqStr)){
            int zq = Integer.parseInt(zqStr);
            if(StringUtil.isBlank(sczlmxDto.getMethod())) {
                setDateByWeek(sczlmxDto);
                sczlmxDto.setTj("week");
            }else if("PesYear".equals(sczlmxDto.getMethod())) {
                setDateByYear(sczlmxDto,-zq);
                sczlmxDto.setTj("year");
            }else if ("PesMonth".equals(sczlmxDto.getMethod())){
                setDateByMonth(sczlmxDto,-zq);
                sczlmxDto.setTj("mon");
            }else if ("PesWeek".equals(sczlmxDto.getMethod())){
                setDateByWeek(sczlmxDto);
                sczlmxDto.setTj("week");
            }else if ("PesDay".equals(sczlmxDto.getMethod())){
                setDateByDay(sczlmxDto,-zq);
                sczlmxDto.setTj("day");
            }
        }else {
            sczlmxDto.setZq("7");
            setDateByWeek(sczlmxDto);
            sczlmxDto.setTj("week");
        }
        List<String> rqs = sczlmxService.getRqsByStartAndEnd(sczlmxDto);
        sczlmxDto.setRqs(rqs);
        sczlmxDtos = sczlmxService.getProduceEligibles(sczlmxDto);
        return sczlmxDtos;
    }
    /**
     * 钉钉生产进度合格统计
     */
    @RequestMapping(value = "/produce/minidataGetProduceEligibleStatis")
    @ResponseBody
    public List<SczlmxDto> minidataGetProduceEligibleStatis(SczlmxDto sczlmxDto) {
        List<SczlmxDto> sczlmxDtos;
        String zqStr = sczlmxDto.getZq();
        if (StringUtil.isNotBlank(zqStr)){
            int zq = Integer.parseInt(zqStr);
            if(StringUtil.isBlank(sczlmxDto.getMethod())) {
                setDateByWeek(sczlmxDto);
                sczlmxDto.setTj("week");
            }else if("PesYear".equals(sczlmxDto.getMethod())) {
                setDateByYear(sczlmxDto,-zq);
                sczlmxDto.setTj("year");
            }else if ("PesMonth".equals(sczlmxDto.getMethod())){
                setDateByMonth(sczlmxDto,-zq);
                sczlmxDto.setTj("mon");
            }else if ("PesWeek".equals(sczlmxDto.getMethod())){
                setDateByWeek(sczlmxDto);
                sczlmxDto.setTj("week");
            }else if ("PesDay".equals(sczlmxDto.getMethod())){
                setDateByDay(sczlmxDto,-zq);
                sczlmxDto.setTj("day");
            }
        }else {
            sczlmxDto.setZq("7");
            setDateByWeek(sczlmxDto);
            sczlmxDto.setTj("week");
        }
        List<String> rqs = sczlmxService.getRqsByStartAndEnd(sczlmxDto);
        sczlmxDto.setRqs(rqs);
        sczlmxDtos = sczlmxService.getProduceEligibles(sczlmxDto);
        return sczlmxDtos;
    }
    /**
     * 设置按年查询的日期
     * 长度。要为负数，代表向前推移几年
     */
    private void setDateByYear(SczlmxDto sczlmxDto,int length) {
        SimpleDateFormat monthSdf=new SimpleDateFormat("yyyy");
        Calendar calendar=Calendar.getInstance();
        sczlmxDto.setLrsjYend(monthSdf.format(calendar.getTime()));
        calendar.add(Calendar.YEAR, length+1);
        sczlmxDto.setLrsjYstart(monthSdf.format(calendar.getTime()));
    }

    /**
     * 设置按月查询的日期
     * 长度。要为负数，代表向前推移几月
     */
    private void setDateByMonth(SczlmxDto sczlmxDto,int length) {
        SimpleDateFormat monthSdf=new SimpleDateFormat("yyyy-MM");
        Calendar calendar=Calendar.getInstance();
        sczlmxDto.setLrsjMend(monthSdf.format(calendar.getTime()));
        calendar.add(Calendar.MONTH,length+1);
        sczlmxDto.setLrsjMstart(monthSdf.format(calendar.getTime()));
    }

    /**
     * 设置周的日期
     */
    private void setDateByWeek(SczlmxDto sczlmxDto) {
        SimpleDateFormat daySdf=new SimpleDateFormat("yyyy-MM-dd");
        int dayOfWeek = DateUtils.getWeek(new Date());
        if (dayOfWeek <= 2)
        {
            sczlmxDto.setLrsjend(daySdf.format(DateUtils.getDate(new Date(), -dayOfWeek-1)));
        } else
        {
            sczlmxDto.setLrsjend(daySdf.format(DateUtils.getDate(new Date(), 6 - dayOfWeek)));
        }
    }

    /**
     * 设置按天查询的日期
     * 长度。要为负数，代表向前推移几天
     */
    private void setDateByDay(SczlmxDto sczlmxDto,int length) {
        SimpleDateFormat daySdf=new SimpleDateFormat("yyyy-MM-dd");
            sczlmxDto.setLrsjend(daySdf.format(new Date()));
            sczlmxDto.setLrsjstart(daySdf.format(DateUtils.getDate(new Date(), length+1)));
    }

    /**
     * 生产进度审核信息统计
     */
    @RequestMapping(value = "/produce/minidataProduceShxxStatis")
    @ResponseBody
    public Map<String,Object> minidataProduceShxxStatis(SczlglDto sczlglDto) {
        Map<String, Object> map = new HashMap<>();
        List<SczlglDto> sczlglDtos = sczlglService.getPagedDtoList(sczlglDto);
        List<String> tgShxxs = new ArrayList<>();
        List<String> tjShxxs = new ArrayList<>();
        for (SczlglDto dto : sczlglDtos) {
            if (StatusEnum.CHECK_PASS.getCode().equals(dto.getZt())){
                tgShxxs.add(dto.getSczlmxid());
            }else if (StatusEnum.CHECK_SUBMIT.getCode().equals(dto.getZt())){
                tjShxxs.add(dto.getSczlmxid());
            }
        }
        if (!CollectionUtils.isEmpty(tjShxxs)){
            ShxxDto shxxDto = new ShxxDto();
            shxxDto.setYwids(tjShxxs);
            List<ShxxDto> shxxDtos = shxxService.getShxxByYwidsTj(shxxDto);
            extracted(sczlglDtos, shxxDtos);
            getShys(shxxDtos);
        }
        if (!CollectionUtils.isEmpty(tgShxxs)){
            ShxxDto shxxDto = new ShxxDto();
            shxxDto.setYwids(tgShxxs);
            List<ShxxDto> shxxDtos = shxxService.getShxxByYwidsTG(shxxDto);
            extracted(sczlglDtos, shxxDtos);
            getShys(shxxDtos);

        }
        List<JcsjDto> cplxs = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.PRODUCTS_TYPE.getCode());
        sczlglDto.setFlag("0");
        int wwcts = sczlglService.getCount(sczlglDto);
        sczlglDto.setFlag("1");
        int ywcts = sczlglService.getCount(sczlglDto);
        map.put("wwcts",wwcts);
        map.put("ywcts",ywcts);
        map.put("qbts",wwcts+ywcts);
        map.put("cplxs",cplxs);
        map.put("sczlglDtos",sczlglDtos);
        return map;
    }
    /**
     *  获取审核用时
     */
    private void getShys(List<ShxxDto> shxxDtos){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            for (ShxxDto shxxDto : shxxDtos) {
                if (StringUtil.isNotBlank(shxxDto.getShsj())) {
                    Date now = format.parse(shxxDto.getShsj());
                    long nowTime = now.getTime();
                    if ("1".equals(shxxDto.getLcxh())) {
                        Date sq = format.parse(shxxDto.getSqsj());
                        long time = sq.getTime();
                        String shys = new BigDecimal(nowTime).subtract(new BigDecimal(time)).divide(new BigDecimal(1000 * 60 * 60), 1, RoundingMode.HALF_UP).setScale(1, RoundingMode.HALF_UP).toString();
                        shxxDto.setShys(shys);
                    }
                    for (ShxxDto dto : shxxDtos) {
                        if (StringUtil.isNotBlank(dto.getShsj())){
                        if (!"1".equals(shxxDto.getLcxh()) && shxxDto.getYwid().equals(dto.getYwid()) && Integer.parseInt(shxxDto.getLcxh()) - 1 == Integer.parseInt(dto.getLcxh())) {
                            Date parse = format.parse(dto.getShsj());
                            long time = parse.getTime();
                            String shys = new BigDecimal(nowTime).subtract(new BigDecimal(time)).divide(new BigDecimal(1000 * 60 * 60), 1, RoundingMode.HALF_UP).setScale(1, RoundingMode.HALF_UP).toString();
                            shxxDto.setShys(shys);
                        }
                    }
                    }
                }
            }
        } catch (ParseException e) {
           log.error(e.getMessage());
        }
    }
    /**
     * 给各个生产阶段赋值
     */
    private void extracted(List<SczlglDto> sczlglDtos, List<ShxxDto> shxxDtos) {
        for (SczlglDto dto : sczlglDtos) {
            for (ShxxDto shxxDto_t : shxxDtos) {
                if (dto.getSczlmxid().equals(shxxDto_t.getYwid())){
                    if (CollectionUtils.isEmpty(dto.getShxxDtos())){
                        List<ShxxDto> list = new ArrayList<>();
                        list.add(shxxDto_t);
                        dto.setShxxDtos(list);
                    }else {
                        dto.getShxxDtos().add(shxxDto_t);
                    }
                }
            }
            if (!"1".equals(dto.getTjbj())){
                ShxxDto shxxDto_t=new ShxxDto();
                shxxDto_t.setGwmc("生产操作完成");
                if ("80".equals(dto.getZt())){
                    if (!CollectionUtils.isEmpty(dto.getShxxDtos())){
                        shxxDto_t.setLcxh(String.valueOf(dto.getShxxDtos().size()+1));
                        shxxDto_t.setXlcxh(String.valueOf(dto.getShxxDtos().size()+1));
                        shxxDto_t.setShsj(dto.getShxxDtos().get(dto.getShxxDtos().size()-1).getShsj());
                        shxxDto_t.setYwid(dto.getShxxDtos().get(0).getYwid());
                        for (ShxxDto shxxDto1 : dto.getShxxDtos()) {
                            shxxDto1.setXlcxh(String.valueOf(dto.getShxxDtos().size()+1));
                        }
                    }
                }else {
                    if (!CollectionUtils.isEmpty(dto.getShxxDtos())){
                        shxxDto_t.setLcxh(String.valueOf(dto.getShxxDtos().size()+1));
                        shxxDto_t.setXlcxh(String.valueOf(dto.getShxxDtos().get(0).getXlcxh()));
                    }

                }
                if (!CollectionUtils.isEmpty(dto.getShxxDtos())){
                    dto.getShxxDtos().add(shxxDto_t);
                }
                ShxxDto shxxDto=new ShxxDto();
                shxxDto.setGwmc("质检");
                if (StringUtil.isNotBlank(dto.getDhjyid())){
                    if (!CollectionUtils.isEmpty(dto.getShxxDtos())){
                        shxxDto.setLcxh(String.valueOf(dto.getShxxDtos().size()+1));
                        shxxDto.setXlcxh(String.valueOf(dto.getShxxDtos().size()+1));
                        shxxDto.setYwid(dto.getShxxDtos().get(0).getYwid());
                        for (ShxxDto shxxDto1 : dto.getShxxDtos()) {
                            shxxDto1.setXlcxh(String.valueOf(dto.getShxxDtos().size()+1));
                        }
                    }
                }else {
                    if (!CollectionUtils.isEmpty(dto.getShxxDtos())){
                        shxxDto.setLcxh(String.valueOf(dto.getShxxDtos().size()+1));
                        shxxDto.setXlcxh(String.valueOf(dto.getShxxDtos().get(0).getXlcxh()));
                    }
                }
                if (!CollectionUtils.isEmpty(dto.getShxxDtos())){
                    dto.getShxxDtos().add(shxxDto);
                }
                ShxxDto shxxDto2=new ShxxDto();
                shxxDto2.setGwmc("入库");
                if (StringUtil.isNotBlank(dto.getRkzt())){
                    if (!CollectionUtils.isEmpty(dto.getShxxDtos())) {
                        shxxDto2.setLcxh(String.valueOf(dto.getShxxDtos().size()+1));
                        shxxDto2.setXlcxh(String.valueOf(dto.getShxxDtos().size()+1));
                        shxxDto2.setYwid(dto.getShxxDtos().get(0).getYwid());
                        for (ShxxDto shxxDto1 : dto.getShxxDtos()) {
                            shxxDto1.setXlcxh(String.valueOf(dto.getShxxDtos().size()+1));
                        }
                    }
                }else {
                    if (!CollectionUtils.isEmpty(dto.getShxxDtos())){
                        shxxDto2.setLcxh(String.valueOf(dto.getShxxDtos().size()+1));
                        shxxDto2.setXlcxh(String.valueOf(dto.getShxxDtos().get(0).getXlcxh()));
                    }
                }
                if (!CollectionUtils.isEmpty(dto.getShxxDtos())){
                    dto.getShxxDtos().add(shxxDto2);
                }
                if (!CollectionUtils.isEmpty(dto.getShxxDtos())){
                    dto.setTjbj("1");
                }
            }
        }
    }

    /**
     * 钉钉查看按钮
     */
    @RequestMapping("/produce/minidataViewProduce")
    @ResponseBody
    public Map<String,Object> minidataViewProduce(SczlglDto sczlglDto) {
        Map<String, Object> map = new HashMap<>();
        SczlglDto dto = sczlglService.getDto(sczlglDto);
        XqjhmxDto xqjhmxDto=xqjhmxService.getDtoById(dto.getXqjhmxid());
        if (null!=xqjhmxDto){
            BomglDto bomglDto=scbomService.selectDtoByMjwlid(xqjhmxDto.getWlid());
            if (null!=bomglDto){
                BommxDto bommxDto=new BommxDto();
                bommxDto.setBomid(bomglDto.getBomid());
                List<BommxDto> bommxDtos=scbommxService.getDtoList(bommxDto);
                if (!CollectionUtils.isEmpty(bommxDtos)){
                    for (BommxDto value : bommxDtos) {
                        BigDecimal bzyl = new BigDecimal(value.getBzyl());
                        BigDecimal sl = new BigDecimal(xqjhmxDto.getSl());
                        BigDecimal yjzyl = bzyl.multiply(sl);
                        value.setYjzyl(String.valueOf(yjzyl));
                        if (Double.parseDouble(value.getBzyl()) < Double.parseDouble(xqjhmxDto.getKlsl())) {
                            value.setYjcml("0");
                        } else {
                            BigDecimal kcl = new BigDecimal(xqjhmxDto.getKlsl());
                            value.setYjcml(String.valueOf(yjzyl.subtract(kcl)));
                        }
                    }
                }
                map.put("bommxDtos",bommxDtos);
                CpxqjhDto cpxqjhDto = cpxqjhService.getDtoById(xqjhmxDto.getXqjhid());
                map.put("cpxqjhDto",cpxqjhDto);
                XqjhmxDto xqjhmxDto1=new XqjhmxDto();
                xqjhmxDto1.setXqjhid(cpxqjhDto.getCpxqid());
                List<XqjhmxDto> xqjhmxDtoList=xqjhmxService.getDtoList(xqjhmxDto1);
                map.put("xqjhmxDtoList",xqjhmxDtoList);
            }
        }else {
            map.put("bommxDtos",null);
            map.put("cpxqjhDto",null);
        }
        map.put("dto",dto);
        map.put("urlPrefix", urlPrefix);
        return map;
    }
	/**
     * 	待生产查看货物信息
     */
    @RequestMapping("/progress/pagedataHwxx")
    public ModelAndView pagedataHwxx(CpxqjhDto cpxqjhDto) {
        ModelAndView mav = new  ModelAndView("production/progress/progressWait_hwxx");
        List<CpxqjhDto> cpxqjhDtoList=cpxqjhService.getHwxxByWlid(cpxqjhDto);
        mav.addObject("cpxqjhDtoList",cpxqjhDtoList);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 生产BOM列表
     */
    @RequestMapping(value = "/produce/pageListProduceBom")
    public ModelAndView pageListProduceBom() {
        ModelAndView mav = new ModelAndView("production/produceBom/produceBom_list");
        List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.SCBOM_TYPE.getCode());
        mav.addObject("bomlblist", jcsjDtos);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 生产BOM列表数据
     */
    @RequestMapping(value = "/produce/pageGetListProduceBom")
    @ResponseBody
    public Map<String, Object> pageGetListProduceBom(BomglDto bomglDto) {
        Map<String, Object> map = new HashMap<>();
        List<BomglDto> pagedDtoList = scbomService.getPagedDtoList(bomglDto);
        map.put("total", bomglDto.getTotalNumber());
        map.put("rows",pagedDtoList);
        return map;
    }
    /**
     * 生产BOM列表数据
     */
    @RequestMapping(value = "/produce/pagedataGetBomDetails")
    @ResponseBody
    public Map<String, Object> pagedataGetBomDetails(BommxDto bommxDto) {
        Map<String, Object> map = new HashMap<>();
        if(StringUtils.isNotBlank(bommxDto.getBomid())) {
            List<BommxDto> dtoList = scbommxService.getDtoList(bommxDto);
            map.put("rows", dtoList);
        }else {
            map.put("rows", null);
        }
        return map;
    }
    /**
     * 生产BOM是否重复
     */
    @RequestMapping(value = "/produce/pagedataGetBomByWl")
    @ResponseBody
    public Map<String, Object> pagedataGetBomByWl(BomglDto bomglDto) {
        Map<String, Object> map = new HashMap<>();
        boolean success = false;
        List<BomglDto> dtoList = scbomService.getDtoList(bomglDto);
        if (CollectionUtils.isEmpty(dtoList)){
            success = true;
        }
        map.put("status", success);
        return map;
    }
    /**
     * 新增生产BOM
     */
    @RequestMapping(value = "/produce/addProduceBom")
    public ModelAndView addProduceBom(BomglDto bomglDto) {
        ModelAndView mav = new ModelAndView("production/produceBom/produceBom_edit");
        List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.SCBOM_TYPE.getCode());
        bomglDto.setFormAction("addSaveProduceBom");
        mav.addObject("bomglDto", bomglDto);
        mav.addObject("jcsjDtos", jcsjDtos);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 新增保存BOM信息
     */
    @RequestMapping("/produce/addSaveProduceBom")
    @ResponseBody
    public Map<String,Object> addSaveProduceBom(BomglDto bomglDto, HttpServletRequest request){
        User user=getLoginInfo(request);
        bomglDto.setLrry(user.getYhid());
        Map<String,Object> map=new HashMap<>();
        boolean isSuccess;
        //保存到货信息
        try {
            isSuccess=scbomService.addSaveProduceBom(bomglDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
            map.put("urlPrefix",urlPrefix);
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
            map.put("urlPrefix",urlPrefix);
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
            map.put("urlPrefix",urlPrefix);
        }
        return map;
    }
    /**
     * 修改生产BOM
     */
    @RequestMapping(value = "/produce/modProduceBom")
    public ModelAndView modProduceBom(BomglDto bomglDto) {
        ModelAndView mav = new ModelAndView("production/produceBom/produceBom_edit");
        List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.SCBOM_TYPE.getCode());
        bomglDto = scbomService.getDtoById(bomglDto.getBomid());
        bomglDto.setFormAction("modSaveProduceBom");
        mav.addObject("bomglDto", bomglDto);
        mav.addObject("jcsjDtos", jcsjDtos);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 修改保存BOM信息
     */
    @RequestMapping("/produce/modSaveProduceBom")
    @ResponseBody
    public Map<String,Object> modSaveProduceBom(BomglDto bomglDto, HttpServletRequest request){
        User user=getLoginInfo(request);
        bomglDto.setXgry(user.getYhid());
        Map<String,Object> map=new HashMap<>();
        boolean isSuccess;
        try {
            isSuccess=scbomService.modSaveProduceBom(bomglDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
            map.put("urlPrefix",urlPrefix);
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
            map.put("urlPrefix",urlPrefix);
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
            map.put("urlPrefix",urlPrefix);
        }
        return map;
    }
    /**
     * 查看生产BOM
     */
    @RequestMapping(value = "/produce/viewProduceBom")
    public ModelAndView viewProduceBom(BomglDto bomglDto) {
        ModelAndView mav = new ModelAndView("production/produceBom/produceBom_view");
        bomglDto = scbomService.getDtoById(bomglDto.getBomid());
        BommxDto bommxDto = new BommxDto();
        bommxDto.setBomid(bomglDto.getBomid());
        List<BommxDto> bommxDtos = scbommxService.getDtoList(bommxDto);
        mav.addObject("bomglDto", bomglDto);
        mav.addObject("bommxDtos", bommxDtos);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     *  删除BOM信息
     */
    @RequestMapping("/produce/delProduceBom")
    @ResponseBody
    public Map<String,Object> delProduceBom(BomglDto bomglDto, HttpServletRequest request){
        User user=getLoginInfo(request);
        bomglDto.setScry(user.getYhid());
        Map<String,Object> map=new HashMap<>();
        boolean isSuccess;
        try {
            isSuccess= scbomService.delProduceBom(bomglDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00003").getXxnr() : xxglService.getModelById("ICOM00004").getXxnr());
            map.put("urlPrefix",urlPrefix);
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00004").getXxnr());
            map.put("urlPrefix",urlPrefix);
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("message", xxglService.getModelById("ICOM00004").getXxnr());
            map.put("urlPrefix",urlPrefix);
        }
        return map;
    }
	/**
     * BOM导入
     */
    @RequestMapping("/produce/pageImportProduceBom")
    public ModelAndView produceBomImport() {
        ModelAndView mav=new ModelAndView("production/produceBom/produceBom_import");
        FjcfbDto fjcfbDto = new FjcfbDto();
        fjcfbDto.setYwlx(BusTypeEnum.BOM_IMPORT.getCode());
        mav.addObject("fjcfbDto", fjcfbDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 生产新增页面引用
     */
    @RequestMapping("/progress/pagedataProgressMxList")
    public ModelAndView pagedataProgressMxList() {
        ModelAndView mav = new  ModelAndView("production/progress/progress_details");
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * bom明细表数据
     */
    @RequestMapping(value = "/progress/pagedataChooseListBommxInfo")
    public ModelAndView pagedataChooseListBommxInfo(BommxDto bommxDto) {
        ModelAndView mav = new ModelAndView("production/produceBom/produceBomMx_details");
        XqjhmxDto xqjhmxDto=xqjhmxService.getDtoById(bommxDto.getXqjhmxid());
        BomglDto bomglDto=scbomService.selectDtoByMjwlid(xqjhmxDto.getWlid());
        if (null!=bomglDto){
            bommxDto.setBomid(bomglDto.getBomid());
            List<BommxDto> bommxDtos=scbommxService.getDtoList(bommxDto);
            if (!CollectionUtils.isEmpty(bommxDtos)){
                for (BommxDto dto : bommxDtos) {
                    BigDecimal bzyl = new BigDecimal(dto.getBzyl());
                    BigDecimal sl = new BigDecimal(xqjhmxDto.getSl());
                    BigDecimal yjzyl = bzyl.multiply(sl);
                    dto.setYjzyl(String.valueOf(yjzyl));
                }
            }
            mav.addObject("dtoList",bommxDtos);
        }else {
            mav.addObject("dtoList",null);
        }
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("bommxDto", bommxDto);
        return mav;
    }
    /**
     *
     */
    @RequestMapping("/progress/pagedataListBommxInfo")
    @ResponseBody
    public Map<String,Object> pagedataListBommxInfo(BommxDto bommxDto){
        Map<String,Object> map=new HashMap<>();
        XqjhmxDto xqjhmxDto=xqjhmxService.getDtoById(bommxDto.getXqjhmxid());
        BomglDto bomglDto=scbomService.selectDtoByMjwlid(xqjhmxDto.getWlid());
        if(null!=bomglDto){
            bommxDto.setBomid(bomglDto.getBomid());
            List<BommxDto> bommxDtos=scbommxService.getDtoList(bommxDto);
            map.put("dtoList",bommxDtos);
        }
        else {
            map.put("dtoList",null);
        }
        map.put("urlPrefix",urlPrefix);
        return map;
    }
    /**
     */
    @RequestMapping("/progress/pagedataListBommxInfoByIds")
    @ResponseBody
    public Map<String,Object> pagedataListBommxInfoByIds(BommxDto bommxDto){
        Map<String,Object> map=new HashMap<>();
        List<BommxDto> bommxDtos=scbommxService.getDtoList(bommxDto);
        XqjhmxDto xqjhmxDto=new XqjhmxDto();
        xqjhmxDto.setIds(bommxDto.getXqjhmxids());
        List<XqjhmxDto> xqjhmxDtoList=xqjhmxService.getDtoList(xqjhmxDto);
        if (!CollectionUtils.isEmpty(bommxDtos)){
            for (BommxDto dto : bommxDtos) {
                for (int j = 0; j < bommxDto.getIds().size(); j++) {
                    if (dto.getBommxid().equals(bommxDto.getIds().get(j))) {
                        dto.setXqjhmxid(bommxDto.getXqjhmxids().get(j));
                        for (XqjhmxDto value : xqjhmxDtoList) {
                            if (dto.getXqjhmxid().equals(value.getXqjhmxid())) {
                                if (StringUtil.isNotBlank(dto.getBzyl()) && StringUtil.isNotBlank(value.getSl())) {
                                    BigDecimal bzyl = new BigDecimal(dto.getBzyl());
                                    BigDecimal sl = new BigDecimal(value.getSl());
                                    BigDecimal yjzyl = bzyl.multiply(sl);
                                    dto.setYjzyl(String.valueOf(yjzyl));
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
        map.put("dtoList",bommxDtos);
        map.put("urlPrefix",urlPrefix);
        return map;
    }
    /**
     * bom明细查看
     */
    @RequestMapping(value = "/progress/pagedataViewBomMx")
    public ModelAndView pagedataViewBomMx(BommxDto bommxDto) {
        ModelAndView mav = new ModelAndView("production/produceBom/produce_details");
        XqjhmxDto xqjhmxDto=xqjhmxService.getDtoById(bommxDto.getXqjhmxid());
        BommxDto bommxDto1=scbommxService.getDtoById(bommxDto.getBommxid());
        if (StringUtil.isNotBlank(xqjhmxDto.getSl())){
            bommxDto1.setXqsl(xqjhmxDto.getSl());
        }
        BigDecimal bzyl=new BigDecimal(bommxDto1.getBzyl());
        BigDecimal sl=new BigDecimal(xqjhmxDto.getSl());
        BigDecimal yjzyl=bzyl.multiply(sl);
        BigDecimal kcl=new BigDecimal(xqjhmxDto.getKlsl());
        if (Double.parseDouble(bommxDto1.getBzyl())<Double.parseDouble(xqjhmxDto.getKlsl())){
            bommxDto1.setYjcml("0");
        }else {
            bommxDto1.setYjcml(String.valueOf(yjzyl.subtract(kcl)));
        }
        bommxDto1.setYjzyl(String.valueOf(yjzyl));
        bommxDto1.setKcl(xqjhmxDto.getKlsl());
        mav.addObject("bommxDto", bommxDto1);
        return mav;
    }
    /**
     *
     */
    @RequestMapping("/progress/pagedataWaitProgress")
    @ResponseBody
    public Map<String,Object> pagedataWaitProgress(CpxqjhDto cpxqjhDto){
        return pageGetListWaitProgress(cpxqjhDto);
    }
    /**
     *  生产展板统计后台接口
     */
    @RequestMapping("/progress/minidataProgressBoard")
    @ResponseBody
    public Map<String,Object> minidataProgressBoard(HttpServletRequest request){
        Map<String,Object> map=new HashMap<>();
            String[] hjywlbms={"GS00149","GS00150","GS00151","GS00152","GS00153","GS00154","GS00155","GS00156","GS00137","GS00138","GS00139","GS00134","GS00135","GS00136","GS00133","GS00111","GS00101"
                    ,"GS00047","GS00046","GS00040"};
            map.put("hjynfs",request.getParameter("hjynfs"));
            map.put("xlnfs",request.getParameter("xlnfs"));
            map.put("threenfs",request.getParameter("threenfs"));
            map.put("hjyyfs",request.getParameter("hjyyfs"));
            map.put("xlyfs",request.getParameter("xlyfs"));
            map.put("threeyfs",request.getParameter("threeyfs"));
            map.put("hjywlbms",hjywlbms);
            String[] xlwlbms={"GS00215","GS00216","GS00217","GS00218","GS00219","GS00214","GS00201","GS00143","GS00140","GS00117","GS00102","GS00099","GS00068"};
            map.put("xlwlbms",xlwlbms);
            String[] threewlbms={"GS00204"};
            map.put("threewlbms",threewlbms);
        map =sczlglService.getHjyProgress(map);
        return map;
    }
    /**
     * 生产展板点击查看物料
     */
    @RequestMapping("/progress/minidataViewProgressBoard")
    @ResponseBody
    public Map<String,Object> minidataViewProgressBoard(HttpServletRequest request){
        Map<String,Object> map=new HashMap<>();
        Map<String,Object> map_t=new HashMap<>();
        if ("hjy".equals(request.getParameter("wlbm"))){
            String[] hjywlbms={"GS00149","GS00150","GS00151","GS00152","GS00153","GS00154","GS00155","GS00156","GS00137","GS00138","GS00139","GS00134","GS00135","GS00136","GS00133","GS00111","GS00101"
                    ,"GS00047","GS00046","GS00040"};
            map.put("wlbms",hjywlbms);
        }
        else if ("xl".equals(request.getParameter("wlbm"))){
            String[] xlwlbms={"GS00215","GS00216","GS00217","GS00218","GS00219","GS00214","GS00201","GS00143","GS00140","GS00117","GS00102","GS00099","GS00068"};
            map.put("wlbms",xlwlbms);
        }
        else if ("three".equals(request.getParameter("wlbm"))){
            String[] threewlbms={"GS00204"};
            map.put("wlbms",threewlbms);
        }
        if ("xq".equals(request.getParameter("lx"))){
            List<Map<String, Object>>  xqlist=sczlglService.selectDingTalkXq(map);
            map_t.put("xqlist",xqlist);
        }
        else if ("sc".equals(request.getParameter("lx"))){
            List<Map<String, Object>> sclist=sczlglService.selectDingTalkSc(map);
            map_t.put("sclist",sclist);
        }else if ("ck".equals(request.getParameter("lx"))){
            List<Map<String, Object>> cklist=sczlglService.selectDingTalkCk(map);
            map_t.put("cklist",cklist);
        }else if ("kc".equals(request.getParameter("lx"))){
            List<Map<String, Object>> kclist=sczlglService.selectDingTalkKc(map);
            map_t.put("kclist",kclist);
        }
        return map_t;
    }
    /**
     * 点击查看物料
     */
    @RequestMapping("/progress/minidataViewMateriel")
    @ResponseBody
    public Map<String,Object> minidataViewMateriel(WlglDto wlglDto) {
        Map<String, Object> map = new HashMap<>();
        wlglDto=wlglService.getDtoById(wlglDto.getWlid());
        StringBuilder scsplbmc= new StringBuilder();
        List<JcsjDto> sjxmhlist=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PRODUCTION_TYPE.getCode());//涉及项目号
        if(StringUtil.isNotBlank(wlglDto.getSscplb())) {
            wlglDto.setSscplbs(wlglDto.getSscplb().trim().split(","));
            for(int i=0;i<wlglDto.getSscplbs().length;i++) {
                if(!CollectionUtils.isEmpty(sjxmhlist)) {
                    for (JcsjDto jcsjDto : sjxmhlist) {
                        if (jcsjDto.getCsid().equals(wlglDto.getSscplbs()[i]))
                            scsplbmc.append(",").append(jcsjDto.getCsmc());
                    }
                }
            }
        }
        if (scsplbmc.length()>0){
            scsplbmc = new StringBuilder(scsplbmc.substring(1));
        }
        wlglDto.setSjxmhmc(scsplbmc.toString());
        map.put("wlglDto",wlglDto);
        return map;
    }
    /**
     * 入库状态维护
     */
    @RequestMapping("/progress/rkmaintenanceProgress")
    public ModelAndView rkmaintenanceProgress (CpxqjhDto cpxqjhDto) {
        ModelAndView mav = new  ModelAndView("production/progress/progress_rkmaintenance");
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("ids", cpxqjhDto.getIds());
        return mav;
    }
    /**入库状态维护保存
     *
     */
    @RequestMapping("/progress/rkmaintenanceSaveProgress")
    @ResponseBody
    public Map<String,Object> rkmaintenanceSaveProgress(CpxqjhDto cpxqjhDto, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        cpxqjhDto.setXgry(user.getYhid());
        boolean isSuccess=cpxqjhService.rkmaintenanceSaveProgress(cpxqjhDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }
    /**
     * 需求物料列表
     */
    @RequestMapping("/produce/pageListDemandMaterial")
    public ModelAndView pageListDemandMaterial() {
        ModelAndView mav = new  ModelAndView("production/produce/produce_demandMaterial");
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**需求物料获取数据
     *
     */
    @RequestMapping("/produce/pageGetListDemandMaterial")
    @ResponseBody
    public Map<String,Object> pageGetListDemandMaterial(XqjhmxDto xqjhmxDto){
        Map<String,Object> map = new HashMap<>();
        List<XqjhmxDto> xqjhmxDtoList=xqjhmxService.getPagedACMaterials(xqjhmxDto);
        map.put("rows",xqjhmxDtoList);
        map.put("total",xqjhmxDto.getTotalNumber());
        return map;
    }
    /**
     * 需求物料查看
     */
    @RequestMapping("/produce/viewDemandMaterial")
    public ModelAndView viewDemandMaterial(XqjhmxDto xqjhmxDto) {
        ModelAndView mav = new  ModelAndView("production/produce/produce_viewMaterial");
        xqjhmxDto=xqjhmxService.getACMaterialDto(xqjhmxDto);
        mav.addObject("xqjhmxDto",xqjhmxDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 打开打印生产交换单页面
     */
    @RequestMapping("/produce/printProduce")
    public ModelAndView printProduce(SczlmxDto sczlmxDto) {
        ModelAndView mav=new ModelAndView("production/produce/produce_print");
        List<SczlmxDto> sczlmxDtos = sczlmxService.getDtoListForPrint(sczlmxDto);
        mav.addObject("sczlmxDtos", sczlmxDtos);
        mav.addObject("urlPrefix", urlPrefix);
        //根据基础数据获取打印信息
        JcsjDto jcsj = new JcsjDto();
        jcsj.setCsdm("SC");
        jcsj.setJclb(BasicDataTypeEnum.DOCUMENTNUM.getCode());
        jcsj = jcsjService.getDto(jcsj);
        mav.addObject("wjbh", jcsj);
        return mav;
    }
    /**
     * 生产评审
     */
    @RequestMapping("/progress/reviewProgress")
    public ModelAndView samplesummaryPage(CpxqjhDto cpxqjhDto) {
        ModelAndView mav =new ModelAndView("production/progress/progress_review");
        cpxqjhDto = cpxqjhService.getDto(cpxqjhDto);
        FjcfbDto fjcfbDto=new FjcfbDto();
        fjcfbDto.setYwid(cpxqjhDto.getCpxqid());
        fjcfbDto.setYwlx(BusTypeEnum.IMP_PRODUCTION_REVIEW.getCode());
        List<FjcfbDto> fjcfbDtos = fjcfbService.getDtoList(fjcfbDto);
        mav.addObject("fjcfbDtos",fjcfbDtos);
        mav.addObject("ywlx", BusTypeEnum.IMP_PRODUCTION_REVIEW.getCode());
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("cpxqjhDto",cpxqjhDto);
        return mav;
    }
    /**
     * 更新留样库存留样小结
     */
    @RequestMapping("/progress/reviewSaveProgress")
    @ResponseBody
    public Map<String,Object> reviewSaveProgress(CpxqjhDto cpxqjhDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        cpxqjhDto.setXgry(user.getYhid());
        boolean isSuccess = cpxqjhService.updateScpsAndFj(cpxqjhDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }
    /**生产领料BOM明细
     */
    @RequestMapping("/produce/pagedataGetProduceReceiveBom")
    @ResponseBody
    public Map<String,Object> pagedataGetProduceReceiveBom(BommxDto bommxDto,HttpServletRequest request){
        User user = getLoginInfo(request);
        bommxDto.setJsid(user.getDqjs());
        Map<String,Object> map=new HashMap<>();
        //缺少库存
        List<Map<String,Object>> qsKcList = new ArrayList<>();
        List<BommxDto> resultDtos = new ArrayList<>();
        List<BommxDto> bommxDtos=scbommxService.getProduceReceiveBom(bommxDto);
        List<String> allWlids = bommxDtos.stream().map(BommxDto::getWlid).distinct().collect(Collectors.toList());
        BigDecimal zeroBec = new BigDecimal(0);
        for (String wlid : allWlids) {
            String bzyl = "0";
            for (BommxDto dto : bommxDtos) {
                if (wlid.equals(dto.getWlid())){
                    bzyl = dto.getBzyl();
                    break;
                }
            }
            BigDecimal klslAll = new BigDecimal(0);
            BigDecimal qlsl = new BigDecimal(bzyl).multiply(new BigDecimal(bommxDto.getScsl()));
            for (BommxDto dto : bommxDtos) {
                if (wlid.equals(dto.getWlid())&&qlsl.compareTo(zeroBec)!=0){
                    BigDecimal klslDec = new BigDecimal(StringUtil.isNotBlank(dto.getKlsl())?dto.getKlsl():"0");
                    if (qlsl.compareTo(klslDec)>0){
                        qlsl = qlsl.subtract(klslDec);
                        dto.setQlsl(String.valueOf(klslDec));
                    }else {
                        dto.setQlsl(String.valueOf(qlsl));
                        qlsl = zeroBec;
                    }
                    klslAll = klslAll.add(klslDec);
                    resultDtos.add(dto);
                }
            }
            if (qlsl.compareTo(zeroBec)>0){
                BommxDto bommxDto_last = resultDtos.get(resultDtos.size() - 1);
                BigDecimal xlsl = new BigDecimal(bzyl).multiply(new BigDecimal(bommxDto.getScsl()));
                Map<String, Object> qskcMap = new HashMap<>();
                qskcMap.put("wlbm",bommxDto_last.getWlbm());
                qskcMap.put("wlmc",bommxDto_last.getWlmc());
                qskcMap.put("scsl",bommxDto.getScsl());
                qskcMap.put("bzyl",bzyl);
                qskcMap.put("xlsl",xlsl);
                qskcMap.put("klsl",String.valueOf(klslAll));
                qskcMap.put("qssl",String.valueOf(xlsl.subtract(klslAll)));
                qsKcList.add(qskcMap);
            }
        }
        map.put("bommxDtos",resultDtos);
        map.put("qsKcList",qsKcList);
        return map;
    }

    /**
     * @Description: 个人物料设置
     * @param
     * @return org.springframework.web.servlet.ModelAndView
     * @Author: 郭祥杰
     * @Date: 2024/7/23 16:41
     */
    @RequestMapping("/progress/setmaterielProgress")
    public ModelAndView setmaterielProgress() {
        ModelAndView mav =new ModelAndView("production/progress/progress_setmateriel");
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * @Description: 获取个人物料设置
     * @param request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2024/7/23 16:41
     */
    @RequestMapping(value = "/produce/pagedataGetSetmateriel")
    @ResponseBody
    public Map<String, Object> pagedataGetSetmateriel(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        WlglDto wlglDto = new WlglDto();
        wlglDto.setYhid(user.getYhid());
        List<WlglDto> wlglDtoList = wlglService.queryByGrsz(wlglDto);
        map.put("rows",wlglDtoList);
        return map;
    }

    /**
     * @Description: 设置个人物料
     * @param grszDto
     * @param request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2024/7/25 9:57
     */
    @RequestMapping("/produce/pagedataSetGrsz")
    @ResponseBody
    public Map<String,Object> pagedataSetGrsz(GrszDto grszDto, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        grszDto.setYhid(user.getYhid());
        boolean isSuccess=grszService.saveGrsz(grszDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * @Description: 删除个人设置
     * @param grszDto
     * @param request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2024/7/25 9:59
     */
    @RequestMapping("/produce/pagedataDelGrsz")
    @ResponseBody
    public Map<String,Object> pagedataDelGrsz(GrszDto grszDto, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        grszDto.setScry(user.getYhid());
        boolean isSuccess=grszService.delGrsz(grszDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00003").getXxnr() : xxglService.getModelById("ICOM00004").getXxnr());
        return map;
    }

}
