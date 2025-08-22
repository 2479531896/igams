package com.matridx.igams.experiment.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.entities.*;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.InvokingChildTypeEnum;
import com.matridx.igams.common.enums.InvokingTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFileImport;
import com.matridx.igams.common.service.svcinterface.IPcrsyjgService;
import com.matridx.igams.common.service.svcinterface.IXxdyService;
import com.matridx.igams.common.util.HttpUtil;
import com.matridx.igams.common.util.InterfaceExceptionUtil;
import com.matridx.igams.experiment.dao.entities.*;
import com.matridx.igams.experiment.dao.post.IWkglDao;
import com.matridx.igams.experiment.dao.post.IWkmxDao;
import com.matridx.igams.experiment.service.svcinterface.ISysjglService;
import com.matridx.igams.experiment.service.svcinterface.IWkmxService;
import com.matridx.igams.experiment.service.svcinterface.IWksjglService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.CommonUtil;
import com.matridx.springboot.util.encrypt.Encrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WkmxServiceImpl extends BaseBasicServiceImpl<WkmxDto, WkmxModel, IWkmxDao>
		implements IWkmxService, IFileImport {
	private final Logger log = LoggerFactory.getLogger(WkmxServiceImpl.class);
	@Autowired
	IWkglDao wkglDao;
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	@Autowired
	private IPcrsyjgService pcrsyjgService;
	@Autowired
	private IWksjglService wksjglService;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private IXxdyService xxdyService;
	@Autowired
	private InterfaceExceptionUtil interfaceExceptionUtil;
	@Autowired
	private ISysjglService sysjglService;
	/**
	 * 根据内部编号查询送检ID
	 */
	@Override
	public List<WkmxDto> getSjxxByNbbh(WkmxDto wkmxDto) {
		return dao.getSjxxByNbbh(wkmxDto);
	}

	/**
	 * 新增前校验内部编号是否存在
	 */
	@Override
	public List<String> exitnbbh(WkmxDto wkmxDto) throws BusinessException {
		List<String> nbbms = new ArrayList<>();
		for (int i = 0; i < wkmxDto.getNbbhs().length; i++) {
			// int nbbhlength=wkmxDto.getNbbhs()[i].length();
			String nbbh = CommonUtil.getNbbmByNbbm(wkmxDto.getNbbhs()[i]);
			nbbms.add(nbbh);
		}
		String[] nbbhjh = new String[nbbms.size()];
		nbbms.toArray(nbbhjh);
		wkmxDto.setNbbms(nbbhjh);
		List<WkmxDto> wksjxx = dao.getSjxxByNbbh(wkmxDto);
		List<String> notexitnbbhs = new ArrayList<>();
		List<String> exitnbbhlist = new ArrayList<>();
		String allnbbh;
		for (WkmxDto dto : wksjxx) {
			if (dto.getSjid() != null && !Objects.equals(dto.getSjid(), "")) {
				exitnbbhlist.add(dto.getNbbh());
			}
		}
		for (int j = 0; j < wkmxDto.getNbbms().length; j++) {
			allnbbh = wkmxDto.getNbbms()[j];
			boolean result = exitnbbhlist.contains(allnbbh);
			if (!result) {
				notexitnbbhs.add(wkmxDto.getNbbms()[j]);
			}
		}
		return notexitnbbhs;
	}

	/**
	 * 新增保存文库明细
	 */
	@SuppressWarnings("unused")
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean addSaveWkxx(WkmxDto wkmxDto) throws BusinessException {
		boolean result;
		Map<String, Object> map = new HashMap<>();

		RestTemplate t_restTemplate = new RestTemplate();
		// sjidlist,jtlist传参用
		List<String> sjidlist = new ArrayList<>();
		List<String> jtlist = new ArrayList<>();
		WkglDto wkglDto = new WkglDto();
		wkglDto.setLrry(wkmxDto.getLrry());
		if (("1").equals(wkmxDto.getZt_flag())) {
			wkglDto.setZt(StatusEnum.CHECK_PASS.getCode());
		} else if (("0").equals(wkmxDto.getZt_flag())) {
			wkglDto.setZt(StatusEnum.CHECK_NO.getCode());
		}
		Date date = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String cjrq = sf.format(date);
		wkglDto.setLrsj(cjrq);
		wkglDto.setWkmc(wkmxDto.getWkmc());
		wkglDto.setJcdw(wkmxDto.getJcdw());
		wkglDto.setYqlx(wkmxDto.getYqlx());
		wkglDto.setSjxz(wkmxDto.getSjxz());
		wkglDto.setWkrq(wkmxDto.getWkrq());
		wkglDto.setWksj(wkmxDto.getWksj());
		List<WkglDto> wkglxx = wkglDao.getWkglByJcdwAndLrsj(wkglDto);
		if (wkglxx != null && wkglxx.size() > 0) {
			int xh = Integer.parseInt(wkglxx.get(0).getXh());
			for (int i = 0; i < wkglxx.size(); i++) {
				if (i < wkglxx.size() - 1) {
					if (Integer.parseInt(wkglxx.get(i).getXh()) > Integer.parseInt(wkglxx.get(i + 1).getXh())) {
						xh = Integer.parseInt(wkglxx.get(i).getXh()) + 1;
					} else {
						xh = Integer.parseInt(wkglxx.get(i + 1).getXh()) + 1;
					}
				}
			}
			wkglDto.setXh(String.valueOf(xh));
		} else {
			wkglDto.setXh("1");
		}
		wkglDto.setSjph1(wkmxDto.getSjph1());
		wkglDto.setSjph2(wkmxDto.getSjph2());
		wkglDto.setSjph3(wkmxDto.getSjph3());
		wkglDto.setWkid(StringUtil.generateUUID());
		if ( StringUtil.isNotBlank(wkmxDto.getExtend_1()) && StringUtil.isNotBlank(wkmxDto.getExtend_2()) && wkmxDto.getExtend_1().equals("mergeSaveWk") ){
			//由文库合并调用新增，则使用Extend_2参数作为wkid
			wkglDto.setWkid(wkmxDto.getExtend_2());
		}
		StringBuilder ids= new StringBuilder();
		for (String id :wkmxDto.getIds()){ ids.append(",").append(id); }
		if (StringUtil.isNotBlank(ids.toString())){ ids = new StringBuilder(ids.substring(1)); }
        wkglDto.setYwkid(ids.toString());
		boolean wkglresult = wkglDao.insert(wkglDto) > 0;
		if (!wkglresult)
			return false;
		SysjglDto sysjglDto = new SysjglDto();
		sysjglDto.setYwid(wkglDto.getWkid());
		sysjglDto.setScry(wkglDto.getLrry());
		//sysjglService.delete(sysjglDto);
		if(StringUtil.isNotBlank(wkmxDto.getSysjStr())){
			List<SysjglDto> sysjglDtos = JSON.parseArray(wkmxDto.getSysjStr(),SysjglDto.class);

			if(sysjglDtos != null && sysjglDtos.size() > 0) {
				for (SysjglDto sysjglDto_t : sysjglDtos) {
					sysjglDto_t.setSysjid(StringUtil.generateUUID());
					sysjglDto_t.setType("WK");
					sysjglDto_t.setYwid(wkglDto.getWkid());
				}
				boolean resultSysj = sysjglService.insertSysjglDtos(sysjglDtos);
				if (!resultSysj) {
					return false;
				}
			}
		}
		List<WkmxDto> wksjxx = dao.getSjxxByNbbh(wkmxDto);
		if (wksjxx != null && wksjxx.size() > 0) {
			List<WkmxDto> wkxxlist = new ArrayList<>();
			for (int i = 0; i < wkmxDto.getNbbhs().length; i++) {
				WkmxDto wkmxdto = new WkmxDto();
				wkxxlist.add(wkmxdto);
				boolean sfczsjid = false;// 用于判断查询到的送检信息中是否存在符合条件的sjid
				if (wkmxDto.getNbbhs()[i].contains("-")) {
					String nbbh = CommonUtil.getNbbmByNbbm(wkmxDto.getNbbhs()[i]);
					for (WkmxDto dto : wksjxx) {
						if (dto.getNbbh().equals(nbbh)) {
							wkxxlist.get(i).setSjid(dto.getSjid());
							sjidlist.add(dto.getSjid());
							sfczsjid = true;
							break;
						}
					}
					if (!sfczsjid) {
						sjidlist.add(null);
					}
				} else {
					for (WkmxDto dto : wksjxx) {
						if (wkmxDto.getNbbhs()[i].indexOf(dto.getNbbh())!=-1) {
							wkxxlist.get(i).setSjid(dto.getSjid());
							sjidlist.add(dto.getSjid());
							sfczsjid = true;
							break;
						}
					}
					if (!sfczsjid) {
						sjidlist.add(null);
					}
				}
				wkxxlist.get(i).setNbbh(wkmxDto.getNbbhs()[i]);
				if (wkmxDto.getNbbhs()[i].contains("-REM"))
					wkxxlist.get(i).setHzbj("REM");
				if (wkmxDto.getNbbhs()[i].contains("-DNA")) {
					wkxxlist.get(i).setJcxmdm("D");
				} else if (wkmxDto.getNbbhs()[i].contains("-RNA")) {
					wkxxlist.get(i).setJcxmdm("R");
				} else if (wkmxDto.getNbbhs()[i].contains("-tNGS")  || wkmxDto.getNbbhs()[i].contains("-TBtNGS") || wkmxDto.getNbbhs()[i].contains("-HS")) {
					wkxxlist.get(i).setJcxmdm("C");
				}
				wkxxlist.get(i).setXh(wkmxDto.getXhs()[i]);
				wkxxlist.get(i).setWkid(wkglDto.getWkid());
				wkxxlist.get(i).setLrry(wkmxDto.getLrry());
				wkxxlist.get(i).setJcdw(wkglDto.getJcdw());
				wkxxlist.get(i).setWkrq(wkmxDto.getWkrq());
				/*if (wkmxDto.getQuantitys() != null && wkmxDto.getQuantitys().length>i ){
					wkxxlist.get(i).setQuantity(wkmxDto.getQuantitys()[i]);
				}*/
				if (wkmxDto.getTqms() != null && wkmxDto.getTqms().length>i){
					wkxxlist.get(i).setTqm(wkmxDto.getTqms()[i]);
				}
				if (wkmxDto.getSyglids() != null && wkmxDto.getSyglids().length>i){
					wkxxlist.get(i).setSyglid(wkmxDto.getSyglids()[i]);
				}
				if (wkmxDto.getTqmxids() != null && wkmxDto.getTqmxids().length>0){
					wkxxlist.get(i).setTqmxid(wkmxDto.getTqmxids()[i]);
				}
				if (wkmxDto.getJtxxs() == null || wkmxDto.getJtxxs().length == 0) {
					wkxxlist.get(i).setJtxx("");
					jtlist.add("");
				} else {
					wkxxlist.get(i).setJtxx(wkmxDto.getJtxxs()[i]);
					jtlist.add(wkmxDto.getJtxxs()[i]);
				}
			}
			if("1".equals(wkmxDto.getZt_flag())){
				List<WkmxDto> list= JSON.parseArray(wkmxDto.getSygl_json(),WkmxDto.class);
				if(list != null && list.size()>0){
					for(WkmxDto dto:list){
						dto.setXgry(wkmxDto.getLrry());
					}
					updateDetectJt(list);
				}
				boolean updateDetectionDate = updateDetectionDate(wkmxDto,wkxxlist);
				if (!updateDetectionDate)
					return false;
			}
			boolean addsavewkxx = dao.insertWkxx(wkxxlist);
			if (addsavewkxx) {
				// 调用igams-wechat中的接口，保存送检接头信息
				map.put("sjid", sjidlist);
				map.put("jt", jtlist);
				map.put("sfgxsyrq", "1");
				map.put("xgry",wkmxDto.getLrry());
				result = insetSjjtxx(map);
				if (!result)
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();// 若为false回滚
				return result;
			}
			return false;
		} else {
			List<WkmxDto> wkxxlist = new ArrayList<>();
			for (int i = 0; i < wkmxDto.getNbbhs().length; i++) {
				WkmxDto wkmxdto = new WkmxDto();
				wkxxlist.add(wkmxdto);
				wkxxlist.get(i).setNbbh(wkmxDto.getNbbhs()[i]);
				wkxxlist.get(i).setXh(wkmxDto.getXhs()[i]);
				wkxxlist.get(i).setWkid(wkglDto.getWkid());
				wkxxlist.get(i).setLrry(wkmxDto.getLrry());
				wkxxlist.get(i).setWkrq(wkmxDto.getWkrq());
				/*if (wkmxDto.getQuantitys() != null && wkmxDto.getQuantitys().length>i){
					wkxxlist.get(i).setQuantity(wkmxDto.getQuantitys()[i]);
				}*/
				if (wkmxDto.getTqms() != null && wkmxDto.getTqms().length>i){
					wkxxlist.get(i).setTqm(wkmxDto.getTqms()[i]);
				}
				if (wkmxDto.getSyglids() != null && wkmxDto.getSyglids().length>i){
					wkxxlist.get(i).setSyglid(wkmxDto.getSyglids()[i]);
				}
				if (wkmxDto.getJtxxs() == null || wkmxDto.getJtxxs().length == 0) {
					wkxxlist.get(i).setJtxx("");
					jtlist.add("");
				} else {
					wkxxlist.get(i).setJtxx(wkmxDto.getJtxxs()[i]);
					jtlist.add(wkmxDto.getJtxxs()[i]);
				}
			}
			return dao.insertWkxx(wkxxlist);
		}
	}


	/**
	 * 根据文库ID查询文库明细数据
	 */
	@Override
	public List<WkmxDto> getWkmxByWkid(WkmxDto wkmxDto) {
		return dao.getWkmxByWkid(wkmxDto);
	}

	/**
	 * 根据wkid和序号更新文库明细数据
	 */
	@SuppressWarnings("unused")
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateWkmxByWkidAndXh(WkmxDto wkmxDto) throws BusinessException {
		boolean result;
		SysjglDto sysjglDto = new SysjglDto();
		sysjglDto.setYwid(wkmxDto.getWkid());
		sysjglDto.setScry(wkmxDto.getXgry());
		//实验试剂数据保存
		sysjglService.delete(sysjglDto);
		result = sysjglService.insertSysjglList(wkmxDto.getSysjStr(),"WK");
		if(!result){
			return false;
		}
		Map<String, Object> map = new HashMap<>();
		RestTemplate t_restTemplate = new RestTemplate();
		// sjidlist,jtlist传参用
		List<String> sjidlist = new ArrayList<>();
		List<String> jtlist = new ArrayList<>();
		WkglDto wkglDto = new WkglDto();
		wkglDto.setXgry(wkmxDto.getXgry());
		wkglDto.setWkid(wkmxDto.getWkid());
		wkglDto.setWkmc(wkmxDto.getWkmc());
		wkglDto.setWkrq(wkmxDto.getWkrq());
		if (("1").equals(wkmxDto.getZt_flag())) {
			wkglDto.setZt(StatusEnum.CHECK_PASS.getCode());
		}
		//查询修改前的文库原始信息
		WkglDto old_wkglDto =wkglDao.getDtoById(wkmxDto.getWkid());

		wkglDto.setJcdw(wkmxDto.getJcdw());
		wkglDto.setYqlx(wkmxDto.getYqlx());
		wkglDto.setSjxz(wkmxDto.getSjxz());
		//wkglDto.setWksj(wkmxDto.getWksj());
		if(old_wkglDto == null){
			List<WkglDto> wkglxx = wkglDao.getWkglByJcdwAndLrsj(wkglDto);
			if (wkglxx != null && wkglxx.size() > 0) {
				int xh = Integer.parseInt(wkglxx.get(0).getXh());
				for (int i = 0; i < wkglxx.size(); i++) {
					if (i < wkglxx.size() - 1) {
						if (Integer.parseInt(wkglxx.get(i).getXh()) > Integer.parseInt(wkglxx.get(i + 1).getXh())) {
							xh = Integer.parseInt(wkglxx.get(i).getXh()) + 1;
						} else {
							xh = Integer.parseInt(wkglxx.get(i + 1).getXh()) + 1;
						}
					}
				}
				wkglDto.setXh(String.valueOf(xh));
			} else {
				wkglDto.setXh("1");
			}
		}else{
			wkglDto.setXh(old_wkglDto.getXh());
		}
		//需要保留原有数据，只是针对页面新增的内容进行替换 2025-08-05
		String wksj = wkmxDto.getWksj();
		if(StringUtil.isNotBlank(wksj)){
			String old_wksj = old_wkglDto.getWksj();
			Map<String,String> wksjMap = JSONObject.parseObject(wksj,Map.class);
			Map<String,String> old_wksjMap = JSONObject.parseObject(old_wksj,Map.class);
			if(old_wksjMap == null){
				old_wksjMap = new HashMap<>();
			}
			for(String key:wksjMap.keySet()){
				if(StringUtil.isNotBlank(wksjMap.get(key))){
					old_wksjMap.put(key,wksjMap.get(key));
				}
			}
			wkglDto.setWksj(JSONObject.toJSONString(old_wksjMap));
		}
		WkmxDto wkmxDto_t=new WkmxDto();
		wkmxDto_t.setWkid(wkmxDto.getWkid());
		//从数据库获取指定wkid的已有文库明细，
		List<WkmxDto> wkmxByWkid = getWkmxByWkid(wkmxDto_t);
		List<String> syglids=new ArrayList<>();
		//把现有文库明细里已有syglid组成list
		if(wkmxByWkid!=null&&wkmxByWkid.size()>0){
			for(WkmxDto dto:wkmxByWkid){
				if(StringUtil.isNotBlank(dto.getSyglid())){
					syglids.add(dto.getSyglid());
				}
			}
		}
		if(syglids.size() > 0){
			WkmxDto wkmxDto_remove=new WkmxDto();
			wkmxDto_remove.setIds(syglids);
			removeDetectJt(wkmxDto_remove);
		}
		List<WkmxDto> wksjxx = dao.getSjxxByNbbh(wkmxDto);
		if (wksjxx != null && wksjxx.size() > 0) {
			List<WkmxDto> wkxxlist = new ArrayList<>();
			for (int i = 0; i < wkmxDto.getNbbhs().length; i++) {
				WkmxDto wkmxdto = new WkmxDto();
				wkxxlist.add(wkmxdto);
				boolean sfczsjid = false;// 用于判断查询到的送检信息中是否存在符合条件的sjid
				if (wkmxDto.getNbbhs()[i].contains("-")) {
					//String t_nbbh = wkmxDto.getNbbhs()[i];
					String nbbh = CommonUtil.getNbbmByNbbm(wkmxDto.getNbbhs()[i]);
					for (WkmxDto dto : wksjxx) {
						if (dto.getNbbh().equals(nbbh)) {
							wkxxlist.get(i).setSjid(dto.getSjid());
							sjidlist.add(dto.getSjid());
							sfczsjid = true;
							break;
						}
					}
					if (!sfczsjid) {
						sjidlist.add(null);
					}
				} else {
					for (WkmxDto dto : wksjxx) {
						if (wkmxDto.getNbbhs()[i].indexOf(dto.getNbbh())!=-1) {
							wkxxlist.get(i).setSjid(dto.getSjid());
							sjidlist.add(dto.getSjid());
							sfczsjid = true;
							break;
						}
					}
					if (!sfczsjid) {
						sjidlist.add(null);
					}
				}
				wkxxlist.get(i).setNbbh(wkmxDto.getNbbhs()[i]);
				if (wkmxDto.getNbbhs()[i].contains("-REM"))
					wkxxlist.get(i).setHzbj("REM");
				if (wkmxDto.getNbbhs()[i].contains("-DNA")) {
					wkxxlist.get(i).setJcxmdm("D");
				} else if (wkmxDto.getNbbhs()[i].contains("-RNA")) {
					wkxxlist.get(i).setJcxmdm("R");
				} else if (wkmxDto.getNbbhs()[i].contains("-tNGS") || wkmxDto.getNbbhs()[i].contains("-TBtNGS") || wkmxDto.getNbbhs()[i].contains("-HS")) {
					wkxxlist.get(i).setJcxmdm("C");
				}
				wkxxlist.get(i).setXh(wkmxDto.getXhs()[i]);
				wkxxlist.get(i).setWkid(wkmxDto.getWkid());
				wkxxlist.get(i).setLrry(wkmxDto.getXgry());
				wkxxlist.get(i).setWkrq(wkmxDto.getWkrq());
				/*if (wkmxDto.getQuantitys() != null && wkmxDto.getQuantitys().length > i) {
					wkxxlist.get(i).setQuantity(wkmxDto.getQuantitys()[i]);
				}*/
				if (wkmxDto.getTqms() != null && wkmxDto.getTqms().length > i) {
					wkxxlist.get(i).setTqm(wkmxDto.getTqms()[i]);
				}
				if (wkmxDto.getSyglids() != null && wkmxDto.getSyglids().length>i){
					wkxxlist.get(i).setSyglid(wkmxDto.getSyglids()[i]);
				}
				if (wkmxDto.getTqmxids() != null && wkmxDto.getTqmxids().length>0){
					wkxxlist.get(i).setTqmxid(wkmxDto.getTqmxids()[i]);
				}
				wkxxlist.get(i).setJcdw(wkglDto.getJcdw());
				if (wkmxDto.getJtxxs() == null || wkmxDto.getJtxxs().length == 0) {
					wkxxlist.get(i).setJtxx("");
					jtlist.add("");
				} else {
					wkxxlist.get(i).setJtxx(wkmxDto.getJtxxs()[i]);
					jtlist.add(wkmxDto.getJtxxs()[i]);
				}
			}
			if("1".equals(wkmxDto.getZt_flag())){
				List<WkmxDto> list= JSON.parseArray(wkmxDto.getSygl_json(),WkmxDto.class);
				if(list != null && list.size()>0){
					for(WkmxDto dto:list){
						dto.setXgry(wkmxDto.getLrry());
					}
					updateDetectJt(list);
				}
				boolean updateDetectionDate = updateDetectionDate(wkmxDto,wkxxlist);
				if (!updateDetectionDate)
					return false;
			}
			boolean deletewkmx = dao.deleteWkmxxx(wkmxDto);// 先删除原有数据
			if (!deletewkmx)
				return false;
			boolean updatesavewkxx = dao.insertWkxx(wkxxlist);
			if (updatesavewkxx) {
				wkglDto.setSjph1(wkmxDto.getSjph1());
				wkglDto.setSjph2(wkmxDto.getSjph2());
				wkglDto.setSjph3(wkmxDto.getSjph3());
				boolean updatewkgl = wkglDao.updateWkxxByWkid(wkglDto);
				if (updatewkgl) {
					// 调用igams-wechat中的接口，保存送检接头信息
					map.put("sjid", sjidlist);
					map.put("jt", jtlist);
					map.put("sfgxsyrq", "0");
					map.put("xgry",wkmxDto.getXgry());
					result = insetSjjtxx(map);
					if (!result)
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();// 若为false回滚
					return result;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			List<WkmxDto> wkxxlist = new ArrayList<>();
			for (int i = 0; i < wkmxDto.getNbbhs().length; i++) {
				WkmxDto wkmxdto = new WkmxDto();
				wkxxlist.add(wkmxdto);
				wkxxlist.get(i).setNbbh(wkmxDto.getNbbhs()[i]);
				wkxxlist.get(i).setXh(wkmxDto.getXhs()[i]);
				wkxxlist.get(i).setWkid(wkglDto.getWkid());
				wkxxlist.get(i).setXgry(wkmxDto.getLrry());
				wkxxlist.get(i).setWkrq(wkmxDto.getWkrq());
				if (wkmxDto.getJtxxs() == null || wkmxDto.getJtxxs().length == 0) {
					wkxxlist.get(i).setJtxx("");
					jtlist.add("");
				} else {
					wkxxlist.get(i).setJtxx(wkmxDto.getJtxxs()[i]);
					jtlist.add(wkmxDto.getJtxxs()[i]);
				}
				if (wkmxDto.getTqms() != null && wkmxDto.getTqms().length > i) {
					wkxxlist.get(i).setTqm(wkmxDto.getTqms()[i]);
				}
				if (wkmxDto.getSyglids() != null && wkmxDto.getSyglids().length>i){
					wkxxlist.get(i).setSyglid(wkmxDto.getSyglids()[i]);
				}
			}
			boolean deletewkmx = dao.deleteWkmxxx(wkmxDto);// 先删除原有数据
			if (!deletewkmx)
				return false;
			boolean addsavewkxx = dao.insertWkxx(wkxxlist);
			if (!addsavewkxx)
				return false;
			wkglDto.setSjph1(wkmxDto.getSjph1());
			wkglDto.setSjph2(wkmxDto.getSjph2());
			wkglDto.setSjph3(wkmxDto.getSjph3());
			return wkglDao.updateWkxxByWkid(wkglDto);
		}
	}

	/**
	 * 根据文库id删除文库明细数据
	 */
	@Override
	public boolean deleteWkmxxx(WkmxDto wkmxDto) {
		return dao.deleteWkmxxx(wkmxDto);
	}

	/**
	 * 选中导出
	 */
	public List<WkmxDto> getListForSelectExp(Map<String, Object> params) {
		WkmxDto wkmxDto = (WkmxDto) params.get("entryData");
		queryJoinFlagExport(params, wkmxDto);
		List<String> wkids = wkmxDto.getIds();
		List<WkmxDto> res = new ArrayList<>();
		for (String wkid : wkids){
			WksjglDto wksjglDto=new WksjglDto();
			WkmxDto wkmxDto1 = new WkmxDto();
			wksjglDto.setWkid(wkid);
			wkmxDto1.setWkid(wkid);
			wkmxDto1.setSqlParam(wkmxDto.getSqlParam());
			wksjglDto=wksjglService.getDtoByWkid(wksjglDto);
			//如果是第一次上机,从文库导出获取
			List<WkmxDto> list;
			if(wksjglDto==null) {
				wkmxDto.setIds(Arrays.asList(wkid));
				list = dao.getListForSelectExp(wkmxDto);
			}else
			{
				//如果不是则从文库上机明细表获取
				wkmxDto.setWksjid(wksjglDto.getWksjid());
				list = dao.getWksjDtoList(wkmxDto);
			}
			res.addAll(list);
		}
		return res;
	}

	public List<WkmxDto> getListForSelectExpOld(Map<String, Object> params) {
		WkmxDto wkmxDto = (WkmxDto) params.get("entryData");
		queryJoinFlagExport(params, wkmxDto);
		return dao.getListForSelectExp(wkmxDto);
	}

	@SuppressWarnings("unchecked")
	private void queryJoinFlagExport(Map<String, Object> params, WkmxDto wkmxDto) {
		List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
		StringBuilder sqlParam = new StringBuilder();
		for (DcszDto dcszDto : choseList) {
			if (dcszDto == null || dcszDto.getDczd() == null)
				continue;

			sqlParam.append(",");
			if (StringUtil.isNotBlank(dcszDto.getSqlzd())) {
				sqlParam.append(dcszDto.getSqlzd());
			}
			sqlParam.append(" ");
			sqlParam.append(dcszDto.getDczd());
		}
		String sqlcs = sqlParam.toString();
		wkmxDto.setSqlParam(sqlcs);
	}

	/**
	 * 根据文库id删除文库明细数据(删除文库信息用)
	 */
	public boolean deleteWkmxxxlist(WkmxDto wkmxDto) {
		return dao.deleteWkmxxxlist(wkmxDto);
	}

	/**
	 * 根据文库id删除文库明细数据（删除标记置为2）
	 */
	@Override
	public boolean delMergeWkmxlist(WkmxDto wkmxDto) {
		return dao.delMergeWkmxlist(wkmxDto);
	}

	/**
	 * 撤销合并的文库明细数据
	 */
	@Override
	public boolean cancelMergeWkmxlist(WkglDto wkgl) {
		return dao.cancelMergeWkmxlist(wkgl);
	}

	/**
	 * 根据IDs获取文库明细
	 */
	@Override
	public List<WkmxDto> getWkmxByIds(WkmxDto wkmxDto) {
		return dao.getWkmxByIds(wkmxDto);
	}

	/**
	 * 根据内部编号查询文库明细数据
	 */
	public List<WkmxDto> getWkmxByNbbh(WkmxDto wkmxDto) {
		return dao.getWkmxByNbbh(wkmxDto);
	}

	/**
	 * 根据文库id和序号更新文库浓度
	 */
	public boolean updateWknd(List<WkmxDto> list) {
		return dao.updateWknd(list);
	}

	/**
	 * 根据送检id获取文库明细数据
	 */
	public List<WkmxDto> getWkmxBySjid(String sjid) {
		return dao.getWkmxBySjid(sjid);
	}

	@Override
	public boolean existCheck(String fieldName, String value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean insertImportRec(BaseModel baseModel, User user,int rowindex, StringBuffer errorMessages) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 处理导入信息
	 */
	@Override
	public String cellTransform(String tranTrack, String value, ImportRecordModel recModel, StringBuffer errorMessage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insertNormalImportRec(Map<String, String> recMap, User user) {
		// TODO Auto-generated method stub
		if (recMap.get("7") != null && !"".equals(recMap.get("7")) && !"Quantity".equals(recMap.get("7"))) {
			WkmxDto wkmxDto = new WkmxDto();
			wkmxDto.setWkid(recMap.get("kzcs1"));
			wkmxDto.setXgry(user.getYhid());
			// 若标准浓度为空或者为“-”时，默认设置为0
			if ("-".equals(recMap.get("7"))) {
				wkmxDto.setQuantity("0");
			} else {
				wkmxDto.setQuantity(recMap.get("7"));
			}
			wkmxDto.setNbbh(recMap.get("1"));
//			double wkyynd = Double.parseDouble(wkmxDto.getQuantity()) * 452 / 288 * 10;
			BigDecimal wkyynd = new BigDecimal(wkmxDto.getQuantity()).multiply(new BigDecimal(4520)).divide( new BigDecimal(288),6, RoundingMode.HALF_UP);
			wkmxDto.setWkyynd(wkyynd + "");
			return dao.updateQuantity(wkmxDto);
		}
		return true;
	}

	/**
	 * 检查标题定义，主要防止模板信息过旧
	 */
	public boolean checkDefined(List<Map<String, String>> defined) {
		return true;
	}

	/**
	 * 根据内部编号查找送检检测项目信息
	 */
	public WkmxDto getSjDtoByNbbh(WkmxDto wkmxDto) {
		return dao.getSjDtoByNbbh(wkmxDto);
	}

	/**
	 * 文库列表查找出用来对接PCR的字段
	 */
	@Override
	public List<WkmxDto> getDtoForPcrReady(WkmxDto wkmxDto) {
		return dao.getDtoForPcrReady(wkmxDto);
	}

	/**
	 * 文库列表查找出用来对接PCR的字段，发送给pcr
	 */
	@SuppressWarnings("static-access")
	@Override
	public boolean sendLibrary(WkmxDto wkmxDto) {
		// 获取文库信息
		List<Map<String,String>> list = dao.getWkmxTopcr(wkmxDto);
		// 转换成json数组
		if (list != null && list.size() > 0) {
			JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(list));
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("result", jsonArray);
			jsonObject.put("wkid", wkmxDto.getWkid());
			jsonObject.put("djcsy", "1");
			jsonObject.put("ispcr", "1");
			SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			JkdymxDto jkdymxDto = new JkdymxDto();
			// 发送信息
			String result2 = null;
			String urlString = "http://172.17.50.165:5001/StartExp";
			try {
				jkdymxDto.setLxqf("send");
				jkdymxDto.setDysj(dateFm.format(new Date()));
				// jkdymxDto.setYwid(inspinfo.getYbbh());
				jkdymxDto.setNr(jsonObject.toJSONString());
				jkdymxDto.setDydz(urlString);
				jkdymxDto.setDyfl(InvokingTypeEnum.INVOKING_WKMX.getCode());
				jkdymxDto.setDyzfl(InvokingChildTypeEnum.INVOKING_CHILD_PCR_RESULT.getCode());
				jkdymxDto.setSfcg("2");
				result2 = HttpUtil.post(urlString, "", jsonObject.toJSONString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// JSONObject jsonObject1 = JSONObject.parseObject(result2);
			// 返回成功后过五秒调用
			try {
				String url2 = "http://172.17.50.165:5001/GetInstrumentInformationStatus ";
				if (Objects.equals(result2, "true")) {
					Thread.currentThread().sleep(50000);
					try {
						HttpUtil.post(url2, "", "");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					JSONObject jsonObject1 = JSONObject.parseObject(result2);
					log.error("银行5秒后调用返回：" + jsonObject1);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return false;
	}

	/**
	 * 接收处理从pcr返回的实验结果
	 */
	@Override
	public boolean getWkmxDtoFromPcr(WkmxPcrModel wkmxPcrModel) {
		// 封装传过来的数据
		// 查询出来所有的文库明细对象
		// 通过位置匹配每个文库明细结果
		if (StringUtil.isBlank(wkmxPcrModel.getWkid())) {
			pcrsyjgService.savePcrsyjgDto(wkmxPcrModel);
		} else {
			List<WkmxPcrResultModel> resultModels = wkmxPcrModel.getResult();

			if (resultModels != null && resultModels.size() > 0) {
				for (WkmxPcrResultModel pr : resultModels) {
					if (pr == null)
						continue;
					WkmxDto wkmxDto = new WkmxDto();
					// 计算位置序号
					if (StringUtil.isBlank(pr.getWellHIndex()) || StringUtil.isBlank(pr.getWellVIndex())) {
						log.info(pr.getSampleUID() + ":位置坐标未传 ");
						return false;
					}
					int hnum = Integer.parseInt(pr.getWellHIndex());
					int znum = Integer.parseInt(pr.getWellVIndex());
					String xh = (znum * 8 + hnum + 1) + "";
					// 先查出一条数据，再更新
					wkmxDto.setWkid(wkmxPcrModel.getWkid());
					wkmxDto.setConcentrationre(pr.getConcentration());
					wkmxDto.setCtvaule(pr.getCtVaule());
					wkmxDto.setReferencedye(pr.getReferenceDye());
					wkmxDto.setQcresult(pr.getQcResult());
					wkmxDto.setDjcsy(wkmxPcrModel.getDjcsy());
					wkmxDto.setXh(xh);
					wkmxDto.setNbbh(pr.getSampleName());
					wkmxDto.setWell(pr.getWell());
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					wkmxDto.setWkrq(df.format(new Date()));
					// 如果有wkid不需要查询，没有需要查询
					if (StringUtil.isBlank(wkmxPcrModel.getWkid())) {
						WkmxDto wDto = dao.getWkmxFristBynbbh(wkmxDto);
						if (wDto != null && StringUtil.isNotBlank(wDto.getWkid())) {
							wkmxDto.setWkid(wDto.getWkid());
						}
					}
					if (StringUtil.isNotBlank(wkmxDto.getWkid()))
						dao.updateWkmxResult(wkmxDto);

				}

			}
		}
		return true;
	}

	/**
	 * 更新送检实验管理表接头数据
	 */
	public boolean updateDetectJt(List<WkmxDto> list){
		return dao.updateDetectJt(list);
	}
	/**
	 * 更新送检实验管理表接头数据
	 */
	public boolean removeDetectJt(WkmxDto wkmxDto){
		return dao.removeDetectJt(wkmxDto);
	}

	/**
	 * 验证是否存在相同syglid
	 */
	public List<WkmxDto> verifySame(WkmxDto wkmxDto){
		return dao.verifySame(wkmxDto);
	}

	/**
	 * 新增文库明细数据
	 */
	public boolean insertWkxx(List<WkmxDto> list){
		return dao.insertWkxx(list);
	}

	/**
	 * 根据接头获取文库明细信息
	 */
	public List<WkmxDto> getWkmxByJt(WkmxDto wkmxDto){
		return dao.getWkmxByJt(wkmxDto);
	}

	/**
	 * 根据syglids获取实验信息
	 */
	public List<WkmxDto> getInfoBySyglids(WkmxDto wkmxDto){
		return dao.getInfoBySyglids(wkmxDto);
	}

	public boolean updateDetectionDate(WkmxDto wkmxDto,List<WkmxDto> wkxxlist){
		Date date=new Date();
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<String> syglids=new ArrayList<>();
		List<String> sjids=new ArrayList<>();
		for(WkmxDto wkmxDto_t:wkxxlist){
			if(StringUtil.isNotBlank(wkmxDto_t.getSyglid())){
				syglids.add(wkmxDto_t.getSyglid());
				sjids.add(wkmxDto_t.getSjid());
			}
		}
		if(!CollectionUtils.isEmpty(syglids)){
			wkmxDto.setIds(syglids);
			List<WkmxDto> detectionInfo = dao.getDetectionInfo(wkmxDto);
			if(!CollectionUtils.isEmpty(detectionInfo)){
				List<WkmxDto> sjxxList=new ArrayList<>();//更新送检信息表的list
				List<WkmxDto> fjsqList=new ArrayList<>();//更新复检的list
				List<WkmxDto> syglList=new ArrayList<>();//更新实验管理的list
				for(WkmxDto dto:detectionInfo){
					if(StringUtil.isNotBlank(wkmxDto.getLrry())){
						dto.setXgry(wkmxDto.getLrry());
					}else{
						dto.setXgry(wkmxDto.getXgry());
					}
					dto.setSyrq(dateFormat.format(date));
					syglList.add(dto);
					WkmxDto wkmxDto_t=new WkmxDto();
					if(StringUtil.isNotBlank(wkmxDto.getLrry())){
						wkmxDto_t.setXgry(wkmxDto.getLrry());
					}else{
						wkmxDto_t.setXgry(wkmxDto.getXgry());
					}
					if (StringUtil.isNotBlank(dto.getKzcs1())){//如果检测类型的扩展参数不为空
						if ("D".equals(dto.getKzcs1())){//如果检测类型的扩展参数1为D
							wkmxDto_t.setDsyrq(dateFormat.format(date));//插入D实验日期
						}else if ("R".equals(dto.getKzcs1())){//如果检测类型的扩展参数1为R
							wkmxDto_t.setRsyrq(dateFormat.format(date));//插入R实验日期
							wkmxDto_t.setSyrq(dateFormat.format(date));
						}else if ("C".equals(dto.getKzcs1())||"T".equals(dto.getKzcs1())){//如果检测类型的扩展参数1为C或T
							if ("D".equals(dto.getJcxmcskz1())){//如果检测项目的扩展参数1为D
								wkmxDto_t.setDsyrq(dateFormat.format(date));//插入D实验日期
							}else if ("R".equals(dto.getJcxmcskz1())){//如果检测项目的扩展参数1为R
								wkmxDto_t.setRsyrq(dateFormat.format(date));//插入R实验日期
								wkmxDto_t.setSyrq(dateFormat.format(date));
							}else if ("C".equals(dto.getJcxmcskz1())){//如果检测项目的扩展参数1为C
								wkmxDto_t.setDsyrq(dateFormat.format(date));//插入D实验日期
								wkmxDto_t.setRsyrq(dateFormat.format(date));//插入R实验日期
								wkmxDto_t.setSyrq(dateFormat.format(date));
							}else {
								wkmxDto_t.setQtsyrq(dateFormat.format(date));//插入其他实验日期
							}
						}else {
							wkmxDto_t.setQtsyrq(dateFormat.format(date));//插入其他实验日期
						}
					}
					if ("DETECT_SJ".equals(dto.getLx())){
						wkmxDto_t.setSjid(dto.getSjid());
						sjxxList.add(wkmxDto_t);
					}else if ("DETECT_FJ".equals(dto.getLx())){
						wkmxDto_t.setFjid(dto.getFjid());
						fjsqList.add(wkmxDto_t);
					}
				}
				if (!CollectionUtils.isEmpty(syglList)){
					dao.updateSyglSyrq(syglList);
				}
				if (!CollectionUtils.isEmpty(sjxxList)){
					dao.updateSjxxSyrq(sjxxList);
				}
				if (!CollectionUtils.isEmpty(fjsqList)){
					dao.updateFjsqSyrq(fjsqList);
				}
			}
			createTaskReda(sjids);
		}
		return true;
	}

	public boolean insetSjjtxx(Map<String,Object> map){
		Date date=new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String syrq=df.format(date);
		String xgry=String.valueOf(map.get("xgry"));
		List<String> sjids=(List<String>)map.get("sjid");
		List<String> jts=(List<String>)map.get("jt");
		String sfgxsyrq=String.valueOf(map.get("sfgxsyrq"));//判断是新增还是修改，1是新增，0是修改。1进行实验日期更新
		if(sjids.size() != jts.size()) {
			return false;
		}
		List<Map<String,String>> sjjtxxlist= new ArrayList<>();
		for(int i=0;i<sjids.size();i++) {
			if(sjids!=null) {
				String sjidstr=(String) sjids.get(i);
				String jtstr=(String) sjids.get(i);
				Map<String,String> t_map=new HashMap<>();
				t_map.put("xgry",xgry);
				t_map.put("sjid",sjidstr);
				t_map.put("jt",jtstr);

				if(sfgxsyrq.equals("1")) {
					t_map.put("syrq",syrq);
				}
				sjjtxxlist.add(t_map);
				if((sjjtxxlist.size()==4 || i==sjids.size()-1) && sjjtxxlist.size()>0) {
					dao.updateJtxxBySjid(sjjtxxlist);
					sjjtxxlist.clear();
				}
			}
		}
		//由于存在内部编号可以不存在的情况，不论更新是否成功都返回true
		return true;
	}

	/**
	 * 创建任务重跑
	 * @param param
	 * @return
	 */
	public Map<String,String> resetCreateTaskReda(Map<String, String> param){
		List<String> l_sjids = new ArrayList<>();
		if(StringUtil.isNotBlank(param.get("sjids"))) {
			String[] sjidstrs = param.get("sjids").split(",");
			for(String sjidstr:sjidstrs) {
				l_sjids.add(sjidstr);
			}
		}
		boolean isCreate = createTaskReda(l_sjids);
		Map<String,String> map = new HashMap<>();
		map.put("status",String.valueOf(isCreate));
		map.put("message",isCreate?"任务创建成功":"任务创建失败");
		return map;
	}

	@Override
	public boolean createTaskReda(List<String> sjids){
		Map<String,Object>sjxxMap=new HashMap<>();

		List<String>new_sjids= sjids.stream().distinct().collect(Collectors.toList());
		sjxxMap.put("sjids",new_sjids);
		sjxxMap.put("db","先声");
		List<Map<String, String>> sjxxList = new ArrayList<>();
		//经常出现 sjxxlist 内为null情况，为减少sql执行，故增加检查
		if(new_sjids != null && sjids.size()>0 && new_sjids.get(0)!= null) {
			sjxxList = dao.getSjxxInfo(sjxxMap);
		}
		Map<String,Map<String,Object>>createTaskMap=new HashMap<>();
		if(!CollectionUtils.isEmpty(sjxxList)){
			List<String>sampleCodeList=new ArrayList<>();

			String goodsCode="";
			String serviceBillId="";
			String samplerCode="";
			String nodeCode="";
			String XIANSHENG_URL="";
			for(Map<String,String> sjxxInfoMap:sjxxList){
				String qtxx=StringUtil.isBlank(sjxxInfoMap.get("qtxx"))?"":sjxxInfoMap.get("qtxx");
				if(StringUtil.isNotBlank(qtxx)){

					JSONObject qtxxObj=JSON.parseObject(qtxx);
					goodsCode=qtxxObj.get("limsTestCode")==null?"":qtxxObj.getString("limsTestCode");
					XxdyDto xxdyDto=new XxdyDto();
					String code="南京先声-"+goodsCode;
					xxdyDto.setYxx(code);
					List<XxdyDto> xxdyDtoList=xxdyService.getDtoMsgByYxx(xxdyDto);
					Optional<XxdyDto> optional=xxdyDtoList.stream().filter(e->code.equals(e.getYxx())).findFirst();
					if(optional.isPresent()){
						XxdyDto xxdyDto1=optional.get();
						nodeCode=xxdyDto1.getKzcs1();
						serviceBillId=qtxxObj.get("orderId")==null?"":qtxxObj.getString("orderId");
						XIANSHENG_URL=qtxxObj.get("XIANSHENG_URL")==null?"":qtxxObj.getString("XIANSHENG_URL");

						Map<String,Object> taskMap=createTaskMap.get(serviceBillId);
						samplerCode=qtxxObj.get("samplerCode")==null?"":qtxxObj.getString("samplerCode");
						sjxxInfoMap.put("serviceBillId",serviceBillId);
						if(taskMap!=null){
							sampleCodeList=(List<String>)taskMap.get("sampleCodeList");
							sampleCodeList.add(samplerCode);
							taskMap.put("sampleCodeList",sampleCodeList);

						}else{
							taskMap=new HashMap<>();
							taskMap.put("goodsCode",goodsCode);
							taskMap.put("nodeCode",nodeCode);
							taskMap.put("serviceBillId",serviceBillId);
							taskMap.put("XIANSHENG_URL",XIANSHENG_URL);
							taskMap.put("sjid",sjxxInfoMap.get("sjid"));
							taskMap.put("ybbh",sjxxInfoMap.get("ybbh"));
							sampleCodeList=new ArrayList<>();
							sampleCodeList.add(samplerCode);
							taskMap.put("sampleCodeList",sampleCodeList);
						}
						createTaskMap.put(serviceBillId,taskMap);
					}
				}
			}
		}
		List<Map<String,String>> kzxxList=new ArrayList<>();
		if(createTaskMap!=null){
			Set<String> createKeyset=createTaskMap.keySet();
			for(String key:createKeyset){
				Map<String,Object>objMap=createTaskMap.get(key);
				String result=createTask(objMap);
				if(StringUtil.isBlank(result)){
					return false;
				}
				JSONObject jsonObject=JSON.parseObject(result);
				//TODO
				if("0".equals(jsonObject.get("code"))){
					JSONObject dataobj=JSON.parseObject(jsonObject.getString("data"));

					for(Map<String,String> sjxxInfoMap:sjxxList){
						if(key.equals(sjxxInfoMap.get("serviceBillId"))){
							String qtxx=StringUtil.isBlank(sjxxInfoMap.get("qtxx"))?"":sjxxInfoMap.get("qtxx");
							JSONObject qtxxObj=JSON.parseObject(qtxx);
							qtxxObj.put("taskCode",dataobj.getString("taskCode"));
							sjxxInfoMap.put("qtxx",qtxxObj.toJSONString());
							kzxxList.add(sjxxInfoMap);

						}
					}
				}
			}
			if(!CollectionUtils.isEmpty(kzxxList)){
				dao.updateSjkzxx(kzxxList);
			}
		}
        return true;
    }
	/**
	 * 先声接口创建任务单
	 * @param map
	 */
	public String createTask(Map<String,Object> map){
		try {
			RestTemplate restTemplate=new RestTemplate();
			String XIANSHENG_URL = map.get("XIANSHENG_URL").toString();
			String url = XIANSHENG_URL + "/lims/api/open/createTask";
			HttpHeaders requestHeaders = new HttpHeaders();
			requestHeaders.setContentType(MediaType.APPLICATION_PROBLEM_JSON_UTF8);
			requestHeaders.set("source","22");
			String ts = String.valueOf(System.currentTimeMillis());
			requestHeaders.set("ts", ts);
			requestHeaders.set("sign", new Encrypt().stringToMD5("e8fae5d61ef7c2afda1b62c178ebcb71"+ts));
			Map<String, Object> requestBody = new HashMap<>();
			requestBody.put("serviceBillId",map.get("serviceBillId"));
			requestBody.put("goodsCode",map.get("goodsCode"));
			requestBody.put("nodeCode",map.get("nodeCode"));
			requestBody.put("sampleCodeList",map.get("sampleCodeList"));
			HttpEntity httpEntity = new HttpEntity(requestBody,requestHeaders);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
			String body = response.getBody();
			if (200 == response.getStatusCodeValue()){
				log.error("调用先声接口------创建任务单：" + map.get("serviceBillId") + "------请求成功:{}",body);
				JSONObject jsonObject=JSON.parseObject(body);
				if ("0".equals(jsonObject.get("code"))){
					return body;
				} else {
					log.error("调用先声接口------创建任务单：" + map.get("serviceBillId") + "------请求失败:{}",JSON.toJSONString(response));
				}
			} else {
				log.error("调用先声接口------创建任务单：" + map.get("serviceBillId") + "------请求失败2:{}",JSON.toJSONString(response));
				//因为异常进入重试机制
				resetTask(map);
			}
			return body;
		} catch (Exception e) {
			log.error("调用先声接口------创建任务单 异常：" + map.get("serviceBillId") + "--返回结果：" + e.getMessage());
			//因为异常进入重试机制
			resetTask(map);
		}
		return "";
	}

	/**
	 * 重新创建创建任务单
	 * @param map
	 * @return
	 */
	private void resetTask(Map<String,Object> map) {
		//boolean result = true;
		log.error("开始进入南京先声重建任务单流程。");
		Map<String, String> params = new HashMap<String, String>();
		params.put("service", "wkmxServiceImpl");
		params.put("method", "resetCreateTaskReda");
		//params.put("resettime", "1,2,3,4,5");
		params.put("key", "先声：createTask:" + map.get("ybbh") + ":"+map.get("sjid"));
		//第三次的时候进行提醒
		params.put("resetRemindcnt", "3");
		params.put("ifuser", "南京先声");
		params.put("ifname", "创建任务单");
		params.put("ifmsg", "标本编码" + map.get("ybbh") + "创建任务单失败");
		//重试机制的个性化参数
		params.put("sjids", (String)map.get("sjid"));

		interfaceExceptionUtil.comResetMechanism(params);
	}

	/**
	 * 根据文库条码里的内部编码和后缀名称从sjsygl表获取项目代码
	 * @param params
	 * @return
	 */
	public List<Map<String, String>> getXmdmFromSjsyByWk(Map<String, String> params){return dao.getXmdmFromSjsyByWk(params);}

	/**
	 * 根据文库条码里的内部编码和后缀名称从项目管理表获取项目代码
	 * @param params
	 * @return
	 */
	public List<Map<String, String>> getXmdmFromSjxmByWk(Map<String, String> params){return dao.getXmdmFromSjxmByWk(params);}

	/**
	 * 根据文库条码里的内部编码和后缀名称从信息对应表获取项目代码
	 * @param params
	 * @return
	 */
	public List<Map<String, String>> getXmdmFromXxdyByWk(Map<String, String> params){return dao.getXmdmFromXxdyByWk(params);}
}
