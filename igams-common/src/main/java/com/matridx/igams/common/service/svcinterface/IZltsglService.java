package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.dao.entities.ZltsglDto;
import com.matridx.igams.common.dao.entities.ZltsglModel;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;

public interface IZltsglService extends BaseBasicService<ZltsglDto, ZltsglModel> {

    /**
     *  获取送检信息
     **/
    ZltsglDto getInspectionInfo(ZltsglDto zltsglDto);

    /**
     *  新增
     **/
    boolean addSaveQualityComplaints(ZltsglDto zltsglDto);
    /**
     *  修改
     **/
    boolean modSaveQualityComplaints(ZltsglDto zltsglDto);
    /**
     *  修改
     **/
    boolean delQualityComplaints(ZltsglDto zltsglDto);
    /**
     *  获取送检信息
     **/
    List<ZltsglDto> getItemSelectionInfo(ZltsglDto zltsglDto);
    /**
     *  结论
     **/
    boolean concludeSaveQualityComplaints(ZltsglDto zltsglDto);

}
