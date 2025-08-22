package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.XxxjDto;
import com.matridx.igams.wechat.dao.entities.XxxjModel;

public interface IXxxjService extends BaseBasicService<XxxjDto, XxxjModel> {

    /**
     * 新增信息小结
     * @param xxxjDto
     * @return
     */
    boolean insertDto (XxxjDto xxxjDto);

    /**
     * 根据id获取信息小结
     * @param xjid
     * @return
     */
    XxxjDto getDtoById(String xjid);

    /**
     * 根据id删除信息小结
     * @param xxxjDto
     * @return
     */
    boolean deleteById(XxxjDto xxxjDto);

    /**
     * 根据id修改信息小结
     * @param xxxjDto
     * @return
     */
    boolean updateDtoById(XxxjDto xxxjDto);


    boolean progressSaveXxxj(XxxjDto xxxjDto);
}
