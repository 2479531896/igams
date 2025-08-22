package com.matridx.igams.bioinformation.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.bioinformation.dao.entities.CnvjgxqDto;
import com.matridx.igams.bioinformation.dao.entities.CnvjgxqModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ICnvjgxqDao extends BaseBasicDao<CnvjgxqDto, CnvjgxqModel> {
    /**
     * 根据id获取数据
     */
    List<CnvjgxqDto> getListById(CnvjgxqDto cnvjgxqDto);
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
