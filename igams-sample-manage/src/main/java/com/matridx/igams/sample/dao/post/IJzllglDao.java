package com.matridx.igams.sample.dao.post;
import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.sample.dao.entities.JzllglDto;
import com.matridx.igams.sample.dao.entities.JzllglModel;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;


/**
 * {@code @author:JYK}
 */
@Mapper
public interface IJzllglDao extends BaseBasicDao<JzllglDto, JzllglModel> {
    /**
     *获取菌种领料审核信息
     */
    List<JzllglDto> getPagedAuditPick(JzllglDto jzllglDto);
    /**
     *获取菌种领料审核信息
     */
    List<JzllglDto> getAuditListPick(List<JzllglDto> list);
    /**
     * 根据ids获取数据
     */
    List<JzllglDto> getDtoByIds(JzllglDto jzllglDto);
    /**
     * 获取流水号
     */
    String getDjhSerial(String prefix);
}
