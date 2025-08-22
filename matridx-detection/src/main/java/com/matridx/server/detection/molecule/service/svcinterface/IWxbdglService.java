package com.matridx.server.detection.molecule.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.detection.molecule.dao.entities.WxbdglDto;
import com.matridx.server.detection.molecule.dao.entities.WxbdglModel;

import java.util.Map;

public interface IWxbdglService extends BaseBasicService<WxbdglDto, WxbdglModel>{
    /**
     * 根据微信id获取绑定手机号
     * @param wxid
     * @return
     */
    WxbdglDto getDtoListByWxid(String wxid);
    /**
     * 根据手机号获取绑定的微信id
     * @param sjh
     * @return
     */
    WxbdglDto getDtoListBySjh(String sjh);
    /**
     * 根据微信id修改绑定手机号
     * @return
     */
    boolean updateSjhByWxid(WxbdglDto wxbdglDto);
    /**
     * 新增绑定微信id、手机号
     * @return
     */
    boolean insertSjhAndWxid(WxbdglDto wxbdglDto);

    /**
     * 手机号绑定
     * @param wxid
     * @param sjh
     * @return
     */
    Map<String,Object> phoneBinding(String wxid, String sjh);
}
