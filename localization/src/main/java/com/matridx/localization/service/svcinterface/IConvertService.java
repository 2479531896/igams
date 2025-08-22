package com.matridx.localization.service.svcinterface;

import java.util.List;
import java.util.Map;

/**
 * IConvertService
 *
 * @author LittleRedBean
 * @version 1.0
 * @date 2024/7/18 上午9:09
 */
public interface IConvertService {

    /**
     * 将传递进来的数据转成JSONList,并发送给主服务器
     * @return
     */
    Map<String,Object> convertJsonAndSendToServer(String flag, String db, String dwmc, String json, String filePath, String lineSplit);
}
