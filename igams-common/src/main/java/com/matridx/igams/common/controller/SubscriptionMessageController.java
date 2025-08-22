package com.matridx.igams.common.controller;

import com.matridx.igams.common.dao.entities.GrszDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.service.svcinterface.IGrszService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : 郭祥杰
 * @date :
 */
@Controller
@RequestMapping("/subscribe")
public class SubscriptionMessageController extends BaseController{
    @Autowired
    private IGrszService grszService;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Override
    public String getPrefix(){
        return urlPrefix;
    }
    @Autowired
    private IJcsjService jcsjService;

    /**
     * @Description: 订阅消息列表
     * @param
     * @return org.springframework.web.servlet.ModelAndView
     * @Author: 郭祥杰
     * @Date: 2024/11/27 15:51
     */
    @RequestMapping("/subscribe/pageListSubscribe")
    public ModelAndView pageListSubscribe() {
        ModelAndView mav= new ModelAndView("globalweb/message/subscriptionMessage_list");
        Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.MESSAGE_TYPE});
        List<JcsjDto> jcsjDtos = jclist.get(BasicDataTypeEnum.MESSAGE_TYPE.getCode());
        JcsjDto jcsjDto = new JcsjDto();
        jcsjDto.setCsmc("审核订阅消息");
        jcsjDto.setCskz2("SH");
        jcsjDtos.add(jcsjDto);
        mav.addObject("xxlxlist",jcsjDtos);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * @Description: 订阅消息列表数据
     * @param grszDto
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2024/11/27 15:52
     */
    @RequestMapping("/subscribe/pageGetListSubscribe")
    @ResponseBody
    public Map<String, Object> pageGetListSubscribe(GrszDto grszDto){
        Map<String,Object> map= new HashMap<>();
        List<GrszDto> grszDtoList = grszService.getPagedDtoList(grszDto);
        for(GrszDto dto : grszDtoList) {
            String xxnrContent = dto.getXxnr().replaceAll("<", "＜").
                    replaceAll(">", "＞").replaceAll("\"","＂").replaceAll("#","＃");
            dto.setXxnr(xxnrContent);
        }
        map.put("total",grszDto.getTotalNumber());
        map.put("rows",grszDtoList);
        return map;
    }

    /**
     * @Description: 消息订阅查看
     * @param grszDto
     * @return org.springframework.web.servlet.ModelAndView
     * @Author: 郭祥杰
     * @Date: 2024/11/27 15:53
     */
    @RequestMapping(value="/subscribe/viewSubscribe")
    @ResponseBody
    public ModelAndView viewSubscribe(GrszDto grszDto){
        ModelAndView mav=new ModelAndView("globalweb/message/subscriptionMessage_view");
        grszDto = grszService.getDto(grszDto);
        mav.addObject("grszDto", grszDto);
        return mav;
    }


}
