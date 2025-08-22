package com.matridx.las.netty.global.material;

import com.matridx.las.netty.dao.entities.*;
import com.matridx.las.netty.enums.MaterialTypeEnum;
import com.matridx.las.netty.util.RedisSetAndGetUtil;
import com.matridx.las.netty.util.SendMessgeToHtml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * 前物料区的物料
 */
@Component
public class FrontMaterialAreaGlobal {
	private static List<TrayModel> fma_tray = new ArrayList<>();
	private static GunVehicleModel fma_gunhead1;
	private static GunVehicleModel fma_gunhead2;

	private static String lx;//推送用
	private static String deviceid;//推送用
	private static String name;
	private static String wz;
	private static String qy;
	private static String commanddeviceid;


	@Autowired
	private RedisSetAndGetUtil redisSetAndGetUtil;
	private static  RedisSetAndGetUtil setAndGetUtil;
	@PostConstruct
	public void init(){
		setAndGetUtil = redisSetAndGetUtil;
	}
	public static List<TrayModel> getFma_tray() {
		List<TrayModel> trayList = setAndGetUtil.getTrayList("fma","fma_tray");
		fma_tray = trayList;
		return fma_tray;
	}

	public static void setFma_tray(List<TrayModel> fma_tray, boolean isSend) {
		//并修改redis中的数据
		boolean result = true;
		result = setAndGetUtil.setTrayList("fma","fma_tray",fma_tray);
		if(result)
		FrontMaterialAreaGlobal.fma_tray = fma_tray;
		if(isSend) {
			SendMessgeToHtml.pushMessage(MaterialTypeEnum.MATERIAL_FMA.getCode());
		}
	}

	public static GunVehicleModel getFma_gunhead1() {
		GunVehicleModel gunVehicleModel = setAndGetUtil.getGunhead("fma","fma_gunhead1");
		if(gunVehicleModel==null){
			gunVehicleModel = new GunVehicleModel();
		}
		return gunVehicleModel;
	}
	public static void setFma_gunhead1(GunVehicleModel gunVehicleModel,boolean isSend ) {
		boolean result = true;
		result = setAndGetUtil.setGunhead("fma","fma_gunhead1", gunVehicleModel);
		if(result)
		FrontMaterialAreaGlobal.fma_gunhead1 = fma_gunhead1;
			if(isSend) {
				SendMessgeToHtml.pushMessage(MaterialTypeEnum.MATERIAL_FMA.getCode());
			}
	}

	public static GunVehicleModel getFma_gunhead2() {
		GunVehicleModel gunVehicleModel = setAndGetUtil.getGunhead("fma","fma_gunhead2");
		if(gunVehicleModel==null){
			gunVehicleModel = new GunVehicleModel();
		}
		return gunVehicleModel;
	}

	public static void setFma_gunhead2(GunVehicleModel fma_gunhead2,boolean isSend) {
		boolean result = true;
		result = setAndGetUtil.setGunhead("fma","fma_gunhead2", fma_gunhead2);
		if(result)
		FrontMaterialAreaGlobal.fma_gunhead2 = fma_gunhead2;
		if(isSend) {
			SendMessgeToHtml.pushMessage(MaterialTypeEnum.MATERIAL_FMA.getCode());
		}
	}

	public static EpVehicleModel getFma_ep1() {
		EpVehicleModel  epVehicleModel = setAndGetUtil.getEp("fma", "fma_ep1");
		if(epVehicleModel==null){
			epVehicleModel = new EpVehicleModel();
		}
		return epVehicleModel;
	}

	public static void setFma_ep1(EpVehicleModel fma_ep1,boolean isSend) {
		//并修改redis中的数据
		boolean result = true;
		result = setAndGetUtil.setEp("fma","fma_ep1",fma_ep1);
		if(result)
		if(isSend) {
			SendMessgeToHtml.pushMessage(MaterialTypeEnum.MATERIAL_FMA.getCode());
		}
	}

	public static EpVehicleModel getFma_ep2() {
		EpVehicleModel  epVehicleModel = setAndGetUtil.getEp("fma", "fma_ep2");
		if(epVehicleModel==null){
			epVehicleModel = new EpVehicleModel();
		}
		return epVehicleModel;
	}

	public static void setFma_ep2(EpVehicleModel fma_ep2,boolean isSend) {
		//并修改redis中的数据
		boolean result = true;
		result = setAndGetUtil.setEp("fma","fma_ep2",fma_ep2);
		if(result)
		if(isSend) {
			SendMessgeToHtml.pushMessage(MaterialTypeEnum.MATERIAL_FMA.getCode());
		}
	}

	public static EpVehicleModel getFma_ep3() {
		EpVehicleModel  epVehicleModel = setAndGetUtil.getEp("fma", "fma_ep3");
		if(epVehicleModel==null){
			epVehicleModel = new EpVehicleModel();
		}
		return epVehicleModel;
	}

	public static void setFma_ep3(EpVehicleModel fma_ep3,boolean isSend) {
		//并修改redis中的数据
		boolean result = true;
		result = setAndGetUtil.setEp("fma","fma_ep3",fma_ep3);
		if(result)
		if(isSend) {
			SendMessgeToHtml.pushMessage(MaterialTypeEnum.MATERIAL_FMA.getCode());
		}
	}

	public static String getLx() {

		String lx = setAndGetUtil.getStrName("fma","lx");
		return lx;
	}

	public static void setLx(String lx) {
		setAndGetUtil.setStrName("fma","lx",lx);

		FrontMaterialAreaGlobal.lx = lx;
	}

	public static void setName(String name) {
		setAndGetUtil.setStrName("fma","name",name);

		FrontMaterialAreaGlobal.name = name;
	}
	public static String getCommanddeviceid() {
		String commanddeviceid = setAndGetUtil.getStrName("fma","commanddeviceid");

		return commanddeviceid;
	}

	public static void setCommanddeviceid(String commanddeviceid) {
		setAndGetUtil.setStrName("fma","commanddeviceid",commanddeviceid);
		FrontMaterialAreaGlobal.commanddeviceid = commanddeviceid;
	}
	public static String getDeviceid() {
		String deviceid = setAndGetUtil.getStrName("fma","deviceid");

		return deviceid;
	}

	public static void setDeviceid(String deviceid) {
		setAndGetUtil.setStrName("fma","deviceid",deviceid);
		FrontMaterialAreaGlobal.deviceid = deviceid;
	}
	public static String getQy() {

		String qy = setAndGetUtil.getStrName("fma","qy");
		return qy;
	}

	public static void setQy(String qy) {
		setAndGetUtil.setStrName("fma","qy",qy);

	}
	public static String getWz(){

		String wz = setAndGetUtil.getStrName("fma","wz");
		return wz;
	}

	public static void setWz(String wz) {
		setAndGetUtil.setStrName("fma","wz",wz);

	}
	public static String getPzwz(){

		String wz = setAndGetUtil.getStrName("auto_desk","pzwz");
		return wz;
	}

	public static void setPzwz(String pzwz) {
		setAndGetUtil.setStrName("auto_desk","pzwz",pzwz);

	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
