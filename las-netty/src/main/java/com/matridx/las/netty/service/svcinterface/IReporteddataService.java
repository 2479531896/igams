package com.matridx.las.netty.service.svcinterface;

import com.matridx.las.netty.dao.entities.YblcsjDto;

import java.util.List;

/**
 * 上报主站信息
 */

public interface IReporteddataService {
   public boolean reportedYblcsjList(List<YblcsjDto> list);

}
