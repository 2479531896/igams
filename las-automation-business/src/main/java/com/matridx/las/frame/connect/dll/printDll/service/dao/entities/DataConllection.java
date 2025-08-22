package com.matridx.las.frame.connect.dll.printDll.service.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="DataConllection")
public class DataConllection{

    private BasicElement Version;

    private List<DataProject> DataProjects;

    public BasicElement getVersion() {
        return Version;
    }

    public void setVersion(BasicElement version) {
        Version = version;
    }

    public List<DataProject> getDataProjects() {
        return DataProjects;
    }

    public void setDataProjects(List<DataProject> dataProjects) {
        DataProjects = dataProjects;
    }

}
