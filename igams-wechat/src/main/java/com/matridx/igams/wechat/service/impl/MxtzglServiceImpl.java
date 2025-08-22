package com.matridx.igams.wechat.service.impl;

import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.post.ICommonDao;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.wechat.dao.entities.MxtzglDto;
import com.matridx.igams.wechat.dao.entities.MxtzglModel;
import com.matridx.igams.wechat.dao.entities.MxxxDto;
import com.matridx.igams.wechat.dao.post.IMxtzglDao;
import com.matridx.igams.wechat.service.svcinterface.IMxtzglService;
import com.matridx.igams.wechat.service.svcinterface.IMxxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MxtzglServiceImpl extends BaseBasicServiceImpl<MxtzglDto, MxtzglModel, IMxtzglDao> implements IMxtzglService {
	@Autowired
	ICommonDao commonDao;
	@Autowired
	DingTalkUtil talkUtil;
	@Autowired
	IXxglService xxglService;
	@Autowired
	private IMxxxService mxxxService;
	@Autowired
	ICommonService commonService;

	private Logger log = LoggerFactory.getLogger(MxtzglServiceImpl.class);
	/**
	 * 多条添加通知记录
	 * @param list
	 * @return
	 */
	public boolean insertList(List<MxtzglDto> list){
		return dao.insertList(list);
	}

	/**
	 * 获取镁信通知信息
	 * @param mxtzglDto
	 * @return
	 */
	public List<MxtzglDto> getListById(MxtzglDto mxtzglDto){
		return dao.getListById(mxtzglDto);
	}

	/**
	 * 发送通知
	 * @param
	 * @return
	 */
	public boolean sendMessage(MxtzglDto mxtzglDto) {
		String ryids = mxtzglDto.getRyid();
		String[] rys = ryids.split(",");
		MxxxDto mxxxDto = mxxxService.getDtoById(mxtzglDto.getMxid());
		List<String> ids = new ArrayList<>();
		List<MxtzglDto> tzlist = new ArrayList<>();
		ids.addAll(Arrays.asList(rys));
		User yh = new User();
		yh.setIds(ids);
		List<User> list = commonDao.getListByIds(yh);
		List<MxtzglDto> mxtzlist = getListById(mxtzglDto);
		List<String> rylist = new ArrayList<>();
		if(mxtzlist!=null&&mxtzlist.size()>0){
			for(MxtzglDto dto:mxtzlist){
				rylist.add(dto.getRyid());
			}
		}
//		String token = talkUtil.getToken();
		for (User user : list) {
			if(!rylist.contains(user.getYhid())){
				MxtzglDto mxtzglDto_t=new MxtzglDto();
				mxtzglDto_t.setRyid(user.getYhid());
				mxtzglDto_t.setMxid(mxtzglDto.getMxid());
				mxtzglDto_t.setDdid(user.getDdid());
				tzlist.add(mxtzglDto_t);
			}
		}
		for(MxtzglDto mxtzglDto_t:tzlist){
			//小程序访问
			String external = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/sjxxlr/sjxxlr&mxid=" + mxtzglDto.getMxid()+"&projectCode="+mxxxDto.getProjectCode()+"&idName="+mxxxDto.getIdName()+"&phoneNo="+mxxxDto.getPhoneNo(),StandardCharsets.UTF_8);
			List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
			OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
			btnJsonList.setTitle("小程序");
			btnJsonList.setActionUrl(external);
			btnJsonLists.add(btnJsonList);
			User user=new User();
			user.setYhid(mxtzglDto_t.getRyid());
			user=commonService.getUserInfoById(user);
			if(user!=null){
				talkUtil.sendCardMessage(user.getYhm(), mxtzglDto_t.getDdid(), xxglService.getMsg("ICOMM_MX00001"), StringUtil.replaceMsg(xxglService.getMsg("ICOMM_MX00002"), mxxxDto.getProjectCode(), mxxxDto.getOrgName(),
						mxxxDto.getIdName(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),btnJsonLists, "1");
			}
		}
		if(tzlist!=null&&tzlist.size()>0){
			insertList(tzlist);
		}
		return true;
	}


}