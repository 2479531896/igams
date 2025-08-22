package com.matridx.igams.bioinformation.service.svcinterface;


import com.matridx.igams.bioinformation.dao.entities.ReceiveFileModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface ICnvFileService {
    /***
     *
     * @param json 多个文件路径的json
     * @param data 接收时间
     */
    Map<String,Object> FileParseingByType(ReceiveFileModel json, String data) throws Exception;
    /**
     *
     * @param targetDirPath 存储MultipartFile文件的目标文件夹
     * @return 文件的存储的绝对路径
     */
    String saveMultipartFile(MultipartFile file, String targetDirPath);
}
