package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.wechat.dao.entities.SjxxDto;

import java.util.Map;

public interface ISjnbbmService {
    /**
     * 内部编码相关处理
     * @param key
     * @param sjxxDto
     * @param infoMap
     * @return
     */
    Map<String, String> dealNbbmKeyMap(String key, SjxxDto sjxxDto, Map<String, String> infoMap);
}
