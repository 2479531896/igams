package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.MxxxDto;
import com.matridx.igams.wechat.dao.entities.MxxxModel;
import com.matridx.igams.wechat.dao.post.IMxxxDao;
import com.matridx.igams.wechat.service.svcinterface.IMxxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MxxxServiceImpl extends BaseBasicServiceImpl<MxxxDto, MxxxModel, IMxxxDao> implements IMxxxService {


	private Logger log = LoggerFactory.getLogger(MxxxServiceImpl.class);

	@Autowired
	RedisUtil redisUtil;

	/**
	 * 自动生成订单号
	 * @param
	 * @return
	 */
	public String generateDdh() {
		// TODO Auto-generated method stub
		// 生成规则: LL-20201022-01 LL-年份日期-流水号 。
		String date = DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
		String prefix = "MX" + "-" + date + "-";
		// 查询流水号
		String serial = dao.getDdhSerial(prefix);
		return prefix + serial;
	}

	@Override
	public boolean insertInto(MxxxModel mxxxModel) {
		mxxxModel.setMxid(StringUtil.generateUUID());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			Date date = simpleDateFormat.parse(mxxxModel.getPayTime());
			SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			mxxxModel.setPayTime(simpleDateFormat1.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return dao.insert(mxxxModel)!=0;
	}

	@Override
	public boolean updateInfo(MxxxModel mxxxModel) {
		List<JcsjDto> jcList = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.IDCARD_CATEGORY.getCode());
		List<JcsjDto> jcsjDtos = new ArrayList<>();
		if (null != jcList && jcList.size()>0){
			for (JcsjDto jcsjDto:jcList) {
				if (StringUtil.isNotBlank(jcsjDto.getCskz2())){
					jcsjDtos.add(jcsjDto);
				}
			}
		}
		if (null != jcsjDtos && jcsjDtos.size()>0){
			for (JcsjDto jcsjDto:jcsjDtos) {
				if (jcsjDto.getCskz2().equals(mxxxModel.getIdType())){
					mxxxModel.setIdType(jcsjDto.getCsid());
				}
			}
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			Date date = simpleDateFormat.parse(mxxxModel.getOrderTime());
			SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			mxxxModel.setOrderTime(simpleDateFormat1.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dao.update(mxxxModel)!=0;
	}

	@Override
	public MxxxDto getDtoByOrderNo(MxxxDto mxxxDto) {
		return dao.getDtoByOrderNo(mxxxDto);
	}

	public void sendMxMessage(String mxid){
		MxxxDto mxxxDto1 = dao.getDtoById(mxid);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Map<String, Object> map = new HashMap<>();
		map.put("requestType","meditrust.equityinsurance.chackorder");
		map.put("requestId",StringUtil.generateUUID());
		map.put("responseTime",simpleDateFormat.format(new Date()));
		map.put("companyId",StringUtil.generateUUID());
		Map<String, Object> responseData = new HashMap<>();
		responseData.put("mxOrderNo",StringUtil.isNotBlank(mxxxDto1.getMxOrderNo())?mxxxDto1.getMxOrderNo():"");
		responseData.put("orderNo",StringUtil.isNotBlank(mxxxDto1.getOrderNo())?mxxxDto1.getOrderNo():"");
		responseData.put("status",StringUtil.isNotBlank(mxxxDto1.getStatus())?mxxxDto1.getStatus():"");
		responseData.put("hasReport",StringUtil.isNotBlank(mxxxDto1.getHasReport())?mxxxDto1.getHasReport():"");
		responseData.put("reportUrl",StringUtil.isNotBlank(mxxxDto1.getReportUrl())?mxxxDto1.getReportUrl():"");
		responseData.put("statusTime",simpleDateFormat.format(new Date()));
		map.put("responseData",responseData);
		RestTemplate restTemplate=new RestTemplate();
		restTemplate.postForObject("http://172.17.53.135:8086/ws/csdk", map, String.class);
//		MxRequestDto requestDto = JSONObject.parseObject(reString, MxRequestDto.class);
//		if (!requestDto.getResultCode().equals(MxRequestEnum.MX_ReQUEST_INFO_000000.getCode())){
//
//		}
	}
}