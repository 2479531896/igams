package com.matridx.igams.storehouse.controller;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.cloud.controller.BaseBasicController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.*;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.storehouse.dao.entities.*;
import com.matridx.igams.storehouse.service.svcinterface.*;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("/repaid")
public class RepaidController extends BaseBasicController {

    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Autowired
    IJcghglService jcghglService;
    @Autowired
    IJcghmxService jcghmxService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    private IJcsjService jcsjService;
    @Autowired
    private IXxglService xxglService;
    @Autowired
    private ICkxxService ckxxService;
    @Autowired
    private IHwxxService hwxxService;
    @Override
    public String getPrefix(){
        return urlPrefix;
    }

    /**
     * 借出借用列表页面
     *
     * @return
     */
    @RequestMapping(value = "/repaid/pageListRepaid")
    public ModelAndView pageListRepaid() {
        ModelAndView mav = new ModelAndView("storehouse/repaid/repaid_list");
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("auditType", AuditTypeEnum.AUDIT_REPAID.getCode());
        return mav;
    }

    /**
     * 借出归还列表
     *
     * @return
     */
    @RequestMapping(value = "/repaid/pageGetListRepaid")
    @ResponseBody
    public Map<String, Object> pageGetListRepaid(JcghglDto jcghglDto) {
        Map<String, Object> map = new HashMap<>();
        List<JcghglDto> list = jcghglService.getPagedDtoList(jcghglDto);
        if(!CollectionUtils.isEmpty(list)){
            for(JcghglDto dto:list){
                if("GYS".equals(dto.getDwlxdm())){
                    dto.setDwid(dto.getGysid());
                    dto.setDwmc(dto.getGysmc());
                    dto.setDwdm(dto.getGysdm());
                }else if("KH".equals(dto.getDwlxdm())){
                    dto.setDwid(dto.getKhid());
                    dto.setDwmc(dto.getKhmc());
                    dto.setDwdm(dto.getKhdm());
                }else if("BM".equals(dto.getDwlxdm())){
                    dto.setDwid(dto.getJgid());
                    dto.setDwmc(dto.getJgmc());
                    dto.setDwdm(dto.getJgdm());
                }
            }
        }
        map.put("total", jcghglDto.getTotalNumber());
        map.put("rows", list);
        return map;
    }
    /**
     * 借出归还钉钉列表
     *
     * @return
     */
    @RequestMapping(value = "/repaid/minidataGetListRepaid")
    @ResponseBody
    public Map<String, Object> minidataGetListRepaid(JcghglDto jcghglDto) {
        Map<String, Object> map = new HashMap<>();
        List<JcghglDto> list = jcghglService.getPagedDtoList(jcghglDto);
        if(!CollectionUtils.isEmpty(list)){
            for(JcghglDto dto:list){
                if("GYS".equals(dto.getDwlxdm())){
                    dto.setDwid(dto.getGysid());
                    dto.setDwmc(dto.getGysmc());
                    dto.setDwdm(dto.getGysdm());
                }else if("KH".equals(dto.getDwlxdm())){
                    dto.setDwid(dto.getKhid());
                    dto.setDwmc(dto.getKhmc());
                    dto.setDwdm(dto.getKhdm());
                }else if("BM".equals(dto.getDwlxdm())){
                    dto.setDwid(dto.getJgid());
                    dto.setDwmc(dto.getJgmc());
                    dto.setDwdm(dto.getJgdm());
                }
            }
        }
        map.put("total", jcghglDto.getTotalNumber());
        map.put("rows", list);
        return map;
    }
    /**
     * 钉钉归还查看页面
     *
     * @return
     */
    @RequestMapping(value = "/repaid/minidataViewRepaid")
    @ResponseBody
    public Map<String, Object> minidatatViewRepaid(JcghglDto jcghglDto) {
        Map<String, Object> map = new HashMap<>();
        JcghglDto dto = jcghglService.getDto(jcghglDto);
        if(dto!=null){
            if("GYS".equals(dto.getDwlxdm())){
                dto.setDwid(dto.getGysid());
                dto.setDwmc(dto.getGysmc());
                dto.setDwdm(dto.getGysdm());
            }else if("KH".equals(dto.getDwlxdm())){
                dto.setDwid(dto.getKhid());
                dto.setDwmc(dto.getKhmc());
                dto.setDwdm(dto.getKhdm());
            }else if("BM".equals(dto.getDwlxdm())){
                dto.setDwid(dto.getJgid());
                dto.setDwmc(dto.getJgmc());
                dto.setDwdm(dto.getJgdm());
            }
        }
        JcghmxDto jcghmxDto=new JcghmxDto();
        jcghmxDto.setJcghid(jcghglDto.getJcghid());
        List<JcghmxDto> dtoList = jcghmxService.getDtoList(jcghmxDto);
        map.put("urlPrefix", urlPrefix);
        map.put("jcghglDto",dto);
        map.put("list",dtoList);
        return map;
    }
    /**
     * 归还页面
     *
     * @return
     */
    @RequestMapping(value = "/repaid/repaid")
    public ModelAndView borrowing() {
        ModelAndView mav = new ModelAndView("storehouse/repaid/repaid_edit");
//        dhxxDto.setAuditType(AuditTypeEnum.AUDIT_GOODS_ARRIVAL.getCode());
//        mav.addObject("dhxxDto", dhxxDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 归还查看页面
     *
     * @return
     */
    @RequestMapping(value = "/repaid/viewRepaid")
    public ModelAndView viewRepaid(JcghglDto jcghglDto) {
        ModelAndView mav = new ModelAndView("storehouse/repaid/repaid_view");
        JcghglDto dto = jcghglService.getDto(jcghglDto);
        if(dto!=null){
            if("GYS".equals(dto.getDwlxdm())){
                dto.setDwid(dto.getGysid());
                dto.setDwmc(dto.getGysmc());
                dto.setDwdm(dto.getGysdm());
            }else if("KH".equals(dto.getDwlxdm())){
                dto.setDwid(dto.getKhid());
                dto.setDwmc(dto.getKhmc());
                dto.setDwdm(dto.getKhdm());
            }else if("BM".equals(dto.getDwlxdm())){
                dto.setDwid(dto.getJgid());
                dto.setDwmc(dto.getJgmc());
                dto.setDwdm(dto.getJgdm());
            }
        }
        JcghmxDto jcghmxDto=new JcghmxDto();
        jcghmxDto.setJcghid(jcghglDto.getJcghid());
        List<JcghmxDto> dtoList = jcghmxService.getDtoList(jcghmxDto);
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("jcghglDto",dto);
        mav.addObject("list",dtoList);
        return mav;
    }

    /**
     * 借出借用审核列表页面
     *
     * @return
     */
    @RequestMapping(value = "/repaid/pageListAudit")
    public ModelAndView pageListRepaidAudit() {
        ModelAndView mav = new ModelAndView("storehouse/repaid/repaid_listaudit");
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 获取领料申请审核列表
     *
     * @param
     * @return
     */
    @RequestMapping("/repaid/pageGetListAudit")
    @ResponseBody
    public Map<String, Object> pageGetListRepaidAudit(JcghglDto jcghglDto, HttpServletRequest request) {
        //附加委托参数
        DataPermission.addWtParam(jcghglDto);
        //附加审核状态过滤
        if(GlobalString.AUDIT_SHZT_YSH.equals(jcghglDto.getDqshzt())){
            DataPermission.add(jcghglDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "jcghgl", "jcghid", AuditTypeEnum.AUDIT_REPAID);
        }else{
            DataPermission.add(jcghglDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "jcghgl", "jcghid", AuditTypeEnum.AUDIT_REPAID);
        }
        DataPermission.addCurrentUser(jcghglDto, getLoginInfo(request));
        DataPermission.addSpDdw(jcghglDto, "jcghgl", SsdwTableEnum.JCGHGL);
        List<JcghglDto> jcjyglDtos = jcghglService.getPagedAuditRepaid(jcghglDto);
        if(!CollectionUtils.isEmpty(jcjyglDtos)) {
            for(JcghglDto dto:jcjyglDtos){
                if("GYS".equals(dto.getDwlxdm())){
                    dto.setDwid(dto.getGysid());
                    dto.setDwmc(dto.getGysmc());
                    dto.setDwdm(dto.getGysdm());
                }else if("KH".equals(dto.getDwlxdm())){
                    dto.setDwid(dto.getKhid());
                    dto.setDwmc(dto.getKhmc());
                    dto.setDwdm(dto.getKhdm());
                }else if("BM".equals(dto.getDwlxdm())){
                    dto.setDwid(dto.getJgid());
                    dto.setDwmc(dto.getJgmc());
                    dto.setDwdm(dto.getJgdm());
                }
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("total", jcghglDto.getTotalNumber());
        map.put("rows", jcjyglDtos);
        return map;
    }




    /**
     * 刷新归还单号
     *
     * @param
     * @return
     */
    @RequestMapping("/repaid/pagedataRefreshGhdh")
    @ResponseBody
    public Map<String, Object> pagedataRefreshGhdh(JcghglDto jcghglDto) {
        Map<String, Object> map = new HashMap<>();
        map.put("ghdh",  jcghglService.generateGhdh(jcghglDto));
        return map;
    }

    /**
     * 废弃
     * @param
     * @param
     * @return
     */
    @RequestMapping("/repaid/discardRepaid")
    @ResponseBody
    public Map<String, Object> discardRepaid(JcghglDto jcghglDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        jcghglDto.setScry(user.getYhid());
        boolean isSuccess = jcghglService.discard(jcghglDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?"废弃成功！":"废弃失败！");
        return map;
    }

    /**
     * 删除
     * @param
     * @param
     * @return
     */
    @RequestMapping("/repaid/delRepaid")
    @ResponseBody
    public Map<String, Object> delRepaid(JcghglDto jcghglDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        jcghglDto.setScry(user.getYhid());
        boolean isSuccess = jcghglService.deleteDto(jcghglDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
        return map;
    }

    /**
     * 提交页面
     * @param
     * @return
     */
    @RequestMapping("/repaid/resubmitRepaid")
    public ModelAndView submitRrepaid(JcghglDto jcghglDto) {
        JcghglDto jcghglDto_t = jcghglService.getDtoById(jcghglDto.getJcghid());
        if("GYS".equals(jcghglDto_t.getDwlxdm())){
            jcghglDto_t.setDwid(jcghglDto_t.getGysid());
            jcghglDto_t.setDwmc(jcghglDto_t.getGysmc());
            jcghglDto_t.setDwdm(jcghglDto_t.getGysdm());
        }else if("KH".equals(jcghglDto_t.getDwlxdm())){
            jcghglDto_t.setDwid(jcghglDto_t.getKhid());
            jcghglDto_t.setDwmc(jcghglDto_t.getKhmc());
            jcghglDto_t.setDwdm(jcghglDto_t.getKhdm());
        }else if("BM".equals(jcghglDto_t.getDwlxdm())){
            jcghglDto_t.setDwid(jcghglDto_t.getJgid());
            jcghglDto_t.setDwmc(jcghglDto_t.getJgmc());
            jcghglDto_t.setDwdm(jcghglDto_t.getJgdm());
        }
        ModelAndView mav = new ModelAndView("storehouse/repaid/repaid_edit");
        jcghglDto_t.setFormAction("/repaid/repaid/resubmitSaveRepaid");
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
        Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
                new BasicDataTypeEnum[] { BasicDataTypeEnum.PERUNIT_TYPE});
        mav.addObject("dwlblist", jclist.get(BasicDataTypeEnum.PERUNIT_TYPE.getCode()));
        mav.addObject("auditType", AuditTypeEnum.AUDIT_REPAID.getCode());
        mav.addObject("url", "/repaid/repaid/pagedataResubmitRepaid");
        mav.addObject("urlPrefix", urlPrefix);
        jcghglDto_t.setXsbj("2");
        mav.addObject("jcghglDto", jcghglDto_t);
        return mav;
    }



    /**
     * 归还修改保存
     * @param
     * @return
     */
    @RequestMapping("/repaid/resubmitSaveRepaid")
    @ResponseBody
    public Map<String,Object> resubmitSaveRepaid(HttpServletRequest request,JcghglDto jcghglDto){
        Map<String,Object> map = new HashMap<>();
        User user=getLoginInfo(request);
        jcghglDto.setXgry(user.getYhid());
        boolean isSuccess;
        //修改保存
        try {
            isSuccess = jcghglService.modSaveRepaid(jcghglDto);
            map.put("ywid",jcghglDto.getJcghid());
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
                    : xxglService.getModelById("ICOM00002").getXxnr());
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
     * 重新提交获取明细
     * @param
     * @return
     */
    @RequestMapping("/repaid/pagedataResubmitRepaid")
    @ResponseBody
    public Map<String, Object> pagedataResubmitRepaid(JcghglDto jcghglDto) {
        Map<String, Object> map = new HashMap<>();
        JcghmxDto jcghmxDto = new JcghmxDto();
        jcghmxDto.setJcghid(jcghglDto.getJcghid());
        List<JcghmxDto> jcghmxDtos = jcghmxService.getDtoList(jcghmxDto);
        if(!CollectionUtils.isEmpty(jcghmxDtos)) {
            for (JcghmxDto jcghmxDto_t : jcghmxDtos) {
                BigDecimal jysl =new BigDecimal(jcghmxDto_t.getJysl());
                BigDecimal yhsl =new BigDecimal(jcghmxDto_t.getYhsl());
                BigDecimal ghsl =new BigDecimal(jcghmxDto_t.getGhsl());
                jcghmxDto_t.setKhsl(String.valueOf(jysl.subtract(yhsl).add(ghsl)));
            }
            map.put("rows", jcghmxDtos);
        }else{
            map.put("rows", null);
        }
        return map;
    }
    /**
     * 请验单打印
     * @param
     * @return
     */
    @RequestMapping("/repaid/ysdprintRepaid")
    public ModelAndView ysdprintRepaid(HwxxDto hwxxDto) {
        ModelAndView mav;
        if("/labigams".equals(urlPrefix)) {
            mav=new ModelAndView("storehouse/dhxx/arrival_goods_printAcceptanceReport_medlab");
        }else {
            mav=new ModelAndView("storehouse/dhxx/arrival_goods_printAcceptanceReport");
        }
        List<HwxxDto> ysdlist=hwxxService.getHwxxByJcghids(hwxxDto);
        //根据基础数据获取打印信息

        JcsjDto jcsj = new JcsjDto();
        jcsj.setCsdm("YS");
        jcsj.setJclb(BasicDataTypeEnum.DOCUMENTNUM.getCode());
        jcsj = jcsjService.getDto(jcsj);
        mav.addObject("dqmc",StringUtil.isNotBlank(jcsj.getCskz3())?jcsj.getCskz3():"");
        mav.addObject("wjbh", StringUtil.isNotBlank(jcsj.getCsmc())?jcsj.getCsmc():"");
        mav.addObject("sxrq", StringUtil.isNotBlank(jcsj.getCskz1())?jcsj.getCskz1():"");
        mav.addObject("bbh", StringUtil.isNotBlank(jcsj.getCskz2())?jcsj.getCskz2():"");


        mav.addObject("ysdlist", ysdlist);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
}
