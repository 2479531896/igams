package com.matridx.igams.common.controller;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.dao.entities.DbszDto;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.GgglDto;
import com.matridx.igams.common.dao.entities.GrbjDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.ShlbDto;
import com.matridx.igams.common.dao.entities.SyszDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.ZjglDto;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.svcinterface.IDbszService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IGgglService;
import com.matridx.igams.common.service.svcinterface.IGrbjService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.ISyszService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.service.svcinterface.IZjglService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author WYX
 * version 1.0
 * className HomePageController
 * description OA首页
 *
 **/
@RestController
@RequestMapping("/homePage")
public class HomePageController extends BaseController {
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Autowired
    ISyszService syszService;
    @Autowired
    IZjglService zjglService;
    @Autowired
    IXxglService xxglService;
    @Autowired
    IDbszService dbszService;
    @Autowired
    IGgglService ggglService;
    @Autowired
    IFjcfbService fjcfbService;
    @Autowired
    IGrbjService grbjService;
    @Autowired
    IShgcService shgcService;
    @Override
    public String getPrefix(){
        return urlPrefix;
    }
    /**
     * 获取首页页面及数据
     */
    @RequestMapping("/homePage/getHomePage")
    public Map<String,Object> getHomePage(SyszDto syszDto, HttpServletRequest request){
        User user = getLoginInfo(request);
        Map<String,Object> map = new HashMap<>();
        syszDto.setYhid(user.getYhid());
        List<SyszDto> syszDtos = syszService.getHomePage(syszDto);
        syszService.getHomePageData(syszDtos,map,syszDto);
        map.put("syszDtos",syszDtos);
        return map;
    }
    /**
     * 获取首页设置
     */
    @RequestMapping("/homePage/homePageSetting")
    public Map<String,Object> homePageSetting(SyszDto syszDto, HttpServletRequest request){
        User user = getLoginInfo(request);
        Map<String,Object> map = new HashMap<>();
        syszDto.setYhid(user.getYhid());
        List<SyszDto> syszDtos = syszService.getHomePage(syszDto);
        syszDto.setYhid("default");
        List<SyszDto> defaults = syszService.getHomePage(syszDto);
        List<ZjglDto> zjglDtos = zjglService.getAllComponent();
        List<ZjglDto> defaultZjglDtos = zjglService.getAllComponent();
        //去除已有的
        for (SyszDto dto : syszDtos) {
            zjglDtos.removeIf(e->e.getZjid().equals(dto.getZjid()));
        }
        //去除已有的
        for (SyszDto dto : defaults) {
            defaultZjglDtos.removeIf(e->e.getZjid().equals(dto.getZjid()));
        }
        map.put("zjglDtos",zjglDtos);
        map.put("syszDtos",syszDtos);
        map.put("defaults",defaults);
        map.put("defaultZjglDtos",defaultZjglDtos);
        return map;
    }

    /**
     * 保存首页设置
     */
    @RequestMapping("/homePage/homePageSaveSetting")
    public Map<String,Object> homePageSaveSetting(SyszDto syszDto, HttpServletRequest request){
        User user = getLoginInfo(request);
        syszDto.setYhid(user.getYhid());
        Map<String,Object> map = new HashMap<>();
        boolean isSuccess;
        try {
            isSuccess = syszService.homePageSaveSetting(syszDto);
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
     * 获取审核代办设置
     */
    @RequestMapping("/homePage/auditTaskWaitingSetting")
    public Map<String,Object> auditTaskWaitingSetting(DbszDto dbszDto, HttpServletRequest request){
        User user = getLoginInfo(request);
        dbszDto.setYhid(user.getYhid());
        return dbszService.auditTaskWaitingSetting(dbszDto);
    }
    /**
     * 保存审核代办设置
     */
    @RequestMapping("/homePage/auditTaskWaitingSaveSetting")
    public Map<String,Object> auditTaskWaitingSaveSetting(DbszDto dbszDto, HttpServletRequest request){
        User user = getLoginInfo(request);
        dbszDto.setYhid(user.getYhid());
        Map<String,Object> map = new HashMap<>();
        boolean isSuccess;
        try {
            isSuccess = dbszService.auditTaskWaitingSaveSetting(dbszDto);
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
     * 获取审核代办列表
     */
    @RequestMapping("/homePage/pagedataListAuditTaskWaiting")
    public Map<String,Object> pagedataListAuditTaskWaiting(ShgcDto shgcDto,HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        User loginInfo = getLoginInfo(request);
        shgcDto.setYhid(loginInfo.getYhid());
        List<ShgcDto> rows = shgcService.getPagedAuditTaskWaitingList(shgcDto);
        map.put("total", shgcDto.getTotalNumber());
        map.put("rows", rows);
        return map;
    }
    /**
     * 获取公告列表
     */
    @RequestMapping("/homePage/pageGetListNotice")
    public Map<String,Object> pageGetListNotice(GgglDto ggglDto,HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        List<GgglDto> ggglDtos = ggglService.getPagedDtoList(ggglDto);
        super.setCzdmList(request,map);
        super.setTyszList(request,map);
        map.put("total", ggglDto.getTotalNumber());
        map.put("rows", ggglDtos);
        return map;
    }
    /**
     * 公告新增
     */
    @RequestMapping("/homePage/addNotice")
    public Map<String,Object> addNotice(GgglDto ggglDto, HttpServletRequest request){
        User user = getLoginInfo(request);
        Map<String, Object> map = new HashMap<>();
        ggglDto.setFbr(user.getYhid());
        ggglDto.setFbrmc(user.getZsxm());
        ggglDto.setRq(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd"));
        map.put("ggglDto",ggglDto);
        map.put("ywlx", BusTypeEnum.IMP_NOTICE.getCode());
        return map;
    }
    /**
     * 公告新增
     */
    @RequestMapping("/homePage/addSaveNotice")
    public Map<String,Object> addSaveNotice(GgglDto ggglDto, HttpServletRequest request){
        User user = getLoginInfo(request);
        ggglDto.setLrry(user.getYhid());
        Map<String,Object> map = new HashMap<>();
        boolean isSuccess;
        try {
            isSuccess = ggglService.addSaveNotice(ggglDto);
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
     * 公告修改
     */
    @RequestMapping("/homePage/modNotice")
    public Map<String,Object> modNotice(GgglDto ggglDto){
        Map<String, Object> map = new HashMap<>();
        ggglDto = ggglService.getDtoById(ggglDto.getGgid());
        FjcfbDto fjcfbDto=new FjcfbDto();
        fjcfbDto.setYwid(ggglDto.getGgid());
        fjcfbDto.setYwlx(BusTypeEnum.IMP_NOTICE.getCode());
        List<FjcfbDto> fjcfbDtos = fjcfbService.getDtoList(fjcfbDto);
        map.put("fjcfbDtos",fjcfbDtos);
        map.put("ggglDto",ggglDto);
        map.put("ywlx", BusTypeEnum.IMP_NOTICE.getCode());
        return map;
    }
    /**
     * 公告修改保存
     */
    @RequestMapping("/homePage/modSaveNotice")
    public Map<String,Object> modSaveNotice(GgglDto ggglDto, HttpServletRequest request){
        User user = getLoginInfo(request);
        ggglDto.setXgry(user.getYhid());
        Map<String,Object> map = new HashMap<>();
        boolean isSuccess;
        try {
            isSuccess = ggglService.modSaveNotice(ggglDto);
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
     * 公告查看
     */
    @RequestMapping("/homePage/viewNotice")
    public Map<String,Object> viewNotice(GgglDto ggglDto){
        Map<String, Object> map = new HashMap<>();
        ggglDto = ggglService.getDtoById(ggglDto.getGgid());
        FjcfbDto fjcfbDto=new FjcfbDto();
        fjcfbDto.setYwid(ggglDto.getGgid());
        fjcfbDto.setYwlx(BusTypeEnum.IMP_NOTICE.getCode());
        List<FjcfbDto> fjcfbDtos = fjcfbService.getDtoList(fjcfbDto);
        map.put("fjcfbDtos",fjcfbDtos);
        map.put("ggglDto",ggglDto);
        map.put("ywlx", BusTypeEnum.IMP_NOTICE.getCode());
        return map;
    }
    /**
     * 公告删除
     */
    @RequestMapping("/homePage/delNotice")
    public Map<String,Object> delNotice(GgglDto ggglDto, HttpServletRequest request){
        User user = getLoginInfo(request);
        ggglDto.setScry(user.getYhid());
        Map<String,Object> map = new HashMap<>();
        boolean isSuccess = ggglService.delete(ggglDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00003").getXxnr() : xxglService.getModelById("ICOM00004").getXxnr());
        return map;
    }
    /**
     * 笔记新增
     */
    @RequestMapping("/homePage/addSaveNote")
    public Map<String,Object> addSaveNote(GrbjDto grbjDto, HttpServletRequest request){
        User user = getLoginInfo(request);
        grbjDto.setYhid(user.getYhid());
        grbjDto.setGrbjid(StringUtil.generateUUID());
        Map<String,Object> map = new HashMap<>();
        boolean isSuccess = grbjService.insert(grbjDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }
    /**
     * 笔记修改 查看
     */
    @RequestMapping("/homePage/modNote")
    public Map<String,Object> modNote(GrbjDto grbjDto){
        Map<String,Object> map = new HashMap<>();
        if (StringUtil.isNotBlank(grbjDto.getGrbjid())){
            grbjDto = grbjService.getDtoById(grbjDto.getGrbjid());
        }
        map.put("grbjDto",grbjDto);
        return map;
    }
    /**
     * 笔记新增
     */
    @RequestMapping("/homePage/modSaveNote")
    public Map<String,Object> modSaveNote(GrbjDto grbjDto){
        Map<String,Object> map = new HashMap<>();
        boolean isSuccess = grbjService.update(grbjDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }
    /**
     * 个人笔记删除
     */
    @RequestMapping("/homePage/delNote")
    public Map<String,Object> delNote(GrbjDto grbjDto){
        Map<String,Object> map = new HashMap<>();
        boolean isSuccess = grbjService.delete(grbjDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00003").getXxnr() : xxglService.getModelById("ICOM00004").getXxnr());
        return map;
    }
    /**
     * 获取个人笔记
     */
    @RequestMapping("/homePage/getNoteList")
    public Map<String,Object> getNoteList(GrbjDto grbjDto,HttpServletRequest request){
        grbjDto.setYhid(getLoginInfo(request).getYhid());
        Map<String,Object> map = new HashMap<>();
        List<GrbjDto> grbjDtos = grbjService.getPagedDtoList(grbjDto);
        map.put("rows",grbjDtos);
        map.put("total", grbjDto.getTotalNumber());
        return map;
    }
    /**
     * 获取日程
     */
    @RequestMapping("/homePage/getScheduleByRq")
    public Map<String,Object> getScheduleByRq(GrbjDto grbjDto,HttpServletRequest request){
        grbjDto.setYhid(getLoginInfo(request).getYhid());
        Map<String,Object> map = new HashMap<>();
        List<GrbjDto> grbjDtos = grbjService.getDtoList(grbjDto);
        map.put("grbjDtos",grbjDtos);
        return map;
    }
    /**
     * 获取个人任务统计(工作管理)
     */
    @RequestMapping("/homePage/pagedataGetHomePageTaskStatis")
    public Map<String,Object> getHomePageTaskStatis(HttpServletRequest request){
        SyszDto syszDto = JSON.parseObject(request.getParameter("syszDto"), SyszDto.class);
        return syszService.getHomePageTaskStatis(syszDto);
    }
    /**
     * 获取审核任务代办
     */
    @RequestMapping("/homePage/pagedataGetHomePageAuditTaskWaiting")
    public Map<String,Object> pagedataGetHomePageAuditTaskWaiting(HttpServletRequest request){
        SyszDto syszDto = JSON.parseObject(request.getParameter("syszDto"), SyszDto.class);
        List<DbszDto> dbszDtos = JSON.parseArray(request.getParameter("dbszDtos"), DbszDto.class);
        return syszService.getHomePageAuditTaskWaiting(syszDto,dbszDtos);
    }
    /**
     * 获取所有审核类别
     */
    @RequestMapping("/homePage/pagedataGetAllAuditType")
    public List<ShlbDto> pagedataGetAllAuditType(){
        return dbszService.getAllAuditType();
    }

    @RequestMapping("/homePage/pagedataGetHtml")
    public Map<String,Object> pagedataGetHtml(ZjglDto zjglDto, HttpServletRequest request) {
        Map<String,Object> map = new HashMap<>();
        if(StringUtil.isNotBlank(zjglDto.getZylj())){
            try {
                Path path = Paths.get(zjglDto.getZylj());
                map.put("status","success");
                map.put("dataString",Files.readString(path, StandardCharsets.UTF_8));
            } catch (IOException e) {
                map.put("status","fail");
                map.put("message","文件读取失败!");
            }
        }else {
            map.put("status","fail");
            map.put("message","文件路径为空!");
        }
        return map;
    }

    @RequestMapping("/homePage/pagedataGetToolBox")
    public Map<String,Object> pagedataGetToolBox(ZjglDto zjglDto) {
        Map<String,Object> map = new HashMap<>();
        zjglDto.setIds("external,tool");
        List<ZjglDto> zjglDtos = zjglService.getDtoList(zjglDto);
        map.put("data",zjglDtos);
        return map;
    }
}
