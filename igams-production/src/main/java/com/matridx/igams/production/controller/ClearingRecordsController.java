package com.matridx.igams.production.controller;

import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.DataPermissionTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.production.dao.entities.QcjlDto;
import com.matridx.igams.production.dao.entities.QcxmDto;
import com.matridx.igams.production.service.svcinterface.IQcjlService;
import com.matridx.igams.production.service.svcinterface.IQcxmService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/clearing")
public class ClearingRecordsController extends BaseController {

    @Autowired
    private IQcjlService qcjlService;
    @Autowired
    private IQcxmService qcxmService;
    @Autowired
    IXxglService xxglService;
    @Autowired
    RedisUtil redisUtil;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Override
    public String getPrefix(){
        return urlPrefix;
    }

    @Autowired
    IJcsjService jcsjService;

    /**
     * 清场记录列表页面
     */
    @RequestMapping(value = "/records/pageListClearingRecords")
    public ModelAndView pageListClearingRecords(QcjlDto qcjlDto) {
        ModelAndView mav = new ModelAndView("clearing/records/clearingRecords_list");
        qcjlDto.setAuditType(AuditTypeEnum.AUDIT_CLEARING_RECORDS.getCode());
        mav.addObject("qcjlDto", qcjlDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 清场记录列表
     */
    @RequestMapping(value = "/records/pageGetListClearingRecords")
    @ResponseBody
    public Map<String, Object> pageGetListClearingRecords(QcjlDto qcjlDto) {
        Map<String, Object> map = new HashMap<>();
        List<QcjlDto> list = qcjlService.getPagedDtoList(qcjlDto);
        map.put("total", qcjlDto.getTotalNumber());
        map.put("rows", list);
        return map;
    }

    /**
     * 清场记录审核列表
     */
    @RequestMapping("/records/pageListAuditClearingRecords")
    public ModelAndView pageListAuditClearingRecords() {
        ModelAndView mav = new ModelAndView("clearing/records/clearingRecords_auditList");
        mav.addObject("auditType",AuditTypeEnum.AUDIT_CLEARING_RECORDS.getCode());
        mav.addObject("urlPrefix",urlPrefix);
        return mav;
    }
    /**
     * 	清场记录审核列表数据
     */
    @RequestMapping("/records/pageGetListAuditClearingRecords")
    @ResponseBody
    public Map<String, Object> pageGetListAuditClearingRecords(QcjlDto qcjlDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        // 附加委托参数
        DataPermission.addWtParam(qcjlDto);
        // 附加审核状态过滤
        if (GlobalString.AUDIT_SHZT_YSH.equals(qcjlDto.getDqshzt())) {
            DataPermission.add(qcjlDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "qcjl", "qcjlid",
                    AuditTypeEnum.AUDIT_CLEARING_RECORDS);
        } else {
            DataPermission.add(qcjlDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "qcjl", "qcjlid",
                    AuditTypeEnum.AUDIT_CLEARING_RECORDS);
        }
        DataPermission.addCurrentUser(qcjlDto, getLoginInfo(request));
        List<QcjlDto> listMap=qcjlService.getPagedAuditClearingRecords(qcjlDto);
        map.put("total", qcjlDto.getTotalNumber());
        map.put("rows", listMap);
        return map;
    }

    /**
     * 清场记录 查看页面
     */
    @RequestMapping(value = "/records/viewClearingRecords")
    public ModelAndView viewClearingRecords(QcjlDto qcjlDto) {
        ModelAndView mav = new ModelAndView("clearing/records/clearingRecords_view");
        QcjlDto dto = qcjlService.getDto(qcjlDto);
        QcxmDto qcxmDto=new QcxmDto();
        qcxmDto.setQcjlid(qcjlDto.getQcjlid());
        List<QcxmDto> qcxmDtos = qcxmService.getDtoList(qcxmDto);
        mav.addObject("qcjlDto", dto);
        mav.addObject("qcxmDtos", qcxmDtos);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 清场记录  删除
     */
    @RequestMapping(value = "/records/delClearingRecords")
    @ResponseBody
    public Map<String, Object> delClearingRecords(QcjlDto qcjlDto,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        qcjlDto.setScry(user.getYhid());
        boolean isSuccess = qcjlService.delClearingRecords(qcjlDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?(xxglService.getModelById("ICOM00003").getXxnr()):xxglService.getModelById("ICOM00004").getXxnr());
        return map;
    }

    /**
     * 清场记录列表 清场
     */
    @RequestMapping("/records/clearClearingRecords")
    public ModelAndView clearClearingRecords(QcjlDto qcjlDto) {
        ModelAndView mav = new ModelAndView("clearing/records/clearingRecords_clear");
        qcjlDto.setFormAction("clearSaveClearingRecords");
        qcjlDto.setQcrq(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd"));
        qcjlDto.setJlbh(qcjlService.generateJlbh());
        qcjlDto.setYxq(DateUtils.format(DateUtils.getDate(new Date(), 7), "yyyy-MM-dd"));
        mav.addObject("qcjlDto", qcjlDto);
        mav.addObject("auditType",AuditTypeEnum.AUDIT_CLEARING_RECORDS.getCode());
        mav.addObject("urlPrefix",urlPrefix);
        return mav;
    }

    /**
     * 清场记录 清场保存
     */
    @RequestMapping(value = "/records/clearSaveClearingRecords")
    @ResponseBody
    public Map<String, Object> clearSaveClearingRecords(QcjlDto qcjlDto,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        qcjlDto.setLrry(user.getYhid());
        boolean isSuccess = qcjlService.clearSaveClearingRecords(qcjlDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
        map.put("auditType",AuditTypeEnum.AUDIT_CLEARING_RECORDS.getCode());
        map.put("ywid",qcjlDto.getQcjlid());
        return map;
    }

    /**
     * 清场记录列表 修改
     */
    @RequestMapping("/records/modClearingRecords")
    public ModelAndView modClearingRecords(QcjlDto qcjlDto) {
        ModelAndView mav = new ModelAndView("clearing/records/clearingRecords_clear");
        QcjlDto dto = qcjlService.getDto(qcjlDto);
        dto.setFormAction("modSaveClearingRecords");
        mav.addObject("qcjlDto", dto);
        mav.addObject("auditType",AuditTypeEnum.AUDIT_CLEARING_RECORDS.getCode());
        mav.addObject("urlPrefix",urlPrefix);
        return mav;
    }

    /**
     * 清场记录 修改保存
     */
    @RequestMapping(value = "/records/modSaveClearingRecords")
    @ResponseBody
    public Map<String, Object> modSaveClearingRecords(QcjlDto qcjlDto,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        qcjlDto.setXgry(user.getYhid());
        boolean isSuccess = qcjlService.modSaveClearingRecords(qcjlDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
        map.put("auditType",AuditTypeEnum.AUDIT_CLEARING_RECORDS.getCode());
        map.put("ywid",qcjlDto.getQcjlid());
        return map;
    }

    /**
     * 获取清场项目明细
     */
    @RequestMapping(value = "/records/pagedataGetDetails")
    @ResponseBody
    public Map<String, Object> pagedataGetDetails(QcxmDto qcxmDto) {
        Map<String, Object> map = new HashMap<>();
        List<QcxmDto> list =new ArrayList<>();
        if(StringUtil.isNotBlank(qcxmDto.getQcjlid())){
            list=qcxmService.getDtoList(qcxmDto);
        }else{
            List<JcsjDto> jcsjDtos = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.CLEARING_PROJECT.getCode());
            if(jcsjDtos!=null&&!jcsjDtos.isEmpty()){
                for(JcsjDto dto:jcsjDtos){
                    QcxmDto qcxmDto_t=new QcxmDto();
                    qcxmDto_t.setXmmc(dto.getCsmc());
                    qcxmDto_t.setXmcskz1(dto.getCskz1());
                    qcxmDto_t.setXmid(dto.getCsid());
                    list.add(qcxmDto_t);
                }
            }
        }
        map.put("rows", list);
        return map;
    }

    /**
     * 清场记录列表 审核
     */
    @RequestMapping("/records/auditClearingRecords")
    public ModelAndView auditClearingRecords(QcjlDto qcjlDto) {
        ModelAndView mav = new ModelAndView("clearing/records/clearingRecords_clear");
        QcjlDto dto = qcjlService.getDto(qcjlDto);
        dto.setFormAction("auditSaveClearingRecords");
        mav.addObject("qcjlDto", dto);
        mav.addObject("auditType",AuditTypeEnum.AUDIT_CLEARING_RECORDS.getCode());
        mav.addObject("urlPrefix",urlPrefix);
        return mav;
    }

    /**
     * 打开打印到货验收单页面
     */
    @RequestMapping("/records/printRecords")
    public ModelAndView printRecords(QcjlDto qcjlDto){
        ModelAndView mav;
        mav=new ModelAndView("clearing/records/clearingRecords_print");
        JcsjDto jcsj = new JcsjDto();
        jcsj.setCsdm("QCJL");
        jcsj.setJclb(BasicDataTypeEnum.DOCUMENTNUM.getCode());
        jcsj = jcsjService.getDto(jcsj);
        mav.addObject("dqmc",StringUtil.isNotBlank(jcsj.getCskz3())?jcsj.getCskz3():"");
        mav.addObject("wjbh", StringUtil.isNotBlank(jcsj.getCsmc())?jcsj.getCsmc():"");
        QcjlDto qcjl = qcjlService.getDto(qcjlDto);
        SimpleDateFormat sdfDO = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfD = new SimpleDateFormat("yyyy年MM月dd日");
        try {
            qcjl.setQcrq(sdfD.format(sdfDO.parse(qcjl.getQcrq())));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        QcxmDto qcxmDto = new QcxmDto();
        qcxmDto.setQcjlid(qcjlDto.getQcjlid());
        List<QcxmDto> qcxmList = qcxmService.getDtoList(qcxmDto);
        mav.addObject("qcjlDto", qcjl);
        mav.addObject("qcxmList", qcxmList);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 合格证打印
     */
    @RequestMapping("/records/certificateprintRecords")
    public ModelAndView certificateprintRecords(QcjlDto qcjlDto) {
        ModelAndView mav = new ModelAndView("clearing/records/clearingRecords_certificatePrint");
        JcsjDto jcsj = new JcsjDto();
        jcsj.setCsdm("QCHGZ");
        jcsj.setJclb(BasicDataTypeEnum.DOCUMENTNUM.getCode());
        jcsj = jcsjService.getDto(jcsj);
        mav.addObject("dqmc",StringUtil.isNotBlank(jcsj.getCskz3())?jcsj.getCskz3():"");
        mav.addObject("wjbh", StringUtil.isNotBlank(jcsj.getCsmc())?jcsj.getCsmc():"");
        mav.addObject("urlPrefix", urlPrefix);
        QcjlDto qcjl = qcjlService.getDto(qcjlDto);
        SimpleDateFormat sdfDO = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfD = new SimpleDateFormat("yyyy.MM.dd");
        try {
            qcjl.setQcrq(sdfD.format(sdfDO.parse(qcjl.getQcrq())));
            qcjl.setYxq(sdfD.format(sdfDO.parse(qcjl.getYxq())));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        mav.addObject("qcjlDto", qcjl);
        return mav;
    }

}
