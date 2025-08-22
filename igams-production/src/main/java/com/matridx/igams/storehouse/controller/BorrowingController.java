package com.matridx.igams.storehouse.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.*;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.storehouse.dao.entities.*;
import com.matridx.igams.storehouse.service.svcinterface.*;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/borrowing")
public class BorrowingController extends BaseController {

    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;

    @Autowired
    private ICommonService commonService;

    @Autowired
    private IJcjyglService jcjyglService;

    @Autowired
    private IJcsjService jcsjService;

    @Autowired
    private IJccglService jccglService;

    @Autowired
    private IXxglService xxglService;

    @Autowired
    private IJcjyxxService jcjyxxService;
    @Autowired
    private IJcjymxService jcjyxmService;
 	@Autowired
	IJcghglService jcghglService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    private ICkxxService ckxxService;
    @Autowired
    IXsglService xsglService;
    @Autowired
    IXsmxService xsmxService;
    @Autowired
    IJcjymxService jcjymxService;
    @Autowired
    IJcghmxService jcghmxService;
    @Override
    public String getPrefix() {
        return urlPrefix;
    }

    /**
     * 借出借用列表页面
     *
     */
    @RequestMapping(value = "/borrowing/pageListBorrowing")
    public ModelAndView pageListBorrowing() {
        ModelAndView mav = new ModelAndView("storehouse/borrowing/borrowing_list");
//        jcjyglDto.setAuditType(AuditTypeEnum.AUDIT_GOODS_ARRIVAL.getCode());
////        mav.addObject("jcjyglDto", jcjyglDto);
        Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
                new BasicDataTypeEnum[]{BasicDataTypeEnum.CUSTOMER_CATEGORY});
        mav.addObject("khlblist", jclist.get(BasicDataTypeEnum.CUSTOMER_CATEGORY.getCode()));
        mav.addObject("auditType", AuditTypeEnum.AUDIT_BORROWING.getCode());
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 借出借用审核列表页面
     *
     */
    @RequestMapping(value = "/borrowing/pageListBorrowingAudit")
    public ModelAndView pageListBorrowingAudit() {
        ModelAndView mav = new ModelAndView("storehouse/borrowing/borrowing_listaudit");
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 获取领料申请审核列表
     */
    @RequestMapping("/borrowing/pageGetListBorrowingAudit")
    @ResponseBody
    public Map<String, Object> getPagedListMaterialAudit(JcjyglDto jcjyglDto, HttpServletRequest request) {
//		=================================
        //附加委托参数
        DataPermission.addWtParam(jcjyglDto);
        //附加审核状态过滤
        if (GlobalString.AUDIT_SHZT_YSH.equals(jcjyglDto.getDqshzt())) {
            DataPermission.add(jcjyglDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "jcjygl", "jcjyid", AuditTypeEnum.AUDIT_BORROWING);
        } else {
            DataPermission.add(jcjyglDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "jcjygl", "jcjyid", AuditTypeEnum.AUDIT_BORROWING);
        }
        DataPermission.addCurrentUser(jcjyglDto, getLoginInfo(request));
        DataPermission.addSpDdw(jcjyglDto, "jcjygl", SsdwTableEnum.JCJYGL);
        List<JcjyglDto> jcjyglDtos = jcjyglService.getPagedAuditJcjyglDto(jcjyglDto);
//		=================================
        Map<String, Object> map = new HashMap<>();
        map.put("total", jcjyglDto.getTotalNumber());
        map.put("rows", jcjyglDtos);
        return map;
    }

    /**
     * 审核页面
     */
    @RequestMapping("/borrowing/auditBorrowing")
    public ModelAndView auditBorrowing(JcjyglDto jcjyglDto) {
        JcjyglDto jcjyglDto1 = jcjyglService.getDtoById(jcjyglDto.getJcjyid());
        ModelAndView mav = new ModelAndView("storehouse/borrowing/borrowing_edit");
        jcjyglDto1.setFormAction("/borrowing/borrowing/resubmitSaveBorrowing");
        Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
                new BasicDataTypeEnum[]{BasicDataTypeEnum.PERUNIT_TYPE,BasicDataTypeEnum.PRODUCT_CLASS,BasicDataTypeEnum.PROVINCE});
        mav.addObject("dwlblist", jclist.get(BasicDataTypeEnum.PERUNIT_TYPE.getCode()));
        mav.addObject("auditType", AuditTypeEnum.AUDIT_BORROWING.getCode());
        mav.addObject("cplxlist", JSONObject.toJSONString(jclist.get(BasicDataTypeEnum.PRODUCT_CLASS.getCode())));
        mav.addObject("sflist", jclist.get(BasicDataTypeEnum.PROVINCE.getCode()));
        mav.addObject("xslxlist", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.SELLING_TYPE.getCode()));
        mav.addObject("url", "/borrowing/borrowing/pagedataResubmit");
        mav.addObject("urlPrefix", urlPrefix);
        jcjyglDto1.setXsbj("1");
        mav.addObject("jcjygl", jcjyglDto1);
        return mav;
    }

    /**
     * 提交页面
     */
    @RequestMapping("/borrowing/resubmitBorrowing")
    public ModelAndView resubmitBorrowing(JcjyglDto jcjyglDto) {
        JcjyglDto jcjyglDto1 = jcjyglService.getDtoById(jcjyglDto.getJcjyid());
        ModelAndView mav = new ModelAndView("storehouse/borrowing/borrowing_edit");
        jcjyglDto1.setFormAction("/borrowing/borrowing/resubmitSaveBorrowing");
        Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
                new BasicDataTypeEnum[]{BasicDataTypeEnum.PERUNIT_TYPE,BasicDataTypeEnum.PRODUCT_CLASS,BasicDataTypeEnum.PROVINCE});
        mav.addObject("dwlblist", jclist.get(BasicDataTypeEnum.PERUNIT_TYPE.getCode()));
        mav.addObject("cplxlist", JSONObject.toJSONString(jclist.get(BasicDataTypeEnum.PRODUCT_CLASS.getCode())));
        mav.addObject("xslxlist", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.SELLING_TYPE.getCode()));
        mav.addObject("ysfslist", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.DELIVERY_METHOD.getCode()));
        mav.addObject("auditType", AuditTypeEnum.AUDIT_BORROWING.getCode());
        mav.addObject("sflist", jclist.get(BasicDataTypeEnum.PROVINCE.getCode()));
        mav.addObject("url", "/borrowing/borrowing/pagedataResubmit");
        mav.addObject("urlPrefix", urlPrefix);
        jcjyglDto1.setXsbj("1");
        mav.addObject("jcjygl", jcjyglDto1);

        return mav;
    }

    /**
     * 领料审核页面
     */
    @RequestMapping("/borrowing/pagedataSeniorModBorrowing")
    public ModelAndView seniorModBorrowing(JcjyglDto jcjyglDto) {
        // User user = getLoginInfo(request);
        JcjyglDto jcjyglDto1 = jcjyglService.getDtoById(jcjyglDto.getJcjyid());
        ModelAndView mav = new ModelAndView("storehouse/borrowing/borrowing_senioredit");
        jcjyglDto1.setFormAction("/borrowing/borrowing/saveSeniorModBorrowing");
        mav.addObject("auditType", AuditTypeEnum.AUDIT_BORROWING.getCode());
        Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
                new BasicDataTypeEnum[]{BasicDataTypeEnum.PERUNIT_TYPE,BasicDataTypeEnum.PROVINCE,BasicDataTypeEnum.DELIVERY_METHOD});
        mav.addObject("dwlblist", jclist.get(BasicDataTypeEnum.PERUNIT_TYPE.getCode()));
        mav.addObject("sflist", jclist.get(BasicDataTypeEnum.PROVINCE.getCode()));
        mav.addObject("ysfslist", jclist.get(BasicDataTypeEnum.DELIVERY_METHOD.getCode()));
        mav.addObject("xslxlist", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.SELLING_TYPE.getCode()));
        mav.addObject("urlPrefix", urlPrefix);
        JcjyxxDto jcjyxxDto = new JcjyxxDto();
        jcjyxxDto.setJcjyid(jcjyglDto.getJcjyid());
        List<JcjyxxDto> jcjyxxDtos = jcjyxxService.getDtoListInfoAndMx(jcjyxxDto);
        JcjymxDto jcjymxDto = new JcjymxDto();
        jcjymxDto.setJcjyid(jcjyglDto.getJcjyid());
        List<JcjymxDto> jcjymxDtos = jcjyxmService.getDtoList(jcjymxDto);
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter(JcjymxDto.class, "jcjyid", "jyxxid", "hwid", "jysl");
        SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
        listFilters[0] = filter;
        mav.addObject("jymx_json", JSONObject.toJSONString(jcjymxDtos, listFilters));
        mav.addObject("jcjymxDtos", jcjymxDtos);
        mav.addObject("jcjyxxDtos", jcjyxxDtos);
        jcjyglDto1.setXsbj("2");
        mav.addObject("jcjygl", jcjyglDto1);
        return mav;
    }

    /**
     * 刷新借用单号
     */
    @RequestMapping("/borrowing/sxjydh")
    @ResponseBody
    public Map<String, Object> sxjydh(JcjyglDto jcjyglDto) {
        Map<String, Object> map = new HashMap<>();
        map.put("jydh", jcjyglService.generateJydh(jcjyglDto));
        return map;
    }

    /**
     * 刷新借用单号
     */
    @RequestMapping("/borrowing/sxjydhOA")
    @ResponseBody
    public Map<String, Object> sxjydhOA(JcjyglDto jcjyglDto) {
        Map<String, Object> map = new HashMap<>();
        map.put("jydh", jcjyglService.generateJydhOA(jcjyglDto));
        return map;
    }


    /**
     * 借出新增页面
     *
     */
    @RequestMapping(value = "/borrowing/addBorrowing")
    public ModelAndView addBorrowing(JcjyglDto jcjyglDto, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("storehouse/borrowing/borrowing_edit");
        //获取当前用户
        User user = getLoginInfo(request);
        user = commonService.getUserInfoById(user);
        if (user != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
            Date date = new Date();
            jcjyglDto.setJyrq(sdf.format(date));//申请时间
        }

        String jydh = jcjyglService.generateJydhOA(jcjyglDto); //生成领料单
        jcjyglDto.setJydh(jydh);
        Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
                new BasicDataTypeEnum[]{BasicDataTypeEnum.PERUNIT_TYPE,BasicDataTypeEnum.PRODUCT_CLASS,BasicDataTypeEnum.PROVINCE});
        mav.addObject("dwlblist", jclist.get(BasicDataTypeEnum.PERUNIT_TYPE.getCode()));
        mav.addObject("cplxlist", JSONObject.toJSONString(jclist.get(BasicDataTypeEnum.PRODUCT_CLASS.getCode())));
        mav.addObject("sflist", jclist.get(BasicDataTypeEnum.PROVINCE.getCode()));
        mav.addObject("xslxlist", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.SELLING_TYPE.getCode()));
        mav.addObject("ysfslist", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.DELIVERY_METHOD.getCode()));
        mav.addObject("url", "/borrowing/borrowing/pagedataBorrowing");
        jcjyglDto.setFormAction("/borrowing/borrowing/addSaveBorrowing");
        mav.addObject("jcjygl", jcjyglDto);
        mav.addObject("auditType", AuditTypeEnum.AUDIT_BORROWING.getCode());
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 借出页面
     *
     */
    @RequestMapping(value = "/borrowing/borrowIng")
    public ModelAndView borrowing(JcjyglDto jcjyglDto, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("storehouse/borrowing/borrowing_edit");
        //获取当前用户
        User user = getLoginInfo(request);
        user = commonService.getUserInfoById(user);
        if (user != null) {
            jcjyglDto.setBmmc(user.getJgmc());
            jcjyglDto.setBm(user.getJgid());
            jcjyglDto.setBmdm(user.getJgdm());
            SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
            Date date = new Date();
            jcjyglDto.setJyrq(sdf.format(date));//申请时间
        }

        String jydh = jcjyglService.generateJydhOA(jcjyglDto); //生成领料单
        jcjyglDto.setJydh(jydh);
        Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
                new BasicDataTypeEnum[]{BasicDataTypeEnum.PERUNIT_TYPE});
        mav.addObject("dwlblist", jclist.get(BasicDataTypeEnum.PERUNIT_TYPE.getCode()));
        mav.addObject("xslxlist", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.SELLING_TYPE.getCode()));
        mav.addObject("url", "/borrowing/borrowing/pagedataBorrowing");
        jcjyglDto.setFormAction("/borrowing/borrowing/borrowSaveIng");
        mav.addObject("jcjygl", jcjyglDto);
        mav.addObject("auditType", AuditTypeEnum.AUDIT_BORROWING.getCode());
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 借用车保存
     */
    @RequestMapping("/borrowing/addSaveBorrowing")
    @ResponseBody
    public Map<String, Object> addSaveBorrowing(JcjyglDto jcjyglDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        jcjyglDto.setLrry(user.getYhid());
        jcjyglDto.setJcjyid(StringUtil.generateUUID());
        jcjyglDto.setSfqr("0");
        jcjyglDto.setDjlx("0");
        boolean isSuccess;
        try {
            isSuccess = jcjyglService.saveAddBorrowing(jcjyglDto);
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", e);
            return map;
        }
        map.put("ywid", jcjyglDto.getJcjyid());
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 借用车保存
     */
    @RequestMapping("/borrowing/borrowSaveIng")
    @ResponseBody
    public Map<String, Object> saveAddBorrowing(JcjyglDto jcjyglDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        jcjyglDto.setLrry(user.getYhid());
        jcjyglDto.setJcjyid(StringUtil.generateUUID());
        boolean isSuccess;
        try {
            isSuccess = jcjyglService.saveAddBorrowing(jcjyglDto);
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", e);
            return map;
        }
        map.put("ywid", jcjyglDto.getJcjyid());
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 删除借出借用信息
     */
    @RequestMapping("/borrowing/delBorrowingInfo")
    @ResponseBody
    public Map<String, Object> delBorrowingInfo(JcjyglDto jcjyglDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        boolean isSuccess;
        try {
            // 获取用户信息
            User user = getLoginInfo(request);
            jcjyglDto.setScry(user.getYhid());
            isSuccess = jcjyglService.delBorrowing(jcjyglDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00003").getXxnr() : xxglService.getModelById("ICOM00004").getXxnr());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg()) ? e.getMsg() : xxglService.getModelById("ICOM00004").getXxnr());
        }
        return map;
    }
    /**
     * 废弃借出借用信息
     */
    @RequestMapping("/borrowing/discardBorrowingInfo")
    @ResponseBody
    public Map<String,Object> discardBorrowingInfo(JcjyglDto jcjyglDto, HttpServletRequest request) {
        return this.delBorrowingInfo(jcjyglDto,request);
    }
    /**
     * 将物料添加至借用
     */
    @RequestMapping("/borrowing/pagedataAddBorrowingCar")
    @ResponseBody
    public Map<String, Object> addBorrowingCar(JccglDto jccglDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        jccglDto.setRyid(user.getYhid());
        boolean isSuccess = jccglService.insert(jccglDto);
        List<JccglDto> jccglDtos = jccglService.getDtoList(jccglDto);
        StringBuilder ids = new StringBuilder();
        if (!CollectionUtils.isEmpty(jccglDtos)) {
            for (JccglDto jccglDto_t : jccglDtos) {
                ids.append(",").append(jccglDto_t.getCkhwid());
            }
            ids = new StringBuilder(ids.substring(1));
        }
        map.put("idsJywl", ids.toString());
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 从借用车删除物料
     */
    @RequestMapping("/borrowing/pagedataDelFromBorrowingCar")
    @ResponseBody
    public Map<String, Object> delFromBorrowingCar(JccglDto jccglDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        jccglDto.setRyid(user.getYhid());
        boolean isSuccess = jccglService.delete(jccglDto);
        // 获取领料车已加入的物料数量
        List<JccglDto> jccglDtos = jccglService.getJccDtoList(jccglDto);
        String ids = "";
        if (!CollectionUtils.isEmpty(jccglDtos)) {
            for (JccglDto jccglDto_t : jccglDtos) {
                ids = "," + jccglDto_t.getCkhwid();
            }
            ids = ids.substring(1);
        }
        map.put("idsJywl", ids);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00003").getXxnr()
                : xxglService.getModelById("ICOM00004").getXxnr());
        return map;
    }

    /**
     * 领料修改保存
     */
    @RequestMapping("/borrowing/resubmitSaveBorrowing")
    @ResponseBody
    public Map<String, Object> resubmitSaveBorrowing(HttpServletRequest request, JcjyglDto jcjyglDto) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        jcjyglDto.setXgry(user.getYhid());
        boolean isSuccess;
        //修改保存
        try {
            isSuccess = jcjyglService.modSaveBorrowing(jcjyglDto);
            map.put("ywid", jcjyglDto.getJcjyid());
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
                    : xxglService.getModelById("ICOM00002").getXxnr());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg()) ? e.getMsg() : xxglService.getModelById("ICOM00002").getXxnr());
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
        }
        return map;
    }

    /**
     * 借用审核保存
     */
    @RequestMapping("/borrowing/saveSeniorModBorrowing")
    @ResponseBody
    public Map<String, Object> saveSeniorModBorrowing(HttpServletRequest request, JcjyglDto jcjyglDto) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        jcjyglDto.setXgry(user.getYhid());
        boolean isSuccess;
        //修改保存
        try {
            isSuccess = jcjyglService.seniorModSaveBorrowing(jcjyglDto);
            map.put("ywid", jcjyglDto.getJcjyid());
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
                    : xxglService.getModelById("ICOM00002").getXxnr());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg()) ? e.getMsg() : xxglService.getModelById("ICOM00002").getXxnr());
        }
        return map;
    }

    /**
     * 借出车
     */
    @RequestMapping("/borrowing/pagedataBorrowing")
    @ResponseBody
    public Map<String, Object> getBorrowing(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        JccglDto jccglDto = new JccglDto();
        jccglDto.setRyid(user.getYhid());
        List<JccglDto> jccDtoList = jccglService.getJccDtoList(jccglDto);
        if (!CollectionUtils.isEmpty(jccDtoList)) {
            map.put("rows", jccDtoList);
        } else {
            map.put("rows", null);
        }
        return map;
    }

    /**
     * 重新提交
     *
     * @param
     */
    @RequestMapping("/borrowing/pagedataResubmit")
    @ResponseBody
    public Map<String, Object> getResubmitBorrowing(JcjyglDto jcjyglDto) {
        Map<String, Object> map = new HashMap<>();
        JcjyxxDto jcjyxxDto = new JcjyxxDto();
        jcjyxxDto.setJcjyid(jcjyglDto.getJcjyid());
        List<JcjyxxDto> jccglDtos = jcjyxxService.getDtoListInfo(jcjyxxDto);
        if (!CollectionUtils.isEmpty(jccglDtos)) {
            map.put("rows", jccglDtos);
        } else {
            map.put("rows", null);
        }
        return map;
    }

    /**
     * 借出查看页面
     *
     */
    @RequestMapping(value = "/borrowing/viewBorrowing")
    public ModelAndView viewBorrowing(JcjyglDto jcjyglDto) {
        ModelAndView mav = new ModelAndView("storehouse/borrowing/borrowing_view");
        JcjyglDto jcjyglDto1 = jcjyglService.getDtoById(jcjyglDto.getJcjyid());
        JcjyxxDto jcjyxxDto = new JcjyxxDto();
        jcjyxxDto.setJcjyid(jcjyglDto.getJcjyid());
        List<JcjyxxDto> jcjyxxDtos = jcjyxxService.getDtoListInfo(jcjyxxDto);
        for (JcjyxxDto jcjyxxDto1:jcjyxxDtos){
            JcjymxDto jcjymxDto=new JcjymxDto();
            jcjymxDto.setJyxxid(jcjyxxDto1.getJyxxid());
            List<JcjymxDto> jcjymxDtos=jcjymxService.getDtoList(jcjymxDto);
            jcjyxxDto1.setJcjymxDtos(jcjymxDtos);
        }
        List<XsglDto> xsglDtos = xsglService.getDtoListByJyId(jcjyglDto1.getJcjyid());
        if (!CollectionUtils.isEmpty(xsglDtos)){
            List<String> ids = new ArrayList<>();
            for (XsglDto xsglDto : xsglDtos) {
                if (!ids.contains(xsglDto.getXsid())){
                    ids.add(xsglDto.getXsid());
                }
            }
            XsmxDto xsmxDto = new XsmxDto();
            xsmxDto.setIds(ids);
            List<XsmxDto> dtoList = xsmxService.getDtoList(xsmxDto);
            mav.addObject("xsmxDtos", dtoList);
        }else {
            mav.addObject("xsmxDtos", null);
        }
        mav.addObject("jcjyxxDtos", jcjyxxDtos);
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("jcjygl", jcjyglDto1);
        return mav;
    }
    /**
     * 借出查看页面
     *
     */
    @RequestMapping(value = "/borrowing/pagedataViewBorrowing")
    public ModelAndView pagedataviewBorrowing(JcjyglDto jcjyglDto) {
        ModelAndView mav = new ModelAndView("storehouse/borrowing/borrowing_view");
        JcjyglDto jcjyglDto1 = jcjyglService.getDtoById(jcjyglDto.getJcjyid());
        JcjyxxDto jcjyxxDto = new JcjyxxDto();
        jcjyxxDto.setJcjyid(jcjyglDto.getJcjyid());
        List<JcjyxxDto> jcjyxxDtos = jcjyxxService.getDtoListInfo(jcjyxxDto);
        List<XsglDto> xsglDtos = xsglService.getDtoListByJyId(jcjyglDto1.getJcjyid());
        if (!CollectionUtils.isEmpty(xsglDtos)){
            List<String> ids = new ArrayList<>();
            for (XsglDto xsglDto : xsglDtos) {
                if (!ids.contains(xsglDto.getXsid())){
                    ids.add(xsglDto.getXsid());
                }
            }
            XsmxDto xsmxDto = new XsmxDto();
            xsmxDto.setIds(ids);
            List<XsmxDto> dtoList = xsmxService.getDtoList(xsmxDto);
            mav.addObject("xsmxDtos", dtoList);
        }else {
            mav.addObject("xsmxDtos", null);
        }
        mav.addObject("jcjyxxDtos", jcjyxxDtos);
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("jcjygl", jcjyglDto1);
        return mav;
    }

    /**
     * 钉钉查看
     */
    @RequestMapping("/borrowing/minidataGetViewBorrowing")
    @ResponseBody
    public Map<String, Object> minidataGetViewBorrowing(JcjyglDto jcjyglDto) {
        Map<String, Object> map = new HashMap<>();
        JcjyglDto jcjyglDto1 = jcjyglService.getDtoById(jcjyglDto.getJcjyid());
        JcjyxxDto jcjyxxDto = new JcjyxxDto();
        jcjyxxDto.setJcjyid(jcjyglDto.getJcjyid());
        List<JcjyxxDto> jcjyxxDtos = jcjyxxService.getDtoListInfo(jcjyxxDto);
        JcjymxDto jcjymxDto = new JcjymxDto();
        jcjymxDto.setJcjyid(jcjyglDto.getJcjyid());
        List<JcjymxDto> jcjymxDtos = jcjyxmService.getDtoList(jcjymxDto);
        map.put("jcjymxDtos", jcjymxDtos);
        map.put("jcjyglDto", jcjyglDto1);
        map.put("jcjyxxDtos", jcjyxxDtos);
        return map;
    }

    /**
     * @Description: 借出归还查看
     * @param jcghglDto
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2024/10/31 16:15
     */
    @RequestMapping("/borrowing/minidataGetViewReturn")
    @ResponseBody
    public Map<String, Object> minidataGetViewReturn(JcghglDto jcghglDto) {
        Map<String, Object> map = new HashMap<>();
        JcghglDto jcghglDto1 = jcghglService.getDtoById(jcghglDto.getJcghid());
        JcghmxDto jcghmxDto = new JcghmxDto();
        jcghmxDto.setJcghid(jcghglDto.getJcghid());
        List<JcghmxDto> jcghmxDtos = jcghmxService.getDtoList(jcghmxDto);
        map.put("jcghmxDtos", jcghmxDtos);
        map.put("jcghglDto", jcghglDto1);
        return map;
    }
    /**
     * 借出借用列表
     *
     */
    @RequestMapping(value = "/borrowing/pageGetListBorrowing")
    @ResponseBody
    public Map<String, Object> getBorrowingList(JcjyglDto jcjyglDto) {
        List<JcjyglDto> jcjyglDtos = jcjyglService.getPagedDtoList(jcjyglDto);
        Map<String, Object> map = new HashMap<>();
        map.put("total", jcjyglDto.getTotalNumber());
        map.put("rows", jcjyglDtos);
        return map;
    }

    /**
     * 借出借用明细列表
     *
     */
    @RequestMapping(value = "/borrowing/pagedataListBorrowingDetails")
    @ResponseBody
    public Map<String, Object> pagedataListBorrowingDetails(JcjymxDto jcjymxDto) {
        List<JcjymxDto> jcjymxDtos = jcjymxService.getPagedDtoList(jcjymxDto);
        Map<String, Object> map = new HashMap<>();
        map.put("total", jcjymxDto.getTotalNumber());
        map.put("rows", jcjymxDtos);
        return map;
    }

    /**
     * 借出借用列表钉钉
     *
     */
    @RequestMapping(value = "/borrowing/minidataGetBorrowingList")
    @ResponseBody
    public Map<String, Object> minidataGetBorrowingList(JcjyglDto jcjyglDto) {
        return getBorrowingList(jcjyglDto);
    }

    /**
     * 获取明细（归还）
     */
    @RequestMapping("/borrowing/pagedataBorrowingDetails")
    @ResponseBody
    public Map<String, Object> pagedataBorrowingDetails(JcjyglDto jcjyglDto) {
        Map<String, Object> map = new HashMap<>();
        JcjymxDto jcjymxDto = new JcjymxDto();
        jcjymxDto.setJcjyid(jcjyglDto.getJcjyid());
        List<JcjymxDto> newList = new ArrayList<>();
        List<JcjymxDto> list = jcjyxmService.getDtoList(jcjymxDto);
        if (!CollectionUtils.isEmpty(list)) {
            for (JcjymxDto jcjymxDto_t : list) {
                BigDecimal jysl =new BigDecimal(jcjymxDto_t.getJysl());
                BigDecimal yhsl =new BigDecimal(jcjymxDto_t.getYhsl());
                jcjymxDto_t.setKhsl(String.valueOf(jysl.subtract(yhsl)));
                if (Double.parseDouble(jcjymxDto_t.getKhsl()) > 0) {
                    newList.add(jcjymxDto_t);
                }
            }
            map.put("rows", newList);
        } else {
            map.put("rows", null);
        }
        return map;
    }

    /**
     * 借出借用统计
     *
     */
    @RequestMapping(value = "/borrowing/minidataBorrowingStatis")
    @ResponseBody
    public List<JcjyglDto> minidataBorrowingStatis() {
        return jcjyglService.getJcjyWithKh();
    }
    /**
     * 借出借用信息查看通过dwid
     *
     */
    @RequestMapping(value = "/borrowing/minidataGetJcjyxx")
    @ResponseBody
    public Map<String,Object> minidataGetJcjyxx(JcjyglDto jcjyglDto) {
        Map<String, Object> map = new HashMap<>();
        List<JcjyglDto> jcjyxxDtos = jcjyglService.getJcjyxxByDwid(jcjyglDto);
        map.put("jcjyxxDtos", jcjyxxDtos);
        return map;
    }
    /**
     * 借出借用信息查看
     *
     */
    @RequestMapping(value = "/borrowing/minidataGetJcjyxxAndMx")
    @ResponseBody
    public Map<String,Object> minidataGetJcjyxxAndMx(JcjyglDto jcjyglDto) {
        Map<String, Object> map = new HashMap<>();
        JcjyxxDto jcjyxxDto = new JcjyxxDto();
        jcjyxxDto.setJcjyid(jcjyglDto.getJcjyid());
        List<JcjyxxDto> jcjyxxDtos = jcjyxxService.getDtoListInfo(jcjyxxDto);
        JcjymxDto jcjymxDto = new JcjymxDto();
        jcjymxDto.setJcjyid(jcjyglDto.getJcjyid());
        List<JcjymxDto> jcjymxDtos = jcjyxmService.getDtoList(jcjymxDto);
        map.put("jcjyxxDtos", jcjyxxDtos);
        map.put("jcjymxDtos", jcjymxDtos);
        return map;
    }
    /**
     * 钉钉借出查看接口
     *
     */
    @RequestMapping(value = "/borrowing/minidataViewBorrowing")
    @ResponseBody
    public Map<String,Object> minidataViewBorrowing(JcjyglDto jcjyglDto) {
        Map<String, Object> map = new HashMap<>();
        JcjyglDto jcjyglDto1 = jcjyglService.getDtoById(jcjyglDto.getJcjyid());
        JcjyxxDto jcjyxxDto = new JcjyxxDto();
        jcjyxxDto.setJcjyid(jcjyglDto.getJcjyid());
        List<JcjyxxDto> jcjyxxDtos = jcjyxxService.getDtoListInfo(jcjyxxDto);
        map.put("jcjyxxDtos", jcjyxxDtos);
        map.put("jcjyglDto", jcjyglDto1);
        return map;
    }

    /**
     * 归还页面
     *
     */
    @RequestMapping(value = "/borrowing/repaidBorrowing")
    public ModelAndView repaidBorrowing(JcghglDto jcghglDto, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("storehouse/repaid/repaid_edit");
        //获取当前用户
        User user = getLoginInfo(request);
        user = commonService.getUserInfoById(user);
        if (user != null) {
            jcghglDto.setBmmc(user.getJgmc());
            jcghglDto.setBm(user.getJgid());
            jcghglDto.setBmdm(user.getJgdm());
            SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
            Date date = new Date();
            jcghglDto.setGhrq(sdf.format(date));//申请时间
        }
        List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.WAREHOUSE_TYPE.getCode());
        CkxxDto ckxxDto = new CkxxDto();
        CkxxDto kwxxDto = new CkxxDto();
        for (JcsjDto jcsjDto:jcsjDtos) {
            if ("CK".equals(jcsjDto.getCsdm())){
                ckxxDto.setCklb(jcsjDto.getCsid());
            }else if("KW".equals(jcsjDto.getCsdm())){
                kwxxDto.setCklb(jcsjDto.getCsid());
            }
        }
        List<CkxxDto> ckxxDtos = ckxxService.getDtoList(ckxxDto);
        List<CkxxDto> kwxxDtos = ckxxService.getDtoList(kwxxDto);
        mav.addObject("cklist", JSONObject.toJSONString(ckxxDtos));
        mav.addObject("kwlist",JSONObject.toJSONString(kwxxDtos));
        String ghdh = jcghglService.generateGhdh(jcghglDto); //生成归还单号
        jcghglDto.setGhdh(ghdh);
        Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
                new BasicDataTypeEnum[] { BasicDataTypeEnum.PERUNIT_TYPE});
        mav.addObject("dwlblist", jclist.get(BasicDataTypeEnum.PERUNIT_TYPE.getCode()));
        mav.addObject("url", "/borrowing/borrowing/pagedataBorrowingDetails");
        jcghglDto.setFormAction("/borrowing/borrowing/repaidSaveBorrowing");
        JcjyglDto jcjyglDto=new JcjyglDto();
        jcjyglDto.setJcjyid(jcghglDto.getJcjyid());
        JcjyglDto dto = jcjyglService.getDto(jcjyglDto);
        if(dto!=null){
            jcghglDto.setDwid(dto.getDwid());
            jcghglDto.setDwdm(dto.getDwdm());
            jcghglDto.setDwmc(dto.getDwmc());
            jcghglDto.setSfzfyf(dto.getSfzfyf());
            jcghglDto.setDwlx(dto.getDwlx());
        }
        mav.addObject("jcghglDto", jcghglDto);
        mav.addObject("auditType", AuditTypeEnum.AUDIT_REPAID.getCode());
        jcghglDto.setXsbj("1");
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 归还保存
     */
    @RequestMapping("/borrowing/repaidSaveBorrowing")
    @ResponseBody
    public Map<String, Object> repaidSaveBorrowing(JcghglDto jcghglDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        jcghglDto.setLrry(user.getYhid());
        jcghglDto.setJcghid(StringUtil.generateUUID());
        jcghglDto.setZt(StatusEnum.CHECK_NO.getCode());
        boolean isSuccess;
        try {
            isSuccess = jcghglService.saveRepaidBorrowing(jcghglDto);
        } catch (BusinessException e) {
            map.put("status","fail");
            map.put("message",e);
            return map;
        }
        map.put("ywid",jcghglDto.getJcghid());
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }
    /**
     * 借出借用换货页面
     */
    @RequestMapping("/borrowing/jytransformBorrowing")
    public ModelAndView jytransformBorrowing(JcjyglDto jcjyglDto) {
        JcjyglDto jcjyglDto1 = jcjyglService.getDtoById(jcjyglDto.getJcjyid());
        String jydh = jcjyglService.generateJydhOA(jcjyglDto); //生成领料单
        jcjyglDto1.setJydh(jydh);
        ModelAndView mav = new ModelAndView("storehouse/borrowing/borrowing_edit");
        jcjyglDto1.setFormAction("/borrowing/borrowing/jytransformSaveBorrowing");
        Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
                new BasicDataTypeEnum[]{BasicDataTypeEnum.PERUNIT_TYPE,BasicDataTypeEnum.PRODUCT_CLASS,BasicDataTypeEnum.PROVINCE});
        mav.addObject("dwlblist", jclist.get(BasicDataTypeEnum.PERUNIT_TYPE.getCode()));
        mav.addObject("cplxlist", JSONObject.toJSONString(jclist.get(BasicDataTypeEnum.PRODUCT_CLASS.getCode())));
        mav.addObject("auditType", AuditTypeEnum.AUDIT_BORROWING.getCode());
        mav.addObject("sflist", jclist.get(BasicDataTypeEnum.PROVINCE.getCode()));
        mav.addObject("xslxlist", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.SELLING_TYPE.getCode()));
        mav.addObject("url", "/borrowing/borrowing/pagedataResubmit");
        mav.addObject("urlPrefix", urlPrefix);
        jcjyglDto1.setXsbj("1");
        jcjyglDto1.setHhbj("1");
        mav.addObject("jcjygl", jcjyglDto1);
        return mav;
    }
    /**
     * 换货保存
     */
    @RequestMapping("/borrowing/jytransformSaveBorrowing")
    @ResponseBody
    public Map<String, Object> jytransformSaveBorrowing(HttpServletRequest request, JcjyglDto jcjyglDto) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        boolean isSuccess;
        jcjyglDto.setLrry(user.getYhid());
        jcjyglDto.setYjcjyid(jcjyglDto.getJcjyid());
        jcjyglDto.setJcjyid(StringUtil.generateUUID());
        jcjyglDto.setSfqr("0");
        jcjyglDto.setDjlx("1");
        //修改保存
        try {
            isSuccess = jcjyglService.saveAddBorrowing(jcjyglDto);
            map.put("ywid", jcjyglDto.getJcjyid());
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
                    : xxglService.getModelById("ICOM00002").getXxnr());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg()) ? e.getMsg() : xxglService.getModelById("ICOM00002").getXxnr());
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
        }
        return map;
    }
    /**
     * 借出借用复制页面
     *
     * @param
     */
    @RequestMapping("/borrowing/copyBorrowing")
    public ModelAndView copyBorrowing(JcjyglDto jcjyglDto) {
        JcjyglDto jcjyglDto1 = jcjyglService.getDtoById(jcjyglDto.getJcjyid());
        String jydh = jcjyglService.generateJydhOA(jcjyglDto); //生成领料单
        jcjyglDto1.setJydh(jydh);
        ModelAndView mav = new ModelAndView("storehouse/borrowing/borrowing_edit");
        jcjyglDto1.setFormAction("/borrowing/borrowing/copySaveBorrowing");
        Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
                new BasicDataTypeEnum[]{BasicDataTypeEnum.PERUNIT_TYPE,BasicDataTypeEnum.PRODUCT_CLASS,BasicDataTypeEnum.PROVINCE});
        mav.addObject("dwlblist", jclist.get(BasicDataTypeEnum.PERUNIT_TYPE.getCode()));
        mav.addObject("cplxlist", JSONObject.toJSONString(jclist.get(BasicDataTypeEnum.PRODUCT_CLASS.getCode())));
        mav.addObject("auditType", AuditTypeEnum.AUDIT_BORROWING.getCode());
        mav.addObject("sflist", jclist.get(BasicDataTypeEnum.PROVINCE.getCode()));
        mav.addObject("xslxlist", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.SELLING_TYPE.getCode()));
        mav.addObject("url", "/borrowing/borrowing/pagedataResubmit");
        mav.addObject("urlPrefix", urlPrefix);
        jcjyglDto1.setXsbj("1");
        jcjyglDto1.setHhbj("1");
        mav.addObject("jcjygl", jcjyglDto1);
        return mav;
    }
    /**
     * 借用车保存
     */
    @RequestMapping("/borrowing/copySaveBorrowing")
    @ResponseBody
    public Map<String, Object> copySaveBorrowing(JcjyglDto jcjyglDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        jcjyglDto.setLrry(user.getYhid());
        jcjyglDto.setJcjyid(StringUtil.generateUUID());
        jcjyglDto.setSfqr("0");
        jcjyglDto.setDjlx("0");
        jcjyglDto.setYjcjyid(null);
        boolean isSuccess;
        try {
            isSuccess = jcjyglService.saveAddBorrowing(jcjyglDto);
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", e);
            return map;
        }
        map.put("ywid", jcjyglDto.getJcjyid());
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }
}