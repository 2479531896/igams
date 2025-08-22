package com.matridx.igams.sample.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.DataPermissionTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.sample.dao.entities.SbglDto;
import com.matridx.igams.sample.dao.entities.YbdbDto;
import com.matridx.igams.sample.dao.entities.YbdbmxDto;
import com.matridx.igams.sample.dao.entities.YbkcxxDto;
import com.matridx.igams.sample.service.svcinterface.ISbglService;
import com.matridx.igams.sample.service.svcinterface.IYbdbService;
import com.matridx.igams.sample.service.svcinterface.IYbdbmxService;
import com.matridx.igams.sample.service.svcinterface.IYbkcxxService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 样本调拨
 */
@Controller
@RequestMapping("/sampleAllot")
public class SampleAllotController extends BaseController {

    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Autowired
    private IYbdbService ybdbService;
    @Autowired
    private ISbglService sbglService;
    @Autowired
    private IYbdbmxService ybdbmxService;
    @Autowired
    private IYbkcxxService ybkcxxService;
    @Autowired
    IShgcService shgcService;
    @Autowired
    IXxglService xxglService;
    @Autowired
    RedisUtil redisUtil;

    private final Logger log = LoggerFactory.getLogger(SampleAllotController.class);
    /**
     * 样本调拨列表 mav
     * @return
     */
    @RequestMapping("/pageListSampleAllot")
    public ModelAndView getSampleAllotPageList() {
        ModelAndView mav = new  ModelAndView("sample/allot/sampleAllot_list");
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("auditType",AuditTypeEnum.AUTID_SAMPLE_ALLOT.getCode());
        mav.addObject("dwlist",redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));
        return mav;
    }

    /**
     * 样本调拨列表 map
     * @param ybdbDto
     * @return
     */
    @RequestMapping("/pageGetListSampleAllot")
    @ResponseBody
    public Map<String, Object> getPageListPutInAllocate(YbdbDto ybdbDto){
        Map<String, Object> map = new HashMap<>();
        List<YbdbDto> ybdbDtos = ybdbService.getPagedDtoList(ybdbDto);
        try{
            shgcService.addShgcxxByYwid(ybdbDtos, AuditTypeEnum.AUTID_SAMPLE_ALLOT.getCode(), "zt", "ybdbid", new String[]{StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
        } catch (BusinessException e){
            // TODO Auto-generated catch block
            log.error("/pageGetListSampleAllot"+e.getMessage());
            log.error(e.toString());
        }
        map.put("rows",ybdbDtos);
        map.put("total", ybdbDto.getTotalNumber());
        return map;
    }

    /**
     * 根据检测单位获取冰箱List
     * @param ybdbDto
     * @return
     */
    @RequestMapping("/pagedataGetBxList")
    @ResponseBody
    public Map<String, Object> pagedataGetBxList(YbdbDto ybdbDto){
        Map<String, Object> map = new HashMap<>();
        SbglDto sbglDto = new SbglDto();
        //将ids转换成数组
        sbglDto.setJcdws(ybdbDto.getFcsids());
        List<SbglDto> sbglDtos = sbglService.getSbListByJcdw(sbglDto);
        List<Map<String,String>> list = new ArrayList<>();
        for (SbglDto dto : sbglDtos) {
            Map<String,String> dtomap = new HashMap<>();
            dtomap.put("csid",dto.getSbid());
            dtomap.put("wz",dto.getWz());
            dtomap.put("sbh",dto.getSbh());
            list.add(dtomap);
        }
        map.put("list",list);
        return map;
    }

    /**
     * 根据冰箱获取抽屉List
     * @param ybdbDto
     * @return
     */
    @RequestMapping("/pagedataGetSbList")
    @ResponseBody
    public Map<String, Object> pagedataGetSbList(YbdbDto ybdbDto){
        Map<String, Object> map = new HashMap<>();
        SbglDto sbglDto = new SbglDto();
        //将ids转换成数组
        sbglDto.setFids(ybdbDto.getFcsids());
        sbglDto.setYbdbid(ybdbDto.getYbdbid());
        List<SbglDto> sbglDtos = sbglService.getSbListByFsbid(sbglDto);
        List<Map<String,String>> list = new ArrayList<>();
        for (SbglDto dto : sbglDtos) {
            Map<String,String> dtomap = new HashMap<>();
            dtomap.put("csid",dto.getSbid());
            dtomap.put("wz",dto.getWz());
            dtomap.put("sbh",dto.getSbh());
            list.add(dtomap);
        }
        map.put("list",list);
        return map;
    }

    /**
     * 样本调拨查看 mav
     * @return
     */
    @RequestMapping("/viewSampleAllot")
    public ModelAndView viewSampleAllot(YbdbDto ybdbDto) {
        ModelAndView mav = new  ModelAndView("sample/allot/sampleAllot_view");
        //设置分布式前缀
        mav.addObject("urlPrefix", urlPrefix);
        //调拨信息
        ybdbDto = ybdbService.getDto(ybdbDto);
        mav.addObject("ybdbDto",ybdbDto);
        YbdbmxDto ybdbmxDto = new YbdbmxDto();
        ybdbmxDto.setYbdbid(ybdbDto.getYbdbid());
        List<YbdbmxDto> ybdbmxDtos = ybdbmxService.getDbmxDtos(ybdbmxDto);
        mav.addObject("ybdbmxDtos",ybdbmxDtos);
        return mav;
    }

    /**
     * 样本调拨 mav
     * @return
     */
    @RequestMapping("/allocationSampleAllot")
    public ModelAndView allocationSampleAllot(HttpServletRequest request) {
        ModelAndView mav = new  ModelAndView("sample/allot/sampleAllot_allocation");
        //设置分布式前缀
        mav.addObject("urlPrefix", urlPrefix);
        YbdbDto ybdbDto = new YbdbDto();
        //设置默认调拨日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
        Date date = new Date();
        ybdbDto.setDbrq(sdf.format(date));
        //设置默认调拨人
        User user = getLoginInfo(request);
        ybdbDto.setDbr(user.getYhid());
        ybdbDto.setDbrmc(user.getZsxm());
        mav.addObject("ybdbDto",ybdbDto);
        //设置调出、调入储存单位List
        mav.addObject("dwlist",redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));
        //审核类型
        mav.addObject("auditType",AuditTypeEnum.AUTID_SAMPLE_ALLOT.getCode());
        //调拨保存接口
        mav.addObject("formAction","allocation");
        return mav;
    }

    /**
     * 样本调拨调拨保存
     * @param ybdbDto
     * @param request
     * @return
     */
    @RequestMapping("/allocationSaveSampleAllot")
    @ResponseBody
    public Map<String,Object> allocationSaveSampleAllot(YbdbDto ybdbDto, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        ybdbDto.setLrry(user.getYhid());
        boolean isSuccess = false;
        try {
            String hzInfoJson = request.getParameter("hzInfo_json");
            if (StringUtil.isNotBlank(hzInfoJson)){
                List<YbdbmxDto> ybdbmxDtos = JSONArray.parseArray(hzInfoJson, YbdbmxDto.class);
                ybdbDto.setYbdbmxDtos(ybdbmxDtos);
                isSuccess = ybdbService.saveSampleAllot(ybdbDto);
            }
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
            map.put("auditType",AuditTypeEnum.AUTID_SAMPLE_ALLOT.getCode());
            map.put("ywid",ybdbDto.getYbdbid());
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMessage())?e.getMessage():xxglService.getModelById("ICOM00002").getXxnr()+e.getMessage());
        }
        return map;
    }

    /**
     * 获取样本调拨明细
     * @return
     */
    @RequestMapping("/pagedataSampleAllotInfo")
    @ResponseBody
    public Map<String,Object> pagedataSampleAllotInfo(HttpServletRequest request,YbdbmxDto ybdbmxDto) {
        Map<String, Object> map= new HashMap<>();
        List<YbdbmxDto> ybdbmxDtos = new ArrayList<>();
        if (StringUtil.isNotBlank(ybdbmxDto.getYbdbid())){
            ybdbmxDtos = ybdbmxService.getDbmxDtos(ybdbmxDto);
            map.put("rows",ybdbmxDtos);
        } else {
            map.put("rows",ybdbmxDtos);
        }
        return map;
    }

    /**
     * 样本调拨修改 mav
     * @return
     */
    @RequestMapping("/modSampleAllot")
    public ModelAndView modSampleAllot(HttpServletRequest request,YbdbDto ybdbDto) {
        ModelAndView mav = new  ModelAndView("sample/allot/sampleAllot_allocation");
        //设置分布式前缀
        mav.addObject("urlPrefix", urlPrefix);
        //调拨信息
        ybdbDto = ybdbService.getDto(ybdbDto);
        mav.addObject("ybdbDto",ybdbDto);
        //冰箱List
        SbglDto bx_sbglDto = new SbglDto();
        String[] jcdws = {ybdbDto.getDcccdw()};
        bx_sbglDto.setJcdws(jcdws);
        List<SbglDto> bx_sbglDtos = sbglService.getSbListByJcdw(bx_sbglDto);
        mav.addObject("bxlist",bx_sbglDtos);
        //抽屉List
        SbglDto ct_sbglDto = new SbglDto();
        String[] ct_fsbdis = {ybdbDto.getDcbx()};
        ct_sbglDto.setFids(ct_fsbdis);
        List<SbglDto> ct_sbglDtos = sbglService.getSbListByFsbid(ct_sbglDto);
        mav.addObject("ctlist", ct_sbglDtos);
        //盒子List
        SbglDto hz_sbglDto = new SbglDto();
        String[] hz_fsbdis = {ybdbDto.getDcct()};
        hz_sbglDto.setFids(hz_fsbdis);
        hz_sbglDto.setYbdbid(ybdbDto.getYbdbid());
        List<SbglDto> hz_sbglDtos = sbglService.getSbListByFsbid(hz_sbglDto);
        List<Map<String,String>> list = new ArrayList<>();
        for (SbglDto dto : hz_sbglDtos) {
            Map<String,String> dtomap = new HashMap<>();
            dtomap.put("csid",dto.getSbid());
            dtomap.put("wz",dto.getWz());
            dtomap.put("sbh",dto.getSbh());
            list.add(dtomap);
        }
        mav.addObject("hzlist", JSON.toJSONString(list));
        //设置调出、调入储存单位List
        mav.addObject("dwlist",redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));
        //审核类型
        mav.addObject("auditType",AuditTypeEnum.AUTID_SAMPLE_ALLOT.getCode());
        //调拨保存接口
        mav.addObject("formAction","mod");
        return mav;
    }

    /**
     * 样本调拨调拨保存
     * @param ybdbDto
     * @param request
     * @return
     */
    @RequestMapping("/modSaveSampleAllot")
    @ResponseBody
    public Map<String,Object> modSaveSampleAllot(YbdbDto ybdbDto, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        ybdbDto.setXgry(user.getYhid());
        boolean isSuccess = false;
        try {
            String hzInfoJson = request.getParameter("hzInfo_json");
            if (StringUtil.isNotBlank(hzInfoJson)){
                List<YbdbmxDto> ybdbmxDtos = JSONArray.parseArray(hzInfoJson, YbdbmxDto.class);
                ybdbDto.setYbdbmxDtos(ybdbmxDtos);
                isSuccess = ybdbService.saveSampleAllot(ybdbDto);
            }
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
            map.put("auditType",AuditTypeEnum.AUTID_SAMPLE_ALLOT.getCode());
            map.put("ywid",ybdbDto.getYbdbid());
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMessage())?e.getMessage():xxglService.getModelById("ICOM00002").getXxnr());
        }
        return map;
    }

    /**
     * 样本调拨调拨删除
     * @param ybdbDto
     * @param request
     * @return
     */
    @RequestMapping("/delSampleAllot")
    @ResponseBody
    public Map<String,Object> delSampleAllot(YbdbDto ybdbDto, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        ybdbDto.setScry(user.getYhid());
        boolean isSuccess = false;
        try {
            isSuccess = ybdbService.delSampleAllot(ybdbDto);
            map.put("status", isSuccess?"success":"fail");
            map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
        } catch (Exception e) {
            map.put("status", isSuccess?"success":"fail");
            map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr()+e.getMessage());
        }
        return map;
    }
    /**
     * 检查盒子是否可以调拨
     * @param ybdbmxDto
     * @param request
     * @return
     */
    @RequestMapping("/pagedataCheckHzCanAllot")
    @ResponseBody
    public Map<String,Object> checkHzCanAllot(YbdbmxDto ybdbmxDto, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        boolean isSuccess = true;
        try {
            //1、判断当前盒子是否在当前调拨单中
            List<String> hzs = ybdbmxDto.getHzs();
            if (StringUtil.isNotBlank(ybdbmxDto.getYbdbid())){
                List<YbdbmxDto> dbmxDtos = ybdbmxService.getDbmxDtos(ybdbmxDto);
                for (YbdbmxDto dbmxDto : dbmxDtos) {
                    if (!CollectionUtils.isEmpty(hzs)){
                        for (String hz : hzs) {
                            if (dbmxDto.getDchz().equals(hz)){
                                hzs.remove(hz);
                                break;
                            }
                        }
                    }else {
                        break;
                    }
                }
            }
            if (!CollectionUtils.isEmpty(hzs)){
                //2、若不在当前调拨单中，则判断盒子中的样本是否为空且是否ydbj是否均为0
                YbkcxxDto ybkcxxDto = new YbkcxxDto();
                ybkcxxDto.setHzids(hzs);
                Map<String, Object> result = ybkcxxService.checkHzCanAllot(ybkcxxDto);
                String zyds = result.get("zyds").toString();
                String zybs = result.get("zybs").toString();
                if (zyds.equals("0") && !zybs.equals("0")){
                    isSuccess = true;
                }else {
                    isSuccess = false;
                }
            }
            map.put("status", isSuccess?"success":"fail");
            map.put("message", isSuccess?"":"当前盒子不可被调拨!");
        } catch (Exception e) {
            isSuccess = false;
            map.put("status", isSuccess?"success":"fail");
            map.put("message", isSuccess?"":"当前盒子不可被调拨!");
        }
        return map;
    }


    /**
     * 样本调拨 详情查看
     * @return
     */
    @RequestMapping("/pagedataSampleAllotListView")
    public ModelAndView pagedataSampleAllotListView(HttpServletRequest request,YbdbmxDto ybdbmxDto) {
        ModelAndView mav = new  ModelAndView("sample/allot/sampleAllot_listview");
        YbkcxxDto ybkcxxDto = new YbkcxxDto();
        ybkcxxDto.setHzid(ybdbmxDto.getDchz());
        List<YbkcxxDto> list = ybkcxxService.getSampleInfoByHzid(ybkcxxDto);
        mav.addObject("list",list);
        //设置分布式前缀
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 跳转样本领料审核列表
     */
    @RequestMapping(value = "/pageListAuditSampleAllot")
    public ModelAndView pageListAuditSampleAllot() {
        ModelAndView mav=new ModelAndView("sample/allot/sampleAllot_auditList");
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 获取样本领料审核列表
     */
    @RequestMapping(value = "/pageGetListAuditSampleAllot")
    @ResponseBody
    public Map<String,Object> pageGetListAuditSampleAllot(YbdbDto ybdbDto, HttpServletRequest request){
        User user = getLoginInfo(request);
        // 附加委托参数
        DataPermission.addWtParam(ybdbDto);
        // 附加审核状态过滤
        if (GlobalString.AUDIT_SHZT_YSH.equals(ybdbDto.getDqshzt())) {
            DataPermission.add(ybdbDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "ybdb", "ybdbid",
                    AuditTypeEnum.AUTID_SAMPLE_ALLOT);
        } else {
            DataPermission.add(ybdbDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "ybdb", "ybdbid",
                    AuditTypeEnum.AUTID_SAMPLE_ALLOT);
        }
        DataPermission.addCurrentUser(ybdbDto,user);
        List<Map<String, String>> jcdwList = ybkcxxService.getJsjcdwByjsid(user.getDqjs());
        if(!CollectionUtils.isEmpty(jcdwList)) {
            if("1".equals(jcdwList.get(0).get("dwxdbj"))) {
                List<String> jcdws= new ArrayList<>();
                for (Map<String, String> map : jcdwList) {
                    if (map.get("jcdw") != null) {
                        jcdws.add(map.get("jcdw"));
                    }
                }
                if(!CollectionUtils.isEmpty(jcdws)) {
                    ybdbDto.setJcdwxz(jcdws);
                }
            }
        }
        List<YbdbDto> ybdbDtos = ybdbService.getPagedAuditSampleAllot(ybdbDto);
        Map<String, Object> map= new HashMap<>();
        map.put("total", ybdbDto.getTotalNumber());
        map.put("rows", ybdbDtos);
        return map;
    }
    /**
     * 样本调拨特殊审核 mav
     * @return
     */
    @RequestMapping("/auditSampleAllot")
    public ModelAndView auditSampleAllot(HttpServletRequest request,YbdbDto ybdbDto) {
        ModelAndView mav = modSampleAllot(request, ybdbDto);
        mav.addObject("formAction","audit");
        return mav;
    }
    /**
     * 样本调拨特殊审核 mav
     * @return
     */
    @RequestMapping("/commonAuditSampleAllot")
    public ModelAndView commonAuditSampleAllot(HttpServletRequest request,YbdbDto ybdbDto) {
        ModelAndView mav = modSampleAllot(request, ybdbDto);
        ybdbDto = (YbdbDto) mav.getModel().get("ybdbDto");
        SbglDto sbglDto_bx = new SbglDto();
        sbglDto_bx.setJcdws(new String[]{ybdbDto.getDrccdw()});
        List<SbglDto> drbxList = sbglService.getSbListByJcdw(sbglDto_bx);
        List<SbglDto> drctList = new ArrayList<>();
        List<SbglDto> drhzList = new ArrayList<>();
        if (StringUtil.isNotBlank(ybdbDto.getDrbx())){
            SbglDto sbglDto_ct = new SbglDto();
            sbglDto_ct.setFids(new String[]{ybdbDto.getDrbx()});
            drctList = sbglService.getSbListByFsbid(sbglDto_ct);
        }
        if (StringUtil.isNotBlank(ybdbDto.getDrct())){
            SbglDto sbglDto_hz = new SbglDto();
            sbglDto_hz.setFids(new String[]{ybdbDto.getDrct()});
            drhzList = sbglService.getSbListByFsbid(sbglDto_hz);
        }
        mav.addObject("drhzList",JSON.toJSONString(drhzList));
        mav.addObject("drctList",drctList);
        mav.addObject("drbxList",drbxList);
        mav.addObject("formAction","commonAudit");
        return mav;
    }
}
