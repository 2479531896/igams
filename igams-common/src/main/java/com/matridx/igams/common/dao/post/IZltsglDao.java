package com.matridx.igams.common.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.ZltsglDto;
import com.matridx.igams.common.dao.entities.ZltsglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IZltsglDao extends BaseBasicDao<ZltsglDto, ZltsglModel>{
    /**
     *  获取送检信息
     **/
    ZltsglDto getInspectionInfo(ZltsglDto zltsglDto);

    /**
     *  获取送检信息
     **/
    List<ZltsglDto> getItemSelectionInfo(ZltsglDto zltsglDto);
    /**
     *  结论
     **/
    boolean conclude(ZltsglDto zltsglDto);

}
