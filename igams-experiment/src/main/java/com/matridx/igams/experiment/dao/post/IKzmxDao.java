package com.matridx.igams.experiment.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.experiment.dao.entities.KzmxDto;
import com.matridx.igams.experiment.dao.entities.KzmxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface IKzmxDao extends BaseBasicDao<KzmxDto, KzmxModel>{

    /**
     * 验证是否存在相同syglid
     */
    KzmxDto verifySame(KzmxDto kzmxDto);

    /**
     * 根据内部编号查询送检ID
     */
    List<KzmxDto> getSjxxByNbbhs(KzmxDto kzmxDto);

    /**
     * 批量新增
     */
    boolean insertList(List<KzmxDto> list);

    /**
     * 根据kzid删除
     */
    boolean deleteInfo(KzmxDto kzmxDto);

    /**
     * 根据ids修改扩增时间
     */
    boolean updateKzsjByIds(KzmxDto kzmxDto);
    /**
     * 根据ids移除扩增时间
     */
    boolean removeKzsjByIds(KzmxDto kzmxDto);

}
