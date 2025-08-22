package com.matridx.igams.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.CharacterEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.web.dao.entities.WbcxDto;
import com.matridx.igams.web.service.impl.WbcxServiceImpl;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/externalPrograms")
public class ExternalProgramsController extends BaseController {

    @Autowired
    WbcxServiceImpl wbcxService;
    @Autowired
    IXxglService xxglService;
    @Autowired
    RedisUtil redisUtil;
    /**
     * 外部程序列表
     * @return
     */
    @RequestMapping("/externalPrograms/pageListExternalPrograms")
    public ModelAndView pageListWbcx() {
        return new ModelAndView("externalPrograms/programs_list");
    }

    /**
     * 获取列表数据
     */
    @RequestMapping("/externalPrograms/pagedataExternalProgramsList")
    @ResponseBody
    public Map<String, Object> pagedataExternalProgramsList(WbcxDto wbcxDto) {
        Map<String, Object> map = new HashMap<>();
        DBEncrypt dBEncrypt = new DBEncrypt();
        List<WbcxDto> list = wbcxService.getPagedDtoList(wbcxDto);
        for (WbcxDto dto : list) {
            if (StringUtil.isNotBlank(dto.getAppid())) {
                dto.setAppid(dBEncrypt.dCode(dto.getAppid()));
            }
            if (StringUtil.isNotBlank(dto.getSecret())) {
                dto.setSecret(dBEncrypt.dCode(dto.getSecret()));
            }
            if (StringUtil.isNotBlank(dto.getJumpdingtalkurl())) {
                dto.setJumpdingtalkurl(dBEncrypt.dCode(dto.getJumpdingtalkurl()));
            }
            if (StringUtil.isNotBlank(dto.getAgentid())) {
                dto.setAgentid(dBEncrypt.dCode(dto.getAgentid()));
            }
            if (StringUtil.isNotBlank(dto.getAeskey())) {
                dto.setAeskey(dBEncrypt.dCode(dto.getAeskey()));
            }
            if (StringUtil.isNotBlank(dto.getMiniappid())) {
                dto.setMiniappid(dBEncrypt.dCode(dto.getMiniappid()));
            }
            if (StringUtil.isNotBlank(dto.getToken())) {
                dto.setToken(dBEncrypt.dCode(dto.getToken()));
            }
        }
        map.put("total",wbcxDto.getTotalNumber());
        map.put("rows",list);
        return map;
    }
    /**
     * 查看信息对应详情
     */
    @RequestMapping("/externalPrograms/viewExternalPrograms")
    public ModelAndView viewExternalPrograms(WbcxDto wbcxDto){
        ModelAndView mav=new ModelAndView("externalPrograms/programs_view");
        WbcxDto dto=wbcxService.getDtoById(wbcxDto.getWbcxid());
        DBEncrypt dBEncrypt = new DBEncrypt();
        if (null != dto){
            if (StringUtil.isNotBlank(dto.getAppid())) {
                dto.setAppid(dBEncrypt.dCode(dto.getAppid()));
            }
            if (StringUtil.isNotBlank(dto.getSecret())) {
                dto.setSecret(dBEncrypt.dCode(dto.getSecret()));
            }
            if (StringUtil.isNotBlank(dto.getJumpdingtalkurl())) {
                dto.setJumpdingtalkurl(dBEncrypt.dCode(dto.getJumpdingtalkurl()));
            }
            if (StringUtil.isNotBlank(dto.getAgentid())) {
                dto.setAgentid(dBEncrypt.dCode(dto.getAgentid()));
            }
            if (StringUtil.isNotBlank(dto.getAeskey())) {
                dto.setAeskey(dBEncrypt.dCode(dto.getAeskey()));
            }
            if (StringUtil.isNotBlank(dto.getCropid())) {
                dto.setCropid(dBEncrypt.dCode(dto.getCropid()));
            }
            if (StringUtil.isNotBlank(dto.getMiniappid())) {
                dto.setMiniappid(dBEncrypt.dCode(dto.getMiniappid()));
            }
            if (StringUtil.isNotBlank(dto.getToken())) {
                dto.setToken(dBEncrypt.dCode(dto.getToken()));
            }
        }
        mav.addObject("dto", dto);
        return mav;
    }

    /**
     * 增加数据
     *
     */
    @RequestMapping("/externalPrograms/addExternalPrograms")
    public ModelAndView addExternalPrograms(WbcxDto wbcxDto){
        ModelAndView mav=new ModelAndView("externalPrograms/programs_add");
        wbcxDto.setFormAction("pagedataAddSaveInfo");
        List<String> list = new ArrayList<>();
        for (CharacterEnum value : CharacterEnum.values()) {
            list.add(value.getCode());
        }
        mav.addObject("dto", wbcxDto);
        mav.addObject("list", list);
        return mav;
    }


    /**
     * 增加信息到数据库
     * @param
     * @return
     */
    @RequestMapping("/externalPrograms/pagedataAddSaveInfo")
    @ResponseBody
    public Map<String, Object> pagedataAddSaveInfo(WbcxDto wbcxDto){
        Map<String, Object> map= new HashMap<>();
        User user = getLoginInfo();
        wbcxDto.setLrry(user.getYhid());
        wbcxDto.setWbcxid(StringUtil.generateUUID());
        DBEncrypt dBEncrypt = new DBEncrypt();
        if (StringUtil.isNotBlank(wbcxDto.getAppid())) {
            wbcxDto.setAppid(dBEncrypt.eCode(wbcxDto.getAppid()));
        }
        if (StringUtil.isNotBlank(wbcxDto.getSecret())) {
            wbcxDto.setSecret(dBEncrypt.eCode(wbcxDto.getSecret()));
        }
        if (StringUtil.isNotBlank(wbcxDto.getJumpdingtalkurl())) {
            wbcxDto.setJumpdingtalkurl(dBEncrypt.eCode(wbcxDto.getJumpdingtalkurl()));
        }
        if (StringUtil.isNotBlank(wbcxDto.getAgentid())) {
            wbcxDto.setAgentid(dBEncrypt.eCode(wbcxDto.getAgentid()));
        }
        if (StringUtil.isNotBlank(wbcxDto.getAeskey())) {
            wbcxDto.setAeskey(dBEncrypt.eCode(wbcxDto.getAeskey()));
        }
        if (StringUtil.isNotBlank(wbcxDto.getCropid())) {
            wbcxDto.setCropid(dBEncrypt.eCode(wbcxDto.getCropid()));
        }
        if (StringUtil.isNotBlank(wbcxDto.getMiniappid())){
            wbcxDto.setDecodeid(wbcxDto.getMiniappid());
            wbcxDto.setMiniappid(dBEncrypt.eCode(wbcxDto.getMiniappid()));
        }
        if (StringUtil.isNotBlank(wbcxDto.getToken())) {
            wbcxDto.setToken(dBEncrypt.eCode(wbcxDto.getToken()));
        }
        boolean isSuccess=wbcxService.insert(wbcxDto);
        if(isSuccess){
            WbcxDto dto = wbcxService.getDtoById(wbcxDto.getWbcxid());
            redisUtil.hset("WbcxInfo",wbcxDto.getWbcxid(), JSONObject.toJSONString(dto),-1);
        }
        map.put("status",isSuccess?"success":"fail");
        map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 修改页面
     * @param
     * @return
     */
    @RequestMapping("/externalPrograms/modExternalPrograms")
    public ModelAndView modExternalPrograms(WbcxDto wbcxDto){
        ModelAndView mav=new ModelAndView("externalPrograms/programs_add");
        DBEncrypt dBEncrypt = new DBEncrypt();
        WbcxDto dto = wbcxService.getDtoById(wbcxDto.getWbcxid());
        if (StringUtil.isNotBlank(dto.getAppid())) {
            dto.setAppid(dBEncrypt.dCode(dto.getAppid()));
        }
        if (StringUtil.isNotBlank(dto.getSecret())) {
            dto.setSecret(dBEncrypt.dCode(dto.getSecret()));
        }
        if (StringUtil.isNotBlank(dto.getJumpdingtalkurl())) {
            dto.setJumpdingtalkurl(dBEncrypt.dCode(dto.getJumpdingtalkurl()));
        }
        if (StringUtil.isNotBlank(dto.getAgentid())) {
            dto.setAgentid(dBEncrypt.dCode(dto.getAgentid()));
        }
        if (StringUtil.isNotBlank(dto.getAeskey())) {
            dto.setAeskey(dBEncrypt.dCode(dto.getAeskey()));
        }
        if (StringUtil.isNotBlank(dto.getCropid())) {
            dto.setCropid(dBEncrypt.dCode(dto.getCropid()));
        }
        if (StringUtil.isNotBlank(dto.getMiniappid())) {
            dto.setMiniappid(dBEncrypt.dCode(dto.getMiniappid()));
        }
        if (StringUtil.isNotBlank(dto.getToken())) {
            dto.setToken(dBEncrypt.dCode(dto.getToken()));
        }
        dto.setFormAction("pagedataModProgramsInfo");
        List<String> list = new ArrayList<>();
        for (CharacterEnum value : CharacterEnum.values()) {
            list.add(value.getCode());
        }
        mav.addObject("dto", dto);
        mav.addObject("list", list);
        return mav;
    }
    /**
     * 修改消息到数据库
     */
    @RequestMapping("/externalPrograms/pagedataModProgramsInfo")
    @ResponseBody
    public Map<String, Object> pagedataModProgramsInfo(WbcxDto wbcxDto) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo();
        wbcxDto.setXgry(user.getYhid());
        DBEncrypt dBEncrypt = new DBEncrypt();
        if (StringUtil.isNotBlank(wbcxDto.getAppid())) {
            wbcxDto.setAppid(dBEncrypt.eCode(wbcxDto.getAppid()));
        }
        if (StringUtil.isNotBlank(wbcxDto.getSecret())) {
            wbcxDto.setSecret(dBEncrypt.eCode(wbcxDto.getSecret()));
        }
        if (StringUtil.isNotBlank(wbcxDto.getJumpdingtalkurl())) {
            wbcxDto.setJumpdingtalkurl(dBEncrypt.eCode(wbcxDto.getJumpdingtalkurl()));
        }
        if (StringUtil.isNotBlank(wbcxDto.getAgentid())) {
            wbcxDto.setAgentid(dBEncrypt.eCode(wbcxDto.getAgentid()));
        }
        if (StringUtil.isNotBlank(wbcxDto.getAeskey())) {
            wbcxDto.setAeskey(dBEncrypt.eCode(wbcxDto.getAeskey()));
        }
        if (StringUtil.isNotBlank(wbcxDto.getCropid())) {
            wbcxDto.setCropid(dBEncrypt.eCode(wbcxDto.getCropid()));
        }
        if (StringUtil.isNotBlank(wbcxDto.getMiniappid())){
            wbcxDto.setDecodeid(wbcxDto.getMiniappid());
            wbcxDto.setMiniappid(dBEncrypt.eCode(wbcxDto.getMiniappid()));
        }
        if (StringUtil.isNotBlank(wbcxDto.getToken())) {
            wbcxDto.setToken(dBEncrypt.eCode(wbcxDto.getToken()));
        }
        boolean isSuccess=wbcxService.update(wbcxDto);
        if(isSuccess){
            WbcxDto dto = wbcxService.getDtoById(wbcxDto.getWbcxid());
            redisUtil.hset("WbcxInfo",wbcxDto.getWbcxid(), JSONObject.toJSONString(dto),-1);
        }
        map.put("status",isSuccess?"success":"fail");
        map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 删除信息
     * @param
     * @return
     */
    @RequestMapping(value="/externalPrograms/delProgramsInfo")
    @ResponseBody
    public Map<String,Object> delProgramsInfo(WbcxDto wbcxDto){
        Map<String,Object> map = new HashMap<>();
        boolean isSuccess=false;
        try {
            User user = getLoginInfo();
            wbcxDto.setScry(user.getYhid());
            isSuccess = wbcxService.delete(wbcxDto);
        } catch (Exception e) {
            map.put("status","fail");
            map.put("message", e.getMessage());
        }
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
        return map;
    }
}
