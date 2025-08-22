package com.matridx.igams.wechat.control;

import com.matridx.igams.wechat.service.svcinterface.ISjwlxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxStatisticService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hmz
 * @date2022/07/28 version V1.0
 **/
@Controller
@RequestMapping("/quality")
public class QualityControl {
    @Autowired
    private ISjwlxxService sjwlxxService;
    @Autowired
    private ISjxxStatisticService sjxxStatisticService;

    /**
     * 质控统计接口
     * @return
     */
    @RequestMapping("/pagedataGetQualityControlStatistics")
    @ResponseBody
    public Map<String,Object> getQualityControlStatistics(HttpServletRequest request, String rq, String rqStart, String rqEnd) {
        Map<String,Object> resutlMap = new HashMap<>();
        Map<String,Object> sqlMap = new HashMap<>();
        if( sqlMap.get("rqStart") == null || sqlMap.get("rqEnd") == null){
            if (StringUtil.isBlank(rq)) {
                rq = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            }
            sqlMap.put("rq",rq);
        }
        List<Map<String, Object>> transportationTimeListByRq = sjwlxxService.getTransportationTimeListByRq(sqlMap);
        List<Map<String, Object>> qualifiedRateByRq = sjxxStatisticService.getQualifiedRateByRq(sqlMap);
        List<Map<String, Object>> disqualificationRateWithTypeByRq = sjxxStatisticService.getDisqualificationRateWithTypeByRq(sqlMap);
        resutlMap.put("transportationTimeListByRq",transportationTimeListByRq);
        resutlMap.put("qualifiedRateByRq",qualifiedRateByRq);
        resutlMap.put("disqualificationRateWithTypeByRq",disqualificationRateWithTypeByRq);
        return resutlMap;
    }
}
