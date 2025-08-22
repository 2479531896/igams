package com.matridx.igams.warehouse.controller;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.cloud.controller.BaseBasicController;
import com.matridx.igams.common.dao.entities.GzglDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.DataPermissionTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.svcinterface.IGzglService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.production.dao.entities.*;
import com.matridx.igams.production.service.svcinterface.ISO_SODetailsService;
import com.matridx.igams.production.service.svcinterface.ISO_SOMainService;
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
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/ship")
public class ShipController extends BaseBasicController {

    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Autowired
    IFhglService fhglService;
    @Autowired
    IGzglService gzglService;
    @Autowired
    IJcsjService jcsjService;
    @Autowired
    ISO_SODetailsService soDetailsService;
    @Autowired
    ISO_SOMainService soMainService;
	@Autowired
    IFhmxService fhmxService;
    @Autowired
    IXsglService xsglService;
    @Autowired
    IXsmxService xsmxService;
	@Override
	public String getPrefix(){
		return urlPrefix;
	}
	@Autowired
	IXxglService xxglService;
	@Autowired
    IHwxxService hwxxService;
	@Autowired
    ICkmxService ckmxService;
	@Autowired
    ICkglService ckglService;
    /**
     * 发货列表
     
     */
    @RequestMapping("/ship/pageListShip")
    public ModelAndView pageListShip() {
        ModelAndView mav = new  ModelAndView("warehouse/ship/ship_list");
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("auditType", AuditTypeEnum.AUDIT_SHIPPING.getCode());
        return mav;
    }

    /**
     * 选择订单号
     * @param
     
     */
    @RequestMapping("/ship/pagedataDdhPage")
    public ModelAndView pagedataDdhPage(GzglDto gzglDto){
        ModelAndView mav = new ModelAndView("warehouse/ship/ship_listDdh");
        mav.addObject("GzglDto", gzglDto);
        mav.addObject("urlprefix", urlPrefix);
        return mav;
    }

    /**
     * 选择客户列表
     * @param
     
     */
    @RequestMapping("/ship/pagedataGetListKh")
    @ResponseBody
    public Map<String, Object> pagedataGetListKh(GzglDto gzglDto){
        List<GzglDto> t_List = gzglService.getPagedKhList(gzglDto);
        Map<String,Object> result = new HashMap<>();
        result.put("total", gzglDto.getTotalNumber());
        result.put("rows", t_List);
        return result;
    }

    /**
     * 选择客户
     * @param
     
     */
    @RequestMapping("/ship/pagedataListKh")
    public ModelAndView pagedataListKh(GzglDto gzglDto){
        ModelAndView mav = new ModelAndView("warehouse/ship/ship_listKh");
        mav.addObject("GzglDto", gzglDto);
        mav.addObject("urlprefix", urlPrefix);
        return mav;
    }
    /**
     * 发货列表
     *
     * @param fhglDto
     
     */
    @RequestMapping("/ship/pageGetListShip")
    @ResponseBody
    public Map<String, Object> getPageDtoShipList(FhglDto fhglDto){
        Map<String, Object> map = new HashMap<>();
        List<FhglDto> list = fhglService.getPagedDtoFhxxList(fhglDto);
        map.put("total", fhglDto.getTotalNumber());
        map.put("rows", list);
        return map;
    }
    /**
     * 发货列表钉钉
     *
     * @param fhglDto
     
     */
    @RequestMapping("/ship/minidataGetPageDtoShipList")
    @ResponseBody
    public Map<String, Object> minidataGetPageDtoShipList(FhglDto fhglDto){
        return getPageDtoShipList(fhglDto);
    }
    /**
     * 查看按钮
     
     */
    @RequestMapping("/ship/viewShip")
    public ModelAndView viewShip(FhglDto fhglDto) {
        ModelAndView mav = new  ModelAndView("warehouse/ship/ship_view");
        FhglDto dtoByid = fhglService.getDtoByid(fhglDto);
        mav.addObject("fhglDto",dtoByid);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 查看
     
     */
    @RequestMapping("/ship/pagedataShip")
    @ResponseBody
    public Map<String, Object> pagedataShip(FhglDto fhglDto){
        Map<String, Object> map = new HashMap<>();
        FhglDto dtoByid = fhglService.getDtoByid(fhglDto);
        map.put("xsid", dtoByid.getDdh());
        return map;
    }
    /**
     * 查看明细
     
     */
    @RequestMapping("/ship/pagedataShipMx")
    @ResponseBody
    public Map<String, Object> pagedataShipMx(FhglDto fhglDto){
        Map<String, Object> map = new HashMap<>();
        List<FhmxDto> list = fhmxService.getDtoMxList(fhglDto.getFhid());
        map.put("rows", list);
        return map;
    }
    /**
     * 发货查看钉钉
     *
     * @param fhglDto
     
     */
    @RequestMapping("/ship/minidataGetViewShip")
    @ResponseBody
    public Map<String, Object> minidataGetViewShip(FhglDto fhglDto){
        Map<String, Object> map = new HashMap<>();
        FhglDto dtoByid = fhglService.getDtoByid(fhglDto);
        List<FhmxDto> list = fhmxService.getDtoMxList(fhglDto.getFhid());
        map.put("fhglDto", dtoByid);
        map.put("mxlist", list);
        return map;
    }

    /**
     * 删除
     
     */
    @RequestMapping("/ship/delShip")
    @ResponseBody
    public  Map<String, Object> delShip(FhglDto fhglDto,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        //判断调拨单是否存在
        User user = getLoginInfo(request);
        fhglDto.setScry(user.getYhid());
        if(!CollectionUtils.isEmpty(fhglDto.getIds())) {
            try {
                fhglService.shipFhDelSave(fhglDto);
                map.put("status","success");
                map.put("message","删除成功");
            } catch (BusinessException e) {
                map.put("status","fail");
                map.put("message",e.getMsg());
            }
        }else{
            map.put("status","fail");
            map.put("message","未获取到发货id");
        }
        return map;
    }
    /**
     * 发货功能跳转页面
     
     */
    @RequestMapping("/ship/shipMents")
    public ModelAndView stockPendingPageList(HttpServletRequest request, FhglDto fhglDto) {
        ModelAndView mav = new  ModelAndView("warehouse/ship/ship_shipments");
        Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(
                new BasicDataTypeEnum[] { BasicDataTypeEnum.SELLING_TYPE});
        if (StringUtil.isNotBlank(fhglDto.getFhid())){
            fhglDto = fhglService.getDtoByid(fhglDto);
            FhmxDto fhmxDto = new FhmxDto();
            fhmxDto.setFhid(fhglDto.getFhid());
            List<FhmxDto> fhmxDtos = fhmxService.getDtoAllByFhid(fhmxDto);
            String ids = "";
            for (FhmxDto dto : fhmxDtos) {
                if (StringUtil.isBlank(ids)){
                    ids += dto.getXsmxid();
                }else {
                    ids += ","+dto.getXsmxid();
                }
            }
            mav.addObject("xsmxids", ids);

            //送检信息
            SimplePropertyPreFilter filter = new SimplePropertyPreFilter(FhmxDto.class, "xsmxid","hwid","fhsl","wlid","ck","ckqxlx");
            SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
            listFilters[0] = filter;
            mav.addObject("fhmxDtos", JSONObject.toJSONString(fhmxDtos,listFilters));
            fhglDto.setFormAction("/ship/ship/shipSaveMents");
        }else{
            User user = getLoginInfo(request);
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            fhglDto.setDjrq(simpleDateFormat.format(date));
            fhglDto.setJsr(user.getYhid());
            fhglDto.setJsrmc(user.getZsxm());
            String fhdh = fhglService.getFhglFhdhh();
            fhglDto.setFhdh(fhdh);
            fhglDto.setFormAction("/ship/ship/shipSaveMents");
        }
        mav.addObject("xslist", jclist.get(BasicDataTypeEnum.SELLING_TYPE.getCode()));
        mav.addObject("url", "/ship/ship/pagedataGetpicking");
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("fhglDto", fhglDto);
        mav.addObject("auditType", AuditTypeEnum.AUDIT_SHIPPING.getCode());
        return mav;
    }
    
    
    /**
     * 发货审核跳转页面
     
     */
    @RequestMapping("/ship/auditMents")
    public ModelAndView auditMents(HttpServletRequest request, FhglDto fhglDto) {
        ModelAndView mav = new  ModelAndView("warehouse/ship/ship_shipments");
        Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(
                new BasicDataTypeEnum[] { BasicDataTypeEnum.SELLING_TYPE});
        if (StringUtil.isNotBlank(fhglDto.getFhid())){
            fhglDto = fhglService.getDtoByid(fhglDto);
            FhmxDto fhmxDto = new FhmxDto();
            fhmxDto.setFhid(fhglDto.getFhid());
            List<FhmxDto> fhmxDtos = fhmxService.getDtoAllByFhid(fhmxDto);
            String ids = "";
            for (FhmxDto dto : fhmxDtos) {
                if (StringUtil.isBlank(ids)){
                    ids += dto.getXsmxid();
                }else {
                    ids += ","+dto.getXsmxid();
                }
            }
            mav.addObject("xsmxids", ids);

            //送检信息
            SimplePropertyPreFilter filter = new SimplePropertyPreFilter(FhmxDto.class, "xsmxid","hwid","fhsl","wlid","ck","ckqxlx");
            SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
            listFilters[0] = filter;
            mav.addObject("fhmxDtos", JSONObject.toJSONString(fhmxDtos,listFilters));
            fhglDto.setFormAction("/ship/ship/shipSaveMents");
        }else{
            User user = getLoginInfo(request);
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            fhglDto.setDjrq(simpleDateFormat.format(date));
            fhglDto.setJsr(user.getYhid());
            fhglDto.setJsrmc(user.getZsxm());
            String fhdh = fhglService.getFhglFhdhh();
            fhglDto.setFhdh(fhdh);
            fhglDto.setFormAction("/ship/ship/shipSaveMents");
        }
        mav.addObject("xslist", jclist.get(BasicDataTypeEnum.SELLING_TYPE.getCode()));
        mav.addObject("url", "/ship/ship/pagedataGetpicking");
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("fhglDto", fhglDto);
        mav.addObject("auditType", AuditTypeEnum.AUDIT_SHIPPING.getCode());
        return mav;
    }


    /**
     * 发货明细
     * @param
     
     */
    @RequestMapping("/ship/pagedataGetpicking")
    @ResponseBody
    public Map<String, Object> getPicking(XsmxDto xsmxDto) {
        Map<String, Object> map = new HashMap<>();
        List<XsmxDto> xsmxDtos = new ArrayList<>();
        if(!CollectionUtils.isEmpty(xsmxDto.getXsmxids())) {
            xsmxDtos = xsmxService.getDtoList(xsmxDto);
        }
        map.put("rows", xsmxDtos);
        map.put("total", xsmxDto.getTotalNumber());
        return map;
    }

    /**
     * 选择订单号
     * @param
     
     */
    @RequestMapping("/ship/pagedataListDdh")
    @ResponseBody
    public Map<String, Object> listDdh(XsglDto xsglDto){
        Map<String,Object> result = new HashMap<>();
        xsglDto.setZt(StatusEnum.CHECK_PASS.getCode());
        List<XsglDto> xsglDtoList = xsglService.getPagedDtoList(xsglDto);
//        List<SO_SOMainDto> so_soMainDtos = soMainService.getPagedDtoList(so_soMainDto);
//        so_soMainDto.setTotalNumber(soMainService.getToTalNumber());

        result.put("rows", xsglDtoList);
        result.put("total", xsglDto.getTotalNumber());
        return result;
    }

    /**
     * 选择订单明细信息
     * @param
     
     */
    @RequestMapping("/ship/getSO_SODetailsInfo")
    @ResponseBody
    public Map<String, Object> getSO_SODetailsInfo(SO_SODetailsDto so_soDetailsDto){
        Map<String,Object> result = new HashMap<>();
        List<SO_SODetailsDto> so_soDetailsDtos = soDetailsService.getSO_SODetailsInfo(so_soDetailsDto);
        result.put("soDetailsDtos", so_soDetailsDtos);
        return result;
    }

    /**
     * 选择订单明细信息
     * @param
     
     */
    @RequestMapping("/ship/pagedataGetXsmxInfo")
    @ResponseBody
    public Map<String, Object> getXsmxInfo(XsmxDto xsmxDto){
        Map<String,Object> result = new HashMap<>();
        xsmxDto.setCzbs("1");
        List<XsmxDto> xsmxDtos = xsmxService.getDtoList(xsmxDto);
        result.put("xsmxDtos", xsmxDtos);
        return result;
    }

    /**
     * 跳转销售明细列表
     *
     * @param
     
     */
    @RequestMapping(value = "/ship/chooseListXsmxInfoU8")
    public ModelAndView chooseListPurchaseInfoU8(SO_SODetailsDto so_soDetailsDto) {
        ModelAndView mav = new ModelAndView("systemmain/task/task_soDetailschooseInfoList");
        SO_SOMainDto so_soMainDto = new SO_SOMainDto();
        so_soMainDto.setID(so_soDetailsDto.getID());
        SO_SOMainDto soMainDto = soMainService.getSO_SOMainInfo(so_soMainDto);
        List<SO_SODetailsDto> soDetailsDtos = soDetailsService.getSO_SODetailsInfo(so_soDetailsDto);
        mav.addObject("soDetailsDtos", soDetailsDtos);
        mav.addObject("soMainDto", soMainDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 跳转销售明细列表
     *
     * @param
     
     */
    @RequestMapping(value = "/ship/pagedataChooseListXsmxInfo")
    public ModelAndView chooseListPurchaseInfo(XsmxDto xsmxDto) {
        ModelAndView mav = new ModelAndView("systemmain/task/task_soDetailschooseInfoList");
        XsglDto xsglDto = xsglService.getDtoById(xsmxDto.getXsid());
        List<XsmxDto> xsmxDtos = xsmxService.getDtoList(xsmxDto);
        if (StringUtil.isNotBlank(xsmxDto.getFhid())){
            List<FhmxDto> list = fhmxService.getDtoMxList(xsmxDto.getFhid());
            if (!CollectionUtils.isEmpty(list) && !CollectionUtils.isEmpty(xsmxDtos)){
                for (FhmxDto dto : list) {
                    for (XsmxDto xsmxDto1 : xsmxDtos) {
                        if (dto.getXsmxid().equals(xsmxDto1.getXsmxid())){
                            xsmxDto1.setYfhsl(String.valueOf(Double.parseDouble(xsmxDto1.getYfhsl()) - Double.parseDouble(dto.getFhsl())));
                            xsmxDto1.setKysl(String.valueOf(Double.parseDouble(xsmxDto1.getKysl()) + Double.parseDouble(dto.getFhsl())));
                        }
                    }
                }

            }
        }
        mav.addObject("xsmxDtos", xsmxDtos);
        mav.addObject("xsglDto", xsglDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 获取销售明细信息
     *
     * @param
     
     */
    @RequestMapping("/ship/pagedataGetXsmxByIds")
    @ResponseBody
    public Map<String, Object> getXsmxByIds(XsmxDto xsmxDto) {
        Map<String, Object> map = new HashMap<>();
        List<XsmxDto> xsmxDtos = xsmxService.getDtoList(xsmxDto);
        if (StringUtil.isNotBlank(xsmxDto.getFhid())){
            List<FhmxDto> list = fhmxService.getDtoMxList(xsmxDto.getFhid());
            if (!CollectionUtils.isEmpty(list) && !CollectionUtils.isEmpty(xsmxDtos)){
                for (FhmxDto dto : list) {
                    for (XsmxDto xsmxDto1 : xsmxDtos) {
                        if (dto.getXsmxid().equals(xsmxDto1.getXsmxid())){
                            xsmxDto1.setYfhsl(String.valueOf(Double.parseDouble(xsmxDto1.getYfhsl()) - Double.parseDouble(dto.getFhsl())));
                            xsmxDto1.setKysl(String.valueOf(Double.parseDouble(xsmxDto1.getKysl()) + Double.parseDouble(dto.getFhsl())));
                        }
                    }
                }

            }
        }
        map.put("xsmxDtos", xsmxDtos);
        return map;
    }
    /**
     * 物料明细选择界面
     */
    @RequestMapping("/ship/pagedataChooseXsmx")
    public ModelAndView chooseXsmx(HwxxDto hwxxDto) {
        ModelAndView mav = new ModelAndView("warehouse/ship/ship_chooseXsmx");
        mav.addObject("hwxxDto", hwxxDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 获取销售明细信息
     *
     * @param
     
     */
    @RequestMapping("/ship/getXsmxByIdsU8")
    @ResponseBody
    public Map<String, Object> getXsmxByIdsU8(SO_SODetailsDto so_soDetailsDto) {
        Map<String, Object> map = new HashMap<>();
        List<SO_SODetailsDto> soDetailsDtos = null;
        soDetailsDtos = soDetailsService.getSO_SODetailsInfo(so_soDetailsDto);
        map.put("soDetailsDtos", soDetailsDtos);
        return map;
    }



    /**
     * 出货保存
     * @param
     
     */
    @RequestMapping("/ship/shipSaveMents")
    @ResponseBody
    public Map<String,Object> shipFhModSave(FhglDto fhglDto, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        //判断调拨单是否存在
        User user = getLoginInfo(request);
        fhglDto.setXgry(user.getYhid());
        // 判断入库单号是否重复
        boolean isSuccess = true;
        List<FhglDto> fhdhRepeat = fhglService.isFhdhRepeat(fhglDto);
        if (null != fhdhRepeat && fhdhRepeat.size()>1){
            isSuccess = false;
        }
        if(!isSuccess) {
            map.put("status", "fail");
            map.put("message", "发货单号不允许重复！");
            return map;
        }
        try {
        	//判断是新增还是修改
        	if(StringUtil.isNotBlank(fhglDto.getFhid())) {
        		isSuccess = fhglService.shipFhModSave(fhglDto);
        	}else {
        		isSuccess = fhglService.shipFhAddSave(fhglDto);
        	}
        	map.put("status", isSuccess ? "success" : "fail");
            map.put("ywid", StringUtil.isNotBlank(fhglDto.getFhid())?fhglDto.getFhid():"");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
                    : xxglService.getModelById("ICOM00002").getXxnr());
            
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
        }
        return map;
    }
    
    /**
     * 刷新发货单号
     * @param
     
     */
    @RequestMapping("/ship/pagedataRefreshNumber")
    @ResponseBody
    public Map<String, Object> RefreshNumber(){
        Map<String,Object> map = new HashMap<>();
        map.put("fhdh",fhglService.getFhglFhdhh());
        return map;
    }


    /**
     * 	审核列表
     
     */
    @RequestMapping("/ship/pageListDeviceAudit")
    public ModelAndView pageListDeviceAudit() {
        ModelAndView mav = new  ModelAndView("warehouse/ship/ship_auditList");
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 	审核列表
     * @param
     
     */
    @RequestMapping("/ship/pageGetListDeviceAudit")
    @ResponseBody
    public Map<String, Object> getListShipAudit(FhglDto fhglDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        // 附加委托参数
        DataPermission.addWtParam(fhglDto);
        // 附加审核状态过滤
        if (GlobalString.AUDIT_SHZT_YSH.equals(fhglDto.getDqshzt())) {
            DataPermission.add(fhglDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "fhgl", "fhid",
                    AuditTypeEnum.AUDIT_SHIPPING);
        } else {
            DataPermission.add(fhglDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "fhgl", "fhid",
                    AuditTypeEnum.AUDIT_SHIPPING);
        }
        DataPermission.addCurrentUser(fhglDto, getLoginInfo(request));
        List<FhglDto> listMap = fhglService.getPagedAuditDevice(fhglDto);
        map.put("total", fhglDto.getTotalNumber());
        map.put("rows", listMap);
        return map;
    }
    /**
     * 	钉钉发货审核列表
     * @param
     
     */
    @RequestMapping("/ship/minidataDeviceAudit")
    @ResponseBody
    public Map<String, Object> minidataDeviceAudit(FhglDto fhglDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        // 附加委托参数
        DataPermission.addWtParam(fhglDto);
        // 附加审核状态过滤
        if (GlobalString.AUDIT_SHZT_YSH.equals(fhglDto.getDqshzt())) {
            DataPermission.add(fhglDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "fhgl", "fhid",
                    AuditTypeEnum.AUDIT_SHIPPING);
        } else {
            DataPermission.add(fhglDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "fhgl", "fhid",
                    AuditTypeEnum.AUDIT_SHIPPING);
        }
        DataPermission.addCurrentUser(fhglDto, getLoginInfo(request));
        List<FhglDto> listMap = fhglService.getPagedAuditDevice(fhglDto);
        map.put("total", fhglDto.getTotalNumber());
        map.put("rows", listMap);
        return map;
    }
    /**
     * 点击快递维护弹出模态框

     */
    @RequestMapping(value = "/ship/expressmaintenanceShip")
    @ResponseBody
    public ModelAndView expressmaintenanceShip(FhglDto fhglDto) {
        fhglDto=fhglService.getDtoByid(fhglDto);
        ModelAndView mav = new ModelAndView("warehouse/ship/ship_expressMaintenance");
        fhglDto.setFormAction("expressmaintenanceSaveShip");
        mav.addObject("fhglDto",fhglDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     *将物流信息插入到数据库中
     * @param
     
     */
    @RequestMapping("/ship/expressmaintenanceSaveShip")
    @ResponseBody
    public Map<String, Object> expressmaintenanceSaveShip(FhglDto fhglDto) {
        Map<String, Object> map=new HashMap<>();
        boolean isSuccess;
        isSuccess = fhglService.expressMaintenanceFh(fhglDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        map.put("status", isSuccess?"success":"fail");
        return map;
    }
    /**
     * 销售打印

     */
    @RequestMapping(value = "/ship/saleprintRequisition")
    @ResponseBody
    public ModelAndView saleprintRequisition(FhglDto fhglDto) {
        ModelAndView mav = new ModelAndView("warehouse/ship/ship_salePrint");
        List<CkglDto> ckglDtos=ckglService.selectCkglDtoByFhids(fhglDto);
        List<CkmxDto> ckmxDtos=ckmxService.selectCkmxListByList(ckglDtos);
        if (!CollectionUtils.isEmpty(ckglDtos)){
            for (CkglDto ckglDto : ckglDtos) {
                List<CkmxDto> ckmxDtoList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(ckmxDtos)) {
                    for (CkmxDto ckmxDto : ckmxDtos) {
                        if (ckmxDto.getCkid().equals(ckglDto.getCkid())) {
                            ckmxDtoList.add(ckmxDto);
                        }
                    }
                }
                ckglDto.setCkmxDtos(ckmxDtoList);
            }
        }
        JcsjDto jcsjDto=new JcsjDto();
        jcsjDto.setCsdm("xsck");
        jcsjDto.setJclb(BasicDataTypeEnum.DOCUMENTNUM.getCode());
        jcsjDto = jcsjService.getDto(jcsjDto);
        mav.addObject("wjbh",jcsjDto);
        mav.addObject("ckglDtos",ckglDtos);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

//    /**
//     * 审核
//     
//     */
//    @RequestMapping("/ship/auditShipCheck")
//    public ModelAndView auditShipCheck(FhglDto fhglDto) {
//        ModelAndView mav = new ModelAndView("warehouse/ship/ship_shipments");
//        mav.addObject("urlPrefix", urlPrefix);
//        FhglDto dtoById = fhglService.getDto(fhglDto);
//        dtoById.setFormAction("shipFhModSave");
//        mav.addObject("fhglDto", dtoById);
//        return mav;
//    }
}
