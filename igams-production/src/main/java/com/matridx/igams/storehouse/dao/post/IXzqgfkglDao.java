package com.matridx.igams.storehouse.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.XzqgfkglDto;
import com.matridx.igams.storehouse.dao.entities.XzqgfkglModel;

import java.util.List;

@Mapper
public interface IXzqgfkglDao extends BaseBasicDao<XzqgfkglDto, XzqgfkglModel>{

    /**
     * 获取最大单号
     * @param prefix
     * @return
     */
    String getDjhSerial(String prefix);

    /**
     * 获取行政请购确认审核的ID列表
     * @param xzqgfkglDto
     * @return
     */
    List<XzqgfkglDto> getPagedAuditIdList(XzqgfkglDto xzqgfkglDto);

    /**
     * 根据行政请购付款审核的ID列表获取审核列表详细信息
     * @param xzqgfkglDtos
     * @return
     */
    List<XzqgfkglDto> getAuditListByIds(List<XzqgfkglDto> xzqgfkglDtos);

    /**
     * 更新行政请购付款钉钉实例ID
     * @param xzqgfkglDto
     */
    void updateDdslid(XzqgfkglDto xzqgfkglDto);

    /**
     * 根据钉钉实例ID获取付款信息
     * @param xzqgfkglDto
     * @return
     */
    XzqgfkglDto getDtoByDdslid(XzqgfkglDto xzqgfkglDto);

    /**
     * 根据yhid获取角色信息
     * @param yhid
     * @return
     */
    List<XzqgfkglDto> getSprjsBySprid(String yhid);

    //选中导出
    List<XzqgfkglDto> getListForSelectExp(XzqgfkglDto xzqgfkglDto);
    //搜索导出计算条数
    int getCountForSearchExp(XzqgfkglDto xzqgfkglDto);
    //搜索导出
    List<XzqgfkglDto> getListForSearchExp(XzqgfkglDto xzqgfkglDto);
}
