package com.matridx.igams.web.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.web.dao.entities.YhzDto;
import com.matridx.igams.web.dao.entities.YhzModel;

import java.util.List;

@Mapper
public interface IYhzDao extends BaseBasicDao<YhzDto, YhzModel>{

    /**
     * 获取用户组成员信息
     * @param yhzDto
     * @return
     */
    List<YhzDto> getYhzxx(YhzDto yhzDto);

    /**
     * 查找当前用户私有的用户组
     * @param yhzDto
     * @return
     */
    List<String> getPrivateYhzList(YhzDto yhzDto);
}
