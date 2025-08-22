package com.matridx.server.detection.molecule.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.detection.molecule.dao.entities.WxbdglDto;
import com.matridx.server.detection.molecule.dao.entities.WxbdglModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IWxbdglDao extends BaseBasicDao<WxbdglDto, WxbdglModel>{
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
}
