package com.matridx.igams.common.util;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 节假日工具类
 */
@Component
public class HolidayUtil {
    @Autowired
    RedisUtil redisUtil;
    /**
     * 发送get请求
     */
    private  String get(String url){
        StringBuilder inputLine = new StringBuilder();
        String read;
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
            urlConnection.setReadTimeout(30 * 1000);
            urlConnection.setConnectTimeout(30 * 1000);
            urlConnection.setRequestProperty("Charset", "UTF-8");
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36)");
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
            while ((read = in.readLine()) != null) {
                inputLine.append(read);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return inputLine.toString();
    }

    /**
     * 调用免费API查询全年工作日、周末、法定节假日、节假日调休补班数据
     * 1、调用 https://api.apihubs.cn/holiday/get?size=500&year=2021 查询全年日历（含周末）
     * 2、调用 https://timor.tech/api/holiday/year/2021 查询全年节假日、调休
     */
    @SuppressWarnings("unchecked")
    public Map<String,HolidayVo> getAllHolidayByYear(String year) {
        Object allHolidayStatus = redisUtil.get("Holiday:allHolidayStatus_" + year);
        if (allHolidayStatus!=null){
            Map<String,HolidayVo> holidayMap = new HashMap<>();
            Map<String,Object> holidayMapRedis = JSON.parseObject(String.valueOf(allHolidayStatus), Map.class);
            for (String s : holidayMapRedis.keySet()) {
                holidayMap.put(s,JSON.parseObject(String.valueOf(holidayMapRedis.get(s)),HolidayVo.class));
            }
            return holidayMap;
        }
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String,HolidayVo> holidayVoMap = new HashMap<>();
        //查询全年日历包含周末
        Object allDay = redisUtil.get("Holiday:allDayJson_"+year);
        List<HolidayResult> allDayDataList = new ArrayList<>();
        if (allDay!=null){
            allDayDataList = JSON.parseArray(String.valueOf(allDay),HolidayResult.class);
        }else {
            String allDayJson = this.get("https://api.apihubs.cn/holiday/get?size=500&year="+year);
            Map<String,Object> allDayMap = JSON.parseObject(allDayJson,Map.class);
            Map<String,Object> allDayData = (Map<String,Object>)allDayMap.get("data");
            List<Object> list = (List<Object>)allDayData.get("list");
            for (Object o : list) {
                allDayDataList.add(JSON.parseObject(String.valueOf(o),HolidayResult.class));
            }
            redisUtil.set("Holiday:allDayJson_"+year,JSON.toJSONString(allDayDataList));
        }
        allDayDataList.forEach((value) -> {
            HolidayVo holidayVo = new HolidayVo();
            String date = value.getDate();
            holidayVo.setData(date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8));
            String STATUS = "0";
            String msg = "工作日";
            if("1".equals(value.getWeekend())){
                STATUS = "1";
                msg = "周末";
            }
            holidayVo.setStatus(STATUS);
            holidayVo.setMsg(msg);
            holidayVoMap.put(holidayVo.getData(),holidayVo);
        });
        Object allHoliday = redisUtil.get("Holiday:allHolidayJson_"+year);
        Map<String,Object> holidayResultMap;
        if (allHoliday!=null){
            holidayResultMap = (Map<String,Object>)JSON.parseObject(String.valueOf(allHoliday),Map.class);
        }else {
            //查询全年节假日、调休
            String allHolidayJson = this.get("https://timor.tech/api/holiday/year/"+year + "/");
            Map<String,Object> holidayMap = JSON.parseObject(allHolidayJson,Map.class);
            holidayResultMap = (Map<String,Object>)holidayMap.get("holiday");
            redisUtil.set("Holiday:allHolidayJson_"+year,JSON.toJSONString(holidayResultMap));
        }
        holidayResultMap.forEach((key,value) -> {
            HolidayVo holidayVo = new HolidayVo();
            Map<String,Object> value1 = (Map<String,Object>) value;
            String dateTime = value1.get("date").toString();

            holidayVo.setData(dateTime);
            String STATUS = "2";
            String msg = "法定节假日("+value1.get("name").toString()+")";
            if(value.toString().contains("调休")||value.toString().contains("补班")){
                STATUS = "3";
                msg = "节假日调休补班("+value1.get("target").toString()+")";
            }
            holidayVo.setStatus(STATUS);
            holidayVo.setMsg(msg);
            holidayVoMap.replace(holidayVo.getData(),holidayVo);
        });
        redisUtil.set("Holiday:allHolidayStatus_"+year,JSON.toJSONString(holidayVoMap));
        return holidayVoMap;
    }
}
class HolidayResult{
    private String date;
    private String name;
    private String target;
    private String weekend;
    private String month;
    private String year;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getWeekend() {
        return weekend;
    }

    public void setWeekend(String weekend) {
        this.weekend = weekend;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}


