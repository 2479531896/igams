package com.matridx.localization.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.util.valuate.EvaluableExpression;
import com.matridx.igams.common.util.valuate.function.Function;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ConvertJsonUtil
 *
 * @author LittleRedBean
 * @version 1.0
 * @date 2024/7/17 上午10:53
 */
public class ConvertJsonUtil {
    private Logger log = LoggerFactory.getLogger(ConvertJsonUtil.class);

    // 需要初始化的公式
    private List<String> functions = Arrays.asList("isNotBlank","isBlank","substring","length","indexOf","append","equals","dateParse","dateFormat","replace","replaceAll");

    private Map<String, Function> functionMap;
    /**
     * 初始化格式化公式
     * @return
     */
    public void initFormatFunction(){
        ConvertValuateFunction convertValuateFunction = new ConvertValuateFunction();
        Map<String, Function> temp_functionMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(functions)){
            for (String function : functions) {
                switch (function) {
                    case "isNotBlank":
                        temp_functionMap.put("isNotBlank", convertValuateFunction.isNotBlank);// isNotBlank(a)
                        break;
                    case "isBlank":
                        temp_functionMap.put("isBlank", convertValuateFunction.isBlank);// isBlank(a)
                        break;
                    case "substring":
                        temp_functionMap.put("substring", convertValuateFunction.substring);// substring(string,beginIndex,endIndex)
                        break;
                    case "length":
                        temp_functionMap.put("length", convertValuateFunction.length);// length(a)
                        break;
                    case "indexOf":
                        temp_functionMap.put("indexOf", convertValuateFunction.indexOf);// indexOf(String, a)
                        break;
                    case "append":
                        temp_functionMap.put("append", convertValuateFunction.append);// append(a,b,c)
                        break;
                    case "equals":
                        temp_functionMap.put("equals", convertValuateFunction.equals);// equals(a,b)
                        break;
                    case "dateParse":
                        temp_functionMap.put("dateParse", convertValuateFunction.dateParse);// dateParse(a,日期格式)
                        break;
                    case "dateFormat":
                        temp_functionMap.put("dateFormat", convertValuateFunction.dateFormat);// dateFormat(a,日期格式)
                        break;
                    case "replace":
                        temp_functionMap.put("replace", convertValuateFunction.replace);// replace(str,target,replacement)
                        break;
                    case "replaceAll":
                        temp_functionMap.put("replaceAll", convertValuateFunction.replaceAll);// replaceAll(str,regex,replacement)
                        break;
                }
            }
        }
        this.functionMap = temp_functionMap;
    }

    /**
     * 转换File(File内以Line格式存储,一行一条数据,每个字段之间有分割符)
     * @param filePath
     * @param settings
     */
    public List<Map<String,Object>> convertLineFile(String filePath, String lineSplit,Map<String,Object> settings){
        String result = readLineFileToString(filePath,lineSplit);
        Object parse = JSON.parse(result);
        if (parse instanceof List){
            return convertList((List<Map<String, Object>>) parse,settings);
        } else if (parse instanceof Map) {
            return convertMap((Map<String, Object>) parse,settings);
        }
        return null;
    }

    /**
     * 读取文件并转换成String
     * @param filePath
     * @return
     */
    public static String readJsonFileToString(String filePath){
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                contentBuilder.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return contentBuilder.toString();
    }
    /**
     * 读取文件并转换成String
     * @param filePath
     * @return
     */
    public static String readJsonLineFileToString(String filePath){
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append("[");
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                contentBuilder.append(line).append(System.lineSeparator());
                contentBuilder.append(",");
            }
            contentBuilder.deleteCharAt(contentBuilder.length() - 1);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        contentBuilder.append("]");
        return contentBuilder.toString().trim().replaceAll("﻿","");
    }

    /**
     * 读取文件并转换成List<String>
     * @param filePath
     * @return
     */
    public static String readLineFileToString(String filePath,String split){
        List<Map<String,Object>> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parameters = line.split(split);
                Map<String, Object> lineMap = new HashMap<>();
                for (int i = 0; i < parameters.length; i++) {
                    lineMap.put("parameter" + i, parameters[i]);
                }
                list.add(lineMap);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return JSON.toJSONString(list);
    }



    /**
     * 转换Object
     * @param result
     * @param settings
     */
    public List<Map<String, Object>> convertObject(Object result, Map<String,Object> settings){
        if (result instanceof String){
            return convertObject(JSON.parse(result.toString()),settings);
        } else if (result instanceof List){
            return convertList((List<Map<String, Object>>) result,settings);
        } else if (result instanceof Map) {
            return convertMap((Map<String, Object>) result,settings);
        }
        return null;
    }
    
    /**
     * 转换Map
     * @param infoMap
     * @param settings
     */
    public List<Map<String, Object>> convertMap(Map<String, Object> infoMap, Map<String,Object> settings){
        return infoConvert(infoMap, settings);
    }

    /**
     * 转换List
     * @param infoList
     * @param settings
     */
    public List<Map<String, Object>> convertList( List<Map<String, Object>> infoList, Map<String,Object> settings){
        return infoConvert(infoList, settings);
    }
    /**
     * 转换MapString
     * @param result
     * @param settings
     */
    public List<Map<String,Object>> convertMapString(String result, Map<String,Object> settings){
        Map<String, Object> infoMap = JSONObject.parseObject(result,Map.class);
        return convertMap(infoMap,settings);
    }

    /**
     * 转换ListString
     * @param result
     * @param settings
     */
    public List<Map<String,Object>> convertListString(String result, Map<String,Object> settings){
        List<Map<String, Object>> infoList = JSONObject.parseObject(result,List.class);
        return convertList(infoList,settings);
    }

    /**
     * 转换消息
     * @param info
     * @param settings
     * @return
     */
    public List<Map<String,Object>> infoConvert(Object info, Map<String,Object> settings){
        initFormatFunction();
        List<Map<String, Object>> list = new ArrayList<>();
        if (info instanceof Map){
            if (settings.containsKey("list")){
                String listKey = (String) settings.get("list");
                Map<String, Object> continueMap = new HashMap<>();
                continueMap.putAll((Map<String, Object>) info);
                if (listKey.contains(".")){
                    String[] listKeys = listKey.split("\\.");
                    for (int i = 0; i < listKeys.length; i++) {
                        Object o_child = continueMap.get(listKeys[i]);
                        if (i == listKeys.length - 1 && o_child instanceof List){
                            list = (List<Map<String, Object>>) o_child;
                        }else {
                            continueMap = (Map<String, Object>) o_child;
                        }
                    }
                }else {
                    list = (List<Map<String, Object>>) continueMap.get(listKey);
                }
            }
        } else if (info instanceof List) {
            list = (List<Map<String, Object>>) info;
        }
        return infoToList(list,settings);
    }

    /**
     * 将infoList转换为List<Map>
     * @param infoList
     * @param settings
     * @return
     */
    public List<Map<String,Object>> infoToList(List<Map<String, Object>> infoList, Map<String,Object> settings){
        List<Map<String,Object>> list = new ArrayList<>();
        for (Map<String, Object> infoMap : infoList) {
            list.add(infoToMap(infoMap,settings));
        }
        return list;
    }

    /**
     * 将infoMap转换为Map
     * @param infoMap
     * @param settings
     * @return
     */
    public Map<String,Object> infoToMap(Map<String, Object> infoMap, Map<String,Object> settings){
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("info",infoMap);
        Map<String,Object> qtxxMap = new HashMap<>();
        qtxxMap.putAll(infoMap);
        dataMap.put("qtxx",qtxxMap);
        // 获取字段配置参数
        Map<String,Object> parameters = (Map<String, Object>) settings.get("parameters");
        for (String key : parameters.keySet()) {
            Object o = parameters.get(key);
            String value = "";
            if (o instanceof String){
                value = getValue((String) o,dataMap).toString();
            }
            putKeyValueIntoMap(key,value,dataMap);
        }
        // 移除infoMap
        dataMap.remove("info");
        return dataMap;
    }

    /**
     * 获取值
     * @param formatStr
     * @param dataMap
     * @return
     */
    public Object getValue(String formatStr, Map<String, Object> dataMap){
        if (StringUtil.isNotBlank(formatStr)){
            //parameter字段设置中直接存在value,直接对应
            return getFormatValue(formatStr,dataMap);
        } else {
            return "";
        }
    }

    /**
     * 获取格式化后的值
     * append({ybbh},{info.Item.SAMPLE_NUMBER},',',{info.Item.GROUP_ID})
     * @param formatStr 设置
     * @param dataMap
     * @return
     */
    private Object getFormatValue(String formatStr, Map<String, Object> dataMap){
        if (StringUtil.isNotBlank(formatStr)){
            //formatStr是否包含functions的任意元素
            if (functions.stream().anyMatch(formatStr::contains)){
                try {
                    Map<String, Object> parameters = new HashMap<>();
                    int index = 0;
                    while (formatStr.contains("{") && formatStr.contains("}")){
                        String key = formatStr.substring(formatStr.indexOf("{") + 1, formatStr.indexOf("}"));
                        String value = getKeyValueInDataMap(key, dataMap);
                        formatStr = formatStr.replace("{"+key+"}","parameter"+index);
                        parameters.put("parameter"+index,value);
                        index ++;
                    }
                    EvaluableExpression evaluable = new EvaluableExpression(formatStr,functionMap);
                    Object evaluate = evaluable.evaluate(parameters);
                    return evaluate;
                } catch (Exception e) {
                    return formatStr;
                }
            } else if (formatStr.contains("{") && formatStr.contains("}")){
                if (getCountOfKeyInStr(formatStr,"{") == 1 && formatStr.indexOf("{") == 0 && formatStr.indexOf("}") == formatStr.length()-1){
                    //formatStr为一整个key
                    if (dataMap != null){
                        removeKeyValueInQtxxMap(formatStr.substring(formatStr.indexOf("{") + 1, formatStr.indexOf("}")),dataMap);
                    }
                }
                while (formatStr.contains("{") && formatStr.contains("}")){
                    String key = formatStr.substring(formatStr.indexOf("{") + 1, formatStr.indexOf("}"));
                    String value = getKeyValueInDataMap(key, dataMap);
                    formatStr = formatStr.replace("{"+key+"}",(StringUtil.isNotBlank(value)?value:""));
                }
                return formatStr;
            } else {
                return formatStr;
            }
        } else {
            return "";
        }
    }

    /**
     * 获取值
     * @param key 对应接收样本信息中的关键词
     *                    {info.Item.REQUISITION_ID}
     *                    {yblx.csid}
     * @param data
     * @return
     */
    private String getKeyValueInDataMap(String key, Object data) {
        String valueStr = "";
        if (StringUtil.isNotBlank(key)){
            if (key.contains(".")) {
                String key_first = key.substring(0, key.indexOf("."));
                String key_last = key.substring(key.indexOf(".") + 1);
                if (data instanceof List){
                    List<Object> list = (List<Object>) data;
                    for (Object object : list) {
                        for (String keySet : ((JSONObject) object).keySet()) {
                            if (key_first.equals(keySet)){
                                Object value = ((JSONObject) object).get(key_first);
                                return getKeyValueInDataMap(key_last, value);
                            }
                        }
                    }
                } else if (data instanceof Map) {
                    if (((Map<String, Object>) data).containsKey(key_first)) {
                        Object value = ((Map<String, Object>) data).get(key_first);
                        return getKeyValueInDataMap(key_last, value);
                    }
                } else if (data instanceof String){
                    return data.toString();
                } else {
                    try {
                        String tmpVar = key_first.substring(0, 1).toUpperCase() + key_first.substring(1);
                        Method method = data.getClass().getMethod("get" + tmpVar);
                        return getKeyValueInDataMap(key_last, method.invoke(data));
                    } catch (Exception e) {
                        return valueStr;
                    }
                }
            } else {
                if (data instanceof List){
                    List<Object> list = (List<Object>) data;
                    for (Object object : list) {
                        for (String keySet : ((JSONObject) object).keySet()) {
                            if (key.equals(keySet)){
                                Object value = ((JSONObject) object).get(key);
                                return (String) value;
                            }
                        }
                    }
                } else if (data instanceof Map) {
                    if (((Map<String, Object>) data).containsKey(key)) {
                        Object value = ((Map<String, Object>) data).get(key);
                        return (String) value;
                    }
                } else if (data instanceof String){
                    return data.toString();
                } else {
                    try {
                        String tmpVar = key.substring(0, 1).toUpperCase() + key.substring(1);
                        Method method = data.getClass().getMethod("get" + tmpVar);
                        return (String) method.invoke(data);
                    } catch (Exception e) {
                        return valueStr;
                    }
                }
            }
        }
        return valueStr;
    }

    /**
     * 获取key在str中出现的次数
     * @param str
     * @param key
     * @return
     */
    private int getCountOfKeyInStr(String str, String key){
        int count = 0;
        int index = str.indexOf(key);
        while (index >= 0) {
            count++;
            index = str.indexOf(key, index + 1);
        }
        return count;
    }

    /**
     * 删除值
     * @param key 对应接收样本信息中的关键词
     *                    {info.Item.REQUISITION_ID}
     *                    {yblx.csid}
     * @param data
     * @return
     */
    private Object removeKeyValueInQtxxMap(String key, Object data) {
        if (StringUtil.isNotBlank(key)){
            if (key.contains(".")) {
                String key_first = key.substring(0, key.indexOf("."));
                String key_last = key.substring(key.indexOf(".") + 1);
                if ("info".equals(key_first)){
                    key_first = "qtxx";
                }
                if (data instanceof List){
                    List<Object> list = (List<Object>) data;
                    for (Object object : list) {
                        for (String keySet : ((JSONObject) object).keySet()) {
                            if (key_first.equals(keySet)){
                                Object value = ((JSONObject) object).get(key_first);
                                ((JSONObject) object).put(key_first, removeKeyValueInQtxxMap(key_last, value));
                                return list;
                            }
                        }
                    }
                } else if (data instanceof Map) {
                    if (((Map<String, Object>) data).containsKey(key_first)) {
                        Object value = ((Map<String, Object>) data).get(key_first);
                        ((Map<String, Object>) data).put(key_first, removeKeyValueInQtxxMap(key_last, value));
                        return data;
                    }
                }
            } else {
                if (data instanceof List){
                    List<Object> list = (List<Object>) data;
                    for (Object object : list) {
                        for (String keySet : ((JSONObject) object).keySet()) {
                            if (key.equals(keySet)){
                                list.remove(object);
                                return list;
                            }
                        }
                    }
                } else if (data instanceof Map) {
                    if (((Map<String, Object>) data).containsKey(key)) {
                        ((Map<String, Object>) data).remove(key);
                        return data;
                    }
                }
            }
        }
        return data;
    }

    /**
     * 将key和value放入dataMap
     * @param key
     * @param value
     * @param dataMap
     */
    private void putKeyValueIntoMap(String key, Object value, Map<String, Object> dataMap){
        if (key.contains(".")){
            String key_first = key.substring(0, key.indexOf("."));
            String key_last = key.substring(key.lastIndexOf(".") + 1);
            Map<String, Object> childDataMap = new HashMap<>();
            if (dataMap.containsKey(key_first)){
                childDataMap = (Map<String, Object>) dataMap.get(key_first);
            }
            putKeyValueIntoMap(key_last,value,childDataMap);
            dataMap.put(key_first,childDataMap);
        } else{
            dataMap.put(key,value);
        }
    }

}
