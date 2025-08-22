package com.matridx.igams.hrm.controller;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisLock;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.hrm.dao.entities.JpglDto;
import com.matridx.igams.hrm.dao.entities.JpjgDto;
import com.matridx.igams.hrm.dao.entities.JpjgModel;
import com.matridx.igams.hrm.dao.entities.JpmxDto;
import com.matridx.igams.hrm.service.svcinterface.IJpglService;
import com.matridx.igams.hrm.service.svcinterface.IJpjgService;
import com.matridx.igams.hrm.service.svcinterface.IJpmxService;
import com.ruiyun.jvppeteer.util.StringUtil;
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
import java.util.*;

@Controller
@RequestMapping("/award")
public class AwardManagementController extends BaseController {

    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Override
    public String getPrefix() {
        return urlPrefix;
    }
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    private IJpglService jpglService;
    @Autowired
    private IJpmxService jpmxService;
    @Autowired
    IXxglService xxglService;
    @Autowired
    IFjcfbService fjcfbService;
    @Autowired
    IJpjgService jpjgService;
	private final Logger log = LoggerFactory.getLogger(AwardManagementController.class);

    /**
     * 奖品管理列表
     */
    @RequestMapping(value="/award/pageListAwardManagement")
    public ModelAndView pageListAwardManagement() {
        ModelAndView mav=new ModelAndView("award/award/awardManagement_list");
        mav.addObject("urlPrefix",urlPrefix);
        mav.addObject("jplxlist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.AWARD_TYPE.getCode()));
        mav.addObject("tzqlist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.NOTIF_GROUP.getCode()));
        return mav;
    }


    /**
     * 奖品管理列表 数据
     */
    @RequestMapping(value="/award/pageGetListAwardManagement")
    @ResponseBody
    public Map<String,Object> pageGetListAwardManagement(JpglDto jpglDto){
        List<JpglDto> list=jpglService.getPagedDtoList(jpglDto);
        Map<String,Object> result = new HashMap<>();
        result.put("total", jpglDto.getTotalNumber());
        result.put("rows", list);
        return result;
    }

    /**
     * 奖品管理列表 删除
     */
    @RequestMapping("/award/delAwardManagement")
    @ResponseBody
    public Map<String, Object> delAwardManagement(JpglDto jpglDto, HttpServletRequest request){
        User user = getLoginInfo(request);
        jpglDto.setScry(user.getYhid());
        Map<String, Object> map = new HashMap<>();
        boolean isSuccess = jpglService.delAwardManagement(jpglDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00003").getXxnr() : xxglService.getModelById("ICOM00004").getXxnr());
        return map;
    }

    /**
     * 奖品管理列表 新增
     */
    @RequestMapping(value="/award/addAwardManagement")
    public ModelAndView addAwardManagement(JpglDto jpglDto) {
        ModelAndView mav=new ModelAndView("award/award/awardManagement_add");
        jpglDto.setFormAction("addSaveAwardManagement");
        mav.addObject("jpglDto",jpglDto);
        mav.addObject("ywlx", BusTypeEnum.IMP_AWARD_MANAGEMENT.getCode());
        mav.addObject("urlPrefix",urlPrefix);
        mav.addObject("jplxlist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.AWARD_TYPE.getCode()));
        mav.addObject("tzqlist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.NOTIF_GROUP.getCode()));
        return mav;
    }


    /**
     * 奖品管理列表明细 数据
     */
    @RequestMapping(value="/award/pagedataGetAwardManagementDetails")
    @ResponseBody
    public Map<String,Object> pagedataGetAwardManagementDetails(JpmxDto jpmxDto){
        List<JpmxDto> list= new ArrayList<>();
        if(StringUtil.isNotBlank(jpmxDto.getJpglid())){
            list=jpmxService.getDtoList(jpmxDto);
        }
        if ("1".equals(jpmxDto.getCopyflag())&& !CollectionUtils.isEmpty(list)){
            for (JpmxDto dto : list) {
                dto.setJpmxid(null);
                dto.setFjid(null);
                dto.setWjm(null);
            }
        }
        Map<String,Object> result = new HashMap<>();
        result.put("rows", list);
        return result;
    }

    /**
     * 奖品管理列表 查看
     */
    @RequestMapping(value="/award/viewAwardManagement")
    public ModelAndView viewAwardManagement(JpglDto jpglDto) {
        ModelAndView mav=new ModelAndView("award/award/awardManagement_view");
        JpglDto dto = jpglService.getDto(jpglDto);
        mav.addObject("jpglDto",dto);
        mav.addObject("urlPrefix",urlPrefix);
        return mav;
    }

    /**
     * 附件列表上传页面
     */
    @RequestMapping("/award/pagedataGetUploadFilePage")
    public ModelAndView pagedataGetUploadFilePage(String ywlx,String fjid) {
        ModelAndView mav = new ModelAndView("award/award/awardManagement_uploadFile");
        FjcfbDto fjcfbDto=new FjcfbDto();
        if(StringUtil.isNotBlank(fjid)){
            fjcfbDto.setFjid(fjid);
            FjcfbDto dto = fjcfbService.getDto(fjcfbDto);
            if(dto!=null){
                fjcfbDto=dto;
            }else{
                Map<Object, Object> mFile = redisUtil.hmget("IMP_:_" + fjid);
                //如果文件信息不存在，则跳过
                if (mFile != null&&mFile.size()>0){
                    //文件名
                    String wjm = (String) mFile.get("wjm");
                    fjcfbDto.setWjm(wjm);
                    //文件全路径
                    String wjlj = (String) mFile.get("wjlj");
                    fjcfbDto.setYwlx(ywlx);
                    fjcfbDto.setWjlj(wjlj);
                }
            }
        }
        mav.addObject("fjcfbDto", StringUtil.isNotBlank(fjcfbDto.getFjid())?fjcfbDto:null);
        mav.addObject("ywlx", ywlx);
        mav.addObject("fjids", StringUtil.isNotBlank(fjid)?fjid:"");
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 奖品管理列表 新增保存
     */
    @RequestMapping("/award/addSaveAwardManagement")
    @ResponseBody
    public Map<String, Object> addSaveAwardManagement(JpglDto jpglDto, HttpServletRequest request){
        User user = getLoginInfo(request);
        jpglDto.setLrry(user.getYhid());
        Map<String, Object> map = new HashMap<>();
        boolean isSuccess = jpglService.addSaveAwardManagement(jpglDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }


    /**
     * 奖品管理列表 修改
     */
    @RequestMapping(value="/award/modAwardManagement")
    public ModelAndView modAwardManagement(JpglDto jpglDto) {
        ModelAndView mav=new ModelAndView("award/award/awardManagement_add");
        JpglDto dto = jpglService.getDto(jpglDto);
        if (StringUtil.isBlank(jpglDto.getFormAction())){
            dto.setFormAction("modSaveAwardManagement");
        }else {
            dto.setFormAction(jpglDto.getFormAction());
        }
        dto.setCopyflag(jpglDto.getCopyflag());
        mav.addObject("jpglDto",dto);
        mav.addObject("ywlx", BusTypeEnum.IMP_AWARD_MANAGEMENT.getCode());
        mav.addObject("urlPrefix",urlPrefix);
        mav.addObject("jplxlist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.AWARD_TYPE.getCode()));
        mav.addObject("tzqlist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.NOTIF_GROUP.getCode()));
        return mav;
    }

    /**
     * 奖品管理列表 修改保存
     */
    @RequestMapping("/award/modSaveAwardManagement")
    @ResponseBody
    public Map<String, Object> modSaveAwardManagement(JpglDto jpglDto, HttpServletRequest request){
        User user = getLoginInfo(request);
        jpglDto.setXgry(user.getYhid());
        Map<String, Object> map = new HashMap<>();
        boolean isSuccess = jpglService.modSaveAwardManagement(jpglDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }
    /**
     * 奖品管理列表 复制
     */
    @RequestMapping(value="/award/copyAwardManagement")
    public ModelAndView copyAwardManagement(JpglDto jpglDto) {
        jpglDto.setFormAction("copySaveAwardManagement");
        jpglDto.setCopyflag("1");
        return modAwardManagement(jpglDto);
    }

    /**
     * 奖品管理列表 复制保存
     */
    @RequestMapping("/award/copySaveAwardManagement")
    @ResponseBody
    public Map<String, Object> copySaveAwardManagement(JpglDto jpglDto, HttpServletRequest request){
       return addSaveAwardManagement(jpglDto,request);
    }
	/**
     * 获取抽奖数据
     */
    @RequestMapping(value="/award/minidataGetLotteryInfo")
    @ResponseBody
    public Map<String,Object> minidataGetLotteryInfo(JpjgDto jpjgDto, HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        User user = getLoginInfo(request);
        JpjgModel jpjgModel = new JpjgModel();
        jpjgModel.setJpglid(jpjgDto.getJpglid());
        jpjgModel.setYhid(user.getYhid());
        //获奖次数
        int hjcs = jpjgService.getCount(jpjgModel);
        JpglDto jpglDto = jpglService.getLotteryInfo(jpjgDto.getJpglid());
        JpmxDto jpmxDto_sel = new JpmxDto();
        jpmxDto_sel.setJpglid(jpjgDto.getJpglid());
        List<JpmxDto> jpmxDtos = jpmxService.getLotteryInfos(jpmxDto_sel);
        //>0说明抽过了
        result.put("isHave", hjcs>0);
        //奖品管理信息
        result.put("jpglDto", jpglDto);
        //奖品明细信息
        result.put("jpmxDtos", jpmxDtos);
        return result;
    }
	
    /**
     * 获取抽奖结果
     */
    @RequestMapping(value="/award/minidataGetLotteryResult")
    @ResponseBody
    @RedisLock
    public Map<String,Object> minidataGetLotteryResult(JpglDto jpglDto, HttpServletRequest request){
        User user = getLoginInfo(request);
        Map<String,Object> result = new HashMap<>();
        try {
            result = jpglService.getLotteryResult(jpglDto,user);
        } catch (BusinessException e) {
            log.error("----------getLotteryResult(BusinessException)异常:"+e.getMsg());
        } catch (Exception e) {
            log.error("----------getLotteryResult(Exception)异常:"+e.getMessage());
        }
        return result;
    }
    /**
     * 获取中奖记录
     */
    @RequestMapping(value="/award/minidataGetLotteryRecords")
    @ResponseBody
    public Map<String,Object> minidataGetLotteryRecords(JpjgDto jpjgDto, HttpServletRequest request){
        User user = getLoginInfo(request);
        jpjgDto.setYhid(user.getYhid());
        Map<String,Object> result = new HashMap<>();
        List<JpjgDto> JpjgDtos = jpjgService.getLotteryRecords(jpjgDto);
        JpjgDto personal = jpjgService.getPersonalLotteryRecord(jpjgDto);
        if (personal!=null&&"谢谢参与".equals(personal.getJpmc())){
            personal = null;
        }
        result.put("personal", personal);
        result.put("JpjgDtos", JpjgDtos);
        return result;
    }

    /**
     * 抽奖页面初始化
     * @param request
     * @return
     */
    @RequestMapping("/award/minidataLuckWheel")
    public ModelAndView luckWheel(HttpServletRequest request) {
        ModelAndView mav =new ModelAndView("luckyWheel/LuckyWheel");
        mav.addObject("access_token",request.getParameter("access_token"));
        mav.addObject("jpglid",request.getParameter("jpglid"));
        mav.addObject("urlPrefix",urlPrefix);
        return mav;
    }

}
