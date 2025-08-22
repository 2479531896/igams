package com.matridx.las.netty.receivehandleimpl;

import com.matridx.igams.common.redis.RedisStreamUtil;
import com.matridx.las.netty.dao.entities.*;
import com.matridx.las.netty.global.InstrumentStateGlobal;
import com.matridx.las.netty.global.material.AgvDesktopGlobal;
import com.matridx.las.netty.global.material.AutoDesktopGlobal;
import com.matridx.las.netty.global.material.FrontMaterialAreaGlobal;
import com.matridx.las.netty.global.material.InstrumentMaterialGlobal;
import com.matridx.las.netty.service.svcinterface.IJkywzszService;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.channel.command.SendBaseCommand;
import com.matridx.las.netty.channel.domain.Command;
import com.matridx.las.netty.channel.domain.CommandDetails;
import com.matridx.las.netty.dobackimpl.AgvCallBackImpl;
import com.matridx.las.netty.enums.GoodsEnum;
import com.matridx.las.netty.enums.RobotStatesEnum;
import com.matridx.las.netty.global.RobotManagementGlobal;
import com.matridx.las.netty.util.RedisSetAndGetUtil;
import com.matridx.las.netty.util.SpringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Service
public class AvgReceiveHandleImpl extends BaseReceiveHandleImpl {
	@Autowired
	private AgvCallBackImpl agvCallBackImpl;
	@Autowired
	private FrontMaterialAreaGlobal frontMaterialAreaGlobal;

	@Autowired
	AutoDesktopGlobal autoDesktopGlobal;
	@Autowired
	private AgvDesktopGlobal agvDesktopGlobal;
	@Autowired
	private InstrumentMaterialGlobal instrumentMaterialGlobal;
	@Autowired
	private RedisStreamUtil redisStreamUtil;
	@Autowired
	RedisSetAndGetUtil redisSetAndGetUtil;

	public boolean receiveHandle(FrameModel recModel) {
		// 需要做的事情
		SendBaseCommand sendBaseCommand = new SendBaseCommand();
		JSONObject rejsonObject = JSONObject.parseObject(recModel.getCmdParam()[0]);
		log.info("机器人接受的参数："+rejsonObject);
		log.info(String.format("机器人接收消息，命令为%s,设备id%s",recModel.getCmd(),recModel.getDeviceID()));
		// 机器人发送充电量达到指定电量，需要判断是否有任务需要完成，如果有，则需要过来完成任务如果没有，则继续充电
		if (recModel.getCommand().equals(Command.AGV.toString())
				&& recModel.getMsgType().equals(CommandDetails.AVG_GE)) {
			RobotManagementGlobal.setElectric(recModel.getCmdParam()[0]);
			if (RobotManagementGlobal.getRemainingTasks() > 0
					&& Integer.valueOf(RobotManagementGlobal.getElectric()) > Integer
							.valueOf(RobotManagementGlobal.getElectric_quantity_charge_min())) {
				// 调用机器人停止充电命令
				sendBaseCommand.stopRobotElectricQuantity(recModel.getDeviceID(), agvCallBackImpl, true);
				// 改状态，改队列
				InstrumentStateGlobal.changeInstrumentState(Command.AGV.toString(),recModel.getDeviceID(), RobotStatesEnum.ROBOT_ONLINE.getCode());
				//RobotManagementGlobal.putRobotQueue(recModel.getDeviceID());
				InstrumentStateGlobal.putInstrumentQueuesToRedis(Command.AGV.toString(),recModel.getDeviceID());
			} else if (Integer.valueOf(RobotManagementGlobal.getElectric()) > Integer
					.valueOf(RobotManagementGlobal.getElectric_quantity_charge_min())) {
				sendBaseCommand.stopRobotElectricQuantity(recModel.getDeviceID(), agvCallBackImpl, true);

				// 改状态，改队列
				InstrumentStateGlobal.changeInstrumentState(Command.AGV.toString(),recModel.getDeviceID(), RobotStatesEnum.ROBOT_ONLINE.getCode());
				InstrumentStateGlobal.putInstrumentQueuesToRedis(Command.AGV.toString(),recModel.getDeviceID());
				//RobotManagementGlobal.putRobotQueue(recModel.getDeviceID());
			}
		}
		System.out.print(recModel.getMsgInfo());
		return true;
	}

	// 取夹爪方法
	public String[] takeClaw(MlpzDto mlpzDto, SjlcDto sjlcDto,Map<String,String> ma) {
		String deviceID = ma.get(mlpzDto.getXylx());
		String[] strArr = new String[2];
		String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx()) ? sjlcDto.getParamlx() : mlpzDto.getParamlx();
		switch (param) {
			// 6-八联管物料夹爪
			case "takePcrClaw_agv":
				strArr[0] = "6";
				break;
			// 7-八联管盖夹爪
			case "takeLidClaw_agv":
				strArr[0] = "7";
				break;
			// 7-八联管/eq管载具夹爪
			case "takeZjClaw_agv":
				strArr[0] = "4";
				AgvDesktopGlobal.setClampingNum("4");
				break;
			//卡盒载具夹爪
			case "cardBoxAndGunHead_agv":
				strArr[0] = GoodsEnum.BOX_Vehicle_CLAW.getCode();
				break;
			//开门夹爪
			case "takeKmClaw_agv":
				strArr[0] = GoodsEnum.OPENDOOR_CLAW.getCode();
				break;
			//卡盒物料夹爪
			case "takeKhwlClaw_agv":
				strArr[0] = GoodsEnum.BOX_CLAW.getCode();
				break;
			//EP管夹爪
			case "takeEPClaw_agv":
				strArr[0] = GoodsEnum.EP_CLAW.getCode();
				break;
			//枪头夹爪
			case "takeQtClaw_agv":
				strArr[0] = GoodsEnum.TIP_CLAW.getCode();
				break;
			//测序仪卡盒夹爪
			case "takeCxzjClaw_agv":
				strArr[0] = "1";
				break;
		}
		return strArr;
	}

	// 机器人移动到某个位置
	public String[] moveMethod(MlpzDto mlpzDto, SjlcDto sjlcDto,Map<String,String> ma) {
		String deviceID = ma.get(mlpzDto.getXylx());
		String[] strArr = new String[2];
		String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx())? sjlcDto.getParamlx() : mlpzDto.getParamlx();
		switch (param) {
			// 移动到配置仪
			case "goAuto_agv":
				strArr[0] = "4";
				break;
			// 移动到压盖机
			case "goCmh_agv":
				strArr[0] = "5";
				break;
			// 移动到PCR
			case "goPcr_agv":
				strArr[0] = "6";
				break;
			// 移动到建库仪后面的物料区
			case "goWl_agv":
				strArr[0] = "3";
				break;
			//移动到前料区
			case "goQlq_agv":
				//获取前料区位置				
				strArr[0] = FrontMaterialAreaGlobal.getWz();
				break;
			//移动到建库仪前
			case "goJkyq_agv":
				strArr[0] = GoodsEnum.MOVE_CUBICS.getCode();
				break;
			//移动到测序仪卡盒
			case "goCx_agv":
				strArr[0] = "7";
				break;
		}

		return strArr;
	}

	// 拍照方法
	public String[] picMethod(MlpzDto mlpzDto, SjlcDto sjlcDto,Map<String,String> ma) {
		String deviceID = ma.get(mlpzDto.getXylx());
		String[] strArr = new String[2];
		String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx())? sjlcDto.getParamlx() : mlpzDto.getParamlx();
		switch (param) {
			//  对测序仪载具台面
			case "setCx_agv":
				strArr[0] = "7";
				break;
			// 对配置仪台面进行拍照
			case "setAuto_agv":
				strArr[0] = "4";
				break;
			//  对压盖机台面进行拍照
			case "setCmh_agv":
				strArr[0] = "5";
				break;
			//  对Pcr台面进行拍照
			case "setPcr_agv":
				strArr[0] = "6";
				break;
			//  对建库仪后面的物料区进行拍照
			case "setWl_agv":
				strArr[0] = "3";
				break;
			//卡盒来料
			case "setCardBox_agv":
				strArr[0] = GoodsEnum.PIC_FRONTMATERIAL.getCode();
				break;
			//18-建库仪开关门拍照位置
			case "setCubsPz_agv":
				//获取当前仪器管道id
				String gdid= ma.get(Command.CUBICS.toString());
				//根据管道id获取建库仪位置
				IJkywzszService jkywzszService = (IJkywzszService) SpringUtil.getBean("jkywzszServiceImpl");
				JkywzszDto jkywzszDto =  jkywzszService.queryById(gdid);
				if(StringUtil.isNotBlank(jkywzszDto.getJkywzbh())) {
						strArr[0] = jkywzszDto.getJkykgmwzbh();
				}
				//区分建库仪开门位置和放料位置
				strArr[1] = GoodsEnum.MOVE_PIC.getCode();
				break;
			//建库仪取放料拍照位
			case "setCubWl_agv":
				//获取当前仪器管道id
				String gdid_t= ma.get(Command.CUBICS.toString());
				//根据管道id获取建库仪位置
				IJkywzszService jkywzszService_t = (IJkywzszService) SpringUtil.getBean("jkywzszServiceImpl");
				JkywzszDto jkywzszDto_t  =  jkywzszService_t.queryById(gdid_t);
				if(StringUtil.isNotBlank(jkywzszDto_t.getJkywzbh())) {
					//当前位置号加1
					strArr[0] = jkywzszDto_t.getJkywzbh();
				}
				//区分建库仪开门拍照和放料位置
				strArr[1] = GoodsEnum.TAKE_PIC.getCode(); //放料位置拍照
				break;
		}
		return strArr;
	}

	// 过渡点
	public String[] setTransition(MlpzDto mlpzDto, SjlcDto sjlcDto,Map<String,String> ma) {
		String deviceID = ma.get(mlpzDto.getXylx());
		String[] strArr = new String[2];
		String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx())? sjlcDto.getParamlx() : mlpzDto.getParamlx();
		switch (param) {
			// 1-配置仪过渡点
			case "setAuto_agv":
				strArr[0] = "5";
				break;
			// 1-压盖机过渡点
			case "setCmh_agv":
				strArr[0] = "7";
				break;
			// 1-Pcr过渡点
			case "setPcr_agv":
				strArr[0] = "6";
				break;
			// 1-载具空间过渡点
			case "setWl_agv":
				strArr[0] = "4";
				break;
			//卡盒载具来料工作位置
			case "setCardBox_agv":
				strArr[0] = "1";
				break;
			//建库仪放料及开关门位置
			case "setCubs_agv":
				//获取当前仪器管道id
				String gdid= ma.get(Command.CUBICS.toString());
				//根据管道id获取建库仪位置
				IJkywzszService jkywzszService = (IJkywzszService) SpringUtil.getBean("jkywzszServiceImpl");
				JkywzszDto jkywzszDto  =  jkywzszService.queryById(gdid);
				if(StringUtil.isNotBlank(jkywzszDto.getJkywzbh())) {
					//判断建库仪位置编号
					if(Integer.valueOf(jkywzszDto.getJkywzbh())>0 && Integer.valueOf(jkywzszDto.getJkywzbh())<9) {
						strArr[0] = "2";
					}else {
						strArr[0] = "3";
					}
				}
				break;
		}
		return strArr;
	}

	// 设置工作空间
	public String[] setWorkspace(MlpzDto mlpzDto, SjlcDto sjlcDto,Map<String,String> ma) {
		String deviceID = ma.get(mlpzDto.getXylx());
		String[] strArr = new String[2];
		String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx())? sjlcDto.getParamlx() : mlpzDto.getParamlx();
		switch (param) {
			// 1-配置仪开门工作空间
			case "setAuto_agv":
				strArr[0] = "5";
				break;
			// 1-压盖机工作空间
			case "setCmh_agv":
				strArr[0] = "7";
				break;
			// 1-PCR工作空间
			case "setPcr_agv":
				strArr[0] = "6";
				break;
			// 1-载具空间工作空间
			case "setWl_agv":
				strArr[0] = "4";
				break;
			//卡盒来料空间
			case "setKhll_agv":
				strArr[0] = "1";
				break;
			//建库仪开门空间
			case "setJkyopen_agv":
				strArr[0] = "3";
				break;
		}
		return strArr;
	}

	// 放夹爪方法
	public String[] dropClaw(MlpzDto mlpzDto, SjlcDto sjlcDto,Map<String,String> ma) {
		String deviceID = ma.get(mlpzDto.getXylx());
		String[] strArr = new String[2];
		String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx()) ? sjlcDto.getParamlx() : mlpzDto.getParamlx();
		switch (param) {
			// 1-测序仪卡盒夹爪
			case "dropCxzjClaw_agv":
				strArr[0] = "1";
				break;
			// 6-八联管物料夹爪
			case "dropPcrClaw_agv":
				strArr[0] = "6";
				break;
			// 6-八联管盖夹爪
			case "dropLidClaw_agv":
				strArr[0] = "7";
				break;
			// 6-八联管载具夹爪
			case "dropZjClaw_agv":
				strArr[0] = "4";
				AgvDesktopGlobal.setClampingNum(null);
				break;
			//托盘枪头载具夹爪
			case "dropTp_agv":
				strArr[0] = GoodsEnum.BOX_Vehicle_CLAW.getCode();
				break;
			//放开门夹爪
			case "dropOpenOrDown_agv":
				strArr[0] = GoodsEnum.OPENDOOR_CLAW.getCode();
				break;
			//卡盒物料夹爪
			case "dropKhwlClaw_agv":
				strArr[0] = GoodsEnum.BOX_CLAW.getCode();
				break;
			//EP管夹爪
			case "dropEPClaw_agv":
				strArr[0] = GoodsEnum.EP_CLAW.getCode();
				break;
			//枪头夹爪
			case "dropQtClaw_agv":
				strArr[0] = GoodsEnum.TIP_CLAW.getCode();
				break;
		}
		return strArr;
	}

	// 放入到AGV平台载具的
	public String[] dropMatter(MlpzDto mlpzDto, SjlcDto sjlcDto,Map<String,String> ma) {
		String deviceID = ma.get(sjlcDto.getZyyqlx());
		String[] strArr = new String[2];
		String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx()) ? sjlcDto.getParamlx() : mlpzDto.getParamlx();
		switch (param) {
			// 6八连管载具
			case "dropEight_agv":
				AutoMaterialModel autoMaterialModel = new AutoMaterialModel();
				autoMaterialModel.setDeviceid(ma.get(Command.AUTO.toString()));
				AutoMaterialModel autoMaterialModel1 = instrumentMaterialGlobal.getAutoMaterial(autoMaterialModel);
				OctalianpipeListModel octalianpipeListModel = autoMaterialModel1.getOctalianpipeList();
				strArr[0] = "6";
				strArr[1] = String.valueOf(octalianpipeListModel.getOctNum()*2-1);// 从redis中获取是偶数乘以2是奇数乘以2减1
				break;
			// 6八连管载具
			case "dropEightCmh_agv":
				strArr[0] = "6";
				OctalianpipeListModel octalianpipeListModel2 = agvDesktopGlobal.getAgv_octalianpipe();
				ChmMaterialModel chmMaterialModel = new ChmMaterialModel();
//				chmMaterialModel.setDeviceid(ma.get(Command.CMH.toString()));
//				ChmMaterialModel chmMaterialModel1 = instrumentMaterialGlobal.getChmsMaterial(chmMaterialModel);
				if (octalianpipeListModel2.getOctgNum()%2 == 0){
					strArr[1] = String.valueOf((octalianpipeListModel2.getOctNum() + 1)*2-1);
				}else{
					strArr[1] = String.valueOf((octalianpipeListModel2.getOctNum() + 2)*2-1);
				}
//				if (null != octalianpipeListModel2){
//					strArr[1] = String.valueOf((octalianpipeListModel2.getOctNum() + octalianpipeListModel2.getOctgNum() + 1)*2-1);
//				}
				break;
			// 6八连管载具
			case "dropWl_agv":
				strArr[0] = "6";
				OctalianpipeListModel octalianpipeListModel3 = agvDesktopGlobal.getAgv_octalianpipe();
				strArr[1] = octalianpipeListModel3.getOctNum().toString();
				octalianpipeListModel3.setOctNum(octalianpipeListModel3.getOctNum() +1);
				break;
			//将托盘放置机器人平台
			case "dropTp_agv":
				//获取平台全局变量
				Map<String , YsybxxDto> agv_tray1 = AgvDesktopGlobal.getAgv_tray1();
				Map<String , YsybxxDto> agv_tray2 = AgvDesktopGlobal.getAgv_tray2();
				if(agv_tray1==null || agv_tray1.size()==0) {
					strArr[0] = GoodsEnum.TYAR_ONE.getCode();
					break;
				}
				if(agv_tray2==null || agv_tray2.size()==0) {
					strArr[0] = GoodsEnum.TYAR_TWO.getCode();
				}
				break;
			//将枪头载具放置机器人平台
			case "dropQt_agv":			
				strArr[0] = GoodsEnum.AGVDESKTOP_GUNHEAD.getCode();
				break;
			//将EP管载具放置机器人平台
			case "dropEP_agv":
				strArr[0] = "3";
				break;
				
		}
		return strArr;
	}

	// 从AGV平台载具取东西
	public String[] takeMatter(MlpzDto mlpzDto, SjlcDto sjlcDto,Map<String,String> ma) {
		String[] strArr = new String[2];
		String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx()) ? sjlcDto.getParamlx() : mlpzDto.getParamlx();
		OctalianpipeListModel octalianpipeListModel = agvDesktopGlobal.getAgv_octalianpipe();
		switch (param) {
			// 6八连管载具
			case "takeEight_agv":
				strArr[0] = "6";
				strArr[1] = String.valueOf(octalianpipeListModel.getOctNum()*2-1);// 从redis中获取
				break;
			// 6八连管载具
			case "takePcrEight_agv":
				strArr[0] = "6";
				strArr[1] = String.valueOf(octalianpipeListModel.getOctgNum()*2-1);// 从redis中获取
				break;
			// 6八连管载具
			case "takeEightGJ_agv":
				strArr[0] = "6";
				strArr[1] = String.valueOf(octalianpipeListModel.getNum()*2);// 从redis中获取
				break;
			//满EP管载具
			case "takeEp_agv":
				//获取全局变量，判断机器人平台是否存在Ep管载具
				EpVehicleModel agv_ep1 = AgvDesktopGlobal.getAgv_ep1();
				EpVehicleModel agv_ep2 = AgvDesktopGlobal.getAgv_ep2();
				EpVehicleModel agv_ep3 = AgvDesktopGlobal.getAgv_ep3();
				//判断Ep1是否不为空
				if(agv_ep1.getEpList()!=null) {
					strArr[0] = "3";
				}else if(agv_ep2.getEpList()!=null) {
					strArr[0] = "3";
				}else if(agv_ep3.getEpList()!=null) {
					strArr[0] = "4";
				}
				break;
			//从AGV平台卡盒载具取料
			case "takeYb_agv":
				//获取全局变量
				Map<String , YsybxxDto> agv_tray1 = AgvDesktopGlobal.getAgv_tray1();
				if(agv_tray1!=null && agv_tray1.size()>0) {
					for (Entry<String, YsybxxDto> entry : agv_tray1.entrySet()) {
						//如果存在，取当前样本
						if(StringUtil.isNotBlank(entry.getKey())) {
							strArr[0] = GoodsEnum.TYAR_ONE.getCode();
							strArr[1] = entry.getKey();
							break;
						}
			        }
				}else {
					Map<String , YsybxxDto> agv_tray2 = AgvDesktopGlobal.getAgv_tray2();
					if(agv_tray2!=null && agv_tray2.size()>0) {
						for (Entry<String, YsybxxDto> entry : agv_tray2.entrySet()) {
							//如果存在，取当前样本
							if(StringUtil.isNotBlank(entry.getKey())) {
								strArr[0] = GoodsEnum.TYAR_TWO.getCode();
								strArr[1] = entry.getKey();
								break;
							}
				        }
					}
				}
				break;
			//从AGV平台EP管载具取料,右
			case "takeEPG_right_agv":
				//获取全局变量
				List<AgvEpModel> agv_ep_right  = redisSetAndGetUtil.getDeskEpMap(sjlcDto.getParam()).getEpList();
				if(agv_ep_right!=null && agv_ep_right.size()>0) {
					String xzb = agv_ep_right.get(0).getXzb();
					String yzb = agv_ep_right.get(0).getYzb();
					String bh = String.valueOf((Integer.valueOf(xzb) - 1)*8 + Integer.valueOf(yzb));
					strArr[1] = bh;
				}
				strArr[0] = GoodsEnum.AGVEP_VEHICLE_ONE.getCode();
				break;
			//从AGV平台EP管载具取料,左
			case "takeEPG_left_agv":
				//获取全局变量
				List<AgvEpModel> agv_ep_left  = redisSetAndGetUtil.getDeskEpMap(sjlcDto.getParam()).getEpList();
				if(agv_ep_left!=null && agv_ep_left.size()>0) {
					String xzb = agv_ep_left.get(1).getXzb();
					String yzb = agv_ep_left.get(1).getYzb();
					String bh = String.valueOf((Integer.valueOf(xzb) - 1)*8 + Integer.valueOf(yzb));
					strArr[1] = bh;
				}
				strArr[0] = GoodsEnum.AGVEP_VEHICLE_ONE.getCode();
				break;
			//从AGV平台枪头载具取料，左
			case "takeQt_left_agv":
				strArr[0] = GoodsEnum.AGVDESKTOP_GUNHEAD.getCode();
				GunVehicleModel gunVehicleModel = AgvDesktopGlobal.getAgv_gunhead();
				if(gunVehicleModel!=null&&gunVehicleModel.getGunhead()!=null && gunVehicleModel.getGunhead().size()>0) {
					List<String> agv_gunhead = gunVehicleModel.getGunhead();
					strArr[1] = agv_gunhead.get(1);
				}
				break;
			//从AGV平台枪头载具取料，右
			case "takeQt_right_agv":
				strArr[0] = GoodsEnum.AGVDESKTOP_GUNHEAD.getCode();
				GunVehicleModel gunVehicleModel1 = AgvDesktopGlobal.getAgv_gunhead();
				if(gunVehicleModel1!=null) {
					List<String> agv_gunhead_right = gunVehicleModel1.getGunhead();
					if (agv_gunhead_right != null && agv_gunhead_right.size() > 0) {
						strArr[1] = agv_gunhead_right.get(0);
					}
				}
				break;
			//托盘
			case "takeTp_agv":
				//获取全局变量
				Map<String , YsybxxDto> agv_tray1_s = AgvDesktopGlobal.getAgv_tray1();
				Map<String , YsybxxDto> agv_tray2_s = AgvDesktopGlobal.getAgv_tray2();
				if(agv_tray1_s!=null && agv_tray1_s.size()==0) {
					strArr[1] = GoodsEnum.TYAR_ONE.getCode();
					AgvDesktopGlobal.setAgv_tray1(null,true);
				}else if(agv_tray2_s!=null && agv_tray2_s.size()==0) {
					strArr[1] = GoodsEnum.TYAR_TWO.getCode();
					AgvDesktopGlobal.setAgv_tray2(null,true);
				}
				break;
		}
		return strArr;
	}

	// 抓取八连管方法
	public String[] takeEight(MlpzDto mlpzDto, SjlcDto sjlcDto,Map<String,String> ma) {
		String deviceID = ma.get(mlpzDto.getXylx());
		String[] strArr = new String[2];
		String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx()) ? sjlcDto.getParamlx() : mlpzDto.getParamlx();
		switch (param) {
			// 从1-6中获取
			case "takeWl_agv":
				OctalianpipeListModel octalianpipeListModel = autoDesktopGlobal.getauto_octalianpipe1();
				OctalianpipeListModel octalianpipeListModel1 = agvDesktopGlobal.getAgv_octalianpipe();
				boolean pd = false;
				if (null != octalianpipeListModel){
					strArr[0] = "3";
				}else{
					octalianpipeListModel = autoDesktopGlobal.getauto_octalianpipe2();
					strArr[0] = "4";
					pd = true;
				}
				if (octalianpipeListModel.getOctNum()>octalianpipeListModel1.getOctbNum()){
					strArr[1] = octalianpipeListModel1.getOctbNum().toString();
					octalianpipeListModel1.setOctbNum(octalianpipeListModel1.getOctbNum() -1);
					agvDesktopGlobal.setAgv_octalianpipe(octalianpipeListModel1,true);
					octalianpipeListModel.setOctNum(octalianpipeListModel.getOctNum() -1);

				}
				if (pd){
					autoDesktopGlobal.setauto_octalianpipe2(octalianpipeListModel,true);
				}else{
					autoDesktopGlobal.setauto_octalianpipe1(octalianpipeListModel,true);
				}
				break;
		}
		return strArr;
	}

	// 抓取载具方法
	public String[] takeVehicles(MlpzDto mlpzDto, SjlcDto sjlcDto,Map<String,String> ma) {
		String deviceID = ma.get(mlpzDto.getXylx());
		String[] strArr = new String[2];
		String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx()) ? sjlcDto.getParamlx() : mlpzDto.getParamlx();
		switch (param) {
			// 从测序仪载具区取载具
			case "seq_agv":
				strArr[0] = "7";
				break;
			// 从机器人平台取载具
			case "seq_auto":
				strArr[0] = "7";
				break;
			// 从1-6中获取
			case "eight_agv":
				strArr[0] = "6";
				break;
			// 从1-6中获取
			case "eight2_agv":
				strArr[0] = "6";
				break;
			// 从1-6中获取
			case "ep_agv":
				strArr[0] = "4";
				break;
			// 机器人平台上得ep管载具
			case "ep2_agv":
				strArr[0] = "4";
				break;
			// 从1-6中获取
			case "takeWl_agv":
				OctalianpipeListModel octalianpipeListModel = autoDesktopGlobal.getauto_octalianpipe1();
				strArr[0] = "3";
//					autoDesktopGlobal.setauto_octalianpipe2(null);
//					agvDesktopGlobal.setAgv_octalianpipe(octalianpipeListModel);
				break;
			case "takeTp_agv":
				List<TrayModel> fma_tray  = FrontMaterialAreaGlobal.getFma_tray();
				if(fma_tray!=null && fma_tray.size()>0) {
					for (int i = 0; i < fma_tray.size(); i++) {
						if("1".equals(fma_tray.get(i).getZt())) {
							strArr[0] = fma_tray.get(i).getTpbh();
							break;
						}
					}
				}
				break;
			case "takeQt_agv":
				strArr[0] = GoodsEnum.FRONTMATERIAL_GUNHEAD.getCode();
				break;
			case "takeEP_agv":
				EpVehicleModel epVehicleModel1 = FrontMaterialAreaGlobal.getFma_ep1();
				List<AgvEpModel> fma_ep1 = epVehicleModel1.getEpList();
				if(fma_ep1!=null && fma_ep1.size()>0) {
					strArr[0] = "5";
				}
				break;				
		}
		return strArr;
	}
	
	//放载具到物料区方法
	public String[] dropToWlq(MlpzDto mlpzDto, SjlcDto sjlcDto,Map<String,String> ma) {
		String deviceID = ma.get(mlpzDto.getXylx());
		String[] strArr = new String[2];
		String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx()) ? sjlcDto.getParamlx() : mlpzDto.getParamlx();
		switch (param) {
		case "drop_Ep_agv":
			//获取全局变量，判断机器人平台是否存在Ep管载具
			EpVehicleModel agv_ep1 = AgvDesktopGlobal.getAgv_ep1();
			EpVehicleModel agv_ep2 = AgvDesktopGlobal.getAgv_ep2();
			EpVehicleModel agv_ep3 = AgvDesktopGlobal.getAgv_ep3();
			//判断Ep1是否不为空
			if(agv_ep1.getEpList()!=null) {
				strArr[0] = "5";
			}else if(agv_ep2.getEpList()!=null) {
				strArr[0] = "6";
			}else if(agv_ep3.getEpList()!=null) {
				strArr[0] = "7";
			}
			break;
			// 抓取托盘载具
			case "takeTp_agv":
				List<TrayModel> fma_tray = FrontMaterialAreaGlobal.getFma_tray();
				if(fma_tray!=null && fma_tray.size()>0) {
					for (TrayModel trayModel : fma_tray) {
						if(trayModel.getBoxList()!=null && trayModel.getBoxList().size()>0) {
							strArr[0] = trayModel.getTpbh();
							break;
						}
					}
				}
				break;
			//抓取枪头载具
			case "takeQt_agv":
				strArr[0] = "8";
				break;
			//抓取EP管载具
			case "takeEP_agv":
				//获取当前机器人平台EP管载具情况
				List<AgvEpModel> fma_ep1 = FrontMaterialAreaGlobal.getFma_ep1().getEpList();
				List<AgvEpModel> fma_ep2 = FrontMaterialAreaGlobal.getFma_ep2().getEpList();
				List<AgvEpModel> fma_ep3 = FrontMaterialAreaGlobal.getFma_ep3().getEpList();
				if(fma_ep1!=null && fma_ep1.size()>0) {
					if(fma_ep2!=null && fma_ep2.size()>0) {
						strArr[0] = "5";
						break;
					}
				}
				if(fma_ep3!=null && fma_ep3.size()>0) {
					strArr[0] = "6";
				}
				break;
			//抓卡盒载具
			case "takeKh_agv":
				//获取全局变量
				List<TrayModel> fma_tray_t = FrontMaterialAreaGlobal.getFma_tray();
				if(fma_tray_t!=null && fma_tray_t.size()>0) {
					for (TrayModel trayModel : fma_tray_t) {
						if("3".equals(trayModel.getZt())) {
							strArr[0] = trayModel.getTpbh();
							break;
						}
					}
				}
				break;
		}
		return strArr;
	}

	// 释放八连管到PCR方法
	public String[] dropEight(MlpzDto mlpzDto, SjlcDto sjlcDto,Map<String,String> ma) {
		String deviceID = ma.get(mlpzDto.getXylx());
		String[] strArr = new String[2];
		String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx()) ? sjlcDto.getParamlx() : mlpzDto.getParamlx();
		switch (param) {
			// 从1-6中获取 好似废弃
			case "dropWl_agv":
				PcrMaterialModel pcrMaterialModel = new PcrMaterialModel();
				pcrMaterialModel.setDeviceid(sjlcDto.getZyyqlx());
				PcrMaterialModel pcrMaterialModel1 = instrumentMaterialGlobal.getPcrMaterial(pcrMaterialModel);
				OctalianpipeListModel octalianpipeListModel = agvDesktopGlobal.getAgv_octalianpipe();
				if (null != pcrMaterialModel1){
					if (null != octalianpipeListModel && octalianpipeListModel.getOctNum() > 0){
						pcrMaterialModel1.getOctalianpipeList().getMap().put(String.valueOf(octalianpipeListModel.getOctgNum()),octalianpipeListModel.getMap().get(String.valueOf(octalianpipeListModel.getOctgNum())));
						pcrMaterialModel1.getOctalianpipeList().setOctNum(pcrMaterialModel1.getOctalianpipeList().getOctNum()+1);
						pcrMaterialModel1.getOctalianpipeList().setOctgNum(pcrMaterialModel1.getOctalianpipeList().getOctgNum()+1);
//						pcrMaterialModel1.getOctalianpipeList().setOctalList(octalianpipeListModel.getOctalList());
						strArr[0] = String.valueOf(octalianpipeListModel.getOctgNum());
						instrumentMaterialGlobal.setPcrMaterial(pcrMaterialModel1,true);
						octalianpipeListModel.getMap().remove(String.valueOf(octalianpipeListModel.getOctgNum()));
						octalianpipeListModel.setOctgNum(octalianpipeListModel.getOctgNum() -1);
//						octalianpipeListModel.setOctbNum(octalianpipeListModel.getOctbNum() +1);
						agvDesktopGlobal.setAgv_octalianpipe(octalianpipeListModel,true);
					}
				}else{
					if (null != octalianpipeListModel && octalianpipeListModel.getOctNum() > 0){
						Map<String,List<OctalYbxxxModel>> map = new HashMap<>();
						map.put(String.valueOf(octalianpipeListModel.getOctgNum()),octalianpipeListModel.getMap().get(String.valueOf(octalianpipeListModel.getOctgNum())));
						OctalianpipeListModel octalianpipeListModel1 = new OctalianpipeListModel();
						octalianpipeListModel1.setOctNum(1);
						octalianpipeListModel1.setOctgNum(1);
						octalianpipeListModel1.setMap(map);
//						octalianpipeListModel1.setOctalList(octalianpipeListModel.getOctalList());
						pcrMaterialModel.setOctalianpipeList(octalianpipeListModel1);
						instrumentMaterialGlobal.setPcrMaterial(pcrMaterialModel,true);
						strArr[0] = String.valueOf(octalianpipeListModel.getOctgNum());
						octalianpipeListModel.getMap().remove(String.valueOf(octalianpipeListModel.getOctgNum()));
						octalianpipeListModel.setOctgNum(octalianpipeListModel.getOctgNum() -1);
//						octalianpipeListModel.setOctbNum(octalianpipeListModel.getOctbNum() +1);
						agvDesktopGlobal.setAgv_octalianpipe(octalianpipeListModel,true);
					}
				}
				break;
		}
		return strArr;
	}

	// 释放载具方法
	public String[] dropVehicles(MlpzDto mlpzDto, SjlcDto sjlcDto,Map<String,String> ma) {
		String deviceID = ma.get(Command.AUTO.toString());
		String[] strArr = new String[2];
		String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx()) ? sjlcDto.getParamlx() : mlpzDto.getParamlx();
		OctalianpipeListModel octalianpipeListModel1 = autoDesktopGlobal.getauto_octalianpipe1();
		OctalianpipeListModel octalianpipeListModel2 = autoDesktopGlobal.getauto_octalianpipe2();
		AutoMaterialModel autoMaterialModel = new AutoMaterialModel();
		autoMaterialModel.setDeviceid(deviceID);
		AutoMaterialModel autoMaterialModel1 = instrumentMaterialGlobal.getAutoMaterial(autoMaterialModel);
		switch (param) {
			//放测序仪卡盒到机器人平台
			case "seq_agv":
				strArr[0] = "7";
				break;
			//放测序仪卡盒到配置仪
			case "seq_auto":
				strArr[0] = "3";
				break;
			//往自身放置八连管载具
			case "eightTo_agv":
				strArr[0] = "6";
				break;
			// 配置仪物料区中
			case "eight_agv":
				if (null == octalianpipeListModel1.getMap()){
					strArr[0] = "3";
				}
				break;
			// 配置仪物料区中
			case "eight2_agv":
				if (null == octalianpipeListModel2.getMap()){
					strArr[0] = "2";
				}
				break;
			// 放到前物料区中ba
			case "ep_agv":
				EpVehicleModel epVehicleModel_q = frontMaterialAreaGlobal.getFma_ep3();
				List<AgvEpModel> maps2 = epVehicleModel_q.getEpList() ;
				if (null == maps2){
					strArr[0] = "7";
				}
				break;
			// 放到前物料区中
			case "ep2_agv":
				EpVehicleModel epVehicleModel = frontMaterialAreaGlobal.getFma_ep2();
				List<AgvEpModel> map = epVehicleModel.getEpList() ;
				if (null == map){
					strArr[0] = "6";
				}
				break;
			// 从配置仪中八连管
			case "dropEight_agv":
				strArr[0] = "6";
				break;
			// 从配置仪中ep管
			case "dropEp_agv":
				if (null != autoMaterialModel1){
					EpVehicleModel epVehicleModel1 = agvDesktopGlobal.getAgv_ep3();
					List<AgvEpModel> maps = epVehicleModel1.getEpList();
					if (maps == null ){
						strArr[0] = "4";
//						epVehicleModel3.setEpList(autoMaterialModel1.getSampleEpList());
//						agvDesktopGlobal.setAgv_ep3(epVehicleModel3);
//						autoMaterialModel1.setSampleEpList(null);
//						instrumentMaterialGlobal.setAutoMaterial(autoMaterialModel,true);
					}
				}
				break;

			case "dropWl_agv":

				if (null != octalianpipeListModel1){
					strArr[0] = "4";
					octalianpipeListModel1 = new OctalianpipeListModel();
					autoDesktopGlobal.setauto_octalianpipe2(octalianpipeListModel1,true);
				}else{
					strArr[0] = "3";
					octalianpipeListModel1 = new OctalianpipeListModel();
					autoDesktopGlobal.setauto_octalianpipe1(octalianpipeListModel1,true);
				}
				agvDesktopGlobal.setAgv_octalianpipe(null,true);
				break;
			// 从1-6中获取
			case "dropbzj_agv":
				strArr[0] = "6";
				break;
		}
		return strArr;
	}
	
	
	//建库仪开关门方法
	public String[] OpenOrDownDoor(MlpzDto mlpzDto, SjlcDto sjlcDto,Map<String,String> ma) {
		String deviceID = ma.get(mlpzDto.getXylx());
		String[] strArr = new String[2];
		String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx()) ? sjlcDto.getParamlx() : mlpzDto.getParamlx();
		switch (param) {
		case "open_cubis_agv":
			//获取当前仪器管道id
			String gdid_t= ma.get(Command.CUBICS.toString());
			//根据管道id获取建库仪位置
			
			IJkywzszService jkywzszService_t = (IJkywzszService) SpringUtil.getBean("jkywzszServiceImpl");
			JkywzszDto jkywzszDto_t =  jkywzszService_t.queryById(gdid_t);
			if(StringUtil.isNotBlank(jkywzszDto_t.getJkywzbh())) {
				//当前位置号加1
				strArr[0] = jkywzszDto_t.getJkywzbh();
			}
			break;
		}
		return strArr;
	}
	
	//放料到建库仪方法
	public String[] putInCubis(MlpzDto mlpzDto, SjlcDto sjlcDto,Map<String,String> ma) {
		String deviceID = ma.get(mlpzDto.getXylx());
		String[] strArr = new String[2];
		String param = !StringUtils.isNoneBlank(mlpzDto.getParamlx()) ? sjlcDto.getParamlx() : mlpzDto.getParamlx();
		switch (param) {
		//放样本
		case "putKh_agv":
			strArr[0] = GoodsEnum.PUTIN_CARDBOX_TOCUBICS.getCode();
			break;
		//放右ep管
		case "putEp_right_agv":
			strArr[0] = GoodsEnum.RIGHT_EP.getCode();
			break;
		//放左ep管
		case "putEp_left_agv":
			strArr[0] = GoodsEnum.LEFT_EP.getCode();
			break;
		//右枪头
		case "putQt_right_agv":
			strArr[0] = GoodsEnum.RIGHT_TIP.getCode();
			break;
		//左枪头
		case "putQt_left_agv":
			strArr[0] = GoodsEnum.LEFT_TIP.getCode();
			break;
		}
		return strArr;
	}
	
}