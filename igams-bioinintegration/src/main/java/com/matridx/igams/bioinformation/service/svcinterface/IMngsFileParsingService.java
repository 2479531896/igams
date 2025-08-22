package com.matridx.igams.bioinformation.service.svcinterface;

import com.matridx.igams.bioinformation.dao.entities.ReceiveFileModel;
import com.matridx.igams.bioinformation.dao.entities.WkcxDto;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface IMngsFileParsingService {

    Map<String, Object> FileParseingByType(ReceiveFileModel receiveFileModel, String date) throws Exception;


    String findDate(String[] strings, String date,int index);

    Map<String,Object> getBlast(WkcxDto wkcxDto, HttpServletResponse response);
}
