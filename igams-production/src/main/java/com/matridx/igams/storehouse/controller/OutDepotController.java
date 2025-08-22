package com.matridx.igams.storehouse.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.cloud.controller.BaseBasicController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.JgxxDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.*;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IJgxxService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.production.dao.entities.HtmxDto;
import com.matridx.igams.production.service.svcinterface.IHtmxService;
import com.matridx.igams.storehouse.dao.entities.*;
import com.matridx.igams.storehouse.service.svcinterface.*;
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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/warehouse")
public class OutDepotController extends BaseBasicController{

    @Autowired
    ICkglService ckglService;
    @Autowired
    ICkmxService ckmxService;
    @Autowired
    ILlglService llglService;
    @Autowired
    ICommonService commonService;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Override
	public String getPrefix(){
		return urlPrefix;
	}
    @Autowired
    IXxglService xxglService;
    @Autowired
    IHwllxxService hwllxxService;
    @Autowired
    IJcsjService jcsjService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    IHwxxService hwxxService;
    @Autowired
    IHtmxService htmxService;
    @Autowired
    private ICkxxService ckxxService;
    @Autowired
    IJgxxService jgxxService;
    /**
     * 入库列表
     * @param
     * @return
     */
    @RequestMapping("/outDepot/pageListOutDepot")
    public ModelAndView getPutInStoragePageList() {
        ModelAndView mav = new  ModelAndView("warehouse/outDepot/outDepot_list");
        mav.addObject("urlPrefix", urlPrefix);
        List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DELIVERY_RECEIPTS_TYPE.getCode());//出库类别
        mav.addObject("jcsjDtos", jcsjDtos);
        mav.addObject("auditType", AuditTypeEnum.AUDIT_GOODS_DELIVERY.getCode());
        return mav;
    }

    /**
     * 出库列表跳转页面
     *
     * @return
     */
    @RequestMapping("/outDepot/pageGetListOutDepot")
    @ResponseBody
    public Map<String, Object>  getPageListPutInOutDepot(CkglDto ckglDto){
        Map<String, Object> map = new HashMap<>();
        List<CkglDto> ckglDtos = ckglService.getPagedDtoList(ckglDto);
        map.put("rows",ckglDtos);
        map.put("total", ckglDto.getTotalNumber());
        map.put("ckdjlist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DELIVERY_RECEIPTS_TYPE.getCode()));
        return map;
    }
    /**
     * 出库列表钉钉
     *
     * @return
     */
    @RequestMapping("/outDepot/minidataGetPageListPutInOutDepot")
    @ResponseBody
    public Map<String, Object>  minidataGetPageListPutInOutDepot(CkglDto ckglDto){
        return getPageListPutInOutDepot(ckglDto);
    }
    /**
     * 出库列表跳转页面
     *
     * @return
     */
    @RequestMapping("/outDepot/delOutDepot")
    @ResponseBody
    public Map<String, Object> delOutDepot(CkglDto ckglDto, HttpServletRequest request){
        //获取用户信息
        Map<String, Object> map = new HashMap<>();
        try {
            //获取用户信息
            User user = getLoginInfo(request);
            ckglDto.setScry(user.getYhid());
            ckglDto.setScbj("1");
            boolean isSuccess = ckglService.delOutDepotPunch(ckglDto);
            map.put("status", isSuccess?"success":"fail");
            map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
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
     * 打开打印材料出库单页面
     */
    @RequestMapping("/outDepot/ckdprintOutbound")
    public ModelAndView printOutbound(CkglDto ckglDto) {
        ModelAndView mav;
        if("/labigams".equals(urlPrefix)){//labigams
            mav=new ModelAndView("storehouse/receiveMateriel/receiveMateriel_printLabOutbound.html");
        }else{
            mav=new ModelAndView("storehouse/receiveMateriel/receiveMateriel_printOutbound.html");
        }
        //获取打印信息
        JcsjDto jcsj = new JcsjDto();
        jcsj.setCsdm("CK");
        jcsj.setJclb(BasicDataTypeEnum.DOCUMENTNUM.getCode());
        jcsj = jcsjService.getDto(jcsj);
        mav.addObject("wjbh", jcsj);
        mav.addObject("dqmc", jcsj.getCskz3());
        // 查看 领料信息
        List<CkglDto> ckglDtoList= ckglService.getOutBoundOrder(ckglDto);
        for (CkglDto dto : ckglDtoList) {
            //判断是否存在U8出库单号，如果没有，使用OA单号
            if(StringUtil.isBlank(dto.getU8ckdh())) {
                dto.setU8ckdh(dto.getCkdh());
            }
            if("WW".equals(ckglDto.getCklbcskz1())){
                BigDecimal sum_qlsl = new BigDecimal("0");
                CkmxDto ckmxDto=new CkmxDto();
                ckmxDto.setCkid(dto.getCkid());
                List<CkmxDto> printDtoList = ckmxService.getPrintDtoList(ckmxDto);
                for (CkmxDto dto_t : printDtoList) {
                    if (dto_t.getCksl()!=null){
                        sum_qlsl = sum_qlsl.add(new BigDecimal(dto_t.getCksl()));
                    }
                }
                dto.setSum_qlsl(String.valueOf(sum_qlsl));
                dto.setCkmxDtos(printDtoList);
            }else if ("HZ".equals(dto.getCklbcsdm())){
                BigDecimal sum_qlsl = new BigDecimal("0");
                CkmxDto ckmxDto=new CkmxDto();
                ckmxDto.setCkid(dto.getCkid());
                List<CkmxDto> ckmxDtoList=ckmxService.getDtoMxList(ckmxDto);
                for (CkmxDto dto_t : ckmxDtoList) {
                    if (dto_t.getCkmxcksl()!=null){
                        sum_qlsl = sum_qlsl.add(new BigDecimal(dto_t.getCkmxcksl()));
                    }
                }
                dto.setSum_qlsl(String.valueOf(sum_qlsl));
                dto.setCkmxDtos(ckmxDtoList);
                mav.addObject("SFHZ", "HZ");
            }
            else{
                BigDecimal sum_qlsl = new BigDecimal("0");
                BigDecimal sum_slsl = new BigDecimal("0");
                sum_qlsl=sum_qlsl.setScale(2, RoundingMode.UP);
                sum_slsl=sum_slsl.setScale(2, RoundingMode.UP);
                // 查看 货物领料信息
                List<HwllxxDto> hwllxxDtos = hwllxxService.getDtoHwllxxListByCkidPrint(dto.getCkid());
                for (HwllxxDto hwllxxDto : hwllxxDtos) {
                    if (hwllxxDto.getQlsl()!=null){
                        sum_qlsl = sum_qlsl.add(new BigDecimal(hwllxxDto.getQlsl()));
                    }
                    if (hwllxxDto.getSlsl()!=null){
                        sum_slsl = sum_slsl.add(new BigDecimal(hwllxxDto.getSlsl()));
                    }
                }
                dto.setSum_qlsl(String.valueOf(sum_qlsl));
                dto.setSum_slsl(String.valueOf(sum_slsl));
                dto.setHwllxxDtos(hwllxxDtos);
            }
        }
        mav.addObject("ckglDtoList", ckglDtoList);
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("lbcskz1", ckglDto.getCklbcskz1());
        //根据当前账套获取打印标题
//        if(StringUtil.isNotBlank(urlPrefix)) {
//            JcsjDto jcsj = new JcsjDto();
//            jcsj.setCsdm(urlPrefix);
//            jcsj.setJclb(BasicDataTypeEnum.PRINTTITLE.getCode());
//            jcsj = jcsjService.getDto(jcsj);
//            mav.addObject("dqmc",jcsj.getCsmc());
//        }else {
//            mav.addObject("dqmc","杭州杰毅生物技术有限公司");
//        }
        return mav;
    }

    /**
     * 出库列表查看
     */
    @RequestMapping("/outDepot/viewOutbound")
    public ModelAndView viewOutbound(CkglDto ckglDto) {
        ModelAndView mav=new ModelAndView("warehouse/outDepot/outDepot_view");
        CkglDto ckglDto_t = ckglService.getDtoByCkid(ckglDto);
        CkmxDto ckmxDto = new CkmxDto();
        ckmxDto.setCkid(ckglDto.getCkid());
        List<CkmxDto> dtoMxList = ckmxService.getDtoList(ckmxDto);
        mav.addObject("ckglDto",ckglDto_t);
        mav.addObject("mxlist",dtoMxList);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 出库列表查看
     */
    @RequestMapping("/outDepot/pagedataOutbound")
    public ModelAndView pagedataviewOutbound(CkglDto ckglDto) {
        return viewOutbound(ckglDto);
    }
    /**
     * 出库列表钉钉
     */
    @RequestMapping("/outDepot/minidataGetOutbound")
    @ResponseBody
    public Map<String, Object> minidataGetOutbound(CkglDto ckglDto){
        //获取用户信息
        Map<String, Object> map = new HashMap<>();
        CkglDto ckglDto1 = ckglService.getDtoByCkid(ckglDto);
        CkmxDto ckmxDto = new CkmxDto();
        ckmxDto.setCkid(ckglDto.getCkid());
        List<CkmxDto> dtoMxList = ckmxService.getDtoMxList(ckmxDto);
        map.put("ckglDto",ckglDto1);
        map.put("dtoMxList",dtoMxList);
        return map;
    }

    /**
     * 出库红字
     */
    @RequestMapping("/outDepot/outpunchDepot")
    public ModelAndView outDepotPunch(CkglDto ckglDto) {

        ModelAndView mav=new ModelAndView("warehouse/outDepot/outDepot_punch");
        CkglDto ckglDto_t= new CkglDto();
        if (StringUtil.isNotBlank(ckglDto.getCkid()))
            ckglDto_t = ckglService.getDtoByCkid(ckglDto);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ckglDto_t.setCkrq(simpleDateFormat.format(new Date()));
        ckglDto_t.setFormAction("/warehouse/outDepot/outpunchSaveDepot");
        List<JcsjDto> cklblist = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DELIVERY_TYPE.getCode());//出库类别
        for (JcsjDto jcsjDto : cklblist) {
            if ("HZ".equals(jcsjDto.getCsdm())){
                ckglDto_t.setCklb(jcsjDto.getCsid());
                break;
            }
        }
        // User user = getLoginInfo(request);
        // user = commonService.getUserInfoById(user);
        LlglDto llglDto = new LlglDto();
        llglDto.setJgid(ckglDto_t.getSqbm());
        String jgdh = llglService.getlljgdh(llglDto);//得到机构的扩展参数
        if(StringUtil.isNotBlank(jgdh)) {
            String jlbh = ckglService.generateCkjlbh(jgdh); //生成记录编号
            ckglDto_t.setJlbh(jlbh);
        }
        ckglDto_t.setJgdm(ckglDto_t.getSqbmdm());
        ckglDto_t.setBm(ckglDto_t.getSqbm());
        ckglDto_t.setJgmc(ckglDto_t.getSqbmmc());
        String date = DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
        String prefix = "CK" + "-" + date + "-";
        String ckdh =  prefix + ckglService.generateDjh(prefix);
        ckglDto_t.setCkdh(ckdh);
        List<JcsjDto> ckxx = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.WAREHOUSE_TYPE.getCode());//仓库类别
        CkxxDto ckxxDto = new CkxxDto();
        CkxxDto kwxxDto = new CkxxDto();
        for (JcsjDto jcsjDto : ckxx) {
            if ("CK".equals(jcsjDto.getCsdm())){
                ckxxDto.setCklb(jcsjDto.getCsid());
            }
            if ("KW".equals(jcsjDto.getCsdm())){
                kwxxDto.setCklb(jcsjDto.getCsid());
            }
        }
        kwxxDto.setFckid(ckglDto_t.getCk());
        List<CkxxDto> kwlist=ckxxService.getKwDtoList(kwxxDto);
        List<CkxxDto> ckxxDtos = ckxxService.getDtoList(ckxxDto);
        mav.addObject("ckglDto",ckglDto_t);
        mav.addObject("auditType",AuditTypeEnum.AUDIT_GOODS_DELIVERY.getCode());
        mav.addObject("url","/warehouse/outDepot/pagedataOutDepotPunchInfo");
        mav.addObject("cklblist",cklblist);

        SimplePropertyPreFilter filter = new SimplePropertyPreFilter(CkxxDto.class, "ckid","fckid","ckdm","ckmc");
        SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
        listFilters[0] = filter;
        mav.addObject("kwlist", JSONObject.toJSONString(kwlist,listFilters));
        mav.addObject("ckxxDtos",ckxxDtos);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 出库红字明细
     *
     * @return
     */
    @RequestMapping("/outDepot/pagedataOutDepotPunchInfo")
    @ResponseBody
    public Map<String, Object> outDepotPunchInfo(CkglDto ckglDto){
        List<CkmxDto> dtoMxList = new ArrayList<>();
        if (StringUtil.isNotBlank(ckglDto.getCkid())){
            CkmxDto ckmxDto = new CkmxDto();
            ckmxDto.setCkid(ckglDto.getCkid());
            dtoMxList = ckmxService.getDtoMxList(ckmxDto);
            if (!CollectionUtils.isEmpty(dtoMxList)){
                for (int i = dtoMxList.size()-1; i >=0 ; i--) {
                    if (StringUtil.isNotBlank(dtoMxList.get(i).getKcksl()) && "0.00".equals(dtoMxList.get(i).getKcksl())){
                        dtoMxList.remove(i);
                    }
                }if (!CollectionUtils.isEmpty(dtoMxList)){
                    for (int i = dtoMxList.size()-1; i >=0 ; i--) {
                        if (StringUtil.isNotBlank(dtoMxList.get(i).getYymxckd())){
                            CkmxDto ckmxDto1 = new CkmxDto();
                            ckmxDto1.setCkmxid(dtoMxList.get(i).getYymxckd());
                            CkmxDto dto = ckmxService.getDto(ckmxDto1);
                            dtoMxList.get(i).setKcksl(String.valueOf(Double.parseDouble(dto.getKcksl()) + Double.parseDouble(dtoMxList.get(i).getKcksl())));
                        }
                    }
                }
            }else{
                dtoMxList = new ArrayList<>();
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("rows",dtoMxList);
        return map;
    }

    /**
     * 出库红字新增保存
     *
     * @return
     */
    @RequestMapping("/outDepot/outpunchSaveDepot")
    @ResponseBody
    public Map<String, Object> addOutDepotPunch(CkglDto ckglDto, HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        User user=getLoginInfo(request);
        ckglDto.setLrry(user.getYhid());
        ckglDto.setCkid(StringUtil.generateUUID());
        try {
            List<CkglDto> dtoList = ckglService.getDtoListByCkdh(ckglDto);
            String ckdh = "";
            if (!CollectionUtils.isEmpty(dtoList)){
                String date = DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
                String prefix = "CK" + "-" + date + "-";
                ckdh =  prefix + ckglService.generateDjh(prefix);
                ckglDto.setCkdh(ckdh);

            }
            List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DELIVERY_RECEIPTS_TYPE.getCode());//出库类别
            for (JcsjDto jcsjDto:jcsjDtos) {
                if (StringUtil.isNotBlank(jcsjDto.getCsdm()) && "HD".equals(jcsjDto.getCsdm())){
                    ckglDto.setDjlx(jcsjDto.getCsid()); //单据类型
                }
            }
            boolean success = ckglService.addOutDepotPunch(ckglDto);
            map.put("ywid",ckglDto.getCkid());
            if (success){
                map.put("status","success");
                if (!CollectionUtils.isEmpty(dtoList)){
                    map.put("message","出库单号已存在以为你替换成"+ckdh);
                }else{
                    map.put("message","保存成功！");
                }
            }else{
                map.put("status","fail");
                map.put("message","保存失败！");
            }
        } catch (BusinessException e) {
            map.put("status","fail");
            map.put("message",e.getMsg());
        }
        return map;
    }

    /**
     * 出库红字
     */
    @RequestMapping("/outDepot/modOutDepotPunch")
    public ModelAndView modOutDepotPunch(CkglDto ckglDto) {

        ModelAndView mav=new ModelAndView("warehouse/outDepot/outDepot_punch");
        CkglDto ckglDto_t = ckglService.getDtoByCkid(ckglDto);
        ckglDto_t.setFormAction("/warehouse/outDepot/modSaveOutDepotPunch");
        List<JcsjDto> cklblist = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DELIVERY_TYPE.getCode());//出库类别
        List<JcsjDto> ckxx = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.WAREHOUSE_TYPE.getCode());//仓库类别
        CkxxDto ckxxDto = new CkxxDto();
        CkxxDto kwxxDto = new CkxxDto();
        for (JcsjDto jcsjDto : ckxx) {
            if ("CK".equals(jcsjDto.getCsdm())){
                ckxxDto.setCklb(jcsjDto.getCsid());
            }
            if ("KW".equals(jcsjDto.getCsdm())){
                kwxxDto.setCklb(jcsjDto.getCsid());
            }
        }
        kwxxDto.setFckid(ckglDto_t.getCk());
        List<CkxxDto> kwlist=ckxxService.getKwDtoList(kwxxDto);
        List<CkxxDto> ckxxDtos = ckxxService.getDtoList(ckxxDto);
        mav.addObject("ckglDto",ckglDto_t);
        mav.addObject("auditType",AuditTypeEnum.AUDIT_GOODS_DELIVERY.getCode());
        mav.addObject("url","/warehouse/outDepot/pagedataOutDepotPunchInfo");
        mav.addObject("cklblist",cklblist);

        SimplePropertyPreFilter filter = new SimplePropertyPreFilter(CkxxDto.class, "ckid","fckid","ckdm","ckmc");
        SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
        listFilters[0] = filter;
        mav.addObject("kwlist", JSONObject.toJSONString(kwlist,listFilters));
        mav.addObject("ckxxDtos",ckxxDtos);
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("pdbj", "1");
        return mav;
    }

    /**
     * 出库红字修改保存
     *
     * @return
     */
    @RequestMapping("/outDepot/modSaveOutDepotPunch")
    @ResponseBody
    public Map<String, Object> modSaveOutDepotPunch(CkglDto ckglDto, HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        User user=getLoginInfo(request);
        ckglDto.setXgry(user.getYhid());
        try {
            List<CkglDto> dtoList = ckglService.getDtoListByCkdh(ckglDto);
            String ckdh = "";
            if (null != dtoList && dtoList.size()>1){
                String date = DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
                String prefix = "CK" + "-" + date + "-";
                ckdh =  prefix + ckglService.generateDjh(prefix);
                ckglDto.setCkdh(ckdh);

            }
            List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DELIVERY_RECEIPTS_TYPE.getCode());//出库类别
            for (JcsjDto jcsjDto:jcsjDtos) {
                if (StringUtil.isNotBlank(jcsjDto.getCsdm()) && "HD".equals(jcsjDto.getCsdm())){
                    ckglDto.setDjlx(jcsjDto.getCsid()); //单据类型
                }
            }
            boolean success = ckglService.modOutDepotPunch(ckglDto);
            map.put("ywid",ckglDto.getCkid());
            if (success){
                map.put("status","success");
                if (null != dtoList && dtoList.size()>1){
                    map.put("message","出库单号已存在以为你替换成"+ckdh);
                }else{
                    map.put("message","保存成功！");
                }
            }else{
                map.put("status","fail");
                map.put("message","保存失败！");
            }
        } catch (BusinessException e) {
            map.put("status","fail");
            map.put("message",e.getMsg());
        }
        return map;
    }

    /**
     * 出库单废弃
     */

    @ResponseBody
    @RequestMapping(value = "/outDepot/discardOutDepotPunch")
    public Map<String,Object> discardOutDepotPunch(CkglDto ckglDto, HttpServletRequest request){
        Map<String,Object> map=new HashMap<>();
        try {
            //获取用户信息
            User user = getLoginInfo(request);
            ckglDto.setScry(user.getYhid());
            ckglDto.setScbj("1");
            boolean isSuccess = ckglService.delOutDepotPunch(ckglDto);
            map.put("status", isSuccess?"success":"fail");
            map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
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
     * 出库审核列表
     * @param
     * @return
     */
    @RequestMapping("/outDepot/pageListOutDepotAudit")
    public ModelAndView pageListOutDepotAudit() {
        ModelAndView mav = new  ModelAndView("warehouse/outDepot/outDepot_auditList");
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("auditType", AuditTypeEnum.AUDIT_GOODS_DELIVERY.getCode());
        return mav;
    }

    /**
     * 审核列表
     *
     * @param
     * @return
     */
    @RequestMapping("/outDepot/pageGetListOutDepotAudit")
    @ResponseBody
    public Map<String, Object> getPagedListOutDepotAudit(CkglDto ckglDto,HttpServletRequest request) {
//		=================================
        //附加委托参数
        DataPermission.addWtParam(ckglDto);
        //附加审核状态过滤
        if(GlobalString.AUDIT_SHZT_YSH.equals(ckglDto.getDqshzt())){
            DataPermission.add(ckglDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "ckgl", "ckid", AuditTypeEnum.AUDIT_GOODS_DELIVERY);
        }else{
            DataPermission.add(ckglDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "ckgl", "ckid", AuditTypeEnum.AUDIT_GOODS_DELIVERY);
        }
        DataPermission.addCurrentUser(ckglDto, getLoginInfo(request));
        DataPermission.addSpDdw(ckglDto, "ckgl", SsdwTableEnum.CKGL);
        List<CkglDto> ckglDtos = ckglService.getPagedAuditCkglDto(ckglDto);
//		=================================
        Map<String, Object> map = new HashMap<>();
        map.put("total", ckglDto.getTotalNumber());
        map.put("rows", ckglDtos);
        return map;
    }


    /**
     * 审核页面
     * @param
     * @return
     */
    @RequestMapping("/outDepot/auditOutDepotPunch")
    public ModelAndView auditOutDepotPunch(CkglDto ckglDto) {
        ModelAndView mav=new ModelAndView("warehouse/outDepot/outDepot_punch");
        CkglDto ckglDto_t = ckglService.getDtoByCkid(ckglDto);
        ckglDto_t.setFormAction("/warehouse/outDepot/auditSaveOutDepotPunch");
        List<JcsjDto> cklblist = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DELIVERY_TYPE.getCode());//出库类别
        List<JcsjDto> ckxx = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.WAREHOUSE_TYPE.getCode());//仓库类别
        CkxxDto ckxxDto = new CkxxDto();
        CkxxDto kwxxDto = new CkxxDto();
        for (JcsjDto jcsjDto : ckxx) {
            if ("CK".equals(jcsjDto.getCsdm())){
                ckxxDto.setCklb(jcsjDto.getCsid());
            }
            if ("KW".equals(jcsjDto.getCsdm())){
                kwxxDto.setCklb(jcsjDto.getCsid());
            }
        }
        kwxxDto.setFckid(ckglDto_t.getCk());
        List<CkxxDto> kwlist=ckxxService.getKwDtoList(kwxxDto);
        List<CkxxDto> ckxxDtos = ckxxService.getDtoList(ckxxDto);
        mav.addObject("ckglDto",ckglDto_t);
        mav.addObject("auditType",AuditTypeEnum.AUDIT_GOODS_DELIVERY.getCode());
        mav.addObject("url","/warehouse/outDepot/pagedataOutDepotPunchInfo");
        mav.addObject("cklblist",cklblist);

        SimplePropertyPreFilter filter = new SimplePropertyPreFilter(CkxxDto.class, "ckid","fckid","ckdm","ckmc");
        SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
        listFilters[0] = filter;
        mav.addObject("kwlist", JSONObject.toJSONString(kwlist,listFilters));
        mav.addObject("ckxxDtos",ckxxDtos);
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("pdbj", "1");
        return mav;
    }
    /**
     * 出库红字审核保存
     *
     * @return
     */
    @RequestMapping("/outDepot/auditSaveOutDepotPunch")
    @ResponseBody
    public Map<String, Object> auditSaveOutDepotPunch(CkglDto ckglDto, HttpServletRequest request){
        return this.modSaveOutDepotPunch(ckglDto,request);
    }
    /**
     * 委外出库
     */
    @RequestMapping(value = "/outDepot/outsourcedeliveryOut")
    public ModelAndView outsourceDelivery(CkglDto ckglDto,HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("warehouse/outDepot/outDepot_outsourceDelivery");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        ckglDto.setCkrq(sdf.format(date));
        User user = getLoginInfo(request);
        user = commonService.getUserInfoById(user);
        ckglDto.setBm(user.getJgid());//申请部门
        ckglDto.setBmmc(user.getJgmc());
        ckglDto.setBmdm(user.getJgdm());
        String format = DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
        String prefix = "CK" + "-" + format + "-";
        ckglDto.setCkdh(prefix+ckglService.generateDjh(prefix));
        JgxxDto jgxxDto=new JgxxDto();
        jgxxDto.setJgid(user.getJgid());
        JgxxDto jgxxDto_t = jgxxService.selectJgxxByJgid(jgxxDto);
        ckglDto.setJlbh(ckglService.generateCkjlbh(jgxxDto_t.getKzcs1()));
        List<JcsjDto> ckxx = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.WAREHOUSE_TYPE.getCode());//仓库类别
        CkxxDto ckxxDto = new CkxxDto();
        for (JcsjDto jcsjDto : ckxx) {
            if ("CK".equals(jcsjDto.getCsdm())){
                ckxxDto.setCklb(jcsjDto.getCsid());
            }
        }
        List<CkxxDto> ckxxDtos = ckxxService.getDtoList(ckxxDto);
        mav.addObject("ckxxDtos",ckxxDtos);
        mav.addObject("url","/warehouse/outDepot/pagedataOutsourcedeliveryOut");
        mav.addObject("ckglDto",ckglDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }


    /**
     * 委外出库获取列表数据
     *
     * @param
     * @return
     */
    @RequestMapping("/outDepot/pagedataOutsourcedeliveryOut")
    @ResponseBody
    public Map<String, Object> getOutsourceDeliveryList(CkglDto ckglDto) {
        Map<String, Object> map = new HashMap<>();
        List<CkglDto> ckglDtos = new ArrayList<>();
        map.put("total", ckglDto.getTotalNumber());
        map.put("rows", ckglDtos);
        return map;
    }


    /**
     * 选择合同明细列表
     *
     * @param htmxDto
     * @return
     */
    @RequestMapping(value = "/outDepot/pagedataChooseContractDetailList")
    public ModelAndView chooseContractDetailList(HtmxDto htmxDto) {
        ModelAndView mav = new ModelAndView("warehouse/outDepot/outDepot_chooseContractDetail");
        mav.addObject("htmxDto", htmxDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 获取合同明细列表数据
     *
     * @param htmxDto
     * @return
     */
    @RequestMapping("/outDepot/pagedataGetPageListContractDetail")
    @ResponseBody
    public Map<String, Object> getPageListContractDetail(HtmxDto htmxDto) {
        Map<String, Object> map = new HashMap<>();
        List<HtmxDto> list = htmxService.getPageListContractDetail(htmxDto);
        map.put("total", htmxDto.getTotalNumber());
        map.put("rows", list);
        return map;
    }


    /**
     * 获取合同明细关联生产结构数据
     *
     * @param htmxDto
     * @return
     */
    @RequestMapping("/outDepot/pagedataGetContractDetailWithStructure")
    @ResponseBody
    public Map<String, Object> getContractDetailWithStructure(HtmxDto htmxDto) {
        Map<String, Object> map = new HashMap<>();
        List<HtmxDto> list = htmxService.getContractDetailWithStructure(htmxDto);
        map.put("htmxDtos", list);
        return map;
    }


    /**
     * 跳转生产结构明细列表
     *
     * @param htmxDto
     * @return
     */
    @RequestMapping(value = "/outDepot/pagedataChooseProductStructureInfo")
    public ModelAndView chooseProductStructureInfo(HtmxDto htmxDto) {
        ModelAndView mav = new ModelAndView("warehouse/outDepot/outDepot_chooseProductStructureInfo");
        List<HtmxDto> htmxDtos = htmxService.getContractDetailWithStructure(htmxDto);
        mav.addObject("htmxDtos", htmxDtos);
        mav.addObject("htmxDto", htmxDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 选择仓库列表
     *
     * @param hwxxDto
     * @return
     */
    @RequestMapping(value = "/outDepot/pagedataChooseStockList")
    public ModelAndView chooseStockList(HwxxDto hwxxDto) {
        ModelAndView mav = new ModelAndView("warehouse/outDepot/outDepot_chooseStock");
        mav.addObject("hwxxDto", hwxxDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 获取选择仓库列表数据
     *
     * @param hwxxDto
     * @return
     */
    @RequestMapping("/outDepot/pagedataGetPageChooseStock")
    @ResponseBody
    public Map<String, Object> getPageChooseStock(HwxxDto hwxxDto) {
        Map<String, Object> map = new HashMap<>();
        List<HwxxDto> hwxxDtos = hwxxService.getPagedListByWlid(hwxxDto);
        map.put("total", hwxxDto.getTotalNumber());
        map.put("rows", hwxxDtos);
        return map;
    }
    /**
     * 委外出库保存
     *
     * @param ckglDto
     * @return
     */
    @RequestMapping("/outDepot/outsourcedeliverySaveOut")
    @ResponseBody
    public Map<String, Object> saveOutsourceDelivery(CkglDto ckglDto,HttpServletRequest request) throws BusinessException {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        ckglDto.setLrry(user.getYhid());
        ckglDto.setZsxm(user.getZsxm());
        ckglDto.setFlr(user.getYhid());
        List<JcsjDto> cklblist = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DELIVERY_TYPE.getCode());//出库类别
        for (JcsjDto jcsjDto:cklblist) {
            if ("WW".equals(jcsjDto.getCskz1())){
                ckglDto.setCklb(jcsjDto.getCsid()); //出库类别
                ckglDto.setCklbcskz1(jcsjDto.getCskz1()); //出库类别
            }
        }
        boolean isSuccess=ckglService.saveOutsourceDelivery(ckglDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 获取出库明细列表用于异常
     */
    @RequestMapping("/outDepot/pagedataListForException")
    @ResponseBody
    public Map<String, Object> pagedataListForException(CkmxDto ckmxDto){
        List<CkmxDto> t_List = ckmxService.getPagedForException(ckmxDto);
        Map<String,Object> result = new HashMap<>();
        result.put("total", ckmxDto.getTotalNumber());
        result.put("rows", t_List);
        return result;
    }

}
