package com.matridx.server.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.XmsyglDto;
import com.matridx.server.wechat.dao.entities.XmsyglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IXmsyglDao extends BaseBasicDao<XmsyglDto, XmsyglModel>{

    /**
     * 删除
     * @param xmsyglDto
     * @return
     */
    int delInfo(XmsyglDto xmsyglDto);

    /**
     * 更新删除标记
     * @param xmsyglDto
     * @return
     */
    int modToNormal(XmsyglDto xmsyglDto);

    /**
     * del删除
     * @param xmsyglDto
     * @return
     */
    int deleteInfo(XmsyglDto xmsyglDto);

    /**
     * 批量插入
     * @param list
     * @return
     */
    int insertList(List<XmsyglDto> list);

    /**
     * 批量更新
     * @param list
     * @return
     */
    boolean updateList(List<XmsyglDto> list);
}
