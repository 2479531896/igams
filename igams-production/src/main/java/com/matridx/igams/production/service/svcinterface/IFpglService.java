package com.matridx.igams.production.service.svcinterface;


import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.FpglDto;
import com.matridx.igams.production.dao.entities.FpglModel;

import java.util.List;


public interface IFpglService extends BaseBasicService<FpglDto, FpglModel>{

    /**
     * 审核列表
     */
    List<FpglDto> getPagedAuditInvoice(FpglDto fpglDto);

    /**
     * 验证发票代码和发票号
     */
    List<FpglDto> verifyDmAndFph(FpglDto fpglDto);

    /**
     * 修改
     */
    boolean deleteDto(FpglDto fpglDto, User user);


}
