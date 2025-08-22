package com.matridx.igams.wechat.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.SjyzDto;
import com.matridx.igams.wechat.service.svcinterface.ISjyzService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SendPCRAudit {

private Logger log = LoggerFactory.getLogger(SendPCRAudit.class);

	private List<String> xtsbys;
	private SjxxDto sjxxDto;
	private RedisUtil redisUtil;
    private Map<String,String> yzlbs;
    private ISjyzService sjyzService;
    private IShgcService shgcService;
	private User user;

	public void init(List<String> xtsbys,SjxxDto sjxxDto,RedisUtil redisUtil,Map<String,String> yzlbs,ISjyzService sjyzService,IShgcService shgcService,User user){
		this.xtsbys = xtsbys;
		this.sjxxDto = sjxxDto;
		this.redisUtil = redisUtil;
        this.yzlbs = yzlbs;
        this.sjyzService=sjyzService;
		this.shgcService=shgcService;
		this.user=user;
	}

	/**
	 * 提交送检验证审核
	 * @return
	 */
	public void sendPCRAudit(){
		log.error("-----------提交送检验证审核线程开始------------");
		boolean isSuccess = false;
		for (String tsby : xtsbys) {
			SjyzDto sjyzDto = new SjyzDto();
			sjyzDto.setJcdw(sjxxDto.getJcdw());
			sjyzDto.setSjid(sjxxDto.getSjid());
			/*设置审核类别*/
			sjyzDto.setAuditType("AUDIT_VERIFICATION");
			/*根据检测项目 设置区分（是否去人源）*/
			boolean contains = false;
			if (sjxxDto.getJcxmids()!=null && sjxxDto.getJcxmids().size()>0){
				for (String jcxmid : sjxxDto.getJcxmids()) {
					contains = redisUtil.hgetDto("matridx_jcsj:"+ BasicDataTypeEnum.DETECT_TYPE.getCode(),jcxmid).getCsdm().contains("_REM");
				}
			} else {
				contains = redisUtil.hgetDto("matridx_jcsj:"+BasicDataTypeEnum.DETECT_TYPE.getCode(),sjxxDto.getJcxm()).getCsdm().contains("_REM");
			}
			JcsjDto jcqf = new JcsjDto();
			jcqf.setJclb(BasicDataTypeEnum.DISTINGUISH.getCode());
			jcqf.setCsdm(contains?"p":"A");
			List<JcsjDto> matridx_jcsj_distinguish = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.DISTINGUISH.getCode());
			for (JcsjDto jcsjDto : matridx_jcsj_distinguish) {
				if (jcqf.getCsdm().equals(jcsjDto.getCsdm())){
					sjyzDto.setQf(jcsjDto.getCsid());
					break;
				}
			}
			/*设置客户通知为不通知*/
			JcsjDto jcsjDto_tz = new JcsjDto();
			jcsjDto_tz.setJclb(BasicDataTypeEnum.CLIENT_NOTICE.getCode());
			jcsjDto_tz.setCsdm("B");
			List<JcsjDto> matridx_jcsj_client_notice = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.CLIENT_NOTICE.getCode());
			for (JcsjDto jcsjDto : matridx_jcsj_client_notice) {
				if (jcsjDto_tz.getCsdm().equals(jcsjDto.getCsdm())){
					sjyzDto.setKhtz(jcsjDto.getCsid());
				}
			}
			/*设置验证类别*/
			JcsjDto yzlb = new JcsjDto();
			yzlb.setJclb("VERIFICATION_TYPE");
			yzlb.setCsdm(yzlbs.get(tsby));
			List<JcsjDto> matridx_jcsj_verification_type = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.VERIFICATION_TYPE.getCode());
			for (JcsjDto jcsjDto : matridx_jcsj_verification_type) {
				if (yzlb.getCsdm().equals(jcsjDto.getCsdm())){
					sjyzDto.setYzlb(jcsjDto.getCsid());
					yzlb.setCsmc(jcsjDto.getCsmc());
					break;
				}
			}
			List<JcsjDto> matridx_jcsj_verify_result = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.VERIFY_RESULT.getCode());
			StringBuilder yzjg = new StringBuilder();
			for (JcsjDto jcsjDto : matridx_jcsj_verify_result) {
				if (jcsjDto.getCsid().equals(sjyzDto.getYzlb())){
					yzjg.append(",").append(jcsjDto.getCsmc()).append(":");
				}
			}
			sjyzDto.setYzjg(StringUtil.isNotBlank(yzjg.toString())?yzjg.substring(1): yzjg.toString());
			List<String> strlist = new ArrayList<>();
			strlist.add(StatusEnum.CHECK_NO.getCode());
			strlist.add(StatusEnum.CHECK_SUBMIT.getCode());
			sjyzDto.setZts(strlist);
			int num = sjyzService.getCount(sjyzDto);
			if (num>0) {
				if(!isSuccess){
					log.error("验证类别:"+yzlb.getCsmc()+"已在审核队列中!");
				}
			}else {
				sjyzDto.setLrry(user.getYhid());
				sjyzDto.setZt(StatusEnum.CHECK_NO.getCode());
				sjyzDto.setXsbj("1");
				isSuccess=sjyzService.insertDto(sjyzDto);
			}
			ShgcDto shgcDto = new ShgcDto();
			shgcDto.setExtend_1(JSONObject.toJSONString(new String[]{sjyzDto.getYzid()}));
			shgcDto.setShlb("AUDIT_VERIFICATION");
			try{
				 shgcService.checkAndCommit(shgcDto,user);
			} catch (BusinessException e) {
				log.error(e.toString());
			} catch (Exception e) {
				log.error("commCommitAudit(ShgcDto shgcDto): Ywids= " + shgcDto.getYwids()!=null ?shgcDto.getYwids().toString():"" + " Extend_1= " + shgcDto.getExtend_1());
				log.error(e.toString());
			}
		}
		log.error("-----------提交送检验证审核线程结束------------");
	}
}
