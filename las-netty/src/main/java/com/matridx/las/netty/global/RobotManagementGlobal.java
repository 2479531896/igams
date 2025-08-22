package com.matridx.las.netty.global;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.matridx.igams.common.redis.RedisStreamUtil;
import com.matridx.las.netty.global.material.AgvDesktopGlobal;
import com.matridx.las.netty.util.RedisSetAndGetUtil;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.stereotype.Service;

import com.matridx.las.netty.channel.command.SendBaseCommand;
import com.matridx.las.netty.channel.domain.Command;
import com.matridx.las.netty.channel.domain.CommandDetails;
import com.matridx.las.netty.dobackimpl.AgvCallBackImpl;
import com.matridx.las.netty.enums.RobotStatesEnum;

/**
 * 机器人状态管理
 *
 * @author DELL
 *
 */
@Service
public class RobotManagementGlobal {
	@Autowired
	private static AgvCallBackImpl agvCallBackImpl;

	@Autowired
	RedisStreamUtil redisStreamUtil;

	@Resource(name = "agvCallBackImpl")
	public void setAgvCallBackImpl(AgvCallBackImpl agvCallBackImpl) {
		RobotManagementGlobal.agvCallBackImpl = agvCallBackImpl;
	}
	private static  RedisStreamUtil streamUtil;

	@PostConstruct
	public void init(){
		streamUtil = redisStreamUtil;
	}
	// 接受到命令，空闲时最小的电量
	@Value("${matridx.netty.electric_quantity_free_min:20}")
	private static String electric_quantity_free_min;
	// 接受到命令，正在路上时最小的电量
	@Value("${matridx.netty.electric_quantity_way_min:40}")
	private static String electric_quantity_way_min;
	// 接受到命令，正在充电时最小的电量
	@Value("${matridx.netty.electric_quantity_charge_min:60}")
	private static String electric_quantity_charge_min;
	private static Logger log = LoggerFactory.getLogger(RobotManagementGlobal.class);
	// 机器人状态管理的
	// 机器人剩余的任务数
	private static Integer remainingTasks = 0;
	// 机器人电量
	private static String electric;






	/**
	 * 1,正在执行命令，没电了不处理 2,刚执行一个命令，如果有下个命令等着，判断下电量，然后电量够的话就去干活，不够的话，就去充电（配置：干活的电量）
	 * 3,刚执行玩一个命令，如果没有下个流程等着，则都去充电 4,空闲去充电，如果在路上，有命令过来，判断电量，电量够就去执行命令，不够就去充电（路上的电量）
	 * 5,已经在充电，有机器发命令，判断电量（充电时的电量）
	 */
	/**
	 * 公用方法，当一段命令结束后检查还有没有后续的命令和判断机器人电量
	 *
	 * @param deviceId
	 */
	public static void isNextTaskAndElectricQuantity(String deviceId) {
		// 减去任务数
		RobotManagementGlobal.setRemainingTasks(1, false);
		// 当后续还有任务，判断电池是否有电
		SendBaseCommand sendBaseCommand = new SendBaseCommand();
		if (remainingTasks > 0) {
			// 发送消息看电池是否有电sendBsaeCommd，用同步的方式（后期看看会不会堵塞）
			//临时注销	boolean result = sendBaseCommand.getRobotElectricQuantity(Command.AGV, deviceId, CommandDetails.AVG_GE, "GE",
			//null, agvCallBackImpl);
			//todo临时
			AgvDesktopGlobal.setState( RobotStatesEnum.ROBOT_ONLINE.getCode());
			InstrumentStateGlobal.putInstrumentQueuesToRedis(Command.AGV.toString(),deviceId);

			//robotQueue.add(deviceId);
			// 判断电池大小，小于多少，就回去充电，否则加入到下一个步骤
				/*if (Integer.valueOf(electric) > Integer.valueOf(electric_quantity_free_min)) {
					// ，将状态变为空闲
					robotState = RobotStatesEnum.ROBOT_ONLINE.getCode();
					//robotQueue.add(deviceId);
					putRobotQueueFromRedis(deviceId);
				} else {
					//todo临时
					robotState = RobotStatesEnum.ROBOT_ONLINE.getCode();
					//robotQueue.add(deviceId);
					putRobotQueueFromRedis(deviceId);
					// 发送充电命令，临时注销
					*//*
			 * boolean result1 = sendBaseCommand.goRobotElectricQuantity(Command.AGV,
			 * deviceId, CommandDetails.AVG_GO, "GO", null, agvCallBackImpl); if (result1) {
			 * robotState = RobotStatesEnum.ROBOT_ON_WAY.getCode(); }
			 *//*
				}*/
		} else {
			// 没任务空闲的，就发送命令让机器人去充电,sendBsaeCommd
			// 发送充电命令临时注销
			/*
			 * boolean result1 = sendBaseCommand.goRobotElectricQuantity(Command.AGV,
			 * deviceId, CommandDetails.AVG_GO, "GO", null, agvCallBackImpl); if (result1) {
			 * robotState = RobotStatesEnum.ROBOT_ON_WAY.getCode(); }
			 */
			//todo 临时
			AgvDesktopGlobal.setState( RobotStatesEnum.ROBOT_ONLINE.getCode());
			//robotQueue.add(deviceId);
			InstrumentStateGlobal.putInstrumentQueuesToRedis(Command.AGV.toString(),deviceId);
		}

	}

	/**
	 * 公用方法，发送一条任务，发现机器正在充电，或者在充电的路上，（由于不是实时的，所以先不区分在路上，或是在充电）
	 * 则发送获取电量的，大于约定的某个电量，则让机器人停止充电，回到工作岗位
	 *
	 * @param deviceId
	 */
	/*public static void getAndSendElc(String deviceId) {
			// 当后续还有任务，判断电池是否有电
			SendBaseCommand sendBaseCommand = new SendBaseCommand();
			// 发送消息看电池是否有电sendBsaeCommd，用同步的方式（后期看看会不会堵塞）
			boolean result = sendBaseCommand.getRobotElectricQuantity(Command.AGV, deviceId, CommandDetails.AVG_GE, "GE", null,
					agvCallBackImpl);
			if (Integer.valueOf(electric) > Integer.valueOf(electric_quantity_charge_min)) {
				// 发送停止充电的命令，todo

				// ，将状态变为空闲
				AgvDesktopGlobal.setState( RobotStatesEnum.ROBOT_ONLINE.getCode());
				//robotQueue.add(deviceId);
				putRobotQueueFromRedis(deviceId);
			}
		
	}*/

	/**
	 * 1,当机器人在那充电时候，充电以达到可用电量，需要机器人主动通知
	 *
	 * @return
	 */

	/**
	 * 机器人上线下线时，数据初始化
	 */
	public static void initRobot() {

	}

	public static String getElectric_quantity_free_min() {
		return electric_quantity_free_min;
	}

	public static String getElectric_quantity_way_min() {
		return electric_quantity_way_min;
	}

	public static String getElectric_quantity_charge_min() {
		return electric_quantity_charge_min = "60";
	}

	public static String getElectric() {
		return electric;
	}

	public static void setElectric(String electric) {
		RobotManagementGlobal.electric = electric;
	}

	public static Integer getRemainingTasks() {
		return remainingTasks;
	}

	public static void setRemainingTasks(Integer num, boolean isAdd) {
		synchronized (RobotManagementGlobal.class) {
			if (isAdd) {
				remainingTasks = remainingTasks + num;
			} else {
				remainingTasks = remainingTasks - num;
			}

		}
	}



}
