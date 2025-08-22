package com.matridx.igams.experiment.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.experiment.dao.entities.KzglDto;
import com.matridx.igams.experiment.dao.entities.KzglModel;

import java.util.List;
import java.util.Map;

public interface IKzglService extends BaseBasicService<KzglDto, KzglModel>{


    /**
     * 获取检测单位限制
     */
    List<Map<String,String>> getJsjcdwByjsid(String jsid);

    /**
     * 查询当天已建立的扩增数量
     */
    KzglDto getCountByDay(KzglDto kzglDto);

    /**
     * 根据扩增名称查询扩增信息
     */
    KzglDto getDtoByKzmc(KzglDto kzglDto);

    /**
     * 删除
     */
    boolean deleteDto(KzglDto kzglDto);

}
