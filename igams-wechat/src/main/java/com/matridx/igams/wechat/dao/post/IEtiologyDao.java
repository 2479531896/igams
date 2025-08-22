package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.EtiologyDto;
import com.matridx.igams.wechat.dao.entities.EtiologyModel;

import java.util.List;

public interface IEtiologyDao extends BaseBasicDao<EtiologyDto, EtiologyModel> {

    boolean delBylx(EtiologyDto etiologyDto);

    boolean insertList(EtiologyDto etiologyDto);

    boolean insertByDay(EtiologyDto etiologyDto);

    List<EtiologyDto> getYjByCxlx(EtiologyDto etiologyDto);
    List<EtiologyDto> getTest(EtiologyDto etiologyDto);
}
