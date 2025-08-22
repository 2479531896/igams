package com.matridx.las.netty.global.material;

import com.matridx.las.netty.dao.entities.AgvEpModel;
import com.matridx.las.netty.dao.entities.EpVehicleModel;
import com.matridx.las.netty.dao.entities.OctalianpipeListModel;
import com.matridx.las.netty.enums.MaterialTypeEnum;
import com.matridx.las.netty.util.RedisSetAndGetUtil;
import com.matridx.las.netty.util.SendMessgeToHtml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * 配置仪边上桌面的材料
 */
@Component
public class AutoDesktopGlobal {
	//八连管的载具，  map的格式:{"位置编号"：“样本编号”}
	private static OctalianpipeListModel auto_octalianpipe1;
	//八连管的载具，为null代表无载具  map的格式:{"位置编号"：“样本编号”}
	private static OctalianpipeListModel auto_octalianpipe2;
	//ep管的载具为null代表无载具，装核酸的板子
	private static EpVehicleModel ad_ep1;
	//ep管的载具为null代表无载具，
	private static EpVehicleModel ad_ep2;
	private static String lx;//推送用
	private static String deviceid;//推送用
	private static String name;
	@Autowired
	private RedisSetAndGetUtil redisSetAndGetUtil;
	private static  RedisSetAndGetUtil setAndGetUtil;
	@PostConstruct
	public void init(){
		setAndGetUtil = redisSetAndGetUtil;
	}
	public static OctalianpipeListModel getauto_octalianpipe1() {
		OctalianpipeListModel octalianpipeListModel  =setAndGetUtil.getOctalianpipe("auto_desk","auto_octalianpipe1");
		auto_octalianpipe1 = octalianpipeListModel;
		return auto_octalianpipe1;
	}
	public static void setauto_octalianpipe1(OctalianpipeListModel auto_octalianpipe1,boolean isSend) {
		//并修改redis中的数据
		boolean result = true;
		result = setAndGetUtil.setOctalianpipe("auto_desk","auto_octalianpipe1", auto_octalianpipe1);
		if(result)
			AutoDesktopGlobal.auto_octalianpipe1 = auto_octalianpipe1;
		if(isSend){
			SendMessgeToHtml.pushMessage(MaterialTypeEnum.MATERIAL_OCP.getCode());
		}
	}

	public static OctalianpipeListModel getauto_octalianpipe2() {
		OctalianpipeListModel octalianpipeListModel  =setAndGetUtil.getOctalianpipe("auto_desk","auto_octalianpipe2");
		auto_octalianpipe2 = octalianpipeListModel;
		return auto_octalianpipe2;
	}
	public static void setauto_octalianpipe2(OctalianpipeListModel auto_octalianpipe2,boolean isSend) {
		//并修改redis中的数据
		boolean result = true;
		result = setAndGetUtil.setOctalianpipe("auto_desk","auto_octalianpipe2", auto_octalianpipe2);
		if(result)
			AutoDesktopGlobal.auto_octalianpipe2 = auto_octalianpipe2;
		if(isSend){
			SendMessgeToHtml.pushMessage(MaterialTypeEnum.MATERIAL_OCP.getCode());
		}
	}

	public static EpVehicleModel getAd_ep1() {
		return setAndGetUtil.getEp("auto_desk","ad_ep1");

	}

	public static void setAd_ep1(EpVehicleModel ad_ep1,boolean isSend) {
		//并修改redis中的数据
		boolean result = true;
		result = setAndGetUtil.setEp("auto_desk","ad_ep1", ad_ep1);
		if(result)
			AutoDesktopGlobal.ad_ep1 = ad_ep1;
		if(isSend){
			SendMessgeToHtml.pushMessage(MaterialTypeEnum.MATERIAL_OCP.getCode());
		}
	}

	public static EpVehicleModel getAd_ep2() {
		return   setAndGetUtil.getEp("auto_desk","ad_ep2");

	}

	public static void setAd_ep2(EpVehicleModel ad_ep2,boolean isSend) {
		//并修改redis中的数据
		boolean result = true;
		result = setAndGetUtil.setEp("auto_desk","ad_ep2", ad_ep2);
		if(result)
		AutoDesktopGlobal.ad_ep2 = ad_ep2;
		if(isSend){
			SendMessgeToHtml.pushMessage(MaterialTypeEnum.MATERIAL_OCP.getCode());
		}
	}
	public static String getLx() {
		String lx = setAndGetUtil.getStrName("auto_desk","lx");
		return lx;
	}

	public static void setLx(String lx) {
		setAndGetUtil.setStrName("auto_desk","lx",lx);

		AutoDesktopGlobal.lx = lx;
	}

	public static void setName(String name) {
		setAndGetUtil.setStrName("auto_desk","name",name);

		AutoDesktopGlobal.name = name;
	}

	public static String getDeviceid() {
		String deviceid = setAndGetUtil.getStrName("auto_desk","deviceid");

		return deviceid;
	}

	public static void setDeviceid(String deviceid) {
		setAndGetUtil.setStrName("auto_desk","deviceid",deviceid);
		AutoDesktopGlobal.deviceid = deviceid;
	}
	public static String getCommanddeviceid() {
		String commanddeviceid = setAndGetUtil.getStrName("auto_desk","commanddeviceid");

		return commanddeviceid;
	}

	public static void setCommanddeviceid(String commanddeviceid) {
		setAndGetUtil.setStrName("auto_desk","commanddeviceid",commanddeviceid);
	}
	public static String getQy() {

		String qy = setAndGetUtil.getStrName("auto_desk","qy");
		return qy;
	}

	public static void setQy(String qy) {
		setAndGetUtil.setStrName("auto_desk","qy",qy);

	}
	public static String getWz(){

		String wz = setAndGetUtil.getStrName("auto_desk","wz");
		return wz;
	}

	public static void setWz(String wz) {
		setAndGetUtil.setStrName("auto_desk","wz",wz);

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
