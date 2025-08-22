package com.matridx.las.home.dao.post;


import com.matridx.igams.common.dao.BaseBasicDao;

import com.matridx.las.home.dao.entities.YqxxInfoDto;
import com.matridx.las.home.dao.entities.YqxxInfoModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IYqxxInfoDao extends BaseBasicDao<YqxxInfoDto, YqxxInfoModel> {

    /***
     * 添加仪器信息
     * @param list
     * @return
     */
    int insertYqxxInfo(List<YqxxInfoDto> list);

    /***
     * 查询信息
     * @param dto
     * @return
     */
    List<YqxxInfoDto> getYqxxList(YqxxInfoDto dto);

    /***
     * 根据id查询信息
     * @param dto
     * @return
     */
    YqxxInfoDto getDtoById(YqxxInfoDto dto);
    /***
     * 根据父区域id查询信息
     * @param dto
     * @return
     */
    YqxxInfoDto getDtoByfqyid(YqxxInfoDto dto);
    /***
     * 根据实验室id删除
     * @param dto
     * @return
     */
    int updateBySysId(YqxxInfoDto dto);

    /***
     * 根据id删除
     * @param dto
     * @return
     */
    int updateById(YqxxInfoDto dto);
}
