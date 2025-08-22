package com.matridx.server.detection.molecule.service.impl;

import com.matridx.server.detection.molecule.dao.entities.FzjcjgModel;
import com.matridx.server.detection.molecule.dao.post.IFzjcjgDao;
import com.matridx.server.detection.molecule.service.svcinterface.IFzjcjgService;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.server.detection.molecule.dao.entities.FzjcjgDto;

import java.util.List;

@Service
public class FzjcjgServiceImpl extends BaseBasicServiceImpl<FzjcjgDto, FzjcjgModel, IFzjcjgDao> implements IFzjcjgService {

    /**
     * 添加分子检测结果信息
     * @param list
     * @return
     */
    public boolean addDtoList(List<FzjcjgDto> list){
        return dao.addDtoList(list);
    }

    /**
     * 根据分子检测IDs删除结果信息
     * @param fzjcjgDto
     * @return
     */
    public boolean delDtoListByIds(FzjcjgDto fzjcjgDto){
        return dao.delDtoListByIds(fzjcjgDto);
    }

    /**
     * 根据分子检测ID查询结果信息
     * @param list
     * @return
     */
    public List<FzjcjgDto> getDtosList(List<String> list){
        return dao.getDtosList(list);
    }
}
