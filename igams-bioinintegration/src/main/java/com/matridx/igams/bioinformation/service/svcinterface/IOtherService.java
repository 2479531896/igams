package com.matridx.igams.bioinformation.service.svcinterface;

import com.matridx.igams.bioinformation.dao.entities.OtherDto;
import com.matridx.igams.bioinformation.dao.entities.OtherModel;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;
import java.util.Map;


public interface IOtherService extends BaseBasicService<OtherDto, OtherModel>{

    /**
     * 查找送检信息
     */
    OtherDto getSjxxDto(OtherDto otherDto);

    List<OtherDto>getSjxxDtoList(OtherDto otherDto);
    /**
     * 查找用户信息
     */
    Map<String,Object> getReviewUserInfo(OtherDto otherDto);
    /**
     * 查询角色检测单位限制
     */
    List<Map<String,String>> getJsjcdwByjsid(String jsid);
    /**
     * 通过用户id查询对应的伙伴id
     */
    List<String> getHbidByYhid(String yhid);
    /**
     * 通过hbid查询 合作伙伴
     */
    List<String> getHbmcByHbid(List<String> list);

    /**
     * 从Dto里把数据放到Map里，减少Dto的属性设置，防止JSON出错
     */
    Map<String,Object> pareMapFromDto(OtherDto otherDto);
    /**
     * 查询所有送检信息(优化)
     */
    List<OtherDto> getDtoListOptimize(Map<String,Object> params);
    /**
     * 查找用户信息
     */
    OtherDto getXtyhByYhid(String yhid);
    /**
     * 获取资源操作权限
     */
    List<OtherDto> getJszyczb(OtherDto otherDto);

    /***
     * 更新wksjmx得onco时间和mngs时间
     */
    boolean updateWksjmx(OtherDto otherDto);
    /***
     * 批量更新wksjmx得onco时间和mngs时间
     */
    boolean updateWksjmxList(List<OtherDto>list);

    List<Map<String,String>> getWksjmxByWkbmOrYbbh(OtherDto otherDto);

    /**
     * 根据物种id查询致病级别
     * @param map
     * @return
     */
    List<Map<String,Object>> getwzzbx(Map<String,String> map);

    /**
     * 批量新增mrzk
     * @param list
     * @return
     */
    boolean batchInsertMrzk(List<Map<String,String>> list);

    /**
     * 获取文库日期
     * @param wkbm
     * @return
     */
    Map<String,String> getWkgl(String wkbm);
}
