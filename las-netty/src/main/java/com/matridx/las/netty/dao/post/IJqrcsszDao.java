package com.matridx.las.netty.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;

import com.matridx.las.netty.dao.entities.JqrcsszDto;
import com.matridx.las.netty.dao.entities.JqrcsszModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IJqrcsszDao extends BaseBasicDao<JqrcsszDto, JqrcsszModel> {
    /***
     * 添加建库仪位置信息
     * @param dto
     * @return
     */
    public int saveJkywzsz (JqrcsszDto dto);

    /***
     * 根据主键修改建库仪位置信息
     * @param dto
     * @return
     */
    public int updateByWzszid (JqrcsszDto dto);

    /***
     * 获取所有建库仪位置信息
     * @return
     */
    public List<JqrcsszDto> getList();

    /***
     * 根据通道号获取建库仪位置信息
     * @param dto
     * @return
     */
    public JqrcsszDto getByTdh(JqrcsszDto dto);
}
