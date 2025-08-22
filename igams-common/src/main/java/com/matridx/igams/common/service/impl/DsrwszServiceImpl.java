package com.matridx.igams.common.service.impl;

import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.LscxszDto;
import com.matridx.igams.common.enums.DingMessageType;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.ILscxszService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.dao.entities.DsrwszDto;
import com.matridx.igams.common.dao.entities.DsrwszModel;
import com.matridx.igams.common.dao.post.IDsrwszDao;
import com.matridx.igams.common.service.svcinterface.IDsrwszService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.util.CollectionUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;


@Service
public class DsrwszServiceImpl extends BaseBasicServiceImpl<DsrwszDto, DsrwszModel, IDsrwszDao> implements IDsrwszService{

	@Value("${matridx.dingtalk.jumpdingtalkurl:}")
	private String jumpdingtalkurl;
	@Autowired
	private DingTalkUtil talkUtil;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IDdxxglService ddxxglService;
	@Autowired
	ILscxszService lscxszService;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	private final Logger log = LoggerFactory.getLogger(DsrwszServiceImpl.class);
	/**
	 * 根据任务id获取定时任务信息
	 */
	public DsrwszDto selectDsrwxxByRwid(DsrwszDto dsrwszdto) {
		return dao.selectDsrwxxByRwid(dsrwszdto);
	}
	
	/** 
	 * 插入定时信息到数据库
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(DsrwszDto dsrwszDto){
		dsrwszDto.setRwid(StringUtil.generateUUID());
		int result = dao.insert(dsrwszDto);
		return result != 0;
	}

	/**
	 * 更新定时任务信息
	 */
	public boolean updateDsxx(DsrwszDto dsrwszdto) {
		return dao.updateDsxx(dsrwszdto);
	}
	
	/**
	 * 通过id删除定时任务信息
	 */
	public boolean deleteByRwid(DsrwszDto dsrwszdto) {
		return dao.deleteByRwid(dsrwszdto);
	}

	@Override
	public DsrwszDto getDsrwByZxlAndZxff(DsrwszDto dsrwszDto) {
		return dao.getDsrwByZxlAndZxff(dsrwszDto);
	}

	@Override
	public boolean updateByClassAndName(DsrwszDto dsrwszDto) {
		return dao.updateByClassAndName(dsrwszDto);
	}

	/**
	 * 查询是否符合添加条件
	 */
	@Override
	public boolean queryByRwid(DsrwszDto dsrwszDto) {
		List<DsrwszDto> dsrwszDtos = dao.queryByRwid(dsrwszDto);
		return dsrwszDtos.size() <= 0;
	}


	/**
	 * 获取限制的查询结果
	 *
	 * dsrwszDto
	 *
	 */
	@Override
	public List<DsrwszDto> getDsrwListByLimt(DsrwszDto dsrwszDto){
		return dao.getDsrwListByLimt(dsrwszDto);
	}
	/**
	 * 定时任务共同发送钉钉消息
	 */
	public void commonSendDdMsg(Map<String,Object> map){
		String xxlx = String.valueOf(map.get("xxlx"));
		if (StringUtil.isNotBlank(xxlx)) {
				List<DdxxglDto> ddxxglDtolist = ddxxglService.selectByDdxxlx(xxlx);
				try {
					if (!CollectionUtils.isEmpty(ddxxglDtolist)) {
						String url = ddxxglDtolist.get(0).getCskz1();
						String xxidsStr = ddxxglDtolist.get(0).getCskz2();
						if (StringUtil.isBlank(xxidsStr)){
							throw new BusinessException("msg","commonSendDdMsg------------未维护消息ID！--xxlx="+xxlx);
						}
						String[] xxids = xxidsStr.split(",");
						if (xxids.length<1){
							throw new BusinessException("msg","commonSendDdMsg------------消息ID不符合规范需调整基础数据参数扩展2！--xxidsStr="+xxidsStr);
						}
//						String token = talkUtil.getToken();
						//标题
						String bt = xxglService.getMsg(xxids[0]);
						//内容
						String nr = xxglService.getMsg(xxids[1]);

						//发送临时查询消息
						if (xxlx.contains(DingMessageType.TEMPORARY_QUERY.getCode())) {
							//发送钉钉消息
							String cxid = String.valueOf(map.get("cxid"));
							if (StringUtil.isBlank(cxid)) {
								throw new BusinessException("msg", "commonSendDdMsg------------请确认要发送的临时查询ID！--xxlx=" + xxlx);
							}
							String xsd = "0";//默认手机端,0:手机端,1:PC端
							Object xxdObj = map.get("xsd");
							if (xxdObj!=null){
								xsd = String.valueOf(xxdObj);
							}
							LscxszDto lscxszDto = new LscxszDto();
							lscxszDto.setCxid(cxid);
							String cxtjs = String.valueOf(map.get("cxtjs"));
							if (StringUtil.isNotBlank(cxtjs)) {
								List<String> cxtjList = new ArrayList<>();
								String[] strings = cxtjs.split(";");
								Collections.addAll(cxtjList, strings);
								lscxszDto.setCxtjs(cxtjList);
							}
							LscxszDto t_lscxszDto = lscxszService.selectLscxByCxid(lscxszDto);
							t_lscxszDto.setCxtjs(lscxszDto.getCxtjs());
							for (DdxxglDto ddxxglDto : ddxxglDtolist) {
								String yhid = ddxxglDto.getYhid();
								String cxdm = lscxszService.dealQuerySql(t_lscxszDto, yhid);

								t_lscxszDto.setCxdm(cxdm);
								String cxdmone = lscxszService.dealStatisticsQuerySql(t_lscxszDto, yhid);
								lscxszDto.setCxdm(cxdmone);
								List<HashMap<String, Object>> listResult = lscxszService.getQueryResult(lscxszDto);
								// 判断类型处理
								if("Table".equals(t_lscxszDto.getXsfsdm())) { // 表格
									List<Map<String, Object>> listmap = new ArrayList<>();
									List<String> headList = new ArrayList<>();
									// 遍历list
									for (HashMap<String, Object> stringObjectHashMap : listResult) {
										// 遍历hashmap，取key和value
										Map<String, Object> tmpmap = new HashMap<>(stringObjectHashMap);
										listmap.add(tmpmap);
									}
									Statement stmt = CCJSqlParserUtil.parse(lscxszDto.getCxdm());
									Select select = (Select) stmt;
									SelectBody selectBody = select.getSelectBody();
									if (selectBody instanceof PlainSelect) {
										PlainSelect plainSelect = (PlainSelect) selectBody;
										List<SelectItem> selectitems = plainSelect.getSelectItems();
										for (SelectItem selectitem : selectitems) {
											String itemString = selectitem.toString();
											if (StringUtil.isNotBlank(itemString)) {
												String[] s_item = itemString.split(" ");
												String t_item = s_item[s_item.length - 1];
												headList.add(t_item.substring(!t_item.contains(".") ? 0 : (t_item.indexOf(".") + 1)));
											}
										}
									}
									StringBuilder content = new StringBuilder("### " + t_lscxszDto.getCxmc() + ":  \n");
									if ("0".equals(xsd)){
										List<Integer> lengthList = new ArrayList<>();
										for (String shortTitle : headList) {
											//计算shortTitle的长度
											int length = dealLengthOfStr(shortTitle);
											for (Map<String, Object> stringObjectMap : listmap) {
												String value = String.valueOf(stringObjectMap.get(shortTitle));
												if (length < dealLengthOfStr(value)) {
													length = dealLengthOfStr(value);
												}
											}
											lengthList.add(length);
										}
										String next = "\t";
										for (int i = 0; i < headList.size(); i++) {
											StringBuilder head = new StringBuilder(headList.get(i));
											int size = lengthList.get(i) - dealLengthOfStr(head.toString());
											if (size>0){
												head.append("\t".repeat(size / 4));
											}
											content.append("**").append(head).append("**").append(next);
										}
										content.append("  \n");

										for (Map<String, Object> stringObjectMap : listmap) {
											for (int i = 0; i < headList.size(); i++) {
												String head = headList.get(i);
												StringBuilder value = new StringBuilder(String.valueOf(stringObjectMap.get(head)));
												int size = lengthList.get(i) - dealLengthOfStr(value.toString());
												if (size>0){
													value.append("\t".repeat(size / 4));
												}
												content.append(value).append(next);
											}
											content.append("  \n");
										}
									}else if ("1".equals(xsd)){
										for (int i = 0; i < headList.size()-1; i++) {
											content.append("**").append(headList.get(i)).append("&emsp;").append("**").append(" | ");
										}
										content.append("**").append(headList.get(headList.size() - 1)).append("&emsp;").append("**").append("  \n");
										for (int i = 0; i < headList.size()-1; i++) {
											content.append("-").append(" | ");
										}
										content.append("-").append("  \n");
										//将listmap转换为list转为markdown表格
										for (Map<String, Object> stringObjectMap : listmap) {
											for (int i = 0; i < headList.size()-1; i++) {
												String head = headList.get(i);
												String value = String.valueOf(stringObjectMap.get(head));
												content.append(value).append("&emsp;").append(" | ");
											}
											String lasthead = headList.get(headList.size()-1);
											String lastvalue = String.valueOf(stringObjectMap.get(lasthead));
											content.append(lastvalue).append("&emsp;").append("  \n");
										}
									}
									if (StringUtil.isBlank(url)){
										talkUtil.sendWorkMessage(ddxxglDto.getYhm(),ddxxglDto.getDdid(),bt, content.toString());
									}else {
										url = url.replaceAll("＆", "&").replaceAll("＞", ">").replaceAll("＜", "<");
										url = url.replace("«cxid»",cxid);
										url = url.replaceAll("«cstjs»",cxtjs.replace(";",","));
										List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
										OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
										btnJsonList.setTitle("查看详情");
										btnJsonList.setActionUrl(url);
										btnJsonLists.add(btnJsonList);
										talkUtil.sendCardMessageThread(ddxxglDto.getYhm(),ddxxglDto.getDdid(),bt, content.toString(),btnJsonLists, "1");
									}
								}else{
									throw new BusinessException("msg","commonSendDdMsg------------为选择正确的显示方式！--xxlx="+xxlx);
								}
							}

						}else {
							if (StringUtil.isBlank(url)){
								throw new BusinessException("msg","commonSendDdMsg------------未维护跳转路径！--xxlx="+xxlx);
							}
							//发送普通消息
							for (DdxxglDto ddxxglDto : ddxxglDtolist) {
								if (StringUtil.isNotBlank(ddxxglDto.getDdid())) {
									DBEncrypt p = new DBEncrypt();
									String internalbtn = p.dCode(jumpdingtalkurl);
									try {
										String encodeUrl;
										url = url.replaceAll("＆", "&").replaceAll("＞", ">").replaceAll("＜", "<");
										if (url.contains("?")){
											encodeUrl = "?" + url.substring(url.indexOf("?")+1)+"&";
											internalbtn += url.substring(0,url.indexOf("?"));
										}else {
											encodeUrl = "?";
											internalbtn += url;
										}
										encodeUrl=encodeUrl.replace("«today»",DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd"));
										encodeUrl += "userid=" + ddxxglDto.getDdid() + "&username=" + ddxxglDto.getZsxm();
										if (StringUtil.isNotBlank(urlPrefix)){
											encodeUrl += "&urlPrefix=" + urlPrefix;
										}
										internalbtn = internalbtn + URLEncoder.encode(encodeUrl, StandardCharsets.UTF_8);
									} catch (Exception e) {
										throw new RuntimeException(e);
									}
									log.error("internalbtn:" + internalbtn);
									List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
									OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
									btnJsonList.setTitle("小程序");
									btnJsonList.setActionUrl(internalbtn);
									btnJsonLists.add(btnJsonList);
									talkUtil.sendCardMessageThread(ddxxglDto.getYhm(),
											ddxxglDto.getDdid(),
											bt,
											StringUtil.replaceMsg(nr, DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd")),
											btnJsonLists, "1");
								}
							}
						}
					}else {
						log.error("commonSendDdMsg-----未配置钉钉消息通知人员！---xxlx="+xxlx);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
		}else {
			log.error("commonSendDdMsg----定时任务参数名不符合或为空！");
		}
	}

	public int dealLengthOfStr(String str){
		int len = 0;
		String reg = "[^\\x00-\\xff]";
		for (int i = 0; i < str.length(); i++) {
			if (String.valueOf(str.charAt(i)).matches(reg)) {
				len += 2;
			} else {
				len += 1;
			}
		}
		return len;
	}
}
