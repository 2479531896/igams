package com.matridx.igams.hrm.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.PxtkglbDto;
import com.matridx.igams.hrm.dao.entities.PxtkglbModel;

import java.util.List;

public interface IPxtkglbService extends BaseBasicService<PxtkglbDto, PxtkglbModel> {

    /**
     * 通过pxid删除题库配置
     */
    void deleteByPxid(PxtkglbDto pxtkglbDto);

    /**
     * 通过pxid获取全部信息
     */
    List<PxtkglbDto> getListByPxid(PxtkglbDto pxtkglbDto);
}
