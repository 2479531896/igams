package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.FjsqDto;
import com.matridx.igams.wechat.dao.entities.SjsyglDto;
import com.matridx.igams.wechat.dao.entities.SjsyglModel;
import com.matridx.igams.wechat.dao.entities.SjxxDto;

import java.util.List;
import java.util.Map;


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
     * 直接删除
     * @param sjsyglDto
     * @return
     */
    Boolean deleteInfo(SjsyglDto sjsyglDto);
    /**
     * 直接删除
     * @param sjsyglDto
     * @return
     */
    Boolean deleteInfoAjust(SjsyglDto sjsyglDto);

    /**
     * 删除多余数据
     * @param sjsyglDto
     * @return
     */
    Boolean updateQyrq(SjsyglDto sjsyglDto);


    /**
     * 批量插入
     * @param list
     * @return
     */
    Boolean insertList(List<SjsyglDto> list);

    /**
     * 获取插入信息
     * @param sjsyglDto
     * @return
     */
    List<SjsyglDto> getInsertInfo(SjsyglDto sjsyglDto);

    /**
     * Rabbit同步
     * @param list
     * @return
     */
    void syncRabbitMsg(List<SjsyglDto> list);
    /**
     * 获取提取信息
     * @param sjsyglDto
     * @return
     */
    List<SjsyglDto> getTqInfoList(SjsyglDto sjsyglDto);

    /**
     * 样本实验列表调整功能
     * @param list
     */
    Boolean adjustUpdateList(List<SjsyglDto> list);
    /**
     * 获取文库信息
     * @param sjsyglDto
     * @return
     */
    List<SjsyglDto> getWkInfoList(SjsyglDto sjsyglDto);
    /**
     * 根据ids顺序获取数据
     * @param sjsyglDto
     * @return
     */
    List<SjsyglDto> getDtoListByTempSyglids(SjsyglDto sjsyglDto);

    /**
     * 获取插入数据
     */
    List<SjsyglDto> getDetectionInfo(SjxxDto sjxxDto, FjsqDto fjsqDto, String lx);
    /**
     * 获取插入数据
     */
    List<SjsyglDto> getDealDetectionInfo(SjxxDto sjxxDto, String lx);

    /**
     * 获取伙伴信息
     * @param sjsyglDto
     * @return
     */
    List<SjsyglDto> getHbDto(SjsyglDto sjsyglDto);

    /**
     * 根据实验管理IDs删除数据
     * @param sjsyglDto
     * @return
     */
    int deleteBySyglids(SjsyglDto sjsyglDto);

    /**
     * 根据业务IDs删除数据
     * @param sjsyglDto
     * @return
     */
    //int deleteByYwids(SjsyglDto sjsyglDto);
    /**
     * 批量修改整张表数据
     * @param
     * @return
     */
    int modAllList(List<SjsyglDto> list);
    /**
     * @description 修改检测时间
     * @param sjsyglDto
     * @return
     */
    boolean updateJcsj(SjsyglDto sjsyglDto);
    /**
     * 根据jcxm csdm和sjid获取数据
     * @param sjsyglDto
     * @return
     */
    SjsyglDto getRfs(SjsyglDto sjsyglDto);

    /**
     * 获取实验信息
     * @param sjsyglDto
     * @return
     */
    List<SjsyglDto> getInfoByNbbm(SjsyglDto sjsyglDto);

    /**
     * 更新实验日期
     * @param list
     * @return
     */
    Boolean updateSyList(List<SjsyglDto> list);


    /**
     * 获取销售展示数据
     * @param sjid
     * @return
     */
    List<SjsyglDto> getViewDetectData(String sjid);

    /**
     * 通过传入的实验管理ID获取对应sjid的所有实验管理数据
     * @param sjsyglDto
     * @return
     */
    List<Map<String,String>> getDtosBySjxxFromSyglid(SjsyglDto sjsyglDto);

    /**
     * 通过sjids获取送检实验管理数据
     * @param sjsyglDto
     * @return
     */
    List<SjsyglModel> getDtosBySjids(SjsyglDto sjsyglDto);
    /**
     * 更新接收信息
     * @param sjsyglDto
     * @return
     */
    boolean updateSfjs(SjsyglDto sjsyglDto);
    /**
     * 更新实验日期
     * @param sjsyglDto
     * @return
     */
    Boolean adjustSaveSjxx(SjsyglDto sjsyglDto);

    /**
     * 送检查看页面检测项目TAB获取实验信息
     * @param sjsyglDto
     * @return
     */
    List<SjsyglDto> getSyxxViewByYwid(SjsyglDto sjsyglDto);

    /**
     * 获取提取信息
     * @param map
     * @return
     */
    List<Map<String,Object>> getExtractInfo(Map<String,Object> map);

    /**
     * 收样确认更新
     * @param list
     * @return
     */
    Boolean updateConfirmList(List<SjsyglDto> list);

    /**
     * 根据条件更新相应接收日期和接收状态
     * @param sjsyglDto
     */
    void updateJsInfo(SjsyglDto sjsyglDto);

    /**
     * 根据送检ID获取相应的实验日期，并按照实验日期进行倒序排序
     * @param sjsyglDto
     * @return
     */
    List<SjsyglDto> getOrderSyrqDto(SjsyglDto sjsyglDto);

    /**
     * 送检的同时增加加测的时候，加测里的是否接收不会进行插入，所以需要在最后进行更新
     * @param sjsyglList
     * @return
     */
    boolean updateFjJsrq(List<SjsyglDto> sjsyglList);
}
