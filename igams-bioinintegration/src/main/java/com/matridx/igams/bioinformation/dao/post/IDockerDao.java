package com.matridx.igams.bioinformation.dao.post;

import com.matridx.igams.bioinformation.dao.entities.DockerDto;
import com.matridx.igams.bioinformation.dao.entities.DockerModel;
import com.matridx.igams.common.dao.BaseBasicDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface IDockerDao extends BaseBasicDao<DockerDto, DockerModel> {

    /**
     * 获取容器
     */
    List<DockerDto> getEndDtoList(List<String> strings);



    /**
     * 修改任务状态
     */
    Boolean updateList(List<DockerDto> dockerDtos);
}
