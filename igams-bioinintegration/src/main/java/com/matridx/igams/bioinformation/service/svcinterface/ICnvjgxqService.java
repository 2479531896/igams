package com.matridx.igams.bioinformation.service.svcinterface;

import com.matridx.igams.bioinformation.dao.entities.CnvjgxqDto;
import com.matridx.igams.bioinformation.dao.entities.CnvjgxqModel;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;

public interface ICnvjgxqService extends BaseBasicService<CnvjgxqDto, CnvjgxqModel>{
    /**
     * 根据id获取数据
     */
    List<CnvjgxqDto> getListById(CnvjgxqDto cnvjgxqDto);
    /**
     * 新增
     */
    boolean addSaveCnvDetail(CnvjgxqDto cnvjgxqDto);
    /**
     * 更新是否汇报字段
     */
    boolean updateSfhb(CnvjgxqDto cnvjgxqDto);
    /**
     * 还原是否汇报字段
     */
    boolean updateSfhbByCnvjgid(String cnvjgid);
    /**
     * 批量新增
     */
    boolean insertList(List<CnvjgxqDto> list);

    List<CnvjgxqDto>getAjyList(CnvjgxqDto cnvjgxqDto);

}
