package com.matridx.server.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;

import com.matridx.server.wechat.dao.entities.SjsyglDto;
import com.matridx.server.wechat.dao.entities.SjsyglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ISjsyglDao extends BaseBasicDao<SjsyglDto, SjsyglModel>{

    /**
     * 获取插入信息
     * @param sjsyglDto
     * @return
     */
    List<SjsyglDto> getInsertInfo(SjsyglDto sjsyglDto);

    /**
     * 批量插入
     * @param list
     * @return
     */
    int insertList(List<SjsyglDto> list);

    /**
     * 批量修改数据
     * @param list
     * @return
     */
    int updateList(List<SjsyglDto> list);
    /**
     * 删除多余数据
     * @param sjsyglDto
     * @return
     */
    Boolean delInfo(SjsyglDto sjsyglDto);

    Boolean adjustUpdateList(List<SjsyglDto> list);

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
     * @param list
     * @return
     */
    int modAllList(List<SjsyglDto> list);
    /**
     * 获取销售展示数据
     * @param sjid
     * @return
     */
    List<SjsyglDto> getViewDetectData(String sjid);
    /**
     * 更新项目名称字段
     * @param sjsyglDto
     * @return
     */
    boolean updateXmmc(SjsyglDto sjsyglDto);

    /**
     * 更新删除标记
     * @param sjsyglDto
     * @return
     */
    boolean updateScbj(SjsyglDto sjsyglDto);

    /**
     * 根据sjid删除sygl信息
     * @param sjsyglDto
     * @return
     */
    boolean delBySjid(SjsyglDto sjsyglDto);
    /*
        修改送检实验接收信息
    */
    void updateJsInfo(SjsyglDto sjsyglDto);
}
