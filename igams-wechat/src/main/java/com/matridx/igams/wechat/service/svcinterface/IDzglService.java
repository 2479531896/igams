package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.DzglDto;
import com.matridx.igams.wechat.dao.entities.DzglModel;

import java.util.List;

public interface IDzglService extends BaseBasicService<DzglDto, DzglModel> {

    /**
     * 根据用户ID获取地址列表
     * @param ryid
     * @return
     */
    List<DzglDto> getListByUser(String ryid);
//
//    /**
//     * 删除地址
//     * @param dzglDto
//     * @return
//     */
//    public boolean deletedz(DzglDto dzglDto);

    void cleardefault(String ryid);
    boolean changedefault(DzglDto dzglDto);
}
