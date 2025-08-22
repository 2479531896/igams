package com.matridx.igams.bioinformation.service.svcinterface;

import com.matridx.igams.bioinformation.dao.entities.CnvjgDto;
import com.matridx.igams.bioinformation.dao.entities.CnvjgModel;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;
import java.util.Map;

public interface ICnvjgService extends BaseBasicService<CnvjgDto, CnvjgModel>{
    /**
     * 获取today的列表数据
     */
    Map<String, Object> getTodayList(User user,CnvjgDto cnvjgDto);
    /**
     * 修改
     */
    boolean modSaveCnv(CnvjgDto cnvjgDto, User user);
    /**
     * 根据芯片id和wkcxid获取cnvjg
     */
    CnvjgDto getDtoByXpidAndWkcxId(CnvjgDto cnvjgDto);
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
     * 根据时间获取数据
     */
    List<CnvjgDto> getListByTime(String time);
    /**
     * 批量修改
     */
    boolean batchModSaveCnv(CnvjgDto cnvjgDto, User user);
    /**
     * 实验室列表数据
     */
    List<CnvjgDto> getPagedLaboratoryList(CnvjgDto cnvjgDto);
    String saveCnvClientFile(CnvjgDto cnvjgDto);

    List<CnvjgDto> getCnvjgByNbbm(CnvjgDto cnvjgDto);
}
