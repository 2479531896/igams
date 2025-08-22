package com.matridx.igams.detection.molecule.service.impl;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.detection.molecule.dao.entities.FzjcjgDto;
import com.matridx.igams.detection.molecule.dao.entities.FzjcjgModel;
import com.matridx.igams.detection.molecule.dao.post.IFzjcjgDao;
import com.matridx.igams.detection.molecule.service.svcinterface.IFzjcjgService;

import java.util.List;

@Service
public class FzjcjgServiceImpl extends BaseBasicServiceImpl<FzjcjgDto, FzjcjgModel, IFzjcjgDao> implements IFzjcjgService{

    /**
     * 添加分子检测结果信息
     */
    public boolean addDtoList(List<FzjcjgDto> list){
        return dao.addDtoList(list);
    }

    /**
     * 根据分子检测IDs删除结果信息
     */
    public boolean delDtoListByIds(FzjcjgDto fzjcjgDto){
        return dao.delDtoListByIds(fzjcjgDto);
    }

    /**
     * 根据分子检测ID查询结果信息
     */
    public List<FzjcjgDto> getDtosList(List<String> list){
        return dao.getDtosList(list);
    }

    /**
     * 根据分子检测ID和分子项目ID查询结果信息
     */
    public List<FzjcjgDto> getListByXmidAndJcid(FzjcjgDto fzjcjgDto){
        return dao.getListByXmidAndJcid(fzjcjgDto);
    }
}
