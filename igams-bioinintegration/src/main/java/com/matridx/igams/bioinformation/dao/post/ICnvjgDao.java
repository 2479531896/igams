package com.matridx.igams.bioinformation.dao.post;

import com.matridx.igams.bioinformation.dao.entities.CnvjgDto;
import com.matridx.igams.bioinformation.dao.entities.CnvjgModel;
import com.matridx.igams.common.dao.BaseBasicDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ICnvjgDao extends BaseBasicDao<CnvjgDto, CnvjgModel> {
    /**
     * 批量新增
     */
    boolean insertList(List<CnvjgDto> list);

    /**
     * 批量更新
     */
    int updateList(List<CnvjgDto> list);
    /**
     * 根据文库编号获取数据
     */
    List<CnvjgDto> getListByWkbhs(CnvjgDto cnvjgDto);

    /**
     * 根据芯片id和wkcxid获取cnvjg
     */
    CnvjgDto getDtoByXpidAndWkcxId(CnvjgDto cnvjgDto);
    /**
     * 根据时间获取数据
     */
    List<CnvjgDto> getListByTime(String time);
    /**
     * 获取today的列表数据
     */
    List<CnvjgDto> getTodayList(CnvjgDto cnvjgDto);
    /**
     * 实验室列表数据
     */
    List<CnvjgDto> getPagedLaboratoryList(CnvjgDto cnvjgDto);

    /**
     * 根据内部编号获取cnvjg
     */
    List<CnvjgDto>  getCnvjgByNbbm(CnvjgDto cnvjgDto);

}
