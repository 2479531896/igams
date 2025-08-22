package com.matridx.las.netty.dobackimpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.matridx.igams.common.enums.YesNotEnum;
import com.matridx.las.netty.channel.domain.CommandDetails;
import com.matridx.las.netty.dao.entities.*;
import com.matridx.las.netty.enums.GlobalParmEnum;
import com.matridx.las.netty.enums.RedisStorageEnum;
import com.matridx.las.netty.global.material.FrontMaterialAreaGlobal;
import com.matridx.las.netty.global.material.InstrumentMaterialGlobal;
import com.matridx.las.netty.service.svcinterface.*;
import com.matridx.las.netty.util.ChangeEpUtil;
import com.matridx.las.netty.util.RedisSetAndGetUtil;
import com.matridx.las.netty.util.SpringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.channel.domain.Command;
import com.matridx.las.netty.global.material.AgvDesktopGlobal;
import com.matridx.las.netty.global.InstrumentStateGlobal;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class AgvCallBackImpl extends BaseCallBackImpl  {

	@Autowired
	private IMlpzService mlpzService  ;
	@Autowired
	RedisSetAndGetUtil redisSetAndGetUtil;
	/**
	 * 回调函数回调处理,启动停止，连接等
	 * 
	 * @param times     返回次数：1 第一次 2 第二次
	 * @param recModel  返回的帧信息
	 * @param sendModel 发送的帧信息
	 * @return 返回是否正确
	 */

	public boolean callfunc(int times, FrameModel recModel, FrameModel sendModel, SjlcDto sjlcDto) {
		try{
			//维护夹爪参数
			//放夹爪
			if(sendModel.getCmd().equals("PARKVEH")){
				System.out.println("12");
			}

			if(CommandDetails.AGV_DC.equals(recModel.getMsgType())){
				AgvDesktopGlobal.setClampingNum("");
			}
			//取夹爪
			if(CommandDetails.AGV_TC.equals(recModel.getMsgType())){
				AgvDesktopGlobal.setClampingNum(sendModel.getCmdParam()[0]);
			}
			if (StringUtil.isNotBlank(sendModel.getMlid())) {
				if(sendModel.getMlid().equals("3015")){
					System.out.println("12");
				}
				// 根据返回信息处理
				MlpzDto mlpzDto = new MlpzDto();
				mlpzDto.setMlid(sendModel.getMlid());
				mlpzDto = mlpzService.getDtoById(sendModel.getMlid());
				String deviceID = mlpzDto.getXylx();
				if (StringUtil.isNotBlank(mlpzDto.getMethodname()) && StringUtil.isNotBlank(mlpzDto.getParamlx())){
					//机器人放八连管到AVG上
					if ("dropMatter".equals(mlpzDto.getMethodname())){
						switch (mlpzDto.getParamlx()) {
							// 配置仪物料区中
							case "dropEightCmh_agv":
								OctalianpipeListModel octalianpipeListModel2 = AgvDesktopGlobal.getAgv_octalianpipe();
								ChmMaterialModel chmMaterialModel = new ChmMaterialModel();
								chmMaterialModel.setDeviceid(InstrumentStateGlobal.getInstrumentUsedListMap(recModel.getCommand(),recModel.getDeviceID()).get(Command.CMH.toString()));
								ChmMaterialModel chmMaterialModel1 = InstrumentMaterialGlobal.getChmsMaterial(chmMaterialModel);
								if(chmMaterialModel1!=null){
									if (octalianpipeListModel2.getOctgNum()%2 == 0){
										octalianpipeListModel2.getMap().put(String.valueOf((octalianpipeListModel2.getOctNum() + 1)*2-1),chmMaterialModel1.getMap().get(String.valueOf(chmMaterialModel1.getOctgNum()+1)));
									}else{
										octalianpipeListModel2.getMap().put(String.valueOf((octalianpipeListModel2.getOctNum() + 2)*2-1),chmMaterialModel1.getMap().get(String.valueOf(chmMaterialModel1.getOctgNum()+1)));
									}
									octalianpipeListModel2.setOctgNum(octalianpipeListModel2.getOctgNum()+1);
									AgvDesktopGlobal.setAgv_octalianpipe(octalianpipeListModel2,true);
									chmMaterialModel1.getMap().remove(String.valueOf(chmMaterialModel1.getOctgNum()+1));
									InstrumentMaterialGlobal.setChmMaterial(chmMaterialModel1,true);
								}

								break;
							//枪头
							case "dropQt_agv":
								GunVehicleModel gv = AgvDesktopGlobal.getAgv_gunhead();
								gv.setGunhead(FrontMaterialAreaGlobal.getFma_gunhead1().getGunhead()); //前料区枪头设置到AGV平台
								gv.setSendSxType(FrontMaterialAreaGlobal.getFma_gunhead1().getSendSxType());
								AgvDesktopGlobal.setAgv_gunhead(gv,true);
								GunVehicleModel gv2 = FrontMaterialAreaGlobal.getFma_gunhead1(); //前料区枪头载具设置为null
								gv2.setGunhead(null);
								FrontMaterialAreaGlobal.setFma_gunhead1(gv2,true);
								break;

							//EP管载具
							case "dropEP_agv":
								//获取参数
								String[] cmdParam_t = sendModel.getCmdParam();
								if(GlobalParmEnum.AGV_EP_WZ1.getCode().equals(cmdParam_t[0])) {
									//设置EP1
									EpVehicleModel epVehicleModel1 = FrontMaterialAreaGlobal.getFma_ep1();
									EpVehicleModel epVehicleModelA = AgvDesktopGlobal.getAgv_ep1();
									epVehicleModelA.setEpList(epVehicleModel1.getEpList());
									AgvDesktopGlobal.setAgv_ep1(epVehicleModelA,true);
									//移除前料区
									epVehicleModel1.setEpList(null);
									FrontMaterialAreaGlobal.setFma_ep1(epVehicleModel1,true);
								}
								break;
						}
					}
					//去前料区托盘
					if ("takeVehicles".equals(mlpzDto.getMethodname())){
						switch (mlpzDto.getParamlx()) {
							// 拿载具
							case "takeTp_agv":
								//获取参数
								String[] cmdParam = sendModel.getCmdParam();
								List<TrayModel> fma_tray  = FrontMaterialAreaGlobal.getFma_tray();
								List<YsybxxDto> boxList = new ArrayList<>();
								Map<String , YsybxxDto> agv_tray = new HashMap<>();
								if(fma_tray!=null && fma_tray.size()>0) {
									for (int i = 0; i < fma_tray.size(); i++) {
										if(cmdParam[0].equals(fma_tray.get(i).getTpbh())) {
											//修改全局变量
											boxList = fma_tray.get(i).getBoxList();
											fma_tray.get(i).setBoxList(null); //前料区移除拿走的托盘
											fma_tray.get(i).setZt("3");
											FrontMaterialAreaGlobal.setFma_tray(fma_tray,true);
											break;
										}
									}
									//获取平台全局变量
									Map<String , YsybxxDto> agv_tray1 = AgvDesktopGlobal.getAgv_tray1();
									Map<String , YsybxxDto> agv_tray2 = AgvDesktopGlobal.getAgv_tray2();
									//存要更新得样本流程
									StringBuilder ids = new StringBuilder();
									//设置AGV平台全局变量
									if(boxList!=null && boxList.size()>0) {
										for (YsybxxDto agvsyybpModel : boxList) {
											agv_tray.put(agvsyybpModel.getTpnwzxh(), agvsyybpModel);
											ids.append(",").append(agvsyybpModel.getYsybid());
										}
									}
									if(StringUtil.isNotBlank(ids.toString())) {
										ids = new StringBuilder(ids.substring(1));
										//更新夹取托盘时间
										IYblcsjService yblcsjService = (IYblcsjService) SpringUtil.getBean("yblcsjServiceImpl");
										YblcsjDto yblcsjDto = new YblcsjDto();
										SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
										Date date = new Date();
										yblcsjDto.setJqrztpsj(sdf.format(date));
										yblcsjDto.setIds(ids.toString());
										yblcsjService.updateYblcsjById(yblcsjDto);
									}
									if(agv_tray1==null || agv_tray1.size()==0) {
										AgvDesktopGlobal.setAgv_tray1(agv_tray,true);
									}else if(agv_tray2==null || agv_tray2.size()==0) {
										AgvDesktopGlobal.setAgv_tray2(agv_tray,true);
									}
								}
								break;


						}
					}
					//机器人放八连管载具到AVG上
					if ("dropVehicles".equals(mlpzDto.getMethodname())){
						AutoMaterialModel autoMaterialModel = new AutoMaterialModel();
						autoMaterialModel.setDeviceid(InstrumentStateGlobal.getInstrumentUsedListMap(recModel.getCommand(),recModel.getDeviceID()).get(Command.AUTO.toString()));
						AutoMaterialModel autoMaterialModel1 = InstrumentMaterialGlobal.getAutoMaterial(autoMaterialModel);
						switch (mlpzDto.getParamlx()) {
							// 八连管载具
							case "dropEight_agv":
								if (null != autoMaterialModel1){
									AgvDesktopGlobal.setAgv_octalianpipe(autoMaterialModel1.getOctalianpipeList(),true);
									autoMaterialModel1.getOctalianpipeList().setMap(null);
									InstrumentMaterialGlobal.setAutoMaterial(autoMaterialModel1,true);
								}
								break;
							// ep管载具
							case "dropEp_agv":
								if (null != autoMaterialModel1){
									Map<String,EpVehicleModel> deskMap=redisSetAndGetUtil.getAllEpMap(RedisStorageEnum.EP_MAP_DESK.getCode());

									EpVehicleModel epVehicleModel1 = null;
									String keyD = "";
									for(String key:deskMap.keySet()){
										EpVehicleModel epVehicleModel2  = deskMap.get(key);
										if(epVehicleModel2!=null&&epVehicleModel2.isIsnull()){
											epVehicleModel1 = epVehicleModel2;
											keyD= key;
											break;
										}
									}
									List<AgvEpModel> listMap;
									if(epVehicleModel1!=null){
											epVehicleModel1.setEpList(autoMaterialModel1.getSampleEpList());
											epVehicleModel1.setIsnull(false);
											epVehicleModel1.setCsdm("ep3");
											redisSetAndGetUtil.setDeskEpMap(keyD,epVehicleModel1);
											autoMaterialModel1.setSampleEpList(null);
											InstrumentMaterialGlobal.setAutoMaterial(autoMaterialModel1,true);
									}
								}
								break;
						}
					}

					//机器人归还夹爪
					if ("dropClaw_agv".equals(mlpzDto.getMethodname())){
//					AutoMaterialModel autoMaterialModel = new AutoMaterialModel();
//					autoMaterialModel.setDeviceid(deviceID);
//					AutoMaterialModel autoMaterialModel1 = instrumentMaterialGlobal.getAutoMaterial(autoMaterialModel);
						if("dropTp_agv".equals(mlpzDto.getParamlx())){
							AgvDesktopGlobal.setClampingNum(null);
						}
//						switch (mlpzDto.getParamlx()) {
//							//托盘
//							case "dropTp_agv":
//								AgvDesktopGlobal.setClampingNum(null);
//								break;
//						}
					}

					//机器人取夹爪
					if ("takeClaw".equals(mlpzDto.getMethodname())){
						AutoMaterialModel autoMaterialModel = new AutoMaterialModel();
						autoMaterialModel.setDeviceid(deviceID);
						//AutoMaterialModel autoMaterialModel1 = InstrumentMaterialGlobal.getAutoMaterial(autoMaterialModel);
						switch (mlpzDto.getParamlx()) {
							//托盘
							case "cardBoxAndGunHead_agv":
								//获取参数
//								String[] cmdParam_t = sendModel.getCmdParam();
//								AgvDesktopGlobal.setClampingNum(cmdParam_t[0]);
//								break;
							//EP管夹爪
							case "takeZjClaw_agv":
								//获取参数
								String[] cmdParam = sendModel.getCmdParam();
								AgvDesktopGlobal.setClampingNum(cmdParam[0]);
								break;
						}
					}

					//载具放置前料区
					if ("dropToWlq".equals(mlpzDto.getMethodname())){
//					AutoMaterialModel autoMaterialModel = new AutoMaterialModel();
//					autoMaterialModel.setDeviceid(deviceID);
//					AutoMaterialModel autoMaterialModel1 = instrumentMaterialGlobal.getAutoMaterial(autoMaterialModel);
						switch (mlpzDto.getParamlx()) {
							//EP管载具
							case "drop_Ep_agv":
								String[] cmdParam = sendModel.getCmdParam();
								EpVehicleModel agv_ep1 = AgvDesktopGlobal.getAgv_ep1();
								EpVehicleModel agv_ep2 = AgvDesktopGlobal.getAgv_ep2();
								EpVehicleModel agv_ep3 = AgvDesktopGlobal.getAgv_ep3();
								if("5".equals(cmdParam[0])) {
									//设置全局变量,AGV平台设置为空，前料区设置AGV平台EP管数据
//									if(agv_ep1!=null) {
//
//									}
									EpVehicleModel ep1 = FrontMaterialAreaGlobal.getFma_ep1();
									ep1.setEpList(agv_ep1.getEpList());
									FrontMaterialAreaGlobal.setFma_ep1(ep1, true);
									agv_ep1.setEpList(null);
									AgvDesktopGlobal.setAgv_ep1(agv_ep1, true);
								}
								if("6".equals(cmdParam[0])) {
									//设置全局变量,AGV平台设置为空，前料区设置AGV平台EP管数据
//									if(agv_ep2!=null) {
////
////									}
									EpVehicleModel ep2 = FrontMaterialAreaGlobal.getFma_ep2();
									ep2.setEpList(agv_ep2.getEpList());
									FrontMaterialAreaGlobal.setFma_ep2(ep2, true);
									agv_ep2.setEpList(null);
									AgvDesktopGlobal.setAgv_ep2(agv_ep2, true);
								}
								if("7".equals(cmdParam[0])) {
									//设置全局变量,AGV平台设置为空，前料区设置AGV平台EP管数据
//									if(agv_ep3!=null) {
//
//									}
									EpVehicleModel ep3 = FrontMaterialAreaGlobal.getFma_ep3();
									ep3.setEpList(agv_ep3.getEpList());
									FrontMaterialAreaGlobal.setFma_ep3(ep3, true);
									agv_ep3.setEpList(null);
									AgvDesktopGlobal.setAgv_ep3(agv_ep3, true);
								}
								break;
							//卡盒载具
							case "takeKh_agv":
								//获取参数
								String[] cmdParam_t = sendModel.getCmdParam();
								List<TrayModel> fma_tray = FrontMaterialAreaGlobal.getFma_tray();
								for (TrayModel trayModel : fma_tray) {
									if(cmdParam_t[0].equals(trayModel.getTpbh())) {
										trayModel.setZt("0");
										trayModel.setBoxList(new ArrayList<>());
									}
								}
								FrontMaterialAreaGlobal.setFma_tray(fma_tray,true);
								break;

						}
					}

					//机器人从AGV台面载具取料
					if ("takeMatter".equals(mlpzDto.getMethodname())){
//					AutoMaterialModel autoMaterialModel = new AutoMaterialModel();
//					autoMaterialModel.setDeviceid(deviceID);
//					AutoMaterialModel autoMaterialModel1 = instrumentMaterialGlobal.getAutoMaterial(autoMaterialModel);
						switch (mlpzDto.getParamlx()) {
							//取卡盒
							case "takeYb_agv":
								//获取参数
								String[] cmdParam = sendModel.getCmdParam();
								Map<String , YsybxxDto> agv_tray;
								//移除托盘中拿出的样本
								if("1".equals(cmdParam[0])) {
									agv_tray = AgvDesktopGlobal.getAgv_tray1();
									modVariable(agv_tray,cmdParam[1],recModel);
									AgvDesktopGlobal.setAgv_tray1(agv_tray,true);
								}
								if("2".equals(cmdParam[0])) {
									agv_tray = AgvDesktopGlobal.getAgv_tray2();
									modVariable(agv_tray,cmdParam[1],recModel);
									AgvDesktopGlobal.setAgv_tray2(agv_tray,true);
								}
								break;
							//取EP管
							case "takeEPG_right_agv":
								//String[] cmdParam_t = sendModel.getCmdParam();
								//获取当前仪器位置通道号
								Map<String, String> instrumentUsedList_t  = InstrumentStateGlobal.getInstrumentUsedListMap(Command.AGV.toString(), recModel.getDeviceID());
								String gdid_t= instrumentUsedList_t.get(Command.CUBICS.toString());
								CubsMaterialModel cubsMaterialModel = new CubsMaterialModel();
								cubsMaterialModel.setPassId(gdid_t); //通道号
								cubsMaterialModel = InstrumentMaterialGlobal.getCubsMaterial(cubsMaterialModel);
								//存入Ep管
								if(cubsMaterialModel!=null){
									cubsMaterialModel.setEpRight(YesNotEnum.YES.getValue());
								}
								InstrumentMaterialGlobal.setCubsMaterial(cubsMaterialModel, true);
								EpVehicleModel epVehicleModel  = redisSetAndGetUtil.getDeskEpMap(sjlcDto.getParam());
								List<AgvEpModel> agv_ep1 = epVehicleModel.getEpList();
								agv_ep1.remove(0);
								epVehicleModel.setEpList(agv_ep1);
								redisSetAndGetUtil.setDeskEpMap(sjlcDto.getParam(),epVehicleModel);
								break;
							//取EP管
							case "takeEPG_left_agv":
								//String[] cmdParam_l = sendModel.getCmdParam();
								//获取当前仪器位置通道号
								Map<String, String> instrumentUsedList_left  = InstrumentStateGlobal.getInstrumentUsedListMap(Command.AGV.toString(), recModel.getDeviceID());
								String gdid_left= instrumentUsedList_left.get(Command.CUBICS.toString());
								CubsMaterialModel cubsMaterialModel_left = new CubsMaterialModel();
								cubsMaterialModel_left.setPassId(gdid_left); //通道号
								cubsMaterialModel_left = InstrumentMaterialGlobal.getCubsMaterial(cubsMaterialModel_left);
								//存入Ep管
								if(cubsMaterialModel_left!=null){
									cubsMaterialModel_left.setEpLeft(YesNotEnum.YES.getValue());
								}
								InstrumentMaterialGlobal.setCubsMaterial(cubsMaterialModel_left, true);
								EpVehicleModel epVehicleModelleft  = redisSetAndGetUtil.getDeskEpMap(sjlcDto.getParam());
								List<AgvEpModel> agv_ep_left = epVehicleModelleft.getEpList();
								agv_ep_left.remove(0);
								epVehicleModelleft.setEpList(agv_ep_left);
								redisSetAndGetUtil.setDeskEpMap(sjlcDto.getParam(),epVehicleModelleft);
								break;
							//取枪头
							case "takeQt_right_agv":
								String[] cmdParam_f = sendModel.getCmdParam();
								GunVehicleModel gunVehicleModel = AgvDesktopGlobal.getAgv_gunhead();
								List<String> agv_gunhead = AgvDesktopGlobal.getAgv_gunhead().getGunhead();
								//获取当前仪器位置通道号
								Map<String, String> instrumentUsedList_qt  = InstrumentStateGlobal.getInstrumentUsedListMap(Command.AGV.toString(), recModel.getDeviceID());
								String gdid_qt= instrumentUsedList_qt.get(Command.CUBICS.toString());
								CubsMaterialModel cubsMaterialModel_qt = new CubsMaterialModel();
								cubsMaterialModel_qt.setPassId(gdid_qt); //通道号
								cubsMaterialModel_qt = InstrumentMaterialGlobal.getCubsMaterial(cubsMaterialModel_qt);
								//存入枪头
								if(cubsMaterialModel_qt!=null){
									cubsMaterialModel_qt.setGunheadRight(cmdParam_f[1]);
								}
								if(agv_gunhead!=null && agv_gunhead.size()>0) {
									agv_gunhead.remove(0);
									gunVehicleModel.setGunhead(agv_gunhead);
									AgvDesktopGlobal.setAgv_gunhead(gunVehicleModel,true);
								}
								break;
							//取枪头,左
							case "takeQt_left_agv":
								String[] cmdParam_left = sendModel.getCmdParam();
								GunVehicleModel gunVehicleModel_left = AgvDesktopGlobal.getAgv_gunhead();
								List<String> agv_gunhead_left = gunVehicleModel_left.getGunhead();
								//获取当前仪器位置通道号
								Map<String, String> instrumentUsedList_qt_left  = InstrumentStateGlobal.getInstrumentUsedListMap(Command.AGV.toString(), recModel.getDeviceID());
								String gdid_qt_left= instrumentUsedList_qt_left.get(Command.CUBICS.toString());
								CubsMaterialModel cubsMaterialModel_qt_left = new CubsMaterialModel();
								cubsMaterialModel_qt_left.setPassId(gdid_qt_left); //通道号
								cubsMaterialModel_qt_left = InstrumentMaterialGlobal.getCubsMaterial(cubsMaterialModel_qt_left);
								//存入枪头
								if(cubsMaterialModel_qt_left!=null){
									cubsMaterialModel_qt_left.setGunheadLeft(cmdParam_left[1]);
								}
								if(agv_gunhead_left!=null && agv_gunhead_left.size()>0) {
									agv_gunhead_left.remove(0);
									gunVehicleModel_left.setGunhead(agv_gunhead_left);
									AgvDesktopGlobal.setAgv_gunhead(gunVehicleModel_left,true);
								}
								break;

						}
					}

					//移动回调
					if ("moveMethod".equals(mlpzDto.getMethodname())){
						String[] cmdParam = sendModel.getCmdParam();
						if("goQlq_agv".equals(mlpzDto.getParamlx())){
							InstrumentMaterialGlobal.setAvgQy(cmdParam[0]);
						}
//						switch (mlpzDto.getParamlx()) {
//							//移动到前料区
//							case "goQlq_agv":
//								//前料区位置设置到全局变量
//								InstrumentMaterialGlobal.setAvgQy(cmdParam[0]);
//								break;
//						}
					}
				}
			}
		}catch (Exception e){
			log.error(String.format("AGV命令回调检查错误%s", e.getMessage()));
			log.error(String.format("出错流程类型id%s", sjlcDto.getLclx()));
			log.error(String.format("出错事件id%s", sjlcDto.getSjid()));
		}

		return true;
	}
	
	/**
	 * 根据返回值做后续处理
	 * @param recModel 命令
	 * @param agv_tray 原始样本Map
	 */
	private void modVariable(Map<String , YsybxxDto> agv_tray,String parm,FrameModel recModel) {
		//拿出样本信息
		YsybxxDto agvsyybpModel1 = agv_tray.get(parm);
		if(agvsyybpModel1!=null){
			//获取当前仪器位置通道号
			Map<String, String> instrumentUsedList_t  = InstrumentStateGlobal.getInstrumentUsedListMap(Command.AGV.toString(), recModel.getDeviceID());
			String gdid_t= instrumentUsedList_t.get(Command.CUBICS.toString());
			CubsMaterialModel cubsMaterialModel1 = new CubsMaterialModel();
			cubsMaterialModel1.setPassId(gdid_t); //通道号
			//查询建库仪
			CubsMaterialModel cubsMaterialModel = InstrumentMaterialGlobal.getCubsMaterial(cubsMaterialModel1);
			if(cubsMaterialModel!=null){
				cubsMaterialModel.setSample(agvsyybpModel1.getNbbh()); //样本编号
				cubsMaterialModel.setEpLeft(YesNotEnum.YES.getValue());
				cubsMaterialModel.setEpRight(YesNotEnum.YES.getValue());
				cubsMaterialModel.setGunheadLeft(YesNotEnum.YES.getValue());
				cubsMaterialModel.setGunheadRight(YesNotEnum.YES.getValue());
				cubsMaterialModel.setYsybid(agvsyybpModel1.getYsybid()); //原始样本id
			}
			InstrumentMaterialGlobal.setCubsMaterial(cubsMaterialModel, true); //放入建库仪
			//修改数据库数据，存入通道号和建库仪位置
			//根据管道id获取建库仪位置
			IJkywzszService jkywzszService = (IJkywzszService) SpringUtil.getBean("jkywzszServiceImpl");
			JkywzszDto jkywzszDto =  jkywzszService.queryById(gdid_t);
			YsybxxDto ysybxx = new YsybxxDto();
			//获取位置
			if(StringUtil.isNotBlank(jkywzszDto.getJkywzbh())) {
				ysybxx.setJkytdwz(jkywzszDto.getJkywzbh());
			}
			if(StringUtil.isNotBlank(jkywzszDto.getJkytdh())) {
				ysybxx.setJkytdh(jkywzszDto.getJkytdh());
			}
			ysybxx.setYsybid(agvsyybpModel1.getYsybid());

			IYsybxxService ysybxxService = (IYsybxxService) SpringUtil.getBean("ysybxxServiceImpl");
			//修改
			ysybxxService.updateDto(ysybxx);
			//移除托盘内样本
			agv_tray.remove(parm);
		}
	}
	
	

}