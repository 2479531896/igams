package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.XxxjDto;
import com.matridx.igams.wechat.dao.entities.XxxjModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IXxxjDao extends BaseBasicDao<XxxjDto, XxxjModel> {
    /**
     * 新增信息小结
     * @param xxxjDto
     * @return
     */
     int insertDto (XxxjDto xxxjDto);

    /**
     * 根据id获取信息小结
     * @param xjid
     * @return
     */
    XxxjDto getDtoById(String xjid);

    /**
     * 根据id删除信息小结
     * @param xxxjDto
     * @return
     */
    int deleteById(XxxjDto xxxjDto);

    /**
     * 根据id修改信息小结
     * @param xxxjDto
     * @return
     */
    int updateDtoById(XxxjDto xxxjDto);
}
