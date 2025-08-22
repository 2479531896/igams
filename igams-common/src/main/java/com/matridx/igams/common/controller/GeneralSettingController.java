package com.matridx.igams.common.controller;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.TyszDto;
import com.matridx.igams.common.dao.entities.TyszmxDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.ITyszService;
import com.matridx.igams.common.service.svcinterface.ITyszmxService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/common")
public class GeneralSettingController extends BaseController{
    @Autowired
    private ITyszService tyszService;
    @Autowired
    private ITyszmxService tyszmxService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    IXxglService xxglService;
    @Autowired
    IJcsjService jcsjService;
    private final Logger log = LoggerFactory.getLogger(GeneralSettingController.class);

    /**
     * 通用设置列表 数据查询
     */
    @RequestMapping("/setting/pageGetListGeneralSetting")
    @ResponseBody
    public Map<String, Object> pageGetListGeneralSetting(TyszDto tyszDto,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        super.setCzdmList(request,map);
        super.setTyszList(request,map);
        List<TyszDto> list = tyszService.getPagedDtoList(tyszDto);
        map.put("total",tyszDto.getTotalNumber());
        map.put("rows",list);
        return map;
    }

    /**
     * 通用设置列表 查看
     */
    @RequestMapping("/setting/viewGeneralSetting")
    @ResponseBody
    public Map<String, Object> viewGeneralSetting(TyszDto tyszDto) {
        Map<String, Object> map = new HashMap<>();
        try{
            TyszDto tyszDto_t = tyszService.getDto(tyszDto);
            map.put("tyszDto",tyszDto_t);
            TyszmxDto tyszmxDto=new TyszmxDto();
            tyszmxDto.setTyszid(tyszDto.getTyszid());
            List<TyszmxDto> dtoList = tyszmxService.getDtoList(tyszmxDto);
            List<TyszmxDto> menuList=new ArrayList<>();
            List<TyszmxDto> buttonList=new ArrayList<>();
            if(dtoList!=null && !dtoList.isEmpty()){
                tyszmxDto=dtoList.get(0);
                List<TyszmxDto> tyszmxDtos=new ArrayList<>();
                for(TyszmxDto dto:dtoList){
                    if("1".equals(dto.getLx())){
                        buttonList.add(dto);
                    }else{
                        if(dto.getBtid().equals(tyszmxDto.getBtid())){
                            if("1".equals(dto.getSxlb())){
                                dto.setTyszmxDtos(redisUtil.hmgetDto("matridx_jcsj:" + dto.getNr()));
                                menuList.add(dto);
                            }else{
                                if("2".equals(dto.getZdlx())){
                                    menuList.add(dto);
                                }else{
                                    if(StringUtil.isBlank(dto.getQqlj())){
                                        tyszmxDtos.add(dto);
                                    }else{
                                        menuList.add(dto);
                                    }
                                }
                            }
                        }else{
                            if(!tyszmxDtos.isEmpty()){
                                TyszmxDto tyszmxDto_t= tyszmxDto.clone();
                                tyszmxDto_t.setTyszmxDtos(tyszmxDtos);
                                menuList.add(tyszmxDto_t);
                            }
                            tyszmxDto=dto;
                            tyszmxDtos=new ArrayList<>();
                            if("1".equals(dto.getSxlb())){
                                dto.setTyszmxDtos(redisUtil.hmgetDto("matridx_jcsj:" + dto.getNr()));
                                menuList.add(dto);
                            }else{
                                if("2".equals(dto.getZdlx())){
                                    menuList.add(dto);
                                }else{
                                    if(StringUtil.isBlank(dto.getQqlj())){
                                        tyszmxDtos.add(dto);
                                    }else{
                                        menuList.add(dto);
                                    }
                                }
                            }
                        }
                    }
                }
                if(!tyszmxDtos.isEmpty()){
                    TyszmxDto tyszmxDto_t= tyszmxDto.clone();
                    tyszmxDto_t.setTyszmxDtos(tyszmxDtos);
                    menuList.add(tyszmxDto_t);
                }
            }
            map.put("filterList",menuList);
            map.put("buttonList",buttonList);
            map.put("status", "success");
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("status", "fail");
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 通用设置列表 新增
     */
    @RequestMapping("/setting/addGeneralSetting")
    @ResponseBody
    public Map<String, Object> addGeneralSetting() {
        Map<String, Object> map = new HashMap<>();
        map.put("ztlxlist",redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.ACCOUNT_TYPE.getCode()));
        TyszDto tyszDto=new TyszDto();
        tyszDto.setCdcc("1");
        List<TyszDto> menuList = tyszService.getMenuList(tyszDto);
        map.put("menuList",menuList);
        List<Map<String, String>> jcsjModels = jcsjService.getJclbList();
        map.put("jclbList", jcsjModels);
        return map;
    }

    /**
     * 通用设置列表 获取基础数据
     */
    @RequestMapping("/setting/pagedataGetBasicData")
    @ResponseBody
    public Map<String, Object> pagedataGetBasicData(JcsjDto jcsjDto) {
        Map<String, Object> map = new HashMap<>();
        map.put("jcsjlist",redisUtil.hmgetDto("matridx_jcsj:"+ jcsjDto.getJclb()));
        return map;
    }

    /**
     * 通用设置列表 获取菜单
     */
    @RequestMapping("/setting/pagedataGetMenuList")
    @ResponseBody
    public Map<String, Object> pagedataGetMenuList(TyszDto tyszDto) {
        Map<String, Object> map = new HashMap<>();
        if(StringUtil.isNotBlank(tyszDto.getCdcc())){
            List<TyszDto> menuList = tyszService.getMenuList(tyszDto);
            map.put("menuList",menuList);
        }
        if(StringUtil.isNotBlank(tyszDto.getZyid())){
            List<TyszDto> buttonList = tyszService.getButtonList(tyszDto);
            map.put("buttonList",buttonList);
        }
        return map;
    }

    /**
     * 通用设置列表 新增保存
     */
    @RequestMapping("/setting/addSaveGeneralSetting")
    @ResponseBody
    public Map<String, Object> addSaveGeneralSetting(TyszDto tyszDto) {
        Map<String, Object> map = new HashMap<>();
        User user=getLoginInfo();
        tyszDto.setLrry(user.getYhid());
        TyszDto dto = tyszService.getDto(tyszDto);
        if(dto!=null){
            map.put("status", "fail");
            map.put("message", "三级菜单已存在!");
            return map;
        }
        boolean isSuccess = tyszService.addSaveGeneralSetting(tyszDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 通用设置列表 修改
     */
    @RequestMapping("/setting/modGeneralSetting")
    @ResponseBody
    public Map<String, Object> modGeneralSetting(TyszDto tyszDto) {
        Map<String, Object> map = this.viewGeneralSetting(tyszDto);
        map.put("ztlxlist",redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.ACCOUNT_TYPE.getCode()));
        TyszDto tyszDto_t=new TyszDto();
        tyszDto_t.setCdcc("1");
        List<TyszDto> menuList = tyszService.getMenuList(tyszDto_t);
        map.put("menuList",menuList);
        tyszDto_t.setCdcc("2");
        tyszDto_t.setFjd(((TyszDto) map.get("tyszDto")).getZyid());
        List<TyszDto> yj_menuList = tyszService.getMenuList(tyszDto_t);
        map.put("yj_menuList",yj_menuList);
        tyszDto_t.setCdcc("3");
        tyszDto_t.setFjd(((TyszDto) map.get("tyszDto")).getYjzyid());
        tyszDto_t.setFbsqz(((TyszDto) map.get("tyszDto")).getZtcskz2());
        List<TyszDto> ej_menuList = tyszService.getMenuList(tyszDto_t);
        map.put("ej_menuList",ej_menuList);
        List<Map<String, String>> jcsjModels = jcsjService.getJclbList();
        map.put("jclbList", jcsjModels);
        return map;
    }

    /**
     * 通用设置列表 修改保存
     */
    @RequestMapping("/setting/modSaveGeneralSetting")
    @ResponseBody
    public Map<String, Object> modSaveGeneralSetting(TyszDto tyszDto) {
        Map<String, Object> map = new HashMap<>();
        User user=getLoginInfo();
        tyszDto.setXgry(user.getYhid());
        tyszDto.setVerifyFlag("1");
        TyszDto dto = tyszService.getDto(tyszDto);
        if(dto!=null){
            map.put("status", "fail");
            map.put("message", "三级菜单已存在!");
            return map;
        }
        boolean isSuccess = tyszService.modSaveGeneralSetting(tyszDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 通用设置列表 删除
     */
    @RequestMapping("/setting/delGeneralSetting")
    @ResponseBody
    public Map<String, Object> delGeneralSetting(TyszDto tyszDto) {
        Map<String, Object> map = new HashMap<>();
        User user=getLoginInfo();
        tyszDto.setScry(user.getYhid());
        boolean isSuccess = tyszService.delGeneralSetting(tyszDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?(xxglService.getModelById("ICOM00003").getXxnr()):xxglService.getModelById("ICOM00004").getXxnr());
        return map;
    }

    @RequestMapping("/setting/pagedataGetChildZqqlj")
    @ResponseBody
    public Map<String, Object> pagedataGetChildZqqlj(TyszmxDto tyszmxDto){
        Map<String, Object> map = new HashMap<>();
        List<TyszmxDto>list=tyszmxService.getListByFnrid(tyszmxDto);
        map.put("list", list);
        return map;
    }
    @RequestMapping("/setting/pagedataGetCommonGjsx")
    @ResponseBody
    public Map<String, Object> pagedataGetCommonGjsx(TyszmxDto tyszmxDto){
        Map<String, Object> map = new HashMap<>();
        List<Map<String,String>> stringMap=tyszmxService.getCommonGjsx(tyszmxDto);
        map.put("list", stringMap);
        return map;
    }

    /**
     * 通用设置列表 获取子级数据
     */
    @RequestMapping("/setting/pagedataGetChildData")
    @ResponseBody
    public Map<String, Object> pagedataGetChildData(TyszmxDto tyszmxDto) {
        Map<String, Object> map = new HashMap<>();
        try {
            String fnrid = tyszmxDto.getFnrid();
            if("1".equals(tyszmxDto.getSxlb())){
                List<JcsjDto> jcsjDtos=new ArrayList<>();
                if(StringUtil.isNotBlank(fnrid)){
                    String[] fcsids = fnrid.split(",");
                    JcsjDto jcsjDto=new JcsjDto();
                    jcsjDto.setFcsids(fcsids);
                    jcsjDtos = jcsjService.getDtoList(jcsjDto);
                }
                List<TyszmxDto> childData=new ArrayList<>();
                if( jcsjDtos!=null && !jcsjDtos.isEmpty()){
                    Object hget = redisUtil.hget("GeneralSetting", tyszmxDto.getEjzyid());
                    List<TyszmxDto> tyszmxDtoList = JSON.parseArray(hget.toString(), TyszmxDto.class);
                    List<TyszmxDto> dtoList=new ArrayList<>();
                    //只保留子级的通用设置
                    if(!tyszmxDtoList.isEmpty()){
                        for(TyszmxDto dto:tyszmxDtoList){
                            if(StringUtil.isNotBlank(dto.getFnrid())){
                                dtoList.add(dto);
                            }
                        }
                    }
                    if(!dtoList.isEmpty()){
                        for(TyszmxDto dto:dtoList){
                            boolean isFind=false;
                            List<JcsjDto> jcsjDtoList=new ArrayList<>();
                            for(JcsjDto jcsjDto:jcsjDtos){
                                if(dto.getNr().equals(jcsjDto.getJclb())){
                                    jcsjDtoList.add(jcsjDto);
                                    isFind=true;
                                }
                            }
                            if(isFind){
                                dto.setTyszmxDtos(jcsjDtoList);
                                childData.add(dto);
                            }
                        }
                    }
                }
                map.put("childData",childData);
            }else{
                Object hget = redisUtil.hget("GeneralSetting", tyszmxDto.getEjzyid());
                List<TyszmxDto> tyszmxDtoList = JSON.parseArray(hget.toString(), TyszmxDto.class);
                List<TyszmxDto> dtoList=new ArrayList<>();
                //只保留子级的通用设置
                if(!tyszmxDtoList.isEmpty()){
                    for(TyszmxDto dto:tyszmxDtoList){
                        if(StringUtil.isNotBlank(dto.getFnrid())&& fnrid.contains(dto.getFnrid())){
                            dtoList.add(dto);
                        }
                    }
                }
                List<TyszmxDto> childData=new ArrayList<>();
                if(!dtoList.isEmpty()){
                    TyszmxDto tyszmxDto_x=dtoList.get(0);
                    List<TyszmxDto> tyszmxDtos=new ArrayList<>();
                    for(TyszmxDto dto:dtoList){
                        if(dto.getBtid().equals(tyszmxDto_x.getBtid())){
                            tyszmxDtos.add(dto);
                        }else{
                            if(!tyszmxDtos.isEmpty()){
                                TyszmxDto tyszmxDto_t= tyszmxDto_x.clone();
                                tyszmxDto_t.setTyszmxDtos(tyszmxDtos);
                                childData.add(tyszmxDto_t);
                            }
                            tyszmxDto_x=dto;
                            tyszmxDtos=new ArrayList<>();
                            tyszmxDtos.add(dto);
                        }
                    }
                    TyszmxDto tyszmxDto_t= tyszmxDto_x.clone();
                    tyszmxDto_t.setTyszmxDtos(tyszmxDtos);
                    childData.add(tyszmxDto_t);
                }
                map.put("childData",childData);
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return map;
    }
}
