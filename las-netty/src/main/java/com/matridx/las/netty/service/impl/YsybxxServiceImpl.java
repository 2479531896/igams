package com.matridx.las.netty.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.las.netty.channel.command.BaseCommand;
import com.matridx.las.netty.channel.command.SendBaseCommand;
import com.matridx.las.netty.dao.entities.*;
import com.matridx.las.netty.enums.GlobalrouteEnum;
import com.matridx.las.netty.enums.MedicalRobotProcessEnum;
import com.matridx.las.netty.enums.RedisStorageEnum;
import com.matridx.las.netty.enums.VehicleStatusEnum;
import com.matridx.las.netty.global.CubsParmGlobal;
import com.matridx.las.netty.global.material.AgvDesktopGlobal;
import com.matridx.las.netty.global.material.FrontMaterialAreaGlobal;
import com.matridx.las.netty.service.svcinterface.IYblcsjService;
import com.matridx.las.netty.util.CommonChannelUtil;
import com.matridx.las.netty.util.RedisSetAndGetUtil;
import com.matridx.las.netty.util.SendMessgeToHtml;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.las.netty.dao.post.IYsybxxDao;
import com.matridx.las.netty.service.svcinterface.IYsybxxService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class YsybxxServiceImpl extends BaseBasicServiceImpl<YsybxxDto, YsybxxModel, IYsybxxDao> implements IYsybxxService {
    @Autowired
    private IYsybxxDao ysybxxDao;
    @Autowired
    private IJcsjService jcsjService;
    @Autowired
    private IYblcsjService yblcsjService;
    @Autowired
    private  RedisSetAndGetUtil setAndGetUtil;

    private final  Logger log = LoggerFactory.getLogger(BaseCommand.class);

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> saveSample(YsybxxDto ysybxxDto) {
        Map<String, Object> map = saveYsybxx(ysybxxDto);
        if ("true".equals(map.get("status"))) {
            Map<String, Object> map1 = getTraySample();
            map.put("tpinfo", map1.get("tpinfo"));
            map.put("zbfinfo", map1.get("zbfinfo"));
        }
        return map;


    }


    @Override
    public Map<String, Object> saveSampleList(List<YsybxxDto> list) {
        Map<String, Object> map=saveYsybxxList(list);
        return map;
    }


    /**
     * 执行流程开始
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> implementSample() {
        Map<String, Object> map = new HashMap<>();
        try {
            //修改物料区和判断是否需要执行
            boolean result = judgeAndEditTray("0");//todo和上面的方法合到一起
            //如果有待执行的样本，并且托盘不在机器人身上
            if ( result) {
                CommonChannelUtil.setSystemState(GlobalrouteEnum.SYSTEM_STATE_WROK.getCode());
                //如果需要继续执行，则调用执行拿托盘操作
                SendBaseCommand sendBaseCommand = new SendBaseCommand();
                boolean result_t = sendBaseCommand.sendEventFlowlist(MedicalRobotProcessEnum.MEDICAL_ROBOT_PLACE_CEHICLE.getCode(), null);
            }
            map.put("status", "true");
            map.put("message", "success");
        } catch (Exception e) {
            map.put("status", "false");
            map.put("message", e.getMessage());
        }
        return map;
    }


    /**
     * 从redis中获取托盘里的信息
     *
     * @return
     */
    @Override
    public Map<String, Object> getTraySample() {
        Map<String, Object> map = new HashMap<>();
        //查询redis的数据
        List<TrayModel> listne = FrontMaterialAreaGlobal.getFma_tray();
        if (listne == null || listne.size() < 1) {
            map.put("status", "false");
            map.put("message", "未找到托盘信息，请准本好托盘");
            return map;
        }
       List<TrayModel> list = SendMessgeToHtml.getSerTrayList(listne);
        JSONArray tpinfo = new JSONArray();
        for (TrayModel trayModel : list) {
            JSONArray ybs = new JSONArray();
            JSONObject tp = new JSONObject();
            String readOnlyTray = "false";
            List<String> agvsyyS = new ArrayList<>();
            if (trayModel.getBoxList() != null && trayModel.getBoxList().size() > 0) {
                for (YsybxxDto ag : trayModel.getBoxList()) {
                    if (ag != null && StringUtil.isNotBlank(ag.getTpnwzxh())) {
                        agvsyyS.add(ag.getTpnwzxh());

                    }
                }


            }
            if(trayModel.getBoxList()==null){
                trayModel.setBoxList(new ArrayList<>());
            }
            //先将样本放入
            for (int j = 1; j < 5; j++) {
                if (agvsyyS.contains(j + "")) {
                    continue;
                }
                YsybxxDto ysybxxDto = new YsybxxDto();
                ysybxxDto.setTpnwzxh(j+"");
                ysybxxDto.setTpbh(trayModel.getTpbh());
                trayModel.getBoxList().add(j-1,ysybxxDto);
            }
            tpinfo.add(trayModel);
        }
        //制备法列表获取
        List<JcsjDto> listJc = jcsjService.getDtoListbyJclb(BasicDataTypeEnum.PREPARATIVE_METHOD);
        JSONArray zbfInfo = new JSONArray();
        for (JcsjDto jcsjDto : listJc) {
            JSONObject zbf = new JSONObject();
            zbf.put("value", jcsjDto.getCsid());
            zbf.put("text", jcsjDto.getCsmc());
            zbfInfo.add(zbf);
        }
        map.put("tpinfo", tpinfo);
        map.put("zbfinfo", zbfInfo);
        map.put("status", "true");
        map.put("message", "success");
        log.info(map.toString());
        return map;
    }




    /**
     * 存样本到数据库
     *
     * @param ysybxxDto
     * @return
     */
    public Map<String, Object> saveYsybxx(YsybxxDto ysybxxDto) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isBlank(ysybxxDto.getNbbh())) {
            map.put("status", "false");
            map.put("message", "样本编号为空");
            log.error(map.toString());
            return map;
        }
        if (StringUtil.isBlank(ysybxxDto.getTpbh())) {
            map.put("status", "false");
            map.put("message", "托盘编号为空");
            log.error(map.toString());
            return map;
        }
        if (StringUtil.isBlank(ysybxxDto.getTpnwzxh())) {
            map.put("status", "false");
            map.put("message", "卡盒在托盘内的位置编号为空");
            log.error(map.toString());
            return map;
        }
        if (StringUtil.isBlank(ysybxxDto.getZbf())) {
            map.put("status", "false");
            map.put("message", "制备法为空");
            log.error(map.toString());
            return map;
        }
        if (StringUtil.isBlank(ysybxxDto.getJkysjph())) {
            map.put("status", "false");
            map.put("message", "建库试剂批号为空");
            log.error(map.toString());
            return map;
        }
        if (StringUtil.isBlank(ysybxxDto.getTcsjph())) {
            map.put("status", "false");
            map.put("message", "提纯试剂批号为空");
            log.error(map.toString());
            return map;
        }
        if (StringUtil.isBlank(ysybxxDto.getJth())) {
            map.put("status", "false");
            map.put("message", "接头号为空");
            log.error(map.toString());
            return map;
        }
        boolean res = saveYsybxxDto(ysybxxDto);
        if (ysybxxDto == null) {
            map.put("message", "保存失败");
            map.put("status", "false");
            log.error(map.toString());
            return map;
        }
        //model替换成ysyb
        ysybxxDto.setYbzt(VehicleStatusEnum.VEHICLE_CARDBOX_SAVE.getCode());
        String num = ysybxxDto.getTpbh();
        int numKh = Integer.parseInt(ysybxxDto.getTpnwzxh());
        TrayModel trayModel1 = null;
        List<TrayModel> list = FrontMaterialAreaGlobal.getFma_tray();
        if (list == null || list.size() < 1) {
            list = new ArrayList<>();
            trayModel1 = new TrayModel();
            trayModel1.setTpbh(num);
            List<YsybxxDto> loi = new ArrayList<>();
            trayModel1.setBoxList(loi);

            list.add(trayModel1);
        }
        for (TrayModel as : list) {
            if (as == null) continue;
            if (num.equals(as.getTpbh())) {
                trayModel1 = as;
                List<YsybxxDto> boxList = as.getBoxList();
                if(boxList!=null&&boxList.size()>0) {
                    for (int i = 0; i < boxList.size(); i++){
                        YsybxxDto agvs = boxList.get(i);
                        if (agvs == null) continue;
                        int agvNum = Integer.parseInt(agvs.getTpnwzxh());
                        if ( agvNum == numKh) {
                            boxList.set(i, ysybxxDto);
                            break;
                        } else if (numKh < agvNum) {
                            //小于卡和编号，则应为新增
                            boxList.add(i, ysybxxDto);
                            break;
                        }
                        if(i+1==boxList.size()){
                            boxList.add(ysybxxDto);
                            break;
                        }
                    }
                }else {
                    if(boxList!=null) {
                        boxList.add(ysybxxDto);
                    }
                }
                as.setZt(VehicleStatusEnum.VEHICLE_TRAY_FREE.getCode());
            }
        }
        //添加入redis 整个dto传过去
        FrontMaterialAreaGlobal.setFma_tray(list, true);
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(trayModel1);
        map.put("tpinfo", jsonArray);
        map.put("message", "保存成功");
        map.put("status", "true");
        log.info(map.toString());
        return map;
    }

    /**
     * 保存样本信息到数据库
     * @param list
     * @return
     */
    @Transactional
    public Map<String,Object> saveYsybxxList(List<YsybxxDto>list){
        //添加list
        List<YsybxxDto> addList=new ArrayList<>();
        //修改list
        List<YsybxxDto> updateList= new ArrayList<>();
        //样本流程时间list
        List<YblcsjDto> yblcsjList=new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        List<String> nbbhlist = new ArrayList<>();//记录相同的样本编号
        if (CubsParmGlobal.getIsStartAuto()) {
            map.put("status", "false");
            map.put("message", "已经开始配液操作，不能添加样本了");
            return map;
        }
            //需要保存的托盘核算和文库
        Map<String,EpVehicleModel> deskMap_wk=setAndGetUtil.getAllEpMap(RedisStorageEnum.EP_MAP_WZ.getCode());
        EpVehicleModel epVehicleModel_wk=deskMap_wk.get(RedisStorageEnum.EP_NAME_WK.getCode());
        EpVehicleModel epVehicleModel_hs=deskMap_wk.get(RedisStorageEnum.EP_NAME_HS.getCode());

        List<AgvEpModel> epList_wk=epVehicleModel_wk.getEpList()==null?new ArrayList<AgvEpModel>():epVehicleModel_wk.getEpList();
        List<AgvEpModel> epList_hs=epVehicleModel_hs.getEpList()==null?new ArrayList<AgvEpModel>():epVehicleModel_hs.getEpList();

        for(YsybxxDto ysybxxDto:list){
            //不允许相同的样本编号
            if(StringUtil.isNotBlank(ysybxxDto.getNbbh())){
                if(nbbhlist .contains(ysybxxDto.getNbbh())){
                    map.put("status", "false");
                    map.put("message", "存在相同的样本编号："+ysybxxDto.getNbbh());
                    return map;
                }
            }
            nbbhlist.add(ysybxxDto.getNbbh());
            //判断是否在同样的位置存在值
            int num = -1;
            for (int i=0;i<epList_wk.size();i++){
                AgvEpModel wkModel=epList_wk.get(i);
                if(wkModel.getXzb().equals(ysybxxDto.getX())&&wkModel.getYzb().equals(ysybxxDto.getY())){
                    num = i;
                    //如果样本编号为空，代表删除了，则从原有的地方删除
                    if(StringUtil.isBlank(ysybxxDto.getNbbh())){
                        epList_wk.remove(i);
                        epList_hs.remove(i);
                    }else {
                        wkModel.setNbbh(ysybxxDto.getNbbh());
                        wkModel.setJth(ysybxxDto.getJth());
                        wkModel.setYsybid(ysybxxDto.getYsybid());
                        if(StringUtil.isNotBlank(ysybxxDto.getYsybid())) {
                            epList_wk.set(i, wkModel);
                            epList_hs.set(i, wkModel);
                        }
                    }
                    break;
                }
            }
            if (StringUtil.isNotBlank(ysybxxDto.getYsybid())) {
                ysybxxDto.setXgry(ysybxxDto.getUserId());
                updateList.add(ysybxxDto);
            }else{
                if(StringUtil.isBlank(ysybxxDto.getNbbh())){
                    continue;
                }
                //判断如果有相同的样本编号或者相同的位置的就不保存
                if(num>=0){
                    continue;
                }
                ysybxxDto.setLrry(ysybxxDto.getUserId());
                ysybxxDto.setYsybid(StringUtil.generateUUID());
                addList.add(ysybxxDto);
                YblcsjDto yblcsjDto = new YblcsjDto();
                yblcsjDto.setYsybid(ysybxxDto.getYsybid());
                yblcsjDto.setLcsjid(StringUtil.generateUUID());
                yblcsjList.add(yblcsjDto);
                AgvEpModel agvEpModel=new AgvEpModel();
                agvEpModel.setXzb(ysybxxDto.getX()+"");
                agvEpModel.setYzb(ysybxxDto.getY()+"");
                agvEpModel.setNbbh(ysybxxDto.getNbbh());
                agvEpModel.setJth(ysybxxDto.getJth());
                agvEpModel.setYsybid(ysybxxDto.getYsybid());
                epList_hs.add(agvEpModel);
                epList_wk.add(agvEpModel);
            }
        }
        try {
            ysybxxDao.updateList(updateList);
            ysybxxDao.insertList(addList);
            yblcsjService.insertList(yblcsjList);
            epVehicleModel_wk.setEpList(epList_wk);
            epVehicleModel_hs.setEpList(epList_hs);
            setAndGetUtil.setWzEpMap(RedisStorageEnum.EP_NAME_WK.getCode(),epVehicleModel_wk);
            setAndGetUtil.setWzEpMap(RedisStorageEnum.EP_NAME_HS.getCode(),epVehicleModel_hs);
            map.put("status", "true");
            map.put("message", "success");
        }catch (Exception e){
            map.put("status", "false");
            map.put("message", e.getMessage());
        }
        return map;
    }
    /**
     * 保存样本到数据库
     *
     * @param ysybxxDto
     * @return
     */
    public boolean saveYsybxxDto(YsybxxDto ysybxxDto) {
        if (StringUtil.isNotBlank(ysybxxDto.getYsybid())) {
            ysybxxDto.setXgry(ysybxxDto.getUserId());
        }
        //接头号生成
       /* JcsjDto jcsjDto = new JcsjDto();
        jcsjDto.setCsid(ysybxxDto.getZbf());
        jcsjDto.setJclb(BasicDataTypeEnum.PREPARATIVE_METHOD.getCode());
        JcsjDto jcsjDto1 = jcsjService.getByAndCsdm(jcsjDto);
        if (jcsjDto1 == null) {
            return false;
        }*/
        //ysybxxDto.setZbfmc(jcsjDto1.getCsmc());
        if (StringUtil.isNotBlank(ysybxxDto.getYsybid())) {
            ysybxxDao.updateDto(ysybxxDto);
        } else {
            ysybxxDto.setLrry(ysybxxDto.getUserId());
            ysybxxDto.setYsybid(StringUtil.generateUUID());
            //添加时间流程表
            YblcsjDto yblcsjDto = new YblcsjDto();
            yblcsjDto.setYsybid(ysybxxDto.getYsybid());
            yblcsjDto.setLcsjid(StringUtil.generateUUID());
            yblcsjService.insert(yblcsjDto);
            ysybxxDao.insertDto(ysybxxDto);
        }
        return true;
    }

    /**机器人在判断和修改editNow时需要在此方法中，以免不同步
     * 判断机器人身上有没有带托盘，和放托盘的判断,返回值为是否立即执行拿托盘
     * type 类型，0，执行时修改并判断,1 托盘归还时判断并修改
     * editNow false表示未进行还托盘操作，或者已判断完成前料区，true代表，一归还托盘，还未判断前物料区
     */
    public static boolean judgeAndEditTray(String type) {
        synchronized (YsybxxServiceImpl.class) {
            Map<String, YsybxxDto> tray1 = AgvDesktopGlobal.getAgv_tray1();
            Map<String, YsybxxDto> tray2 = AgvDesktopGlobal.getAgv_tray2();
            List<TrayModel> list = FrontMaterialAreaGlobal.getFma_tray();
            boolean isPre = false;
            if ("0".equals(type)) {
                //修改，此时托盘上的样本改为待执行
                if (list != null && list.size() > 0) {
                    //遍历修改托盘里的样本为待执行
                    for (TrayModel ts : list) {
                        List<YsybxxDto> lsAgvyb = ts.getBoxList();
                        if (!VehicleStatusEnum.VEHICLE_TRAY_FREE.getCode().equals(ts.getZt())) {
                            isPre = true;
                        }
                        if (lsAgvyb != null && lsAgvyb.size() > 0) {
                            ts.setZt(VehicleStatusEnum.VEHICLE_TRAY_PENDING.getCode());
                            for (YsybxxDto as : lsAgvyb) {
                                if (as != null) {
                                    as.setYbzt(VehicleStatusEnum.VEHICLE_CARDBOX_PENDING.getCode());
                                }
                            }
                        }
                    }
                    FrontMaterialAreaGlobal.setFma_tray(list, true);
                }
                //判断此时机器人身上有无托盘
                String editNow = AgvDesktopGlobal.getEditNow();
                if ( !isPre&&("false".equals(editNow)&&tray1 == null && tray2 == null)) {
                    return true;
                }
            } else {
                //判断前物料区是否有待执行得托盘，并且托盘里还有样本。
            	boolean flag = false;
            	//判断AGV平台是否存在空闲位置
				Map<String , YsybxxDto> try1 = AgvDesktopGlobal.getAgv_tray1();
	        	Map<String , YsybxxDto> try2 = AgvDesktopGlobal.getAgv_tray2();
	        	if((try1==null) || (try2==null)) {
	        		//存在空闲位置，判断是否存在待放置托盘
	        		List<TrayModel> fma_tr = FrontMaterialAreaGlobal.getFma_tray();
	        		if(fma_tr!=null && fma_tr.size()>0) {
	        			for (TrayModel trayModel : fma_tr) {
                    		if("1".equals(trayModel.getZt()) && trayModel.getBoxList()!=null) {
                    			flag=true;	                    			
                    			break;
    						}
    					}
	        			
	        		}
	        	}
            	
                //判断完成后修改editNow为false，代表，已经判断完毕
                AgvDesktopGlobal.setEditNow("false");
                return flag;
            }

        }
        return false;
    }

    /**
     * 遍历一个托盘map，修改托盘的样本的状态
     *
     * @param map
     * @return
     */
    public static Map<String, YsybxxDto> changeSampleState(Map<String, YsybxxDto> map) {
        if (map == null) return null;
        for (String a : map.keySet()) {
            YsybxxDto as = map.get(a);
            if (as != null) {
                //修改状态为待执行
                as.setYbzt(VehicleStatusEnum.VEHICLE_CARDBOX_PENDING.getCode());
                map.put(a, as);
            }
        }
        return map;
    }


}
