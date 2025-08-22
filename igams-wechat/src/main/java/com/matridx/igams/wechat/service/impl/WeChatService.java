package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.wechat.dao.entities.WxcdDto;
import com.matridx.igams.wechat.service.svcinterface.IWeChatService;
import com.matridx.igams.wechat.service.svcinterface.IWxcdService;
import com.matridx.igams.wechat.util.WeChatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class WeChatService implements IWeChatService{
	
	@Autowired
	IWxcdService wxcdService;
	
	@Value("${matridx.wechat.menuurl:}")
	private String menuurl;
	
	/**
	 * 创建菜单
	 * @param wxcdDto
	 * @return
	 */
	public boolean createMenu(WxcdDto wxcdDto){
		List<WxcdDto> wxcdTreeList = wxcdService.getWxcdTreeList(wxcdDto);
		String url = menuurl+"/wechat/createMenu";
		
		RestTemplate t_restTemplate = new RestTemplate();
		
		boolean result = WeChatUtil.createMenu(url, t_restTemplate, wxcdTreeList);
        return result;
    }
}
