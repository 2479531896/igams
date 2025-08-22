package com.matridx.server.detection.molecule.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.server.detection.molecule.dao.entities.FzjcxmDto;
import com.matridx.server.detection.molecule.dao.entities.FzjcxmModel;
import com.matridx.server.detection.molecule.dao.post.IFzjcxmDao;
import com.matridx.server.detection.molecule.service.svcinterface.IFzjcxmService;
import org.springframework.stereotype.Service;

@Service
public class FzjcxmServiceImpl extends BaseBasicServiceImpl<FzjcxmDto, FzjcxmModel, IFzjcxmDao> implements IFzjcxmService{
    /*新增分子检测项目*/
    @Override
    public boolean insertFzjcxmDto(FzjcxmDto fzjcxmDto) {
        return dao.insertFzjcxmDto(fzjcxmDto);
    }

    /**
     * 根据分子检测信息的主键id删除关联的分子检测项目
     *
     * @param fzjcxmDto
     */
    @Override
    public boolean delFzjcxmByFzjcid(FzjcxmDto fzjcxmDto) {
        return dao.delFzjcxmByFzjcid(fzjcxmDto);
    }
}
