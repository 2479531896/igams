package com.matridx.igams.wechat.control;


import com.alibaba.fastjson.JSON;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.post.ICommonDao;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.EntityTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.wechat.dao.entities.DzglDto;
import com.matridx.igams.wechat.dao.entities.SjhbxxDto;
import com.matridx.igams.wechat.dao.entities.SjpdglDto;
import com.matridx.igams.wechat.dao.entities.SjtdxxDto;
import com.matridx.igams.wechat.dao.entities.SjwltzDto;
import com.matridx.igams.wechat.dao.entities.SjwlxxDto;
import com.matridx.igams.wechat.service.svcinterface.IDzglService;
import com.matridx.igams.wechat.service.svcinterface.ISjkdxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjpdglService;
import com.matridx.igams.wechat.service.svcinterface.ISjtdxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjwltzService;
import com.matridx.igams.wechat.service.svcinterface.ISjwlxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Controller
@RequestMapping("/logistics")
public class InspectionLogisticsController extends BaseController {

    @Autowired
    private ISjpdglService sjpdglService;
    @Autowired
    private ISjwlxxService sjwlxxService;
    @Autowired
    private ISjwltzService sjwltzService;
    @Autowired
    private IXxglService xxglservice;
    @Autowired
    private ISjkdxxService sjkdxxService;
	@Autowired
    private RedisUtil redisUtil;
	@Autowired
    ICommonDao commonDao;
    @Autowired
    private IDzglService dzglService;
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    IXxglService xxglService;
    @Autowired
    ISjxxService sjxxService;
    @Autowired
    ISjtdxxService sjtdxxService;
    @Autowired
    IFjcfbService fjcfbService;
    Logger logger = LoggerFactory.getLogger(InspectionLogisticsController.class);
    /**
     * 跳转页面
     * @return
     */
    @RequestMapping(value="/sjpdgl/pageListView")
    public ModelAndView View() {
        ModelAndView mav=new ModelAndView("wechat/sjpdgl/sjpdgl_List");
//        Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.DETECTION_UNIT,BasicDataTypeEnum.SENDING_METHOD,
//                BasicDataTypeEnum.WECGATSAMPLE_TYPE});
        mav.addObject("detectionList", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位目的地
//        mav.addObject("sendingList", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.SENDING_METHOD.getCode()));//寄送方式合并快递类型
        mav.addObject("sendingList", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.SD_TYPE.getCode()));//寄送方式合并快递类型
        mav.addObject("samplelist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));//检测单位目的地

        return mav;
    }


    /**
     * 派单列表
     * @param
     * @return
     */
    @RequestMapping(value="/sjpdgl/pageGetListView")
    @ResponseBody
    public Map<String, Object> getPagedDtoList(SjpdglDto sjpdglDto){
        List<SjpdglDto> sjpdlist=sjpdglService.getPagedDtoList(sjpdglDto);
        Map<String, Object> map= new HashMap<>();
        map.put("total", sjpdglDto.getTotalNumber());
        map.put("rows",  sjpdlist);
        return map;
    }

    /**
     * 查看
     * @param
     * @return
     */
    @RequestMapping(value="/sjpdgl/View")
    public ModelAndView ViewList(SjpdglDto sjpdglDto){
        ModelAndView mav = new ModelAndView("wechat/sjpdgl/sjpdgl_View");
        SjpdglDto t_sjpdDto = sjpdglService.getDtoById(sjpdglDto.getSjpdid());
        sjpdglService.getYssc(t_sjpdDto);
        SjwlxxDto sjwlxxDto=new SjwlxxDto();
        sjwlxxDto.setSjpdid(sjpdglDto.getSjpdid());
        Set<String> tzrymc=new HashSet<>();
        List<SjwltzDto> sjwltzDtos=sjwltzService.getDtoListBySjpd(sjpdglDto.getSjpdid());
        for (SjwltzDto sjwltzDto : sjwltzDtos) {
            if(StringUtil.isNotBlank(sjwltzDto.getRymc())){
                tzrymc.add(sjwltzDto.getRymc());
            }
        }
        if(tzrymc.size()>0){
            t_sjpdDto.setTzry_str(String.join(",",tzrymc));
        }
        List<Map<String,Object>> mapList = (List<Map<String, Object>>) sjwlxxService.generateWlxx(sjwlxxDto).get("mapList");
        mav.addObject("sjpdglDto", t_sjpdDto);
        mav.addObject("mapList", mapList);
        return mav;
    }
    /**
     * 删除伙伴
     * @param
     * @return
     */
    @RequestMapping(value="/sjpdgl/delSjpd")
    @ResponseBody
    public Map<String, Object> delSjpd(SjpdglDto sjpdglDto){
        User user=getLoginInfo();
        sjpdglDto.setScry(user.getYhid());
        boolean isSuccess=sjpdglService.deletesjpd(sjpdglDto);
        Map<String, Object> map= new HashMap<>();
        map.put("status",isSuccess?"success":"fail");
        map.put("message",isSuccess?xxglservice.getModelById("ICOM00003").getXxnr():xxglservice.getModelById("ICOM00004").getXxnr());
        return map;
    }

/**
     * 	派单接口
     * @return
     */
    @RequestMapping("/minidataDispatchInterface")
    @ResponseBody
    public Map<String, Object> dispatchInterface() {
        Map<String, Object> map = new HashMap<>();
//        List<JcsjDto> jsfslist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.SENDING_METHOD.getCode());//寄送方式并为快递类型
        List<JcsjDto> jsfslist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.SD_TYPE.getCode());
        List<JcsjDto> yblxlist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode());
        List<JcsjDto> mddlist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
        List<JcsjDto> jsfsDtos = new ArrayList<>();
        List<JcsjDto> mddDtos = new ArrayList<>();
//        for (JcsjDto jsfs: jsfslist) {
//          if (StringUtil.isNotBlank(jsfs.getCskz1()) && jsfs.getCskz1().contains("PD")){
//              jsfsDtos.add(jsfs);
//          }
//        }
        for (JcsjDto jsfs: jsfslist) {
            List<String> jsfsStrlist = new ArrayList<>();
            if (StringUtil.isNotBlank(jsfs.getCskz2())){
                if ( jsfs.getCskz2().contains(";") && jsfs.getCskz2().contains(":") ){
                    jsfsStrlist = Arrays.asList(jsfs.getCskz2().split(";|:"));
                }else if (  jsfs.getCskz2().contains(";") ){
                    jsfsStrlist = Arrays.asList(jsfs.getCskz2().split(";"));
                }else if (  jsfs.getCskz2().contains(":") ){
                    jsfsStrlist = Arrays.asList(jsfs.getCskz2().split(":"));
                }else {
                    jsfsStrlist.add(jsfs.getCskz2());
                }
                if (jsfsStrlist.contains("PD")){
                    jsfsDtos.add(jsfs);
                }
            }
        }
        //检测单位初始值支取默认检测单位
        for (JcsjDto mdd: mddlist) {
            if (StringUtil.isNotBlank(mdd.getSfmr()) && "1".equals(mdd.getSfmr())){
                mddDtos.add(mdd);
            }
        }
        User user=new User();
        user.setJsmc("物流");
        List<User> yhjsDtos = commonDao.getYhjsDtos(user);
        List<User> xtjsDtos = commonDao.getXtjsDtos(user);
        User user2 = getLoginInfo();
        List<DzglDto> cydzlist=dzglService.getListByUser(user2.getYhid());
        SjpdglDto sjpdglDto = new SjpdglDto();
        sjpdglDto.setLrry(user2.getYhid());
        sjpdglDto = sjpdglService.getLatestSjpd(sjpdglDto);
        if (sjpdglDto != null && StringUtil.isNotBlank(sjpdglDto.getHbmc())){
            mddDtos = sjxxService.getDetectionUnit(String.valueOf(sjpdglDto.getHbmc()));
            map.put("hbmc", sjpdglDto.getHbmc());
        }else {
            map.put("hbmc", "");
        }
        map.put("cydzlist",cydzlist);
        map.put("jsfslist", jsfsDtos);
        map.put("yblxlist", yblxlist);
        map.put("mddlist", mddDtos);
//        map.put("mddlist", mddjcsjlist);
        map.put("yhjslist", yhjsDtos);
        map.put("xtjslist", xtjsDtos);
        map.put("ywlx",BusTypeEnum.IMP_LOGISTICS_DISPATCH_IMG.getCode());
        return map;
    }

    /**
     *常用地址
     * @return
     */
    @RequestMapping("/minidataAddressInterface")
    @ResponseBody
    public Map<String,Object> minidataAddressInterface(){
        Map<String,Object> map = new HashMap<>();
        User user = getLoginInfo();
        List<DzglDto> cydzlist=dzglService.getListByUser(user.getYhid());
        map.put("cydzlist",cydzlist);
        return map;
    }

    /**
     * 新增地址
     * @param
     * @return
     */
    @RequestMapping("/minidataAddaddress")
    @ResponseBody
    public Map<String,Object> minidataAddaddress(DzglDto dzglDto){
        Map<String, Object> map= new HashMap<>();
        dzglDto.setDzid(StringUtil.generateUUID());
        boolean isSuccess=dzglService.insert(dzglDto);
        User user = getLoginInfo();
        List<JcsjDto> sflist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.PROVINCE.getCode());
        map.put("sflist",sflist);
        map.put("ryid",user.getYhid());
        map.put("status",isSuccess?"success":"fail");
        map.put("message",isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 修改默认地址
     * @param
     * @return
     */
    @RequestMapping("/minidataDefaultaddress")
    @ResponseBody
    public Map<String,Object> minidataDefaultaddress(DzglDto dzglDto){
        Map<String, Object> map= new HashMap<>();
        User user = getLoginInfo();
        int flag=0;
        List<DzglDto> alladdress=dzglService.getListByUser(user.getYhid());
        for (DzglDto dto : alladdress) {
            if(dto.getDzid().equals(dzglDto.getDzid())&&"1".equals(dto.getSfmr())){
                flag=1;
                break;
            }
        }
        dzglService.cleardefault(user.getYhid());
        if(flag!=1){
            dzglService.changedefault(dzglDto);}

        map.put("status","success");
        map.put("message",xxglservice.getModelById("ICOM00001").getXxnr());
        return map;
    }

    /**
     * 修改地址
     * @param dzglDto
     * @return
     */
    @RequestMapping(value="/minidataModaddress")
    @ResponseBody
    public Map<String, Object> modaddress(DzglDto dzglDto){
        boolean isSuccess=dzglService.update(dzglDto);
        Map<String, Object> map= new HashMap<>();
        map.put("status",isSuccess?"success":"fail");
        map.put("message",isSuccess?xxglservice.getModelById("ICOM00003").getXxnr():xxglservice.getModelById("ICOM00004").getXxnr());
        return map;
    }

    /**
     * 删除
     * @param dzglDto
     * @return
     */
    @RequestMapping(value="/minidataDeladdress")
    @ResponseBody
    public Map<String,Object> minidataDeladdress(DzglDto dzglDto){
        boolean isSuccess=dzglService.delete(dzglDto);
        Map<String, Object> map= new HashMap<>();
        map.put("status",isSuccess?"success":"fail");
        map.put("message",isSuccess?xxglservice.getModelById("ICOM00003").getXxnr():xxglservice.getModelById("ICOM00004").getXxnr());
        return map;
    }

    /**
     * 	派单保存接口
     * @return
     */
    @RequestMapping("/minidataDispatchSaveInterface")
    @ResponseBody
    public Map<String, Object> dispatchSaveInterface(SjpdglDto sjpdglDto) throws RuntimeException{
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo();
        sjpdglDto.setLrry(user.getYhid());
        sjpdglDto.setXgry(user.getYhid());
        sjpdglDto.setPdrddid(user.getDdid());
        sjpdglDto.setPdrmc(user.getZsxm());
        try {
            sjpdglDto.setSjpdid(sjpdglService.pdMsgSave(sjpdglDto));
        }catch (RuntimeException e){
            map.put("status", "fail");
            map.put("message", e.getMessage());
            return map;
        }
        boolean isSuccess = StringUtil.isNotBlank(sjpdglDto.getSjpdid());
        //自送或同城闪送处理到送达
           if ((sjpdglDto.getJsfsmc().equals("自送")||sjpdglDto.getJsfsmc().equals("同城闪送")||sjpdglDto.getJsfsmc().equals("同城/闪送/滴滴"))&&sjpdglDto.getTzry_str().equals(sjpdglDto.getLrry())){
               SjwlxxDto sjwlxxDto=sjwlxxService.getDtoById(sjpdglDto.getSjpdid());
               sjwlxxDto.setTzbj("1");
               sjwlxxDto.setYgqjsj(sjpdglDto.getYjddsj());
               sjwlxxDto.setQjsj(sjpdglDto.getYjddsj());
               sjwlxxDto.setYjsdsj(sjpdglDto.getYjddsj());
               orderSaveLrInterface(sjwlxxDto);
               sjwlxxDto=sjwlxxService.getSjwlxxDtoById(sjwlxxDto);
               sjwlxxDto.setTzbj("1");
               sjwlxxDto.setFjids(sjpdglDto.getFjids());
               sjwlxxDto.setSjpdzt("35");
               sjwlxxDto.setWlzt("30");
               sjwlxxDto.setWlfy(sjpdglDto.getWlfy());
               sjwlxxDto.setGldh(sjpdglDto.getGldh());
               minidataPickUpSave(sjwlxxDto);
               sjwlxxDto=sjwlxxService.getSjwlxxDtoById(sjwlxxDto);
               sjwlxxDto.setSjpdzt("35");
               sjwlxxDto.setWlzt("40");
               sjwlxxDto.setJsfsdm("qt");
               arriveSaveLrInterface(sjwlxxDto);
        }
        //顺丰快递何京东快递全部处理
        if ((sjpdglDto.getJsfsmc().equals("顺丰快递")||sjpdglDto.getJsfsmc().equals("京东快递")||sjpdglDto.getJsfsmc().equals("京东"))){
            sjkdxxService.insertSjkdxxInfo(sjpdglDto.getSjpdid(),sjpdglDto.getGldh(),sjpdglDto.getJsfsmc(),sjpdglDto.getPdh(), EntityTypeEnum.ENTITY_SJPDGLDTO.getCode());
            SjwlxxDto sjwlxxDto=sjwlxxService.getDtoById(sjpdglDto.getSjpdid());
            sjwlxxDto.setTzbj("1");
            sjwlxxDto.setYgqjsj(sjpdglDto.getYjddsj());
            sjwlxxDto.setQjsj(sjpdglDto.getYjddsj());
            sjwlxxDto.setYjsdsj(sjpdglDto.getYjddsj());
            orderSaveLrInterface(sjwlxxDto);
            sjwlxxDto=sjwlxxService.getSjwlxxDtoById(sjwlxxDto);
            sjwlxxDto.setTzbj("1");
            sjwlxxDto.setFjids(sjpdglDto.getFjids());
            sjwlxxDto.setSjpdzt("35");
            sjwlxxDto.setWlzt("30");
            sjwlxxDto.setWlfy(sjpdglDto.getWlfy());
            sjwlxxDto.setGldh(sjpdglDto.getGldh());
            minidataPickUpSave(sjwlxxDto);
            sjwlxxDto=sjwlxxService.getSjwlxxDtoById(sjwlxxDto);
            sjwlxxDto.setSjpdzt("35");
            sjwlxxDto.setWlzt("40");
            sjwlxxDto.setJsfsdm("qt");
            arriveSaveLrInterface(sjwlxxDto);
        }
        map.put("sjpdid",sjpdglDto.getSjpdid());
        SjpdglDto sjpdglDto_t=sjpdglService.getDtoById(sjpdglDto.getSjpdid());
        map.put("pdorbc",sjpdglDto_t.getZt());
        map.put("sjpdglDto",sjpdglDto_t);
        map.put("status",isSuccess?"success":"fail");
        map.put("message",isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 	获取订单信息接口
     * @return
     */
    @RequestMapping("/minidataDispatchGetInfoInterface")
    @ResponseBody
    public Map<String, Object> dispatchGetInfoInterface(SjpdglDto sjpdglDto) {
        Map<String, Object> map;
        User user = getLoginInfo();
        sjpdglDto.setLrry(user.getYhid());
        map = sjpdglService.pdMsgGet(sjpdglDto);
        //修改派单初始化根据伙伴名称修改目的地初始范围
        List<JcsjDto> jcsjlist = sjxxService.getDetectionUnit(String.valueOf(map.get("hbmc")));
        map.put("unitList", jcsjlist);
        List<FjcfbDto> fjcfbDtos=fjcfbService.selectFjcfbDtoByYwid(sjpdglDto.getSjpdid());
        map.put("fjcfbDtos",fjcfbDtos);
        return map;
    }

    /**
     * 根据伙伴查询检测单位
     * @return
     */
    @RequestMapping("/minidataSelectDetectionUnit")
    @ResponseBody
    public Map<String, Object> selectDetectionUnit(String hbmc){
        Map<String,Object> map = new HashMap<>();
        List<JcsjDto> jcsjlist = sjxxService.getDetectionUnit(hbmc);
        map.put("unitList", jcsjlist);
        return map;
    }


    /**
     * 	取消接单
     * @return
     */
    @RequestMapping("/minidataCancelReceipt")
    @ResponseBody
    public Map<String ,Object>CancelReceipt(SjwlxxDto sjwlxxDto){
        Map<String,Object> map = new HashMap<>();
        sjwlxxDto.setXgry(sjwlxxDto.getJdr());
        boolean flag=false;
        if (StringUtil.isNotBlank(sjwlxxDto.getSjtdid())) {
            String sffs="no";
            int i=0;
            List<SjwlxxDto> sjwlxxDtoList=sjwlxxService.getSjwlxxsBySjtdid(sjwlxxDto);
            if (sjwlxxDtoList!=null&&sjwlxxDtoList.size()>0){

                for (SjwlxxDto sjwlxxDto1:sjwlxxDtoList){
                    i++;
                    sjwlxxDto1.setFjids(sjwlxxDto.getFjids());
                    sjwlxxDto1.setSjtdid(sjwlxxDto.getSjtdid());
                    sjwlxxDto1.setQxjdyy(sjwlxxDto.getQxjdyy());
                    if (i==sjwlxxDtoList.size())
                        sffs="yes";
                    flag = sjwlxxService.cancelReceipt(sjwlxxDto1,sffs);
                }
                if (flag){
                    SjtdxxDto sjtdxxDto=new SjtdxxDto();
                    sjtdxxDto.setSjtdid(sjwlxxDto.getSjtdid());
                    SjtdxxDto sjtdxxDto1=sjtdxxService.getDtoById(sjwlxxDto.getSjtdid());
                    sjtdxxDto.setJdr(sjtdxxDto1.getLrry());
                    sjtdxxDto.setWlzt("10");
                    sjtdxxDto.setJdbj("0");
                    flag=sjtdxxService.update(sjtdxxDto);
                }
            }
        }
        else{
            flag=sjwlxxService.cancelReceipt(sjwlxxDto,"yes");
        }
        map.put("status",flag?"success":"fail");
        map.put("ywlx", BusTypeEnum.IMP_LOGISTICS_CANCELRECEIPT.getCode());
        map.put("message",flag?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 	我的全部订单
     * @return
     */
    @RequestMapping("/minidataMyLogistics")
    @ResponseBody
    public Map<String, Object> MyLogistics(SjpdglDto sjpdglDto) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo();
        sjpdglDto.setLrry(user.getYhid());
        List<SjpdglDto> MyLogisticsList=sjpdglService.getPagedBylrry(sjpdglDto);
        map.put("MyLogisticsList",MyLogisticsList);
        return map;
    }
    /**
     * 	接单列表接口
     * @return
     */
    @RequestMapping("/minidataOrderListInterface")
    @ResponseBody
    public Map<String, Object> orderListInterface(SjpdglDto sjpdglDto) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo();
        sjpdglDto.setRyid(user.getYhid());
        List<SjpdglDto> dtoList = sjpdglService.getPagedOrderList(sjpdglDto);
        map.put("rows",dtoList);
        map.put("total",sjpdglDto.getTotalNumber());
        return map;
    }
    /**
     * 	录入接口
     * @return
     */
    @RequestMapping("/minidataLrInterface")
    @ResponseBody
    public Map<String, Object> lrInterface(SjwlxxDto sjwlxxDto) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isBlank(sjwlxxDto.getSjtdid())) {
            sjwlxxDto = sjwlxxService.getDto(sjwlxxDto);
            //获取高铁
            if ("GT".equals(sjwlxxDto.getJsfsdm())) {
                SjwlxxDto sjwlxxDto_t = new SjwlxxDto();
                sjwlxxDto_t.setJsfs(sjwlxxDto.getWljsfs());
                sjwlxxDto_t.setJdr(sjwlxxDto.getJdr());
                sjwlxxDto_t.setWlid(sjwlxxDto.getWlid());
                sjwlxxDto_t.setWlzt("30");
                sjwlxxDto_t.setJcdw(sjwlxxDto.getJcdw());
                List<SjwlxxDto> sjwlxxs = sjwlxxService.getSjwlxxsByJsfsAndYh(sjwlxxDto_t);
                map.put("sjwlxxs", sjwlxxs);
            }
            map.put("sjpdglDto", sjwlxxDto);
            User user = new User();
            user.setJsmc("物流");
            List<User> yhjsDtos = commonDao.getYhjsDtos(user);
            List<User> xtjsDtos = commonDao.getXtjsDtos(user);
            map.put("yhjslist", yhjsDtos);
            map.put("xtjslist", xtjsDtos);
            map.put("ywlx", BusTypeEnum.IMP_LOGISTICS_ARRIVE.getCode());
        }
        else {
            SjtdxxDto sjtdxxDto=new SjtdxxDto();
            sjtdxxDto.setSjtdid(sjwlxxDto.getSjtdid());
            sjtdxxDto=sjtdxxService.getDtoById(sjtdxxDto.getSjtdid());
            List<SjwlxxDto> sjwlxxDtoList=sjwlxxService.getSjwlxxsBySjtdid(sjwlxxDto);
            map.put("sjwlxxDtoList",sjwlxxDtoList);
            map.put("sjtdxxDto",sjtdxxDto);
        }
        return map;
    }
    /**
     * 新增团单信息
     */
    @RequestMapping("/minidataAddTdxxInterface")
    @ResponseBody
    public Map<String, Object> minidataAddTdxxInterface(SjwlxxDto sjwlxxDto) {
        Map<String, Object> map = new HashMap<>();
//        List<JcsjDto> jsfsList = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.SENDING_METHOD.getCode());//寄送方式合并快递类型
        List<JcsjDto> jsfsList = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.SD_TYPE.getCode());
        for (JcsjDto jcsjDto:jsfsList){
            if ("GT".equals(jcsjDto.getCsdm())){
                sjwlxxDto.setJsfs(jcsjDto.getCsid());
                break;
            }
        }
        User user=getLoginInfo();
        sjwlxxDto.setJdr(user.getYhid());
        List<SjwlxxDto> sjwlxxs = sjwlxxService.getSjwlxxsByJsfsAndYh(sjwlxxDto);
        map.put("sjwlxxs", sjwlxxs);
        return map;
    }
        /**
         * 	接单录入保存接口
         * @return
         */
    @RequestMapping("/minidataOrderSaveLrInterface")
    @ResponseBody
    public Map<String, Object> orderSaveLrInterface(SjwlxxDto sjwlxxDto) {
        User user = getLoginInfo();
        Map<String, Object> map = new HashMap<>();
        String sffs="no";
        //转换成时间格式24小时制
        Date date = new Date();
        SimpleDateFormat df_24 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sjwlxxDto.setJdsj(df_24.format(date));
        sjwlxxDto.setXgry(user.getYhid());
        if (StringUtil.isBlank(sjwlxxDto.getJdr())) {
                sjwlxxDto.setJdr(user.getYhid());
        }
        if (StringUtil.isBlank(sjwlxxDto.getSjtdid())) {
            //根据主键ID从redis中获取数据
            Object get = redisUtil.get("SJWLXX:" + sjwlxxDto.getWlid());
            //若获取到，则更新redis中的接单标记，再发送Rabbit更新数据库
            if (get != null) {
                SjwlxxDto sjwlxxDto_t = JSON.parseObject(get.toString(), SjwlxxDto.class);
                if ("0".equals(sjwlxxDto_t.getScbj())) {
                    if ("10".equals(sjwlxxDto_t.getWlzt())) {//接单标记为0或者wlzt=10，则可以接单
                        sjwlxxDto_t.setJdbj("1");
                        sjwlxxDto_t.setWlzt("20");
                        sjwlxxDto_t.setJdr(sjwlxxDto.getJdr());
                        sjwlxxDto_t.setJdrmc(sjwlxxDto.getJdrmc());
                        sjwlxxDto_t.setJdrddid(sjwlxxDto.getJdrddid());
                        SjpdglDto sjpdglDto = new SjpdglDto();
                        sjpdglDto.setSjpdid(sjwlxxDto.getSjpdid());
                        sjpdglDto.setJdrmc(sjwlxxDto.getJdrmc());
                        sjpdglDto.setPdrddid(sjwlxxDto_t.getPdrddid());
                        sjpdglDto.setPdrmc(sjwlxxDto_t.getPdrmc());
                        sjpdglDto.setBblxmc(sjwlxxDto_t.getBblxmc());
                        sjpdglDto.setQydz(sjwlxxDto_t.getQydz());
                        sjpdglDto.setYgqjsj(sjwlxxDto_t.getYgqjsj());
                        sjpdglDto.setLrry(sjwlxxDto_t.getLrry());
                        sjpdglDto.setJdbj("1");
                        redisUtil.set("SJWLXX:" + sjwlxxDto.getWlid(), JSON.toJSONString(sjwlxxDto_t), 288000);
                        sjwlxxService.orderLrSave(sjwlxxDto, sjpdglDto,sffs);
                        map.put("status", "success");
                        map.put("message", "接单成功！");
                    } else {//否则，无法接单
                        map.put("status", "fail");
                        map.put("message", "手速慢了哦，已经被别人接单了！");
                    }
                } else {
                    map.put("status", "fail");
                    map.put("message", "该订单已经删除或取消！");
                }
            } else {//若未获取到，则重新查询送检派单信息，重新将其放到redis中，更新redis中的接单标记，再发送Rabbit更新数据库
                SjpdglDto sjpdglDto = new SjpdglDto();
                sjpdglDto.setSjpdid(sjwlxxDto.getSjpdid());
                SjwlxxDto dto = sjwlxxService.getDto(sjwlxxDto);
                if (dto != null) {
                    if ("0".equals(dto.getScbj())) {
                        if ("10".equals(dto.getWlzt())) {//接单标记为0，则可以接单
                            dto.setJdbj("1");
                            dto.setWlzt("20");
                            dto.setJdr(sjwlxxDto.getJdr());
                            dto.setJdrmc(sjwlxxDto.getJdrmc());
                            dto.setJdrddid(sjwlxxDto.getJdrddid());
                            sjpdglDto.setJdbj("1");
                            sjpdglDto.setJdrmc(sjwlxxDto.getJdrmc());
                            sjpdglDto.setPdrddid(dto.getPdrddid());
                            sjpdglDto.setPdrmc(dto.getPdrmc());
                            sjpdglDto.setBblxmc(dto.getBblxmc());
                            sjpdglDto.setLrry(dto.getLrry());
                            if (StringUtil.isNotBlank(dto.getSjwlid())) {
                                sjpdglDto.setQydz(dto.getDddd());
                            } else {
                                sjpdglDto.setQydz(dto.getQydz());
                            }
                            sjpdglDto.setYgqjsj(dto.getYgqjsj());
                            redisUtil.set("SJWLXX:" + dto.getWlid(), JSON.toJSONString(dto), 288000);
                            sjwlxxService.orderLrSave(sjwlxxDto, sjpdglDto,sffs);
                            map.put("status", "success");
                            map.put("message", "接单成功！");
                        } else {//否则，无法接单
                            redisUtil.set("SJWLXX:" + dto.getWlid(), JSON.toJSONString(dto), 288000);
                            map.put("status", "fail");
                            map.put("message", "手速慢了哦，已经被别人接单了！");
                        }
                    } else {
                        map.put("status", "fail");
                        map.put("message", "该订单已经删除或取消！");
                    }
                }

            }
        }
        if (StringUtil.isNotBlank(sjwlxxDto.getSjtdid())) {
            List<SjwlxxDto> sjwlxxDtoList = sjwlxxService.getSjwlxxsBySjtdid(sjwlxxDto);
            if (sjwlxxDtoList.size() > 0) {
                int i=0;
                for (SjwlxxDto sjwlxxDto3 : sjwlxxDtoList) {
                    i++;
                    if (i==sjwlxxDtoList.size())
                        sffs="yes";
                    sjwlxxDto.setWlid(sjwlxxDto3.getWlid());
                    //根据主键ID从redis中获取数据
                    Object get = redisUtil.get("SJWLXX:" + sjwlxxDto.getWlid());
                    //若获取到，则更新redis中的接单标记，再发送Rabbit更新数据库
                    if (get != null) {
                        if ("0".equals(sjwlxxDto3.getScbj())) {
                            if ("10".equals(sjwlxxDto3.getWlzt())) {//接单标记为0或者wlzt=10，则可以接单
                                sjwlxxDto3.setJdbj("1");
                                sjwlxxDto3.setWlzt("20");
                                sjwlxxDto3.setJdr(sjwlxxDto.getJdr());
                                sjwlxxDto3.setJdrmc(sjwlxxDto.getJdrmc());
                                sjwlxxDto3.setJdrddid(sjwlxxDto.getJdrddid());
                                SjpdglDto sjpdglDto = new SjpdglDto();
                                sjpdglDto.setSjpdid(sjwlxxDto.getSjpdid());
                                sjpdglDto.setJdrmc(sjwlxxDto.getJdrmc());
                                sjpdglDto.setPdrddid(sjwlxxDto3.getPdrddid());
                                sjpdglDto.setPdrmc(sjwlxxDto3.getPdrmc());
                                sjpdglDto.setBblxmc(sjwlxxDto3.getBblxmc());
                                sjpdglDto.setQydz(sjwlxxDto3.getQydz());
                                sjpdglDto.setYgqjsj(sjwlxxDto3.getYgqjsj());
                                sjpdglDto.setSjpdid(sjwlxxDto3.getSjpdid());
                                sjpdglDto.setLrry(sjwlxxDto3.getLrry());
                                sjpdglDto.setJdbj("1");
                                redisUtil.set("SJWLXX:" + sjwlxxDto.getWlid(), JSON.toJSONString(sjwlxxDto3), 288000);
                                sjwlxxService.orderLrSave(sjwlxxDto, sjpdglDto,sffs);
                                map.put("status", "success");
                                map.put("message", "接单成功！");
                            } else {//否则，无法接单
                                map.put("status", "fail");
                                map.put("message", "手速慢了哦，已经被别人接单了！");
                            }
                        } else {
                            map.put("status", "fail");
                            map.put("message", "该订单已经删除或取消！");
                        }
                    } else {//若未获取到，则重新查询送检派单信息，重新将其放到redis中，更新redis中的接单标记，再发送Rabbit更新数据库
                        SjpdglDto sjpdglDto = new SjpdglDto();
                        sjpdglDto.setSjpdid(sjwlxxDto.getSjpdid());
                        SjwlxxDto dto = sjwlxxService.getDto(sjwlxxDto);
                        if (dto != null) {
                            if ("0".equals(dto.getScbj())) {
                                if ("10".equals(dto.getWlzt())) {//接单标记为0，则可以接单
                                    dto.setJdbj("1");
                                    dto.setWlzt("20");
                                    dto.setJdr(sjwlxxDto.getJdr());
                                    dto.setJdrmc(sjwlxxDto.getJdrmc());
                                    dto.setJdrddid(sjwlxxDto.getJdrddid());
                                    sjpdglDto.setJdbj("1");
                                    sjpdglDto.setJdrmc(sjwlxxDto.getJdrmc());
                                    sjpdglDto.setPdrddid(dto.getPdrddid());
                                    sjpdglDto.setPdrmc(dto.getPdrmc());
                                    sjpdglDto.setBblxmc(dto.getBblxmc());
                                    sjpdglDto.setSjpdid(dto.getSjpdid());
                                    sjpdglDto.setLrry(dto.getLrry());
                                    if (StringUtil.isNotBlank(dto.getSjwlid())) {
                                        sjpdglDto.setQydz(dto.getDddd());
                                    } else {
                                        sjpdglDto.setQydz(dto.getQydz());
                                    }
                                    sjpdglDto.setYgqjsj(dto.getYgqjsj());
                                    redisUtil.set("SJWLXX:" + dto.getWlid(), JSON.toJSONString(dto), 288000);
                                    sjwlxxService.orderLrSave(sjwlxxDto, sjpdglDto,sffs);
                                } else {//否则，无法接单
                                    redisUtil.set("SJWLXX:" + dto.getWlid(), JSON.toJSONString(dto), 288000);
                                    map.put("status", "fail");
                                    map.put("message", "手速慢了哦，已经被别人接单了！");
                                    return map;
                                }
                            } else {
                                map.put("status", "fail");
                                map.put("message", "该订单已经删除或取消！");
                                return map;
                            }
                        }

                    }

                }
            }
            SjtdxxDto sjtdxxDto = new SjtdxxDto();
            sjtdxxDto.setSjtdid(sjwlxxDto.getSjtdid());
            sjtdxxDto.setJdbj("1");
            sjtdxxDto.setWlzt("20");
            sjtdxxDto.setJdr(user.getYhid());
            sjtdxxDto.setJdsj(sjwlxxDto.getJdsj());
            sjtdxxDto.setYgqjsj(sjwlxxDto.getYgqjsj());
            sjtdxxDto.setQjbz(sjwlxxDto.getQjbz());
            sjtdxxDto.setWlfy(sjwlxxDto.getWlfy());
            boolean isSuccess = sjtdxxService.update(sjtdxxDto);
            if (isSuccess) {
                map.put("status", "success");
                map.put("message", "接单成功！");
            }
        }
        return map;
    }

    /**
     * 取件页面初始化
     * @param sjwlxxDto
     * @return
     */
    @RequestMapping("/minidataPickUpPage")
    @ResponseBody
    public Map<String, Object> minidataPickUpPage(SjwlxxDto sjwlxxDto) {
        Map<String, Object> map = new HashMap<>();
//        List<JcsjDto> jsfsList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.SENDING_METHOD.getCode());//寄送方式并为快递类型
        List<JcsjDto> jsfsList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.SD_TYPE.getCode());
        if (StringUtil.isBlank(sjwlxxDto.getSjtdid())) {
            SjwlxxDto sjwlxxDto_t = sjwlxxService.getSjwlxxDtoById(sjwlxxDto);
            map.put("sjwlxxDto", sjwlxxDto_t);
        }else {
            List<SjwlxxDto> sjwlxxDtoList=sjwlxxService.getSjwlxxsBySjtdid(sjwlxxDto);
            SjtdxxDto sjtdxxDto=sjtdxxService.getDtoById(sjwlxxDto.getSjtdid());
            map.put("sjtdxxDto",sjtdxxDto);
            map.put("sjwlxxDtoList",sjwlxxDtoList);
        }
        map.put("jsfsList", jsfsList);
        map.put("ywlx", BusTypeEnum.IMP_LOGISTICS_PICKUP_IMG.getCode());
        return map;
    }
    /**
     * 取件数据保存
     * @param sjwlxxDto
     * @return
     */
    @RequestMapping("/minidataPickUpSave")
    @ResponseBody
    public Map<String, Object> minidataPickUpSave(SjwlxxDto sjwlxxDto) throws RuntimeException{
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo();
        sjwlxxDto.setXgry(user.getYhid());
        sjwlxxDto.setLrrymc(user.getZsxm());
        boolean isSuccess = false;
        if (StringUtil.isNotBlank(sjwlxxDto.getSjtdid())) {
            List<SjwlxxDto> sjwlxxDtoList = sjwlxxService.getSjwlxxsBySjtdid(sjwlxxDto);
            for (SjwlxxDto sjwlxxDto1 : sjwlxxDtoList) {
                sjwlxxDto.setWlid(sjwlxxDto1.getWlid());
                SjwlxxDto sjwlxxDto3=sjwlxxService.getDto(sjwlxxDto);
                sjwlxxDto.setSjpdid(sjwlxxDto3.getSjpdid());
                sjwlxxDto.setSjtdid(sjwlxxDto1.getSjtdid());
                sjwlxxDto.setJdrmc(sjwlxxDto3.getJdrmc());
                sjwlxxDto.setLrry(sjwlxxDto3.getLrry());
                try {
                    isSuccess = sjwlxxService.pickUpSaveInfo(sjwlxxDto, user);
                } catch (RuntimeException e) {
                    map.put("status", "fail");
                    map.put("message", e.getMessage());
                    return map;
                }
                if (isSuccess){
                    SjtdxxDto sjtdxxDto=new SjtdxxDto();
                    sjtdxxDto.setSjtdid(sjwlxxDto.getSjtdid());
                    sjtdxxDto.setWlzt("30");
                    sjtdxxDto.setJdr(user.getYhid());
                    sjtdxxDto.setYjddsj(sjwlxxDto.getYjddsj());
                    sjtdxxDto.setJsfs(sjwlxxDto.getJsfs());
                    if (StringUtil.isNotBlank(sjwlxxDto.getQjbz()))
                        sjtdxxDto.setQjbz(sjwlxxDto.getQjbz());
                    if (StringUtil.isNotBlank(sjwlxxDto.getWlfy()))
                        sjtdxxDto.setWlfy(sjwlxxDto.getWlfy());
                    isSuccess=sjtdxxService.update(sjtdxxDto);
                }
            }
            if (isSuccess){
                sjwlxxDto.setWlid(sjwlxxDtoList.get(0).getWlid());
                //发送取件通知给派单人
                if(isSuccess&&(StringUtil.isBlank(sjwlxxDto.getTzbj()))){
                    //获取送检派单信息
                    SjwlxxDto sjwlxxDto_t =sjwlxxService.getSjwlxxDtoById(sjwlxxDto);
                    //消息替换map
                    Map<String, Object> msgMap = new HashMap<>();
                    msgMap.put("jdrmc", user.getZsxm());
                    msgMap.put("sjwlxxDto", sjwlxxDto_t);
                    String msgContent = xxglService.getReplaceMsg("ICOMM_QJ00002",msgMap);
                    String jsfs = sjwlxxDto.getJsfs();
//                    JcsjDto jsfsDto = redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.SENDING_METHOD.getCode(), jsfs);//寄送方式并为快递类型
                    JcsjDto jsfsDto = redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.SD_TYPE.getCode(), jsfs);
                    String jsfscsdm = jsfsDto.getCsdm();
                    if("SF".equals(jsfscsdm)||"JD".equals(jsfscsdm)||"TC".equals(jsfscsdm)){
                        msgContent += "并由"+jsfsDto.getCsmc()+"配送！";
                    }
                    //发送取消接单的消息通知到派单人
                    String internalbtn = null;
                    try {
                        internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/logistics/my/viewlogistics/viewlogistics&wbfw=1&sjpdid=" + sjwlxxDto_t.getSjpdid(), "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        logger.error(e.getMessage());
                    }
                    List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
                    OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
                    btnJsonList.setTitle("小程序");
                    btnJsonList.setActionUrl(internalbtn);
                    btnJsonLists.add(btnJsonList);
                    //小程序访问
                    talkUtil.sendCardMessage(sjwlxxDto_t.getYhm(), sjwlxxDto_t.getPdrddid(), xxglService.getMsg("ICOMM_QJ00001"), msgContent, btnJsonLists, "1");

                }
            }
        }
        else {
            try {
                SjwlxxDto sjwlxxDto1=sjwlxxService.getDto(sjwlxxDto);
                sjwlxxDto.setJdrmc(sjwlxxDto1.getJdrmc());
                isSuccess = sjwlxxService.pickUpSaveInfo(sjwlxxDto, user);
            } catch (RuntimeException e) {
                map.put("status", "fail");
                map.put("message", e.getMessage());
                return map;
            }
        }

        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
        return map;
    }
    /**
     * 	送达录入保存接口
     * @return
     */
    @RequestMapping("/minidataArriveSaveLrInterface")
    @ResponseBody
    public Map<String, Object> arriveSaveLrInterface(SjwlxxDto sjwlxxDto) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo();
        boolean isSuccess = sjwlxxService.arriveSaveLr(sjwlxxDto,user);
        map.put("status",isSuccess?"success":"fail");
        map.put("message",isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
        return map;
    }
    /**
     * 取消订单接口
     */
    @RequestMapping("/minidataConcelOrder")
    @ResponseBody
    public Map<String, Object> concelOrder(SjpdglDto sjpdglDto) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo();
        sjpdglDto.setScry(user.getYhid());
        sjpdglDto.setLsjl(user.getZsxm());
        boolean isSuccess=sjpdglService.cancelOrder(sjpdglDto);
        map.put("status",isSuccess?"success":"fail");
        map.put("message",isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 获取物流信息
     * @param sjwlxxDto
     * @return
     */
    @RequestMapping("/minidataGetWlxx")
    @ResponseBody
    public Map<String,Object> getWlxxList(SjwlxxDto sjwlxxDto) {
        return sjwlxxService.generateWlxx(sjwlxxDto);
    }
    /**
     * 获取基本信息
     * @param sjpdglDto
     * @return
     */
    @RequestMapping("/minidataGetPdJbxx")
    @ResponseBody
    public Map<String,Object> getPdJbxx(SjpdglDto sjpdglDto) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isBlank(sjpdglDto.getSjtdid())) {
            sjpdglDto = sjpdglService.getDtoById(sjpdglDto.getSjpdid());
            map.put("sjpdglDto",sjpdglDto);
        }
        else {
            SjwlxxDto sjwlxxDto = new SjwlxxDto();
            sjwlxxDto.setSjtdid(sjpdglDto.getSjtdid());
            List<SjwlxxDto> sjwlxxDtoList = sjwlxxService.getSjwlxxsBySjtdid(sjwlxxDto);
            SjtdxxDto sjtdxxDto=sjtdxxService.getDtoById(sjwlxxDto.getSjtdid());
            map.put("sjwlxxDtoList", sjwlxxDtoList);
            map.put("sjtdxxDto", sjtdxxDto);
        }
        return map;
    }
    /**
     * 获取基本信息
     * @param sjwlxxDto
     * @return
     */
    @RequestMapping("/minidataGetTdxx")
    @ResponseBody
    public Map<String,Object> minidataGetTdxx(SjwlxxDto sjwlxxDto) {
        Map<String, Object> map = new HashMap<>();
            List<SjwlxxDto> sjwlxxDtoList = sjwlxxService.getSjwlxxsBySjtdid(sjwlxxDto);
            SjtdxxDto sjtdxxDto=sjtdxxService.getDtoById(sjwlxxDto.getSjtdid());
            map.put("sjwlxxDtoList", sjwlxxDtoList);
            map.put("sjtdxxDto",sjtdxxDto);
        return map;
    }
    /**
     * 获取已完成和运输中的订单(不做权限设置)
     * @param sjpdglDto
     * @return
     */
    @RequestMapping("/minidataGetArried")
    @ResponseBody
    public Map<String,Object> minidataGetArried(SjpdglDto sjpdglDto) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo();
        List<SjpdglDto> list=sjpdglService.getPagedOrdersByZt(sjpdglDto,user);
        map.put("rows",list);
        return map;
    }
    /**
     * 个人物流清单
     */
    @RequestMapping("/minidataGetSignalOrders")
    @ResponseBody
    public Map<String,Object> minidataGetSinal(SjpdglDto sjpdglDto){
        Map<String,Object> map= new HashMap<>();
        User user = getLoginInfo();
        sjpdglDto.setLrry(user.getYhid());
        List<SjpdglDto> list=sjpdglService.getDtoListSignalAndAll(sjpdglDto,user);
        Map<String,Object> ybs=sjpdglService.getTodayArriveYbs(sjpdglDto,user);
        map.put("list",list);
        map.put("bblxs",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));
//        map.put("jsfss",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.SENDING_METHOD.getCode()));//寄送方式合并快递类型
        map.put("jsfss",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.SD_TYPE.getCode()));//寄送方式合并快递类型
        map.put("jcdws",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));
        map.put("ybs",ybs);
        return map;
    }


    /**
     * 	新增派单接口
     * @return
     */
    @RequestMapping("/sjpdgl/addSjpdgl")
    public ModelAndView addSjpdgl() {
        ModelAndView mav=new ModelAndView("wechat/sjpdgl/sjpdgl_Add");
//        List<JcsjDto> jsfslist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.SENDING_METHOD.getCode());//寄送方式合并快递类型
        List<JcsjDto> jsfslist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.SD_TYPE.getCode());
        List<JcsjDto> yblxlist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode());
        List<JcsjDto> mddlist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
        List<JcsjDto> jsfsDtos = new ArrayList<>();
        List<JcsjDto> mddDtos = new ArrayList<>();
//        for (JcsjDto jsfs: jsfslist) {
//            if (StringUtil.isNotBlank(jsfs.getCskz1()) && jsfs.getCskz1().contains("PD")){
//                jsfsDtos.add(jsfs);
//            }
//        }
        for (JcsjDto jsfs: jsfslist) {
            List<String> jsfsStrlist = new ArrayList<>();
            if (StringUtil.isNotBlank(jsfs.getCskz2())){
                if ( jsfs.getCskz2().contains(";") && jsfs.getCskz2().contains(":") ){
                    jsfsStrlist = Arrays.asList(jsfs.getCskz2().split(";|:"));
                }else if (  jsfs.getCskz2().contains(";") ){
                    jsfsStrlist = Arrays.asList(jsfs.getCskz2().split(";"));
                }else if (  jsfs.getCskz2().contains(":") ){
                    jsfsStrlist = Arrays.asList(jsfs.getCskz2().split(":"));
                }else {
                    jsfsStrlist.add(jsfs.getCskz2());
                }
                if (jsfsStrlist.contains("PD")){
                    jsfsDtos.add(jsfs);
                }
            }
        }
        //检测单位初始值支取默认检测单位
        for (JcsjDto mdd: mddlist) {
            if (StringUtil.isNotBlank(mdd.getSfmr()) && "1".equals(mdd.getSfmr())){
                mddDtos.add(mdd);
            }
        }
        User user=new User();
        user.setJsmc("物流");
        List<User> yhjsDtos = commonDao.getYhjsDtos(user);
        List<User> xtjsDtos = commonDao.getXtjsDtos(user);
        User user2 = getLoginInfo();
        List<DzglDto> cydzlist=dzglService.getListByUser(user2.getYhid());
        SjpdglDto sjpdglDto = new SjpdglDto();
        sjpdglDto.setLrry(user2.getYhid());
        sjpdglDto = sjpdglService.getLatestSjpd(sjpdglDto);
        if (sjpdglDto != null && StringUtil.isNotBlank(sjpdglDto.getHbmc())){
            mddDtos = sjxxService.getDetectionUnit(String.valueOf(sjpdglDto.getHbmc()));
            mav.addObject("hbmc", sjpdglDto.getHbmc());
        }else {
            mav.addObject("hbmc", "");
        }
        SjpdglDto sjpdglDto_t = new SjpdglDto();
        sjpdglDto_t.setSfsf("0");
        mav.addObject("sjpdglDto",sjpdglDto_t);
        mav.addObject("cydzlist",cydzlist);//默认地址暂时不要
        mav.addObject("jsfslist", jsfsDtos);
        mav.addObject("yblxlist", yblxlist);
        mav.addObject("mddlist", mddDtos);
        mav.addObject("yhjslist", yhjsDtos);
        mav.addObject("xtjslist", xtjsDtos);
        mav.addObject("ywlx",BusTypeEnum.IMP_LOGISTICS_DISPATCH_IMG.getCode());
        mav.addObject("pdbj","1");
        return mav;
    }

    /**
     * 	修改派单接口
     * @return
     */
    @RequestMapping("/sjpdgl/modSjpdgl")
    @ResponseBody
    public ModelAndView modSjpdgl(SjpdglDto sjpdglDto) {
        ModelAndView mav=new ModelAndView("wechat/sjpdgl/sjpdgl_Add");
        List<JcsjDto> jsfslist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.SD_TYPE.getCode());
        List<JcsjDto> yblxlist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode());
        List<JcsjDto> mddlist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
        List<JcsjDto> jsfsDtos = new ArrayList<>();
        List<JcsjDto> mddDtos = new ArrayList<>();
        for (JcsjDto jsfs: jsfslist) {
            List<String> jsfsStrlist = new ArrayList<>();
            if (StringUtil.isNotBlank(jsfs.getCskz2())){
                if ( jsfs.getCskz2().contains(";") && jsfs.getCskz2().contains(":") ){
                    jsfsStrlist = Arrays.asList(jsfs.getCskz2().split(";|:"));
                }else if (  jsfs.getCskz2().contains(";") ){
                    jsfsStrlist = Arrays.asList(jsfs.getCskz2().split(";"));
                }else if (  jsfs.getCskz2().contains(":") ){
                    jsfsStrlist = Arrays.asList(jsfs.getCskz2().split(":"));
                }else {
                    jsfsStrlist.add(jsfs.getCskz2());
                }
                if (jsfsStrlist.contains("PD")){
                    jsfsDtos.add(jsfs);
                }
            }
        }
        SjpdglDto dtoById = sjpdglService.getDtoById(sjpdglDto.getSjpdid());
        //检测单位初始值支取默认检测单位
        for (JcsjDto mdd: mddlist) {
            if (StringUtil.isNotBlank(mdd.getSfmr()) && "1".equals(mdd.getSfmr())){
                mddDtos.add(mdd);
            }
        }
        mav.addObject("hbmc", dtoById.getHbmc());
        mav.addObject("jsfslist", jsfsDtos);
        mav.addObject("yblxlist", yblxlist);
        mav.addObject("mddlist", mddDtos);
        mav.addObject("ywlx",BusTypeEnum.IMP_LOGISTICS_DISPATCH_IMG.getCode());
        FjcfbDto fjcfbDto = new FjcfbDto();
        fjcfbDto.setYwid(sjpdglDto.getSjpdid());
        fjcfbDto.setYwlx(BusTypeEnum.IMP_LOGISTICS_DISPATCH_IMG.getCode());
        List<FjcfbDto> list = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        mav.addObject("fjcfbDtos", list);
        SjwlxxDto sjwlxxDto = new SjwlxxDto();
        sjwlxxDto.setSjpdid(sjpdglDto.getSjpdid());
        List<SjwlxxDto> wlxxList = sjwlxxService.getWlxxList(sjwlxxDto);
        if (!CollectionUtils.isEmpty(wlxxList)){
            dtoById.setGldh(wlxxList.get(0).getGldh());
            dtoById.setWlfy(wlxxList.get(0).getWlfy());
            List<SjwltzDto> dtos = sjwltzService.getDtoListBySjpdid(wlxxList.get(0).getWlid());
            if (!CollectionUtils.isEmpty(dtos)){
                var hbmcs = "";
                var tzry_str = "";
                var tzrymc = "";
                for (SjwltzDto dto : dtos) {
                    tzry_str+=","+dto.getRyid();
                    hbmcs+=","+dto.getRymc();
                }
                if (dtos.size()>1){
                    tzrymc = dtos.get(0).getRymc()+"等"+dtos.size()+"人";
                }else{
                    tzrymc = dtos.get(0).getRymc();
                }
                mav.addObject("hbmcs", hbmcs.substring(1));
                mav.addObject("tzry_str", tzry_str.substring(1));
                mav.addObject("tzrymc", tzrymc);
            }
        }
        mav.addObject("sjpdglDto",dtoById);
        mav.addObject("pdbj","0");
        return mav;
    }


    /**
     * 	伙伴更改获取目的地
     * @return
     */
    @RequestMapping("/sjpdgl/pagedataGetMdd")
    @ResponseBody
    public Map<String, Object> pagedataGetMdd(SjpdglDto sjpdglDto) {
        Map<String, Object> map = new HashMap<>();
        List<JcsjDto> mddDtos = new ArrayList<>();
        if (StringUtil.isNotBlank(sjpdglDto.getHbmc())){
            mddDtos = sjxxService.getDetectionUnit(String.valueOf(sjpdglDto.getHbmc()));
        }else{
            List<JcsjDto> mddlist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
            //检测单位初始值支取默认检测单位
            for (JcsjDto mdd: mddlist) {
                if (StringUtil.isNotBlank(mdd.getSfmr()) && "1".equals(mdd.getSfmr())){
                    mddDtos.add(mdd);
                }
            }
        }
        map.put("list", mddDtos);
        return map;
    }

    /**
     * 关联伙伴功能跳转页面
     * @return
     */
    @RequestMapping("/sjpdgl/pagedataTz")
    public ModelAndView pagedataTz(SjhbxxDto sjhbxxDto) {
        return new  ModelAndView("wechat/sjpdgl/sjpdgl_partner");
    }
 /**
     * 	修改团单信息
     * @return
     */
    @RequestMapping("/minidataModTdxx")
    @ResponseBody
    public Map<String, Object> minidataModTdxx(SjwlxxDto sjwlxxDto,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        sjwlxxDto.setXgry(user.getYhid());
        try {
            boolean isSuccess = sjtdxxService.modSaveTdxx(sjwlxxDto);
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
     * 小程序获取送检信息是否派过单
     * @param sjpdglDto
     * @return
     */
    @RequestMapping("/sjpdgl/minidataGetSfpd")
    @ResponseBody
    public Map<String,Object> minidataGetSfpd(SjpdglDto sjpdglDto){
        Map<String, Object> map= new HashMap<>();
        SjpdglDto sjpdglDto_t = sjpdglService.getSfpd(sjpdglDto);
        if (sjpdglDto_t!=null){
            map.put("ispd",true);
        }else {
            map.put("ispd",false);
        }
        return map;
    }

}
