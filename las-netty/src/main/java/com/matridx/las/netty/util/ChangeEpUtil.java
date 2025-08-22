package com.matridx.las.netty.util;

import com.matridx.las.netty.dao.entities.EpVehicleModel;
import com.matridx.las.netty.enums.RedisStorageEnum;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public   class  ChangeEpUtil {

    @Autowired
    private RedisSetAndGetUtil redisSetAndGetUtil;
    private static  RedisSetAndGetUtil setAndGetUtil;

    @PostConstruct
    public void init(){
        setAndGetUtil = redisSetAndGetUtil;

       /* Map<String,Object> map=new HashMap<>();
        EpVehicleModel takeModel=new EpVehicleModel();
        List<AgvEpModel> list2 = new ArrayList<>();
        int total = Integer.parseInt(GlobalParmEnum.EP_TOTAL_NUM.getCode());
        for (int j = 0; j <total; j++) {
            AgvEpModel agvEpModel = new AgvEpModel();
            agvEpModel.setBlank(true);
            int xzb = (int) Math.floor(j / 8) + 1;
            int yzb = (j+1) % 8;
            if (yzb == 0) {
                yzb = 8;
            }
            agvEpModel.setXzb(xzb + "");
            agvEpModel.setYzb(yzb + "");
            list2.add(agvEpModel);
        }
        takeModel.setEpList(list2);
        map.put("qy",1);
        map.put("wz",5);
        map.put("isnull",true);
        map.put("model",null);
        map.put("zt","");
        map.put("deviceId","");
        map.put("name","ep1");
        wzMap.put("ep1",takeModel);
      //  wzList.add(map);
        list2 = new ArrayList<>();
        takeModel=new EpVehicleModel();
        takeModel.setEpList(list2);
        map=new HashMap<>();
        map.put("qy",1);
        map.put("wz",6);
        map.put("isnull",false);
        map.put("model",takeModel);
        map.put("zt","");
        map.put("deviceId","");
        map.put("name","ep2");
        wzMap.put("ep2",takeModel);
       // wzList.add(map);
        list2 = new ArrayList<>();
        takeModel=new EpVehicleModel();
        takeModel.setEpList(list2);
        map=new HashMap<>();
        map.put("qy",1);
        map.put("wz",7);
        map.put("isnull",false);
        map.put("model",takeModel);
        map.put("zt","");
        map.put("deviceId","");
        map.put("name","ep3");
        wzMap.put("ep3",takeModel);
    //    wzList.add(map);
        list2 = new ArrayList<>();
        takeModel=new EpVehicleModel();
        takeModel.setEpList(list2);
        map=new HashMap<>();
        map.put("wz",1);
        map.put("qy",3);
        map.put("model",takeModel);
        map.put("zt","");
        map.put("deviceId","");
        map.put("isnull",false);
        map.put("name","ep4");
        wzMap.put("ep4",takeModel);
    //    wzList.add(map);
        Map<String,Object> desk_map=new HashMap<>();
        list2 = new ArrayList<>();
        takeModel=new EpVehicleModel();
        takeModel.setEpList(list2);
        desk_map.put("wz",3);
        desk_map.put("zjmc","ep1");
        desk_map.put("isnull",false);
        desk_map.put("model",takeModel);
        desk_map.put("zt","");
        desk_map.put("deviceId","");
        deskMap.put("ep1",takeModel);
     //   deskList.add(desk_map);
        desk_map=new HashMap<>();
        list2 = new ArrayList<>();
        takeModel=new EpVehicleModel();
        takeModel.setEpList(list2);
        desk_map.put("wz",4);
        desk_map.put("zjmc","");
        desk_map.put("isnull",true);
        desk_map.put("model",takeModel);
        desk_map.put("zt","");
        desk_map.put("deviceId","");
        deskMap.put("ep2",takeModel);*/
       // deskList.add(desk_map);
    }

    /**
     * 获取放到物料区的载具
     * @param list 需要的ep管载具
     * @param agvList 需要从机器人身上拿下去的ep管位置
     * @return
     */
    public static   Map<String,Object> changeEpCommon_drop(List<String> list,List<String>agvList){
        Map<String,EpVehicleModel> wzMap= setAndGetUtil.getAllEpMap(RedisStorageEnum.EP_MAP_WZ.getCode());
        Map<String,EpVehicleModel> deskMap= setAndGetUtil.getAllEpMap(RedisStorageEnum.EP_MAP_DESK.getCode());
        Map<String,Object> returnMap=null;
        if(list!=null&&list.size()>0) {
            for(String agvParam : agvList){
                if(list.contains(deskMap.get(agvParam).getCsdm())){
                    continue;
                }else if(StringUtil.isNotBlank(agvParam)&&!deskMap.get(agvParam).isIsnull()){
                    if(wzMap.get(agvParam).isIsnull()){
                        returnMap=new HashMap<>();
                        returnMap.put("qy",wzMap.get(agvParam).getQy());
                        returnMap.put("desk",deskMap.get(agvParam).getWz());
                        returnMap.put("wlq",wzMap.get(agvParam).getWz());
                        returnMap.put("wzName",agvParam);
                        returnMap.put("deskName",agvParam);
                        return returnMap;
                    }
                }

            }

        }else if(list==null||list.size()==0){
            for(String deskKye:deskMap.keySet()){
                if(!deskMap.get(deskKye).isIsnull()){
                    returnMap=new HashMap<>();
                    returnMap.put("qy",wzMap.get(deskMap.get(deskKye).getCsdm()).getQy());
                    returnMap.put("desk",deskMap.get(deskKye).getWz());
                    returnMap.put("wlq",wzMap.get(deskMap.get(deskKye).getCsdm()).getWz());
                    returnMap.put("wzName",deskMap.get(deskKye).getCsdm());
                    returnMap.put("deskName",deskKye);
                    return returnMap;
                }
            }

        }
        return returnMap;
    }

    /**
     *获取抓到机器人上的载具
     * @param list 需要的ep管载具
     * @param agvList 需要从物料区拿到机器人身上的EP管载具
     * @return
     */
    public static Map<String,Object> changeEpCommon_take(List<String> list,List<String>agvList){
        Map<String,EpVehicleModel> wzMap= setAndGetUtil.getAllEpMap(RedisStorageEnum.EP_MAP_WZ.getCode());
        Map<String,EpVehicleModel> deskMap= setAndGetUtil.getAllEpMap(RedisStorageEnum.EP_MAP_DESK.getCode());
        Map<String,Object> returnMap=null;
        if(list!=null&&list.size()>0){
            for(String agvParam:agvList){
                if(list.contains(deskMap.get(agvParam).getCsdm())){
                    continue;
                }else if(StringUtil.isNotBlank(agvParam)&&deskMap.get(agvParam).isIsnull()){
                    for(String param:list){
                        if(!wzMap.get(param).isIsnull()){
                            returnMap=new HashMap<>();
                            returnMap.put("qy",wzMap.get(param).getQy());
                            returnMap.put("desk",deskMap.get(agvParam).getWz());
                            returnMap.put("wlq",wzMap.get(param).getWz());
                            returnMap.put("wzName",param);
                            returnMap.put("deskName",agvParam);
                            return returnMap;
                        }
                    }
                }
            }
        }

        return returnMap;
    }

    /**
     * p更换后的放到物料区的更新
     * @param map
     */
    public static void changeRedis_drop(Map<String,Object>map){
        Map<String,EpVehicleModel> wzMap= setAndGetUtil.getAllEpMap(RedisStorageEnum.EP_MAP_WZ.getCode());
        Map<String,EpVehicleModel> deskMap= setAndGetUtil.getAllEpMap(RedisStorageEnum.EP_MAP_DESK.getCode());
        EpVehicleModel takeModel=deskMap.get(map.get("deskName").toString());
        EpVehicleModel dropModel=wzMap.get(map.get("wzName").toString());
        dropModel.setIsnull(false);
        dropModel.setCsdm(takeModel.getCsdm());
        dropModel.setZt("");
        //清台的时候，删除ep样本
        if("true".equals(map.get("clear"))){
            dropModel.setEpList(new ArrayList<>());
        }else{
            dropModel.setEpList(takeModel.getEpList());
        }
        wzMap.put(map.get("wzName").toString(),dropModel);
        setAndGetUtil.setWzEpMap(map.get("wzName").toString(),dropModel);
        takeModel.setCsdm("");
        takeModel.setIsnull(true);
        takeModel.setZt("");
        takeModel.setEpList(null);
        deskMap.put(map.get("deskName").toString(),takeModel);
        setAndGetUtil.setDeskEpMap(map.get("deskName").toString(),takeModel);
    }

    /**
     * ep更换后的抓到机器人台面上的更新
     * @param map
     */
    public static void changeRedis_take(Map<String,Object>map){
        Map<String,EpVehicleModel> wzMap= setAndGetUtil.getAllEpMap(RedisStorageEnum.EP_MAP_WZ.getCode());
        Map<String,EpVehicleModel> deskMap= setAndGetUtil.getAllEpMap(RedisStorageEnum.EP_MAP_DESK.getCode());
        EpVehicleModel takeModel=deskMap.get(map.get("deskName").toString());
        EpVehicleModel dropModel=wzMap.get(map.get("wzName").toString());
        takeModel.setCsdm(dropModel.getCsdm());
        takeModel.setIsnull(false);
        takeModel.setSendSxType(dropModel.getSendSxType());
        takeModel.setZt("");
        takeModel.setEpList(dropModel.getEpList());
        dropModel.setCsdm("");
        dropModel.setIsnull(true);
        dropModel.setZt("");
        dropModel.setEpList(null);
        wzMap.put(map.get("wzName").toString(),dropModel);
        setAndGetUtil.setWzEpMap(map.get("wzName").toString(),dropModel);
        setAndGetUtil.setDeskEpMap(map.get("deskName").toString(),takeModel);

    }

    /**
     * 获取map
     * @param epName
     * @return
     */
    public static EpVehicleModel getMap(String epName){
        Map<String,EpVehicleModel> wzMap= setAndGetUtil.getAllEpMap(RedisStorageEnum.EP_MAP_WZ.getCode());
        Map<String,EpVehicleModel> deskMap= setAndGetUtil.getAllEpMap(RedisStorageEnum.EP_MAP_DESK.getCode());
        for(String wzKey :wzMap.keySet()){
            EpVehicleModel wzmodel=wzMap.get(wzKey);
            if(wzmodel.getCsdm().equals(epName)){
                return wzmodel;
            }
        }

        for(String wzKey :deskMap.keySet()){
            EpVehicleModel deskmodel=deskMap.get(wzKey);
            if(deskmodel.getCsdm().equals(epName)){
                return deskmodel;
            }
        }
        return null;
    }

    /**
     * 更新map
     * @param epName
     * @return
     */
    public static boolean setMap(EpVehicleModel epVehicleModel,String epName){
        Map<String,EpVehicleModel> wzMap= setAndGetUtil.getAllEpMap(RedisStorageEnum.EP_MAP_WZ.getCode());
        Map<String,EpVehicleModel> deskMap= setAndGetUtil.getAllEpMap(RedisStorageEnum.EP_MAP_DESK.getCode());
        boolean flag=false;
        for(String wzKey :wzMap.keySet()){
            EpVehicleModel wzmodel=wzMap.get(wzKey);
            if(wzmodel.getCsdm().equals(epName)) {
                wzmodel.setEpList(epVehicleModel.getEpList());
                wzmodel.setIsnull(epVehicleModel.isIsnull());
                wzmodel.setCsdm(epVehicleModel.getCsdm());
                setAndGetUtil.setWzEpMap(wzKey, wzmodel);
                flag = true;
                return flag;
            }

        }
        for(String wzKey :deskMap.keySet()){
            EpVehicleModel deskmodel=deskMap.get(wzKey);
            if(deskmodel.getCsdm().equals(epName)){
                deskmodel.setEpList(epVehicleModel.getEpList());
                deskmodel.setIsnull(epVehicleModel.isIsnull());
                deskmodel.setCsdm(epVehicleModel.getCsdm());
                deskmodel.setSendSxType(epVehicleModel.getSendSxType());
                setAndGetUtil.setDeskEpMap(wzKey,deskmodel);
                flag=true;
                break;
            }
        }

        return flag;
    }
}
