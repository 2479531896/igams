package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.wechat.dao.entities.SjhbxxDto;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.post.IHbxxzDao;
import com.matridx.igams.wechat.dao.post.ISjxxDao;
import com.matridx.igams.wechat.service.svcinterface.ISjhbxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxCommonService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.email.EmailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SjxxCommonServiceImpl implements ISjxxCommonService{

	@Autowired
	private DingTalkUtil talkUtil;
	@Autowired
	private EmailUtil emailUtil;
	@Autowired
	private ISjxxDao sjxxDao;
	@Autowired
	ICommonService commonService;
	@Autowired
	ISjhbxxService sjhbxxService;
	@Autowired
	private IHbxxzDao hbxxzDao;
	
	private Logger log = LoggerFactory.getLogger(SjxxCommonServiceImpl.class);
	
	/**
	 * 发送消息公用方法
	 * @param sjxxDto
	 * @param map
	 * @return
	 */
	@Override
	public boolean sendMessage(SjxxDto sjxxDto, Map<String, Object> map) throws BusinessException {

		// 查询接收人员列表
		List<SjxxDto> sjxxDtos = sjxxDao.getReceiveUserByDb(sjxxDto);
		if (sjxxDtos != null && sjxxDtos.size() > 0) {
			try {
				// 发送钉钉消息
				boolean sendFlg = false;
				for (int i = 0; i < sjxxDtos.size(); i++) {
					SjxxDto t_SjxxDto = sjxxDtos.get(i);
					// 如果邮箱不为空，则发送邮件
					if(StringUtil.isNotBlank(t_SjxxDto.getYx())){
						sendFlg = true;
						List<String> yxlist = new ArrayList<>();
						yxlist = StringUtil.splitMsg(yxlist, t_SjxxDto.getYx(), "，|,");
						String subject = (String) map.get("yxbt"); // 标题
						String content = (String) map.get("yxnr"); // 内容
						String filePath = (String) map.get("filePath"); // 文件路径
						String wjm = (String) map.get("wjm"); // 文件名
						emailUtil.sendEmail(yxlist, subject, content, filePath, wjm);
					}
					// 如果微信不为空，则微信通知
					if (StringUtil.isNotBlank(t_SjxxDto.getHbcskz1())) {
						sendFlg = true;
						String templateid = (String) map.get("templateid"); // 微信模板ID
						String title = (String) map.get("wxbt"); // 微信标题
						String keyword1 = (String) map.get("keyword1");
						String keyword2 = (String) map.get("keyword2");
						String keyword3 = (String) map.get("keyword3");
						String keyword4 = (String) map.get("keyword4");
						String remark = (String) map.get("remark");
						String reporturl = (String) map.get("reporturl");
						Object obj=map.get("xxmb");
						// 让服务器发送信息到相应的微信里
						String[] arrWxid = t_SjxxDto.getHbcskz1().split("，|,");
						log.error("微信模板ID:"+templateid+",微信标题:"+title+",keyword1"+keyword1+",keyword2"+keyword2+",keyword3"+keyword3+",keyword4"+keyword4+",remark"+remark+",reporturl"+reporturl);

						for (int j = 0; j < arrWxid.length; j++) {
							log.error("微信ID:"+arrWxid[j] + " xxmb:" + obj);
							if(obj==null){
								commonService.sendWeChatMessage(templateid, arrWxid[j], title, keyword1, keyword2, keyword3, keyword4, remark, reporturl);
							}else{
								Map<String, String> messageMap = new HashMap<>();
								messageMap.put("title", title);
								messageMap.put("keyword1", keyword1);
								messageMap.put("keyword2", keyword2);
								messageMap.put("keyword3", keyword3);
								messageMap.put("keyword4", keyword4);
								messageMap.put("reporturl", reporturl);
								messageMap.put("remark", remark);
								commonService.sendWeChatMessageMap((String) obj,arrWxid[j],null,messageMap);
							}

						}
					}
					// 如果合作伙伴类型为1（直销）的情况，则为公司员工，则通过钉钉进行通知
					if ("1".equals(t_SjxxDto.getHblx()) && StringUtil.isNotBlank(t_SjxxDto.getDdid())) {
						sendFlg = true;
						String title = (String) map.get("ddbt"); // 钉钉标题
						String content = (String) map.get("ddnr"); // 钉钉消息
						User user=new User();
						user.setYhid(t_SjxxDto.getYhid());
						user=commonService.getUserInfoById(user);
						if(user!=null){
							talkUtil.sendWorkMessage(user.getYhm(), t_SjxxDto.getDdid(), title, content);
						}
					}
				}
				if (!sendFlg) {
					// 查询伙伴为'无'的钉钉ID
					List<SjhbxxDto> sjhbxxDtos = sjhbxxService.getXtyhByHbmc();
					if (sjhbxxDtos != null && sjhbxxDtos.size()>0) {
						for (int i = 0; i < sjhbxxDtos.size(); i++) {
							String title = (String) map.get("ddbt"); // 钉钉标题
							String content = (String) map.get("ddnr"); // 钉钉消息
							talkUtil.sendWorkMessage(sjhbxxDtos.get(i).getYhm(), sjhbxxDtos.get(i).getDdid(), title, content);
						}
					}
				}
			}catch (BusinessException e) {
				log.error("SjxxCommonServiceImpl - sendMessage -- Exception --- " + e.toString());
				throw new BusinessException("msg", e.getMsg());
			}catch (Exception e) {
				log.error("SjxxCommonServiceImpl - sendMessage -- Exception --- " + e.toString());
				throw new BusinessException("msg", e.toString());
			}
			return true;
		}
		return false;
	}

	public void sendMessageThread(SjxxDto sjxxDto, Map<String, Object> map)  {
		Thread thread=new Thread(){
			@Override
			public void run(){
				try {
					sendMessage(sjxxDto, map);
				} catch (BusinessException e) {
					throw new RuntimeException(e);
				}
			}
		};
		thread.start();
	}
	
	public SjhbxxDto getHbDtoFromId(SjxxDto sjxxDto) {
		List<SjhbxxDto> sjhbxxDtos = hbxxzDao.getHbDtoFromId(sjxxDto);
		if (sjhbxxDtos != null && sjhbxxDtos.size() > 0) {
			return sjhbxxDtos.get(0);
		}
		return null;
	}


	public List<SjhbxxDto> getHbxzDtoByHbmc(String hbmc){
		return hbxxzDao.getHbxzDtoByHbmc(hbmc);
	}
	
}
