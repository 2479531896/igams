package com.matridx.igams.hrm.controller;

import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.ShlcDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XtshDto;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.*;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IShlcService;
import com.matridx.igams.common.service.svcinterface.IXtshService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.hrm.dao.entities.*;
import com.matridx.igams.hrm.service.svcinterface.*;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/performanceHomePage")
public class PerformanceManagementHomePageController extends BaseController {

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    private IXxglService xxglService;
    @Autowired
    ICsszService csszService;
    @Autowired
    IMbszService mbszService;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Autowired
    IJxmbService jxmbService;
    @Autowired
    IGrjxService grjxService;
    @Autowired
    IJxmbmxService jxmbmxService;
    @Autowired
    IGrjxmxService grjxmxService;
    @Autowired
    IQzszService qzszService;
    @Autowired
    IXtshService xtshService;
    @Autowired
    IYghmcService yghmcService;
    @Autowired
    ISyfwService syfwService;
    @Autowired
    IShlcService shlcService;
    @Autowired
    IShgcService shgcService;
    @Autowired
    IJxtxService jxtxService;
    @Autowired
    IJxtxmxService jxtxmxService;
    @Override
    public String getPrefix(){
        return urlPrefix;
    }


    /**
     * 模板列表修改保存
     * @param jxmbDto
     * @return
     */
    @RequestMapping("/template/modSaveTemplate")
    @ResponseBody
    public Map<String,Object> modSaveTemplate(JxmbDto jxmbDto,HttpServletRequest request){
        User user=getLoginInfo(request);
        jxmbDto.setXgry(user.getYhid());
        jxmbDto.setBm(user.getJgid());
        Map<String,Object> result = new HashMap<>();
        try {
            boolean isSuccess=jxmbService.modSaveTemplate(jxmbDto);
            result.put("status",isSuccess?"success":"fail");
            result.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
            result.put("auditType", AuditTypeEnum.AUDIT_PERFORMANCE_TEMPLATE.getCode());
            result.put("jxmbid", jxmbDto.getJxmbid());
            result.put("mbshid", jxmbDto.getMbshid());
            result.put("sftj","1".equals(jxmbDto.getLx()));
        }catch (Exception e){
            result.put("status","fail");
            result.put("message",e.getMessage());
        }
        return result;
    }

    /**
     * 模板列表新增选择模板类型
     * @return
     */
    @PostMapping("/template/addTemplate")
    @ResponseBody
    public Map<String,Object> addTemplate(){
        Map<String,Object> result = new HashMap<>();
        CsszDto csszDto=new CsszDto();
        csszDto.setKhlxjclb(BasicDataTypeEnum.ASSESSMENT_TYPE.getCode());
        csszDto.setSfsz("1");
        List<CsszDto> csszDtos=csszService.getDtoList(csszDto);
        result.put("csszDtos",csszDtos);
        return result;
    }
    /**
     * 模板列表新增保存
     * @param jxmbDto
     * @return
     */
    @PostMapping("/template/addSaveTemplate")
    @ResponseBody
    public Map<String,Object> addSaveTemplate(JxmbDto jxmbDto,HttpServletRequest request) {
        User user=getLoginInfo(request);
        jxmbDto.setLx("1");
        jxmbDto.setBm(user.getJgid());
        jxmbDto.setLrry(user.getYhid());
        Map<String,Object> result = new HashMap<>();
        try {
            boolean isSuccess=jxmbService.addSaveTemplate(jxmbDto);
            result.put("jxmbid",jxmbDto.getJxmbid());
            result.put("status",isSuccess?"success":"fail");
            result.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
            result.put("auditType", AuditTypeEnum.AUDIT_PERFORMANCE_TEMPLATE.getCode());
            result.put("mbshid",jxmbDto.getMbshid());
            result.put("sftj","1".equals(jxmbDto.getLx()));
        }catch (Exception e){
            result.put("status","fail");
            result.put("message",e.getMessage());
        }
        return result;
    }
    /**
     * 绩效模板导入
     * @param syfwDto
     * @return
     */
    @RequestMapping(value = "/template/importTemplate",method =RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> importTemplate(SyfwDto syfwDto, HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        User user=getLoginInfo(request);
        syfwDto.setYhid(user.getYhid());
        List<SyfwDto> syfwDtoList=syfwService.getPagedDtoList(syfwDto);
        result.put("rows",syfwDtoList);
        result.put("total", syfwDto.getTotalNumber());
        return result;
    }

    /**
     * 模板审核列表
     * @param jxmbDto
     * @return
     */
    @RequestMapping(value = "/template/pageGetListTemplateAudit",method =RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> pageGetListTemplateAudit(JxmbDto jxmbDto,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        // 附加委托参数
        DataPermission.addWtParam(jxmbDto);
        // 附加审核状态过滤
        if (GlobalString.AUDIT_SHZT_YSH.equals(jxmbDto.getDqshzt())) {
            DataPermission.add(jxmbDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "jxmb", "jxmbid", AuditTypeEnum.AUDIT_PERFORMANCE_TEMPLATE);
        } else {
            DataPermission.add(jxmbDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "jxmb", "jxmbid", AuditTypeEnum.AUDIT_PERFORMANCE_TEMPLATE);
        }
        DataPermission.addCurrentUser(jxmbDto, getLoginInfo(request));
        DataPermission.addSpDdw(jxmbDto, "jxmb", SsdwTableEnum.JXMB);
        List<JxmbDto> mbAuditList = jxmbService.getAuditListData(jxmbDto);
        map.put("total", jxmbDto.getTotalNumber());
        map.put("rows", mbAuditList);
        super.setCzdmList(request,map);
        return map;
    }

    /**
     * 模板列表
     * @param jxmbDto
     * @return
     */
    @RequestMapping(value = "/template/pageGetListTemplate",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> pageGetListTemplate(JxmbDto jxmbDto, HttpServletRequest request){
        User user=getLoginInfo(request);
        jxmbDto.setJsid(user.getDqjs());
        if (StringUtil.isNotBlank(jxmbDto.getFlag())){
            if ("gr".equals(jxmbDto.getFlag())){
                jxmbDto.setRyid(user.getYhid());
            }
        }
        List<JxmbDto> list = new ArrayList<>();
        if("bm".equals(jxmbDto.getFlag())){
            List<String> ryids=new ArrayList<>();
            if("0".equals(jxmbDto.getSxlx())&&StringUtil.isNotBlank(user.getDdid())){
                YghmcDto yghmcDto = new YghmcDto();
                yghmcDto.setZszg(user.getDdid());
                List<YghmcDto> xjygDtos = yghmcService.getSubordinateEmployee(yghmcDto);
                if(!CollectionUtils.isEmpty(xjygDtos)){
                    for(YghmcDto dto:xjygDtos){
                        ryids.add(dto.getYhid());
                    }
                }
            }else if("1".equals(jxmbDto.getSxlx())&&StringUtil.isNotBlank(user.getDdid())){
                List<YghmcDto> allList = yghmcService.getAllList();
                List<String> ddids=new ArrayList<>();
                ddids.add(user.getDdid());
                yghmcService.recursiveGetInfo(allList,ddids,Integer.parseInt(jxmbDto.getCj()),ryids);
            }
            if(!CollectionUtils.isEmpty(ryids)){
                jxmbDto.setRyids(ryids);
                list = jxmbService.getPagedDtoList(jxmbDto);
            }
        }else{
            list = jxmbService.getPagedDtoList(jxmbDto);
        }
        Map<String,Object> result = new HashMap<>();
        super.setCzdmList(request,result);
        result.put("khlxs", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.ASSESSMENT_TYPE.getCode()));
        result.put("khzqs", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.ASSESSMENT_PERIOD.getCode()));
        XtshDto xtshDto=new XtshDto();
        xtshDto.setShlb(AuditTypeEnum.AUDIT_PERFORMANCE_TEMPLATE.getCode());
        List<XtshDto> xtshDtos=xtshService.getDtoList(xtshDto);
        List<JxmbDto> bmlist=jxmbService.getAllMbJgxx(jxmbDto);
        result.put("bmlist",bmlist);
        result.put("total", jxmbDto.getTotalNumber());
        result.put("rows", list);
        result.put("urlPrefix",urlPrefix);
        result.put("auditType", AuditTypeEnum.AUDIT_PERFORMANCE_TEMPLATE.getCode());
        result.put("splxs",xtshDtos);
        result.put("jxmbDto",jxmbDto);
        return result;
    }

    /**
     * 模板列表删除
     * @param jxmbDto
     * @return
     */
    @PostMapping("/template/delTemplate")
    @ResponseBody
    public Map<String,Object> delTemplate(JxmbDto jxmbDto,HttpServletRequest request) {
        User user=getLoginInfo(request);
        jxmbDto.setScry(user.getYhid());
        Map<String,Object> result = new HashMap<>();
        try {
            boolean isSuccess=jxmbService.delete(jxmbDto);
            result.put("status",isSuccess?"success":"fail");
            result.put("message",isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
        }catch (Exception e){
            result.put("status","fail");
            result.put("message",e.getMessage());
        }
        return result;
    }

    /**
     * 模板列表查看
     * @param jxmbDto
     * @return
     */
    @PostMapping("/template/viewTemplate")
    @ResponseBody
    public Map<String,Object> viewTemplate(JxmbDto jxmbDto){
        Map<String,Object> result = new HashMap<>();
        jxmbDto = jxmbService.getDtoById(jxmbDto.getJxmbid());
        JxmbmxDto jxmbmxDto=new JxmbmxDto();
        jxmbmxDto.setJxmbid(jxmbDto.getJxmbid());
        List<JxmbmxDto> jxmbmxDtoList=jxmbmxService.getJxmbmxList(jxmbmxDto);
        MbszDto mbszDto=mbszService.getDtoById(jxmbDto.getJxmbid());
        SyfwDto syfwDto=new SyfwDto();
        syfwDto.setJxmbid(jxmbDto.getJxmbid());
        List<SyfwDto> syfwDtoList=syfwService.getDtoList(syfwDto);
        jxmbDto.setList(jxmbmxDtoList);
        QzszDto qzszDto = new QzszDto();
        qzszDto.setJxmbid(jxmbDto.getJxmbid());
        qzszDto.setMbszid(jxmbDto.getMbszid());
        List<QzszDto> qzszDtos = qzszService.getDtoList(qzszDto);
        ShlcDto shlcDto = new ShlcDto();
        shlcDto.setShid(mbszDto.getMbshid());
        List<ShlcDto> mbshlist = shlcService.getDtoListById(shlcDto);
        result.put("jxmbDto", jxmbDto);
        result.put("mbszDto",mbszDto);
        result.put("syfwDtoList",syfwDtoList);
        result.put("qzszDtos",qzszDtos);
        result.put("mbshlist",mbshlist);
        return result;
    }

    /**
     * 模板列表新增修改页面所需数据
     * @param jxmbDto
     * @return
     */
    @PostMapping("/template/modTemplate")
    @ResponseBody
    public Map<String,Object> modTemplate(JxmbDto jxmbDto,JxmbmxDto jxmbmxDto){
        Map<String,Object> result = new HashMap<>();
        jxmbDto=jxmbService.getDtoById(jxmbDto.getJxmbid());
        List<JxmbmxDto> jxmbmxDtoList=jxmbmxService.getJxmbmxList(jxmbmxDto);
        jxmbDto.setList(jxmbmxDtoList);
        result.put("jxmbDto",jxmbDto);
        return result;
    }

    /**
     * 个人绩效修改页面数据
     * @param grjxDto
     * @return Map
     */
    @RequestMapping(value = "/performance/submitPerformance",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> submitPerformance(GrjxDto grjxDto){
        Map<String,Object> map = new HashMap<>();
        GrjxDto dtoById = grjxService.getDtoById(grjxDto.getGrjxid());
        if (dtoById==null){
            map.put("status", "fail");
            map.put("message", "未找到对应绩效信息");
            return map;
        }
        GrjxmxDto grjxmxDto = new GrjxmxDto();
        grjxmxDto.setGrjxid(dtoById.getGrjxid());
        List<GrjxmxDto> dtoList =   grjxmxService.getDtoListWithNull(grjxmxDto);
        List<GrjxmxDto> newDtoList = grjxmxService.getNewDtoList(dtoList);
        dtoById.setQz(newDtoList.get(0).getQz());
        map.put("grjxmxDtos",newDtoList);
        map.put("grjxDto",dtoById);
        return map;
    }

    /**
     * 个人绩效提交保存
     * @param grjxDto
     * @return Map
     */
    @RequestMapping(value = "/performance/submitSavePerformance",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> submitSavePerformance(GrjxDto grjxDto,HttpServletRequest request){
        User user = getLoginInfo(request);
        Map<String,Object> map = new HashMap<>();
        grjxDto.setXgry(user.getYhid());
        boolean isSuccess;
        try {
            isSuccess = grjxService.modSavePerformance(grjxDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
            map.put("urlPrefix",urlPrefix);
            map.put("auditType", AuditTypeEnum.AUDIT_PERFORMANCE.getCode());
            map.put("jxshid",grjxDto.getJxshid());
            map.put("ywid",grjxDto.getGrjxid());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
            map.put("urlPrefix",urlPrefix);
        }catch (Exception e) {
            map.put("status", "fail");
            map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
            map.put("urlPrefix",urlPrefix);
        }
        return map;
    }

    /**
     * 个人绩效审核页面数据
     * @param grjxDto
     * @return Map
     */
    @RequestMapping(value = "/performance/auditPerformance",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> auditPerformance(GrjxDto grjxDto,HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        GrjxDto dtoById = grjxService.getDtoById(grjxDto.getGrjxid());
        if (dtoById==null){
            map.put("status", "fail");
            map.put("message", "未找到对应绩效信息");
            return map;
        }
        List<List<GrjxmxDto>> lists = new ArrayList<>();
        GrjxmxDto grjxmxDto = new GrjxmxDto();
        grjxmxDto.setGrjxid(dtoById.getGrjxid());
        List<GrjxmxDto> grjxmxDtos = grjxmxService.getDtoListWithScore(grjxmxDto);
        if ("1".equals(dtoById.getZp())){
            List<GrjxmxDto> dtoList = new ArrayList<>();
            for (GrjxmxDto dto : grjxmxDtos) {
                if (StringUtil.isBlank(dto.getGwid())){
                    dtoList.add(dto);
                }
            }
            List<GrjxmxDto> newDtoList = grjxmxService.getNewDtoList(dtoList);
            lists.add(newDtoList);
            dtoById.setQz(newDtoList.get(0).getQz());
        }
        ShgcDto dtoByYwid = shgcService.getDtoByYwid(dtoById.getGrjxid());
        ShlcDto shlcDto = new ShlcDto();
        shlcDto.setShid(dtoById.getJxshid());
        List<ShlcDto> dtoListById = shlcService.getDtoListById(shlcDto);
        if (!CollectionUtils.isEmpty(dtoListById)){
            for (ShlcDto dto : dtoListById) {
                //当前审核岗位放置list第一个
                if (dtoByYwid!=null&&dtoByYwid.getXlcxh().equals(dto.getLcxh())){
                    List<GrjxmxDto> dtoList = new ArrayList<>();
                    dtoById.setGwid(dto.getGwid());
                    for (GrjxmxDto dto_t : grjxmxDtos) {
                        if (dto.getGwid().equals(dto_t.getGwid())){
                            dtoList.add(dto_t);
                        }
                    }
                    List<GrjxmxDto> newDtoList = grjxmxService.getNewDtoList(dtoList);
                    lists.add(newDtoList);
                    break;
                }
            }
            //非当前审核岗位list
            for (ShlcDto dto : dtoListById) {
                if (dtoByYwid!=null&&!dtoByYwid.getXlcxh().equals(dto.getLcxh())&&Integer.parseInt(dtoByYwid.getXlcxh()) > Integer.parseInt(dto.getLcxh())) {
                    List<GrjxmxDto> dtoList = new ArrayList<>();
                    for (GrjxmxDto dto_t : grjxmxDtos) {
                        if (dto.getGwid().equals(dto_t.getGwid())) {
                            dtoList.add(dto_t);
                        }
                    }
                    List<GrjxmxDto> newDtoList = grjxmxService.getNewDtoList(dtoList);
                    if (!CollectionUtils.isEmpty(newDtoList)){
                        lists.add(newDtoList);
                    }
                }
            }
        }
        map.put("lists",lists);
        map.put("grjxDto",dtoById);
        return map;
    }

    /**
     * 个人绩效查看页面数据
     * @param grjxDto
     * @return Map
     */
    @RequestMapping(value = "/performance/viewPerformance",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> viewPerformance(GrjxDto grjxDto,HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        GrjxDto dtoById = grjxService.getDtoById(grjxDto.getGrjxid());
        if (dtoById==null){
            map.put("status", "fail");
            map.put("message", "未找到对应绩效信息");
            return map;
        }
        List<List<GrjxmxDto>> lists = new ArrayList<>();
        GrjxmxDto grjxmxDto = new GrjxmxDto();
        grjxmxDto.setGrjxid(dtoById.getGrjxid());
        List<GrjxmxDto> grjxmxDtos = grjxmxService.getDtoListWithScore(grjxmxDto);
        if ("1".equals(dtoById.getZp())){
            List<GrjxmxDto> dtoList = new ArrayList<>();
            for (GrjxmxDto dto : grjxmxDtos) {
                if (StringUtil.isBlank(dto.getGwid())){
                    dtoList.add(dto);
                }
            }
            List<GrjxmxDto> newDtoList = grjxmxService.getNewDtoList(dtoList);
            lists.add(newDtoList);
            dtoById.setQz(newDtoList.get(0).getQz());
        }
        if (!StatusEnum.CHECK_NO.getCode().equals(dtoById.getZt())){
            ShlcDto shlcDto = new ShlcDto();
            shlcDto.setShid(dtoById.getJxshid());
            List<ShlcDto> dtoListById = shlcService.getDtoListById(shlcDto);
            if (!CollectionUtils.isEmpty(dtoListById)){
                for (ShlcDto dto : dtoListById) {
                    List<GrjxmxDto> dtoList = new ArrayList<>();
                    for (GrjxmxDto dto_t : grjxmxDtos) {
                        if (dto.getGwid().equals(dto_t.getGwid())){
                            dtoList.add(dto_t);
                        }
                    }
                    List<GrjxmxDto> newDtoList = grjxmxService.getNewDtoList(dtoList);
                    if (!CollectionUtils.isEmpty(newDtoList)){
                        lists.add(newDtoList);
                    }
                }
            }
        }
        map.put("lists",lists);
        map.put("grjxDto",dtoById);
        return map;
    }

    /**
     * 个人绩效列表
     * @param grjxDto
     * @return
     */
    @GetMapping("/performance/pageGetListPerformance")
    @ResponseBody
    public Map<String,Object> pageGetListPerformance(GrjxDto grjxDto,HttpServletRequest request) {
        Map<String,Object> result = new HashMap<>();
        User user=getLoginInfo(request);
        if (StringUtil.isNotBlank(grjxDto.getFlag())){
            if("gr".equals(grjxDto.getFlag())){
                grjxDto.setRyid(user.getYhid());
            }
        }
        super.setCzdmList(request,result);
        List<String> nfs=grjxService.getGrjxYears();
        List<Map<String,String>> bms=grjxService.getGrjxBms();
        List<GrjxDto> list = new ArrayList<>();
        if("bm".equals(grjxDto.getFlag())){
            List<String> ryids=new ArrayList<>();
            if("0".equals(grjxDto.getSxlx())&&StringUtil.isNotBlank(user.getDdid())){
                YghmcDto yghmcDto = new YghmcDto();
                yghmcDto.setZszg(user.getDdid());
                List<YghmcDto> xjygDtos = yghmcService.getSubordinateEmployee(yghmcDto);
                if(!CollectionUtils.isEmpty(xjygDtos)){
                    for(YghmcDto dto:xjygDtos){
                        ryids.add(dto.getYhid());
                    }
                }
            }else if("1".equals(grjxDto.getSxlx())&&StringUtil.isNotBlank(user.getDdid())){
                List<YghmcDto> allList = yghmcService.getAllList();
                List<String> ddids=new ArrayList<>();
                ddids.add(user.getDdid());
                yghmcService.recursiveGetInfo(allList,ddids,Integer.parseInt(grjxDto.getCj()),ryids);
            }
            if(!CollectionUtils.isEmpty(ryids)){
                grjxDto.setRyids(ryids);
                list = grjxService.getPagedDtoList(grjxDto);
            }
        }else{
            list = grjxService.getPagedDtoList(grjxDto);
        }
        result.put("rows",list);
        result.put("total",grjxDto.getTotalNumber());
        result.put("urlPrefix",urlPrefix);
        result.put("auditType", AuditTypeEnum.AUDIT_PERFORMANCE.getCode());
        result.put("khlxs", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.ASSESSMENT_TYPE.getCode()));
        result.put("khzqs", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.ASSESSMENT_PERIOD.getCode()));
        result.put("mbjbs", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.TEMPLATE_LEVEL.getCode()));
        result.put("nfs",nfs);
        result.put("bmlist",bms);
        result.put("grjxDto",grjxDto);
        return result;
    }

    /**
     * 绩效审核列表
     * @param grjxDto
     * @return
     */
    @RequestMapping(value = "/template/pageGetListPerformanceAudit",method =RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> pageGetListPerformanceAudit(GrjxDto grjxDto,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        // 附加委托参数
        DataPermission.addWtParam(grjxDto);
        // 附加审核状态过滤
        if (GlobalString.AUDIT_SHZT_YSH.equals(grjxDto.getDqshzt())) {
            DataPermission.add(grjxDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "grjx", "grjxid", AuditTypeEnum.AUDIT_PERFORMANCE);
        } else {
            DataPermission.add(grjxDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "grjx", "grjxid", AuditTypeEnum.AUDIT_PERFORMANCE);
        }
        DataPermission.addCurrentUser(grjxDto, getLoginInfo(request));
        DataPermission.addSpDdw(grjxDto, "grjx", SsdwTableEnum.GRJX);
        List<GrjxDto> jxAuditList = grjxService.getAuditListData(grjxDto);
        List<String> nfs=grjxService.getGrjxYears();
        List<Map<String,String>> bms=grjxService.getGrjxBms();
        map.put("total", grjxDto.getTotalNumber());
        map.put("rows", jxAuditList);
        map.put("khlxs", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.ASSESSMENT_TYPE.getCode()));
        map.put("khzqs", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.ASSESSMENT_PERIOD.getCode()));
        map.put("nfs",nfs);
        map.put("bmlist",bms);
        super.setCzdmList(request,map);
        return map;
    }
}
