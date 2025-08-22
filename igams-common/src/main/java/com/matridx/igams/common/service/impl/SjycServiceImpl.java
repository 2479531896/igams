package com.matridx.igams.common.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.FjcfbModel;
import com.matridx.igams.common.dao.entities.RyczxgDto;
import com.matridx.igams.common.dao.entities.SjycDto;
import com.matridx.igams.common.dao.entities.SjycModel;
import com.matridx.igams.common.dao.entities.SjycStatisticsDto;
import com.matridx.igams.common.dao.entities.SjycfkDto;
import com.matridx.igams.common.dao.entities.SjyctzDto;
import com.matridx.igams.common.dao.post.ISjycDao;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.DingNotificationTypeEnum;
import com.matridx.igams.common.enums.HabitsTypeEnum;
import com.matridx.igams.common.enums.MQTypeEnum;
import com.matridx.igams.common.enums.TwrTypeEnum;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IRyczxgService;
import com.matridx.igams.common.service.svcinterface.ISjycService;
import com.matridx.igams.common.service.svcinterface.ISjycStatisticsService;
import com.matridx.igams.common.service.svcinterface.ISjycfkService;
import com.matridx.igams.common.service.svcinterface.ISjyctzService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.common.util.ExceptionSSEUtil;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class SjycServiceImpl extends BaseBasicServiceImpl<SjycDto, SjycModel, ISjycDao> implements ISjycService{

	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	ISjycfkService sjycfkService;
	@Autowired
	ISjyctzService sjyctzService;
	@Autowired
	private DingTalkUtil talkUtil;
	@Autowired
	IRyczxgService ryczxgService;
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	@Value("${matridx.dingtalk.jumpdingtalkurl:}")
	private String jumpdingtalkurl;
	@Value("${matridx.wechat.menuurl:}")
	private String menuurl;
	@Autowired
	private ExceptionSSEUtil exceptionSSEUtil;
	@Autowired(required = false)
	private AmqpTemplate amqpTempl;

	@Autowired
	ISjycStatisticsService sjycStatisticsService;

	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;


	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean insertDto(SjycDto sjycDto)
	{
		if (StringUtil.isBlank(sjycDto.getYcid()))
		{
			sjycDto.setYcid(StringUtil.generateUUID());
		}
		int result = dao.insert(sjycDto);
		return result != 0;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean addSaveException(SjycDto sjycDto) {
		// TODO Auto-generated method stub
		boolean result = insertDto(sjycDto);
		if(sjycDto.getSjycstatisticsids()!=null&& !sjycDto.getSjycstatisticsids().isEmpty()){
			List<SjycStatisticsDto> sjycStatisticsDtoList=new ArrayList<>();
			for(String statisticsId:sjycDto.getSjycstatisticsids()){
				if(StringUtil.isBlank(statisticsId)){
					continue;
				}
				SjycStatisticsDto sjycStatisticsDto=new SjycStatisticsDto();
				sjycStatisticsDto.setYcid(sjycDto.getYcid());
				sjycStatisticsDto.setSjid(sjycDto.getYwid());
				sjycStatisticsDto.setStatisticsid(StringUtil.generateUUID());
				sjycStatisticsDto.setJcsjcsid(statisticsId);
				sjycStatisticsDtoList.add(sjycStatisticsDto);
			}
			sjycStatisticsService.insertList(sjycStatisticsDtoList);
		}
			if(!result){
				return false;
			}


//		String token = talkUtil.getToken();
		SjycDto t_sjycDto = dao.getDto(sjycDto);
		t_sjycDto.setQrrmc(sjycDto.getQrrmc());
		List<SjycDto> tzrylist= new ArrayList<>();
		List<String> tzrys = sjycDto.getTzrys();
		if (tzrys!=null && !tzrys.isEmpty()){
			List<SjyctzDto> sjycDtos=new ArrayList<>();
			for(String tzry:tzrys){
				String[] split = tzry.split("-");
				SjyctzDto sjyctzDto=new SjyctzDto();
				sjyctzDto.setRyid(split[1]);
				sjyctzDto.setYcid(sjycDto.getYcid());
				sjyctzDto.setLx(split[0]);
				sjyctzDto.setYctzid(StringUtil.generateUUID());
				sjycDtos.add(sjyctzDto);
			}

			boolean addYctz=sjyctzService.insertList(sjycDtos);
			if(addYctz) {
				List<String> jsids=new ArrayList<>();
				List<String> yhids=new ArrayList<>();
				for(SjyctzDto sjyctzDto:sjycDtos){
					if(sjyctzDto.getLx().equals(DingNotificationTypeEnum.ROLE_TYPE.getCode())){
						jsids.add(sjyctzDto.getRyid());
					}else{
						yhids.add(sjyctzDto.getRyid());
					}
				}
				if(!jsids.isEmpty()){
					sjycDto.setTzrys(jsids);
					List<SjycDto> ddidByTzjgs = dao.getDdidByTzjgs(sjycDto);
					if(ddidByTzjgs!=null&& !ddidByTzjgs.isEmpty()){
						for(SjycDto dto:ddidByTzjgs){
							yhids.add(dto.getYhid());
						}
					}
				}
				if(!yhids.isEmpty()){
					sjycDto.setTzrys(yhids);
					tzrylist=dao.getDdidByTzrys(sjycDto);
				}
			}
			exceptionSSEUtil.addExceptionMessage(tzrylist,sjycDto.getYcid());
			//保存人员操作习惯
			for(String ryid:tzrys){
				String[] split = ryid.split("-");
				RyczxgDto ryczxgDto=new RyczxgDto();
				ryczxgDto.setYhid(sjycDto.getLrry());
				if(split[0].equals(DingNotificationTypeEnum.USER_TYPE.getCode())){
					ryczxgDto.setQf(HabitsTypeEnum.USER_HABITS.getCode());
				}else{
					ryczxgDto.setQf(HabitsTypeEnum.ROLE_HABITS.getCode());
				}

				ryczxgDto.setDxid(split[1]);
				ryczxgService.insertOrUpdate(ryczxgDto);
			}
		}
		if (sjycDto.getFjids() != null && !sjycDto.getFjids().isEmpty()) {
			if("local".equals(sjycDto.getFjbcbj())) {
				for (int i = 0; i < sjycDto.getFjids().size(); i++)
				{
					boolean saveFile = fjcfbService.saveRealFileNewId(sjycDto.getFjids().get(i), t_sjycDto.getYcid());
					if (!saveFile)
						return false;
				}
			}else {
				MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
				String fjids = StringUtils.join(sjycDto.getFjids(), ",");
				paramMap.add("fjids", fjids);
				paramMap.add("ywid", t_sjycDto.getYcid());
				RestTemplate restTemplate = new RestTemplate();
				String param;
				if("dingding".equals(sjycDto.getFjbcbj())){

					param=restTemplate.postForObject(menuurl +"/wechat/getFileAddress", paramMap, String.class);
				}else{
					param=restTemplate.postForObject(menuurl +"/wechat/getFjcfbModel", paramMap, String.class);
				}

				if(param!=null) {
					JSONArray parseArray = JSONObject.parseArray(param);
					for (Object o : parseArray) {
						FjcfbModel fjcfbModel = JSONObject.toJavaObject((JSONObject) o, FjcfbModel.class);
						fjcfbModel.setYwid(sjycDto.getYcid());
						// 下载服务器文件到指定文件夹
						boolean isSuccess = fjcfbService.insert(fjcfbModel);
						downloadFile(fjcfbModel);
						if (!isSuccess)
							return false;
					}
				}
			}
		}
		// 获取送检附件IDs
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwid(sjycDto.getYcid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_EXCEPTION.getCode());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		if (fjcfbDtos != null && !fjcfbDtos.isEmpty()) {
			List<String> fjids = new ArrayList<>();
			for (FjcfbDto dto : fjcfbDtos) {
				fjids.add(dto.getFjid());
			}
			sjycDto.setFjids(fjids);
		}
		// 确认角色、人 发送钉钉消息
		// 推送到钉钉
		List<SjycDto> sjycDto_ddids;
		//保存人员操作习惯
		String qf = HabitsTypeEnum.USER_HABITS.getCode();

		if (DingNotificationTypeEnum.ROLE_TYPE.getCode().equals(sjycDto.getQrlx())){
			//确认角色
			sjycDto_ddids = dao.getDdidByQrjs(sjycDto);
			qf = HabitsTypeEnum.ROLE_HABITS.getCode();
		}else {
			sjycDto_ddids = dao.getDdidByQrr(sjycDto);
		}
		exceptionSSEUtil.addExceptionMessage(sjycDto_ddids,sjycDto.getYcid());

		//保存人员操作习惯
		RyczxgDto ryczxgDto=new RyczxgDto();
		ryczxgDto.setYhid(sjycDto.getLrry());
		ryczxgDto.setQf(qf);
		ryczxgDto.setDxid(sjycDto.getQrr());
		ryczxgService.insertOrUpdate(ryczxgDto);
		//发送钉钉消息
		sendExceptionDingtalkMessage(sjycDto_ddids, t_sjycDto, "ICOMM_SJ00039");
		if(!tzrylist.isEmpty()){
			//发送钉钉消息
			sendExceptionDingtalkMessage(tzrylist, t_sjycDto, "ICOMM_SJ00029");
		}
		//新增工作任务
//		GzglDto gzglDto = new GzglDto();
//		gzglDto.setYwid(sjycDto.getYcid());
		SjycDto dtoById = dao.getDtoById(sjycDto.getYcid());
		dtoById.setSjycstatisticsids(sjycDto.getSjycstatisticsids());
		if("WECHAT_EXCEPTION".equals(dtoById.getYcqfdm())&&TwrTypeEnum.WECHAT.getCode().equals(dtoById.getTwrlx())){
			Map<String,Object> rabbitMap=new HashMap<>();
			rabbitMap.put("type","add");
			rabbitMap.put("sjycDto",dtoById);
			amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.SSE_SENDMSG_EXCEPRION_WECHART.getCode(), JSON.toJSONString(rabbitMap));
		}
		return true;
	}

	public void fxsjaddException(SjycDto sjycDto){
		List<SjycDto> sjycDto_ddids = dao.getDdidByQrjs(sjycDto);
		exceptionSSEUtil.addExceptionMessage(sjycDto_ddids,sjycDto.getYcid());
		sendExceptionDingtalkMessage(sjycDto_ddids, sjycDto, "ICOMM_SJ00039");
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
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean modSaveException(SjycDto sjycDto) {
		// TODO Auto-generated method stub
		int result = dao.update(sjycDto);
		SjycStatisticsDto delDto=new SjycStatisticsDto();
		delDto.setYcid(sjycDto.getYcid());
		sjycStatisticsService.delByYcid(delDto);
		if(sjycDto.getSjycstatisticsids()!=null&& !sjycDto.getSjycstatisticsids().isEmpty()){
			List<SjycStatisticsDto> sjycStatisticsDtoList=new ArrayList<>();
			for(String statisticsId:sjycDto.getSjycstatisticsids()){
				if(StringUtil.isBlank(statisticsId)){
					continue;
				}
				SjycStatisticsDto sjycStatisticsDto=new SjycStatisticsDto();
				sjycStatisticsDto.setYcid(sjycDto.getYcid());
				sjycStatisticsDto.setSjid(sjycDto.getYwid());
				sjycStatisticsDto.setStatisticsid(StringUtil.generateUUID());
				sjycStatisticsDto.setJcsjcsid(statisticsId);
				sjycStatisticsDtoList.add(sjycStatisticsDto);
			}
			sjycStatisticsService.insertList(sjycStatisticsDtoList);
		}
		if (result<=0)
			return false;
		List<String> tzrys = sjycDto.getTzrys();

		SjyctzDto sjyctzDto1=new SjyctzDto();

		sjyctzDto1.setYcid(sjycDto.getYcid());
		List<String> oldYhids=new ArrayList<>();
		List<String> oldJsids=new ArrayList<>();
		if(!"ROLE_TYPE".equals(sjycDto.getQrlx())){
			oldYhids.add(sjycDto.getQrr());
		}else{
			oldJsids.add(sjycDto.getQrr());
		}
		sjyctzDto1.setYcid(sjycDto.getYcid());
		List<SjyctzDto> dtoList = sjyctzService.getDtoList(sjyctzDto1);
		if(dtoList!=null&& !dtoList.isEmpty()){
			if(!"ROLE_TYPE".equals(dtoList.get(0).getLx())){
				for(SjyctzDto dto:dtoList){
					if(!oldYhids.contains(dto.getId())){
						oldYhids.add(dto.getId());
					}
				}
			}else{
				for(SjyctzDto dto:dtoList){
					if(!oldJsids.contains(dto.getId())){
						oldJsids.add(dto.getId());
					}
				}
			}
		}
		if(!oldJsids.isEmpty()){
			SjyctzDto sjyctzDto_t=new SjyctzDto();
			sjyctzDto_t.setIds(oldJsids);
			List<SjyctzDto> yhjsList = sjyctzService.getYhjsList(sjyctzDto_t);
			for(SjyctzDto dto:yhjsList){
				if(!oldYhids.contains(dto.getRyid())){
					oldYhids.add(dto.getRyid());
				}
			}
		}
		List<String> newYhids=new ArrayList<>();
		List<String> newJsids=new ArrayList<>();
		if("ROLE_TYPE".equals(sjycDto.getQrlx())){
			newJsids.add(sjycDto.getQrr());
		}else{
			newYhids.add(sjycDto.getQrr());
		}

		if(tzrys!=null&& !tzrys.isEmpty()){
			for(String ryid:tzrys){
				String[] split = ryid.split("-");
				if("ROLE_TYPE".equals(split[0])&&!newJsids.contains(ryid)){
					newJsids.add(ryid);
				}
			}
		}
		if(tzrys!=null&& !tzrys.isEmpty()){
			for(String ryid:tzrys){
				String[] split = ryid.split("-");
				if("USER_TYPE".equals(split[0])&&!newYhids.contains(ryid)){
					newYhids.add(ryid);
				}
			}
		}
		if(!newJsids.isEmpty()){
			SjyctzDto sjyctzDto_t=new SjyctzDto();
			sjyctzDto_t.setIds(newJsids);
			List<SjyctzDto> yhjsList = sjyctzService.getYhjsList(sjyctzDto_t);
			for(SjyctzDto dto:yhjsList){
				if(!newYhids.contains(dto.getRyid())){
					newYhids.add(dto.getRyid());
				}
			}
		}
		List<SjycDto> sjycDtos=new ArrayList<>();
		for(String newYhid:newYhids){
			boolean isFind=false;
			for(String oldYhid:oldYhids){
				if(newYhid.equals(oldYhid)){
					isFind=true;
					break;
				}
			}
			if(!isFind){
				SjycDto sjycDto_t=new SjycDto();
				sjycDto_t.setRyid(newYhid);
				sjycDtos.add(sjycDto_t);
			}
		}
		exceptionSSEUtil.addExceptionMessage(sjycDtos,sjycDto.getYcid());
		SjycDto t_sjycDto = dao.getDto(sjycDto);
		t_sjycDto.setSjycstatisticsids(sjycDto.getSjycstatisticsids());
		if("WECHAT_EXCEPTION".equals(t_sjycDto.getYcqfdm())&&TwrTypeEnum.WECHAT.getCode().equals(t_sjycDto.getTwrlx())){
			Map<String,Object> rabbitMap=new HashMap<>();
			rabbitMap.put("type","xg");
			rabbitMap.put("sjycDto",t_sjycDto);

			amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.SSE_SENDMSG_EXCEPRION_WECHART.getCode(), JSON.toJSONString(rabbitMap));
		}
		if("1".equals(sjycDto.getSfjs())){
			exceptionSSEUtil.finishException(sjycDto.getYcid());
		}
		//先删除原有通知人员
		SjyctzDto sjyctzDto=new SjyctzDto();
		sjyctzDto.setYcid(sjycDto.getYcid());
		sjyctzService.delete(sjyctzDto);
		if(tzrys!=null&& !tzrys.isEmpty()){
			List<SjyctzDto> insertList=new ArrayList<>();
			for(String tzry:tzrys){
				String[] split = tzry.split("-");
				SjyctzDto sjyctzDto_t=new SjyctzDto();
				sjyctzDto_t.setRyid(split[1]);
				sjyctzDto_t.setYcid(sjycDto.getYcid());
				sjyctzDto_t.setLx(split[0]);
				sjyctzDto_t.setYctzid(StringUtil.generateUUID());
				insertList.add(sjyctzDto_t);
			}


			return sjyctzService.insertList(insertList);
		}
		return true;
	}

	/**
	 * 保存异常反馈信息
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean addSaveExceptionFeedback(SjycfkDto sjycfkDto,SjycDto sjycDto){
		boolean isSucces = sjycfkService.insertDto(sjycfkDto);
		if(!isSucces){
			return false;
		}
		int result = dao.updateDto(sjycDto);
		return result > 0;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean delException(SjycDto sjycDto) {
		int reslut=dao.delete(sjycDto);
		SjycStatisticsDto delDto=new SjycStatisticsDto();
		delDto.setYcid(sjycDto.getYcid());
		sjycStatisticsService.delByYcid(delDto);
		return reslut > 0;
	}

	/**
	 * 根据送检id获取异常信息
	 */
	public List<SjycDto> getDtoBySjid(SjycDto sjycDto){
		return dao.getDtoBySjid(sjycDto);
	}

	/**
	 * 选中导出
	 */
	public List<SjycDto> getListForSelectExp(Map<String, Object> params)
	{
		SjycDto sjycDto = (SjycDto) params.get("entryData");
		queryJoinFlagExport(params, sjycDto);
		if("1".equals(sjycDto.getPersonal_flg())){
			//判断伙伴权限
			MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
			paramMap.add("yhid", sjycDto.getRyid());
			RestTemplate t_restTemplate = new RestTemplate();
			List<String> hbmcList = t_restTemplate.postForObject(applicationurl+"/ws/getHbqxList", paramMap, List.class);
			sjycDto.setSjhbs(hbmcList);
		}
		return dao.getListForSelectExp(sjycDto);
	}

	@SuppressWarnings("unchecked")
	private void queryJoinFlagExport(Map<String, Object> params, SjycDto sjycDto)
	{
		List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
		StringBuilder sqlParam = new StringBuilder();
		for (DcszDto dcszDto : choseList)
		{
			if (dcszDto == null || dcszDto.getDczd() == null)
				continue;
			sqlParam.append(",");
			if (StringUtil.isNotBlank(dcszDto.getSqlzd()))
			{
				sqlParam.append(dcszDto.getSqlzd());
			}
			sqlParam.append(" ");
			sqlParam.append(dcszDto.getDczd());
		}
		sjycDto.setSqlParam(sqlParam.toString());
	}

	/**
	 * 根据搜索条件获取导出条数
	 */
	public int getCountForSearchExp(SjycDto sjycDto,Map<String, Object> params) {
		if("1".equals(sjycDto.getPersonal_flg())){
			//判断伙伴权限
			MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
			paramMap.add("yhid", sjycDto.getRyid());
			RestTemplate t_restTemplate = new RestTemplate();
			List<String> hbmcList = t_restTemplate.postForObject(applicationurl+"/ws/getHbqxList", paramMap, List.class);
			sjycDto.setSjhbs(hbmcList);
		}else {
			String jsid = (String) params.get("jsid");
			List<Map<String,String>> jcdwList=dao.getJsjcdwByjsid(jsid);
			if(jcdwList!=null&& !jcdwList.isEmpty()){
				//判断检测单位是否为1（单位限制）
				if("1".equals(jcdwList.get(0).get("dwxdbj"))) {
					//取出检测单位为一个List
					List<String> strList = new ArrayList<>();
					for (Map<String, String> stringStringMap : jcdwList) {
						if (stringStringMap.get("jcdw") != null) {
							strList.add(stringStringMap.get("jcdw"));
						}
					}
					//如果检测单位不为空，进行查询。
					if(!strList.isEmpty()) {
						sjycDto.setJcdwxz(strList);
					}
				}
			}
		}
		return dao.getCountForSearchExp(sjycDto);
	}

	/**
	 * 根据搜索条件分页获取导出信息
	 */
	public List<SjycDto> getListForSearchExp(Map<String,Object> params){
		SjycDto sjycDto = (SjycDto)params.get("entryData");
		queryJoinFlagExport(params,sjycDto);
		if("1".equals(sjycDto.getPersonal_flg())){
			//判断伙伴权限
			MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
			paramMap.add("yhid", sjycDto.getRyid());
			RestTemplate t_restTemplate = new RestTemplate();
			List<String> hbmcList = t_restTemplate.postForObject(applicationurl+"/ws/getHbqxList", paramMap, List.class);
			sjycDto.setSjhbs(hbmcList);
		}else {
			String jsid = (String) params.get("jsid");
			List<Map<String,String>> jcdwList=dao.getJsjcdwByjsid(jsid);
			if(jcdwList!=null&& !jcdwList.isEmpty()){
				//判断检测单位是否为1（单位限制）
				if("1".equals(jcdwList.get(0).get("dwxdbj"))) {
					//取出检测单位为一个List
					List<String> strList = new ArrayList<>();
					for (Map<String, String> stringStringMap : jcdwList) {
						if (stringStringMap.get("jcdw") != null) {
							strList.add(stringStringMap.get("jcdw"));
						}
					}
					//如果检测单位不为空，进行查询。
					if(!strList.isEmpty()) {
						sjycDto.setJcdwxz(strList);
					}
				}
			}
		}
		return dao.getListForSearchExp(sjycDto);
	}

	/**
	 * 结束异常任务
	 */
	@Override
	public boolean finishYc(SjycDto sjycDto) {
		return dao.finishYc(sjycDto);
	}

	/**
	 * 异常转发
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean exceptionRepeat(SjycDto sjycDto) {
		SjycDto sjycDto1=getDto(sjycDto);
		List<String> oldYhids=new ArrayList<>();
		List<String> oldJsids=new ArrayList<>();
		if("ROLE_TYPE".equals(sjycDto1.getQrlx())){
			oldJsids.add(sjycDto1.getQrr());
		}else{
			oldYhids.add(sjycDto1.getQrr());
		}
		SjyctzDto sjyctzDto=new SjyctzDto();
		sjyctzDto.setYcid(sjycDto.getYcid());
		List<SjyctzDto> dtoList = sjyctzService.getDtoList(sjyctzDto);
		if(dtoList!=null&& !dtoList.isEmpty()){
			if("ROLE_TYPE".equals(dtoList.get(0).getLx())){
				for(SjyctzDto dto:dtoList){
					if(!oldJsids.contains(dto.getId())){
						oldJsids.add(dto.getId());
					}
				}
			}else{
				for(SjyctzDto dto:dtoList){
					if(!oldYhids.contains(dto.getId())){
						oldYhids.add(dto.getId());
					}
				}
			}
		}
		if(!oldJsids.isEmpty()){
			SjyctzDto sjyctzDto_t=new SjyctzDto();
			sjyctzDto_t.setIds(oldJsids);
			List<SjyctzDto> yhjsList = sjyctzService.getYhjsList(sjyctzDto_t);
			for(SjyctzDto dto:yhjsList){
				if(!oldYhids.contains(dto.getRyid())){
					oldYhids.add(dto.getRyid());
				}
			}
		}
		boolean result;
		StringBuilder stringBuffer=new StringBuilder();
		List<String> newYhids=new ArrayList<>();
		List<String> newJsids=new ArrayList<>();
		if("ROLE_TYPE".equals(sjycDto.getQrlx())){
			newJsids.add(sjycDto.getQrr());
		}else{
			newYhids.add(sjycDto.getQrr());
		}
		if ("ROLE_TYPE".equals(sjycDto.getTzlx())){
			if(sjycDto.getTzrys()!=null&& !sjycDto.getTzrys().isEmpty()){
				List<String> mcs=getxtjsmcs(sjycDto.getTzrys());
				stringBuffer.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				stringBuffer.append("\n");
				stringBuffer.append(sjycDto.getZfrymc());
				stringBuffer.append("将");
				stringBuffer.append(sjycDto1.getYcbt());
				stringBuffer.append("转发给角色:");
				for (int i=0;i<mcs.size();i++){
					if (StringUtil.isNotBlank(mcs.get(i))){
						stringBuffer.append(mcs.get(i));
						if (i<mcs.size()-1) {
							stringBuffer.append(",");
						}
					}
				}
				for(String ryid:sjycDto.getTzrys()){
					if(!newJsids.contains(ryid)){
						newJsids.add(ryid);
					}
				}
			}
		}
		if ("USER_TYPE".equals(sjycDto.getTzlx())){
			if(sjycDto.getTzrys()!=null&& !sjycDto.getTzrys().isEmpty()){
				List<String> mcs=getxtyhmcs(sjycDto.getTzrys());
				stringBuffer.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				stringBuffer.append("\n");
				stringBuffer.append(sjycDto.getZfrymc());
				stringBuffer.append("将");
				stringBuffer.append(sjycDto1.getYcbt());
				stringBuffer.append("转发给用户:");
				for (int i=0;i<mcs.size();i++){
					if (StringUtil.isNotBlank(mcs.get(i))){
						stringBuffer.append(mcs.get(i));
						if (i<mcs.size()-1) {
							stringBuffer.append(",");
						}
					}
				}
				//保存人员操作习惯
				for(String ryid:sjycDto.getTzrys()){
					RyczxgDto ryczxgDto=new RyczxgDto();
					ryczxgDto.setYhid(sjycDto.getXgry());
					ryczxgDto.setQf(HabitsTypeEnum.USER_HABITS.getCode());
					ryczxgDto.setDxid(ryid);
					ryczxgService.insertOrUpdate(ryczxgDto);
					if(!newYhids.contains(ryid)){
						newYhids.add(ryid);
					}
				}
			}
		}
		if(!newJsids.isEmpty()){
			SjyctzDto sjyctzDto_t=new SjyctzDto();
			sjyctzDto_t.setIds(newJsids);
			List<SjyctzDto> yhjsList = sjyctzService.getYhjsList(sjyctzDto_t);
			for(SjyctzDto dto:yhjsList){
				if(!newYhids.contains(dto.getRyid())){
					newYhids.add(dto.getRyid());
				}
			}
		}
		stringBuffer.append("\n");
		stringBuffer.append("确认人:");
		stringBuffer.append(sjycDto.getQrrmc());
		sjycDto.setZfjl(stringBuffer +"\n");
		result=updateDto(sjycDto);
		//更新确认人、确认类型
		boolean updateQrr=dao.updateQrr(sjycDto);
		if (updateQrr){
			//更新通知类型、通知人 并发送通知消息
			updatePower(sjycDto);
		}
		SjycDto t_sjycDto = dao.getDto(sjycDto);
		// 推送到钉钉（确认消息）
		List<SjycDto> sjycDto_ddids;
		if (sjycDto.getQrlx().equals(DingNotificationTypeEnum.USER_TYPE.getCode())){
			//保存人员操作习惯
			RyczxgDto ryczxgDto=new RyczxgDto();
			ryczxgDto.setYhid(sjycDto.getXgry());
			ryczxgDto.setQf(HabitsTypeEnum.USER_HABITS.getCode());
			ryczxgDto.setDxid(sjycDto.getQrr());
			ryczxgService.insertOrUpdate(ryczxgDto);
			//确认人员
			sjycDto_ddids = dao.getDdidByQrr(sjycDto);
		}else {
			//确认角色
			sjycDto_ddids = dao.getDdidByQrjs(sjycDto);
		}
		//发送钉钉消息
		sendExceptionDingtalkMessage(sjycDto_ddids, t_sjycDto, "ICOMM_SJ00039");
		List<SjycDto> sjycDtos=new ArrayList<>();
		for(String newYhid:newYhids){
			boolean isFind=false;
			for(String oldYhid:oldYhids){
				if(newYhid.equals(oldYhid)){
					isFind=true;
					break;
				}
			}
			if(!isFind){
				SjycDto sjycDto_t=new SjycDto();
				sjycDto_t.setRyid(newYhid);
				sjycDtos.add(sjycDto_t);
			}
		}
		exceptionSSEUtil.addExceptionMessage(sjycDtos,sjycDto.getYcid());
		if("WECHAT_EXCEPTION".equals(t_sjycDto.getYcqfdm())){
			Map<String,Object> rabbitMap=new HashMap<>();
			rabbitMap.put("type","zf");
			rabbitMap.put("sjycDto",sjycDto);
			amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.SSE_SENDMSG_EXCEPRION_WECHART.getCode(), JSON.toJSONString(rabbitMap));
		}
		return result;
	}

	/**
	 * 小程序获取异常清单
	 */
	public List<SjycDto> getMiniDtoList(SjycDto sjycDto){
		return dao.getMiniDtoList(sjycDto);
	}

	/**
	 * 异常置顶
	 */
	public boolean setExceptionTop(SjycDto sjycDto) {
		return dao.setExceptionTop(sjycDto);
	}

	/**
	 * 小程序个人清单(被通知的用户都可以看到)
	 */
	public List<SjycDto> getMiniPersonalList(SjycDto sjycDto){
		return dao.getMiniPersonalList(sjycDto);
	}
	/**
	 * 获取用户所有角色
	 */
	public List<SjycDto> getYhjsList(SjycDto sjycDto){
		return dao.getYhjsList(sjycDto);
	}

	/**
	 * 钉钉小程序更新异常权限
	 */
	public boolean updatePower(SjycDto sjycDto) {
		boolean result=false;
		//更新通知人员
		SjyctzDto sjyctzDto=new SjyctzDto();
		sjyctzDto.setYcid(sjycDto.getYcid());
		sjyctzDto.setLx(sjycDto.getTzlx());
		//先删除原有通知人员
		sjyctzService.delete(sjyctzDto);
		if(sjycDto.getTzrys()!=null && !sjycDto.getTzrys().isEmpty()) {
			List<SjyctzDto> insertList=new ArrayList<>();
			for(String tzry:sjycDto.getTzrys()){
				SjyctzDto sjyctzDto_t=new SjyctzDto();
				sjyctzDto_t.setRyid(tzry);
				sjyctzDto_t.setYcid(sjycDto.getYcid());
				sjyctzDto_t.setLx(sjycDto.getTzlx());
				sjyctzDto_t.setYctzid(StringUtil.generateUUID());
				insertList.add(sjyctzDto_t);
			}

			result=sjyctzService.insertList(insertList);
		}
		if (result){
			SjycDto t_sjycDto = dao.getDto(sjycDto);
//			String token = talkUtil.getToken();
			if (sjycDto.getTzrys()!=null && !sjycDto.getTzrys().isEmpty()){
				List<SjycDto> list;
				//通知人员
				if(sjycDto.getTzlx().equals(DingNotificationTypeEnum.USER_TYPE.getCode())) {
					list=dao.getDdidByTzrys(sjycDto);
				}else {
					//通知机构
					list=dao.getDdidByTzjgs(sjycDto);
				}
				//发送钉钉消息
				sendExceptionDingtalkMessage(list, t_sjycDto, "ICOMM_SJ00029");
			}
		}
		return result;
	}

	@Override
	public List<String> getxtjsmcs(List<String> list) {
		return dao.getxtjsmcs(list);
	}

	@Override
	public List<String> getxtyhmcs(List<String> list) {
		return dao.getxtyhmcs(list);
	}

	/**
	 * 发送带有异常查看链接的钉钉消息
	 */
	public boolean sendExceptionDingtalkMessage(List<SjycDto> sjycDtoList,SjycDto sjycDto,String xxid){
//		DBEncrypt p = new DBEncrypt();
//		String dingtalkurl=p.dCode(jumpdingtalkurl);
		//消息替换map
		Map<String, Object> xxglmap = new HashMap<>();
		xxglmap.put("sjycDto",sjycDto);
		xxglmap.put("sj",DateUtils.getCustomFomratCurrentDate("HH:mm:ss"));
		if (sjycDtoList!=null && !sjycDtoList.isEmpty()){
			for (SjycDto sjycDto_ddid : sjycDtoList) {
				//小程序访问
				String dingtalkbtn;
				dingtalkbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/exception/discussexception/discussexception&ycid="+sjycDto.getYcid()+"&=urlPrefix"+urlPrefix, StandardCharsets.UTF_8);
				List<BtnJsonList> t_btnJsonLists = new ArrayList<>();
				BtnJsonList t_btnJsonList = new BtnJsonList();
				t_btnJsonList.setTitle("小程序");
				t_btnJsonList.setActionUrl(dingtalkbtn);
				t_btnJsonLists.add(t_btnJsonList);
				if (sjycDto_ddid != null && StringUtil.isNotBlank(sjycDto_ddid.getYhm()))
					talkUtil.sendCardMessage(sjycDto_ddid.getYhm(), sjycDto_ddid.getDdid(), xxglService.getMsg("ICOMM_SJ00028"), xxglService.getReplaceMsg(xxid,xxglmap),t_btnJsonLists,"1");
			}
			return true;
		}
		return false;
	}

	/**
	 * 查询角色检测单位限制
	 */
	public List<Map<String, String>> getJsjcdwByjsid(String jsid){
		return dao.getJsjcdwByjsid(jsid);
	}

	/**
	 * 根据业务ID获取送检信息
	 */
	public SjycDto getSjxxByYwid(String ywid){
		return dao.getSjxxByYwid(ywid);
	}
	/**
	 * 根据业务ID获取送检信息
	 */
	public List<SjycDto> getSjxxByYwids(List<String> sjids){
		return dao.getSjxxByYwids(sjids);
	}
	
	@Override
	public boolean sendFkMessage(List<SjycDto> sjycDtoList, SjycfkDto sjycfkDto,boolean isFin) {
		if (sjycDtoList!=null && !sjycDtoList.isEmpty()){
			List<SjycDto> sjycDto_ddids=dao.getDdidByYhids(sjycDtoList);
			SjycDto sjycDto=new SjycDto();
			sjycDto.setYcid(sjycfkDto.getYcid());
			SjycDto t_sjycDto = dao.getDto(sjycDto);
			t_sjycDto.setFknr(sjycfkDto.getFkxx());
			t_sjycDto.setMc(sjycfkDto.getLrrymc());
			sendExceptionDingtalkMessage_fk(sjycDto_ddids, t_sjycDto, "ICOMM_SJ00066", jumpdingtalkurl, null,isFin);
		}
		return false;
	}

	/**
	 * 发送带有异常查看链接的钉钉消息
	 */
	public boolean sendExceptionDingtalkMessage_fk(List<SjycDto> sjycDtoList,SjycDto sjycDto,String xxid,String jumpdingtalkurl,String token,boolean isFin){
		if(isFin){
			xxid="ICOMM_SJ00067";
		}
//		DBEncrypt p = new DBEncrypt();
//		String dingtalkurl=p.dCode(jumpdingtalkurl);
		//消息替换map
		Map<String, Object> xxglmap = new HashMap<>();
		xxglmap.put("sjycDto",sjycDto);
		xxglmap.put("sj",DateUtils.getCustomFomratCurrentDate("HH:mm:ss"));
		if (sjycDtoList!=null && !sjycDtoList.isEmpty()){
			for (SjycDto sjycDto_ddid : sjycDtoList) {
//				if(!exceptionSSEUtil.isSendDD(sjycDto_ddid.getYhid(),sjycDto.getYcid())&&!isFin){
//					continue;
//				}

				//小程序访问
				String dingtalkbtn;
				if(isFin){
					dingtalkbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/exception/discussexception/discussexception&ycid="+sjycDto.getYcid()+"&sfjs=1"+"&urlPrefix="+urlPrefix, StandardCharsets.UTF_8);
				}else{
					dingtalkbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/exception/discussexception/discussexception&ycid="+sjycDto.getYcid()+"&urlPrefix="+urlPrefix, StandardCharsets.UTF_8);
				}
				List<BtnJsonList> t_btnJsonLists = new ArrayList<>();
				BtnJsonList t_btnJsonList = new BtnJsonList();
				t_btnJsonList.setTitle("小程序");
				t_btnJsonList.setActionUrl(dingtalkbtn);
				t_btnJsonLists.add(t_btnJsonList);
				if (StringUtil.isNotBlank(sjycDto_ddid.getYhm()))
					talkUtil.sendCardMessage(sjycDto_ddid.getYhm(), sjycDto_ddid.getDdid(), xxglService.getMsg("ICOMM_SJ00028"), xxglService.getReplaceMsg(xxid,xxglmap),t_btnJsonLists,"1");
			}
			return true;
		}
		return false;
	}

	/**
	 * 异常评价保存
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean evaluation(SjycDto sjycDto){
		return dao.evaluation(sjycDto);
	}

	/**
	 * 更新投诉标记字段
	 * sjycDto
	 *
	 */
	public boolean updateTsbj(SjycDto sjycDto){
		return dao.updateTsbj(sjycDto);
	}
}
