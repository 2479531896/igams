package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.wechat.dao.entities.SjdwxxDto;
import com.matridx.igams.wechat.service.svcinterface.ISjdwxxService;
import com.matridx.igams.wechat.service.svcinterface.IWeChatHisService;
import com.matridx.igams.wechat.util.WeChatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
@Service
public class WeChatHisServiceImpl implements IWeChatHisService{
	@Autowired
	ISjdwxxService sjdwxxservice;
	@Value("${matridx.wechat.menuurl:}")
	private String menuurl;
	
	@Override
	public boolean creatHis(SjdwxxDto sjdwxxDto) {
		RestTemplate t_restTemplate = new RestTemplate();
	// TODO Auto-generated method stub
		List<SjdwxxDto> sjdwTreeList=sjdwxxservice.getAllSjdwxx();
		String url=menuurl+"/wechat/creatHis";
        return WeChatUtil.createHis(url, t_restTemplate, sjdwTreeList);
    }

}
