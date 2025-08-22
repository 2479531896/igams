package com.matridx.igams.hrm.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.hrm.dao.entities.YhkqxxDto;
import com.matridx.igams.hrm.dao.entities.YhkqxxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IYhkqxxDao extends BaseBasicDao<YhkqxxDto, YhkqxxModel> {

    /**
     * 根据kqid获取用户出勤信息
     */
    YhkqxxDto getKqxxByKqid(YhkqxxDto yhkqxxDto);
    /**
     * 根据ids删除信息
     */
    boolean delKqxx(YhkqxxDto yhkqxxDto);
    /**
     * 根据yhid和日期获取主键id
     */
    YhkqxxDto getKqid(YhkqxxDto yhkqxxDto);
    /**
     * 根据kqid获取信息
     */
    YhkqxxDto getByKqid(String kqid);

    /**
     * 从数据库分页获取导出数据
     */
    List<YhkqxxDto> getListForSelectExp(YhkqxxDto yhkqxxDto);
    /**
     * 根据搜索条件获取导出条数
     */
    int getCountForSearchExp(YhkqxxDto yhkqxxDto);
    /**
     * 从数据库分页获取导出数据
     */
    List<YhkqxxDto> getListForSearchExp(YhkqxxDto yhkqxxDto);
    /**
     * 更新出勤退勤信息
     */
    boolean updateSj(YhkqxxDto yhkqxxDto);
    /**
     * @description 批量新增用户考勤信息
     */
    void insertOrUpdateYhkqxxDtos(List<YhkqxxDto> yhkqxxDtos);
    /**
     * @description 用户考勤统计列表
     */
    List<YhkqxxDto> getPagedStatis(YhkqxxDto yhkqxxDto);
    /**
     * @description 个人用户考勤
     */
    List<YhkqxxDto> getDtoListByYh(YhkqxxDto yhkqxxDto);
    /**
     * @description 批量新增用户考勤信息(考勤组)
     */
    void insertOrUpdateList(List<YhkqxxDto> yhkqxxDtos);
    /**
     * @description 获取用户考勤统计信息
     */
    YhkqxxDto getDtoByYhAndRq(YhkqxxDto yhkqxxDto);
    /**
     * @description 批量新增或修改用户考勤信息(打卡时间)
     */
    void insertOrUpdateYhRecords(List<YhkqxxDto> yhkqxxDtos);
    /**
     * @description 获取补贴信息
     */
    List<YhkqxxDto> getPagedSubsidy(YhkqxxDto yhkqxxDto);
    /**
     * 从数据库分页获取导出数据
     */
    List<YhkqxxDto> getAttListForSelectExp(YhkqxxDto yhkqxxDto);
    /**
     * 根据搜索条件获取导出条数
     */
    int getAttCountForSearchExp(YhkqxxDto yhkqxxDto);
    /**
     * 从数据库分页获取导出数据
     */
    List<YhkqxxDto> getAttListForSearchExp(YhkqxxDto yhkqxxDto);
    /**
     * @description 批量新增或修改用户考勤信息(加班班次)
     */
    boolean insertOrUpdateYhWorkOvertime(List<YhkqxxDto> yhkqxxDtos);
    /**
     * 从数据库分页获取导出数据
     */
    List<YhkqxxDto> getDetailsListForSelectExp(YhkqxxDto yhkqxxDto);
    /**
     * 根据搜索条件获取导出条数
     */
    int getDetailsCountForSearchExp(YhkqxxDto yhkqxxDto);
    /**
     * 从数据库分页获取导出数据
     */
    List<YhkqxxDto> getDetailsListForSearchExp(YhkqxxDto yhkqxxDto);
}
