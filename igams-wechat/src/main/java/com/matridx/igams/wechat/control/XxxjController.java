package com.matridx.igams.wechat.control;


import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.redis.RedisStreamUtil;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.wechat.dao.entities.XxxjDto;
import com.matridx.igams.wechat.service.svcinterface.IXxxjService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/xxxj")
public class XxxjController extends BaseController {

    @Autowired
    private IXxxjService xxxjService;
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IFjcfbService fjcfbService;

    @Autowired
    RedisStreamUtil redisStreamUtil;

    /**
     * 信息小结列表页面
     * @return
     */
    @RequestMapping("/xxxj/pageListXxxj")
    public ModelAndView getXxxjPage(){
        List<JcsjDto> xxxjList=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.XXXJ_TYPE.getCode());
        ModelAndView mav=new ModelAndView("wechat/xxxj/xxxj_list");
        mav.addObject("xxxjList",xxxjList);


        return mav;
    }

    /**
     * 列表数据
     * @param xxxjDto
     * @return
     */
    @RequestMapping("/xxxj/pageGetListXxxj")
    @ResponseBody
    public Map<String, Object> getLibraryPageList(XxxjDto xxxjDto) {
        List<XxxjDto> xxxjList;
        xxxjList=xxxjService.getPagedDtoList(xxxjDto);
        Map<String, Object> map = new HashMap<>();
        map.put("total", xxxjDto.getTotalNumber());
        map.put("rows", xxxjList);
        return map;
    }

    /**
     * 跳转新增界面
     */
    @RequestMapping("/xxxj/addXxxj")
    @ResponseBody
    public ModelAndView getaddXxxjPage(XxxjDto xxxjDto) {
        List<JcsjDto> xxxjList=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.XXXJ_TYPE.getCode());
        ModelAndView mav=new ModelAndView("wechat/xxxj/xxxj_add");
        xxxjDto.setFormAction("addSaveXxxj");
        mav.addObject("xxxjList",xxxjList);
        mav.addObject("fjcfbDtos",new ArrayList<>());
        mav.addObject("ywlx", BusTypeEnum.IMP_ZZXJ_APPLICATION_FILE.getCode());
        mav.addObject("xxxjDto",xxxjDto);
        return mav;
    }

    /**
     * 新增提交保存
     */
    @RequestMapping("/xxxj/addSaveXxxj")
    @ResponseBody
    public Map<String, Object> addSaveXxxj(XxxjDto xxxjDto, HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo();
        xxxjDto.setLrry(user.getYhid());
        try {
            xxxjService.progressSaveXxxj(xxxjDto);
            map.put("status", "success");
            map.put("message", "保存成功");
        }catch (Exception e){
            map.put("status",  "fail");
            map.put("message", "保存失败");
        }
        return map;
    }

    /**
     * 跳转修改界面
     */
    @RequestMapping("/xxxj/modXxxj")
    @ResponseBody
    public ModelAndView modXxxjPage(XxxjDto xxxjDto) {
        ModelAndView mav = new ModelAndView("wechat/xxxj/xxxj_add");
        XxxjDto dto=xxxjService.getDto(xxxjDto);
        dto.setFormAction("modSaveXxxj");
        FjcfbDto fjcfbDto=new FjcfbDto();
        fjcfbDto.setYwlx(BusTypeEnum.IMP_ZZXJ_APPLICATION_FILE.getCode());
        fjcfbDto.setYwid(xxxjDto.getXjid());
        List<FjcfbDto> fjcfbDtos=fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        List<JcsjDto> xxxjList=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.XXXJ_TYPE.getCode());
        mav.addObject("fjcfbDtos",fjcfbDtos);
        mav.addObject("ywlx", BusTypeEnum.IMP_ZZXJ_APPLICATION_FILE.getCode());
        mav.addObject("xxxjDto",dto);
        mav.addObject("xxxjList",xxxjList);
        return mav;
    }

    /**
     * 修改提交保存
     */
    @RequestMapping("/xxxj/modSaveXxxj")
    @ResponseBody
    public Map<String, Object> modSaveXxxj(XxxjDto xxxjDto,HttpServletRequest request) {
        boolean isSuccess;
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo();
        xxxjDto.setXgry(user.getYhid());
        try {
            xxxjService.progressSaveXxxj(xxxjDto);
            map.put("status",  "success");
            map.put("message", "修改成功");
        }catch (Exception e){
            map.put("status",  "fail");
            map.put("message", "保存失败");
        }
        return map;
    }

    /**
     * 修改提交保存
     */
    @RequestMapping("/xxxj/delXxxj")
    @ResponseBody
    public Map<String, Object> delXxxj(XxxjDto xxxjDto,HttpServletRequest request) {
        boolean isSuccess;
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo();
        xxxjDto.setXgry(user.getYhid());
        try {
            xxxjService.deleteById(xxxjDto);
            map.put("status",  "success");
            map.put("message", "删除成功");
        }catch (Exception e){
            map.put("status",  "fail");
            map.put("message", "删除失败");
        }
        return map;
    }
    /**
     * 跳转信息小结查看页面
     */
    @RequestMapping(value = "/xxxj/viewXxxj")
    @ResponseBody
    public ModelAndView getXxxjViewPage(XxxjDto xxxjDto,HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("wechat/xxxj/xxxj_view");
        XxxjDto dto=xxxjService.getDto(xxxjDto);
        FjcfbDto fjcfbDto=new FjcfbDto();
        fjcfbDto.setYwlx(BusTypeEnum.IMP_ZZXJ_APPLICATION_FILE.getCode());
        fjcfbDto.setYwid(xxxjDto.getXjid());
        List<FjcfbDto> fjcfbDtos=fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        mav.addObject("fjcfbDtos",fjcfbDtos);
        mav.addObject("xxxjDto",dto);
        return mav;
    }
}
