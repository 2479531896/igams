package com.matridx.igams.wechat.control;


import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.wechat.dao.entities.CkwxglDto;
import com.matridx.igams.wechat.dao.entities.WzglDto;
import com.matridx.igams.wechat.service.svcinterface.ICkwxglService;
import com.matridx.igams.wechat.service.svcinterface.IWzglService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/wzjb")
public class WzglController extends BaseController {
    @Autowired
    private IWzglService wzglService;
    @Autowired
    private IXxglService xxglService;
    @Autowired
    ICkwxglService ckwxglService;
    /**
     * 物种级别页面
     * @param wzglDto
     * @return
     */
    @RequestMapping(value ="/wzjb/pageListWzjb")
    public ModelAndView viewWzjb(WzglDto wzglDto) {
        ModelAndView mav=new ModelAndView("wechat/sjxx/wzjb_List");
        mav.addObject("WzglDto", wzglDto);
        return mav;
    }

    /**
     * 物种级别列表信息
     * @param wzglDto
     * @return
     */
    @RequestMapping("/wzjb/pageGetListWzjb")
    @ResponseBody
    public Map<String, Object> pageGetListWzjb(WzglDto wzglDto){
        Map<String, Object> map= new HashMap<>();
        List<WzglDto> wzglList=wzglService.getPagedDtoList(wzglDto);
        map.put("rows", wzglList);
        map.put("total", wzglDto.getTotalNumber());
        return map;
    }

    /**
     * 根据病原名称获取病原信息
     * @param
     * @return
     */
    @RequestMapping(value="/inspection/pagedataGetInfoByVirusName")
    @ResponseBody
    public Map<String, Object> pagedataGetInfoByVirusName(WzglDto wzglDto){
        Map<String, Object> map= new HashMap<>();
        if (StringUtil.isNotBlank(wzglDto.getEntire())){
            List<WzglDto> wzglList=wzglService.getPagedDtoList(wzglDto);
            map.put("rows", wzglList);
            map.put("status", "success");
            map.put("total", wzglDto.getTotalNumber());
        }else{
            map.put("status","fail");
        }
        return map;
    }



    /**
     * 通过物种id获取参考文献信息
     * @param
     * @return
     */
    @RequestMapping(value="/wzjb/pagedataGetInfoById")
    @ResponseBody
    public Map<String, Object> pagedataGetInfoById(WzglDto wzglDto){
        Map<String, Object> map= new HashMap<>();
        if (StringUtil.isNotBlank(wzglDto.getWzid()) || StringUtil.isNotBlank(wzglDto.getWzzwm())){
            WzglDto dto = wzglService.getDto(wzglDto);
            if (null != dto && StringUtil.isNotBlank(dto.getWzid())){
                CkwxglDto ckwxglDto = new CkwxglDto();
                ckwxglDto.setWzid(dto.getWzid());
                List<CkwxglDto> dtoList = ckwxglService.getDtoList(ckwxglDto);
                map.put("dto", dto);
                map.put("dtoList", dtoList);
                map.put("status", "success");
            }else{
                map.put("status","fail");
            }
        }else{
            map.put("status","fail");
        }
        return map;
    }
    /**
     * 查看页面
     * @param wzglDto
     * @return
     */
    @RequestMapping("/wzjb/viewWzjb")
    public ModelAndView viewOne(WzglDto wzglDto) {
        ModelAndView mav=new ModelAndView("wechat/sjxx/wzjb_view");
        WzglDto wzglDto_t = wzglService.getDtoByWzid(wzglDto);
        mav.addObject("wzglDto",wzglDto_t );
        return mav;
    }
    /**
     * 修改页面
     * @param wzglDto
     * @return
     */
    @RequestMapping("/wzjb/modWzjb")
    public ModelAndView modWzjb(WzglDto wzglDto) {
        ModelAndView mav=new ModelAndView("wechat/sjxx/wzjb_add");
        WzglDto wzglDto_t= wzglService.getDtoByWzid(wzglDto);
        wzglDto_t.setFormAction("modSaveWzjb");
        mav.addObject("wzglDto", wzglDto_t);
        return mav;
    }
    /**
     * 修改保存
     * @param wzglDto
     * @return
     */
    @RequestMapping("/wzjb/modSaveWzjb")
    @ResponseBody
    public Map<String,Object> updateWzjb(WzglDto wzglDto){
        Map<String,Object> map= new HashMap<>();
        if(wzglDto.getCrbjb().equals("0")){
            wzglDto.setCrbjb("甲级");
        }else if(wzglDto.getCrbjb().equals("1")){
            wzglDto.setCrbjb("乙级");
        }else if(wzglDto.getCrbjb().equals("2")){
            wzglDto.setCrbjb("丙级");
        }else{
            wzglDto.setCrbjb(null);
        }
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        wzglDto.setXgsj(dateformat.format(date));
        User user=getLoginInfo();
        wzglDto.setXgry(user.getYhid());
        boolean iSsuccess=wzglService.update(wzglDto);
        map.put("status",iSsuccess?"success":"fail");
        map.put("message", iSsuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }
    /**
     * 物种拦截页面
     * @param wzglDto
     * @return
     */
    @RequestMapping(value ="/wzjb/pageListWzlj")
    public ModelAndView viewWzlj(WzglDto wzglDto) {
        ModelAndView mav=new ModelAndView("wechat/sjxx/wzlj_List");
        mav.addObject("WzglDto", wzglDto);
        return mav;
    }

    /**
     * 物种拦截列表信息
     * @param wzglDto
     * @return
     */
    @RequestMapping("/wzjb/pageGetListWzlj")
    @ResponseBody
    public Map<String, Object> wzljPage(WzglDto wzglDto){
        Map<String, Object> map= new HashMap<>();
        List<WzglDto> wzglList=wzglService.getPagedWzljList(wzglDto);
        map.put("rows", wzglList);
        map.put("total", wzglDto.getTotalNumber());
        return map;
    }
    /**
     * 拦截列表查看页面
     * @param wzglDto
     * @return
     */
    @RequestMapping("/wzjb/viewWzlj")
    public ModelAndView selectWzlj(WzglDto wzglDto) {
        ModelAndView mav=new ModelAndView("wechat/sjxx/wzlj_view");
        WzglDto wzglDto_t = wzglService.getDtoByLjid(wzglDto);
        wzglDto.setFid(wzglDto_t.getFid());
        WzglDto wzglDto_fid = wzglService.getDtoByFid(wzglDto);
        mav.addObject("wzglDto",wzglDto_t );
        mav.addObject("wzglDto_fid",wzglDto_fid );
        return mav;
    }
    /**
     * 物种信息
     * @param wzglDto
     * @return
     */
    @RequestMapping("/wzjb/pagedataGetInfoByWzid")
    @ResponseBody
    public Map<String, Object> pagedataGetInfoByWzid(WzglDto wzglDto){
        Map<String, Object> map= new HashMap<>();
        WzglDto dtoById = wzglService.getDtoById(wzglDto.getWzid());
        map.put("wzglDto", dtoById);
        return map;
    }
}
