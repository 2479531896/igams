package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.HbxxzDto;
import com.matridx.igams.wechat.dao.entities.HbxxzModel;
import com.matridx.igams.wechat.dao.entities.SjhbxxDto;
import com.matridx.igams.wechat.dao.entities.SjxxDto;

import java.util.List;

public interface IHbxxzDao extends BaseBasicDao<HbxxzDto, HbxxzModel>{
	List<SjhbxxDto> getHbDtoFromId(SjxxDto sjxxDto);
	List<SjhbxxDto> getHbxzDtoByHbmc(String hbmc);

    HbxxzDto getSameDto(HbxxzDto hbxxzDto);
}
