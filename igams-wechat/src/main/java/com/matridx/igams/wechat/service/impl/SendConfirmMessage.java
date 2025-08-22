package com.matridx.igams.wechat.service.impl;


import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.wechat.dao.entities.SjhbxxDto;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.service.svcinterface.ISjhbxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.email.EmailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SendConfirmMessage {

	private Logger log = LoggerFactory.getLogger(SendConfirmMessage.class);

	private List<SjxxDto> sjxxDtos;
	private IXxglService xxglService;
	private boolean sendFlg;
	private SjxxDto sjxxDto;
	private DingTalkUtil talkUtil;
	private EmailUtil emailUtil;
	private ISjhbxxService sjhbxxService;
	private ICommonService commonService;
	private String ybzt_templateid;
	private String menuurl;

	public void init( List<SjxxDto> sjxxDtos,IXxglService xxglService, boolean sendFlg,SjxxDto sjxxDto,DingTalkUtil talkUtil,EmailUtil emailUtil,ISjhbxxService sjhbxxService,ICommonService commonService,String ybzt_templateid,String menuurl){
		this.sjxxDtos = sjxxDtos;
		this.xxglService=xxglService;
		this.sendFlg=sendFlg;
		this.sjxxDto=sjxxDto;
		this.talkUtil=talkUtil;
		this.emailUtil=emailUtil;
		this.sjhbxxService=sjhbxxService;
		this.commonService=commonService;
		this.ybzt_templateid=ybzt_templateid;
		this.menuurl=menuurl;
	}

	/**
	 * 发送确认报告
	 * @return
	 */
	public void sendConfirmMessage(){
		// TODO Auto-generated method stub

		log.error("--送检收样确认--发送确认消息线程开始");

		//钉钉
		String ICOMM_3000_HEAD = xxglService.getMsg("ICOMM_SJ00018");// 标题
		for (int i = 0; i < sjxxDtos.size(); i++) {
			SjxxDto t_SjxxDto = sjxxDtos.get(i);
			// 如果邮箱不为空，则发送邮件
			if(StringUtil.isNotBlank(t_SjxxDto.getYx())){
				//邮箱
				String ICOMM_1000_HEAD = xxglService.getMsg("ICOMM_SJ00022", sjxxDto.getHzxm(), sjxxDto.getYblxmc());// 标题
				sendFlg = true;
				List<String> yxlist = new ArrayList<>();
				yxlist = StringUtil.splitMsg(yxlist, t_SjxxDto.getYx(), "，|,");
				String xxid = sjxxDto.getYbzt_cskz2()+"_YX";//这里只取到对应的内容的消息的名字
				String ICOMM_XXNR = xxglService.getMsg(xxid, sjxxDto.getHzxm(), sjxxDto.getYblxmc(), sjxxDto.getYbztmc());
				try {
					emailUtil.sendEmail(yxlist, ICOMM_1000_HEAD, ICOMM_XXNR, null, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// 如果微信不为空，则微信通知
			if (StringUtil.isNotBlank(t_SjxxDto.getHbcskz1())) {
				//微信
				String ICOMM_2000_HEAD = xxglService.getMsg("ICOMM_SJ00019");// 标题
				sendFlg = true;
				String remark = null;
				String xxid = sjxxDto.getYbzt_cskz2()+"_WX";//这里只取到对应的内容的消息的名字
				remark = xxglService.getMsg(xxid, sjxxDto.getHzxm(), sjxxDto.getYblxmc(), sjxxDto.getYbztmc());
				//================
				// 让服务器发送信息到相应的微信里
				String[] arrWxid = t_SjxxDto.getHbcskz1().split("，|,");
				for (int j = 0; j < arrWxid.length; j++) {
					Map<String, String> messageMap = new HashMap<>();
					messageMap.put("title", ICOMM_2000_HEAD);
					messageMap.put("keyword1", sjxxDto.getYbztmc());
					messageMap.put("keyword2", DateUtils.getCustomFomratCurrentDate("HH:mm:ss"));
					messageMap.put("keyword3", remark);
					messageMap.put("keyword4", null);
					String reporturl = menuurl +"/wechat/statistics/wxRemark?remark="+remark;

					messageMap.put("reporturl",reporturl);
					messageMap.put("remark", remark);

					commonService.sendWeChatMessageMap("TEMPLATE_EXCEPTION",arrWxid[j],null,messageMap);
					//commonService.sendWeChatMessage(ybzt_templateid, arrWxid[j], ICOMM_2000_HEAD,  sjxxDto.getYbztmc(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss"), remark, null, null, null);
				}
			}
			// 如果合作伙伴类型为1（直销）的情况，钉钉通知
			if ("1".equals(t_SjxxDto.getHblx()) && StringUtil.isNotBlank(t_SjxxDto.getDdid())) {
				sendFlg = true;
				String xxid = sjxxDto.getYbzt_cskz2()+"_DD";//这里只取到对应的内容的消息的名字
				String ICOMM_XXNR = xxglService.getMsg(xxid, sjxxDto.getHzxm(), sjxxDto.getYblxmc(), sjxxDto.getYbztmc());
//				talkUtil.sendMarkdownMessage(t_SjxxDto.getYhm(), t_SjxxDto.getDdid(), ICOMM_3000_HEAD, ICOMM_XXNR);
				talkUtil.sendWorkMessage(t_SjxxDto.getYhm(), t_SjxxDto.getYhid(), ICOMM_3000_HEAD, ICOMM_XXNR);
				//================
			}
		}
		if (!sendFlg) {
			// 查询伙伴为'无'的钉钉ID
			List<SjhbxxDto> sjhbxxDtos = sjhbxxService.getXtyhByHbmc();
			for (int i = 0; i < sjhbxxDtos.size(); i++) {
				String xxid = sjxxDto.getYbzt_cskz2()+"_DD";//这里只取到对应的内容的消息的名字
				String ICOMM_XXNR = xxglService.getMsg(xxid, sjxxDto.getHzxm(), sjxxDto.getYblxmc(), sjxxDto.getYbztmc());
				talkUtil.sendWorkMessage(sjhbxxDtos.get(i).getYhm(),sjhbxxDtos.get(i).getYhid(), ICOMM_3000_HEAD,ICOMM_XXNR);
//				talkUtil.sendMarkdownMessage(sjhbxxDtos.get(i).getYhm(), sjhbxxDtos.get(i).getDdid(), ICOMM_3000_HEAD,ICOMM_XXNR);
			}
		}
		log.error("--送检收样确认--发送确认消息线程结束");
	}



	/**
	 * 发送确认报告
	 * @return
	 */
	public void sendConfirmNormalMessage(){
		// TODO Auto-generated method stub

		log.error("--送检收样确认--发送Normal确认消息线程开始。size:" + sjxxDtos.size());
		String ICOMM_SJ00019 = xxglService.getMsg("ICOMM_SJ00019");// 标题(微信)
		String ICOMM_SJ00025 = xxglService.getMsg("ICOMM_SJ00025", sjxxDto.getHzxm(), sjxxDto.getYblxmc());// 微信
		String ICOMM_SJ00027 = xxglService.getMsg("ICOMM_SJ00027");// 标题
		String ICOMM_SJ00026 = xxglService.getMsg("ICOMM_SJ00026", sjxxDto.getHzxm(), sjxxDto.getYblxmc());// 钉钉
		String ICOMM_SJ00023 = xxglService.getMsg("ICOMM_SJ00023", sjxxDto.getHzxm());// 标题(邮箱)
		String ICOMM_SJ00024 = xxglService.getMsg("ICOMM_SJ00024", sjxxDto.getHzxm(), sjxxDto.getYblxmc());// 邮箱

		for (int i = 0; i < sjxxDtos.size(); i++) {
			SjxxDto t_SjxxDto = sjxxDtos.get(i);
			log.error("邮箱：" + t_SjxxDto.getYx() + " 微信:" +t_SjxxDto.getHbcskz1() + " 钉钉:" + t_SjxxDto.getDdid());
			// 如果邮箱不为空，则发送邮件
			if(StringUtil.isNotBlank(t_SjxxDto.getYx())){
				sendFlg = true;
				List<String> yxlist = new ArrayList<>();
				yxlist = StringUtil.splitMsg(yxlist, t_SjxxDto.getYx(), "，|,");
				try {
					emailUtil.sendEmail(yxlist, ICOMM_SJ00023, ICOMM_SJ00024, null, null);
				} catch (Exception e) {
					log.error("邮箱发送失败。" + e.getMessage());
					e.printStackTrace();
				}
			}
			// 如果微信不为空，则微信通知
			if (StringUtil.isNotBlank(t_SjxxDto.getHbcskz1())) {
				sendFlg = true;
				// 让服务器发送信息到相应的微信里
				String[] arrWxid = t_SjxxDto.getHbcskz1().split("，|,");
				for (int j = 0; j < arrWxid.length; j++) {
					Map<String, String> messageMap = new HashMap<>();
					messageMap.put("title", ICOMM_SJ00019);
					messageMap.put("keyword1", "正常");
					messageMap.put("keyword2", DateUtils.getCustomFomratCurrentDate("HH:mm:ss"));
					messageMap.put("keyword3", ICOMM_SJ00025);
					messageMap.put("keyword4", null);
					messageMap.put("remark", ICOMM_SJ00025);
					String reporturl = menuurl +"/wechat/statistics/wxRemark?remark="+ICOMM_SJ00025;

					messageMap.put("reporturl",reporturl);
					commonService.sendWeChatMessageMap("TEMPLATE_EXCEPTION",arrWxid[j],null,messageMap);
					//commonService.sendWeChatMessage(ybzt_templateid, arrWxid[j], ICOMM_SJ00019,  "正常", DateUtils.getCustomFomratCurrentDate("HH:mm:ss"),null, null, ICOMM_SJ00025, null);
				}
			}
			// 如果合作伙伴类型为1（直销）的情况，钉钉通知
			if ("1".equals(t_SjxxDto.getHblx()) && StringUtil.isNotBlank(t_SjxxDto.getDdid())) {
				sendFlg = true;
//				talkUtil.sendMarkdownMessage(t_SjxxDto.getYhm(), t_SjxxDto.getDdid(), ICOMM_SJ00027, ICOMM_SJ00026);
				talkUtil.sendWorkMessage(t_SjxxDto.getYhm(), t_SjxxDto.getDdid(), ICOMM_SJ00027, ICOMM_SJ00026);
			}
		}
		if (!sendFlg) {
			// 查询伙伴为'无'的钉钉ID
			List<SjhbxxDto> sjhbxxDtos = sjhbxxService.getXtyhByHbmc();
			for (int i = 0; i < sjhbxxDtos.size(); i++) {
//				talkUtil.sendMarkdownMessage(sjhbxxDtos.get(i).getYhm(), sjhbxxDtos.get(i).getDdid(), ICOMM_SJ00027,ICOMM_SJ00026);
				talkUtil.sendWorkMessage(sjhbxxDtos.get(i).getYhm(), sjhbxxDtos.get(i).getDdid(), ICOMM_SJ00027,ICOMM_SJ00026);
			}
		}
		log.error("--送检收样确认--发送Normal确认消息线程结束");
	}
}
