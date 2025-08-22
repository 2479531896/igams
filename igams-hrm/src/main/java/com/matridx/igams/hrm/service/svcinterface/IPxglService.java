package com.matridx.igams.hrm.service.svcinterface;


import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.PxglDto;
import com.matridx.igams.hrm.dao.entities.PxglModel;

import java.util.List;
import java.util.Map;


public interface IPxglService extends BaseBasicService<PxglDto, PxglModel> {
    /**
     * 根据gzid获取信息
     */
    PxglDto viewTrainTask(PxglDto pxglDto);
    /**
     * 根据pxid获取信息
     */
    PxglDto viewDetailedList(PxglDto pxglDto);
    /**
     * 插入培训信息
     */
    boolean insertPxglDto(PxglDto pxglDto) throws BusinessException;
    /**
     * 修改培训信息
     */
    boolean updatePxglDto(PxglDto pxglDto) throws BusinessException;

    /**
     * 根据用户Ids拿到去除部分人的list
     */
    List<User> getPagedDtoListOutByIds(PxglDto pxglDto);
    /**
     * 获取用户列表
     */
    List<User> getPagedDtoListXtyh(PxglDto pxglDto);


    /**
     * 递归增加部门
     */
    void addDepartment(String token,Long id,List<Long> bmlist);

    List<User> getListByYhms(PxglDto pxglDto);
    List<PxglDto> getDtoListByFzr(PxglDto pxglDto);
    /**
     * 审核列表
     */
    List<PxglDto> getPagedAuditTrain(PxglDto pxglDto);
    /**
     * 获取统计年份
     */
    List<PxglDto> getYearGroup();

    /**
     * 获取培训类别统计
     */
    List<PxglDto> getTypeStatis();
    /**
     * 获取培训个数统计
     */
    List<PxglDto> getCountStatis(PxglDto pxglDto);
    /**
     * 获取培训学习状态数量
     */
    Integer getTrainStausCount(PxglDto pxglDto);
    /**
     * 获取培训学习未学习数量
     */
    List<Map<String, Object>> getTrainStausGroup(PxglDto pxglDto);
    /**
     * 获取培训类别分组
     */
    List<Map<String, Object>> getTrainGroupLb(PxglDto pxglDto);
    /**
     * 获取人员过期培训标题s
     */
    List<String> getTrainGqPxBts(PxglDto pxglDto);

    /**
     * 获取所有员工
     */
    List<String> getAllUses(PxglDto pxglDto);
    /*
        获取培训档案
     */
    List<PxglDto> getTrainRecords(PxglDto pxglDto);
    /*
        培训生成签到码
     */
    boolean generatesignincodeSaveTrain(PxglDto pxglDto) throws BusinessException;
    /*
        钉钉培训签到
     */
    boolean signInTrainForDD(PxglDto pxglDto) throws BusinessException;
    /*
        培训结果保存
     */
    boolean modresultSaveTrain(PxglDto pxglDto);
    /*
        培训提醒统计
     */
    Map<String, Object> getTrainRemindStatis(PxglDto pxglDto);
}
