package com.matridx.las.home.service.svcinterface;


import com.matridx.las.home.dao.entities.YqxxInfoDto;

import java.util.List;
import java.util.Map;

public interface MaterialScienceInitService {

      Map<String, Object> initMaterial();

      Map<String, Object>initWlqMaterial();

      void initRedis(List<YqxxInfoDto> list);
      void fillupAuto(String deviceid);
}
