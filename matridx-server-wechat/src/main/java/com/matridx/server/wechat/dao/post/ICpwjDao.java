package com.matridx.server.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.CpwjDto;
import com.matridx.server.wechat.dao.entities.CpwjModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ICpwjDao extends BaseBasicDao<CpwjDto, CpwjModel>{
    /**
     * @description 通过版本号和产品代码获取产品说明书信息
     * @param cpwjDto
     * @return
     */
    CpwjDto getDtoByBbhAndCpdm(CpwjDto cpwjDto);
    /**
     * @description 通过版本号和产品代码修改产品说明书信息
     * @param cpwjDto
     * @return
     */
    boolean updateByBbhAndCpdm(CpwjDto cpwjDto);

    /**
     * @description 通过版本号和产品代码修改是否公开
     * @param cpwjDtos
     */
    void updateGkByBbhAndCpdm(List<CpwjDto> cpwjDtos);

    /**
     * @Description: 修改
     * @param cpwjDto
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2025/4/21 11:01
     */
    boolean updateCpwjDto(CpwjDto cpwjDto);
}
