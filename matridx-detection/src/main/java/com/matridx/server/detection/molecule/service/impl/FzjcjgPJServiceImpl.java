package com.matridx.server.detection.molecule.service.impl;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.server.detection.molecule.dao.entities.FzjcjgDto;
import com.matridx.server.detection.molecule.dao.entities.FzjcjgModel;
import com.matridx.server.detection.molecule.dao.post.IFzjcjgPJDao;
import com.matridx.server.detection.molecule.service.svcinterface.IFzjcjgPJService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class FzjcjgPJServiceImpl extends BaseBasicServiceImpl<FzjcjgDto, FzjcjgModel, IFzjcjgPJDao> implements IFzjcjgPJService {

    /**
     * 添加分子检测结果信息
     */
    public boolean addDtoList(List<FzjcjgDto> list){
        return dao.addDtoList(list);
    }

    /**
     * 根据分子检测IDs删除结果信息
     */
    public boolean delDtoListByFzxmid(FzjcjgDto fzjcjgDto){
        return dao.delDtoListByFzxmid(fzjcjgDto);
    }
    /**
     * 根据分子检测id查询结果信息
     */
    public List<Map<String, Object>> getFzjcjgListById(String fzjcid){
        return dao.getFzjcjgListById(fzjcid);
    }
    /**
     * 批量更新
     */
    public int updateList(List<FzjcjgDto> list){
        return dao.updateList(list);
    }

    /**
     * 批量新增
     */
    public int insertList(List<FzjcjgDto> list){
        return dao.insertList(list);
    }
}
