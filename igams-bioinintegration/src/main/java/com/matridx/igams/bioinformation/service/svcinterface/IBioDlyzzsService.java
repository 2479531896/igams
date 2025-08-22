package com.matridx.igams.bioinformation.service.svcinterface;


import com.matridx.igams.bioinformation.dao.entities.BioDlyzzsDto;
import com.matridx.igams.bioinformation.dao.entities.BioDlyzzsModel;
import com.matridx.igams.common.service.BaseBasicService;

public interface IBioDlyzzsService extends BaseBasicService<BioDlyzzsDto, BioDlyzzsModel> {

    /**
     * 删除
     */
    boolean deleteDto(BioDlyzzsDto bioDlyzzsDto);
}
