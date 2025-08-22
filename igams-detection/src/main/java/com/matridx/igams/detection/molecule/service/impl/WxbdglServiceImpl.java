package com.matridx.igams.detection.molecule.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.detection.molecule.dao.entities.WxbdglDto;
import com.matridx.igams.detection.molecule.dao.entities.WxbdglModel;
import com.matridx.igams.detection.molecule.dao.post.IWxbdglDao;
import com.matridx.igams.detection.molecule.service.svcinterface.IHzxxService;
import com.matridx.igams.detection.molecule.service.svcinterface.IWxbdglService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WxbdglServiceImpl extends BaseBasicServiceImpl<WxbdglDto, WxbdglModel, IWxbdglDao> implements IWxbdglService {

    @Autowired
    IHzxxService hzxxtService;

    /**
     * 根据微信id获取绑定手机号
     */
    @Override
    public WxbdglDto getDtoListByWxid(String wxid) {
        return dao.getDtoListByWxid(wxid);
    }
    /**
     * 根据手机号获取绑定的微信id
     */
    @Override
    public WxbdglDto getDtoListBySjh(String sjh){
        return dao.getDtoListBySjh(sjh);
    }
    /**
     * 根据微信id修改绑定手机号
     */
    @Override
    public boolean updateSjhByWxid(WxbdglDto wxbdglDto){
        return dao.updateSjhByWxid(wxbdglDto);
    }
    /**
     * 新增绑定微信id、手机号
     */
    @Override
    public boolean insertSjhAndWxid(WxbdglDto wxbdglDto){
        return dao.insertSjhAndWxid(wxbdglDto);
    }

}
