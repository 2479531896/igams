package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.KhyhzcbzDto;
import com.matridx.igams.wechat.dao.entities.KhyhzcbzModel;

import java.util.List;
import java.util.Map;

public interface IKhyhzcbzService extends BaseBasicService<KhyhzcbzDto, KhyhzcbzModel>{

    boolean insertList(List<KhyhzcbzDto> list);

    boolean updateList(List<KhyhzcbzDto> list);

    boolean delInfo(KhyhzcbzDto khyhzcbzDto);

    List<KhyhzcbzDto> getYhzcByXmcss(KhyhzcbzDto khyhzcbzDto);

    List<KhyhzcbzDto> getYhzcByXmje(KhyhzcbzDto khyhzcbzDto);

    List<KhyhzcbzDto> getYhzcByDcsl(KhyhzcbzDto khyhzcbzDto);

    List<Map<String,Object>> getFinalListByXmcss(KhyhzcbzDto khyhzcbzDto);
}
