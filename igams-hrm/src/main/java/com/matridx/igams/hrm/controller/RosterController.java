package com.matridx.igams.hrm.controller;

import com.matridx.igams.common.cloud.controller.BaseBasicController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.GzglDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.post.ICommonDao;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IGzglService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.hrm.dao.entities.GrksglDto;
import com.matridx.igams.hrm.dao.entities.JkdaDto;
import com.matridx.igams.hrm.dao.entities.PxglDto;
import com.matridx.igams.hrm.dao.entities.YghmcDto;
import com.matridx.igams.hrm.dao.entities.YghtxxDto;
import com.matridx.igams.hrm.dao.entities.YglzxxDto;
import com.matridx.igams.hrm.service.svcinterface.IGrksglService;
import com.matridx.igams.hrm.service.svcinterface.IJkdaService;
import com.matridx.igams.hrm.service.svcinterface.IPxglService;
import com.matridx.igams.hrm.service.svcinterface.IYghmcService;
import com.matridx.igams.hrm.service.svcinterface.IYghtxxService;
import com.matridx.igams.hrm.service.svcinterface.IYglzxxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author:JYK
 */
@Controller
@RequestMapping("/roster")
public class RosterController extends BaseBasicController {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    private IYghmcService yghmcService;
    @Autowired
    private ICommonDao commonDao;
    @Autowired
    private IXxglService xxglService;
    @Autowired
    private IGrksglService grksglService;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Autowired
    IYglzxxService yglzxxService;
    @Autowired
    IYghtxxService yghtxxService;
    @Autowired
    IJcsjService jcsjService;
    @Autowired
    IPxglService pxglService;
    @Autowired
    IGzglService gzglService;
    @Autowired
    IJkdaService jkdaService;
    @Override
    public String getPrefix(){
        return urlPrefix;
    }
    /**
     * 花名册页面
     */
    @RequestMapping("/roster/pageListRoster")
    public ModelAndView pageListRoster(YghmcDto yghmcDto){
        ModelAndView mav = new ModelAndView("train/roster/roster_list");
        mav.addObject("pxlblist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.TRAINCATEGORY.getCode()));//培训类别
        mav.addObject("pxzlblist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.SUBCLASSTRAINCATEGORY.getCode()));//培训子类别
        mav.addObject("ssgslist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.AFFILIATED_COMPANY.getCode()));//培训子类别
        List<YghmcDto> bmlist=yghmcService.getAllRosterBm(yghmcDto);
        Map<String,Object> fls=yghmcService.getScreenClassfy();
        mav.addObject("bmlist", bmlist);
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("fl",fls);
        return mav;
    }

    /**
     * 获取花名册数据
     */
    @RequestMapping("/roster/pageGetListRoster")
    @ResponseBody
    public Map<String,Object> pageGetListRoster(YghmcDto yghmcDto){
        Map<String,Object> map = new HashMap<>();
        List<YghmcDto> yghmcDtoList=yghmcService.getPagedDtoList(yghmcDto);
        map.put("rows", yghmcDtoList);
        map.put("total", yghmcDto.getTotalNumber());
        return map;
    }
    /**
     * 花名册查看
     */
    @RequestMapping("/roster/viewRoster")
    public ModelAndView viewRoster(YghmcDto yghmcDto,User user){
        ModelAndView mav = new ModelAndView("train/roster/roster_view");
        user.setYhid(yghmcDto.getYhid());
        user=commonDao.getUserInfoById(user);
        yghmcDto=yghmcService.getDtoById(yghmcDto.getYghmcid());
        mav.addObject("ssgslist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.AFFILIATED_COMPANY.getCode()));
        mav.addObject("pxfslist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.TRAINING_METHODS.getCode()));
        mav.addObject("pxlblist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.TRAINCATEGORY.getCode()));//培训类别
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("yghmcDto", yghmcDto);
        mav.addObject("xtyhDto", user);
        return mav;
    }
    /**
     * 获取员工考试信息
     */
    @RequestMapping("/roster/pagedataKxxx")
    @ResponseBody
    public Map<String,Object> pagedataKxxx(YghmcDto yghmcDto,GrksglDto grksglDto){
        Map<String,Object> map = new HashMap<>();
        grksglDto.setRyid(yghmcDto.getYhid());
        List<GrksglDto> grksglDtoList=grksglService.getDtoList(grksglDto);
        map.put("rows",grksglDtoList);
        return map;
    }
    /**
     * 花名册查看
     */
    @RequestMapping("/roster/modRoster")
    public ModelAndView modRoster(YghmcDto yghmcDto){
        ModelAndView mav = new ModelAndView("train/roster/roster_edit");
        mav.addObject("urlPrefix", urlPrefix);
        yghmcDto.setFormAction("/roster/roster/modSaveRoster");
        mav.addObject("yghmcDto", yghmcDto);
        return mav;
    }
    /**
     * 保存修改内容
     */
    @RequestMapping("/roster/modSaveRoster")
    @ResponseBody
    public Map<String,Object> modSaveRoster(YghmcDto yghmcDto){
        Map<String,Object> map = new HashMap<>();
        boolean isSuccess=yghmcService.updateList(yghmcDto);
        if(isSuccess) {
            map.put("status","success");
            map.put("message",xxglService.getModelById("ICOM00001").getXxnr());
        }else {
            map.put("status","fail");
            map.put("message",xxglService.getModelById("ICOM00002").getXxnr());
        }
        return map;
    }
    /**
     *合同到期处理
     */
    @RequestMapping("/roster/processRoster")
    @ResponseBody
    public Map<String,Object> processRoster(YghmcDto yghmcDto){
        Map<String,Object> map = new HashMap<>();
        boolean isSuccess = yghmcService.processSaveRoster(yghmcDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }
    /**
     * 员工合同维护
     */
    @RequestMapping("/roster/yghtupholdRoster")
    public ModelAndView yghtupholdRoster(YghtxxDto yghtxxDto) {
        ModelAndView mav = new ModelAndView("train/roster/yghtuphold_Page");
        yghtxxDto.setFormAction("/roster/roster/yghtupholdSaveRoster");
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("yghtxxDto", yghtxxDto);
        return mav;
    }
    /**
     * 员工合同维护保存
     */
    @RequestMapping("/roster/yghtupholdSaveRoster")
    @ResponseBody
    public Map<String,Object> yghtupholdSaveRoster(YghtxxDto yghtxxDto,HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        User user=getLoginInfo(request);
        yghtxxDto.setLrry(user.getYhid());
        boolean isSuccess;
        try {
            isSuccess = yghtxxService.htupholdSaveRoster(yghtxxDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
                    : xxglService.getModelById("ICOM00002").getXxnr());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
        }
        return  map;
    }
    /**
     * 员工离职维护
     */
    @RequestMapping("/roster/yglzupholdRoster")
    public ModelAndView yglzupholdRoster(YglzxxDto yglzxxDto) {
        ModelAndView mav = new ModelAndView("train/roster/yglzuphold_Page");
        yglzxxDto.setFormAction("/roster/roster/yglzupholdSaveRoster");
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("yglzxxDto", yglzxxDto);
        return mav;
    }
    /**
     * 员工离职维护保存
     */
    @RequestMapping("/roster/yglzupholdSaveRoster")
    @ResponseBody
    public Map<String,Object> yglzupholdSaveRoster(YglzxxDto yglzxxDto,HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        User user=getLoginInfo(request);
        yglzxxDto.setLrry(user.getYhid());
        boolean isSuccess;
        try {
            isSuccess = yglzxxService.yglzupholdSaveRoster(yglzxxDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
                    : xxglService.getModelById("ICOM00002").getXxnr());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
        }
        return  map;
    }
    /**
     * 获取员工合同信息
     */
    @RequestMapping("/roster/pagedataGetHtxxById")
    @ResponseBody
    public Map<String, Object> pagedataGetHtxxById(YghtxxDto yghtxxDto){
        Map<String, Object> map = new HashMap<>();
        List<YghtxxDto> dtoList = yghtxxService.getDtoList(yghtxxDto);
        map.put("rows",dtoList);
        map.put("total",yghtxxDto.getTotalNumber());
        return map;
    }
    /**
     * 获取员工离职信息
     */
    @RequestMapping("/roster/pagedataGetLzxxById")
    @ResponseBody
    public Map<String, Object> pagedataGetLzxxById(YglzxxDto yglzxxDto){
        Map<String, Object> map = new HashMap<>();
        List<YglzxxDto> dtoList = yglzxxService.getDtoList(yglzxxDto);
        map.put("rows",dtoList);
        map.put("total",yglzxxDto.getTotalNumber());
        return map;
    }
    /**
     * 培训档案打印选择
     */
    @RequestMapping("/roster/trainprintRoster")
    public ModelAndView trainprintRoster(YghmcDto yghmcDto) {
        ModelAndView mav = new ModelAndView("train/roster/choose_trainprint");
        mav.addObject("yghmcDto",yghmcDto);
        mav.addObject("ssgslist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.AFFILIATED_COMPANY.getCode()));
        mav.addObject("pxfslist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.TRAINING_METHODS.getCode()));
        mav.addObject("pxlblist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.TRAINCATEGORY.getCode()));//培训类别
        mav.addObject("urlPrefix",urlPrefix);
        return mav;
    }

    /**
     * 培训档案打印数据修改
     */
    @RequestMapping("/roster/pagedataModPrintTrainData")
    public ModelAndView pagedataModPrintTrainData(YghmcDto yghmcDto) {
        ModelAndView mav = new ModelAndView("train/roster/trainprint_modPage");
        YghmcDto yghmcDto_t = yghmcService.getDtoById(yghmcDto.getYghmcid());
        yghmcDto_t.setGzids(yghmcDto.getGzids());
        mav.addObject("ssgslist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.AFFILIATED_COMPANY.getCode()));
        mav.addObject("yghmcDto", yghmcDto_t);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 培训档案打印
     */
    @RequestMapping("/roster/pagedataPrintTrainForChoose")
    public ModelAndView pagedataPrintTrainForChoose(YghmcDto yghmcDto) {
        ModelAndView mav;
        if("/labigams".equals(yghmcDto.getCskz1())) {
            mav=new ModelAndView("train/roster/trainprint_Page_lab");
        }else {
            mav=new ModelAndView("train/roster/trainprint_Page");
        }
        YghmcDto yghmcDto_t = yghmcService.getDtoById(yghmcDto.getYghmcid());
        PxglDto pxglDto = new PxglDto();
        pxglDto.setIds(yghmcDto.getGzids());
        List<PxglDto> trainRecords = pxglService.getTrainRecords(pxglDto);
        yghmcDto_t.setPxglDtos(trainRecords);
        yghmcDto_t.setRzrq(yghmcDto.getRzrq());
        yghmcDto_t.setZc(yghmcDto.getZc());
        //获取打印信息
        JcsjDto jcsj = new JcsjDto();
        jcsj.setCsdm("ROSTER_TRAIN");
        jcsj.setJclb(BasicDataTypeEnum.DOCUMENTNUM.getCode());
        jcsj = jcsjService.getDto(jcsj);
        mav.addObject("wjbh", jcsj);
        mav.addObject("yghmcPrint", yghmcDto_t);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 花名册  修改培训档案
     */
    @RequestMapping("/roster/modtrainrecordRoster")
    public ModelAndView modtrainrecordRoster(YghmcDto yghmcDto){
        ModelAndView mav = new ModelAndView("train/roster/roster_modTrainRecord");
        mav.addObject("ssgslist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.AFFILIATED_COMPANY.getCode()));
        mav.addObject("pxfslist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.TRAINING_METHODS.getCode()));
        mav.addObject("pxlblist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.TRAINCATEGORY.getCode()));//培训类别
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("yghmcDto", yghmcDto);
        return mav;
    }

    /**
     * 花名册  修改培训档案
     */
    @RequestMapping("/roster/pagedataModTrainRecordDetail")
    public ModelAndView pagedataModTrainRecord(GzglDto gzglDto){
        ModelAndView mav = new ModelAndView("train/roster/roster_modTrainRecord_detail");
        GzglDto dto = gzglService.getDtoById(gzglDto.getGzid());
        mav.addObject("ssgslist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.AFFILIATED_COMPANY.getCode()));
        mav.addObject("pxfslist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.TRAINING_METHODS.getCode()));
        mav.addObject("pxlblist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.TRAINCATEGORY.getCode()));//培训类别
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("gzglDto", dto);
        return mav;
    }

    /**
     * @Description: 健康档案新增
     * @param jkdaDto
     * @return org.springframework.web.servlet.ModelAndView
     * @Author: 郭祥杰
     * @Date: 2024/7/19 14:22
     */
    @RequestMapping("/roster/addHealthRecord")
    public ModelAndView addHealthRecord(JkdaDto jkdaDto){
        ModelAndView mav = new ModelAndView("train/roster/health_add");
        jkdaDto.setFormAction("addSaveHealthRecord");
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("jkdaDto", jkdaDto);
        return mav;
    }

    /**
     * @Description: 新增健康档案保存
     * @param jkdaDto
     * @param request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2024/7/19 14:31
     */
    @RequestMapping("/roster/addSaveHealthRecord")
    @ResponseBody
    public Map<String,Object> addSaveHealthRecord(JkdaDto jkdaDto, HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        jkdaDto.setLrry(user.getYhid());
        jkdaDto.setJkdaid(StringUtil.generateUUID());
        boolean isSuccess = jkdaService.insert(jkdaDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * @Description: 健康档案修改页面
     * @param jkdaDto
     * @return org.springframework.web.servlet.ModelAndView
     * @Author: 郭祥杰
     * @Date: 2024/7/19 16:05
     */
    @RequestMapping("/roster/modHealthRecord")
    public ModelAndView modHealthRecord(JkdaDto jkdaDto){
        ModelAndView mav = new ModelAndView("train/roster/health_edit");
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("jkdaDto", jkdaDto);
        return mav;
    }

    /**
     * @Description: 获取健康档案
     * @param jkdaDto
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2024/7/22 9:24
     */
    @ResponseBody
    @RequestMapping(value = "/roster/pagedataGetHealth")
    public Map<String, Object> pagedataGetHealth(JkdaDto jkdaDto) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isNotBlank(jkdaDto.getYghmcid())) {
            List<JkdaDto> jkdaDtoList = jkdaService.getDtoList(jkdaDto);
            map.put("rows", jkdaDtoList);
        } else {
            map.put("rows", new ArrayList<>());
        }
        return map;
    }

    /**
     * @Description: 健康答案修改
     * @param jkdaDto
     * @return org.springframework.web.servlet.ModelAndView
     * @Author: 郭祥杰
     * @Date: 2024/7/22 14:25
     */
    @RequestMapping("/roster/pagedataModHealth")
    public ModelAndView pagedataModHealth(JkdaDto jkdaDto){
        ModelAndView mav = new ModelAndView("train/roster/health_add");
        jkdaDto = jkdaService.getDtoById(jkdaDto.getJkdaid());
        jkdaDto.setFormAction("pagedataModSaveHealth");
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("jkdaDto", jkdaDto);
        return mav;
    }

    /**
     * @Description: 修改保存
     * @param jkdaDto
     * @param request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2024/7/22 14:41
     */
    @RequestMapping("/roster/pagedataModSaveHealth")
    @ResponseBody
    public Map<String,Object> pagedataModSaveHealth(JkdaDto jkdaDto, HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        jkdaDto.setXgry(user.getYhid());
        boolean isSuccess = jkdaService.update(jkdaDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * @Description: 删除
     * @param jkdaDto
     * @param request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2024/7/22 14:41
     */
    @RequestMapping("/roster/pagedataDelHealth")
    @ResponseBody
    public Map<String,Object> pagedataDelHealth(JkdaDto jkdaDto, HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        jkdaDto.setScry(user.getYhid());
        boolean isSuccess = jkdaService.delete(jkdaDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00003").getXxnr() : xxglService.getModelById("ICOM00004").getXxnr());
        return map;
    }


    /**
     * @Description: 健康答案打印选择页面
     * @param jkdaDto
     * @return org.springframework.web.servlet.ModelAndView
     * @Author: 郭祥杰
     * @Date: 2024/7/22 15:37
     */
    @RequestMapping("/roster/printHealthRecord")
    public ModelAndView printHealthRecord(JkdaDto jkdaDto) {
        ModelAndView mav = new ModelAndView("train/roster/chooseprintHealth");
        mav.addObject("jkdaDto",jkdaDto);
        mav.addObject("urlPrefix",urlPrefix);
        return mav;
    }

    @RequestMapping("/roster/pagedataModPrintHealth")
    public ModelAndView pagedataModPrintHealth(YghmcDto yghmcDto) {
        ModelAndView mav = new ModelAndView("train/roster/printHealth_modPage");
        YghmcDto yghmcDto_t = yghmcService.queryYghmcDto(yghmcDto.getYghmcid());
        yghmcDto_t.setIds(yghmcDto.getIds());
        Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.AFFILIATED_COMPANY});
        mav.addObject("ssgslist", jclist.get(BasicDataTypeEnum.AFFILIATED_COMPANY.getCode()));
        mav.addObject("yghmcDto", yghmcDto_t);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 培训档案打印
     */
    @RequestMapping("/roster/pagedataPrintHealthChoose")
    public ModelAndView pagedataPrintHealthChoose(JkdaDto jkdaDto) {
        ModelAndView mav;
        JcsjDto jcsj = new JcsjDto();
        if("/labigams".equals(jkdaDto.getCskz1())) {
            mav=new ModelAndView("train/roster/healthprint_Page_lab");
            jcsj.setCsdm("JKDA_LAB");
            jcsj.setJclb(BasicDataTypeEnum.DOCUMENTNUM.getCode());
            jcsj = jcsjService.getDto(jcsj);
        }else {
            mav=new ModelAndView("train/roster/healthprint_Page");
            jcsj.setCsdm("JKDA");
            jcsj.setJclb(BasicDataTypeEnum.DOCUMENTNUM.getCode());
            jcsj = jcsjService.getDto(jcsj);
        }
        List<JkdaDto> jkdaDtoList = jkdaService.getDtoList(jkdaDto);
        mav.addObject("dqmc",StringUtil.isNotBlank(jcsj.getCskz3())?jcsj.getCskz3():"");
        mav.addObject("wjbh", StringUtil.isNotBlank(jcsj.getCsmc())?jcsj.getCsmc():"");
        mav.addObject("cskz1", StringUtil.isNotBlank(jcsj.getCskz1())?jcsj.getCskz1():"");
        mav.addObject("cskz2", StringUtil.isNotBlank(jcsj.getCskz2())?jcsj.getCskz2():"");
        mav.addObject("jkdaDto", jkdaDto);
        mav.addObject("jkdaDtoList", jkdaDtoList);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * @Description: 健康档案导入
     * @param
     * @return org.springframework.web.servlet.ModelAndView
     * @Author: 郭祥杰
     * @Date: 2024/7/23 9:55
     */
    @RequestMapping("/roster/pageImportRoster")
    public ModelAndView pageImportRoster(){
        ModelAndView mav = new ModelAndView("train/roster/health_import");
        FjcfbDto fjcfbDto = new FjcfbDto();
        fjcfbDto.setYwlx(BusTypeEnum.IMP_HEALTH.getCode());
        mav.addObject("fjcfbDto", fjcfbDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
}
