package com.matridx.las.netty.global.material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.matridx.las.netty.dao.entities.*;
import com.matridx.las.netty.enums.MaterialTypeEnum;
import com.matridx.las.netty.util.RedisSetAndGetUtil;
import com.matridx.las.netty.util.SendMessgeToHtml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 机器人物料台
 */
@Component
public class AgvDesktopGlobal {
    @Autowired
    private RedisSetAndGetUtil redisSetAndGetUtil;
    private static RedisSetAndGetUtil setAndGetUtil;
    private static Map<String, AgvsyybpModel> agv_tray2 = new HashMap<>();
    private static Map<String, AgvsyybpModel> agv_tray1 = new HashMap<>();

    private static List<TrayModel> agv_tray = new ArrayList<>();
    //枪头卡盒如果没有置为null
    private static GunVehicleModel agv_gunhead;
    //八连管 如果没有置为null
    private static OctalianpipeListModel agv_octalianpipe;
    private static String state;
    //机器人当前夹爪号
    private static String clampingNum;
    private static String lx;//推送用
    private static String deviceid;//推送用
    private static String name;
    //修改托盘的标识
    private static String editNow = "false";
    //位置信息
    private static String position;

    //static RedisSetAndGetUtil redisSetAndGetUtil = (RedisSetAndGetUtil) SpringUtil.getBean("redisSetAndGetUtil");
    @PostConstruct
    public void init() {
        setAndGetUtil = redisSetAndGetUtil;
    }


    public static String getClampingNum() {

        String clampingNum = setAndGetUtil.getStrName("agv","clampingNum");
        return clampingNum;
    }


    public static void setClampingNum(String clampingNum) {
        setAndGetUtil.setStrName("agv","clampingNum",clampingNum);

    }

    public static List<TrayModel> getAgv_trayList() {
        List<TrayModel> trayList = setAndGetUtil.getTrayList("agv", "agv_tray");
        agv_tray = trayList;
        return agv_tray;
    }

    public static void setAgv_trayList(List<TrayModel> agv_tray, boolean isSend) {
        //并修改redis中的数据
        boolean result = true;
        result = setAndGetUtil.setTrayList("agv", "agv_tray", agv_tray);
        if (result)
            AgvDesktopGlobal.agv_tray = agv_tray;
    }

    /**
     * 因为
     *
     * @return
     */
    public static Map<String, YsybxxDto> getAgv_tray1() {

        Map<String, YsybxxDto> map = setAndGetUtil.getTray("agv", "agv_tray1");
        return map;

    }

    public static void setAgv_tray1(Map<String, YsybxxDto> agv_tray1,boolean isSend) {
        //并修改redis中的数据
        boolean result = true;
        result = setAndGetUtil.setTray("agv", "agv_tray1", agv_tray1);

        if(isSend){
            SendMessgeToHtml.pushMessage(MaterialTypeEnum.MATERIAL_AGV.getCode());
        }
    }

    public static Map<String, YsybxxDto> getAgv_tray2() {
        Map<String, YsybxxDto> map = setAndGetUtil.getTray("agv", "agv_tray2");
        return map;
    }

    public static void setAgv_tray2(Map<String, YsybxxDto> agv_tray2,boolean isSend) {
        //并修改redis中的数据
        boolean result = true;
        result = setAndGetUtil.setTray("agv", "agv_tray2", agv_tray2);

        if(isSend){
            SendMessgeToHtml.pushMessage(MaterialTypeEnum.MATERIAL_AGV.getCode());
        }
    }

    public static GunVehicleModel getAgv_gunhead() {
        GunVehicleModel gunVehicleModel = setAndGetUtil.getGunhead("agv","agv_gunhead");
        if(gunVehicleModel==null){
            gunVehicleModel = new GunVehicleModel();
        }
        return gunVehicleModel;
    }

    public static void setAgv_gunhead(GunVehicleModel agv_gunhead,boolean isSend) {
        //并修改redis中的数据
        boolean result = true;
        result = setAndGetUtil.setGunhead("agv", "agv_gunhead", agv_gunhead);
        if (result)
            AgvDesktopGlobal.agv_gunhead = agv_gunhead;
        if(isSend){
            SendMessgeToHtml.pushMessage(MaterialTypeEnum.MATERIAL_AGV.getCode());
        }
    }

    public static EpVehicleModel getAgv_ep1() {
        EpVehicleModel  epVehicleModel = setAndGetUtil.getEp("agv", "agv_ep1");
        if(epVehicleModel==null){
            epVehicleModel = new EpVehicleModel();
        }
        return epVehicleModel;
    }

    public static void setAgv_ep1(EpVehicleModel agv_ep1,boolean isSend) {
        //并修改redis中的数据
        boolean result = true;
        result = setAndGetUtil.setEp("agv", "agv_ep1", agv_ep1);
        if(isSend){
            SendMessgeToHtml.pushMessage(MaterialTypeEnum.MATERIAL_AGV.getCode());
        }
    }

    public static EpVehicleModel getAgv_ep2() {
        EpVehicleModel  epVehicleModel = setAndGetUtil.getEp("agv", "agv_ep2");
        if(epVehicleModel==null){
            epVehicleModel = new EpVehicleModel();
        }
        return epVehicleModel;

    }

    public static void setAgv_ep2(EpVehicleModel agv_ep2,boolean isSend) {
        //并修改redis中的数据
        boolean result = true;
        result = setAndGetUtil.setEp("agv", "agv_ep2", agv_ep2);

        if(isSend){
            SendMessgeToHtml.pushMessage(MaterialTypeEnum.MATERIAL_AGV.getCode());
        }
    }

    public static EpVehicleModel getAgv_ep3() {
        EpVehicleModel  epVehicleModel = setAndGetUtil.getEp("agv", "agv_ep3");
        if(epVehicleModel==null){
            epVehicleModel = new EpVehicleModel();
        }
        return epVehicleModel;
    }

    public static void setAgv_ep3(EpVehicleModel agv_ep3,boolean isSend) {
        //并修改redis中的数据
        boolean result = true;
        result = setAndGetUtil.setEp("agv", "agv_ep3", agv_ep3);

        if(isSend){
            SendMessgeToHtml.pushMessage(MaterialTypeEnum.MATERIAL_AGV.getCode());
        }
    }

    public static OctalianpipeListModel getAgv_octalianpipe() {
        OctalianpipeListModel octalianpipeListModel = setAndGetUtil.getOctalianpipe("agv", "agv_octalianpipe");
        agv_octalianpipe = octalianpipeListModel;
        return agv_octalianpipe;
    }

    public static void setAgv_octalianpipe(OctalianpipeListModel agv_octalianpipe,boolean isSend) {
        //并修改redis中的数据
        boolean result = true;
        result = setAndGetUtil.setOctalianpipe("agv", "agv_octalianpipe", agv_octalianpipe);
        if (result)
            AgvDesktopGlobal.agv_octalianpipe = agv_octalianpipe;
        if(isSend){
            SendMessgeToHtml.pushMessage(MaterialTypeEnum.MATERIAL_AGV.getCode());
        }
    }

    public static String getLx() {

        String lx = setAndGetUtil.getStrName("agv","lx");
        return lx;
    }

    public static void setLx(String lx) {
        setAndGetUtil.setStrName("agv","lx",lx);

        AgvDesktopGlobal.lx = lx;
    }

    public static void setName(String name) {
        setAndGetUtil.setStrName("agv","name",name);

        AgvDesktopGlobal.name = name;
    }

    public static String getCommanddeviceid() {
        String deviceid = setAndGetUtil.getStrName("agv","commanddeviceid");
        return deviceid;
    }

    public static void setCommanddeviceid(String commanddeviceid) {
        setAndGetUtil.setStrName("agv","commanddeviceid",commanddeviceid);
    }
    public static String getDeviceid() {
        String deviceid = setAndGetUtil.getStrName("agv","deviceid");
        return deviceid;
    }

    public static void setDeviceid(String deviceid) {
        setAndGetUtil.setStrName("agv","deviceid",deviceid);
        AgvDesktopGlobal.deviceid = deviceid;
    }
    public static String getState() {
        String state = setAndGetUtil.getStrName("agv","state");
        return state;
    }

    public static void setState(String state) {
        setAndGetUtil.setStrName("agv","state",state);
        AgvDesktopGlobal.state = state;
    }
    public static String getQy() {

        String qy = setAndGetUtil.getStrName("agv","qy");
        return qy;
    }

    public static void setQy(String qy) {
        setAndGetUtil.setStrName("agv","qy",qy);

    }
    public static String getWz(){

        String wz = setAndGetUtil.getStrName("agv","wz");
        return wz;
    }

    public static void setWz(String wz) {
        setAndGetUtil.setStrName("agv","wz",wz);

    }
    public static String getPzwz(){

        String wz = setAndGetUtil.getStrName("agv","pzwz");
        return wz;
    }

    public static void setPzwz(String pzwz) {
        setAndGetUtil.setStrName("agv","pzwz",pzwz);
    }
    public static String getEditNow(){

        String wz = setAndGetUtil.getStrName("agv","editNow");
        return wz;
    }

    public static String getPosition() {
        String position = setAndGetUtil.getStrName("agv","position");

        return position;
    }

    public static void setPosition(String position) {
        setAndGetUtil.setStrName("agv","position",position);
        AgvDesktopGlobal.position = position;
    }

    public static void setEditNow(String editNow) {

        setAndGetUtil.setStrName("agv","editNow",editNow);

    }
    /**
     *
     */
    private static final long serialVersionUID = 1L;

}
