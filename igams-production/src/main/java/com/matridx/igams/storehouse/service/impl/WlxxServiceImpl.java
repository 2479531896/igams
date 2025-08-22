package com.matridx.igams.storehouse.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.enums.DingMessageType;
import com.matridx.igams.common.enums.MQTypeEnum_pub;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.production.service.svcinterface.IWjglService;
import com.matridx.igams.storehouse.dao.entities.*;
import com.matridx.igams.storehouse.dao.post.IWlxxDao;
import com.matridx.igams.storehouse.service.svcinterface.*;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WlxxServiceImpl extends BaseBasicServiceImpl<WlxxDto, WlxxModel, IWlxxDao> implements IWlxxService {
	@Autowired
	DingTalkUtil talkUtil;
	@Autowired
	ICommonService commonService;
	@Autowired
	ILlglService llglService;
	@Autowired
	IJcjyglService jcjyglService;
	@Autowired
	IFhglService fhglService;
	@Autowired
	IDdxxglService ddxxglService;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IXsglService xsglService;
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	IWjglService wjglService;
	@Autowired(required=false)
	private AmqpTemplate amqpTempl;
	/**
	 * 文档转换完成OK
	 */
	@Value("${spring.rabbitmq.docok:mq.tran.basic.ok}")
	private String DOC_OK = null;
	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean logisticsupholdSave(WlxxDto wlxxDto) throws BusinessException {
		List<WlxxDto> wlxxDtos = JSON.parseArray(wlxxDto.getWlmx_json(), WlxxDto.class);
		List<WlxxDto> dtoList = dao.getDtoListById(wlxxDto.getYwid());
		if (!CollectionUtils.isEmpty(wlxxDtos)||!CollectionUtils.isEmpty(dtoList)){
			String lrsj = DateUtils.format(new Date(), "yyyy-MM-dd");
//			String token = talkUtil.getToken();
			List<DdxxglDto> ddxxglDtolist = null;
			if ("ll".equals(wlxxDto.getYwlx())){
				ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.LOGISTICS_LLTZ.getCode());
			}else if ("fh".equals(wlxxDto.getYwlx())){
				ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.LOGISTICS_FHTZ.getCode());
			}else if ("jcjy".equals(wlxxDto.getYwlx())){
				ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.LOGISTICS_JCJYTZ.getCode());
			}else if ("xs".equals(wlxxDto.getYwlx())){
				ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.LOGISTICS_XSTZ.getCode());
			}
			if (!CollectionUtils.isEmpty(dtoList)){
				if (CollectionUtils.isEmpty(wlxxDtos)){
					int delete = dao.delete(wlxxDto);
					if (delete<1){
						throw new BusinessException("msg","物流信息删除失败!");
					}
					wlxxDto.setSfqr(null);
					boolean b = processWlxx(wlxxDto, null);
					if (!b){
						throw new BusinessException("msg","快递信息保存失败!");
					}
				}else {
					String sfqs="0";
					int delete = dao.delete(wlxxDto);
					if (delete<1){
						throw new BusinessException("msg","物流信息删除失败!");
					}
					List<WlxxDto> addWlxxDtos= new ArrayList<>();
					StringBuilder kdxx = new StringBuilder();
					boolean sfqbqr = true;
					for (WlxxDto dto : wlxxDtos) {
						kdxx.append(",").append(dto.getKdgs()).append(dto.getWldh()).append(":").append(StringUtil.isNotBlank(dto.getFhrq()) ? dto.getFhrq().replace("-", "/") + "发货" : DateUtils.getCustomFomratCurrentDate("yyyy/MM/dd") + "发货").append(StringUtil.isNotBlank(dto.getQsrq()) ? (dto.getQsrq().replace("-", "/").substring(0, dto.getQsrq().length() - 3) + "签收") : "未签收");
						if (StringUtil.isBlank(dto.getWlxxid())){
							dto.setLrry(wlxxDto.getLrry());
							dto.setWlxxid(StringUtil.generateUUID());
							dto.setYwid(wlxxDto.getYwid());
							dto.setSfqr("0");
							dto.setLrsj(lrsj);
						}
						if ("ll".equals(wlxxDto.getYwlx())||"jcjy".equals(wlxxDto.getYwlx())||"xs".equals(wlxxDto.getYwlx())){
							if (StringUtil.isNotBlank(dto.getQsrq())){
								sfqs="1";
							}
						}
						addWlxxDtos.add(dto);
						if ("0".equals(dto.getSfqr())){
							sfqbqr = false;
						}
						if (!CollectionUtils.isEmpty(dto.getFjids())) {
							for (int j = 0; j < dto.getFjids().size(); j++) {
								if(StringUtil.isNotBlank(dto.getFjids().get(j))) {
									fjcfbService.save3RealFile(dto.getFjids().get(j), dto.getYwid(), dto.getWlxxid());
									FjcfbDto fjcfbDto=new FjcfbDto();
									fjcfbDto.setFjid(dto.getFjids().get(j));
									FjcfbDto t_fjcfbDto=fjcfbService.getDto(fjcfbDto);
									int begin=t_fjcfbDto.getWjm().lastIndexOf(".");
									String wjmkzm=t_fjcfbDto.getWjm().substring(begin);
									DBEncrypt p = new DBEncrypt();
									if((wjmkzm.equalsIgnoreCase(".doc"))||(wjmkzm.equalsIgnoreCase(".docx"))||(wjmkzm.equalsIgnoreCase(".xls"))||(wjmkzm.equalsIgnoreCase(".xlsx"))) {
										String wjljjm=p.dCode(t_fjcfbDto.getWjlj());
										//连接服务器
										boolean sendFlg=wjglService.sendWordFile(wjljjm);
										if(sendFlg) {
											Map<String,String> t_map=new HashMap<>();
											String fwjm=p.dCode(t_fjcfbDto.getFwjm());
											t_map.put("wordName", fwjm);
											t_map.put("fwjlj",t_fjcfbDto.getFwjlj());
											t_map.put("fjid",t_fjcfbDto.getFjid());
											t_map.put("ywlx",t_fjcfbDto.getYwlx());
											t_map.put("MQDocOkType",DOC_OK);
											//发送Rabbit消息转换pdf
											amqpTempl.convertAndSend("doc2pdf_exchange", MQTypeEnum_pub.CONTRACE_BASIC_W2P.getCode(), JSONObject.toJSONString(t_map));
										}
									}
								}
							}
						}
					}
					if ("ll".equals(wlxxDto.getYwlx())){
						LlglDto llglDto=new LlglDto();
						llglDto.setLlid(wlxxDto.getYwid());
						llglDto.setSfqs(sfqs);
						llglService.update(llglDto);
					}
					else if("jcjy".equals(wlxxDto.getYwlx())){
						JcjyglDto jcjyglDto=new JcjyglDto();
						jcjyglDto.setJcjyid(wlxxDto.getYwid());
						jcjyglDto.setSfqs(sfqs);
						jcjyglService.update(jcjyglDto);

					}else if ("xs".equals(wlxxDto.getYwlx())){
						XsglDto xsglDto=new XsglDto();
						xsglDto.setXsid(wlxxDto.getYwid());
						xsglDto.setSfqs(sfqs);
						xsglService.update(xsglDto);
					}
					kdxx = new StringBuilder(kdxx.substring(1));
					if (sfqbqr){
						wlxxDto.setSfqr("1");
					}else {
						wlxxDto.setSfqr("0");
					}
					boolean b = processWlxx(wlxxDto, kdxx.toString());
					if (!b){
						throw new BusinessException("msg","快递信息保存失败!");
					}
					if (!CollectionUtils.isEmpty(addWlxxDtos)){
						boolean isSucccess = dao.insertList(addWlxxDtos);
						if (!isSucccess){
							throw new BusinessException("msg","物流信息保存失败!");
						}
					}
					if(StringUtil.isBlank(wlxxDto.getXxbj())){
						String ICOMM_OAWLTZ002 = xxglService.getMsg("ICOMM_OAWLTZ002");
						String ICOMM_OAWLTZ004 = xxglService.getMsg("ICOMM_OAWLTZ004");
						if (!CollectionUtils.isEmpty(ddxxglDtolist)){
							for (DdxxglDto ddxxglDto : ddxxglDtolist) {
								if(StringUtil.isNotBlank(ddxxglDto.getDdid())){
									// 内网访问
									String internalbtn =applicationurl + urlPrefix
											+ "/ws/storehouse/receiveMateriel/viewYwxxWithWlxx?ywid=" + wlxxDto.getYwid()+"&ywlx="+wlxxDto.getYwlx();
									List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
									OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
									btnJsonList.setTitle("详细");
									btnJsonList.setActionUrl(internalbtn);
									btnJsonLists.add(btnJsonList);
									talkUtil.sendCardMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(),ICOMM_OAWLTZ002,StringUtil.replaceMsg(ICOMM_OAWLTZ004,
													wlxxDto.getYwdh(),"ll".equals(wlxxDto.getYwlx())?"领料单":"fh".equals(wlxxDto.getYwlx())?"发货单":"jcjy".equals(wlxxDto.getYwlx())?"借出借用单":"xs".equals(wlxxDto.getYwlx())?"销售单":"",
													kdxx.toString(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),
											btnJsonLists,"1");
								}
							}
						}
					}
				}
			}else {
				StringBuilder kdxx = new StringBuilder();
				for (WlxxDto dto : wlxxDtos) {
					dto.setLrry(wlxxDto.getLrry());
					dto.setWlxxid(StringUtil.generateUUID());
					dto.setYwid(wlxxDto.getYwid());
					dto.setSfqr("0");
					dto.setLrsj(lrsj);
					if (!CollectionUtils.isEmpty(dto.getFjids())) {
						for (int j = 0; j < dto.getFjids().size(); j++) {
							if (StringUtil.isNotBlank(dto.getFjids().get(j))) {
								fjcfbService.save3RealFile(dto.getFjids().get(j), dto.getYwid(), dto.getWlxxid());
								FjcfbDto fjcfbDto=new FjcfbDto();
								fjcfbDto.setFjid(dto.getFjids().get(j));
								FjcfbDto t_fjcfbDto=fjcfbService.getDto(fjcfbDto);
								int begin=t_fjcfbDto.getWjm().lastIndexOf(".");
								String wjmkzm=t_fjcfbDto.getWjm().substring(begin);
								DBEncrypt p = new DBEncrypt();
								if((wjmkzm.equalsIgnoreCase(".doc"))||(wjmkzm.equalsIgnoreCase(".docx"))||(wjmkzm.equalsIgnoreCase(".xls"))||(wjmkzm.equalsIgnoreCase(".xlsx"))) {
									String wjljjm=p.dCode(t_fjcfbDto.getWjlj());
									//连接服务器
									boolean sendFlg=wjglService.sendWordFile(wjljjm);
									if(sendFlg) {
										Map<String,String> t_map=new HashMap<>();
										String fwjm=p.dCode(t_fjcfbDto.getFwjm());
										t_map.put("wordName", fwjm);
										t_map.put("fwjlj",t_fjcfbDto.getFwjlj());
										t_map.put("fjid",t_fjcfbDto.getFjid());
										t_map.put("ywlx",t_fjcfbDto.getYwlx());
										t_map.put("MQDocOkType",DOC_OK);
										//发送Rabbit消息转换pdf
										amqpTempl.convertAndSend("doc2pdf_exchange", MQTypeEnum_pub.CONTRACE_BASIC_W2P.getCode(), JSONObject.toJSONString(t_map));
									}
								}

							}
						}
					}
					kdxx.append(",").append(dto.getKdgs()).append(dto.getWldh()).append(":").append(StringUtil.isNotBlank(dto.getFhrq()) ? dto.getFhrq().replace("-", "/") + "发货" : DateUtils.getCustomFomratCurrentDate("yyyy/MM/dd") + "发货").append(StringUtil.isNotBlank(dto.getQsrq()) ? (dto.getQsrq().replace("-", "/").substring(0, dto.getQsrq().length() - 3) + "签收") : "未签收");
				}
				kdxx = new StringBuilder(kdxx.substring(1));
				boolean isSucccess = dao.insertList(wlxxDtos);
				if (!isSucccess){
					throw new BusinessException("msg","物流信息保存失败!");
				}
				wlxxDto.setSfqr("0");
				boolean b = processWlxx(wlxxDto, kdxx.toString());
				if (!b){
					throw new BusinessException("msg","快递信息保存失败!");
				}
				if(StringUtil.isBlank(wlxxDto.getXxbj())){
					String ICOMM_OAWLTZ001 = xxglService.getMsg("ICOMM_OAWLTZ001");
					String ICOMM_OAWLTZ003 = xxglService.getMsg("ICOMM_OAWLTZ003");
					if (!CollectionUtils.isEmpty(ddxxglDtolist)){
						for (DdxxglDto ddxxglDto : ddxxglDtolist) {
							if(StringUtil.isNotBlank(ddxxglDto.getDdid())){
								// 内网访问
								String internalbtn =applicationurl + urlPrefix
										+ "/ws/storehouse/receiveMateriel/viewYwxxWithWlxx?ywid=" + wlxxDto.getYwid()+"&ywlx="+wlxxDto.getYwlx();
								List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
								OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
								btnJsonList.setTitle("详细");
								btnJsonList.setActionUrl(internalbtn);
								btnJsonLists.add(btnJsonList);
								talkUtil.sendCardMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(),ICOMM_OAWLTZ001,StringUtil.replaceMsg(ICOMM_OAWLTZ003,
												wlxxDto.getYwdh(),"ll".equals(wlxxDto.getYwlx())?"领料单":"fh".equals(wlxxDto.getYwlx())?"发货单":"jcjy".equals(wlxxDto.getYwlx())?"借出借用单":"xs".equals(wlxxDto.getYwlx())?"销售单":"",
												kdxx.toString(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),
										btnJsonLists,"1");
							}
						}
					}
				}
			}
		}
		return true;
	}

	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean processWlxx(WlxxDto wlxxDto, String kdxx) throws BusinessException {
		if ("ll".equals(wlxxDto.getYwlx())){
			LlglDto llglDto = new LlglDto();
			llglDto.setLlid(wlxxDto.getYwid());
			llglDto.setKdxx(kdxx);
			llglDto.setSfqr(wlxxDto.getSfqr());
			llglDto.setXgry(wlxxDto.getLrry());
			boolean isSuccess = llglService.updateWlxx(llglDto);
			if (!isSuccess){
				throw new BusinessException("msg","快递信息保存失败!");
			}
		}else if ("fh".equals(wlxxDto.getYwlx())){
			FhglDto fhglDto = new FhglDto();
			fhglDto.setFhid(wlxxDto.getYwid());
			fhglDto.setKdxx(kdxx);
			fhglDto.setSfqr(wlxxDto.getSfqr());
			fhglDto.setXgry(wlxxDto.getLrry());
			boolean isSuccess = fhglService.updateWlxx(fhglDto);
			if (!isSuccess){
				throw new BusinessException("msg","快递信息保存失败!");
			}
		}else if ("jcjy".equals(wlxxDto.getYwlx())){
			JcjyglDto jcjyglDto = new JcjyglDto();
			jcjyglDto.setJcjyid(wlxxDto.getYwid());
			jcjyglDto.setKdxx(kdxx);
			jcjyglDto.setSfqr(wlxxDto.getSfqr());
			jcjyglDto.setXgry(wlxxDto.getLrry());
			boolean isSuccess = jcjyglService.updateWlxx(jcjyglDto);
			if (!isSuccess){
				throw new BusinessException("msg","快递信息保存失败!");
			}
		}else if ("xs".equals(wlxxDto.getYwlx())){
			XsglDto xsglDto = new XsglDto();
			xsglDto.setXsid(wlxxDto.getYwid());
			xsglDto.setKdxx(kdxx);
			xsglDto.setSfqr(wlxxDto.getSfqr());
			xsglDto.setXgry(wlxxDto.getLrry());
			boolean isSuccess = xsglService.updateWlxx(xsglDto);
			if (!isSuccess){
				throw new BusinessException("msg","快递信息保存失败!");
			}
		}
		return true;
	}

	@Override
	public List<WlxxDto> getDtoListById(String ywid) {
		return dao.getDtoListById(ywid);
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean signforSave(WlxxDto wlxxDto) throws BusinessException {
		List<WlxxDto> wlxxDtos = JSON.parseArray(wlxxDto.getWlmx_json(), WlxxDto.class);
		if (!CollectionUtils.isEmpty(wlxxDtos)){
			boolean isSuccess = dao.updateList(wlxxDtos);
			if (!isSuccess){
				throw new BusinessException("msg","物流信息保存失败!");
			}
//			String token = talkUtil.getToken();
			List<DdxxglDto> ddxxglDtolist = null;
			if ("ll".equals(wlxxDto.getYwlx())){
				ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.LOGISTICS_LLTZ.getCode());
			}else if ("fh".equals(wlxxDto.getYwlx())){
				ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.LOGISTICS_FHTZ.getCode());
			}else if ("jcjy".equals(wlxxDto.getYwlx())){
				ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.LOGISTICS_JCJYTZ.getCode());
			}else if ("xs".equals(wlxxDto.getYwlx())){
				ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.LOGISTICS_XSTZ.getCode());
			}
			StringBuilder kdxx = new StringBuilder();
			String sfqs="0";
			boolean sfqbqr = true;
			for (WlxxDto dto : wlxxDtos) {
				kdxx.append(",").append(dto.getKdgs()).append(dto.getWldh()).append(":").append(StringUtil.isNotBlank(dto.getFhrq()) ? dto.getFhrq().replace("-", "/") + "发货" : DateUtils.getCustomFomratCurrentDate("yyyy/MM/dd") + "发货").append(StringUtil.isNotBlank(dto.getQsrq()) ? (dto.getQsrq().replace("-", "/").substring(0, dto.getQsrq().length() - 3) + "签收") : "未签收");
				if ("0".equals(dto.getSfqr())){
					sfqbqr = false;
				}
				if ("ll".equals(wlxxDto.getYwlx())||"jcjy".equals(wlxxDto.getYwlx())||"xs".equals(wlxxDto.getYwlx())){
					if (StringUtil.isNotBlank(dto.getQsrq())){
						sfqs="1";
					}
				}
			}
			if ("ll".equals(wlxxDto.getYwlx())){
				LlglDto llglDto=new LlglDto();
				llglDto.setLlid(wlxxDto.getYwid());
				llglDto.setSfqs(sfqs);
				llglService.update(llglDto);
			}
			else if("jcjy".equals(wlxxDto.getYwlx())){
				JcjyglDto jcjyglDto=new JcjyglDto();
				jcjyglDto.setJcjyid(wlxxDto.getYwid());
				jcjyglDto.setSfqs(sfqs);
				jcjyglService.update(jcjyglDto);

			}else if ("xs".equals(wlxxDto.getYwlx())){
				XsglDto xsglDto=new XsglDto();
				xsglDto.setXsid(wlxxDto.getYwid());
				xsglDto.setSfqs(sfqs);
				xsglService.update(xsglDto);
			}
			kdxx = new StringBuilder(kdxx.substring(1));
			if (sfqbqr){
				wlxxDto.setSfqr("1");
			}else {
				wlxxDto.setSfqr("0");
			}
			boolean b = processWlxx(wlxxDto, kdxx.toString());
			if (!b){
				throw new BusinessException("msg","快递信息保存失败!");
			}
			if (!CollectionUtils.isEmpty(ddxxglDtolist) && StringUtil.isBlank(wlxxDto.getXxbj())){
				String ICOMM_OAWLTZ005 = xxglService.getMsg("ICOMM_OAWLTZ005");
				String ICOMM_OAWLTZ006 = xxglService.getMsg("ICOMM_OAWLTZ006");
				for (DdxxglDto ddxxglDto : ddxxglDtolist) {
					if(StringUtil.isNotBlank(ddxxglDto.getDdid())){
						// 内网访问
						String internalbtn =applicationurl + urlPrefix
								+ "/ws/storehouse/receiveMateriel/viewYwxxWithWlxx?ywid=" + wlxxDto.getYwid()+"&ywlx="+wlxxDto.getYwlx();
						List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
						OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
						btnJsonList.setTitle("详细");
						btnJsonList.setActionUrl(internalbtn);
						btnJsonLists.add(btnJsonList);
						talkUtil.sendCardMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(),ICOMM_OAWLTZ005,StringUtil.replaceMsg(ICOMM_OAWLTZ006,
										wlxxDto.getYwdh(),"ll".equals(wlxxDto.getYwlx())?"领料单":"fh".equals(wlxxDto.getYwlx())?"发货单":"jcjy".equals(wlxxDto.getYwlx())?"借出借用单":"xs".equals(wlxxDto.getYwlx())?"销售单":"",
										kdxx.toString(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),
								btnJsonLists,"1");
					}
				}
			}
		}
		return true;
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean signforconfirmSave(WlxxDto wlxxDto) throws BusinessException {
		List<WlxxDto> wlxxDtos = JSON.parseArray(wlxxDto.getWlmx_json(), WlxxDto.class);
		if (!CollectionUtils.isEmpty(wlxxDtos)){
			boolean isSuccess = dao.updateList(wlxxDtos);
			if (!isSuccess){
				throw new BusinessException("msg","物流信息保存失败!");
			}
			/*String token = talkUtil.getToken();
			List<DdxxglDto> ddxxglDtolist = null;*/
			/*if ("ll".equals(wlxxDto.getYwlx())){
				ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.LOGISTICS_LLTZ.getCode());
			}else if ("fh".equals(wlxxDto.getYwlx())){
				ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.LOGISTICS_FHTZ.getCode());
			}else if ("jcjy".equals(wlxxDto.getYwlx())){
				ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.LOGISTICS_JCJYTZ.getCode());
			}else if ("xs".equals(wlxxDto.getYwlx())){
				ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.LOGISTICS_XSTZ.getCode());
			}*/
			StringBuilder kdxx = new StringBuilder();
			boolean sfqbqr = true;
			for (WlxxDto dto : wlxxDtos) {
				kdxx.append(",").append(dto.getKdgs()).append(dto.getWldh()).append(":").append(StringUtil.isNotBlank(dto.getFhrq()) ? dto.getFhrq().replace("-", "/") + "发货" : DateUtils.getCustomFomratCurrentDate("yyyy/MM/dd") + "发货").append(StringUtil.isNotBlank(dto.getQsrq()) ? (dto.getQsrq().replace("-", "/").substring(0, dto.getQsrq().length() - 3) + "签收") : "未签收");
				if ("0".equals(dto.getSfqr())){
					sfqbqr = false;
				}
			}
			kdxx = new StringBuilder(kdxx.substring(1));
			if (sfqbqr){
				wlxxDto.setSfqr("1");
			}else {
				wlxxDto.setSfqr("0");
			}
			boolean b = processWlxx(wlxxDto, kdxx.toString());
			if (!b){
				throw new BusinessException("msg","快递信息保存失败!");
			}
			/*String ICOMM_OAWLTZ007 = xxglService.getMsg("ICOMM_OAWLTZ007");
			String ICOMM_OAWLTZ008 = xxglService.getMsg("ICOMM_OAWLTZ008");
			for (DdxxglDto ddxxglDto : ddxxglDtolist) {
				if(StringUtil.isNotBlank(ddxxglDto.getDdid())){
					// 内网访问
					String internalbtn =applicationurl + urlPrefix
							+ "/ws/storehouse/receiveMateriel/viewYwxxWithWlxx?ywid=" + wlxxDto.getYwid()+"&ywlx="+wlxxDto.getYwlx();
					List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
					OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
					btnJsonList.setTitle("详细");
					btnJsonList.setActionUrl(internalbtn);
					btnJsonLists.add(btnJsonList);
					talkUtil.sendWorkMessage("null", ddxxglDto.getDdid(),ICOMM_OAWLTZ007,StringUtil.replaceMsg(ICOMM_OAWLTZ008,
									wlxxDto.getYwdh(),"ll".equals(wlxxDto.getYwlx())?"领料单":"fh".equals(wlxxDto.getYwlx())?"发货单":"jcjy".equals(wlxxDto.getYwlx())?"借出借用单":"xs".equals(wlxxDto.getYwlx())?"销售单":"",
									kdxx, DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),
							btnJsonLists,"1");
				}
			}*/
		}
		return true;
	}
}
