package com.matridx.igams.production.controller;

import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.DataPermissionTypeEnum;
import com.matridx.igams.common.enums.ExportTypeEnum;
import com.matridx.igams.common.enums.SsdwTableEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.production.dao.entities.SbpdDto;
import com.matridx.igams.production.dao.entities.SbpdmxDto;
import com.matridx.igams.production.service.svcinterface.ISbpdService;
import com.matridx.igams.production.service.svcinterface.ISbpdmxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/inventory")
public class EquipmentInventoryController extends BaseController {
    private final Logger log = LoggerFactory.getLogger(EquipmentInventoryController.class);
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Override
    public String getPrefix(){
        return urlPrefix;
    }
    @Autowired
    private ISbpdService sbpdService;
    @Autowired
    private ISbpdmxService sbpdmxService;
    @Autowired
    IShgcService shgcService;
    @Autowired
    IXxglService xxglService;
    /**
     * 设备盘点列表
     */
    @RequestMapping("/inventory/pageGetListEquipmentInventory")
    @ResponseBody
    public Map<String, Object> pageGetListEquipmentInventory(SbpdDto sbpdDto,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        super.setCzdmList(request,map);
        User user = getLoginInfo(request);
        if ("1".equals(sbpdDto.getSfbmxz())){
            DataPermission.addCurrentUser(sbpdDto,user);
            DataPermission.addJsDdw(sbpdDto, "sbpd", SsdwTableEnum.SBPD);
        }
        List<SbpdDto> list = sbpdService.getPagedDtoList(sbpdDto);
        try{
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_EQUIPMENT_INVENTORY.getCode(), "zt", "sbpdid", new String[]{
                    StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
        } catch (BusinessException e){
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }
        List<Map<String,String>> bms=sbpdService.getDepartments();
        map.put("total", sbpdDto.getTotalNumber());
        map.put("rows", list);
        map.put("bmlist", bms);
        map.put("auditType",AuditTypeEnum.AUDIT_EQUIPMENT_INVENTORY.getCode());
        map.put("select", ExportTypeEnum.INVENTORY_SELECT.getCode());
        map.put("search",ExportTypeEnum.INVENTORY_SEARCH.getCode());
        return map;
    }

    /**
     * 设备盘点列表  查看
     */
    @RequestMapping("/inventory/viewEquipmentInventory")
    @ResponseBody
    public Map<String, Object> viewEquipmentInventory(SbpdDto sbpdDto) {
        Map<String, Object> map = new HashMap<>();
        SbpdDto sbpdDto_t = sbpdService.getDto(sbpdDto);
        SbpdmxDto sbpdmxDto=new SbpdmxDto();
        sbpdmxDto.setSbpdid(sbpdDto.getSbpdid());
        List<SbpdmxDto> dtoList = sbpdmxService.getDtoList(sbpdmxDto);
        map.put("sbpdDto", sbpdDto_t);
        map.put("sbpdmxDtos", dtoList);
        return map;
    }

    /**
     * 设备盘点列表  删除
     */
    @RequestMapping("/inventory/delEquipmentInventory")
    @ResponseBody
    public Map<String, Object> delEquipmentInventory(SbpdDto sbpdDto,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        sbpdDto.setScry(user.getYhid());
        boolean isSuccess = sbpdService.delEquipmentInventory(sbpdDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
        return map;
    }

    /**
     * 	设备盘点审核列表
     */
    @RequestMapping("/inventory/pageGetListEquipmentInventoryAudit")
    @ResponseBody
    public Map<String, Object> pageGetListEquipmentInventoryAudit(SbpdDto sbpdDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        // 附加委托参数
        DataPermission.addWtParam(sbpdDto);
        // 附加审核状态过滤
        if (GlobalString.AUDIT_SHZT_YSH.equals(sbpdDto.getDqshzt())) {
            DataPermission.add(sbpdDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "sbpd", "sbpdid",
                    AuditTypeEnum.AUDIT_EQUIPMENT_INVENTORY);
        } else {
            DataPermission.add(sbpdDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "sbpd", "sbpdid",
                    AuditTypeEnum.AUDIT_EQUIPMENT_INVENTORY);
        }
        DataPermission.addCurrentUser(sbpdDto, getLoginInfo(request));
        DataPermission.addSpDdw(sbpdDto, "sbpd", SsdwTableEnum.SBPD);
        List<SbpdDto> listMap = sbpdService.getPagedAuditEquipmentInventory(sbpdDto);
        map.put("total", sbpdDto.getTotalNumber());
        map.put("auditType",AuditTypeEnum.AUDIT_EQUIPMENT_INVENTORY.getCode());
        map.put("rows", listMap);
        super.setCzdmList(request,map);
        return map;
    }

    /**
     * 设备盘点列表  新增
     */
    @RequestMapping("/inventory/addEquipmentInventory")
    @ResponseBody
    public Map<String, Object> addEquipmentInventory(SbpdDto sbpdDto,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        sbpdDto.setLrry(user.getYhid());
        boolean isSuccess = sbpdService.addEquipmentInventory(sbpdDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 设备盘点列表  提交
     */
    @RequestMapping("/inventory/submitEquipmentInventory")
    @ResponseBody
    public Map<String, Object> submitEquipmentInventory(SbpdDto sbpdDto) {
        Map<String, Object> stringObjectMap = viewEquipmentInventory(sbpdDto);
        stringObjectMap.put("shlb",AuditTypeEnum.AUDIT_EQUIPMENT_INVENTORY.getCode());
        return stringObjectMap;
    }

    /**
     * 设备盘点列表  提交保存
     */
    @RequestMapping("/inventory/submitSaveEquipmentInventory")
    @ResponseBody
    public Map<String, Object> submitSaveEquipmentInventory(SbpdDto sbpdDto,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        sbpdDto.setXgry(user.getYhid());
        boolean isSuccess = sbpdService.modEquipmentInventory(sbpdDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 设备盘点列表  审核
     */
    @RequestMapping("/inventory/auditEquipmentInventory")
    @ResponseBody
    public Map<String, Object> auditEquipmentInventory(SbpdDto sbpdDto) {
        Map<String, Object> stringObjectMap = viewEquipmentInventory(sbpdDto);
        stringObjectMap.put("shlb",AuditTypeEnum.AUDIT_EQUIPMENT_INVENTORY.getCode());
        return stringObjectMap;
    }

}
