package com.matridx.igams.detection.molecule.controller;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.GrszDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.PersonalSettingEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IGrszService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxxDto;
import com.matridx.igams.detection.molecule.dao.entities.KzbglDto;
import com.matridx.igams.detection.molecule.dao.entities.KzbmxDto;
import com.matridx.igams.detection.molecule.service.svcinterface.*;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/detectlab")
public class DetectLabController extends BaseController {

    @Autowired
    IFzjcxxService fzjcxxService;
    @Autowired
    private IGrszService grszService;
    @Autowired
    IJcsjService jcsjService;
    @Autowired
    IFjcfbService fjcfbService;
    @Autowired
    IXxglService xxglService;
    @Autowired
    IHzxxService hzxxService;
    @Autowired
    IFzjcxmService fzjcxmService;
    @Autowired
    IKzbglService kzbglService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IKzbmxService kzbmxService;

    public static final String COLUMN_MERGE = "column_merge";
    private final Logger log = LoggerFactory.getLogger(DetectLabController.class);

    /**
     *增加一个实验列表页面
     */
    @RequestMapping("/detectlab/pageListDetectlab")
    public ModelAndView pageListDetectlab(FzjcxxDto fzjcxxDto) {
        ModelAndView mav = new ModelAndView("detection/detectLab/detectLab_list");
        List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
        if(jclxList.size()>0){
            for(JcsjDto jcsjDto : jclxList){
                if(fzjcxxDto.getJclx().equals(jcsjDto.getCsdm()))
                    fzjcxxDto.setJclx(jcsjDto.getCsid());
            }
        }
        mav.addObject("jclx",fzjcxxDto.getJclx());
        mav.addObject("jcdxlxlist", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.MOLECULAR_OBJECT.getCode())  );
        List<JcsjDto> fxm = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_ITEM.getCode());
        List<JcsjDto> zxm = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_SUBITEM.getCode());
        Iterator<JcsjDto> iterator = fxm.iterator();
        while ((iterator.hasNext())){
            JcsjDto next = iterator.next();
            System.out.println(next.getFcsid());
            if(!(fzjcxxDto.getJclx().equals(next.getFcsid()))){
                iterator.remove();
            }
        }
        List<JcsjDto> zxmlist =new ArrayList<>();
        for (JcsjDto jcsjDto : zxm) {
            for (JcsjDto dto : fxm) {
                if(dto.getCsid().equals(jcsjDto.getFcsid())){
                    zxmlist.add(jcsjDto);
                }
            }
        }
        mav.addObject("zxmlist", zxmlist );
        return mav;
    }

    /**
     * 获取实验列表数据
     */
    @RequestMapping(value = "/detectlab/pageGetListDetectlab")
    @ResponseBody
    public Map<String, Object> pageGetListDetectlab(FzjcxxDto fzjcxxDto) {
        Map<String, Object> map = new HashMap<>();
        DBEncrypt dbEncrypt=new DBEncrypt();
        if(StringUtils.isNotBlank(fzjcxxDto.getZjh()))
            fzjcxxDto.setZjh(dbEncrypt.eCode(fzjcxxDto.getZjh()));
        List<FzjcxxDto> fzjcxxlist = fzjcxxService.getPagedDetectlab(fzjcxxDto);
        //对列表的
        if(fzjcxxlist!=null&&fzjcxxlist.size()>0){
            for(FzjcxxDto dto:fzjcxxlist){
                String zjh = "";
                if(StringUtil.isNotBlank(dto.getZjh())){
                    try {
                        zjh = dbEncrypt.dCode(dto.getZjh());
                    } catch (Exception e) {
                        log.error("证件号解密失败！"+e.getMessage());
                    }
                }
                dto.setZjh(zjh);
            }
        }
        map.put("total",fzjcxxDto.getTotalNumber());
        map.put("rows",fzjcxxlist);
        return map;
    }

    /**
     * 点击实验跳转页面
     */
    @RequestMapping(value="/detectlab/modDetectlab")
    public ModelAndView Detection(FzjcxxDto fzjcxxDto) {
        ModelAndView mav=new ModelAndView("detection/covid/covid_detection");
        List<FzjcxxDto> fzjcxxDtos = fzjcxxService.checkJcxm(fzjcxxDto);
        FzjcxxDto fzjcxxDto_t = fzjcxxDtos.get(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date();
        fzjcxxDto_t.setSysj(sdf.format(date));
        fzjcxxDto_t.setIds(fzjcxxDto.getIds());
        mav.addObject("fzjcxxDto",fzjcxxDto_t);
        return mav;
    }
	
	 /**
     * 点击检测跳转页面前校验所选数据检测项目是否相同
     */
    @RequestMapping(value="/detectlab/pagedataCheckJcxm")
    @ResponseBody
    public boolean checkJcxm(FzjcxxDto fzjcxxDto) {
        List<FzjcxxDto> jcxmlist=fzjcxxService.checkJcxm(fzjcxxDto);
        return jcxmlist == null || jcxmlist.size() <= 1;
    }

    /**
     *增加一个扩增板页面
     */
    @RequestMapping("/kzbinfo/pageListKzb")
    public ModelAndView pageListKzb() {
        return new ModelAndView("detection/detectLab/kzb_list");
    }

    /**
     * 获取实验列表数据
     */
    @RequestMapping(value = "/kzbinfo/pageGetListKzb")
    @ResponseBody
    public Map<String, Object> getPageKzb(KzbglDto kzbglDto, HttpServletRequest request) {
        Map<String,Object> map= new HashMap<>();
        List<KzbglDto> kzblist = new ArrayList<>();
        User user = getLoginInfo(request);
        List<Map<String,String>> jcdwList = kzbglService.getJsjcdwByjsid(user.getDqjs());
        if (jcdwList != null && jcdwList.size()>0){
            if("1".equals(jcdwList.get(0).get("dwxdbj"))) {
                List<String> strList=new ArrayList<>();
                for (Map<String, String> stringStringMap : jcdwList) {
                    if (stringStringMap.get("jcdw") != null) {
                        strList.add(stringStringMap.get("jcdw"));
                    }
                }
                if(strList.size()>0) {
                    kzbglDto.setJcdwxz(strList);
                    kzblist = kzbglService.getPagedDtoList(kzbglDto);
                }
            }else {
                kzblist = kzbglService.getPagedDtoList(kzbglDto);
            }
        }else {
        	kzblist = kzbglService.getPagedDtoList(kzbglDto);
        }
        map.put("total",kzbglDto.getTotalNumber());
        map.put("rows",kzblist);
        return map;
    }

    //新增扩增板
    @RequestMapping("/kzbinfo/addKzb")
    public ModelAndView addKzb(KzbglDto kzbglDto, HttpServletRequest request) {
        User user = getLoginInfo(request);
        GrszDto grszDto = new GrszDto();
        grszDto.setYhid(user.getYhid());
        grszDto.setSzlbs(new String[]{PersonalSettingEnum. KZBGL_SJDW.getCode(),PersonalSettingEnum.REAGENT_KZ_SETTINGS.getCode(),PersonalSettingEnum.REAGENT_TQ_SETTINGS.getCode()});
        Map<String, GrszDto> grszDtoMap = grszService.selectGrszMapByYhidAndSzlb(grszDto);
        //查个人设置检查单位
        JcsjDto jcsjDto = new JcsjDto();
        if (grszDtoMap.get(PersonalSettingEnum. KZBGL_SJDW.getCode()) != null) {
            jcsjDto = jcsjService.getDtoById(grszDtoMap.get(PersonalSettingEnum. KZBGL_SJDW.getCode()).getSzz());
        }
        //扩增试剂批号个人设置
        if (grszDtoMap.get(PersonalSettingEnum. REAGENT_KZ_SETTINGS.getCode()) != null) {
            kzbglDto.setKzsjph(grszDtoMap.get(PersonalSettingEnum. REAGENT_KZ_SETTINGS.getCode()).getSzz());
        }
        //提取试剂批号个人设置
        if (grszDtoMap.get(PersonalSettingEnum. REAGENT_TQ_SETTINGS.getCode()) != null) {
            kzbglDto.setTqsjph(grszDtoMap.get(PersonalSettingEnum. REAGENT_TQ_SETTINGS.getCode()).getSzz());
        }

        ModelAndView mav = new ModelAndView("detection/detectLab/kzb_edit");
        kzbglDto.setDqjs(user.getDqjs());
        kzbglDto.setFormAction("addSaveKzb");
        //获取检测单位基础数据
        List<JcsjDto> jcdwList = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.DETECTION_UNIT.getCode());
        String kzbh = kzbglService.generateKzbh(kzbglDto);
        kzbglDto.setKzbh(kzbh);
        mav.addObject("kzbglDto",kzbglDto);
        mav.addObject("jcdwList",jcdwList);

        mav.addObject("mrjcdw", jcsjDto);
        mav.addObject("sjxsbj1", "1");
        mav.addObject("sjxsbj2", "1");
        return mav;
    }

    //新增扩增板
    @RequestMapping(value = "/kzbinfo/addSaveKzb")
    @ResponseBody
    public Map<String, Object> addSaveKzb(KzbglDto kzbglDto, HttpServletRequest request) {
        Map<String,Object> map= new HashMap<>();
        User user = getLoginInfo(request);
        kzbglDto.setLrry(user.getYhid());

        GrszDto grszDto = new GrszDto();
        //检测单位个人设置
        grszDto.setSzlbs(new String[]{PersonalSettingEnum.KZBGL_SJDW.getCode(),PersonalSettingEnum.REAGENT_KZ_SETTINGS.getCode(),PersonalSettingEnum.REAGENT_TQ_SETTINGS.getCode()});
        Map<String, GrszDto> grszDtoMap = grszService.selectGrszMapByYhidAndSzlb(grszDto);
        grszDto.setSzlb(PersonalSettingEnum. KZBGL_SJDW.getCode());
        grszDto.setYhid(user.getYhid());
        GrszDto grszxx = grszDtoMap.get(PersonalSettingEnum.KZBGL_SJDW.getCode());
        if (grszxx == null) {
            grszDto.setSzz(kzbglDto.getJcdw());
            grszDto.setLrry(user.getYhid());
            grszService.insertDto(grszDto);
        } else {
            if (grszxx.getSzz()==null || !grszxx.getSzz().equals(kzbglDto.getJcdw())) {
                grszDto.setSzz(kzbglDto.getJcdw());
                grszService.updateByYhidAndSzlb(grszDto);
            }
        }
        //扩增试剂批号个人设置
        grszDto.setSzlb(PersonalSettingEnum.REAGENT_KZ_SETTINGS.getCode());
        GrszDto grszxx_o = grszDtoMap.get(PersonalSettingEnum.REAGENT_KZ_SETTINGS.getCode());
        if (grszxx_o == null) {
            grszDto.setSzz(kzbglDto.getKzsjph());
            grszDto.setLrry(user.getYhid());
            grszService.insertDto(grszDto);
        } else {
            if (grszxx_o.getSzz()==null ||!grszxx_o.getSzz().equals(kzbglDto.getKzsjph())) {
                grszDto.setSzz(kzbglDto.getKzsjph());
                grszService.updateByYhidAndSzlb(grszDto);
            }
        }
        //提取试剂批号个人设置
        grszDto.setSzlb(PersonalSettingEnum.REAGENT_TQ_SETTINGS.getCode());
        GrszDto grszxx_t = grszDtoMap.get(PersonalSettingEnum.REAGENT_TQ_SETTINGS.getCode());
        if (grszxx_t == null) {
            grszDto.setSzz(kzbglDto.getTqsjph());
            grszDto.setLrry(user.getYhid());
            grszService.insertDto(grszDto);
        } else {
            if (grszxx_t.getSzz()==null ||!grszxx_t.getSzz().equals(kzbglDto.getTqsjph())) {
                grszDto.setSzz(kzbglDto.getTqsjph());
                grszService.updateByYhidAndSzlb(grszDto);
            }
        }

        List<String> notExitsYbbhs = kzbglService.exitYbbh(kzbglDto);

        boolean isok;
        try {
	        if (kzbglDto.getYbbhs().length<=0){
	            map.put("status", "fail");
	            map.put("message", "保存失败!标本编号未填写!");
	            return map;
	        }else if (notExitsYbbhs == null || notExitsYbbhs.size() < 1 ) {
	            isok = kzbglService.insertKzbmx(kzbglDto);
	            map.put("status",isok?"success":"fail");
	            map.put("message",isok?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
	            return map;
	        } else {
	            map.put("status", "fail");
	            map.put("message", "保存失败!标本编号:" + String.join(",", notExitsYbbhs) + "不存在!请修改!");
	            map.put("notExitsYbbhs", notExitsYbbhs);
	            return map;
	        }
        }catch(Exception e) {
        	map.put("status","fail");
            map.put("message",xxglService.getModelById("ICOM00002").getXxnr());
            return map;
        }
    }

    /**
     *修改一个扩增板页面
     */
    @RequestMapping("/kzbinfo/modKzb")
    public ModelAndView modKzb(KzbglDto kzbglDto, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("detection/detectLab/kzb_edit");
        User user = getLoginInfo(request);
        //获取检测单位基础数据
        List<JcsjDto> jcdwList = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.DETECTION_UNIT.getCode());
        kzbglDto = kzbglService.getDto(kzbglDto);
        kzbglDto.setFormAction("modSaveKzb");
        kzbglDto.setXgry(user.getYhid());
        mav.addObject("kzbglDto",kzbglDto);
        mav.addObject("jcdwList",jcdwList);

        GrszDto grszDto = new GrszDto();
        grszDto.setYhid(user.getYhid());
        grszDto.setSzlbs(new String[]{PersonalSettingEnum.REAGENT_KZ_SETTINGS.getCode(),PersonalSettingEnum.REAGENT_TQ_SETTINGS.getCode()});
        Map<String, GrszDto> grszDtoMap = grszService.selectGrszMapByYhidAndSzlb(grszDto);
        GrszDto grszxx_o = grszDtoMap.get(PersonalSettingEnum.REAGENT_KZ_SETTINGS.getCode());
        GrszDto grszxx_t = grszDtoMap.get(PersonalSettingEnum.REAGENT_TQ_SETTINGS.getCode());
        if(StringUtil.isBlank(kzbglDto.getKzsjph()) && grszxx_o!=null) {
            kzbglDto.setKzsjph(grszxx_o.getSzz());
            mav.addObject("sjxsbj1", "1");
        }else{
            mav.addObject("sjxsbj1", "0");
        }
        if(StringUtil.isBlank(kzbglDto.getTqsjph()) && grszxx_t!=null) {
            kzbglDto.setTqsjph(grszxx_t.getSzz());
            mav.addObject("sjxsbj2", "1");
        }else{
            mav.addObject("sjxsbj2", "0");
        }
        return mav;
    }

    //前端页面ajax请求获取扩增板明细数据
    @RequestMapping(value = "/kzbinfo/pagedataKzbmxList")
    @ResponseBody
    public Map<String, Object> getKzbmxList(KzbglDto kzbglDto) {
        Map<String,Object> map= new HashMap<>();
        List<KzbmxDto> kzbmxlist = kzbmxService.getKzbmxListByKzbid(kzbglDto.getKzbid());
        map.put("kzbmxlist",kzbmxlist);
        return map;
    }

    /**
     * 修改扩增板数据
     */
    @RequestMapping(value = "/kzbinfo/modSaveKzb")
    @ResponseBody
    public Map<String, Object> modSaveKzb(KzbglDto kzbglDto, HttpServletRequest request) {
        Map<String,Object> map= new HashMap<>();
        User user = getLoginInfo(request);
        kzbglDto.setLrry(user.getYhid());
        kzbglDto.setXgry(user.getYhid());
        kzbglDto.setScry(user.getYhid());

        GrszDto grszDto = new GrszDto();
        grszDto.setSzlbs(new String[]{PersonalSettingEnum.KZBGL_SJDW.getCode(),PersonalSettingEnum.REAGENT_KZ_SETTINGS.getCode(),PersonalSettingEnum.REAGENT_TQ_SETTINGS.getCode()});
        grszDto.setYhid(user.getYhid());
        Map<String, GrszDto> grszDtoMap = grszService.selectGrszMapByYhidAndSzlb(grszDto);
        grszDto.setSzlb(PersonalSettingEnum. KZBGL_SJDW.getCode());
        GrszDto grszxx = grszDtoMap.get(PersonalSettingEnum.KZBGL_SJDW.getCode());
        if (grszxx == null) {
            grszDto.setSzz(kzbglDto.getJcdw());
            grszDto.setLrry(user.getYhid());
            grszService.insertDto(grszDto);
        } else {
            if (grszxx.getSzz()==null  || !grszxx.getSzz().equals(kzbglDto.getJcdw())) {
                grszDto.setSzz(kzbglDto.getJcdw());
                grszService.updateByYhidAndSzlb(grszDto);
            }
        }
        //扩增试剂批号个人设置
        grszDto.setSzlb(PersonalSettingEnum.REAGENT_KZ_SETTINGS.getCode());
        GrszDto grszxx_o = grszDtoMap.get(PersonalSettingEnum.REAGENT_KZ_SETTINGS.getCode());
        if (grszxx_o == null) {
            grszDto.setSzz(kzbglDto.getKzsjph());
            grszDto.setLrry(user.getYhid());
            grszService.insertDto(grszDto);
        } else {
            if (grszxx_o.getSzz()==null || !grszxx_o.getSzz().equals(kzbglDto.getKzsjph())) {
                grszDto.setSzz(kzbglDto.getKzsjph());
                grszService.updateByYhidAndSzlb(grszDto);
            }
        }
        //提取试剂批号个人设置
        grszDto.setSzlb(PersonalSettingEnum.REAGENT_TQ_SETTINGS.getCode());
        GrszDto grszxx_t = grszDtoMap.get(PersonalSettingEnum.REAGENT_TQ_SETTINGS.getCode());
        if (grszxx_t == null) {
            grszDto.setSzz(kzbglDto.getTqsjph());
            grszDto.setLrry(user.getYhid());
            grszService.insertDto(grszDto);
        } else {
            if (grszxx_t.getSzz()==null || !grszxx_t.getSzz().equals(kzbglDto.getTqsjph())) {
                grszDto.setSzz(kzbglDto.getTqsjph());
                grszService.updateByYhidAndSzlb(grszDto);
            }
        }

        //区分出不存在和存在的样本编号
        List<String> notExitsYbbhs = kzbglService.exitYbbh(kzbglDto);
        
        boolean isok;
        if (kzbglDto.getYbbhs().length<=0){
            map.put("status", "fail");
            map.put("message", "保存失败!标本编号未填写!");
            return map;
        }else if (notExitsYbbhs == null || notExitsYbbhs.size() < 1 ) {
            isok = kzbglService.updateData(kzbglDto);
            map.put("status",isok?"success":"fail");
            map.put("message",isok?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
            return map;
        } else {
            map.put("status", "fail");
            map.put("message", "保存失败!标本编号:" + String.join(",", notExitsYbbhs) + "不存在!请修改!");
            map.put("notExitsYbbhs", notExitsYbbhs);
            return map;
        }
    }

    /**
     * 删除消息
     */
    @RequestMapping("/kzbinfo/delKzb")
    @ResponseBody
    public Map<String,Object> delKzb(KzbglDto kzbglDto, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        kzbglDto.setScry(user.getYhid());
        boolean isSuccess = kzbglService.deleteData(kzbglDto);
        map.put("status",isSuccess?"success":"fail");
        map.put("message",isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
        return map;
    }

    /**
     *修改一个扩增板页面
     */
    @RequestMapping("/kzbinfo/viewKzb")
    public ModelAndView viewKzb(KzbglDto kzbglDto) {
        ModelAndView mav = new ModelAndView("detection/detectLab/kzb_view");
        kzbglDto = kzbglService.getDto(kzbglDto);
        mav.addObject("kzbglDto",kzbglDto);
        return mav;
    }

    /**
     * 扩增布板生成
     */
    @RequestMapping(value = "/kzbinfo/generateLayoutKzb", method = RequestMethod.GET)
    @ResponseBody
    public void generateLayoutKzb(KzbmxDto kzbmxDto, HttpServletResponse response) {
        List<KzbmxDto> kzbmxListByKzbid = kzbmxService.getKzbmxListByKzbid(kzbmxDto.getKzbid());
        String syh="";
        String kzbh="new";
        String kzsjph="";
        String tqsjph="";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date=new Date();
        String createdate = sdf.format(date);
        // 表头数据
        List<Object> head = Arrays.asList("","1","2","3","4","5","6","7","8","9","10","11","12","","Well","Sample");
        // 将数据汇总
        List<List<Object>> sheetDataList = new ArrayList<>();
        sheetDataList.add(head);
        int i = 1;
        char well='A';
        int wellNumber=1;
        for (char c2 = 'A'; c2 < 'I'; c2++, i++) {
            List<Object> object = new ArrayList<>();
            object.add(c2);
            for(int j=1;j<=12;j++){
                boolean flag=true;
                if(kzbmxListByKzbid!=null&&kzbmxListByKzbid.size()>0){
                    for(KzbmxDto kzbmxDto_t:kzbmxListByKzbid){
                        if(String.valueOf(i).equals(kzbmxDto_t.getHs())&&String.valueOf(j).equals(kzbmxDto_t.getLs())){
                            String[] split = kzbmxDto_t.getSyh().split("-");
                            if(StringUtil.isNotBlank(split[0])){
                                syh=split[0];
                            }
                            if(StringUtil.isNotBlank(kzbmxDto_t.getKzbh())){
                                kzbh=kzbmxDto_t.getKzbh();
                            }
                            if(StringUtil.isNotBlank(kzbmxDto_t.getKzsjph())){
                                kzsjph=kzbmxDto_t.getKzsjph();
                            }
                            if(StringUtil.isNotBlank(kzbmxDto_t.getTqsjph())){
                                tqsjph=kzbmxDto_t.getTqsjph();
                            }
                            object.add(kzbmxDto_t.getSyh());
                            flag=false;
                        }
                    }
                }
                if(flag){
                    object.add("");
                }
            }
            object.add("");
            object.add(well+String.format("%02d",i));
            if(kzbmxListByKzbid!=null&&kzbmxListByKzbid.size()>0){
                for(KzbmxDto kzbmxDto_t:kzbmxListByKzbid){
                    if(String.valueOf(wellNumber).equals(kzbmxDto_t.getHs())&&String.valueOf(i).equals(kzbmxDto_t.getLs())){
                        object.add(kzbmxDto_t.getSyh());
                        break;
                    }
                }
            }
            sheetDataList.add(object);
        }
        for(int xh=9;xh<97;xh++){
            if(i==13){
                i=1;
                well++;
                wellNumber++;
            }
            String cellSyh="";
            if(kzbmxListByKzbid!=null&&kzbmxListByKzbid.size()>0){
                for(KzbmxDto kzbmxDto_t:kzbmxListByKzbid){
                    if(String.valueOf(wellNumber).equals(kzbmxDto_t.getHs())&&String.valueOf(i).equals(kzbmxDto_t.getLs())){
                        cellSyh=kzbmxDto_t.getSyh();
                        break;
                    }
                }
            }
            if(xh==9){
                List<Object> row = Arrays.asList("","1-1",COLUMN_MERGE,"1-2",COLUMN_MERGE,"1-3",COLUMN_MERGE,"1-4",COLUMN_MERGE,"1-5",COLUMN_MERGE,"1-6",COLUMN_MERGE,"",well+String.format("%02d",i),cellSyh);
                sheetDataList.add(row);
            }else if(xh==10){
                List<Object> row = Arrays.asList("","",COLUMN_MERGE,COLUMN_MERGE,COLUMN_MERGE,"",COLUMN_MERGE,COLUMN_MERGE,COLUMN_MERGE,"",COLUMN_MERGE,COLUMN_MERGE,COLUMN_MERGE,"",well+String.format("%02d",i),cellSyh);
                sheetDataList.add(row);
            }else if(xh==11){
                List<Object> row = Arrays.asList("","","","","","","","","","","","","","",well+String.format("%02d",i),cellSyh);
                sheetDataList.add(row);
            }else if(xh==12){
                List<Object> row = Arrays.asList("","实验号:"+syh+"-","","","文件名称:"+createdate,COLUMN_MERGE,"","扩增仪:","","操作者/日期:",COLUMN_MERGE,"","","",well+String.format("%02d",i),cellSyh);
                sheetDataList.add(row);
            }else if(xh==13){
                List<Object> row = Arrays.asList("","","","","","","","","","","","","","",well+String.format("%02d",i),cellSyh);
                sheetDataList.add(row);
            }else if(xh==14){
                List<Object> row = Arrays.asList("","提取试剂","□达安","□之江","提取试剂批号:",tqsjph,"","提取操作者/日期：",COLUMN_MERGE,"","","","","",well+String.format("%02d",i),cellSyh);
                sheetDataList.add(row);
            }else if(xh==15){
                List<Object> row = Arrays.asList("","扩增试剂","□达安","□之江","扩增试剂批号:",kzsjph,"","扩增操作者/日期：",COLUMN_MERGE,"","","","","",well+String.format("%02d",i),cellSyh);
                sheetDataList.add(row);
            }else if(xh==16){
                List<Object> row = Arrays.asList("","","","","","","","","","","","","","",well+String.format("%02d",i),cellSyh);
                sheetDataList.add(row);
            }else if(xh==17){
                List<Object> row = Arrays.asList("","质控","阴性","□在控","□失控","","","弱阳性","ORF：","","N:","","","",well+String.format("%02d",i),cellSyh);
                sheetDataList.add(row);
            }else if(xh==18){
                List<Object> row = Arrays.asList("","异常情况说明：","","","","","","","","","","","","",well+String.format("%02d",i),cellSyh);
                sheetDataList.add(row);
            }else{
                List<Object> row=Arrays.asList("","","","","","","","","","","","","","",well+String.format("%02d",i),cellSyh);
                sheetDataList.add(row);
            }
            i++;
        }
        // 导出数据
        kzbglService.exportKzb(response,null,createdate,kzbh,sheetDataList,null);
    }
}