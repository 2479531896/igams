package com.matridx.igams.storehouse.service.impl;

import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.enums.DingMessageType;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.production.dao.entities.QgglDto;
import com.matridx.igams.production.dao.matridxsql.CurrentStockDao;
import com.matridx.igams.production.dao.post.IQgglDao;
import com.matridx.igams.storehouse.dao.entities.CkhwxxDto;
import com.matridx.igams.storehouse.dao.entities.CkhwxxModel;
import com.matridx.igams.storehouse.dao.entities.HwxxDto;
import com.matridx.igams.storehouse.dao.post.ICkhwxxDao;
import com.matridx.igams.storehouse.dao.post.IHwxxDao;
import com.matridx.igams.storehouse.service.svcinterface.ICkhwxxService;
import com.matridx.igams.storehouse.service.svcinterface.IHwxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class CkhwxxServiceImpl extends BaseBasicServiceImpl<CkhwxxDto, CkhwxxModel, ICkhwxxDao>
		implements ICkhwxxService {

	@Autowired
	IQgglDao qgglDao;
	
	@Autowired
	CurrentStockDao currentStockDao;
	
	@Autowired
	IHwxxDao hwxxDao;
	@Autowired
	IDdxxglService ddxxglService;
	@Autowired
	DingTalkUtil talkUtil;
	@Autowired
	IXxglService xxglService;
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Override
	public CkhwxxDto getJsInfo(String userId) {
		return dao.getJsInfo(userId);
	}

	@Override
	public List<CkhwxxDto> getDtoByWlbmAndCkdm(CkhwxxDto ckhwxxDto) {
		return dao.getDtoByWlbmAndCkdm(ckhwxxDto);
	}

	/**
	 * 库存列表
	 * 
	 * @param
	 * @return
	 */
	public List<CkhwxxDto> getPagedDtoStockList(CkhwxxDto ckhwxxDto,String dqjs) {
		CkhwxxDto ckhwxxDto1 = dao.getJsInfo(dqjs);
		if (null != ckhwxxDto1){
			if (StringUtil.isNotBlank(ckhwxxDto1.getCkqx())){
				String[] temps = ckhwxxDto1.getCkqx().split(",");
				List<String> arr = new ArrayList<>();
				Collections.addAll(arr, temps);
				ckhwxxDto.setCkqxlxs(arr);
			}
		}else{
			ckhwxxDto.setCskz1("1");
		}
		return dao.getPagedDtoStockList(ckhwxxDto);
	}

	@Override
	public List<CkhwxxDto> getPagedDtoSecureStockList(CkhwxxDto ckhwxxDto, String dqjs) {
		CkhwxxDto ckhwxxDto1 = dao.getJsInfo(dqjs);
		if (null != ckhwxxDto1){
			if (StringUtil.isNotBlank(ckhwxxDto1.getCkqx())){
				String[] temps = ckhwxxDto1.getCkqx().split(",");
				List<String> arr = new ArrayList<>();
				Collections.addAll(arr, temps);
				ckhwxxDto.setCkqxlxs(arr);
			}
		}else{
			ckhwxxDto.setCskz1("1");
		}
		return dao.getPagedDtoSecureStockList(ckhwxxDto);
	}

	@Override
	public CkhwxxDto getDtoByWlidAndCkid(CkhwxxDto ckhwxxDto) {
		return dao.getDtoByWlidAndCkid(ckhwxxDto);
	}

	/**
	 * 根据ckhwid查看货物基本信息
	 * @param
	 * @return
	 */
	public CkhwxxDto getJbxxByCkhwid(CkhwxxDto ckhwxxDto) {
		return dao.getJbxxByCkhwid(ckhwxxDto);
	}

	@Override
	public List<CkhwxxDto> getJbxxListByCkhwid(CkhwxxDto ckhwxxDto) {
		return dao.getJbxxListByCkhwid(ckhwxxDto);
	}

	/**
	 * 根据ckhwid查看进货信息
	 * @param ckhwid
	 * @return
	 */
	public List<CkhwxxDto> getJhxxByCkhwid(String ckhwid) {
		return dao.getJhxxByCkhwid(ckhwid);
	}
	public List<CkhwxxDto> getJhxxList(CkhwxxDto ckhwxxDto){
		return dao.getJhxxList(ckhwxxDto);
	}

	@Override
	public List<CkhwxxDto> getJhxxListByWlid(CkhwxxDto ckhwxxDto) {
		return dao.getJhxxListByWlid(ckhwxxDto);
	}

	@Override
	public List<CkhwxxDto> getWlxxByWlid(CkhwxxDto ckhwxxDto) {
		return dao.getWlxxByWlid(ckhwxxDto);
	}

	/**
	 * 更新仓库货物信息
	 * 
	 * @param ckhwxxDtos
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateCkhwxxs(List<CkhwxxDto> ckhwxxDtos) {
		int result = dao.updateCkhwxxs(ckhwxxDtos);
		return result != 0;
	}

	/**
	 * 添加仓库货物信息
	 * 
	 * @param
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean insertCkhwxxs(List<CkhwxxDto> ckhwxxDtos) {
		int result = dao.insertCkhwxxs(ckhwxxDtos);
		return result != 0;
	}

	/**
	 * 根据输入信息查询物料
	 * @param
	 * @return
	 */
	@Override
	public List<CkhwxxDto> queryWlxx(CkhwxxDto ckhwxxDto,String dqjs) {
		if (StringUtil.isBlank(dqjs)){
			return null;
		}
		ckhwxxDto.setJsid(dqjs);
		// TODO Auto-generated method stub
		return dao.queryWlxx(ckhwxxDto);
	}

	/**
	 * 查询要添加的物料信息
	 * @param ckhwxxDto
	 * @return
	 */
	@Override
	public CkhwxxDto queryWlxxByWlid(CkhwxxDto ckhwxxDto) {
		// TODO Auto-generated method stub
		CkhwxxDto ckhwxxDto_t = dao.queryWlxxByWlid(ckhwxxDto);
		//判断库存量
		Double kqls = Double.parseDouble(ckhwxxDto_t.getKcl())-(StringUtil.isNotBlank(ckhwxxDto_t.getYds())?Double.parseDouble(ckhwxxDto_t.getYds()):0.00);
		if(kqls>0) {
			ckhwxxDto_t.setKcbj("1");
		}else {
			ckhwxxDto_t.setKcbj("0");
		}
		DecimalFormat df = new DecimalFormat("#.00");
		ckhwxxDto_t.setKqls(df.format(kqls));
		QgglDto qgglDto_q = new QgglDto();
		qgglDto_q.setWlid(ckhwxxDto_t.getWlid());
        
		List<QgglDto> qgglDtos = qgglDao.queryByWlid(qgglDto_q);
		if(!CollectionUtils.isEmpty(qgglDtos)) {
			for (QgglDto qgglDto : qgglDtos) {
				if(ckhwxxDto_t.getWlid().equals(qgglDto.getWlid())) {
					ckhwxxDto_t.setXmdl(qgglDto.getXmdl());
					ckhwxxDto_t.setXmbm(qgglDto.getXmbm());
					break;
				}
			}
		}
//		CkhwxxDto ckhwxxDto1 = dao.getJsInfo(yhid);
//		if (null != ckhwxxDto1.getKzcs2() && "" != ckhwxxDto1.getKzcs2()) {
//			String[] temps = ckhwxxDto1.getKzcs2().split(",");
//			List<String> arr = new ArrayList<>();
//			for (String ckid : temps) {
//				arr.add(ckid);
//			}
//			HwxxDto hwxxDt = new HwxxDto();
//			hwxxDt.setWlid(ckhwxxDto.getWlid());
//			hwxxDt.setIds(arr);
//			List<HwxxDto> hwxxDtos = hwxxService.getHwInfoByCkids(hwxxDt);
//			if (hwxxDtos.size() >0){
//				for (HwxxDto hwxxDto_r : hwxxDtos) {
//					if(ckhwxxDto_t.getWlid().equals(hwxxDto_r.getWlid())) {
//						ckhwxxDto_t.setKcl(hwxxDto_r.getKcl());
//						break;
//					}
//				}
//			}
//		}
//		Double kcl = Double.parseDouble(ckhwxxDto_t.getKcl());
////		if(kqls>0) {
////			ckhwxxDto_t.setKcbj("1");
////		}else {
////			ckhwxxDto_t.setKcbj("0");
////		}
//		DecimalFormat df = new DecimalFormat("#.00");
//		ckhwxxDto_t.setKqls(df.format(kcl).toString());
//		ckhwxxDto_t.setKlsl(df.format(kcl).toString());
//		if(kcl>0) {
//			ckhwxxDto_t.setKcbj("1");
//		}else {
//			ckhwxxDto_t.setKcbj("0");
//		}
		return ckhwxxDto_t;
	}

	/**
	 * 根据物料id更新仓库货物信息
	 * 
	 * @param ckhwxxDtos
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateByCkhwid(List<CkhwxxDto> ckhwxxDtos) {
		int result = dao.updateByCkhwid(ckhwxxDtos);
		return result>0;
	}
	
	
	@SuppressWarnings("unchecked")
	private void queryJoinFlagExport(Map<String, Object> params, CkhwxxDto ckhwxxDto){
		List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
		StringBuilder sqlParam = new StringBuilder();
		for (DcszDto dcszDto : choseList){
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
		ckhwxxDto.setSqlParam(sqlParam.toString());
	}
	/**
	 * 列表选中导出
	 * @param params
	 * @return
	 */
    public List<CkhwxxDto> getListForSelectExp(Map<String, Object> params){
    	CkhwxxDto ckhwxxDto = (CkhwxxDto) params.get("entryData");
        queryJoinFlagExport(params,ckhwxxDto);
        return dao.getListForSelectExp(ckhwxxDto);
	}
	
	/**
	 * 根据搜索条件获取导出条数
	 * @param ckhwxxDto
	 * @return
	 */
	public int getCountForSearchExp(CkhwxxDto ckhwxxDto,Map<String,Object> params){
		return dao.getCountForSearchExp(ckhwxxDto);
	}
	
	/**
	 * 根据搜索条件分页获取导出信息
	 * @return
	 */
	public List<CkhwxxDto> getListForSearchExp(Map<String,Object> params){
		CkhwxxDto ckhwxxDto = (CkhwxxDto)params.get("entryData");
		queryJoinFlagExport(params,ckhwxxDto);
		return dao.getListForSearchExp(ckhwxxDto);
	}


	/**
	 * 列表选中导出
	 * @param params
	 * @return
	 */
	public List<CkhwxxDto> getListForSeruceSelectExp(Map<String, Object> params){
		CkhwxxDto ckhwxxDto = (CkhwxxDto) params.get("entryData");
		queryJoinFlagExport(params,ckhwxxDto);
		return dao.getListForSeruceSelectExp(ckhwxxDto);
	}

	/**
	 * 根据搜索条件获取导出条数
	 * @param ckhwxxDto
	 * @return
	 */
	public int getCountForSeruceSearchExp(CkhwxxDto ckhwxxDto,Map<String,Object> params){
		return dao.getCountForSeruceSearchExp(ckhwxxDto);
	}

	/**
	 * 根据搜索条件分页获取导出信息
	 * @return
	 */
	public List<CkhwxxDto> getListForSeruceSearchExp(Map<String,Object> params){
		CkhwxxDto ckhwxxDto = (CkhwxxDto)params.get("entryData");
		queryJoinFlagExport(params,ckhwxxDto);
		return dao.getListForSeruceSearchExp(ckhwxxDto);
	}
	/**
	 * 批量更新仓库货物数量
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateHwSl(List<CkhwxxDto> ckhwxxDtos) {
		int result = dao.updateHwSl(ckhwxxDtos);
		return result > 0;
	}

	@Override
	public void updateCkhwxx(CkhwxxDto ckhwxxDto) {
		dao.updateCkhwxx(ckhwxxDto);
	}

	@Override
	public CkhwxxDto getCkhwInfo(String wlid) {
		return dao.getCkhwInfo(wlid);
	}

	
	//定时同步U8库存
	/*@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean SynchronizationU8() {
		//获取U8所有库存信息
		List<CurrentStockDto> currentStockList = currentStockDao.getDtoAll();

		//获取所有货物信息(因为存在库存关联id错误的数据，所以用追溯号，生产批号，仓库编码分组查询)
		List<HwxxDto> hwxxList = hwxxDao.getDtoAllGroupBy();
		//获取所有货物信息(不分组)
		List<HwxxDto> hwxxList_all = hwxxDao.getDtoAll();
		//存更新货物信息库存量
		List<HwxxDto> hwxxList_mod = new ArrayList<>();
		//存新增的货物信息
		List<HwxxDto> hwxxList_add = new ArrayList<>();
		for (CurrentStockDto cuS : currentStockList) {
			boolean kcbj = true;
			for (HwxxDto hwxxDto : hwxxList) {
				//根据生产批号，物料编码，仓库对比库存
				if(cuS.getcInvCode().equals(hwxxDto.getWlbm()) && cuS.getcBatch().equals(hwxxDto.getScph()) && cuS.getcWhCode().equals(hwxxDto.getCkdm())) {
					kcbj =false;
					//U8库存-OA库存，如果结果小于0，OA做修改，减去超出的量，如果大于0，OA做新增，新增超出的量
					Double poor = Double.parseDouble(cuS.getiQuantity())-Double.parseDouble(hwxxDto.getKcl());
					if(poor<0.00) { //OA做修改
						//遍历获取关联该U8库存的所有OA货物信息
						for (HwxxDto hwxxDto_all : hwxxList_all) {
							//判断货物库存关联id是否一致
							if(hwxxDto.getWlbm().equals(hwxxDto_all.getWlbm()) && hwxxDto.getCkdm().equals(hwxxDto_all.getCkdm()) && hwxxDto.getScph().equals(hwxxDto_all.getScph())) {
								if(poor<0.00) {
									//因为存在有的货物已领料，所以库存量-预定数-差
									poor = Double.parseDouble(hwxxDto_all.getKcl())-Double.parseDouble(StringUtil.isNotBlank(hwxxDto_all.getYds())?hwxxDto_all.getYds():"0")+poor;
									//判断poor是否小于0
									if(poor<0.00) {
										HwxxDto hwxxDto_mod = new HwxxDto();
										hwxxDto_mod.setHwid(hwxxDto_all.getHwid());
										hwxxDto_mod.setKcl(StringUtil.isNotBlank(hwxxDto_all.getYds())?hwxxDto_all.getYds():"0"); //小于0，库存量=预定数
										hwxxList_mod.add(hwxxDto_mod);
									}else {
										HwxxDto hwxxDto_mod = new HwxxDto();
										hwxxDto_mod.setHwid(hwxxDto_all.getHwid());
										//大于等于0，库存量=计算结果+预定数
										hwxxDto_mod.setKcl(String.valueOf(poor+Double.parseDouble(StringUtil.isNotBlank(hwxxDto_all.getYds())?hwxxDto_all.getYds():"0")));
										hwxxList_mod.add(hwxxDto_mod);
									}
								}
							}
						}
						//U8库存小于OA库存，修改OA库存量
						HwxxDto hwxx_mod = new HwxxDto();
						hwxx_mod.setHwid(hwxxDto.getHwid());
						hwxx_mod.setKcl(cuS.getiQuantity());
						hwxxList_mod.add(hwxx_mod);
					}
					if(poor>0.00) {
						//U8库存大于OA库存量，OA新增货物信息
						HwxxDto hwxx_add = new HwxxDto();
						hwxx_add.setHwid(StringUtil.generateUUID());
						hwxx_add.setWlid(hwxxDto.getWlid());
						hwxx_add.setScrq(hwxxDto.getScrq());
						hwxx_add.setYxq(hwxxDto.getYxq());
						hwxx_add.setCkid(hwxxDto.getCkid());
						hwxx_add.setKcl(String.valueOf(poor));
						hwxx_add.setKcglid(String.valueOf(cuS.getAutoID()));
						hwxx_add.setScph(cuS.getcBatch());
						hwxx_add.setZt("99");
						hwxxList_add.add(hwxx_add);
					}
				}
			}
			//判断OA是否存在此库存 true不存在，false存在
			if(kcbj) {
				//不存在，OA新增一条货物信息
			}
		}
		return false;
	}*/

	/**
	 * 根据wlid和ckqx查找数据
	 *
	 * @param ckhwxxDto
	 * @return
	 */
	@Override
	public List<CkhwxxDto> getDtoListByWlids(CkhwxxDto ckhwxxDto) {
		// TODO Auto-generated method stub
		return dao.getDtoListByWlids(ckhwxxDto);
	}

	/**
	 * 更改仓库货物信息
	 *
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateList(List<CkhwxxDto> ckhwxxDtos) {
		int result=dao.updateList(ckhwxxDtos);
		return result>0;
	}

	/**
	 * 更改仓库货物信息
	 *
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateCkhwxxYds(List<CkhwxxDto> ckhwxxDtos) {
		// TODO Auto-generated method stub
		return dao.updateCkhwxxYds(ckhwxxDtos)>0;
	}

	/**
	 * 批量更新预定数
	 * @param list
	 * @return
	 */
	public int updateYdsList(List<CkhwxxDto> list){
		return dao.updateYdsList(list);
	}

	@Override
	public List<CkhwxxDto> getHwxxtbyCkhwid(String ckhwid) {
		return dao.getHwxxtbyCkhwid(ckhwid);
	}

	@Override
	public List<CkhwxxDto> getHwxxtbyWlid(String wlid) {
		return dao.getHwxxtbyWlid(wlid);
	}

	@Override
	public List<CkhwxxDto> getPagedDtoStockListDingTalk(CkhwxxDto ckhwxxDto,String dqjs) {
		CkhwxxDto ckhwxxDto1 = dao.getJsInfo(dqjs);
		if (null != ckhwxxDto1){
			if (StringUtil.isNotBlank(ckhwxxDto1.getCkqx())){
				String[] temps = ckhwxxDto1.getCkqx().split(",");
				List<String> arr = new ArrayList<>();
				Collections.addAll(arr, temps);
				ckhwxxDto.setCkqxlxs(arr);
			}
		}else{
			ckhwxxDto.setCskz1("1");
		}
		return dao.getPagedDtoStockListDingTalk(ckhwxxDto);
	}

	@Override
	public boolean updateByWlidAndCkid(List<CkhwxxDto> ckhwxxDtos) {
		return dao.updateByWlidAndCkid(ckhwxxDtos);
	}
	/**
	 * 定时发送库存不足的货物钉钉消息
	 */
	public void SelectInsufficientInventory(){
		List<DdxxglDto> ddxxgldtolist = ddxxglService.selectByDdxxlx(DingMessageType.INSUFFICIENT_INVENTORY.getCode());
		List<CkhwxxDto> ckhwxxDtos=dao.SelectInsufficientInventory();
//		String token = talkUtil.getToken();
		String ICOMM_KC00002 = xxglService.getMsg("ICOMM_KC00002");
		String ICOMM_KC00003 = xxglService.getMsg("ICOMM_KC00003");
		for (CkhwxxDto ckhwxxDto:ckhwxxDtos){
		for(DdxxglDto ddxxglDto :ddxxgldtolist) {//这里是遍历用户
			if (StringUtil.isNotBlank(ddxxglDto.getDdid())) {
				// 内网访问
				String internalbtn = applicationurl + urlPrefix
						+ "/ws/production/materiel/viewMateriel?wlid=" + ckhwxxDto.getWlid();
				List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
				OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
				btnJsonList.setTitle("详细");
				btnJsonList.setActionUrl(internalbtn);
				btnJsonLists.add(btnJsonList);
				talkUtil.sendCardMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(), ICOMM_KC00002, StringUtil.replaceMsg(ICOMM_KC00003,
						ckhwxxDto.getWlmc(), ckhwxxDto.getWlbm(),ckhwxxDto.getJldw(),
						ckhwxxDto.getGg(), ckhwxxDto.getKcl(),ckhwxxDto.getAqkc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),
						btnJsonLists, "1");
			}
		}
		}
	}

	@Override
	public CkhwxxDto getLastCkInfo(CkhwxxDto ckhwxxDto) {
		return dao.getLastCkInfo(ckhwxxDto);
	}

	@Override
	public CkhwxxDto queryCkhwxxGroupByWlid(CkhwxxDto ckhwxxDto) {
		return dao.queryCkhwxxGroupByWlid(ckhwxxDto);
	}

}
