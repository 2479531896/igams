package com.matridx.igams.common.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.ZjglDto;
import com.matridx.igams.common.dao.entities.ZjglModel;

import java.util.List;

@Mapper
public interface IZjglDao extends BaseBasicDao<ZjglDto, ZjglModel>{
    /**
     * @description 获取所有组件
     */
    List<ZjglDto> getAllComponent();

    /**
     * @Description: 批量新增
     * @param list
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2025/4/22 15:28
     */
    boolean insertList(List<ZjglDto> list);

    /**
     * @Description: 生成组件id
     * @param zjglDto
     * @return java.lang.String
     * @Author: 郭祥杰
     * @Date: 2025/4/23 14:26
     */
    String generateZjid(ZjglDto zjglDto);
}
