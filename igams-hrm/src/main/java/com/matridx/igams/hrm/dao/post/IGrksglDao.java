package com.matridx.igams.hrm.dao.post;


import com.matridx.igams.common.dao.BaseBasicDao;

import com.matridx.igams.hrm.dao.entities.GrksglDto;
import com.matridx.igams.hrm.dao.entities.GrksglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IGrksglDao extends BaseBasicDao<GrksglDto, GrksglModel> {
    /**
     * 获取考试清单
     */
    List<GrksglDto> getPersonalTests(GrksglDto grksglDto);
    /**
     * 更新分数
     */
    boolean updateGrksglDtos(List<GrksglDto> upGrksglDtos);
    /**
     * 未参加培训或考试未通过
     */
    List<GrksglDto> getPagedIncomplete(GrksglDto grksglDto);
    /**
     * 根据搜索条件获取导出条数
     */
    int getCountForSearchExp(GrksglDto grksglDto);

    /**
     * 从数据库分页获取导出数据
     */
    List<GrksglDto> getListForSearchExp(GrksglDto grksglDto);

    /**
     * 从数据库分页获取导出数据
     */
    List<GrksglDto> getListForSelectExp(GrksglDto grksglDto);
    /**
     * 考试未通过设置提醒
     */
    List<GrksglDto> remindInComplete(GrksglDto grksglDto);
    /**
     * 根据ddid和培训id获取信息
     */
    GrksglDto getDtoByPxidAndFzr(GrksglDto grksglDto);
    /**
     * 获取个人红包列表
     */
    List<GrksglDto> getPagedRedPacket(GrksglDto grksglDto);
    /**
     * 个人红包列表查看
     */
    GrksglDto getDtoRedPacketById(String  id);
    /**
     * 更新是否发放字段
     */
    boolean updateRedPacket(GrksglDto grksglDto);
    /**
     * @description 获取红包金额排行
     */
    List<GrksglDto> getRedPacketGroup(GrksglDto grksglDto);
    /**
     * @description 获取红包金额排名
     */
    GrksglDto getRedPacketGroupPm(GrksglDto grksglDto);
    /**
     * @description 获取是否领取过红包
     */
    GrksglDto getSfHaveRedpacket(GrksglDto grksglDto);
    /**
     * @description 获取是否有没领取过红包
     */
    GrksglDto getSfHaveRedpacketNo(GrksglDto grksglDto);

    GrksglDto getDtoByIdAndGzid(GrksglDto grksglDto);

    /**
     * 根据搜索条件获取导出条数
     */
    int getCountForSearchExpHb(GrksglDto grksglDto);

    /**
     * 从数据库分页获取导出数据
     */
    List<GrksglDto> getListForSearchExpHb(GrksglDto grksglDto);

    /**
     * 从数据库分页获取导出数据
     */
    List<GrksglDto> getListForSelectExpHb(GrksglDto grksglDto);
    /**
     * 获培训未通过的数据
     */
    List<GrksglDto> getIncompleteDtoList(GrksglDto grksglDto);
    /**
     * 获取个人红包总和
     */
    Double getRedPacketSum(GrksglDto grksglDto);
}
