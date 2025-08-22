package com.matridx.igams.detection.molecule.service.impl;

import com.matridx.igams.detection.molecule.dao.entities.FzjcsjModel;
import com.matridx.igams.detection.molecule.dao.post.IFzjcsjDao;
import com.matridx.igams.detection.molecule.service.svcinterface.IFzjcsjService;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.detection.molecule.dao.entities.FzjcsjDto;

import java.util.List;

@Service
public class FzjcsjServiceImpl extends BaseBasicServiceImpl<FzjcsjDto, FzjcsjModel, IFzjcsjDao> implements IFzjcsjService {

    @Override
    public Boolean insertDtoList(List<FzjcsjDto> list) {
        return dao.insertDtoList(list);
    }

    /**
     * 根据分子检测ID和分子项目ID删除检测数据
     */
    public boolean deleteByFzjcidsAndFzxmid(FzjcsjDto fzjcsjDto){
        return dao.deleteByFzjcidsAndFzxmid(fzjcsjDto);
    }

    /**
     * 根据分子检测ID和分子项目ID查询检测数据
     */
    public List<FzjcsjDto> getDtoByFzjcidAndFzxmid(FzjcsjDto fzjcsjDto){
        return dao.getDtoByFzjcidAndFzxmid(fzjcsjDto);
    }
}
