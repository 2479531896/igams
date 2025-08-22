package com.matridx.server.detection.molecule.controller;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.ProgramCodeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.server.detection.molecule.dao.entities.FzjcxmDto;
import com.matridx.server.detection.molecule.dao.entities.FzjcxxDto;
import com.matridx.server.detection.molecule.service.svcinterface.IFzjcxmPJService;
import com.matridx.server.detection.molecule.service.svcinterface.IFzjcxxPJService;
import com.matridx.server.wechat.dao.entities.SjdwxxDto;
import com.matridx.server.wechat.service.svcinterface.ISjdwxxService;
import com.matridx.server.wechat.service.svcinterface.ISjysxxService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/wechat")
public class DetectionPJController extends BaseController {
    private final Logger log = LoggerFactory.getLogger(DetectionPJController.class);

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    IFzjcxxPJService fzjcxxPJService;
    @Autowired
    IFzjcxmPJService fzjcxmPJService;
    @Autowired
    ISjysxxService sjysxxService;
    @Autowired
    ISjdwxxService sjdwxxService;
    @Autowired
    IFjcfbService fjcfbService;
    @Autowired
    IXxglService xxglService;
    @Autowired
    ICommonService commonService;

    /*普检系统选择页面获取微信授权后跳转*/
    @RequestMapping(value="/detectionPJ/generalInspectionChooseMap")
    @ResponseBody
    public Map<String,Object> generalInspectionChooseMap(@RequestParam(name = "wxid", required = false) String wxid, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        map.put("mavUrl","wechat/general_inspChoose");
        String flag = request.getParameter("flag");
        if (StringUtil.isNotBlank(flag)){
            map.put("flag",flag);
        }
        String wbcxdm = request.getParameter("wbcxdm");
        if(StringUtil.isBlank(wbcxdm)){
            wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
        }
        List<JcsjDto> jcsjDtos = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
        map.put("fzjclxList",jcsjDtos);
        map.put("wbcxdm",wbcxdm);
        map.put("wxid", wxid);
        return map;
    }

    /*普检系统选择页面*/
    @RequestMapping(value="/detectionPJ/generalInspectionChoose")
    public ModelAndView generalInspectionChoose(@RequestParam(name = "wxid", required = false) String wxid, HttpServletRequest request){
        Map<String, Object> map = generalInspectionChooseMap(wxid, request);
        String mavUrl = map.get("mavUrl").toString();
        ModelAndView mav = new ModelAndView(mavUrl);
        mav.addAllObjects(map);
        return mav;
    }

    /*送检系统选择页面（无code）*/
    @RequestMapping(value="/detectionPJ/generalReportViewChoose")
    public ModelAndView generalReportViewChoose(@RequestParam(name = "wxid", required = false) String wxid, HttpServletRequest request){
        ModelAndView mav = new ModelAndView("wechat/general_inspIndex");
        String wbcxdm = request.getParameter("wbcxdm");
        if(StringUtil.isBlank(wbcxdm)){
            wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
        }
        mav.addObject("wbcxdm",wbcxdm);
        mav.addObject("wxid",wxid);
        String fzjclx = request.getParameter("fzjclx");
//        String fzjclxdm = request.getParameter("fzjclxdm");
        JcsjDto jcsjDto = redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode(), fzjclx);
        mav.addObject("jcsjDto",jcsjDto);
        mav.addObject("request",request);
        return mav;
    }

    /**
     * 普检录入页面
     * @param wxid
     * @param request
     * @return
     */
    @RequestMapping(value="/detectionPJ/generalInspectionInput")
    public ModelAndView generalInspectionInput(@RequestParam(name = "wxid", required = false) String wxid, HttpServletRequest request){
        ModelAndView mav = new ModelAndView("wechat/general_inspReport");
        String wbcxdm = request.getParameter("wbcxdm");
        if(StringUtil.isBlank(wbcxdm)){
            wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
        }
        mav.addObject("wbcxdm",wbcxdm);
        mav.addObject("wxid",wxid);
        String fzjclx = request.getParameter("fzjclx");
//        String fzjclxdm = request.getParameter("fzjclxdm");
        JcsjDto jcsjDto = redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode(), fzjclx);
        mav.addObject("jcsjDto",jcsjDto);
        mav.addObject("fzjcxxDto",new FzjcxxDto());
        //根据检测项目代码限制标本类型
        mav.addObject("genderlist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.GENDER_TYPE.getCode()));
        return mav;
    }

    /**
     * 普检修改页面
     * @param fzjcxxDto
     * @param request
     * @return
     */
    @RequestMapping(value="/detectionPJ/modGeneralInspection")
    public ModelAndView modGeneralInspection(FzjcxxDto fzjcxxDto, HttpServletRequest request){
        ModelAndView mav = new ModelAndView("wechat/general_inspReport");
        String wbcxdm = request.getParameter("wbcxdm");
        if(StringUtil.isBlank(wbcxdm)){
            wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
        }
        mav.addObject("wbcxdm",wbcxdm);
        mav.addObject("wxid",fzjcxxDto.getWxid());
        String fzjclx = request.getParameter("fzjclx");
        String fzjclxdm = request.getParameter("fzjclxdm");
        JcsjDto jcsjDto = redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode(), fzjclx);
        mav.addObject("jcsjDto",jcsjDto);
        FzjcxxDto dto = fzjcxxPJService.getPjDto(fzjcxxDto);
        List<FzjcxmDto> xmlist = fzjcxmPJService.getDtoListByFzjcid(fzjcxxDto.getFzjcid());
        StringBuilder zxmids= new StringBuilder();//子项目ids
        StringBuilder fxmids= new StringBuilder();//父项目ids
        for (FzjcxmDto zxm : xmlist) {
            if(StringUtil.isNoneBlank(zxm.getFzjczxmid())){
                zxmids.append(',').append(zxm.getFzjczxmid());
            }
            if(StringUtil.isNoneBlank(zxm.getFzjcxmid())){
                fxmids.append(',').append(zxm.getFzjcxmid());
            }
        }
        if(StringUtil.isNoneBlank(zxmids.toString())){
            zxmids = new StringBuilder(zxmids.substring(1));
            dto.setJczxmid(zxmids.toString());
        }
        if(StringUtil.isNoneBlank(fxmids.toString())){
            fxmids = new StringBuilder(fxmids.substring(1));
            dto.setJcxmid(fxmids.toString());
        }
        mav.addObject("fzjcxxDto",dto);
        mav.addObject("genderlist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.GENDER_TYPE.getCode()));
        return mav;
    }

    /**
     * 普检录入保存
     * @param fzjcxxDto
     * @return
     */
    @RequestMapping(value="/detectionPJ/addSaveGeneralInspection")
    @ResponseBody
    public Map<String,Object> addSaveGeneralInspection(FzjcxxDto fzjcxxDto){
        Map<String,Object> map = new HashMap<>();
        boolean result = false;
        try {
            fzjcxxDto.setJclx(fzjcxxDto.getFzjclx());
            result = fzjcxxPJService.addSaveFzjcxx(fzjcxxDto);
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message",e.getMessage());
            return map;
        }
        map.put("status", result ? "success" : "fail");
        map.put("message", result ? xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 普检清单页面
     * @param wxid
     * @param request
     * @return
     */
    @RequestMapping(value="/detectionPJ/generalInspectionPerfect")
    public ModelAndView generalInspectionPerfect(@RequestParam(name = "wxid", required = false) String wxid, HttpServletRequest request){
        ModelAndView mav = new ModelAndView("wechat/general_reportList");
        String wbcxdm = request.getParameter("wbcxdm");
        if(StringUtil.isBlank(wbcxdm)){
            wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
        }
        mav.addObject("wbcxdm",wbcxdm);
        mav.addObject("wxid",wxid);
        mav.addObject("sign",commonService.getSign());
        String fzjclx = request.getParameter("fzjclx");
        mav.addObject("fzjclx",fzjclx);
        return mav;
    }

    /**
     * 普检清单数据
     * @param fzjcxxDto
     * @param request
     * @return
     */
    @RequestMapping(value="/detectionPJ/generalReportlist")
    @ResponseBody
    public Map<String, Object> generalReportlist(FzjcxxDto fzjcxxDto,HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> searchMap = new HashMap<>();
        List<FzjcxxDto> fzjcxxDtos = new ArrayList<>();
        boolean checkSign = commonService.checkSign(fzjcxxDto.getSign(),request);
        if(!checkSign){
            map.put("total", fzjcxxDto.getTotalNumber());
            map.put("rows", fzjcxxDtos);
            return map;
        }
        //根据微信ID获取录入人员列表
        if(StringUtil.isNotBlank(fzjcxxDto.getWxid())){
            List<String> lrrylist = sjysxxService.getLrrylist(fzjcxxDto.getWxid(), null);
            fzjcxxDto.setLrrys(lrrylist);
            searchMap.put("lrrys",fzjcxxDto.getLrrys());
            searchMap.put("sortOrder",fzjcxxDto.getSortOrder());
            searchMap.put("sortName",fzjcxxDto.getSortName());
            searchMap.put("pageNumber",fzjcxxDto.getPageNumber());
            searchMap.put("pageSize",fzjcxxDto.getPageSize());
            searchMap.put("pageStart",(fzjcxxDto.getPageNumber()-1)*fzjcxxDto.getPageSize());
            searchMap.put("entire",fzjcxxDto.getEntire());
            searchMap.put("fzjclx",fzjcxxDto.getFzjclx());

            fzjcxxDtos = fzjcxxPJService.getListWithMap(searchMap);
            if(fzjcxxDtos != null && fzjcxxDtos.size() > 0){
                for (int i = 0; i < fzjcxxDtos.size(); i++) {
                    fzjcxxDtos.get(i).setSign(commonService.getSign(fzjcxxDtos.get(i).getYbbh()));
                }
            }
        }
        map.put("total", fzjcxxDto.getTotalNumber());
        map.put("rows", fzjcxxDtos);
        //需要筛选钉钉字段的，请调用该方法
        screenClassColumns(request,map);
        return map;
    }

    /**
     * 普检清单数据
     * @param fzjcxxDto
     * @param request
     * @return
     */
    @RequestMapping(value="/detectionPJ/delGeneralInspection")
    @ResponseBody
    public Map<String, Object> delGeneralInspection(FzjcxxDto fzjcxxDto,HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        fzjcxxDto.setScry(fzjcxxDto.getWxid());
        boolean isSuccess = fzjcxxPJService.delGeneralInspection(fzjcxxDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00003").getXxnr()
                : xxglService.getModelById("ICOM00004").getXxnr());
        return map;
    }

    /**
     * 微信送检录入页面获取基础数据
     * @param request
     * @return
     */
    @RequestMapping(value = "/detectionPJ/getGeneralInspBasicData")
    @ResponseBody
    public Map<String, Object> getGeneralInspBasicData(String flag,HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        map.put("jcdwList" , redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.DETECTION_UNIT.getCode()));
        String fzjclx = request.getParameter("fzjclx");
        List<JcsjDto> jcsjDtos = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_ITEM.getCode());
        for (int i = jcsjDtos.size() - 1; i >= 0; i--) {
            if (!fzjclx.equals(jcsjDtos.get(i).getFcsid())){
                jcsjDtos.remove(i);
            }
        }
        map.put("fzjcxmList",jcsjDtos);
        map.put("fzjczxmList", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.MOLECULAR_SUBITEM.getCode()));
        List<SjdwxxDto> sjdwlist = sjdwxxService.selectSjdwList();
        map.put("ksxxList", sjdwlist);
        //根据检测项目代码限制标本类型
        List<JcsjDto> yblxList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.GENERALSAMPLE_TYPE.getCode());
        for (int i = yblxList.size() - 1; i >= 0; i--) {
            if (!fzjclx.equals(yblxList.get(i).getFcsid())){
                yblxList.remove(i);
            }
        }
        map.put("yblxList", yblxList);
        List<FzjcxmDto> fzjcxmDtos = new ArrayList<>();
        if (StringUtil.isNotBlank(request.getParameter("fzjcid"))){
            fzjcxmDtos = fzjcxmPJService.getDtoListByFzjcid(request.getParameter("fzjcid"));
        }
        map.put("fzjcxmDtos", fzjcxmDtos);
        return map;
    }
    /**
     * 送检清单点击后获取送检结果信息
     * @param fzjcxxDto
     * @return
     */
    @RequestMapping("/getGeneralReportView")
    public ModelAndView getGeneralReportView(FzjcxxDto fzjcxxDto, HttpServletRequest request){
        boolean checkSign = commonService.checkSign(fzjcxxDto.getSign(), null, request);
        if(!checkSign){
            return commonService.jumpError();
        }
        ModelAndView mav = new ModelAndView("wechat/general_reportview");
        FzjcxxDto pjDto = fzjcxxPJService.getPjDto(fzjcxxDto);
        /*if (pjDto != null){
            FjcfbDto fjcfbDto=new FjcfbDto();
            fjcfbDto.setYwid(fzjcxxDto.getFzjcid());
            if ("TYPE_FLU".equals(pjDto.getJclxdm())){
                fjcfbDto.setYwlx(BusTypeEnum.IMP_REPORT_FLU_WORD.getCode());
            }else {
                fjcfbDto.setYwlx(BusTypeEnum.IMP_REPORT_HPV_WORD.getCode());
            }
            List<FjcfbDto> wordList = fzjcxxPJService.getReport(fjcfbDto);
            if ("TYPE_FLU".equals(pjDto.getJclxdm())){
                fjcfbDto.setYwlx(BusTypeEnum.IMP_REPORT_FLU.getCode());
            }else {
                fjcfbDto.setYwlx(BusTypeEnum.IMP_REPORT_HPV.getCode());
            }
            List<FjcfbDto> pdfList = fzjcxxPJService.getReport(fjcfbDto);
            mav.addObject("wordList", wordList);
            mav.addObject("pdfList",pdfList);
        }*/
        mav.addObject("fzjcxxDto", pjDto);
        List<FzjcxmDto> fzjcxmDtos = new ArrayList<>();
        if (StringUtil.isNotBlank(request.getParameter("fzjcid"))){
            fzjcxmDtos = fzjcxmPJService.getDtoListByFzjcid(request.getParameter("fzjcid"));
            if (!CollectionUtils.isEmpty(fzjcxmDtos)){
                List<String> ywids = fzjcxmDtos.stream().map(FzjcxmDto::getFzxmid).collect(Collectors.toList());
                FjcfbDto fjcfbDto = new FjcfbDto();
                fjcfbDto.setYwids(ywids);
                //word报告
//                if ("TYPE_FLU".equals(fzjcxxDto.getJclxdm())){
//                    fjcfbDto.setYwlx(BusTypeEnum.IMP_REPORT_FLU_WORD.getCode());
//                }else {
//                    fjcfbDto.setYwlx(BusTypeEnum.IMP_REPORT_HPV_WORD.getCode());
//                }
//                List<FjcfbDto> allWordList = fjcfbService.selectFjcfbDtoByYwidsAndYwlx(fjcfbDto);
//                for (int i = 0; i < allWordList.size(); i++) {
//                    allWordList.get(i).setSign(commonService.getSign(allWordList.get(i).getFjid()));
//                }
//                if (!CollectionUtils.isEmpty(allWordList)){
//                    for (FzjcxmDto fzjcxmDto : fzjcxmDtos) {
//                        fzjcxmDto.setWordList(allWordList.stream().filter(e->e.getYwid().equals(fzjcxmDto.getFzxmid())).collect(Collectors.toList()));
//                    }
//                }
                if ("TYPE_FLU".equals(fzjcxxDto.getJclxdm())){
                    fjcfbDto.setYwlx(BusTypeEnum.IMP_REPORT_FLU.getCode());
                }else {
                    fjcfbDto.setYwlx(BusTypeEnum.IMP_REPORT_HPV.getCode());
                }
                List<FjcfbDto> allPdfList = fjcfbService.selectFjcfbDtoByYwidsAndYwlx(fjcfbDto);
                for (int i = 0; i < allPdfList.size(); i++) {
                    allPdfList.get(i).setSign(commonService.getSign(allPdfList.get(i).getFjid()));
                }
                if (!CollectionUtils.isEmpty(allPdfList)){
                    for (FzjcxmDto fzjcxmDto : fzjcxmDtos) {
                        fzjcxmDto.setPdfList(allPdfList.stream().filter(e->e.getYwid().equals(fzjcxmDto.getFzxmid())).collect(Collectors.toList()));
                    }
                }
            }
        }
        mav.addObject("fzjcxmDtos", fzjcxmDtos);
        return mav;
    }
}
