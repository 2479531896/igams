package com.matridx.igams.production.controller;

import com.matridx.igams.common.cloud.controller.BaseBasicController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.production.dao.entities.GcjlDto;
import com.matridx.igams.production.dao.entities.LysbjlDto;

import com.matridx.igams.production.service.svcinterface.IGcjlService;
import com.matridx.igams.production.service.svcinterface.ILysbjlService;
import com.matridx.igams.storehouse.dao.entities.LykcxxDto;
import com.matridx.igams.storehouse.dao.entities.LyrkxxDto;
import com.matridx.igams.storehouse.dao.entities.QyxxDto;
import com.matridx.igams.storehouse.dao.entities.ZjszDto;
import com.matridx.igams.storehouse.service.svcinterface.ILykcxxService;
import com.matridx.igams.storehouse.service.svcinterface.ILyrkxxService;
import com.matridx.igams.storehouse.service.svcinterface.IQyxxService;
import com.matridx.igams.storehouse.service.svcinterface.IZjszService;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author:JYK
 */
@Controller
@RequestMapping("/retention")
public class RetentionEquipmentController extends BaseBasicController {
    @Autowired
    private ILysbjlService lysbjlService;
    @Autowired
    private ILykcxxService lykcxxService;
    @Autowired
    private ILyrkxxService lyrkxxService;
    @Autowired
    private IQyxxService qyxxService;
    @Autowired
    private IGcjlService gcjlService;
    @Autowired
    private IXxglService xxglService;
    @Autowired
    private IFjcfbService fjcfbService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    IZjszService zjszService;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Override
    public String getPrefix(){
        return urlPrefix;
    }
    private final Logger log = LoggerFactory.getLogger(RetentionEquipmentController.class);
    /**
     * 设备留样列表页面
     */
    @RequestMapping("/retention/pageListRetention")
    public ModelAndView pageListRetention() {
        ModelAndView mav =new ModelAndView("production/retention/retentionEquipment_list");
        List<JcsjDto> sbbhList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DEVICE_NUMBER.getCode());
        mav.addObject("bhlblist",sbbhList);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 获取数据列表
     */
    @RequestMapping("/retention/pageGetListRetention")
    @ResponseBody
    public Map<String,Object> pageGetListRetention(LysbjlDto lysbjlDto) {
        Map<String, Object> map = new HashMap<>();
        List<LysbjlDto> lysbjlDtos=lysbjlService.getPagedDtoList(lysbjlDto);
        map.put("rows",lysbjlDtos);
        map.put("total",lysbjlDto.getTotalNumber());
        return map;
    }
    /**
     * 设备留样查看页面
     */
    @RequestMapping("/retention/viewRetention")
    public ModelAndView viewRetention(LysbjlDto lysbjlDto) {
        ModelAndView mav =new ModelAndView("production/retention/retentionEquipment_view");
        lysbjlDto=lysbjlService.getDtoById(lysbjlDto.getSbjlid());
        mav.addObject("lysbjlDto",lysbjlDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 留样库存列表页面
     */
    @RequestMapping("/retention/pageListSampleStock")
    public ModelAndView pageListSampleStock() {
        ModelAndView mav =new ModelAndView("production/retention/sampleStock_list");
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 获取留样库存列表数据
     */
    @RequestMapping("/retention/pageGetListSampleStock")
    @ResponseBody
    public Map<String,Object> pageGetListSampleStock(LykcxxDto lykcxxDto) {
        Map<String, Object> map = new HashMap<>();
        List<LykcxxDto> lykcxxDtos=lykcxxService.getPagedDtoList(lykcxxDto);
        map.put("rows",lykcxxDtos);
        map.put("total",lykcxxDto.getTotalNumber());
        return map;
    }
    /**
     * 留样库存查看页面
     */
    @RequestMapping("/retention/viewSampleStock")
    public ModelAndView viewSampleStock(LykcxxDto lykcxxDto) {
        ModelAndView mav =new ModelAndView("production/retention/sampleStock_view");
        lykcxxDto=lykcxxService.getDtoById(lykcxxDto.getLykcid());
        FjcfbDto fjcfbDto=new FjcfbDto();
        fjcfbDto.setYwid(lykcxxDto.getLykcid());
        fjcfbDto.setYwlx(BusTypeEnum.IMP_SAMPLING_SUMMARY.getCode());
        List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        mav.addObject("t_fjcfbDtos",fjcfbDtos);
        mav.addObject("lykcxxDto",lykcxxDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 获取留样库存查看取样数据
     */
    @RequestMapping("/retention/pagedataQySampleStock")
    @ResponseBody
    public Map<String,Object> pagedataQySampleStock(QyxxDto qyxxDto) {
        Map<String, Object> map = new HashMap<>();
        List<QyxxDto> list=qyxxService.getDtolistQySampleStock(qyxxDto);
        map.put("rows",list);
        return map;
    }
    /**
     * 获取留样入库查看观察记录
     */
    @RequestMapping("/retention/pagedataGcSampleStock")
    @ResponseBody
    public Map<String,Object> pagedataGcSampleStock(GcjlDto gcjlDto) {
        Map<String, Object> map = new HashMap<>();
        List<GcjlDto> list=gcjlService.getDtolistGcSampleStock(gcjlDto);
        map.put("rows",list);
        return map;
    }
    /**
     * 留样入库列表页面
     */
    @RequestMapping("/retention/pageListSampleWarehousing")
    public ModelAndView pageListSampleWarehousing() {
        ModelAndView mav =new ModelAndView("production/retention/sampleWarehousing_list");
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 获取留样入库列表数据
     */
    @RequestMapping("/retention/pageGetListSampleWarehousing")
    @ResponseBody
    public Map<String,Object> pageGetListSampleWarehousing(LyrkxxDto lyrkxxDto) {
        Map<String, Object> map = new HashMap<>();
        List<LyrkxxDto> lyrkxxDtos=lyrkxxService.getPagedDtoList(lyrkxxDto);
        map.put("rows",lyrkxxDtos);
        map.put("total",lyrkxxDto.getTotalNumber());
        return map;
    }
    /**
     * 留样入库查看页面
     */
    @RequestMapping("/retention/viewSampleWarehousing")
    public ModelAndView viewSampleWarehousing(LyrkxxDto lyrkxxDto) {
        ModelAndView mav =new ModelAndView("production/retention/sampleWarehousing_view");
        lyrkxxDto=lyrkxxService.getDtoById(lyrkxxDto.getLyrkid());
        mav.addObject("lyrkxxDto",lyrkxxDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 取样列表页面
     */
    @RequestMapping("/retention/pageListSampling")
    public ModelAndView pageListSampling() {
        ModelAndView mav =new ModelAndView("production/retention/sampling_list");
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 获取取样列表页面数据
     */
    @RequestMapping("/retention/pageGetListSampling")
    @ResponseBody
    public Map<String,Object> pageGetListSampling(QyxxDto qyxxDto) {
        Map<String, Object> map = new HashMap<>();
        List<QyxxDto> qyxxDtos=qyxxService.getPagedDtoList(qyxxDto);
        map.put("rows",qyxxDtos);
        map.put("total",qyxxDto.getTotalNumber());
        return map;
    }
    /**
     * 取样查看页面
     */
    @RequestMapping("/retention/viewSampling")
    public ModelAndView viewSampling(QyxxDto qyxxDto) {
        ModelAndView mav =new ModelAndView("production/retention/sampling_view");
        qyxxDto=qyxxService.getDtoById(qyxxDto.getQyid());
        FjcfbDto fjcfbDto=new FjcfbDto();
        fjcfbDto.setYwid(qyxxDto.getQyid());
        fjcfbDto.setYwlx(BusTypeEnum.IMP_SAMPLING_Q_SUMMARY.getCode());
        List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        mav.addObject("fjcfbDtos",fjcfbDtos);
        fjcfbDto.setYwlx(BusTypeEnum.IMP_SAMPLING_Q_SUMMARY_RESULT.getCode());
        List<FjcfbDto> t_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        mav.addObject("t_fjcfbDtos",t_fjcfbDtos);
        mav.addObject("qyxxDto",qyxxDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     *
     */
    @RequestMapping("/retention/pagedataViewSampling")
    public ModelAndView pagedataViewSampling(QyxxDto qyxxDto) {
        return viewSampling(qyxxDto);
    }
    /**
     * 留样库存-取样
     */
    @RequestMapping("/retention/samplingPage")
    public ModelAndView samplingPage(LykcxxDto lykcxxDto,HttpServletRequest request) {
        ModelAndView mav =new ModelAndView("production/retention/sampling_page");
        User loginInfo = getLoginInfo(request);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        lykcxxDto = lykcxxService.getDtoById(lykcxxDto.getLykcid());
        lykcxxDto.setQyrmc(loginInfo.getZsxm());
        lykcxxDto.setQyr(loginInfo.getYhid());
        lykcxxDto.setLrsj(sdf.format(date));
        mav.addObject("lykcxxDto", lykcxxDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 留样库存-取样保存
     */
    @RequestMapping(value = "/retention/samplingSavePage")
    @ResponseBody
    public Map<String,Object> saveSampling(QyxxDto qyxxDto, HttpServletRequest request) {
        User user = getLoginInfo(request);
        qyxxDto.setLrry(user.getYhid());
        Map<String,Object> map=new HashMap<>();
        boolean isSuccess;
        try {
            isSuccess = qyxxService.saveSampling(qyxxDto);
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
     * 取样列表-删除
     */
    @RequestMapping(value ="/retention/delSampling")
    @ResponseBody
    public Map<String,Object> delSampling(QyxxDto qyxxDto, HttpServletRequest request){
        User user = getLoginInfo(request);
        qyxxDto.setScry(user.getYhid());
        Map<String, Object> map = new HashMap<>();
        boolean isSuccess;
        try {
            isSuccess = qyxxService.delSampling(qyxxDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? (xxglService.getModelById("ICOM00003").getXxnr()) : xxglService.getModelById("ICOM00004").getXxnr());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00004").getXxnr());
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("message", xxglService.getModelById("ICOM00004").getXxnr());
        }
        return map;
    }
	
    /**
     * 观察记录
     */
    @RequestMapping("/retention/inspectionrecordsPage")
    public ModelAndView inspectionrecordsPage(GcjlDto gcjlDto) {
        ModelAndView mav =new ModelAndView("production/retention/inspection_records");
        gcjlDto.setFormAction("inspectionrecordsSavePage");
        List<JcsjDto> gcyqlist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.OBSERVATION_REQUIREMENTS.getCode());
        String gcyq="";
        String[] gcyqids=null;
        if (!CollectionUtils.isEmpty(gcyqlist)) {
            gcyq = gcyqlist.get(0).getCsid();
            gcyqids = gcyqlist.get(0).getCsmc().split(",");
        }
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("gcjlDto",gcjlDto);
        mav.addObject("gcyq",gcyq);
        mav.addObject("gcyqids",gcyqids);
        mav.addObject("ids",StringUtil.join(gcjlDto.getIds(),","));
        return mav;
    }
	   /**
     * 观察记录
     */
    @RequestMapping("/retention/inspectionrecordsSavePage")
    @ResponseBody
    public Map<String,Object> inspectionrecordsSavePage(GcjlDto gcjlDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        gcjlDto.setLrry(user.getYhid());
        boolean isSuccess = false;
        try {
            isSuccess = gcjlService.inspectionrecordsSavePage(gcjlDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
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
     * 留样库存留样小结页面
     */
    @RequestMapping("/retention/samplesummaryPage")
    public ModelAndView samplesummaryPage(LykcxxDto lykcxxDto) {
        ModelAndView mav =new ModelAndView("production/retention/sampling_summary");
        lykcxxDto=lykcxxService.getDtoById(lykcxxDto.getLykcid());
        lykcxxDto.setFormAction("samplesummarySavePage");
        FjcfbDto fjcfbDto=new FjcfbDto();
        fjcfbDto.setYwid(lykcxxDto.getLykcid());
        fjcfbDto.setYwlx(BusTypeEnum.IMP_SAMPLING_SUMMARY.getCode());
        List<FjcfbDto> dto = fjcfbService.getDtoList(fjcfbDto);
        mav.addObject("fjcfbDtos",dto);
        mav.addObject("ywlx", BusTypeEnum.IMP_SAMPLING_SUMMARY.getCode());
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("lykcxxDto",lykcxxDto);
        return mav;
    }
    /**
     * 更新留样库存留样小结
     */
    @RequestMapping("/retention/samplesummarySavePage")
    @ResponseBody
    public Map<String,Object> samplesummarySavePage(LykcxxDto lykcxxDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        lykcxxDto.setXgry(user.getYhid());
        boolean isSuccess = lykcxxService.updateDto(lykcxxDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }
    /**
     * 留样设备列表-记录
     */
    @RequestMapping("/retention/recordRetention")
    public ModelAndView recordRetention(LysbjlDto lysbjlDto) {
        ModelAndView mav =new ModelAndView("production/retention/retentionEquipment_record");
        List<JcsjDto> sbbhList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DEVICE_NUMBER.getCode());
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        Date date = new Date();
        lysbjlDto.setJlsj(sdf.format(date));
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("sbbhList", sbbhList);
        mav.addObject("formAction", "/recordSaveRetention");
        mav.addObject("lysbjlDto", lysbjlDto);
        return mav;
    }
    /**
     * 留样设备列表-记录保存
     */
    @RequestMapping(value ="/retention/recordSaveRetention")
    @ResponseBody
    public Map<String,Object> recordSaveRetention(LysbjlDto lysbjlDto, HttpServletRequest request){
        User user = getLoginInfo(request);
        lysbjlDto.setJlry(user.getYhid());
        Map<String, Object> map = new HashMap<>();
        boolean isSuccess = lysbjlService.saveRecordRetention(lysbjlDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }
    /**
     * 留样设备-修改
     */
    @RequestMapping("/retention/modRetention")
    public ModelAndView modRetention(LysbjlDto lysbjlDto) {
        ModelAndView mav =new ModelAndView("production/retention/retentionEquipment_record");
        List<JcsjDto> sbbhList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DEVICE_NUMBER.getCode());
        lysbjlDto = lysbjlService.getDtoById(lysbjlDto.getSbjlid());
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("sbbhList", sbbhList);
        mav.addObject("formAction", "/modSaveRetention");
        mav.addObject("lysbjlDto", lysbjlDto);
        return mav;
    }
    /**
     * 留样设备-修改保存
     */
    @RequestMapping(value ="/retention/modSaveRetention")
    @ResponseBody
    public Map<String,Object> modSaveRetention(LysbjlDto lysbjlDto, HttpServletRequest request){
        User user = getLoginInfo(request);
        lysbjlDto.setXgry(user.getYhid());
        Map<String, Object> map = new HashMap<>();
        boolean isSuccess;
        try {
            isSuccess = lysbjlService.update(lysbjlDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
        }  catch (Exception e) {
            map.put("status", "fail");
            map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
        }
        return map;
    }
    /**
     * 留样设备-删除
     */
    @RequestMapping(value ="/retention/delRetention")
    @ResponseBody
    public Map<String,Object> delRetention(LysbjlDto lysbjlDto, HttpServletRequest request){
        User user = getLoginInfo(request);
        lysbjlDto.setScry(user.getYhid());
        Map<String, Object> map = new HashMap<>();
        boolean isSuccess;
        try {
            isSuccess = lysbjlService.delete(lysbjlDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? (xxglService.getModelById("ICOM00003").getXxnr()) : xxglService.getModelById("ICOM00004").getXxnr());
        }  catch (Exception e) {
            map.put("status", "fail");
            map.put("message", xxglService.getModelById("ICOM00004").getXxnr());
        }
        return map;
    }
    /**
     * 取样列表-取样小结页面
     */
    @RequestMapping("/retention/summaryPage")
    public ModelAndView summaryPage(QyxxDto qyxxDto) {
        ModelAndView mav =new ModelAndView("production/retention/sampling_summaryPage");
        qyxxDto=qyxxService.getDtoById(qyxxDto.getQyid());
        qyxxDto.setFormAction("summarySavePage");
        FjcfbDto fjcfbDto=new FjcfbDto();
        fjcfbDto.setYwid(qyxxDto.getQyid());
        fjcfbDto.setYwlx(BusTypeEnum.IMP_SAMPLING_Q_SUMMARY.getCode());
        List<FjcfbDto> fjcfbDtos = fjcfbService.getDtoList(fjcfbDto);
        mav.addObject("fjcfbDtos",fjcfbDtos);
        fjcfbDto.setYwlx(BusTypeEnum.IMP_SAMPLING_Q_SUMMARY_RESULT.getCode());
        List<FjcfbDto> fjcfbDtos_t = fjcfbService.getDtoList(fjcfbDto);
        mav.addObject("fjcfbDtos_t",fjcfbDtos_t);
        mav.addObject("ywlx", BusTypeEnum.IMP_SAMPLING_Q_SUMMARY.getCode());
        mav.addObject("ywlx1", BusTypeEnum.IMP_SAMPLING_Q_SUMMARY_RESULT.getCode());
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("qyxxDto",qyxxDto);
        return mav;
    }
    /**
     * 取样列表-取样小结
     */
    @RequestMapping("/retention/summarySavePage")
    @ResponseBody
    public Map<String,Object> summarySavePage(QyxxDto qyxxDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        qyxxDto.setXgry(user.getYhid());
        boolean isSuccess;
        try {
            isSuccess = qyxxService.updateSummary(qyxxDto);
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
     * 留样库存列表-质检设置页面
     */
    @RequestMapping("/retention/qualitySetting")
    public ModelAndView qualitySetting(ZjszDto zjszDto,HttpServletRequest request) {
        ModelAndView mav =new ModelAndView("production/retention/sampleStock_qualitySetting");
        User user = getLoginInfo(request);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String data = format.format(new Date());
        zjszDto.setKssj(data);
        zjszDto.setJssj(data);
        zjszDto.setZjry(user.getYhid());
        zjszDto.setZjrymc(user.getZsxm());
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("zjszDto",zjszDto);
        return mav;
    }
    /**
     * 获取质检设置数据
     */
    @RequestMapping("/retention/pagedataQualitySetting")
    @ResponseBody
    public Map<String,Object> pagedataQualitySetting(ZjszDto zjszDto) {
        Map<String, Object> map = new HashMap<>();
        List<ZjszDto> dtoList = zjszService.getDtoList(zjszDto);
        map.put("rows",dtoList);
        map.put("total",zjszDto.getTotalNumber());
        return map;
    }
    /**
     * 新增质检设置数据
     */
    @RequestMapping("/retention/pagedataAddQualitySetting")
    @ResponseBody
    public Map<String,Object> pagedataAddQualitySetting(ZjszDto zjszDto,HttpServletRequest request) {
        User user = getLoginInfo(request);
        zjszDto.setLrry(user.getYhid());
        Map<String, Object> map = new HashMap<>();
        try {
            map = zjszService.insertQualitySetting(zjszDto);
        } catch (BusinessException e) {
            map.put("status","fail");
        }
        return map;
    }
    /**
     * 删除质检设置数据
     */
    @RequestMapping("/retention/pagedataDelQualitySetting")
    @ResponseBody
    public boolean pagedataDelQualitySetting(ZjszDto zjszDto,HttpServletRequest request) {
        User user = getLoginInfo(request);
        zjszDto.setScry(user.getYhid());
        boolean isSuccess = false;
        try {
            isSuccess = zjszService.delQualitySetting(zjszDto);
        } catch (BusinessException e) {
            log.error(e.getMsg());
        }
        return isSuccess;
    }
    /**
     * 留样设备统计
     */
    @RequestMapping("/retention/pageStatisticsRetention")
    public ModelAndView pageStatisticsRetention() {
        ModelAndView mav =new ModelAndView("production/retention/retentionEquipment_statistics");
        List<JcsjDto> bhlblist = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DEVICE_NUMBER.getCode());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        mav.addObject("jlsjend",format.format(calendar.getTime()));
        calendar.add(Calendar.MONTH,-1);
        mav.addObject("jlsjstart",format.format(calendar.getTime()));
        mav.addObject("bhlblist", bhlblist);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 获取留样设备统计数据
     */
    @RequestMapping("/retention/pagedataStatisticsRetention")
    @ResponseBody
    public Map<String,Object> pagedataStatisticsRetention(LysbjlDto lysbjlDto) {
        List<LysbjlDto> list = lysbjlService.getDtoListGroup(lysbjlDto);
        List<String> ids = list.stream().map(LysbjlDto::getSbid).distinct().collect(Collectors.toList());
        lysbjlDto.setIds(ids);
        return lysbjlService.getStatisticsRetention(lysbjlDto);
    }

    /**
    * @Description: 修改留样日期
    * @param lyrkxxDto
    * @return org.springframework.web.servlet.ModelAndView
    * @Author: 郭祥杰
    * @Date: 2024/9/5 13:57
    */
    @RequestMapping("/retention/modSampleWarehousing")
    public ModelAndView modSampleWarehousing(LyrkxxDto lyrkxxDto) {
        ModelAndView mav =new ModelAndView("production/retention/sampleStock_mod");
        mav.addObject("lyrkxxDto", lyrkxxDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * @Description: 留样日期修改保存
     * @param lyrkxxDto
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2024/9/5 13:57
     */
    @RequestMapping("/retention/modSaveSampleWarehousing")
    @ResponseBody
    public Map<String,Object> modSaveSampleWarehousing(LyrkxxDto lyrkxxDto,HttpServletRequest request) {
        User user = getLoginInfo(request);
        lyrkxxDto.setXgry(user.getYhid());
        boolean isSuccess = lyrkxxService.update(lyrkxxDto);
        Map<String, Object> map = new HashMap<>();
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }
}
