package com.matridx.igams.storehouse.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.JcjyglDto;
import com.matridx.igams.storehouse.dao.entities.JcjyglModel;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IJcjyglDao extends BaseBasicDao<JcjyglDto, JcjyglModel>{

    /**
     * 查询已有流水号
     * @param prefix
     * @return
     */
    String getJydhSerial(String prefix);

    /**
     * 审核列表
     * @param
     * @return
     */
    List<JcjyglDto> getPagedAuditJcjyglDto(JcjyglDto jcjyglDto);
    List<JcjyglDto> getAuditListJcjyglDto(List<JcjyglDto> list);
    /**
     * 根据借用单号查询
     * @param
     * @return
     */
    List<JcjyglDto> getDtoByJydh(String jydh);

    /**
     * 根据ids查询
     * @param
     * @return
     */
    List<JcjyglDto> getDtoListByIds(JcjyglDto jcjyglDto);

    boolean deleteByIds(JcjyglDto jcjyglDto);

    List<JcjyglDto> getJcjyWithKh();

    List<JcjyglDto> getJcjyxxByDwid(JcjyglDto jcjyglDto);

    /**
     * 根据搜索条件获取导出条数
     * @param jcjyglDto
     * @return
     */
    int getCountForSearchExp(JcjyglDto jcjyglDto);

    /**
     * 从数据库分页获取导出数据
     * @param jcjyglDto
     * @return
     */
    List<JcjyglDto> getListForSearchExp(JcjyglDto jcjyglDto);

    /**
     * 从数据库分页获取导出数据
     * @param jcjyglDto
     * @return
     */
    List<JcjyglDto> getListForSelectExp(JcjyglDto jcjyglDto);

    boolean updateWlxx(JcjyglDto jcjyglDto);
    /**
     * 负责人设置保存
     */
    void updateFzrByYfzr(JcjyglDto jcjyglDto);

}
