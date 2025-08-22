package com.matridx.igams.sample.service.svcinterface;

import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.sample.dao.entities.*;
import java.util.List;
import java.util.Map;

public interface IYbkcxxService extends BaseBasicService<YbkcxxDto, YbkcxxModel> {

    //查询样本库存信息列表
    List<YbkcxxDto> getPagedDtoList(YbkcxxDto ybkcxxDto);

    /**
     * 根据搜索条件获取导出条数
     */
    int getCountForSearchExp(YbkcxxDto ybkcxxDto, Map<String, Object> params);


    //通过盒子id查询位置list
    List<YbkcxxDto> getWzListByHzid(String hzid);

    boolean insertYbkcxxDtos(List<YbkcxxDto> ybglDtos);

    /**
     * 通过样本库存id获取样本明细
     */
    YbkcxxDto queryYbmxByYbkcid(YbkcxxDto ybkcxxDto);
    /**
     * 通过内部编码模糊查询库存
     */
    List<YbkcxxDto> getYbkcxxByNbbm(String ybbh);

    boolean updateYdbj(YbkcxxDto ybkcxxDto);

    boolean deleteListForCK(YbkcxxDto ybkcxxDto);
    /**
     * 样本录入保存
     */
    boolean addsaveYbkcxx(YbkcxxDto ybkcxxDto, User user) throws BusinessException;
    /**
     * 样本领料车保存
     */
    boolean addSaveYbPickingCar(YbllglDto ybllglDto, User user) throws BusinessException;
    /*
    * 通过内部编码获取样本信息
    * */
    YbkcxxDto getSjxxByNbbm(String nbbm);
 /**
     * 样本库存修改保存
     */
    boolean modSaveYbkcxx(YbkcxxDto ybkcxxDto) throws BusinessException;
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
        样本修改盒子保存
     */
    boolean modboxSaveYbkcxx(YbkcxxDto ybkcxxDto) throws BusinessException;
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
