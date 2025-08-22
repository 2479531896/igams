package com.matridx.igams.hrm.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;

import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.hrm.dao.entities.PxglDto;
import com.matridx.igams.hrm.dao.entities.PxglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IPxglDao extends BaseBasicDao<PxglDto, PxglModel> {
    /**
     * 根据gzid获取信息
     */
    PxglDto viewTrainTask(PxglDto pxglDto);
    /**
     * 根据pxid获取信息
     */
    PxglDto viewDetailedList(PxglDto pxglDto);

    /**
     * 根据用户Ids拿到去除部分人的list
     */
    List<User> getPagedDtoListOutByIds(PxglDto pxglDto);

    /**
     * 获取用户列表
     */
    List<User> getPagedDtoListXtyh(PxglDto pxglDto);


    List<User> getListByYhms(PxglDto pxglDto);
    List<PxglDto> getDtoListByFzr(PxglDto pxglDto);
    /**
     * 获取审核列表
     */
    List<PxglDto> getPagedAuditTrain(PxglDto pxglDto);
    /**
     * 审核列表
     */
    List<PxglDto> getAuditListTrain(List<PxglDto> list);

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
     * 从数据库分页获取导出数据
     */
    List<PxglDto> getListForSearchExp(PxglDto pxglDto);

    /**
     * 从数据库分页获取导出数据
     */
    List<PxglDto> getListForSelectExp(PxglDto pxglDto);
    /**
     * 根据搜索条件获取导出条数
     */
    int getCountForSearchExp(PxglDto pxglDto);
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
}
