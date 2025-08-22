package com.matridx.igams.storehouse.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.JcghglDto;
import com.matridx.igams.storehouse.dao.entities.JcghglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface IJcghglDao extends BaseBasicDao<JcghglDto, JcghglModel>{
    /**
     * 审核列表
     * @param
     * @return
     */
    List<JcghglDto> getPagedAuditRepaid(JcghglDto jcghglDto);
    List<JcghglDto> getRepaidAuditList(List<JcghglDto> list);

    /**
     * 根据归还单号查询
     * @param
     * @return
     */
    List<JcghglDto> getDtoByGhdh(String ghdh);

    /**
     * 查询已有流水号
     * @param prefix
     * @return
     */
    String getGhdhSerial(String prefix);
    /**
     * 生成请验单号
     */
    String generteQydh(String prefix);

}
