package com.matridx.las.netty.dosjback;

import com.matridx.las.netty.dao.entities.AgvEpModel;
import com.matridx.las.netty.dao.entities.EpVehicleModel;
import com.matridx.las.netty.global.CubsParmGlobal;
import com.matridx.las.netty.global.material.FrontMaterialAreaGlobal;
import com.matridx.las.netty.global.material.InstrumentMaterialGlobal;
import com.matridx.las.netty.util.ChangeEpUtil;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.dao.entities.SjlcDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Service
public class CubicsCheckbackImpl implements EventCheckskipback {

	@Override//检查事件做不做
	public boolean checkSkip(SjlcDto sjlcDto, FrameModel frameModel, Map<String,String> map) {
		boolean flag = true;
		try {
			log.info(String.format("建库仪事件检查，检查事件为%s",sjlcDto.getSjid()));
			String xhpdlx = sjlcDto.getChecklx();

			List<AgvEpModel> listMap;
			if(StringUtils.isNoneBlank(xhpdlx)) {
				switch (xhpdlx) {
					//此处需要判断AGV身上的ep载具是否是需要的载具编号从redis中取
//				case "sfChangeEp":
//					List<String> list=null;
//					String param = null;
//					if(StringUtils.isNotBlank(sjlcDto.getParam())){
//						param=sjlcDto.getParam();
//						list= Arrays.asList(param.split(","));
//					}
//					if(ChangeEpUtil.changeEpCommon_drop(list)!=null||ChangeEpUtil.changeEpCommon_take(list)!=null){
//						flag=true;
//					}else{
//						flag=false;
//					}
//					break;
					case "sfChangeEp_drop":
						List<String> list1=null;
						String param1;
						if(StringUtils.isNotBlank(sjlcDto.getParam())){
							param1=sjlcDto.getParam();
							list1= Arrays.asList(param1.split(","));
						}
						String agvParam1;
						List<String> agvList1=new ArrayList<>();
						if(StringUtil.isNotBlank(sjlcDto.getAgvparam())){
							agvParam1=sjlcDto.getAgvparam();
							agvList1=Arrays.asList(agvParam1.split(","));
						}
//						if(ChangeEpUtil.changeEpCommon_drop(list1,agvList1)!=null){
//							flag=true;
//						}else{
//							flag=false;
//						}
						flag= ChangeEpUtil.changeEpCommon_drop(list1, agvList1) != null;
						break;
					case "sfChangeEp_take":
						List<String> list3=null;
						String param3;
						if(StringUtils.isNotBlank(sjlcDto.getParam())){
							param3=sjlcDto.getParam();
							list3= Arrays.asList(param3.split(","));
						}
						String agvParam3;
						List<String> agvList3=new ArrayList<>();
						if(StringUtil.isNotBlank(sjlcDto.getAgvparam())){
							agvParam3=sjlcDto.getAgvparam();
							agvList3=Arrays.asList(agvParam3.split(","));
						}
//						if(ChangeEpUtil.changeEpCommon_take(list3,agvList3)!=null){
//							flag=true;
//						}else{
//							flag=false;
//						}
						flag= ChangeEpUtil.changeEpCommon_take(list3, agvList3) != null;
						break;
					case "sfChangeEp_dropClaw":
						//是否还夹爪回调
//						if(StringUtil.isNotBlank(InstrumentMaterialGlobal.getEpClaw())){
//							flag=true;
//						}else{
//							flag=false;
//						}
						flag= StringUtil.isNotBlank(InstrumentMaterialGlobal.getEpClaw());
						break;
					case "checkEP":
//						if(FrontMaterialAreaGlobal.getFma_ep3().getEpList()!=null&&FrontMaterialAreaGlobal.getFma_ep3().getEpList().size()==1){
//							flag=true;
//						}else {
//							flag=false;
//						}
						flag= FrontMaterialAreaGlobal.getFma_ep3().getEpList() != null && FrontMaterialAreaGlobal.getFma_ep3().getEpList().size() == 1;
						break;
					case "checkEP1":
//						if(FrontMaterialAreaGlobal.getFma_ep3().getEpList()!=null&&FrontMaterialAreaGlobal.getFma_ep3().getEpList().size()>0&&CubsParmGlobal.getIsStartAuto()){
//							flag=true;
//						}else {
//							flag=false;
//						}
						flag= FrontMaterialAreaGlobal.getFma_ep3().getEpList() != null && FrontMaterialAreaGlobal.getFma_ep3().getEpList().size() > 0 && CubsParmGlobal.getIsStartAuto();
						break;
					//此处判断ep管是否满40个从redis中取
					case "checkEPNums":

						EpVehicleModel epVehicleModel=ChangeEpUtil.getMap("ep3");
						//todo
						//if((listMap!=null&&listMap.size()>=Integer.parseInt(GlobalParmEnum.AUTO_START_NUM.getCode()))){
						if(epVehicleModel!=null){
							listMap= epVehicleModel.getEpList();
							flag= (listMap != null && listMap.size() == 1);
						}else {
							flag=false;
						}
//						if((listMap!=null&&listMap.size()==1)){
//							//if((listMap!=null&&listMap.size()>=Integer.parseInt(GlobalParmEnum.AUTO_START_NUM.getCode()))){
//							flag=true;
//						}else {
//							flag=false;
//						}

						break;
					case "checkhsEPNums":
						EpVehicleModel epVehicleModel1=ChangeEpUtil.getMap("ep2");
						if(epVehicleModel1!=null){
							listMap= epVehicleModel1.getEpList();
							flag= (listMap != null && listMap.size() == 1);
						}else{
							flag=false;
						}
//						if((listMap!=null&&listMap.size()==1)){
//							flag=true;
//						}else {
//							flag=false;
//						}
						break;
					case "checkhsEPNums1":
						EpVehicleModel epVehicleModel2=ChangeEpUtil.getMap("ep2");

//						if((listMap!=null&&listMap.size()>0)&&CubsParmGlobal.getIsStartAuto()){
//							flag=true;
//						}else {
//							flag=false;
//						}
						if(epVehicleModel2!=null){
							listMap= epVehicleModel2.getEpList();
							flag= (listMap != null && listMap.size() > 0) && CubsParmGlobal.getIsStartAuto();
						}else{
							flag=false;
						}
						break;
					case "checkEPNums1":
						EpVehicleModel epVehicleModel3=ChangeEpUtil.getMap("ep3");

//						if(listMap!=null&&listMap.size()>0&&CubsParmGlobal.getIsStartAuto()){
//							flag=true;
//						}else {
//							flag=false;
//						}
						if(epVehicleModel3!=null){
							listMap= epVehicleModel3.getEpList();
							flag= listMap != null && listMap.size() > 0 && CubsParmGlobal.getIsStartAuto();
						}else {
							flag=false;
						}

						break;
					case "checkAutoEp":
//						if(CubsParmGlobal.getIsIsAutoWork()){
//							flag=true;
//						}else {
//							flag=false;
//						}
						flag= CubsParmGlobal.getIsIsAutoWork();
						break;
					case "checkAutoEp1":
//						if(CubsParmGlobal.getIsIsAutoWork1()&&CubsParmGlobal.getIsStartAuto()){
//							flag=true;
//						}else {
//							flag=false;
//						}
						flag= CubsParmGlobal.getIsIsAutoWork1() && CubsParmGlobal.getIsStartAuto();
						break;


				}
			}

		}catch (Exception e){
			log.error(String.format("Cubis事件检查错误%s", e.getMessage()));
			log.error(String.format("出错流程类型id%s", sjlcDto.getLclx()));
			log.error(String.format("出错事件id%s", sjlcDto.getSjid()));
		}


		return flag;
	}
}
