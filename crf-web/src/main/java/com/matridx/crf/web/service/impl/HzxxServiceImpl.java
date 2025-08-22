package com.matridx.crf.web.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.matridx.igams.common.exception.BusinessException;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.crf.web.dao.entities.BeanLdzxxFroms;
import com.matridx.crf.web.dao.entities.HzxxDto;
import com.matridx.crf.web.dao.entities.HzxxModel;
import com.matridx.crf.web.dao.entities.NdzxxjlDto;
import com.matridx.crf.web.dao.entities.NdzyzzbDto;
import com.matridx.crf.web.dao.post.IHzxxDao;
import com.matridx.crf.web.dao.post.INdzxxjlDao;
import com.matridx.crf.web.dao.post.INdzdmxqDao;
import com.matridx.crf.web.dao.post.INdzshDao;
import com.matridx.crf.web.dao.post.INdzxcgDao;
import com.matridx.crf.web.dao.post.INdzyzzbDao;
import com.matridx.crf.web.service.svcinterface.IHzxxService;
import com.matridx.crf.web.service.svcinterface.INdzdmxqService;
import com.matridx.crf.web.service.svcinterface.INdzshService;
import com.matridx.crf.web.service.svcinterface.INdzxcgService;
import com.matridx.crf.web.service.svcinterface.INdzyzzbService;
import com.matridx.crf.web.util.XWPFPurchaseUtil;

@Service
public class HzxxServiceImpl extends BaseBasicServiceImpl<HzxxDto, HzxxModel, IHzxxDao> implements IHzxxService {
	@Autowired
	private IHzxxDao hzxxDao;
	@Autowired
	private INdzxxjlDao iNdzxxjlDao;
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	IJcsjService jcsjService;
	@Value("${matridx.rabbit.flg:}")
	private String prefixFlg;
	@Value("${matridx.fileupload.releasePath}")
	private String releaseFilePath;
	@Value("${matridx.fileupload.tempPath}")
	private String tempPath;
	@Value("${matridx.ftp.url:}")
	private String FTP_URL = null;
	@Autowired(required = false)
	private AmqpTemplate amqpTempl;
	@Autowired
	private INdzdmxqService ndzdmxqService;
	@Autowired
	private INdzshService ndzshService;
	@Autowired
	private INdzyzzbService ndzyzzbService;
	@Autowired
	private INdzxcgService ndzxcgService;
	@Autowired
	private INdzyzzbDao ndzyzzbDao;
	@Autowired
	private INdzshDao ndzshDao;
	@Autowired
	private INdzdmxqDao ndzdmxqDao;
	@Autowired
	private INdzxcgDao ndzxcgDao;
	/**
	 * 文档转换完成OK
	 */
	@Value("${spring.rabbitmq.docok:mq.tran.basic.ok}")
	private String DOC_OK = null;;

	/**
	 * 新增
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean insertDto(BeanLdzxxFroms beanLdzxxFroms, HzxxDto hzxxDto){
		String hzid = StringUtil.generateUUID();
		hzxxDto.setHzid(hzid);
		//如果亚组为空的话，不设置脓毒症的值
		if(StringUtil.isNotBlank(hzxxDto.getYz())){
			setNdzxxjl(hzxxDto, beanLdzxxFroms, "add");
		}
		// 第一次新增把
		String djtbj = beanLdzxxFroms.getDjrbj();
		NdzxxjlDto ndzxxjlDtoOne = beanLdzxxFroms.getNdzxxjlDtoOne();
		NdzxxjlDto ndzxxjlDtoThree = beanLdzxxFroms.getNdzxxjlDtoThree();
		NdzxxjlDto ndzxxjlDtoFive = beanLdzxxFroms.getNdzxxjlDtoFive();
		NdzxxjlDto ndzxxjlDtoSeven = beanLdzxxFroms.getNdzxxjlDtoSeven();
		
		if(StringUtil.isBlank(hzxxDto.getYz())){
			ndzxxjlDtoOne.setHzid(hzxxDto.getHzid());
			ndzxxjlDtoThree.setHzid(hzxxDto.getHzid());
			ndzxxjlDtoFive.setHzid(hzxxDto.getHzid());
			ndzxxjlDtoSeven.setHzid(hzxxDto.getHzid());
			String idO = StringUtil.generateUUID();
			String idT = StringUtil.generateUUID();
			String idF = StringUtil.generateUUID();
			String idS = StringUtil.generateUUID();
			ndzxxjlDtoOne.setNdzjlid(idO);
			ndzxxjlDtoFive.setNdzjlid(idF);
			ndzxxjlDtoThree.setNdzjlid(idT);
			ndzxxjlDtoSeven.setNdzjlid(idS);
			// 设置第几天
			ndzxxjlDtoOne.setJldjt("1");
			ndzxxjlDtoThree.setJldjt("3");
			ndzxxjlDtoFive.setJldjt("5");
			ndzxxjlDtoSeven.setJldjt("7");
		}
		if (djtbj.equals("0") || djtbj.equals("1")) {
			iNdzxxjlDao.insert(ndzxxjlDtoOne);
			saveAll(ndzxxjlDtoOne, beanLdzxxFroms, "1");
		}
		if (djtbj.equals("0") || djtbj.equals("3")) {
			iNdzxxjlDao.insert(ndzxxjlDtoThree);
			saveAll(ndzxxjlDtoThree, beanLdzxxFroms, "3");

			ndzdmxqService.saveDmxq(beanLdzxxFroms.getDmxqsThree(), ndzxxjlDtoThree);
		}
		if (djtbj.equals("0") || djtbj.equals("5")) {
			iNdzxxjlDao.insert(ndzxxjlDtoFive);
			saveAll(ndzxxjlDtoFive, beanLdzxxFroms, "5");
		}
		if (djtbj.equals("0") || djtbj.equals("7")) {
			iNdzxxjlDao.insert(ndzxxjlDtoSeven);
			saveAll(ndzxxjlDtoSeven, beanLdzxxFroms, "7");
		}
		// 保存
		// 设置纳入编号
		getNrbh(hzxxDto);
		hzxxDto.setScbj("0");
		if(hzxxDto.getFjids()!=null && hzxxDto.getFjids().size() > 0){
			for (int i = 0; i < hzxxDto.getFjids().size(); i++) {
				boolean saveFile = fjcfbService.save2RealFile(hzxxDto.getFjids().get(i),hzxxDto.getHzid());
				if(!saveFile)
					return false;
			}
		}else {
			//当附件没有之的时候，不存报告日期
			hzxxDto.setBgsj(null);
		}
		hzxxDao.insert(hzxxDto);
		
		return true;
	}

	/**
	 * 修改
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateDto(BeanLdzxxFroms beanLdzxxFroms, HzxxDto hzxxDto) {
		String hzid = hzxxDto.getHzid();
		// 处理不同亚组，存入的值过滤
		setNdzxxjl(hzxxDto, beanLdzxxFroms, "mod");
		NdzxxjlDto ndzxxjlDtoOne = beanLdzxxFroms.getNdzxxjlDtoOne();
		NdzxxjlDto ndzxxjlDtoThree = beanLdzxxFroms.getNdzxxjlDtoThree();
		NdzxxjlDto ndzxxjlDtoFive = beanLdzxxFroms.getNdzxxjlDtoFive();
		NdzxxjlDto ndzxxjlDtoSeven = beanLdzxxFroms.getNdzxxjlDtoSeven();
		String djtbj = beanLdzxxFroms.getDjrbj();
		//判断是否有文件
		List<FjcfbDto> flList = getFjcfb(hzxxDto);
		if(hzxxDto.getFjids()!=null && hzxxDto.getFjids().size() > 0){
			for (int i = 0; i < hzxxDto.getFjids().size(); i++) {
				boolean saveFile = fjcfbService.save2RealFile(hzxxDto.getFjids().get(i),hzxxDto.getHzid());
				if(!saveFile)
					return false;
			}
		}else {
			if(flList==null) {
				hzxxDto.setBgsj(null);
			}else if(flList.size()==0) {
				hzxxDto.setBgsj(null);
			}

		}

		if (djtbj.equals("0") || djtbj.equals("1")) {
			if (StringUtil.isNotBlank(ndzxxjlDtoOne.getNdzjlid())) {
				ndzxxjlDtoOne.setXgr(hzxxDto.getXgr());
				iNdzxxjlDao.update(ndzxxjlDtoOne);
			} else {
				ndzxxjlDtoOne.setHzid(hzid);
				String idO = StringUtil.generateUUID();
				ndzxxjlDtoOne.setNdzjlid(idO);
				ndzxxjlDtoOne.setJldjt("1");
				ndzxxjlDtoOne.setTjr(hzxxDto.getXgr());
				iNdzxxjlDao.insert(ndzxxjlDtoOne);
			}
			saveAll(ndzxxjlDtoOne, beanLdzxxFroms, "1");

		}
		if (djtbj.equals("0") || djtbj.equals("3")) {
			if (StringUtil.isNotBlank(ndzxxjlDtoThree.getNdzjlid())) {
			ndzxxjlDtoThree.setXgr(hzxxDto.getXgr());
			iNdzxxjlDao.update(ndzxxjlDtoThree);
			} else {
				ndzxxjlDtoThree.setHzid(hzid);
				String idO = StringUtil.generateUUID();
				ndzxxjlDtoThree.setNdzjlid(idO);
				ndzxxjlDtoThree.setJldjt("3");
				ndzxxjlDtoThree.setTjr(hzxxDto.getXgr());
				iNdzxxjlDao.insert(ndzxxjlDtoThree);
			}
			saveAll(ndzxxjlDtoThree, beanLdzxxFroms, "3");
		}
		if (djtbj.equals("0") || djtbj.equals("5")) {
			if (StringUtil.isNotBlank(ndzxxjlDtoFive.getNdzjlid())) {
				ndzxxjlDtoFive.setXgr(hzxxDto.getXgr());
				iNdzxxjlDao.update(ndzxxjlDtoFive);
				} else {
					ndzxxjlDtoFive.setHzid(hzid);
					String idO = StringUtil.generateUUID();
					ndzxxjlDtoFive.setNdzjlid(idO);
					ndzxxjlDtoFive.setJldjt("5");
					ndzxxjlDtoFive.setTjr(hzxxDto.getXgr());
					iNdzxxjlDao.insert(ndzxxjlDtoFive);
				}
			saveAll(ndzxxjlDtoFive, beanLdzxxFroms, "5");
		}
		if (djtbj.equals("0") || djtbj.equals("7")) {
			if (StringUtil.isNotBlank(ndzxxjlDtoSeven.getNdzjlid())) {
				ndzxxjlDtoSeven.setXgr(hzxxDto.getXgr());
				iNdzxxjlDao.update(ndzxxjlDtoSeven);
				} else {
					ndzxxjlDtoSeven.setHzid(hzid);
					String idO = StringUtil.generateUUID();
					ndzxxjlDtoSeven.setNdzjlid(idO);
					ndzxxjlDtoSeven.setJldjt("7");
					ndzxxjlDtoSeven.setTjr(hzxxDto.getXgr());
					iNdzxxjlDao.insert(ndzxxjlDtoSeven);
				}
			saveAll(ndzxxjlDtoSeven, beanLdzxxFroms, "7");
		}
		if (djtbj.equals("0") || djtbj.equals("2")) {
			// 保存
			getNrbh(hzxxDto);
			hzxxDao.update(hzxxDto);
		}
		return true;
	}

	/**
	 * 删除
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean delHzxx(String hzid, String userId) {
		String[] hzids = hzid.split(",");
		for (String id : hzids) {
			HzxxDto hz = hzxxDao.getDtoById(id);
			if (hz != null) {
				hz.setScbj("1");
				hz.setXgr(userId);
				// 同时删除报告记录
				List<NdzxxjlDto> list = iNdzxxjlDao.getNdzByHzid(hz.getHzid());
				if (list != null && list.size() > 0) {
					for (NdzxxjlDto ld : list) {
						if (ld != null) {
							ld.setScbj("1");
							ld.setXgr(userId);
							iNdzxxjlDao.update(ld);
						}
					}
				}
				int result = hzxxDao.update(hz);
			}
		}
		return true;
	}

	/**
	 * 获取单日上传的文件信息
	 * @param ndzxxjlDto
	 * @return
	 */
	public FjcfbDto getFjcfbByjlid(NdzxxjlDto ndzxxjlDto){
		return dao.getFjcfbByjlid(ndzxxjlDto);
	}


	/**
	 *报告上传保存
	 * @param hzxxDto
	 * @return
	 * @throws BusinessException
	 */
	public boolean uploadHzxxSave(HzxxDto hzxxDto) throws BusinessException{
		if(hzxxDto.getFjids()!=null && hzxxDto.getFjids().size() > 0){
			for (int i = 0; i < hzxxDto.getFjids().size(); i++) {
				boolean saveFile = fjcfbService.save2RealFile(hzxxDto.getFjids().get(i),hzxxDto.getHzid());
				if(!saveFile)
					throw new BusinessException("msg","附件保存失败!");
			}
		}else {
			List<FjcfbDto> flList = getFjcfb(hzxxDto);
			if(flList==null) {
				hzxxDto.setBgsj(null);
			}else if(flList.size()==0) {
				hzxxDto.setBgsj(null);
			}
		}
		hzxxDao.updateBgsjById(hzxxDto);
		return true;
	}
	/**
	 * 查询数据,并导出
	 * 
	 * @param
	 * @return
	 */
	@Override

	public List<Map<String, Object>> getParamForHzxx(HzxxDto hzxxDto) {
		// TODO Auto-generated method stub
		FjcfbDto fjDto = new FjcfbDto();
		fjDto.setYwid(hzxxDto.getHzid());
		fjDto.setYwlx(BusTypeEnum.IMP_HZXX_TEMPLATE.getCode());
		List<FjcfbDto> fjlist = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjDto);
		if (fjlist != null && fjlist.size() > 0) {
			DBEncrypt bpe = new DBEncrypt();
			for (FjcfbDto fjcfbDto : fjlist) {
				if (StringUtils.isNotBlank(fjcfbDto.getWjlj())) {
					String path = bpe.dCode(fjcfbDto.getWjlj());
					File file = new File(path);
					if (file.exists())
						file.delete();
				}
				if (StringUtils.isNotBlank(fjcfbDto.getZhwjxx())) {
					String path = bpe.dCode(fjcfbDto.getZhwjxx());
					File file = new File(path);
					if (file.exists())
						file.delete();
				}
			}
			// 删除数据表信息
			fjcfbService.deleteByYwidAndYwlx(fjDto);
		}
		XWPFPurchaseUtil xwpfPurchaseUtil = new XWPFPurchaseUtil();
		Map<String, Object> resultMap = new HashedMap();
		List<Map<String, Object>> resultmap_list = new ArrayList<Map<String, Object>>();

		// 亚组对应的模板类型
		hzxxDto = hzxxDao.getDtoById(hzxxDto.getHzid());
		JcsjDto jsMb = jcsjService.getDtoById(hzxxDto.getYz());
		if(StringUtil.isBlank(hzxxDto.getYz())) {
			JcsjDto jSDtoYZDto = new JcsjDto();
			jSDtoYZDto.setJclb("REPORTMODEL");
			jSDtoYZDto.setCsdm("UNKNOWN");
			jsMb = jcsjService.getDtoByCsdmAndJclb(jSDtoYZDto);
			jsMb.setCskz1(jsMb.getCsdm());
		}
		if (jsMb == null) {
			resultMap.put("message", "模板不存在！请重新确认模板！");
			resultMap.put("status", "error");
			resultmap_list.add(resultMap);
			return resultmap_list;
		}
		hzxxDto.setMbYzLx(jsMb.getCskz1());
		Map<String, Object> map = hzxxDao.getHzxxDtoMap(hzxxDto);
		// 将基础数据中的的名字替换掉
		map.put("grbw", getNames(map.get("grbw"), BasicDataTypeEnum.ANTIBACTERIALHISTORY.getCode()));
		map.put("jwhbz", getNames(map.get("jwhbz"), BasicDataTypeEnum.COMPLICATION.getCode()));
		map.put("cicuzt", getNames(map.get("cicuzt"), BasicDataTypeEnum.DISCHARGESTATUS.getCode()));
		if (map.get("qtgrbw") != null && !map.get("qtgrbw").equals("")) {
			map.put("qtgrbw", "既往抗菌药物暴露史：" + map.get("qtgrbw"));
		}
		// 获取单日的记录信息
		List<NdzxxjlDto> ndzMapList = iNdzxxjlDao.getNdzByHzid(hzxxDto.getHzid());
		if (ndzMapList != null && ndzMapList.size() > 0) {
			for (NdzxxjlDto l : ndzMapList) {
				if (l != null) {
					if (l.getJldjt().equals("1")) {
						putndzMap(map, l, "1");
					} else if (l.getJldjt().equals("3")) {
						putndzMap(map, l, "3");
					} else if (l.getJldjt().equals("5")) {
						putndzMap(map, l, "5");
					} else if (l.getJldjt().equals("7")) {
						putndzMap(map, l, "7");
					}
				}
			}
		}
		// 查询单日病例并放到map中
		map.put("releaseFilePath", releaseFilePath); // 正式文件路径
		map.put("tempPath", tempPath); // 临时文件路径
		map.put("hwmc", "CES");
		resultMap = xwpfPurchaseUtil.replacePurchase(map, fjcfbService, FTP_URL, DOC_OK, amqpTempl, "1");
		resultmap_list.add(resultMap);

		return resultmap_list;
	}
	/**
	 * 获取上传的文件信息
	 * @param hzxxDto
	 * @return
	 */
	public List<FjcfbDto> getFjcfb(HzxxDto hzxxDto){
		return dao.getFjcfb(hzxxDto);
	}
	/**
	 * 将脓毒症的单日记录信息放入map中
	 * 
	 * @param hzxxMap
	 * @param NdzxxjlDto
	 * @param dayType
	 */
	public void putndzMap(Map<String, Object> hzxxMap, NdzxxjlDto ndzxxjlDto, String dayType) {
		hzxxMap.put("hrmax" + dayType, ndzxxjlDto.getHrmax());
		hzxxMap.put("mapmax" + dayType, ndzxxjlDto.getMapmax());
		hzxxMap.put("sapmax" + dayType, ndzxxjlDto.getSapmax());
		hzxxMap.put("rrmax" + dayType, ndzxxjlDto.getRrmax());
		hzxxMap.put("tmax" + dayType, ndzxxjlDto.getTmax());
		// 将配置的信息放入map中
		List<Map<String, String>> lists = ndzshDao.getSjzName(ndzxxjlDto.getNdzjlid());
		List<Map<String, String>> listd = ndzdmxqDao.getSjzName(ndzxxjlDto.getNdzjlid());
		List<Map<String, String>> listx = ndzxcgDao.getSjzName(ndzxxjlDto.getNdzjlid());
		List<Map<String, String>> listy = ndzyzzbDao.getSjzName(ndzxxjlDto.getNdzjlid());
		putHzxxMap(lists, hzxxMap, dayType);
		putHzxxMap(listd, hzxxMap, dayType);
		putHzxxMap(listx, hzxxMap, dayType);
		putHzxxMap(listy, hzxxMap, dayType);
		if (ndzxxjlDto.getXghxy() != null) {
			if (ndzxxjlDto.getXghxy().equals("1")) {
				hzxxMap.put("xghxy" + dayType, "是");
			} else if (ndzxxjlDto.getXghxy().equals("0")) {
				hzxxMap.put("xghxy" + dayType, "否");
			}
		}
		if (ndzxxjlDto.getCrrt() != null) {
			if (ndzxxjlDto.getCrrt().equals("1")) {
				hzxxMap.put("crrt" + dayType, "是");
			} else if (ndzxxjlDto.getCrrt().equals("0")) {
				hzxxMap.put("crrt" + dayType, "否");
			}
		}
		if (ndzxxjlDto.getJxtq() != null) {
			if (ndzxxjlDto.getJxtq().equals("1")) {
				hzxxMap.put("jxtq" + dayType, "是");
			} else if (ndzxxjlDto.getJxtq().equals("0")) {
				hzxxMap.put("jxtq" + dayType, "否");
			}
		}
		hzxxMap.put("gcs" + dayType, ndzxxjlDto.getGcs());
		if (dayType.equals("1")) {
			if (ndzxxjlDto.getSjqsfsykjyw() != null) {
				if (ndzxxjlDto.getSjqsfsykjyw().equals("1")) {
					hzxxMap.put("sjqsfsykjyw" + dayType, "是");
				} else if (ndzxxjlDto.getSjqsfsykjyw().equals("0")) {
					hzxxMap.put("sjqsfsykjyw" + dayType, "否");
				}
			}
			hzxxMap.put("kjywzl" + dayType, ndzxxjlDto.getKjywzl());
			if (ndzxxjlDto.getMcfdna() != null) {
				if (ndzxxjlDto.getMcfdna().equals("1")) {
					hzxxMap.put("mcfdna" + dayType, "是");
				} else if (ndzxxjlDto.getMcfdna().equals("0")) {
					hzxxMap.put("mcfdna" + dayType, "否");
				}
			}
			if (ndzxxjlDto.getXpy() != null) {
				if (ndzxxjlDto.getXpy().equals("1")) {
					hzxxMap.put("xpy" + dayType, "是");
				} else if (ndzxxjlDto.getXpy().equals("0")) {
					hzxxMap.put("xpy" + dayType, "否");
				}
			}
			hzxxMap.put("ttp" + dayType, ndzxxjlDto.getTtp());
			if (ndzxxjlDto.getTtp() != null) {
				if (ndzxxjlDto.getTtp().equals("1")) {
					hzxxMap.put("ttp" + dayType, "是");
				} else if (ndzxxjlDto.getTtp().equals("0")) {
					hzxxMap.put("ttp" + dayType, "否");
				}
			}
			if (ndzxxjlDto.getTpy() != null) {
				if (ndzxxjlDto.getTpy().equals("1")) {
					hzxxMap.put("tpy" + dayType, "是");
				} else if (ndzxxjlDto.getTpy().equals("0")) {
					hzxxMap.put("tpy" + dayType, "否");
				}
			}
			if (ndzxxjlDto.getFstp() != null) {
				if (ndzxxjlDto.getFstp().equals("1")) {
					hzxxMap.put("fstp" + dayType, "是");
				} else if (ndzxxjlDto.getFstp().equals("0")) {
					hzxxMap.put("fstp" + dayType, "否");
				}
			}
			if (ndzxxjlDto.getFspy() != null) {
				if (ndzxxjlDto.getFspy().equals("1")) {
					hzxxMap.put("fspy" + dayType, "是");
				} else if (ndzxxjlDto.getFspy().equals("0")) {
					hzxxMap.put("fspy" + dayType, "否");
				}
			}
			if (ndzxxjlDto.getQtf() != null) {
				if (ndzxxjlDto.getQtf().equals("1")) {
					hzxxMap.put("qtf" + dayType, "是");
				} else if (ndzxxjlDto.getQtf().equals("0")) {
					hzxxMap.put("qtf" + dayType, "否");
				}
			}
			if (ndzxxjlDto.getQtt() != null) {
				if (ndzxxjlDto.getQtt().equals("1")) {
					hzxxMap.put("qtt" + dayType, "是");
				} else if (ndzxxjlDto.getQtt().equals("0")) {
					hzxxMap.put("qtt" + dayType, "否");
				}
			}
			hzxxMap.put("mcfdnajg" + dayType, ndzxxjlDto.getMcfdnajg());
			hzxxMap.put("xpyjg" + dayType, ndzxxjlDto.getXpyjg());
			hzxxMap.put("ttpjg" + dayType, ndzxxjlDto.getTtpjg());
			hzxxMap.put("tpyjg" + dayType, ndzxxjlDto.getTpyjg());
			hzxxMap.put("fstpjg" + dayType, ndzxxjlDto.getFstpjg());
			hzxxMap.put("fspyjg" + dayType, ndzxxjlDto.getFspyjg());
			hzxxMap.put("qtfjg" + dayType, ndzxxjlDto.getQtfjg());
			hzxxMap.put("qttjg" + dayType, ndzxxjlDto.getQttjg());
			hzxxMap.put("apache2" + dayType, ndzxxjlDto.getApache2());
			hzxxMap.put("sofapf" + dayType, ndzxxjlDto.getSofapf());
		}

	}

	public void putHzxxMap(List<Map<String, String>> list, Map<String, Object> hzxxMap, String dayType) {
		if (list != null && list.size() > 0) {
			for (Map<String, String> t : list) {
				if (t.get("sjz") != null && !t.get("sjz").equals("")) {
					hzxxMap.put(t.get("cskz1") + dayType, t.get("sjz"));
				}
			}
		}
	}

	/**
	 * 替换患者表中的名字
	 */
	public String getNames(Object name, String type) {
		String names = "";
		if (name != null && !name.toString().equals("")) {
			JcsjDto grbwjsDto = new JcsjDto();
			grbwjsDto.setJclb(type);
			grbwjsDto.setIds(name.toString());
			List<JcsjDto> grbwjcsjDtos = jcsjService.selectDetectionUnit(grbwjsDto);
			if (grbwjcsjDtos != null && grbwjcsjDtos.size() > 0) {
				for (JcsjDto jcs : grbwjcsjDtos) {
					names = names + jcs.getCsmc() + "  ";
				}
			}
		}
		return names;
	}

	/**
	 * 根据角色获取医院信息
	 */
	@Override
	public List<Map> getHospitailList(HzxxDto hzxxDto) {

		return dao.getHospitailList(hzxxDto);
	}

	// 拼接纳入研究编号
	public void getNrbh(HzxxDto hzxxDto) {
		Map<String, Object> map = hzxxDao.getnryjbh(hzxxDto);
		JcsjDto js = new JcsjDto();
		js.setCsid(hzxxDto.getSsyy());
		JcsjDto jcsjDto = jcsjService.getDto(js);
		String nrbh = jcsjDto.getCsdm();
		boolean isSc = true;
		// 如果本身有纳入编号，并且医院没变动，不修改
		if (hzxxDto.getNryjbh() != null && !hzxxDto.getNryjbh().equals("")) {
			if (hzxxDto.getNryjbh().length() > 4) {
				if (hzxxDto.getNryjbh().substring(0, 4).equals(nrbh)) {
					isSc = false;
				}
			}
		}
		if (isSc) {
			BigDecimal xh = new BigDecimal(1);
			if (map != null) {
				if (map.get("nryjbhpx") != null && map.get("nryjbh") != null) {
					xh = ((BigDecimal) map.get("nryjbhpx")).add(new BigDecimal(1));
				}
			}
			hzxxDto.setNryjbh(nrbh + xh);
			hzxxDto.setNryjbhpx("" + xh);
		}
	}

//保存脓毒症配置信息
	public void saveAll(NdzxxjlDto ndzxxjlDto, BeanLdzxxFroms beanLdzxxFroms, String day) {
		if (day.equals("1")) {
			ndzdmxqService.saveDmxq(beanLdzxxFroms.getDmxqsOne(), ndzxxjlDto);
			ndzshService.saveSh(beanLdzxxFroms.getShsOne(), ndzxxjlDto);
			ndzxcgService.saveXcg(beanLdzxxFroms.getXcgsOne(), ndzxxjlDto);
			ndzyzzbService.saveYzzb(beanLdzxxFroms.getYzzbsOne(), ndzxxjlDto);

		} else if (day.equals("3")) {
			ndzdmxqService.saveDmxq(beanLdzxxFroms.getDmxqsThree(), ndzxxjlDto);
			ndzshService.saveSh(beanLdzxxFroms.getShsThree(), ndzxxjlDto);
			ndzxcgService.saveXcg(beanLdzxxFroms.getXcgsThree(), ndzxxjlDto);
			ndzyzzbService.saveYzzb(beanLdzxxFroms.getYzzbsThree(), ndzxxjlDto);

		} else if (day.equals("5")) {
			ndzdmxqService.saveDmxq(beanLdzxxFroms.getDmxqsFive(), ndzxxjlDto);
			ndzshService.saveSh(beanLdzxxFroms.getShsFive(), ndzxxjlDto);
			ndzxcgService.saveXcg(beanLdzxxFroms.getXcgsFive(), ndzxxjlDto);
			ndzyzzbService.saveYzzb(beanLdzxxFroms.getYzzbsFive(), ndzxxjlDto);

		} else if (day.equals("7")) {
			ndzdmxqService.saveDmxq(beanLdzxxFroms.getDmxqsSeven(), ndzxxjlDto);
			ndzshService.saveSh(beanLdzxxFroms.getShsSeven(), ndzxxjlDto);
			ndzxcgService.saveXcg(beanLdzxxFroms.getXcgsSeven(), ndzxxjlDto);
			ndzyzzbService.saveYzzb(beanLdzxxFroms.getYzzbsSeven(), ndzxxjlDto);

		}

	}

	// 判断根绝亚组分类，过滤不需要存入的值
	public void setNdzxxjl(HzxxDto hzxxDto, BeanLdzxxFroms beanLdzxxFroms, String type) {
		// 获取亚组分类
		JcsjDto jsYz = jcsjService.getDtoById(hzxxDto.getYz());
		String csck1 = jsYz.getCskz1();
		NdzxxjlDto ndzxxjlDtoOne = beanLdzxxFroms.getNdzxxjlDtoOne();
		NdzxxjlDto ndzxxjlDtoSeven = beanLdzxxFroms.getNdzxxjlDtoSeven();
		NdzxxjlDto ndzxxjlDtoThree = new NdzxxjlDto();
		NdzxxjlDto ndzxxjlDtoFive = new NdzxxjlDto();
		// 只有免疫抑制版和成人通用版本，保存三七日
		if (jsYz.getCskz1().equals("ADULT") || jsYz.getCskz1().equals("IMMUNOSUPPRESSION")) {
			ndzxxjlDtoThree = beanLdzxxFroms.getNdzxxjlDtoThree();
			ndzxxjlDtoFive = beanLdzxxFroms.getNdzxxjlDtoFive();
		} else {
			// 将其他两天的配置项置空
			beanLdzxxFroms.setDmxqsFive(null);
			beanLdzxxFroms.setDmxqsThree(null);
			beanLdzxxFroms.setYzzbsFive(null);
			beanLdzxxFroms.setYzzbsThree(null);
			beanLdzxxFroms.setXcgsFive(null);
			beanLdzxxFroms.setXcgsThree(null);
			beanLdzxxFroms.setShsFive(null);
			beanLdzxxFroms.setShsThree(null);
		}
		// 炎症指标，不同版本之间不同显示值
		if ("ADULT".equals(jsYz.getCskz1()) || "CHILD".equals(jsYz.getCskz1())) {
			// 成人版，儿童版本不需要保存炎症中的新加值
			removeYzzb(beanLdzxxFroms.getYzzbsFive());
			removeYzzb(beanLdzxxFroms.getYzzbsOne());
			removeYzzb(beanLdzxxFroms.getYzzbsSeven());
			removeYzzb(beanLdzxxFroms.getYzzbsThree());
		}
		if (type.equals("add")) {
			ndzxxjlDtoOne.setHzid(hzxxDto.getHzid());
			ndzxxjlDtoThree.setHzid(hzxxDto.getHzid());
			ndzxxjlDtoFive.setHzid(hzxxDto.getHzid());
			ndzxxjlDtoSeven.setHzid(hzxxDto.getHzid());
			String idO = StringUtil.generateUUID();
			String idT = StringUtil.generateUUID();
			String idF = StringUtil.generateUUID();
			String idS = StringUtil.generateUUID();
			ndzxxjlDtoOne.setNdzjlid(idO);
			ndzxxjlDtoFive.setNdzjlid(idF);
			ndzxxjlDtoThree.setNdzjlid(idT);
			ndzxxjlDtoSeven.setNdzjlid(idS);
			// 设置第几天
			ndzxxjlDtoOne.setJldjt("1");
			ndzxxjlDtoThree.setJldjt("3");
			ndzxxjlDtoFive.setJldjt("5");
			ndzxxjlDtoSeven.setJldjt("7");

		} else if (type.equals("mod")) {
			ndzxxjlDtoThree.setNdzjlid(beanLdzxxFroms.getNdzxxjlDtoThree().getNdzjlid());
			ndzxxjlDtoFive.setNdzjlid(beanLdzxxFroms.getNdzxxjlDtoFive().getNdzjlid());
		}
		beanLdzxxFroms.setNdzxxjlDtoOne(ndzxxjlDtoOne);
		beanLdzxxFroms.setNdzxxjlDtoThree(ndzxxjlDtoThree);
		beanLdzxxFroms.setNdzxxjlDtoFive(ndzxxjlDtoFive);
		beanLdzxxFroms.setNdzxxjlDtoSeven(ndzxxjlDtoSeven);
	}
	public void removeYzzb(List<NdzyzzbDto> yzzbs ) {
		if (yzzbs != null && yzzbs.size() > 0) {
			for (int i = yzzbs.size() - 1; i >= 0; i--) {
				if ("1".equals(yzzbs.get(i).getCskz2())) {
					yzzbs.remove(i);
				}
			}
		}
	}
}
