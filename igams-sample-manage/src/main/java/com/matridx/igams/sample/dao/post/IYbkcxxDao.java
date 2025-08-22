package com.matridx.igams.sample.dao.post;

import com.matridx.igams.sample.dao.entities.*;
import org.apache.ibatis.annotations.Mapper;
import com.matridx.igams.common.dao.BaseBasicDao;
import java.util.List;
import java.util.Map;

@Mapper
public interface IYbkcxxDao extends BaseBasicDao<YbkcxxDto, YbkcxxModel>{
    /**
     * 根据搜索条件获取导出条数
     */
    int getCountForSearchExp(YbkcxxDto ybkcxxDto);
    /**
     * 从数据库分页获取导出数据
     */
    List<YbkcxxDto> getListForSearchExp(YbkcxxDto ybkcxxDto);

    /**
     * 从数据库分页获取导出数据
     */
    List<YbkcxxDto> getListForSelectExp(YbkcxxDto ybkcxxDto);

    //通过盒子id查询位置list
    List<YbkcxxDto> getWzListByHzid(String hzid);

    boolean insertYbkcxxDtos(List<YbkcxxDto> ybkcxxDtos);

    /**
     * 通过样本库存id获取样本明细
     */
    YbkcxxDto queryYbmxByYbkcid(YbkcxxDto ybkcxxDto);
    //样本编号模糊查询库存
    List<YbkcxxDto> getYbkcxxByNbbm(String nbbm);

    boolean updateYdbj(YbkcxxDto ybkcxxDto);

    boolean deleteListForCK(YbkcxxDto ybkcxxDto);
    //样本内部编码查送检信息
    YbkcxxDto getSjxxByNbbm(String nbbm);
    /**
     * 样本库存查看
     */
    List<YbkcxxDto> getPagedDtoYds(YbkcxxDto ybkcxxDto);
    /**
     * 获取单位限制
     */
    List<Map<String, String>> getJsjcdwByjsid(String dqjs);

    /**
     * 根据盒子id修改预定标记
     * @param ybkcxxDto
     * @return
     */
    boolean updateYdbjByHzids(YbkcxxDto ybkcxxDto);

    /**
     * 根据盒子id恢复预定标记
     * @param ybkcxxDto
     * @return
     */
    boolean restoreYdbjByHzids(YbkcxxDto ybkcxxDto);
    /**
     * 根据盒子id获取样本信息
     * @param ybkcxxDto
     * @return
     */
    List<YbkcxxDto> getSampleInfoByHzid(YbkcxxDto ybkcxxDto);

    Map<String,Object> checkHzCanAllot(YbkcxxDto ybkcxxDto);
    /*
        修改盒子
     */
    boolean updateBox(YbkcxxDto ybkcxxDto);
    /*
           更新预定标记
        */
    boolean updateYdbjForDelete(YbkcxxDto ybkcxxDto);
    /*
        更新删除标记
    */
    boolean updateScbjForDelete(YbkcxxDto ybkcxxDto);
     /*
        更新调拨明细id
    */
    boolean updateByDbmx(YbkcxxDto ybkcxxDto);
    /*
        调拨单删除更新调出盒子预定标记
    */
    boolean deleteBydbmx(YbdbDto ybdbDto);
    /*
        调拨单删除更新调出盒子预定标记
    */
    boolean updateYbkcxx(YbdbDto ybdbDto);


}
