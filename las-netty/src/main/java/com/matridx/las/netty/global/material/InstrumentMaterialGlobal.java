package com.matridx.las.netty.global.material;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.matridx.las.netty.channel.domain.Command;
import com.matridx.las.netty.dao.entities.*;
import com.matridx.las.netty.dao.post.IJkywzszDao;
import com.matridx.las.netty.enums.InstrumentStatusEnum;
import com.matridx.las.netty.enums.MaterialTypeEnum;
import com.matridx.las.netty.util.RedisSetAndGetUtil;
import com.matridx.las.netty.util.SendMessgeToHtml;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 各个仪器材料
 */
@Component
public class InstrumentMaterialGlobal {
	private CubsMaterialModel cubsMaterialModel;
	private AutoMaterialModel autoMaterialModel;
	@Autowired
	private RedisSetAndGetUtil redisSetAndGetUtil;
	@Autowired
	private IJkywzszDao jkywzszDao;
	private static  RedisSetAndGetUtil setAndGetUtil;
	private static  IJkywzszDao jkywzszDaoStatic;
	private static LinkedBlockingQueue<String> autoBlockQueue=new LinkedBlockingQueue<String>();
	@PostConstruct
	public void init(){
		setAndGetUtil = redisSetAndGetUtil;
		jkywzszDaoStatic = jkywzszDao;
	}

	public static void setAutoBlockQueue() throws InterruptedException {
		autoBlockQueue.put("");
	}

	public static LinkedBlockingQueue<String> getAutoBlockQueue() throws InterruptedException {
		return  autoBlockQueue;
	}
	//修改状态
	public static Boolean setMaterial(String commd,String state,String devicid){
		boolean result = true;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		if(Command.AUTO.toString().equals(commd)){
			synchronized (AutoMaterialModel.class) {
				AutoMaterialModel autoMaterialModel = new AutoMaterialModel();
				autoMaterialModel.setDeviceid(devicid);
				AutoMaterialModel autoMaterialModelN = getAutoMaterial(autoMaterialModel);
				autoMaterialModelN.setState(state);
				result = setAutoMaterial(autoMaterialModelN,false);
			}
		} else if(Command.CUBICS.toString().equals(commd)){
			synchronized (CubsMaterialModel.class) {
				CubsMaterialModel cubsMaterialModel = new CubsMaterialModel();
				cubsMaterialModel.setPassId(devicid);
				CubsMaterialModel cubsMaterialModelN = getCubsMaterial(cubsMaterialModel);
				if(cubsMaterialModelN!=null){
					cubsMaterialModelN.setState(state);
					result = setCubsMaterial(cubsMaterialModelN,false);
				}

			}
		}else if(Command.PCR.toString().equals(commd)){
			synchronized (PcrMaterialModel.class) {
				PcrMaterialModel pcrMaterialModel = new PcrMaterialModel();
				pcrMaterialModel.setDeviceid(devicid);
				PcrMaterialModel pcrMaterialModelN = getPcrMaterial(pcrMaterialModel);
				if(pcrMaterialModelN!=null) {
					pcrMaterialModelN.setState(state);
					result = setPcrMaterial(pcrMaterialModelN, false);
				}
			}
		}else if(Command.CMH.toString().equals(commd)){
			synchronized (ChmMaterialModel.class) {
				ChmMaterialModel chmMaterialModel = new ChmMaterialModel();
				chmMaterialModel.setDeviceid(devicid);
				ChmMaterialModel chmMaterialModelN = getChmsMaterial(chmMaterialModel);
				chmMaterialModelN.setState(state);

				result = setChmMaterial(chmMaterialModelN,false);
			}
		}else if(Command.SEQ.toString().equals(commd)){
			synchronized (SeqMaterialModel.class) {
				SeqMaterialModel seqMaterialModel = new SeqMaterialModel();
				seqMaterialModel.setDeviceid(devicid);
				SeqMaterialModel seqMaterialModelN = getSeqMaterial(seqMaterialModel);
				seqMaterialModelN.setState(state);

				result = setSeqMaterial(seqMaterialModelN,false);
			}
		}else if(Command.AGV.toString().equals(commd)){
			AgvDesktopGlobal.setState(state);
		}
		//修改状态后，上报状态
		SendMessgeToHtml.pushMessage(null);
		return result;
	}

	/**
	 * 获取机器人身上是否有夹爪
	 * @return
	 */
	public static  String getEpClaw(){
		return setAndGetUtil.getEpClaw();
	}

	/**
	 * 机器人身上是否有夹爪
	 * @param claw
	 */
	public static  void setEpClaw(String claw){
		 setAndGetUtil.setEpClaw(claw);
	}

	/**
	 * 获取机器人当前区域
	 * @return
	 */
	public static  String getAvgQy(){
		return setAndGetUtil.getAvgQy();
	}

	/**
	 * 设置机器人当前区域
	 * @param qy
	 */
	public static  void setAvgQy(String qy){
		setAndGetUtil.setAvgQy(qy);
	}

	/**
	 * 删除仪器和物料区所有信息
	 *
	 */
	public static void delAllINstr(){
		setAndGetUtil.delStream("cubics");
		setAndGetUtil.delStream("auto");
		setAndGetUtil.delStream("fma");
		setAndGetUtil.delStream("auto_desk");
		setAndGetUtil.delStream("pcr");
		setAndGetUtil.delStream("seq");
		setAndGetUtil.delStream("chm");
		setAndGetUtil.delStream("agv");
		setAndGetUtil.delStream("deskEpMap");
		setAndGetUtil.delStream("wzEpMap");



	}
	/**
	 * 建库仪放入
	 * @param cubsMaterialModel
	 * @param isSend 是否推送
	 * @return
	 */
	public static Boolean setCubsMaterial(CubsMaterialModel cubsMaterialModel,Boolean isSend){
		//放入时加锁，防止查询放入报错
		boolean result = true;
		synchronized (CubsMaterialModel.class) {
			result = setAndGetUtil.setInstrument("cubics", cubsMaterialModel.getWzszid(), cubsMaterialModel);
		}
		//如果推送
		if(isSend){
			SendMessgeToHtml.pushMessage(MaterialTypeEnum.MATERIAL_CUBICS.getCode());
		}
		return result;

	}
	/**
	 * 建库仪拿出
	 */
	public  static CubsMaterialModel getCubsMaterial(CubsMaterialModel cubsMaterialModel){
		//由于修改，建库仪用wzid作为id，所以先通过通道号，查询出
		JkywzszDto jkywzszDto = jkywzszDaoStatic.queryById(cubsMaterialModel.getPassId());
		if(jkywzszDto==null|| StringUtil.isBlank(jkywzszDto.getWzszid())) return  null;
		Object ob = setAndGetUtil.getInstrument("cubics",jkywzszDto.getWzszid());
		if(ob!=null){
			CubsMaterialModel cubsMaterial = JSONObject.parseObject(ob.toString(),CubsMaterialModel.class);
			return cubsMaterial;
		}
		return null;
	}
	/**
	 * 所有建库仪拿出
	 */
	public  static JSONArray getCubsMaterialList(){
		Map<Object, Object> map = setAndGetUtil.getInstruList("cubics");
		JSONArray cubsArray = new JSONArray();
		for(Object obj : map.values()) {
			CubsMaterialModel jsonObject = JSONObject.parseObject(obj.toString(),CubsMaterialModel.class);
			//jsonObject.put("deviceid",jsonObject.get("passId"));
			cubsArray.add(jsonObject);
		}
		return cubsArray;
	}
	/**
	 * 配置仪放入
	 */
	public static Boolean setAutoMaterial(AutoMaterialModel autoMaterialModel,boolean isSend){
		boolean result = true;
		synchronized (AutoMaterialModel.class) {
			result = setAndGetUtil.setInstrument("auto", autoMaterialModel.getDeviceid(), autoMaterialModel);
		}
		//如果推送
		if(isSend){
			SendMessgeToHtml.pushMessage(MaterialTypeEnum.MATERIAL_AUTO.getCode());
		}
		return result;
	}
	/**
	 * 配置仪拿出
	 */
	public static AutoMaterialModel getAutoMaterial(AutoMaterialModel autoMaterialModel){
		Object ob = setAndGetUtil.getInstrument("auto",autoMaterialModel.getDeviceid());
		if(ob!=null){
			AutoMaterialModel autoMaterial = JSONObject.parseObject(ob.toString(),AutoMaterialModel.class);
			return autoMaterial;
		}
		return null;
	}
	/**
	 * 所有配置仪仪拿出
	 */
	public  static JSONArray getAutoMaterialList(){
		Map<Object, Object> map = setAndGetUtil.getInstruList("auto");
		JSONArray autoArray = new JSONArray();
		for(Object obj : map.values()) {
			AutoMaterialModel j = JSON.parseObject(obj.toString(),AutoMaterialModel.class);
			autoArray.add(j);
		}
		return autoArray;
	}
	/**
	 * 加盖机放入
	 */
	public static Boolean setChmMaterial(ChmMaterialModel chmMaterialModel,boolean isSend){
		boolean result = true;
		synchronized (ChmMaterialModel.class) {
			result = setAndGetUtil.setInstrument("chm", chmMaterialModel.getDeviceid(), chmMaterialModel);
		}
		//如果推送
		if(isSend){
			SendMessgeToHtml.pushMessage(MaterialTypeEnum.MATERIAL_CMH.getCode());
		}
		return result;
	}
	/**
	 * 所有加该机仪拿出
	 */
	public  static JSONArray getChmMaterialList(){
		Map<Object, Object> map = setAndGetUtil.getInstruList("chm");
		JSONArray chmArray = new JSONArray();
		for(Object obj : map.values()) {
			ChmMaterialModel j = JSON.parseObject(obj.toString(),ChmMaterialModel.class);
			chmArray.add(j);
		}
		return chmArray;
	}
	/**
	 * 加盖机拿出
	 */
	public static ChmMaterialModel getChmsMaterial(ChmMaterialModel cuhmMaterialModel){
		Object ob = setAndGetUtil.getInstrument("chm",cuhmMaterialModel.getDeviceid());
		if(ob!=null){
			ChmMaterialModel chmMaterial = JSONObject.parseObject(ob.toString(),ChmMaterialModel.class);
			return chmMaterial;
		}
		return null;
	}




	/**
	 * pcr放入
	 */
	public static Boolean setPcrMaterial(PcrMaterialModel pcrMaterialModel,boolean isSend){
		boolean result = true;
		synchronized (PcrMaterialModel.class) {
			result = setAndGetUtil.setInstrument("pcr", pcrMaterialModel.getDeviceid(), pcrMaterialModel);
		}
		//如果推送
		if(isSend){
			SendMessgeToHtml.pushMessage(MaterialTypeEnum.MATERIAL_CUBICS.getCode());
		}
		return result;
	}
	/**
	 * pcr拿出
	 */
	public static  PcrMaterialModel getPcrMaterial(PcrMaterialModel pcrMaterialModel){
		Object ob = setAndGetUtil.getInstrument("pcr",pcrMaterialModel.getDeviceid());
		if(ob!=null){
			PcrMaterialModel pcrMaterial = JSONObject.parseObject(ob.toString(),PcrMaterialModel.class);
			return pcrMaterial;
		}
		return null;
	}
	/**
	 * 所有定量仪仪拿出
	 */
	public  static JSONArray getPcrMaterialList(){
		Map<Object, Object> map = setAndGetUtil.getInstruList("pcr");
		JSONArray pcrArray = new JSONArray();
		for(Object obj : map.values()) {
			PcrMaterialModel j = JSON.parseObject(obj.toString(),PcrMaterialModel.class);
			pcrArray.add(j);
		}
		return pcrArray;
	}
	/**
	 * 测序仪放入
	 */
	public static Boolean setSeqMaterial(SeqMaterialModel seqMaterialModel,boolean isSend){
		boolean result = true;
		synchronized (SeqMaterialModel.class) {
			result = setAndGetUtil.setInstrument("seq", seqMaterialModel.getDeviceid(), seqMaterialModel);
		}
		//如果推送
		if(isSend){
			SendMessgeToHtml.pushMessage(MaterialTypeEnum.MATERIAL_CUBICS.getCode());
		}
		return result;
	}
	/**
	 * 建库仪拿出
	 */
	public static SeqMaterialModel getSeqMaterial(SeqMaterialModel seqMaterialModel){
		Object ob = setAndGetUtil.getInstrument("seq",seqMaterialModel.getDeviceid());
		if(ob!=null){
			SeqMaterialModel seqMaterial = JSONObject.parseObject(ob.toString(),SeqMaterialModel.class);
			return seqMaterial;
		}
		return null;
	}
	/**
	 * 所有配置仪仪拿出
	 */
	public  static JSONArray getSeqMaterialList(){
		Map<Object, Object> map = setAndGetUtil.getInstruList("seq");
		JSONArray seqArray = new JSONArray();
		for(Object obj : map.values()) {
			SeqMaterialModel j = JSON.parseObject(obj.toString(),SeqMaterialModel.class);
			seqArray.add(j);
		}
		return seqArray;
	}

	/**
	 * 所有前料区信息拿出
	 */
	public  static Map<Object, Object> getFrontMaterialList(){
		Map<Object, Object> map = setAndGetUtil.getInstruList("fma");
		if(map!=null){
			return map;
		}
		return null;
	}
	/**
	 * 所有机器人身上信息拿出
	 */
	public  static Map<Object, Object> getAgvMaterialList(){
		Map<Object, Object> map = setAndGetUtil.getInstruList("agv");
		if(map!=null){
			return map;
		}
		return null;
	}
	/**
	 * 所有后物料区信息拿出
	 */
	public  static Map<Object, Object> getOCMaterialList(){
		Map<Object, Object> map = setAndGetUtil.getInstruList("auto_desk");
		if(map!=null){
			return map;
		}
		return null;
	}

	public static void setInstrumentWz(String key,String value){
		setAndGetUtil.setInstrumentWz( key, value);
	}

	public static String getInstrumentWz(String key,String command){
		if(!Command.CUBICS.toString().equals(command)){
			key = command+key;
		}
		Map<String,Object>map=setAndGetUtil.getInstrumentWz();
		return map.get(key).toString();
	}
	public static  Map<String,EpVehicleModel>  getWzEpMap(String item){
		return  setAndGetUtil.getAllEpMap(item);

	}
	public static Map<String,EpVehicleModel>  getDeskEpMap(String item){

		return setAndGetUtil.getAllEpMap(item) ;

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
