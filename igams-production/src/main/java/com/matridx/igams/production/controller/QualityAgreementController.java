package com.matridx.igams.production.controller;

import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.cloud.controller.BaseBasicController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.JgxxDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.DataPermissionTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IJgxxService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.production.dao.entities.WlglDto;
import com.matridx.igams.production.dao.entities.ZlxyDto;
import com.matridx.igams.production.dao.entities.ZlxymxDto;
import com.matridx.igams.production.service.svcinterface.IWlglService;
import com.matridx.igams.production.service.svcinterface.IZlxyService;
import com.matridx.igams.production.service.svcinterface.IZlxymxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
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
@RequestMapping("/agreement")
public class QualityAgreementController extends BaseBasicController {

    @Autowired
    private IZlxyService zlxyService;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Override
    public String getPrefix() {
        return urlPrefix;
    }
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    private IZlxymxService zlxymxService;
    @Autowired
    IFjcfbService fjcfbService;
    @Autowired
    private IWlglService wlglService;
    @Autowired
    IXxglService xxglService;
    @Autowired
    IJgxxService jgxxService;
    @Autowired
    IJcsjService jcsjService;
    /**
     * 质量协议列表
     */
    @RequestMapping("/agreement/pageListAgreement")
    public ModelAndView pageListAgreement() {
        ModelAndView mav = new ModelAndView("production/quality/quality_list");
        List<JcsjDto> gfgllblist=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.SUPPLIER_TYPE.getCode());//供应商管理类别
        List<JcsjDto> sjxmhlist=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PRODUCTION_TYPE.getCode());//涉及项目号
        mav.addObject("gfgllblist",gfgllblist);
        mav.addObject("sjxmhlist",sjxmhlist);
        mav.addObject("auditType",AuditTypeEnum.AUDIT_QUALITYAGREEMENT.getCode());
        mav.addObject("urlPrefix",urlPrefix);
        return mav;
    }

    /**
     * 质量协议获取数据
     */
    @RequestMapping("/agreement/pageGetListAgreement")
    @ResponseBody
    public Map<String, Object> pageGetListAgreement(ZlxyDto zlxyDto) {
        Map<String, Object> map = new HashMap<>();
        List<ZlxyDto> list = zlxyService.getPagedDtoList(zlxyDto);
        map.put("total", zlxyDto.getTotalNumber());
        map.put("rows", list);
        return map;
    }
    /**
     * 质量协议列表查看
     */
    @RequestMapping("/agreement/viewAgreement")
    public ModelAndView viewAgreement(ZlxyDto zlxyDto) {
        ModelAndView mav = new ModelAndView("production/quality/quality_view");
        zlxyDto=zlxyService.getDtoById(zlxyDto.getZlxyid());
        FjcfbDto fjcfbDto=new FjcfbDto();
        fjcfbDto.setYwid(zlxyDto.getZlxyid());
        fjcfbDto.setYwlx(BusTypeEnum.IMP_QUALITY_AGREEMENT_FILE.getCode());
        List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        fjcfbDto.setYwlx(BusTypeEnum.IMP_UPQUALITYCONTRACT.getCode());
        List<FjcfbDto> fjcfbDtos_t = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        mav.addObject("t_fjcfbDtos",fjcfbDtos);
        mav.addObject("fjcfbDtos_t",fjcfbDtos_t);
        mav.addObject("zlxyDto",zlxyDto);
        mav.addObject("urlPrefix",urlPrefix);
        return mav;
    }
    /**
     * 质量协议查看明细获取数据
     */
    @RequestMapping("/agreement/pagedataAgreementMx")
    @ResponseBody
    public Map<String, Object> pagedataAgreementMx(ZlxymxDto zlxymxDto) {
        Map<String, Object> map = new HashMap<>();
        List<ZlxymxDto> list=new ArrayList<>();
        if(StringUtils.isNotBlank(zlxymxDto.getZlxyid())){
            list=zlxymxService.getDtoList(zlxymxDto);
        }
        List<JcsjDto> sjxmhlist=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PRODUCTION_TYPE.getCode());//涉及项目号
        for (ZlxymxDto zlxymxDto1:list){
            StringBuilder sjxmhmc= new StringBuilder();
            if (StringUtil.isNotBlank(zlxymxDto1.getSjxmh())){
                zlxymxDto1.setSjxmhs(zlxymxDto1.getSjxmh().trim().split(","));
                for(int i=0;i<zlxymxDto1.getSjxmhs().length;i++) {
                    if(!CollectionUtils.isEmpty(sjxmhlist)) {
                        for (JcsjDto jcsjDto : sjxmhlist) {
                            if (jcsjDto.getCsid().equals(zlxymxDto1.getSjxmhs()[i]))
                                sjxmhmc.append(",").append(jcsjDto.getCsmc());
                        }
                    }
                }
            }
            if (sjxmhmc.length()>0){
                sjxmhmc = new StringBuilder(sjxmhmc.substring(1));
            }
            zlxymxDto1.setSjxmhmc(sjxmhmc.toString());
        }
        map.put("rows", list);
        return map;
    }
    /**
     * 质量协议列表新增
     */
    @RequestMapping("/agreement/addAgreement")
    public ModelAndView addAgreement(ZlxyDto zlxyDto,HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("production/quality/quality_add");
        User user=getLoginInfo(request);
        zlxyDto.setFzrmc(user.getZsxm());
        zlxyDto.setFzr(user.getYhid());
        zlxyDto.setCjsj(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd"));
        mav.addObject("htlxlist",redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.CONTRACT_TYPE.getCode()));
        mav.addObject("zlxyDto",zlxyDto);
        mav.addObject("ywlx",BusTypeEnum.IMP_QUALITY_AGREEMENT_FILE.getCode());
        mav.addObject("formAction","addSaveAgreement");
        mav.addObject("urlPrefix",urlPrefix);
        return mav;
    }
    /**
     * 正式合同上传页面
     */
    @RequestMapping(value = "/agreement/formalAgreementContract")
    public ModelAndView formalAgreementContract(ZlxyDto zlxyDto) {
        ModelAndView mav = new ModelAndView("production/quality/contract_upload");
        // 查询合同信息
        ZlxyDto t_zlxyDto = zlxyService.getDtoById(zlxyDto.getZlxyid());
        FjcfbDto fjcfbDto = new FjcfbDto();
        fjcfbDto.setYwlx(BusTypeEnum.IMP_UPQUALITYCONTRACT.getCode());
        fjcfbDto.setYwid(zlxyDto.getZlxyid());
        List<FjcfbDto> t_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        mav.addObject("fjcfbDtos",t_fjcfbDtos);
        t_zlxyDto.setFormAction("formalSaveAgreementContract");
        mav.addObject("fjywlx", BusTypeEnum.IMP_UPQUALITYCONTRACT.getCode());
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("zlxyDto",t_zlxyDto);
        return mav;
    }

    /**
     *正式合同提交保存
     */
    @ResponseBody
    @RequestMapping(value = "/agreement/formalSaveAgreementContract")
    public Map<String, Object> formalSaveAgreementContract(ZlxyDto zlxyDto, HttpServletRequest request) {
        User user = getLoginInfo(request);
        zlxyDto.setXgry(user.getYhid());
        zlxyDto.setSzbj("1");
        Map<String, Object> map = new HashMap<>();
        if (CollectionUtils.isEmpty(zlxyDto.getFjids())) {
            map.put("status", "fail");
            map.put("message", "请上传双章合同!");
            return map;
        }
        boolean isSuccess = zlxyService.formalSaveAgreementContract(zlxyDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
                : xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }
    /**
     * 获取数据
     */
    @RequestMapping("/agreement/pagedataGetDetails")
    @ResponseBody
    public Map<String, Object> pageGetListAgreement(ZlxymxDto zlxymxDto) {
        Map<String, Object> map = new HashMap<>();
        List<ZlxymxDto> list = new ArrayList<>();
        if(StringUtils.isNotBlank(zlxymxDto.getGysid())){
            list = zlxymxService.getPagedDtoList(zlxymxDto);
        }
        List<JcsjDto> sjxmhlist=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PRODUCTION_TYPE.getCode());//涉及项目号
        for (ZlxymxDto zlxymxDto1:list){
            StringBuilder sjxmhmc= new StringBuilder();
            if (StringUtil.isNotBlank(zlxymxDto1.getSjxmh())){
                zlxymxDto1.setSjxmhs(zlxymxDto1.getSjxmh().trim().split(","));
                for(int i=0;i<zlxymxDto1.getSjxmhs().length;i++) {
                    if(!CollectionUtils.isEmpty(sjxmhlist)) {
                        for (JcsjDto jcsjDto : sjxmhlist) {
                            if (jcsjDto.getCsid().equals(zlxymxDto1.getSjxmhs()[i]))
                                sjxmhmc.append(",").append(jcsjDto.getCsmc());
                        }
                    }
                }
            }
            if (sjxmhmc.length()>0){
                sjxmhmc = new StringBuilder(sjxmhmc.substring(1));
            }
            zlxymxDto1.setSjxmhmc(sjxmhmc.toString());
        }
        map.put("total", zlxymxDto.getTotalNumber());
        map.put("rows", list);
        return map;
    }

    /**
     * 操作明细
     */
    @RequestMapping("/agreement/pagedataOperateDetail")
    @ResponseBody
    public Map<String, Object> pagedataOperateDetail(ZlxymxDto zlxymxDto,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user=getLoginInfo(request);
        zlxymxDto.setXgry(user.getYhid());
        boolean isSuccess = zlxymxService.updateZt(zlxymxDto);
        map.put("status", isSuccess ? "success" : "fail");
        return map;
    }

    /**
     * 获取合同附件
     */
    @RequestMapping("/agreement/pagedataGetDocuments")
    @ResponseBody
    public Map<String, Object> pagedataGetDocuments(ZlxyDto zlxyDto) {
        Map<String, Object> map = new HashMap<>();
        List<ZlxyDto> dtoList = zlxyService.getDtoList(zlxyDto);
        List<FjcfbDto> fjcfbDtos =new ArrayList<>();
        if(!CollectionUtils.isEmpty(dtoList)){
            List<String> ids =new ArrayList<>();
            for(ZlxyDto dto:dtoList){
                if(StringUtil.isNotBlank(zlxyDto.getZlxyid())){
                    if(!zlxyDto.getZlxyid().equals(dto.getZlxyid())){
                        ids.add(dto.getZlxyid());
                    }
                }else{
                    ids.add(dto.getZlxyid());
                }

            }
            if(!CollectionUtils.isEmpty(ids)){
                FjcfbDto fjcfbDto=new FjcfbDto();
                fjcfbDto.setYwids(ids);
                fjcfbDto.setYwlx(BusTypeEnum.IMP_QUALITY_AGREEMENT_FILE.getCode());
                fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidsAndYwlx(fjcfbDto);
            }

        }
        map.put("fjcfbDtos",fjcfbDtos);
        return map;
    }

    /**
     * 选择物料列表
     */
    @RequestMapping("/agreement/pagedataListMaterial")
    public ModelAndView pagedataListMaterial() {
        ModelAndView mav = new ModelAndView("production/quality/quality_addMaterial");
        mav.addObject("urlPrefix",urlPrefix);
        return mav;
    }

    /**
     * 选择物料列表
     */
    @RequestMapping("/agreement/pagedataGetListMaterial")
    @ResponseBody
    public Map<String, Object> pagedataGetListMaterial(WlglDto wlglDto) {
        Map<String, Object> map = new HashMap<>();
        List<WlglDto> list = wlglService.getPagedDtoList(wlglDto);
        map.put("total", wlglDto.getTotalNumber());
        map.put("rows", list);
        return map;
    }

    /**
     * 新增保存
     */
    @RequestMapping("/agreement/addSaveAgreement")
    @ResponseBody
    public Map<String, Object> addSaveAgreement(ZlxyDto zlxyDto,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user=getLoginInfo(request);
        zlxyDto.setLrry(user.getYhid());
        // 判断协议编号重复
        boolean isSuccess = zlxyService.isZlxybhRepeat(zlxyDto.getZlxybh(), zlxyDto.getZlxyid());
        if(!isSuccess) {
            map.put("status", "fail");
            map.put("message", "协议编号重复，请重新输入！");
            return map;
        }
        isSuccess = zlxyService.addSaveAgreement(zlxyDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("auditType", AuditTypeEnum.AUDIT_QUALITYAGREEMENT.getCode());
        map.put("ywid", zlxyDto.getZlxyid());
        map.put("urlPrefix", urlPrefix);
        map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 获取物料明细
     */
    @RequestMapping("/agreement/pagedataGetMaterialDetails")
    @ResponseBody
    public Map<String, Object> pagedataGetMaterialDetails(WlglDto wlglDto) {
        Map<String, Object> map = new HashMap<>();
        List<WlglDto> list = wlglService.getOrderedDtoList(wlglDto);
        List<JcsjDto> sjxmhlist=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PRODUCTION_TYPE.getCode());//涉及项目号
        for (WlglDto wlglDto1:list){
            StringBuilder sjxmhmc= new StringBuilder();
            if (StringUtil.isNotBlank(wlglDto1.getSscplb())){
                wlglDto1.setSscplbs(wlglDto1.getSscplb().trim().split(","));
                for(int i=0;i<wlglDto1.getSscplbs().length;i++) {
                    if(!CollectionUtils.isEmpty(sjxmhlist)) {
                        for (JcsjDto jcsjDto : sjxmhlist) {
                            if (jcsjDto.getCsid().equals(wlglDto1.getSscplbs()[i]))
                                sjxmhmc.append(",").append(jcsjDto.getCsmc());
                        }
                    }
                }
            }
            if (sjxmhmc.length()>0){
                sjxmhmc = new StringBuilder(sjxmhmc.substring(1));
            }
            wlglDto1.setSjxmhmc(sjxmhmc.toString());
        }
        map.put("wlglDtos", list);
        return map;
    }
    /**
     * 质量协议编号刷新
     */
    @ResponseBody
    @RequestMapping(value = "/agreement/pagedataGetAgreementContractCode")
    public Map<String, Object> getAgreementContractCode(ZlxyDto zlxyDto, HttpServletRequest request) {
        User user = getLoginInfo(request);
        Map<String, Object> map = new HashMap<>();
        // 根据合同类型基础数据参数扩展一 + - +部门参数扩展一+ - + 年月日+ - +基础数据参数扩展三 + - +两位流水号。
        String date = DateUtils.getCustomFomratCurrentDate("yyMMdd");
        String prefix;
        JcsjDto jcsjDto = jcsjService.getDtoById(zlxyDto.getHtlx());
        if (StringUtil.isBlank(jcsjDto.getCskz1())){
            map.put("status", "fail");
            map.put("message", "未配置基础数据参数扩展1");
            return map;
        }
        JgxxDto jgxxDto = new JgxxDto();
        jgxxDto.setJgid(user.getJgid());
        JgxxDto dtoById = jgxxService.selectJgxxByJgid(jgxxDto);
        if (StringUtil.isBlank(dtoById.getKzcs1())){
            map.put("status", "fail");
            map.put("message", "未配置机构参数扩展1");
            return map;
        }
        String serial;
        if (StringUtil.isNotBlank(zlxyDto.getYyxybh())){
            prefix  = zlxyDto.getYyxybh() + "-";
            serial = zlxyService.getAgreementContractSerial(prefix,"1");
        }else {
            prefix = jcsjDto.getCskz1() + "-" +dtoById.getKzcs1();
            if (StringUtil.isNotBlank(jcsjDto.getCskz3())){
                prefix = prefix + jcsjDto.getCskz3();
            }
            prefix = prefix + "-" + date + "-";
            serial = zlxyService.getAgreementContractSerial(prefix,"2");
        }
        if (StringUtil.isNotBlank(zlxyDto.getYyxybh())){
            serial = serial + "("+DateUtils.getCustomFomratCurrentDate("MMdd")+")";
        }
        // 查询流水号
        map.put("status", "success");
        map.put("message", prefix + serial);
        return map;
    }
    /**
     * 补充合同 质量协议
     */
    @RequestMapping("/agreement/supplementAgreement")
    public ModelAndView supplementAgreement(ZlxyDto zlxyDto,HttpServletRequest request) {
        zlxyDto.setFormAction("supplementSaveAgreement");
        return modAgreement(zlxyDto,request);
    }
    /**
     * 质量协议列表修改
     */
    @RequestMapping("/agreement/modAgreement")
    public ModelAndView modAgreement(ZlxyDto zlxyDto,HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("production/quality/quality_add");
        ZlxyDto dto = zlxyService.getDtoById(zlxyDto.getZlxyid());
        FjcfbDto fjcfbDto=new FjcfbDto();
        fjcfbDto.setYwid(zlxyDto.getZlxyid());
        fjcfbDto.setYwlx(BusTypeEnum.IMP_QUALITY_AGREEMENT_FILE.getCode());
        List<FjcfbDto> fjcfbDtos = fjcfbService.getDtoList(fjcfbDto);
        if ("supplementSaveAgreement".equals(zlxyDto.getFormAction())){
            dto.setYyxybh(dto.getZlxybh());
            dto.setZlxybh(null);
            dto.setBchtid(dto.getZlxyid());
            dto.setZlxyid(null);
            dto.setCjsj(null);
            dto.setDqsj(null);
            dto.setKssj(null);
            dto.setWbxybh(null);
            Map<String, Object> agreementContractCode = getAgreementContractCode(dto, request);
            if ("success".equals(String.valueOf(agreementContractCode.get("status")))){
                dto.setZlxybh(String.valueOf(agreementContractCode.get("message")));
            }
        }else {
            zlxyDto.setFormAction("modSaveAgreement");
        }
        mav.addObject("htlxlist",redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.CONTRACT_TYPE.getCode()));
        mav.addObject("fjcfbDtos",fjcfbDtos);
        mav.addObject("zlxyDto",dto);
        mav.addObject("ywlx",BusTypeEnum.IMP_QUALITY_AGREEMENT_FILE.getCode());
        mav.addObject("formAction",zlxyDto.getFormAction());
        mav.addObject("urlPrefix",urlPrefix);
        return mav;
    }
    /**
     * 质量协议列表高级修改
     */
    @RequestMapping("/agreement/advancedmodAgreement")
    public ModelAndView advancedmodAgreement(ZlxyDto zlxyDto,HttpServletRequest request) {
        return modAgreement(zlxyDto,request);
    }
 /**
     * 质量协议列表 补充合同保存
     */
    @RequestMapping("/agreement/supplementSaveAgreement")
    @ResponseBody
    public Map<String, Object> supplementSaveAgreement(ZlxyDto zlxyDto,HttpServletRequest request) {
        return addSaveAgreement(zlxyDto,request);
    }

    /**
     * 修改保存
     */
    @RequestMapping("/agreement/modSaveAgreement")
    @ResponseBody
    public Map<String, Object> modSaveAgreement(ZlxyDto zlxyDto,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user=getLoginInfo(request);
        zlxyDto.setXgry(user.getYhid());
        // 判断协议编号重复
        boolean isSuccess = zlxyService.isZlxybhRepeat(zlxyDto.getZlxybh(), zlxyDto.getZlxyid());
        if(!isSuccess) {
            map.put("status", "fail");
            map.put("message", "协议编号重复，请重新输入！");
            return map;
        }
        isSuccess = zlxyService.modSaveAgreement(zlxyDto);
        map.put("urlPrefix", urlPrefix);
        map.put("auditType", AuditTypeEnum.AUDIT_QUALITYAGREEMENT.getCode());
        map.put("ywid", zlxyDto.getZlxyid());
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 删除
     */
    @RequestMapping("/agreement/delAgreement")
    @ResponseBody
    public Map<String, Object> delAgreement(ZlxyDto zlxyDto,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user=getLoginInfo(request);
        zlxyDto.setScry(user.getYhid());
        boolean isSuccess = zlxyService.delAgreement(zlxyDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?(xxglService.getModelById("ICOM00003").getXxnr()):xxglService.getModelById("ICOM00004").getXxnr());
        return map;
    }
    /**
     * 质量协议列表提交
     */
    @RequestMapping("/agreement/submitAgreement")
    public ModelAndView submitAgreement(ZlxyDto zlxyDto) {
        ModelAndView mav = new ModelAndView("production/quality/quality_add");
        ZlxyDto dto = zlxyService.getDtoById(zlxyDto.getZlxyid());
        FjcfbDto fjcfbDto=new FjcfbDto();
        fjcfbDto.setYwid(zlxyDto.getZlxyid());
        fjcfbDto.setYwlx(BusTypeEnum.IMP_QUALITY_AGREEMENT_FILE.getCode());
        List<FjcfbDto> fjcfbDtos = fjcfbService.getDtoList(fjcfbDto);
        mav.addObject("htlxlist",redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.CONTRACT_TYPE.getCode()));
        mav.addObject("fjcfbDtos",fjcfbDtos);
        mav.addObject("zlxyDto",dto);
        mav.addObject("ywlx",BusTypeEnum.IMP_QUALITY_AGREEMENT_FILE.getCode());
        mav.addObject("formAction","submitSaveAgreement");
        mav.addObject("urlPrefix",urlPrefix);
        return mav;
    }
    /**
     * 提交保存
     */
    @RequestMapping("/agreement/submitSaveAgreement")
    @ResponseBody
    public Map<String, Object> submitSaveAgreement(ZlxyDto zlxyDto,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user=getLoginInfo(request);
        zlxyDto.setXgry(user.getYhid());
        // 判断协议编号重复
        boolean isSuccess = zlxyService.isZlxybhRepeat(zlxyDto.getZlxybh(), zlxyDto.getZlxyid());
        if(!isSuccess) {
            map.put("status", "fail");
            map.put("message", "协议编号重复，请重新输入！");
            return map;
        }
        isSuccess = zlxyService.modSaveAgreement(zlxyDto);
        map.put("auditType",AuditTypeEnum.AUDIT_QUALITYAGREEMENT.getCode());
        map.put("ywid", zlxyDto.getZlxyid());
        map.put("status", isSuccess?"success":"fail");
        map.put("urlPrefix", urlPrefix);
        map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }
    /**
     * 质量协议审核列表
     */
    @RequestMapping("/agreement/pageListAuditAgreement")
    public ModelAndView pageListAuditAgreement() {
        ModelAndView mav = new ModelAndView("production/quality/quality_audit_list");
        List<JcsjDto> gfgllblist=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.SUPPLIER_TYPE.getCode());//供应商管理类别g
        mav.addObject("gfgllblist",gfgllblist);
        mav.addObject("auditType",AuditTypeEnum.AUDIT_QUALITYAGREEMENT.getCode());
        mav.addObject("urlPrefix",urlPrefix);
        return mav;
    }
    /**
     * 	审核列表数据
     */
    @RequestMapping("/agreement/pageGetListAuditAgreement")
    @ResponseBody
    public Map<String, Object> pageGetListAuditAgreement(ZlxyDto zlxyDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        // 附加委托参数
        DataPermission.addWtParam(zlxyDto);
        // 附加审核状态过滤
        if (GlobalString.AUDIT_SHZT_YSH.equals(zlxyDto.getDqshzt())) {
            DataPermission.add(zlxyDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "zlxy", "zlxyid",
                    AuditTypeEnum.AUDIT_QUALITYAGREEMENT);
        } else {
            DataPermission.add(zlxyDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "zlxy", "zlxyid",
                    AuditTypeEnum.AUDIT_QUALITYAGREEMENT);
        }
        DataPermission.addCurrentUser(zlxyDto, getLoginInfo(request));
        List<ZlxyDto> listMap=zlxyService.getPagedAuditAgreement(zlxyDto);
        map.put("total", zlxyDto.getTotalNumber());
        map.put("rows", listMap);
        return map;
    }
    /**
     * 质量协议审核
     */
    @RequestMapping("/agreement/auditAgreement")
    public ModelAndView auditAgreement(ZlxyDto zlxyDto,HttpServletRequest request) {
        return modAgreement(zlxyDto,request);
    }
    /**
     * 	质量协议数据
     */
    @RequestMapping("/agreement/pagedataAgreement")
    @ResponseBody
    public Map<String, Object> pagedataAgreement(ZlxyDto zlxyDto) {
        Map<String, Object> map = new HashMap<>();
        List<ZlxyDto> listMap=zlxyService.getDtoList(zlxyDto);
        map.put("rows", listMap);
        return map;
    }
    /**
     * 附件查看
     */
    @RequestMapping("/agreement/pagedataViewFj")
    public ModelAndView uploadQyxxFile(FjcfbDto fjcfbDto) {
        ModelAndView mav=new ModelAndView("production/retention/retention_uploadQyxxFile");
        List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        mav.addObject("fjcfbDtos", fjcfbDtos);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 生成合同
     */
    @RequestMapping("/agreement/contractAgreemen")
    public ModelAndView contractAgreemen(ZlxyDto zlxyDto) {
        ModelAndView mav=new ModelAndView("production/quality/contract_generate");
        mav.addObject("zlxyDto",zlxyDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 替换合同模板
     */
    @RequestMapping("/agreement/pagedataReplaceContract")
    @ResponseBody
    public Map<String,Object> replaceContract(ZlxyDto zlxyDto){
        return zlxyService.getParamForContract(zlxyDto);
    }
    /**
     * 查询PDF版合同
     */
    @RequestMapping("/agreement/pagedataGetConttractPDF")
    @ResponseBody
    public Map<String,Object> pagedataGetConttractPDF(FjcfbDto fjcfbDto){
        Map<String,Object> map=new HashMap<>();
        FjcfbDto fjcfbDto_t=fjcfbService.getDto(fjcfbDto);
        map.put("fjcfbDto", fjcfbDto_t);
        return map;
    }
    /**
     * 选择涉及项目号
     */
    @RequestMapping("/agreement/pagedataChooseAgreement")
    public ModelAndView pagedataChooseAgreement(ZlxyDto zlxyDto) {
        ModelAndView mav=new ModelAndView("production/quality/quality_choose");
        List<JcsjDto> sjxmhlist=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PRODUCTION_TYPE.getCode());//涉及项目号
        if(StringUtil.isNotBlank(zlxyDto.getSjxmh())) {
            zlxyDto.setSjxmhs(zlxyDto.getSjxmh().trim().split(","));
            for(int i=0;i<zlxyDto.getSjxmhs().length;i++) {
                if(!CollectionUtils.isEmpty(sjxmhlist)) {
                    for (JcsjDto jcsjDto : sjxmhlist) {
                        if (jcsjDto.getCsid().equals(zlxyDto.getSjxmhs()[i]))
                            jcsjDto.setChecked("1");
                    }
                }
            }
        }
        mav.addObject("zlxyDto",zlxyDto);
        mav.addObject("sjxmhlist",sjxmhlist);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 质量协议明细
     */
    @RequestMapping("/agreement/pagedataGetAgreementDetails")
    @ResponseBody
    public Map<String,Object> pagedataGetAgreementDetails(ZlxymxDto zlxymxDto){
        Map<String,Object> map=new HashMap<>();
        zlxymxDto=zlxymxService.getDtoById(zlxymxDto.getZlxymxid());
        List<JcsjDto> sjxmhlist=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PRODUCTION_TYPE.getCode());//涉及项目号
        StringBuilder sjxmhmc= new StringBuilder();
        if (StringUtil.isNotBlank(zlxymxDto.getSjxmh())){
            zlxymxDto.setSjxmhs(zlxymxDto.getSjxmh().trim().split(","));
            for(int i=0;i<zlxymxDto.getSjxmhs().length;i++) {
                if(!CollectionUtils.isEmpty(sjxmhlist)) {
                    for (JcsjDto jcsjDto : sjxmhlist) {
                        if (jcsjDto.getCsid().equals(zlxymxDto.getSjxmhs()[i]))
                            sjxmhmc.append(",").append(jcsjDto.getCsmc());
                    }
                }
            }
        }
        if (sjxmhmc.length()>0){
            sjxmhmc = new StringBuilder(sjxmhmc.substring(1));
        }
        zlxymxDto.setSjxmhmc(sjxmhmc.toString());
        map.put("zlxymxDto", zlxymxDto);
        return map;
    }
}
