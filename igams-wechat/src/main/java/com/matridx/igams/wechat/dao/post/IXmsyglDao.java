package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.XmsyglDto;
import com.matridx.igams.wechat.dao.entities.XmsyglModel;
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
    int modToNormal(List<XmsyglDto> xmsyglDtos);

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
     
     /**
      * 根据业务IDs删除数据
      * @param xmsyglDto
      * @return
      */
     int deleteByYwids(XmsyglDto xmsyglDto);

    /**
     * 根据实验管理ids获取数据
     * @param xmsyglDto
     * @return
     */
     List<XmsyglDto> getDtoListBySyglids(XmsyglDto xmsyglDto);

    /**
     * 根据ywids获取数据
     * @param xmsyglDto
     * @return
     */
     List<XmsyglDto> getDtoListByYwids(XmsyglDto xmsyglDto);
    /**
     * 获取样本的信息对应信息
     * @param xmsyglDto
     * @return
     */
     List<XmsyglDto> getXmsyXXdyYwids(XmsyglDto xmsyglDto);
}
