package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.FpglDto;
import com.matridx.igams.production.dao.entities.FpglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IFpglDao extends BaseBasicDao<FpglDto, FpglModel>{
    /**
     * 获取审核列表
     */
    List<FpglDto> getPagedAuditInvoice(FpglDto fpglDto);
    /**
     * 审核列表
     */
    List<FpglDto> getAuditListInvoice(List<FpglDto> list);

    /**
     * 验证发票代码和发票号
     */
    List<FpglDto> verifyDmAndFph(FpglDto fpglDto);

}
