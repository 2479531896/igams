package com.matridx.igams.storehouse.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import com.matridx.igams.storehouse.dao.entities.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.ImportRecordModel;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IFileImport;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.production.dao.entities.WlglDto;
import com.matridx.igams.production.service.svcinterface.IRdRecordService;
import com.matridx.igams.production.service.svcinterface.IWlglService;
import com.matridx.igams.storehouse.dao.post.IHwllxxDao;
import com.matridx.igams.storehouse.service.svcinterface.ICkhwxxService;
import com.matridx.igams.storehouse.service.svcinterface.IHwllmxService;
import com.matridx.igams.storehouse.service.svcinterface.IHwllxxService;
import com.matridx.igams.storehouse.service.svcinterface.IHwxxService;
import com.matridx.igams.storehouse.service.svcinterface.ILlglService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.util.CollectionUtils;

@Service
public class HwllxxServiceImpl extends BaseBasicServiceImpl<HwllxxDto, HwllxxModel, IHwllxxDao>
		implements IHwllxxService ,IAuditService,IFileImport{
	@Autowired
	ICkhwxxService ckhwxxService;
	@Autowired
	IHwxxService hwxxService;
	@Autowired
	ILlglService llglService;
	@Autowired
	IHwllmxService hwllmxService;
	@Autowired
	IRdRecordService rdRecordService;
	@Autowired
	IWlglService wlglService;
	@Autowired
	ICommonService commonService;
	@Autowired
	IJcsjService jcsjService;
	private final Logger log = LoggerFactory.getLogger(HwllxxServiceImpl.class);

	/**
	 * 根据出库ID查询货物领料信息
	 * @param ckid
	 * @return
	 */
	public List<HwllxxDto> getDtoHwllxxListByCkidPrint(String ckid){
		return dao.getDtoHwllxxListByCkidPrint(ckid);

	}
	/**
	 * 根据领料ID查询货物领料信息
	 * @param llid
	 * @return
	 */
	@Override
	public List<HwllxxDto> getDtoHwllxxListByLlidPrint(String llid){
		return dao.getDtoHwllxxListByLlid(llid);
	}

	/**
	 * 删除货物领料信息
	 *
	 * @param hwllxxDto
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean delDtoListReceiveMaterie(HwllxxDto hwllxxDto) {
//		Boolean result = false;
//		//根据wlid获取仓库货物信息
//		CkhwxxDto ckhwxxDto = ckhwxxService.getDtoById(hwllxxDto.getWlid());
//		//修改库存量
//		BigDecimal yds = new BigDecimal(ckhwxxDto.getYds()).subtract(new BigDecimal(hwllxxDto.getQlsl()));
//		ckhwxxDto.setYds(yds.toString());
//		result = ckhwxxService.updateDto(ckhwxxDto);
//		if (!result) {
//			throw new BusinessException("msg", "删除失败!");
//		}
//		//如果为审核中的数据，则同步删除 审核过程中的数据
//			
//		//当货物信息的库存量==0时，删除 仓库货物信息 表里的该物料信息
//		HwxxDto hwxxDto = hwxxService.getDtoById(hwllxxDto.getWlid());
//		BigDecimal kcl = new BigDecimal(hwxxDto.getKcl());
//		if (kcl == new BigDecimal(0)) {
//			result = ckhwxxService.delete(ckhwxxDto);
//		}
//		if ( !result) {
//			throw new BusinessException("msg", "删除失败!");
//		}
		return dao.delDtoListReceiveMaterie(hwllxxDto);
	}

	@Override
	public List<HwllxxDto> getHwllInfo(HwllxxDto hwllxxDto, String userId) {
		//		CkhwxxDto ckhwxxDto1 = ckhwxxService.getJsInfo(userId);
//		if (null != ckhwxxDto1.getKzcs2() && "" != ckhwxxDto1.getKzcs2()) {
//			String[] temps = ckhwxxDto1.getKzcs2().split(",");
//			List<String> arr = new ArrayList<>();
//			for (String ckid : temps) {
//				arr.add(ckid);
//			}
//			List<String> strs = new ArrayList<>();
//			for (HwllxxDto hwllxx:hwllxxDtos) {
//				strs.add(hwllxx.getWlid());
//			}
//			HwxxDto hwxxDto = new HwxxDto();
//			hwxxDto.setIds(arr);
//			hwxxDto.setWlids(strs);
//			List<HwxxDto> hwxxDtos = hwxxService.getHwInfoByCkids(hwxxDto);
//			for (HwllxxDto hwllxx : hwllxxDtos) {
//				for (HwxxDto hwxxDto_r : hwxxDtos) {
//					if(hwllxx.getWlid().equals(hwxxDto_r.getWlid())) {
//						Double kcl = Double.parseDouble(hwxxDto_r.getKcl()) + Double.parseDouble(hwllxx.getQlsl());
//						hwllxx.setKcl(kcl.toString());
//						hwllxx.setKqls(kcl.toString());
//						break;
//					}
//				}
//			}
//
//		}
		return dao.getDtoList(hwllxxDto);
	}

	/**
	 * 获取货物领料信息和货物信息
	 * @return
	 */
	@Override
	public List<HwllxxDto> queryHwllxx(String llid,String bj,String dqjs) {
		LlglDto llglDto = llglService.getDtoById(llid);
		HwllxxDto hwllxxDto = new HwllxxDto();
		hwllxxDto.setLlid(llid);
		List<HwllxxDto> hwllxxDtos;
		//判断是否为取样请领，如果是查出请领明细
		if("1".equals(llglDto.getQybj())) {
			hwllxxDtos = dao.getListByLlid(hwllxxDto);			
		}else {
			hwllxxDtos = getDtoList(hwllxxDto);
		}
		List<HwxxDto> hwxxs = new ArrayList<>();
//		CkhwxxDto ckhwxxDto = ckhwxxService.getJsInfo(dqjs);
//		List<String> arr = new ArrayList<>();
//		if (null != ckhwxxDto.getCkqx() && "" != ckhwxxDto.getCkqx()){
//			String[] temps = ckhwxxDto.getCkqx().split(",");
//			for (String ckqx:temps) {
//				arr.add(ckqx);
//			}
//		}
		for (HwllxxDto hwllxxDto_t : hwllxxDtos) {
			HwxxDto hwxxDto = new HwxxDto();
			hwxxDto.setLlid(llid);
			if("1".equals(llglDto.getQybj())) {
				hwxxDto.setHwid(hwllxxDto_t.getHwid());
			}else {
				hwxxDto.setWlid(hwllxxDto_t.getWlid());
				hwxxDto.setCkid(hwllxxDto_t.getCkid());
				hwxxDto.setHwzt("99");
			}	
			List<HwxxDto> hwxxDtos = hwxxService.getDtoList(hwxxDto);
			//去除为可领数量为0的货物
			if(!"1".equals(llglDto.getQybj())) {
				for (HwxxDto hwxxDto_t : hwxxDtos) {
					hwxxDto_t.setHwllid(hwllxxDto_t.getHwllid());
					hwxxDto_t.setLlid(llid);
					hwxxDto_t.setQlsl(hwllxxDto_t.getQlsl());
					hwxxDto_t.setSlsl(hwxxDto_t.getYxsl());
					if(StringUtil.isBlank(hwxxDto_t.getYxsl())) {
						hwxxDto_t.setYxsl("0");
					}
					BigDecimal klsl = new BigDecimal(hwxxDto_t.getKlsl()).setScale(2, RoundingMode.HALF_UP);
					if("0".equals(bj)) {
						if(new BigDecimal("0").compareTo(klsl) == 0 ){
							hwxxs.add(hwxxDto_t);
						}
					}else {
						if(new BigDecimal("0").compareTo(klsl) == 0 || new BigDecimal("0").compareTo(new BigDecimal(hwxxDto_t.getYxsl())) == 0 ){
							hwxxs.add(hwxxDto_t);
						}
					}
				}		
				hwxxDtos.removeAll(hwxxs);
				List<HwxxDto> setList = hwxxDtos.stream()
	                    .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(HwxxDto::getHwid))), ArrayList::new));
				hwllxxDto_t.setHwxxDtos(setList);
			}else {
				hwllxxDto_t.setHwxxDtos(hwxxDtos);
			}
			
		}
		return hwllxxDtos;
	}

	/**
	 * 根据物料分组查询请领总数
	 * @return
	 */
	@Override
	public List<HwllxxDto> queryCkhwGroupByWlid(HwllxxDto hwllxxDto) {
		// TODO Auto-generated method stub
		return dao.queryCkhwGroupByWlid(hwllxxDto);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam)
			throws BusinessException {
		LlglDto llglDto = (LlglDto) baseModel;
		llglDto.setXgry(operator.getYhid());
		return llglService.deliverymodSavePicking(llglDto);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam)
			throws BusinessException {
		if (shgcList == null || shgcList.isEmpty()) {
			return true;
		}
		for (ShgcDto shgcDto : shgcList) {
			LlglDto llglDto = new LlglDto();
			llglDto.setLlid(shgcDto.getYwid());
			llglDto.setXgry(operator.getYhid());
			// 审核退回
			if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
				llglDto.setCkzt(StatusEnum.CHECK_UNPASS.getCode());
			}else {
				llglDto.setCkzt(StatusEnum.CHECK_PASS.getCode());
				LlglDto llglDto_t = llglService.getDtoById(llglDto.getLlid());
				llglDto_t.setZsxm(operator.getZsxm());
				//出库U8操作
				Map<String, Object> map = rdRecordService.addU8CkData(llglDto_t);
				LlglDto llglDto_ll = (LlglDto) map.get("llglDto");
				llglDto.setCkglid(llglDto_ll.getCkglid());
				@SuppressWarnings("unchecked")
				List<HwllmxDto> hwllmxList = (List<HwllmxDto>) map.get("hwllmxList");
				boolean result_ck = hwllmxService.updateMxList(hwllmxList);
				if(!result_ck) {
					throw new BusinessException("msg","批量更新出库明细关联id失败！");
				}
			}
			boolean result_ll = llglService.update(llglDto);
			if(!result_ll) {
				throw new BusinessException("msg","领料状态更新失败！");
			}
		}

	return true;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam)
			throws BusinessException {
		if (shgcList == null || shgcList.isEmpty()) {
			return true;
		}
		if (auditParam.isCancelOpe()) {
		// 审核回调方法
			for (ShgcDto shgcDto : shgcList) {
				String llid = shgcDto.getYwid();
				LlglDto llglDto = new LlglDto();
				llglDto.setXgry(operator.getYhid());
				llglDto.setLlid(llid);
				llglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());				
				llglService.deliverymodSavePicking(llglDto);
			}
		} else {
			// 审核回调方法
			for (ShgcDto shgcDto : shgcList) {
				String llid = shgcDto.getYwid();
				LlglDto llglDto = new LlglDto();
				llglDto.setXgry(operator.getYhid());
				llglDto.setLlid(llid);
				llglDto.setZt(StatusEnum.CHECK_NO.getCode());				
				llglService.deliverymodSavePicking(llglDto);
			}
		}
		return true;
	}
//================================================================================
	@Override
	public boolean existCheck(String fieldName, String value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean insertImportRec(BaseModel baseModel, User user,int rowindex, StringBuffer errorMessages) {
		try{
			//xlsx的数据:物料编码|物料名称|数量|单位|生产商|货号|规格/型号|备注
			//存入hwllxx表的信息：wlid，qlsl，lrry，lrsj，bz，zt，scbj，xmbm，xmdl
			//通过导入的wlbm查wlgl的wlid，通过wlid查qgmx的qgid，通过qgid查qggl的wlbm和xmdl
			HwllxxDto hwllxxDto = (HwllxxDto)baseModel;
			if(StringUtils.isNotBlank(hwllxxDto.getLlid())) {
				//生成一个领料单
				LlglDto llglDto = new LlglDto();
				llglDto.setLlid(hwllxxDto.getLlid());//领料ID
				String lldh = llglService.generateDjh(llglDto); 
				llglDto.setLldh(lldh);//领料单号
				Date date=new Date();
				SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
				String str_date=formatter.format(date);
				llglDto.setSqrq(str_date);//申请日期
				llglDto.setSqry(user.getYhid());//申请人
				user = commonService.getUserInfoById(user);
				llglDto.setSqbm(user.getJgid());//申请部门
				llglDto.setLrry(user.getYhid());//录入人员
				JcsjDto jcsjDto=new JcsjDto();
				jcsjDto.setCsdm("b");
				jcsjDto.setJclb(BasicDataTypeEnum.DELIVERY_TYPE.getCode());
				jcsjDto=jcsjService.getDto(jcsjDto);
				llglDto.setCklb(jcsjDto.getCsid());//出库类别
				llglDto.setQybj("0");
				llglDto.setZt(StatusEnum.CHECK_NO.getCode());
				llglDto.setCkzt(StatusEnum.CHECK_NO.getCode());
				llglDto.setJlbh(lldh);//记录编号设为lldh
				
				LlglDto t_llglDto=llglService.getDtoById(hwllxxDto.getLlid());
				if(t_llglDto==null) {
					llglService.insert(llglDto);
				}
			}
			hwllxxDto.setHwllid(StringUtil.generateUUID());
			hwllxxDto.setLrry(user.getYhid());
			WlglDto wlglDto = wlglService.getWlidByWlbm(hwllxxDto.getWlbm());
			hwllxxDto.setWlid(wlglDto.getWlid());
			hwllxxDto.setXmbm(wlglDto.getXmbm());
			hwllxxDto.setXmdl(wlglDto.getXmdl());
			hwllxxDto.setScbj("0");
			hwllxxDto.setZt("00");
			CkhwxxDto ckhwxxdto = new CkhwxxDto();//导入的货物领料数据的数量需要增加到出库货物表的预定数上
			ckhwxxdto.setWlid(wlglDto.getWlid());
			if (StringUtil.isNotBlank(hwllxxDto.getCkqxmc())){
				JcsjDto jcsjDto = new JcsjDto();
				jcsjDto.setCsmc(hwllxxDto.getCkqxmc());
				jcsjDto.setJclb("CK_PERMISSIONS_TYPE");
				JcsjDto jcsjDto1 = jcsjService.getDto(jcsjDto);
				if (jcsjDto1 != null){
					ckhwxxdto.setCkqxlx(jcsjDto1.getCsid());
				}else{
					throw new BusinessException("仓库分类错误或者该物料没有在该分类中！");
				}
			}
			//有问题暂且搁置
			CkhwxxDto ckhwxxDto = ckhwxxService.getDtoByWlidAndCkid(ckhwxxdto);
			if (ckhwxxDto != null){
				hwllxxDto.setCkhwid(ckhwxxDto.getCkhwid());
			}else{
				throw new BusinessException("仓库分类错误或者该物料没有在该分类中！");
			}
			dao.insertDto(hwllxxDto);		
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return true;
	}

	@Override
	public String cellTransform(String tranTrack, String value, ImportRecordModel recModel, StringBuffer errorMessage) {
		//通过导入的wlbm查wlgl的wlid，通过wlid查qgmx的qgid，通过qgid查qggl的wlbm和xmdl
		JcsjDto jcsjDto=new JcsjDto();
		jcsjDto.setCsdm("b");
		jcsjDto.setJclb(BasicDataTypeEnum.DELIVERY_TYPE.getCode());
		jcsjDto=jcsjService.getDto(jcsjDto);
		if(jcsjDto== null){
			errorMessage.append("出库类别无参数代码为b的基础数据").append("；<br>");
		}
		
		if(tranTrack.equalsIgnoreCase("LL001")){
			WlglDto t_wlglDto = wlglService.getWlidByWlbm(value);
			if(t_wlglDto== null){
				errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
				.append("列，物料编码不存在，单元格值为：").append(value).append("；<br>");
			}else if(StringUtil.isBlank(t_wlglDto.getWlid())){
				errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
				.append("列，物料编码找不到对应的wlid，单元格值为：").append(value).append("；<br>");
			}
			else{
				return value;
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
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Map<String, Object> requirePreAuditMember(ShgcDto shgcDto) {
		return null;
	}

	@Override
	public Map<String, Object> returnAuditServiceInfo(Map<String, Object> param) {
		Map<String, Object> map =new HashMap<>();
		@SuppressWarnings("unchecked")
		List<String> ids = (List<String>)param.get("ywids");
		LlglDto llglDto = new LlglDto();
		llglDto.setIds(ids);
		List<LlglDto> dtoList = llglService.getDtoList(llglDto);
		List<String> list=new ArrayList<>();
		if(!CollectionUtils.isEmpty(dtoList)){
			for(LlglDto dto:dtoList){
				list.add(dto.getLlid());
			}
		}
		map.put("list",list);
		return map;
	}

	/**
	 *	复制按钮查询明细
	 * @param hwllxxDto
	 * @return
	 */
	public List<HwllxxDto> getDtoCopyList(HwllxxDto hwllxxDto){
		return dao.getDtoCopyList(hwllxxDto);
	}
}
