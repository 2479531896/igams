package com.matridx.igams.experiment.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.experiment.dao.entities.KzglDto;
import com.matridx.igams.experiment.dao.entities.KzglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IKzglDao extends BaseBasicDao<KzglDto, KzglModel>{

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
     * 根据检测单位和创建日期查询信息
     */
    List<KzglDto> getListByJcdwAndLrsj(KzglDto kzglDto);


}
