package com.matridx.igams.production.controller;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.post.ICommonDao;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.production.dao.entities.CljlxxDto;
import com.matridx.igams.production.dao.entities.PdjlDto;
import com.matridx.igams.production.dao.entities.ShfkdjDto;
import com.matridx.igams.production.service.svcinterface.ICljlxxService;
import com.matridx.igams.production.service.svcinterface.IPdjlService;
import com.matridx.igams.production.service.svcinterface.IShfkdjService;
import com.matridx.igams.storehouse.dao.entities.LlglDto;
import com.matridx.igams.storehouse.service.svcinterface.ILlglService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author:JYK
 */
@Controller
@RequestMapping("/saleFeedback")
public class SaleFeedbackController extends BaseController {
    @Autowired
    private IShfkdjService shfkdjService;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Autowired
    ICljlxxService cljlxxService;
    @Autowired
    IXxglService xxglService;
    @Autowired
    ILlglService llglService;
    @Override
    public String getPrefix() {
        return urlPrefix;
    }
    @Autowired
    ICommonDao commonDao;
    @Autowired
    IPdjlService pdjlService;
    @Autowired
    RedisUtil redisUtil;
    /**
     * 新增
     */
    @RequestMapping(value = "/saleFeedback/addSaleFeedback")
    public ModelAndView addSaleFeedback(ShfkdjDto shfkdjDto) {
        ModelAndView mav = new ModelAndView("production/feedback/saleFeedBack_edit");
        List<JcsjDto> lxList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.SALEFEEDBACK_TYPE.getCode());
        shfkdjDto.setFormAction("addSaveSaleFeedback");
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("shfkdjDto", shfkdjDto);
        mav.addObject("lxList", lxList);
        return mav;
    }

    /**
     * 新增保存
     */
    @RequestMapping(value = "/saleFeedback/addSaveSaleFeedback")
    @ResponseBody
    public Map<String,Object> addSaveSaleFeedback(ShfkdjDto shfkdjDto, HttpServletRequest request) {
        User user = getLoginInfo(request);
        shfkdjDto.setLrry(user.getYhid());
        Map<String,Object> map=new HashMap<>();
        boolean isSuccess = shfkdjService.addSaveSaleFeedback(shfkdjDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }


    /**
     * 修改
     */
    @RequestMapping(value = "/saleFeedback/modSaleFeedback")
    public ModelAndView modSaleFeedback(ShfkdjDto shfkdjDto) {
        ModelAndView mav = new ModelAndView("production/feedback/saleFeedBack_edit");
        ShfkdjDto shfkdjDto_t = shfkdjService.getDtoById(shfkdjDto.getShfkid());
        List<JcsjDto> lxList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.SALEFEEDBACK_TYPE.getCode());
        shfkdjDto_t.setFormAction("modSaveSaleFeedback");
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("shfkdjDto", shfkdjDto_t);
        mav.addObject("lxList", lxList);
        return mav;
    }

    /**
     * 修改保存
     */
    @RequestMapping(value = "/saleFeedback/modSaveSaleFeedback")
    @ResponseBody
    public Map<String,Object> modSaveSaleFeedback(ShfkdjDto shfkdjDto, HttpServletRequest request) {
        User user = getLoginInfo(request);
        shfkdjDto.setXgry(user.getYhid());
        Map<String,Object> map=new HashMap<>();
        boolean isSuccess = shfkdjService.modSaveSaleFeedback(shfkdjDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }
    /**
     * 删除售后反馈信息
     */
    @RequestMapping(value ="/saleFeedback/delSaleFeedback")
    @ResponseBody
    public Map<String,Object> delSaleFeedback(ShfkdjDto shfkdjDto, HttpServletRequest request){
        User user = getLoginInfo(request);
        Map<String, Object> map = new HashMap<>();
        shfkdjDto.setScry(user.getYhid());
        boolean isSuccess= shfkdjService.delete(shfkdjDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? (xxglService.getModelById("ICOM00003").getXxnr()) : xxglService.getModelById("ICOM00004").getXxnr());
        return map;
    }
    /**
     * 钉钉删除售后反馈信息
     */
    @RequestMapping(value ="/saleFeedback/minidataDelSaleFeedback")
    @ResponseBody
    public Map<String,Object> minidataDelSaleFeedback(ShfkdjDto shfkdjDto, HttpServletRequest request){
        User user = getLoginInfo(request);
        Map<String, Object> map = new HashMap<>();
        shfkdjDto.setScry(user.getYhid());
        boolean isSuccess= shfkdjService.delete(shfkdjDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? (xxglService.getModelById("ICOM00003").getXxnr()) : xxglService.getModelById("ICOM00004").getXxnr());
        return map;
    }
    /**
     * 修改接口
     */
    @RequestMapping(value = "/saleFeedback/minidataModInterface")
    @ResponseBody
    public Map<String,Object> modInterface(ShfkdjDto shfkdjDto) {
        Map<String,Object> map=new HashMap<>();
        ShfkdjDto shfkdjDto_t = shfkdjService.getDtoById(shfkdjDto.getShfkid());
        List<JcsjDto> lxList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.SALEFEEDBACK_TYPE.getCode());
        map.put("lxList", lxList);
        map.put("shfkdjDto", shfkdjDto_t);
        return map;
    }
    /**
     * 新增接口
     */
    @RequestMapping(value = "/saleFeedback/minidataAddInterface")
    @ResponseBody
    public Map<String,Object> minidataAddInterface() {
        Map<String,Object> map=new HashMap<>();
        List<JcsjDto> lxList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.SALEFEEDBACK_TYPE.getCode());
        map.put("lxList", lxList);
        return map;
    }
    /**
     * 售后反馈登记列表页面
     */
    @RequestMapping(value = "/saleFeedback/pageListSaleFeedback")
    public ModelAndView pageListSaleFeedback() {
        ModelAndView mav = new ModelAndView("production/feedback/saleFeedBack_list");
        List<JcsjDto> lxList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.SALEFEEDBACK_TYPE.getCode());
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("lxList", lxList);
        return mav;
    }

    /**
     * 获取售后反馈登记列表
     */
    @RequestMapping(value = "/saleFeedback/pageGetListSaleFeedback")
    @ResponseBody
    public Map<String, Object> pageGetListSaleFeedback(ShfkdjDto shfkdjDto) {
        Map<String, Object> map = new HashMap<>();
        List<ShfkdjDto> shfkdjDtoList=shfkdjService.getPagedDtoList(shfkdjDto);
        map.put("total", shfkdjDto.getTotalNumber());
        map.put("rows", shfkdjDtoList);
        return map;
    }
    /**
     * 售后反馈登记处理记录页面
     */
    @RequestMapping(value = "/saleFeedback/processingrecordsSh")
    public ModelAndView processingRecords(ShfkdjDto shfkdjDto) {
        ModelAndView mav = new ModelAndView("production/feedback/saleFeedBack_records");
        mav.addObject("shfkid",shfkdjDto.getShfkid());
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 获取售后反馈登记处理记录数据
     */
    @RequestMapping(value = "/saleFeedback/pagedataProcessingRecords")
    @ResponseBody
    public Map<String, Object> getPagedListProcessingRecords(CljlxxDto cljlxxDto) {
        Map<String, Object> map = new HashMap<>();
        List<CljlxxDto> cljlxxDtoList=cljlxxService.getRecords(cljlxxDto);
        StringBuilder ids = new StringBuilder();
        for (CljlxxDto cljlxxDto_t:cljlxxDtoList){
            String id=cljlxxDto_t.getLlid();
            ids.append(",").append(id);
        }
        if (ids.length()>0){
            ids = new StringBuilder(ids.substring(1));
        }
        String[] split = ids.toString().split(",");
        List<String> list = Arrays.asList(split);
        List<LlglDto> llglDtos =llglService.getLldhByIds(list);
        map.put("rows", cljlxxDtoList);
        map.put("llglDtos",llglDtos);
        return map;
    }
    /**
     * 售后反馈评论页面
     */
    @RequestMapping(value = "/saleFeedback/commentSaleFeedback")
    public ModelAndView commentSaleFeedback(ShfkdjDto shfkdjDto) {
        ModelAndView mav = new ModelAndView("production/feedback/saleFeedBack_comment");
        shfkdjDto.setFormAction("commentSaveSaleFeedback");
        mav.addObject("shfkdjDto",shfkdjDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 售后反馈评论保存
     */
    @RequestMapping(value = "/saleFeedback/commentSaveSaleFeedback")
    @ResponseBody
    public Map<String,Object> commentSave(ShfkdjDto shfkdjDto, HttpServletRequest request) {
        User user = getLoginInfo(request);
        shfkdjDto.setLrry(user.getYhid());
        Map<String,Object> map=new HashMap<>();
        boolean isSuccess;
        try {
            isSuccess = cljlxxService.saveComment(shfkdjDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
        }
        return map;
    }
    /**
     * 钉钉售后反馈评论保存
     */
    @RequestMapping(value = "/saleFeedback/minidataCommentSaveSaleFeedback")
    @ResponseBody
    public Map<String,Object> minidataCommentSaveSaleFeedback(ShfkdjDto shfkdjDto, HttpServletRequest request) {
        return commentSave(shfkdjDto,request);
    }
    /**
     * 跳转领料选择列表
     */
    @RequestMapping(value = "/saleFeedback/pagedataChooseLlxx")
    public ModelAndView chooseListLlxx() {
        ModelAndView mav = new ModelAndView("production/feedback/saleFeedBack_chooseList");
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 派单页面
     */
    @RequestMapping(value = "/saleFeedback/sendordersSh")
    public ModelAndView sendordersSh(ShfkdjDto shfkdjDto) {
        ModelAndView mav = new ModelAndView("production/feedback/saleFeedBack_sendOrders");
        shfkdjDto.setFormAction("sendordersSaveSh");
        User user=new User();
        user.setJsmc("售后处理");
        List<User> yhjsDtos = commonDao.getYhjsDtos(user);
        for (User yhjsDto : yhjsDtos) {
            yhjsDto.setLhmc(yhjsDto.getZsxm()+"-"+yhjsDto.getYhm()+"-"+yhjsDto.getJsmc());
        }
        mav.addObject("yhjsDtos",yhjsDtos);
        mav.addObject("shfkdjDto",shfkdjDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 派单保存
     */
    @RequestMapping(value = "/saleFeedback/sendordersSaveSh")
    @ResponseBody
    public Map<String, Object> sendOrdersSave(ShfkdjDto shfkdjDto) {
        Map<String, Object> map = new HashMap<>();
        boolean isSuccess;
        try {
            isSuccess = shfkdjService.sendOrdersSave(shfkdjDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
        }
        return map;
    }
    /**
     * 发送通知消息
     */
    @RequestMapping(value = "/saleFeedback/minidataSendSaleFeedbackMsg")
    @ResponseBody
    public Map<String, Object> sendSaleFeedbackMsg(ShfkdjDto shfkdjDto) {
        Map<String, Object> map = new HashMap<>();
        boolean isSuccess;
        try {
            isSuccess = shfkdjService.sendSaleFeedbackMsg(shfkdjDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM99024").getXxnr() : xxglService.getModelById("ICOM99025").getXxnr());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM99025").getXxnr());
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("message", xxglService.getModelById("ICOM99025").getXxnr());
        }
        return map;
    }
    /**
     * 钉钉取消派单
     */
    @RequestMapping(value = "/saleFeedback/minidataCancelsendordersSh")
    @ResponseBody
    public Map<String, Object> minidataCancelsendordersSh(ShfkdjDto shfkdjDto) {
        return this.cancelSendOrders(shfkdjDto);
    }
    /**
     * 取消派单
     */
    @RequestMapping(value = "/saleFeedback/cancelsendordersSh")
    @ResponseBody
    public Map<String, Object> cancelSendOrders(ShfkdjDto shfkdjDto) {
        Map<String, Object> map = new HashMap<>();
        boolean isSuccess;
        try {
            isSuccess = shfkdjService.cancelSendOrders(shfkdjDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
        }
        return map;
    }
    /**
     * 获取售后反馈登记列表钉钉
     */
    @RequestMapping(value = "/saleFeedback/minidataListSaleFeedbackDingTalk")
    @ResponseBody
    public Map<String, Object> getPagedListSaleFeedbackDingTalk(ShfkdjDto shfkdjDto,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        shfkdjDto.setLrry(user.getYhid());
        List<ShfkdjDto> shfkdjDtoList=shfkdjService.getPagedListSaleFeedbackDingTalk(shfkdjDto);
        user.setJsmc("售后处理");
        List<User> yhjsDtos = commonDao.getYhjsDtos(user);
        List<User> xtjsDtos = commonDao.getXtjsDtos(user);
        map.put("yhjslist", yhjsDtos);
        map.put("xtjslist", xtjsDtos);
        map.put("total", shfkdjDto.getTotalNumber());
        map.put("FeedBackList", shfkdjDtoList);
        return map;
    }
    /**
     * 售后反馈登记查看页面
     */
    @RequestMapping(value = "/saleFeedback/minidataSaleFeedback")
    @ResponseBody
    public Map<String, Object> getSaleFeedback(ShfkdjDto shfkdjDto) {
        Map<String, Object> map = new HashMap<>();
        shfkdjDto=shfkdjService.getDtoById(shfkdjDto.getShfkid());
        if (!shfkdjDto.getJd().equals("0")){
          List<Map<String,Object>> cljlList=shfkdjService.getShfkCljl(shfkdjDto);
            map.put("cljlList",cljlList);
        }
        map.put("shfkdjDto",shfkdjDto);
        return map;
    }
    /**
     * 钉钉我的接单接口
     */
    @RequestMapping(value = "/saleFeedback/pagedataGetOrders")
    @ResponseBody
    public Map<String, Object> getOrders(PdjlDto pdjlDto,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        pdjlDto.setLrry(getLoginInfo(request).getYhid());
        List<PdjlDto> pdjlDtoList=pdjlService.getPagedDtoList(pdjlDto);
        map.put("orderList",pdjlDtoList);
        return map;
    }
    /**
     * 钉钉我的接单查看接口
     */
    @RequestMapping(value = "/saleFeedback/minidataViewOrdersDingTalk")
    @ResponseBody
    public Map<String, Object> viewOrdersDingTalk(PdjlDto pdjlDto,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        pdjlDto.setLrry(getLoginInfo(request).getYhid());
        pdjlDto=pdjlService.getDtoById(pdjlDto.getPdjlid());
        ShfkdjDto shfkdjDto=shfkdjService.getDtoById(pdjlDto.getShfkid());
        if (!shfkdjDto.getJd().equals("0")){
            List<Map<String,Object>> cljlList=shfkdjService.getShfkCljl(shfkdjDto);
            map.put("cljlList",cljlList);
        }
        map.put("pdjlDto",pdjlDto);
        return map;
    }
}
