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
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IJgxxService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.production.dao.entities.YxhtDto;
import com.matridx.igams.production.service.svcinterface.IYxhtService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author WYX
 * @version 1.0
 * @className MarketingContractController
 * @description TODO
 * @date 16:06 2023/3/31
 **/
@Controller
@RequestMapping("/marketingContract")
public class MarketingContractController extends BaseBasicController {
    @Autowired
    IYxhtService yxhtService;
    @Autowired
    IXxglService xxglService;
    @Autowired
    IJcsjService jcsjService;
    @Autowired
    RedisUtil redisUtil;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Autowired
    IFjcfbService fjcfbService;
    @Autowired
    ICommonService commonService;
    @Autowired
    IJgxxService jgxxService;
    @Override
    public String getPrefix(){
        return urlPrefix;
    }


    /**
     * 营销合同新增页面
     */
    @RequestMapping(value = "/marketingContract/addMarketingContract")
    public ModelAndView addMarketingContract(YxhtDto yxhtDto, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("marketingContract/marketingContract/marketingcontract_edit");
        List<JcsjDto> xslblist=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.MARKET_SELLING_TYPE.getCode());//销售类型
        List<JcsjDto> htlxList=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.MARKETING_CONTRACT_TYPE.getCode());//营销合同类型
        List<JcsjDto> yzlblist=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.CHAPTER_TYPE.getCode());//用章类别
        List<JcsjDto> htfxcdlist=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.CONTRACT_RISK.getCode());//合同风险程度
        List<JcsjDto> hkfslist=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PAYMENTBACK_METHOD.getCode());//回款方式
        List<JcsjDto> yxhtlxlist=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.YXHT_TYPE.getCode());
        User user = getLoginInfo(request);
        User userInfoById = commonService.getUserInfoById(user);
        yxhtDto.setHtjbr(userInfoById.getYhid());
        yxhtDto.setHtjbrmc(userInfoById.getZsxm());
        yxhtDto.setHtjbbm(userInfoById.getJgid());
        yxhtDto.setHtjbbmmc(userInfoById.getJgmc());
        mav.addObject("ywlx", BusTypeEnum.IMP_MARKETING_CONTRACT.getCode());
        mav.addObject("htlxList", htlxList);
        mav.addObject("xslblist", xslblist);
        mav.addObject("yzlblist", yzlblist);
        mav.addObject("htfxcdlist", htfxcdlist);
        mav.addObject("hkfslist", hkfslist);
        yxhtDto.setFormAction("addSaveMarketingContract");
        mav.addObject("yxhtDto", yxhtDto);
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("yxhtlxlist", yxhtlxlist);
        return mav;
    }
    /**
     * 营销合同新增保存
     */
    @ResponseBody
    @RequestMapping(value = "/marketingContract/addSaveMarketingContract")
    public Map<String, Object> addSaveMarketingContract(YxhtDto yxhtDto, HttpServletRequest request) {
        User user = getLoginInfo(request);
        yxhtDto.setLrry(user.getYhid());
        yxhtDto.setZt(StatusEnum.CHECK_NO.getCode());
        yxhtDto.setSzht("0");
        yxhtDto.setHtqdzt("0");
        Map<String, Object> map = new HashMap<>();
        // 判断合同编号重复
        boolean isSuccess = yxhtService.isHtbhRepeat(yxhtDto.getHtbh(), yxhtDto.getHtid());
        if(!isSuccess) {
            map.put("status", "fail");
            map.put("message", "合同编号重复，请重新输入！");
            return map;
        }
        try {
            isSuccess = yxhtService.addSaveMarketingContract(yxhtDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
            map.put("ywid", yxhtDto.getHtid());
            map.put("urlPrefix", urlPrefix);
            map.put("auditType", AuditTypeEnum.AUDIT_MARKETING_CONTRACT.getCode());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
        }catch (Exception e) {
            map.put("status", "fail");
            map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
        }
        return map;
    }
    /**
     * 营销合同合同编号刷新
     */
    @ResponseBody
    @RequestMapping(value = "/marketingContract/pagedataGetMarketingContractCode")
    public Map<String, Object> getMarketingContractCode(YxhtDto yxhtDto) {
        Map<String, Object> map = new HashMap<>();
        // 根据合同类型基础数据参数扩展一 + - +部门参数扩展一+ - + 年月日+ - +两位流水号。
        String date = DateUtils.getCustomFomratCurrentDate("yyMMdd");
        String prefix;
        JcsjDto jcsjDto = jcsjService.getDtoById(yxhtDto.getHtlx());
        if (StringUtil.isBlank(jcsjDto.getCskz1())){
            map.put("status", "fail");
            map.put("message", "未配置基础数据参数扩展1");
            return map;
        }
        JgxxDto jgxxDto = new JgxxDto();
        jgxxDto.setJgid(yxhtDto.getHtjbbm());
        JgxxDto dtoById = jgxxService.selectJgxxByJgid(jgxxDto);
        if (StringUtil.isBlank(dtoById.getKzcs1())){
            map.put("status", "fail");
            map.put("message", "未配置机构参数扩展1");
            return map;
        }
        prefix = jcsjDto.getCskz1() + "-" +dtoById.getKzcs1();
        if (StringUtil.isNotBlank(jcsjDto.getCskz2())){
            prefix = prefix + jcsjDto.getCskz2();
        }
        prefix = prefix + "-" + date + "-";
        // 查询流水号
        String serial = yxhtService.getMarketingContractSerial(prefix);
        map.put("status", "success");
        map.put("message", prefix + serial);
        return map;
    }

    /**
     * 营销合同修改页面
     */
    @RequestMapping(value = "/marketingContract/modMarketingContract")
    public ModelAndView modMarketingContract(YxhtDto yxhtDto) {
        ModelAndView mav = new ModelAndView("marketingContract/marketingContract/marketingcontract_edit");
        YxhtDto dtoById = yxhtService.getDtoById(yxhtDto.getHtid());
        List<JcsjDto> xslblist=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.MARKET_SELLING_TYPE.getCode());//销售类型
        List<JcsjDto> htlxList=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.MARKETING_CONTRACT_TYPE.getCode());//营销合同类型
        List<JcsjDto> yzlblist=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.CHAPTER_TYPE.getCode());//用章类别
        List<JcsjDto> htfxcdlist=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.CONTRACT_RISK.getCode());//合同风险程度
        List<JcsjDto> hkfslist=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PAYMENTBACK_METHOD.getCode());//回款方式
        List<JcsjDto> yxhtlxlist=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.YXHT_TYPE.getCode());
        dtoById.setFormAction("modSaveMarketingContract");
        StringBuilder zsldmc = new StringBuilder();
        List<YxhtDto> zsldInfo = yxhtService.getInfoByZsld(dtoById);
        for (YxhtDto dto : zsldInfo) {
            zsldmc.append(",").append(dto.getZsxm());
        }
        dtoById.setZsldmc(zsldmc.substring(1));
        FjcfbDto fjcfbDto=new FjcfbDto();
        fjcfbDto.setYwid(yxhtDto.getHtid());
        fjcfbDto.setYwlx(BusTypeEnum.IMP_MARKETING_CONTRACT.getCode());
        List<FjcfbDto> fjcfbDtos = fjcfbService.getDtoList(fjcfbDto);
        mav.addObject("fjcfbDtos",fjcfbDtos);
        mav.addObject("ywlx", BusTypeEnum.IMP_MARKETING_CONTRACT.getCode());
        mav.addObject("yxhtDto", dtoById);
        mav.addObject("htlxList", htlxList);
        mav.addObject("xslblist", xslblist);
        mav.addObject("yzlblist", yzlblist);
        mav.addObject("htfxcdlist", htfxcdlist);
        mav.addObject("hkfslist", hkfslist);
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("yxhtlxlist", yxhtlxlist);
        return mav;
    }
    /**
     * 营销合同修改保存
     */
    @ResponseBody
    @RequestMapping(value = "/marketingContract/modSaveMarketingContract")
    public Map<String, Object> modSaveMarketingContract(YxhtDto yxhtDto, HttpServletRequest request) {
        User user = getLoginInfo(request);
        yxhtDto.setXgry(user.getYhid());
        Map<String, Object> map = new HashMap<>();
        // 判断合同编号重复
        boolean isSuccess = yxhtService.isHtbhRepeat(yxhtDto.getHtbh(), yxhtDto.getHtid());
        if(!isSuccess) {
            map.put("status", "fail");
            map.put("message", "合同编号重复，请重新输入！");
            return map;
        }
        try {
            isSuccess = yxhtService.modSaveMarketingContract(yxhtDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
        }catch (Exception e) {
            map.put("status", "fail");
            map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
        }
        return map;
    }
    /**
     * 营销合同提交页面
     */
    @RequestMapping(value = "/marketingContract/submitMarketingContract")
    public ModelAndView submitMarketingContract(YxhtDto yxhtDto) {
        ModelAndView mav = new ModelAndView("marketingContract/marketingContract/marketingcontract_edit");
        YxhtDto dtoById = yxhtService.getDtoById(yxhtDto.getHtid());
        List<JcsjDto> xslblist=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.MARKET_SELLING_TYPE.getCode());//销售类型
        List<JcsjDto> htlxList=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.MARKETING_CONTRACT_TYPE.getCode());//营销合同类型
        List<JcsjDto> yzlblist=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.CHAPTER_TYPE.getCode());//用章类别
        List<JcsjDto> htfxcdlist=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.CONTRACT_RISK.getCode());//合同风险程度
        List<JcsjDto> hkfslist=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PAYMENTBACK_METHOD.getCode());//回款方式
        List<JcsjDto> yxhtlxlist=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.YXHT_TYPE.getCode());
        mav.addObject("yxhtlxlist", yxhtlxlist);
        dtoById.setFormAction("submitSaveMarketingContract");
        StringBuilder zsldmc = new StringBuilder();
        List<YxhtDto> zsldInfo = yxhtService.getInfoByZsld(dtoById);
        for (YxhtDto dto : zsldInfo) {
            zsldmc.append(",").append(dto.getZsxm());
        }
        dtoById.setZsldmc(zsldmc.substring(1));
        FjcfbDto fjcfbDto=new FjcfbDto();
        fjcfbDto.setYwid(yxhtDto.getHtid());
        fjcfbDto.setYwlx(BusTypeEnum.IMP_MARKETING_CONTRACT.getCode());
        List<FjcfbDto> fjcfbDtos = fjcfbService.getDtoList(fjcfbDto);
        mav.addObject("fjcfbDtos",fjcfbDtos);
        mav.addObject("ywlx", BusTypeEnum.IMP_MARKETING_CONTRACT.getCode());
        mav.addObject("yxhtDto", dtoById);
        mav.addObject("htlxList", htlxList);
        mav.addObject("xslblist", xslblist);
        mav.addObject("yzlblist", yzlblist);
        mav.addObject("htfxcdlist", htfxcdlist);
        mav.addObject("hkfslist", hkfslist);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 营销合同提交保存
     */
    @ResponseBody
    @RequestMapping(value = "/marketingContract/submitSaveMarketingContract")
    public Map<String, Object> submitSaveMarketingContract(YxhtDto yxhtDto, HttpServletRequest request) {
        User user = getLoginInfo(request);
        yxhtDto.setXgry(user.getYhid());
        Map<String, Object> map = new HashMap<>();
        // 判断合同编号重复
        boolean isSuccess = yxhtService.isHtbhRepeat(yxhtDto.getHtbh(), yxhtDto.getHtid());
        if(!isSuccess) {
            map.put("status", "fail");
            map.put("message", "合同编号重复，请重新输入！");
            return map;
        }
        try {
            isSuccess = yxhtService.modSaveMarketingContract(yxhtDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
            map.put("ywid", yxhtDto.getHtid());
            map.put("urlPrefix", urlPrefix);
            map.put("auditType", AuditTypeEnum.AUDIT_MARKETING_CONTRACT.getCode());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
        }catch (Exception e) {
            map.put("status", "fail");
            map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
        }
        return map;
    }
    /**
     *  废弃营销合同信息
     */
    @RequestMapping("/marketingContract/discardMarketingContract")
    @ResponseBody
    public Map<String,Object> discardMarketingContract(YxhtDto yxhtDto, HttpServletRequest request){
        yxhtDto.setScbj("2");
        return delMarketingContract(yxhtDto,request);
    }
    /**
     *  删除营销合同信息
     */
    @RequestMapping("/marketingContract/delMarketingContract")
    @ResponseBody
    public Map<String,Object> delMarketingContract(YxhtDto yxhtDto, HttpServletRequest request){
        User user=getLoginInfo(request);
        yxhtDto.setScry(user.getYhid());
        Map<String,Object> map=new HashMap<>();
        boolean Success;
        //保存到货信息
        try {
            Success=yxhtService.delMarketingContract(yxhtDto);
            map.put("status", Success ? "success" : "fail");
            map.put("message", Success ? xxglService.getModelById("ICOM00003").getXxnr() : xxglService.getModelById("ICOM00004").getXxnr());
            map.put("urlPrefix",urlPrefix);
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00004").getXxnr());
            map.put("urlPrefix",urlPrefix);
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("message", xxglService.getModelById("ICOM00004").getXxnr());
            map.put("urlPrefix",urlPrefix);
        }
        return map;
    }
    /**
     * 正式合同上传页面
     */
    @RequestMapping(value = "/marketingContract/formalMarketingContract")
    public ModelAndView formalMarketingContract(YxhtDto yxhtDto) {
        ModelAndView mav = new ModelAndView("marketingContract/marketingContract/marketingContract_upload");
        // 查询合同信息
        YxhtDto t_yxhtDto = yxhtService.getDtoById(yxhtDto.getHtid());
        FjcfbDto fjcfbDto = new FjcfbDto();
        fjcfbDto.setYwlx(BusTypeEnum.IMP_UPLOADMARKETING_CONTRACT.getCode());
        fjcfbDto.setYwid(t_yxhtDto.getHtid());
        List<FjcfbDto> t_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        mav.addObject("fjcfbDtos",t_fjcfbDtos);
        t_yxhtDto.setFormAction("formalSaveMarketingContract");
        mav.addObject("fjywlx", BusTypeEnum.IMP_UPLOADMARKETING_CONTRACT.getCode());
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("yxhtDto",t_yxhtDto);
        return mav;
    }

    /**
     *正式合同提交保存
     */
    @ResponseBody
    @RequestMapping(value = "/marketingContract/formalSaveMarketingContract")
    public Map<String, Object> formalSaveMarketingContract(YxhtDto yxhtDto, HttpServletRequest request) {
        User user = getLoginInfo(request);
        yxhtDto.setXgry(user.getYhid());
        yxhtDto.setSzht("1");
        yxhtDto.setHtqdzt("1");
        Map<String, Object> map = new HashMap<>();
        if (CollectionUtils.isEmpty(yxhtDto.getFjids())) {
            map.put("status", "fail");
            map.put("message", "附件不允许为空");
            return map;
        }
        boolean Success;
        //保存信息
        try {
            Success = yxhtService.formalSaveMarketingContract(yxhtDto);
            map.put("status", Success ? "success" : "fail");
            map.put("message", Success ? xxglService.getModelById("ICOM00001").getXxnr(): xxglService.getModelById("ICOM00002").getXxnr());
            map.put("urlPrefix",urlPrefix);
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00004").getXxnr());
            map.put("urlPrefix",urlPrefix);
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("message", xxglService.getModelById("ICOM00004").getXxnr());
            map.put("urlPrefix",urlPrefix);
        }

        return map;
    }
    /**
     * 营销合同审核页面
     */
    @RequestMapping(value = "/marketingContract/auditMarketingContract")
    public ModelAndView auditMarketingContract(YxhtDto yxhtDto) {
        ModelAndView mav = new ModelAndView("marketingContract/marketingContract/marketingcontract_edit");
        YxhtDto dtoById = yxhtService.getDtoById(yxhtDto.getHtid());
        List<JcsjDto> xslblist=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.MARKET_SELLING_TYPE.getCode());//销售类型
        List<JcsjDto> htlxList=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.MARKETING_CONTRACT_TYPE.getCode());//营销合同类型
        List<JcsjDto> yzlblist=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.CHAPTER_TYPE.getCode());//用章类别
        List<JcsjDto> htfxcdlist=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.CONTRACT_RISK.getCode());//合同风险程度
        List<JcsjDto> hkfslist=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PAYMENTBACK_METHOD.getCode());//回款方式
        List<JcsjDto> yxhtlxlist=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.YXHT_TYPE.getCode());
        mav.addObject("yxhtlxlist", yxhtlxlist);
        dtoById.setFormAction("modSaveMarketingContract");
        StringBuilder zsldmc = new StringBuilder();
        List<YxhtDto> zsldInfo = yxhtService.getInfoByZsld(dtoById);
        for (YxhtDto dto : zsldInfo) {
            zsldmc.append(",").append(dto.getZsxm());
        }
        dtoById.setZsldmc(zsldmc.substring(1));
        FjcfbDto fjcfbDto=new FjcfbDto();
        fjcfbDto.setYwid(yxhtDto.getHtid());
        fjcfbDto.setYwlx(BusTypeEnum.IMP_MARKETING_CONTRACT.getCode());
        List<FjcfbDto> fjcfbDtos = fjcfbService.getDtoList(fjcfbDto);
        mav.addObject("fjcfbDtos",fjcfbDtos);
        mav.addObject("ywlx", BusTypeEnum.IMP_MARKETING_CONTRACT.getCode());
        mav.addObject("yxhtDto", dtoById);
        mav.addObject("htlxList", htlxList);
        mav.addObject("xslblist", xslblist);
        mav.addObject("yzlblist", yzlblist);
        mav.addObject("htfxcdlist", htfxcdlist);
        mav.addObject("hkfslist", hkfslist);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 跳转营销合同审核列表
     */
    @RequestMapping(value = "/marketingContract/pageListMarketingContractsh")
    public ModelAndView pageListMarketingContractsh() {
        ModelAndView mav=new ModelAndView("marketingContract/marketingContract/marketingcontract_auditlist");
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 获取营销合同审核列表
     */
    @RequestMapping(value = "/marketingContract/pageGetListMarketingContractsh")
    @ResponseBody
    public Map<String,Object> pageGetListMarketingContractsh(YxhtDto yxhtDto, HttpServletRequest request){
        // 附加委托参数
        DataPermission.addWtParam(yxhtDto);
        // 附加审核状态过滤
        if (GlobalString.AUDIT_SHZT_YSH.equals(yxhtDto.getDqshzt())) {
            DataPermission.add(yxhtDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "yxht", "htid",
                    AuditTypeEnum.AUDIT_MARKETING_CONTRACT);
        } else {
            DataPermission.add(yxhtDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "yxht", "htid",
                    AuditTypeEnum.AUDIT_MARKETING_CONTRACT);
        }
        DataPermission.addCurrentUser(yxhtDto, getLoginInfo(request));
        List<YxhtDto> yxhtDtos = yxhtService.getPagedAuditMarketingContract(yxhtDto);
        Map<String, Object> map=new HashMap<>();
        map.put("total", yxhtDto.getTotalNumber());
        map.put("rows", yxhtDtos);
        return map;
    }
    /**
     * 营销合同列表页面
     */
    @RequestMapping(value = "/marketingContract/pageListMarketingContract")
    public ModelAndView pageListMarketingContract(YxhtDto yxhtDto) {
        ModelAndView mav = new ModelAndView("marketingContract/marketingContract/marketingContract_list");
        List<JcsjDto> xslxList=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.MARKET_SELLING_TYPE.getCode());//销售类型
        List<JcsjDto> khlxList=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.KHGL_TYPE.getCode());//客户管理类型
        List<JcsjDto> htlxList=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.MARKETING_CONTRACT_TYPE.getCode());//营销合同类型
        List<JcsjDto> yzlxList=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.CHAPTER_TYPE.getCode());//用章类别
        List<JcsjDto> risklist=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.CONTRACT_RISK.getCode());//合同风险程度
        List<JcsjDto> yxhtlxlist=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.YXHT_TYPE.getCode());//合同类型
        List<YxhtDto> htqxList=yxhtService.selectHtqxList();
        yxhtDto.setAuditType(AuditTypeEnum.AUDIT_MARKETING_CONTRACT.getCode());
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("htlxList", htlxList);
        mav.addObject("xslxList", xslxList);
        mav.addObject("khlxList", khlxList);
        mav.addObject("yzlxList", yzlxList);
        mav.addObject("risklist", risklist);
        mav.addObject("htqxList", htqxList);
        mav.addObject("yxhtDto", yxhtDto);
        mav.addObject("yxhtlxlist", yxhtlxlist);
        return mav;
    }

    /**
     * 营销合同列表
     */
    @RequestMapping(value = "/marketingContract/pageGetListMarketingContract")
    @ResponseBody
    public Map<String, Object> pageGetListMarketingContract(YxhtDto yxhtDto) {
        List<YxhtDto> yxhtDtoList=yxhtService.getPagedDtoList(yxhtDto);
        Map<String, Object> map = new HashMap<>();
        map.put("total", yxhtDto.getTotalNumber());
        map.put("rows", yxhtDtoList);
        return map;
    }
    /**
     * 营销合同列表查看
     */
    @RequestMapping(value = "/marketingContract/viewMarketingContract")
    public ModelAndView viewMarketingContract(YxhtDto yxhtDto) {
        ModelAndView mav = new ModelAndView("marketingContract/marketingContract/marketingContract_view");
        yxhtDto=yxhtService.getDtoById(yxhtDto.getHtid());
        //查询附件
        FjcfbDto fjcfbDto = new FjcfbDto();
        fjcfbDto.setYwlx(BusTypeEnum.IMP_MARKETING_CONTRACT.getCode());
        fjcfbDto.setYwid(yxhtDto.getHtid());
        List<FjcfbDto> t_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        mav.addObject("t_fjcfbDtos",t_fjcfbDtos);
        fjcfbDto.setYwlx(BusTypeEnum.IMP_UPLOADMARKETING_CONTRACT.getCode());
        List<FjcfbDto> h_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        mav.addObject("h_fjcfbDtos",h_fjcfbDtos);
        mav.addObject("yxhtDto", yxhtDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 营销合同列表跳转查看
     */
    @RequestMapping(value = "/marketingContract/pagedataViewMarketingContract")
    public ModelAndView pagedataViewMarketingContract(YxhtDto yxhtDto) {
        ModelAndView mav = new ModelAndView("marketingContract/marketingContract/marketingContract_view");
        yxhtDto=yxhtService.getDtoById(yxhtDto.getHtid());
        if (yxhtDto==null){
            return null;
        }
        //查询附件
        FjcfbDto fjcfbDto = new FjcfbDto();
        fjcfbDto.setYwlx(BusTypeEnum.IMP_MARKETING_CONTRACT.getCode());
        fjcfbDto.setYwid(yxhtDto.getHtid());
        List<FjcfbDto> t_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        mav.addObject("t_fjcfbDtos",t_fjcfbDtos);
        fjcfbDto.setYwlx(BusTypeEnum.IMP_UPLOADMARKETING_CONTRACT.getCode());
        List<FjcfbDto> h_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        mav.addObject("h_fjcfbDtos",h_fjcfbDtos);
        mav.addObject("yxhtDto", yxhtDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 选择营销合同
     */
    @RequestMapping("/marketingContract/pagedataChooseMarketingContract")
    public ModelAndView pagedataChooseMarketingContract(YxhtDto yxhtDto) {
        ModelAndView mav = new ModelAndView("marketingContract/marketingContract/marketingContract_choose");
        mav.addObject("yxhtDto", yxhtDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 选择营销合同列表
     */
    @RequestMapping("/marketingContract/pagedataGetListMarketingContract")
    @ResponseBody
    public Map<String, Object> pagedataGetListMarketingContract(YxhtDto yxhtDto){
        List<YxhtDto> t_List = yxhtService.getPagedDtoList(yxhtDto);
        Map<String,Object> result = new HashMap<>();
        result.put("total", yxhtDto.getTotalNumber());
        result.put("rows", t_List);
        return result;
    }
    /**
     * 营销合同复制页面
     */
    @RequestMapping(value = "/marketingContract/copyMarketingContract")
    public ModelAndView copyMarketingContract(YxhtDto yxhtDto) {
        ModelAndView mav = new ModelAndView("marketingContract/marketingContract/marketingcontract_edit");
        YxhtDto dtoById = yxhtService.getDtoById(yxhtDto.getHtid());
        List<JcsjDto> xslblist=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.MARKET_SELLING_TYPE.getCode());//销售类型
        List<JcsjDto> htlxList=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.MARKETING_CONTRACT_TYPE.getCode());//营销合同类型
        List<JcsjDto> yzlblist=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.CHAPTER_TYPE.getCode());//用章类别
        List<JcsjDto> htfxcdlist=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.CONTRACT_RISK.getCode());//合同风险程度
        List<JcsjDto> hkfslist=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PAYMENTBACK_METHOD.getCode());//回款方式
        List<JcsjDto> yxhtlxlist=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.YXHT_TYPE.getCode());
        dtoById.setFormAction("copySaveMarketingContract");
        StringBuilder zsldmc = new StringBuilder();
        List<YxhtDto> zsldInfo = yxhtService.getInfoByZsld(dtoById);
        for (YxhtDto dto : zsldInfo) {
            zsldmc.append(",").append(dto.getZsxm());
        }
        dtoById.setZsldmc(zsldmc.substring(1));
        mav.addObject("ywlx", BusTypeEnum.IMP_MARKETING_CONTRACT.getCode());
        mav.addObject("yxhtDto", dtoById);
        mav.addObject("htlxList", htlxList);
        mav.addObject("xslblist", xslblist);
        mav.addObject("yzlblist", yzlblist);
        mav.addObject("htfxcdlist", htfxcdlist);
        mav.addObject("hkfslist", hkfslist);
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("yxhtlxlist", yxhtlxlist);
        return mav;
    }
    /**
     * 营销合同复制保存
     */
    @ResponseBody
    @RequestMapping(value = "/marketingContract/copySaveMarketingContract")
    public Map<String, Object> copySaveMarketingContract(YxhtDto yxhtDto, HttpServletRequest request) {
        yxhtDto.setHtid(null);
        return addSaveMarketingContract(yxhtDto,request);
    }

    /**
     * @Description: 是否寄回
     * @param yxhtDto
     * @return org.springframework.web.servlet.ModelAndView
     * @Author: 郭祥杰
     * @Date: 2024/12/12 10:49
     */
    @RequestMapping(value = "/marketingContract/sendbackMarketingContract")
    public ModelAndView sendbackMarketingContract(YxhtDto yxhtDto) {
        ModelAndView mav = new ModelAndView("marketingContract/marketingContract/marketingContract_sendback");
        yxhtDto.setFormAction("sendbackSaveMarketingContract");
        mav.addObject("yxhtDto", yxhtDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * @Description: 是否寄回保存
     * @param yxhtDto
     * @param request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2024/12/12 12:51
     */
    @ResponseBody
    @RequestMapping(value = "/marketingContract/sendbackSaveMarketingContract")
    public Map<String, Object> sendbackSaveMarketingContract(YxhtDto yxhtDto, HttpServletRequest request) {
        User user = getLoginInfo(request);
        yxhtDto.setXgry(user.getYhid());
        Map<String, Object> map = new HashMap<>();
        boolean isSuccess = yxhtService.update(yxhtDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
    return map;
    }

}
