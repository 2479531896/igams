package com.matridx.igams.detection.molecule.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.detection.molecule.dao.entities.FzjcjgDto;
import com.matridx.igams.detection.molecule.dao.entities.FzjcjgModel;

import java.util.List;
import java.util.Map;

public interface IFzjcjgPJService extends BaseBasicService<FzjcjgDto, FzjcjgModel>{

    /**
     * 添加分子检测结果信息
     */
     boolean addDtoList(List<FzjcjgDto> list);

    /**
     * 根据分子检测IDs删除结果信息
     */
     boolean delDtoListByFzxmid(FzjcjgDto fzjcjgDto);

    /**
     * 根据分子项目id查询结果信息
     */
     List<Map<String, Object>> getFzjcjgListById(String fzxmid);
    /**
     * 批量更新
     */
     int updateList(List<FzjcjgDto> list);
    /**
     * 批量新增
     */
     int insertList(List<FzjcjgDto> list);
    /**
     * 附件上传保存
     */
    boolean uploadSave(FzjcjgDto fzjcjgDto);
}
