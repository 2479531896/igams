package com.matridx.igams.common.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.FjcfbModel;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.common.dao.entities.SjycfkDto;
import com.matridx.igams.common.dao.entities.SjycfkModel;
import com.matridx.igams.common.dao.post.ISjycfkDao;
import com.matridx.igams.common.service.svcinterface.ISjycfkService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class SjycfkServiceImpl extends BaseBasicServiceImpl<SjycfkDto, SjycfkModel, ISjycfkDao> implements ISjycfkService{

	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	ICommonService commonService;
	@Value("${matridx.wechat.menuurl:}")
	private String menuurl;
	@Autowired
	DingTalkUtil talkUtil;
	@Autowired
	IXxglService xxglService;
	private final Logger log = LoggerFactory.getLogger(SjycfkServiceImpl.class);
	
	/**
	 * 根据异常ID获取送检异常反馈信息
	 */
	public List<SjycfkDto> getDtoByYcid(SjycfkDto sjycfkDto){
		return dao.getDtoByYcid(sjycfkDto);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean insertDto(SjycfkDto sjycfkDto)
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
		if(sjycfkDto.getFjids()!=null && !sjycfkDto.getFjids().isEmpty()) {
			if("local".equals(sjycfkDto.getFjbcbj())) {
				List<FjcfbModel> fjcfbModels = new ArrayList<>();
				for (int i = 0; i < sjycfkDto.getFjids().size(); i++)
				{
					boolean saveFile = fjcfbService.save2RealFile(sjycfkDto.getFjids().get(i),sjycfkDto.getFkid());
					if (!saveFile)
						return false;

					FjcfbModel t_fjcfbModel = new FjcfbModel();
					t_fjcfbModel.setFjid(sjycfkDto.getFjids().get(i));
					FjcfbModel fjcfbModel = fjcfbService.getModel(t_fjcfbModel);
					fjcfbModels.add(fjcfbModel);
					boolean isSuccess = sendFilesToAli(fjcfbModels);
					if (!isSuccess)
						return false;
				}
			}else {
				MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
				String fjids = StringUtils.join(sjycfkDto.getFjids(), ",");
				paramMap.add("fjids", fjids);
				paramMap.add("ywid", sjycfkDto.getFkid());
				RestTemplate restTemplate = new RestTemplate();
				String param;
				if("dingding".equals(sjycfkDto.getFjbcbj())){

					param=restTemplate.postForObject(menuurl +"/wechat/getFileAddress", paramMap, String.class);
					sjycfkDto.setFjcfparam(param);
				}else{
					param=restTemplate.postForObject(menuurl +"/wechat/getFjcfbModel", paramMap, String.class);
				}

				if(param!=null) {
					JSONArray parseArray = JSONObject.parseArray(param);
					for (Object o : parseArray) {
						FjcfbModel fjcfbModel = JSONObject.toJavaObject((JSONObject) o, FjcfbModel.class);
						fjcfbModel.setYwid(sjycfkDto.getFkid());
						// 下载服务器文件到指定文件夹
						boolean isSuccess = fjcfbService.insert(fjcfbModel);
						downloadFile(fjcfbModel);
						if (!isSuccess)
							return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * 文件下载
	 */
	public boolean downloadFile(FjcfbModel fjcfbModel)
	{
		String wjlj = fjcfbModel.getWjlj();
		String fwjlj = fjcfbModel.getFwjlj();
		DBEncrypt crypt = new DBEncrypt();
		String filePath = crypt.dCode(wjlj);
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.add("wjlj", wjlj);
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try
		{
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(paramMap, headers);
			RestTemplate t_restTemplate = new RestTemplate();
			ResponseEntity<byte[]> response = t_restTemplate.exchange(menuurl + "/wechat/getImportFile", HttpMethod.POST, httpEntity, byte[].class);
			// 校验文件夹目录是否存在，不存在就创建一个目录
			mkDirs(crypt.dCode(fwjlj));
			byte[] result = response.getBody();
			inputStream = new ByteArrayInputStream(result);

			outputStream = new FileOutputStream(filePath);

			int len;
			byte[] buf = new byte[1024];
			while ((len = inputStream.read(buf, 0, 1024)) != -1)
			{
				outputStream.write(buf, 0, len);
			}
			outputStream.flush();
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{
			try
			{
				if (inputStream != null)
					inputStream.close();
				if (outputStream != null)
					outputStream.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return true;
	}
	/**
	 * 根据路径创建文件
	 */
	private boolean mkDirs(String storePath)
	{
		File file = new File(storePath);
		if (file.isDirectory())
		{
			return true;
		}
		return file.mkdirs();
	}

	/**
	 * 获取该送检信息下的所有异常的评论数
	 */
	public List<SjycfkDto> getExceptionPls(SjycfkDto sjycfkDto){
		return dao.getExceptionPls(sjycfkDto);
	}

	/**
	 * 小程序获取异常反馈信息，父反馈ID为空 
	 */
	public List<SjycfkDto> getMiniDtoByYcid(SjycfkDto sjycfkDto){
		List<SjycfkDto> list=dao.getMiniDtoByYcid(sjycfkDto);
		List<String> fkidList=new ArrayList<>();
		for (SjycfkDto sjycfkDto_t:list){
			if("WECHAT_FEEDBACK".equals(sjycfkDto_t.getFkqfdm())){
				sjycfkDto_t.setFkrymc(sjycfkDto_t.getLrrymc());
			}
			fkidList.add(sjycfkDto_t.getFkid());
		}
		if(list!=null && !list.isEmpty()) {
			FjcfbDto fjcfbDto=new FjcfbDto();
			fjcfbDto.setIds(fkidList);
				//根据异常ID查询附件表信息
				List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByIds(fjcfbDto);
				if(fjcfbDtos != null && !fjcfbDtos.isEmpty()){
					for (FjcfbDto dto : fjcfbDtos) {
						String wjmhz = dto.getWjm().substring(dto.getWjm().lastIndexOf(".") + 1);
						dto.setWjmhz(wjmhz);
					}
				}
				if (fjcfbDtos!=null&& !fjcfbDtos.isEmpty()){
					for (SjycfkDto sjycfkDto1:list){
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
	 */
	public List<SjycfkDto> getDtosByGid(SjycfkDto sjycfkDto){
		return dao.getDtosByGid(sjycfkDto);
	}
	
	/**
	 * 根据fkid查找该评论下的所有子评论
	 */
	public List<SjycfkDto> getZplByFkid(SjycfkDto sjycfkDto){
		return dao.getZplByFkid(sjycfkDto);
	}

	public void sendDingMessage(SjycfkDto sjycfkDto){
		String ICOMM_YC00001 = xxglService.getMsg("ICOMM_YC00001");
		String ICOMM_YC00002 = xxglService.getMsg("ICOMM_YC00002");
		String internalbtn;
		//Tzrys格式为：ddid1-zsxm1-yhid1-yhm1,ddid2-zsxm2-yhid2-yhm2
		String str = sjycfkDto.getTzrys();
		String[] tzrys = str.split(",");
		for(String tzry:tzrys){
			String[] split = tzry.split("-");
			internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/exception/discussexception/discussexception&ycid="+sjycfkDto.getYcid(), StandardCharsets.UTF_8);
			//访问链接
			List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> t_btnJsonLists = new ArrayList<>();
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
	}

	public void sendMessageFromOA(SjycfkDto sjycfkDto){
		String ICOMM_YC00001 = xxglService.getMsg("ICOMM_YC00001");
		String ICOMM_YC00002 = xxglService.getMsg("ICOMM_YC00002");
		String internalbtn;
		List<User> users=sjycfkDto.getUsers();
		for(User user_t:users){
			if (StringUtil.isNotBlank(user_t.getDdid())){
				internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/exception/discussexception/discussexception&ycid="+sjycfkDto.getYcid(), StandardCharsets.UTF_8);
				//访问链接
				List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> t_btnJsonLists = new ArrayList<>();
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
	}
	/**
	 * 发送多文件至微信服务器
	 */
	public boolean sendFilesToAli(List<FjcfbModel> fjcfbModels)
	{
		//如果为空，直接返回
		if(StringUtil.isBlank(menuurl)) {
			return true;
		}
		// 拷贝文件到微信服务器
		try
		{
			DBEncrypt dbEncrypt = new DBEncrypt();
			List<ByteArrayResource> contentsAsResources = new ArrayList<>();
			List<FjcfbModel> sendFjcfbModels = new ArrayList<>();
			if (fjcfbModels != null && !fjcfbModels.isEmpty()) {
				for (FjcfbModel fjcfbModel : fjcfbModels) {
					String wjlj = fjcfbModel.getWjlj();
					String pathString = dbEncrypt.dCode(wjlj);
					File file = new File(pathString);
					// 文件不存在不做任何操作
					if (!file.exists())
						break;

					byte[] bytesArray = new byte[(int) file.length()];

					FileInputStream fis = new FileInputStream(file);
					fis.read(bytesArray); // read file into bytes[]
					fis.close();
					// 需要给文件的名称
					ByteArrayResource contentsAsResource = new ByteArrayResource(bytesArray)
					{
						@Override
						public String getFilename()
						{
							return file.getName();
						}
					};
					contentsAsResources.add(contentsAsResource);
					sendFjcfbModels.add(fjcfbModel);
				}

				MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
				for (ByteArrayResource contentsAsResource : contentsAsResources) {
					paramMap.add("files", contentsAsResource);
				}
				paramMap.add("fjcfbModels", sendFjcfbModels);

				// 设置超时时间
				HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
				httpRequestFactory.setConnectionRequestTimeout(6000);
				httpRequestFactory.setConnectTimeout(6000);
				httpRequestFactory.setReadTimeout(240000);
				RestTemplate t_restTemplate = new RestTemplate(httpRequestFactory);
				// 发送文件到服务器
				String reString = t_restTemplate.postForObject(menuurl + "/wechat/upSaveInspReportWithFiles", paramMap, String.class);

				if ("OK".equals(reString)) {
					List<String> ids = new ArrayList<>();
					for (FjcfbModel fjcfbModel : sendFjcfbModels) {
						ids.add(fjcfbModel.getFjid());
					}
					FjcfbModel updatezhbjFjModel = new FjcfbModel();
					updatezhbjFjModel.setIds(ids);
					// 更新文件的转换标记为true
					boolean isSuccess = fjcfbService.updateZhbj(updatezhbjFjModel);
					if (!isSuccess)
						return false;
				}
			}
		} catch (Exception e)
		{
			log.error(e.getMessage());
			return false;
		}
		return true;
	}
}
