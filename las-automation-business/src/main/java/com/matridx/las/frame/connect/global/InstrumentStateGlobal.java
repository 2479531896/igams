package com.matridx.las.frame.connect.global;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.alibaba.fastjson.JSONArray;
import com.matridx.igams.common.redis.RedisStreamUtil;
import com.matridx.las.frame.netty.util.RedisSetAndGetUtil;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.las.frame.connect.channel.command.FrameModel;
import com.matridx.las.frame.connect.channel.domain.Command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class InstrumentStateGlobal {

	//private static Logger log = LoggerFactory.getLogger(InstrumentStateGlobal.class);

	// 仪器队列
	private static Map<String, BlockingQueue<String>> instrumentQueues = new HashMap<>();
	//已被占用的仪器deviceid的list
	private static List<String> haveTakeList = new ArrayList<>();
	// 仪器流程所用的仪器记录
	//private static List<Map<String, String>> instrumentUsedList = new ArrayList<Map<String, String>>();
	//仪器目前接收收到的完成消息
	//private static Map<String, FrameModel> instrumentFinMap = new HashMap<>();
	//文库开始信息
	//private static Map<String, Map<String, Object>> libaryInfMap = new HashMap<String, Map<String, Object>>();
	//手动导入excel文件后执行开始需要用到的
	private static Map<String, FrameModel> pcrdoStartMap = new HashMap<>();

	static {
		instrumentQueues.put(Command.PCR.toString(), new LinkedBlockingQueue<String>());
		instrumentQueues.put(Command.SEQ.toString(), new LinkedBlockingQueue<String>());
	}

	@Autowired
	RedisStreamUtil redisStreamUtil;
	private static RedisStreamUtil streamUtil;
	@Autowired
	RedisSetAndGetUtil redisSetAndGetUtil;
	private static RedisSetAndGetUtil setAndGetUtil;

	@PostConstruct
	public void init() {
		streamUtil = redisStreamUtil;
		setAndGetUtil = redisSetAndGetUtil;
	}


	/**
	 * 拿出所有信息
	 *
	 * @return
	 */
	public static Map<String, FrameModel> getPcrdoStartMapAll() {
		return pcrdoStartMap;
	}

	//放入空闲仪器
	public static void putInstrumentQueuesToRedis(String commd, String deviceId) {
		String groupStr = "";
		if (Command.CUBICS.toString().equals(commd)) {
			return;
		} else if (Command.SEQ.toString().equals(commd)) {
			groupStr = "SeqGroup";
		} else if (Command.PCR.toString().equals(commd)) {
			groupStr = "PcrGroup";
		} 
		//读取数据
		List<MapRecord<String, Object, Object>> mapRecordList = streamUtil.range(groupStr);
		boolean flag = true;
		//循环流中的数据判断是否有重复得deviceid
		for (MapRecord<String, Object, Object> mapRecord : mapRecordList) {
			for (Object obj : mapRecord.getValue().keySet()) {
				String key = String.valueOf(obj);
				if (key.equals(deviceId)) {
					flag = false;
					break;
				}
			}
		}
		if (flag) {
			streamUtil.add(groupStr, deviceId, deviceId);
		}


	}


	public static MapRecord<String, Object, Object> getYQQueueFromRedis(String commd) {
		String groupStr = "";
		//todo不要名称
		if (Command.CUBICS.toString().equals(commd)) {
			groupStr = "CubisGroup";
		} else if (Command.SEQ.toString().equals(commd)) {
			groupStr = "SeqGroup";
		} else if (Command.PCR.toString().equals(commd)) {
			groupStr = "PcrGroup";
		} 
		Consumer consumer = Consumer.from(groupStr + "1", StringUtil.generateUUID());
		//PendingMessages messages=redisStreamUtil.pending("lassGroup",consumer);
		// 读取设置
		StreamReadOptions options = StreamReadOptions.empty();
		// 设置堵塞读取多少时间
		options = options.block(Duration.ZERO);
		// 设置读取数目
		options = options.count(1);
		// 设置自动提交
		options = options.autoAcknowledge();

		List<MapRecord<String, Object, Object>> map1 = streamUtil.read(consumer, options, groupStr);

		return map1.get(0);
	}

	public static void removeYqQueuesFromRedis(String commd, String RecordId) {
		String groupStr = "";
		if (Command.CUBICS.toString().equals(commd)) {
			groupStr = "CubisGroup";
		}  else if (Command.SEQ.toString().equals(commd)) {
			groupStr = "SeqGroup";
		} else if (Command.PCR.toString().equals(commd)) {
			groupStr = "PcrGroup";
		} 

		streamUtil.del(groupStr, RecordId);
	}

	public static void removeYqQueuesFromRedis1(String commd, String deviceId) {

		String groupStr = "";
		if (Command.CUBICS.toString().equals(commd)) {
			groupStr = "CubisGroup";
		}  else if (Command.SEQ.toString().equals(commd)) {
			groupStr = "SeqGroup";
		} else if (Command.PCR.toString().equals(commd)) {
			groupStr = "PcrGroup";
		} 
		List<MapRecord<String, Object, Object>> mapRecordList = streamUtil.range(groupStr);
		String RecordId = "";
		for (MapRecord<String, Object, Object> mapRecord : mapRecordList) {
			RecordId = mapRecord.getId().toString();
			for (Object obj : mapRecord.getValue().keySet()) {
				String key = String.valueOf(obj);
				if (key.equals(deviceId)) {
					streamUtil.del(groupStr, RecordId);
					return;
				}
			}
		}
	}

	/**
	 * @param commd
	 * @param decidId
	 * @return
	 */
	public static Map<String, String> getInstrumentUsedListMap(String commd, String decidId) {

		return setAndGetUtil.getInstrumentUsedList(commd, decidId);
	}

	public static void setInstrumentUsedList(Map<String, String> map) {
		synchronized (InstrumentStateGlobal.class) {
			setAndGetUtil.setInstrumentUsedList(map);
		}
	}

	public static boolean modInstrumentUsedListMap(Map<String, String> map) {
		return setAndGetUtil.modInstrumentUsedListMap(map);
	}

	public static JSONArray getAllInstrumentUsedList() {
		JSONArray objects = setAndGetUtil.getAllInstrumentUsedList();
		return objects;
	}

	public static void setInstrumentUsedList_Map(Map<String, String> map, String command, String deviceId) {
		setAndGetUtil.setInstrumentUsedList_Map(map, command, deviceId);
	}

	public static void removeInstrumentUsedList_Map(Map<String, String> map, String command, String deviceId) {
		setAndGetUtil.removeInstrumentUsedList_Map(map, command, deviceId);
	}

	public static void removeInstrumentUsetMap(Map<String, String> map) {
		synchronized (InstrumentStateGlobal.class) {
			// 遍历获取map对象，并删除
			setAndGetUtil.removeInstrumentUsetMap(map);
		}
	}

	/**
	 * 获取仪器的完成消息
	 *
	 * @param deviedid 仪器id
	 * @return
	 */
	public static FrameModel getInstrumentFinMap(String deviedid) {

		return setAndGetUtil.getInstrumentFinMap(deviedid);
	}

	/**
	 * 放入的完成消息
	 *
	 * @param deviedid   仪器id
	 * @param frameModel 完成的消息
	 */
	public static void setInstrumentFinMap(String deviedid, FrameModel frameModel) {
		setAndGetUtil.setInstrumentFinMap(deviedid, frameModel);
	}

	public static FrameModel getPcrdoStartMap(String wkid) {

		return setAndGetUtil.getPcrdoStartMap(wkid);
	}

	public static void setPcrdoStartMap(String wkid, FrameModel frameModel) {
		setAndGetUtil.setPcrdoStartMap(wkid, frameModel);
	}

	/**
	 * 获取文库信息
	 *
	 * @param deviedid
	 * @return
	 */
	public static Map<String, Object> getLibaryInfMap(String deviedid) {

		return setAndGetUtil.getLibaryInfMap(deviedid);
	}

	/**
	 * 获所有取文库信息
	 *
	 * @param
	 * @return
	 */
	public static Map<String, Object> getLibaryInfMapAll() {

		return setAndGetUtil.getLibaryInfMapAll();
	}

	/**
	 * 放入文库信息
	 *
	 * @param deviedid
	 * @param map
	 */
	public static void setLibaryInfMap(String deviedid, Map<String, Object> map) {
		setAndGetUtil.setLibaryInfMap(deviedid, map);
	}

	/**
	 * 文库id和配液仪id绑定
	 *
	 * @param
	 * @return
	 */
	public static String getAutoIdWkid(String wkid) {

		return setAndGetUtil.getAutoIdWkid(wkid);
	}

	/**
	 * 文库id和配液仪id绑定
	 *
	 * @param deviedid
	 * @param
	 */
	public static void setAutoIdWkid(String wkid, String deviedid) {
		setAndGetUtil.setAutoIdWkid(wkid, deviedid);
	}

	/**
	 * 文库id和配液仪id绑定,以仪器为主
	 *
	 * @param
	 * @return
	 */
	public static String getWkidByAutoId(String deviceid) {

		return setAndGetUtil.getWkidByAutoId(deviceid);
	}

	/**
	 * 文库id和配液仪id绑定
	 *
	 * @param deviedid
	 * @param
	 */
	public static void setWkidByAutoId(String deviedid, String wkid) {
		setAndGetUtil.setWkidByAutoId(deviedid, wkid);
	}

	public List<String> getHaveTakeList() {
		return haveTakeList;
	}

}
