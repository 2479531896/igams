package com.matridx.las.netty.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.dao.entities.*;
import com.matridx.las.netty.enums.RedisStorageEnum;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class RedisSetAndGetUtil implements ApplicationRunner {
    @Autowired
    private  RedisUtil redisUtil;

private static  RedisSetAndGetUtil redisSetAndGetUtil = new RedisSetAndGetUtil();
    /**
     * 放入托盘信息
     * @param item
     * @param trayName
     * @param ma
     * @return
     */
    public  boolean setTray(String item,String trayName, Map<String ,YsybxxDto> ma) {
        if(StringUtil.isBlank(trayName)){
            return  false;
        }
        return  redisUtil.hsetNoTime(item, trayName, ma==null?null:JSONObject.toJSONString(ma, SerializerFeature.WriteMapNullValue));
    }

    /**
     * 查寻托盘信息
     * @param item
     * @param trayName
     * @return
     */
    public  Map<String,YsybxxDto> getTray(String item,String trayName) {
        if(StringUtil.isBlank(item)||StringUtil.isBlank(trayName)){
            return null;
        }
         Object  ob = redisUtil.hget(item, trayName);
          if(ob!=null){
              String jsonObject = ob.toString();
             Map<String,Object> map = (Map<String,Object>) JSONObject.parse(jsonObject);
              Map<String,YsybxxDto> map1 = new HashMap<String,YsybxxDto>();
             for(String key:map.keySet()){
                 JSONObject jsonObject1 = (JSONObject) map.get(key);
                 YsybxxDto av = JSONObject.toJavaObject(jsonObject1,YsybxxDto.class);
                 map1.put(key,av);
             }
              return map1;
          }
          return null;
    }

    /**
     * 放入枪头信息
     * @param item
     * @param trayName
     * @param ls
     * @return
     */
    public  boolean setGunhead(String item,String trayName, GunVehicleModel ls) {
        if(StringUtil.isBlank(trayName)){
            return  false;
        }
        return  redisUtil.hsetNoTime(item, trayName, ls==null?null:JSONObject.toJSONString(ls, SerializerFeature.WriteMapNullValue));
    }

    /**
     * 获取枪头信息
     * @param item
     * @param vehicleName
     * @return
     */
    public  GunVehicleModel getGunhead(String item,String vehicleName) {
        if(StringUtil.isBlank(item)||StringUtil.isBlank(vehicleName)){
            return null;
        }
        Object  ob = redisUtil.hget(item, vehicleName);
        if(ob!=null){
           GunVehicleModel gunVehicleModel = JSONObject.parseObject(ob.toString(),GunVehicleModel.class);
            return gunVehicleModel;
        }
        return null;
    }

    /**
     * 放入Ep管信息
     * @param item
     * @param trayName
     * @param ls
     * @return
     */
    public  boolean setEp(String item,String trayName, EpVehicleModel ls) {
        if(StringUtil.isBlank(trayName)){
            return  false;
        }
        return  redisUtil.hsetNoTime(item, trayName, ls==null?null:JSONObject.toJSONString(ls, SerializerFeature.WriteMapNullValue));
    }

    /**
     * 获取Ep管信息
     * @param item
     * @param vehicleName
     * @return
     */
    public  EpVehicleModel getEp(String item, String vehicleName) {
        if(StringUtil.isBlank(item)||StringUtil.isBlank(vehicleName)){
            return null;
        }
        Object  ob = redisUtil.hget(item, vehicleName);
        if(ob!=null){
            EpVehicleModel epVehicleModel = JSONObject.parseObject(ob.toString(),EpVehicleModel.class);
            return epVehicleModel;
        }
        return null;
    }

    /**
     * 放入仪器信息
     * @param item
     * @param trayName
     * @param ob
     * @return
     */
    public  boolean setInstrument(String item,String trayName,Object ob) {
        if(StringUtil.isBlank(trayName)){
            return  false;
        }
        return  redisUtil.hset(item, trayName, ob==null?null:JSONObject.toJSONString(ob, SerializerFeature.WriteMapNullValue),-1);
    }

    /**
     * 获取仪器里信息
     * @param item
     * @param vehicleName
     * @return
     */
    public  Object getInstrument(String item,String vehicleName) {
        if(StringUtil.isBlank(item)||StringUtil.isBlank(vehicleName)){
            return null;
        }
        Object  ob = redisUtil.hget(item, vehicleName);
        return ob;
    }

    /**
     *
     * @param item
     * @param trayName
     * @param ob
     * @return
     */
    public  boolean setOctalianpipe(String item, String trayName, OctalianpipeListModel ob) {
        if(StringUtil.isBlank(trayName)){
            return  false;
        }
        return  redisUtil.hset(item, trayName, ob==null?null:JSONObject.toJSONString(ob, SerializerFeature.WriteMapNullValue),-1);
    }

    /**
     * 获取八连管信息
     * @param item
     * @param vehicleName
     * @return
     */
    public  OctalianpipeListModel getOctalianpipe(String item,String vehicleName) {
        if(StringUtil.isBlank(item)||StringUtil.isBlank(vehicleName)){
            return null;
        }
        Object  ob = redisUtil.hget(item, vehicleName);
        if(ob!=null){
            OctalianpipeListModel octalianpipeListModel = JSON.parseObject(ob.toString(),OctalianpipeListModel.class);
            return octalianpipeListModel;
        }
        return null;
    }
    /**
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        //setOctalianpipe();
    }
    
    /**
     * 放入托盘信息
     * @param item
     * @param trayName
     * @param ma
     * @return
     */
    public  boolean setTrayList(String item,String trayName, List<TrayModel> ma) {
        if(StringUtil.isBlank(trayName)){
            return  false;
        }
        return  redisUtil.hsetNoTime(item, trayName, ma==null?null:JSONObject.toJSONString(ma, SerializerFeature.WriteMapNullValue  ));
    }

    /**
     * 查寻托盘信息
     * @param trayName
     * @return
     */
    public  List<TrayModel> getTrayList(String item, String trayName) {
        if(StringUtil.isBlank(item)||StringUtil.isBlank(trayName)){
            return null;
        }
         Object  ob = redisUtil.hget(item, trayName);
          if(ob!=null){
              String jsonObject = ob.toString();
              List<TrayModel> avs = JSON.parseArray(jsonObject, TrayModel.class);
              return avs;
          }
          return null;
    }
    /**
     * 查寻仪器信息
     * @param item
     * @return
     */
    public  Map<Object, Object> getInstruList(String item) {
        if(StringUtil.isBlank(item)){
            return null;
        }
        Map<Object, Object> map = redisUtil.hmget(item);
        return map;
    }
    /**
     * 单个string类型
     * @param item
     * @return
     */
    public  String getStrName(String item,String name) {
        if(StringUtil.isBlank(item)||StringUtil.isBlank(name)){
            return null;
        }
        Object  ob = redisUtil.hget(item, name);
        if(ob!=null){
            return ob.toString();
        }
        return "";
    }
    /**
     * 放入单个string类型
     * @param item
     * @param trayName
     * @param ob
     * @return
     */
    public  boolean setStrName(String item,String trayName,String ob) {
        if(StringUtil.isBlank(trayName)){
            return  false;
        }
        return  redisUtil.hset(item, trayName, ob,-1);
    }

    /**
     * 从完成list中取第一个
     * @param key
     * @return
     */
    public FrameModel getLeftByQubinsFinQueue(String key){
        Object obj=redisUtil.lGetIndex(key,0);
        if(obj!=null){
            return (FrameModel)obj;
        }
        return  null;
    }

    public void delQubinsFinQueue(String key){
        redisUtil.lLeftPop(key);
    }
    /**
     * 放入list中
     * @param key
     * @param frameModel
     */
    public  void setCubFinQueues(String key,FrameModel frameModel){
        redisUtil.lSet(key,frameModel);
    }

    /**
     * 获取完成List的长度
     * @param key
     * @return
     */
    public  long getCubFinQueuesSize(String key){
        return  redisUtil.lGetListSize(key);
    }

    /**
     * 获取建库仪完成map
     * @param Key
     * @return
     */
    public FrameModel getCubFinMap(String Key){
        Map<Object,Object>map=redisUtil.hmget("cubFinMap");
        return  (FrameModel)map.get(Key);
    }

    /**
     * set获取建库仪完成map
     * @param Key
     * @param frameModel
     */
    public void setCubFinMap(String Key,FrameModel frameModel){
        Map<String,Object>map=new HashMap<>();

        map.put(Key,frameModel);
        redisUtil.hmset("cubFinMap",map);
    }

    public void setHavaUpCubis(Map<String,String> havaUpCubis){
        Map<String,Object>map=new HashMap<>();
        for(Object set:havaUpCubis.keySet()){
            map.put(set.toString(),havaUpCubis.get(set));
        }
        redisUtil.del("havaUpCubis");
        redisUtil.hmset("havaUpCubis",map);
    }

    public Map<String,String> getHavaUpCubis(){
        Map<String,String> stringObjectMap=new HashMap<>();
        Map<Object,Object>map=redisUtil.hmget("havaUpCubis");
        for(Object set:map.keySet()){
            stringObjectMap.put(set.toString(),map.get(set).toString());
        }
        return stringObjectMap;
    }

    public void clearHavaUpCubis(){
        redisUtil.del("havaUpCubis");
        Map<String,Object> stringObjectMap=new HashMap<>();
        redisUtil.hmset("havaUpCubis",stringObjectMap);
    }

    public boolean getIsStartWork(){
        return Boolean.valueOf(redisUtil.get("IsStartWork").toString());
    }

    public void setIsStartWork(boolean flag){
        redisUtil.set("IsStartWork",flag);
    }

    public String getEpClaw(){
        return redisUtil.get("EpClaw").toString();
    }

    public void setEpClaw(String flag){
        redisUtil.set("EpClaw",flag);
    }

    public   String getAvgQy(){
        return redisUtil.get("AgvQy").toString();
    }

    public   void setAvgQy(String qy){
        redisUtil.set("AgvQy",qy);
    }

    public boolean getIsWorklc(){
        return Boolean.valueOf(redisUtil.get("IsWorklc").toString());
    }

    public void setisWorklc(boolean flag){
        redisUtil.set("IsWorklc",flag);
    }

    public boolean getIsAutoWork1(){
        return Boolean.valueOf(redisUtil.get("IsAutoWork1").toString());
    }

    public void setIsAutoWork1(boolean flag){
        redisUtil.set("IsAutoWork1",flag);
    }

    public boolean getIsAutoWork(){
        return Boolean.valueOf(redisUtil.get("IsAutoWork").toString());
    }

    public void setIsAutoWork(boolean flag){
        redisUtil.set("IsAutoWork",flag);
    }

    /**
     * 设置PCR开始MAP
     * @param key
     * @param frameModel
     */
    public void setPcrdoStartMap(String key,FrameModel frameModel) {
        Map<String,Object>map=new HashMap<>();
        map.put(key,frameModel);
        redisUtil.hmset("PcrdoStartMap",map);
    }

    /**
     * 获取PCR开始MAP
     * @param wkid
     * @return
     */
    public FrameModel getPcrdoStartMap(String wkid){
        Map<Object,Object>map=redisUtil.hmget("PcrdoStartMap");
        return  (FrameModel)map.get(wkid);
    }

    /**
     * 添加完成MAP（配置仪）
     * @param key
     * @param frameModel
     */
    public void setInstrumentFinMap(String key,FrameModel frameModel) {
        Map<String,Object>map=new HashMap<>();
        map.put(key,frameModel);
        redisUtil.hmset("InstrumentFinMap",map);
    }

    /**
     * 获取完成MAP（配置仪）
     * @param deviedid
     * @return
     */
    public FrameModel getInstrumentFinMap(String deviedid){
        Map<Object,Object>map=redisUtil.hmget("InstrumentFinMap");
        return  (FrameModel)map.get(deviedid);
    }

    /**
     * 添加艰苦信息map
     * @param key
     * @param libmap
     */
    public void setLibaryInfMap(String key,Map<String,Object> libmap ) {
        Map<String,Object>map=new HashMap<>();
        map.put(key,libmap);
        redisUtil.set("LibaryInfMap",map);
    }

    /**
     * 获取建库信息map
     * @param key
     * @return
     */
    public Map<String,Object> getLibaryInfMap(String key){
        Map<String,Object>map=(Map<String,Object>)redisUtil.get("LibaryInfMap");
        if(map==null){
            return null;
        }
        return  (Map<String,Object>)map.get(key);
    }

    public void setInstrumentUsedList(Map<String, String> map){
        if(StringUtil.isBlank(map.get("id"))){
            map.put("id",StringUtil.generateUUID());
        }
        redisUtil.lRightPush("InstrumentUsedList",map);
    }

    /**
     * 添加map
     * @param map
     * @param command
     * @param deviceId
     */
    public void setInstrumentUsedList_Map(Map<String, String> map,String command,String deviceId){
        synchronized(redisSetAndGetUtil) {
            List<Object> objects = redisUtil.lGet("InstrumentUsedList");
            Map<String, String> mapNew = new HashMap<>();
            boolean isHave = false;//标识是否有这个map，如果有则修改，没有就是新增
            int i = 0;
            for (Object mp : objects) {
                Map<String, String> map1 = (Map<String, String>) mp;
                if (map1.get("id").equals(map.get("id"))) {
                    isHave = true;
                    mapNew = map1;
                    break;
                }
                i++;
            }
            //有值的修改
            mapNew.put(command,deviceId);
            //没有值的修改
            map.put(command, deviceId);
            if (StringUtil.isBlank(map.get("id"))) {
                map.put("id", StringUtil.generateUUID());
            }
            if (isHave) {
                redisUtil.lUpdateIndex("InstrumentUsedList", i, mapNew);
            } else {
                //不存在则加入新的
                redisUtil.lSet("InstrumentUsedList", map);
            }
        }
    }

    /**
     * 替换map
     * @param map
     */
    public boolean modInstrumentUsedListMap(Map<String, String> map){
        synchronized(redisSetAndGetUtil) {
            List<Object> objects = redisUtil.lGet("InstrumentUsedList");
            boolean isHave = false;//标识是否有这个map，如果有则修改，没有就是新增
            int i = 0;
            for (Object mp : objects) {
                Map<String, String> map1 = (Map<String, String>) mp;
                if (map1.get("id").equals(map.get("id"))) {
                    isHave = true;
                    break;
                }
                i++;
            }
            if(isHave) {
                redisUtil.lUpdateIndex("InstrumentUsedList", i, map);
                return true;
            }
            return false;
        }
    }
    /**
     * 移除MA
     * @param map
     * @param command
     * @param deviceId
     */
    public void removeInstrumentUsedList_Map(Map<String, String> map,String command,String deviceId){
        synchronized(redisSetAndGetUtil) {
            List<Object> objects = redisUtil.lGet("InstrumentUsedList");
            Map<String, String> map2 = new HashMap<>();
            int i = 0;
            boolean isSame = false;
            for (Object mp : objects) {
                Map<String, String> map1 = (Map<String, String>) mp;
                for (String a : map1.keySet()) {
                    if (command.equals(a) && deviceId.equals(map1.get(a))) {
                        isSame = true;
                        break;
                    }
                }
                if (isSame) {
                    map2 = map1;
                    break;
                }
                i++;
            }
            if (isSame) {
                if (map != null) {
                    map.remove(command, deviceId);
                }
                map2.remove(command, deviceId);
                redisUtil.lUpdateIndex("InstrumentUsedList", i, map2);
            }
        }

    }

    /**
     * 根据协议类型和deviceID获取MAP
     * @param commd
     * @param decidId
     * @return
     */
    public Map<String, String>  getInstrumentUsedList(String commd, String decidId){
        List<Object>objects=redisUtil.lGet("InstrumentUsedList");
        for (Object mp : objects) {
            // 检查是否一至
            if (mp != null) {
                Map<String, String> map=(Map<String, String>)mp;
                if (map.get(commd) != null && map.get(commd).equals(decidId)) {
                    return map;
                }
            }
        }
        return null;
    }
    public List<Object>  getInstrumentUsedListAll(){
        List<Object>objects=redisUtil.lGet("InstrumentUsedList");

        return objects;
    }
    public void removeInstrumentUsetMap(Map<String, String> map){
        redisUtil.lRemove("InstrumentUsedList",1,map);
    }

    public void delStream(String key){
        redisUtil.del(key);
    }


    public void setAutoIdWkid(String key,String value ) {
        redisUtil.hset("autoIdWkid",key,value,-1);
    }

    public String getAutoIdWkid(String key){
        Object o=redisUtil.hget("autoIdWkid",key);
        if(o!=null){
            return  (String) o;
        }
        else {
            return "";
        }
    }
    public void setWkidByAutoId(String key,String value ) {
        redisUtil.hset("wkidAutoId",key,value,-1);
    }

    public String getWkidByAutoId(String key){
        Object o=redisUtil.hget("wkidAutoId",key);
        if(o!=null){
            return  (String) o;
        }
        else {
            return "";
        }
    }

    public Map<String,Object> getLibaryInfMapAll(){
        Map<String,Object>map=(Map<String,Object>)redisUtil.get("LibaryInfMap");
        return  map;
    }

    public  void setInstrumentWz(String key,String value){
        Map<String,Object>map=null;
        map=(Map<String,Object>)redisUtil.get("InstrumentWz");
        if(map!=null){
            map.put(key,value);
            redisUtil.set("InstrumentWz",map);
        }else{
            map=new HashMap<>();
            map.put(key,value);
            redisUtil.set("InstrumentWz",map);
        }
    }

    public  Map<String,Object> getInstrumentWz(){
        Map<String,Object>map=(Map<String,Object>)redisUtil.get("InstrumentWz");
        return  map;
    }
    /**
     * 放入Ep管信息
     * @param key
     * @param ls
     * @return
     */
    public  boolean setWzEpMap(String key, EpVehicleModel ls) {
        if(StringUtil.isBlank(key)){
            return  false;
        }
        return  redisUtil.hsetNoTime(RedisStorageEnum.EP_MAP_WZ.getCode(), key, ls==null?null:JSONObject.toJSONString(ls, SerializerFeature.WriteMapNullValue));
    }

    /**
     * 获取Ep管信息
     * @param key
     * @return
     */
    public  EpVehicleModel getWzEpMap( String key) {

        Object  ob = redisUtil.hget(RedisStorageEnum.EP_MAP_WZ.getCode(), key);
        if(ob!=null){
            EpVehicleModel epVehicleModel = JSONObject.parseObject(ob.toString(),EpVehicleModel.class);
            return epVehicleModel;
        }
        return null;
    }
    /**
     * 放入Ep管信息
     * @param key
     * @param ls
     * @return
     */
    public  boolean setDeskEpMap(String key, EpVehicleModel ls) {
        if(StringUtil.isBlank(key)){
            return  false;
        }
        return  redisUtil.hsetNoTime(RedisStorageEnum.EP_MAP_DESK.getCode(), key, ls==null?null:JSONObject.toJSONString(ls, SerializerFeature.WriteMapNullValue));
    }

    /**
     * 获取Ep管信息
     * @param key
     * @return
     */
    public  EpVehicleModel getDeskEpMap( String key) {

        Object  ob = redisUtil.hget(RedisStorageEnum.EP_MAP_DESK.getCode(), key);
        if(ob!=null){
            EpVehicleModel epVehicleModel = JSONObject.parseObject(ob.toString(),EpVehicleModel.class);
            return epVehicleModel;
        }
        return null;
    }
    /**
     * 获取所有Ep管信息
     * @param item
     * @return
     */
    public  Map<String,EpVehicleModel> getAllEpMap( String item) {

        Map<Object,Object> map = redisUtil.hmget(item);
        Map<String,EpVehicleModel> epMap = new HashMap<>();
        if(map!=null){
            for(Object obj : map.keySet()) {
                if(obj!=null&&StringUtil.isNotBlank(obj.toString())) {
                    EpVehicleModel epVehicleModel = JSON.parseObject(map.get(obj).toString(), EpVehicleModel.class);
                    epMap.put(obj.toString(),epVehicleModel);
                }
            }
        }
        return epMap;
    }

    public  JSONArray getAllInstrumentUsedList() {
        List<Object> objects = redisUtil.lGet("InstrumentUsedList");
        JSONArray jsonArray = new JSONArray();
        for(Object o:objects){
            jsonArray.add(JSONObject.toJSONString(o));
        }
        return jsonArray;
    }


}
