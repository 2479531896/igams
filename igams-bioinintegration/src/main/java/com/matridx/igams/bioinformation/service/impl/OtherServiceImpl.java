package com.matridx.igams.bioinformation.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.bioinformation.dao.entities.OtherDto;
import com.matridx.igams.bioinformation.dao.entities.OtherModel;
import com.matridx.igams.bioinformation.dao.post.IOtherDao;
import com.matridx.igams.bioinformation.service.svcinterface.IOtherService;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OtherServiceImpl extends BaseBasicServiceImpl<OtherDto, OtherModel, IOtherDao> implements IOtherService {

	@Autowired
	RedisUtil redisUtil;
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;

	@Override
	public OtherDto getSjxxDto(OtherDto otherDto) {
		OtherDto sjxxDto = dao.getSjxxDto(otherDto);
		List<OtherDto> dtoList = dao.getSjyzDtoList(sjxxDto);
//		String pcr = "";
//		if ( null != dtoList && dtoList.size()>0){
//
//			for (OtherDto dto : dtoList) {
//				if(dto!=null){
//					if(pcr.equals("")){
//						pcr += dto.getCsmc();
//					}else{
//						pcr += ","+dto.getCsmc();
//					}
//
//				}
//
//			}
//
//		}
		if (null != sjxxDto){
			sjxxDto.setPcr(dtoList);
		}
		return sjxxDto;
	}

	@Override
	public List<OtherDto> getSjxxDtoList(OtherDto otherDto) {
		return dao.getSjxxDtoList(otherDto);
	}

	@Override
	public Map<String,Object> getReviewUserInfo(OtherDto otherDto) {
		Map<String,Object> map= new HashMap<>();
		otherDto.setMrfz("1");
		List<OtherDto> xtyhDtos = dao.getListByYhm(otherDto);
		otherDto.setMrfz("ALL");
		List<OtherDto> xtyhDtoList= dao.getListByYhm(otherDto);
		Map<String, List<OtherDto>> collect = xtyhDtos.stream().collect(Collectors.groupingBy(OtherDto::getMrfz));
		Iterator<Map.Entry<String, List<OtherDto>>> entries = collect.entrySet().iterator();
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter(OtherDto.class, "yhm","zsxm","yhid");
		SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
		listFilters[0] = filter;
		while (entries.hasNext()) {
			Map.Entry<String, List<OtherDto>> entry = entries.next();
			List<OtherDto> dtoList = entry.getValue();
			if (null != xtyhDtoList && !xtyhDtoList.isEmpty()){
				dtoList.addAll(xtyhDtoList);
			}
			if ("JYRY".equals( entry.getKey())){
				map.put("JYRY", JSONObject.toJSONString(dtoList,listFilters));
			}
			if ("SHRY".equals( entry.getKey())){
				map.put("SHRY",JSONObject.toJSONString(dtoList,listFilters));
			}
		}
		return map;
	}
	/**
	 * 查询角色检测单位限制
	 */
	@Override
	public List<Map<String, String>> getJsjcdwByjsid(String jsid)
	{
		// TODO Auto-generated method stub
		return dao.getJsjcdwByjsid(jsid);
	}

	/**
	 * 通过用户id查询对应的伙伴id
	 */
	@Override
	public List<String> getHbidByYhid(String yhid){
		// TODO Auto-generated method stub
		return dao.getHbidByYhid(yhid);
	}

	/**
	 * 通过hbid查询 合作伙伴
	 */
	@Override
	public List<String> getHbmcByHbid(List<String> list){
		// TODO Auto-generated method stub
		return dao.getHbmcByHbid(list);
	}


	/**
	 * 从Dto里把数据放到Map里，减少Dto的属性设置，防止JSON出错
	 */
	public Map<String,Object> pareMapFromDto(OtherDto otherDto){
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("hzxm", otherDto.getHzxm());
		paramMap.put("nl", otherDto.getNl());
		paramMap.put("xb", otherDto.getXb());
		paramMap.put("yblx", otherDto.getYblx());
		paramMap.put("yblxmc", otherDto.getYblxmc());
		paramMap.put("ybbh", otherDto.getYbbh());
		paramMap.put("nbbm", otherDto.getNbbm());
		paramMap.put("zsxm",otherDto.getZsxm());
		paramMap.put("userids",otherDto.getUserids());
		paramMap.put("sjhbs",otherDto.getSjhbs());
		paramMap.put("jcdwxz",otherDto.getJcdwxz());
		paramMap.put("sortName",otherDto.getSortName());
		paramMap.put("sortOrder",otherDto.getSortOrder());
		paramMap.put("sortLastName",otherDto.getSortLastName());
		paramMap.put("sortLastOrder",otherDto.getSortLastOrder());
		paramMap.put("pageSize",otherDto.getPageSize());
		paramMap.put("pageNumber",otherDto.getPageNumber());
		paramMap.put("pageStart",(otherDto.getPageNumber()-1) * otherDto.getPageSize());
		paramMap.put("sjys",otherDto.getSjys());
		return paramMap;
	}

	/**
	 * 分页查询送检信息(优化)
	 */
	@Override
	public List<OtherDto> getDtoListOptimize(Map<String,Object> params) {
		return dao.getDtoListOptimize(params);
	}

	/**
	 * 查找用户信息
	 */
	public OtherDto getXtyhByYhid(String yhid){
		return dao.getXtyhByYhid(yhid);
	}

	/**

	 * 定时任务获取blast token
	 */
	public void getSxToken() {
		try {
			DBEncrypt dbEncrypt = new DBEncrypt();
			List<NameValuePair> pairs = new ArrayList<>();
			pairs.add(new BasicNameValuePair("client_id", "medlab"));
			org.apache.commons.codec.binary.Base64 base64 = new Base64();
			String enPass = base64.encodeToString("Matridx123!".getBytes());
			pairs.add(new BasicNameValuePair("client_secret", enPass));
			pairs.add(new BasicNameValuePair("grant_type", "matridx"));
			pairs.add(new BasicNameValuePair("sign", dbEncrypt.eCode("Matridx123!")));
			CloseableHttpClient httpclient = null;
			CloseableHttpResponse httpresponse = null;
			try {
				httpclient = HttpClients.createDefault();
				HttpPost httppost = new HttpPost(applicationurl+"/oauth/token");
				// StringEntity stringentity = new StringEntity(data);
				httppost.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
				httpresponse = httpclient.execute(httppost);
				String response = EntityUtils.toString(httpresponse.getEntity());

				Map<String, Object> returnMap = JSONObject.parseObject(response, new TypeReference<Map<String, Object>>(){});
				String token = returnMap.get("access_token").toString();
				redisUtil.set("BLAST_TOKEN", token, 60 * 60 * 2);
			} finally {
				if (httpclient != null) {
					httpclient.close();
				}
				if (httpresponse != null) {
					httpresponse.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * 获取资源操作权限
	 * @param
	 * @return
	 */
	public List<OtherDto> getJszyczb(OtherDto otherDto){
		return dao.getJszyczb(otherDto);
	}

	@Override
	public boolean updateWksjmx(OtherDto otherDto) {
		return dao.updateWksjmx(otherDto);
	}

	@Override
	public boolean updateWksjmxList(List<OtherDto> list) {
		return dao.updateWksjmxList(list);
	}

	@Override
	public List<Map<String, String>> getWksjmxByWkbmOrYbbh(OtherDto otherDto) {
		return dao.getWksjmxByWkbmOrYbbh(otherDto);
	}

	@Override
	public List<Map<String,Object>> getwzzbx(Map<String, String> map) {
		return dao.getwzzbx(map);
	}

	@Override
	public boolean batchInsertMrzk(List<Map<String, String>> list) {
		return dao.batchInsertMrzk(list);
	}

	@Override
	public Map<String, String> getWkgl(String wkbm) {
		return dao.getWkgl(wkbm);
	}
}
