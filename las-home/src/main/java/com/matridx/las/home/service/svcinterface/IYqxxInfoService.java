package com.matridx.las.home.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.las.home.dao.entities.YqxxInfoDto;
import com.matridx.las.home.dao.entities.YqxxInfoModel;

import java.util.List;
import java.util.Map;

public interface IYqxxInfoService extends BaseBasicService<YqxxInfoDto, YqxxInfoModel> {
    /***
     * 添加仪器信息
     * @param list
     * @return
     */
    boolean insertYqxxInfo(List<YqxxInfoDto> list,String userid);

    /***
     * 查询信息
     * @param dto
     * @return
     */
    List<YqxxInfoDto> getYqxxList(YqxxInfoDto dto);
    /***
     * 查询主键信息
     * @param dto
     * @return
     */
    List<YqxxInfoDto> getYqxxZjList(YqxxInfoDto dto);
    /***
     * 根据id查询信息
     * @param dto
     * @return
     */
    YqxxInfoDto getDtoById(YqxxInfoDto dto);

    /***
     * 根据实验室id删除
     * @param dto
     * @return
     */
    boolean updateBySysId(YqxxInfoDto dto);

    /***
     * 根据id删除
     * @param dto
     * @return
     */
    boolean updateById(YqxxInfoDto dto);

    /**
     * 建库仪通道号
     * @return
     */
    Map<String ,Object> getJkywzxxList();

}
