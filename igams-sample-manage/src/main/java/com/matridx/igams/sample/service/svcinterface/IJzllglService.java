package com.matridx.igams.sample.service.svcinterface;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.sample.dao.entities.JzllglDto;
import com.matridx.igams.sample.dao.entities.JzllglModel;
import java.util.List;

/**
 * {@code @author:JYK}
 */
public interface IJzllglService extends BaseBasicService<JzllglDto, JzllglModel> {
    /**
     *获取菌种领料审核信息
     */
    List<JzllglDto> getPagedAuditPick(JzllglDto jzllglDto);
    /**
     * 根据ids获取数据
     */
    List<JzllglDto> getDtoByIds(JzllglDto jzllglDto);
    /**
     * 自动生成领料单号
     */
    String generateLldh();
    /**
     * 新增保存
     */
    boolean addSaveGermPicking(JzllglDto jzllglDto);
    /**
     * 修改保存
     */
    boolean modSaveGermPicking(JzllglDto jzllglDto);
    /**
     * 出库保存
     */
    boolean deliverySaveGermPicking(JzllglDto jzllglDto);
    /**
     * 删除
     */
    boolean delGermPicking(JzllglDto jzllglDto);

}
