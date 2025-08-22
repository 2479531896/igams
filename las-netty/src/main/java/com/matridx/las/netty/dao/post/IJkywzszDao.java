package com.matridx.las.netty.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.las.netty.dao.entities.JkywzszDto;
import com.matridx.las.netty.dao.entities.JkywzszModel;

import java.util.List;

@Mapper
public interface IJkywzszDao extends BaseBasicDao<JkywzszDto, JkywzszModel> {
	JkywzszDto queryById(String jkytdh);
	int insertList(List<JkywzszDto> list);
	int updateTdhById(JkywzszDto jkywzszDto);
	List<String> getTdhList(JkywzszDto jkywzszDto);
	int updateList(List<JkywzszDto> list);
	int delJkywzsz(String deviceid);
	int delJkywzszAll();

}
