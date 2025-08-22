package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.wechat.dao.entities.SjhbxxDto;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.service.svcinterface.ISjnbbmService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxCommonService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

@Service
public class SjnbbmServiceImpl implements ISjnbbmService {

    @Autowired
    ISjxxCommonService sjxxCommonService;

    /**
     * 内部编码相关处理
     * @param key
     * @param sjxxDto
     * @param infoMap
     * @return
     */
    public Map<String, String> dealNbbmKeyMap(String key, SjxxDto sjxxDto, Map<String, String> infoMap){
        if ("nf".equals(key)){
            // 年份 处理
            dealNf(infoMap);
        }else if ("yf".equals(key)) {
            // 周数 处理
            dealYf(infoMap);
        }else if ("ts".equals(key)) {
            // 周数 处理
            dealTs(infoMap);
        }else if ("zs".equals(key)){
            // 周数 处理
            dealZs(infoMap);
        }else if ("lxdm".equals(key)){
            // 类型代码 处理
            dealLxdm(infoMap,sjxxDto);
        }else if ("jcxm".equals(key)){
            // 检测项目 处理(处理X项目)
            dealJcxm(infoMap, sjxxDto);
        }
        return infoMap;
    }

    /**
     * 年份处理
     * @param infoMap
     */
    void dealNf(Map<String, String> infoMap){
        String year = new SimpleDateFormat("yyyy", Locale.CHINESE).format(new Date().getTime());
        infoMap.put("nf",year);
    }
    /**
     * 月份处理
     * @param infoMap
     */
    void dealYf(Map<String, String> infoMap){
        Calendar currentDate = Calendar.getInstance();
        // 获取当前月份
        int yf = currentDate.get(Calendar.MONTH) + 1;
        infoMap.put("yf", String.valueOf(yf));

    }
    /**
     * 天数处理
     * @param infoMap
     */
    void dealTs(Map<String, String> infoMap){
        Calendar currentDate = Calendar.getInstance();
        // 获取当前天数
        int ts = currentDate.get(Calendar.DAY_OF_MONTH);
        infoMap.put("ts", String.valueOf(ts));

    }

    /**
     * 年份处理 长度两位
     * @param infoMap
     */
    void dealZs(Map<String, String> infoMap){
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);// 设置星期一为一周开始的第一天
        calendar.setTimeInMillis(System.currentTimeMillis());// 获得当前的时间戳
        String weekOfYear = String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR));// 获得当前日期属于今年的第几周
        if ("1".equals(weekOfYear) && calendar.get(Calendar.MONTH) == 11){
            calendar.add(Calendar.DATE, -7);
            weekOfYear = String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR)+1);// 获得当前日期属于今年的第几周
        }
        weekOfYear = weekOfYear.length()>1?weekOfYear:"0"+weekOfYear;
        infoMap.put("zs",weekOfYear);
    }

    /**
     * 类型代码处理（样本类型代码）
     * 特殊样本类型代码处理
     * 只处理样本类型cskz1为1和样本类型名称为血浆或全血的样本
     * @param infoMap
     */
    void dealLxdm(Map<String, String> infoMap,SjxxDto sjxxDto){
        if ( "1".equals(infoMap.get("lxcskz1")) && ("全血".equals(infoMap.get("yblxmc"))||"血浆".equals(infoMap.get("yblxmc")))){
            infoMap.put("lxdm","B");
            sjxxDto.setYblx_flg("1");//用于判断全血或血浆时，重新获取sygl信息
        }
    }

    /**
     * 检测项目处理(处理X项目)
     * @param infoMap
     * @param sjxxDto
     */
    void dealJcxm(Map<String, String> infoMap, SjxxDto sjxxDto){
        //X项目限制修改 因为新开展2.5的原因 D + XT，所以不再只限制杭州 2024-04-03
        if("IMP_REPORT_ONCO_QINDEX_TEMEPLATE".equals(infoMap.get("jcxmcskz3")) && !"科研".equals(infoMap.get("sjqfmc")) ) {
            SjhbxxDto sjhbxxDto = sjxxCommonService.getHbDtoFromId(sjxxDto);
            if (null == sjhbxxDto || StringUtil.isBlank(sjhbxxDto.getHbid())) {
                infoMap.put("jcxm","X");
            }else if(StringUtil.isNotBlank(sjhbxxDto.getXzlx()) && !"X".equals(sjhbxxDto.getXzlx())){
                infoMap.put("jcxm","X");
            }
        }
    }
}
