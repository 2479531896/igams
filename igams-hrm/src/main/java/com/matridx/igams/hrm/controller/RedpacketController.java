package com.matridx.igams.hrm.controller;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.RedisCommonKeyEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisLock;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.hrm.dao.entities.GrksglDto;
import com.matridx.igams.hrm.dao.entities.HbszDto;
import com.matridx.igams.hrm.dao.entities.HbszmxDto;
import com.matridx.igams.hrm.service.svcinterface.IGrksglService;
import com.matridx.igams.hrm.service.svcinterface.IHbszService;
import com.matridx.igams.hrm.service.svcinterface.IHbszmxService;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.commons.lang3.StringUtils;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author WYX
 * @version 1.0
 * @className RedpacketController
 * @description TODO
 * @date 10:00 2022/12/30
 **/
@Controller
@RequestMapping("/redpacket")
public class RedpacketController extends BaseController {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    private IXxglService xxglService;
    @Autowired
    private IHbszService hbszService;
    @Autowired
    private IHbszmxService hbszmxService;
    @Autowired
    IGrksglService grksglService;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Override
    public String getPrefix(){
        return urlPrefix;
    }
    private final Logger log = LoggerFactory.getLogger(RedpacketController.class);

    /**
     * 红包设置列表页面
     */
    @RequestMapping("/redpacket/pageListRedpacketsetting")
    public ModelAndView pageLisRedpacketsetting(){
        ModelAndView mav = new ModelAndView("train/redpacket/redpacketsetting_list");
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 获取红包设置列表页面数据
     */
    @RequestMapping("/redpacket/pageGetListRedpacketsetting")
    @ResponseBody
    public Map<String,Object> pageGetLisRedpacketsetting(HbszDto hbszDto){
        Map<String,Object> map = new HashMap<>();
        List<HbszDto> hbszDtos = hbszService.getPagedDtoList(hbszDto);
        map.put("rows", hbszDtos);
        map.put("total", hbszDto.getTotalNumber());
        return map;
    }
    /**
     * 查看红包设置页面
     */
    @RequestMapping("/redpacket/viewRedpacketsetting")
    public ModelAndView viewReturnManagement(HbszDto hbszDto) {
        ModelAndView mav=new ModelAndView("train/redpacket/redpacketsetting_view");
        HbszDto dtoById = hbszService.getDtoById(hbszDto.getHbszid());
        HbszmxDto hbszmxDto = new HbszmxDto();
        hbszmxDto.setHbszid(hbszDto.getHbszid());
        List<HbszmxDto> hbszmxDtos = hbszmxService.getDtoList(hbszmxDto);
        mav.addObject("hbszDto", dtoById);
        mav.addObject("hbszmxDtos", hbszmxDtos);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 新增红包设置页面
     */
    @RequestMapping("/redpacket/addRedpacketsetting")
    public ModelAndView addRedpacketsetting(HbszDto hbszDto){
        ModelAndView mav = new ModelAndView("train/redpacket/redpacketsetting_edit");
        mav.addObject("tzqlist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.NOTIF_GROUP.getCode()));//通知群
        mav.addObject("yslist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.STYLE.getCode()));//样式
        hbszDto.setFormAction("addSaveRedpacketsetting");
        mav.addObject("hbszDto", hbszDto);//通知群
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 新增保存红包设置信息
     */
    @RequestMapping("/redpacket/addSaveRedpacketsetting")
    @ResponseBody
    public Map<String,Object> addSaveRedpacketsetting(HbszDto hbszDto, HttpServletRequest request){
        User user=getLoginInfo(request);
        hbszDto.setLrry(user.getYhid());
        Map<String,Object> map=new HashMap<>();
        boolean isSuccess;
        //保存红包设置信息
        try {
            isSuccess = hbszService.addSaveRedpacketsetting(hbszDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
            map.put("urlPrefix",urlPrefix);
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
            map.put("urlPrefix",urlPrefix);
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
            map.put("urlPrefix",urlPrefix);
        }
        return map;
    }
    /**
     * 修改红包设置页面
     */
    @RequestMapping("/redpacket/modRedpacketsetting")
    public ModelAndView modRedpacketsetting(HbszDto hbszDto){
        ModelAndView mav = new ModelAndView("train/redpacket/redpacketsetting_edit");
        mav.addObject("tzqlist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.NOTIF_GROUP.getCode()));//通知群
        mav.addObject("yslist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.STYLE.getCode()));//样式
        HbszDto dtoById = hbszService.getDtoById(hbszDto.getHbszid());
        dtoById.setFormAction("modSaveRedpacketsetting");
        mav.addObject("hbszDto", dtoById);//通知群
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 修改保存红包设置信息
     */
    @RequestMapping("/redpacket/modSaveRedpacketsetting")
    @ResponseBody
    public Map<String,Object> modSaveRedpacketsetting(HbszDto hbszDto, HttpServletRequest request){
        User user=getLoginInfo(request);
        hbszDto.setXgry(user.getYhid());
        Map<String,Object> map=new HashMap<>();
        boolean isSuccess;
        //保存红包设置信息
        try {
            isSuccess = hbszService.modSaveRedpacketsetting(hbszDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
            map.put("urlPrefix",urlPrefix);
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
            map.put("urlPrefix",urlPrefix);
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
            map.put("urlPrefix",urlPrefix);
        }
        return map;
    }
    /**
     * 复制红包设置页面
     */
    @RequestMapping("/redpacket/copyRedpacketsetting")
    public ModelAndView copyRedpacketsetting(HbszDto hbszDto){
        ModelAndView mav = new ModelAndView("train/redpacket/redpacketsetting_edit");
        mav.addObject("tzqlist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.NOTIF_GROUP.getCode()));//通知群
        mav.addObject("yslist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.STYLE.getCode()));//样式
        HbszDto dtoById = hbszService.getDtoById(hbszDto.getHbszid());
        dtoById.setFormAction("copySaveRedpacketsetting");
        mav.addObject("hbszDto", dtoById);//通知群
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 复制保存红包设置信息
     */
    @RequestMapping("/redpacket/copySaveRedpacketsetting")
    @ResponseBody
    public Map<String,Object> copySaveRedpacketsetting(HbszDto hbszDto, HttpServletRequest request){
        User user=getLoginInfo(request);
        hbszDto.setLrry(user.getYhid());
        Map<String,Object> map= new HashMap<>();
        boolean isSuccess;
        //保存红包设置信息
        try {
            isSuccess = hbszService.addSaveRedpacketsetting(hbszDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
            map.put("urlPrefix",urlPrefix);
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
            map.put("urlPrefix",urlPrefix);
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
            map.put("urlPrefix",urlPrefix);
        }
        return map;
    }
    /**
     *  删除红包设置信息
     */
    @RequestMapping("/redpacket/delRedpacketsetting")
    @ResponseBody
    public Map<String,Object> delRedpacketsetting(HbszDto hbszDto, HttpServletRequest request){
        User user=getLoginInfo(request);
        hbszDto.setScry(user.getYhid());
        Map<String,Object> map= new HashMap<>();
        boolean Success;
        //保存到货信息
        try {
            Success=hbszService.delRedpacketsetting(hbszDto);
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
    /*
     * 查询红包设置明细
     * */
    @RequestMapping("/redpacket/pagedataGetRedpacketsettingDetails")
    @ResponseBody
    public Map<String,Object> pagedataGetRedpacketsettingDetails(HbszmxDto hbszmxDto) {
        Map<String,Object> map= new HashMap<>();
        if(StringUtils.isNotBlank(hbszmxDto.getHbszid())) {
            List<HbszmxDto> dtoList = hbszmxService.getDtoList(hbszmxDto);
            map.put("rows", dtoList);
        }else {
            map.put("rows", new ArrayList<>());
        }
        return map;
    }
    /*
     * 小程序链接查询是否有得到了但没领取的红包
     * */
    @RequestMapping("/redpacket/minidataGetSfHaveNo")
    @ResponseBody
    public Map<String,Object>  minidataGetSfHaveNo(HbszDto hbszDto,HttpServletRequest request) {
        User user=getLoginInfo(request);
        Map<String,Object> map = new HashMap<>();
        GrksglDto grksglDto = new GrksglDto();
        grksglDto.setPxid(hbszDto.getPxid());
        grksglDto.setRyid(user.getYhid());
        log.error("---------------------minidataGetSfHaveNo参数："+ JSON.toJSONString(hbszDto)+"ryid:"+user.getYhid());
        Object userPxInfo = redisUtil.hget(RedisCommonKeyEnum.TRAIN_USERPXINFO.getKey() + hbszDto.getPxid(),user.getYhid());
        log.error("---------------------minidataGetSfHaveNo-USERPXINFO_：" + userPxInfo);
        GrksglDto grksglDto_t;
        GrksglDto grksglDto_n;
        if (userPxInfo!=null) {
            GrksglDto grksglDto_info = JSON.parseObject(String.valueOf(userPxInfo), GrksglDto.class);
            if (StringUtil.isNotBlank(grksglDto_info.getJe())){
                map.put("isHave",true);
                return map;
            }else {
                grksglDto_n = grksglDto_info;
            }
        }else {
            grksglDto_t = grksglService.getSfHaveRedpacket(grksglDto);
            //已经领取过红包
            if (grksglDto_t!=null){
                map.put("isHave",true);
                return map;
            }
            grksglDto_n = grksglService.getSfHaveRedpacketNo(grksglDto);
        }
        //是否有红包但没领的
        log.error("---------------------minidataGetSfHaveNo-grksglDto_n："+ JSON.toJSONString(grksglDto_n));
        if (grksglDto_n!=null){
            map.put("isHaveNo",true);
            Object hbszmxs = redisUtil.get(RedisCommonKeyEnum.TRAIN_HBSZMX.getKey() + hbszDto.getHbszid());
            log.error("---------------------minidataGetSfExistRedpacket-HBSZMX_：" + hbszmxs);
            List<HbszmxDto> hbszmxDtos;
            if (hbszmxs!=null){
                hbszmxDtos = JSON.parseArray(String.valueOf(hbszmxs), HbszmxDto.class);
                if (CollectionUtils.isEmpty(hbszmxDtos)){
                    map.put("result", false);
                    map.put("sysl", -1);
                    return map;
                }
                Iterator<HbszmxDto> iterator = hbszmxDtos.iterator();
                while (iterator.hasNext()){
                    HbszmxDto next = iterator.next();
                    BigDecimal zdf=new BigDecimal(next.getZdf());
                    BigDecimal df=new BigDecimal(hbszDto.getDf());
                    // -1小于，0相等，1大于
                    if (zdf.compareTo(df) > 0){
                        iterator.remove();
                    }
                }
            }else {
                HbszmxDto hbszmxDto_t = new HbszmxDto();
                hbszmxDto_t.setHbszid(hbszDto.getHbszid());
                hbszmxDto_t.setDf(grksglDto_n.getZf());
                hbszmxDtos = hbszmxService.getDtoList(hbszmxDto_t);
            }
            log.error("---------------------minidataGetSfHaveNo-hbszmxDtos："+ JSON.toJSONString(hbszmxDtos));
            //您这次没有获得红包，下次再接再厉！
            if (CollectionUtils.isEmpty(hbszmxDtos)){
                map.put("result",false);
                map.put("sysl",-1);
                return map;
            }
            int zsl = 0;
            for (HbszmxDto hbszmxDto : hbszmxDtos) {
                int sysl = Integer.parseInt(StringUtil.isBlank(hbszmxDto.getSysl())?"0":hbszmxDto.getSysl());
                sysl = Math.max(sysl, 0);
                zsl = zsl + sysl;
            }
            //很遗憾，红包已经被抢完了！
            if (zsl<=0){
                map.put("result",false);
                map.put("sysl",0);
                return map;
            }
            //显示红包页面
            map.put("df",grksglDto_n.getZf());
            map.put("grksid",grksglDto_n.getGrksid());
            map.put("result",true);
            map.put("sysl",1);
            return map;
        }
        map.put("isHave",false);
        map.put("isHaveNo",false);
        return map;
    }
    /*
     * 答题完成获取是否存在对应红包
     * */
    @RequestMapping("/redpacket/minidataGetSfExistRedpacket")
    @ResponseBody
    public Map<String,Object>  minidataGetSfExistRedpacket(HbszDto hbszDto,HttpServletRequest request) {
        User user=getLoginInfo(request);
        Map<String,Object> map = new HashMap<>();
        HbszmxDto hbszmxDto_t = new HbszmxDto();
        hbszmxDto_t.setHbszid(hbszDto.getHbszid());
        hbszmxDto_t.setDf(hbszDto.getDf());
        log.error("---------------------minidataGetSfExistRedpacket参数："+ JSON.toJSONString(hbszDto)+"ryid:"+user.getYhid());
        GrksglDto grksglDto = new GrksglDto();
        grksglDto.setPxid(hbszDto.getPxid());
        grksglDto.setRyid(user.getYhid());
        Object userPxInfo = redisUtil.hget(RedisCommonKeyEnum.TRAIN_USERPXINFO.getKey() + hbszDto.getPxid(),user.getYhid());
        log.error("---------------------minidataGetSfExistRedpacket-USERPXINFO_：" + userPxInfo);
        GrksglDto grksglDto_t;
        if (userPxInfo!=null) {
            GrksglDto grksglDto_info = JSON.parseObject(String.valueOf(userPxInfo), GrksglDto.class);
            if (StringUtil.isNotBlank(grksglDto_info.getJe())){
                map.put("isHave",true);
                return map;
            }
        }else {
            grksglDto_t = grksglService.getSfHaveRedpacket(grksglDto);
            log.error("---------------------minidataGetSfExistRedpacket-grksglDto_t："+ JSON.toJSONString(grksglDto_t));
            //已经领取过红包
            if (grksglDto_t!=null){
                map.put("isHave",true);
                return map;
            }
        }
        Object hbszmxs = redisUtil.get(RedisCommonKeyEnum.TRAIN_HBSZMX.getKey() + hbszDto.getHbszid());
        log.error("---------------------minidataGetSfExistRedpacket-HBSZMX_：" + hbszmxs);
        List<HbszmxDto> hbszmxDtos;
        if (hbszmxs!=null){
            hbszmxDtos = JSON.parseArray(String.valueOf(hbszmxs), HbszmxDto.class);
            if (CollectionUtils.isEmpty(hbszmxDtos)){
                map.put("result", false);
                map.put("sysl", -1);
                return map;
            }
            Iterator<HbszmxDto> iterator = hbszmxDtos.iterator();
            while (iterator.hasNext()){
                HbszmxDto next = iterator.next();
                BigDecimal zdf=new BigDecimal(next.getZdf());
                BigDecimal df=new BigDecimal(hbszDto.getDf());
                // -1小于，0相等，1大于
                if (zdf.compareTo(df) > 0){
                    iterator.remove();
                }
            }
        }else {
            hbszmxDtos = hbszmxService.getDtoList(hbszmxDto_t);
            log.error("---------------------minidataGetSfExistRedpacket-hbszmxDtos："+ JSON.toJSONString(hbszmxDtos));
        }
        //您这次没有获得红包，下次再接再厉！
        if (CollectionUtils.isEmpty(hbszmxDtos)){
            map.put("result",false);
            map.put("sysl",-1);
            return map;
        }
        int zsl = 0;
        for (HbszmxDto hbszmxDto : hbszmxDtos) {
            int sysl = Integer.parseInt(StringUtil.isBlank(hbszmxDto.getSysl())?"0":hbszmxDto.getSysl());
            sysl = Math.max(sysl, 0);
            zsl = zsl + sysl;
        }
        //很遗憾，红包已经被抢完了！
        if (zsl<=0){
            map.put("result",false);
            map.put("sysl",0);
            return map;
        }
        //显示红包页面
        map.put("isHave",false);
        map.put("result",true);
        map.put("sysl",1);
        return map;
    }
    /*
     * 点击红包获取红包金额
     * */
    @RedisLock
    @RequestMapping("/redpacket/minidataGetRedpacket")
    @ResponseBody
    public Map<String,Object> minidataGetRedpacket(HbszDto hbszDto,HttpServletRequest request) {
        User user=getLoginInfo(request);
        hbszDto.setLrry(user.getYhid());
        hbszDto.setDdid(user.getDdid());
        hbszDto.setLrrymc(user.getZsxm());
        Map<String,Object> map = new HashMap<>();
        try {
            map = hbszService.processRedpacketInfo(hbszDto);
        } catch (BusinessException e) {
            log.error("----------processRedpacketInfo(BusinessException)异常:"+e.getMsg());
        } catch (Exception e) {
            log.error("----------processRedpacketInfo(Exception)异常:"+e.getMessage());
        }
        return map;
    }
    /*
     * 获取红包金额排行
     * */
    @RequestMapping("/redpacket/minidataGetRedpacketTop")
    @ResponseBody
    public Map<String,Object> minidataGetRedpacketTop(GrksglDto grksglDto,HttpServletRequest request) {
        User user=getLoginInfo(request);
        Map<String,Object> map = new HashMap<>();
        Set<Object> hbpmInfos = redisUtil.reverseRange(RedisCommonKeyEnum.TRAIN_ALLHBINFOPM.getKey(),0,-1);
        if (!CollectionUtils.isEmpty(hbpmInfos)) {
            GrksglDto grksglDto_pm = null;
            for (Object hbpmInfo : hbpmInfos) {
                GrksglDto grksglDto_info = JSON.parseObject(String.valueOf(hbpmInfo), GrksglDto.class);
                if (user.getYhid().equals(grksglDto_info.getRyid())){
                    grksglDto_pm = grksglDto_info;
                    break;
                }
            }
            if (grksglDto_pm!=null){
                int pm = 0;
                Long pmL = redisUtil.zReverseRank(RedisCommonKeyEnum.TRAIN_ALLHBINFOPM.getKey(), JSON.toJSONString(grksglDto_pm));
                if (pmL!=null){
                    pm = pmL.intValue();
                }
                grksglDto_pm.setPm(String.valueOf(pm+1));
            }
            map.put("grksglDtos",redisUtil.reverseRange(RedisCommonKeyEnum.TRAIN_ALLHBINFOPM.getKey(),0,grksglDto.getPageSize()-1).stream().map(e -> JSON.parseObject(String.valueOf(e), GrksglDto.class)).collect(Collectors.toList()));
            map.put("pm",grksglDto_pm);
        }else {
            List<GrksglDto> grksglDtos = grksglService.getRedPacketGroup(grksglDto);
            grksglDto.setRyid(user.getYhid());
            GrksglDto pm = grksglService.getRedPacketGroupPm(grksglDto);
            map.put("pm",pm);
            map.put("grksglDtos",grksglDtos);
        }
        return map;
    }

}
