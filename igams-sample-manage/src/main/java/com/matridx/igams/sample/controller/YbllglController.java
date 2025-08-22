package com.matridx.igams.sample.controller;

import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.DataPermissionTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.sample.dao.entities.YbllglDto;
import com.matridx.igams.sample.dao.entities.YbllmxDto;
import com.matridx.igams.sample.service.svcinterface.IYbkcxxService;
import com.matridx.igams.sample.service.svcinterface.IYbllglService;
import com.matridx.igams.sample.service.svcinterface.IYbllmxService;
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
 * {@code @author:JYK}
 */
@RequestMapping("/sample")
@Controller
public class YbllglController extends BaseController {
    @Autowired
    IYbllglService ybllglService;
    @Autowired
    IXxglService xxglService;
    @Autowired
    IYbllmxService ybllmxService;
    @Autowired
    IYbkcxxService ybkcxxService;
    @Autowired
    RedisUtil redisUtil;

    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Override
    public String getPrefix(){
        return urlPrefix;
    }

    /**
     * 跳转样本领料界面
     */
    @RequestMapping(value = "/auditing/pageListYbll")
    public ModelAndView pageListYbll() {
        ModelAndView mav=new ModelAndView("sample/auditing/samplePick_list");
        List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode());
        YbllglDto ybllglDto = new YbllglDto();
        ybllglDto.setAuditType(AuditTypeEnum.AUDIT_SAMPLE.getCode());
        mav.addObject("ybllglDto", ybllglDto);
        mav.addObject("bblxlist", jcsjDtos);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 获取样本领料列表
     */
    @RequestMapping(value = "/auditing/pageGetListYbll")
    @ResponseBody
    public Map<String,Object> pageGetListYbll(YbllglDto ybllglDto,HttpServletRequest request){
        User user = getLoginInfo(request);
        List<Map<String, String>> dwAndbjlist = ybkcxxService.getJsjcdwByjsid(user.getDqjs());
        List<String> jcdwxz = new ArrayList<>();
        if(dwAndbjlist!=null&& !dwAndbjlist.isEmpty()){
            if("1".equals(dwAndbjlist.get(0).get("dwxdbj"))) {
                for (Map<String, String> stringStringMap : dwAndbjlist) {
                    if (stringStringMap.get("jcdw") != null) {
                        jcdwxz.add(stringStringMap.get("jcdw"));
                    }
                }
            }
        }
        ybllglDto.setJcdwxz(jcdwxz);
        List<YbllglDto> ybllDtoList = ybllglService.getPagedDtoList(ybllglDto);
        Map<String, Object> map= new HashMap<>();
        map.put("total", ybllglDto.getTotalNumber());
        map.put("rows", ybllDtoList);
        return map;
    }
    /**
     * 查看样本领料信息
     */
    @RequestMapping("/auditing/viewYbll")
    public ModelAndView viewYbll(YbllglDto ybllglDto) {
        ModelAndView mav = new ModelAndView("sample/auditing/samplePick_view");
        List<YbllglDto> ybllglDtoList = ybllglService.getDtoList(ybllglDto);
        ybllglDto = ybllglService.getDtoById(ybllglDto.getLlid());
        mav.addObject("ybllList",ybllglDtoList);
        mav.addObject("ybllglDto", ybllglDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 跳转至修改界面
     */
    @RequestMapping(value = "/auditing/modYbll")
    public ModelAndView modYbll(YbllglDto ybllglDto,HttpServletRequest request) {
        ModelAndView mav=new ModelAndView("sample/inventory/ybkcxx_ybpickingCar");
        YbllglDto dtoById = ybllglService.getDtoById(ybllglDto.getLlid());
        User user = getLoginInfo(request);
        dtoById.setXgqlldh(dtoById.getLldh());
        if ("submitSaveYbll".equals(ybllglDto.getLjbj())){
            dtoById.setFormAction("submitSaveYbll");
        }else if ("auditSaveYbll".equals(ybllglDto.getLjbj())){
            dtoById.setFormAction("auditSaveYbll");
        } else if ("deliverySaveYbll".equals(ybllglDto.getLjbj())){
            dtoById.setFormAction("deliverySaveYbll");
            dtoById.setFlrmc(user.getZsxm());
            dtoById.setFlr(user.getYhid());
            dtoById.setXgqflr(dtoById.getFlr());
        }else {
            dtoById.setFormAction("modSaveYbll");
        }
        List<YbllmxDto> dtoList = ybllmxService.getDtoListWithPrint(ybllglDto.getLlid());
        StringBuilder ids= new StringBuilder();
        if(dtoList!=null&&!dtoList.isEmpty()) {
            for (YbllmxDto ybllmxDto : dtoList) {
                ids.append(",").append(ybllmxDto.getYbkcid());
            }
            ids.substring(1);
        }
        mav.addObject("idsyb", ids.toString());
        mav.addObject("ybllglDto", dtoById);
        mav.addObject("url", "/sample/auditing/");
        mav.addObject("CHECK_PASS", StatusEnum.CHECK_PASS.getCode());
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 跳转至提交界面
     */
    @RequestMapping(value = "/auditing/submitYbll")
    public ModelAndView submitYbll(YbllglDto ybllglDto,HttpServletRequest request) {
        ybllglDto.setLjbj("submitSaveYbll");
        return this.modYbll(ybllglDto,request);
    }
    /**
     * 样本领料提交保存
     */
    @RequestMapping(value ="/auditing/submitSaveYbll")
    @ResponseBody
    public Map<String,Object> submitSaveYbll(YbllglDto ybllglDto,HttpServletRequest request) {
        return this.modSaveYbll(ybllglDto,request);
    }
    /**
     * 跳转至审核界面
     */
    @RequestMapping(value = "/auditing/auditYbll")
    public ModelAndView auditYbll(YbllglDto ybllglDto,HttpServletRequest request) {
        ybllglDto.setLjbj("auditSaveYbll");
        return this.modYbll(ybllglDto,request);
    }
    /**
     * 样本领料审核保存
     */
    @RequestMapping(value ="/auditing/auditSaveYbll")
    @ResponseBody
    public Map<String,Object> auditSaveYbll(YbllglDto ybllglDto,HttpServletRequest request) {
        return this.modSaveYbll(ybllglDto,request);
    }
    /**
     * 跳转至出库界面
     */
    @RequestMapping(value = "/auditing/deliveryYbll")
    public ModelAndView deliveryYbll(YbllglDto ybllglDto,HttpServletRequest request) {
        ybllglDto.setLjbj("deliverySaveYbll");
        return this.modYbll(ybllglDto,request);
    }
    /**
     * 样本领料修改保存或提交或审核
     */
    @RequestMapping(value ="/auditing/modSaveYbll")
    @ResponseBody
    public Map<String,Object> modSaveYbll(YbllglDto ybllglDto,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        if (!ybllglDto.getXgqlldh().equals(ybllglDto.getLldh())){
            // 校验领料单号是否重复
            boolean result = ybllglService.getDtoByLldh(ybllglDto);
            if (!result) {
                map.put("status", "fail");
                map.put("message", "领料单号不允许重复！");
                return map;
            }
        }
        User user = getLoginInfo(request);
        try {
            boolean isSuccess = ybllglService.modSaveYbll(ybllglDto,user);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
            map.put("ywid",ybllglDto.getLlid());
            map.put("auditType", AuditTypeEnum.AUDIT_SAMPLE.getCode());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
        }
        map.put("urlPrefix",urlPrefix);
        return map;
    }

    /**
     * 样本领料出库
     */
    @RequestMapping(value ="/auditing/deliverySaveYbll")
    @ResponseBody
    public Map<String,Object> deliverySaveYbll(YbllglDto ybllglDto,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        try {
            boolean isSuccess = ybllglService.deliveryYbll(ybllglDto,user);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
        }
        map.put("urlPrefix",urlPrefix);
        return map;
    }
    /**
     * 删除或废弃样本领料信息
     */
    @RequestMapping(value ="/auditing/delYbll")
    @ResponseBody
    public Map<String,Object> delYbll(YbllglDto ybllglDto, HttpServletRequest request){
        User user = getLoginInfo(request);
        Map<String, Object> map = new HashMap<>();
        try {
            boolean isSuccess = ybllglService.delYbll(ybllglDto,user);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? (xxglService.getModelById("ICOM00003").getXxnr()) : xxglService.getModelById("ICOM00004").getXxnr());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00004").getXxnr());
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("message", xxglService.getModelById("ICOM00004").getXxnr());
        }
        return map;
    }
    @RequestMapping(value ="/auditing/discardYbll")
    @ResponseBody
    public Map<String,Object> discardYbll(YbllglDto ybllglDto, HttpServletRequest request) {
        return this.delYbll(ybllglDto,request);
    }
    /**
     * 跳转样本领料审核列表
     */
    @RequestMapping(value = "/auditing/pageListYbllsh")
    public ModelAndView pageListYbllsh() {
        ModelAndView mav=new ModelAndView("sample/auditing/samplePick_auditList");
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 获取样本领料审核列表
     */
    @RequestMapping(value = "/auditing/pageGetListYbllsh")
    @ResponseBody
    public Map<String,Object> pageGetListYbllsh(YbllglDto ybllglDto, HttpServletRequest request){
        // 附加委托参数
        DataPermission.addWtParam(ybllglDto);
        // 附加审核状态过滤
        if (GlobalString.AUDIT_SHZT_YSH.equals(ybllglDto.getDqshzt())) {
            DataPermission.add(ybllglDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "ybllgl", "llid",
                    AuditTypeEnum.AUDIT_SAMPLE);
        } else {
            DataPermission.add(ybllglDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "ybllgl", "llid",
                    AuditTypeEnum.AUDIT_SAMPLE);
        }
        DataPermission.addCurrentUser(ybllglDto, getLoginInfo(request));
		List<YbllglDto> ybllDtoList = ybllglService.getPagedAuditPick(ybllglDto);
        Map<String, Object> map= new HashMap<>();
        map.put("total", ybllglDto.getTotalNumber());
        map.put("rows", ybllDtoList);
        return map;
    }

    /**
     * 打开打印材料领料单页面
     */
    @RequestMapping("/auditing/lldprintYbll")
    public ModelAndView lldprintYbll(YbllglDto ybllglDto) {
        ModelAndView mav=new ModelAndView("sample/auditing/samplePickPrint");
        //获取上半部分
        List<YbllglDto> ybllglDtos = ybllglService.getDtoByIds(ybllglDto);
        for (YbllglDto dto : ybllglDtos) {
            //获取下半部分
            List<YbllmxDto> ybllmxDtos = ybllmxService.getDtoListWithPrint(dto.getLlid());
            dto.setYbllmxDtos(ybllmxDtos);
        }
        mav.addObject("ybllglDtos", ybllglDtos);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

}
