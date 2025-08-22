package com.matridx.igams.wechat.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.HtyhzcDto;
import com.matridx.igams.wechat.dao.entities.HtyhzcModel;

import java.util.List;

@Mapper
public interface IHtyhzcDao extends BaseBasicDao<HtyhzcDto, HtyhzcModel>{

    boolean insertList(List<HtyhzcDto> htyhzcDtos);

    boolean updateList(List<HtyhzcDto> htyhzcDtos);

    List<HtyhzcDto> getYhzcInfo(HtyhzcDto htyhzcDto);
}
