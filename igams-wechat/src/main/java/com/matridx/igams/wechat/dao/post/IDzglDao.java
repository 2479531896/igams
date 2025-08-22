package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.DzglDto;
import com.matridx.igams.wechat.dao.entities.DzglModel;

import java.util.List;

public interface IDzglDao extends BaseBasicDao<DzglDto, DzglModel> {


    /**
     * 根据钉钉ID获取地址列表
     * @param ryid
     * @return
     */
    List<DzglDto> getListByUser(String ryid);

    /**
     * 清除所有默认标记
     * @param ryid
     */
    void cleardefault(String ryid);

    /**
     * 改变默认地址
     * @param dzglDto
     * @return
     */
    boolean changedefault(DzglDto dzglDto);


}
