package com.matridx.igams.bioinformation.service.impl;


import com.matridx.igams.bioinformation.dao.entities.DockerDto;
import com.matridx.igams.bioinformation.dao.entities.DockerModel;
import com.matridx.igams.bioinformation.dao.post.IDockerDao;
import com.matridx.igams.bioinformation.service.svcinterface.IDockerService;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DockerServiceImpl extends BaseBasicServiceImpl<DockerDto, DockerModel, IDockerDao> implements IDockerService {

    @Override
    public List<DockerDto> getEndDtoList(List<String> strings) {
        return dao.getEndDtoList(strings);
    }

    @Override
    public Boolean updateList(List<DockerDto> dockerDtos) {
        return dao.updateList(dockerDtos);
    }
}
