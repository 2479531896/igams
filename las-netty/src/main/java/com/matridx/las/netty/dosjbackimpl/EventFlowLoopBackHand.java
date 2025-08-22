package com.matridx.las.netty.dosjbackimpl;

import com.matridx.las.netty.dao.entities.*;
import com.matridx.las.netty.enums.LoopCheckEventFlowEnum;
import com.matridx.las.netty.global.material.AgvDesktopGlobal;
import com.matridx.las.netty.global.material.FrontMaterialAreaGlobal;
import com.matridx.las.netty.global.CubsParmGlobal;
import com.matridx.las.netty.global.material.InstrumentMaterialGlobal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.matridx.las.netty.util.ChangeEpUtil;
import com.matridx.las.netty.util.GetWLNumUtil;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class EventFlowLoopBackHand {


    Logger log = LoggerFactory.getLogger(EventFlowLoopBackHand.class);

    @Autowired
    private GetWLNumUtil getWLNumUtil;

    public boolean checkLoop(SjlcDto sjlcDto, Map<String,String> map){
        boolean result = false;
        try {
            //判断循环类型根据类型查询是否需要循环 //判断时候需要循环
            String deviceId =map.get(sjlcDto.getZyyqlx());
            if(sjlcDto.getXhpdlx().equals(LoopCheckEventFlowEnum.LOOP_CHECK_SJLC_QTP.getCode())){
                //获取机器人平台的托盘
                Map<String , YsybxxDto> agv_tray1 = AgvDesktopGlobal.getAgv_tray1();
                Map<String , YsybxxDto> agv_tray2 = AgvDesktopGlobal.getAgv_tray2();
                //判断机器人平台是否存在空位
                if(agv_tray1==null || agv_tray1.size()==0 || agv_tray2==null || agv_tray2.size()==0) {
                    //获取前置区的托盘
                    List<TrayModel> fma_tray = FrontMaterialAreaGlobal.getFma_tray();
                    //判断是否为null
                    if(fma_tray!=null && fma_tray.size()>0) {
                        for (TrayModel trayModel : fma_tray) {
                            //判断是否存在样本状态为待放置状态的
                            if("1".equals(trayModel.getZt())) { //带处理的托盘
                                result = true;
                                break;
                            }
                        }
                    }
                }
            }else if(sjlcDto.getXhpdlx().equals(LoopCheckEventFlowEnum.LOOP_CHECK_DROPEP.getCode())){
                List<String> list=null;
                String param ;
                if(StringUtils.isNotBlank(sjlcDto.getParam())){
                    param=sjlcDto.getParam();
                    list= Arrays.asList(param.split(","));
                }
                String agvParam;
                List<String> agvList=new ArrayList<>();
                if(StringUtil.isNotBlank(sjlcDto.getAgvparam())){
                    agvParam=sjlcDto.getAgvparam();
                    agvList=Arrays.asList(agvParam.split(","));
                }
                if(ChangeEpUtil.changeEpCommon_drop(list,agvList)!=null){
                    result = true;
                }

            }else if(sjlcDto.getXhpdlx().equals(LoopCheckEventFlowEnum.LOOP_CHECK_TAKEEP.getCode())){
                List<String> list=null;
                String param=null;
                if(StringUtils.isNotBlank(sjlcDto.getParam())){
                    param=sjlcDto.getParam();
                }
                String agvParam1;
                List<String> agvList1=new ArrayList<>();
                if(StringUtil.isNotBlank(sjlcDto.getAgvparam())){
                    agvParam1=sjlcDto.getAgvparam();
                    agvList1=Arrays.asList(agvParam1.split(","));
                }
                if(param!=null){
                    list= Arrays.asList(param.split(","));
                }

                if(ChangeEpUtil.changeEpCommon_take(list,agvList1)!=null){
                    result = true;
                }

            }else if(sjlcDto.getXhpdlx().equals(LoopCheckEventFlowEnum.LOOP_CHECK_SJLC_FZYB.getCode())) {
                //判断是否存在空闲仪器
                Long len = CubsParmGlobal.getCubquenLenth();
                if(len!=null && len>0) {
                    //判断机器人平台上是否存在待放置的样本
                    Map<String , YsybxxDto> agv_tray1 = AgvDesktopGlobal.getAgv_tray1();
                    Map<String , YsybxxDto> agv_tray2 = AgvDesktopGlobal.getAgv_tray2();
                    if((agv_tray1!=null && agv_tray1.size()>0) || (agv_tray2!=null && agv_tray2.size()>0)) {
                        result = true;
                    }
                }
            }else if(sjlcDto.getXhpdlx().equals(LoopCheckEventFlowEnum.LOOP_CHECK_SJLC_GHTP.getCode())) {
                //判断机器人平台是否存在空的托盘
                Map<String , YsybxxDto> agv_tray1 = AgvDesktopGlobal.getAgv_tray1();
                Map<String , YsybxxDto> agv_tray2 = AgvDesktopGlobal.getAgv_tray2();
                if((agv_tray1!=null && agv_tray1.size()==0) || (agv_tray2!=null && agv_tray2.size()==0)) {
                    result = true;
                }
            }else if(sjlcDto.getXhpdlx().equals(LoopCheckEventFlowEnum.LOOP_CHECK_SJLC_RETURNEPZJ.getCode())) {
                //归还EP管是否循环
                EpVehicleModel agv_ep2 = AgvDesktopGlobal.getAgv_ep2();
                EpVehicleModel agv_ep3 = AgvDesktopGlobal.getAgv_ep3();
                if(agv_ep2.getEpList()!=null) {
                    result = true;
                }
                if(agv_ep3.getEpList()!=null) {
                    result = true;
                }

            }else if(sjlcDto.getXhpdlx().equals(LoopCheckEventFlowEnum.LOOP_CHECK_SJLC_QCYB.getCode())){
                Lock lock =new ReentrantLock();
                try{
                    lock.lock();
                    if(CubsParmGlobal.getCubFinQueuesSize()>0){
                        return true;
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    lock.unlock();// 必须在finally中释放
                }
            }else if(sjlcDto.getXhpdlx().equals(LoopCheckEventFlowEnum.LOOP_CHECK_TAKE_EIGHT.getCode())){
                //此处需要判断pcr中是否还有
                //此处需要判断pcr中是否还有
                PcrMaterialModel pcrMaterialModel=new PcrMaterialModel();
                pcrMaterialModel.setDeviceid(deviceId);
                if(StringUtil.isNotBlank(deviceId)){
                    PcrMaterialModel model=InstrumentMaterialGlobal.getPcrMaterial(pcrMaterialModel);
                    if(model!=null&&model.getOctalianpipeList().getOctNum()>0){
                        return true;
                    }
                }

            }else if(sjlcDto.getXhpdlx().equals(LoopCheckEventFlowEnum.LOOP_CHECK_DROP_EIGHT.getCode())){
                //此处需要判断pcr中是否还有
            }else if(sjlcDto.getXhpdlx().equals(LoopCheckEventFlowEnum.LOOP_CHECK_TAKE_EIGHT_AUTO.getCode())){
                //需要判断配置仪是否还有八连管
                AutoMaterialModel autoMaterialModel = new AutoMaterialModel();
                autoMaterialModel.setDeviceid(deviceId);
                if(StringUtil.isNotBlank(deviceId)){
                    AutoMaterialModel autoMaterialModel1 = InstrumentMaterialGlobal.getAutoMaterial(autoMaterialModel);
                    if(autoMaterialModel1!=null){
                        OctalianpipeListModel octalianpipeListModel = autoMaterialModel1.getOctalianpipeList();
                        if (octalianpipeListModel.getOctNum() > 0){
                            return  true;
                        }
                    }
                }

            }else if(sjlcDto.getXhpdlx().equals(LoopCheckEventFlowEnum.LOOP_CHECK_TAKE_EIGHT_TO_CMH.getCode())){
                //需要判断压盖机上还有没有空位
                ChmMaterialModel chmMaterialModel = new ChmMaterialModel();
                chmMaterialModel.setDeviceid(deviceId);
                if(StringUtil.isNotBlank(deviceId)){
                    ChmMaterialModel chmMaterialModel1 = InstrumentMaterialGlobal.getChmsMaterial(chmMaterialModel);
                    if(chmMaterialModel1!=null){
                        OctalianpipeListModel octalianpipeListModel = AgvDesktopGlobal.getAgv_octalianpipe();
//            if (null != chmMaterialModel1.getMap() && chmMaterialModel1.getMap().size() < 2 && null != octalianpipeListModel && octalianpipeListModel.getOctNum() > 0){
                        if (chmMaterialModel1.getOctNum() < 2 && null != octalianpipeListModel && octalianpipeListModel.getOctNum() > 0){
                            return  true;
                        }
                    }
                }


            }else if(sjlcDto.getXhpdlx().equals(LoopCheckEventFlowEnum.LOOP_CHECK_TAKE_LID_TO_EIGHT.getCode())){
                //将八连管盖放到八连管上
                //需要判断压盖机上还有没有空位
                ChmMaterialModel chmMaterialModel = new ChmMaterialModel();
                chmMaterialModel.setDeviceid(deviceId);
                if(StringUtil.isNotBlank(deviceId)){
                    ChmMaterialModel chmMaterialModel1 = InstrumentMaterialGlobal.getChmsMaterial(chmMaterialModel);
                    if (null != chmMaterialModel1 && chmMaterialModel1.getOctgNum() <2 && chmMaterialModel1.getOctgNum() < chmMaterialModel1.getOctNum()){
                        return  true;
                    }
                }

            }else if(sjlcDto.getXhpdlx().equals(LoopCheckEventFlowEnum.LOOP_CHECK_TAKE_EIGHT_TO_AVG.getCode())){
                //将八连管放到AGV上
                ChmMaterialModel chmMaterialModel = new ChmMaterialModel();
                chmMaterialModel.setDeviceid(deviceId);
                if(StringUtil.isNotBlank(deviceId)){
                    ChmMaterialModel chmMaterialModel1 = InstrumentMaterialGlobal.getChmsMaterial(chmMaterialModel);
                    if (null != chmMaterialModel1 && chmMaterialModel1.getOctgNum() > 0){
                        return  true;
                    }
                }


            }else if(sjlcDto.getXhpdlx().equals(LoopCheckEventFlowEnum.LOOP_CHECK_TAKE_EIGHT_TO_PCR.getCode())){
                //将八连管放入PCR
                OctalianpipeListModel octalianpipeListModel = AgvDesktopGlobal.getAgv_octalianpipe();
                if (null != octalianpipeListModel && octalianpipeListModel.getOctgNum() >0){
                    return  true;
                }
            }else if(sjlcDto.getXhpdlx().equals(LoopCheckEventFlowEnum.LOOP_CHECK_TAKE_EIGHT_FROM_WL.getCode())){
                //从建库仪后的物料区取八连管
                OctalianpipeListModel octalianpipeListModel = AgvDesktopGlobal.getAgv_octalianpipe();
                if (null != octalianpipeListModel && octalianpipeListModel.getOctbNum() > 0){
                    return  true;
                }
            }else if(sjlcDto.getXhpdlx().equals(LoopCheckEventFlowEnum.LOOP_CHECK_TAKE_EIGHT_TO_AUTO.getCode())){
                //夹取八连管到配置仪
                OctalianpipeListModel octalianpipeListModel = AgvDesktopGlobal.getAgv_octalianpipe();
                if (null != octalianpipeListModel && octalianpipeListModel.getOctbNum() >0 && octalianpipeListModel.getNum()>0){
                    return  true;
                }
            }else if(sjlcDto.getXhpdlx().equals(LoopCheckEventFlowEnum.LOOP_CHECK_SFKSAUTO.getCode())){
                Map<String,Object> startAutoMap=getWLNumUtil.getWlNum("startAuto","");
                return !Boolean.valueOf(startAutoMap.get("state").toString());
            }else if(sjlcDto.getXhpdlx().equals(LoopCheckEventFlowEnum.LOOP_CHECK_SFKSAUTO.getCode())){

                Map<String,Object> startAutoMap=getWLNumUtil.getWlNum("startCmh",deviceId);
                return !Boolean.valueOf(startAutoMap.get("state").toString());
            }
        }catch (Exception e){
            log.error(String.format("事件检查错误%s", e.getMessage()));
            log.error(String.format("出错流程类型id%s", sjlcDto.getLclx()));
            log.error(String.format("出错事件id%s", sjlcDto.getSjid()));
        }


        return  result;
    }


}