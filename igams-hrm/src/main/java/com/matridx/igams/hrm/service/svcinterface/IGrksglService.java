package com.matridx.igams.hrm.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.GrksglDto;
import com.matridx.igams.hrm.dao.entities.GrksglModel;


import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

public interface IGrksglService  extends BaseBasicService<GrksglDto, GrksglModel> {
    /**
     * 获取考试清单
     */
    List<GrksglDto> getPersonalTests(GrksglDto grksglDto);
    /**
     * 提交并判分
     */
    void submitScore(String str);
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
    int getCountForSearchExp(GrksglDto grksglDto, Map<String, Object> params);
    /**
     * 考试未通过设置提醒
     */
    boolean remindInComplete(GrksglDto grksglDto);
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
     * 培训提醒
     */
    boolean remandTrain(GrksglDto grksglDto);
    /*
        获取个人红包总金额
     */
    Double getRedPacketSum(GrksglDto grksglDto);
}
