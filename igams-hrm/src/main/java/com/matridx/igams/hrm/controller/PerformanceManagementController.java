package com.matridx.igams.hrm.controller;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.ShlcDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XtshDto;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.DataPermissionTypeEnum;
import com.matridx.igams.common.enums.SsdwTableEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IShlcService;
import com.matridx.igams.common.service.svcinterface.IXtshService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.hrm.dao.entities.CsszDto;
import com.matridx.igams.hrm.dao.entities.GrjxDto;
import com.matridx.igams.hrm.dao.entities.GrjxmxDto;
import com.matridx.igams.hrm.dao.entities.JxmbDto;
import com.matridx.igams.hrm.dao.entities.JxmbmxDto;
import com.matridx.igams.hrm.dao.entities.JxtxDto;
import com.matridx.igams.hrm.dao.entities.JxtxmxDto;
import com.matridx.igams.hrm.dao.entities.MbszDto;
import com.matridx.igams.hrm.dao.entities.QzszDto;
import com.matridx.igams.hrm.dao.entities.SyfwDto;
import com.matridx.igams.hrm.dao.entities.YghmcDto;
import com.matridx.igams.hrm.service.svcinterface.ICsszService;
import com.matridx.igams.hrm.service.svcinterface.IGrjxService;
import com.matridx.igams.hrm.service.svcinterface.IGrjxmxService;
import com.matridx.igams.hrm.service.svcinterface.IJxmbService;
import com.matridx.igams.hrm.service.svcinterface.IJxmbmxService;
import com.matridx.igams.hrm.service.svcinterface.IJxtxService;
import com.matridx.igams.hrm.service.svcinterface.IJxtxmxService;
import com.matridx.igams.hrm.service.svcinterface.IMbszService;
import com.matridx.igams.hrm.service.svcinterface.IQzszService;
import com.matridx.igams.hrm.service.svcinterface.ISyfwService;
import com.matridx.igams.hrm.service.svcinterface.IYghmcService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author WYX
 * @version 1.0
 * @className PerformanceManagementController
 * @description 绩效管理
 * @date 15:00 2023/1/9
 **/
@Controller
@RequestMapping("/performance")
public class PerformanceManagementController  extends BaseController {
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
     * 初始设置列表数据
     * @return Map
     */
    @RequestMapping(value = "/performance/pageGetListInitSetting",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> pageGetListInitSetting(){
        Map<String,Object> map = new HashMap<>();
        CsszDto csszDto = new CsszDto();
        csszDto.setKhlxjclb(BasicDataTypeEnum.ASSESSMENT_TYPE.getCode());
        csszDto.setSfsz("0");
        List<CsszDto> wszCsszDtos = csszService.getDtoList(csszDto);
        csszDto.setSfsz("1");
        List<CsszDto> yszCsszDtos = csszService.getDtoList(csszDto);
        XtshDto xtshDto = new XtshDto();
        xtshDto.setShlb(AuditTypeEnum.AUDIT_PERFORMANCE.getCode());
        List<XtshDto> jxShs = xtshService.getDtoList(xtshDto);
        xtshDto.setShlb(AuditTypeEnum.AUDIT_PERFORMANCE_TEMPLATE.getCode());
        List<XtshDto> mbShs = xtshService.getDtoList(xtshDto);
        map.put("wszCsszDtos", wszCsszDtos);//未设置初始设置
        map.put("yszCsszDtos", yszCsszDtos);//已设置初始设置
        map.put("jxShs", jxShs);//绩效审核
        map.put("mbShs", mbShs);//模板审核
        map.put("khlxs", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.ASSESSMENT_TYPE.getCode()));//考核类型
        map.put("khzqs", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.ASSESSMENT_PERIOD.getCode()));//考核周期
        map.put("mbjbs", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.TEMPLATE_LEVEL.getCode())); //模板级别
        map.put("mblxs", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.TEMPLATE_TYPE.getCode()));//模板类型
        map.put("mbzlxs", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.TEMPLATE_SUBTYPE.getCode()));//模板子类型
        map.put("urlPrefix",urlPrefix);
        return map;
    }
    /**
     * 新增保存初始设置信息
     * @param csszDto
     * @return Map
     */
    @RequestMapping(value = "/performance/minidataAddSaveInitSetting" , method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> minidataAddSaveInitSetting(CsszDto csszDto,HttpServletRequest request){
        User user = getLoginInfo(request);
        csszDto.setLrry(user.getYhid());
        csszDto.setLrrymc(user.getZsxm());
        Map<String,Object> map = new HashMap<>();
        boolean isSuccess;
        try {
            isSuccess = csszService.addSaveInitSetting(csszDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
            map.put("urlPrefix",urlPrefix);
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
     * 修改保存初始设置信息
     * @param csszDto
     * @return Map
     */
    @RequestMapping(value = "/performance/minidataModSaveInitSetting", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> minidataModSaveInitSetting(CsszDto csszDto,HttpServletRequest request){
        User user = getLoginInfo(request);
        csszDto.setXgry(user.getYhid());
        csszDto.setXgrymc(user.getZsxm());
        Map<String,Object> map = new HashMap<>();
        boolean isSuccess;
        try {
            isSuccess = csszService.modSaveInitSetting(csszDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
            map.put("urlPrefix",urlPrefix);
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
     * 获取初始设置信息
     * @param csszDto
     * @return Map
     */
    @RequestMapping(value = "/performance/minidataGetInitSettingInfo",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> minidataGetInitSettingInfo(CsszDto csszDto){
        Map<String,Object> map = new HashMap<>();
        CsszDto dtoById = csszService.getDtoById(csszDto.getCsszid());
        if (dtoById ==null){
            return null;
        }
        QzszDto qzszDto = new QzszDto();
        qzszDto.setCsszid(csszDto.getCsszid());
        List<QzszDto> qzszDtos = qzszService.getDtoList(qzszDto);
        ShlcDto shlcDto = new ShlcDto();
        shlcDto.setShid(dtoById.getMbshid());
        List<ShlcDto> shlcDtos = shlcService.getDtoListById(shlcDto);
        map.put("csszDto",dtoById);
        map.put("mbShlcDtos",shlcDtos);
        map.put("qzszDtos",qzszDtos);
        map.put("urlPrefix",urlPrefix);
        return map;
    }
    /**
     * 获取绩效模板适用范围
     * @param syfwDto
     * @return Map
     */
    @RequestMapping(value = "/performance/forpeopleSyfw",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> forpeopleSyfw(SyfwDto syfwDto){
        Map<String,Object> map = new HashMap<>();
        List<SyfwDto> syfwDtos = syfwService.getPagedDtoListByJxmbid(syfwDto);
        map.put("rows",syfwDtos);
        map.put("total",syfwDto.getTotalNumber());
        return map;
    }
    /**
     * 获取指定人员下级员工
     * @param zszg
     * @return Map
     */
    @RequestMapping(value = "/performance/minidataGetSubordinateEmployee",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> minidataGetSubordinateEmployee(String zszg){
        Map<String,Object> map = new HashMap<>();
        YghmcDto yghmcDto = new YghmcDto();
        yghmcDto.setZszg(zszg);
        List<YghmcDto> xjygDtos = yghmcService.getSubordinateEmployee(yghmcDto);
        map.put("xjygDtos",xjygDtos);
        return map;
    }
    /**
     * 根据用户名或真实姓名查询用户
     * @param yhmorzsxm
     * @return Map
     */
    @RequestMapping(value = "/performance/minidataGetUserByYhmOrZsxm",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> minidataGetUserByYhmOrZsxm(String yhmorzsxm){
        Map<String,Object> map = new HashMap<>();
        YghmcDto yghmcDto = new YghmcDto();
        yghmcDto.setYhmorzsxm(yhmorzsxm);
        List<YghmcDto> xtyhDtos = yghmcService.getUserByYhmOrZsxm(yghmcDto);
        map.put("xtyhDtos",xtyhDtos);
        return map;
    }
    /**
     * 发放绩效
     */
    @RequestMapping(value = "/performance/minidataDistributePerformance", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> minidataDistributePerformance(JxmbDto jxmbDto,HttpServletRequest request){
        User loginInfo = getLoginInfo(request);
        jxmbDto.setLrry(loginInfo.getYhid());
        jxmbDto.setBm(loginInfo.getJgid());
        Map<String,Object> map = new HashMap<>();
        boolean isSuccess;
        try {
            jxmbDto.setBatchFlag("0");
            isSuccess = grjxService.distributePerformance(jxmbDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
            map.put("urlPrefix",urlPrefix);
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
     * 发放绩效
     * @param jxmbDto
     * @return Map
     */
    @RequestMapping(value = "/performance/distributePerformance", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> distributePerformance(JxmbDto jxmbDto,HttpServletRequest request){
        User loginInfo = getLoginInfo(request);
        jxmbDto.setLrry(loginInfo.getYhid());
        jxmbDto.setBm(loginInfo.getJgid());
        Map<String,Object> map = new HashMap<>();
        boolean isSuccess;
        try {
            //批量发放绩效
            jxmbDto.setBatchFlag("1");
            isSuccess = grjxService.batchDistributePerformance(jxmbDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
            map.put("urlPrefix",urlPrefix);
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
     * 获取模板高级设置信息
     * @param mbszDto
     * @return Map
     */
    @RequestMapping(value = "/performance/advancedsettingTemplate",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> advancedsettingTemplate(MbszDto mbszDto){
        Map<String,Object> map = new HashMap<>();
        MbszDto dtoById = mbszService.getDtoById(mbszDto.getMbid());
        if (dtoById ==null){
            return null;
        }
        QzszDto qzszDto = new QzszDto();
        qzszDto.setMbszid(dtoById.getMbszid());
        List<QzszDto> qzszDtos = qzszService.getDtoListByMzbszid(qzszDto);
        XtshDto xtshDto = new XtshDto();
        xtshDto.setShlb(AuditTypeEnum.AUDIT_PERFORMANCE.getCode());
        List<XtshDto> jxShs = xtshService.getDtoList(xtshDto);
        map.put("jxShs",jxShs);
        map.put("mbszDto",dtoById);
        map.put("qzszDtos",qzszDtos);
        map.put("khzqs", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.ASSESSMENT_PERIOD.getCode()));//考核周期
        map.put("mbjbs", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.TEMPLATE_LEVEL.getCode())); //模板级别
        map.put("mblxs", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.TEMPLATE_TYPE.getCode()));//模板类型
        map.put("mbzlxs", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.TEMPLATE_SUBTYPE.getCode()));//模板子类型
        map.put("urlPrefix",urlPrefix);
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
     * 删除绩效
     * @param grjxDto
     * @param request
     * @return
     */
    @RequestMapping(value = "/performance/delPerformance",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> delPerformance(GrjxDto grjxDto,HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        grjxDto.setScry(user.getYhid());
        map.put("status", "success");
        try {
            grjxService.delByIds(grjxDto);
        }catch (BusinessException e){
            map.put("status", "fail");
        }
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
     * 钉钉个人绩效审核页面数据
     * @param grjxDto
     * @return Map
     */
    @RequestMapping(value = "/performance/minidataAuditPerformanceDing",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> minidataAuditPerformanceDing(GrjxDto grjxDto){
        Map<String,Object> map = new HashMap<>();
        GrjxDto dtoById = grjxService.getDtoById(grjxDto.getGrjxid());
        if (dtoById==null){
            map.put("status", "fail");
            map.put("message", "未找到对应绩效信息");
            return map;
        }
        ShgcDto dtoByYwid = shgcService.getDtoByYwid(dtoById.getGrjxid());
        JxmbmxDto jxmbmxDto = new JxmbmxDto();
        jxmbmxDto.setJxmbid(dtoById.getJxmbid());
        List<JxmbmxDto> jxmbmxDtos = jxmbmxService.getDtoList(jxmbmxDto);
        ShlcDto shlcDto = new ShlcDto();
        shlcDto.setShid(dtoById.getJxshid());
        List<ShlcDto> dtoListById = shlcService.getDtoListById(shlcDto);
        GrjxmxDto grjxmxDto = new GrjxmxDto();
        grjxmxDto.setGrjxid(dtoById.getGrjxid());
        List<GrjxmxDto> grjxmxDtos = grjxmxService.getDtoListWithScore(grjxmxDto);
        //将评分信息放入对应明细
        for (JxmbmxDto dto : jxmbmxDtos) {
            if ("1".equals(dtoById.getZp())){
                List<GrjxmxDto> dtoList = new ArrayList<>();
                for (GrjxmxDto dto_t : grjxmxDtos) {
                    if (StringUtil.isBlank(dto_t.getGwid())&&dto.getJxmbmxid().equals(dto_t.getJxmbmxid())){
                        dto_t.setGwmc("自评");
                        dtoList.add(dto_t);
                        break;
                    }
                }
                dto.setPfxxs(dtoList);
            }
            List<GrjxmxDto> pfxxs = new ArrayList<>();
            for (ShlcDto shlcDto_t : dtoListById) {
                //只展示当前待审核岗位和之前的数据
                if (Integer.parseInt(dtoByYwid.getXlcxh()) >= Integer.parseInt(shlcDto_t.getLcxh())) {
                    if (dtoByYwid.getXlcxh().equals(shlcDto_t.getLcxh())){
                        dtoById.setGwid(shlcDto_t.getGwid());
                    }
                    for (GrjxmxDto dto_t : grjxmxDtos) {
                        if (shlcDto_t.getGwid().equals(dto_t.getGwid())&&dto.getJxmbmxid().equals(dto_t.getJxmbmxid())) {
                            pfxxs.add(dto_t);
                            break;
                        }
                    }
                }
            }
            if (!CollectionUtils.isEmpty(dto.getPfxxs())){
                dto.getPfxxs().addAll(pfxxs);
            }else {
                dto.setPfxxs(pfxxs);
            }
        }
        //递归获取树结构
        List<JxmbmxDto> newDtoList = jxmbmxService.getNewDtoList(jxmbmxDtos);
        map.put("lists",newDtoList);
        map.put("grjxDto",dtoById);
        return map;
    }
    /**
     * 钉钉个人绩效查看页面数据
     * @param grjxDto
     * @return Map
     */
    @RequestMapping(value = "/performance/minidataViewPerformanceDing",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> minidataViewPerformanceDing(GrjxDto grjxDto){
        Map<String,Object> map = new HashMap<>();
        GrjxDto dtoById = grjxService.getDtoById(grjxDto.getGrjxid());
        if (dtoById==null){
            map.put("status", "fail");
            map.put("message", "未找到对应绩效信息");
            return map;
        }
        JxmbmxDto jxmbmxDto = new JxmbmxDto();
        jxmbmxDto.setJxmbid(dtoById.getJxmbid());
        List<JxmbmxDto> jxmbmxDtos = jxmbmxService.getDtoList(jxmbmxDto);
        ShlcDto shlcDto = new ShlcDto();
        shlcDto.setShid(dtoById.getJxshid());
        List<ShlcDto> dtoListById = shlcService.getDtoListById(shlcDto);
        GrjxmxDto grjxmxDto = new GrjxmxDto();
        grjxmxDto.setGrjxid(dtoById.getGrjxid());
        List<GrjxmxDto> grjxmxDtos = grjxmxService.getDtoListWithScore(grjxmxDto);
        //将评分信息放入对应明细
        for (JxmbmxDto dto : jxmbmxDtos) {
            if ("1".equals(dtoById.getZp())){
                List<GrjxmxDto> dtoList = new ArrayList<>();
                for (GrjxmxDto dto_t : grjxmxDtos) {
                    if (StringUtil.isBlank(dto_t.getGwid())&&dto.getJxmbmxid().equals(dto_t.getJxmbmxid())){
                        dtoList.add(dto_t);
                        break;
                    }
                }
                dto.setPfxxs(dtoList);
            }
            if (!StatusEnum.CHECK_NO.getCode().equals(dtoById.getZt())) {
                List<GrjxmxDto> pfxxs = new ArrayList<>();
                for (ShlcDto shlcDto_t : dtoListById) {
                    for (GrjxmxDto dto_t : grjxmxDtos) {
                        if (shlcDto_t.getGwid().equals(dto_t.getGwid()) && dto.getJxmbmxid().equals(dto_t.getJxmbmxid())) {
                            pfxxs.add(dto_t);
                            break;
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(dto.getPfxxs())) {
                    dto.getPfxxs().addAll(pfxxs);
                } else {
                    dto.setPfxxs(pfxxs);
                }
            }
        }
        //递归获取树结构
        List<JxmbmxDto> newDtoList = jxmbmxService.getNewDtoList(jxmbmxDtos);
        List<GrjxmxDto> pflist = new ArrayList<>();
        if ("1".equals(dtoById.getZp())){
            GrjxmxDto grjxmxDto_pf = new GrjxmxDto();
            for (GrjxmxDto grjxmxDto_t : grjxmxDtos) {
                if (StringUtil.isBlank(grjxmxDto_t.getGwid())){
                    grjxmxDto_pf.setGwmc("自评");
                    grjxmxDto_pf.setQz(grjxmxDto_t.getQz());
                    if (StringUtil.isNotBlank(grjxmxDto_t.getJxzf())){
                        grjxmxDto_pf.setJxzf(grjxmxDto_t.getJxzf());
                        break;
                    }
                }
            }
            pflist.add(grjxmxDto_pf);
        }
        for (ShlcDto dto : dtoListById) {
            if (!StatusEnum.CHECK_NO.getCode().equals(dtoById.getZt())) {
                GrjxmxDto grjxmxDto_pf = new GrjxmxDto();
                for (GrjxmxDto grjxmxDto_t : grjxmxDtos) {
                    if (dto.getGwid().equals(grjxmxDto_t.getGwid())) {
                        grjxmxDto_pf.setGwmc(dto.getGwmc());
                        grjxmxDto_pf.setQz(grjxmxDto_t.getQz());
                        grjxmxDto_pf.setJxzf(grjxmxDto_t.getJxzf());
                        if (StringUtil.isNotBlank(grjxmxDto_t.getShrmc())) {
                            grjxmxDto_pf.setShrmc(grjxmxDto_t.getShrmc());
                            break;
                        }
                    }
                }
                pflist.add(grjxmxDto_pf);
            }
        }
        map.put("lists",newDtoList);
        map.put("pflist",pflist);
        map.put("grjxDto",dtoById);
        return map;
    }
    /**
     * 个人绩效修改保存
     * @param grjxDto
     * @return Map
     */
    @RequestMapping(value = "/performance/minidataModSavePerformance",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> minidataModSavePerformance(GrjxDto grjxDto,HttpServletRequest request){
        User user = getLoginInfo(request);
        Map<String,Object> map = new HashMap<>();
        grjxDto.setXgry(user.getYhid());
        boolean isSuccess;
        try {
            isSuccess = grjxService.modSavePerformance(grjxDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
            map.put("urlPrefix",urlPrefix);
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
     * 个人绩效废弃
     * @param grjxDto
     * @return Map
     */
    @RequestMapping(value = "/performance/discardPerformance",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> discardPerformance(GrjxDto grjxDto,HttpServletRequest request){
        User user = getLoginInfo(request);
        Map<String,Object> map = new HashMap<>();
        grjxDto.setXgry(user.getYhid());
        boolean isSuccess;
        try {
            isSuccess = grjxService.discardPerformance(grjxDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00003").getXxnr() : xxglService.getModelById("ICOM00004").getXxnr());
            map.put("urlPrefix",urlPrefix);
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00004").getXxnr());
            map.put("urlPrefix",urlPrefix);
        }catch (Exception e) {
            map.put("status", "fail");
            map.put("message", xxglService.getModelById("ICOM00004").getXxnr());
            map.put("urlPrefix",urlPrefix);
        }
        return map;
    }
    /**
     * 模板保存高级设置
     * @param mbszDto
     * @return Map
     */
    @RequestMapping(value = "/performance/advancedsettingSaveTemplate", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> advancedsettingSaveTemplate(MbszDto mbszDto,HttpServletRequest request){
        User user = getLoginInfo(request);
        mbszDto.setLrry(user.getYhid());
        mbszDto.setLrrymc(user.getZsxm());
        Map<String,Object> map = new HashMap<>();
        boolean isSuccess;
        try {
            isSuccess = mbszService.advancedTemplateSetting(mbszDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
            map.put("urlPrefix",urlPrefix);
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
     * 保存适用范围
     * @param syfwDto
     * @return Map
     */
    @RequestMapping(value = "/performance/forpeopleSaveSyfw", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> forpeopleSaveSyfw(SyfwDto syfwDto){
        Map<String,Object> map = new HashMap<>();
        boolean isSuccess;
        try {
            isSuccess = syfwService.addSaveSyfw(syfwDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
            map.put("urlPrefix",urlPrefix);
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
     * 模板列表
     * @param jxmbDto
     * @return
     */
    @RequestMapping(value = "/template/pageGetListTemplate",method =RequestMethod.GET)
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
     * 模板列表指定下级目标
     */
    @PostMapping("/template/makegoalsTemplate")
    @ResponseBody
    public Map<String,Object> makegoalsTemplate(){
        Map<String,Object> result = new HashMap<>();
        CsszDto csszDto=new CsszDto();
        csszDto.setKhlxjclb(BasicDataTypeEnum.ASSESSMENT_TYPE.getCode());
        csszDto.setSfsz("1");
        List<CsszDto> csszDtos=csszService.getDtoList(csszDto);
        result.put("csszDtos",csszDtos);
        return result;
    }
    /**
     * 模板列表制定下级目标保存
     * @param jxmbDto
     * @return
     */
    @PostMapping("/template/makegoalsSaveTemplate")
    @ResponseBody
    public Map<String,Object> makegoalsSaveTemplate(JxmbDto jxmbDto,HttpServletRequest request) {
        User user=getLoginInfo(request);
        jxmbDto.setLx("0");
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
     * 绩效模板导入
     * @param syfwDto
     * @return
     */
    @RequestMapping(value = "/template/importTemplate",method =RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> importTemplate(SyfwDto syfwDto,HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        User user=getLoginInfo(request);
        syfwDto.setYhid(user.getYhid());
        List<SyfwDto> syfwDtoList=syfwService.getPagedDtoList(syfwDto);
        result.put("rows",syfwDtoList);
        result.put("total", syfwDto.getTotalNumber());
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
     * 个人绩效发放撤回
     * @param grjxDto
     * @return
     */
    @PostMapping("/template/minidataReleaseRecallPerformance")
    @ResponseBody
    public Map<String,Object> minidataReleaseRecallPerformance(GrjxDto grjxDto,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        grjxDto.setScry(user.getYhid());
        boolean isSuccess;
        try {
            isSuccess = grjxService.releaseRecallPerformance(grjxDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
            map.put("urlPrefix",urlPrefix);
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
     * 绩效模板草稿箱
     * @param grjxDto
     * @return
     */
    @PostMapping("/template/minidataPerformanceDrafts")
    @ResponseBody
    public Map<String,Object> minidataPerformanceDrafts(GrjxDto grjxDto,HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        User user=getLoginInfo(request);
        grjxDto.setYhid(user.getYhid());
        grjxDto=grjxService.getLastPerformance(grjxDto);
        result.put("grjxDto",grjxDto);
        return result;
    }
    /**
     * 我的待办页面数据
     * @param request
     * @return
     */
    @PostMapping("/template/minidataPersonalToDo")
    @ResponseBody
    public Map<String,Object> minidataPersonalToDo(HttpServletRequest request) {
        Map<String,Object> result = new HashMap<>();
        User user=getLoginInfo(request);
        result.put("ddtxlj", user.getDdtxlj());
        result.put("zsxm", user.getZsxm());
        //获取绩效数据
        GrjxDto grjxDto=new GrjxDto();
        grjxDto.setRyid(user.getYhid());
        List<GrjxDto> grjxDtoList = grjxService.getToDoList(grjxDto);
        List<GrjxDto> jxDbDtos = new ArrayList<>();//绩效待办数据
        List<GrjxDto> jxClzDtos = new ArrayList<>();//绩效处理中数据
        if(!CollectionUtils.isEmpty(grjxDtoList)){
            for(GrjxDto grjxDto_t:grjxDtoList){
                grjxDto_t.setAuditType(AuditTypeEnum.AUDIT_PERFORMANCE.getCode());
                if(StatusEnum.CHECK_NO.getCode().equals(grjxDto_t.getZt())||StatusEnum.CHECK_UNPASS.getCode().equals(grjxDto_t.getZt())){
                    jxDbDtos.add(grjxDto_t);
                }else if(StatusEnum.CHECK_SUBMIT.getCode().equals(grjxDto_t.getZt())){
                    jxClzDtos.add(grjxDto_t);
                }
            }
        }
        result.put("jxDbNum", jxDbDtos.size());
        result.put("jxDbDtos", jxDbDtos);
        result.put("jxClzNum", jxClzDtos.size());
        result.put("jxClzDtos", jxClzDtos);
        //获取模板数据
        JxmbDto jxmbDto=new JxmbDto();
        jxmbDto.setRyid(user.getYhid());
        List<JxmbDto> jxmbDtoList = jxmbService.getToDoList(jxmbDto);
        List<JxmbDto> mbDbDtos = new ArrayList<>();//模板待办数据
        List<JxmbDto> mbClzDtos = new ArrayList<>();//模板处理中数据
        if(!CollectionUtils.isEmpty(jxmbDtoList)){
            for(JxmbDto jxmbDto_t:jxmbDtoList){
                jxmbDto_t.setAuditType(AuditTypeEnum.AUDIT_PERFORMANCE_TEMPLATE.getCode());
                if(StatusEnum.CHECK_NO.getCode().equals(jxmbDto_t.getZt())||StatusEnum.CHECK_UNPASS.getCode().equals(jxmbDto_t.getZt())){
                    mbDbDtos.add(jxmbDto_t);
                }else if(StatusEnum.CHECK_SUBMIT.getCode().equals(jxmbDto_t.getZt())){
                    mbClzDtos.add(jxmbDto_t);
                }
            }
        }
        result.put("mbDbNum", mbDbDtos.size());
        result.put("mbDbDtos", mbDbDtos);
        result.put("mbClzNum", mbClzDtos.size());
        result.put("mbClzDtos", mbClzDtos);
        //获取绩效审核数据(只查两个)
        GrjxDto auditJxDto=new GrjxDto();
        auditJxDto.setSortName("shxx_sqsj");
        auditJxDto.setSortOrder("desc");
        auditJxDto.setPageSize(2);
        auditJxDto.setPageNumber(1);
        auditJxDto.setPageStart(0);
        DataPermission.addWtParam(auditJxDto);
        DataPermission.add(auditJxDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "grjx", "grjxid", AuditTypeEnum.AUDIT_PERFORMANCE);
        DataPermission.addCurrentUser(auditJxDto, user);
        DataPermission.addSpDdw(auditJxDto, "grjx", SsdwTableEnum.GRJX);
        List<GrjxDto> jxAuditList = grjxService.getAuditListData(auditJxDto);
        result.put("jxAuditNum", auditJxDto.getTotalNumber());
        result.put("jxAuditList", jxAuditList);
        //获取模板审核数据(只查两个)
        JxmbDto auditMbDto=new JxmbDto();
        auditMbDto.setSortName("shxx_sqsj");
        auditMbDto.setSortOrder("desc");
        auditMbDto.setPageSize(2);
        auditMbDto.setPageNumber(1);
        auditJxDto.setPageStart(0);
        DataPermission.addWtParam(auditMbDto);
        DataPermission.addSpDdw(auditMbDto, "jxmb", SsdwTableEnum.JXMB);
        DataPermission.add(auditMbDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "jxmb", "jxmbid", AuditTypeEnum.AUDIT_PERFORMANCE_TEMPLATE);
        DataPermission.addCurrentUser(auditMbDto, user);
        List<JxmbDto> mbAuditList = jxmbService.getAuditListData(auditMbDto);
        result.put("mbAuditNum", auditMbDto.getTotalNumber());
        result.put("mbAuditList", mbAuditList);
        //获取草稿箱数据
        List<Map<String,Object>> cgxData=new ArrayList<>();
        if(!CollectionUtils.isEmpty(jxmbDtoList)){
            for(JxmbDto jxmbDto_t:jxmbDtoList){
                if(StatusEnum.CHECK_NO.getCode().equals(jxmbDto_t.getZt())){
                    JxmbmxDto jxmbmxDto=new JxmbmxDto();
                    jxmbmxDto.setJxmbid(jxmbDto_t.getJxmbid());
                    List<JxmbmxDto> jxmbmxList = jxmbmxService.getJxmbmxList(jxmbmxDto);
                    if(!CollectionUtils.isEmpty(jxmbmxList)){
                        Map<String,Object> map = new HashMap<>();
                        map.put("syrq",jxmbDto_t.getSyrq());
                        map.put("mc",jxmbmxList.get(0).getMbmc());
                        map.put("jxmbid",jxmbmxList.get(0).getJxmbid());
                        map.put("mbshid",jxmbmxList.get(0).getMbshid());
                        map.put("xgxsm",jxmbmxList.get(0).getXgxsm());
                        map.put("auditType",AuditTypeEnum.AUDIT_PERFORMANCE_TEMPLATE.getCode());
                        map.put("lx",jxmbmxList.get(0).getLx());
                        map.put("zf",jxmbmxList.get(0).getZf());
                        map.put("list",jxmbmxList);
                        cgxData.add(map);
                    }
                }
            }
        }
        result.put("cgxData", cgxData);
        //获取绩效统计数据
        YghmcDto yghmcDto = new YghmcDto();
        yghmcDto.setZszg(user.getDdid());
        List<YghmcDto> subordinateEmployeeList = yghmcService.getSubordinateEmployee(yghmcDto);
        if (!CollectionUtils.isEmpty(subordinateEmployeeList)){
            LocalDateTime now = LocalDateTime.now();
            int day = now.getDayOfMonth();
            if(day<8){
                yghmcDto.setDqy("1");
            }
            Map<String, Object> jxmbData = jxmbService.getPerformanceTargetStatistics(yghmcDto);
            Map<String, Object> jxData = grjxService.getPerformanceStatistics(yghmcDto);
            jxmbData.put("ygs",subordinateEmployeeList.size());
            jxData.put("ygs",subordinateEmployeeList.size());
            result.put("jxmbData", jxmbData);
            result.put("jxData", jxData);
        }
        return result;
    }
    /**
     * 绩效目标统计
     * @param request
     * @return
     */
    @RequestMapping("/template/minidataPerformanceTargetStatistics")
    @ResponseBody
    public Map<String,Object> minidataPerformanceTargetStatistics(HttpServletRequest request) {
        Map<String,Object> result = new HashMap<>();
        User user=getLoginInfo(request);
        //获取绩效统计数据
        YghmcDto yghmcDto = new YghmcDto();
        yghmcDto.setZszg(user.getDdid());
        LocalDateTime now = LocalDateTime.now();
        int day = now.getDayOfMonth();
        if(day<8){
            yghmcDto.setDqy("1");
        }
        List<Map<String, Object>> jxmbData = jxmbService.getPerformanceTargetStatisticsView( yghmcDto);
        result.put("rows", jxmbData);
        return result;
    }
    /**
     * 绩效统计
     * @param request
     * @return
     */
    @RequestMapping("/template/minidataPerformanceStatistics")
    @ResponseBody
    public Map<String,Object> minidataPerformanceStatistics(HttpServletRequest request) {
        Map<String,Object> result = new HashMap<>();
        User user=getLoginInfo(request);
        //获取绩效统计数据
        YghmcDto yghmcDto = new YghmcDto();
        yghmcDto.setZszg(user.getDdid());
        LocalDateTime now = LocalDateTime.now();
        int day = now.getDayOfMonth();
        if(day<8){
            yghmcDto.setDqy("1");
        }
        List<Map<String, Object>> jxData = grjxService.getPerformanceStatisticsView(yghmcDto);
        result.put("rows", jxData);
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
     * 获取考核类型基础数据
     * @return
     */
    @PostMapping("/template/minidataGetBasicData")
    @ResponseBody
    public Map<String,Object> minidataGetBasicData() {
        Map<String, Object> map = new HashMap<>();
        map.put("khlxs", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.ASSESSMENT_TYPE.getCode()));//考核类型
        return map;
    }
    /**
     * 全部和部门模板提交前的更新状态操作
     */
    @PostMapping("/template/pagedataUpdateZt")
    @ResponseBody
    public Map<String,Object> pagedataUpdateZt(JxmbDto jxmbDto) {
        Map<String, Object> map = new HashMap<>();
        if ("tj".equals(jxmbDto.getSignal())){
            jxmbDto.setZt("80");
            Date date=new Date();
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            jxmbDto.setTjsj(dateFormat.format(date));
        }
        boolean isSuccess=jxmbService.update(jxmbDto);
        map.put("status",isSuccess?"success":"fail");
        map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }
    /**
     * 绩效提醒列表数据
     * @return Map
     */
    @RequestMapping(value = "/performance/pageGetListPerformanceReminder",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> pageGetListPerformanceReminder(JxtxDto jxtxDto,HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        List<JxtxDto> jxtxDtos = jxtxService.getPagedDtoList(jxtxDto);
        super.setCzdmList(request,map);
        map.put("khzqs", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.ASSESSMENT_PERIOD.getCode()));//考核周期
        map.put("zjs", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.JOB_RANK.getCode()));//职级
        map.put("rows",jxtxDtos);
        map.put("total", jxtxDto.getTotalNumber());
        map.put("urlPrefix",urlPrefix);
        return map;
    }
    /**
     * 绩效提醒查看
     * @return Map
     */
    @RequestMapping(value = "/performance/viewPerformanceReminder",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> viewPerformanceReminder(JxtxDto jxtxDto){
        Map<String,Object> map = new HashMap<>();
        JxtxDto dtoById = jxtxService.getDtoById(jxtxDto.getJxtxid());
        JxtxmxDto jxtxmxDto = new JxtxmxDto();
        jxtxmxDto.setJxtxid(dtoById.getJxtxid());
        List<JxtxmxDto> jxtxmxDtos = jxtxmxService.getDtoList(jxtxmxDto);
        map.put("khzqs", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.ASSESSMENT_PERIOD.getCode()));//考核周期
        map.put("jxtxDto",dtoById);
        map.put("jxtxmxDtos",jxtxmxDtos);
        map.put("urlPrefix",urlPrefix);
        return map;
    }
    /**
     * 新增保存绩效提醒信息
     * @param jxtxDto
     * @return Map
     */
    @RequestMapping(value = "/performance/addSavePerformanceReminder" , method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> addSavePerformanceReminder(JxtxDto jxtxDto,HttpServletRequest request){
        User user = getLoginInfo(request);
        jxtxDto.setLrry(user.getYhid());
        Map<String,Object> map = new HashMap<>();
        boolean isSuccess;
        try {
            isSuccess = jxtxService.addSavePerformanceReminder(jxtxDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
            map.put("urlPrefix",urlPrefix);
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
     * 修改绩效提醒信息
     * @return Map
     */
    @RequestMapping(value = "/performance/modPerformanceReminder",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> modPerformanceReminder(JxtxDto jxtxDto,HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        JxtxDto dtoById = jxtxService.getDtoById(jxtxDto.getJxtxid());
        JxtxmxDto jxtxmxDto = new JxtxmxDto();
        jxtxmxDto.setJxtxid(dtoById.getJxtxid());
        List<JxtxmxDto> jxtxmxDtos = jxtxmxService.getDtoList(jxtxmxDto);
        map.put("khzqs", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.ASSESSMENT_PERIOD.getCode()));//考核周期
        map.put("jxtxDto",dtoById);
        map.put("jxtxmxDtos",jxtxmxDtos);
        map.put("urlPrefix",urlPrefix);
        return map;
    }
    /**
     * 修改保存绩效提醒信息
     * @param jxtxDto
     * @return Map
     */
    @RequestMapping(value = "/performance/modSavePerformanceReminder" , method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> modSavePerformanceReminder(JxtxDto jxtxDto,HttpServletRequest request){
        User user = getLoginInfo(request);
        jxtxDto.setXgry(user.getYhid());
        Map<String,Object> map = new HashMap<>();
        boolean isSuccess;
        try {
            isSuccess = jxtxService.modSavePerformanceReminder(jxtxDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
            map.put("urlPrefix",urlPrefix);
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
     * 删除绩效提醒信息
     * @param jxtxDto
     * @return Map
     */
    @RequestMapping(value = "/performance/delPerformanceReminder" , method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> delPerformanceReminder(JxtxDto jxtxDto,HttpServletRequest request){
        User user = getLoginInfo(request);
        jxtxDto.setScry(user.getYhid());
        Map<String,Object> map = new HashMap<>();
        boolean isSuccess;
        try {
            isSuccess = jxtxService.delPerformanceReminder(jxtxDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00003").getXxnr() : xxglService.getModelById("ICOM00004").getXxnr());
            map.put("urlPrefix",urlPrefix);
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00003").getXxnr());
            map.put("urlPrefix",urlPrefix);
        }catch (Exception e) {
            map.put("status", "fail");
            map.put("message", xxglService.getModelById("ICOM00003").getXxnr());
            map.put("urlPrefix",urlPrefix);
        }
        return map;
    }
    /**
     * 绩效确认列表
     * @return Map
     */
    @RequestMapping(value = "/performance/minidataGetListPerformanceConfirm",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> minidataGetListPerformanceConfirm(GrjxDto grjxDto,HttpServletRequest request){
        User loginInfo = getLoginInfo(request);
        grjxDto.setYhid(loginInfo.getYhid());
        Map<String,Object> map = new HashMap<>();
        List<GrjxDto> grjxDtos = grjxService.getPagedListPerformanceConfirm(grjxDto);
        map.put("grjxDtos",grjxDtos);
        map.put("urlPrefix",urlPrefix);
        return map;
    }
    /**
     * 绩效确认页面数据
     * @return Map
     */
    @RequestMapping(value = "/performance/minidataGetPerformanceConfirm",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> minidataGetPerformanceConfirm(JxmbDto jxmbDto){
        Map<String,Object> map = new HashMap<>();
        JxmbDto dtoById = jxmbService.getDtoById(jxmbDto.getJxmbid());
        JxmbmxDto jxmbmxDto = new JxmbmxDto();
        jxmbmxDto.setJxmbid(dtoById.getJxmbid());
        List<JxmbmxDto> dtoList = jxmbmxService.getDtoList(jxmbmxDto);
        List<JxmbmxDto> newDtoList = jxmbmxService.getNewDtoList(dtoList);
        map.put("jxmbDto",dtoById);
        map.put("jxmbmxDtos",newDtoList);
        map.put("urlPrefix",urlPrefix);
        return map;
    }
    /**
     *  确认绩效信息
     * @param gxjxDto
     * @return Map
     */
    @RequestMapping(value = "/performance/minidataConfirmPerformance" , method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> minidataConfirmPerformance(GrjxDto gxjxDto,HttpServletRequest request){
       return this.confirmPerformance(gxjxDto,request);
    }
    /**
     *  确认绩效信息
     * @param gxjxDto
     * @return Map
     */
    @RequestMapping(value = "/performance/confirmPerformance" , method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> confirmPerformance(GrjxDto gxjxDto,HttpServletRequest request){
        User user = getLoginInfo(request);
        GrjxDto grjxDto = new GrjxDto();
        grjxDto.setGrjxid(gxjxDto.getGrjxid());
        grjxDto.setXgry(user.getYhid());
        Map<String,Object> map = new HashMap<>();
        boolean isSuccess;
        try {
            isSuccess = grjxService.confirmPerformance(grjxDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
        }
        return map;
    }
    /**
     * 目标导入页面
     * @return
     */
    @RequestMapping(value ="/template/pageImportTemplate")
    public ModelAndView pageImportTemplate(){
        ModelAndView mav = new ModelAndView("performance/performanceTemplate_import");

        FjcfbDto fjcfbDto = new FjcfbDto();
        fjcfbDto.setYwlx(BusTypeEnum.IMP_PERFORMANCE_TEMPLATE.getCode());
        mav.addObject("fjcfbDto", fjcfbDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
}
