package com.matridx.igams.production.controller;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.production.dao.entities.SbfyDto;
import com.matridx.igams.production.service.svcinterface.ISbfyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : 郭祥杰
 * @date :
 */
@Controller
@RequestMapping("/equipmentCost")
public class EquipmentCostController extends BaseController {
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Override
    public String getPrefix(){
        return urlPrefix;
    }
    @Autowired
    private ISbfyService sbfyService;
    @Autowired
    private IXxglService xxglService;

    /**
     * @Description: 设备费用维护查询
     * @param sbfyDto
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2024/11/5 17:09
     */
    @RequestMapping(value = "/equipmentCost/payEquipmentCost")
    @ResponseBody
    public Map<String,Object> payEquipmentCost(SbfyDto sbfyDto){
        Map<String,Object> sbfyMap = new HashMap<>();
        sbfyMap = sbfyService.querySbfyList(sbfyDto);
        return sbfyMap;
    }

    /**
     * @Description: 设备费用删除
     * @param sbfyDto
     * @param request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2024/11/5 17:18
     */
    @RequestMapping(value = "/equipmentCost/pagedataDelCost")
    @ResponseBody
    public Map<String,Object> pagedataDelCost(SbfyDto sbfyDto,HttpServletRequest request){
        User user = getLoginInfo(request);
        sbfyDto.setScry(user.getYhid());
        Map<String,Object> result = new HashMap<>();
        boolean isSuccess = sbfyService.delete(sbfyDto);
        result.put("status", isSuccess ? "success" : "fail");
        result.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        return result;
    }
    @RequestMapping(value = "/equipmentCost/pagedataAddCost")
    @ResponseBody
    public Map<String,Object> pagedataAddCost(SbfyDto sbfyDto,HttpServletRequest request){
        User user = getLoginInfo(request);
        sbfyDto.setLrry(user.getYhid());
        Map<String,Object> result = new HashMap<>();
        boolean isSuccess = sbfyService.insertSbfyDto(sbfyDto);
        result.put("status", isSuccess ? "success" : "fail");
        result.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        return result;
    }
}
