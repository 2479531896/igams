package com.matridx.igams.wechat.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.KhyhzcbzDto;
import com.matridx.igams.wechat.dao.entities.KhyhzcbzModel;

import java.util.List;
import java.util.Map;

@Mapper
public interface IKhyhzcbzDao extends BaseBasicDao<KhyhzcbzDto, KhyhzcbzModel>{

    boolean insertList(List<KhyhzcbzDto> list);

    boolean updateList(List<KhyhzcbzDto> list);

    boolean delInfo(KhyhzcbzDto khyhzcbzDto);

    List<KhyhzcbzDto> getYhzcByXmcss(KhyhzcbzDto khyhzcbzDto);

    List<KhyhzcbzDto> getYhzcByXmje(KhyhzcbzDto khyhzcbzDto);

    List<KhyhzcbzDto> getYhzcByDcsl(KhyhzcbzDto khyhzcbzDto);

    List<Map<String,Object>> getFinalListByXmcss(KhyhzcbzDto khyhzcbzDto);

}
