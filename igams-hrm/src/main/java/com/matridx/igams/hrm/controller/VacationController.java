package com.matridx.igams.hrm.controller;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.UserDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.RedisCommonKeyEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IUserService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.hrm.dao.entities.JqffjlDto;
import com.matridx.igams.hrm.dao.entities.JqffszDto;
import com.matridx.igams.hrm.dao.entities.JqxzDto;
import com.matridx.igams.hrm.service.svcinterface.IJqffjlService;
import com.matridx.igams.hrm.service.svcinterface.IJqffszService;
import com.matridx.igams.hrm.service.svcinterface.IJqxzService;
import com.matridx.igams.hrm.dao.entities.YhjqDto;
import com.matridx.igams.hrm.service.svcinterface.IYhjqService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author:JYK
 */
@Controller
@RequestMapping("/vacation")
public class VacationController extends BaseController {
    @Autowired
    IJqffszService jqffszService;
    @Autowired
    RedisUtil redisUtil;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Autowired
    IXxglService xxglService;
    @Autowired
    IJqffjlService jqffjlService;
    @Autowired
    IYhjqService yhjqService;
    @Autowired
    IJqxzService jqxzService;
    @Autowired
    IUserService userService;
    @Override
    public String getPrefix() {
        return urlPrefix;
    }

    /**
     * 假期发放设置列表
     */
    @RequestMapping("/vacation/pageListVacationGrant")
    public ModelAndView pageListVacationGrant(){
        ModelAndView mav = new ModelAndView("vacation/vacation/vacationGrant_list");
        List<JcsjDto> jqlxlist=redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.HOLIDAY_TYPE.getCode());//假期类型
        mav.addObject("jqlxlist",jqlxlist);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 列表获取数据
     */
    @RequestMapping("/vacation/pageGetListVacationGrant")
    @ResponseBody
    public Map<String,Object> pageGetListVacationGrant(JqffszDto jqffszDto){
        Map<String,Object> result = new HashMap<>();
        List<JqffszDto> rows=jqffszService.getPagedDtoList(jqffszDto);
        result.put("rows",rows);
        result.put("total",jqffszDto.getTotalNumber());
        return result;
    }
    /**
     * 假期发放设置列表查看
     */
    @RequestMapping("/vacation/viewVacationGrant")
    public ModelAndView viewVacationGrant(JqffszDto jqffszDto){
        ModelAndView mav = new ModelAndView("vacation/vacation/vacationGrant_view");
        jqffszDto=jqffszService.getDtoById(jqffszDto.getFfszid());
        mav.addObject("jqffszDto",jqffszDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 假期查看获取假期发放记录数据
     */
    @RequestMapping("/vacation/pagedataViewVacationGrant")
    @ResponseBody
    public Map<String,Object> pagedataViewVacationGrant(JqffjlDto jqffjlDto){
        Map<String,Object> result = new HashMap<>();
        List<JqffjlDto> rows=jqffjlService.getPagedDtoList(jqffjlDto);
        result.put("rows",rows);
        result.put("total",jqffjlDto.getTotalNumber());
        return result;
    }
    /**
     * 假期查看获取假期限制
     */
    @RequestMapping("/vacation/pagedataViewVacationLimitation")
    @ResponseBody
    public Map<String,Object> pagedataViewVacationLimitation(JqxzDto jqxzDto){
        Map<String,Object> result = new HashMap<>();
        List<JqxzDto> rows=jqxzService.getPagedDtoList(jqxzDto);
        result.put("rows",rows);
        result.put("total",jqxzDto.getTotalNumber());
        return result;
    }
    /**
     * 假期设置删除
     */
    @RequestMapping("/vacation/pagedataDelVacationGrant")
    @ResponseBody
    public Map<String,Object> pagedataDelVacationGrant(JqffjlDto jqffjlDto){
        Map<String,Object> result = new HashMap<>();
        List<JqffjlDto> rows=jqffjlService.getDtoList(jqffjlDto);
        StringBuilder message=new StringBuilder();
         message.append("注意:");
         message.append("</br>");
        for (JqffjlDto row : rows) {
            if (StringUtil.isNotBlank(row.getYhm()) && StringUtil.isNotBlank(row.getJeze())) {
                String dw = "";
                if (StringUtil.isNotBlank(row.getDw())) {
                    dw = "1".equals(row.getDw()) ? "天" : "小时";
                }
                message.append(row.getYhm()).append(row.getZsxm()).append("已发放").append(row.getJeze()).append(dw).append("假期").append("</br>");
            }
        }
        message.append("您确定要删除所选择的记录吗?");
        result.put("message",message.toString());
        return result;
    }
    /**
     * 假期设置删除
     */
    @RequestMapping("/vacation/delVacationGrant")
    @ResponseBody
    public Map<String,Object> delVacationGrant(JqffszDto jqffszDto, HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        User user=getLoginInfo(request);
        user.setScry(user.getYhid());
        boolean isSuccess=jqffszService.delete(jqffszDto);
        result.put("status", isSuccess?"success":"fail");
        result.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
        return result;
    }
    /**
     * 假期发放设置新增
     */
    @RequestMapping("/vacation/addVacationGrant")
    public ModelAndView addVacationGrant(){
        ModelAndView mav = new ModelAndView("vacation/vacation/vacationGrant_add");
        List<JcsjDto> jqlxlist=redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.HOLIDAY_TYPE.getCode());//假期类型
        mav.addObject("jqlxlist",jqlxlist);
        mav.addObject("jqlxlist_t", JSON.toJSONString(jqlxlist));
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("formAction", "addSaveVacationGrant");
        return mav;
    }
    /**
     * 假期获取数据
     */
    @RequestMapping("/vacation/pagedataOnloadMx")
    @ResponseBody
    public Map<String,Object> pagedataOnloadMx(){
        Map<String,Object> result = new HashMap<>();
        List<JqffszDto> list=new ArrayList<>();
        result.put("rows",list);
        return result;
    }
    /**
     * 假期设置新增保存
     */
    @RequestMapping("/vacation/addSaveVacationGrant")
    @ResponseBody
    public Map<String,Object> addSaveVacationGrant(JqffszDto jqffszDto, HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        try {
            User user=getLoginInfo(request);
            jqffszDto.setLrry(user.getYhid());
            boolean isSuccess=jqffszService.addSaveVacationGrant(jqffszDto);
            result.put("status", isSuccess?"success":"fail");
            result.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        }catch (BusinessException e){
            result.put("status", "fail");
            result.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
        }
        return result;
    }
    /**
     * 获取假期不同的数据
     */
    @RequestMapping("/vacation/minidataGetVacationGrant")
    @ResponseBody
    public Map<String,Object> minidataGetVacationGrant(YhjqDto yhjqDto){
        Map<String,Object> result = new HashMap<>();
        List<YhjqDto> rows=yhjqService.getPagedDtoList(yhjqDto);
        result.put("rows",rows);
        result.put("total",yhjqDto.getTotalNumber());
        return result;
    }
    /**
     * 获取假期不同的数据
     */
    @RequestMapping("/vacation/pagedataGetVacationGrant")
    @ResponseBody
    public Map<String,Object> pagedataGetVacationGrant(YhjqDto yhjqDto){
        return minidataGetVacationGrant(yhjqDto);
    }
    /**
     * 假期发放设置列表
     */
    @RequestMapping("/vacation/pageListVacationRecord")
    public ModelAndView pageListVacationRecord(){
        ModelAndView mav = new ModelAndView("vacation/vacation/vacationRecord_list");
        List<JcsjDto> jqlxlist=redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.HOLIDAY_TYPE.getCode());//假期类型
        mav.addObject("jqlxlist",jqlxlist);
        List<String> nds = jqffjlService.getAllNd();
        mav.addObject("nds", nds);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 列表获取数据
     */
    @RequestMapping("/vacation/pageGetListVacationRecord")
    @ResponseBody
    public Map<String,Object> pageGetListVacationRecord(JqffjlDto jqffjlDto){
        Map<String,Object> result = new HashMap<>();
        List<JqffjlDto> rows=jqffjlService.getPagedDtoList(jqffjlDto);
        result.put("rows",rows);
        result.put("total",jqffjlDto.getTotalNumber());
        return result;
    }
    /**
     * 假期发放记录新增
     */
    @RequestMapping("/vacation/addVacationRecord")
    public ModelAndView addVacationRecord(){
        ModelAndView mav = new ModelAndView("vacation/vacation/vacationRecord_add");
        List<JcsjDto> jqlxlist=redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.HOLIDAY_TYPE.getCode());//假期类型
        mav.addObject("jqlxlist",jqlxlist);
        mav.addObject("fffs","1");
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("formAction", "addSaveVacationRecord");
        return mav;
    }
    /**
     * 假期设置新增保存
     */
    @RequestMapping("/vacation/addSaveVacationRecord")
    @ResponseBody
    public Map<String,Object> addSaveVacationRecord(JqffjlDto jqffjlDto, HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        boolean isSuccess;
        try {
            User user=getLoginInfo(request);
            jqffjlDto.setLrry(user.getYhid());
            jqffjlDto.setYhm(user.getYhm());
            isSuccess = jqffjlService.addSaveVacationRecord(jqffjlDto);
            result.put("status", isSuccess?"success":"fail");
            result.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        }catch (BusinessException e){
            result.put("status", "fail");
            result.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
            result.put("lb", e.getLb());
            result.put("cs", JSON.toJSONString(jqffjlDto));
        }
        return result;
    }
    /**
     * 假期发放记录新增保存 确认ONE
     */
    @RequestMapping("/vacation/pagedataConfirmSaveOne")
    @ResponseBody
    public Map<String,Object> confirmSaveOne(HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        String jqffjlDtoStr = request.getParameter("jqffjlDto");
        JqffjlDto jqffjlDto = JSON.parseObject(jqffjlDtoStr, JqffjlDto.class);
        boolean isSuccess;
        try {
            isSuccess = jqffjlService.confirmSaveOne(jqffjlDto);
            result.put("status", isSuccess?"success":"fail");
            result.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        }catch (BusinessException e){
            result.put("status", "fail");
            result.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
            result.put("lb", e.getLb());
            result.put("cs", JSON.toJSONString(jqffjlDto));
        }
        return result;
    }
    /**
     * 假期发放记录新增保存 确认Two
     */
    @RequestMapping("/vacation/pagedataConfirmSaveTwo")
    @ResponseBody
    public Map<String,Object> confirmSaveTwo(HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        String jqffjlDtoStr = request.getParameter("jqffjlDto");
        JqffjlDto jqffjlDto = JSON.parseObject(jqffjlDtoStr, JqffjlDto.class);
        boolean isSuccess;
        try {
            isSuccess = jqffjlService.confirmSaveTwo(jqffjlDto);
            result.put("status", isSuccess?"success":"fail");
            result.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        }catch (BusinessException e){
            result.put("status", "fail");
            result.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
        }
        return result;
    }
    /**
     * 假期发放记录查看
     */
    @RequestMapping("/vacation/viewVacationRecord")
    public ModelAndView viewVacationRecord(JqffjlDto jqffjlDto){
        ModelAndView mav = new ModelAndView("vacation/vacation/vacationRecord_view");
        jqffjlDto=jqffjlService.getDtoById(jqffjlDto.getFfjlid());
        JqffszDto jqffszDto  = new JqffszDto();
        if (StringUtil.isNotBlank(jqffjlDto.getFfszid())){
            jqffszDto = jqffszService.getDtoById(jqffjlDto.getFfszid());
        }
        mav.addObject("jqffjlDto",jqffjlDto);
        mav.addObject("jqffszDto",jqffszDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     *  删除假期发放记录
     */
    @RequestMapping("/vacation/delVacationRecord")
    @ResponseBody
    public Map<String,Object> delVacationRecord(JqffjlDto jqffjlDto,HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        boolean isSuccess;
        try {
            User user=getLoginInfo(request);
            jqffjlDto.setScry(user.getYhid());
            jqffjlDto.setYhm(user.getYhm());
            isSuccess = jqffjlService.delVacationRecord(jqffjlDto);
            result.put("status", isSuccess?"success":"fail");
            result.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
        }catch (BusinessException e){
            result.put("status", "fail");
            result.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00004").getXxnr());
            result.put("lb", e.getLb());
            result.put("cs", JSON.toJSONString(jqffjlDto));
        }
        return result;
    }
    /**
     * 删除假期发放记录 确认ONE
     */
    @RequestMapping("/vacation/pagedataConfirmDelVacationRecord")
    @ResponseBody
    public Map<String,Object> confirmDelVacationRecord(HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        String jqffjlDtoStr = request.getParameter("jqffjlDto");
        JqffjlDto jqffjlDto = JSON.parseObject(jqffjlDtoStr, JqffjlDto.class);
        boolean isSuccess;
        try {
            isSuccess = jqffjlService.confirmDelVacationRecord(jqffjlDto);
            result.put("status", isSuccess?"success":"fail");
            result.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
        }catch (BusinessException e){
            result.put("status", "fail");
            result.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00003").getXxnr());
        }
        return result;
    }
    /**
     * 列表获取数据
     */
    @RequestMapping("/vacation/minidataGetDiffentWithDDAndOA")
    @ResponseBody
    public Map<String,Object> minidataGetDiffentWithDDAndOA(YhjqDto yhjqDto){
        Map<String,Object> result = new HashMap<>();
        List<YhjqDto> rows=yhjqService.getPagedDtoList(yhjqDto);
        List<JcsjDto> jqlxs=redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.HOLIDAY_TYPE.getCode());//假期类型
        result.put("jqlxs",jqlxs);
        result.put("rows",rows);
        result.put("total",yhjqDto.getTotalNumber());
        return result;
    }
    /**
     * 假期发放记录查看
     */
    @RequestMapping("/vacation/setadminPeo")
    public ModelAndView setadmin(HttpServletRequest request){
        User user = getLoginInfo(request);
        ModelAndView mav = new ModelAndView("vacation/vacation/vacationGrant_setadmin");
        String ddid = String.valueOf(redisUtil.get(RedisCommonKeyEnum.VACATION_ADMIN.getKey()));
        mav.addObject("ddid",ddid);
        UserDto userDto = new UserDto();
        userDto.setWbcxid(user.getWbcxid());
        List<UserDto> userDtos = userService.getAllUserList(userDto);
        mav.addObject("userDtos", userDtos);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 列表获取数据
     */
    @RequestMapping("/vacation/setadminSavePeo")
    @ResponseBody
    public Map<String,Object> setadminSave(String ddid){
        Map<String,Object> result = new HashMap<>();
        boolean isSuccess = redisUtil.set(RedisCommonKeyEnum.VACATION_ADMIN.getKey(), ddid);
        result.put("status", isSuccess?"success":"fail");
        result.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        return result;
    }
}
