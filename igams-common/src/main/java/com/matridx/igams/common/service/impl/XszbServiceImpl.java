package com.matridx.igams.common.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.entities.ImportRecordModel;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XszbDto;
import com.matridx.igams.common.dao.entities.XszbModel;
import com.matridx.igams.common.dao.post.IXszbDao;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class XszbServiceImpl extends BaseBasicServiceImpl<XszbDto, XszbModel, IXszbDao> implements IXszbService,IFileImport{

	@Autowired
	IJcsjService jcsjService;
	@Autowired
	ILshkService lshkservice;
	@Autowired
	IXxglService xxglService;
	@Autowired
	private RedisUtil redisUtil;
	
	@Override
	public boolean existCheck(String fieldName, String value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertImportRec(BaseModel baseModel, User user,int rowindex,StringBuffer errorMessages) {
		try{
			//xlsx的表格中的数据是工号，姓名，开始周期。结束周期。指标
			//要导入数据库中，有zbid，yhid，zblx，kszq，jszq，sz
			//zbid是uuid随机生成，yhid是根据根据导入文件的工号找到yhid
			XszbDto xszbDto = (XszbDto)baseModel;
			
			xszbDto.setLrry(user.getYhid());
			xszbDto.setZbid(StringUtil.generateUUID());

//			XszbDto xszbDto_t = dao.getYhxxByYhm(xszbDto.getGh());//一行一行处理：根据工号yhm找到为null的yhid情况要处理，重复的要处理
			xszbDto.setYhid(xszbDto.getGh());
			
			XszbDto repeatDto = dao.getXszbRepeat(xszbDto);
			if(repeatDto == null) {
				dao.insertDto(xszbDto);//inser之前先判断是否重复
			}else {//DB存在相同数据
				repeatDto.setXgry(user.getYhid());
				repeatDto.setSz(xszbDto.getSz());
				dao.updateDto(repeatDto);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public String cellTransform(String tranTrack, String value, ImportRecordModel recModel, StringBuffer errorMessage) {
		if(tranTrack.equalsIgnoreCase("XS001")){//销售指标类型  second before
//			JcsjDto jcsjDto = new JcsjDto();
//			jcsjDto.setJclb(BasicDataTypeEnum.SALE_TYPE.getCode());
//			jcsjDto.setCsdm(value);
			List<JcsjDto> jcsjDtos = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.SALE_TYPE.getCode());
			JcsjDto t_jcsjDto = null;
			for (JcsjDto jcpt : jcsjDtos){
				if (value.equals(jcpt.getCsdm()) || value.equals(jcpt.getCsmc())){
					t_jcsjDto = jcpt;
				}
			}
			if(t_jcsjDto== null){
				errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
				.append("列，基础数据中找不到对应的销售指标类型的参数代码，单元格值为：").append(value).append("；<br>");
			}
			else{
				return t_jcsjDto.getCsid();
			}
		}else if(tranTrack.equalsIgnoreCase("XS002")){//工号（可能为yhm，也可能是基础数据平台的参数名称, 也可能是伙伴名称）
			XszbDto xszbDto_yhm = dao.getYhxxByYhm(value);//查用户工号，用户scbj !=1
            if(xszbDto_yhm == null ) {
				List<XszbDto> xszb_sjhb = dao.getSjhbByHbmc(value);//查伙伴名称, 伙伴scbj ='0'
				if (xszb_sjhb == null || xszb_sjhb.isEmpty()){
					//查平台基础数据
					List<JcsjDto> jcsjDtos = redisUtil.lgetDto("All_matridx_jcsj:"+ BasicDataTypeEnum.PLATFORM_OWNERSHIP.getCode());
					JcsjDto t_jcsjDto = null;
					for (JcsjDto jcpt : jcsjDtos){
						if (value.equals(jcpt.getCsmc())){
							t_jcsjDto = jcpt;
						}
					}
					if(t_jcsjDto== null){
						errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
								.append("列，请确认工号填写是否正确，工号内容是否存在，单元格值为：").append(value).append("；<br>");
					}else{
						return t_jcsjDto.getCsid();
					}
				}else{
					return xszb_sjhb.get(0).getYhid();
				}
			}else {
				return xszbDto_yhm.getYhid();
			}
		}else if(tranTrack.equalsIgnoreCase("XS003")){//分类3，子分类4,填写的是参数名称
			if (StringUtil.isNotBlank(value)){
				List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.SALE_CLASSIFY.getCode());
				JcsjDto t_jcsjDto = null;
				for (JcsjDto jcfl : jcsjDtos){
					if (value.equals(jcfl.getCsmc())){
						t_jcsjDto = jcfl;
					}
				}
				if(t_jcsjDto== null){
					errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
							.append("列，基础数据中找不到对应的销售分类的参数名称，单元格值为：").append(value).append("；<br>");
				}else{
					return t_jcsjDto.getCsid();
				}
			}else {
				return null;
			}
		}else if(tranTrack.equalsIgnoreCase("XS004")){//分类3，子分类4,填写的是参数名称
			if (StringUtil.isNotBlank(value)){
				List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.SALE_SUBCLASSIFY.getCode());
				JcsjDto t_jcsjDto = null;
				for (JcsjDto jcfl : jcsjDtos){
					if (value.equals(jcfl.getCsmc())){
						t_jcsjDto = jcfl;
					}
				}
				if(t_jcsjDto== null){
					errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
							.append("列，基础数据中找不到对应的销售分类的参数名称，单元格值为：").append(value).append("；<br>");
				}else{
					return t_jcsjDto.getCsid();
				}
			}else {
				return null;
			}
		}
		
		return null;
	}

	@Override
	public boolean insertNormalImportRec(Map<String, String> recMap, User user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkDefined(List<Map<String, String>> defined) {
		// TODO Auto-generated method stub first befer
		return true;
	}

	@Override
	public List<XszbDto> getXszbQyxx(XszbDto xszbDto) {
		List<JcsjDto> jc_zflList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.USER_SUB_DISTINGUISH.getCode());
		List<JcsjDto> jc_flList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.USER_DISTINGUISH.getCode());
		JcsjDto jc_yh = new JcsjDto();
		for (JcsjDto yh : jc_flList) {
			if (yh.getCsmc().equals(xszbDto.getJsmc())) {
				jc_yh = yh;
				break;
			}
		}
		List<XszbDto> dqList = new ArrayList<>();
		for (JcsjDto zfl : jc_zflList) {
			if (StringUtil.isNotBlank(zfl.getFcsid()) && zfl.getFcsid().equals(jc_yh.getCsid())) {
				XszbDto dq = new XszbDto();
				dq.setQyid(zfl.getCsid());
				dq.setQymc(zfl.getCsmc());
				dqList.add(dq);
			}
		}
		return dqList;
	}

	@Override
	public Map<String, Object> editSaveSale(XszbDto xszbDto) {
		Map<String, Object> map= new HashMap<>();
		boolean crmflag=false;
		if (StringUtil.isNotBlank(xszbDto.getZbfl())){
			//指标分类为：平台(用户存平台基础数据csid); 特检销售统计(用户存伙伴ID); 其他情况用户存yhid
			JcsjDto zbfl_jcsj = redisUtil.hgetDto("matridx_jcsj:"+BasicDataTypeEnum.SALE_CLASSIFY.getCode(),xszbDto.getZbfl());
			if ("PT".equals(zbfl_jcsj.getCsdm())){
				List<JcsjDto> platformlist = redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.PLATFORM_OWNERSHIP.getCode());
				boolean isfind = false;
				for (JcsjDto jcsj: platformlist) {
					if (StringUtil.isNotBlank(xszbDto.getZsxm()) && xszbDto.getZsxm().equals(jcsj.getCsmc())){
						xszbDto.setYhid(jcsj.getCsid());
						isfind = true;
					}
				}
				if (!isfind){
					map.put("status","fail");
					map.put("message","销售部门不存在，请确认");
					return map;
				}
			}else if ("TJXSTJ".equals(zbfl_jcsj.getCsdm())){
				List<XszbDto> xszb_sjhb = dao.getSjhbByHbmc(xszbDto.getZsxm());//查伙伴名称
				if (CollectionUtils.isNotEmpty(xszb_sjhb)){
					xszbDto.setYhid(xszb_sjhb.get(0).getYhid());
				}else {
					map.put("status","fail");
					map.put("message","伙伴名称不存在或伙伴状态不正常，请确认");
					return map;
				}
			}else if ("SALES_TARGET".equals(zbfl_jcsj.getCsdm())){//当维护CRM指标时，进行批量调用
				xszbDto.setSfqr("0");
				crmflag=true;
			}
		}
		if (StringUtil.isBlank(xszbDto.getYhid())){
			XszbDto xszbDto_yhm = dao.getYhxxByYhm(xszbDto.getZsxm());//查用户工号
			if (xszbDto_yhm != null){
				xszbDto.setYhid(xszbDto_yhm.getYhid());
			}else {
				map.put("status","fail");
				map.put("message","用户工号不存在，请确认");
				return map;
			}
		}
		XszbDto xszbDto_repeat = dao.getXszbRepeat(xszbDto);
		if(xszbDto_repeat!=null) {
			if (StringUtil.isNotBlank(xszbDto.getZbid()) && (xszbDto.getZbid().equals(xszbDto_repeat.getZbid())) && (!xszbDto.getSz().equals(xszbDto_repeat.getSz())) ){
				dao.updateDto(xszbDto);
			}else {
				map.put("status","fail");
				map.put("message","销售指标已经存在，不允许重复添加！");
				return map;
			}
		}else {
			if (StringUtil.isNotBlank(xszbDto.getZbid())){
				dao.update(xszbDto);
			}else {
				xszbDto.setZbid(StringUtil.generateUUID());
				dao.insertDto(xszbDto);
			}
		}
		if(crmflag && StringUtil.isNotBlank(xszbDto.getLrry())){//新增时才触发
			//生成月度年度指标
			String mzblx="";//月指标类型
			String nzblx="";//年指标类型
			List<XszbDto> mxszbDtos=new ArrayList<>();
			List<XszbDto> addList=new ArrayList<>();
			List<XszbDto> modList=new ArrayList<>();
			List<JcsjDto> zblxlist=redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.SALE_TYPE.getCode());
			for(JcsjDto jcsjDto:zblxlist){
				if("M".equals(jcsjDto.getCsdm()))
					mzblx=jcsjDto.getCsid();
				if("Y".equals(jcsjDto.getCsdm()))
					nzblx=jcsjDto.getCsid();
			}
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
			YearMonth startMonth = YearMonth.parse(xszbDto.getKszq(), formatter);
			YearMonth endMonth = YearMonth.parse(xszbDto.getJszq(), formatter);
			int yfc=endMonth.getMonthValue() - startMonth.getMonthValue()+1;//计算月份差，仅限同一年
			int sz_s=(int)Math.floor(Double.valueOf(xszbDto.getSz()))/yfc;//取商
			Double sz_y=Double.valueOf(xszbDto.getSz())%yfc;//取余
			while (!startMonth.isAfter(endMonth)){
				XszbDto xszbDto1=new XszbDto();
				xszbDto1.setZblx(mzblx);//设置为月度
				xszbDto1.setZbfl(xszbDto.getZbfl());
				xszbDto1.setZbzfl(xszbDto.getZbzfl());
				xszbDto1.setKszq(startMonth.toString());
				xszbDto1.setJszq(startMonth.toString());
				xszbDto1.setYhid(xszbDto.getYhid());
				xszbDto1.setZbid(StringUtil.generateUUID());
				xszbDto1.setSfqr("0");
				xszbDto1.setLrry(xszbDto.getLrry());
				if(!startMonth.equals(endMonth)){//若不是最后一个月取商
					xszbDto1.setSz(String.valueOf(sz_s));
				}else{
					xszbDto1.setSz(String.valueOf(sz_s+sz_y));//最后一个月取商+余
				}
				mxszbDtos.add(xszbDto1);
				startMonth = startMonth.plusMonths(1);//月份加1
			}
			//数据库查询获取已存在的和即将插入的月度指标，若数据库有的说明有ID，需要更新，若没有，则插入
			List<XszbDto> xszbDtos=dao.getTempSaleList(mxszbDtos);

			for(XszbDto xszbDto1:xszbDtos){
				if(StringUtil.isNotBlank(xszbDto1.getZbid())) {
					xszbDto1.setXgry(xszbDto.getLrry());
					modList.add(xszbDto1);
				}else{
					xszbDto1.setZbid(StringUtil.generateUUID());
					xszbDto1.setLrry(xszbDto.getLrry());
					addList.add(xszbDto1);
				}
			}

			//自动生成年度指标
			BigDecimal num=new BigDecimal(0);//用于设置到年度指标
			int year = endMonth.getYear();
			XszbDto nxszbDto=new XszbDto();
			nxszbDto.setZbfl(xszbDto.getZbfl());
			nxszbDto.setZbzfl(xszbDto.getZbzfl());
			nxszbDto.setYhid(xszbDto.getYhid());
			nxszbDto.setLrry(xszbDto.getLrry());
			nxszbDto.setZblx(xszbDto.getZblx());//这里为季度指标类型
			List<XszbDto> xszbDtoList=dao.getDtoList(nxszbDto);
			if(!CollectionUtils.isEmpty(xszbDtoList)){
				for(XszbDto xszbDto1:xszbDtoList){
					num=num.add(new BigDecimal(xszbDto1.getSz()));
				}
			}
			nxszbDto.setZblx(nzblx);//设置为年度
			nxszbDto.setKszq(String.valueOf(year)+"-01");
			nxszbDto.setJszq(String.valueOf(year)+"-12");
			nxszbDto.setXgry(xszbDto.getLrry());
			XszbDto yxszbDto = dao.getXszbRepeat(nxszbDto);
			nxszbDto.setSz(String.valueOf(num));
			if(yxszbDto!=null){//执行update
				nxszbDto.setZbid(yxszbDto.getZbid());
				modList.add(nxszbDto);
			}else{//执行insert
				nxszbDto.setZbid(StringUtil.generateUUID());
				addList.add(nxszbDto);
			}
			if(!CollectionUtils.isEmpty(addList))
				dao.insertXszbDtos(addList);
			if(!CollectionUtils.isEmpty(modList))
				dao.updateDtoList(modList);
		}
		map.put("status","success");
		map.put("message",xxglService.getModelById("ICOM00001").getXxnr());
		return map;
	}
	@Override
	public List<String> getDtosByZfl(XszbDto xszbDto) {
		return dao.getDtosByZfl(xszbDto);
	}

	@Override
	public List<XszbDto> getMySalesIndicator(XszbDto xszbDto) {
		return dao.getMySalesIndicator(xszbDto);
	}

	@Override
	public List<XszbDto> getSubordinateIndicator(XszbDto xszbDto) {
		return dao.getSubordinateIndicator(xszbDto);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean confirmSalesIndicator(XszbDto xszbDto) {
		return dao.confirmSalesIndicator(xszbDto);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean editSaveSalesIndicator(XszbDto xszbDto) throws BusinessException {
		//获取CRM销售分类
		JcsjDto jcsjDto_zbfl = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.SALE_CLASSIFY.getCode()).stream().filter(e -> "SALES_TARGET".equals(e.getCsdm())).findFirst().get();
		JcsjDto jcsjDto_zbzfl = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.SALE_SUBCLASSIFY.getCode()).stream().filter(e -> "SALES_TARGET_GROUP".equals(e.getCsdm())).findFirst().get();
		xszbDto.setZbfl(jcsjDto_zbfl.getCsid());
		xszbDto.setScry(xszbDto.getLrry());
		dao.discardSalesIndicator(xszbDto);
		List<XszbDto> allXszbDtos = new ArrayList<>();
		List<XszbDto> xszbDtos = JSON.parseArray(xszbDto.getXszb_json(), XszbDto.class);
		List<JcsjDto> xsmbs = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.SALE_TYPE.getCode());
		JcsjDto jcsjDto_nd = xsmbs.stream().filter(e -> "Y".equals(e.getCsdm())).findFirst().get();
		JcsjDto jcsjDto_jd = xsmbs.stream().filter(e -> "Q".equals(e.getCsdm())).findFirst().get();
		JcsjDto jcsjDto_yd = xsmbs.stream().filter(e -> "M".equals(e.getCsdm())).findFirst().get();
		List<XszbDto> allXszbDtos_yd = new ArrayList<>();
		XszbDto xszbDto_nd = new XszbDto();
		xszbDto_nd.setZbfl(jcsjDto_zbfl.getCsid());
		xszbDto_nd.setZbzfl(jcsjDto_zbzfl.getCsid());
		xszbDto_nd.setKszq(xszbDto.getNf()+"-01");
		xszbDto_nd.setJszq(xszbDto.getNf()+"-12");
		xszbDto_nd.setYhid(xszbDto.getYhid());
		xszbDto_nd.setLrry(xszbDto.getLrry());
		xszbDto_nd.setZblx(jcsjDto_nd.getCsid());
		xszbDto_nd.setZbid(StringUtil.generateUUID());
		BigDecimal zjeBig = new BigDecimal(0);
		for (XszbDto dto : xszbDtos) {
			if (StringUtil.isBlank(dto.getSz())){
				dto.setSz("0");
			}
		   dto.setZbid(StringUtil.generateUUID());
		   dto.setZbfl(jcsjDto_zbfl.getCsid());
		   dto.setZbzfl(jcsjDto_zbzfl.getCsid());
		   dto.setYhid(xszbDto.getYhid());
		   dto.setLrry(xszbDto.getLrry());
		   dto.setZblx(jcsjDto_jd.getCsid());
			List<XszbDto> yds_xszb = new ArrayList<>();
			for (int i = 0; i < 3; i++) {
				XszbDto xszbDto_yd = new XszbDto();
				xszbDto_yd.setZbfl(jcsjDto_zbfl.getCsid());
				xszbDto_yd.setZbzfl(jcsjDto_zbzfl.getCsid());
				xszbDto_yd.setYhid(xszbDto.getYhid());
				xszbDto_yd.setLrry(xszbDto.getLrry());
				xszbDto_yd.setZblx(jcsjDto_yd.getCsid());
				xszbDto_yd.setZbid(StringUtil.generateUUID());
				yds_xszb.add(xszbDto_yd);
			}
			BigDecimal szBig = new BigDecimal(dto.getSz());
			BigDecimal ydszBig = szBig.divide(new BigDecimal(3), 0, RoundingMode.HALF_UP);
			String firstSz = ydszBig.toString();
			String lastSz = szBig.subtract(ydszBig).subtract(ydszBig).toString();
			switch (dto.getJd()){
				case "Q1":dto.setKszq(xszbDto.getNf()+"-01");dto.setJszq(xszbDto.getNf()+"-03");
					yds_xszb.get(0).setKszq(xszbDto.getNf()+"-01");
					yds_xszb.get(0).setJszq(xszbDto.getNf()+"-01");
					yds_xszb.get(0).setSz(firstSz);
					yds_xszb.get(1).setKszq(xszbDto.getNf()+"-02");
					yds_xszb.get(1).setJszq(xszbDto.getNf()+"-02");
					yds_xszb.get(1).setSz(firstSz);
					yds_xszb.get(2).setKszq(xszbDto.getNf()+"-03");
					yds_xszb.get(2).setJszq(xszbDto.getNf()+"-03");
					yds_xszb.get(2).setSz(lastSz);
					break;
				case "Q2":dto.setKszq(xszbDto.getNf()+"-04");dto.setJszq(xszbDto.getNf()+"-06");
					yds_xszb.get(0).setKszq(xszbDto.getNf()+"-04");
					yds_xszb.get(0).setJszq(xszbDto.getNf()+"-04");
					yds_xszb.get(0).setSz(firstSz);
					yds_xszb.get(1).setKszq(xszbDto.getNf()+"-05");
					yds_xszb.get(1).setJszq(xszbDto.getNf()+"-05");
					yds_xszb.get(1).setSz(firstSz);
					yds_xszb.get(2).setKszq(xszbDto.getNf()+"-06");
					yds_xszb.get(2).setJszq(xszbDto.getNf()+"-06");
					yds_xszb.get(2).setSz(lastSz);
					break;
				case "Q3":dto.setKszq(xszbDto.getNf()+"-07");dto.setJszq(xszbDto.getNf()+"-09");
					yds_xszb.get(0).setKszq(xszbDto.getNf()+"-07");
					yds_xszb.get(0).setJszq(xszbDto.getNf()+"-07");
					yds_xszb.get(0).setSz(firstSz);
					yds_xszb.get(1).setKszq(xszbDto.getNf()+"-08");
					yds_xszb.get(1).setJszq(xszbDto.getNf()+"-08");
					yds_xszb.get(1).setSz(firstSz);
					yds_xszb.get(2).setKszq(xszbDto.getNf()+"-09");
					yds_xszb.get(2).setJszq(xszbDto.getNf()+"-09");
					yds_xszb.get(2).setSz(lastSz);
					break;
				case "Q4":dto.setKszq(xszbDto.getNf()+"-10");dto.setJszq(xszbDto.getNf()+"-12");
					yds_xszb.get(0).setKszq(xszbDto.getNf()+"-10");
					yds_xszb.get(0).setJszq(xszbDto.getNf()+"-10");
					yds_xszb.get(0).setSz(firstSz);
					yds_xszb.get(1).setKszq(xszbDto.getNf()+"-11");
					yds_xszb.get(1).setJszq(xszbDto.getNf()+"-11");
					yds_xszb.get(1).setSz(firstSz);
					yds_xszb.get(2).setKszq(xszbDto.getNf()+"-12");
					yds_xszb.get(2).setJszq(xszbDto.getNf()+"-12");
					yds_xszb.get(2).setSz(lastSz);
					break;
		   }
			zjeBig = zjeBig.add(new BigDecimal(dto.getSz()));
			allXszbDtos_yd.addAll(yds_xszb);
		}
		xszbDto_nd.setSz(zjeBig.toString());

		allXszbDtos.add(xszbDto_nd);
		allXszbDtos.addAll(xszbDtos);
		allXszbDtos.addAll(allXszbDtos_yd);
		dao.insertXszbDtos(allXszbDtos);
		return true;
	}

	@Override
	public List<XszbDto> getSalesIndicatorList(XszbDto xszbDto) {
		return dao.getSalesIndicatorList(xszbDto);
	}

	@Override
	public XszbDto getSalesIndicator(XszbDto xszbDto) {
		return dao.getSalesIndicator(xszbDto);
	}
}
