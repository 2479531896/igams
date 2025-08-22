package com.matridx.igams.bioinformation.service.svcinterface;

import com.matridx.igams.bioinformation.dao.entities.DockerDto;
import com.matridx.igams.bioinformation.dao.entities.DockerModel;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;


public interface IDockerService extends BaseBasicService<DockerDto, DockerModel>{
    /**
     * 获取容器
     */
    List<DockerDto> getEndDtoList(List<String> strings);


    /**
     * 修改任务状态
     */
    Boolean updateList(List<DockerDto> dockerDtos);

}
