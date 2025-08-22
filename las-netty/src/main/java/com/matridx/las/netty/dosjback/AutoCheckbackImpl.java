package com.matridx.las.netty.dosjback;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.redis.RedisStreamUtil;
import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.channel.domain.Command;
import com.matridx.las.netty.dao.entities.*;
import com.matridx.las.netty.dosjbackimpl.PcrSjBackImpl;
import com.matridx.las.netty.global.CubsParmGlobal;
import com.matridx.las.netty.global.InstrumentStateGlobal;
import com.matridx.las.netty.global.material.AgvDesktopGlobal;
import com.matridx.las.netty.global.material.FrontMaterialAreaGlobal;
import com.matridx.las.netty.service.impl.YsybxxServiceImpl;


import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class AutoCheckbackImpl implements EventCheckskipback {

	@Autowired
	private  RedisStreamUtil streamUtil;

	@Override//检查事件做不做
	public boolean checkSkip(SjlcDto sjlcDto, FrameModel frameModel, Map<String,String> map) {
		boolean flag = false;
		try {
			log.info(String.format("auto事件检查，检查事件为%s",sjlcDto.getSjid()));
			String xhpdlx = sjlcDto.getChecklx();
			if(StringUtils.isNoneBlank(xhpdlx)) {
				switch (xhpdlx) {
					//此处判断配置仪是否开门成功
					case "openAuto":
						if("OK".equals(frameModel.getCmd())){
							flag=true;
						}
						break;
					//判断机器人是否有八连管载具
					case "eightZj":
						OctalianpipeListModel octalianpipeListModel = AgvDesktopGlobal.getAgv_octalianpipe();
						if(null == octalianpipeListModel.getMap()){
							flag=true;
						}
						break;
					//判断机器人是否有八连管载具
					case "eightBc":
						FrameModel recModel = InstrumentStateGlobal.getInstrumentFinMap(map.get(Command.AUTO.toString()));
						JSONObject json = JSONObject.parseObject(recModel.getCmdParam()[0]);
						if (json.get("Action").toString().contains("Finish")) {
							PcrSjBackImpl pcrSjBack = new PcrSjBackImpl();
							JSONObject backJson = pcrSjBack.setBackData(json.get("backdata").toString()); // 先保存状态和动作流程
							// 配液仪器第一次完成配置,调用
							if (backJson.get("djcsy").toString().equals("1")) {
								flag=true;
							}
						}
						break;
					//判断当前夹爪，取夹爪
					case "clamping8_Pick":
						String clampingNum8_pick = AgvDesktopGlobal.getClampingNum();
						if(!"8".equals(clampingNum8_pick)) {
							flag=true;
						}
						break;
					//判断当前夹爪EP官载具，还夹爪
					case "clamping4_Pick":
						String clampingNum4_pick = AgvDesktopGlobal.getClampingNum();
						if("4".equals(clampingNum4_pick)) {
							flag=true;
						}
						break;
					//判断是否放置托盘
					case "place_tray1":
						//获取机器人平台的托盘
						Map<String , YsybxxDto> agv_tray1 = AgvDesktopGlobal.getAgv_tray1();
						//判断机器人平台是否存在空位
						if(agv_tray1==null) {
							//获取前置区的托盘
							List<TrayModel> fma_tray = FrontMaterialAreaGlobal.getFma_tray();
							//判断是否为null
							if(fma_tray!=null && fma_tray.size()>0) {
								for (TrayModel trayModel : fma_tray) {
									//判断是否存在样本状态为待放置状态的
									if("1".equals(trayModel.getZt())) { //带处理的托盘
										flag = true;
										break;
									}
								}
							}
						}
						break;
					//判断是否放置托盘2
					case "place_tray2":
						//获取机器人平台的托盘
						Map<String , YsybxxDto> agv_tray2 = AgvDesktopGlobal.getAgv_tray2();
						//判断机器人平台是否存在空位
						if(agv_tray2==null) {
							//获取前置区的托盘
							List<TrayModel> fma_tray = FrontMaterialAreaGlobal.getFma_tray();
							//判断是否为null
							if(fma_tray!=null && fma_tray.size()>0) {
								for (TrayModel trayModel : fma_tray) {
									//判断是否存在样本状态为待放置状态的
									if("1".equals(trayModel.getZt())) { //带处理的托盘
										flag = true;
										break;
									}
								}
							}
						}
						break;
					//判断是否放置枪头载具
					case "gunHead_vehicle":
						GunVehicleModel gunVehicleModel = AgvDesktopGlobal.getAgv_gunhead();
						if(gunVehicleModel.getGunhead()==null||gunVehicleModel.getGunhead().size()==0) {
							flag = true;
						}
						break;
					//判断机器人平台是否存在空的EP管载具
					case "ep_vehicle":
						EpVehicleModel epVehicleModel1 = AgvDesktopGlobal.getAgv_ep1();
						List<AgvEpModel> ep_vehicle1 = epVehicleModel1.getEpList();
						EpVehicleModel epVehicleModel2 = AgvDesktopGlobal.getAgv_ep2();
						List<AgvEpModel> ep_vehicle2 = epVehicleModel2.getEpList();
						if(ep_vehicle1==null || ep_vehicle1.size()==0 || ep_vehicle2==null || ep_vehicle2.size()==0) {
							flag = true;
						}
						break;
					//判断机器人平台是否存在空的EP管载具
					case "ep3_vehicle":
						List<AgvEpModel> agv_ep2 = AgvDesktopGlobal.getAgv_ep2().getEpList();
						if(agv_ep2==null || agv_ep2.size()==0) {
							flag = true;
						}
						break;
					//判断机器人平台是否存在核酸ep管载具和文库ep管载具
					case "ep2_vehicle":
						List<AgvEpModel> agv_ep1 = AgvDesktopGlobal.getAgv_ep1().getEpList();
						if(agv_ep1==null || agv_ep1.size()==0) {
							flag = true;
						}
						break;
//				//判断是否归还夹爪
//				case "clamping_return":
//					String clamping_return = AgvDesktopGlobal.getClampingNum();
//					if(StringUtil.isNotBlank(clamping_return)) {
//						flag = true;
//					}
//					break;
					//判断是否更换EP管载具
					case "ep_exchange":
						List<AgvEpModel> ep_exchange = AgvDesktopGlobal.getAgv_ep1().getEpList();
						//不存在EP管，去前料区拿EP管
						if(ep_exchange==null) {
							flag = true;
						}
						break;
					//判断是否归还EP管载具
					case "returnEpZj":
						//判断建库仪是否存在空闲，如果存在，判断AGV是否存在待放置样本，存在待放置样本，不执行，不存在待处理样本，执行；不存在空闲建库仪执行。
						Long len = CubsParmGlobal.getCubquenLenth();
						if(len!=null && len>0) {
							Map<String, YsybxxDto> tray1 = AgvDesktopGlobal.getAgv_tray1();
							Map<String, YsybxxDto> tray2 = AgvDesktopGlobal.getAgv_tray2();
							if(tray1==null && tray2==null) {
								flag = true;
							}
						}else {
							flag = true;
						}
						break;

					//判断是否需要归还托盘夹爪
					case "clamping8_return":
						//获取空闲仪器
						List<MapRecord<String, Object, Object>> clamping8queue= streamUtil.range("CubisGroup");
						//判断是否存在空闲仪器
						if(clamping8queue!=null && clamping8queue.size()>0) {
							flag=true;
						}
						//判断物料区是否存在待处理托盘
						List<TrayModel> fma_tray = FrontMaterialAreaGlobal.getFma_tray();
						if(fma_tray!=null && fma_tray.size()>0) {
							//获取待入库样本
							Map<String ,YsybxxDto> tray1 = AgvDesktopGlobal.getAgv_tray1();
							Map<String ,YsybxxDto> tray2 = AgvDesktopGlobal.getAgv_tray2();
							//判断是否存在待入库样本
//							if((tray1!=null && tray1.size()>0) || (tray2!=null && tray2.size()>0)){
//								flag=true;
//							}else {
//								flag=false;
//							}
							flag= (tray1 != null && tray1.size() > 0) || (tray2 != null && tray2.size() > 0);
						}else {
							flag=false;
						}

						break;
					//判断是否执行移动至前料区，如果机器人平台上存在空的托盘，或没有空闲建库仪，或前料区没有待处理样本
					case "isMvAndPic":
						//判断机器人平台是否存在空的托盘
						Map<String , YsybxxDto> tray1 = AgvDesktopGlobal.getAgv_tray1();
						Map<String , YsybxxDto> tray2 = AgvDesktopGlobal.getAgv_tray2();
						if((tray1!=null && tray1.size()==0) || (tray2!=null && tray2.size()==0)) {
							//加锁
							AgvDesktopGlobal.setEditNow("true");
							//存在空托盘，归还托盘，移动拍照
							flag=true;
						}else {
							//判断是否存在空闲仪器
							Long len_t = CubsParmGlobal.getCubquenLenth();
							//if((len_t!=null && len_t==0) || len_t==null) {
							if(len_t==null||len_t==0) {
								//不存在，归还Ep管，移动，拍照
								flag=true;
							}
						}
						break;
					//判断是否拿托盘夹爪
					case "isTakeTrayClaw":
						//判断机器人平台是否存在空的托盘
						Map<String , YsybxxDto> tr1 = AgvDesktopGlobal.getAgv_tray1();
						Map<String , YsybxxDto> tr2 = AgvDesktopGlobal.getAgv_tray2();
						List<TrayModel> fma_t = FrontMaterialAreaGlobal.getFma_tray();
						if((tr1!=null && tr1.size()==0) || (tr2!=null && tr2.size()==0)) {
							//存在空托盘，归还托盘
							flag=true;
						}
						//判断机器人平台是否存在空闲位置，判断前料区是否有待放置托盘
						if(tr1==null || tr2==null) {
							if(fma_t!=null && fma_t.size()>0) {
								flag=true;
							}
						}

						break;
					//判断机器人是否归还托盘
					case "isReturnTray":
						//判断机器人平台是否存在空的托盘
						Map<String , YsybxxDto> tr1_t = AgvDesktopGlobal.getAgv_tray1();
						Map<String , YsybxxDto> tr2_t = AgvDesktopGlobal.getAgv_tray2();
						if((tr1_t!=null && tr1_t.size()==0) || (tr2_t!=null && tr2_t.size()==0)) {
							//存在空托盘，归还托盘
							flag=true;
						}
						break;
					//判断AGV平台是否存在空闲位置，是否存在待放置托盘
					case "place_tr":
						flag = YsybxxServiceImpl.judgeAndEditTray("1");
						break;
					//判断当前夹爪编号，如果是8，托盘夹爪，就归还
					case "jawNum":
						String jawnum = AgvDesktopGlobal.getClampingNum();
						if("8".equals(jawnum)) {
							flag=true;
						}
						break;
					//判断机器人平台是否存在EP管载具2，3
					case "isHaveEp":
						List<AgvEpModel> ep2 = AgvDesktopGlobal.getAgv_ep2().getEpList();
						List<AgvEpModel> ep3 = AgvDesktopGlobal.getAgv_ep3().getEpList();
						if((ep2!=null && ep2.size()>0) || (ep3!=null && ep3.size()>0)) {
							flag=true;
						}
						break;
					//判断是否拿EP管夹爪
					case "isTakeClaw":
						//判断AGV是否存在EP管载具
						List<AgvEpModel> epList1 = AgvDesktopGlobal.getAgv_ep1().getEpList();
						List<AgvEpModel> epList2 = AgvDesktopGlobal.getAgv_ep2().getEpList();
						List<AgvEpModel> epList3 = AgvDesktopGlobal.getAgv_ep3().getEpList();
						if(epList1==null) {
							flag=true;
						}
						if((epList2!=null && epList2.size()>0) || (epList3!=null && epList3.size()>0)) {
							flag=true;
						}
						break;
				}
			}

		}catch (Exception e){
			log.error(String.format("Auto事件检查错误%s", e.getMessage()));
			log.error(String.format("出错流程类型id%s", sjlcDto.getLclx()));
			log.error(String.format("出错事件id%s", sjlcDto.getSjid()));
		}
		return flag;
	}
}
