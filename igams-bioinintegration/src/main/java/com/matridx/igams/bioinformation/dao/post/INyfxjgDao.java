package com.matridx.igams.bioinformation.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.bioinformation.dao.entities.NyfxjgDto;
import com.matridx.igams.bioinformation.dao.entities.NyfxjgModel;

import java.util.List;

@Mapper
public interface INyfxjgDao extends BaseBasicDao<NyfxjgDto, NyfxjgModel>{

    /**
     * 批量插入耐药分析结果
     */
    boolean insertList(List<NyfxjgDto> nyfxjgDtoList);
    /**
     * 根据文库测序ID和版本号查询
     */
    List<NyfxjgDto> getListByWkcxid(NyfxjgDto nyfxjgDto);

    /**
     * 根据耐药基因查找数据
     */
    List<NyfxjgDto> getListByNyjy(NyfxjgDto nyfxjgDto);
    /**
     * 根据ids查找数据
     */
    List<NyfxjgDto> getDtoListByIds(NyfxjgDto nyfxjgDto);
    /**
     * 耐药结果导出
     */
    List<NyfxjgDto> getListForExp(NyfxjgDto nyfxjgDto);

}
