package com.matridx.crf.web.dao.post;

import com.matridx.crf.web.dao.entities.JnsjbgjlDto;
import com.matridx.crf.web.dao.entities.JnsjbgjlModel;
import com.matridx.igams.common.dao.BaseBasicDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface IJnsjbgjlDao extends BaseBasicDao<JnsjbgjlDto, JnsjbgjlModel> {
    //查看艰难梭菌报告记录，很多字段0/1/2等在sql里直接转为是/否等
    JnsjbgjlDto getViewDto(JnsjbgjlDto jnsjbgjlDto);

    JnsjbgjlDto getDtoById(JnsjbgjlDto dto);

    int updateDto(JnsjbgjlDto dto);

    int getSizeByblrzddw (JnsjbgjlDto dto);

    /**
     * 通过ids批量删除数据
     * @param jnsjbgjlDto
     * @return
     */
    boolean deletelistByIds(JnsjbgjlDto jnsjbgjlDto);

    /**
     *获取报告关联的患者信息
     * @param jnsjbgjlDto
     * @return
     */
    List<String> getJnsjhzids(JnsjbgjlDto jnsjbgjlDto);

    boolean extendUpdateJnsj(JnsjbgjlDto jnsjbgjlDto);

    List<JnsjbgjlDto> getListForSelectExp(JnsjbgjlDto jnsjbgjlDto);

    int getCountForSearchExp(JnsjbgjlDto jnsjbgjlDto);

    String generateBlrzbh(String cskz1);

}
