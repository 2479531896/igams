package com.matridx.igams.production.service.impl;

import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.matridx.igams.common.dao.entities.DdxxglDto;

import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.post.ICommonDao;
import com.matridx.igams.common.enums.DingMessageType;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;

import com.matridx.igams.production.dao.entities.PdjlDto;
import com.matridx.igams.production.dao.entities.ShfkdjDto;
import com.matridx.igams.production.dao.entities.ShfkdjModel;
import com.matridx.igams.production.dao.post.IShfkdjDao;
import com.matridx.igams.production.service.svcinterface.IPdjlService;
import com.matridx.igams.production.service.svcinterface.IShfkdjService;
import com.matridx.igams.storehouse.dao.entities.LlglDto;
import com.matridx.igams.storehouse.service.svcinterface.ILlglService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.igams.common.util.DingTalkUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;


/**
 * @author:JYK
 */
@Service
public class ShfkdjServiceImpl extends BaseBasicServiceImpl<ShfkdjDto, ShfkdjModel, IShfkdjDao> implements IShfkdjService {
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    ICommonDao commonDao;
    @Autowired
    IPdjlService pdjlService;
    @Autowired
    ILlglService llglService;
    @Autowired
    IXxglService xxglService;
    @Autowired
    IDdxxglService ddxxglService;
    @Autowired
    IJcsjService jcsjService;
    private final Logger logger = LoggerFactory.getLogger(ShfkdjServiceImpl.class);
    @Override
    public List<ShfkdjDto> getPagedListSaleFeedbackDingTalk(ShfkdjDto shfkdjDto) {
        return dao.getPagedListSaleFeedbackDingTalk(shfkdjDto);
    }

    @Override
    public List<Map<String,Object>> getShfkCljl(ShfkdjDto shfkdjDto) {
        List<Map<String,Object>> mapList=new ArrayList<>();
        List<ShfkdjDto> shfkdjDtoList=dao.getCljlList(shfkdjDto);
        if (shfkdjDtoList.size()<=1)
            return mapList;
        StringBuilder ids = new StringBuilder();
        for (ShfkdjDto dto : shfkdjDtoList) {
            String id=dto.getLlid();
            ids.append(",").append(id);

        }
        if (ids.length()>0){
            ids = new StringBuilder(ids.substring(1));
        }
        String[] split = ids.toString().split(",");
        List<String> list = Arrays.asList(split);
        List<LlglDto> llglDtos =llglService.getLldhByIds(list);
        for (ShfkdjDto dto : shfkdjDtoList) {
            String[] llids = dto.getLlid().split(",");
            List<Map<String, String>> lldhlist = new ArrayList<>();
            for (String llid : llids) {
                for (LlglDto llglDto : llglDtos) {
                    if (llid.equals(llglDto.getLlid())) {
                        Map<String, String> hashMap = new HashMap<>();
                        hashMap.put("llid", llid);
                        hashMap.put("lldh", llglDto.getLldh());
                        lldhlist.add(hashMap);
                    }
                }
            }
            Map<String, Object> map = new HashMap<>();
            String str = "负责人:" + dto.getFzrmc();
            String bz = "备注:" + dto.getBz();
            map.put("str", str);
            map.put("str2", dto.getLrsj());
            map.put("str3", dto.getLrtime());
            map.put("str4", lldhlist);
            map.put("bz", bz);
            mapList.add(map);
        }
        return mapList;
    }

    @Override
    public boolean updateJd(ShfkdjDto shfkdjDto) {
        return dao.updateJd(shfkdjDto);
    }

    /**
     * 新增保存
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean addSaveSaleFeedback(ShfkdjDto shfkdjDto){
        shfkdjDto.setShfkid(StringUtil.generateUUID());
        int insert = dao.insert(shfkdjDto);
        if(insert==0){
            return false;
        }
        ShfkdjDto dtoById = dao.getDtoById(shfkdjDto.getShfkid());
        List<DdxxglDto> ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.SALEFEED_BACK.getCode());
//        String token = talkUtil.getToken();
        if (!CollectionUtils.isEmpty(ddxxglDtolist)){
            for (DdxxglDto ddxxglDto : ddxxglDtolist) {
                if (StringUtil.isNotBlank(ddxxglDto.getYhm())){
                    String internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/index/sale/saleFeedBack/feedBack&qflb=all&wbfw=1", StandardCharsets.UTF_8);
                    logger.error("internalbtn:"+internalbtn);
                    List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
                    OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
                    btnJsonList.setTitle("小程序");
                    btnJsonList.setActionUrl(internalbtn);
                    btnJsonLists.add(btnJsonList);
                    talkUtil.sendCardMessageThread(ddxxglDto.getYhm(),
                            ddxxglDto.getDdid(),
                            xxglService.getMsg("ICOMM_FK00005"),
                            StringUtil.replaceMsg(xxglService.getMsg("ICOMM_FK00007"),dtoById.getYqmc(),dtoById.getXlh(),StringUtil.isNotBlank(dtoById.getKhmc())?dtoById.getKhmc():"",dtoById.getLxr(),StringUtil.isNotBlank(dtoById.getWtms())?dtoById.getWtms():"",StringUtil.isNotBlank(dtoById.getClyj())?dtoById.getClyj():"",dtoById.getDjry()),
                            btnJsonLists,"1"
                    );
                }
            }
        }
        return true;
    }

    /**
     * 修改保存
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean modSaveSaleFeedback(ShfkdjDto shfkdjDto){
        int update = dao.update(shfkdjDto);
        if(update==0){
            return false;
        }
        ShfkdjDto dtoById = dao.getDtoById(shfkdjDto.getShfkid());
        List<DdxxglDto> ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.SALEFEED_BACK.getCode());
//        String token = talkUtil.getToken();
        if (!CollectionUtils.isEmpty(ddxxglDtolist)){
            for (DdxxglDto ddxxglDto : ddxxglDtolist) {
                if (StringUtil.isNotBlank(ddxxglDto.getYhm())){
                    String internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/sale/saleFeedBack/feedBack&qflb=all&wbfw=1", StandardCharsets.UTF_8);
                    List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
                    OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
                    btnJsonList.setTitle("小程序");
                    btnJsonList.setActionUrl(internalbtn);
                    btnJsonLists.add(btnJsonList);
                    talkUtil.sendCardMessageThread(ddxxglDto.getYhm(),
                            ddxxglDto.getDdid(),
                            xxglService.getMsg("ICOMM_FK00006"),
                            StringUtil.replaceMsg(xxglService.getMsg("ICOMM_FK00008"),dtoById.getYqmc(),dtoById.getXlh(),StringUtil.isNotBlank(dtoById.getKhmc())?dtoById.getKhmc():"",dtoById.getLxr(),StringUtil.isNotBlank(dtoById.getWtms())?dtoById.getWtms():"",StringUtil.isNotBlank(dtoById.getClyj())?dtoById.getClyj():"",dtoById.getDjry()),
                            btnJsonLists,"1"
                    );
                }
            }
        }
        return true;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean sendSaleFeedbackMsg(ShfkdjDto shfkdjDto) throws BusinessException {
        shfkdjDto.setJd("1");
        boolean updateJd = dao.updateJd(shfkdjDto);
        if (!updateJd){
            throw new BusinessException("msg","更新售后反馈进度失败");
        }
        shfkdjDto.setFzr(shfkdjDto.getYhid());
        boolean updateFzr = dao.updateFzr(shfkdjDto);
        if (!updateFzr){
            throw new BusinessException("msg","更新售后反馈负责人失败");
        }
        PdjlDto pdjlDto = new PdjlDto();
        pdjlDto.setPdjlid(StringUtil.generateUUID());
        pdjlDto.setShfkid(shfkdjDto.getShfkid());
        pdjlDto.setPdryid(shfkdjDto.getYhid());
        pdjlDto.setPdzt("1");
        boolean insert = pdjlService.insert(pdjlDto);
        if (!insert){
            throw new BusinessException("msg","保存派单记录失败");
        }
        //派单需要给通知人员发送钉钉消息
//        String token = talkUtil.getToken();
        User user = new User();
        user.setYhid(shfkdjDto.getYhid());
        User userInfoById = commonDao.getUserInfoById(user);
        String ddid = null;
        String yhm = null;
        if (userInfoById!=null){
            shfkdjDto.setFzrmc(userInfoById.getZsxm());
            ddid = userInfoById.getDdid();
            yhm = userInfoById.getYhm();
        }
        ShfkdjDto dtoById = dao.getDtoById(shfkdjDto.getShfkid());
        if (StringUtil.isNotBlank(yhm)){
            String internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/sale/saleFeedBack/feedBack&qflb=signal&wbfw=1", StandardCharsets.UTF_8);
            List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
            OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
            btnJsonList.setTitle("小程序");
            btnJsonList.setActionUrl(internalbtn);
            btnJsonLists.add(btnJsonList);
            talkUtil.sendCardMessageThread(yhm,
                    ddid,
                    xxglService.getMsg("ICOMM_FK00001"),
                    StringUtil.replaceMsg(xxglService.getMsg("ICOMM_FK00003"),dtoById.getYqmc(),dtoById.getXlh(),StringUtil.isNotBlank(dtoById.getKhmc())?dtoById.getKhmc():"",dtoById.getLxr(),StringUtil.isNotBlank(dtoById.getWtms())?dtoById.getWtms():"",StringUtil.isNotBlank(dtoById.getClyj())?dtoById.getClyj():"",dtoById.getDjry(),StringUtil.isNotBlank(dtoById.getFzrmc())?dtoById.getFzrmc():""),
                    btnJsonLists,"1"
            );
        }else {
            throw new BusinessException("msg","钉钉id为空");
        }
        return true;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean sendOrdersSave(ShfkdjDto shfkdjDto) throws BusinessException{
        boolean updateFzr = dao.updateFzr(shfkdjDto);
        if (!updateFzr){
            throw new BusinessException("msg","更新售后反馈负责人失败");
        }
        shfkdjDto.setJd("1");
        boolean updateJd = dao.updateJd(shfkdjDto);
        if (!updateJd){
            throw new BusinessException("msg","更新售后反馈进度失败");
        }
        PdjlDto pdjlDto = new PdjlDto();
        pdjlDto.setPdjlid(StringUtil.generateUUID());
        pdjlDto.setShfkid(shfkdjDto.getShfkid());
        pdjlDto.setPdryid(shfkdjDto.getFzr());
        pdjlDto.setPdzt("1");
        boolean insert = pdjlService.insert(pdjlDto);
        if (!insert){
            throw new BusinessException("msg","保存派单记录失败");
        }
        //派单需要给通知人员发送钉钉消息
//        String token = talkUtil.getToken();
        User user = new User();
        user.setYhid(shfkdjDto.getFzr());
        User userInfoById = commonDao.getUserInfoById(user);
        String ddid = null;
        String yhm = null;
        if (userInfoById!=null){
            shfkdjDto.setFzrmc(userInfoById.getZsxm());
            ddid = userInfoById.getDdid();
            yhm = userInfoById.getYhm();
        }
        ShfkdjDto dtoById = dao.getDtoById(shfkdjDto.getShfkid());
        if (StringUtil.isNotBlank(yhm)){
            String internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/sale/saleFeedBack/feedBack&qflb=signal&wbfw=1", StandardCharsets.UTF_8);
            List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
            OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
            btnJsonList.setTitle("小程序");
            btnJsonList.setActionUrl(internalbtn);
            btnJsonLists.add(btnJsonList);
            talkUtil.sendCardMessageThread(yhm,
                    ddid,
                    xxglService.getMsg("ICOMM_FK00001"),
                    StringUtil.replaceMsg(xxglService.getMsg("ICOMM_FK00003"),dtoById.getYqmc(),dtoById.getXlh(),StringUtil.isNotBlank(dtoById.getKhmc())?dtoById.getKhmc():"",dtoById.getLxr(),StringUtil.isNotBlank(dtoById.getWtms())?dtoById.getWtms():"",StringUtil.isNotBlank(dtoById.getClyj())?dtoById.getClyj():"",dtoById.getDjry(),StringUtil.isNotBlank(dtoById.getFzrmc())?dtoById.getFzrmc():""),
                    btnJsonLists,"1"
            );
        }else {
            throw new BusinessException("msg","钉钉id为空");
        }
        return true;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean cancelSendOrders(ShfkdjDto shfkdjDto) throws BusinessException{
        //取消派单需要给通知人员发送钉钉消息
        ShfkdjDto dtoById = dao.getDtoById(shfkdjDto.getShfkid());
        shfkdjDto.setJd("0");
        boolean updateJd = dao.updateJd(shfkdjDto);
        if (!updateJd){
            throw new BusinessException("msg","更新售后反馈进度失败");
        }
        shfkdjDto.setFzr(null);
        boolean updateFzr = dao.updateFzr(shfkdjDto);
        if (!updateFzr){
            throw new BusinessException("msg","更新售后反馈负责人失败");
        }
        PdjlDto pdjlDto = new PdjlDto();
        pdjlDto.setShfkid(shfkdjDto.getShfkid());
        pdjlDto.setPdzt("0");
        boolean update = pdjlService.update(pdjlDto);
        if (!update){
            throw new BusinessException("msg","更新派单记录状态失败");
        }

        if (StringUtil.isNotBlank(dtoById.getFzryhm())){
            String internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/sale/saleFeedBack/feedBack&qflb=signal&wbfw=1", StandardCharsets.UTF_8);
            List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
            OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
            btnJsonList.setTitle("小程序");
            btnJsonList.setActionUrl(internalbtn);
            btnJsonLists.add(btnJsonList);
            talkUtil.sendCardMessageThread(dtoById.getFzryhm(),
                    dtoById.getFzrddid(),
                    xxglService.getMsg("ICOMM_FK00002"),
                    StringUtil.replaceMsg(xxglService.getMsg("ICOMM_FK00004"),dtoById.getYqmc(),dtoById.getXlh(),StringUtil.isNotBlank(dtoById.getKhmc())?dtoById.getKhmc():"",dtoById.getLxr(),StringUtil.isNotBlank(dtoById.getWtms())?dtoById.getWtms():"",StringUtil.isNotBlank(dtoById.getClyj())?dtoById.getClyj():"",dtoById.getDjry(),StringUtil.isNotBlank(dtoById.getFzrmc())?dtoById.getFzrmc():""),
                    btnJsonLists,"1"
            );
        }else {
            throw new BusinessException("msg","钉钉id为空");
        }
        return true;
    }



}
