package com.matridx.server.wechat.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.FjcfbModel;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.server.wechat.dao.entities.SjycfkWechatDto;
import com.matridx.server.wechat.dao.entities.SjycfkWechatModel;
import com.matridx.server.wechat.dao.post.ISjycfkWechatDao;
import com.matridx.server.wechat.service.svcinterface.ISjycfkWechatService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class SjycfkWechatServiceImpl extends BaseBasicServiceImpl<SjycfkWechatDto, SjycfkWechatModel, ISjycfkWechatDao> implements ISjycfkWechatService {

	private Logger log = LoggerFactory.getLogger(SjycfkWechatServiceImpl.class);
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	DingTalkUtil talkUtil;
	@Autowired
	IXxglService xxglService;
	
	/**
	 * 根据异常ID获取送检异常反馈信息
	 * @param sjycfkDto
	 * @return
	 */
	public List<SjycfkWechatDto> getDtoByYcid(SjycfkWechatDto sjycfkDto){
		return dao.getDtoByYcid(sjycfkDto);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean insertDto(SjycfkWechatDto sjycfkDto)
	{
		if (StringUtil.isBlank(sjycfkDto.getFkid()))
		{
			sjycfkDto.setFkid(StringUtil.generateUUID());
		}
		if (StringUtil.isBlank(sjycfkDto.getGid()))
		{
			sjycfkDto.setGid(sjycfkDto.getFkid());
		}
		int result = dao.insert(sjycfkDto);
		if (result == 0)
			return false;
		String wechatfkqf = "";
		List<JcsjDto> fkqfList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.FEEDBACK_DISTINGUISH.getCode());
		if (fkqfList!=null && fkqfList.size()>0) {
			for (JcsjDto jc_fkqf : fkqfList) {
				if ("WECHAT_FEEDBACK".equals(jc_fkqf.getCsdm())){
					wechatfkqf = jc_fkqf.getCsid();
					break;
				}
			}
		}
		if(sjycfkDto.getFjids()!=null && sjycfkDto.getFjids().size()>0 && wechatfkqf.equals(sjycfkDto.getFkqf())){
			//文件复制到正式文件夹，插入信息至正式表
			String prefjidString = "";
			for (int i = 0; i < sjycfkDto.getFjids().size(); i++) {
				String t_fjid = sjycfkDto.getFjids().get(i);
				if(StringUtil.isNotBlank(t_fjid) && t_fjid.equals(prefjidString))
					continue;
				prefjidString = t_fjid;
				boolean saveFile = fjcfbService.save2RealFile(sjycfkDto.getFjids().get(i),sjycfkDto.getFkid());
				if(!saveFile)
					return false;
			}

			// 附件排序
			FjcfbDto fjcfbDto = new FjcfbDto();
			fjcfbDto.setYwid(sjycfkDto.getFkid());
			fjcfbDto.setYwlx(BusTypeEnum.IMP_EXCEPTION_FEEDBACK.getCode());;
			boolean sortSuccess = fjcfbService.fileSort(fjcfbDto);
			if (!sortSuccess) {
				log.error("异常id："+sjycfkDto.getFkid()+" 附件排序失败！");
			}
		}

			if(StringUtil.isNotBlank(sjycfkDto.getFjcfparam())) {
				JSONArray parseArray = JSONObject.parseArray(sjycfkDto.getFjcfparam());
				for (int i = 0; i < parseArray.size(); i++) {
					FjcfbModel fjcfbModel = JSONObject.toJavaObject((JSONObject) parseArray.get(i), FjcfbModel.class);
					fjcfbModel.setYwid(sjycfkDto.getFkid());
					fjcfbModel.setYwlx(BusTypeEnum.IMP_EXCEPTION_FEEDBACK.getCode());;
					// 下载服务器文件到指定文件夹
					boolean isSuccess = fjcfbService.insert(fjcfbModel);
					if (!isSuccess)
						return false;
				}
			}
		return true;
	}
	
	
	/**
	 * 获取该送检信息下的所有异常的评论数
	 * @param sjycfkDto
	 * @return
	 */
	public List<SjycfkWechatDto> getExceptionPls(SjycfkWechatDto sjycfkDto){
		return dao.getExceptionPls(sjycfkDto);
	}

	/**
	 * 小程序获取异常反馈信息，父反馈ID为空 
	 * @param sjycfkDto
	 * @return
	 */
	public List<SjycfkWechatDto> getMiniDtoByYcid(SjycfkWechatDto sjycfkDto){
		List<SjycfkWechatDto> list=dao.getMiniDtoByYcid(sjycfkDto);
		List<String> fkidList=new ArrayList<>();
		for (SjycfkWechatDto sjycfkDto_t:list){
			fkidList.add(sjycfkDto_t.getFkid());
		}
		if(list!=null && list.size()>0) {
			FjcfbDto fjcfbDto=new FjcfbDto();
			fjcfbDto.setIds(fkidList);
				//根据异常ID查询附件表信息
				List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByIds(fjcfbDto);
				if(fjcfbDtos != null && fjcfbDtos.size()>0){
					for (int j = 0; j < fjcfbDtos.size(); j++) {
						String wjmhz = fjcfbDtos.get(j).getWjm().substring(fjcfbDtos.get(j).getWjm().lastIndexOf(".") + 1);
						fjcfbDtos.get(j).setWjmhz(wjmhz);
					}
				}
				if (fjcfbDtos!=null&&fjcfbDtos.size()>0){
					for (SjycfkWechatDto sjycfkDto1:list){
						List<FjcfbDto> fjcfbDtoList=new ArrayList<>();
						for (FjcfbDto fjcfbDto1:fjcfbDtos){
							if (StringUtil.isNotBlank(sjycfkDto1.getFkid())&&StringUtil.isNotBlank(fjcfbDto1.getYwid())&&sjycfkDto1.getFkid().equals(fjcfbDto1.getYwid())){
								fjcfbDtoList.add(fjcfbDto1);
							}
						}
						sjycfkDto1.setFjcfbDtos(fjcfbDtoList);
					}
				}
		}
		return list;
	}
	
	/**
	 * 小程序根据根ID获取异常反馈信息
	 * @param sjycfkDto
	 * @return
	 */
	public List<SjycfkWechatDto> getDtosByGid(SjycfkWechatDto sjycfkDto){
		return dao.getDtosByGid(sjycfkDto);
	}
	
	/**
	 * 根据fkid查找该评论下的所有子评论
	 * @param sjycfkDto
	 * @return
	 */
	public List<SjycfkWechatDto> getZplByFkid(SjycfkWechatDto sjycfkDto){
		return dao.getZplByFkid(sjycfkDto);
	}

	public void sendDingMessage(SjycfkWechatDto sjycfkDto){
		String ICOMM_YC00001 = xxglService.getMsg("ICOMM_YC00001");
		String ICOMM_YC00002 = xxglService.getMsg("ICOMM_YC00002");
		String internalbtn = null;
		//Tzrys格式为：ddid1-zsxm1-yhid1-yhm1,ddid2-zsxm2-yhid2-yhm2
		String str = sjycfkDto.getTzrys();
		String[] tzrys = str.split(",");
		try {
			for(String tzry:tzrys){
				String[] split = tzry.split("-");
				internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/exception/discussexception/discussexception&ycid="+sjycfkDto.getYcid(),"utf-8");
				//访问链接
				List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> t_btnJsonLists = new ArrayList<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList>();
				OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList t_btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
				t_btnJsonList.setTitle("小程序");
				t_btnJsonList.setActionUrl(internalbtn);
				t_btnJsonLists.add(t_btnJsonList);
				//发送钉钉消息
				talkUtil.sendCardMessage(split.length==4?split[3]:"",
						split[0],
						ICOMM_YC00001,
						StringUtil.replaceMsg(ICOMM_YC00002,
								sjycfkDto.getYcbt(), sjycfkDto.getFkxx()),
						t_btnJsonLists, "1");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void sendMessageFromOA(SjycfkWechatDto sjycfkDto,User user){
		String ICOMM_YC00001 = xxglService.getMsg("ICOMM_YC00001");
		String ICOMM_YC00002 = xxglService.getMsg("ICOMM_YC00002");
		String internalbtn = null;
		List<User> users=sjycfkDto.getUsers();
		try {
			for(User user_t:users){
				if (StringUtil.isNotBlank(user_t.getDdid())){
					internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/exception/discussexception/discussexception&ycid="+sjycfkDto.getYcid(),"utf-8");
					//访问链接
					List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> t_btnJsonLists = new ArrayList<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList>();
					OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList t_btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
					t_btnJsonList.setTitle("小程序");
					t_btnJsonList.setActionUrl(internalbtn);
					t_btnJsonLists.add(t_btnJsonList);
					//发送钉钉消息
					talkUtil.sendCardMessage(user_t.getYhm(),
							user_t.getDdid(),
							ICOMM_YC00001,
							StringUtil.replaceMsg(ICOMM_YC00002,
									sjycfkDto.getYcbt(), sjycfkDto.getFkxx()),
							t_btnJsonLists, "1");
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
