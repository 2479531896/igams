package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.WzjbDto;
import com.matridx.igams.wechat.dao.entities.WzjbModel;
import com.matridx.igams.wechat.dao.post.IWzjbDao;
import com.matridx.igams.wechat.service.svcinterface.IWzjbService;
import org.springframework.stereotype.Service;

@Service
public class WzjbServiceImpl extends BaseBasicServiceImpl<WzjbDto, WzjbModel, IWzjbDao> implements IWzjbService{
}
