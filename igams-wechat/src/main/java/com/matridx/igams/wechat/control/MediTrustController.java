package com.matridx.igams.wechat.control;


import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.wechat.dao.entities.MxtzglDto;
import com.matridx.igams.wechat.dao.entities.MxxxDto;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.service.svcinterface.IMxtzglService;
import com.matridx.igams.wechat.service.svcinterface.IMxxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/medi")
public class MediTrustController extends BaseController {

    @Autowired
    private IMxxxService mxxxService;
    @Autowired
    private IMxtzglService mxtzglService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    IXxglService xxglService;
    @Autowired
    ISjxxService sjxxService;
    @Autowired
    ICommonService commonService;


    /**
     * 镁信列表页面
     * @return
     */
    @RequestMapping("/medi/pageListMediTrust")
    public ModelAndView pageListMediTrust(){
        ModelAndView mav = new ModelAndView("wechat/mediTrust/medi_list");
        List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.IDCARD_CATEGORY.getCode());
        List<JcsjDto> list =new ArrayList<>();
       for(JcsjDto dto:jcsjDtos){
           if(StringUtil.isNotBlank(dto.getCskz2())){
               list.add(dto);
           }
       }
        mav.addObject("zjlxlist",list);
        return mav;
    }

    /**
     * 镁信列表
     * @param mxxxDto
     * @return
     */
    @RequestMapping("/medi/listMediTrust")
    @ResponseBody
    public Map<String,Object> listMediTrust(MxxxDto mxxxDto){
        List<MxxxDto> list = mxxxService.getPagedDtoList(mxxxDto);
        List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.IDCARD_CATEGORY.getCode());
        for(MxxxDto mxxxDto_t:list){
            if(StringUtil.isNotBlank(mxxxDto_t.getIdType())){
                for(JcsjDto dto:jcsjDtos){
                    if(dto.getCsid().equals(mxxxDto_t.getIdType())){
                        mxxxDto_t.setIdType(dto.getCsmc());
                    }
                }
            }
        }
        Map<String,Object> result = new HashMap<>();
        result.put("total", mxxxDto.getTotalNumber());
        result.put("rows", list);
        return result;
    }

    /**
     * 镁信列表查看页面
     * @return
     */
    @RequestMapping("/medi/viewMediTrust")
    public ModelAndView viewMediTrust(MxxxDto mxxxDto){
        ModelAndView mav = new ModelAndView("wechat/mediTrust/medi_view");
        MxxxDto mxxxDto_t = mxxxService.getDtoById(mxxxDto.getMxid());
        List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.IDCARD_CATEGORY.getCode());
        for(JcsjDto dto:jcsjDtos){
            if(dto.getCsid().equals(mxxxDto_t.getIdType())){
                mxxxDto_t.setIdType(dto.getCsmc());
            }
        }
        mav.addObject("mxxxDto",mxxxDto_t);
        return mav;
    }

    /**
     * 镁信列表通知页面
     * @return
     */
    @RequestMapping("/medi/noticeMediTrust")
    public ModelAndView noticeMediTrust(MxxxDto mxxxDto){
        ModelAndView mav = new ModelAndView("wechat/mediTrust/medi_notice");
        mav.addObject("mxxxDto",mxxxDto);
        return mav;
    }

    /**
     * 镁信通知
     * @param mxtzglDto
     * @return
     */
    @RequestMapping("/medi/saveNoticeMediTrust")
    @ResponseBody
    public Map<String,Object> saveNoticeMediTrust(MxtzglDto mxtzglDto){
        Map<String,Object> result = new HashMap<>();
        boolean isSuccess = mxtzglService.sendMessage(mxtzglDto);
        result.put("status", isSuccess ? "success" : "fail");
        result.put("message", isSuccess ? xxglService.getModelById("ICOM99024").getXxnr() : xxglService.getModelById("ICOM99025").getXxnr());
        return result;
    }

    /**
     * 镁信列表关联特检页面
     * @return
     */
    @RequestMapping("/medi/relationInspect")
    public ModelAndView relationInspect(MxxxDto mxxxDto){
        ModelAndView mav = new ModelAndView("wechat/mediTrust/medi_relation");
        mav.addObject("mxxxDto",mxxxDto);
        return mav;
    }

    /**
     * 镁信关联特检
     * @param sjxxDto
     * @return
     */
    @RequestMapping("/medi/saveMediRelation")
    @ResponseBody
    public Map<String,Object> saveMediRelation(SjxxDto sjxxDto){
        Map<String,Object> result = new HashMap<>();
        sjxxService.update(sjxxDto);
        MxxxDto mxxxDto =new MxxxDto();
        mxxxDto.setMxid(sjxxDto.getMxid());
        mxxxDto.setStatus("1");
        boolean isSuccess = mxxxService.update(mxxxDto);
        result.put("status", isSuccess ? "success" : "fail");
        result.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        return result;
    }

    /**
     * 获取镁信数据
     * @param mxtzglDto
     * @return
     */
    @RequestMapping("/medi/minidataMxxxList")
    @ResponseBody
    public Map<String,Object> minidataMxxxList(MxtzglDto mxtzglDto){
        Map<String,Object> result = new HashMap<>();
        User user=new User();
        user.setDdid(mxtzglDto.getDdid());
        user.setYhid(mxtzglDto.getYhid());
        User user_t = commonService.getUserInfoById(user);
        mxtzglDto.setRyid(user_t.getYhid());
        List<MxtzglDto> dtoList = mxtzglService.getDtoList(mxtzglDto);
        result.put("mxlist",dtoList);
        return result;
    }

}
