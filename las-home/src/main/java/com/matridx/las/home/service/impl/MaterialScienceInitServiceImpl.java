
package com.matridx.las.home.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.enums.YesNotEnum;
import com.matridx.igams.common.redis.RedisStreamUtil;
import com.matridx.las.home.dao.entities.YqxxInfoDto;
import com.matridx.las.home.dao.post.IYqxxInfoDao;
import com.matridx.las.home.service.svcinterface.IYqxxInfoService;
import com.matridx.las.home.service.svcinterface.MaterialScienceInitService;
import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.channel.command.SendBaseCommand;
import com.matridx.las.netty.channel.domain.Command;
import com.matridx.las.netty.dao.entities.*;
import com.matridx.las.netty.dao.post.IJkywzszDao;
import com.matridx.las.netty.enums.*;
import com.matridx.las.netty.global.InstrumentStateGlobal;
import com.matridx.las.netty.global.material.AgvDesktopGlobal;
import com.matridx.las.netty.global.material.AutoDesktopGlobal;
import com.matridx.las.netty.global.material.FrontMaterialAreaGlobal;
import com.matridx.las.netty.global.material.InstrumentMaterialGlobal;
import com.matridx.las.netty.service.svcinterface.IYqxxinfosxService;
import com.matridx.las.netty.util.RedisSetAndGetUtil;
import com.matridx.las.netty.util.SendMessgeToHtml;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 试验台和机器人身上的材料初始化或者添加材料
 *
 * @author DELL
 */

@Service
public class MaterialScienceInitServiceImpl implements MaterialScienceInitService {
    @Autowired
    private IJkywzszDao iJkywzszDao;
    @Autowired
    private IYqxxInfoDao yqxxInfoDao;
    @Autowired
    private IYqxxInfoService yqxxInfoService;
    @Value("${matridx.sysid:}")
    private String sysid;
    @Autowired
    private RedisStreamUtil redisStreamUtil;
    @Autowired
    private IYqxxinfosxService yqxxinfosxService;
    @Autowired
    private RedisSetAndGetUtil redisSetAndGetUtil;
    private static final Logger logger = LoggerFactory.getLogger(SendMessgeToHtml.class);
    private static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    /**
     * 初始化，装满材料
     */

    public Map<String, Object>  initMaterial() {
        Map<String, Object> mapRe = new HashMap<>();
        try {
            //删除所有信息
            InstrumentMaterialGlobal.delAllINstr();
            //根据实验室查找仪器和物料区
            YqxxInfoDto yqxxInfoDto = new YqxxInfoDto();
            yqxxInfoDto.setSysid(sysid);
            List<YqxxInfoDto> listYq = yqxxInfoService.getYqxxList(yqxxInfoDto);
            if (listYq != null && listYq.size() > 0) {
                this.initRedis(listYq);
            }
            SendMessgeToHtml.pushMessage(null);
            SendMessgeToHtml.putTraySample();

            //redisStreamUtil.delGroup("lassGroup","lassGroup1");
            //初始化队列
            redisSetAndGetUtil.delStream("InstrumentUsedList");

            redisSetAndGetUtil.delStream("LibaryInfMap");
            redisSetAndGetUtil.delStream("cubFinMap");
            redisSetAndGetUtil.delStream("InstrumentFinMap");
            redisSetAndGetUtil.delStream("CubFinQueue");
            redisSetAndGetUtil.delStream("lassGroup");
            redisStreamUtil.creartGroup("lassGroup", "lassGroup1");

            //redisStreamUtil.delGroup("AutoGroup","AutoGroup1");
            redisSetAndGetUtil.delStream("AutoGroup");
            redisStreamUtil.creartGroup("AutoGroup", "AutoGroup1");

            //redisStreamUtil.delGroup("CubisGroup","CubisGroup1");
            redisSetAndGetUtil.delStream("CubisGroup");
            redisStreamUtil.creartGroup("CubisGroup", "CubisGroup1");

            //redisStreamUtil.delGroup("PcrGroup","PcrGroup1");
            redisSetAndGetUtil.delStream("PcrGroup");
            redisStreamUtil.creartGroup("PcrGroup", "PcrGroup1");

            //redisStreamUtil.delGroup("CmhGroup","CmhGroup1");
            redisSetAndGetUtil.delStream("CmhGroup");
            redisStreamUtil.creartGroup("CmhGroup", "CmhGroup1");

            //redisStreamUtil.delGroup("SeqGroup","SeqGroup1");
            redisSetAndGetUtil.delStream("SeqGroup");
            redisStreamUtil.creartGroup("SeqGroup", "SeqGroup1");

            redisSetAndGetUtil.setisWorklc(false);
            redisSetAndGetUtil.setIsAutoWork(false);
            redisSetAndGetUtil.setIsAutoWork1(false);
            redisSetAndGetUtil.setIsStartWork(false);
            redisSetAndGetUtil.setEpClaw("");
            redisSetAndGetUtil.setAvgQy("");
        } catch (Exception e) {
            logger.info(e.getMessage());
            mapRe.put("status", "false");
            mapRe.put("message", e.getMessage());
            return mapRe;
        }
        SendMessgeToHtml.pushMessage(null);
        mapRe.put("status", "true");
        mapRe.put("message", "success");
        return mapRe;

    }
    public void initRedis(List<YqxxInfoDto> listYq){
        List<TrayModel> listFmTr = new ArrayList<>();
        List<EpVehicleModel> listEp = new ArrayList<>();
        List<GunVehicleModel> listFmQt = new ArrayList<>();
        List<OctalianpipeListModel> listOCBl = new ArrayList<>();
        Map<String,Map<String,String>> cubsMap = new HashMap<>();
        for (YqxxInfoDto yqxx : listYq) {
            YqxxinfosxDto yqxxinfosxDto = new YqxxinfosxDto();
            yqxxinfosxDto.setYqxxid(yqxx.getYqxxid());
            List<YqxxinfosxDto> yqxxinfosxDtoList = yqxxinfosxService.getYqxxinfoSxList(yqxxinfosxDto);
            if(yqxxinfosxDtoList==null){
                yqxxinfosxDtoList=new ArrayList<>();
            }
            Map<String,String> yqsxMap = new HashMap<>();
            for(YqxxinfosxDto sx:yqxxinfosxDtoList){
                if(StringUtil.isNotBlank(sx.getCsdm())) {
                    yqsxMap.put(sx.getCsdm(), sx.getSxmc());
                }
            }
            yqxx.setYqxxinfosxDtoMap(yqsxMap);
            if (MaterialTypeEnum.MATERIAL_ZJ.getCode().equals(yqxx.getLx())) {
                initZJ(yqxx,listEp,listFmQt,listFmTr,listOCBl);
            } else if (MaterialTypeEnum.MATERIAL_AUTO.getCode().equals(yqxx.getLx())) {
                initAuto(yqxx,false);
            } else if (MaterialTypeEnum.MATERIAL_AGV.getCode().equals(yqxx.getLx())) {
                initAgv(yqxx);
            } else if (MaterialTypeEnum.MATERIAL_CMH.getCode().equals(yqxx.getLx())) {
                initCmh(yqxx,false);
            } else if (MaterialTypeEnum.MATERIAL_PCR.getCode().equals(yqxx.getLx())) {
                initPcr(yqxx);
            } else if (MaterialTypeEnum.MATERIAL_SEQ.getCode().equals(yqxx.getLx())) {
                initSeq(yqxx);
            } else if (MaterialTypeEnum.MATERIAL_CUBICS.getCode().equals(yqxx.getLx())) {
                cubsMap.put(yqxx.getDeviceid(),yqsxMap);
            } else if (MaterialTypeEnum.MATERIAL_OCP.getCode().equals(yqxx.getLx())) {
                initOCP(yqxx);
            } else if (MaterialTypeEnum.MATERIAL_FMA.getCode().equals(yqxx.getLx())) {
                initFMA(yqxx);
            }

        }
        //将组件放入物料区

        //将组件放入物料区

        initRedisByList(listEp,listFmQt,listOCBl);
        FrontMaterialAreaGlobal.setFma_tray(listFmTr, false);
        //初始化建库仪
        initCubis(cubsMap);
    }

    /**
     * 初始化测序仪
     * @param yqxx
     */
    private void initSeq(YqxxInfoDto yqxx){
        InstrumentMaterialGlobal.setInstrumentWz(yqxx.getCommanddeviceid(),yqxx.getWz());
        SeqMaterialModel seqMaterialModel = new SeqMaterialModel();
        seqMaterialModel.setDeviceid(yqxx.getDeviceid());
        seqMaterialModel.setName(yqxx.getName());
        seqMaterialModel.setWz(yqxx.getWz());
        seqMaterialModel.setPzwz(yqxx.getPzwz());
        seqMaterialModel.setQy(yqxx.getQy());
        seqMaterialModel.setCsdm(yqxx.getCsdm());
        seqMaterialModel.setBz(yqxx.getBz());
        seqMaterialModel.setSendSxType(yqxx.getYqxxinfosxDtoMap());
        seqMaterialModel.setCommanddeviceid(yqxx.getCommanddeviceid());
        OctalianpipeListModel octalianpipeListModel = new OctalianpipeListModel();
        seqMaterialModel.setOctalianpipeList(octalianpipeListModel);
        seqMaterialModel.setState(InstrumentStatusEnum.STATE_OFFLINE.getCode());
        InstrumentMaterialGlobal.setSeqMaterial(seqMaterialModel, false);
    }

    /**
     * 初始化艰苦仪
     * @param yqxx
     */
    private void initPcr(YqxxInfoDto yqxx){
        InstrumentMaterialGlobal.setInstrumentWz(yqxx.getCommanddeviceid(),yqxx.getWz());
        PcrMaterialModel pcrMaterialModel = new PcrMaterialModel();
        pcrMaterialModel.setDeviceid(yqxx.getDeviceid());
        pcrMaterialModel.setName(yqxx.getName());
        pcrMaterialModel.setWz(yqxx.getWz());
        pcrMaterialModel.setPzwz(yqxx.getPzwz());
        pcrMaterialModel.setQy(yqxx.getQy());
        pcrMaterialModel.setCsdm(yqxx.getCsdm());
        pcrMaterialModel.setBz(yqxx.getBz());
        pcrMaterialModel.setCommanddeviceid(yqxx.getCommanddeviceid());
        OctalianpipeListModel octalianpipeListModel = new OctalianpipeListModel();
        pcrMaterialModel.setOctalianpipeList(octalianpipeListModel);
        pcrMaterialModel.setSendSxType(yqxx.getYqxxinfosxDtoMap());
        pcrMaterialModel.setState(InstrumentStatusEnum.STATE_OFFLINE.getCode());
        InstrumentMaterialGlobal.setPcrMaterial(pcrMaterialModel, false);
    }
    /**
     * 初始化建库仪
     */
    private void initCubis(Map<String,Map<String,String>> map){
        JkywzszDto jkywzszDto = new JkywzszDto();
        List<JkywzszDto> listJky = iJkywzszDao.getDtoList(jkywzszDto);
        int il = 0;
        for (JkywzszDto jky : listJky) {
            CubsMaterialModel cus = new CubsMaterialModel();
            il = il + 1;
            cus.setState(InstrumentStatusEnum.STATE_OFFLINE.getCode());
            cus.setPassId(jky.getJkytdh());
            cus.setEpLeft(YesNotEnum.NOT.getValue());
            cus.setEpRight(YesNotEnum.NOT.getValue());
            cus.setGunheadLeft(YesNotEnum.NOT.getValue());
            cus.setGunheadRight(YesNotEnum.NOT.getValue());
            cus.setDeviceid(jky.getDeviceid());
            cus.setBz(jky.getBz());
            cus.setCsdm(jky.getCsdm());
            cus.setCommanddeviceid(Command.CUBICS.toString()+jky.getDeviceid());
            //cus.setSample(il+"1232N");
            if(map!=null){
                cus.setSendSxType( map.get(jky.getDeviceid()));
            }
            cus.setPosition(jky.getJkywzbh());
            cus.setWzszid(jky.getWzszid());
            InstrumentMaterialGlobal.setCubsMaterial(cus, false);
        }
    }
    /**
     * 初始化机器人身上物料
     * @param yqxx
     */
    private void initAgv(YqxxInfoDto yqxx){
        EpVehicleModel epVehicleModelA1 = new EpVehicleModel();
        EpVehicleModel epVehicleModelA2 = new EpVehicleModel();
        OctalianpipeListModel octalianpipeListModel1 = new OctalianpipeListModel();
        octalianpipeListModel1.setWz(GlobalParmEnum.AGV_BLG_WZ.getCode());
        octalianpipeListModel1.setName("八连管");
        GunVehicleModel gunVehicleModel = new GunVehicleModel();
        gunVehicleModel.setWz(GlobalParmEnum.AGV_QT_WZ.getCode());
        gunVehicleModel.setName("枪头");
        AgvDesktopGlobal.setAgv_tray1(null, false);
        AgvDesktopGlobal.setAgv_tray2(null, false);
        epVehicleModelA1.setEpList(null);
        epVehicleModelA1.setName("EP(1)");
        epVehicleModelA2.setEpList(null);
        epVehicleModelA1.setIsnull(true);
        epVehicleModelA2.setIsnull(true);
        epVehicleModelA2.setName("EP(2)");
        epVehicleModelA1.setWz(GlobalParmEnum.AGV_EP_WZ1.getCode());
        epVehicleModelA2.setWz(GlobalParmEnum.AGV_EP_WZ2.getCode());
        //Ep管
        redisSetAndGetUtil.setDeskEpMap(RedisStorageEnum.EP_AGV_DESK1.getCode(),epVehicleModelA1);
        redisSetAndGetUtil.setDeskEpMap(RedisStorageEnum.EP_AGV_DESK2.getCode(),epVehicleModelA2);
        AgvDesktopGlobal.setDeviceid(yqxx.getDeviceid());
        AgvDesktopGlobal.setLx(yqxx.getLx());
        AgvDesktopGlobal.setName(yqxx.getName());
        AgvDesktopGlobal.setDeviceid(yqxx.getDeviceid());
        AgvDesktopGlobal.setWz(yqxx.getWz());
        AgvDesktopGlobal.setPzwz(yqxx.getPzwz());
        AgvDesktopGlobal.setQy(yqxx.getQy());
        AgvDesktopGlobal.setEditNow("false");
        AgvDesktopGlobal.setCommanddeviceid(yqxx.getCommanddeviceid());
        AgvDesktopGlobal.setState(InstrumentStatusEnum.STATE_OFFLINE.getCode());
        AgvDesktopGlobal.setAgv_octalianpipe(octalianpipeListModel1, false);
        AgvDesktopGlobal.setAgv_gunhead(gunVehicleModel,false);
    }
    /**
     * 初始化组件
     * @param yqxx
     * @param listEp
     * @param listFmQt
     * @param listFmTr
     * @param listOCBl
     */
    private void initZJ(YqxxInfoDto yqxx, List<EpVehicleModel> listEp, List<GunVehicleModel> listFmQt, List<TrayModel> listFmTr, List<OctalianpipeListModel> listOCBl){
        //查找一下父区域
        YqxxInfoDto yqxxInfoDto2 = new YqxxInfoDto();
        yqxxInfoDto2.setFqy(yqxx.getFqy());
        YqxxInfoDto yqxxInfoDto1 = yqxxInfoDao.getDtoByfqyid(yqxxInfoDto2);
        if (yqxxInfoDto1 == null) return;
        //前物料区
        if (MaterialTypeEnum.ZJ_TYPE_TP.getCode().equals(yqxx.getYq())) {
            TrayModel trayModel = new TrayModel();
            trayModel.setDeviceid(yqxx.getDeviceid());
            trayModel.setTpbh(yqxx.getWz());
            trayModel.setName(yqxx.getName());
            trayModel.setCsdm(yqxx.getCsdm());
            trayModel.setBz(yqxx.getBz());
            trayModel.setWz(yqxx.getWz());
            trayModel.setQy(yqxx.getQy());
            trayModel.setPzwz(yqxx.getPzwz());
            trayModel.setCommanddeviceid(yqxx.getCommanddeviceid());
            trayModel.setZt(VehicleStatusEnum.VEHICLE_TRAY_FREE.getCode());
            trayModel.setSendSxType(yqxx.getYqxxinfosxDtoMap());
            if (MaterialTypeEnum.MATERIAL_FMA.getCode().equals(yqxxInfoDto1.getLx())) {
                listFmTr.add(trayModel);
            }
        } else if (MaterialTypeEnum.ZJ_TYPE_BL.getCode().equals(yqxx.getYq())) {
            OctalianpipeListModel octalianpipeListModel = new OctalianpipeListModel();
            octalianpipeListModel.setDeviceid(yqxx.getDeviceid());
            octalianpipeListModel.setOcType(MaterialTypeEnum.ZJ_TYPE_BL.getCode());
            octalianpipeListModel.setMap(null);
            octalianpipeListModel.setWz(yqxx.getWz());
            octalianpipeListModel.setPzwz(yqxx.getPzwz());
            octalianpipeListModel.setCsdm(yqxx.getCsdm());
            octalianpipeListModel.setBz(yqxx.getBz());
            octalianpipeListModel.setQy(yqxx.getQy());
            octalianpipeListModel.setCommanddeviceid(yqxx.getCommanddeviceid());
            octalianpipeListModel.setName(yqxx.getName());
            octalianpipeListModel.setSendSxType(yqxx.getYqxxinfosxDtoMap());
            if (MaterialTypeEnum.MATERIAL_OCP.getCode().equals(yqxxInfoDto1.getLx())) {
                listOCBl.add(octalianpipeListModel);
            }
        } else if (MaterialTypeEnum.ZJ_TYPE_EP.getCode().equals(yqxx.getYq())) {
            EpVehicleModel epVehicleModel = new EpVehicleModel();
            epVehicleModel.setDeviceid(yqxx.getDeviceid());
            epVehicleModel.setName(yqxx.getName());
            epVehicleModel.setIsnull(false);
            epVehicleModel.setEpList(new ArrayList<>());
            epVehicleModel.setCommanddeviceid(yqxx.getCommanddeviceid());
            epVehicleModel.setWz(yqxx.getWz());
            epVehicleModel.setCsdm(yqxx.getCsdm());
            epVehicleModel.setBz(yqxx.getBz());
            epVehicleModel.setQy(yqxx.getQy());
            epVehicleModel.setPzwz(yqxx.getPzwz());
            epVehicleModel.setEpType(MaterialTypeEnum.ZJ_TYPE_EP.getCode());
            epVehicleModel.setSendSxType(yqxx.getYqxxinfosxDtoMap());
            listEp.add(epVehicleModel);

        } else if (MaterialTypeEnum.ZJ_TYPE_QT.getCode().equals(yqxx.getYq())) {
            GunVehicleModel gunVehicleModel = new GunVehicleModel();
            gunVehicleModel.setDeviceid(yqxx.getDeviceid());
            gunVehicleModel.setName(yqxx.getName());
            gunVehicleModel.setWz(yqxx.getWz());
            gunVehicleModel.setPzwz(yqxx.getPzwz());
            gunVehicleModel.setCsdm(yqxx.getCsdm());
            gunVehicleModel.setBz(yqxx.getBz());
            gunVehicleModel.setCommanddeviceid(yqxx.getCommanddeviceid());
            gunVehicleModel.setQy(yqxx.getQy());
            gunVehicleModel.setSendSxType(yqxx.getYqxxinfosxDtoMap());
            gunVehicleModel.setGunType(MaterialTypeEnum.ZJ_TYPE_QT.getCode());
            //枪头装满
            List<String> list = new ArrayList<>();
            int total = Integer.parseInt(GlobalParmEnum.GUN_TOTAL_NUM.getCode());
            for (int i = 1; i < total; i++) {
                list.add(i + "");
            }
            gunVehicleModel.setGunhead(list);
            if (MaterialTypeEnum.MATERIAL_FMA.getCode().equals(yqxxInfoDto1.getLx())) {
                listFmQt.add(gunVehicleModel);
            }

        }

    }

    /**
     * 初始化AUTO
     * @param yqxx
     */
    private void initAuto(YqxxInfoDto yqxx,boolean isAllInit){
        //初始化配置仪
        AutoMaterialModel autoMaterialModel=null;
        if(isAllInit){
            Object ob = redisSetAndGetUtil.getInstrument("auto",yqxx.getDeviceid());
             autoMaterialModel = JSONObject.parseObject(ob.toString(), AutoMaterialModel.class);
        }else{
            //InstrumentMaterialGlobal.setInstrumentWz(yqxx.getDeviceid(),yqxx.getWz());
            InstrumentMaterialGlobal.setInstrumentWz(yqxx.getCommanddeviceid(),"4");
             autoMaterialModel = new AutoMaterialModel();
        }

        autoMaterialModel.setDeviceid(yqxx.getDeviceid());
        autoMaterialModel.setWz(yqxx.getWz());
        autoMaterialModel.setPzwz(yqxx.getPzwz());
        autoMaterialModel.setQy(yqxx.getQy());
        autoMaterialModel.setCommanddeviceid(yqxx.getCommanddeviceid());
        autoMaterialModel.setName(yqxx.getName());
        autoMaterialModel.setCsdm(yqxx.getCsdm());
        autoMaterialModel.setBz(yqxx.getBz());
        autoMaterialModel.setSendSxType(yqxx.getYqxxinfosxDtoMap());
        autoMaterialModel.setState(InstrumentStatusEnum.STATE_OFFLINE.getCode());
        OctalianpipeListModel octalianpipeList = new OctalianpipeListModel();
        int total = Integer.parseInt(GlobalParmEnum.OC_TOTAL_NUM.getCode());
        octalianpipeList.setNum(total);
        Map<String, List<OctalYbxxxModel>> map = new HashMap<>();
        octalianpipeList.setMap(map);
        autoMaterialModel.setOctalianpipeList(octalianpipeList);
        InstrumentMaterialGlobal.setAutoMaterial(autoMaterialModel, false);
    }

    /**
     * 初始化CMH
     * @param yqxx
     */
    private void initCmh(YqxxInfoDto yqxx,boolean isAllInit){
        ChmMaterialModel chmMaterialModel=null;
        if(!isAllInit){
            InstrumentMaterialGlobal.setInstrumentWz(yqxx.getCommanddeviceid(),yqxx.getWz());
             chmMaterialModel = new ChmMaterialModel();
             chmMaterialModel.setOctNum(0);
        }else{
            Object ob = redisSetAndGetUtil.getInstrument("chm",yqxx.getDeviceid());
            chmMaterialModel= JSONObject.parseObject(ob.toString(), ChmMaterialModel.class);
            chmMaterialModel.setOctgNum(Integer.valueOf(GlobalParmEnum.COVER_TOTAL_NUM.getCode()));
        }
        chmMaterialModel.setDeviceid(yqxx.getDeviceid());
        chmMaterialModel.setWz(yqxx.getWz());
        chmMaterialModel.setPzwz(yqxx.getPzwz());
        chmMaterialModel.setQy(yqxx.getQy());
        chmMaterialModel.setCommanddeviceid(yqxx.getCommanddeviceid());
        chmMaterialModel.setName(yqxx.getName());
        chmMaterialModel.setCsdm(yqxx.getCsdm());
        chmMaterialModel.setBz(yqxx.getBz());
        chmMaterialModel.setState(InstrumentStatusEnum.STATE_OFFLINE.getCode());
        chmMaterialModel.setSendSxType(yqxx.getYqxxinfosxDtoMap());
        chmMaterialModel.setChmgNum(Integer.parseInt(GlobalParmEnum.COVER_TOTAL_NUM.getCode()));
        InstrumentMaterialGlobal.setChmMaterial(chmMaterialModel, false);

    }

    /**
     * 初始化后物料区
     * @param yqxx
     */
    private void initOCP(YqxxInfoDto yqxx){
        AutoDesktopGlobal.setDeviceid(yqxx.getDeviceid());
        AutoDesktopGlobal.setLx(yqxx.getLx());
        AutoDesktopGlobal.setQy(yqxx.getQy());
        AutoDesktopGlobal.setWz(yqxx.getWz());
        AutoDesktopGlobal.setPzwz(yqxx.getPzwz());
        AutoDesktopGlobal.setName(yqxx.getName());
        AutoDesktopGlobal.setCommanddeviceid(yqxx.getCommanddeviceid());
    }

    /**
     * 初始化前物料区
     * @param yqxx
     */
    private void initFMA(YqxxInfoDto yqxx){
        FrontMaterialAreaGlobal.setDeviceid(yqxx.getDeviceid());
        FrontMaterialAreaGlobal.setLx(yqxx.getLx());
        FrontMaterialAreaGlobal.setQy(yqxx.getQy());
        FrontMaterialAreaGlobal.setWz(yqxx.getWz());
        FrontMaterialAreaGlobal.setPzwz(yqxx.getPzwz());
        FrontMaterialAreaGlobal.setName(yqxx.getName());
        FrontMaterialAreaGlobal.setCommanddeviceid(yqxx.getCommanddeviceid());
    }

    /**
     * //将组件放入物料区
     * @param listEp
     * @param listFmQt
     * @param listOCBl
     */
    private void initRedisByList(List<EpVehicleModel> listEp,List<GunVehicleModel> listFmQt,List<OctalianpipeListModel> listOCBl){
        for (int i = 0; i < listEp.size(); i++) {
            EpVehicleModel epVehicleModel = listEp.get(i);
            epVehicleModel.setEpNm(i + 1 + "");
            //配置指定的装kongEp管的载具名称
            if(RedisStorageEnum.EP_NAME_EMPTY.getCode().equals(epVehicleModel.getCsdm())) {
                List<AgvEpModel> list2 = new ArrayList<>();
                int total = Integer.parseInt(GlobalParmEnum.EP_TOTAL_NUM.getCode());
                for (int j = 0; j < total; j++) {
                    AgvEpModel agvEpModel = new AgvEpModel();
                    agvEpModel.setBlank(true);
                    int xzb = (int) Math.floor(j / 8) + 1;
                    int yzb = (j+1) % 8;
                    if (yzb == 0) {
                        yzb = 8;
                    }
                    agvEpModel.setXzb(xzb + "");
                    agvEpModel.setYzb(yzb + "");
                    list2.add(agvEpModel);
                }
                epVehicleModel.setEpList(list2);
            }
            redisSetAndGetUtil.setWzEpMap(epVehicleModel.getCsdm(),epVehicleModel);
        }
        for (int i = 0; i < listFmQt.size(); i++) {
            GunVehicleModel gunVehicleModel = listFmQt.get(i);
            gunVehicleModel.setGunNm(i + 1 + "");
            if (i == 0) {
                FrontMaterialAreaGlobal.setFma_gunhead1(gunVehicleModel, false);
            } else if (i == 1) {
                FrontMaterialAreaGlobal.setFma_gunhead2(gunVehicleModel, false);
            }
        }
        for (int i = 0; i < listOCBl.size(); i++) {
            OctalianpipeListModel octalianpipeListModel = listOCBl.get(i);
            if (i == 0) {
                int total = Integer.parseInt(GlobalParmEnum.OC_TOTAL_NUM.getCode());
                octalianpipeListModel.setNum(total);
                Map<String, List<OctalYbxxxModel>> map = new HashMap<>();
                octalianpipeListModel.setMap(map);
                AutoDesktopGlobal.setauto_octalianpipe1(octalianpipeListModel, false);
            } else if (i == 1) {
                //Map<String,List<OctalYbxxxModel>> map = new HashMap<>();
                octalianpipeListModel.setMap(null);
                AutoDesktopGlobal.setauto_octalianpipe2(octalianpipeListModel, false);
            }
        }
    }

    /***
     * 更新物料区以及auto和pcr中的物料
     * @return
     */
    public Map<String, Object> initWlqMaterial() {
        Map<String, Object> mapRe = new HashMap<>();
        try {
            //根据实验室查找仪器和物料区
            YqxxInfoDto yqxxInfoDto = new YqxxInfoDto();
            yqxxInfoDto.setSysid(sysid);
            List<YqxxInfoDto> listYq = yqxxInfoService.getYqxxList(yqxxInfoDto);
            if (listYq != null && listYq.size() > 0) {
                List<TrayModel> listFmTr = new ArrayList<>();
                List<EpVehicleModel> listEp = new ArrayList<>();
                List<GunVehicleModel> listFmQt = new ArrayList<>();
                List<OctalianpipeListModel> listOCBl = new ArrayList<>();
                for (YqxxInfoDto yqxx : listYq) {
                    if (MaterialTypeEnum.MATERIAL_ZJ.getCode().equals(yqxx.getLx())) {
                        initZJ(yqxx,listEp,listFmQt,listFmTr,listOCBl);
                    }else if (MaterialTypeEnum.MATERIAL_AUTO.getCode().equals(yqxx.getLx())) {
                        initAuto(yqxx,true);
                    } else if (MaterialTypeEnum.MATERIAL_CMH.getCode().equals(yqxx.getLx())) {
                        initCmh(yqxx,true);
                    }else if (MaterialTypeEnum.MATERIAL_OCP.getCode().equals(yqxx.getLx())) {
                        initOCP(yqxx);
                    } else if (MaterialTypeEnum.MATERIAL_FMA.getCode().equals(yqxx.getLx())) {
                        initFMA(yqxx);
                    }

                }
                initRedisByList(listEp,listFmQt,listOCBl);
                FrontMaterialAreaGlobal.setFma_tray(listFmTr, false);
            }
            SendMessgeToHtml.pushMessage(null);
            SendMessgeToHtml.putTraySample();
        } catch (Exception e) {
            logger.info(e.getMessage());
            mapRe.put("status", "false");
            mapRe.put("message", e.getMessage());
            return mapRe;
        }
        mapRe.put("status", "true");
        mapRe.put("message", "success");
        return mapRe;

    }
    //找到相应的仪器信息，并补充枪头
    public void fillupAuto(String deviceid){
        Runnable newRunnable = new Runnable() {
            public void run() {
                SendBaseCommand sendBaseCommand = new SendBaseCommand();
                while(true){
                    String dv = null;
                    try {
                        dv = sendBaseCommand.getDeviceIdByCommd(Command.AUTO.toString());
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                    if(deviceid.equals(dv)){
                        InstrumentStateGlobal.setInstrumentUsedList_Map(new HashMap<>(),Command.AUTO.toString() , dv);
                        break;
                    }else{
                        InstrumentStateGlobal.putInstrumentQueuesToRedis(Command.AUTO.toString(),deviceid);
                    }

                }

                FrameModel frameModel = new FrameModel();
                frameModel.setDeviceID(deviceid);
                sendBaseCommand.sendEventFlowlist(MedicalRobotProcessEnum.MEDICAL_AUTO_FILL_UP.getCode(), frameModel);

            }
        };
        cachedThreadPool.execute(newRunnable);

    }

}

