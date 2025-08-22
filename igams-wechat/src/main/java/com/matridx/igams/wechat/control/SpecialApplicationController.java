package com.matridx.igams.wechat.control;


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
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.wechat.dao.entities.SjjcxmDto;
import com.matridx.igams.wechat.dao.entities.SjtssqDto;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.service.svcinterface.ISjjcxmService;
import com.matridx.igams.wechat.service.svcinterface.ISjtssqService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/application")
public class SpecialApplicationController extends BaseController {

    @Autowired
    ISjtssqService sjtssqService;
    @Autowired
    IJcsjService jcsjService;
    @Autowired
    ISjxxService sjxxService;
    @Autowired
    IXxglService xxglService;
    @Autowired
    IShgcService shgcService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    ISjjcxmService sjjcxmService;
    /**
     * 跳转特殊申请列表页面
     * @return
     */
    @RequestMapping(value = "/application/pageListApplication")
    public ModelAndView getApplicationPage(SjtssqDto sjtssqDto) {
        ModelAndView mav =new ModelAndView("wechat/application/application_List");
        mav.addObject("detectlist", redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode()));//检测项目
        mav.addObject("samplelist", redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));
        mav.addObject("applylist",redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.APPLICATION_ITEM.getCode()) );
        mav.addObject("reasonlist", redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.APPLICATION_REASON.getCode()));
        mav.addObject("detectionUnit", redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));
        mav.addObject("sjtssqDto",sjtssqDto);
        return mav;
    }

    /**
     * 获取特殊申请列表
     * @param sjtssqDto
     * @return
     */
    @RequestMapping(value ="/application/pageGetListApplication")
    @ResponseBody
    public Map<String,Object> getApplicationListPageList(SjtssqDto sjtssqDto,HttpServletRequest request){
        User user_t=getLoginInfo();
        List<Map<String,String>> jcdwList=sjxxService.getJsjcdwByjsid(user_t.getDqjs());
        if(jcdwList!=null&&jcdwList.size() > 0){
            if("1".equals(jcdwList.get(0).get("dwxdbj"))) {
                List<String> strList= new ArrayList<>();
                for (int i = 0; i < jcdwList.size(); i++){
                    if(jcdwList.get(i).get("jcdw")!=null) {
                        strList.add(jcdwList.get(i).get("jcdw"));
                    }
                }
                if(strList!=null && strList.size()>0) {
                    sjtssqDto.setJcdwxz(strList);
                }
            }
        }

        if("1".equals(sjtssqDto.getLoad_flag())) {
            sjtssqDto.setLrry(user_t.getYhid());
        }
        List<SjtssqDto> list=sjtssqService.getPagedDtoList(sjtssqDto);
        try{
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_FREESAMPLES.getCode(), "zt", "tssqid", new String[]{
                    StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_VIPSAMPLES.getCode(), "zt", "tssqid", new String[]{
                    StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_PKSAMPLES.getCode(), "zt", "tssqid", new String[]{
                    StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
        } catch (BusinessException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Map<String, Object> map= new HashMap<>();
        map.put("total", sjtssqDto.getTotalNumber());
        map.put("rows", list);
        return map;
    }

    /**
     * 查看
     * @return
     */
    @RequestMapping(value = "/application/viewApplication")
    public ModelAndView viewApplication(SjtssqDto sjtssqDto) {
        ModelAndView mav =new ModelAndView("wechat/application/application_view");
        SjtssqDto dtoById = sjtssqService.getDto(sjtssqDto);
        mav.addObject("sjtssqDto",dtoById);
        return mav;
    }

    /**
     * 查看接口
     * @return
     */
    @RequestMapping(value = "/application/minidataViewInterface")
    @ResponseBody
    public Map<String,Object> minidataViewInterface(SjtssqDto sjtssqDto) {
        Map<String, Object> map= new HashMap<>();
        SjtssqDto dtoById = sjtssqService.getDto(sjtssqDto);
        map.put("sjtssqDto",dtoById);
        return map;
    }


    /**
     * 修改接口
     * @return
     */
    @RequestMapping(value = "/application/minidataModInterface")
    @ResponseBody
    public Map<String,Object> modInterface(SjtssqDto sjtssqDto) {
        Map<String, Object> map= new HashMap<>();
        SjtssqDto dtoById = sjtssqService.getDto(sjtssqDto);
        List<JcsjDto> reasonlist = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.APPLICATION_REASON.getCode());
        map.put("sjtssqDto",dtoById);
        map.put("subapplylist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.APPLICATION_SUBITEM.getCode()));
        map.put("reasonlist", reasonlist);
        map.put("applylist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.APPLICATION_ITEM.getCode()));
        map.put("formAction", "/application/application/modSaveApplication");
        return map;
    }

    /**
     * 修改
     * @return
     */
    @RequestMapping(value = "/application/modApplication")
    public ModelAndView modApplication(SjtssqDto sjtssqDto) {
        ModelAndView mav =new ModelAndView("wechat/application/application_edit");
        SjtssqDto dtoById = sjtssqService.getDto(sjtssqDto);
        List<JcsjDto> dtoList =new ArrayList<>();
        List<JcsjDto> reasonlist = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.APPLICATION_REASON.getCode());
        if(dtoById!=null){
            if(StringUtil.isNotBlank(dtoById.getSqxm())){
                JcsjDto jcsjDto=new JcsjDto();
                jcsjDto.setFcsid(dtoById.getSqxm());
                jcsjDto.setJclb(BasicDataTypeEnum.APPLICATION_SUBITEM.getCode());
                List<JcsjDto> list = jcsjService.getDtoList(jcsjDto);
                for(int i=0;i<list.size();i++){
                    if("FREE".equals(dtoById.getSqxmdm())){
                        if(list.get(i).getCskz2().indexOf(dtoById.getJcxmdm())!=-1){
                            dtoList.add(list.get(i));
                        }
                    }else{
                        dtoList.add(list.get(i));
                    }
                }
            }
            for(int i=0;i<reasonlist.size();i++){
                if(reasonlist.get(i).getCskz2().indexOf(dtoById.getSqxmdm())==-1){
                    reasonlist.remove(i);
                }
            }
            if("FREE".equals(dtoById.getSqxmdm())) {
                dtoById.setAuditType(AuditTypeEnum.AUDIT_FREESAMPLES.getCode());
            }else if("VIP".equals(dtoById.getSqxmdm())) {
                dtoById.setAuditType(AuditTypeEnum.AUDIT_VIPSAMPLES.getCode());
            }else if("PK".equals(dtoById.getSqxmdm())) {
                dtoById.setAuditType(AuditTypeEnum.AUDIT_PKSAMPLES.getCode());
            }
        }
        SjjcxmDto sjjcxmDto=new SjjcxmDto();
        sjjcxmDto.setSjid(dtoById.getSjid());
        List<SjjcxmDto> sjjcxmList=sjjcxmService.getDtoList(sjjcxmDto);
        mav.addObject("sjjcxmList",sjjcxmList);
        mav.addObject("sjtssqDto",dtoById);
        mav.addObject("jcsjlist", dtoList);
        mav.addObject("reasonlist", reasonlist);
        mav.addObject("applylist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.APPLICATION_ITEM.getCode()));
        mav.addObject("formAction", "/application/application/modSaveApplication");
        return mav;
    }

    /**
     * 修改保存
     * @return
     */
    @RequestMapping(value = "/application/modSaveApplication")
    @ResponseBody
    public Map<String,Object> modSaveApplication(SjtssqDto sjtssqDto,HttpServletRequest request) {
        Map<String, Object> map= new HashMap<>();
        SjtssqDto dto = sjtssqService.verification(sjtssqDto);
        String sqxms = sjtssqService.getSqxms(sjtssqDto);
        List<JcsjDto> list = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.APPLICATION_ITEM.getCode());
        if(list!=null&&list.size()>0){
            for(JcsjDto jcsjDto:list){
                if(sjtssqDto.getSqxm().equals(jcsjDto.getCsid())){
                    sjtssqDto.setSqxmdm(jcsjDto.getCsdm());
                    sjtssqDto.setSqxmmc(jcsjDto.getCsmc());
                }
            }
        }
        if(dto!=null){
            map.put("status","fail");
            map.put("message","此申请项目已存在!");
        } else {
            sqxms=sqxms+","+sjtssqDto.getSqxmdm();
            if(sqxms.indexOf("VIP")!=-1&&sqxms.indexOf("PK")!=-1){
                map.put("status","fail");
                map.put("message","您之前申请过PK和VIP其一，所以只能继续申请同一个申请项目!");
            }else{
                User user = getLoginInfo(request);
                sjtssqDto.setXgry(user.getYhid());
                boolean isSuccess = sjtssqService.updateDto(sjtssqDto);
                map.put("status", isSuccess?"success":"fail");
                map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
            }
        }
        map.put("ywid",sjtssqDto.getTssqid());
        SjtssqDto dtoById = sjtssqService.getDto(sjtssqDto);
        if(dtoById!=null){
            if("FREE".equals(dtoById.getSqxmdm())) {
                map.put("auditType", AuditTypeEnum.AUDIT_FREESAMPLES.getCode());
            }else if("VIP".equals(dtoById.getSqxmdm())) {
                map.put("auditType", AuditTypeEnum.AUDIT_VIPSAMPLES.getCode());
            }else if("PK".equals(dtoById.getSqxmdm())) {
                map.put("auditType", AuditTypeEnum.AUDIT_PKSAMPLES.getCode());
            }
        }
        return map;
    }


    /**
     * 删除
     * @return
     */
    @RequestMapping(value = "/application/delApplication")
    @ResponseBody
    public Map<String,Object> delApplication(SjtssqDto sjtssqDto,HttpServletRequest request) {
        Map<String, Object> map= new HashMap<>();
        User user = getLoginInfo(request);
        sjtssqDto.setScry(user.getYhid());
        boolean isSuccess = sjtssqService.deleteDto(sjtssqDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
        return map;
    }

    /**
     * 处理按钮
     * @return
     */
    @RequestMapping(value = "/application/dealApplication")
    @ResponseBody
    public Map<String,Object> dealApplication(SjtssqDto sjtssqDto,HttpServletRequest request) {
        Map<String, Object> map= new HashMap<>();
        User user = getLoginInfo(request);
        sjtssqDto.setXgry(user.getYhid());
        boolean isSuccess = sjtssqService.updateClbj(sjtssqDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 申请接口
     * @param sjxxDto
     * @return
     */
    @RequestMapping(value ="/application/minidataApplyInterface")
    @ResponseBody
    public Map<String,Object> applyInterface(SjxxDto sjxxDto){
        Map<String, Object> map= new HashMap<>();
        SjxxDto dto = sjxxService.getDto(sjxxDto);
        SjjcxmDto sjjcxmDto=new SjjcxmDto();
        sjjcxmDto.setSjid(dto.getSjid());
        List<SjjcxmDto> sjjcxmList=sjjcxmService.getDtoList(sjjcxmDto);
        List<JcsjDto> list = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.APPLICATION_SUBITEM.getCode());
        map.put("sjjcxmList",sjjcxmList);
        map.put("applylist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.APPLICATION_ITEM.getCode()));
        map.put("applysublist", list);
        map.put("reasonlist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.APPLICATION_REASON.getCode()));
        map.put("sjxxDto",dto);
        return map;
    }


    /**
     * 	申请审核
     * @return
     */
    @RequestMapping("/application/pageListApplicationAudit")
    public ModelAndView pageListApplicationAudit() {
        return new  ModelAndView("wechat/application/application_auditList");
    }

    /**
     * 	申请审核列表
     * @param sjtssqDto
     * @return
     */
    @RequestMapping("/application/pageGetListApplicationAudit")
    @ResponseBody
    public Map<String, Object> pageGetListApplicationAudit(SjtssqDto sjtssqDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        // 附加委托参数
        DataPermission.addWtParam(sjtssqDto);
        // 附加审核状态过滤
        if (GlobalString.AUDIT_SHZT_YSH.equals(sjtssqDto.getDqshzt())) {
            List<AuditTypeEnum> auditTypes = new ArrayList<>();
            auditTypes.add(AuditTypeEnum.AUDIT_FREESAMPLES);
            auditTypes.add(AuditTypeEnum.AUDIT_VIPSAMPLES);
            auditTypes.add(AuditTypeEnum.AUDIT_PKSAMPLES);
            DataPermission._add(sjtssqDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "tssq", "tssqid",
                    auditTypes,null);
        } else {
            List<AuditTypeEnum> auditTypes = new ArrayList<>();
            auditTypes.add(AuditTypeEnum.AUDIT_FREESAMPLES);
            auditTypes.add(AuditTypeEnum.AUDIT_VIPSAMPLES);
            auditTypes.add(AuditTypeEnum.AUDIT_PKSAMPLES);
            DataPermission._add(sjtssqDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "tssq", "tssqid",
                    auditTypes,null);
        }
        DataPermission.addCurrentUser(sjtssqDto, getLoginInfo(request));
        List<SjtssqDto> listMap = sjtssqService.getPagedAuditApplication(sjtssqDto);
        map.put("total", sjtssqDto.getTotalNumber());
        map.put("rows", listMap);
        return map;
    }
    /**
     * 	根据项目查询子项目
     * @param jcsjDto
     * @param request
     * @return
     */
    @RequestMapping("/pagedataApplicationTypes")
    @ResponseBody
    public Map<String, Object> getApplicationTypes(JcsjDto jcsjDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        List<JcsjDto> dtoList = jcsjService.getDtoList(jcsjDto);
        List<JcsjDto> reasonList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.APPLICATION_REASON.getCode());
        map.put("jcsjlist", dtoList);
        map.put("reasonlist", reasonList);
        return map;
    }

    /**
     * 驳回
     * @return
     */
    @RequestMapping(value = "/application/rejectApplication")
    public ModelAndView rejectApplication(SjtssqDto sjtssqDto) {
        ModelAndView mav =new ModelAndView("wechat/application/application_reject");
        SjtssqDto dtoById = sjtssqService.getDto(sjtssqDto);
        mav.addObject("sjtssqDto",dtoById);
        mav.addObject("formAction", "/application/application/rejectSaveApplication");
        return mav;
    }

    /**
     * 	驳回保存
     * @param sjtssqDto
     * @return
     */
    @RequestMapping("/application/rejectSaveApplication")
    @ResponseBody
    public Map<String, Object> rejectSaveApplication(SjtssqDto sjtssqDto,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        boolean isSuccess = sjtssqService.rejectSaveApplication(sjtssqDto,user);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }
}
