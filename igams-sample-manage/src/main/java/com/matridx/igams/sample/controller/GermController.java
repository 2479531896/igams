package com.matridx.igams.sample.controller;

import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.cloud.controller.BaseBasicController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.DataPermissionTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.sample.dao.entities.JzkcxxDto;
import com.matridx.igams.sample.dao.entities.JzllcDto;
import com.matridx.igams.sample.dao.entities.JzllglDto;
import com.matridx.igams.sample.dao.entities.JzllmxDto;
import com.matridx.igams.sample.service.svcinterface.IJzkcxxService;
import com.matridx.igams.sample.service.svcinterface.IJzllcService;
import com.matridx.igams.sample.service.svcinterface.IJzllglService;
import com.matridx.igams.sample.service.svcinterface.IJzllmxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@code @author:JYK}
 */
@Controller
@RequestMapping("/germ")
public class GermController extends BaseBasicController {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    IJzkcxxService jzkxxService;
    @Autowired
    IXxglService xxglService;
    @Autowired
    IJzllglService jzllglService;
    @Autowired
    IJzllmxService jzllmxService;
    @Autowired
    IJzllcService jzllcService;
    @Autowired
    ICommonService commonService;
    /**
     * 跳转菌种库存列表界面
     */
    @RequestMapping(value = "/inventory/pageListGerm")
    public ModelAndView pageListGerm() {
        ModelAndView mav=new ModelAndView("germ/stock/germStock_list");
        List<JcsjDto> jzfllist = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.GERM_CLASS.getCode());//菌种分类
        List<JcsjDto> jzlxlist = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.GERM_TYPE.getCode());//菌种类型
        JzllcDto jzllcDto=new JzllcDto();
        User user=getLoginInfo();
        jzllcDto.setRyid(user.getYhid());
        List<JzllcDto> jzllcDtos=jzllcService.getDtoList(jzllcDto);
        StringBuilder ids= new StringBuilder();
        if (jzllcDtos!=null&&!jzllcDtos.isEmpty()) {
            for (JzllcDto dto : jzllcDtos) {
                ids.append(",").append(dto.getJzkcid());
            }
        }
        mav.addObject("ids", ids.toString());
        mav.addObject("llcsl",jzllcDtos.size());
        mav.addObject("jzfllist",jzfllist);
        mav.addObject("jzlxlist",jzlxlist);
        return mav;
    }

    /**
     * 获取菌种库存列表
     */
    @RequestMapping(value = "/inventory/pageGetListGerm")
    @ResponseBody
    public Map<String,Object> pageGetListGerm(JzkcxxDto jzkcxxDto){
        Map<String, Object> map= new HashMap<>();
        List<JzkcxxDto> jzkcxxDtos=jzkxxService.getPagedDtoList(jzkcxxDto);
        map.put("rows",jzkcxxDtos);
        map.put("total",jzkcxxDto.getTotalNumber());
        return map;
    }
    /**
     * 跳转菌种库存列表查看
     */
    @RequestMapping(value = "/inventory/viewGerm")
    public ModelAndView viewGerm(JzkcxxDto jzkcxxDto) {
        ModelAndView mav=new ModelAndView("germ/stock/germStock_view");
        jzkcxxDto=jzkxxService.getDtoById(jzkcxxDto.getJzkcid());
        mav.addObject("jzkcxxDto",jzkcxxDto);
        return mav;
    }
    /**
     * 菌种库存列表新增保存
     */
    @RequestMapping(value = "/inventory/pagedataViewGerm")
    @ResponseBody
    public Map<String,Object> pagedataViewGerm(JzkcxxDto jzkcxxDto){
        Map<String, Object> map= new HashMap<>();
        jzkcxxDto=jzkxxService.getDtoById(jzkcxxDto.getJzkcid());
        map.put("jzkcxxDto",jzkcxxDto);
        return map;
    }
    /**
     * 跳转菌种库存列表新增页面
     */
    @RequestMapping(value = "/inventory/addGerm")
    public ModelAndView addGerm(JzkcxxDto jzkcxxDto) {
        ModelAndView mav=new ModelAndView("germ/stock/germStock_edit");
        List<JcsjDto> jzfllist = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.GERM_CLASS.getCode());//菌种分类
        List<JcsjDto> jzlxlist = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.GERM_TYPE.getCode());//菌种类型
        jzkcxxDto.setFormAction("addSaveGerm");
        mav.addObject("jzfllist",jzfllist);
        mav.addObject("jzlxlist",jzlxlist);
        mav.addObject("jzkcxxDto",jzkcxxDto);
        return mav;
    }

    /**
     * 菌种库存列表新增保存
     */
    @RequestMapping(value = "/inventory/addSaveGerm")
    @ResponseBody
    public Map<String,Object> addSaveGerm(JzkcxxDto jzkcxxDto){
        Map<String, Object> map= new HashMap<>();
        boolean isSuccess;
        User user=getLoginInfo();
        jzkcxxDto.setLrry(user.getYhid());
        jzkcxxDto.setJzkcid(StringUtil.generateUUID());
        jzkcxxDto.setKcl(jzkcxxDto.getRksl());
        jzkcxxDto.setYds("0");
        isSuccess=jzkxxService.insert(jzkcxxDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }
    /**
     * 跳转菌种库存列表修改页面
     */
    @RequestMapping(value = "/inventory/modGerm")
    public ModelAndView modGerm(JzkcxxDto jzkcxxDto) {
        ModelAndView mav=new ModelAndView("germ/stock/germStock_edit");
        List<JcsjDto> jzfllist = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.GERM_CLASS.getCode());//菌种分类
        List<JcsjDto> jzlxlist = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.GERM_TYPE.getCode());//菌种类型
        jzkcxxDto=jzkxxService.getDtoById(jzkcxxDto.getJzkcid());
        JzllmxDto jzllmxDto=new JzllmxDto();
        jzllmxDto.setJzkcid(jzkcxxDto.getJzkcid());
        List<JzllmxDto> jzllmxDtos=jzllmxService.getDtoList(jzllmxDto);
        if (!CollectionUtils.isEmpty(jzllmxDtos)){
            jzkcxxDto.setFlag("1");
        }
        jzkcxxDto.setFormAction("modSaveGerm");
        mav.addObject("jzfllist",jzfllist);
        mav.addObject("jzlxlist",jzlxlist);
        mav.addObject("jzkcxxDto",jzkcxxDto);
        return mav;
    }

    /**
     * 菌种库存列表修改保存
     */
    @RequestMapping(value = "/inventory/modSaveGerm")
    @ResponseBody
    public Map<String,Object> modSaveGerm(JzkcxxDto jzkcxxDto){
        Map<String, Object> map= new HashMap<>();
        boolean isSuccess;
        User user=getLoginInfo();
        jzkcxxDto.setXgry(user.getYhid());
        jzkcxxDto.setKcl(jzkcxxDto.getRksl());
        isSuccess=jzkxxService.update(jzkcxxDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }
    /**
     * 菌种库存列表删除数据
     */
    @RequestMapping(value = "/inventory/delGerm")
    @ResponseBody
    public Map<String,Object> delGerm(JzkcxxDto jzkcxxDto){
        Map<String, Object> map= new HashMap<>();
        boolean isSuccess;
        User user=getLoginInfo();
        jzkcxxDto.setScry(user.getYhid());
        isSuccess=jzkxxService.delete(jzkcxxDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
        return map;
    }
    /**
     * 跳转菌种领料列表界面
     */
    @RequestMapping(value = "/inventory/pageListGermPick")
    public ModelAndView pageListGermPick(JzllglDto jzllglDto) {
        ModelAndView mav=new ModelAndView("germ/stock/germPick_list");
        jzllglDto.setAuditType(AuditTypeEnum.AUDIT_GERM_PICK.getCode());
        List<JcsjDto> jzfllist = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.GERM_CLASS.getCode());//菌种分类
        List<JcsjDto> jzlxlist = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.GERM_TYPE.getCode());//菌种类型
        mav.addObject("jzfllist",jzfllist);
        mav.addObject("jzlxlist",jzlxlist);
        mav.addObject("jzllglDto",jzllglDto);
        return mav;
    }

    /**
     * 获取菌种领料列表
     */
    @RequestMapping(value = "/inventory/pageGetListGermPick")
    @ResponseBody
    public Map<String,Object> pageGetListGermPick(JzllglDto jzllglDto){
        Map<String, Object> map= new HashMap<>();
        List<JzllglDto> jzllglDtos=jzllglService.getPagedDtoList(jzllglDto);
        map.put("rows",jzllglDtos);
        map.put("total",jzllglDto.getTotalNumber());
        return map;
    }
    /**
     * 跳转菌种领料列表查看
     */
    @RequestMapping(value = "/inventory/viewGermPick")
    public ModelAndView viewGermPick(JzllglDto jzllglDto) {
        ModelAndView mav=new ModelAndView("germ/stock/germPick_view");
        jzllglDto=jzllglService.getDtoById(jzllglDto.getLlid());
        mav.addObject("jzllglDto",jzllglDto);
        return mav;
    }
    /**
     * 获取菌种领料列查看明细
     */
    @RequestMapping(value = "/inventory/pagedataGermPickMx")
    @ResponseBody
    public Map<String,Object> pagedataGermPickMx(JzllmxDto jzllmxDto){
        Map<String, Object> map= new HashMap<>();
        List<JzllmxDto> jzllmxDtos=jzllmxService.getDtoListByllid(jzllmxDto.getLlid());
        map.put("rows",jzllmxDtos);
        return map;
    }
    /**
     * 跳转菌种领料审核列表
     */
    @RequestMapping(value = "/auditing/pageListGermAuditing")
    public ModelAndView pageListGermAuditing() {
        return new ModelAndView("germ/stock/germPick_auditList");
    }
    /**
     * 获取菌种领料审核列表
     */
    @RequestMapping(value = "/auditing/pageGetListGermAuditing")
    @ResponseBody
    public Map<String,Object> pageGetListGermAuditing(JzllglDto jzllglDto, HttpServletRequest request){
        // 附加委托参数
        DataPermission.addWtParam(jzllglDto);
        // 附加审核状态过滤
        if (GlobalString.AUDIT_SHZT_YSH.equals(jzllglDto.getDqshzt())) {
            DataPermission.add(jzllglDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "jzllgl", "llid",
                    AuditTypeEnum.AUDIT_GERM_PICK);
        } else {
            DataPermission.add(jzllglDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "jzllgl", "llid",
                    AuditTypeEnum.AUDIT_GERM_PICK);
        }
        DataPermission.addCurrentUser(jzllglDto, getLoginInfo(request));
        List<JzllglDto> jzllglDtoList = jzllglService.getPagedAuditPick(jzllglDto);
        Map<String, Object> map= new HashMap<>();
        map.put("total", jzllglDto.getTotalNumber());
        map.put("rows", jzllglDtoList);
        return map;
    }
    /**
     * 打开打印材料领料单页面
     */
    @RequestMapping("/inventory/lldprintGerm")
    public ModelAndView lldprintGerm(JzllglDto jzllglDto) {
        ModelAndView mav=new ModelAndView("germ/stock/germPick_print");
        //获取上半部分
        List<JzllglDto> jzllglDtos = jzllglService.getDtoByIds(jzllglDto);
        for (JzllglDto dto : jzllglDtos) {
            //获取下半部分
            List<JzllmxDto> jzllmxDtos = jzllmxService.getDtoListByllid(dto.getLlid());
            dto.setJzllmxDtos(jzllmxDtos);
        }
        mav.addObject("jzllglDtos", jzllglDtos);
        return mav;
    }

    /**
     * 将物料添加至领料车
     */
    @RequestMapping("/inventory/pagedataAddToPickingCar")
    @ResponseBody
    public Map<String, Object> addToPickingCar(JzllcDto jzllcDto,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        jzllcDto.setRyid(user.getYhid());
        boolean isSuccess = jzllcService.insert(jzllcDto);
        List<JzllcDto> jzllcDtos = jzllcService.getDtoList(jzllcDto);
        StringBuilder ids= new StringBuilder();
        if(jzllcDtos!=null&&!jzllcDtos.isEmpty()) {
            for (JzllcDto dto : jzllcDtos) {
                ids.append(",").append(dto.getJzkcid());
            }
            ids.substring(1);
        }
        map.put("ids", ids.toString());
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }
    /**
     * 从借用车删除物料
     */
    @RequestMapping("/inventory/pagedataDelFromPickingCarr")
    @ResponseBody
    public Map<String, Object> delFromBorrowingCar(JzllcDto jzllcDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        jzllcDto.setRyid(user.getYhid());
        boolean isSuccess = jzllcService.delete(jzllcDto);
        // 获取领料车已加入的物料数量
        List<JzllcDto> jzllcDtos = jzllcService.getDtoList(jzllcDto);
        String ids = "";
        if (jzllcDtos != null && !jzllcDtos.isEmpty()) {
            for (JzllcDto dto : jzllcDtos) {
                ids = "," + dto.getJzkcid();
            }
            ids = ids.substring(1);
        }
        map.put("ids", ids);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00003").getXxnr()
                : xxglService.getModelById("ICOM00004").getXxnr());
        return map;
    }

    /**
     * 领料车新增页面
     */
    @RequestMapping("/inventory/pickingcarGermPicking")
    public ModelAndView pickingcarGermPicking(JzllglDto jzllglDto, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("germ/stock/germPick_edit");
        //获取当前用户
        User user = getLoginInfo(request);
        jzllglDto.setLlr(user.getYhid());
        user = commonService.getUserInfoById(user);
        if (user != null) {
            jzllglDto.setLlrmc(user.getZsxm());
            jzllglDto.setBmmc(user.getJgmc());
            jzllglDto.setBm(user.getJgid());
            SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
            Date date = new Date();
            jzllglDto.setSqrq(sdf.format(date));//申请时间
        }
        jzllglDto.setAuditType(AuditTypeEnum.AUDIT_GERM_PICK.getCode());
        jzllglDto.setLldh(jzllglService.generateLldh()); //生成领料单
        jzllglDto.setFormAction("/germ/inventory/addSaveGermPicking");
        mav.addObject("url", "/germ/inventory/pagedataGermPicking");
        mav.addObject("jzllglDto", jzllglDto);
        return mav;
    }

    /**
     * 领料新增页面
     */
    @RequestMapping("/inventory/addGermPicking")
    public ModelAndView addGermPicking(JzllglDto jzllglDto, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("germ/stock/germPick_edit");
        //获取当前用户
        User user = getLoginInfo(request);
        jzllglDto.setLlr(user.getYhid());
        user = commonService.getUserInfoById(user);
        if (user != null) {
            jzllglDto.setLlrmc(user.getZsxm());
            jzllglDto.setBmmc(user.getJgmc());
            jzllglDto.setBm(user.getJgid());
            SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
            Date date = new Date();
            jzllglDto.setSqrq(sdf.format(date));//申请时间
        }
        jzllglDto.setAuditType(AuditTypeEnum.AUDIT_GERM_PICK.getCode());
        jzllglDto.setLldh(jzllglService.generateLldh()); //生成领料单
        jzllglDto.setFormAction("/germ/inventory/addSaveGermPicking");
        mav.addObject("url", "/germ/inventory/pagedataGermPicking");
        mav.addObject("jzllglDto", jzllglDto);
        return mav;
    }

    /**
     * 根据输入信息查询菌种
     */
    @RequestMapping("/inventory/pagedataQueryGerm")
    @ResponseBody
    public Map<String,Object> pagedataQueryGerm(JzkcxxDto jzkcxxDto){
        List<JzkcxxDto> jzkcxxDtos = jzkxxService.getDtoList(jzkcxxDto);
        Map<String,Object> map = new HashMap<>();
        map.put("jzkcxxDtos", jzkcxxDtos);
        return map;
    }
    /**
     * 根据id查询信息
     */
    @RequestMapping("/inventory/pagedataQueryKcxxById")
    @ResponseBody
    public Map<String,Object> pagedataQueryKcxxById(String jzkcid){
        JzkcxxDto jzkcxxDto = jzkxxService.getDtoById(jzkcid);
        Map<String,Object> map = new HashMap<>();
        map.put("jzkcxxDto", jzkcxxDto);
        return map;
    }

    /**
     * 领料明细
     */
    @RequestMapping("/inventory/pagedataGermPicking")
    @ResponseBody
    public Map<String, Object> getGermPicking(JzllglDto jzllglDto,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user=getLoginInfo(request);
        //判断领料id是否为空，不为空是修改，空是新增
        if(StringUtil.isNotBlank(jzllglDto.getLlid())) {
            List<JzllmxDto> jzllmxDtos = jzllmxService.getDtoListByllid(jzllglDto.getLlid());
            map.put("rows", jzllmxDtos);
        }else {
            //若为新增从领料车获取已加入领料车的数据
            JzllcDto jzllcDto=new JzllcDto();
            jzllcDto.setRyid(user.getYhid());
            List<JzllcDto> llcgllist = jzllcService.getLlcDtoList(jzllcDto);
            if(llcgllist!=null && !llcgllist.isEmpty()) {
                map.put("rows", llcgllist);
            }else {
                map.put("rows", null);
            }
        }
        return map;
    }
    /**
     * 新增保存
     */
    @RequestMapping("/inventory/addSaveGermPicking")
    @ResponseBody
    public Map<String,Object> addSaveGermPicking(JzllglDto jzllglDto){
        Map<String,Object> map = new HashMap<>();
        User user=getLoginInfo();
        jzllglDto.setLrry(user.getYhid());
        JzllglDto jzllglDto_t=new JzllglDto();
        jzllglDto_t.setLldh(jzllglDto.getLldh());
        JzllglDto dto = jzllglService.getDto(jzllglDto_t);
        if(dto!=null){
            map.put("status", "fail");
            map.put("message", "领料单号已经存在！");
            return map;
        }
        boolean isSuccess = jzllglService.addSaveGermPicking(jzllglDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
        map.put("auditType",AuditTypeEnum.AUDIT_GERM_PICK.getCode());
        map.put("ywid", jzllglDto.getLlid());
        return map;
    }

    /**
     * 领料修改页面
     */
    @RequestMapping("/inventory/modGermPicking")
    public ModelAndView modGermPicking(JzllglDto jzllglDto) {
        ModelAndView mav = new ModelAndView("germ/stock/germPick_edit");
        JzllglDto jzllglDto_t = jzllglService.getDtoById(jzllglDto.getLlid());
        jzllglDto_t.setAuditType(AuditTypeEnum.AUDIT_GERM_PICK.getCode());
        jzllglDto_t.setFormAction("/germ/inventory/modSaveGermPicking");
        mav.addObject("url", "/germ/inventory/pagedataGermPicking");
        mav.addObject("jzllglDto", jzllglDto_t);
        return mav;
    }

    /**
     * 修改保存
     */
    @RequestMapping("/inventory/modSaveGermPicking")
    @ResponseBody
    public Map<String,Object> modSaveGermPicking(JzllglDto jzllglDto){
        Map<String,Object> map = new HashMap<>();
        User user=getLoginInfo();
        jzllglDto.setXgry(user.getYhid());
        JzllglDto jzllglDto_t=new JzllglDto();
        jzllglDto_t.setLldh(jzllglDto.getLldh());
        JzllglDto dto = jzllglService.getDto(jzllglDto_t);
        JzllglDto dtoById = jzllglService.getDtoById(jzllglDto.getLlid());
        if(!dtoById.getLldh().equals(jzllglDto.getLldh())){
            if(dto!=null){
                map.put("status", "fail");
                map.put("message", "领料单号已经存在！");
                return map;
            }
        }
        boolean isSuccess = jzllglService.modSaveGermPicking(jzllglDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
        map.put("auditType",AuditTypeEnum.AUDIT_GERM_PICK.getCode());
        map.put("ywid", jzllglDto.getLlid());
        return map;
    }


    /**
     * 领料出库页面
     */
    @RequestMapping("/inventory/deliveryGermPicking")
    public ModelAndView deliveryGermPicking(JzllglDto jzllglDto) {
        ModelAndView mav = new ModelAndView("germ/stock/germPick_delivery");
        JzllglDto jzllglDto_t = jzllglService.getDtoById(jzllglDto.getLlid());
        jzllglDto_t.setFormAction("/germ/inventory/deliverySaveGermPicking");
        mav.addObject("url", "/germ/inventory/pagedataGermPicking");
        mav.addObject("jzllglDto", jzllglDto_t);
        return mav;
    }


    /**
     * 出库保存
     */
    @RequestMapping("/inventory/deliverySaveGermPicking")
    @ResponseBody
    public Map<String,Object> deliverySaveGermPicking(JzllglDto jzllglDto){
        Map<String,Object> map = new HashMap<>();
        User user=getLoginInfo();
        jzllglDto.setXgry(user.getYhid());
        jzllglDto.setFlr(user.getYhid());
        boolean isSuccess = jzllglService.deliverySaveGermPicking(jzllglDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }


    /**
     * 删除保存
     */
    @RequestMapping("/inventory/delGermPicking")
    @ResponseBody
    public Map<String,Object> delGermPicking(JzllglDto jzllglDto){
        Map<String,Object> map = new HashMap<>();
        User user=getLoginInfo();
        jzllglDto.setScry(user.getYhid());
        boolean isSuccess = jzllglService.delGermPicking(jzllglDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }
    /**
     * 废弃保存
     */
    @RequestMapping("/inventory/discardGermPicking")
    @ResponseBody
    public Map<String,Object> discardGermPicking(JzllglDto jzllglDto){
        Map<String,Object> map = new HashMap<>();
        User user=getLoginInfo();
        jzllglDto.setScry(user.getYhid());
        boolean isSuccess = jzllglService.delGermPicking(jzllglDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }
}
