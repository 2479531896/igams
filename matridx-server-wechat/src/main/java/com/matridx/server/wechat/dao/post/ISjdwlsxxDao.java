package com.matridx.server.wechat.dao.post;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.SjdwlsxxDto;
import com.matridx.server.wechat.dao.entities.SjdwlsxxModel;
import com.matridx.server.wechat.dao.entities.SjdwxxDto;

@Mapper
public interface ISjdwlsxxDao extends BaseBasicDao<SjdwlsxxDto, SjdwlsxxModel>{
	
	boolean insertLsxx(List<SjdwxxDto> lsdwList);
	

}
