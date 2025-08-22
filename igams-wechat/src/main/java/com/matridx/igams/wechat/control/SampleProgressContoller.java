package com.matridx.igams.wechat.control;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.service.svcinterface.IHbqxService;
import com.matridx.igams.wechat.service.svcinterface.ISjhbxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxService;
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
@RequestMapping("/sampleprogress")
public class SampleProgressContoller extends BaseController {

    @Autowired
    private ISjxxService sjxxService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    private IHbqxService hbqxService;
    @Autowired
    private ISjhbxxService sjhbxxService;

    /**
     * 跳转标本进度列表页面
     * @return
     */
    @RequestMapping(value = "/sampleprogress/pageListSampleProgress")
    public ModelAndView pageListSampleProgress(SjxxDto sjxxDto) {
        ModelAndView mav =new ModelAndView("wechat/sampleprogress/sampleProgress_List");
        mav.addObject("sjxxDto", sjxxDto);
        mav.addObject("samplelist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));//标本类型
        mav.addObject("detectlist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode()));//检测项目r
        mav.addObject("detectionList", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位
        return mav;
    }

    /**
     * 钉钉跳转标本进度列表页面
     * @return
     */
    @RequestMapping(value ="/sampleprogress/pagedataSampleProgress")
    @ResponseBody
    public Map<String,Object> pagedataSampleProgress(){
        Map<String, Object> map= new HashMap<>();
        map.put("samplelist",  redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));//标本类型
        map.put("detectlist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode()));//检测项目
        map.put("detectionList", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位
        return map;
    }

    /**
     * 获取标本进度列表
     * @param sjxxDto
     * @return
     */
    @RequestMapping(value ="/sampleprogress/pageGetListSampleProgress")
    @ResponseBody
    public Map<String,Object> pageGetListSampleProgress(SjxxDto sjxxDto){
        User user=getLoginInfo();
        List<Map<String,String>> list=sjxxService.getJsjcdwByjsid(user.getDqjs());
        List<String> jcdwList=new ArrayList<>();
        List<String> hbmcList=new ArrayList<>();
        if(list!=null&&list.size() > 0){
            if("1".equals(list.get(0).get("dwxdbj"))) {//判断当前角色是否进行了单位限制
                jcdwList= new ArrayList<>();
                for (int i = 0; i < list.size(); i++){
                    if(list.get(i).get("jcdw")!=null) {
                        jcdwList.add(list.get(i).get("jcdw"));
                    }
                }
                //判断伙伴权限
                List<String> hbqxList = hbqxService.getHbidByYhid(user.getYhid());
                if(hbqxList!=null && hbqxList.size()>0) {
                    hbmcList=sjhbxxService.getHbmcByHbid(hbqxList);
                }
                sjxxDto.setDwxzbj("1");
            }else {
                sjxxDto.setDwxzbj("0");
            }
        }else {
            sjxxDto.setDwxzbj("0");
        }

        sjxxDto.setJcdwxz(jcdwList);
        sjxxDto.setSjhbs(hbmcList);
        List<SjxxDto> sjxxDtos=sjxxService.getPagedSampleProgressList(sjxxDto);
        Map<String, Object> map= new HashMap<>();
        map.put("total", sjxxDto.getTotalNumber());
        map.put("rows", sjxxDtos);
        return map;
    }
}
