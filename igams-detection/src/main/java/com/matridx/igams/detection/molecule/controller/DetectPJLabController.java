package com.matridx.igams.detection.molecule.controller;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxxDto;
import com.matridx.igams.detection.molecule.service.svcinterface.IFzjcxxPJService;
import com.matridx.igams.detection.molecule.service.svcinterface.IKzbglService;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@code @author:JYK}
 */
@Controller
@RequestMapping("/detectPJLab")
public class DetectPJLabController extends BaseController {
    @Autowired
    IFzjcxxPJService fzjcxxPJService;
    @Autowired
    IJcsjService jcsjService;
    @Autowired
    IXxglService xxglService;
    @Autowired
    IKzbglService kzbglService;
    @Autowired
    private RedisUtil redisUtil;
    private final Logger log = LoggerFactory.getLogger(DetectPJLabController.class);
    /**
     *增加一个实验列表页面
     */
    @RequestMapping("/detectPJLab/pageListDetectPJlab")
    public ModelAndView pageListDetectlab(FzjcxxDto fzjcxxDto) {
        ModelAndView mav = new ModelAndView("detection/detectPJLab/detectPJLab_list");
        mav.addObject("samplelist",redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.GENERALSAMPLE_TYPE.getCode()));
        mav.addObject("fzjcxxDto",fzjcxxDto);
        return mav;
    }

    /**
     * 获取实验列表数据
     */
    @RequestMapping(value = "/detectPJLab/pageGetListDetectPJlab")
    @ResponseBody
    public Map<String, Object> getPageDetectPJlab(FzjcxxDto fzjcxxDto) {
        Map<String, Object> map = new HashMap<>();
        DBEncrypt dbEncrypt=new DBEncrypt();
        if(StringUtils.isNotBlank(fzjcxxDto.getZjh()))
            fzjcxxDto.setZjh(dbEncrypt.eCode(fzjcxxDto.getZjh()));
        //区分普检数据
        List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
        if(jclxList.size()>0){
            for(JcsjDto jcsjDto : jclxList){
                if(fzjcxxDto.getJclx().equals(jcsjDto.getCsdm()))
                    fzjcxxDto.setJclx(jcsjDto.getCsid());
            }
        }
        User user=getLoginInfo();
        List<Map<String,String>> jcdwList=fzjcxxPJService.getJsjcdwByjsid(user.getDqjs());
        if(!CollectionUtils.isEmpty(jcdwList) && "1".equals(jcdwList.get(0).get("dwxdbj"))) {
            List<String> strList=new ArrayList<>();
            for (Map<String, String> stringStringMap : jcdwList) {
                if (stringStringMap.get("jcdw") != null) {
                    strList.add(stringStringMap.get("jcdw"));
                }
            }
            if(strList.size()>0) {
                fzjcxxDto.setJcdwxz(strList);
            }
        }
        List<FzjcxxDto> fzjcxxlist = fzjcxxPJService.getPagedDetectPJlab(fzjcxxDto);
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
}
