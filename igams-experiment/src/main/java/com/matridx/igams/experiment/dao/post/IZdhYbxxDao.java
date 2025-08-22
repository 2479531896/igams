package com.matridx.igams.experiment.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.experiment.dao.entities.ZdhYbxxDto;
import com.matridx.igams.experiment.dao.entities.ZdhYbxxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author : 郭祥杰
 * @date :
 */
@Mapper
public interface IZdhYbxxDao extends BaseBasicDao<ZdhYbxxDto, ZdhYbxxModel> {
    /***
     * @Description: 根据标本编号和状态!=80和样本架编号查询样本信息
     * @param ybxxDtoList
     * @return java.util.List<com.matridx.igams.experiment.dao.entities.ZdhYbxxDto>
     * @Author: 郭祥杰
     * @Date: 2024/6/6 15:07
     */
    List<ZdhYbxxDto> queryByYbxxDto(List<ZdhYbxxDto> ybxxDtoList);

    /**
     * @Description: 批量新增样本信息
     * @param ybxxDtoList
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/6 15:22
     */
    boolean insertYbxxDtoList(List<ZdhYbxxDto> ybxxDtoList);

    /**
     * @Description: 批量更新样本信息
     * @param ybxxDtoList
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/6 16:57
     */
    boolean updateYbxxDtoList(List<ZdhYbxxDto> ybxxDtoList);

    /**
     * @Description: 批量更新样本明细
     * @param ybxxDtoList
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/7 15:47
     */
    boolean insertYbmxDtoList(List<ZdhYbxxDto> ybxxDtoList);

    /**
     * @Description: 批量更新样本明细
     * @param ybxxDtoList
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/7 15:46
     */
    boolean updateYbmxDtoList(List<ZdhYbxxDto> ybxxDtoList);

    /**
     * @Description: 批量新增样本试剂信息
     * @param ybxxDtoList
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/7 15:49
     */
    boolean insertYbsjxxDtoList(List<ZdhYbxxDto> ybxxDtoList);

    /**
     * @Description: 查询样本明细信息
     * @param ybxxDtoList
     * @return java.util.List<com.matridx.igams.experiment.dao.entities.ZdhYbxxDto>
     * @Author: 郭祥杰
     * @Date: 2024/6/7 13:33
     */
    List<ZdhYbxxDto> queryYbmxDtoList(List<ZdhYbxxDto> ybxxDtoList);
}
