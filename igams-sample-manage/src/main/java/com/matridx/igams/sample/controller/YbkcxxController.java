package com.matridx.igams.sample.controller;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.sample.dao.entities.SbglDto;
import com.matridx.igams.sample.dao.entities.YbkcxxDto;
import com.matridx.igams.sample.dao.entities.YbllcDto;
import com.matridx.igams.sample.dao.entities.YbllglDto;
import com.matridx.igams.sample.service.svcinterface.ISbglService;
import com.matridx.igams.sample.service.svcinterface.IYbkcxxService;
import com.matridx.igams.sample.service.svcinterface.IYbllcService;
import com.matridx.igams.sample.service.svcinterface.IYbllglService;
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

/*
 *@date 2022年06月06日11:16
 *
 */
@RequestMapping("/sample")
@Controller
public class YbkcxxController extends BaseController {

    @Autowired
    IYbkcxxService ybkcxxService;

    @Autowired
    IXxglService xxglService;

    @Autowired
    IYbllcService ybllcService;

    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;

    @Autowired
    ICommonService commonService;

    @Autowired
    IYbllglService ybllglService;

    @Autowired
    ISbglService sbglService;

    @Autowired
    RedisUtil redisUtil;
    @Override
    public String getPrefix(){
        return urlPrefix;
    }

    /**
     * 跳转样本库存列表界面
     */
    @RequestMapping(value = "/inventory/pageListYbkcxx")
    public ModelAndView pageListYbkcxx(HttpServletRequest request,YbllcDto ybllcDto) {
        ModelAndView mav=new ModelAndView("sample/inventory/ybkcxx_list");
        User user = getLoginInfo(request);
        ybllcDto.setRyid(user.getYhid());
        // 获取样本领料车已加入的样本数量
        List<YbllcDto> ybllcDtos = ybllcService.getDtoList(ybllcDto);
        StringBuilder ids= new StringBuilder();
        if(!CollectionUtils.isEmpty(ybllcDtos)) {
            for (YbllcDto ybllcDto_t : ybllcDtos) {
                ids.append(",").append(ybllcDto_t.getYbkcid());
            }
            ids.substring(1);
        }
        List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode());
        List<JcsjDto> yblrlxs = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.SAMPLE_ENTRY_TYPE.getCode());
        mav.addObject("jcdwlist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));//存储单位
        int ybllzsl=ybllcDtos.size();
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("ybllzsl", Integer.toString(ybllzsl));
        mav.addObject("idsyb", ids.toString());
        mav.addObject("bblxlist", jcsjDtos);
        mav.addObject("yblrlxs", yblrlxs);
        return mav;
    }

    /**
     * 获取样本库存列表
     */
    @RequestMapping(value = "/inventory/pageGetListYbkcxx")
    @ResponseBody
    public Map<String,Object> pageGetListYbkcxx(YbkcxxDto ybkcxxDto,HttpServletRequest request){
        User user = getLoginInfo(request);
        List<Map<String, String>> dwAndbjlist = ybkcxxService.getJsjcdwByjsid(user.getDqjs());
        List<String> jcdwxz = new ArrayList<>();
        if(!CollectionUtils.isEmpty(dwAndbjlist)){
            if("1".equals(dwAndbjlist.get(0).get("dwxdbj"))) {
                for (Map<String, String> stringStringMap : dwAndbjlist) {
                    if (stringStringMap.get("jcdw") != null) {
                        jcdwxz.add(stringStringMap.get("jcdw"));
                    }
                }
            }
        }
        ybkcxxDto.setJcdwxz(jcdwxz);
        List<YbkcxxDto> ybkcxxlist = ybkcxxService.getPagedDtoList(ybkcxxDto);
        Map<String, Object> map= new HashMap<>();
        map.put("total",ybkcxxDto.getTotalNumber());
        map.put("rows",ybkcxxlist);
        return map;
    }
    /**
     * 查看样本库存列表
     */
    @RequestMapping(value = "/inventory/viewYbkcxxlist")
    public ModelAndView viewYbkcxxlist(YbkcxxDto ybkcxxDto) {
        ModelAndView mav=new ModelAndView("sample/inventory/ybkcxx_view");
        YbkcxxDto dtoById = null;
        if (ybkcxxDto!=null){
            dtoById = ybkcxxService.getDtoById(ybkcxxDto.getYbkcid());
        }
        mav.addObject("ybkcxxDto", dtoById);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 样本库存查看
     */
    @RequestMapping(value = "/inventory/stockviewYbkcxxlist")
    public ModelAndView stockviewYbkcxxlist() {
        ModelAndView mav=new ModelAndView("sample/inventory/ybkcxx_stockview");
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 获取样本库存列表
     */
    @RequestMapping(value = "/inventory/pagedataStockYbkcxx")
    @ResponseBody
    public Map<String,Object> pagedataStockYbkcxx(YbkcxxDto ybkcxxDto){
        List<YbkcxxDto> ybkcxxlist = ybkcxxService.getPagedDtoYds(ybkcxxDto);
        Map<String, Object> map= new HashMap<>();
        map.put("total",ybkcxxDto.getTotalNumber());
        map.put("rows",ybkcxxlist);
        return map;
    }
    /**
     * 跳转至样本录入界面
     */
    @RequestMapping(value = "/inventory/ybaddYbkcxx")
    public ModelAndView ybaddYbkcxx(YbkcxxDto ybkcxxDto) {
        ModelAndView mav=new ModelAndView("sample/inventory/ybkcxx_add");
        ybkcxxDto.setFormAction("ybaddSaveYbkcxx");
        List<JcsjDto> yblrlxlist = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.SAMPLE_ENTRY_TYPE.getCode());//样本录入类型
        mav.addObject("ybkcxxDto", ybkcxxDto);
        mav.addObject("yblrlxList", yblrlxlist);
        mav.addObject("yblrlxlist", JSONObject.toJSONString(yblrlxlist));//样本录入类型
        mav.addObject("jcdwlist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));//存储单位
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 样本录入保存
     */
    @RequestMapping(value = "/inventory/ybaddSaveYbkcxx")
    @ResponseBody
    public Map<String,Object> ybaddSaveYbkcxx(YbkcxxDto ybkcxxDto, HttpServletRequest request) {
        User user = getLoginInfo(request);
        Map<String,Object> map= new HashMap<>();
        try {
            boolean isSuccess = ybkcxxService.addsaveYbkcxx(ybkcxxDto,user);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
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
     * 修改样本库存信息
     */
    @RequestMapping(value = "/inventory/modYbkcxx")
    public ModelAndView modYbkcxx(YbkcxxDto ybkcxxDto) {
        ModelAndView mav=new ModelAndView("sample/inventory/ybkcxx_add");
        YbkcxxDto dtoById = ybkcxxService.getDtoById(ybkcxxDto.getYbkcid());
        dtoById.setFormAction("modSaveYbkcxx");
        SbglDto sbglDto_bx = new SbglDto();
        sbglDto_bx.setJcdws(new String[]{dtoById.getJcdw()});
        List<SbglDto> bxList = sbglService.getSbListByJcdw(sbglDto_bx);
        ybkcxxDto.setBxh(dtoById.getBxh());
        ybkcxxDto.setBxid(dtoById.getBxid());
        ybkcxxDto.setCth(dtoById.getCth());
        ybkcxxDto.setChtid(dtoById.getChtid());
        ybkcxxDto.setHh(dtoById.getHh());
        ybkcxxDto.setHzid(dtoById.getHzid());
        SbglDto sbglDto_ct = new SbglDto();
        sbglDto_ct.setFids(dtoById.getBxid().split(","));
        List<SbglDto> ctlList =sbglService.getSbListByFsbid(sbglDto_ct);
        SbglDto sbglDto_hz = new SbglDto();
        sbglDto_hz.setFids(dtoById.getChtid().split(","));
        List<SbglDto> hzList = sbglService.getSbListByFsbid(sbglDto_hz);
        List<YbkcxxDto> wzListByHzid = ybkcxxService.getWzListByHzid(dtoById.getHzid());
        StringBuilder wzList = new StringBuilder();
        if (!CollectionUtils.isEmpty(wzListByHzid)){
            for (YbkcxxDto dto : wzListByHzid) {
                wzList.append(dto.getWz()).append(",");
            }
            if (!wzList.toString().isEmpty()){
                wzList = new StringBuilder(wzList.substring(0, wzList.length() - 1));
            }
        }
        dtoById.setXgqhzid(dtoById.getHzid());
        dtoById.setXgqwz(dtoById.getWz());
        List<JcsjDto> yblrlxlist = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.SAMPLE_ENTRY_TYPE.getCode());//样本录入类型
        mav.addObject("bxList", bxList);
        mav.addObject("ctList", ctlList);
        mav.addObject("hzList", hzList);
        mav.addObject("wzList", wzList.toString());
        mav.addObject("yblrlxlist", JSONObject.toJSONString(yblrlxlist));//样本录入类型
        mav.addObject("jcdwlist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));//存储单位
        mav.addObject("ybkcxxDto", dtoById);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 样本库存修改保存
     */
    @RequestMapping(value = "/inventory/modSaveYbkcxx")
    @ResponseBody
    public Map<String,Object> modSaveYbkcxx(YbkcxxDto ybkcxxDto, HttpServletRequest request) {
        User user = getLoginInfo(request);
        ybkcxxDto.setXgry(user.getYhid());
        Map<String, Object> map = new HashMap<>();
        try {
            boolean isSuccess = ybkcxxService.modSaveYbkcxx(ybkcxxDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
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
     * 修改样本库存盒子信息
     */
    @RequestMapping(value = "/inventory/modboxYbkcxx")
    public ModelAndView modboxYbkcxx(YbkcxxDto ybkcxxDto) {
        ModelAndView mav=new ModelAndView("sample/inventory/ybkcxx_modbox");
        List<JcsjDto> jcdwlist = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());//样本录入类型
        mav.addObject("jcdwlist", jcdwlist);//存储单位
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 样本库存修改盒子保存
     */
    @RequestMapping(value = "/inventory/modboxSaveYbkcxx")
    @ResponseBody
    public Map<String,Object> modboxSaveYbkcxx(YbkcxxDto ybkcxxDto, HttpServletRequest request) {
        User user = getLoginInfo(request);
        ybkcxxDto.setXgry(user.getYhid());
        Map<String, Object> map = new HashMap<>();
        try {
            boolean isSuccess = ybkcxxService.modboxSaveYbkcxx(ybkcxxDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
        }
        return map;
    }
    //通过盒子id查询位置list
    @RequestMapping(value = "/inventory/pagedataWzlist")
    @ResponseBody
    public String[] pagedataWzlist(String hzid) {
        List<YbkcxxDto> wzListByHzid = ybkcxxService.getWzListByHzid(hzid);
        StringBuilder wzListStr = new StringBuilder();
        if (!CollectionUtils.isEmpty(wzListByHzid)){
            for (YbkcxxDto dto : wzListByHzid) {
                wzListStr.append(dto.getWz()).append(",");
            }
            if (!wzListStr.toString().isEmpty()){
                wzListStr = new StringBuilder(wzListStr.substring(0, wzListStr.length() - 1));
            }
            return wzListStr.toString().split(",");
        }
        return new String[]{""};
    }

    @RequestMapping(value = "/inventory/pagedataYbkcxx")
    @ResponseBody
    public YbkcxxDto pagedataYbkcxx(String nbbm,String yblrlx) {
        String[] sznbbm = nbbm.trim().split(" ");
        YbkcxxDto ybkcxxDto = ybkcxxService.getSjxxByNbbm(sznbbm[0]);
        List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.SAMPLE_ENTRY_TYPE.getCode());//样本录入类型
        String csid = "";
        for (JcsjDto jcsjDto : jcsjDtos) {
            //核酸的参数代码
            if ("1".equals(jcsjDto.getSfmr())){
                csid = jcsjDto.getCsid();
            }
        }
        if (StringUtil.isNotBlank(yblrlx)){
            csid = yblrlx;
        }
        if (ybkcxxDto!= null){
            ybkcxxDto.setTj("4");
            ybkcxxDto.setYblrlx(csid);
        }
        return ybkcxxDto;
    }
    @RequestMapping(value = "/inventory/pagedataYbkcxxlist")
    @ResponseBody
    public Map<String,Object> pagedataYbkcxxlist(YbkcxxDto ybkcxxDto) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isNotBlank(ybkcxxDto.getYbkcid())){
            YbkcxxDto dtoById = ybkcxxService.getDtoById(ybkcxxDto.getYbkcid());
            List<YbkcxxDto> rows = new ArrayList<>();
            rows.add(dtoById);
            map.put("total", ybkcxxDto.getTotalNumber());
            map.put("rows", rows);
        }else {
            map.put("total", 0);
            map.put("rows", null);
        }
        return map;
    }

    /**
     * 跳转领料车界面
     */
    @RequestMapping(value = "/inventory/pickingcarAddYb")
    @ResponseBody
    public ModelAndView pickingcarAddYb(YbllglDto ybllglDto,HttpServletRequest request) {
        ModelAndView mav=new ModelAndView("sample/inventory/ybkcxx_ybpickingCar");
        User user = getLoginInfo(request);
        ybllglDto.setLlr(user.getYhid());
        ybllglDto.setLlrmc(user.getZsxm());
        if ("addSaveYbll".equals(ybllglDto.getLjbj())){
            ybllglDto.setFormAction("addSaveYbll");
        }else {
            ybllglDto.setFormAction("pickingcarSaveAddYb");
        }
        //获取默认部门
        user = commonService.getUserInfoById(user);
        ybllglDto.setBm(user.getJgid());
        ybllglDto.setBmmc(user.getJgmc());
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
        Date date = new Date();
        ybllglDto.setSqrq(sdf.format(date));
        String lldh = ybllglService.generateLldh(ybllglDto);
        ybllglDto.setLldh(lldh);
        YbllcDto ybllcDto = new YbllcDto();
        ybllcDto.setRyid(user.getYhid());
        // 获取样本领料车已加入的样本数量
        List<YbllcDto> ybllcDtos = ybllcService.getDtoList(ybllcDto);
        StringBuilder ids= new StringBuilder();
        if(!CollectionUtils.isEmpty(ybllcDtos)) {
            for (YbllcDto ybllcDto_t : ybllcDtos) {
                ids.append(",").append(ybllcDto_t.getYbkcid());
            }
            ids.substring(1);
        }
        mav.addObject("idsyb", ids.toString());
        mav.addObject("ybllglDto", ybllglDto);
        mav.addObject("url", "/sample/inventory/");
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("CHECK_PASS", StatusEnum.CHECK_PASS.getCode());
        return mav;
    }
    /**
     * 将样本添加至样本领料车
     */
    @RequestMapping("/inventory/pagedataAddToPickingCar")
    @ResponseBody
    public Map<String, Object> pagedataAddToPickingCar(YbllcDto ybllcDto,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        ybllcDto.setRyid(user.getYhid());
        boolean isSuccess = ybllcService.insert(ybllcDto);
        // 获取样本领料车已加入的样本数量
        List<YbllcDto> llcglDtos = ybllcService.getDtoList(ybllcDto);
        StringBuilder ids= new StringBuilder();
        if(!CollectionUtils.isEmpty(llcglDtos)) {
            for (YbllcDto ybllcDto_t : llcglDtos) {
                ids.append(",").append(ybllcDto_t.getYbkcid());
            }
            ids.substring(1);
        }
        map.put("idsyb", ids.toString());
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }
    /**
     * 从样本领料车中删除
     */
    @RequestMapping("/inventory/pagedataDelFromPickingCar")
    @ResponseBody
    public Map<String, Object> pagedataDelFromPickingCar(YbllcDto ybllcDto,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        ybllcDto.setRyid(user.getYhid());
        boolean isSuccess = ybllcService.delete(ybllcDto);
        // 获取样本领料车已加入的样本数量
        List<YbllcDto> llcglDtos = ybllcService.getDtoList(ybllcDto);
        StringBuilder ids= new StringBuilder();
        if(!CollectionUtils.isEmpty(llcglDtos)) {
            for (YbllcDto ybllcDto_t : llcglDtos) {
                ids.append(",").append(ybllcDto_t.getYbkcid());
            }
            ids.substring(1);
        }
        map.put("idsyb", ids.toString());
        map.put("status", isSuccess ? "success":"fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00003").getXxnr()
                : xxglService.getModelById("ICOM00004").getXxnr());
        return map;
    }
    /**
     * 获取领料车数据
     */
    @RequestMapping("/inventory/pagedataYbllcList")
    @ResponseBody
    public Map<String, Object> pagedataYbllcList(YbllcDto ybllcDto,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        ybllcDto.setRyid(user.getYhid());
        List<YbllcDto> ybllcDtos = ybllcService.getYbLlcDtoList(ybllcDto);
        map.put("total", ybllcDto.getTotalNumber());
        map.put("rows", ybllcDtos);
        return map;
    }
    /**
     * 通过样本库存id获取样本明细
     */
    @RequestMapping("/inventory/pagedataYbmxByYbkcid")
    @ResponseBody
    public YbkcxxDto pagedataYbmxByYbkcid(YbkcxxDto ybkcxxDto) {
        ybkcxxDto = ybkcxxService.queryYbmxByYbkcid(ybkcxxDto);
        return ybkcxxDto;
    }
    /**
     * 内部编码模糊查询库存
     */
    @RequestMapping("/inventory/pagedataYbkcxxByNbbm")
    @ResponseBody
    public List<YbkcxxDto> pagedataYbkcxxByNbbm(String nbbm) {
        return ybkcxxService.getYbkcxxByNbbm(nbbm);
    }

    /**
     * 样本领料车保存
     */
    @RequestMapping(value = "/inventory/pickingcarSaveAddYb")
    @ResponseBody
    public  Map<String,Object> pickingcarSaveYb(YbllglDto ybllglDto,HttpServletRequest request) {
        Map<String,Object> map= new HashMap<>();
        // 校验领料单号是否重复
        boolean result = ybllglService.getDtoByLldh(ybllglDto);
        if (!result) {
            map.put("status", "fail");
            map.put("message", "领料单号不允许重复！");
            return map;
        }
        User user = getLoginInfo(request);
        try {
            boolean isSuccess = ybkcxxService.addSaveYbPickingCar(ybllglDto,user);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
            map.put("ywid",ybllglDto.getLlid());
            map.put("auditType", AuditTypeEnum.AUDIT_SAMPLE.getCode());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
        }
        map.put("urlPrefix",urlPrefix);
        return map;
    }
    /**
     * 跳转领料新增界面
     */
    @RequestMapping(value = "/inventory/addYbll")
    @ResponseBody
    public ModelAndView addYbll(YbllglDto ybllglDto,HttpServletRequest request) {
        ybllglDto.setLjbj("addSaveYbll");
        return this.pickingcarAddYb(ybllglDto,request);
    }
    /**
     * 样本领料车新增保存
     */
    @RequestMapping(value = "/inventory/addSaveYbll")
    @ResponseBody
    public  Map<String,Object> addSaveYbll(YbllglDto ybllglDto,HttpServletRequest request) {
        return this.pickingcarSaveYb(ybllglDto,request);
    }
}
