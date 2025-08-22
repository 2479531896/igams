package com.matridx.igams.bioinformation.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.bioinformation.dao.entities.OtherDto;
import com.matridx.igams.bioinformation.dao.entities.OtherModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IOtherDao extends BaseBasicDao<OtherDto, OtherModel> {

    /**
     * 查找送检信息
     */
    OtherDto getSjxxDto(OtherDto otherDto);

    /***
     * 更新wksjmx得onco时间或者mngs时间
     */
     boolean updateWksjmx(OtherDto otherDto);

    /***
     * 批量更新wksjmx得onco时间或者mngs时间
     */
     boolean updateWksjmxList(List<OtherDto> list);
    /**
     * 查找用户信息
     */
    List<OtherDto> getListByYhm(OtherDto otherDto);

    List<OtherDto>getSjxxDtoList(OtherDto otherDto);
    /**
     * 查找送检验证信息
     */
    List<OtherDto> getSjyzDtoList(OtherDto otherDto);
    /**
     * 查询角色检测单位限制
     */
    List<Map<String, String>> getJsjcdwByjsid(String jsid);
    /**
     * 通过用户id查询对应的伙伴id
     */
    List<String> getHbidByYhid(String yhid);
    /**
     * 通过hbid查询 合作伙伴
     */
    List<String> getHbmcByHbid(List<String> list);
    /**
     * 优化送检列表查询(数量)
     */
    int getCountOptimize(Map<String,Object> params);
    /**
     * 优化送检列表查询(内部拆分)
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

    List<Map<String,String>> getWksjmxByWkbmOrYbbh(OtherDto otherDto);


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
