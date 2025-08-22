package com.matridx.server.detection.molecule.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.enums.RabbitEnum;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.server.detection.molecule.dao.entities.WxbdglDto;
import com.matridx.server.detection.molecule.dao.entities.WxbdglModel;
import com.matridx.server.detection.molecule.dao.post.IWxbdglDao;
import com.matridx.server.detection.molecule.enums.DetMQTypeEnum;
import com.matridx.server.detection.molecule.service.svcinterface.IHzxxtService;
import com.matridx.server.detection.molecule.service.svcinterface.IWxbdglService;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class WxbdglServiceImpl extends BaseBasicServiceImpl<WxbdglDto, WxbdglModel, IWxbdglDao> implements IWxbdglService {

    @Autowired
    IHzxxtService hzxxtService;
    @Autowired(required = false)
    private AmqpTemplate amqpTempl;

	private Logger log = LoggerFactory.getLogger(WxbdglServiceImpl.class);

    /**
     * 根据微信id获取绑定手机号
     *
     * @param wxid
     * @return
     */
    @Override
    public WxbdglDto getDtoListByWxid(String wxid) {
        return dao.getDtoListByWxid(wxid);
    }
    /**
     * 根据手机号获取绑定的微信id
     * @param sjh
     * @return
     */
    @Override
    public WxbdglDto getDtoListBySjh(String sjh){
        return dao.getDtoListBySjh(sjh);
    }
    /**
     * 根据微信id修改绑定手机号
     * @return
     */
    @Override
    public boolean updateSjhByWxid(WxbdglDto wxbdglDto){
        return dao.updateSjhByWxid(wxbdglDto);
    }
    /**
     * 新增绑定微信id、手机号
     * @return
     */
    @Override
    public boolean insertSjhAndWxid(WxbdglDto wxbdglDto){
        return dao.insertSjhAndWxid(wxbdglDto);
    }
    /**
     * 手机号绑定
     * @param wxid
     * @param sjh
     * @return
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public Map<String,Object> phoneBinding(String wxid, String sjh){
        Map<String,Object> map = new HashMap<>();
        DBEncrypt dbEncrypt = new DBEncrypt();
        sjh = dbEncrypt.eCode(sjh);
        //查询该手机号是否已被绑定
        WxbdglDto dtoListBySjh = getDtoListBySjh(sjh);
        //若已被绑定
        if (dtoListBySjh!=null) {
            if (wxid.equals(dtoListBySjh.getWxid())){
                map.put("status","fail");
                map.put("message","您已绑定该手机，请勿重复绑定！");
                log.error("您已绑定该手机，请勿重复绑定！" + wxid + ":" + sjh);
                return map;
            }
            map.put("status","fail");
            map.put("message","该手机号已被绑定，请先解绑后再绑定！");
            return map;
        }
        WxbdglDto dtoListByWxid = getDtoListByWxid(wxid);
        WxbdglDto wxbdglDto = new WxbdglDto();
        wxbdglDto.setSjh(sjh);
        wxbdglDto.setWxid(wxid);
        boolean isSuccess = false;
        Map<String, Object> rabbitMap=new HashMap<String, Object>();
        rabbitMap.put("wxbdglDto",wxbdglDto);
        if (dtoListByWxid!=null) {
            isSuccess = updateSjhByWxid(wxbdglDto);
            amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECTION.getCode(), RabbitEnum.WXBD_MOD.getCode()+ JSONObject.toJSONString(rabbitMap));
        }else {
            isSuccess = insertSjhAndWxid(wxbdglDto);
            amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECTION.getCode(), RabbitEnum.WXBD_ADD.getCode()+ JSONObject.toJSONString(rabbitMap));
        }
        map.put("status",isSuccess?"success":"fail");
        map.put("message",isSuccess?"绑定成功！":"绑定失败！");
        return map;
    }
}
