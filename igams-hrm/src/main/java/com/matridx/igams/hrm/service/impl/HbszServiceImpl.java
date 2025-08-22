package com.matridx.igams.hrm.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.DsrwszDto;
import com.matridx.igams.common.enums.RedisCommonKeyEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IDsrwszService;
import com.matridx.igams.hrm.controller.RedpacketController;
import com.matridx.igams.hrm.dao.entities.GrksglDto;
import com.matridx.igams.hrm.dao.entities.HbszDto;
import com.matridx.igams.hrm.dao.entities.HbszModel;
import com.matridx.igams.hrm.dao.entities.HbszmxDto;
import com.matridx.igams.hrm.dao.post.IHbszDao;
import com.matridx.igams.hrm.service.svcinterface.IGrksglService;
import com.matridx.igams.hrm.service.svcinterface.IHbszService;
import com.matridx.igams.hrm.service.svcinterface.IHbszmxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.CronUtil;
import com.matridx.igams.common.util.DingTalkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * @author:JYK
 */
@Service
public class HbszServiceImpl extends BaseBasicServiceImpl<HbszDto, HbszModel, IHbszDao> implements IHbszService {
    @Autowired
    IDsrwszService dsrwszService;
    @Autowired
    IHbszmxService hbszmxService;
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    @Lazy
    IGrksglService grksglService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired(required=false)
    private AmqpTemplate amqpTempl;
    @Value("${matridx.rabbit.flg:}")
    private String rabbitFlg;
    @Value("${matridx.rabbit.preflg:}")
    private String preRabbitFlg;
    private final Logger log = LoggerFactory.getLogger(RedpacketController.class);
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean addSaveRedpacketsetting(HbszDto hbszDto) throws BusinessException {
        boolean isSuccess;
//        DsrwszDto dsrwszDto = new DsrwszDto();
        String rwid = StringUtil.generateUUID();
//        dsrwszDto.setRwid(rwid);
//        dsrwszDto.setRwmc(hbszDto.getHbmc());
//        dsrwszDto.setDsxx(CronUtil.getCron(hbszDto.getFssj()));
//        dsrwszDto.setZxl("pxglServiceImpl");
//        dsrwszDto.setZxff("remindRedPacket");
//        dsrwszDto.setLrry(hbszDto.getLrry());
//        isSuccess = dsrwszService.insert(dsrwszDto);
//        if (!isSuccess){
//            throw new BusinessException("msg","新增定时任务信息失败");
//        }
        String hbszid = StringUtil.generateUUID();
        hbszDto.setHbszid(hbszid);
        hbszDto.setRwid(rwid);
        isSuccess = insert(hbszDto);
        if (!isSuccess){
            throw new BusinessException("msg","新增红包设置信息失败");
        }
        List<HbszmxDto> hbszmxDtos = JSON.parseArray(hbszDto.getHbszmx_json(), HbszmxDto.class);
        for (HbszmxDto hbszmxDto : hbszmxDtos) {
            hbszmxDto.setHbszid(hbszid);
            hbszmxDto.setHbmxid(StringUtil.generateUUID());
            hbszmxDto.setLrry(hbszDto.getLrry());
            hbszmxDto.setSysl(hbszmxDto.getSl());
            hbszmxDto.setBs(hbszmxDto.getBs());
            if (StringUtil.isBlank(hbszmxDto.getBs())){
                hbszmxDto.setBs("1");
            }
        }
        isSuccess = hbszmxService.insertHbszmxDtos(hbszmxDtos);
        if (!isSuccess){
            throw new BusinessException("msg","新增红包设置明细信息失败");
        }
        return true;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean modSaveRedpacketsetting(HbszDto hbszDto) throws BusinessException {
        boolean isSuccess = update(hbszDto);
        if (!isSuccess){
            throw new BusinessException("msg","修改红包设置信息失败");
        }
        List<HbszmxDto> hbszmxDtos = JSON.parseArray(hbszDto.getHbszmx_json(), HbszmxDto.class);
        List<HbszmxDto> xgqhbszmxDtos = JSON.parseArray(hbszDto.getXgqhbszmx_json(), HbszmxDto.class);
        List<HbszmxDto> addHbszmxDtos = new ArrayList<>();
        List<HbszmxDto> modHbszmxDtos = new ArrayList<>();
        for (HbszmxDto hbszmxDto : hbszmxDtos) {
            //剩下的就是删除的
            xgqhbszmxDtos.removeIf(next -> next.getHbmxid().equals(hbszmxDto.getHbmxid()));
            if (StringUtil.isBlank(hbszmxDto.getBs())){
                hbszmxDto.setBs("1");
            }
            hbszmxDto.setHbszid(hbszDto.getHbszid());
            if (StringUtil.isBlank(hbszmxDto.getHbmxid())){
                hbszmxDto.setHbmxid(StringUtil.generateUUID());
                hbszmxDto.setSysl(hbszmxDto.getSl());
                hbszmxDto.setLrry(hbszDto.getXgry());
                hbszmxDto.setBs(hbszmxDto.getBs());
                addHbszmxDtos.add(hbszmxDto);
            }else {
                hbszmxDto.setXgry(hbszDto.getXgry());
                modHbszmxDtos.add(hbszmxDto);
            }
        }
        if (!CollectionUtils.isEmpty(addHbszmxDtos)){
            isSuccess = hbszmxService.insertHbszmxDtos(addHbszmxDtos);
            if (!isSuccess){
                throw new BusinessException("msg","新增红包设置明细信息失败");
            }
        }
        if (!CollectionUtils.isEmpty(xgqhbszmxDtos)){
            HbszmxDto hbszmxDto = new HbszmxDto();
            hbszmxDto.setScry(hbszDto.getXgry());
            hbszmxDto.setIds(new ArrayList<>());
            for (HbszmxDto xgqhbszmxDto : xgqhbszmxDtos) {
                hbszmxDto.getIds().add(xgqhbszmxDto.getHbmxid());
            }
            isSuccess = hbszmxService.deleteByHbmxids(hbszmxDto);
            if (!isSuccess){
                throw new BusinessException("msg","删除红包设置明细信息失败");
            }
        }
        if (!CollectionUtils.isEmpty(modHbszmxDtos)){
            isSuccess = hbszmxService.updateHbszmxDtos(modHbszmxDtos);
            if (!isSuccess){
                throw new BusinessException("msg","修改红包设置明细信息失败");
            }
        }
        DsrwszDto dsrwszDto = new DsrwszDto();
        dsrwszDto.setRwid(hbszDto.getRwid());
        dsrwszDto=dsrwszService.getDto(dsrwszDto);
        if (null!=dsrwszDto){
            dsrwszDto.setRwmc(hbszDto.getHbmc());
            dsrwszDto.setDsxx(CronUtil.getCron(hbszDto.getFssj()));
            dsrwszDto.setXgry(hbszDto.getXgry());
            isSuccess = dsrwszService.updateDsxx(dsrwszDto);
            if (!isSuccess){
                throw new BusinessException("msg","修改定时任务信息失败");
            }
            Map<String,Object>newMap=new HashMap<>();
            newMap.put("type","update");
            newMap.put("dsrwid",dsrwszDto.getRwid());
            amqpTempl.convertAndSend("sys.igams", preRabbitFlg+"sys.igams.task.taskHandle"+rabbitFlg+"1", JSONObject.toJSONString(newMap));
        }
        return true;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean delRedpacketsetting(HbszDto hbszDto) throws BusinessException {
        boolean isSuccess;
        List<HbszDto> hbszDtos = dao.getDtoListWithPx(hbszDto);
        for (HbszDto dto : hbszDtos) {
            if (StringUtil.isNotBlank(dto.getPxid())){
                throw new BusinessException("msg",dto.getHbmc()+"已被使用，不允许删除！");
            }
        }
        isSuccess = delete(hbszDto);
        if (!isSuccess){
            throw new BusinessException("msg","删除红包设置信息失败");
        }
        HbszmxDto hbszmxDto = new HbszmxDto();
        hbszmxDto.setIds(hbszDto.getIds());
        hbszmxDto.setScry(hbszDto.getScry());
        isSuccess = hbszmxService.delete(hbszmxDto);
        if (!isSuccess){
            throw new BusinessException("msg","删除红包设置明细信息失败");
        }
        DsrwszDto dsrwszDto = new DsrwszDto();
        dsrwszDto.setIds(hbszDto.getRwids());
        dsrwszDto.setScry(hbszDto.getScry());
        dsrwszService.deleteByRwid(dsrwszDto);
        for (String rwid : hbszDto.getRwids().split(",")) {
            Map<String,Object>newMap=new HashMap<>();
            newMap.put("type","del");
            newMap.put("dsrwid",rwid);
            amqpTempl.convertAndSend("sys.igams", preRabbitFlg+"sys.igams.task.taskHandle"+rabbitFlg+"1", JSONObject.toJSONString(newMap));
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> processRedpacketInfo(HbszDto hbszDto) throws BusinessException {
        log.error("---------------------processRedpacketInfo参数："+ JSON.toJSONString(hbszDto)+"ryid:"+hbszDto.getLrry()+"pxid:"+hbszDto.getPxid());
        Map<String,Object> map = new HashMap<>();
        Object userPxInfo = redisUtil.hget(RedisCommonKeyEnum.TRAIN_USERPXINFO.getKey() + hbszDto.getPxid(),hbszDto.getLrry());
        log.error("---------------------processRedpacketInfo-USERPXINFO_：" + userPxInfo);
        GrksglDto grksglDto_dfInfo;
        //确保安全 总分从数据库拿取
        if (userPxInfo != null) {
            grksglDto_dfInfo = JSON.parseObject(String.valueOf(userPxInfo), GrksglDto.class);
        }else {
            GrksglDto grksglDto_sel = new GrksglDto();
            grksglDto_sel.setGzid(hbszDto.getGzid());
            grksglDto_sel.setGrksid(hbszDto.getGrksid());
            grksglDto_dfInfo = grksglService.getDtoByIdAndGzid(grksglDto_sel);
        }
        Object hbszmxs = redisUtil.get(RedisCommonKeyEnum.TRAIN_HBSZMX.getKey() + hbszDto.getHbszid());
        log.error("---------------------processRedpacketInfo-HBSZMX_：" + hbszmxs);
        List<HbszmxDto> hbszmxDtos;
        List<HbszmxDto> upHbszmxDtos;
        if (hbszmxs!=null){
            hbszmxDtos = JSON.parseArray(String.valueOf(hbszmxs), HbszmxDto.class);
            if (CollectionUtils.isEmpty(hbszmxDtos)){
                map.put("result", false);
                map.put("sysl", -1);
                return map;
            }
            upHbszmxDtos = new ArrayList<>(hbszmxDtos);
            Iterator<HbszmxDto> iterator = hbszmxDtos.iterator();
            while (iterator.hasNext()){
                HbszmxDto next = iterator.next();
                BigDecimal zdf=new BigDecimal(next.getZdf());
                BigDecimal zf=new BigDecimal(grksglDto_dfInfo.getZf());
                // -1小于，0相等，1大于
                if (zdf.compareTo(zf) > 0){
                    iterator.remove();
                }
            }
        }else {
            HbszmxDto hbszmxDto_t = new HbszmxDto();
            hbszmxDto_t.setHbszid(hbszDto.getHbszid());
            hbszmxDtos = hbszmxService.getDtoList(hbszmxDto_t);
            upHbszmxDtos = new ArrayList<>(hbszmxDtos);
            Iterator<HbszmxDto> iterator = hbszmxDtos.iterator();
            while (iterator.hasNext()){
                HbszmxDto next = iterator.next();
                BigDecimal zdf=new BigDecimal(next.getZdf());
                BigDecimal zf=new BigDecimal(grksglDto_dfInfo.getZf());
                // -1小于，0相等，1大于
                if (zdf.compareTo(zf) > 0){
                    iterator.remove();
                }
            }
            log.error("---------------------processRedpacketInfo--hbszmxDtos："+ JSON.toJSONString(hbszmxDtos));
        }
        //您这次没有获得红包，下次再接再厉！
        if (CollectionUtils.isEmpty(hbszmxDtos)){
            map.put("result",false);
            map.put("sysl",-1);
            return map;
        }
        int zsl = 0;
        for (HbszmxDto hbszmxDto : hbszmxDtos) {
            int sysl = Integer.parseInt(StringUtil.isBlank(hbszmxDto.getSysl())?"0":hbszmxDto.getSysl());
            sysl = Math.max(sysl, 0);
            //增加倍率
            if (StringUtil.isNotBlank(hbszmxDto.getBs())){
                sysl = new BigDecimal(hbszmxDto.getBs()).multiply(new BigDecimal(sysl)).intValue();
            }
            zsl = zsl + sysl;
        }
        //很遗憾，红包已经被抢完了！
        if (zsl<=0){
            map.put("result",false);
            map.put("sysl",0);
            return map;
        }
        Random random = new Random();
        int hbs =  random.nextInt(zsl)+1;
        int zhbsl = 0;
        for (HbszmxDto hbszmxDto : hbszmxDtos) {
            int sysl = Integer.parseInt(StringUtil.isBlank(hbszmxDto.getSysl())?"0":hbszmxDto.getSysl());
            sysl = Math.max(sysl, 0);
            zhbsl = zhbsl + new BigDecimal(StringUtil.isNotBlank(hbszmxDto.getBs())?hbszmxDto.getBs():"1").multiply(new BigDecimal(sysl)).intValue();
            if (zhbsl>=hbs){
                map.put("hbszmxDto",hbszmxDto);
                log.error("---------------------processRedpacketInfo--hbszmxDto："+ JSON.toJSONString(hbszmxDto)+"随机数："+hbs+",ryid:"+hbszDto.getLrry()+"rymc"+hbszDto.getLrrymc());
                if ("1".equals(hbszmxDto.getSftz())&&StringUtil.isNotBlank(hbszmxDto.getTznr())&&StringUtil.isNotBlank(hbszDto.getLrrymc())) {
                    String tznr = hbszmxDto.getTznr();
                    tznr = tznr.replace("【name】", hbszDto.getLrrymc()).replace("【je】", hbszmxDto.getJe());
                    talkUtil.sendGroupMessage(hbszDto.getLrry(), hbszmxDto.getTzqcskz1(), tznr);
                }
                for (HbszmxDto upHbszmxDto : upHbszmxDtos) {
                	log.error("--------upHbszmxDto.getHbmxid()"+upHbszmxDto.getHbmxid()+"---------------sysl:"+sysl);
                    if (hbszmxDto.getHbmxid().equals(upHbszmxDto.getHbmxid())){
                        upHbszmxDto.setSysl(String.valueOf(sysl - 1));
                    }
                }
                log.error("--------存redis-------------");
                redisUtil.set(RedisCommonKeyEnum.TRAIN_HBSZMX.getKey() + hbszDto.getHbszid(), JSON.toJSONString(upHbszmxDtos),RedisCommonKeyEnum.TRAIN_HBSZMX.getExpire());
                GrksglDto grksglDto = new GrksglDto();
                grksglDto.setGrksid(hbszDto.getGrksid());
                grksglDto.setJe(hbszmxDto.getJe());
                grksglDto.setSfff("0");
                log.error("--------更新个人考试管理-------------");
                boolean updateGrks = grksglService.update(grksglDto);
                if (!updateGrks) {
                    throw new BusinessException("msg", "修改个人考试信息失败！");
                }
                if (userPxInfo != null) {
                    GrksglDto grksglDto_up = JSON.parseObject(String.valueOf(userPxInfo), GrksglDto.class);
                    grksglDto_up.setJe(grksglDto.getJe());
                    redisUtil.hset(RedisCommonKeyEnum.TRAIN_USERPXINFO.getKey() + hbszDto.getPxid(),hbszDto.getLrry(),JSON.toJSONString(grksglDto_up),RedisCommonKeyEnum.TRAIN_USERPXINFO.getExpire());
                }
                Set<Object> hbpmInfos = redisUtil.zRange(RedisCommonKeyEnum.TRAIN_ALLHBINFOPM.getKey(),0,-1);
                if (hbpmInfos != null) {
                    boolean flag = true;
                    for (Object hbpmInfo : hbpmInfos) {
                        GrksglDto grksglDto_sort = JSON.parseObject(String.valueOf(hbpmInfo), GrksglDto.class);
                        if (hbszDto.getLrry().equals(grksglDto_sort.getRyid())){
                            flag = false;
                            //先删除
                            redisUtil.zRemove(RedisCommonKeyEnum.TRAIN_ALLHBINFOPM.getKey(),JSON.toJSONString(grksglDto_sort));
                            //有就金额累加
                            BigDecimal zje= new BigDecimal(grksglDto.getJe()).setScale(2, RoundingMode.HALF_UP);
                            BigDecimal je= new BigDecimal(grksglDto_sort.getJe()).setScale(2, RoundingMode.HALF_UP);
                            grksglDto_sort.setJe(String.valueOf(zje.add(je)));
                            //再重新加入
                            redisUtil.zIncrementScore(RedisCommonKeyEnum.TRAIN_ALLHBINFOPM.getKey(),JSON.toJSONString(grksglDto_sort),Double.parseDouble(grksglDto_sort.getJe()),RedisCommonKeyEnum.TRAIN_ALLHBINFOPM.getExpire());
                            break;
                        }
                    }
                    if (flag){
                        GrksglDto grksglDto_top = new GrksglDto();
                        grksglDto_top.setJe(grksglDto.getJe());
                        grksglDto_top.setXm(hbszDto.getLrrymc());
                        grksglDto_top.setDdtxlj(hbszDto.getDdtxlj());
                        grksglDto_top.setRyid(hbszDto.getLrry());
                        redisUtil.zIncrementScore(RedisCommonKeyEnum.TRAIN_ALLHBINFOPM.getKey(),JSON.toJSONString(grksglDto_top),Double.parseDouble(grksglDto_top.getJe()),RedisCommonKeyEnum.TRAIN_ALLHBINFOPM.getExpire());
                    }
                } else {
                    GrksglDto grksglDto_top = new GrksglDto();
                    grksglDto_top.setJe(grksglDto.getJe());
                    grksglDto_top.setXm(hbszDto.getLrrymc());
                    grksglDto_top.setDdtxlj(hbszDto.getDdtxlj());
                    grksglDto_top.setRyid(hbszDto.getLrry());
                    redisUtil.zIncrementScore(RedisCommonKeyEnum.TRAIN_ALLHBINFOPM.getKey(),JSON.toJSONString(grksglDto_top),Double.parseDouble(grksglDto_top.getJe()),RedisCommonKeyEnum.TRAIN_ALLHBINFOPM.getExpire());
                }
                break;
            }
        }
        //显示红包页面
        map.put("result",true);
        map.put("sysl",1);
        return map;
    }

    /**
     * 定时将Redis的数据同步到数据库
     */
    public void synchronizeRedPacketSettingInfo() {
        HbszDto hbszDto = new HbszDto();
        List<HbszDto> dtoList = dao.getDtoListWithPx(hbszDto);
        if (!CollectionUtils.isEmpty(dtoList)){
            List<HbszmxDto> hbszmxDtos = new ArrayList<>();
            for (HbszDto dto : dtoList) {
                Object hbszmxs = redisUtil.get(RedisCommonKeyEnum.TRAIN_HBSZMX.getKey() + dto.getHbszid());
                if (hbszmxs!=null){
                    hbszmxDtos.addAll(JSON.parseArray(String.valueOf(hbszmxs), HbszmxDto.class));
                }
            }
            log.error("---------------------synchronizeRedPacketSettingInfo-HBSZMX_：" + JSON.toJSONString(hbszmxDtos));
            if (!CollectionUtils.isEmpty(hbszmxDtos)){
                hbszmxService.updateHbszmxSysl(hbszmxDtos);
            }
        }
    }
}
