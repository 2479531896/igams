package com.matridx.las.netty.global;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.matridx.igams.common.redis.RedisStreamUtil;
import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.channel.domain.Command;
import com.matridx.las.netty.dao.entities.CubisUpParamModel;
import com.matridx.las.netty.dao.entities.CubsMaterialModel;
import com.matridx.las.netty.enums.InstrumentStatusEnum;
import com.matridx.las.netty.global.material.InstrumentMaterialGlobal;
import com.matridx.las.netty.util.RedisSetAndGetUtil;
import com.matridx.las.netty.util.SendMessgeToHtml;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.matridx.las.netty.util.CommonChannelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;


@Service
public class CubsParmGlobal {

	private static Logger log = LoggerFactory.getLogger(CubsParmGlobal.class);
	// 建库仪是将仪器单独放进队列,建库仪本身不放队列
	private static BlockingQueue<String> cubQueues = new LinkedBlockingQueue<String>();
	//建库仪的通道号和对应的接受到的完成的消息
	private static Map<String,FrameModel> cubFinMap = new HashMap<>();
	//已上报的建库一通道信息
	private static Map<String,String> havaUpCubis = new HashMap<>();

	// 建库仪的收到完成的消息
	public static ConcurrentLinkedQueue<FrameModel> cubFinQueues = new ConcurrentLinkedQueue<FrameModel>();


	public static Object lockObj;


	private static boolean isStartAuto =false;

	private static boolean isWorklc= false;

	private static boolean isAutoWork1= false;

	private static boolean isAutoWork= false;

	@Autowired
	RedisStreamUtil redisStreamUtil;
	@Autowired
	RedisSetAndGetUtil redisSetAndGetUtil;
	private static  RedisStreamUtil streamUtil;
	private static  RedisSetAndGetUtil setAndGetUtil;
	@PostConstruct
	public void init(){
		streamUtil = redisStreamUtil;
		setAndGetUtil = redisSetAndGetUtil;
	}

	public static Logger getLog() throws InterruptedException {
		return log;
	}

	public static void setLog(Logger log) {
		CubsParmGlobal.log = log;
	}



	public static boolean getIsStartAuto() {
		return setAndGetUtil.getIsStartWork();
	}

	public static void setIsStartAuto(boolean isStartAuto) {
		setAndGetUtil.setIsStartWork(isStartAuto);
	}

	public static boolean getIisIsWorklc() {
		return setAndGetUtil.getIsWorklc();
	}

	public static void setIsWorklc(boolean isWorklc) {
		setAndGetUtil.setisWorklc(isWorklc);
	}

	public static boolean getIsIsAutoWork1() {
		return setAndGetUtil.getIsAutoWork1();
	}

	public static void setIsAutoWork1(boolean isAutoWork1) {
		setAndGetUtil.setIsAutoWork1(isAutoWork1);
	}

	public static boolean getIsIsAutoWork() {
		return setAndGetUtil.getIsAutoWork();
	}

	public static void setIsAutoWork(boolean isAutoWork) {
		setAndGetUtil.setIsAutoWork(isAutoWork);
	}



/*	// 将空闲的建库仪通道放入队列中放入队列中，这里加一个，如果没有设置过，则不能放入，必须等到已经设置过才能放入
	public static void putListCubQueues(List<CubisUpParamModel> list, String deviceID) {
		Map<String,String> map = getHavaUpCubis();
		synchronized (cubQueues) {
			//上报的时候先清空队列
			cubQueues.clear();
			//上报的时候清空原本已上报信息
			removeAllHavaUpCubis();
			if (list != null && list.size() > 0) {
				CommonChannelUtil.putCubs(deviceID, list);
				for (CubisUpParamModel cubChl : list) {
					if(StringUtil.isNotBlank(cubChl.getCubsChannel())){
						String cubChlId = cubChl.getCubsChannel();
						//修改建库仪状态
						CubsMaterialModel cubsMaterialModel = new CubsMaterialModel();
						cubsMaterialModel.setPassId(cubChlId);
						CubsMaterialModel cubsMaterialModel1 = InstrumentMaterialGlobal.getCubsMaterial(cubsMaterialModel);
						if(!"Error".equals(cubChl.getProcessStage())){
							map.put(cubChlId,InstrumentStatusEnum.STATE_FREE.getCode());
							if(!cubQueues.contains(cubChlId)){
								//判断下这个建库仪是否已经设置，如果有，才放入队列
								if(cubsMaterialModel1!=null) {
									cubQueues.add(cubChlId);
									cubsMaterialModel1.setState(InstrumentStatusEnum.STATE_FREE.getCode());
								}else{
									log.info("有未设置的通道号：" + cubChlId);
								}
							}
						}else{
							map.put(cubChlId,InstrumentStatusEnum.STATE_FREE.getCode());
							if(cubsMaterialModel1!=null) {

								//修改建库仪状态
								cubsMaterialModel1.setState(InstrumentStatusEnum.STATE_ERROR.getCode());
							}
						}
						if(cubsMaterialModel1!=null) {
							InstrumentMaterialGlobal.setCubsMaterial(cubsMaterialModel1,false);
						}
					}
				}
				//上线之后推送一下
				SendMessgeToHtml.pushMessage(Command.CUBICS.toString());
				log.info("cubiss中空闲仪器个数：" + cubQueues.size());
			}
		}
	}*/
	public static void setCubFinQueues(FrameModel frameModel){
		setAndGetUtil.setCubFinQueues("CubFinQueue",frameModel);
	}

	public static long getCubFinQueuesSize(){
		return setAndGetUtil.getCubFinQueuesSize("CubFinQueue");
	}

	public static FrameModel getCubFinQueues(){
		return setAndGetUtil.getLeftByQubinsFinQueue("CubFinQueue");
	}

	public static void delQubinsFinQueue(){
		setAndGetUtil.delQubinsFinQueue("CubFinQueue");
	}

	public static void putListCubQueuesToRedis(List<CubisUpParamModel> list, String deviceID) {
		List<MapRecord<String, Object, Object>> list1= streamUtil.range("CubisGroup");
		for(MapRecord<String, Object, Object>mapRecord:list1){
			streamUtil.del("CubisGroup",mapRecord.getId().toString());
		}
		Long a=streamUtil.len("CubisGroup");
			//上报的时候先清空队列
			//上报的时候清空原本已上报信息
			removeAllHavaUpCubis();
			Map<String,String> map = getHavaUpCubis();

		if (list != null && list.size() > 0) {
				CommonChannelUtil.putCubs(deviceID, list);
				for (CubisUpParamModel cubChl : list) {
					if(StringUtil.isNotBlank(cubChl.getCubsChannel())){
						String cubChlId = cubChl.getCubsChannel();
						//修改建库仪状态
						CubsMaterialModel cubsMaterialModel = new CubsMaterialModel();
						cubsMaterialModel.setPassId(cubChlId);
						CubsMaterialModel cubsMaterialModel1 = InstrumentMaterialGlobal.getCubsMaterial(cubsMaterialModel);
						if(!"Error".equals(cubChl.getProcessStage())){
							map.put(cubChlId,InstrumentStatusEnum.STATE_FREE.getCode());
							list1= streamUtil.read("CubisGroup");
							int i=0;
							for(MapRecord<String, Object, Object>mapRecord:list1){
								Map<Object,Object>mapRecordValue=mapRecord.getValue();
								if(mapRecordValue.get(cubChlId)!=null){
									i++;
								}
							}
							if(i==0){
								if(cubsMaterialModel1!=null) {
									streamUtil.add("CubisGroup",cubChlId,cubChlId);
									cubsMaterialModel1.setState(InstrumentStatusEnum.STATE_FREE.getCode());
								}else{
									log.info("有未设置的通道号：" + cubChlId);
								}
							}
 						}else{
							map.put(cubChlId,InstrumentStatusEnum.STATE_FREE.getCode());
							if(cubsMaterialModel1!=null) {

								//修改建库仪状态
								cubsMaterialModel1.setState(InstrumentStatusEnum.STATE_ERROR.getCode());
							}
						}
						if(cubsMaterialModel1!=null) {
							InstrumentMaterialGlobal.setCubsMaterial(cubsMaterialModel1,false);
						}
					}
				}
				setHavaUpCubis(map);

				//上线之后推送一下
				SendMessgeToHtml.pushMessage(Command.CUBICS.toString());
				log.info("cubiss中空闲仪器个数：" + cubQueues.size());
			}
	}

	/**
	 * 上报得建库仪，跟新建库仪状态
	 * @param list
	 * @param deviceID
	 */
	public static void setListCubQueues(List<CubisUpParamModel> list, String deviceID) {
		synchronized (cubQueues) {
			if (list != null && list.size() > 0) {
				//CommonChannelUtil.putCubs(deviceID, list);
				for (CubisUpParamModel cubChl : list) {
					if(StringUtil.isNotBlank(cubChl.getCubsChannel())){
						String cubChlId = cubChl.getCubsChannel();
						//建库仪出现error得仪器移除队列，并修改其状态
						if("Error".equals(cubChl.getProcessStage())){
							if(cubQueues.contains("cubChlId")){
								cubQueues.remove(cubChlId);
							}
							//修改建库仪状态
							CubsMaterialModel cubsMaterialModel = new CubsMaterialModel();
							cubsMaterialModel.setPassId(cubChlId);
							CubsMaterialModel cubsMaterialModel1 = InstrumentMaterialGlobal.getCubsMaterial(cubsMaterialModel);
							cubsMaterialModel1.setState(InstrumentStatusEnum.STATE_ERROR.getCode());
							InstrumentMaterialGlobal.setCubsMaterial(cubsMaterialModel1,false);
						}
					}

				}
				log.info("cubiss中空闲仪器个数：" + cubQueues.size());
			}
		}
		//推送所有状态
		SendMessgeToHtml.pushMessage(Command.CUBICS.toString());
	}


	// 移除建库仪通道
	public static void removeListCubQueues(List<String> list, String deviceID) {
		synchronized (cubQueues) {
			if (list != null && list.size() > 0) {
				for (String cubChlId : list) {
					CommonChannelUtil.removeCubs(deviceID, cubChlId);
					cubQueues.remove(cubChlId);
					CubsMaterialModel cubsMaterialModel = new CubsMaterialModel();
					cubsMaterialModel.setPassId(cubChlId);
					cubsMaterialModel.setState(InstrumentStatusEnum.STATE_OFFLINE.getCode());
					InstrumentMaterialGlobal.setCubsMaterial(cubsMaterialModel,false);
				}
				log.info("cubiss中空闲仪器个数：" + cubQueues.size());
			}
			SendMessgeToHtml.pushMessage(Command.CUBICS.toString());
		}
	}

	// 根据建库仪注册的系统id移除建库仪通道
	public static void removeCubQueuesByDevice(String deviceID) {
		synchronized (cubQueues) {
			// 通过注册信息获取建库仪各个通道
			List<String> list = CommonChannelUtil.getCubsList(deviceID);
			if (list != null && list.size() > 0) {
				for (String cubChlId : list) {
					CommonChannelUtil.removeCubs(deviceID, cubChlId);
					cubQueues.remove(cubChlId);
					CubsMaterialModel cubsMaterialModel = new CubsMaterialModel();
					cubsMaterialModel.setPassId(cubChlId);
					cubsMaterialModel.setState(InstrumentStatusEnum.STATE_OFFLINE.getCode());
					InstrumentMaterialGlobal.setCubsMaterial(cubsMaterialModel,false);
				}
			}
			SendMessgeToHtml.pushMessage(Command.CUBICS.toString());
			log.info("cubiss中空闲仪器个数：" + cubQueues.size());
		}
	}

	// 将空闲的建库仪通道放入队列中放入队列中
	public static void putCubQueues(String cubChlId) {
		synchronized (cubQueues) {
			cubQueues.add(cubChlId);
			log.info("cubiss中空闲仪器个数：" + cubQueues.size());
		}
	}

	public static void putCubQueuesFromRedis(String cubChlId) {
		streamUtil.add("CubisGroup",cubChlId,cubChlId);
		log.info("cubiss中空闲仪器个数：" + streamUtil.len("CubisGroup"));
	}


	// 取出Cubs队列
	public static BlockingQueue<String> getCubQueues1() {
		return cubQueues;
	}



	/**
	 * 获取通道的完成消息
	 * @param passid 建库仪通道号
	 * @return
	 */
	public static FrameModel getCubFinMap(String passid) {
		return setAndGetUtil.getCubFinMap(passid);
	}

	/**
	 * 获取整个通道消息信息
	 * @return
	 */
	public static Map<String,FrameModel> getCubFinMapAll() {
		return CubsParmGlobal.cubFinMap;
	}
	/**
	 * 放入通道的完成消息
	 * @param passid 建库仪通道号
	 * @param frameModel 完成的建库仪消息
	 */
	public static void setCubFinMap(String passid,FrameModel frameModel) {

		setAndGetUtil.setCubFinMap(passid,frameModel);
	}


	public static Map<String, String> getHavaUpCubis() {
		return setAndGetUtil.getHavaUpCubis();
	}

	public static void setHavaUpCubis(Map<String, String> havaUpCubis) {

		setAndGetUtil.setHavaUpCubis(havaUpCubis);
	}
	public static void removeAllHavaUpCubis() {
		setAndGetUtil.clearHavaUpCubis();

	}
	public static Long getCubquenLenth() {
		return  streamUtil.len("CubisGroup");

	}


	public static void setCubFinQueues(ConcurrentLinkedQueue<FrameModel> cubFinQueues) {
		CubsParmGlobal.cubFinQueues = cubFinQueues;
	}
}
