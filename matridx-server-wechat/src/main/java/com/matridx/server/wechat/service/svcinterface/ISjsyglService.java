package com.matridx.server.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.FjsqDto;
import com.matridx.server.wechat.dao.entities.SjsyglDto;
import com.matridx.server.wechat.dao.entities.SjsyglModel;
import com.matridx.server.wechat.dao.entities.SjxxDto;

import java.util.List;


public interface ISjsyglService extends BaseBasicService<SjsyglDto, SjsyglModel> {
    /**
     * 插入数据
     * @param sjsyglDto
     * @return
     */
    Boolean insertInfo(SjsyglDto sjsyglDto);

    /**
     * 批量修改数据
     * @param
     * @return
     */
    Boolean updateList(List<SjsyglDto> list);
    /**
     * 删除多余数据
     * @param sjsyglDto
     * @return
     */
    Boolean delInfo(SjsyglDto sjsyglDto);


    /**
     * 批量插入
     * @param list
     * @return
     */
    Boolean insertList(List<SjsyglDto> list);

    /**
     * 样本实验调整功能
     * @param list
     * @return
     */
    Boolean adjustUpdateList(List<SjsyglDto> list);

    /**
     * 获取插入数据
     */
    List<SjsyglDto> getDetectionInfo(SjxxDto sjxxDto, FjsqDto fjsqDto, String lx);

    /**
     * 直接删除
     * @param sjsyglDto
     * @return
     */
    Boolean deleteInfo(SjsyglDto sjsyglDto);

    /**
     * 获取伙伴信息
     * @param sjsyglDto
     * @return
     */
    List<SjsyglDto> getHbDto(SjsyglDto sjsyglDto);
    /**
     * 批量修改整张表数据
     * @param
     * @return
     */
    int modAllList(List<SjsyglDto> list);

    /**
     * Rabbit同步
     * @param list
     */
    void syncRabbitMsg(List<SjsyglDto> list);

    /**
     * 获取销售展示数据
     * @param sjid
     * @return
     */
    List<SjsyglDto> getViewDetectData(String sjid);
    /**
     * 样本实验调整功能
     * @param sjsyglDto
     * @return
     */
    boolean adjustSaveSjxx(SjsyglDto sjsyglDto);
    /*
        修改送检实验接收信息
     */
    void updateJsInfo(SjsyglDto sjsyglDto);
}
