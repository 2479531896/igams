package com.matridx.igams.wechat.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.GrlbzdszDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.LbzdszDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.MQTypeEnum;
import com.matridx.igams.common.enums.RabbitEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IGrlbzdszService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.ILbzdszService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.RabbitUtils;
import com.matridx.igams.wechat.dao.entities.SjdwxxDto;
import com.matridx.igams.wechat.dao.entities.SjkzxxDto;
import com.matridx.igams.wechat.dao.entities.SjsyglDto;
import com.matridx.igams.wechat.dao.entities.SjsyglModel;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.service.svcinterface.IHbqxService;
import com.matridx.igams.wechat.service.svcinterface.ISjhbxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjkzxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjsyglService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author WYX
 * @version 1.0
 * @className SampleExperimentController
 * @description TODO
 * @date 10:15 2023/2/20
 **/
@Controller
@RequestMapping("/sampleExperiment")
public class SampleExperimentController extends BaseController {
    @Autowired
    private IGrlbzdszService grlbzdszService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    private ISjxxService sjxxService;
    @Autowired
    ISjsyglService sjsyglService;
    @Autowired
    private ILbzdszService lbzdszService;
    @Autowired
    private IJcsjService jcsjService;
    @Autowired(required=false)
    private AmqpTemplate amqpTempl;
    @Autowired
    private ISjkzxxService sjkzxxService;
    @Autowired
    private IXxglService xxglService;
    @Autowired
    private IHbqxService hbqxService;
    @Autowired
    private ISjhbxxService sjhbxxService;
//    Logger log = LoggerFactory.getLogger(SampleExperimentController.class);

    /**
     * 标本实验列表页面
     * @return
     */
    @RequestMapping(value="/sampleExperiment/pageListSpecimenExperimental")
    public ModelAndView pageListSpecimenExperimental(SjxxDto sjxxDto) {
        ModelAndView mav=new ModelAndView("wechat/sampleExperiment/specimenExperimental_list");
        mav.addObject("single_flag", sjxxDto.getSingle_flag());
        Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.DETECT_TYPE,BasicDataTypeEnum.DETECTION_UNIT});
        List<JcsjDto> detectlist = jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode());
        mav.addObject("detectlist", detectlist);//检测项目
        mav.addObject("detectionList", jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位
        User user = getLoginInfo();
        List<Map<String, String>> dwAndbjlist = sjxxService.getJsjcdwByjsid(user.getDqjs());
        if(dwAndbjlist!=null&&dwAndbjlist.size() > 0){
            if("1".equals(dwAndbjlist.get(0).get("dwxdbj"))) {
                String jcdwxz = "";
                for(int i=0; i<dwAndbjlist.size(); i++) {
                    if(dwAndbjlist.get(i).get("jcdw") != null) {
                        jcdwxz+=","+dwAndbjlist.get(i).get("jcdw");

                    }
                }
                if(StringUtil.isNotBlank(jcdwxz)) {
                    mav.addObject("jcdwxz", jcdwxz.substring(1));
                }
            }
        }

        GrlbzdszDto grlbzdszDto = new GrlbzdszDto();
        grlbzdszDto.setYhid(getLoginInfo().getYhid());
        grlbzdszDto.setYwid(sjxxDto.getYwlx());
        LbzdszDto lbzdszDto = new LbzdszDto();
        lbzdszDto.setYwid(sjxxDto.getYwlx());
        lbzdszDto.setYhid(user.getYhid());
        lbzdszDto.setJsid(user.getDqjs());
        List<LbzdszDto> choseList = grlbzdszService.getChoseList(grlbzdszDto);
        List<LbzdszDto> waitList = lbzdszService.getWaitList(lbzdszDto);
        String xszdlist = "";
        for (LbzdszDto lbzdszdto: choseList) {
            xszdlist = xszdlist+","+lbzdszdto.getXszd();
        }
        for (LbzdszDto lbzdszdto: waitList) {
            xszdlist = xszdlist+","+lbzdszdto.getXszd();
        }
        String limitColumns = "";
        if (StringUtil.isNotBlank(xszdlist)){
            limitColumns = "{'sjxxDto':'"+xszdlist.substring(1)+"'}";
        }
        mav.addObject("limitColumns",limitColumns);
        mav.addObject("lx",sjxxDto.getYwlx());
        mav.addObject("choseList", choseList);
        mav.addObject("waitList", waitList);
        return mav;
    }

    /**
     * 标本实验列表
     * @param
     * @return
     */
    @RequestMapping(value="/sampleExperiment/pageGetListSpecimenExperimental")
    @ResponseBody
    public Map<String, Object> pageGetListSpecimenExperimental(SjkzxxDto sjkzxxDto, HttpServletRequest request){
        Map<String, Object> map= new HashMap<>();
        User user=getLoginInfo();
        List<Map<String,String>> jcdwList=sjxxService.getJsjcdwByjsid(user.getDqjs());
        if(!CollectionUtils.isEmpty(jcdwList) && "1".equals(jcdwList.get(0).get("dwxdbj"))) {
            //判断是否显示个人清单
            if("1".equals(sjkzxxDto.getSingle_flag())) {
                List<String> userids= new ArrayList<>();
                if(user.getYhid()!=null) {
                    userids.add(user.getYhid());
                }
                if(user.getDdid()!=null) {
                    userids.add(user.getDdid());
                }
                if(user.getWechatid()!=null) {
                    userids.add(user.getWechatid());
                }
                if(userids.size()>0) {
                    sjkzxxDto.setUserids(userids);
                }
                //判断伙伴权限
                List<String> hbqxList = hbqxService.getHbidByYhid(user.getYhid());
                if(hbqxList!=null && hbqxList.size()>0) {
                    List<String> hbmcList=sjhbxxService.getHbmcByHbid(hbqxList);
                    if(hbmcList!=null  && hbmcList.size()>0) {
                        sjkzxxDto.setSjhbs(hbmcList);
                    }
                }
            }
        }
        List<SjkzxxDto> list = sjkzxxService.getPagedSpecimenExperimentalList(sjkzxxDto);
        map.put("total", sjkzxxDto.getTotalNumber());
        map.put("rows", list);
        return map;
    }

    /**
     * 文库实验列表页面
     * @return
     */
    @RequestMapping(value="/sampleExperiment/pageListSampleExperiment")
    public ModelAndView pageListInspection(SjsyglDto sjsyglDto) {
        ModelAndView mav=new ModelAndView("wechat/sampleExperiment/sample_experiment");
        mav.addObject("single_flag", sjsyglDto.getSingle_flag());
        User user=getLoginInfo();
        List<SjdwxxDto> sjdwxxlist=sjxxService.getSjdw();
        mav.addObject("sjdwxxlist", sjdwxxlist);
        Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.WECGATSAMPLE_TYPE,BasicDataTypeEnum.FIRST_SJXXKZ,
                BasicDataTypeEnum.SECOND_SJXXKZ,BasicDataTypeEnum.THIRD_SJXXKZ,BasicDataTypeEnum.FOURTH_SJXXKZ,BasicDataTypeEnum.DETECT_TYPE,BasicDataTypeEnum.SD_TYPE,
                BasicDataTypeEnum.DETECTION_UNIT,BasicDataTypeEnum.STAMP_TYPE,BasicDataTypeEnum.CLASSIFY,BasicDataTypeEnum.INSPECTION_DIVISION,BasicDataTypeEnum.RESEARCH_PROJECT});
        List<JcsjDto> detectlist = jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode());
        mav.addObject("samplelist", jclist.get(BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));//标本类型
        List<JcsjDto> kyxmList = jclist.get(BasicDataTypeEnum.RESEARCH_PROJECT.getCode());
        mav.addObject("kylist", kyxmList);//科研项目
        List<String> kylxList = new ArrayList<>();
        for (JcsjDto kyxm : kyxmList) {
            String kylx = StringUtil.isNotBlank(kyxm.getCskz1()) ? kyxm.getCskz1() : "其它";
            boolean isIn = false;
            for (String kylxmc : kylxList) {
                if (kylxmc.equals(kylx)){
                    isIn = true;
                    break;
                }
            }
            if (!isIn){
                kylxList.add(kylx);
            }
        }
        mav.addObject("kylxList", kylxList);//科研项目类型
        mav.addObject("cskz1List", jclist.get(BasicDataTypeEnum.FIRST_SJXXKZ.getCode()));
        mav.addObject("cskz2List", jclist.get(BasicDataTypeEnum.SECOND_SJXXKZ.getCode()));
        mav.addObject("cskz3List", jclist.get(BasicDataTypeEnum.THIRD_SJXXKZ.getCode()));
        mav.addObject("cskz4List", jclist.get(BasicDataTypeEnum.FOURTH_SJXXKZ.getCode()));
        mav.addObject("expressage", jclist.get(BasicDataTypeEnum.SD_TYPE.getCode()));//快递类型
        mav.addObject("stamplist", jclist.get(BasicDataTypeEnum.STAMP_TYPE.getCode()));//盖章类型
        mav.addObject("detectionList", jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位
        mav.addObject("sjhbflList", jclist.get(BasicDataTypeEnum.CLASSIFY.getCode()));//合作伙伴分类
        mav.addObject("sjqfList", jclist.get(BasicDataTypeEnum.INSPECTION_DIVISION.getCode()));//送检区分
        GrlbzdszDto grlbzdszDto = new GrlbzdszDto();
        grlbzdszDto.setYhid(getLoginInfo().getYhid());
        grlbzdszDto.setYwid("SAMPLE_EXPERIMENT");
        LbzdszDto lbzdszDto = new LbzdszDto();
        lbzdszDto.setYwid("SAMPLE_EXPERIMENT");
        lbzdszDto.setYhid(user.getYhid());
        lbzdszDto.setJsid(user.getDqjs());
        List<LbzdszDto> choseList = grlbzdszService.getChoseList(grlbzdszDto);
        List<LbzdszDto> waitList = lbzdszService.getWaitList(lbzdszDto);
//		List<String> zdList = lbzdszService.getSjxxZdList();//从列表字段设置表中获取默认设置为 1显示、0隐藏、3角色限制、9主键的SQL字段
        String xszdlist = "";
        for (LbzdszDto lbzdszdto: choseList) {
            xszdlist = xszdlist+","+lbzdszdto.getXszd();
        }
        for (LbzdszDto lbzdszdto: waitList) {
            xszdlist = xszdlist+","+lbzdszdto.getXszd();
        }
        String limitColumns = "";
        if (StringUtil.isNotBlank(xszdlist)){
            limitColumns = "{'sjsyglDto':'"+xszdlist.substring(1)+"'}";
        }
        String jcxmids = "";
        List<JcsjDto> jcxmlist = new ArrayList<>();
        if (!CollectionUtils.isEmpty(detectlist)&&!CollectionUtils.isEmpty(sjsyglDto.getJcxmids())){
            for (JcsjDto jcsjDto : detectlist) {
                for (String jcxmid : sjsyglDto.getJcxmids()) {
                    if (jcsjDto.getCsdm().equals(jcxmid)){
                        jcxmids = jcxmids+","+jcsjDto.getCsid();
                        jcxmlist.add(jcsjDto);
                    }
                }
            }
            if (jcxmids.length()>0){
                jcxmids = jcxmids.substring(1);
            }
        }
        if (!CollectionUtils.isEmpty(jcxmlist)){
            mav.addObject("detectlist", jcxmlist);//检测项目
        }else {
            mav.addObject("detectlist", detectlist);//检测项目
        }
        mav.addObject("jcxmids",jcxmids);
        mav.addObject("limitColumns",limitColumns);
        mav.addObject("choseList", choseList);
        mav.addObject("waitList", waitList);
        return mav;
    }
    /**
     * 文库实验列表
     * @param sjsyglDto
     * @return
     */
    @RequestMapping(value="/sampleExperiment/pageGetListSampleExperiment")
    @ResponseBody
    public Map<String, Object> pageGetListSampleExperiment(SjsyglDto sjsyglDto, HttpServletRequest request){
        Map<String, Object> map= new HashMap<>();
        User user=getLoginInfo();
        List<Map<String,String>> jcdwList = sjxxService.getJsjcdwByjsid(user.getDqjs());
        List<SjsyglDto> sjsyglDtos = new ArrayList<>();
        if(jcdwList!=null&&jcdwList.size() > 0){
            if("1".equals(jcdwList.get(0).get("dwxdbj"))) {
                List<String> jcdwlist = new ArrayList<>();
                for(int i=0; i<jcdwList.size(); i++) {
                    if(jcdwList.get(i).get("jcdw") != null) {
                        jcdwlist.add(jcdwList.get(i).get("jcdw"));
                    }
                }
                //判断是否显示个人清单
                if("1".equals(sjsyglDto.getSingle_flag())) {
                    List<String> userids= new ArrayList<>();
                    if(user.getYhid()!=null) {
                        userids.add(user.getYhid());
                    }
                    if(user.getDdid()!=null) {
                        userids.add(user.getDdid());
                    }
                    if(user.getWechatid()!=null) {
                        userids.add(user.getWechatid());
                    }
                    if(userids.size()>0) {
                        sjsyglDto.setUserids(userids);
                    }
                    //判断伙伴权限
                    List<String> hbqxList = hbqxService.getHbidByYhid(user.getYhid());
                    if(hbqxList!=null && hbqxList.size()>0) {
                        List<String> hbmcList=sjhbxxService.getHbmcByHbid(hbqxList);
                        if(hbmcList!=null  && hbmcList.size()>0) {
                            sjsyglDto.setSjhbs(hbmcList);
                        }
                    }
                }
                if(!CollectionUtils.isEmpty(jcdwlist)) {
                    sjsyglDto.setJcdwxz(jcdwlist);
                    sjsyglDtos = sjsyglService.getPagedDtoList(sjsyglDto);
                }
            }else {
                sjsyglDtos = sjsyglService.getPagedDtoList(sjsyglDto);
            }
        }else {
            sjsyglDtos = sjsyglService.getPagedDtoList(sjsyglDto);
        }

        map.put("total", sjsyglDto.getTotalNumber());
        map.put("rows", sjsyglDtos);
        return map;
    }
    /**
     * 标本实验列表调整操作
     * @param sjsyglDto
     * @return
     */
    @RequestMapping(value="/sampleExperiment/adjustBbsy")
    public ModelAndView adjustBbsy(SjsyglDto sjsyglDto) {
        ModelAndView mav=new ModelAndView("wechat/sjxx/bbsy_adjust");
        String ids="";
        List<SjsyglModel> sygllist = new ArrayList<>();
        if (!CollectionUtils.isEmpty(sjsyglDto.getIds()) && !CollectionUtils.isEmpty(sjsyglDto.getJcxmids())){
            sygllist = sjsyglService.getDtosBySjids(sjsyglDto);
        }
        if (!CollectionUtils.isEmpty(sygllist)){
            for (SjsyglModel sygl :sygllist){
                ids = ids + "," +sygl.getSyglid();
            }
        }else{
            for (String id :sjsyglDto.getIds()){
                ids = ids + "," +id;
            }
        }
        if (StringUtil.isNotBlank(ids)){
            ids = ids.substring(1);
        }
        String jcxm = "";
        if (!CollectionUtils.isEmpty(sjsyglDto.getJcxmids())){
            for (String jcxmid : sjsyglDto.getJcxmids()){
                jcxm = jcxm +","+ jcxmid;
            }
        }
        if (StringUtil.isNotBlank(jcxm)){
            jcxm = jcxm.substring(1);
        }
        mav.addObject("ids", ids);
        mav.addObject("jcxm",jcxm);
        mav.addObject("dwList", JSONObject.toJSONString(redisUtil.lgetDto(("List_matridx_jcsj:"+BasicDataTypeEnum.DETECTION_UNIT))) );
        return mav;
    }

    /**
     * 获取调整数据
     * @return
     */
    @RequestMapping(value="/sampleExperiment/pagedataAdjustInfo")
    @ResponseBody
    public Map<String, Object> pagedataAdjustInfo(SjsyglDto sjsyglDto){
        Map<String, Object> map= new HashMap<>();
        List<SjsyglDto> dtoList = sjsyglService.getDtoListByTempSyglids(sjsyglDto);
        if(dtoList!=null&&dtoList.size()>0){
            for(SjsyglDto sjsygl : dtoList){
                if(sjsygl == null)
                    continue;
                if(StringUtil.isNotBlank(sjsygl.getQysj())){
                    sjsygl.setSfqy("1");
                }
            }
        }
        map.put("rows",dtoList);
        return map;
    }

    /**
     * 获取调整数据
     * @return
     */
    @RequestMapping(value="/sampleExperiment/pagedataAdjustSave")
    @ResponseBody
    public Map<String, Object> adjustSaveSjxx(SjsyglDto sjsyglDto){
        Map<String, Object> map= new HashMap<>();
        User user=getLoginInfo();
        sjsyglDto.setXgry(user.getYhid());
        sjsyglService.adjustSaveSjxx(sjsyglDto);
        RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.BBSY_ADJ.getCode() + JSONObject.toJSONString(sjsyglDto));
        //查实验管理表数据，若某送检下所有实验管理的sfjs都为0，则将送检表的sfjs，jsrq, jsry改为null
        List<Map<String,String>> sjsyglDtos = sjsyglService.getDtosBySjxxFromSyglid(sjsyglDto);
        Map<String, List<Map<String,String>>> sjsyglMap = sjsyglDtos.stream() .collect(Collectors.groupingBy(item -> item.get("sjid").toString()));
        List<SjxxDto> sjxxDtoList = new ArrayList<>();
        for (String key: sjsyglMap.keySet()){
            boolean flag=true;
            if (!CollectionUtils.isEmpty(sjsyglMap.get(key))){
                for (Map<String, String> item : sjsyglMap.get(key)){
                    if (  item != null && StringUtil.isNotBlank(item.get("sfjs")) && !"0".equals(item.get("sfjs")))   {
                        flag=false;
                        break;
                    }
                }
                if (flag){
                    SjxxDto sjxxDto = new SjxxDto();
                    sjxxDto.setSjid(key);
                    sjxxDto.setXgry(user.getYhid());
                    sjxxDtoList.add(sjxxDto);
                }
            }
        }
        if (!CollectionUtils.isEmpty(sjxxDtoList)){
            sjxxService.updateSfjsInfo(sjxxDtoList);
            RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.SFJS_UPD.getCode() + JSONObject.toJSONString(sjxxDtoList));
        }
        map.put("status", "success");
        map.put("message",  xxglService.getModelById("ICOM00001").getXxnr());
        return map;
    }

    /**
     * 标本实验列表新增操作
     * @param sjsyglDto
     * @return
     */
    @RequestMapping(value="/sampleExperiment/addBbsy")
    public ModelAndView addBbsy(SjsyglDto sjsyglDto) {
        ModelAndView mav=new ModelAndView("wechat/sjxx/bbsy_add");
        List<SjsyglDto> sjsyglDtos=sjsyglService.getDtoList(sjsyglDto);
        mav.addObject("sjsyglDtos", sjsyglDtos);
        mav.addObject("json", JSON.toJSONString(sjsyglDtos));
        SjxxDto sjxxDto=new SjxxDto();
        sjxxDto.setSjid(sjsyglDto.getSjid());
        SjxxDto sjxxDto_t=sjxxService.getDto(sjxxDto);
        mav.addObject("sjxxDto", sjxxDto_t);
        mav.addObject("sjsyglDto", sjsyglDto);
        return mav;
    }

    /**
     * 新增保存
     * @return
     */
    @RequestMapping(value="/sampleExperiment/addSaveBbsy")
    @ResponseBody
    public Map<String, Object> addSaveBbsy(SjsyglDto sjsyglDto){
        Map<String, Object> map= new HashMap<>();
        List<SjsyglDto> list=(List<SjsyglDto>) JSON.parseArray(sjsyglDto.getBbsy_json(),SjsyglDto.class);
        User user=getLoginInfo();
        for (SjsyglDto dto : list) {
            dto.setSyglid(StringUtil.generateUUID());
            dto.setLrry(user.getYhid());
        }
        boolean isSuccess=sjsyglService.insertList(list);
        RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.SJSY_ADD.getCode() + JSONObject.toJSONString(list));
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }
}
