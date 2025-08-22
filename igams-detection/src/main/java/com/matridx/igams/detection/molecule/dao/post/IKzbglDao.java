package com.matridx.igams.detection.molecule.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.detection.molecule.dao.entities.KzbglDto;
import com.matridx.igams.detection.molecule.dao.entities.KzbglModel;

import java.util.List;
import java.util.Map;

@Mapper
public interface IKzbglDao extends BaseBasicDao<KzbglDto, KzbglModel>{

    /**
     * 通过角色ID获取角色检测单位
     */
    List<Map<String, String>> getJsjcdwByjsid(String dqjs);

    /**
     * 查询已有扩展编号
     */
    String getKzbhSerial(String prefix);
}
