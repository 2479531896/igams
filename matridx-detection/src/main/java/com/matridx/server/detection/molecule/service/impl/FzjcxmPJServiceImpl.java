package com.matridx.server.detection.molecule.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.server.detection.molecule.dao.entities.FzjcxmDto;
import com.matridx.server.detection.molecule.dao.entities.FzjcxmModel;
import com.matridx.server.detection.molecule.dao.post.IFzjcxmPJDao;
import com.matridx.server.detection.molecule.service.svcinterface.IFzjcxmPJService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FzjcxmPJServiceImpl extends BaseBasicServiceImpl<FzjcxmDto, FzjcxmModel, IFzjcxmPJDao> implements IFzjcxmPJService {

    /**
     * 根据分子检测信息的主键id删除关联的分子检测项目
     *
     * @param fzjcxmDto
     */
    @Override
    public boolean delFzjcxmByFzjcid(FzjcxmDto fzjcxmDto) {
        return dao.delFzjcxmByFzjcid(fzjcxmDto);
    }

    /**
     * 成组插入分子检测项目
     */
    @Override
    public boolean insertList(List<FzjcxmDto> fzjcxmDtos){
        return dao.insertList(fzjcxmDtos);
    }

    /**
     * 根据分子检测id查询有关的检测项目信息
     */
    @Override
    public List<FzjcxmDto> getDtoListByFzjcid(String fzjcid){
        return dao.getDtoListByFzjcid(fzjcid);
    }

    @Override
    public void delFzjcxmByIds(FzjcxmDto fzjcxmDto) {
        dao.delFzjcxmByIds(fzjcxmDto);
    }

    @Override
    public void updateZt(FzjcxmDto fzjcxmDto) {
        dao.updateZt(fzjcxmDto);
    }

    @Override
    public void delFzjcxmByFzjc(FzjcxmDto fzjcxmDto) {
        dao.delFzjcxmByFzjc(fzjcxmDto);
    }
}
