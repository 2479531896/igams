package com.matridx.igams.bioinformation.service.svcinterface;

import com.matridx.igams.bioinformation.dao.entities.WkcxbbDto;
import com.matridx.igams.bioinformation.dao.entities.WkcxbbModel;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;


public interface IWkcxbbService extends BaseBasicService<WkcxbbDto, WkcxbbModel>{

    /**
     * 通过版本号和文库编号获取文库版本信息
     */
    WkcxbbDto getDtoByBbhAndWkbh(WkcxbbDto wkcxbbDto);
    /**
     * 获取报告导出
     */
    WkcxbbDto getDtoByJgid(WkcxbbDto wkcxbbDto);
    /**
     * 查找最大最小值
     */
    WkcxbbDto getWzqzfw(WkcxbbDto wkcxbbDto);
    /**
     * 查找送检表
     */
    WkcxbbDto getSjxx(WkcxbbDto wkcxbbDto);
    /**
     * 添加物种Q值范围
     */
    boolean addWzqf(List<WkcxbbDto> list);

    WkcxbbDto getZxbbDto(WkcxbbDto wkcxbbDto);

    WkcxbbDto getZxbbDtoByXpAndWKbh(WkcxbbDto wkcxbbDto);
}
