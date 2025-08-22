package com.matridx.igams.production.controller;

import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.cloud.controller.BaseBasicController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.DataPermissionTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.production.dao.entities.SbysDto;
import com.matridx.igams.production.service.svcinterface.ISbysService;
import com.matridx.igams.storehouse.dao.entities.HwxxDto;
import com.matridx.igams.storehouse.dao.entities.XzllmxDto;
import com.matridx.igams.storehouse.service.svcinterface.IHwxxService;
import com.matridx.igams.storehouse.service.svcinterface.IXzllmxService;
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
@RequestMapping("/device")
public class DeviceCheckController extends BaseBasicController {
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    ISbysService sbysService;
    @Autowired
    IShgcService shgcService;
    @Autowired
    IXxglService xxglService;
    @Autowired
    IHwxxService hwxxService;
    @Autowired
    IFjcfbService fjcfbService;
    @Autowired
    IXzllmxService xzllmxService;
    @Override
    public String getPrefix(){
        return urlPrefix;
    }

    /**
     * 设备列表
     */
    @RequestMapping("/device/pageListDeviceCheck")
    public ModelAndView pageListDeviceCheck(SbysDto sbysDto) {
        ModelAndView mav = new ModelAndView("deviceinfo/devicecheck/deviceCheck_list");
        mav.addObject("ssxmlist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.RESEARCH_ITEM.getCode()));
        mav.addObject("urlPrefix", urlPrefix);
        sbysDto.setAuditType( AuditTypeEnum.AUDIT_DEVICECHECK.getCode());
        mav.addObject("sbysDto", sbysDto);
        return mav;
    }

    /**
     * 设备列表数据
     */
    @RequestMapping("/device/pageGetListDeviceCheck")
    @ResponseBody
    public Map<String, Object> getPageListDevice(SbysDto sbysDto) {
        Map<String, Object> map = new HashMap<>();
        List<SbysDto> list = sbysService.getPagedDtoList(sbysDto);
        try{
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_DEVICECHECK.getCode(), "zt", "sbysid", new String[]{
                    StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
        } catch (BusinessException e){
            // TODO Auto-generated catch block

        }
        map.put("total", sbysDto.getTotalNumber());
        map.put("rows", list);
        return map;
    }

    /**
     * 设备验收查看
     */
    @RequestMapping("/device/viewDeviceCheck")
    public ModelAndView viewDeviceCheck(SbysDto sbysDto) {
        ModelAndView mav = new ModelAndView("deviceinfo/devicecheck/deviceCheck_view");
        SbysDto dtoById = sbysService.getDtoById(sbysDto.getHwid());
        //查询附件
        FjcfbDto fjcfbDto = new FjcfbDto();
        fjcfbDto.setYwlx(BusTypeEnum.IMP_DEVICE_CHECK.getCode());
        fjcfbDto.setYwid(dtoById.getSbysid());
        List<FjcfbDto> t_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        mav.addObject("t_fjcfbDtos",t_fjcfbDtos);
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("sbysDto", dtoById);
        return mav;
    }
    /**
     * 设备验收查看 sbysid
     */
    @RequestMapping("/device/pagedataViewDeviceCheck")
    public ModelAndView pagedataViewDeviceCheck(SbysDto sbysDto) {
        ModelAndView mav = new ModelAndView("deviceinfo/devicecheck/deviceCheck_view");
        SbysDto dtoById = sbysService.getSbysDtoById(sbysDto.getSbysid());
        //查询附件
        FjcfbDto fjcfbDto = new FjcfbDto();
        fjcfbDto.setYwlx(BusTypeEnum.IMP_DEVICE_CHECK.getCode());
        fjcfbDto.setYwid(dtoById.getSbysid());
        List<FjcfbDto> t_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        mav.addObject("t_fjcfbDtos",t_fjcfbDtos);
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("sbysDto", dtoById);
        return mav;
    }
    /**
     * 设备维护
     */
    @RequestMapping("/device/addDeviceCheck")
    public ModelAndView addDeviceCheck(SbysDto sbysDto,HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("deviceinfo/devicecheck/deviceCheck_edit");
        mav.addObject("urlPrefix", urlPrefix);
        sbysDto.setFormAction("addSaveDeviceCheck");
        List<JcsjDto> sblxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.EQUIPMENT_TYPE.getCode());
        List<JcsjDto> qwfsList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DECONTAMINATION_METHOD.getCode());
        // 设置默认创建日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        sbysDto.setYsrq(sdf.format(date));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String now=simpleDateFormat.format(date);
        String s="YS-"+now;
        String sbysdh = sbysService.getSbysdh(s);
        sbysDto.setSbysdh(sbysdh);
        HwxxDto scphAndBxsjByHwid = hwxxService.getScphAndBxsjByHwid(sbysDto.getHwid());
        if (scphAndBxsjByHwid!=null){
            sbysDto.setSbccbh(scphAndBxsjByHwid.getScph());
            sbysDto.setBxsj(scphAndBxsjByHwid.getBxsj());
            sbysDto.setGys(scphAndBxsjByHwid.getGys());
        }
        User user = getLoginInfo(request);
        sbysDto.setGlry(user.getYhid());
        sbysDto.setGlrymc(user.getZsxm());
        sbysDto.setYsr(user.getYhid());
        sbysDto.setYsrmc(user.getZsxm());
        mav.addObject("ywlx", BusTypeEnum.IMP_DEVICE_CHECK.getCode());
        mav.addObject("sbysDto", sbysDto);
        mav.addObject("sblxList", sblxList);
        mav.addObject("qwfsList", qwfsList);
        return mav;
    }

    /**
     * 设备维护保存
     */
    @RequestMapping("/device/addSaveDeviceCheck")
    @ResponseBody
    public Map<String, Object> addSaveDeviceCheck(SbysDto sbysDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        sbysDto.setLrry(user.getYhid());
        sbysDto.setZt(StatusEnum.CHECK_NO.getCode());
        HwxxDto hwxxDto = hwxxService.getDtoById(sbysDto.getHwid());
        if(hwxxDto!=null){
            if(StringUtil.isNotBlank(hwxxDto.getReduce())){
                sbysDto.setYssl(hwxxDto.getReduce());
            }
        }
        List<String> gdzcbhs = sbysService.getDtoByGdzcbh(sbysDto);
        if (!CollectionUtils.isEmpty(gdzcbhs) && !"/".equals(sbysDto.getGdzcbh())){
            map.put("status", "repetition");
            map.put("urlPrefix",urlPrefix);
        }else {
            sbysDto.setTzzt("0");
            boolean isSuccess = sbysService.insertDto(sbysDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
            map.put("ywid",sbysDto.getSbysid());
            map.put("auditType", AuditTypeEnum.AUDIT_DEVICECHECK.getCode());
            map.put("urlPrefix",urlPrefix);
        }
        return map;
    }

    /**
     * 修改
     */
    @RequestMapping("/device/submitDeviceCheck")
    public ModelAndView submitDeviceCheck(SbysDto sbysDto) {
        ModelAndView mav = new ModelAndView("deviceinfo/devicecheck/deviceCheck_edit");
        List<JcsjDto> sblxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.EQUIPMENT_TYPE.getCode());
        List<JcsjDto> qwfsList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DECONTAMINATION_METHOD.getCode());
        mav.addObject("urlPrefix", urlPrefix);
        SbysDto dtoById = sbysService.getDtoById(sbysDto.getHwid());
        dtoById.setFormAction("modSaveDeviceCheck");
        dtoById.setXgqgdzcbh(dtoById.getGdzcbh());
        FjcfbDto fjcfbDto=new FjcfbDto();
        fjcfbDto.setYwid(dtoById.getSbysid());
        fjcfbDto.setYwlx(BusTypeEnum.IMP_DEVICE_CHECK.getCode());
        List<FjcfbDto> fjcfbDtos = fjcfbService.getDtoList(fjcfbDto);
        mav.addObject("fjcfbDtos",fjcfbDtos);
        mav.addObject("ywlx", BusTypeEnum.IMP_DEVICE_CHECK.getCode());
        mav.addObject("sbysDto", dtoById);
        mav.addObject("sblxList", sblxList);
        mav.addObject("qwfsList", qwfsList);
        return mav;
    }

    /**
     * 提交
     */
    @RequestMapping("/device/modDeviceCheck")
    public ModelAndView modDeviceCheck(SbysDto sbysDto) {
        ModelAndView mav = new ModelAndView("deviceinfo/devicecheck/deviceCheck_edit");
        mav.addObject("urlPrefix", urlPrefix);
        List<JcsjDto> sblxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.EQUIPMENT_TYPE.getCode());
        List<JcsjDto> qwfsList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DECONTAMINATION_METHOD.getCode());
        SbysDto dtoById = sbysService.getDtoById(sbysDto.getHwid());
        dtoById.setFormAction("modSaveDeviceCheck");
        dtoById.setXgqgdzcbh(dtoById.getGdzcbh());
        FjcfbDto fjcfbDto=new FjcfbDto();
        fjcfbDto.setYwid(dtoById.getSbysid());
        fjcfbDto.setYwlx(BusTypeEnum.IMP_DEVICE_CHECK.getCode());
        List<FjcfbDto> fjcfbDtos = fjcfbService.getDtoList(fjcfbDto);
        mav.addObject("fjcfbDtos",fjcfbDtos);
        mav.addObject("ywlx", BusTypeEnum.IMP_DEVICE_CHECK.getCode());
        mav.addObject("sblxList", sblxList);
        mav.addObject("qwfsList", qwfsList);
        mav.addObject("sbysDto", dtoById);
        return mav;
    }
    /**
     * 提交
     */
    @RequestMapping("/device/pagedataModDeviceCheck")
    public ModelAndView pagedataModDeviceCheck(XzllmxDto xzllmxDto) {
        ModelAndView mav = new ModelAndView("deviceinfo/devicecheck/deviceCheck_edit");
        mav.addObject("urlPrefix", urlPrefix);
        xzllmxDto=xzllmxService.getDtoById(xzllmxDto.getXzllmxid());
        SbysDto sbysDto=new SbysDto();
        sbysDto.setSbmc(xzllmxDto.getHwmc());
        sbysDto.setGgxh(xzllmxDto.getHwbz());
        sbysDto.setXzllid(xzllmxDto.getXzllid());
        sbysDto.setXzllmxid(xzllmxDto.getXzllmxid());
        sbysDto.setFormAction("pagedataAddDeviceCheck");
        List<FjcfbDto> fjcfbDtos=new ArrayList<>();
        if (StringUtil.isNotBlank(xzllmxDto.getSbysid())){
            sbysDto=sbysService.getSbysDtoById(xzllmxDto.getSbysid());
            FjcfbDto fjcfbDto=new FjcfbDto();
            fjcfbDto.setYwid(sbysDto.getSbysid());
            fjcfbDto.setYwlx(BusTypeEnum.IMP_DEVICE_CHECK.getCode());
            fjcfbDtos = fjcfbService.getDtoList(fjcfbDto);
            sbysDto.setFormAction("modSaveDeviceCheck");
        }
        List<JcsjDto> sblxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.EQUIPMENT_TYPE.getCode());
        List<JcsjDto> qwfsList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DECONTAMINATION_METHOD.getCode());
        mav.addObject("ywlx", BusTypeEnum.IMP_DEVICE_CHECK.getCode());
        mav.addObject("sblxList", sblxList);
        mav.addObject("qwfsList", qwfsList);
        mav.addObject("sbysDto", sbysDto);
        mav.addObject("fjcfbDtos",fjcfbDtos);
        return mav;
    }
    /**
     * 设备维护保存
     */
    @RequestMapping("/device/pagedataAddDeviceCheck")
    @ResponseBody
    public Map<String, Object> pagedataAddDeviceCheck(SbysDto sbysDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        sbysDto.setLrry(user.getYhid());
        sbysDto.setZt(StatusEnum.CHECK_NO.getCode());
        sbysDto.setSbzt("00");
        XzllmxDto xzllmxDto_t=xzllmxService.getDtoById(sbysDto.getXzllmxid());
        sbysDto.setSbmc(xzllmxDto_t.getHwmc());
        sbysDto.setGgxh(xzllmxDto_t.getHwbz());
        List<String> gdzcbhs = sbysService.getDtoByGdzcbh(sbysDto);
        if (!CollectionUtils.isEmpty(gdzcbhs) && !"/".equals(sbysDto.getGdzcbh())){
            map.put("status", "repetition");
            map.put("urlPrefix",urlPrefix);
        }else {
            sbysDto.setTzzt("0");
            sbysDto.setZt("80");
            boolean isSuccess = sbysService.insertDto(sbysDto);
            if (isSuccess){
                XzllmxDto xzllmxDto=new XzllmxDto();
                xzllmxDto.setXgry(user.getYhid());
                xzllmxDto.setXzllmxid(sbysDto.getXzllmxid());
                map.put("xzllmxid",xzllmxDto.getXzllmxid());
                xzllmxDto.setSbysid(sbysDto.getSbysid());
                isSuccess=xzllmxService.update(xzllmxDto);
            }
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
            map.put("ywid",sbysDto.getSbysid());
            map.put("auditType", AuditTypeEnum.AUDIT_DEVICECHECK.getCode());
            map.put("urlPrefix",urlPrefix);
        }
        return map;
    }
    /**
     * 审核
     */
    @RequestMapping("/device/auditDeviceCheck")
    public ModelAndView auditDeviceCheck(SbysDto sbysDto) {
        ModelAndView mav = new ModelAndView("deviceinfo/devicecheck/deviceCheck_edit");
        mav.addObject("urlPrefix", urlPrefix);
        List<JcsjDto> sblxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.EQUIPMENT_TYPE.getCode());
        List<JcsjDto> qwfsList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DECONTAMINATION_METHOD.getCode());
        SbysDto dtoById = sbysService.getDto(sbysDto);
        dtoById.setFormAction("modSaveDeviceCheck");
        FjcfbDto fjcfbDto=new FjcfbDto();
        fjcfbDto.setYwid(dtoById.getSbysid());
        fjcfbDto.setYwlx(BusTypeEnum.IMP_DEVICE_CHECK.getCode());
        List<FjcfbDto> fjcfbDtos = fjcfbService.getDtoList(fjcfbDto);
        mav.addObject("fjcfbDtos",fjcfbDtos);
        mav.addObject("ywlx", BusTypeEnum.IMP_DEVICE_CHECK.getCode());
        mav.addObject("sblxList", sblxList);
        mav.addObject("qwfsList", qwfsList);
        mav.addObject("sbysDto", dtoById);
        return mav;
    }

    /**
     * 修改保存
     */
    @RequestMapping("/device/modSaveDeviceCheck")
    @ResponseBody
    public Map<String, Object> modSaveDeviceCheck(SbysDto sbysDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        sbysDto.setXgry(user.getYhid());
        List<String> gdzcbhs = sbysService.getDtoByGdzcbh(sbysDto);
        if (!CollectionUtils.isEmpty(gdzcbhs) && !"/".equals(sbysDto.getGdzcbh())){
            map.put("status", "repetition");
            map.put("urlPrefix",urlPrefix);
        }else {
            boolean isSuccess = sbysService.updatePageEvent(sbysDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
            map.put("ywid",sbysDto.getSbysid());
            if (StringUtil.isNotBlank(sbysDto.getXzllmxid())){
                map.put("xzllmxid",sbysDto.getXzllmxid());
            }
            map.put("auditType", AuditTypeEnum.AUDIT_DEVICECHECK.getCode());
            map.put("urlPrefix",urlPrefix);
        }
        return map;
    }

    /**
     * 废弃
     */
    @RequestMapping("/device/discardDeviceCheck")
    @ResponseBody
    public Map<String, Object> discardDeviceCheck(SbysDto sbysDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        sbysDto.setXgry(user.getYhid());
        boolean isSuccess = sbysService.discard(sbysDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 删除
     */
    @RequestMapping("/device/delDeviceCheck")
    @ResponseBody
    public Map<String, Object> delDeviceCheck(SbysDto sbysDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        List<SbysDto> list = sbysService.getDtoList(sbysDto);
        List<String> hwids=new ArrayList<>();
        List<String> sbysids=new ArrayList<>();
        List<String> ywids=new ArrayList<>();
        if(!CollectionUtils.isEmpty(list)){
            for(SbysDto dto:list){
                if(StatusEnum.CHECK_NO.getCode().equals(dto.getRkzt())||StringUtil.isBlank(dto.getRkzt())){
                    sbysids.add(dto.getSbysid());
                    ywids.add(dto.getSbysid());
                    if(!hwids.contains(dto.getHwid())){
                        hwids.add(dto.getHwid());
                    }
                }else{
                    map.put("status", "fail");
                    map.put("message", "设备编码为  "+dto.getWlbm()+" 的数据已经入库，无法删除！");
                    return map;
                }
            }
        }
        boolean isSuccess =false;
        if(!CollectionUtils.isEmpty(sbysids)){
            User user = getLoginInfo(request);
            SbysDto sbysDto_t=new SbysDto();
            sbysDto_t.setIds(sbysids);
            sbysDto_t.setScry(user.getYhid());
            sbysService.delete(sbysDto_t);
            HwxxDto hwxxDto=new HwxxDto();
            hwxxDto.setIds(hwids);
            hwxxDto.setZt("02");
            hwxxDto.setXgry(user.getYhid());
            hwxxDto.setSbysid(null);
            isSuccess=hwxxService.updateForSbys(hwxxDto);
            shgcService.deleteByYwids(ywids);
        }
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00003").getXxnr() : xxglService.getModelById("ICOM00004").getXxnr());
        return map;
    }

    /**
     * 	审核列表
     */
    @RequestMapping("/device/pageListDeviceAudit")
    public ModelAndView pageListDeviceAudit() {
        ModelAndView mav = new  ModelAndView("deviceinfo/devicecheck/deviceCheck_auditList");
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 	审核列表
     */
    @RequestMapping("/device/pageGetListDeviceAudit")
    @ResponseBody
    public Map<String, Object> getListDeviceAudit(SbysDto sbysDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        // 附加委托参数
        DataPermission.addWtParam(sbysDto);
        // 附加审核状态过滤
        if (GlobalString.AUDIT_SHZT_YSH.equals(sbysDto.getDqshzt())) {
            DataPermission.add(sbysDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "sbys", "sbysid",
                    AuditTypeEnum.AUDIT_DEVICECHECK);
        } else {
            DataPermission.add(sbysDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "sbys", "sbysid",
                    AuditTypeEnum.AUDIT_DEVICECHECK);
        }
        DataPermission.addCurrentUser(sbysDto, getLoginInfo(request));
        List<SbysDto> listMap = sbysService.getPagedAuditDevice(sbysDto);
        map.put("total", sbysDto.getTotalNumber());
        map.put("rows", listMap);
        return map;
    }

    /**
     * 高级修改
     */
    @RequestMapping("/device/advancedmodDeviceCheck")
    @ResponseBody
    public Map<String, Object> advanceModDeviceCheck(SbysDto sbysDto) {
        Map<String, Object> map = new HashMap<>();
        boolean isSuccess=false;
       if(StringUtil.isNotBlank(sbysDto.getYsjg())){
           if("0".equals(sbysDto.getYsjg())){
               sbysDto.setYsjg("1");
               HwxxDto hwxxDto=new HwxxDto();
               hwxxDto.setHwid(sbysDto.getHwid());
               hwxxDto.setSl(sbysDto.getYssl());
               hwxxDto.setZjthsl("0");
               hwxxService.update(hwxxDto);
           }else if("1".equals(sbysDto.getYsjg())){
               sbysDto.setYsjg("0");
               HwxxDto dtoById = hwxxService.getDtoById(sbysDto.getHwid());
               if(dtoById!=null){
                   Double sl=Double.parseDouble(dtoById.getSl());
                   Double yssl=Double.parseDouble(StringUtil.isNotBlank(sbysDto.getYssl())?sbysDto.getYssl():"0");
                   HwxxDto hwxxDto=new HwxxDto();
                   hwxxDto.setHwid(sbysDto.getHwid());
                   hwxxDto.setSl(String.valueOf(sl-yssl));
                   hwxxDto.setZjthsl(sbysDto.getYssl());
                   hwxxService.update(hwxxDto);
               }
           }
           isSuccess=sbysService.update(sbysDto);
       }
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }


    /**
     * 打印
     */
    @RequestMapping("/device/printDeviceCheck")
    public ModelAndView printDeviceCheck(SbysDto sbysDto) {
        ModelAndView mav=new ModelAndView("deviceinfo/devicecheck/deviceCheck_print");
        mav.addObject("urlPrefix", urlPrefix);
        SbysDto dtoById = sbysService.getDto(sbysDto);
        if(dtoById!=null){
            mav.addObject("sbysDto", dtoById);
        }else{
            SbysDto sbysDto_t=new SbysDto();
            mav.addObject("sbysDto", sbysDto_t);
        }
        return mav;
    }
    /**
     * 刷新单号
     */
    @RequestMapping("/device/pagedataRefreshDh")
    @ResponseBody
    public Map<String, Object> refreshDh() {
        Map<String, Object> map = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String now=sdf.format(date);
        String s="YS-"+now;
        map.put("sbysdh",  sbysService.getSbysdh(s));
        return map;
    }
}
